/*
 * Copyright:
 * License :
 *  The following code is deliver as is.
 *  I take care that code compile and work, but I am not responsible about any  damage it may  cause.
 *  You can use, modify, the code as your need for any usage.
 *  But you can't do any action that avoid me or other person use,  modify this code.
 *  The code is free for usage and modification, you can't change that fact.
 *  @author JHelp
 *
 */

package jhelp.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import jhelp.security.encrypt.desEncrypt.DESencrypt;
import jhelp.security.io.JHelpBase;
import jhelp.util.debug.Debug;
import jhelp.util.io.StringInputStream;
import jhelp.util.io.StringOutputStream;
import jhelp.util.text.UtilText;
import jhelp.util.util.Utilities;

/**
 * Data base manipulation<br>
 * An automatic column ID is created for all table
 */
public class Database
{
    /**
     * Column name for store password
     */
    private static final String            PASSWORD           = "password";
    /**
     * Column description of password column
     */
    private static final ColumnDescription COLUMN_PASSWORD    = new ColumnDescription(Database.PASSWORD,
                                                                                      DataType.LONGVARCHAR);
    /**
     * Password table name
     */
    private static final String            PASSWORD_TABLE     = UtilText.concatenate(Database.class.getName(),
                                                                                     "_PASSWORD_TABLE")
                                                                        .replace('.', '_');
    /**
     * Column of column name
     */
    public static final  String            COLUMN_NAME        = "Column_Name";
    /**
     * Description of column of column name
     */
    public static final  ColumnDescription COLUMN_COLUMN_NAME = new ColumnDescription(Database.COLUMN_NAME,
                                                                                      DataType.VARCHAR);
    /**
     * Column of column type
     */
    public static final  String            COLUMN_TYPE        = "Column_Type";
    /**
     * Description of column of column type
     */
    public static final  ColumnDescription COLUMN_COLUMN_TYPE = new ColumnDescription(Database.COLUMN_TYPE,
                                                                                      DataType.VARCHAR);
    /**
     * ID column name
     */
    public static final  String            ID                 = "id";
    /**
     * Column ID
     */
    public static final  ColumnDescription COLUMN_ID          = new ColumnDescription(Database.ID, DataType.INT);
    /**
     * Metadata table for store colum descriptions
     */
    public static final  String            METADATA_COLUMN    = UtilText.concatenate(Database.class.getName(),
                                                                                     "_METADATA_COLUMN")
                                                                        .replace('.', '_');
    /**
     * Metadata table for store table name
     */
    public static final  String            METADATA_TABLE     = UtilText.concatenate(Database.class.getName(),
                                                                                     "_METADATA_TABLE")
                                                                        .replace('.', '_');
    /**
     * Column foreign key of metadata table to know at witch table the column is associate
     */
    public static final  String            TABLE_ID           = "Table_ID";
    /**
     * Description of metadata table foreign key
     */
    public static final  ColumnDescription COLUMN_TABLE_ID    = new ColumnDescription(Database.TABLE_ID, DataType.INT);
    /**
     * Column of table name
     */
    public static final  String            TABLE_NAME         = "Table_Name";
    /**
     * Column description of column table name
     */
    public static final  ColumnDescription COLUMN_TABLE_NAME  = new ColumnDescription(Database.TABLE_NAME,
                                                                                      DataType.VARCHAR);
    /**
     * Connection to the data base
     */
    private       Connection connection;
    /**
     * Indicates that matadata are ready to use
     */
    private       boolean    metedataReady;
    /**
     * The password
     */
    private final String     password;

    /**
     * Constructs Database
     *
     * @param path Relative path of data base
     * @throws DatabaseException On creation problem
     */
    public Database(final String path)
            throws DatabaseException
    {
        this(path, "");
    }

    /**
     * Constructs Database protect by a password<br>
     * The password is store at first creation of database. Then in next use the password is just control to see if it is valid<br>
     * Note : Actually only data in {@link DataType#LONGVARCHAR} are encrypted, so if you want protect data you have to store
     * them in {@link DataType#LONGVARCHAR}
     *
     * @param path     Database path
     * @param password Password to access encrypted data.
     * @throws DatabaseException On creation problem
     */
    public Database(final String path, final String password)
            throws DatabaseException
    {
        if (password == null)
        {
            throw new NullPointerException("password must not be null");
        }

        this.metedataReady = false;
        this.password = password;

        // Create database
        try
        {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();

            this.connection = DriverManager.getConnection(UtilText.concatenate("jdbc:hsqldb:file:", path), "sa", "");
            this.connection.setAutoCommit(true);
        }
        catch (final Exception exception)
        {
            throw new DatabaseException("Initialization database failed", exception);
        }

        // Create password table
        try
        {
            this.createTable(Database.PASSWORD_TABLE, Database.COLUMN_PASSWORD);

            // If password table doesn't already exists, store the password
            this.insert(Database.PASSWORD_TABLE, new Value(Database.PASSWORD, Data.createLongStringData(password)));
        }
        catch (final Exception exception)
        {
            try
            {
                // If password table already exists, get stored password
                final SelectQuery selectQuery = new SelectQuery(Database.PASSWORD_TABLE, Database.COLUMN_PASSWORD);
                final QueryResult queryResult = this.query(selectQuery);

                // Check if given password match
                if (!password.equals(queryResult.getData(0, 0)
                                                .getLongString()))
                {
                    // Random sleep against the "time attack"
                    Utilities.sleep((int) ((Math.random() * 123) + 12));
                    throw new DatabaseException("Wrong password !");
                }

                queryResult.destroy();
            }
            catch (final Exception e)
            {
                // Random sleep against the "time attack"
                Utilities.sleep((int) ((Math.random() * 123) + 12));
                throw new DatabaseException("Wrong password !", e);
            }
        }

        // Create metadata tables
        try
        {
            this.createTable(Database.METADATA_TABLE, Database.COLUMN_TABLE_NAME);
            this.createTable(Database.METADATA_COLUMN, Database.COLUMN_TABLE_ID, Database.COLUMN_COLUMN_NAME,
                             Database.COLUMN_COLUMN_TYPE);
        }
        catch (final Exception ignored)
        {
        }

        this.metedataReady = true;
    }

    /**
     * Read a string.<br>
     * If the password is empty, then given string not modified, else given string decrypted
     *
     * @param source String to read
     * @return Result string
     */
    String readString(final String source)
    {
        // If password empty, do nothing to string
        if (this.password.length() == 0)
        {
            return source;
        }

        // Decrypt string
        try
        {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(JHelpBase.decode(source));
            final StringOutputStream   stringOutputStream   = new StringOutputStream();

            DESencrypt.DES.decrypt(this.password, byteArrayInputStream, stringOutputStream);

            return stringOutputStream.getString();
        }
        catch (final Exception exception)
        {
            return source;
        }
    }

    /**
     * Make a simple query that no need result
     *
     * @param query Query to execute
     * @throws DatabaseException On execution problem
     */
    void simpleQuery(final String query) throws DatabaseException
    {
        try
        {
            Statement statement = this.connection.createStatement();

            statement.executeQuery(query);

            statement.close();

            statement = null;
        }
        catch (final SQLException exception)
        {
            throw new DatabaseException(UtilText.concatenate("query : ", query, " failed"), exception);
        }
    }

    /**
     * Compute string to write for data.<br>
     * If data have type {@link DataType#LONGVARCHAR} and password if not empty, then return string representation encrypted,
     * else it return the string representation clear
     *
     * @param data Data to write
     * @return String to write
     */
    String stringForWrite(final Data data)
    {
        switch (data.getDataType())
        {
            case LONGVARCHAR:
                return data.toString(this);
        }

        return data.toString();
    }

    /**
     * Compute string to write.<br>
     * If password is not empty, it return and encrypted string
     *
     * @param source String source
     * @return String result
     */
    String stringForWrite(final String source)
    {
        // If password empty, do nothing to string
        if (this.password.length() == 0)
        {
            return source;
        }

        // Encrypt the string
        try
        {
            final StringInputStream     stringInputStream     = new StringInputStream(source);
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            DESencrypt.DES.encrypt(this.password, stringInputStream, byteArrayOutputStream);

            return JHelpBase.encode(byteArrayOutputStream.toByteArray());
        }
        catch (final Exception exception)
        {
            Debug.exception(exception);
            return source;
        }
    }

    /**
     * Compute string to write for value.<br>
     * If data have type {@link DataType#LONGVARCHAR} and password if not empty, then return string representation encrypted,
     * else it return the string representation clear
     *
     * @param value Value to write
     * @return String to write
     */
    String stringForWrite(final Value value)
    {
        return this.stringForWrite(value.getData());
    }

    /**
     * Make an update query (like UDATE, INSERT or DELETE)
     *
     * @param update Update query
     * @throws DatabaseException On update problem
     */
    void update(final String update) throws DatabaseException
    {
        try
        {
            Statement statement = this.connection.createStatement();

            statement.executeUpdate(update);

            statement.close();

            statement = null;
        }
        catch (final SQLException exception)
        {
            throw new DatabaseException(UtilText.concatenate("update : ", update, " failed"), exception);
        }
    }

    /**
     * Close the data base.<br>
     * Call this method to free memory and to be sure all operations are commit before exit application
     *
     * @throws DatabaseException On closing problem
     */
    public void closeDatabase() throws DatabaseException
    {
        this.simpleQuery("SHUTDOWN");
        try
        {
            this.connection.close();
            this.connection = null;
        }
        catch (final SQLException exception)
        {
            throw new DatabaseException("Close failed", exception);
        }
    }

    /**
     * Create a table<br>
     * An 'id' column primary key is automatically created, so no need to specify one<br>
     * It check if table already exits, if table already exits, nothing append
     *
     * @param tableName          Table name
     * @param columnDescriptions Columns on table (Except 'id')
     * @throws DatabaseException On creation problem
     */
    public void createTable(final String tableName, final ColumnDescription... columnDescriptions)
            throws DatabaseException
    {
        if (this.metedataReady)
        {
            final Data tableNameData = new Data(tableName.toUpperCase());

            // Test if table already define
            SelectQuery selectQuery = new SelectQuery(Database.METADATA_TABLE, Database.COLUMN_TABLE_NAME);
            selectQuery.setWhere(new ConditionColumnEquals(Database.TABLE_NAME, tableNameData));

            QueryResult queryResult = this.query(selectQuery);
            if (queryResult.numberOfRows() > 0)
            {
                // Already exits, so not create
                queryResult.destroy();

                return;
            }

            queryResult.destroy();

            //
            // Register table

            this.insert(Database.METADATA_TABLE, new Value(Database.TABLE_NAME, tableNameData));

            // Get the ID of the just added table
            selectQuery = new SelectQuery(Database.METADATA_TABLE, Database.COLUMN_ID);
            selectQuery.setWhere(new ConditionColumnEquals(Database.TABLE_NAME, tableNameData));

            queryResult = this.query(selectQuery);

            final int id = queryResult.getData(0, 0).getInt();

            queryResult.destroy();

            // Register columns
            final Value tableId        = new Value(Database.TABLE_ID, new Data(id));
            final Data  columnNameData = new Data("ID");
            final Value columnName     = new Value(Database.COLUMN_NAME, columnNameData);
            final Data  columnTypeData = new Data(DataType.INT.name());
            final Value columnType     = new Value(Database.COLUMN_TYPE, columnTypeData);

            this.insert(Database.METADATA_COLUMN, tableId, columnName, columnType);

            for (final ColumnDescription columnDescription : columnDescriptions)
            {
                columnNameData.set(columnDescription.getColumnName().toUpperCase());
                columnTypeData.set(columnDescription.getDataType().name());

                this.insert(Database.METADATA_COLUMN, tableId, columnName, columnType);
            }
        }

        // Create the table
        StringBuffer update = new StringBuffer("CREATE TABLE ");
        update.append(tableName);
        update.append(" ( id INT GENERATED BY DEFAULT AS IDENTITY(START WITH 1) PRIMARY KEY");
        if (columnDescriptions != null)
        {
            for (final ColumnDescription columnDescription : columnDescriptions)
            {
                update.append(" , ");
                update.append(columnDescription.getColumnName());
                update.append(" ");
                update.append(columnDescription.getDataType().name());
                switch (columnDescription.getDataType())
                {
                    case VARCHAR:
                        update.append("(128)");
                        break;
                }
            }
        }
        update.append(" )");

        this.update(update.toString());

        update = null;
    }

    /**
     * Create a table
     *
     * @param tableInfo Table information
     * @throws DatabaseException On database issue
     */
    public void createTable(final TableInfo tableInfo) throws DatabaseException
    {
        final int           size    = tableInfo.numberOfColumns();
        ColumnDescription[] columns = new ColumnDescription[size];
        int                 index   = -1;

        for (int i = 0; i < size; i++)
        {
            columns[i] = tableInfo.getColumn(i);

            if (columns[i].equals(Database.COLUMN_ID))
            {
                index = i;
            }
        }

        if (index >= 0)
        {
            final ColumnDescription[] cols = new ColumnDescription[size - 1];

            System.arraycopy(columns, 0, cols, 0, index);
            System.arraycopy(columns, index + 1, cols, index, size - 1 - index);

            columns = cols;
        }

        this.createTable(tableInfo.getName(), columns);
    }

    /**
     * Delete rows from table
     *
     * @param table Table where delete
     * @param where Condition that match deleting rows (Can be {@code null} for clear all rows)
     * @throws DatabaseException On deletion problem
     */
    public void delete(final String table, final Condition where) throws DatabaseException
    {
        if (table == null)
        {
            throw new NullPointerException("table mustn't be null");
        }

        StringBuffer delete = new StringBuffer("DELETE FROM ");
        delete.append(table);

        if (where != null)
        {
            delete.append(" WHERE ");
            delete.append(where.toString(this));
        }

        this.update(delete.toString());

        delete = null;
    }

    /**
     * Get table info for a table
     *
     * @param tableName Table name
     * @return Table info or {@code null} if table dosen't exists
     * @throws DatabaseException On database access problem
     */
    public TableInfo getTableInfo(final String tableName) throws DatabaseException
    {
        SelectQuery selectQuery = new SelectQuery(Database.METADATA_TABLE, Database.COLUMN_ID);
        selectQuery.setWhere(new ConditionColumnEquals(Database.TABLE_NAME, new Data(tableName.toUpperCase())));

        QueryResult queryResult = this.query(selectQuery);

        if (queryResult.numberOfRows() == 0)
        {
            queryResult.destroy();

            return null;
        }

        final TableInfo tableInfo = new TableInfo(tableName.toUpperCase());

        final Data id = queryResult.getData(0, 0);
        queryResult.destroy();

        selectQuery = new SelectQuery(Database.METADATA_COLUMN, Database.COLUMN_COLUMN_NAME,
                                      Database.COLUMN_COLUMN_TYPE);
        selectQuery.setWhere(new ConditionColumnEquals(Database.TABLE_ID, id));
        queryResult = this.query(selectQuery, Database.COLUMN_NAME, true);

        final int rowCount = queryResult.numberOfRows();
        for (int row = 0; row < rowCount; row++)
        {
            tableInfo.addColumn(queryResult.getData(0, row).getString(),//
                                queryResult.getData(1, row).getString());
        }

        queryResult.destroy();

        return tableInfo;
    }

    /**
     * Insert value to a table
     *
     * @param table  Table where add value
     * @param values Values for columns
     * @return ID where value insert
     * @throws DatabaseException On insertion problem
     */
    public int insert(final String table, final Value... values) throws DatabaseException
    {
        final StringBuilder insert = new StringBuilder("INSERT INTO ");
        insert.append(table);
        insert.append(" ( ");
        insert.append(values[0].getName());
        for (int i = 1; i < values.length; i++)
        {
            insert.append(" , ");
            insert.append(values[i].getName());
        }
        insert.append(" ) VALUES ( ");
        insert.append(this.stringForWrite(values[0]));
        for (int i = 1; i < values.length; i++)
        {
            insert.append(" , ");
            insert.append(this.stringForWrite(values[i]));
        }
        insert.append(" )");

        this.update(insert.toString());

        // ---

        final SelectQuery selectQuery = new SelectQuery(table, Database.COLUMN_ID);
        final QueryResult queryResult = this.query(selectQuery, Database.ID, false);
        final int         id          = queryResult.getData(0, 0).getInt();
        queryResult.destroy();

        return id;
    }

    /**
     * Insert or update a database row in a table.<br>
     * The row is update if and only if, where condition isn't {@code null} and where condition design one and only one row in
     * the table. In other case the row is insert
     *
     * @param table  Table where insertion/update append
     * @param where  Condition to know if it is insertion or update (Can be {@code null} to force an insert
     * @param values Values to insert or update
     * @return -1 if value is update, else its the ID where value insert
     * @throws DatabaseException On database access problem
     */
    public int insertOrUpdate(final String table, final Condition where, final Value... values) throws DatabaseException
    {
        // Where is null, so it is an insert
        if (where == null)
        {
            return this.insert(table, values);
        }

        // Do the select for check
        // For update, it collects in same time the row id
        final SelectQuery selectQuery = new SelectQuery(table, Database.COLUMN_ID);
        selectQuery.setWhere(where);

        final QueryResult queryResult = this.query(selectQuery);

        // If there 1 and only 1 row
        if (queryResult.numberOfRows() == 1)
        {
            // Do the update
            final UpdateQuery updateQuery = new UpdateQuery(table, values);
            updateQuery.setWhere(new ConditionColumnEquals(Database.ID, queryResult.getData(0, 0)));

            this.update(updateQuery);

            queryResult.destroy();

            return -1;
        }

        // In other case, do the insert
        queryResult.destroy();
        return this.insert(table, values);
    }

    /**
     * Collect the list of table name
     *
     * @return List of table name
     * @throws DatabaseException On database access problem
     */
    public String[] listOfTableName() throws DatabaseException
    {
        final SelectQuery selectQuery = new SelectQuery(Database.METADATA_TABLE, Database.COLUMN_TABLE_NAME);

        final QueryResult queryResult = this.query(selectQuery, Database.TABLE_NAME, true);

        final int      length = queryResult.numberOfRows();
        final String[] tables = new String[length];

        for (int i = 0; i < length; i++)
        {
            tables[i] = queryResult.getData(0, i).getString();
        }

        queryResult.destroy();

        return tables;
    }

    /**
     * Make a select query
     *
     * @param query Query to execute
     * @return Result of query
     * @throws DatabaseException On execution problem
     */
    public QueryResult query(final SelectQuery query) throws DatabaseException
    {
        return this.query(query, null, false);
    }

    /**
     * Make a select query
     *
     * @param query       Query to execute
     * @param columnSort  Column for sort (Can be {@code null} if no sort)
     * @param isAscendent Indicates if the sort is ascendent ({@code true}) or descendant ({@code false})
     * @return Result of query
     * @throws DatabaseException On execution problem
     */
    public QueryResult query(final SelectQuery query, final String columnSort, final boolean isAscendent)
            throws DatabaseException
    {
        try
        {
            // create the query
            Statement statement = this.connection.createStatement();

            final StringBuilder queryString = new StringBuilder(query.toString(this));

            // Add sort, if one specify
            if (columnSort != null)
            {
                queryString.append(" ORDER BY ");
                queryString.append(columnSort);

                if (isAscendent)
                {
                    queryString.append(" ASC");
                }
                else
                {
                    queryString.append(" DESC");
                }
            }

            // Do the query
            ResultSet resultSet = statement.executeQuery(queryString.toString());

            // Collect the result
            final QueryResult queryResult = new QueryResult(query.getColumns());
            final int         colums      = queryResult.numberOfColumns();
            Data[]            datas;

            while (resultSet.next())
            {
                datas = new Data[colums];
                for (int i = 0; i < colums; i++)
                {
                    switch (queryResult.getColumnDescription(i).getDataType())
                    {
                        case BIGINT:
                            datas[i] = new Data(resultSet.getLong(i + 1));
                            break;
                        case BINARY:
                            datas[i] = new Data(resultSet.getBytes(i + 1));
                            break;
                        case BOOLEAN:
                            datas[i] = new Data(resultSet.getBoolean(i + 1));
                            break;
                        case DATE:
                            datas[i] = new Data(resultSet.getDate(i + 1));
                            break;
                        case DECIMAL:
                            datas[i] = new Data(resultSet.getBigDecimal(i + 1));
                            break;
                        case DOUBLE:
                            datas[i] = new Data(resultSet.getDouble(i + 1));
                            break;
                        case INT:
                            datas[i] = new Data(resultSet.getInt(i + 1));
                            break;
                        case OBJECT:
                            datas[i] = new Data(resultSet.getObject(i + 1));
                            break;
                        case SMALLINT:
                            datas[i] = new Data(resultSet.getShort(i + 1));
                            break;
                        case TIME:
                            datas[i] = new Data(resultSet.getTime(i + 1));
                            break;
                        case TIMESTAMP:
                            datas[i] = new Data(resultSet.getTimestamp(i + 1));
                            break;
                        case TINYINT:
                            datas[i] = new Data(resultSet.getByte(i + 1));
                            break;
                        case VARCHAR:
                            datas[i] = new Data(resultSet.getString(i + 1));
                            break;
                        case LONGVARCHAR:
                            datas[i] = Data.createLongStringData(this.readString(resultSet.getString(i + 1)));
                            break;
                    }
                }
                queryResult.addRow(datas);
            }

            resultSet.close();
            statement.close();

            statement = null;
            resultSet = null;

            return queryResult;
        }
        catch (final SQLException exception)
        {
            throw new DatabaseException(UtilText.concatenate("query : ", query, " failed"), exception);
        }
    }

    /**
     * Make update query
     *
     * @param query Update query
     * @throws DatabaseException On update problem
     */
    public void update(final UpdateQuery query) throws DatabaseException
    {
        if (query == null)
        {
            throw new NullPointerException("query mustn't be null");
        }

        this.update(query.toString(this));
    }
}
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

package jhelp.database.cache;

import jhelp.database.ConditionColumnEquals;
import jhelp.database.Data;
import jhelp.database.DataType;
import jhelp.database.Database;
import jhelp.database.DatabaseException;
import jhelp.database.QueryResult;
import jhelp.database.SelectQuery;
import jhelp.database.TableInfo;
import jhelp.database.UpdateQuery;
import jhelp.database.Value;
import jhelp.security.io.JHelpBase;

/**
 * Cache of data stored in database
 *
 * @author JHelp
 */
public class DatabaseCache
{
    static
    {
        TABLE_CACHE = "CACHE";
        TABLE_CACHE_COLUM_NAME = "Name";
        TABLE_CACHE_COLUM_TYPE = "Type";
        TABLE_CACHE_COLUM_VALUE = "Value";

        final TableInfo tableInfo = new TableInfo(DatabaseCache.TABLE_CACHE);
        tableInfo.addColumn(DatabaseCache.TABLE_CACHE_COLUM_NAME, DataType.LONGVARCHAR);
        tableInfo.addColumn(DatabaseCache.TABLE_CACHE_COLUM_TYPE, DataType.LONGVARCHAR);
        tableInfo.addColumn(DatabaseCache.TABLE_CACHE_COLUM_VALUE, DataType.LONGVARCHAR);
        TABLE_INFO = tableInfo;

        DATA_KEY_NAME = Data.createLongStringData("");

        final SelectQuery selectQuery = new SelectQuery(DatabaseCache.TABLE_INFO);
        selectQuery.setWhere(
                new ConditionColumnEquals(DatabaseCache.TABLE_CACHE_COLUM_NAME, DatabaseCache.DATA_KEY_NAME));
        SELECT_QUERY_VALUE = selectQuery;

        DATA_VALUE = Data.createLongStringData("");

        final UpdateQuery updateQuery = new UpdateQuery(DatabaseCache.TABLE_CACHE,
                                                        new Value(DatabaseCache.TABLE_CACHE_COLUM_VALUE,
                                                                  DatabaseCache.DATA_VALUE));
        updateQuery.setWhere(
                new ConditionColumnEquals(DatabaseCache.TABLE_CACHE_COLUM_NAME, DatabaseCache.DATA_KEY_NAME));
        UPDATE_QUERY_VALUE = updateQuery;
    }

    /**
     * Types can be stored in cache
     *
     * @author JHelp
     */
    public enum Type
    {
        /**
         * boolean type
         */
        BOOLEAN,
        /**
         * byte[] type
         */
        BYTE_ARRAY,
        /**
         * int type
         */
        INTEGER,
        /**
         * double type
         */
        REAL,
        /**
         * String type
         */
        STRING
    }

    /**
     * Value element of cache
     *
     * @author JHelp
     */
    private static class ValueCache
    {
        /**
         * Element key
         */
        public final String key;
        /**
         * Element type
         */
        public final Type   type;
        /**
         * Element value
         */
        public       String value;

        /**
         * Create a new instance of ValueCache
         *
         * @param key   Key
         * @param type  Type
         * @param value Value
         */
        public ValueCache(final String key, final Type type, final String value)
        {
            this.key = key;
            this.type = type;
            this.value = value;
        }
    }

    /**
     * Value of the element search in {@link #SELECT_QUERY_VALUE} and {@link #UPDATE_QUERY_VALUE}
     */
    private static final Data        DATA_KEY_NAME;
    /**
     * Value to store in {@link #UPDATE_QUERY_VALUE}
     */
    private static final Data        DATA_VALUE;
    /**
     * Query for select an element
     */
    private static final SelectQuery SELECT_QUERY_VALUE;
    /**
     * Table cache name
     */
    private static final String      TABLE_CACHE;
    /**
     * Column key
     */
    private static final String      TABLE_CACHE_COLUM_NAME;
    /**
     * Column type
     */
    private static final String      TABLE_CACHE_COLUM_TYPE;
    /**
     * Column value
     */
    private static final String      TABLE_CACHE_COLUM_VALUE;
    /**
     * Table cache information
     */
    private static final TableInfo   TABLE_INFO;
    /**
     * Query for modify an element
     */
    private static final UpdateQuery UPDATE_QUERY_VALUE;
    /**
     * Database where store cache
     */
    private              Database    database;

    /**
     * Create a new instance of DatabaseCache
     *
     * @param path Relative path of database
     * @throws DatabaseException On creation/access database issue
     */
    public DatabaseCache(final String path)
            throws DatabaseException
    {
        this.database = new Database(path);

        this.initializeTables();
    }

    /**
     * Create a new instance of DatabaseCache
     *
     * @param path     Relative path for database
     * @param password Password to use. On creation it defines the password, other times it is use to check authorization
     * @throws DatabaseException On creation/access database issue
     */
    public DatabaseCache(final String path, final String password)
            throws DatabaseException
    {
        this.database = new Database(path, password);

        this.initializeTables();
    }

    /**
     * Add a value to the database
     *
     * @param value Value to add
     * @throws DatabaseException On adding issue
     */
    private void addValue(final ValueCache value) throws DatabaseException
    {
        this.database.insert(DatabaseCache.TABLE_CACHE,
                             new Value(DatabaseCache.TABLE_CACHE_COLUM_NAME, Data.createLongStringData(value.key)),
                             new Value(
                                     DatabaseCache.TABLE_CACHE_COLUM_TYPE,
                                     Data.createLongStringData(value.type.name())),
                             new Value(DatabaseCache.TABLE_CACHE_COLUM_VALUE, Data.createLongStringData(value.value)));
    }

    /**
     * Change a element value
     *
     * @param value Element to change with new value
     * @throws DatabaseException On changing issue
     */
    private void changeValue(final ValueCache value) throws DatabaseException
    {
        DatabaseCache.DATA_KEY_NAME.setLongString(value.key);
        DatabaseCache.DATA_VALUE.setLongString(value.value);

        this.database.update(DatabaseCache.UPDATE_QUERY_VALUE);
    }

    /**
     * Initialize database cache tables
     *
     * @throws DatabaseException On initialization issue
     */
    private void initializeTables() throws DatabaseException
    {
        this.database.createTable(DatabaseCache.TABLE_INFO);
    }

    /**
     * Obtain a value from database
     *
     * @param key Value key
     * @return Value or {@code null} if not found
     * @throws DatabaseException On getting issue
     */
    private ValueCache obtainValue(final String key) throws DatabaseException
    {
        DatabaseCache.DATA_KEY_NAME.setLongString(key);

        final QueryResult queryResult = this.database.query(DatabaseCache.SELECT_QUERY_VALUE);

        if (queryResult.numberOfRows() == 0)
        {
            queryResult.destroy();

            return null;
        }

        final Type type = Type.valueOf(
                queryResult.getData(queryResult.getColumnIndex(DatabaseCache.TABLE_CACHE_COLUM_TYPE), 0)
                           .getLongString());
        final String value = queryResult.getData(queryResult.getColumnIndex(DatabaseCache.TABLE_CACHE_COLUM_VALUE), 0)
                                        .getLongString();

        queryResult.destroy();

        return new ValueCache(key, type, value);
    }

    /**
     * Store value. It replaces the old value if element already exists
     *
     * @param key   Element key
     * @param type  Element type
     * @param value Element value
     * @throws DatabaseException On storing issue
     */
    private void store(final String key, final Type type, final String value) throws DatabaseException
    {
        final ValueCache val = this.obtainValue(key);

        if (val == null)
        {
            this.addValue(new ValueCache(key, type, value));

            return;
        }

        if (type != val.type)
        {
            throw new IllegalArgumentException("Try to modify a " + val.type + " with a " + type);
        }

        val.value = value;
        this.changeValue(val);
    }

    /**
     * Close the cache to free memory and to commit last changes before closing application
     *
     * @throws DatabaseException On closing issue
     */
    public void closeDatabaseCache() throws DatabaseException
    {
        this.database.closeDatabase();
        this.database = null;
    }

    /**
     * Obtain a boolean value from cache
     *
     * @param key          Key of the value
     * @param defaultValue Value to use if key dosen't exists
     * @return Element value or deafultValue if key not exists
     * @throws DatabaseException On reading database issue
     */
    public boolean getBoolean(final String key, final boolean defaultValue) throws DatabaseException
    {
        final ValueCache val = this.obtainValue(key);

        if (val == null)
        {
            return defaultValue;
        }

        if (val.type != Type.BOOLEAN)
        {
            throw new IllegalStateException("The stored value is a " + val.type + " not a " + Type.BOOLEAN);
        }

        return Boolean.parseBoolean(val.value);
    }

    /**
     * Obtain a byte array value from cache
     *
     * @param key Key of the value
     * @return Element value or {@code null} if key not exists
     * @throws DatabaseException On reading database issue
     */
    public byte[] getByteArray(final String key) throws DatabaseException
    {
        final ValueCache val = this.obtainValue(key);

        if (val == null)
        {
            return null;
        }

        if (val.type != Type.BYTE_ARRAY)
        {
            throw new IllegalStateException("The stored value is a " + val.type + " not a " + Type.BYTE_ARRAY);
        }

        if (val.value.length() == 0)
        {
            return null;
        }

        return JHelpBase.decode(val.value);
    }

    /**
     * Obtain a int value from cache
     *
     * @param key          Key of the value
     * @param defaultValue Value to use if key dosen't exists
     * @return Element value or deafultValue if key not exists
     * @throws DatabaseException On reading database issue
     */
    public int getInteger(final String key, final int defaultValue) throws DatabaseException
    {
        final ValueCache val = this.obtainValue(key);

        if (val == null)
        {
            return defaultValue;
        }

        if (val.type != Type.INTEGER)
        {
            throw new IllegalStateException("The stored value is a " + val.type + " not a " + Type.INTEGER);
        }

        return Integer.parseInt(val.value);
    }

    /**
     * Obtain a double value from cache
     *
     * @param key          Key of the value
     * @param defaultValue Value to use if key dosen't exists
     * @return Element value or deafultValue if key not exists
     * @throws DatabaseException On reading database issue
     */
    public double getReal(final String key, final double defaultValue) throws DatabaseException
    {
        final ValueCache val = this.obtainValue(key);

        if (val == null)
        {
            return defaultValue;
        }

        if (val.type != Type.REAL)
        {
            throw new IllegalStateException("The stored value is a " + val.type + " not a " + Type.REAL);
        }

        return Double.parseDouble(val.value);
    }

    /**
     * Obtain a String value from cache
     *
     * @param key Key of the value
     * @return Element value or {@code null} if key not exists
     * @throws DatabaseException On reading database issue
     */
    public String getString(final String key) throws DatabaseException
    {
        final ValueCache val = this.obtainValue(key);

        if (val == null)
        {
            return null;
        }

        if (val.type != Type.STRING)
        {
            throw new IllegalStateException("The stored value is a " + val.type + " not a " + Type.STRING);
        }

        return val.value;
    }

    /**
     * Obtain the type of an element
     *
     * @param key Element key
     * @return Element type or {@code null} if key not exists
     * @throws DatabaseException On reading database issue
     */
    public Type getTpye(final String key) throws DatabaseException
    {
        final ValueCache val = this.obtainValue(key);

        if (val == null)
        {
            return null;
        }

        return val.type;
    }

    /**
     * Store a boolean value
     *
     * @param key   Element key
     * @param value Element value
     * @throws DatabaseException On writing database issue
     */
    public void store(final String key, final boolean value) throws DatabaseException
    {
        this.store(key, Type.BOOLEAN, String.valueOf(value));
    }

    /**
     * Store a byte array value
     *
     * @param key   Element key
     * @param value Element value
     * @throws DatabaseException On writing database issue
     */
    public void store(final String key, final byte[] value) throws DatabaseException
    {
        if (value == null)
        {
            this.store(key, Type.BYTE_ARRAY, "");

            return;
        }

        this.store(key, Type.BYTE_ARRAY, JHelpBase.encode(value));
    }

    /**
     * Store a double value
     *
     * @param key   Element key
     * @param value Element value
     * @throws DatabaseException On writing database issue
     */
    public void store(final String key, final double value) throws DatabaseException
    {
        this.store(key, Type.REAL, String.valueOf(value));
    }

    /**
     * Store a int value
     *
     * @param key   Element key
     * @param value Element value
     * @throws DatabaseException On writing database issue
     */
    public void store(final String key, final int value) throws DatabaseException
    {
        this.store(key, Type.INTEGER, String.valueOf(value));
    }

    /**
     * Store a String value
     *
     * @param key   Element key
     * @param value Element value
     * @throws DatabaseException On writing database issue
     */
    public void store(final String key, final String value) throws DatabaseException
    {
        this.store(key, Type.STRING, value);
    }
}
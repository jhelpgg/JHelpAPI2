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

import jhelp.util.util.Utilities;

/**
 * Query for make a selection<br>
 * The where can be modified<br>
 * Can be reuse several times<br>
 * All query is compute each time it is use, so if you use a Data instance id where you can modify the data value and reuse the
 * query, the new value will be use
 */
public class SelectQuery
{
    /**
     * Columns return the query
     */
    private final ColumnDescription[] columns;
    /**
     * Table for selection
     */
    private final String              table;
    /**
     * Condition of selection (can be {@code null} for no condition)
     */
    private       Condition           where;

    /**
     * Constructs SelectQuery
     *
     * @param table   Table for selection
     * @param columns Columns in result
     */
    public SelectQuery(final String table, final ColumnDescription... columns)
    {
        if (table == null)
        {
            throw new NullPointerException("table mustn't be null");
        }
        if (columns == null)
        {
            throw new NullPointerException("columns mustn't be null");
        }
        if (columns.length == 0)
        {
            throw new IllegalArgumentException("At least one column is need");
        }

        this.columns = columns;
        this.table = table;
    }

    /**
     * Constructs SelectQuery for query all columns
     *
     * @param tableInfo Table information
     */
    public SelectQuery(final TableInfo tableInfo)
    {
        this.table = tableInfo.getName();
        this.columns = tableInfo.getColumns();
    }

    /**
     * String representation on asking database for encrypt if need
     *
     * @param database Database reference
     * @return String representation
     */
    String toString(final Database database)
    {
        final StringBuilder stringBuffer = new StringBuilder("SELECT ");

        stringBuffer.append(this.columns[0].getColumnName());
        for (int i = 1; i < this.columns.length; i++)
        {
            stringBuffer.append(",");
            stringBuffer.append(this.columns[i].getColumnName());
        }

        stringBuffer.append(" FROM ");
        stringBuffer.append(this.table);

        if (this.where != null)
        {
            stringBuffer.append(" WHERE ");
            stringBuffer.append(this.where.toString(database));
        }

        return stringBuffer.toString();
    }

    /**
     * Columns in selection
     *
     * @return Columns in selection
     */
    public ColumnDescription[] getColumns()
    {
        return Utilities.createCopy(this.columns);
    }

    /**
     * Change selection condition
     *
     * @param where New condition. Can be {@code null} for no condition
     * @return The query itself
     */
    public SelectQuery setWhere(final Condition where)
    {
        this.where = where;

        return this;
    }

    /**
     * Compute the select query
     *
     * @return Select query
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        final StringBuilder stringBuffer = new StringBuilder("SELECT ");

        stringBuffer.append(this.columns[0].getColumnName());
        for (int i = 1; i < this.columns.length; i++)
        {
            stringBuffer.append(",");
            stringBuffer.append(this.columns[i].getColumnName());
        }

        stringBuffer.append(" FROM ");
        stringBuffer.append(this.table);

        if (this.where != null)
        {
            stringBuffer.append(" WHERE ");
            stringBuffer.append(this.where.toString());
        }

        return stringBuffer.toString();
    }
}
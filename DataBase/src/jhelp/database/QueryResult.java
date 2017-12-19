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

import java.util.ArrayList;

/**
 * Result from a select query
 */
public class QueryResult
{
    /**
     * Columns in the result
     */
    private ColumnDescription[] columns;
    /**
     * Result table
     */
    private ArrayList<Data[]>   rows;

    /**
     * Constructs QueryResult
     *
     * @param columns Columns in result
     */
    QueryResult(final ColumnDescription... columns)
    {
        this.columns = columns;
        this.rows = new ArrayList<Data[]>();
    }

    /**
     * Add row to the result
     *
     * @param datas Data for each column
     */
    void addRow(final Data... datas)
    {
        this.rows.add(datas);
    }

    /**
     * Destroy the result<br>
     * Use it when you not need result again, to free memory
     */
    public void destroy()
    {
        for (int i = this.columns.length - 1; i >= 0; i--)
        {
            this.columns[i] = null;
        }
        this.columns = null;
        this.rows.clear();
        this.rows = null;
    }

    /**
     * Column description
     *
     * @param column Column index
     * @return Column description
     */
    public ColumnDescription getColumnDescription(final int column)
    {
        return this.columns[column];
    }

    /**
     * Obtain index of column with the given name
     *
     * @param columnName Column name search
     * @return Index of the column
     */
    public int getColumnIndex(final String columnName)
    {
        for (int i = 0; i < this.columns.length; i++)
        {
            if (this.columns[i].getColumnName()
                               .equals(columnName))
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * Data in the result
     *
     * @param column Column
     * @param row    Row
     * @return Data at the specified position
     */
    public Data getData(final int column, final int row)
    {
        return this.rows.get(row)[column];
    }

    /**
     * Number of columns in the result
     *
     * @return Number of columns in the result
     */
    public int numberOfColumns()
    {
        return this.columns.length;
    }

    /**
     * Number of rows in the result
     *
     * @return Number of rows in the result
     */
    public int numberOfRows()
    {
        return this.rows.size();
    }
}
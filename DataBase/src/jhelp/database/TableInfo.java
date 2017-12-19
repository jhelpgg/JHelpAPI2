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
import jhelp.util.list.EnumerationIterator;

/**
 * Table information<br>
 * Describe a table
 */
public class TableInfo
{
    /**
     * Table columns
     */
    private final ArrayList<ColumnDescription> colums;
    /**
     * Table name
     */
    private final String                       name;

    /**
     * Constructs TableInfo
     *
     * @param name Table name
     */
    public TableInfo(final String name)
    {
        this.name = name;
        this.colums = new ArrayList<ColumnDescription>();
    }

    /**
     * Create a new instance of TableInfo
     *
     * @param name               Table name
     * @param columnDescriptions List of columns
     */
    public TableInfo(final String name, final ColumnDescription... columnDescriptions)
    {
        this(name);

        for (final ColumnDescription columnDescription : columnDescriptions)
        {
            this.addColumn(columnDescription);
        }
    }

    /**
     * Add a column
     *
     * @param columnDescription Column description
     */
    public void addColumn(final ColumnDescription columnDescription)
    {
        this.colums.add(columnDescription);
    }

    /**
     * Add a column
     *
     * @param name     Column name
     * @param dataType Data type
     */
    public void addColumn(final String name, final DataType dataType)
    {
        this.colums.add(new ColumnDescription(name, dataType));
    }

    /**
     * Add a column
     *
     * @param name       Column name
     * @param dataType   Data type
     * @param foreignKey If no {@code null} indicates that is a key a the table name here
     */
    public void addColumn(final String name, final DataType dataType, final String foreignKey)
    {
        final ColumnDescription columnDescription = new ColumnDescription(name, dataType);
        columnDescription.setForeignKey(foreignKey);

        this.colums.add(columnDescription);
    }

    /**
     * Add a column
     *
     * @param name     Column name
     * @param dataType Column type
     */
    public void addColumn(final String name, final String dataType)
    {
        this.colums.add(new ColumnDescription(name, DataType.valueOf(dataType)));
    }

    /**
     * Add a column
     *
     * @param name       Column name
     * @param dataType   Data type
     * @param foreignKey If no {@code null} indicates that is a key a the table name here
     */
    public void addColumn(final String name, final String dataType, final String foreignKey)
    {
        final ColumnDescription columnDescription = new ColumnDescription(name, DataType.valueOf(dataType));
        columnDescription.setForeignKey(foreignKey);

        this.colums.add(columnDescription);
    }

    /**
     * Indicates that a table info is equals
     *
     * @param tableInfo Table info to compare
     * @return {@code true} if equals
     */
    public boolean equals(final TableInfo tableInfo)
    {
        if (!this.name.equalsIgnoreCase(tableInfo.name))
        {
            return false;
        }

        final int size = this.colums.size();
        if (size != tableInfo.colums.size())
        {
            return false;
        }

        for (final ColumnDescription columnDescription : this.colums)
        {
            if (tableInfo.indexOfColumnDescription(columnDescription) < 0)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Get a column description
     *
     * @param index Column index
     * @return Column description
     */
    public ColumnDescription getColumn(final int index)
    {
        return this.colums.get(index);
    }

    /**
     * List of column description
     *
     * @return List of column description
     */
    public EnumerationIterator<ColumnDescription> getColumnList()
    {
        return new EnumerationIterator<ColumnDescription>(this.colums.iterator());
    }

    /**
     * Obtain columns description
     *
     * @return List of column description
     */
    public ColumnDescription[] getColumns()
    {
        return this.colums.toArray(new ColumnDescription[this.colums.size()]);
    }

    /**
     * Table name
     *
     * @return Table name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Index of column description
     *
     * @param columnDescription Column description search
     * @return Index or -1 if not found
     */
    public int indexOfColumnDescription(final ColumnDescription columnDescription)
    {
        return this.colums.indexOf(columnDescription);
    }

    /**
     * Number of columns
     *
     * @return Number of columns
     */
    public int numberOfColumns()
    {
        return this.colums.size();
    }
}
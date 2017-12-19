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

import jhelp.util.util.HashCode;

/**
 * describe a column<br>
 * <br>
 * Last modification : 16 avr. 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public class ColumnDescription
{
    /**
     * Column name
     */
    private final String   columnName;
    /**
     * Column data type
     */
    private final DataType dataType;
    /**
     * Table reference if column is foreign key
     */
    private       String   foreignKey;

    /**
     * Constructs ColumnDescription
     *
     * @param columnName Column name
     * @param dataType   Data type
     */
    public ColumnDescription(final String columnName, final DataType dataType)
    {
        if (columnName == null)
        {
            throw new NullPointerException("columnName mustn't be null");
        }
        if (dataType == null)
        {
            throw new NullPointerException("dataType mustn't be null");
        }
        this.columnName = columnName;
        this.dataType = dataType;
        this.foreignKey = null;
    }

    /**
     * Return columnName
     *
     * @return columnName
     */
    public String getColumnName()
    {
        return this.columnName;
    }

    /**
     * Return dataType
     *
     * @return dataType
     */
    public DataType getDataType()
    {
        return this.dataType;
    }

    /**
     * Hash code
     *
     * @return Hash code
     * @see Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return HashCode.computeHashCode(this.columnName, this.dataType);
    }

    /**
     * Indicates if this column description is equals to a given object
     *
     * @param obj Object test
     * @return {@code true} if the given object is equals to this column description
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof ColumnDescription))
        {
            return false;
        }
        final ColumnDescription other = (ColumnDescription) obj;
        if (!this.columnName.equalsIgnoreCase(other.columnName))
        {
            return false;
        }
        return this.dataType.equals(other.dataType);
    }

    /**
     * Return foreignKey
     *
     * @return foreignKey
     */
    public String isForeignKey()
    {
        return this.foreignKey;
    }

    /**
     * Modify foreignKey
     *
     * @param foreignKey New foreignKey value
     */
    public void setForeignKey(final String foreignKey)
    {
        this.foreignKey = foreignKey;
    }
}
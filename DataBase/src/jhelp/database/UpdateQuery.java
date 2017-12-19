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

/**
 * Query for update
 */
public class UpdateQuery
{
    /**
     * Table to update
     */
    private final String    table;
    /**
     * Values for each columns
     */
    private final Value[]   values;
    /**
     * Condition for update
     */
    private       Condition where;

    /**
     * Constructs UpdateQuery
     *
     * @param table  Table to update
     * @param values New values
     */
    public UpdateQuery(final String table, final Value... values)
    {
        if (table == null)
        {
            throw new NullPointerException("table mustn't be null");
        }

        if (values == null)
        {
            throw new NullPointerException("values mustn't be null");
        }

        if (values.length < 1)
        {
            throw new IllegalArgumentException("values must have at least 1 element");
        }

        this.table = table;
        this.values = values;
    }

    /**
     * String representation on asking database for encrypt if need
     *
     * @param database Database reference
     * @return String representation
     */
    String toString(final Database database)
    {
        final StringBuilder stringBuffer = new StringBuilder("UPDATE ");
        stringBuffer.append(this.table);
        stringBuffer.append(" SET ");
        stringBuffer.append(this.values[0].getName());
        stringBuffer.append("=");
        stringBuffer.append(database.stringForWrite(this.values[0]));

        for (int i = 1; i < this.values.length; i++)
        {
            stringBuffer.append(" , ");
            stringBuffer.append(this.values[i].getName());
            stringBuffer.append("=");
            stringBuffer.append(database.stringForWrite(this.values[i]));
        }

        if (this.where != null)
        {
            stringBuffer.append(" WHERE ");
            stringBuffer.append(this.where.toString(database));
        }

        return stringBuffer.toString();
    }

    /**
     * Actual condition (can be {@code null} if no condition)
     *
     * @return Actual condition
     */
    public Condition getWhere()
    {
        return this.where;
    }

    /**
     * Set the condition (can be {@code null} if no condition)
     *
     * @param where New condition
     */
    public void setWhere(final Condition where)
    {
        this.where = where;
    }

    /**
     * Condition string
     *
     * @return Condition string
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        final StringBuilder stringBuffer = new StringBuilder("UPDATE ");
        stringBuffer.append(this.table);
        stringBuffer.append(" SET ");
        stringBuffer.append(this.values[0].getName());
        stringBuffer.append("=");
        stringBuffer.append(this.values[0].getData().toString());

        for (int i = 1; i < this.values.length; i++)
        {
            stringBuffer.append(" , ");
            stringBuffer.append(this.values[i].getName());
            stringBuffer.append("=");
            stringBuffer.append(this.values[i].getData().toString());
        }

        if (this.where != null)
        {
            stringBuffer.append(" WHERE ");
            stringBuffer.append(this.where.toString());
        }

        return stringBuffer.toString();
    }
}
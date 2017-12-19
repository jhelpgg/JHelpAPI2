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

import jhelp.util.text.UtilText;

/**
 * Condition specify that a column is either in :<li>A list a value</li><br>
 * <b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;OR</b><li>A result of a select query</li>
 */
public class ConditionIN
        extends Condition
{
    /**
     * Column name
     */
    private final String      columnName;
    /**
     * Select query
     */
    private       SelectQuery selectQuery;
    /**
     * Values list
     */
    private       Data[]      values;

    /**
     * Constructs ConditionIN
     *
     * @param columnName Column name
     * @param values     Values can take the column
     */
    public ConditionIN(final String columnName, final Data... values)
    {
        if (columnName == null)
        {
            throw new NullPointerException("columnName mustn't be null");
        }
        if (values == null)
        {
            throw new NullPointerException("values mustn't be null");
        }
        if (values.length == 0)
        {
            throw new IllegalArgumentException("Must have at least one value");
        }
        for (int i = 0; i < values.length; i++)
        {
            if (values[i] == null)
            {
                throw new IllegalArgumentException(UtilText.concatenate("At least one null value : ", i));
            }
        }

        this.columnName = columnName;
        this.values = values;
    }

    /**
     * Constructs ConditionIN
     *
     * @param columnName  Column name
     * @param selectQuery Select query for the list of values
     */
    public ConditionIN(final String columnName, final SelectQuery selectQuery)
    {
        if (columnName == null)
        {
            throw new NullPointerException("columnName mustn't be null");
        }
        if (selectQuery == null)
        {
            throw new NullPointerException("selectQuery mustn't be null");
        }

        this.columnName = columnName;
        this.selectQuery = selectQuery;
    }

    /**
     * String representation on asking database for encrypt if need
     *
     * @param database Database reference
     * @return String representation
     * @see Condition#toString(Database)
     */
    @Override
    public String toString(final Database database)
    {
        final StringBuilder stringBuffer = new StringBuilder(this.columnName);

        if (this.selectQuery != null)
        {
            stringBuffer.append(" IN (");
            stringBuffer.append(this.selectQuery.toString(database));
            stringBuffer.append(')');
        }
        else
        {
            stringBuffer.append(" IN (");
            stringBuffer.append(database.stringForWrite(this.values[0]));
            for (int i = 1; i < this.values.length; i++)
            {
                stringBuffer.append(",");
                stringBuffer.append(database.stringForWrite(this.values[i]));
            }
            stringBuffer.append(")");
        }

        return stringBuffer.toString();
    }

    /**
     * String representation
     *
     * @return String representation
     * @see Condition#toString()
     */
    @Override
    public String toString()
    {
        final StringBuilder stringBuffer = new StringBuilder(this.columnName);

        if (this.selectQuery != null)
        {
            stringBuffer.append(" IN (");
            stringBuffer.append(this.selectQuery.toString());
            stringBuffer.append(')');
        }
        else
        {
            stringBuffer.append(" IN (");
            stringBuffer.append(this.values[0].toString());
            for (int i = 1; i < this.values.length; i++)
            {
                stringBuffer.append(",");
                stringBuffer.append(this.values[i].toString());
            }
            stringBuffer.append(")");
        }

        return stringBuffer.toString();
    }
}
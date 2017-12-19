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
 * Condition that implies two conditions
 */
public abstract class BinaryCondition
        extends Condition
{
    /**
     * First condition
     */
    private final Condition condition1;
    /**
     * Second condition
     */
    private final Condition condition2;
    /**
     * Condition operation name
     */
    private final String    conditionName;

    /**
     * Constructs BinaryCondition
     *
     * @param conditionName Condition operation name
     * @param condition1    First condition
     * @param condition2    Second condition
     */
    public BinaryCondition(final String conditionName, final Condition condition1, final Condition condition2)
    {
        if (conditionName == null)
        {
            throw new NullPointerException("conditionName mustn't be null");
        }
        if (condition1 == null)
        {
            throw new NullPointerException("condition1 mustn't be null");
        }
        if (condition2 == null)
        {
            throw new NullPointerException("condition2 mustn't be null");
        }

        this.conditionName = conditionName;
        this.condition1 = condition1;
        this.condition2 = condition2;
    }

    /**
     * String representation on asking database for encrypt if need
     *
     * @param database Database reference
     * @return String representation
     * @see Condition#toString(Database)
     */
    @Override
    protected String toString(final Database database)
    {
        final StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append('(');
        stringBuffer.append(this.condition1.toString(database));
        stringBuffer.append(") ");
        stringBuffer.append(this.conditionName);
        stringBuffer.append(" (");
        stringBuffer.append(this.condition2.toString(database));
        stringBuffer.append(')');

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
        final StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append('(');
        stringBuffer.append(this.condition1.toString());
        stringBuffer.append(") ");
        stringBuffer.append(this.conditionName);
        stringBuffer.append(" (");
        stringBuffer.append(this.condition2.toString());
        stringBuffer.append(')');

        return stringBuffer.toString();
    }
}
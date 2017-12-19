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
 * Condition with one argument
 */
public abstract class UnaryCondition
        extends Condition
{
    /**
     * Condition where apply operation
     */
    private final Condition condition;
    /**
     * Condition operation name
     */
    private final String    conditionName;

    /**
     * Constructs UnaryCondition
     *
     * @param conditionName Condition operation name
     * @param condition     Condition where apply condition
     */
    public UnaryCondition(final String conditionName, final Condition condition)
    {
        if (conditionName == null)
        {
            throw new NullPointerException("conditionName mustn't be null");
        }
        if (condition == null)
        {
            throw new NullPointerException("condition mustn't be null");
        }
        this.conditionName = conditionName;
        this.condition = condition;
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
        final StringBuilder stringBuffer = new StringBuilder(this.conditionName);
        stringBuffer.append('(');
        stringBuffer.append(this.condition.toString(database));
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
        final StringBuilder stringBuffer = new StringBuilder(this.conditionName);
        stringBuffer.append('(');
        stringBuffer.append(this.condition.toString());
        stringBuffer.append(')');

        return stringBuffer.toString();
    }
}
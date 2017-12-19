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
 * Condition make AND between tow conditions
 */
public class BinaryConditionAND
        extends BinaryCondition
{
    /**
     * Constructs BinaryConditionAND
     *
     * @param condition1 First condition
     * @param condition2 Second condition
     */
    public BinaryConditionAND(final Condition condition1, final Condition condition2)
    {
        super("AND", condition1, condition2);
    }
}
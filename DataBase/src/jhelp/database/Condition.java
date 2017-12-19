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
 * Generic condition
 */
public abstract class Condition
{

    /**
     * Constructs Condition
     */
    public Condition()
    {
    }

    /**
     * String representation on asking database for encrypt if need
     *
     * @param database Database reference
     * @return String representation
     */
    protected abstract String toString(Database database);

    /**
     * String representation
     *
     * @return String representation
     * @see Object#toString()
     */
    @Override
    public abstract String toString();
}
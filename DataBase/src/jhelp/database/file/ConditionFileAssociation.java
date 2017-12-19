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

package jhelp.database.file;

import jhelp.database.Condition;
import jhelp.database.Database;

/**
 * Condition specification file association
 */
class ConditionFileAssociation
        extends Condition
{
    /**
     * Files id
     */
    private final int[] filesID;

    /**
     * Constructs ConditionFileAssociation
     *
     * @param filesID files ID
     */
    public ConditionFileAssociation(final int... filesID)
    {
        this.filesID = filesID;
    }

    /**
     * String representation on asking database for encrypt if need
     *
     * @param database Database reference
     * @return String representation
     * @see jhelp.database.Condition#toString(jhelp.database.Database)
     */
    @Override
    public String toString(final Database database)
    {
        final StringBuilder condition = new StringBuilder();

        final int length = this.filesID.length;

        int mult = 1;
        if (this.filesID[0] < 0)
        {
            condition.append(" NOT (");
            mult = -1;
        }

        condition.append("ID");

        condition.append(" IN (");
        condition.append(mult * this.filesID[0]);

        for (int i = 1; i < length; i++)
        {
            condition.append(", ");
            condition.append(mult * this.filesID[i]);
        }
        condition.append(")");

        if (mult < 0)
        {
            condition.append(")");
        }

        return condition.toString();
    }

    /**
     * String representation
     *
     * @return String representation
     * @see jhelp.database.Condition#toString()
     */
    @Override
    public String toString()
    {
        final StringBuilder condition = new StringBuilder();

        final int length = this.filesID.length;

        int mult = 1;
        if (this.filesID[0] < 0)
        {
            condition.append(" NOT (");
            mult = -1;
        }

        condition.append("ID");

        condition.append(" IN (");
        condition.append(mult * this.filesID[0]);

        for (int i = 1; i < length; i++)
        {
            condition.append(", ");
            condition.append(mult * this.filesID[i]);
        }
        condition.append(")");

        if (mult < 0)
        {
            condition.append(")");
        }

        return condition.toString();
    }
}
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

/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.database;

/**
 * Condition for make a OR between two conditions<br>
 * <br>
 * Last modification : 17 mars 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public class BinaryConditionOR
        extends BinaryCondition
{
    /**
     * Constructs BinaryConditionOR
     *
     * @param condition1
     *           First condition
     * @param condition2
     *           Second condition
     */
    public BinaryConditionOR(final Condition condition1, final Condition condition2)
    {
        super("OR", condition1, condition2);
    }
}
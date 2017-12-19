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

package jhelp.util.thread;

/**
 * {@link Future} status
 */
public enum FutureStatus
{
    /**
     * Indicates if task still computing (May be not already launched)
     */
    COMPUTING,
    /**
     * Indicates that task finished successfully
     */
    RESULT,
    /**
     * Indicates that task had error on its execution
     */
    ERROR,
    /**
     * Indicates that task wad cancelled during its execution
     */
    CANCELLED
}

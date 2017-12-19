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
 * Exception that may happen while play a task
 */
public class TaskException extends Exception
{
    /**
     * Create the exception
     *
     * @param message Exception message
     */
    public TaskException(String message)
    {
        super(message);
    }

    /**
     * Create the exception
     *
     * @param message Exception message
     * @param cause   Exception cause
     */
    public TaskException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

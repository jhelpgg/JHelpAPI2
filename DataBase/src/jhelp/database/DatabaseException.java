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
 * Exception on use data base
 */
public class DatabaseException
        extends Exception
{

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -5698685938159931801L;

    /**
     * Constructs DatabaseException
     */
    public DatabaseException()
    {
    }

    /**
     * Constructs DatabaseException
     *
     * @param message Message
     */
    public DatabaseException(final String message)
    {
        super(message);
    }

    /**
     * Constructs DatabaseException
     *
     * @param message Message
     * @param cause   Cause
     */
    public DatabaseException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructs DatabaseException
     *
     * @param cause Cause
     */
    public DatabaseException(final Throwable cause)
    {
        super(cause);
    }
}
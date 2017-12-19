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
 * User of {@link PostOffice} have to extends this class
 *
 * @author JHelp
 */
public abstract class User
{
    /**
     * User name
     */
    private final String name;

    /**
     * Create a new instance of User
     *
     * @param name User name
     */
    public User(final String name)
    {
        if (name == null)
        {
            throw new NullPointerException("name MUST NOT be null");
        }

        this.name = name;
    }

    /**
     * User name
     *
     * @return User name
     */
    public final String getName()
    {
        return this.name;
    }

    /**
     * Call when user receive a message.<br>
     * Since it is call in its own thread, you can do operation as long/heavy as you need
     *
     * @param message Received message
     */
    public abstract void receiveMessage(Message message);
}
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
 * Message description
 *
 * @author JHelp
 */
public final class Message
{
    /**
     * Message sender, use for reply
     */
    public final String from;
    /**
     * Message itself
     */
    public final Object message;
    /**
     * Message ID
     */
    public final int    messageId;

    /**
     * Create a new instance of Message
     *
     * @param messageId Message ID
     * @param from      Sender
     * @param message   Message itself
     */
    Message(final int messageId, final String from, final Object message)
    {
        this.messageId = messageId;
        this.from = from;
        this.message = message;
    }
}
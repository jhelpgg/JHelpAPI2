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
 * Inspired from strand notion in C++.<br>
 * A strand is a queue of task to be played. Each task are executed one after other.
 */
public final class Strand extends MessageHandler<RunnableTask>
{
    /**
     * Create a strand
     */
    public Strand()
    {
    }

    /**
     * Called when a message received and treat it
     *
     * @param message Received message
     */
    @Override
    protected void handleMessage(final RunnableTask message)
    {
        try
        {
            message.run();
        }
        catch (Exception exception)
        {
            message.taskError(new TaskException("Execution of task failed", exception));
        }
    }
}

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

import com.sun.istack.internal.Nullable;

/**
 * Task with no result
 */
public interface ConsumerTask<P> extends Task<P, Void>
{
    /**
     * Consume the value
     *
     * @param parameter Task parameter
     */
    void consume(@Nullable P parameter);

    /**
     * Play the task.<br>
     * By default call {@link #consume(Object)} and return {@code null}
     *
     * @param parameter Task parameter
     * @return Task result
     */
    @Override
    default @Nullable Void playTask(P parameter)
    {
        this.consume(parameter);
        return null;
    }
}

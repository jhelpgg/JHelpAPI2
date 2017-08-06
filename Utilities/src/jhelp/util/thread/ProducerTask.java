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
 * task with no parameter
 */
public interface ProducerTask<R> extends Task<Void, R>
{
    /**
     * Play the task.<br>
     * By default call {@link #produce()}
     *
     * @param ignored Ignored parameter
     * @return Task result
     */
    @Override
    default @Nullable R playTask(@Nullable Void ignored)
    {
        return this.produce();
    }

    /***
     * Produce the value
     * @return Task result
     */
    @Nullable R produce();
}

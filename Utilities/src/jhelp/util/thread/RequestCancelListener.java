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

import com.sun.istack.internal.NotNull;

/**
 * Listener of task cancellation
 */
public interface RequestCancelListener<R>
{
    /**
     * Called if someone request to cancel a task.<br>
     * Have to decide to propagate the cancel status (Via {@link Promise#cancel(String)}) or not
     *
     * @param promise Promise that manage task status
     * @param reason  Cancellation reason
     */
    void cancellationRequested(@NotNull Promise<R> promise, @NotNull String reason);
}

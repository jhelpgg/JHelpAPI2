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
import java.util.concurrent.atomic.AtomicBoolean;
import jhelp.util.list.Pair;

/**
 * Task that can be cancelled.<br>
 * Task can oly be cancelled if not already launched.
 *
 * @param <P> Task parameter type
 * @param <R> Task result type
 */
public final class CancellableTask<P, R>
{
    /**
     * Indicated if task is cancelled
     */
    private final AtomicBoolean cancelled;
    /**
     * Task ID
     */
    private final int           taskID;

    /**
     * Create a cancellable task
     *
     * @param task      Task to do
     * @param parameter Task parameter
     * @param delay     Time before play
     */
    CancellableTask(@NotNull Task<P, R> task, @NotNull P parameter, long delay)
    {
        this.cancelled = new AtomicBoolean(false);
        this.taskID = ThreadManager.doTask(pair ->
                                           {
                                               if (this.cancelled.get())
                                               {
                                                   throw new IllegalStateException("Task is cancelled");
                                               }

                                               return pair.first.playTask(pair.second);
                                           },
                                           new Pair<>(task, parameter),
                                           delay);
    }

    /**
     * Cancel the task.<br>
     * Does nothing if task is playing or played
     */
    public void cancel()
    {
        this.cancelled.set(true);
        ThreadManager.THREAD_MANAGER.removeTask(this.taskID);
    }
}

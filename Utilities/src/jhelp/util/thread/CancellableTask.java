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

import java.util.concurrent.atomic.AtomicBoolean;
import jhelp.util.list.Pair;

public final class CancellableTask<P, R>
{
    private final AtomicBoolean cancelled;
    private final int           taskID;

    CancellableTask(Task<P, R> task, P parameter, long delay)
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

    public void cancel()
    {
        this.cancelled.set(true);
        ThreadManager.THREAD_MANAGER.removeTask(this.taskID);
    }
}

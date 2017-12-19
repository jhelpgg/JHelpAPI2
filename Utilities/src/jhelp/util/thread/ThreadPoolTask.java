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

/***
 * Task to do in {@link ThreadPool}
 */
final class ThreadPoolTask implements RunnableTask
{
    /**
     * Thread pool parent
     */
    private final ThreadPool   parent;
    /**
     * Task to do
     */
    private final RunnableTask task;

    /**
     * Create the task
     *
     * @param parent Thread pool parent
     * @param task   Task to do
     */
    ThreadPoolTask(@NotNull ThreadPool parent, @NotNull RunnableTask task)
    {
        this.parent = parent;
        this.task = task;
    }

    /**
     * Play the task
     */
    @Override
    public void run()
    {
        try
        {
            this.task.run();
        }
        finally
        {
            this.parent.oneTaskFinished();
        }
    }

    /**
     * Called if excption happen in playing task
     *
     * @param taskException Task exception
     */
    @Override
    public void taskError(final TaskException taskException)
    {
        this.task.taskError(taskException);
    }

    /**
     * Called when task successfully finished
     *
     * @param result Task result
     */
    @Override
    public void taskResult(final Void result)
    {
        this.task.taskResult(result);
    }
}

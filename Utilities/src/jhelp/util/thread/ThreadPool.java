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
import java.util.Objects;
import jhelp.util.list.Queue;
import jhelp.util.math.Math2;

/**
 * Pool with limited number task in same time
 */
public final class ThreadPool
{
    /**
     * Current  number of free thread
     */
    private       int                   numberThreadFree;
    /**
     * Queue of tasks that's wait a thread is free
     */
    private final Queue<ThreadPoolTask> tasks;

    /**
     * Create the thread pool
     *
     * @param numberOfThread Number maximum of tasks in same time
     */
    public ThreadPool(int numberOfThread)
    {
        this.numberThreadFree = Math2.limit(numberOfThread, 1, ThreadManager.NUMBER_OF_THREAD);
        this.tasks = new Queue<>();
    }

    /**
     * Called when a task is finished
     */
    void oneTaskFinished()
    {
        synchronized (this.tasks)
        {
            this.numberThreadFree++;

            if (!this.tasks.empty())
            {
                this.numberThreadFree--;
                ThreadManager.parallel(this.tasks.outQueue());
            }
        }
    }

    /**
     * Launch a task.<br>
     * If there enough free thread the task is launch immediately in its own thread, else the task is queued to wait its turn
     *
     * @param runnableTask Task to do
     */
    public void runThread(@NotNull RunnableTask runnableTask)
    {
        Objects.requireNonNull(runnableTask, "runnableTask MUST NOT be null!");
        ThreadPoolTask threadPoolTask = new ThreadPoolTask(this, runnableTask);

        synchronized (this.tasks)
        {
            if (this.numberThreadFree > 0)
            {
                this.numberThreadFree--;
                ThreadManager.parallel(threadPoolTask);
            }
            else
            {
                this.tasks.inQueue(threadPoolTask);
            }
        }
    }
}

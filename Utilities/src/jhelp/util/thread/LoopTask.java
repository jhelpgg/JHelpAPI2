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
import com.sun.istack.internal.Nullable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Task play in loop
 *
 * @param <P> Task parameter type
 * @param <R> Task result type
 */
public final class LoopTask<P, R>
{
    private final AtomicBoolean loop;
    private final P             parameter;
    private final long          repeatDelay;
    private final AtomicBoolean sleeping;
    private final Task<P, R>    task;

    /**
     * Create the task
     *
     * @param task        Task to repeat
     * @param delay       First launch delay
     * @param repeatDelay Delay between each repetition
     */
    LoopTask(@NotNull Task<P, R> task, long delay, long repeatDelay)
    {
        this(task, null, delay, repeatDelay);
    }

    /**
     * Create the task
     *
     * @param task        Task to repeat
     * @param parameter   Task parameter
     * @param delay       First launch delay
     * @param repeatDelay Delay between each repetition
     */
    LoopTask(@NotNull Task<P, R> task, @Nullable P parameter, long delay, long repeatDelay)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        this.loop = new AtomicBoolean(true);
        this.sleeping = new AtomicBoolean(false);
        this.task = task;
        this.parameter = parameter;
        this.repeatDelay = Math.max(1, repeatDelay);
        ThreadManager.parallel(this::repeat, delay);
    }

    /**
     * Repeat the task
     */
    private void repeat()
    {
        while (this.loop.get())
        {
            this.task.playTask(this.parameter);

            synchronized (this.sleeping)
            {
                this.sleeping.set(true);

                try
                {
                    this.sleeping.wait(this.repeatDelay);
                }
                catch (Exception ignored)
                {
                }

                this.sleeping.set(false);
            }
        }
    }

    /**
     * Stop the repetition
     */
    public void stop()
    {
        this.loop.set(false);

        synchronized (this.sleeping)
        {
            if (this.sleeping.get())
            {
                this.sleeping.notify();
            }
        }
    }
}
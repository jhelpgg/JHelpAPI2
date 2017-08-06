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

/**
 * Task that launch a task and manage an associated {@link Promise}
 */
final class LaunchTask<P, R> implements RunnableTask
{
    /**
     * Task parameter
     */
    private final P          parameter;
    /**
     * Promise associated to the task
     */
    private final Promise<R> promise;
    /**
     * Task to launch
     */
    private final Task<P, R> task;

    /**
     * Crete the main task
     *
     * @param task      Task to play
     * @param parameter Task parameter
     */
    LaunchTask(@NotNull final Task<P, R> task, @Nullable final P parameter)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        this.task = task;
        this.parameter = parameter;
        this.promise = new Promise<>();
    }

    /**
     * Future of the task result
     *
     * @return Future of the task result
     */
    @NotNull Future<R> future()
    {
        return this.promise.future();
    }

    /**
     * Play the task
     */
    @Override
    public void run()
    {
        try
        {
            this.promise.setResult(this.task.playTask(this.parameter));
        }
        catch (Throwable throwable)
        {
            this.promise.setError(new TaskException("Failed to launch the task!", throwable));
        }
    }
}

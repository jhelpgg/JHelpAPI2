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
import jhelp.util.list.Pair;

/**
 * Task that call an other task and then a callback when the task is finished
 */
public final class CallbackTask<P, R> implements Task<P, R>
{
    /**
     * Call back to alert
     */
    private final ConsumerTask<Pair<String, R>> callback;
    /**
     * Callback task name
     */
    private final String                        name;
    /**
     * Task to execute
     */
    private final Task<P, R>                    task;

    /**
     * Create the callback task
     *
     * @param name     Callback task name
     * @param task     Task to execute
     * @param callback Callback to alert when task is finished
     */
    public CallbackTask(
            @NotNull final String name, @NotNull final Task<P, R> task,
            @NotNull final ConsumerTask<Pair<String, R>> callback)
    {
        Objects.requireNonNull(name, "name MUST NOT be null!");
        Objects.requireNonNull(task, "task MUST NOT be null!");
        Objects.requireNonNull(callback, "callback MUST NOT be null!");
        this.name = name;
        this.task = task;
        this.callback = callback;
    }

    /**
     * Called when task play
     *
     * @param parameter Task parameter
     * @return Task result
     */
    @Override
    public @Nullable R playTask(final @Nullable P parameter)
    {
        return this.task.playTask(parameter);
    }

    /**
     * Called if task execution failed
     *
     * @param taskException Exception happen
     */
    @Override
    public void taskError(final @NotNull TaskException taskException)
    {
        this.callback.taskError(taskException);
    }

    /**
     * Called when result is computed
     *
     * @param result Task result
     */
    @Override
    public void taskResult(final @Nullable R result)
    {
        this.callback.consume(new Pair<>(this.name, result));
    }
}

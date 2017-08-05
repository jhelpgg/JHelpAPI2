package jhelp.util.thread;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.Objects;

/**
 * Task that play a task and give a future to track/link to the embed task
 */
class TaskFuture<P, R> implements RunnableTask
{
    /**
     * Cancellation reason
     */
    private String cancellationReason;
    /**
     * Indicated if task was cancelled
     */
    private boolean cancelled = false;
    /**
     * Task parameter
     */
    private       P             parameter;
    /**
     * Promise of task result
     */
    private final Promise<R>    promise;
    /**
     * Task to play
     */
    private final Task<P, R>    task;
    /**
     * Last exception happen
     */
    private       TaskException taskException;

    /**
     * Create the task
     *
     * @param task Task to play
     */
    TaskFuture(final @NotNull Task<P, R> task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null");
        this.task = task;
        this.promise = new Promise<>();
    }

    /**
     * Task is cancelled
     *
     * @param reason Cancellation reason
     */
    void cancel(@NotNull String reason)
    {
        this.cancelled = true;
        this.cancellationReason = reason;
        ThreadManager.parallel(this);
    }

    /**
     * Task is on error
     *
     * @param taskException Exception happen
     */
    void error(@NotNull TaskException taskException)
    {
        Objects.requireNonNull(taskException, "taskException MUST NOT be null!");
        this.taskException = taskException;
        ThreadManager.parallel(this);
    }

    /**
     * Future to track/link to the result
     *
     * @return Future to track/link to the result
     */
    @NotNull Future<R> future()
    {
        return this.promise.future();
    }

    /**
     * Launch the task
     *
     * @param parameter Task parameter
     */
    void launch(@Nullable P parameter)
    {
        this.parameter = parameter;
        ThreadManager.parallel(this);
    }

    /**
     * Play the task
     */
    @Override
    public void run()
    {
        if (this.cancelled)
        {
            this.promise.cancelRequested(this.cancellationReason);
        }
        else if (this.taskException == null)
        {
            try
            {
                this.promise.setResult(this.task.playTask(this.parameter));
            }
            catch (Throwable throwable)
            {
                this.promise.setError(new TaskException("Failed to play a task", throwable));
            }
        }
        else
        {
            this.promise.setError(this.taskException);
        }
    }
}

package jhelp.util.data;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import jhelp.util.thread.Task;
import jhelp.util.thread.TaskException;
import jhelp.util.thread.ConsumerTask;

/**
 * Call a {@link Task} when an {@link Observable} fulfill a {@link Condition}
 */
public final class ConditionChecker<T, R> implements Observer<T>
{
    /**
     * Call a {@link Task} when an {@link Observable} full fill a {@link Condition}.<br>
     * The given "repeat" decide, when the task just played, if have to play it again. If the returned boolean is {@code true},
     * the task will be recalled next time the condition is valid.<br>
     * Note: if "repeat" is {@code null}, the task will be played only one time.
     *
     * @param observable {@link Observable} checked
     * @param condition  {@link Condition} to validate
     * @param task       {@link Task} to do
     * @param id         Condition checker ID, used by "repeat" to identify this condition checker
     * @param repeat     Decide if the condition have to checked again after a play. {@code null} means task play only one time
     * @param <T>        Type of value follow
     * @param <R>        Task result type
     */
    public static <T, R> void when(
            @NotNull Observable<T> observable, @NotNull Condition<T> condition, @NotNull Task<T, R> task, int id,
            @Nullable Combiner<R, Integer, Boolean> repeat)
    {
        Objects.requireNonNull(observable, "observable");
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(task, "task");
        observable.startObserve(new ConditionChecker<>(observable, condition, task, id, repeat));
    }

    /**
     * Call a {@link Task} when an {@link Observable} full fill a {@link Condition}.<br>
     * The task is called only one time
     *
     * @param observable {@link Observable} checked
     * @param condition  {@link Condition} to validate
     * @param task       {@link Task} task to do
     * @param <T>        Type of value follow
     */
    public static <T> void when(
            @NotNull Observable<T> observable, @NotNull Condition<T> condition, @NotNull ConsumerTask<T> task)
    {
        ConditionChecker.when(observable, condition, task, 0, null);
    }

    /**
     * Call a {@link Task} when an {@link Observable} full fill a {@link Condition}.<br>
     * The task is called each time condition full fill
     *
     * @param observable {@link Observable} checked
     * @param condition  {@link Condition} to validate
     * @param task       {@link Task} task to do
     * @param <T>        Type of value follow
     */
    public static <T> void eachTime(
            @NotNull Observable<T> observable, @NotNull Condition<T> condition, @NotNull ConsumerTask<T> task)
    {
        ConditionChecker.when(observable, condition, task, 0, Combiner.TRUE());
    }

    /**
     * Call a {@link Task} when an {@link Observable} embed value become {@code true}.<br>
     * The given "repeat" decide, when the task just played, if have to play it again. If the returned boolean is {@code true},
     * the task will be recalled next time the condition is valid.<br>
     * Note: if "repeat" is {@code null}, the task will be played only one time.
     *
     * @param observable {@link Observable} checked
     * @param task       {@link Task} task to do
     * @param id         Condition checker ID, used by "repeat" to identify this condition checker
     * @param repeat     Decide if the condition have to checked again after a play. {@code null} means task play only one time
     * @param <R>        Task result type
     */
    public static <R> void when(
            @NotNull Observable<Boolean> observable, @NotNull Task<Boolean, R> task, int id,
            @Nullable Combiner<R, Integer, Boolean> repeat)
    {
        ConditionChecker.when(observable, Condition.IS_TRUE, task, id, repeat);
    }

    /**
     * Call a {@link Task} when an {@link Observable} embed value become {@code true}.<br>
     * The task is called only one time
     *
     * @param observable {@link Observable} checked
     * @param task       {@link Task} task to do
     */
    public static void when(@NotNull Observable<Boolean> observable, @NotNull ConsumerTask<Boolean> task)
    {
        ConditionChecker.when(observable, Condition.IS_TRUE, task, 0, null);
    }

    /**
     * Observable to follow
     */
    private final Observable<T> observable;
    /**
     * Condition to validate
     */
    private final Condition<T> condition;
    /**
     * Task to play
     */
    private final Task<T, R> task;
    /**
     * Condition checker ID
     */
    private final int id;
    /**
     * Decide if repeat or not
     */
    private final Combiner<R, Integer, Boolean> repeat;
    /**
     * Indicates if condition checker still alive
     */
    private final AtomicBoolean alive = new AtomicBoolean(true);

    /**
     * Create a condition checker
     *
     * @param observable {@link Observable} checked
     * @param condition  {@link Condition} to validate
     * @param task       {@link Task} task to do
     * @param id         Condition checker ID, used by "repeat" to identify this condition checker
     * @param repeat     Decide if the condition have to checked again after a play. {@code null} means task play once
     */
    private ConditionChecker(
            final Observable<T> observable, final Condition<T> condition, final Task<T, R> task, final int id,
            final Combiner<R, Integer, Boolean> repeat)
    {
        this.observable = observable;
        this.condition = condition;
        this.task = task;
        this.id = id;
        this.repeat = repeat;
    }

    /**
     * Called when the following observable value change
     *
     * @param observable {@link Observable} that value changed
     * @param value      New value
     */
    public void valueChanged(final Observable<T> observable, T value)
    {
        synchronized (this.alive)
        {
            if (this.alive.get() && this.condition.isValid(value))
            {
                R result = null;

                try
                {
                    result = this.task.playTask(value);
                    this.task.taskResult(result);
                }
                catch (Throwable throwable)
                {
                    this.task.taskError(new TaskException("Task failed", throwable));
                }

                if (this.repeat == null || !this.repeat.combine(result, this.id))
                {
                    this.alive.set(false);
                    this.observable.endObserve(this);
                }
            }
        }
    }
}

package jhelp.util.data;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.Objects;
import jhelp.util.list.ArrayObject;
import jhelp.util.thread.Task;
import jhelp.util.thread.ConsumerTask;

/**
 * Value that can be observe its changes.<br>
 * It is possible to override {@link #valueIsValid(Object)} to accept only some values.
 */
public class Observable<T>
{
    /**
     * Observed value
     */
    private       T                        data;
    /**
     * Observers to alert on change
     */
    private final ArrayObject<Observer<T>> observers;

    /**
     * Create the observable
     *
     * @param data Initial value
     */
    public Observable(@NotNull T data)
    {
        Objects.requireNonNull(data);
        this.data = data;
        this.observers = new ArrayObject<>();
    }

    /**
     * Register an observer of value changes.<br>
     * The observer will immediately receive the current value
     *
     * @param observer Observer to register
     * @return This observable instance, convenient for chaining
     */
    public final Observable<T> startObserve(@NotNull Observer<T> observer)
    {
        if (observer == null)
        {
            return this;
        }

        synchronized (this.observers)
        {
            if (!this.observers.contains(observer))
            {
                this.observers.add(observer);
            }
        }

        observer.valueChanged(this, this.data);
        return this;
    }

    /**
     * Unregister an observer to watch the changes
     *
     * @param observer Observer to unregister
     * @return This observable instance, convenient for chaining
     */
    public final Observable<T> endObserve(@NotNull Observer<T> observer)
    {
        synchronized (this.observers)
        {
            this.observers.remove(observer);
        }

        return this;
    }

    /**
     * Current value
     *
     * @return Current value
     */
    public final @NotNull T value()
    {
        return this.data;
    }

    /**
     * Check, before set the value, if given value is accepted.<br>
     * By default it accepts any value.
     *
     * @param value Value to test
     * @return {@code true} if given value is valid
     */
    protected boolean valueIsValid(@NotNull T value)
    {
        return true;
    }

    /**
     * Modify, if possible, the value.<br>
     * The modification can be refused if the given value is not valid.<br>
     * The returned boolean indicates if a real change happen. It occurs if and only if given value is valid and not equals to current value.
     *
     * @param value New value to set
     * @return {@code true} if value really change
     */
    public final boolean value(@NotNull T value)
    {
        Objects.requireNonNull(value);

        synchronized (this.observers)
        {
            if (this.valueIsValid(value) && !this.data.equals(value))
            {
                this.data = value;
                this.observers.forEach((ConsumerTask<Observer<T>>) observer -> observer.valueChanged(this, value));
                return true;
            }
        }

        return false;
    }

    /**
     * Create an observable that change value if this observable validate or not a given condition.<br>
     * The returned observable is automatically updated when this observable value change
     *
     * @param condition Condition to validate
     * @return Observable on result of full fill condition
     */
    public final @NotNull Observable<Boolean> validate(@NotNull Condition<T> condition)
    {
        Objects.requireNonNull(condition, "condition");
        return new ObservableCondition<>(this, condition);
    }

    /**
     * Create an observable result of this validate a given condition and an other observable is {@code true}
     *
     * @param condition  Condition to validate
     * @param observable Other observable to combine with
     * @return Observable on the combination result
     */
    public final @NotNull Observable<Boolean> and(
            @NotNull Condition<T> condition, @NotNull Observable<Boolean> observable)
    {
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(observable, "observable");
        return new ObservableAnd(this.validate(condition), observable);
    }

    /**
     * Create an observable result of this validate a given condition AND an other observable respects an other condition
     *
     * @param condition      Condition to validate
     * @param observable     Other observable to combine with
     * @param conditionOther Condition that other observable have to full fill
     * @param <T1>           Other observable data type
     * @return Observable on the combination result
     */
    public final @NotNull <T1> Observable<Boolean> and(
            @NotNull Condition<T> condition, @NotNull Observable<T1> observable, @NotNull Condition<T1> conditionOther)
    {
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(observable, "observable");
        Objects.requireNonNull(conditionOther, "conditionOther MUST NOT be null!");
        return new ObservableAnd(this.validate(condition), observable.validate(conditionOther));
    }

    /**
     * Create an observable that be {@code true} if this observable match all given conditions
     *
     * @param condition  First condition
     * @param conditions Other conditions
     * @return Created observable
     */
    public final @NotNull Observable<Boolean> and(@NotNull Condition<T> condition, @NotNull Condition<T>... conditions)
    {
        Objects.requireNonNull(condition, "condition1 MUST NOT be null!");
        Objects.requireNonNull(conditions, "conditions MUST NOT be null!");
        return this.validate(Condition.and(condition, conditions));
    }

    /**
     * Create an observable that be {@code true} if this observable match at least one of given conditions
     *
     * @param condition  First condition
     * @param conditions Other conditions
     * @return Created observable
     */
    public final @NotNull Observable<Boolean> or(@NotNull Condition<T> condition, @NotNull Condition<T>... conditions)
    {
        Objects.requireNonNull(condition, "condition1 MUST NOT be null!");
        Objects.requireNonNull(conditions, "conditions MUST NOT be null!");
        return this.validate(Condition.or(condition, conditions));
    }

    /**
     * Create an observable result of this validate a given condition OR an other observable respects an other condition
     *
     * @param condition      Condition to validate
     * @param observable     Other observable to combine with
     * @param conditionOther Condition that other observable have to full fill
     * @param <T1>           Other observable data type
     * @return Observable on the combination result
     */
    public final @NotNull <T1> Observable<Boolean> or(
            @NotNull Condition<T> condition, @NotNull Observable<T1> observable, @NotNull Condition<T1> conditionOther)
    {
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(observable, "observable");
        Objects.requireNonNull(conditionOther, "conditionOther MUST NOT be null!");
        return new ObservableOr(this.validate(condition), observable.validate(conditionOther));
    }

    /**
     * Create an observable result of this validate a given condition or an other observable is {@code true}
     *
     * @param condition  Condition to validate
     * @param observable Other observable to combine with
     * @return Observable on the combination result
     */
    public final @NotNull Observable<Boolean> or(
            @NotNull Condition<T> condition, @NotNull Observable<Boolean> observable)
    {
        Objects.requireNonNull(condition, "condition");
        Objects.requireNonNull(observable, "observable");
        return new ObservableOr(this.validate(condition), observable);
    }

    /**
     * Create an observable that change value if this observable invalidate or not a given condition.<br>
     * The returned observable is automatically updated when this observable value change
     *
     * @param condition Condition to invalidate
     * @return Observable on result of not validate condition
     */
    public final @NotNull Observable<Boolean> not(@NotNull Condition<T> condition)
    {
        Objects.requireNonNull(condition, "condition");
        return this.validate(condition.negate());
    }

    /**
     * Call a {@link Task} when this {@link Observable} full fill a {@link Condition}.<br>
     * The given "repeat" decide, when the task just played, if have to play it again. If the returned boolean is {@code true},
     * the task will be recalled next time the condition is valid.<br>
     * Note: if "repeat" is {@code null}, the task will be played only one time.
     *
     * @param condition {@link Condition} to validate
     * @param task      {@link Task} to do
     * @param id        Condition checker ID, used by "repeat" to identify this condition checker
     * @param repeat    Decide if the condition have to checked again after a play. {@code null} means task play only one time
     * @param <R>       Task result type
     * @return This observable instance, convenient for chaining
     */
    public final <R> Observable<T> when(
            @NotNull Condition<T> condition, @NotNull Task<T, R> task, int id,
            @Nullable Combiner<R, Integer, Boolean> repeat)
    {
        Objects.requireNonNull(condition, "condition MUST NOT be null!");
        Objects.requireNonNull(task, "task MUST NOT be null!");
        ConditionChecker.when(this, condition, task, id, repeat);
        return this;
    }

    /**
     * Call a {@link Task} when this {@link Observable} full fill a {@link Condition}.<br>
     * The task is called only one time
     *
     * @param condition {@link Condition} to validate
     * @param task      {@link Task} task to do
     * @return This observable instance, convenient for chaining
     */
    public final Observable<T> when(@NotNull Condition<T> condition, @NotNull ConsumerTask<T> task)
    {
        Objects.requireNonNull(condition, "condition MUST NOT be null!");
        Objects.requireNonNull(task, "task MUST NOT be null!");
        ConditionChecker.when(this, condition, task);
        return this;
    }

    /**
     * Call a {@link Task} when this {@link Observable} full fill a {@link Condition}.<br>
     * The task is called each time condition full fill
     *
     * @param condition {@link Condition} to validate
     * @param task      {@link Task} task to do
     * @return This observable instance, convenient for chaining
     */
    public final Observable<T> eachTime(@NotNull Condition<T> condition, @NotNull ConsumerTask<T> task)
    {
        Objects.requireNonNull(condition, "condition MUST NOT be null!");
        Objects.requireNonNull(task, "task MUST NOT be null!");
        ConditionChecker.eachTime(this, condition, task);
        return this;
    }

    /**
     * Combine this observable with an other one
     *
     * @param observable Observable to combine with
     * @param combiner   Describe how transform the value of this observable and given one to obtain the value of returned observable
     * @param <T1>       Observable to combine with data type
     * @param <T2>       Returned observable data type
     * @return Observable combination of this observable and given one
     */
    public final @NotNull <T1, T2> Observable<T2> combine(
            @NotNull Observable<T1> observable, @NotNull Combiner<T, T1, T2> combiner)
    {
        Objects.requireNonNull(observable, "observable MUST NOT be null!");
        Objects.requireNonNull(combiner, "combiner MUST NOT be null!");
        return new ObservableBinary<>(this, observable, combiner);
    }

    /**
     * Apply a task on this observable and create on observable of the result.<br>
     * Each time this observable change, the task is play to compute the result observable value
     *
     * @param task Task for change the value
     * @param <T1> New observable data type
     * @return Mapped observable
     */
    public final @NotNull <T1> Observable<T1> map(final @NotNull Task<T, T1> task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        final Observable<T1> observable = new Observable<>(task.playTask(this.value()));
        this.startObserve((ignored, value) -> observable.value(task.playTask(value)));
        return observable;
    }

    /**
     * Apply a task on this observable and create on observable of the result.<br>
     * Each time this observable change, the task is play to compute the result observable value
     *
     * @param task Task for change the value
     * @param <T1> New observable data type
     * @return Mapped observable
     */
    public final @NotNull <T1> Observable<T1> flatMap(final @NotNull Task<T, Observable<T1>> task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        final Observable<T1> observable = new Observable<>(task.playTask(this.value()).value());
        this.startObserve((ignored, value) -> observable.value(task.playTask(value).value()));
        return observable;
    }
}

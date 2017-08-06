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

package jhelp.util.data;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.Objects;
import jhelp.util.math.Math2;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.Task;

/**
 * Interval of numbers
 */
public final class Interval<N extends Number>
{
    /**
     * Maxiimum
     */
    private final double maximum;
    /**
     * Minimum
     */
    private final double minimum;

    /**
     * Create interval
     *
     * @param number1 First interval limit
     * @param number2 Second interval limit
     */
    public Interval(@NotNull N number1, @NotNull N number2)
    {
        Objects.requireNonNull(number1, "number1 MUST NOT be null!");
        Objects.requireNonNull(number2, "number2 MUST NOT be null!");
        final double value1 = number1.doubleValue();
        final double value2 = number2.doubleValue();
        this.minimum = Math.min(value1, value2);
        this.maximum = Math.max(value1, value2);
    }

    /**
     * Call a task when an observable enter inside the interval.<br>
     * Task will be called each time observable enter
     *
     * @param observable Observable to track
     * @param task       Task to do if enter in interval
     */
    public final void eachTimeInside(@NotNull Observable<N> observable, @NotNull ConsumerTask<N> task)
    {
        Objects.requireNonNull(observable, "observable MUST NOT be null!");
        Objects.requireNonNull(task, "task MUST NOT be null!");
        observable.eachTime(Condition.insideInterval(this), task);
    }

    /**
     * Call a task when an observable exit outside the interval.<br>
     * Task will be called each time observable enter
     *
     * @param observable Observable to track
     * @param task       Task to do if exit from interval
     */
    public final void eachTimeOutside(@NotNull Observable<N> observable, @NotNull ConsumerTask<N> task)
    {
        Objects.requireNonNull(observable, "observable MUST NOT be null!");
        Objects.requireNonNull(task, "task MUST NOT be null!");
        observable.eachTime(Condition.outsideInterval(this), task);
    }

    /**
     * Indicates if a number inside the interval
     *
     * @param number Tested number
     * @return {@code true} if number inside interval
     */
    public boolean inside(@NotNull N number)
    {
        Objects.requireNonNull(number, "number MUST NOT be null!");
        final double value = number.doubleValue();
        return Math2.compare(value, this.minimum) >= 0 && Math2.compare(value, this.maximum) <= 0;
    }

    /**
     * Create an observable based on condition that an observable of a number inside an interval.<br>
     * That is to say, the created observable is {@code true} when the number, carry by given observable,
     * inside the interval.<br>
     * The created observable is {@code false} otherwise<br>
     *
     * @param observable Observable of the value to check
     * @return Created observable
     */
    public @NotNull Observable<Boolean> inside(@NotNull Observable<N> observable)
    {
        Objects.requireNonNull(observable, "observable MUST NOT be null!");
        return observable.validate(Condition.insideInterval(this));
    }

    /**
     * Create an observable based on condition that an observable of a number outside an interval.<br>
     * That is to say, the created observable is {@code true} when the number, carry by given observable,
     * outside the interval.<br>
     * The created observable is {@code false} otherwise<br>
     *
     * @param observable Observable of the value to check
     * @return Created observable
     */
    public @NotNull Observable<Boolean> outside(@NotNull Observable<N> observable)
    {
        Objects.requireNonNull(observable, "observable MUST NOT be null!");
        return observable.validate(Condition.outsideInterval(this));
    }

    /**
     * Call a task when an observable enter inside the interval.<br>
     * Task will be called only one time
     *
     * @param observable Observable to track
     * @param task       Task to do if enter in interval
     */
    public final void whenInside(Observable<N> observable, @NotNull ConsumerTask<N> task)
    {
        Objects.requireNonNull(observable, "observable MUST NOT be null!");
        Objects.requireNonNull(task, "task MUST NOT be null!");
        observable.when(Condition.insideInterval(this), task);
    }

    /**
     * Call a task when an observable enter inside the interval
     *
     * @param observable Observable to track
     * @param task       Task to do if enter in interval
     * @param id         Task ID, used by "repeat"
     * @param repeat     Decide if have to do the task again or not. If {@code null} the task is play once
     * @param <R>        Task return type
     */
    public final <R> void whenInside(
            @NotNull Observable<N> observable, @NotNull Task<N, R> task, int id,
            @Nullable Combiner<R, Integer, Boolean> repeat)
    {
        Objects.requireNonNull(observable, "observable MUST NOT be null!");
        Objects.requireNonNull(task, "task MUST NOT be null!");
        observable.when(Condition.insideInterval(this), task, id, repeat);
    }

    /**
     * Call a task when an observable exit outside the interval
     *
     * @param observable Observable to track
     * @param task       Task to do if exit from interval
     * @param id         Task ID, used by "repeat"
     * @param repeat     Decide if have to do the task again or not. If {@code null} the task is play once
     * @param <R>        Task return type
     */
    public final <R> void whenOutside(
            @NotNull Observable<N> observable, @NotNull Task<N, R> task, int id,
            @Nullable Combiner<R, Integer, Boolean> repeat)
    {
        Objects.requireNonNull(observable, "observable MUST NOT be null!");
        Objects.requireNonNull(task, "task MUST NOT be null!");
        observable.when(Condition.outsideInterval(this), task, id, repeat);
    }

    /**
     * Call a task when an observable exit outside the interval.<br>
     * Task will be called only one time
     *
     * @param observable Observable to track
     * @param task       Task to do if exit from interval
     */
    public final void whenOutside(@NotNull Observable<N> observable, @NotNull ConsumerTask<N> task)
    {
        Objects.requireNonNull(observable, "observable MUST NOT be null!");
        Objects.requireNonNull(task, "task MUST NOT be null!");
        observable.when(Condition.outsideInterval(this), task);
    }
}

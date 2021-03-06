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
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Observable that the value is the combination of two other observables.<br>
 * The value change each time one of two observable value change
 */
public class ObservableBinary<T1, T2, T3> extends Observable<T3>
{
    /**
     * Indicates if change is currently valid to avoid external changes
     */
    private final AtomicBoolean changeIsValid = new AtomicBoolean(false);
    /**
     * Describe how combine the two values to make this value
     */
    private final Combiner<T1, T2, T3> combiner;
    /**
     * First observable
     */
    private final Observable<T1>       observable1;
    /**
     * Second observable
     */
    private final Observable<T2>       observable2;

    /**
     * Create the observable
     *
     * @param observable1 First observable
     * @param observable2 Second observable
     * @param combiner    Describe how combine the two values to make this value
     */
    public ObservableBinary(
            @NotNull final Observable<T1> observable1, @NotNull final Observable<T2> observable2,
            @NotNull final Combiner<T1, T2, T3> combiner)
    {
        super(combiner.combine(observable1.value(), observable2.value()));
        this.observable1 = observable1;
        this.observable2 = observable2;
        this.combiner = combiner;
        this.observable1.startObserve(this::update);
        this.observable2.startObserve(this::update);
    }

    /**
     * Called when one of watched observable changed
     *
     * @param observable Changed observable
     * @param value      Observable value
     * @param <T>        Value type
     */
    private <T> void update(@NotNull Observable<T> observable, @NotNull T value)
    {
        synchronized (this.changeIsValid)
        {
            this.changeIsValid.set(true);
            this.value(this.combiner.combine(this.observable1.value(), this.observable2.value()));
            this.changeIsValid.set(false);
        }
    }

    /**
     * Indicates if a value is valid
     *
     * @param value Value to test
     * @return {@code true} if value is valid
     */
    @Override
    protected final boolean valueIsValid(@NotNull final T3 value)
    {
        return this.changeIsValid.get();
    }
}

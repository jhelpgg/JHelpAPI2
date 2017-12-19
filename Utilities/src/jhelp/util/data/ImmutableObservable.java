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

import java.util.concurrent.atomic.AtomicBoolean;

public final class ImmutableObservable<T> extends Observable<T>
{
    private final AtomicBoolean changeAllowed;

    public ImmutableObservable(Observable<T> observable)
    {
        super(observable.value());
        this.changeAllowed = new AtomicBoolean(false);
        observable.startObserve(this::internalChange);
    }

    private void internalChange(Observable<T> observable, T value)
    {
        synchronized (this.changeAllowed)
        {
            this.changeAllowed.set(true);
            this.value(value);
            this.changeAllowed.set(false);
        }
    }

    @Override
    protected boolean valueIsValid(final T value)
    {
        synchronized (this.changeAllowed)
        {
            return this.changeAllowed.get();
        }
    }
}

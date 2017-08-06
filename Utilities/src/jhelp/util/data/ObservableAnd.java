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

/**
 * Observable that be true if and only if two other observable are true in same time
 */
public final class ObservableAnd extends ObservableBinary<Boolean, Boolean, Boolean>
{
    /**
     * Create the observable
     *
     * @param observable1 First observable
     * @param observable2 Second observable
     */
    public ObservableAnd(
            @NotNull final Observable<Boolean> observable1, @NotNull final Observable<Boolean> observable2)
    {
        super(observable1, observable2, Combiner.AND);
    }
}

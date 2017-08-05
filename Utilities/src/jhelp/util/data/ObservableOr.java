package jhelp.util.data;

import com.sun.istack.internal.NotNull;

/**
 * Observable that be true if and only if at least one of two other observable is true
 */
public final class ObservableOr extends ObservableBinary<Boolean, Boolean, Boolean>
{
    /**
     * Create the observable
     *
     * @param observable1 First observable
     * @param observable2 Second observable
     */
    public ObservableOr(@NotNull final Observable<Boolean> observable1, @NotNull final Observable<Boolean> observable2)
    {
        super(observable1, observable2, Combiner.OR);
    }
}

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
            @NotNull final Observable<Boolean> observable1, @NotNull  final Observable<Boolean> observable2)
    {
        super(observable1, observable2, Combiner.AND);
    }
}

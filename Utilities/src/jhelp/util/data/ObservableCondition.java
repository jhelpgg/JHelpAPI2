package jhelp.util.data;

import com.sun.istack.internal.NotNull;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Observable that track if an observable validate a condition
 */
public final class ObservableCondition<T> extends Observable<Boolean>
{
    /**
     * Indicates if change is currently valid to avoid external changes
     */
    private final AtomicBoolean changeIsValid = new AtomicBoolean(false);
    /**
     * Condition to validate
     */
    private final Condition<T> condition;

    /**
     * Create the observable
     *
     * @param observable Observable to watch
     * @param condition  Condition to validate
     */
    public ObservableCondition(@NotNull Observable<T> observable, @NotNull Condition<T> condition)
    {
        super(condition.isValid(observable.value()));
        this.condition = condition;
        observable.startObserve(this::update);
    }

    /**
     * Indicates if a value is valid
     *
     * @param value Value to test
     * @return {@code true} if value is valid
     */
    @Override
    protected final boolean valueIsValid(final Boolean value)
    {
        return this.changeIsValid.get();
    }

    /**
     * Called when watched observable changed
     *
     * @param observable Changed observable
     * @param value      Observable value
     */
    private void update(@NotNull Observable<T> observable, @NotNull T value)
    {
        this.changeIsValid.set(true);
        this.value(this.condition.isValid(value));
        this.changeIsValid.set(false);
    }
}

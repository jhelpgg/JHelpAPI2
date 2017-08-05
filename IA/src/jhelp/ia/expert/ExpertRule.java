package jhelp.ia.expert;

import com.sun.istack.internal.NotNull;
import java.util.Objects;
import jhelp.util.data.Condition;
import jhelp.util.data.Observable;

/**
 * Created by jhelp on 01/07/17.
 */
public abstract class ExpertRule<P, R>
{
    private final Observable<P> observable;
    private final Condition<P>  condition;

    public ExpertRule(@NotNull Observable<P> observable, @NotNull Condition<P> condition)
    {
        Objects.requireNonNull(observable, "observable MUST NOT be null!");
        Objects.requireNonNull(condition, "condition MUST NOT be null!");
        this.observable = observable;
        this.condition = condition;
    }

    public final boolean applicable()
    {
        return this.condition.isValid(this.observable.value());
    }

    public final R apply()
    {
        if (!this.applicable())
        {
            throw new IllegalStateException("Rule is not applicable");
        }

        return this.apply(this.observable.value());
    }

    protected abstract R apply(P value);
}

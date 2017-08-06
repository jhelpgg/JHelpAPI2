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
    private final Condition<P>  condition;
    private final Observable<P> observable;

    public ExpertRule(@NotNull Observable<P> observable, @NotNull Condition<P> condition)
    {
        Objects.requireNonNull(observable, "observable MUST NOT be null!");
        Objects.requireNonNull(condition, "condition MUST NOT be null!");
        this.observable = observable;
        this.condition = condition;
    }

    protected abstract R apply(P value);

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
}

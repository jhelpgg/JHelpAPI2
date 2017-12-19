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

package jhelp.ia.solver;

import com.sun.istack.internal.NotNull;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Created by jhelp on 24/06/17.
 */
public class BestNode<T> extends AndNode<T>
{
    private final Comparator<T> comparator;

    public BestNode(@NotNull Comparator<T> comparator)
    {
        Objects.requireNonNull(comparator, "comparator MUST NOT be null!");
        this.comparator = comparator;
    }

    @Override
    protected T combine(final List<T> intermediates)
    {
        T best = intermediates.get(0);
        T value;

        for (int index = intermediates.size() - 1; index > 0; index--)
        {
            value = intermediates.get(index);

            if (this.comparator.compare(value, best) > 0)
            {
                best = value;
            }
        }

        return best;
    }
}

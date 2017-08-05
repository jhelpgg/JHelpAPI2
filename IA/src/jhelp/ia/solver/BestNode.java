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

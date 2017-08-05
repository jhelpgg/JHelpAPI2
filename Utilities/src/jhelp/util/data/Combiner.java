package jhelp.util.data;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.Optional;
import jhelp.util.thread.Filter;

/**
 * Combine two parameter to produce a third one
 *
 * @param <P1> First parameter type
 * @param <P2> Second parameter type
 * @param <R>  Result type
 */
public interface Combiner<P1, P2, R>
{
    /**
     * Combiner returns always {@code true}
     */
    Combiner<?, ?, Boolean> TRUE = (p1, p2) -> true;

    /**
     * Combiner returns always {@code true}
     *
     * @param <P1> First combiner parameter type
     * @param <P2> Second combiner parameter type
     * @return The combiner always {@code true}
     */
    static @NotNull <P1, P2> Combiner<P1, P2, Boolean> TRUE()
    {
        return (Combiner<P1, P2, Boolean>) Combiner.TRUE;
    }

    /**
     * Combiner returns always {@code false}
     */
    Combiner<?, ?, Boolean> FALSE = (p1, p2) -> false;

    /**
     * Combiner returns always {@code false}
     *
     * @param <P1> First combiner parameter type
     * @param <P2> Second combiner parameter type
     * @return The combiner always {@code false}
     */
    static @NotNull <P1, P2> Combiner<P1, P2, Boolean> FALSE()
    {
        return (Combiner<P1, P2, Boolean>) Combiner.FALSE;
    }

    /**
     * Combiner that make <b>and</b> on two values
     */
    Combiner<Boolean, Boolean, Boolean> AND = (p1, p2) -> p1 && p2;
    /**
     * Combiner that make <b>or</b> on two values
     */
    Combiner<Boolean, Boolean, Boolean> OR  = (p1, p2) -> p1 || p2;

    /**
     * Create a combiner that fill an optional with a value if a boolean is {@code true}
     *
     * @param <P> Value type
     * @return Created combiner
     */
    static @NotNull <P> Combiner<Boolean, P, Optional<P>> IF()
    {
        return (condition, value) ->
        {
            if (condition)
            {
                return Optional.ofNullable(value);
            }

            return Optional.empty();
        };
    }

    /**
     * Create a combiner that fill an optional with a value if a condition on parameter is full fill
     *
     * @param <P> Value type
     * @return Created combiner
     */
    static @NotNull <P> Combiner<Filter<P>, P, Optional<P>> ifCondtion()
    {
        return (condition, value) ->
        {
            if (condition.isFiltered(value))
            {
                return Optional.ofNullable(value);
            }

            return Optional.empty();
        };
    }

    /**
     * Combine two parameters to produce a third one.
     *
     * @param parameter1 First parameter.
     * @param parameter2 Second parameter.
     * @return Produced result.
     */
    @Nullable R combine(@Nullable P1 parameter1, @Nullable P2 parameter2);
}

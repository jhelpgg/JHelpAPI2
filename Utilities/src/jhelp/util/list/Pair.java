package jhelp.util.list;

import com.sun.istack.internal.Nullable;
import java.util.Objects;

/**
 * A pair of element
 */
public final class Pair<T1, T2>
{
    /**
     * First element
     */
    public final @Nullable
    T1 first;
    /**
     * Second element
     */
    public final @Nullable
    T2 second;

    /**
     * Create pair
     *
     * @param first  First element
     * @param second Second element
     */
    public Pair(final @Nullable T1 first, @Nullable final T2 second)
    {
        this.first = first;
        this.second = second;
    }

    /**
     * String representation
     *
     * @return String representation
     */
    @Override
    public String toString()
    {
        return this.first + ":" + this.second;
    }

    /**
     * Indicates if given object equals to this pair
     *
     * @param object Object to compare
     * @return {@code true} if given object is equals
     */
    @Override
    public boolean equals(final @Nullable Object object)
    {
        if (this == object)
        {
            return true;
        }

        if (null == object)
        {
            return false;
        }

        try
        {
            Pair<T1, T2> pair = (Pair<T1, T2>) object;
            return Objects.equals(this.first, pair.first) && Objects.equals(this.second, pair.second);
        }
        catch (Exception exception)
        {
            return false;
        }
    }

    /**
     * Hash code
     *
     * @return Hash code
     */
    @Override
    public int hashCode()
    {
        return this.first.hashCode() * 31 + this.second.hashCode();
    }
}

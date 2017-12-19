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
    public @Nullable
    T1 first;
    /**
     * Second element
     */
    public @Nullable
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
     * Hash code
     *
     * @return Hash code
     */
    @Override
    public int hashCode()
    {
        return this.first.hashCode() * 31 + this.second.hashCode();
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
     * String representation
     *
     * @return String representation
     */
    @Override
    public String toString()
    {
        return this.first + ":" + this.second;
    }
}

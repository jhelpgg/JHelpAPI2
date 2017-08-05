package jhelp.util.thread;

import com.sun.istack.internal.Nullable;

/**
 * Filter on element
 */
public interface Filter<T>
{
    /**
     * Indicates if given element is accepted
     *
     * @param element Element tested
     * @return {@code true} if given element is accepted
     */
    boolean isFiltered(@Nullable T element);

    default Filter<T> negate()
    {
        return element1 -> !this.isFiltered(element1);
    }
}

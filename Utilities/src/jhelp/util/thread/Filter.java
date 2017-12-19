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

    /**
     * Obtain filter that revert the filtering.<br>
     * In other words:
     * every things accepted by this filter will be refused by created filter, and
     * every things refused by this filter will be accepted by created filter.
     *
     * @return Opposite filter
     */
    default Filter<T> negate()
    {
        final Filter<T> parent = this;

        return new Filter<T>()
        {
            @Override
            public boolean isFiltered(final T element)
            {
                return !parent.isFiltered(element);
            }

            @Override
            public Filter<T> negate()
            {
                return parent;
            }
        };
    }
}

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

import java.util.Iterator;
import java.util.NoSuchElementException;

final class QueueIterator<T> implements Iterator<T>
{
    private QueueElement<T> current;

    QueueIterator(QueueElement<T> startElement)
    {
        this.current = startElement;
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext()
    {
        return this.current != null;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public T next()
    {
        if (this.current == null)
        {
            throw new NoSuchElementException("No more element!");
        }

        final T element = this.current.element;
        this.current = this.current.next;
        return element;
    }
}

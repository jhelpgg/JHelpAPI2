package jhelp.util.list;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * {@link Enumeration}, {@link Iterator} or array can be see as {@link Enumeration}, {@link Iterator} and {@link Iterable}<br>
 * Usefull when you have an {@link Enumeration}, {@link Iterator} or array and need see it as something else in {@link Enumeration}, {@link Iterator} and {@link Iterable}
 */
public final class EnumerationIterator<T> implements Iterable<T>, Enumeration<T>, Iterator<T>
{
    /**
     * Embed enumeration
     */
    private final Enumeration<T> enumeration;
    /**
     * Embed iterator
     */
    private final Iterator<T>    iterator;
    /**
     * Embed array
     */
    private final T[]            array;
    /**
     * Read index in array
     */
    private       int            index;
    /**
     * Indicates if remove is allowed
     */
    private final boolean        removeAllowed;

    /**
     * Create with an {@link Enumeration}
     *
     * @param enumeration Embed enumeration
     */
    public EnumerationIterator(@NotNull Enumeration<T> enumeration)
    {
        Objects.requireNonNull(enumeration, "enumeration MUST NOT be null");
        this.enumeration = enumeration;
        this.iterator = null;
        this.array = null;
        this.removeAllowed = false;
    }

    /**
     * Create with an {@link Iterator}
     *
     * @param iterator Embed iterator
     */
    public EnumerationIterator(@NotNull Iterator<T> iterator)
    {
        this(iterator, true);
    }

    /**
     * Create with an {@link Iterator}
     *
     * @param iterator      Embed iterator
     * @param removeAllowed Indicates if remove is delegate to iterator
     */
    public EnumerationIterator(@NotNull Iterator<T> iterator, boolean removeAllowed)
    {
        Objects.requireNonNull(iterator, "iterator MUST NOT be null");
        this.enumeration = null;
        this.iterator = iterator;
        this.array = null;
        this.removeAllowed = removeAllowed;
    }

    /**
     * Create with an array
     *
     * @param array Embed array
     */
    public EnumerationIterator(@NotNull T[] array)
    {
        Objects.requireNonNull(array, "array MUST NOT be null");
        this.enumeration = null;
        this.iterator = null;
        this.array = array;
        this.index = 0;
        this.removeAllowed = false;
    }

    /**
     * Tests if this enumeration contains more elements.
     *
     * @return <code>true</code> if and only if this enumeration object
     * contains at least one more element to provide;
     * <code>false</code> otherwise.
     */
    @Override
    public boolean hasMoreElements()
    {
        if (this.enumeration != null)
        {
            return this.enumeration.hasMoreElements();
        }

        if (this.iterator != null)
        {
            return this.iterator.hasNext();
        }

        return this.index < this.array.length;
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
        return this.hasMoreElements();
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public @Nullable T next()
    {
        if (this.enumeration != null)
        {
            return this.enumeration.nextElement();
        }

        if (this.iterator != null)
        {
            return this.iterator.next();
        }

        if (this.index >= this.array.length)
        {
            throw new NoSuchElementException("No next element");
        }

        T value = this.array[this.index];
        this.index++;
        return value;
    }

    /**
     * Removes from the underlying collection the last element returned
     * by this iterator (optional operation).  This method can be called
     * only once per call to {@link #next}.  The behavior of an iterator
     * is unspecified if the underlying collection is modified while the
     * iteration is in progress in any way other than by calling this
     * method.
     *
     * @throws UnsupportedOperationException if the {@code remove}
     *                                       operation is not supported by this iterator
     * @throws IllegalStateException         if the {@code next} method has not
     *                                       yet been called, or the {@code remove} method has already
     *                                       been called after the last call to the {@code next}
     *                                       method
     * @implSpec The default implementation throws an instance of
     * {@link UnsupportedOperationException} and performs no other action.
     */
    @Override
    public void remove()
    {
        if (!this.removeAllowed)
        {
            throw new UnsupportedOperationException("Remove not allowed!");
        }

        //"removeAllowed" is true only if initialized with iterator, so its sure that "iterator" is not null
        this.iterator.remove();
    }

    /**
     * Returns the next element of this enumeration if this enumeration
     * object has at least one more element to provide.
     *
     * @return the next element of this enumeration.
     * @throws java.util.NoSuchElementException if no more elements exist.
     */
    @Override
    public @Nullable T nextElement()
    {
        return this.next();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public @NotNull Iterator<T> iterator()
    {
        return this;
    }
}

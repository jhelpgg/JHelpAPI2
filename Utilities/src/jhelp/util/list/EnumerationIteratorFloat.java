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

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * {@link Enumeration}, {@link Iterator} or array can be see as {@link Enumeration}, {@link Iterator} and {@link Iterable}<br>
 * Usefull when you have an {@link Enumeration}, {@link Iterator} or array and need see it as something else in {@link Enumeration}, {@link Iterator} and {@link Iterable}
 */
public final class EnumerationIteratorFloat
        implements Iterable<Float>, Enumeration<Float>, Iterator<Float>
{
    /**
     * Embed array
     */
    private       float[]            array;
    /**
     * Embed enumeration
     */
    private final Enumeration<Float> enumeration;
    /**
     * Read index in array
     */
    private       int                index;
    /**
     * Embed iterator
     */
    private final Iterator<Float>    iterator;
    /**
     * Indicates if remove is allowed
     */
    private final boolean            removeAllowed;

    /**
     * Create with an {@link Enumeration}
     *
     * @param enumeration Embed enumeration
     */
    public EnumerationIteratorFloat(@NotNull Enumeration<Float> enumeration)
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
    public EnumerationIteratorFloat(@NotNull Iterator<Float> iterator)
    {
        this(iterator, true);
    }

    /**
     * Create with an {@link Iterator}
     *
     * @param iterator      Embed iterator
     * @param removeAllowed Indicates if remove is delegate to iterator
     */
    public EnumerationIteratorFloat(@NotNull Iterator<Float> iterator, boolean removeAllowed)
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
    public EnumerationIteratorFloat(@NotNull float[] array)
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
     * {@inheritDoc}
     *
     * @param action
     * @implSpec If the action is an instance of {@code FloatConsumer} then it is
     * cast to {@code FloatConsumer} and passed to
     * {@link #forEachRemaining}; otherwise the action is adapted to
     * an instance of {@code FloatConsumer}, by boxing the argument of
     * {@code FloatConsumer}, and then passed to
     * {@link #forEachRemaining}.
     */
    @Override
    public void forEachRemaining(final Consumer<? super Float> action)
    {
        this.forEach(action);
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
    public @Nullable Float next()
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

        float value = this.array[this.index];
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
     * @throws NoSuchElementException if no more elements exist.
     */
    @Override
    public @Nullable Float nextElement()
    {
        return this.next();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public @NotNull Iterator<Float> iterator()
    {
        return this;
    }

    /**
     * Performs the given action for each element of the {@code Iterable}
     * until all elements have been processed or the action throws an
     * exception.  Unless otherwise specified by the implementing class,
     * actions are performed in the order of iteration (if an iteration order
     * is specified).  Exceptions thrown by the action are relayed to the
     * caller.
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     * @implSpec <p>The default implementation behaves as if:
     * <pre>{@code
     *     for (T t : this)
     *         action.accept(t);
     * }</pre>
     * @since 1.8
     */
    @Override
    public void forEach(final Consumer<? super Float> action)
    {
        ForEach.forEach((Iterable<Float>) this, action::accept);
    }

    /**
     * Returns the next {@code float} element in the iteration.
     *
     * @return the next {@code float} element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    public float nextFloat()
    {
        return this.next();
    }
}

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
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import jhelp.util.thread.ThreadManager;

/**
 * {@link Enumeration}, {@link Iterator} or array can be see as {@link Enumeration}, {@link Iterator} and {@link Iterable}<br>
 * Usefull when you have an {@link Enumeration}, {@link Iterator} or array and need see it as something else in {@link Enumeration}, {@link Iterator} and {@link Iterable}
 */
public final class EnumerationIteratorInt
        implements Iterable<Integer>, Enumeration<Integer>, Iterator<Integer>, PrimitiveIterator.OfInt,
                   Spliterator.OfInt
{
    /**
     * Embed array
     */
    private       int[]                array;
    /**
     * Embed enumeration
     */
    private final Enumeration<Integer> enumeration;
    /**
     * Read index in array
     */
    private       int                  index;
    /**
     * Embed iterator
     */
    private final Iterator<Integer>    iterator;
    /**
     * Indicates if remove is allowed
     */
    private final boolean              removeAllowed;

    /**
     * Create with an {@link Enumeration}
     *
     * @param enumeration Embed enumeration
     */
    public EnumerationIteratorInt(@NotNull Enumeration<Integer> enumeration)
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
    public EnumerationIteratorInt(@NotNull Iterator<Integer> iterator)
    {
        this(iterator, true);
    }

    /**
     * Create with an {@link Iterator}
     *
     * @param iterator      Embed iterator
     * @param removeAllowed Indicates if remove is delegate to iterator
     */
    public EnumerationIteratorInt(@NotNull Iterator<Integer> iterator, boolean removeAllowed)
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
    public EnumerationIteratorInt(@NotNull int[] array)
    {
        Objects.requireNonNull(array, "array MUST NOT be null");
        this.enumeration = null;
        this.iterator = null;
        this.array = array;
        this.index = 0;
        this.removeAllowed = false;
    }

    /**
     * Returns an estimate of the number of elements that would be
     * encountered by a {@link #forEachRemaining} traversal, or returns {@link
     * Long#MAX_VALUE} if infinite, unknown, or too expensive to compute.
     * <p>
     * <p>If this Spliterator is {@link #SIZED} and has not yet been partially
     * traversed or split, or this Spliterator is {@link #SUBSIZED} and has
     * not yet been partially traversed, this estimate must be an accurate
     * count of elements that would be encountered by a complete traversal.
     * Otherwise, this estimate may be arbitrarily inaccurate, but must decrease
     * as specified across invocations of {@link #trySplit}.
     *
     * @return the estimated size, or {@code Long.MAX_VALUE} if infinite,
     * unknown, or too expensive to compute.
     * @apiNote Even an inexact estimate is often useful and inexpensive to compute.
     * For example, a sub-spliterator of an approximately balanced binary tree
     * may return a value that estimates the number of elements to be half of
     * that of its parent; if the root Spliterator does not maintain an
     * accurate count, it could estimate size to be the power of two
     * corresponding to its maximum depth.
     */
    @Override
    public long estimateSize()
    {
        if (this.iterator != null)
        {
            return Long.MAX_VALUE;
        }

        if (this.enumeration != null)
        {
            return Long.MAX_VALUE;
        }

        return this.array.length - this.index;
    }

    /**
     * Returns a set of characteristics of this Spliterator and its
     * elements. The result is represented as ORed values from {@link
     * #ORDERED}, {@link #DISTINCT}, {@link #SORTED}, {@link #SIZED},
     * {@link #NONNULL}, {@link #IMMUTABLE}, {@link #CONCURRENT},
     * {@link #SUBSIZED}.  Repeated calls to {@code characteristics()} on
     * a given spliterator, prior to or in-between calls to {@code trySplit},
     * should always return the same result.
     * <p>
     * <p>If a Spliterator reports an inconsistent set of
     * characteristics (either those returned from a single invocation
     * or across multiple invocations), no guarantees can be made
     * about any computation using this Spliterator.
     *
     * @return a representation of characteristics
     * @apiNote The characteristics of a given spliterator before splitting
     * may differ from the characteristics after splitting.  For specific
     * examples see the characteristic values {@link #SIZED}, {@link #SUBSIZED}
     * and {@link #CONCURRENT}.
     */
    @Override
    public int characteristics()
    {
        int characteristics = Spliterator.ORDERED;

        if (!this.removeAllowed)
        {
            characteristics |= Spliterator.IMMUTABLE;
        }

        if (this.iterator == null && this.enumeration == null)
        {
            characteristics |= Spliterator.SIZED;
        }

        return characteristics;
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
     * @implSpec If the action is an instance of {@code IntConsumer} then it is
     * cast to {@code IntConsumer} and passed to
     * {@link #forEachRemaining}; otherwise the action is adapted to
     * an instance of {@code IntConsumer}, by boxing the argument of
     * {@code IntConsumer}, and then passed to
     * {@link #forEachRemaining}.
     */
    @Override
    public void forEachRemaining(final Consumer<? super Integer> action)
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
    public @Nullable Integer next()
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

        int value = this.array[this.index];
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
    public @Nullable Integer nextElement()
    {
        return this.next();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public @NotNull Iterator<Integer> iterator()
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
    public void forEach(final Consumer<? super Integer> action)
    {
        ForEach.forEach((Iterable<Integer>) this, action::accept);
    }

    /**
     * Creates a {@link Spliterator} over the elements described by this
     * {@code Iterable}.
     *
     * @return a {@code Spliterator} over the elements described by this
     * {@code Iterable}.
     * @implSpec The default implementation creates an
     * <em><a href="Spliterator.html#binding">early-binding</a></em>
     * spliterator from the iterable's {@code Iterator}.  The spliterator
     * inherits the <em>fail-fast</em> properties of the iterable's iterator.
     * @implNote The default implementation should usually be overridden.  The
     * spliterator returned by the default implementation has poor splitting
     * capabilities, is unsized, and does not report any spliterator
     * characteristics. Implementing classes can nearly always provide a
     * better implementation.
     * @since 1.8
     */
    @Override
    public Spliterator<Integer> spliterator()
    {
        return this;
    }

    /**
     * Returns the next {@code int} element in the iteration.
     *
     * @return the next {@code int} element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public int nextInt()
    {
        return this.next();
    }

    /**
     * Performs the given action for each remaining element until all elements
     * have been processed or the action throws an exception.  Actions are
     * performed in the order of iteration, if that order is specified.
     * Exceptions thrown by the action are relayed to the caller.
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     * @implSpec <p>The default implementation behaves as if:
     * <pre>{@code
     *     while (hasNext())
     *         action.accept(nextInt());
     * }</pre>
     */
    @Override
    public void forEachRemaining(final IntConsumer action)
    {
        ForEach.forEach((Iterable<Integer>) this, action::accept);
    }

    @Override
    public Spliterator.OfInt trySplit()
    {
        if (this.iterator != null)
        {
            return null;
        }

        if (this.enumeration != null)
        {
            return null;
        }

        final int left = this.array.length - this.index;

        if (left <= 1)
        {
            return null;
        }

        final int second = left >> 1;
        final int first  = left - second;

        final int[] array = new int[second];
        System.arraycopy(this.array, this.index + first, array, 0, second);
        final int[] buffer = new int[first];
        System.arraycopy(this.array, this.index, buffer, 0, first);
        this.array = buffer;
        this.index = 0;

        return new EnumerationIteratorInt(array);
    }

    @Override
    public boolean tryAdvance(final IntConsumer action)
    {
        if (this.hasNext())
        {
            ThreadManager.parallel(action::accept, this.nextInt());
            return true;
        }

        return false;
    }
}

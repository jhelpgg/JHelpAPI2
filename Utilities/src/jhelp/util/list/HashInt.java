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
import java.util.TreeSet;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.Filter;
import jhelp.util.thread.Future;
import jhelp.util.thread.Task;

/**
 * Hash table where keys are integers
 *
 * @param <T> Element stored type
 * @author JHelp
 */
public class HashInt<T> implements SizedIterable<T>, ParallelList<T, HashInt<T>>
{
    /**
     * Inside element
     *
     * @param <DATA> Data type
     * @author JHelp
     */
    static class Element<DATA>
            implements Comparable<Element<DATA>>
    {
        /**
         * Stored data
         */
        DATA data;
        /**
         * Element's hash
         */
        final int hash;

        /**
         * Create a new instance of Element
         *
         * @param hash Element's hash
         */
        Element(final int hash)
        {
            this.hash = hash;
        }

        /**
         * Create a new instance of Element
         *
         * @param hash Element's hash
         * @param data Stored data
         */
        Element(final int hash, final DATA data)
        {
            this.hash = hash;
            this.data = data;
        }

        /**
         * Compare with an other element.<br>
         * It returns:
         * <table border=0>
         * <tr>
         * <th>&lt;0</th>
         * <td>:</td>
         * <td>If this element is before given one</td>
         * </tr>
         * <tr>
         * <th>0</th>
         * <td>:</td>
         * <td>If this element and given one have the same place</td>
         * </tr>
         * <tr>
         * <th>&gt;0</th>
         * <td>:</td>
         * <td>If this element is after given one</td>
         * </tr>
         * </table>
         * <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param element Element to compare with
         * @return Comparison result
         * @see Comparable#compareTo(Object)
         */
        @Override
        public int compareTo(final Element<DATA> element)
        {
            return this.hash - element.hash;
        }

        /**
         * Element hash code <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @return Element hash code
         * @see Object#hashCode()
         */
        @Override
        public int hashCode()
        {
            return this.hash;
        }

        /**
         * Indicates if an object is equals to this element <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param object Object to compare with
         * @return {@code true} if the object is equals to the element
         * @see Object#equals(Object)
         */
        @Override
        public boolean equals(final Object object)
        {
            if (object == null)
            {
                return false;
            }

            if (object == this)
            {
                return true;
            }

            if (!(object instanceof Element))
            {
                return false;
            }

            return this.hash == ((Element<?>) object).hash;
        }

        /**
         * Element string representation <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @return Element string representation
         * @see Object#toString()
         */
        @Override
        public String toString()
        {
            return this.hash + ":" + this.data;
        }
    }

    /**
     * Iterator on hash table values
     *
     * @param <ELEMENT> Element type
     * @author JHelp
     */
    static class IteratorElement<ELEMENT>
            implements Iterator<ELEMENT>
    {
        /**
         * Iterator reference
         */
        private final Iterator<Element<ELEMENT>> iteratorBase;

        /**
         * Create a new instance of IteratorElement
         *
         * @param iteratorBase Iterator reference
         */
        IteratorElement(final Iterator<Element<ELEMENT>> iteratorBase)
        {
            this.iteratorBase = iteratorBase;
        }

        /**
         * Indicates if their are on more element in the iterator <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @return {@code true} if their are one more element in the iterator
         * @see Iterator#hasNext()
         */
        @Override
        public boolean hasNext()
        {
            return this.iteratorBase.hasNext();
        }

        /**
         * Next element in iterator <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @return Next element in iterator
         * @see Iterator#next()
         */
        @Override
        public ELEMENT next()
        {
            return this.iteratorBase.next().data;
        }

        /**
         * Do nothing here <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @see Iterator#remove()
         */
        @Override
        public void remove()
        {
        }
    }

    /**
     * Set where stored hash table elements
     */
    private final TreeSet<Element<T>> treeSet;

    /**
     * Create a new instance of HashInt
     */
    public HashInt()
    {
        this.treeSet = new TreeSet<>();
    }

    /**
     * Obtain an element from the list that match the given filter.<br>
     * The result can be any element in list that match.<br>
     * If no element found, the result future will be on error.<br>
     * The search is do in separate thread
     *
     * @param filter Filter to use for search
     * @return Future link to the search. It will contains the found element or be on error if no elements match
     */
    @Override
    public Future<T> any(final Filter<T> filter)
    {
        return Future.firstMatch(this, filter);
    }

    /**
     * Execute a task in parallel on each element (filtered gby given filter) of the list.<br>
     * Tasks are launch, and method return immediately<br>
     * Note:
     * <ul>
     * <li> Their no guarantee about the order of elements' list meet by the task. If order mandatory use {@link #consumeAsync(ConsumerTask, Filter)}</li>
     * <li> Since order is not important, thread management is simplified, so it is faster than {@link #consumeAsync(ConsumerTask, Filter)} </li>
     * </ul>
     *
     * @param task   Task to play
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @return Future to track/link to the end of all parallel tasks
     */
    @Override
    public Future<Void> async(final ConsumerTask<T> task, final Filter<T> filter)
    {
        return ForEach.async(this, task, filter);
    }

    /**
     * Execute task for each element (that respects the given filter) of the list
     *
     * @param task   Task to do
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @return This list itself, convenient for chaining
     */
    @Override
    public HashInt<T> consume(final ConsumerTask<T> task, final Filter<T> filter)
    {
        for (Element<T> element : this.treeSet)
        {
            if (filter == null || filter.isFiltered(element.data))
            {
                task.consume(element.data);
            }
        }

        return this;
    }

    /**
     * Create list composed of filtered elements of the list
     *
     * @param filter Filter to select elements
     * @return List of filtered elements
     */
    @Override
    public HashInt<T> filter(final Filter<T> filter)
    {
        final HashInt<T> hashInt = new HashInt<>();

        for (Element<T> element : this.treeSet)
        {
            if (filter.isFiltered(element.data))
            {
                hashInt.treeSet.add(element);
            }
        }

        return hashInt;
    }

    /**
     * Execute task for each element (that respects the given filter) of the list and collect the result in other list
     *
     * @param task   Task to do
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @return List with transformed elements
     */
    @Override
    public <R> ParallelList<R, ?> flatMap(final Task<T, ParallelList<R, ?>> task, final Filter<T> filter)
    {
        final HashInt<R> hashInt = new HashInt<>();

        for (Element<T> element : this.treeSet)
        {
            if (filter == null || filter.isFiltered(element.data))
            {
                task.playTask(element.data).consume(value -> hashInt.treeSet.add(new Element<>(element.hash, value)));
            }
        }

        return hashInt;
    }

    /**
     * Execute a task in parallel on each element (filtered gby given filter) of the list.<br>
     * Tasks are launch, and method return immediately<br>
     * Note:
     * <ul>
     * <li> Their no guarantee about the order of elements' list meet by the task. If order mandatory use {@link #consumeAsync(ConsumerTask, Filter)}</li>
     * <li> Since order is not important, thread management is simplified, so it is faster than {@link #consumeAsync(ConsumerTask, Filter)} </li>
     * </ul>
     *
     * @param task   Task to play
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @return The list itself, convenient for chaining
     */
    @Override
    public HashInt<T> forEach(final ConsumerTask<T> task, final Filter<T> filter)
    {
        ForEach.forEach(this, task, filter);
        return this;
    }

    /**
     * Execute task for each element (that respects the given filter) of the list and collect the result in other list
     *
     * @param task   Task to do
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @return List with transformed elements
     */
    @Override
    public <R> ParallelList<R, ?> map(final Task<T, R> task, final Filter<T> filter)
    {
        final HashInt<R> hashInt = new HashInt<>();

        for (Element<T> element : this.treeSet)
        {
            if (filter == null || filter.isFiltered(element.data))
            {
                hashInt.treeSet.add(new Element<>(element.hash, task.playTask(element.data)));
            }
        }

        return hashInt;
    }

    /**
     * Execute a task in parallel on each element (filtered gby given filter) of the list.<br>
     * The method will wait all parallel task finished before return<br>
     * Note:
     * <ul>
     * <li> Their no guarantee about the order of elements' list meet by the task. If order mandatory use {@link #consumeAsync(ConsumerTask, Filter)} and {@link Future#waitFinish()}</li>
     * <li> Since order is not important, thread management is simplified, so it is faster than {@link #consumeAsync(ConsumerTask, Filter)} and {@link Future#waitFinish()} </li>
     * </ul>
     *
     * @param task   Task to play
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @return The list itself, convenient for chaining
     */
    @Override
    public HashInt<T> sync(final ConsumerTask<T> task, final Filter<T> filter)
    {
        ForEach.sync(this, task, filter);
        return this;
    }

    /**
     * Clear the hash table
     */
    public void clear()
    {
        this.treeSet.clear();
    }

    /**
     * Indicates if a key exists in the table
     *
     * @param key Tested key
     * @return {@code true} if the ket exists
     */
    public boolean contains(final int key)
    {
        return this.treeSet.contains(new Element<T>(key));
    }

    /**
     * Obtain element associated to a key.<br>
     * It returns {@code null} if no element are associated to the key
     *
     * @param key Ket for get the value
     * @return Associated value or {@code null} if the key not associated
     */
    public T get(final int key)
    {
        final Element<T> element = new Element<>(key);

        final Element<T> found = this.treeSet.ceiling(element);

        if (found == null)
        {
            return null;
        }

        if (found.hash == key)
        {
            return found.data;
        }

        return null;
    }

    /**
     * Hash int keys
     *
     * @return Hash int keys
     */
    public int[] getKeys()
    {
        final ArrayInt keys = new ArrayInt();

        for (final Element<T> element : this.treeSet)
        {
            keys.add(element.hash);
        }

        keys.sort();
        return keys.toArray();
    }

    /**
     * Number of elements in the table
     *
     * @return Number of elements in the table
     */
    public int getSize()
    {
        return this.treeSet.size();
    }

    /**
     * Indicates if table is empty
     *
     * @return {@code true} if table is empty
     */
    public boolean isEmpty()
    {
        return this.treeSet.isEmpty();
    }

    /**
     * Iterator on elements <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Iterator on elements
     * @see Iterable#iterator()
     */
    @Override
    public Iterator<T> iterator()
    {
        return new IteratorElement<>(this.treeSet.iterator());
    }

    public int obtainKeyOf(T value)
    {
        if (value == null)
        {
            return Integer.MIN_VALUE;
        }

        for (Element<T> element : this.treeSet)
        {
            if (element.data.equals(value))
            {
                return element.hash;
            }
        }

        return Integer.MIN_VALUE;
    }

    /**
     * Associate an element to a key
     *
     * @param key   Key
     * @param value Value to associate
     */
    public void put(final int key, final T value)
    {
        if (value == null)
        {
            throw new NullPointerException("value MUST NOT be null");
        }

        final Element<T> element = new Element<>(key, value);
        this.treeSet.remove(element);
        this.treeSet.add(element);
    }

    /**
     * Remove an element
     *
     * @param key Key of element to remove
     */
    public void remove(final int key)
    {
        this.treeSet.remove(new Element<T>(key));
    }

    /**
     * Iterable size
     *
     * @return Iterable size
     */
    @Override
    public int size()
    {
        return this.getSize();
    }

    /**
     * String representation <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return String representation
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        final StringBuilder stringBuilder = new StringBuilder(1024);

        stringBuilder.append('[');

        final Iterator<Element<T>> iterator = this.treeSet.iterator();

        if (iterator.hasNext())
        {
            Element<T> element = iterator.next();
            stringBuilder.append(element.hash);
            stringBuilder.append("->");
            stringBuilder.append(element.data);

            while (iterator.hasNext())
            {
                element = iterator.next();
                stringBuilder.append(" | ");
                stringBuilder.append(element.hash);
                stringBuilder.append("->");
                stringBuilder.append(element.data);
            }
        }

        stringBuilder.append(']');

        return stringBuilder.toString();
    }
}
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.Filter;
import jhelp.util.thread.Future;
import jhelp.util.thread.Task;

/**
 * Array of integer.<br>
 * More optimized than {@link ArrayList ArrayList<Integer>}
 *
 * @author JHelp
 */
public final class ArrayInt implements ParallelList<Integer, ArrayInt>, SizedIterable<Integer>
{
    /**
     * Array of integer
     */
    private int[]   array;
    /**
     * Actual size
     */
    private int     size;
    /**
     * Indicates if array is sorted
     */
    private boolean sorted;

    /**
     * Create a new instance of ArrayInt
     */
    public ArrayInt()
    {
        this(128);
    }

    /**
     * Create a new instance of ArrayInt
     *
     * @param initalSize Initial capacity
     */
    public ArrayInt(final int initalSize)
    {
        this.array = new int[Math.max(initalSize, 128)];
        this.size = 0;

        this.sorted = true;
    }

    /**
     * Check if an index is valid
     *
     * @param index Index checked
     * @throws IllegalArgumentException if index not valid
     */
    private void checkIndex(final int index)
    {
        if ((index < 0) || (index >= this.size))
        {
            throw new IllegalArgumentException("index must be in [0, " + this.size + "[ not " + index);
        }
    }

    /**
     * Expand, if need, the capacity
     *
     * @param more Number of free space at least need
     */
    private void expand(final int more)
    {
        if ((this.size + more) >= this.array.length)
        {
            int newSize = this.size + more;
            newSize += (newSize / 10) + 1;

            final int[] temp = new int[newSize];
            System.arraycopy(this.array, 0, temp, 0, this.size);

            this.array = temp;
        }
    }

    /**
     * Add an integer is the array
     *
     * @param integer Integer to add
     */
    public void add(final int integer)
    {
        this.expand(1);

        this.sorted = (this.size == 0) || ((this.sorted) && (this.array[this.size - 1] <= integer));

        this.array[this.size] = integer;
        this.size++;
    }

    /***
     * Add all elements of an array
     *
     * @param toAdd
     *           Array to add its elements
     */
    public void addAll(final ArrayInt toAdd)
    {
        if (toAdd == null)
        {
            return;
        }

        toAdd.sync(this::add);
    }

    /**
     * Call by garbage collector when want free some memory <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @throws Throwable On issue
     * @see Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable
    {
        this.array = null;

        this.size = 0;

        super.finalize();
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
    public Future<Integer> any(final Filter<Integer> filter)
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
    public Future<Void> async(final ConsumerTask<Integer> task, final Filter<Integer> filter)
    {
        return ForEach.async(this.toIntegerArray(), task, filter);
    }

    /**
     * Execute task for each element (that respects the given filter) of the list
     *
     * @param task   Task to do
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @return This list itself, convenient for chaining
     */
    @Override
    public ArrayInt consume(final ConsumerTask<Integer> task, final Filter<Integer> filter)
    {
        for (Integer element : this)
        {
            if (filter == null || filter.isFiltered(element))
            {
                task.consume(element);
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
    public ArrayInt filter(final Filter<Integer> filter)
    {
        final ArrayInt arrayInt = new ArrayInt();
        this.consume(arrayInt::add, filter);
        return arrayInt;
    }

    /**
     * Execute task for each element (that respects the given filter) of the list and collect the result in other list
     *
     * @param task   Task to do
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @param <R>    Result list element type
     * @return List with transformed elements
     */
    @Override
    public <R> ParallelList<R, ?> flatMap(
            final Task<Integer, ParallelList<R, ?>> task, final Filter<Integer> filter)
    {
        final ArrayObject<R>   arrayObject = new ArrayObject<>();
        final Task<R, Boolean> add         = arrayObject::add;

        for (Integer element : this)
        {
            if (filter == null || filter.isFiltered(element))
            {
                task.playTask(element).map(add);
            }
        }

        return arrayObject;
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
    public ArrayInt forEach(final ConsumerTask<Integer> task, final Filter<Integer> filter)
    {
        ForEach.forEach(this.toIntegerArray(), task, filter);
        return this;
    }

    /**
     * Execute task for each element (that respects the given filter) of the list and collect the result in other list
     *
     * @param task   Task to do
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @param <R>    Result list element type
     * @return List with transformed elements
     */
    @Override
    public <R> ParallelList<R, ?> map(final Task<Integer, R> task, final Filter<Integer> filter)
    {
        final ArrayObject<R> arrayObject = new ArrayObject<>();

        for (Integer element : this)
        {
            if (filter == null || filter.isFiltered(element))
            {
                arrayObject.add(task.playTask(element));
            }
        }

        return arrayObject;
    }

    /**
     * Clear the array
     */
    public void clear()
    {
        this.size = 0;
        this.sorted = true;
    }

    /**
     * Indicates if an integer is in the array.<br>
     * Search is on O(n)
     *
     * @param integer Integer search
     * @return {@code true} if the integer is inside
     */
    public boolean contains(final int integer)
    {
        return this.getIndex(integer) >= 0;
    }

    /**
     * Indicates if an integer is in the array.<br>
     * Search is in O(LN(n)) but work only if the array is sorted
     *
     * @param integer Integer search
     * @return {@code true} if the integer is inside
     */
    public boolean containsSupposeSorted(final int integer)
    {
        return this.getIndexSupposeSorted(integer) >= 0;
    }

    /**
     * Create a copy of the array
     *
     * @return The copy
     */
    public ArrayInt createCopy()
    {
        final ArrayInt copy = new ArrayInt();

        final int length = this.array.length;
        copy.array = new int[length];
        System.arraycopy(this.array, 0, copy.array, 0, length);

        copy.size = this.size;
        copy.sorted = this.sorted;

        return copy;
    }

    /**
     * Destroy properly the array int
     */
    public void destroy()
    {
        this.size = 0;
        this.array = null;
        this.sorted = true;
    }

    /**
     * Index of an integer or -1 if integer not in the array.<br>
     * Search is on O(n)
     *
     * @param integer Integer search
     * @return Integer index or -1 if integer not in the array
     */
    public int getIndex(final int integer)
    {
        if (this.sorted)
        {
            return this.getIndexSupposeSorted(integer);
        }

        for (int i = 0; i < this.size; i++)
        {
            if (this.array[i] == integer)
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * Index of an integer or -1 if integer not in the array.<br>
     * Search is in O(LN(n)) but work only if the array is sorted
     *
     * @param integer Integer search
     * @return Integer index or -1 if integer not in the array
     */
    public int getIndexSupposeSorted(final int integer)
    {
        if (this.size <= 0)
        {
            return -1;
        }

        int actual = this.array[0];

        if (integer < actual)
        {
            return -1;
        }

        if (integer == actual)
        {
            return 0;
        }

        int min = 0;
        int max = this.size - 1;

        actual = this.array[max];

        if (integer > actual)
        {
            return -1;
        }

        if (integer == actual)
        {
            return max;
        }

        int mil;
        while (min < (max - 1))
        {
            mil = (min + max) >> 1;
            actual = this.array[mil];

            if (integer == actual)
            {
                return mil;
            }

            if (integer > actual)
            {
                min = mil;
            }
            else
            {
                max = mil;
            }
        }

        return -1;
    }

    /**
     * Obtain an integer from the array
     *
     * @param index Integer index
     * @return Integer
     */
    public int getInteger(final int index)
    {
        this.checkIndex(index);

        return this.array[index];
    }

    /**
     * Array size
     *
     * @return Array size
     */
    public int getSize()
    {
        return this.size;
    }

    /**
     * Insert an integer to a given index
     *
     * @param integer Integer to insert
     * @param index   Index where insert
     */
    public void insert(final int integer, int index)
    {
        this.expand(1);

        if (index < 0)
        {
            index = 0;
        }

        if (index >= this.size)
        {
            this.add(integer);

            return;
        }

        this.sorted =
                (this.sorted) && ((index == 0) || (integer >= this.array[index - 1])) && (integer <= this.array[index]);

        System.arraycopy(this.array, index, this.array, index + 1, this.array.length - index - 1);

        this.array[index] = integer;
        this.size++;
    }

    /**
     * Indicates if array is empty
     *
     * @return {@code true} if array is empty
     */
    public boolean isEmpty()
    {
        return this.size == 0;
    }

    /**
     * Indicates if array is sorted.<br>
     * But it does it in fast way, so if the answer is {@code true}, its sure that the array is sorted, but if {@code false}
     * indicates that sorted is unknown
     *
     * @return {@code true} if array is sorted. {@code false} if not sure about sorted status
     */
    public boolean isSortedFast()
    {
        return this.sorted;
    }

    /**
     * Indicates if array is sorted.<br>
     * It is a slower method than {@link #isSortedFast()} but the answer is accurate, that means if {@code false} is answer, you
     * are sure that the array is not sorted
     *
     * @return {@code true} if array is sorted. {@code false} if array not sorted
     */
    public boolean isSortedSlow()
    {
        if (this.sorted)
        {
            return true;
        }

        int previous = this.array[0];
        int actual;

        for (int i = 1; i < this.size; i++)
        {
            actual = this.array[i];

            if (previous > actual)
            {
                return false;
            }

            previous = actual;
        }

        this.sorted = true;
        return true;
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
    public ArrayInt sync(final ConsumerTask<Integer> task, final Filter<Integer> filter)
    {
        ForEach.sync(this.toIntegerArray(), task, filter);
        return this;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Integer> iterator()
    {
        return this.iteratorInt();
    }

    /**
     * remove an integer
     *
     * @param index Index of integer to remove
     */
    public void remove(final int index)
    {
        this.checkIndex(index);

        System.arraycopy(this.array, index + 1, this.array, index, this.size - index - 1);
        this.size--;

        if (this.size < 2)
        {
            this.sorted = true;
        }
    }

    /**
     * Change an integer on the array
     *
     * @param index   Index to change
     * @param integer New value
     */
    public void setInteger(final int index, final int integer)
    {
        this.checkIndex(index);

        this.array[index] = integer;

        this.sorted = (this.sorted) && ((index == 0) || (integer >= this.array[index - 1]))
                      && ((index == (this.size - 1)) || (integer <= this.array[index + 1]));
    }

    public EnumerationIteratorInt iteratorInt()
    {
        return new EnumerationIteratorInt(Arrays.copyOf(this.array, this.size));
    }

    /**
     * Sort the array.<br>
     * For example, [2, 5, 9, 2, 6, 2, 5, 7, 1] -> [1, 2, 2, 2, 5, 5, 6, 7, 9]
     */
    public void sort()
    {
        if (this.sorted)
        {
            return;
        }

        Arrays.sort(this.array, 0, this.size);
        this.sorted = true;
    }

    /**
     * Sort array in unique mode.<br>
     * That is to say if tow integer are equals, only one is keep.<br>
     * For example, [2, 5, 9, 2, 6, 2, 5, 7, 1] -> [1, 2, 5, 6, 7, 9]
     */
    public void sortUniq()
    {
        if (this.size < 2)
        {
            return;
        }

        this.sort();
        int actual;
        int previous = this.array[this.size - 1];

        for (int index = this.size - 2; index >= 0; index--)
        {
            actual = this.array[index];

            if (actual == previous)
            {
                System.arraycopy(this.array, index + 1, this.array, index, this.size - index - 1);
                this.size--;
            }

            previous = actual;
        }
    }

    /**
     * Convert in int array
     *
     * @return Extracted array
     */
    public int[] toArray()
    {
        final int[] array = new int[this.size];

        System.arraycopy(this.array, 0, array, 0, this.size);

        return array;
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
        final StringBuilder stringBuilder = new StringBuilder("[");

        if (this.size > 0)
        {
            stringBuilder.append(this.array[0]);

            for (int i = 1; i < this.size; i++)
            {
                stringBuilder.append(", ");
                stringBuilder.append(this.array[i]);
            }
        }

        stringBuilder.append(']');

        return stringBuilder.toString();
    }

    public Integer[] toIntegerArray()
    {
        Integer[] array = new Integer[this.size];

        for (int index = this.size - 1; index >= 0; index--)
        {
            array[index] = this.array[index];
        }

        return array;
    }
}
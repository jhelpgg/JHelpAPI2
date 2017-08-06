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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import jhelp.util.math.JHelpRandom;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.Filter;
import jhelp.util.thread.Future;
import jhelp.util.thread.Task;

/**
 * Created by jhelp on 22/07/17.
 */
public final class ArrayObject<T> implements List<T>, SizedIterable<T>, ParallelList<T, ArrayObject<T>>
{
    private final List<T> list;

    public ArrayObject()
    {
        this.list = new ArrayList<>();
    }

    private ArrayObject(List<T> list)
    {
        this.list = list;
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
        return Future.firstMatch((SizedIterable<T>) this, filter);
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
        return ForEach.async((SizedIterable<T>) this, task, filter);
    }

    /**
     * Execute task for each element (that respects the given filter) of the list
     *
     * @param task   Task to do
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @return This list itself, convenient for chaining
     */
    @Override
    public ArrayObject<T> consume(final ConsumerTask<T> task, final Filter<T> filter)
    {
        for (T element : this)
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
    public ArrayObject<T> filter(final Filter<T> filter)
    {
        ArrayObject<T> arrayObject = new ArrayObject<>();
        this.consume(arrayObject::add, filter);
        return arrayObject;
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
            final Task<T, ParallelList<R, ?>> task, final Filter<T> filter)
    {
        final ArrayObject<R>   arrayObject = new ArrayObject<>();
        final Task<R, Boolean> add         = arrayObject::add;

        for (T element : this)
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
    public ArrayObject<T> forEach(final ConsumerTask<T> task, final Filter<T> filter)
    {
        ForEach.forEach(this, task, filter);
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
    public <R> ParallelList<R, ?> map(final Task<T, R> task, final Filter<T> filter)
    {
        final ArrayObject<R> arrayObject = new ArrayObject<>();

        for (T element : this)
        {
            if (filter == null || filter.isFiltered(element))
            {
                arrayObject.add(task.playTask(element));
            }
        }

        return arrayObject;
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
    public ArrayObject<T> sync(final ConsumerTask<T> task, final Filter<T> filter)
    {
        ForEach.sync((SizedIterable<T>) this, task, filter);
        return this;
    }

    public T first()
    {
        if (this.list.isEmpty())
        {
            throw new IllegalStateException("The list is empty");
        }

        return this.list.get(0);
    }

    public T last()
    {
        if (this.list.isEmpty())
        {
            throw new IllegalStateException("The list is empty");
        }

        return this.list.get(this.list.size() - 1);
    }

    public T oneElement()
    {
        if (this.list.isEmpty())
        {
            throw new IllegalStateException("The list is empty");
        }

        return JHelpRandom.random(this.list);
    }

    /**
     * Returns the number of elements in this list.  If this list contains
     * more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this list
     */
    @Override
    public int size()
    {
        return this.list.size();
    }

    /**
     * Returns <tt>true</tt> if this list contains no elements.
     *
     * @return <tt>true</tt> if this list contains no elements
     */
    @Override
    public boolean isEmpty()
    {
        return this.list.isEmpty();
    }

    /**
     * Returns <tt>true</tt> if this list contains the specified element.
     * More formally, returns <tt>true</tt> if and only if this list contains
     * at least one element <tt>e</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
     *
     * @param object element whose presence in this list is to be tested
     * @return <tt>true</tt> if this list contains the specified element
     * @throws ClassCastException   if the type of the specified element
     *                              is incompatible with this list
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     *                              list does not permit null elements
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean contains(final Object object)
    {
        return this.list.contains(object);
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * @return an iterator over the elements in this list in proper sequence
     */
    @Override
    public Iterator<T> iterator()
    {
        return this.list.iterator();
    }

    /**
     * Returns an array containing all of the elements in this list in proper
     * sequence (from first to last element).
     * <p>
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this list.  (In other words, this method must
     * allocate a new array even if this list is backed by an array).
     * The caller is thus free to modify the returned array.
     * <p>
     * <p>This method acts as bridge between array-based and collection-based
     * APIs.
     *
     * @return an array containing all of the elements in this list in proper
     * sequence
     */
    @Override
    public Object[] toArray()
    {
        return this.list.toArray();
    }

    /**
     * Returns an array containing all of the elements in this list in
     * proper sequence (from first to last element); the runtime type of
     * the returned array is that of the specified array.  If the list fits
     * in the specified array, it is returned therein.  Otherwise, a new
     * array is allocated with the runtime type of the specified array and
     * the size of this list.
     * <p>
     * <p>If the list fits in the specified array with room to spare (i.e.,
     * the array has more elements than the list), the element in the array
     * immediately following the end of the list is set to <tt>null</tt>.
     * (This is useful in determining the length of the list <i>only</i> if
     * the caller knows that the list does not contain any null elements.)
     * <p>
     * <p>Like the {@link #toArray()} method, this method acts as bridge between
     * array-based and collection-based APIs.  Further, this method allows
     * precise control over the runtime type of the output array, and may,
     * under certain circumstances, be used to save allocation costs.
     * <p>
     * <p>Suppose <tt>x</tt> is a list known to contain only strings.
     * The following code can be used to dump the list into a newly
     * allocated array of <tt>String</tt>:
     * <p>
     * <pre>{@code
     *     String[] y = x.toArray(new String[0]);
     * }</pre>
     * <p>
     * Note that <tt>toArray(new Object[0])</tt> is identical in function to
     * <tt>toArray()</tt>.
     *
     * @param array the array into which the elements of this list are to
     *              be stored, if it is big enough; otherwise, a new array of the
     *              same runtime type is allocated for this purpose.
     * @return an array containing the elements of this list
     * @throws ArrayStoreException  if the runtime type of the specified array
     *                              is not a supertype of the runtime type of every element in
     *                              this list
     * @throws NullPointerException if the specified array is null
     */
    @Override
    public <T1> T1[] toArray(final T1[] array)
    {
        return this.list.toArray(array);
    }

    /**
     * Appends the specified element to the end of this list (optional
     * operation).
     * <p>
     * <p>Lists that support this operation may place limitations on what
     * elements may be added to this list.  In particular, some
     * lists will refuse to add null elements, and others will impose
     * restrictions on the type of elements that may be added.  List
     * classes should clearly specify in their documentation any restrictions
     * on what elements may be added.
     *
     * @param element element to be appended to this list
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     * @throws UnsupportedOperationException if the <tt>add</tt> operation
     *                                       is not supported by this list
     * @throws ClassCastException            if the class of the specified element
     *                                       prevents it from being added to this list
     * @throws NullPointerException          if the specified element is null and this
     *                                       list does not permit null elements
     * @throws IllegalArgumentException      if some property of this element
     *                                       prevents it from being added to this list
     */
    @Override
    public boolean add(final T element)
    {
        return this.list.add(element);
    }

    /**
     * Removes the first occurrence of the specified element from this list,
     * if it is present (optional operation).  If this list does not contain
     * the element, it is unchanged.  More formally, removes the element with
     * the lowest index <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>
     * (if such an element exists).  Returns <tt>true</tt> if this list
     * contained the specified element (or equivalently, if this list changed
     * as a result of the call).
     *
     * @param object element to be removed from this list, if present
     * @return <tt>true</tt> if this list contained the specified element
     * @throws ClassCastException            if the type of the specified element
     *                                       is incompatible with this list
     *                                       (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if the specified element is null and this
     *                                       list does not permit null elements
     *                                       (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws UnsupportedOperationException if the <tt>remove</tt> operation
     *                                       is not supported by this list
     */
    @Override
    public boolean remove(final Object object)
    {
        return this.list.remove(object);
    }

    /**
     * Returns <tt>true</tt> if this list contains all of the elements of the
     * specified collection.
     *
     * @param collection collection to be checked for containment in this list
     * @return <tt>true</tt> if this list contains all of the elements of the
     * specified collection
     * @throws ClassCastException   if the types of one or more elements
     *                              in the specified collection are incompatible with this
     *                              list
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified collection contains one
     *                              or more null elements and this list does not permit null
     *                              elements
     *                              (<a href="Collection.html#optional-restrictions">optional</a>),
     *                              or if the specified collection is null
     * @see #contains(Object)
     */
    @Override
    public boolean containsAll(final Collection<?> collection)
    {
        return this.list.containsAll(collection);
    }

    /**
     * Appends all of the elements in the specified collection to the end of
     * this list, in the order that they are returned by the specified
     * collection's iterator (optional operation).  The behavior of this
     * operation is undefined if the specified collection is modified while
     * the operation is in progress.  (Note that this will occur if the
     * specified collection is this list, and it's nonempty.)
     *
     * @param collection collection containing elements to be added to this list
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
     *                                       is not supported by this list
     * @throws ClassCastException            if the class of an element of the specified
     *                                       collection prevents it from being added to this list
     * @throws NullPointerException          if the specified collection contains one
     *                                       or more null elements and this list does not permit null
     *                                       elements, or if the specified collection is null
     * @throws IllegalArgumentException      if some property of an element of the
     *                                       specified collection prevents it from being added to this list
     * @see #add(Object)
     */
    @Override
    public boolean addAll(final Collection<? extends T> collection)
    {
        return this.list.addAll(collection);
    }

    /**
     * Inserts all of the elements in the specified collection into this
     * list at the specified position (optional operation).  Shifts the
     * element currently at that position (if any) and any subsequent
     * elements to the right (increases their indices).  The new elements
     * will appear in this list in the order that they are returned by the
     * specified collection's iterator.  The behavior of this operation is
     * undefined if the specified collection is modified while the
     * operation is in progress.  (Note that this will occur if the specified
     * collection is this list, and it's nonempty.)
     *
     * @param index      index at which to insert the first element from the
     *                   specified collection
     * @param collection collection containing elements to be added to this list
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
     *                                       is not supported by this list
     * @throws ClassCastException            if the class of an element of the specified
     *                                       collection prevents it from being added to this list
     * @throws NullPointerException          if the specified collection contains one
     *                                       or more null elements and this list does not permit null
     *                                       elements, or if the specified collection is null
     * @throws IllegalArgumentException      if some property of an element of the
     *                                       specified collection prevents it from being added to this list
     * @throws IndexOutOfBoundsException     if the index is out of range
     *                                       (<tt>index &lt; 0 || index &gt; size()</tt>)
     */
    @Override
    public boolean addAll(final int index, final Collection<? extends T> collection)
    {
        return this.list.addAll(index, collection);
    }

    /**
     * Removes from this list all of its elements that are contained in the
     * specified collection (optional operation).
     *
     * @param collection collection containing elements to be removed from this list
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws UnsupportedOperationException if the <tt>removeAll</tt> operation
     *                                       is not supported by this list
     * @throws ClassCastException            if the class of an element of this list
     *                                       is incompatible with the specified collection
     *                                       (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if this list contains a null element and the
     *                                       specified collection does not permit null elements
     *                                       (<a href="Collection.html#optional-restrictions">optional</a>),
     *                                       or if the specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    @Override
    public boolean removeAll(final Collection<?> collection)
    {
        return this.list.removeAll(collection);
    }

    /**
     * Retains only the elements in this list that are contained in the
     * specified collection (optional operation).  In other words, removes
     * from this list all of its elements that are not contained in the
     * specified collection.
     *
     * @param collection collection containing elements to be retained in this list
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws UnsupportedOperationException if the <tt>retainAll</tt> operation
     *                                       is not supported by this list
     * @throws ClassCastException            if the class of an element of this list
     *                                       is incompatible with the specified collection
     *                                       (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException          if this list contains a null element and the
     *                                       specified collection does not permit null elements
     *                                       (<a href="Collection.html#optional-restrictions">optional</a>),
     *                                       or if the specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    @Override
    public boolean retainAll(final Collection<?> collection)
    {
        return this.list.retainAll(collection);
    }

    /**
     * Removes all of the elements from this list (optional operation).
     * The list will be empty after this call returns.
     *
     * @throws UnsupportedOperationException if the <tt>clear</tt> operation
     *                                       is not supported by this list
     */
    @Override
    public void clear()
    {
        this.list.clear();
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   (<tt>index &lt; 0 || index &gt;= size()</tt>)
     */
    @Override
    public T get(final int index)
    {
        return this.list.get(index);
    }

    /**
     * Replaces the element at the specified position in this list with the
     * specified element (optional operation).
     *
     * @param index   index of the element to replace
     * @param element element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws UnsupportedOperationException if the <tt>set</tt> operation
     *                                       is not supported by this list
     * @throws ClassCastException            if the class of the specified element
     *                                       prevents it from being added to this list
     * @throws NullPointerException          if the specified element is null and
     *                                       this list does not permit null elements
     * @throws IllegalArgumentException      if some property of the specified
     *                                       element prevents it from being added to this list
     * @throws IndexOutOfBoundsException     if the index is out of range
     *                                       (<tt>index &lt; 0 || index &gt;= size()</tt>)
     */
    @Override
    public T set(final int index, final T element)
    {
        return this.list.set(index, element);
    }

    /**
     * Inserts the specified element at the specified position in this list
     * (optional operation).  Shifts the element currently at that position
     * (if any) and any subsequent elements to the right (adds one to their
     * indices).
     *
     * @param index   index at which the specified element is to be inserted
     * @param element element to be inserted
     * @throws UnsupportedOperationException if the <tt>add</tt> operation
     *                                       is not supported by this list
     * @throws ClassCastException            if the class of the specified element
     *                                       prevents it from being added to this list
     * @throws NullPointerException          if the specified element is null and
     *                                       this list does not permit null elements
     * @throws IllegalArgumentException      if some property of the specified
     *                                       element prevents it from being added to this list
     * @throws IndexOutOfBoundsException     if the index is out of range
     *                                       (<tt>index &lt; 0 || index &gt; size()</tt>)
     */
    @Override
    public void add(final int index, final T element)
    {
        this.list.add(index, element);
    }

    /**
     * Removes the element at the specified position in this list (optional
     * operation).  Shifts any subsequent elements to the left (subtracts one
     * from their indices).  Returns the element that was removed from the
     * list.
     *
     * @param index the index of the element to be removed
     * @return the element previously at the specified position
     * @throws UnsupportedOperationException if the <tt>remove</tt> operation
     *                                       is not supported by this list
     * @throws IndexOutOfBoundsException     if the index is out of range
     *                                       (<tt>index &lt; 0 || index &gt;= size()</tt>)
     */
    @Override
    public T remove(final int index)
    {
        return this.list.remove(index);
    }

    /**
     * Returns the index of the first occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * More formally, returns the lowest index <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
     * or -1 if there is no such index.
     *
     * @param object element to search for
     * @return the index of the first occurrence of the specified element in
     * this list, or -1 if this list does not contain the element
     * @throws ClassCastException   if the type of the specified element
     *                              is incompatible with this list
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     *                              list does not permit null elements
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public int indexOf(final Object object)
    {
        return this.list.indexOf(object);
    }

    /**
     * Returns the index of the last occurrence of the specified element
     * in this list, or -1 if this list does not contain the element.
     * More formally, returns the highest index <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
     * or -1 if there is no such index.
     *
     * @param object element to search for
     * @return the index of the last occurrence of the specified element in
     * this list, or -1 if this list does not contain the element
     * @throws ClassCastException   if the type of the specified element
     *                              is incompatible with this list
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     *                              list does not permit null elements
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public int lastIndexOf(final Object object)
    {
        return this.list.lastIndexOf(object);
    }

    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence).
     *
     * @return a list iterator over the elements in this list (in proper
     * sequence)
     */
    @Override
    public ListIterator<T> listIterator()
    {
        return this.list.listIterator();
    }

    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence), starting at the specified position in the list.
     * The specified index indicates the first element that would be
     * returned by an initial call to {@link ListIterator#next next}.
     * An initial call to {@link ListIterator#previous previous} would
     * return the element with the specified index minus one.
     *
     * @param index index of the first element to be returned from the
     *              list iterator (by a call to {@link ListIterator#next next})
     * @return a list iterator over the elements in this list (in proper
     * sequence), starting at the specified position in the list
     * @throws IndexOutOfBoundsException if the index is out of range
     *                                   ({@code index < 0 || index > size()})
     */
    @Override
    public ListIterator<T> listIterator(final int index)
    {
        return this.list.listIterator(index);
    }

    /**
     * Returns a view of the portion of this list between the specified
     * <tt>fromIndex</tt>, inclusive, and <tt>toIndex</tt>, exclusive.  (If
     * <tt>fromIndex</tt> and <tt>toIndex</tt> are equal, the returned list is
     * empty.)  The returned list is backed by this list, so non-structural
     * changes in the returned list are reflected in this list, and vice-versa.
     * The returned list supports all of the optional list operations supported
     * by this list.<p>
     * <p>
     * This method eliminates the need for explicit range operations (of
     * the sort that commonly exist for arrays).  Any operation that expects
     * a list can be used as a range operation by passing a subList view
     * instead of a whole list.  For example, the following idiom
     * removes a range of elements from a list:
     * <pre>{@code
     *      list.subList(from, to).clear();
     * }</pre>
     * Similar idioms may be constructed for <tt>indexOf</tt> and
     * <tt>lastIndexOf</tt>, and all of the algorithms in the
     * <tt>Collections</tt> class can be applied to a subList.<p>
     * <p>
     * The semantics of the list returned by this method become undefined if
     * the backing list (i.e., this list) is <i>structurally modified</i> in
     * any way other than via the returned list.  (Structural modifications are
     * those that change the size of this list, or otherwise perturb it in such
     * a fashion that iterations in progress may yield incorrect results.)
     *
     * @param fromIndex low endpoint (inclusive) of the subList
     * @param toIndex   high endpoint (exclusive) of the subList
     * @return a view of the specified range within this list
     * @throws IndexOutOfBoundsException for an illegal endpoint index value
     *                                   (<tt>fromIndex &lt; 0 || toIndex &gt; size ||
     *                                   fromIndex &gt; toIndex</tt>)
     */
    @Override
    public List<T> subList(final int fromIndex, final int toIndex)
    {
        return new ArrayObject<>(this.list.subList(fromIndex, toIndex));
    }

    @Override
    public String toString()
    {
        return this.list.toString();
    }
}

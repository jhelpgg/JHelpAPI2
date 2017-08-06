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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.Filter;
import jhelp.util.thread.Future;
import jhelp.util.thread.Task;

/**
 * Created by jhelp on 21/07/17.
 */
public final class CollectionParallel<T> implements ParallelList<T, CollectionParallel<T>>, Collection<T>
{
    public static <T1> CollectionParallel<T1> makeParallel(@NotNull Collection<T1> collection)
    {
        Objects.requireNonNull(collection, "collection MUST NOT be null!");

        if (collection instanceof CollectionParallel)
        {
            return (CollectionParallel<T1>) collection;
        }

        return new CollectionParallel<>(collection);
    }

    private final Collection<T> collection;

    public CollectionParallel(@NotNull final Collection<T> collection)
    {
        Objects.requireNonNull(collection, "collection MUST NOT be null!");
        this.collection = collection;
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
        return ForEach.async(this.collection, task, filter);
    }

    /**
     * Execute task for each element (that respects the given filter) of the list
     *
     * @param task   Task to do
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @return This list itself, convenient for chaining
     */
    @Override
    public CollectionParallel<T> consume(final ConsumerTask<T> task, final Filter<T> filter)
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
    public CollectionParallel<T> filter(final Filter<T> filter)
    {
        final Collection<T> collection = new ArrayList<>();
        this.consume(collection::add, filter);
        return new CollectionParallel<>(collection);
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
    public CollectionParallel<T> forEach(final ConsumerTask<T> task, final Filter<T> filter)
    {
        ForEach.forEach(this.collection, task, filter);
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
    public CollectionParallel<T> sync(final ConsumerTask<T> task, final Filter<T> filter)
    {
        ForEach.sync(this.collection, task, filter);
        return this;
    }

    /**
     * Returns the number of elements in this collection.  If this collection
     * contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this collection
     */
    @Override
    public int size()
    {
        return this.collection.size();
    }

    /**
     * Returns <tt>true</tt> if this collection contains no elements.
     *
     * @return <tt>true</tt> if this collection contains no elements
     */
    @Override
    public boolean isEmpty()
    {
        return this.collection.isEmpty();
    }

    /**
     * Returns <tt>true</tt> if this collection contains the specified element.
     * More formally, returns <tt>true</tt> if and only if this collection
     * contains at least one element <tt>e</tt> such that
     * <tt>(bject==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;bject.equals(e))</tt>.
     *
     * @param object element whose presence in this collection is to be tested
     * @return <tt>true</tt> if this collection contains the specified
     * element
     * @throws ClassCastException   if the type of the specified element
     *                              is incompatible with this collection
     *                              (<a href="#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     *                              collection does not permit null elements
     *                              (<a href="#optional-restrictions">optional</a>)
     */
    @Override
    public boolean contains(final Object object)
    {
        return this.collection.contains(object);
    }

    /**
     * Returns an iterator over the elements in this collection.  There are no
     * guarantees concerning the order in which the elements are returned
     * (unless this collection is an instance of some class that provides a
     * guarantee).
     *
     * @return an <tt>Iterator</tt> over the elements in this collection
     */
    @Override
    public Iterator<T> iterator()
    {
        return this.collection.iterator();
    }

    /**
     * Returns an array containing all of the elements in this collection.
     * If this collection makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in
     * the same order.
     * <p>
     * <p>The returned array will be "safe" in that no references to it are
     * maintained by this collection.  (In other words, this method must
     * allocate a new array even if this collection is backed by an array).
     * The caller is thus free to modify the returned array.
     * <p>
     * <p>This method acts as bridge between array-based and collection-based
     * APIs.
     *
     * @return an array containing all of the elements in this collection
     */
    @Override public Object[] toArray()
    {
        return this.collection.toArray();
    }

    /**
     * Returns an array containing all of the elements in this collection;
     * the runtime type of the returned array is that of the specified array.
     * If the collection fits in the specified array, it is returned therein.
     * Otherwise, a new array is allocated with the runtime type of the
     * specified array and the size of this collection.
     * <p>
     * <p>If this collection fits in the specified array with room to spare
     * (i.e., the array has more elements than this collection), the element
     * in the array immediately following the end of the collection is set to
     * <tt>null</tt>.  (This is useful in determining the length of this
     * collection <i>only</i> if the caller knows that this collection does
     * not contain any <tt>null</tt> elements.)
     * <p>
     * <p>If this collection makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in
     * the same order.
     * <p>
     * <p>Like the {@link #toArray()} method, this method acts as bridge between
     * array-based and collection-based APIs.  Further, this method allows
     * precise control over the runtime type of the output array, and may,
     * under certain circumstances, be used to save allocation costs.
     * <p>
     * <p>Suppose <tt>x</tt> is a collection known to contain only strings.
     * The following code can be used to dump the collection into a newly
     * allocated array of <tt>String</tt>:
     * <p>
     * <pre>
     *     String[] y = x.toArray(new String[0]);</pre>
     * <p>
     * Note that <tt>toArray(new Object[0])</tt> is identical in function to
     * <tt>toArray()</tt>.
     *
     * @param array the array into which the elements of this collection are to be
     *              stored, if it is big enough; otherwise, a new array of the same
     *              runtime type is allocated for this purpose.
     * @return an array containing all of the elements in this collection
     * @throws ArrayStoreException  if the runtime type of the specified array
     *                              is not a supertype of the runtime type of every element in
     *                              this collection
     * @throws NullPointerException if the specified array is null
     */
    @Override public <T1> T1[] toArray(final T1[] array)
    {
        return this.collection.toArray(array);
    }

    /**
     * Ensures that this collection contains the specified element (optional
     * operation).  Returns <tt>true</tt> if this collection changed as a
     * result of the call.  (Returns <tt>false</tt> if this collection does
     * not permit duplicates and already contains the specified element.)<p>
     * <p>
     * Collections that support this operation may place limitations on what
     * elements may be added to this collection.  In particular, some
     * collections will refuse to add <tt>null</tt> elements, and others will
     * impose restrictions on the type of elements that may be added.
     * Collection classes should clearly specify in their documentation any
     * restrictions on what elements may be added.<p>
     * <p>
     * If a collection refuses to add a particular element for any reason
     * other than that it already contains the element, it <i>must</i> throw
     * an exception (rather than returning <tt>false</tt>).  This preserves
     * the invariant that a collection always contains the specified element
     * after this call returns.
     *
     * @param element element whose presence in this collection is to be ensured
     * @return <tt>true</tt> if this collection changed as a result of the
     * call
     * @throws UnsupportedOperationException if the <tt>add</tt> operation
     *                                       is not supported by this collection
     * @throws ClassCastException            if the class of the specified element
     *                                       prevents it from being added to this collection
     * @throws NullPointerException          if the specified element is null and this
     *                                       collection does not permit null elements
     * @throws IllegalArgumentException      if some property of the element
     *                                       prevents it from being added to this collection
     * @throws IllegalStateException         if the element cannot be added at this
     *                                       time due to insertion restrictions
     */
    @Override
    public boolean add(final T element)
    {
        return this.collection.add(element);
    }

    /**
     * Removes a single instance of the specified element from this
     * collection, if it is present (optional operation).  More formally,
     * removes an element <tt>e</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>, if
     * this collection contains one or more such elements.  Returns
     * <tt>true</tt> if this collection contained the specified element (or
     * equivalently, if this collection changed as a result of the call).
     *
     * @param element element to be removed from this collection, if present
     * @return <tt>true</tt> if an element was removed as a result of this call
     * @throws ClassCastException            if the type of the specified element
     *                                       is incompatible with this collection
     *                                       (<a href="#optional-restrictions">optional</a>)
     * @throws NullPointerException          if the specified element is null and this
     *                                       collection does not permit null elements
     *                                       (<a href="#optional-restrictions">optional</a>)
     * @throws UnsupportedOperationException if the <tt>remove</tt> operation
     *                                       is not supported by this collection
     */
    @Override
    public boolean remove(final Object element)
    {
        return this.collection.remove(element);
    }

    /**
     * Returns <tt>true</tt> if this collection contains all of the elements
     * in the specified collection.
     *
     * @param collection collection to be checked for containment in this collection
     * @return <tt>true</tt> if this collection contains all of the elements
     * in the specified collection
     * @throws ClassCastException   if the types of one or more elements
     *                              in the specified collection are incompatible with this
     *                              collection
     *                              (<a href="#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified collection contains one
     *                              or more null elements and this collection does not permit null
     *                              elements
     *                              (<a href="#optional-restrictions">optional</a>),
     *                              or if the specified collection is null.
     * @see #contains(Object)
     */
    @Override
    public boolean containsAll(final Collection<?> collection)
    {
        return this.collection.containsAll(collection);
    }

    /**
     * Adds all of the elements in the specified collection to this collection
     * (optional operation).  The behavior of this operation is undefined if
     * the specified collection is modified while the operation is in progress.
     * (This implies that the behavior of this call is undefined if the
     * specified collection is this collection, and this collection is
     * nonempty.)
     *
     * @param collection collection containing elements to be added to this collection
     * @return <tt>true</tt> if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the <tt>addAll</tt> operation
     *                                       is not supported by this collection
     * @throws ClassCastException            if the class of an element of the specified
     *                                       collection prevents it from being added to this collection
     * @throws NullPointerException          if the specified collection contains a
     *                                       null element and this collection does not permit null elements,
     *                                       or if the specified collection is null
     * @throws IllegalArgumentException      if some property of an element of the
     *                                       specified collection prevents it from being added to this
     *                                       collection
     * @throws IllegalStateException         if not all the elements can be added at
     *                                       this time due to insertion restrictions
     * @see #add(Object)
     */
    @Override
    public boolean addAll(final Collection<? extends T> collection)
    {
        return this.collection.addAll(collection);
    }

    /**
     * Removes all of this collection's elements that are also contained in the
     * specified collection (optional operation).  After this call returns,
     * this collection will contain no elements in common with the specified
     * collection.
     *
     * @param collection collection containing elements to be removed from this collection
     * @return <tt>true</tt> if this collection changed as a result of the
     * call
     * @throws UnsupportedOperationException if the <tt>removeAll</tt> method
     *                                       is not supported by this collection
     * @throws ClassCastException            if the types of one or more elements
     *                                       in this collection are incompatible with the specified
     *                                       collection
     *                                       (<a href="#optional-restrictions">optional</a>)
     * @throws NullPointerException          if this collection contains one or more
     *                                       null elements and the specified collection does not support
     *                                       null elements
     *                                       (<a href="#optional-restrictions">optional</a>),
     *                                       or if the specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    @Override
    public boolean removeAll(final Collection<?> collection)
    {
        return this.collection.removeAll(collection);
    }

    /**
     * Retains only the elements in this collection that are contained in the
     * specified collection (optional operation).  In other words, removes from
     * this collection all of its elements that are not contained in the
     * specified collection.
     *
     * @param collection collection containing elements to be retained in this collection
     * @return <tt>true</tt> if this collection changed as a result of the call
     * @throws UnsupportedOperationException if the <tt>retainAll</tt> operation
     *                                       is not supported by this collection
     * @throws ClassCastException            if the types of one or more elements
     *                                       in this collection are incompatible with the specified
     *                                       collection
     *                                       (<a href="#optional-restrictions">optional</a>)
     * @throws NullPointerException          if this collection contains one or more
     *                                       null elements and the specified collection does not permit null
     *                                       elements
     *                                       (<a href="#optional-restrictions">optional</a>),
     *                                       or if the specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    @Override
    public boolean retainAll(final Collection<?> collection)
    {
        return this.collection.retainAll(collection);
    }

    /**
     * Removes all of the elements from this collection (optional operation).
     * The collection will be empty after this method returns.
     *
     * @throws UnsupportedOperationException if the <tt>clear</tt> operation
     *                                       is not supported by this collection
     */
    @Override
    public void clear()
    {
        this.collection.clear();
    }

}

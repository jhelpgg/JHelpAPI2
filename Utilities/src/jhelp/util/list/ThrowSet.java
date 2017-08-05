package jhelp.util.list;

import java.util.ArrayList;
import jhelp.util.text.UtilText;
import jhelp.util.thread.Filter;
import jhelp.util.thread.Future;
import jhelp.util.thread.Task;
import jhelp.util.thread.ConsumerTask;

/**
 * Set of elements, where elements have no order
 *
 * @param <T> Elements type
 * @author JHelp
 */
public class ThrowSet<T> implements ParallelList<T, ThrowSet<T>>
{
    /**
     * Elements list
     */
    private final ArrayList<T> set;

    /**
     * Create a new instance of ThrowSet
     */
    public ThrowSet()
    {
        this.set = new ArrayList<>();
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
    public ThrowSet<T> forEach(final ConsumerTask<T> task, final Filter<T> filter)
    {
        ForEach.forEach(this.set, task, filter);
        return this;
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
    public ThrowSet<T> sync(final ConsumerTask<T> task, final Filter<T> filter)
    {
        ForEach.sync(this.set, task, filter);
        return this;
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
        return ForEach.async(this.set, task, filter);
    }

    /**
     * Create list composed of filtered elements of the list
     *
     * @param filter Filter to select elements
     * @return List of filtered elements
     */
    @Override
    public ThrowSet<T> filter(final Filter<T> filter)
    {
        final ThrowSet<T> throwSet = new ThrowSet<>();
        this.consume(throwSet::throwElement, filter);
        return throwSet;
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
        return Future.firstMatch(this.set, filter);
    }

    /**
     * Indicates if set is empty
     *
     * @return {@code true} if set is empty
     */
    public boolean isEmpty()
    {
        return this.set.isEmpty();
    }

    /**
     * Set size
     *
     * @return Number of elements
     */
    public int size()
    {
        return this.set.size();
    }

    /**
     * Take an element from the set.<br>
     * The element is removed from the set
     *
     * @return An element
     */
    public T take()
    {
        final int size = this.set.size();

        if (size == 0)
        {
            throw new IllegalStateException("ThrowSet is empty");
        }

        final int index = (int) (Math.random() * size);

        final T element = this.set.get(index);
        this.set.remove(index);

        return element;
    }

    /**
     * Add an element to the set
     *
     * @param element Element to add
     */
    public void throwElement(final T element)
    {
        final int size = this.set.size();

        if (size == 0)
        {
            this.set.add(element);

            return;
        }

        this.set.add((int) (Math.random() * size), element);
    }

    /**
     * Return the throw set to an array of element
     *
     * @param array Array to store the result
     * @return Array of elements
     */
    public T[] toArray(final T[] array)
    {
        return this.set.toArray(array);
    }

    /**
     * String representation of the throw set <br>
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
        return UtilText.concatenate("ThrowSet:", this.set);
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
        final ThrowSet<R> throwSet = new ThrowSet<>();

        for (T element : this.set)
        {
            if (filter == null || filter.isFiltered(element))
            {
                throwSet.throwElement(task.playTask(element));
            }
        }

        return throwSet;
    }

    /**
     * Execute task for each element (that respects the given filter) of the list
     *
     * @param task   Task to do
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @return This list itself, convenient for chaining
     */
    @Override
    public ThrowSet<T> consume(final ConsumerTask<T> task, final Filter<T> filter)
    {
        for (T element : this.set)
        {
            if (filter == null || filter.isFiltered(element))
            {
                task.consume(element);
            }
        }

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
    public <R> ParallelList<R, ?> flatMap(
            final Task<T, ParallelList<R, ?>> task, final Filter<T> filter)
    {
        final ThrowSet<R>     throwSet = new ThrowSet<>();
        final ConsumerTask<R> add      = throwSet::throwElement;

        for (T element : this.set)
        {
            if (filter == null || filter.isFiltered(element))
            {
                task.playTask(element).consume(add);
            }
        }

        return throwSet;
    }

}
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
import java.util.Iterator;
import java.util.Objects;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.Filter;
import jhelp.util.thread.Future;
import jhelp.util.thread.Task;
import jhelp.util.thread.ThreadManager;

/**
 * A queue of elements
 */
public final class Queue<T> implements ParallelList<T, Queue<T>>, SizedIterable<T>
{
    /**
     * Head of queue
     */
    private QueueElement<T> head;
    /**
     * Queue of queue
     */
    private QueueElement<T> queue;
    /**
     * Queue size
     */
    private int             size;

    /**
     * Create empty queue
     */
    public Queue()
    {
        this.size = 0;
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
        return Future.launch(element ->
                             {
                                 while (element != null)
                                 {
                                     if (filter.isFiltered(element.element))
                                     {
                                         return element.element;
                                     }

                                     element = element.next;
                                 }

                                 throw new RuntimeException("Element not found!");
                             }, this.head);
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
        final ArrayObject<Future<Void>> futures      = new ArrayObject<>();
        QueueElement<T>                 queueElement = this.head;

        while (queueElement != null)
        {
            if (filter == null || filter.isFiltered(queueElement.element))
            {
                futures.add(Future.launchConsume(task, queueElement.element));
            }

            queueElement = queueElement.next;
        }

        return Future.waitAll(futures.toArray(new Future[futures.size()])).thenConsume(ignored ->
                                                                                       {
                                                                                       });
    }

    /**
     * Execute task for each element (that respects the given filter) of the list
     *
     * @param task   Task to do
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @return This list itself, convenient for chaining
     */
    @Override
    public Queue<T> consume(final ConsumerTask<T> task, final Filter<T> filter)
    {
        QueueElement<T> queueElement = this.head;

        while (queueElement != null)
        {
            if (filter == null || filter.isFiltered(queueElement.element))
            {
                task.consume(queueElement.element);
            }

            queueElement = queueElement.next;
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
    public Queue<T> filter(final Filter<T> filter)
    {
        Queue<T> queue = new Queue<>();
        this.consume(queue::inQueue, filter);
        return queue;
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
        final Queue<R>        queue        = new Queue<>();
        final ConsumerTask<R> add          = queue::inQueue;
        QueueElement<T>       queueElement = this.head;

        while (queueElement != null)
        {
            if (filter == null || filter.isFiltered(queueElement.element))
            {
                task.playTask(queueElement.element).consume(add);
            }

            queueElement = queueElement.next;
        }

        return queue;
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
    public Queue<T> forEach(final ConsumerTask<T> task, final Filter<T> filter)
    {
        ThreadManager.parallel(element ->
                               {
                                   while (element != null)
                                   {
                                       if (filter == null || filter.isFiltered(element.element))
                                       {
                                           ThreadManager.parallel(task, element.element);
                                       }

                                       element = element.next;
                                   }
                               }, this.head);
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
        final Queue<R>  queue        = new Queue<>();
        QueueElement<T> queueElement = this.head;

        while (queueElement != null)
        {
            if (filter == null || filter.isFiltered(queueElement.element))
            {
                queue.inQueue(task.playTask(queueElement.element));
            }

            queueElement = queueElement.next;
        }

        return queue;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator()
    {
        return new QueueIterator<>(this.head);
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
    public Queue<T> sync(final ConsumerTask<T> task, final Filter<T> filter)
    {
        this.async(task, filter).waitFinish();
        return this;
    }

    /**
     * Clear the queue
     */
    public void clear()
    {
        this.head = null;
        this.queue = null;
        this.size = 0;
    }

    /**
     * Indicates if queue is empty
     *
     * @return {@code true} if queue is empty
     */
    public boolean empty()
    {
        return this.head == null;
    }

    /**
     * Add element at last position of queue
     *
     * @param element Element to add
     */
    public void inQueue(@NotNull T element)
    {
        Objects.requireNonNull(element, "element MUST NOT be null!");

        if (this.head == null)
        {
            this.head = new QueueElement(element);
            this.queue = this.head;
            this.size = 1;
            return;
        }

        this.queue.next = new QueueElement(element);
        this.queue = this.queue.next;
        this.size++;
    }

    @Override
    public StreamIterator<T> streamIterator()
    {
        return StreamIterator.from(this);
    }

    /**
     * Get first element of the queue and remove it from the queue
     *
     * @return First element of the queue
     * @throws IllegalStateException If queue is empty
     */
    public @NotNull T outQueue()
    {
        if (this.head == null)
        {
            throw new IllegalStateException("Queue is empty!");
        }

        T element = this.head.element;
        this.head = this.head.next;

        if (this.head == null)
        {
            this.queue = null;
        }

        this.size--;
        return element;
    }

    /**
     * Get first element of the queue and remove it from the queue.<br>
     * If the queue is empty, it returns {@code null}
     *
     * @return First element of the queue OR {@code null} if queue is empty
     */
    public @Nullable T outQueueOrNull()
    {
        if (this.empty())
        {
            return null;
        }

        return this.outQueue();
    }

    /**
     * Look first element of the queue
     *
     * @return First element of the queue
     * @throws IllegalStateException If queue is empty
     */
    public @NotNull T peek()
    {
        if (this.head == null)
        {
            throw new IllegalStateException("Queue is empty!");
        }

        return this.head.element;
    }

    /**
     * Look first element of the queue<br>
     * If the queue is empty, it returns {@code null}
     *
     * @return First element of the queue OR {@code null} if queue is empty
     */
    public @Nullable T peekOrNull()
    {
        if (this.empty())
        {
            return null;
        }

        return this.peek();
    }

    /**
     * Queue size
     *
     * @return Queue size
     */
    public int size()
    {
        return this.size;
    }
}

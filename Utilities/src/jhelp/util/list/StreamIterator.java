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
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.Filter;
import jhelp.util.thread.Future;
import jhelp.util.thread.FutureStatus;
import jhelp.util.thread.Promise;
import jhelp.util.thread.Task;
import jhelp.util.thread.TaskException;
import jhelp.util.thread.ThreadManager;

public class StreamIterator<T> implements Iterator<T>, Iterable<T>
{
    public static <T> StreamIterator<T> from(Iterable<T> iterable)
    {
        return StreamIterator.from(iterable.iterator());
    }

    public static <T> StreamIterator<T> from(Iterator<T> iterator)
    {
        Objects.requireNonNull(iterator, "iterator MUST NOT be null!");
        final StreamIterator<T> streamIterator = new StreamIterator<>();
        ThreadManager.parallel(pair ->
                               {
                                   while (pair.first.hasNext())
                                   {
                                       pair.second.add(pair.first.next());
                                   }

                                   pair.second.endFill();
                               }, new Pair<>(iterator, streamIterator));
        return streamIterator;
    }

    private final AtomicBoolean chained;
    private final AtomicBoolean filling;
    private final Queue<T>      queue;

    private StreamIterator()
    {
        this.queue = new Queue<>();
        this.filling = new AtomicBoolean(true);
        this.chained = new AtomicBoolean(false);
    }

    private void chain()
    {
        synchronized (this.chained)
        {
            this.checkNotChained();
            this.chained.set(true);
        }
    }

    private void checkNotChained()
    {
        if (this.chained.get())
        {
            throw new IllegalStateException("Can't do this operation on chained StreamIterator");
        }
    }

    void add(T element)
    {
        Objects.requireNonNull(element, "element MUST NOT be null!");

        synchronized (this.filling)
        {
            this.queue.inQueue(element);
            this.filling.notifyAll();
        }
    }

    void endFill()
    {
        synchronized (this.filling)
        {
            this.filling.set(false);
            this.filling.notifyAll();
        }
    }

    public Future<Boolean> allMatch(Filter<T> filter)
    {
        return this.noneMatch(filter.negate());
    }

    public Future<Boolean> anyMatch(Filter<T> filter)
    {
        return this.firstMatch(filter).thenDo(future -> future.status() == FutureStatus.RESULT);
    }

    public boolean chained()
    {
        return this.chained.get();
    }

    public StreamIterator<T> consume(ConsumerTask<T> consumerTask)
    {
        return this.consume(consumerTask, null);
    }

    public StreamIterator<T> consume(ConsumerTask<T> consumerTask, Filter<T> filter)
    {
        return this.map(element ->
                        {
                            consumerTask.consume(element);
                            return element;
                        }, filter);
    }

    public StreamIterator<T> filter(Filter<T> filter)
    {
        this.chain();
        final StreamIterator<T> streamIterator = new StreamIterator<>();
        ThreadManager.parallel(() ->
                               {
                                   synchronized (this.filling)
                                   {
                                       T data;

                                       while (this.filling.get() || !this.queue.empty())
                                       {
                                           while (this.filling.get() && this.queue.empty())
                                           {
                                               try
                                               {
                                                   this.filling.wait();
                                               }
                                               catch (Exception ignored)
                                               {
                                               }
                                           }

                                           if (this.queue.empty())
                                           {
                                               streamIterator.endFill();
                                               return;
                                           }

                                           data = this.queue.outQueue();

                                           if (filter.isFiltered(data))
                                           {
                                               streamIterator.add(data);
                                           }
                                       }

                                       streamIterator.endFill();
                                   }
                               });
        return streamIterator;
    }

    public Future<T> firstMatch(Filter<T> filter)
    {
        this.chain();
        final Promise<T> promise = new Promise<>();
        ThreadManager.parallel(() ->
                               {
                                   synchronized (this.filling)
                                   {
                                       T data;

                                       while (this.filling.get() || !this.queue.empty())
                                       {
                                           while (this.filling.get() && this.queue.empty())
                                           {
                                               try
                                               {
                                                   this.filling.wait();
                                               }
                                               catch (Exception ignored)
                                               {
                                               }
                                           }

                                           if (this.queue.empty())
                                           {
                                               promise.setError(new TaskException("No element found"));
                                               return;
                                           }

                                           data = this.queue.outQueue();

                                           if (filter.isFiltered(data))
                                           {
                                               promise.setResult(data);
                                               return;
                                           }
                                       }

                                       promise.setError(new TaskException("No element found"));
                                   }
                               });
        return promise.future();
    }

    public <T1> StreamIterator<T1> flatMap(
            Task<T, StreamIterator<T1>> task)
    {
        return this.flatMap(task, null, null);
    }

    public <T1> StreamIterator<T1> flatMap(
            Task<T, StreamIterator<T1>> task, Filter<T> filterSource)
    {
        return this.flatMap(task, filterSource, null);
    }

    public <T1> StreamIterator<T1> flatMap(
            Task<T, StreamIterator<T1>> task, Filter<T> filterSource, Filter<T1> filterDestination)
    {
        this.chain();
        final StreamIterator<T1> streamIterator = new StreamIterator<>();
        ThreadManager.parallel(() ->
                               {
                                   synchronized (this.filling)
                                   {
                                       StreamIterator<T1> streamIterator1;
                                       T                  dataSource;
                                       T1                 dataDestination;

                                       while (this.filling.get() || !this.queue.empty())
                                       {
                                           while (this.filling.get() && this.queue.empty())
                                           {
                                               try
                                               {
                                                   this.filling.wait();
                                               }
                                               catch (Exception ignored)
                                               {
                                               }
                                           }

                                           if (this.queue.empty())
                                           {
                                               streamIterator.endFill();
                                               return;
                                           }

                                           dataSource = this.queue.outQueue();

                                           if (filterSource == null || filterSource.isFiltered(dataSource))
                                           {
                                               streamIterator1 = task.playTask(dataSource);

                                               if (streamIterator1 != null)
                                               {
                                                   while (streamIterator1.hasNext())
                                                   {
                                                       dataDestination = streamIterator1.next();

                                                       if (filterDestination == null ||
                                                           filterDestination.isFiltered(dataDestination))
                                                       {
                                                           streamIterator.add(dataDestination);
                                                       }
                                                   }
                                               }
                                           }
                                       }

                                       streamIterator.endFill();
                                   }
                               });
        return streamIterator;
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
        this.checkNotChained();

        synchronized (this.filling)
        {
            while (this.filling.get() && this.queue.empty())
            {
                try
                {
                    this.filling.wait();
                }
                catch (Exception ignored)
                {
                }
            }

            return !this.queue.empty();
        }
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
        this.checkNotChained();

        synchronized (this.filling)
        {
            while (this.filling.get() && this.queue.empty())
            {
                try
                {
                    this.filling.wait();
                }
                catch (Exception ignored)
                {
                }
            }

            if (this.queue.empty())
            {
                throw new NoSuchElementException("No more elements");
            }

            return this.queue.outQueue();
        }
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator()
    {
        this.checkNotChained();
        return this;
    }

    public <T1> StreamIterator<T1> map(Task<T, T1> task)
    {
        return this.map(task, null, null);
    }

    public <T1> StreamIterator<T1> map(Task<T, T1> task, Filter<T> filterSource)
    {
        return this.map(task, filterSource, null);
    }

    public <T1> StreamIterator<T1> map(Task<T, T1> task, Filter<T> filterSource, Filter<T1> filterDestination)
    {
        this.chain();
        final StreamIterator<T1> streamIterator = new StreamIterator<>();
        ThreadManager.parallel(() ->
                               {
                                   synchronized (this.filling)
                                   {
                                       T  dataSource;
                                       T1 dataDestination;

                                       while (this.filling.get() || !this.queue.empty())
                                       {
                                           while (this.filling.get() && this.queue.empty())
                                           {
                                               try
                                               {
                                                   this.filling.wait();
                                               }
                                               catch (Exception ignored)
                                               {
                                               }
                                           }

                                           if (this.queue.empty())
                                           {
                                               streamIterator.endFill();
                                               return;
                                           }

                                           dataSource = this.queue.outQueue();

                                           if (filterSource == null || filterSource.isFiltered(dataSource))
                                           {
                                               dataDestination = task.playTask(dataSource);

                                               if (filterDestination == null ||
                                                   filterDestination.isFiltered(dataDestination))
                                               {
                                                   streamIterator.add(dataDestination);
                                               }
                                           }
                                       }

                                       streamIterator.endFill();
                                   }
                               });
        return streamIterator;
    }

    public Future<Boolean> noneMatch(Filter<T> filter)
    {
        return this.anyMatch(filter).andDo(succeed -> !succeed);
    }

    public Future<ArrayObject<T>> toArray()
    {
        this.chain();
        final Promise<ArrayObject<T>> promise = new Promise<>();
        ThreadManager.parallel(() ->
                               {
                                   synchronized (this.filling)
                                   {
                                       final ArrayObject<T> arrayObject = new ArrayObject<>();

                                       while (this.filling.get() || !this.queue.empty())
                                       {
                                           while (this.filling.get() && this.queue.empty())
                                           {
                                               try
                                               {
                                                   this.filling.wait();
                                               }
                                               catch (Exception ignored)
                                               {
                                               }
                                           }

                                           if (this.queue.empty())
                                           {
                                               promise.setResult(arrayObject);
                                               return;
                                           }

                                           arrayObject.add(this.queue.outQueue());
                                       }

                                       promise.setResult(arrayObject);
                                   }
                               });
        return promise.future();
    }

    public StreamIterator<T> waitFinish()
    {
        synchronized (this.filling)
        {
            while (this.filling.get())
            {
                try
                {
                    this.filling.wait();
                }
                catch (Exception ignored)
                {
                }
            }
        }

        return this;
    }
}

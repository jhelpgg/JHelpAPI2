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

package jhelp.util.thread;

import java.util.Objects;

/**
 * Created by jhelp on 22/07/17.
 */
public final class Pointer<T>
{
    private T     data;
    private Mutex mutex;

    public Pointer()
    {
    }

    public Pointer(T data)
    {
        this.data = data;
    }

    public T data()
    {
        return this.mutex.playInCriticalSection(() -> this.data);
    }

    public void data(final T data)
    {
        this.mutex.playInCriticalSectionVoid(data1 -> this.data = data1, data);
    }

    public Pointer<T> filter(final Filter<T> filter)
    {
        Objects.requireNonNull(filter, "filter MUST NOT be null!");

        this.mutex.playInCriticalSectionVoid(filter1 ->
                                             {
                                                 if (this.data == null || !filter1.isFiltered(this.data))
                                                 {
                                                     this.data = null;
                                                 }
                                             }, filter);

        return this;
    }

    public <R> Pointer<R> ifNotNullCombine(final Task<T, Pointer<R>> task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");

        return this.mutex.playInCriticalSection(task1 ->
                                                {
                                                    if (this.data == null)
                                                    {
                                                        return new Pointer<>();
                                                    }

                                                    Pointer<R> pointer = task1.playTask(this.data);

                                                    if (pointer == null)
                                                    {
                                                        return new Pointer<>();
                                                    }

                                                    return pointer;
                                                }, task);

    }

    public void ifNotNullConsume(ConsumerTask<T> task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");

        this.mutex.playInCriticalSectionVoid(task1 ->
                                             {
                                                 if (this.data != null)
                                                 {
                                                     task1.consume(this.data);
                                                 }
                                             }, task);
    }

    public <R> Pointer<R> ifNotNullDo(Task<T, R> task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");

        return this.mutex.playInCriticalSection(task1 ->
                                                {
                                                    if (this.data == null)
                                                    {
                                                        return new Pointer<>();
                                                    }

                                                    return new Pointer<>(task1.playTask(this.data));
                                                }, task);
    }

    public <R> Pointer<R> ifNotNullProduce(ProducerTask<R> task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");

        return this.mutex.playInCriticalSection(task1 ->
                                                {
                                                    if (this.data == null)
                                                    {
                                                        return new Pointer<>();
                                                    }

                                                    return new Pointer<>(task1.produce());
                                                }, task);
    }

    public void ifNotNullRun(RunnableTask task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");

        this.mutex.playInCriticalSectionVoid(task1 ->
                                             {
                                                 if (this.data != null)
                                                 {
                                                     task1.run();
                                                 }
                                             }, task);
    }

    public Pointer<T> orElse(T data)
    {
        this.mutex.playInCriticalSectionVoid(data1 ->
                                             {
                                                 if (this.data == null)
                                                 {
                                                     this.data = data1;
                                                 }
                                             }, data);

        return this;
    }

    public Pointer<T> orElse(ProducerTask<T> task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");

        this.mutex.playInCriticalSectionVoid(task1 ->
                                             {
                                                 if (this.data == null)
                                                 {
                                                     this.data = task1.produce();
                                                 }
                                             }, task);

        return this;
    }
}

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

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.Objects;

/**
 * Pointer on an object
 */
public final class Pointer<T>
{
    /**
     * Pointed value
     */
    private       T     data;
    /**
     * Synchronization mutex
     */
    private final Mutex mutex;

    /**
     * Create an empty pointer
     */
    public Pointer()
    {
        this.mutex = new Mutex();
    }

    /**
     * Create pointer on an object
     *
     * @param data Pointed object
     */
    public Pointer(@Nullable T data)
    {
        this();
        this.data = data;
    }

    /**
     * Pointed object
     *
     * @return Pointed object
     */
    public @Nullable T data()
    {
        return this.mutex.playInCriticalSection(() -> this.data);
    }

    /**
     * Change pointed object
     *
     * @param data New object
     */
    public void data(final @Nullable T data)
    {
        this.mutex.playInCriticalSectionVoid(data1 -> this.data = data1, data);
    }

    /**
     * Remove the object pointed if the pointed object not match a given filter
     *
     * @param filter Filter to apply
     * @return This pointer, convenient for chaining
     */
    public @NotNull Pointer<T> filter(final @NotNull Filter<T> filter)
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

    /**
     * If the pointed object is not {@code null}, it returns a pointer with the value obtain on applying the given task
     * to this pointed value.<br>
     * If the pointed object is {@code null}, it return an empty pointer
     *
     * @param task Task to apply
     * @param <R>  Task result type
     * @return Computed pointer
     */
    public @NotNull <R> Pointer<R> ifNotNullCombine(final @NotNull Task<T, Pointer<R>> task)
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

    /**
     * If the pointed object is not {@code null}, it call the given task on it.<br>
     * If the pointed object is{@code null}, nothing happen
     *
     * @param task Task to play
     */
    public void ifNotNullConsume(@NotNull ConsumerTask<T> task)
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

    /**
     * If the pointed object is not {@code null}, it returns a pointer with the value obtain on applying the given task
     * to this pointed value.<br>
     * If the pointed object is {@code null}, it return an empty pointer
     *
     * @param task Task to apply
     * @param <R>  Task result type
     * @return Computed pointer
     */
    public @NotNull <R> Pointer<R> ifNotNullDo(@NotNull Task<T, R> task)
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

    /**
     * If the pointed object is not {@code null}, it returns a pointer with the value of the producer.<br>
     * If the pointed object is {@code null}, it return an empty pointer
     *
     * @param task Task to apply
     * @param <R>  Task result type
     * @return Computed pointer
     */
    public @NotNull <R> Pointer<R> ifNotNullProduce(@NotNull ProducerTask<R> task)
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

    /**
     * If the pointed object is not {@code null}, it call the given task.<br>
     * If the pointed object is{@code null}, nothing happen
     *
     * @param task Task to play
     */
    public void ifNotNullRun(@NotNull RunnableTask task)
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

    /**
     * If the pointed value is {@code null} it is replace by given one.<br>
     * Else nothing happen.
     *
     * @param data New pointed object if current one is {@code null}
     * @return This pointer, convenient for chaining
     */
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

    /**
     * If the pointed value is {@code null} it is replace by the producer value.<br>
     * Else nothing happen.
     *
     * @param task Object value producer
     * @return This pointer, convenient for chaining
     */
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

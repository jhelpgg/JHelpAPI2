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
import java.util.Collection;
import java.util.Objects;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.Filter;
import jhelp.util.thread.Future;
import jhelp.util.thread.FutureStatus;
import jhelp.util.thread.Task;

/**
 * List of elements can be crawl in parallel
 *
 * @param <T> List elements' type
 * @param <P> List instance type
 */
public interface ParallelList<T, P extends ParallelList<T, P>>
{
    /**
     * Transform a collection to a parallel list
     *
     * @param collection Collection to transform
     * @param <T1>       Collection elements' type
     * @return Result parallel list
     */
    static @NotNull <T1> ParallelList<T1, CollectionParallel<T1>> toParallelList(@NotNull Collection<T1> collection)
    {
        Objects.requireNonNull(collection, "collection MUST NOT be null!");

        if (collection instanceof CollectionParallel)
        {
            return (CollectionParallel<T1>) collection;
        }

        return new CollectionParallel<>(collection);
    }

    /**
     * Indicates if all element of the list match to given filter.<br>
     * The only case where result future is on error, is if the filter throw an exception while evaluating an element.
     * That should not happen. So use {@link Future#value()} is safe
     *
     * @param filter Filter to use
     * @return Future to know if all element match or not
     */
    default @NotNull Future<Boolean> allMatch(@NotNull Filter<T> filter)
    {
        Objects.requireNonNull(filter, "filter MUST NOT be null!");
        return this.noneMatch(filter.negate());
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
    @NotNull Future<T> any(@NotNull Filter<T> filter);

    /**
     * Indicates if at least one element of the list match to given filter.<br>
     * The only case where result future is on error, is if the filter throw an exception while evaluating an element.
     * That should not happen. So use {@link Future#value()} is safe
     *
     * @param filter Filter to use
     * @return Future to know if an element match or not
     */
    default @NotNull Future<Boolean> anyMatch(@NotNull Filter<T> filter)
    {
        Objects.requireNonNull(filter, "filter MUST NOT be null!");
        return this.any(filter).thenDo(future -> future.status() == FutureStatus.RESULT);
    }

    /**
     * Execute a task in parallel on each element of the list.<br>
     * Tasks are launch, and method return immediately<br>
     * Note:
     * <ul>
     * <li> Their no guarantee about the order of elements' list meet by the task. If order mandatory use {@link #consumeAsync(ConsumerTask)}</li>
     * <li> Since order is not important, thread management is simplified, so it is faster than {@link #consumeAsync(ConsumerTask)} </li>
     * </ul>
     *
     * @param task Task to play
     * @return Future to track/link to the end of all parallel tasks
     */
    default @NotNull Future<Void> async(@NotNull ConsumerTask<T> task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        return this.async(task, null);
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
    @NotNull Future<Void> async(@NotNull ConsumerTask<T> task, @Nullable Filter<T> filter);

    /**
     * Execute task for each element (that respects the given filter) of the list
     *
     * @param task   Task to do
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @return This list itself, convenient for chaining
     */
    @NotNull P consume(@NotNull ConsumerTask<T> task, @Nullable Filter<T> filter);

    /**
     * Execute task for each element of the list
     *
     * @param task Task to do
     * @return This list itself, convenient for chaining
     */
    default @NotNull P consume(@NotNull ConsumerTask<T> task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        return this.consume(task, null);
    }

    /**
     * Execute task for each element (that respects the given filter) of the list<br>
     * The execution is do in a separate thread, the execution is launch and this method returns immediately
     *
     * @param task   Task to do
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @return Future link to the execution
     */
    default @NotNull Future<Void> consumeAsync(@NotNull ConsumerTask<T> task, @Nullable Filter<T> filter)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        return Future.launchConsume(pair -> this.consume(pair.first, pair.second), new Pair<>(task, filter));
    }

    /**
     * Execute task for each element  of the list<br>
     * The execution is do in a separate thread, the execution is launch and this method returns immediately
     *
     * @param task Task to do
     * @return Future link to the execution
     */
    default @NotNull Future<Void> consumeAsync(@NotNull ConsumerTask<T> task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        return this.consumeAsync(task, null);
    }

    /**
     * Create list composed of filtered elements of the list
     *
     * @param filter Filter to select elements
     * @return List of filtered elements
     */
    @NotNull P filter(@NotNull Filter<T> filter);

    /**
     * Create list composed of filtered elements of the list.<br>
     * The creation is do in a separate thread, the creation is launch and this method returns immediately
     *
     * @param filter Filter to select elements
     * @return Future link to the list creation
     */
    default @NotNull Future<P> filterAsync(@NotNull Filter<T> filter)
    {
        Objects.requireNonNull(filter, "filter MUST NOT be null!");
        return Future.launch(this::filter, filter);
    }

    /**
     * Execute task for each element (that respects the given filter) of the list and collect the result in other list
     *
     * @param task   Task to do
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @param <R>    Result list element type
     * @return List with transformed elements
     */
    @NotNull <R> ParallelList<R, ?> flatMap(@NotNull Task<T, ParallelList<R, ?>> task, @Nullable Filter<T> filter);

    /**
     * Execute task for each element of the list and collect the result in other list
     *
     * @param task Task to do
     * @param <R>  Result list element type
     * @return List with transformed elements
     */
    default @NotNull <R> ParallelList<R, ?> flatMap(@NotNull Task<T, ParallelList<R, ?>> task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        return this.flatMap(task, null);
    }

    /**
     * Execute task for each element (that respects the given filter) of the list and collect the result in other list<br>
     * The execution is do in a separate thread, the execution is launch and this method returns immediately
     *
     * @param task   Task to do
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @param <R>    Result list element type
     * @return Future link to the list creation
     */
    default @NotNull <R> Future<ParallelList<R, ?>> flatMapAsync(
            @NotNull Task<T, ParallelList<R, ?>> task, @Nullable Filter<T> filter)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        //pair -> this.flatMap(pair.first, pair.second)
        return Future.launch(
                (Task<Pair<Task<T, ParallelList<R, ?>>, Filter<T>>, ParallelList<R, ?>>) pair ->
                        ParallelList.this.flatMap(pair.first, pair.second)
                , new Pair<>(task, filter));
    }

    /**
     * Execute task for each element of the list and collect the result in other list<br>
     * The execution is do in a separate thread, the execution is launch and this method returns immediately
     *
     * @param task Task to do
     * @param <R>  Result list element type
     * @return Future link to the list creation
     */
    default @NotNull <R> Future<ParallelList<R, ?>> flatMapAsync(@NotNull Task<T, ParallelList<R, ?>> task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        return this.flatMapAsync(task, null);
    }

    /**
     * Execute a task in parallel on each element of the list.<br>
     * Tasks are launch, and method return immediately<br>
     * Note:
     * <ul>
     * <li> Their no guarantee about the order of elements' list meet by the task. If order mandatory use {@link #consumeAsync(ConsumerTask)}</li>
     * <li> Since order is not important, thread management is simplified, so it is faster than {@link #consumeAsync(ConsumerTask)} </li>
     * </ul>
     *
     * @param task Task to play
     * @return The list itself, convenient for chaining
     */
    default @NotNull P forEach(@NotNull ConsumerTask<T> task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        return this.forEach(task, null);
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
    @NotNull P forEach(@NotNull ConsumerTask<T> task, @Nullable Filter<T> filter);

    /**
     * Launch a task if all elements match the given filter.
     *
     * @param condition Filter to use
     * @param task      Task to do. I will receive the all list as parameter
     * @return This list itself, convenient for chaining
     */
    default @NotNull ParallelList<T, P> ifAllThen(
            @NotNull Filter<T> condition, @NotNull ConsumerTask<ParallelList<T, P>> task)
    {
        Objects.requireNonNull(condition, "condition MUST NOT be null!");
        Objects.requireNonNull(task, "task MUST NOT be null!");
        this.allMatch(condition).andConsume(result ->
                                            {
                                                if (result)
                                                {
                                                    task.consume(this);
                                                }
                                            });
        return this;
    }

    /**
     * Apply a function to first element match the given filter.<br>
     * The result future is on error if no element found
     *
     * @param condition Filter to use
     * @param task      Function to apply on found element. It will receive the found element
     * @param <R>       Function result type
     * @return Future link to transformed result
     */
    default @NotNull <R> Future<R> ifAnyMap(@NotNull Filter<T> condition, @NotNull Task<T, R> task)
    {
        Objects.requireNonNull(condition, "condition MUST NOT be null!");
        Objects.requireNonNull(task, "task MUST NOT be null!");
        return this.any(condition).andDo(task);
    }

    /**
     * Launch a task on first element  match the given filter.<br>
     * The task is launched only if their at least one element match, and launch only on first found.
     *
     * @param condition Filter to use
     * @param task      Task to do. It will receive the found element
     * @return This list itself, convenient for chaining
     */
    default @NotNull ParallelList<T, P> ifAnyThen(@NotNull Filter<T> condition, @NotNull ConsumerTask<T> task)
    {
        Objects.requireNonNull(condition, "condition MUST NOT be null!");
        Objects.requireNonNull(task, "task MUST NOT be null!");
        this.any(condition).andConsume(task);
        return this;
    }

    /**
     * Launch a task if none element match the given filter.
     *
     * @param condition Filter to use
     * @param task      Task to do. I will receive the all list as parameter
     * @return This list itself, convenient for chaining
     */
    default @NotNull ParallelList<T, P> ifNoneThen(
            @NotNull Filter<T> condition, @NotNull ConsumerTask<ParallelList<T, P>> task)
    {
        Objects.requireNonNull(condition, "condition MUST NOT be null!");
        Objects.requireNonNull(task, "task MUST NOT be null!");
        this.noneMatch(condition).andConsume(result ->
                                             {
                                                 if (result)
                                                 {
                                                     task.consume(this);
                                                 }
                                             });
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
    @NotNull <R> ParallelList<R, ?> map(@NotNull Task<T, R> task, @Nullable Filter<T> filter);

    /**
     * Execute task for each element of the list and collect the result in other list
     *
     * @param task Task to do
     * @param <R>  Result list element type
     * @return List with transformed elements
     */
    default @NotNull <R> ParallelList<R, ?> map(@NotNull Task<T, R> task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        return this.map(task, null);
    }

    /**
     * Execute task for each element (that respects the given filter) of the list and collect the result in other list<br>
     * The execution is do in a separate thread, the execution is launch and this method returns immediately
     *
     * @param task   Task to do
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @param <R>    Result list element type
     * @return Future link to the list creation
     */
    default @NotNull <R> Future<ParallelList<R, ?>> mapAsync(@NotNull Task<T, R> task, @Nullable Filter<T> filter)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        return Future.launch((Task<Pair<Task<T, R>, Filter<T>>, ParallelList<R, ?>>) pair ->
                                     ParallelList.this.map(pair.first, pair.second),
                             new Pair<>(task, filter));
    }

    /**
     * Execute task for each element of the list and collect the result in other list<br>
     * The execution is do in a separate thread, the execution is launch and this method returns immediately
     *
     * @param task Task to do
     * @param <R>  Result list element type
     * @return Future link to the list creation
     */
    default @NotNull <R> Future<ParallelList<R, ?>> mapAsync(@NotNull Task<T, R> task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        return this.mapAsync(task, null);
    }

    /**
     * Indicates if none element of the list match to given filter.<br>
     * The only case where result future is on error, is if the filter throw an exception while evaluating an element.
     * That should not happen. So use {@link Future#value()} is safe
     *
     * @param filter Filter to use
     * @return Future to know if none element match or not
     */
    default @NotNull Future<Boolean> noneMatch(@NotNull Filter<T> filter)
    {
        Objects.requireNonNull(filter, "filter MUST NOT be null!");
        return this.anyMatch(filter).andDo(result -> !result);
    }

    StreamIterator<T> streamIterator();

    /**
     * Execute a task in parallel on each element of the list.<br>
     * The method will wait all parallel task finished before return<br>
     * Note:
     * <ul>
     * <li> Their no guarantee about the order of elements' list meet by the task. If order mandatory use {@link #consumeAsync(ConsumerTask)} and {@link Future#waitFinish()}</li>
     * <li> Since order is not important, thread management is simplified, so it is faster than {@link #consumeAsync(ConsumerTask)} and {@link Future#waitFinish()} </li>
     * </ul>
     *
     * @param task Task to play
     * @return The list itself, convenient for chaining
     */
    default @NotNull P sync(@NotNull ConsumerTask<T> task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        return this.sync(task, null);
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
    @NotNull P sync(@NotNull ConsumerTask<T> task, @Nullable Filter<T> filter);
}

package jhelp.util.list;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import jhelp.util.thread.Filter;
import jhelp.util.thread.Future;
import jhelp.util.thread.Promise;
import jhelp.util.thread.Task;
import jhelp.util.thread.TaskException;
import jhelp.util.thread.ThreadManager;
import jhelp.util.thread.ConsumerTask;

/**
 * Tools for execute a task in parallel for each element of a collection, iterable, iterator, enumeration or array.<br>
 * The sync methods are recommended if you want launch the tasks in parallel and wait that all finished before the following code.<br>
 * The async methods are recommended if you want launch the tasks in parallel, return immediately,
 * but have the possibility to track the finished, link task to do after the tasks, ...<br>
 * The foreach methods are recommended if you want launch the tasks in parallel and don't care when it is really finished.<br>
 * Note: foreach methods are little bit more faster and take less memory than async methods.
 * So if you don't need track when tasks are really finished, forEach methods are the best choice.
 */
public class ForEach
{
    /**
     * Execute a task in parallel on each element.<br>
     * Tasks are launch, and method return immediately.<br>
     * The returned Future allow you to track/link to the end of all parallel tasks
     *
     * @param collection Collection over elements
     * @param task       Task to play
     * @param <T>        Elements type
     * @return Future to track/link to the end of all parallel tasks
     */
    public static @NotNull <T> Future<Void> async(@NotNull Collection<T> collection, @NotNull ConsumerTask<T> task)
    {
        return ForEach.async(collection.size(), collection, task, null);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * Tasks are launch, and method return immediately.<br>
     * The returned Future allow you to track/link to the end of all parallel tasks
     *
     * @param collection Collection over elements
     * @param task       Task to play
     * @param filter     Filter on elements. Only filtered elements are given to the task
     * @param <T>        Elements type
     * @return Future to track/link to the end of all parallel tasks
     */
    public static @NotNull <T> Future<Void> async(
            @NotNull Collection<T> collection, @NotNull ConsumerTask<T> task, @Nullable Filter<T> filter)
    {
        return ForEach.async(collection.size(), collection, task, filter);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * Tasks are launch, and method return immediately.<br>
     * The returned Future allow you to track/link to the end of all parallel tasks
     *
     * @param sizedIterable Iterable over elements
     * @param task          Task to play
     * @param <T>           Elements type
     * @return Future to track/link to the end of all parallel tasks
     */
    public static @NotNull <T> Future<Void> async(@NotNull SizedIterable<T> sizedIterable, @NotNull ConsumerTask<T> task)
    {
        return ForEach.async(sizedIterable.size(), sizedIterable, task, null);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * Tasks are launch, and method return immediately.<br>
     * The returned Future allow you to track/link to the end of all parallel tasks
     *
     * @param sizedIterable Iterable over elements
     * @param task          Task to play
     * @param filter        Filter on elements. Only filtered elements are given to the task
     * @param <T>           Elements type
     * @return Future to track/link to the end of all parallel tasks
     */
    public static @NotNull <T> Future<Void> async(
            @NotNull SizedIterable<T> sizedIterable, @NotNull ConsumerTask<T> task, @Nullable Filter<T> filter)
    {
        return ForEach.async(sizedIterable.size(), sizedIterable, task, filter);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * Tasks are launch, and method return immediately.<br>
     * The returned Future allow you to track/link to the end of all parallel tasks
     *
     * @param sortedArray SortedArray over elements
     * @param task        Task to play
     * @param <T>         Elements type
     * @return Future to track/link to the end of all parallel tasks
     */
    public static @NotNull <T> Future<Void> async(@NotNull SortedArray<T> sortedArray, @NotNull ConsumerTask<T> task)
    {
        return ForEach.async(sortedArray.size(), sortedArray, task, null);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * Tasks are launch, and method return immediately.<br>
     * The returned Future allow you to track/link to the end of all parallel tasks
     *
     * @param sortedArray SortedArray over elements
     * @param task        Task to play
     * @param filter      Filter on elements. Only filtered elements are given to the task
     * @param <T>         Elements type
     * @return Future to track/link to the end of all parallel tasks
     */
    public static @NotNull <T> Future<Void> async(
            @NotNull SortedArray<T> sortedArray, @NotNull ConsumerTask<T> task, @Nullable Filter<T> filter)
    {
        return ForEach.async(sortedArray.size(), sortedArray, task, filter);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * Tasks are launch, and method return immediately.<br>
     * The returned Future allow you to track/link to the end of all parallel tasks
     *
     * @param array Array of elements
     * @param task  Task to play
     * @param <T>   Elements type
     * @return Future to track/link to the end of all parallel tasks
     */
    public static @NotNull <T> Future<Void> async(@NotNull T[] array, @NotNull ConsumerTask<T> task)
    {
        return ForEach.async(array.length, new EnumerationIterator<>(array), task, null);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * Tasks are launch, and method return immediately.<br>
     * The returned Future allow you to track/link to the end of all parallel tasks
     *
     * @param array  Array of elements
     * @param task   Task to play
     * @param filter Filter on elements. Only filtered elements are given to the task
     * @param <T>    Elements type
     * @return Future to track/link to the end of all parallel tasks
     */
    public static @NotNull <T> Future<Void> async(
            @NotNull T[] array, @NotNull ConsumerTask<T> task, @Nullable Filter<T> filter)
    {
        return ForEach.async(array.length, new EnumerationIterator<>(array), task, filter);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * Tasks are launch, and method return immediately.<br>
     * The returned Future allow you to track/link to the end of all parallel tasks
     *
     * @param size     Number of elements
     * @param iterable Iterable of elements
     * @param task     Task to play
     * @param filter   Filter on elements. Only filtered elements are given to the task
     * @param <T>      Elements type
     * @return Future to track/link to the end of all parallel tasks
     */
    private static @NotNull <T> Future<Void> async(
            final int size, @NotNull final Iterable<T> iterable, @NotNull final ConsumerTask<T> task,
            @Nullable final Filter<T> filter)
    {
        Objects.requireNonNull(iterable, "iterable");
        Objects.requireNonNull(task, "task");
        final Promise<Void> promise       = new Promise<>();
        final AtomicInteger atomicInteger = new AtomicInteger(size);
        iterable.forEach(element -> ThreadManager.parallel(parameter ->
                                                           {
                                                               if (filter == null || filter.isFiltered(parameter))
                                                               {
                                                                   try
                                                                   {
                                                                       task.consume(parameter);
                                                                   }
                                                                   catch (Throwable throwable)
                                                                   {
                                                                       task.taskError(new TaskException(
                                                                               "Failed to execute the task!",
                                                                               throwable));
                                                                   }
                                                               }

                                                               if (atomicInteger.decrementAndGet() == 0)
                                                               {
                                                                   promise.setResult(null);
                                                               }
                                                           }, element));
        return promise.future();
    }

    /**
     * Execute a task in parallel on each element.<br>
     * Tasks are launch, and method return immediately
     *
     * @param iterable Iterable over elements
     * @param task     Task to play
     * @param <T>      Elements type
     * @param <R>      Task result type
     */
    public static <T, R> void forEach(@NotNull Iterable<T> iterable, @NotNull Task<T, R> task)
    {
        ForEach.forEach(iterable, task, null);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * Tasks are launch, and method return immediately
     *
     * @param iterable Iterable over elements
     * @param task     Task to play
     * @param <T>      Elements type
     */
    public static <T> void forEach(@NotNull Iterable<T> iterable, @NotNull ConsumerTask<T> task)
    {
        ForEach.forEach(iterable, task, null);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * Tasks are launch, and method return immediately
     *
     * @param array Array of elements
     * @param task  Task to play
     * @param <T>   Elements type
     * @param <R>   Task result type
     */
    public static <T, R> void forEach(@NotNull T[] array, @NotNull Task<T, R> task)
    {
        ForEach.forEach(array, task, null);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * Tasks are launch, and method return immediately
     *
     * @param array  Array of elements
     * @param task   Task to play
     * @param filter Filter on elements. Only filtered elements are given to the task
     * @param <T>    Elements type
     * @param <R>    Task result type
     */
    public static <T, R> void forEach(@NotNull T[] array, @NotNull Task<T, R> task, @Nullable Filter<T> filter)
    {
        ForEach.forEach((Iterable<T>) new EnumerationIterator<>(array), task, null);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * Tasks are launch, and method return immediately
     *
     * @param enumeration Enumeration over elements
     * @param task        Task to play
     * @param <T>         Elements type
     */
    public static <T> void forEach(@NotNull Enumeration<T> enumeration, @NotNull ConsumerTask<T> task)
    {
        ForEach.forEach(enumeration, task, null);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * Tasks are launch, and method return immediately
     *
     * @param enumeration Enumeration over elements
     * @param task        Task to play
     * @param filter      Filter on elements. Only filtered elements are given to the task
     * @param <T>         Elements type
     * @param <R>         Task return type
     */
    public static <T, R> void forEach(
            @NotNull Enumeration<T> enumeration, @NotNull Task<T, R> task, @Nullable Filter<T> filter)
    {
        ForEach.forEach((Iterable<T>) new EnumerationIterator<>(enumeration), task, null);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * Tasks are launch, and method return immediately
     *
     * @param iterator Iterator over elements
     * @param task     Task to play
     * @param <T>      Elements type
     */
    public static <T> void forEach(@NotNull Iterator<T> iterator, @NotNull ConsumerTask<T> task)
    {
        ForEach.forEach(iterator, task, null);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * Tasks are launch, and method return immediately
     *
     * @param iterator Iterator over elements
     * @param task     Task to play
     * @param filter   Filter on elements. Only filtered elements are given to the task
     * @param <T>      Elements type
     * @param <R>      Task return type
     */
    public static <T, R> void forEach(
            @NotNull Iterator<T> iterator, @NotNull Task<T, R> task, @Nullable Filter<T> filter)
    {
        ForEach.forEach((Iterable<T>) new EnumerationIterator<>(iterator), task, null);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * Tasks are launch, and method return immediately
     *
     * @param iterable Iterable over elements
     * @param task     Task to play
     * @param filter   Filter on elements. Only filtered elements are given to the task
     * @param <T>      Elements type
     * @param <R>      Task return type
     */
    public static <T, R> void forEach(
            @NotNull Iterable<T> iterable, @NotNull Task<T, R> task, @Nullable Filter<T> filter)
    {
        Objects.requireNonNull(iterable, "iterable");
        Objects.requireNonNull(task, "task");
        iterable.forEach(element ->
                         {
                             if (filter == null || filter.isFiltered(element))
                             {
                                 ThreadManager.doTask(task, element);
                             }
                         });
    }

    /**
     * Execute a task in parallel on each element.<br>
     * Tasks are launch, and method return immediately
     *
     * @param array Array of elements
     * @param task  Task to play
     * @param <T>   Elements type
     */
    public static <T> void forEach(@NotNull T[] array, @NotNull ConsumerTask<T> task)
    {
        ForEach.forEach(array, task, null);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * Tasks are launch, and method return immediately
     *
     * @param array  Array of elements
     * @param task   Task to play
     * @param filter Filter on elements. Only filtered elements are given to the task
     * @param <T>    Elements type
     */
    public static <T> void forEach(@NotNull T[] array, @NotNull ConsumerTask<T> task, @Nullable Filter<T> filter)
    {
        ForEach.forEach((Iterable<T>) new EnumerationIterator<T>(array), task, null);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * Tasks are launch, and method return immediately
     *
     * @param iterable Iterable over elements
     * @param task     Task to play
     * @param filter   Filter on elements. Only filtered elements are given to the task
     * @param <T>      Elements type
     */
    public static <T> void forEach(@NotNull Iterable<T> iterable, @NotNull ConsumerTask<T> task, @Nullable Filter<T> filter)
    {
        Objects.requireNonNull(iterable, "iterable");
        Objects.requireNonNull(task, "task");
        iterable.forEach(element ->
                         {
                             if (filter == null || filter.isFiltered(element))
                             {
                                 ThreadManager.doTask(task, element);
                             }
                         });
    }

    /**
     * Execute a task in parallel on each element.<br>
     * The method will wait all parallel task finished before return
     *
     * @param collection Collection of elements
     * @param task       Task to play
     * @param <T>        Elements type
     */
    public static <T> void sync(@NotNull Collection<T> collection, @NotNull ConsumerTask<T> task)
    {
        ForEach.sync(collection.size(), collection, task, null);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * The method will wait all parallel task finished before return
     *
     * @param collection Collection of elements
     * @param task       Task to play
     * @param filter     Filter on elements. Only filtered elements are given to the task
     * @param <T>        Elements type
     */
    public static <T> void sync(
            @NotNull Collection<T> collection, @NotNull ConsumerTask<T> task, @Nullable Filter<T> filter)

    {
        ForEach.sync(collection.size(), collection, task, filter);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * The method will wait all parallel task finished before return
     *
     * @param sizedIterable Iterable of elements
     * @param task          Task to play
     * @param <T>           Elements type
     */
    public static <T> void sync(@NotNull SizedIterable<T> sizedIterable, @NotNull ConsumerTask<T> task)
    {
        ForEach.sync(sizedIterable.size(), sizedIterable, task, null);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * The method will wait all parallel task finished before return
     *
     * @param sizedIterable Iterable of elements
     * @param task          Task to play
     * @param filter        Filter on elements. Only filtered elements are given to the task
     * @param <T>           Elements type
     */
    public static <T> void sync(
            @NotNull SizedIterable<T> sizedIterable, @NotNull ConsumerTask<T> task, @Nullable Filter<T> filter)

    {
        ForEach.sync(sizedIterable.size(), sizedIterable, task, filter);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * The method will wait all parallel task finished before return
     *
     * @param sortedArray SortedArray of elements
     * @param task        Task to play
     * @param <T>         Elements type
     */
    public static <T> void sync(@NotNull SortedArray<T> sortedArray, @NotNull ConsumerTask<T> task)
    {
        ForEach.sync(sortedArray.size(), sortedArray, task, null);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * The method will wait all parallel task finished before return
     *
     * @param sortedArray SortedArray of elements
     * @param task        Task to play
     * @param filter      Filter on elements. Only filtered elements are given to the task
     * @param <T>         Elements type
     */
    public static <T> void sync(
            @NotNull SortedArray<T> sortedArray, @NotNull ConsumerTask<T> task, @Nullable Filter<T> filter)
    {
        ForEach.sync(sortedArray.size(), sortedArray, task, filter);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * The method will wait all parallel task finished before return
     *
     * @param array Array of elements
     * @param task  Task to play
     * @param <T>   Elements type
     */
    public static <T> void sync(@NotNull T[] array, @NotNull ConsumerTask<T> task)
    {
        ForEach.sync(array.length, new EnumerationIterator<>(array), task, null);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * The method will wait all parallel task finished before return
     *
     * @param array  Array of elements
     * @param task   Task to play
     * @param filter Filter on elements. Only filtered elements are given to the task
     * @param <T>    Elements type
     */
    public static <T> void sync(@NotNull T[] array, @NotNull ConsumerTask<T> task, @Nullable Filter<T> filter)
    {
        ForEach.sync(array.length, new EnumerationIterator<>(array), task, filter);
    }

    /**
     * Execute a task in parallel on each element.<br>
     * The method will wait all parallel task finished before return
     *
     * @param size     Number of elements
     * @param iterable Iterable of elements
     * @param task     Task to play
     * @param filter   Filter on elements. Only filtered elements are given to the task
     * @param <T>      Elements type
     */
    private static @NotNull <T> void sync(
            int size, @NotNull Iterable<T> iterable, @NotNull ConsumerTask<T> task, @Nullable Filter<T> filter)
    {
        ForEach.async(size, iterable, task, filter).waitFinish();
    }
}

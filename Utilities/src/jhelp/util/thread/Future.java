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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import jhelp.util.list.ForEach;
import jhelp.util.list.Queue;
import jhelp.util.list.SizedIterable;
import jhelp.util.util.Utilities;

/**
 * Future is the result of asynchronous task.<br>
 * Since the task is played in parallel, we may not have already the result.
 * Future is the link to this task to able wait for the result or chain an other task to be played when result, finished or error.<br>
 * Usually in the thread that produce the result, we create a {@link Promise} and give the future {@link Promise#future()}
 * to anyone interested by the result.<br>
 * For convenience, it is possible to use {@link #launch(Task, Object)}, {@link #launchConsume(ConsumerTask, Object)},
 * {@link #launchProduce(ProducerTask)} or {@link #launchRun(RunnableTask)} to launch a task OR
 * {@link #of(Object)} to have a future based on fix value
 */
public final class Future<R>
{
    static
    {
        Promise promise = new Promise();
        promise.setResult(null);
        NULL = promise.future();
    }

    /**
     * Future of null
     */
    private static final Future NULL;

    /**
     * Get list of result of several futures.<br>
     * The result future succeed if and only if all given futures succeed<br>
     * The result list order is not guarantee be futures given order.
     *
     * @param futures Futures to get the result
     * @param <R>     Result type
     * @return Future that collect all results
     */
    public static @NotNull <R> Future<List<R>> and(final @NotNull Future<R>... futures)
    {
        if (futures.length == 0)
        {
            return Future.of(Collections.EMPTY_LIST);
        }

        final Promise<List<R>> promise = new Promise<>();
        final List<R>          result  = new ArrayList<>();

        ForEach.async(futures, future ->
        {
            synchronized (result)
            {
                switch (future.status)
                {
                    case ERROR:
                        promise.setError(future.error());
                        break;
                    case CANCELLED:
                        promise.cancel(future.cancellationReason());
                        break;
                    case RESULT:
                        result.add(future.value());
                        break;
                }
            }
        }).andConsume(ignored -> promise.setResult(result));

        return promise.future();
    }

    /**
     * Search first element match given filter.<br>
     * The returned future is on error if no element match
     *
     * @param iterable Where search element
     * @param filter   Filter for select element
     * @param <R>      Element type
     * @return Future to know when found or search finished
     */
    public static @NotNull <R> Future<R> firstMatch(
            @NotNull final Iterable<R> iterable, @NotNull final Filter<R> filter)
    {
        Objects.requireNonNull(iterable, "iterable MUST NOT be null!");
        Objects.requireNonNull(filter, "filter MUST NOT be null!");
        final Promise<R> promise = new Promise<>();

        ThreadManager.parallel(() ->
                               {
                                   for (R element : iterable)
                                   {
                                       if (filter.isFiltered(element))
                                       {
                                           promise.setResult(element);
                                           return;
                                       }
                                   }

                                   promise.setError(new TaskException("No element match"));
                               });

        return promise.future();
    }

    /**
     * Search first element match given filter.<br>
     * The returned future is on error if no element match.<br>
     * It is more efficient to use this method than {@link #firstMatch(Iterable, Filter)}, if possible
     *
     * @param collection Where search element
     * @param filter     Filter for select element
     * @param <R>        Element type
     * @return Future to know when found or search finished
     */
    public static <R> Future<R> firstMatch(@NotNull final Collection<R> collection, @NotNull final Filter<R> filter)
    {
        Objects.requireNonNull(collection, "collection MUST NOT be null!");
        Objects.requireNonNull(filter, "filter MUST NOT be null!");
        final Promise<R> promise = new Promise<>();

        ForEach.async(collection, promise::setResult, filter)
               .thenConsume(ignored -> promise.setError(new TaskException("No element match")));

        return promise.future();
    }

    /**
     * Search first element match given filter.<br>
     * The returned future is on error if no element match.<br>
     * It is more efficient to use this method than {@link #firstMatch(Iterable, Filter)}, if possible
     *
     * @param sizedIterable Where search element
     * @param filter        Filter for select element
     * @param <R>           Element type
     * @return Future to know when found or search finished
     */
    public static @NotNull <R> Future<R> firstMatch(
            @NotNull final SizedIterable<R> sizedIterable, @NotNull final Filter<R> filter)
    {
        Objects.requireNonNull(sizedIterable, "sizedIterable MUST NOT be null!");
        Objects.requireNonNull(filter, "filter MUST NOT be null!");
        final Promise<R> promise = new Promise<>();

        ForEach.async(sizedIterable, promise::setResult, filter)
               .thenConsume(ignored -> promise.setError(new TaskException("No element match")));

        return promise.future();
    }

    /**
     * Convenient method to launch a task in parallel and get a future to track/link to result.
     *
     * @param task      Task to launch
     * @param parameter Parameter to give to the task
     * @param <P>       Task parameter type
     * @param <R>       Task result type
     * @return Future to track/link to result.
     */
    public static @NotNull <P, R> Future<R> launch(@NotNull Task<P, R> task, @Nullable P parameter)
    {
        Objects.requireNonNull(task, "task");
        LaunchTask<P, R> launchTask = new LaunchTask<>(task, parameter);
        ThreadManager.doTask(launchTask, null);
        return launchTask.future();
    }

    /**
     * Convenient method to launch a task in parallel and get a future to track/link to result.
     *
     * @param task      Task to launch
     * @param parameter Parameter to give to the task
     * @param <P>       Task parameter type
     * @return Future to track/link to result.
     */
    public static @NotNull <P> Future<Void> launchConsume(@NotNull ConsumerTask<P> task, @Nullable P parameter)
    {
        Objects.requireNonNull(task, "task");
        return Future.launch(task, parameter);
    }

    /**
     * Convenient method to launch a task in parallel and get a future to track/link to result.
     *
     * @param task Task to launch
     * @param <R>  Task result type
     * @return Future to track/link to result.
     */
    public static @NotNull <R> Future<R> launchProduce(@NotNull ProducerTask<R> task)
    {
        Objects.requireNonNull(task, "task");
        return Future.launch(task, null);
    }

    /**
     * Convenient method to launch a task in parallel and get a future to track/link to result.
     *
     * @param task Task to launch
     * @return Future to track/link to result.
     */
    public static @NotNull Future<Void> launchRun(@NotNull RunnableTask task)
    {
        Objects.requireNonNull(task, "task");
        return Future.launch(task, null);
    }

    /**
     * Convenient method to have a future with fix value
     *
     * @param result Fixed value
     * @param <R>    Fixed value type
     * @return Future of the value
     */
    public static @NotNull <R> Future<R> of(@Nullable R result)
    {
        if (result == null)
        {
            return Future.NULL;
        }

        Promise<R> promise = new Promise<>();
        promise.setResult(result);
        return promise.future();
    }

    /**
     * Get the first valid result of given futures.<br>
     * The result future is on error if and only if all given future are on error.
     *
     * @param futures Futures in competition
     * @param <R>     Result type
     * @return Future link to result of first win given futures
     */
    public static @NotNull <R> Future<R> or(@NotNull Future<R>... futures)
    {
        if (futures.length == 0)
        {
            return Future.NULL;
        }

        final Promise<R>        promise       = new Promise<>();
        final AtomicInteger     atomicInteger = new AtomicInteger(futures.length);
        ConsumerTask<? super R> alertPromise  = promise::setResult;
        ConsumerTask<TaskException> errorManagement = taskException ->
        {
            if (atomicInteger.decrementAndGet() == 0)
            {
                promise.setError(taskException);
            }
        };

        for (Future<R> future : futures)
        {
            future.andConsume(alertPromise);
            future.onError(errorManagement);
        }

        return promise.future();
    }

    /**
     * Get the first valid result of given futures.<br>
     * The result future is on error if and only if all given future are on error or filter reject their solution.
     *
     * @param filter  Filter on proposed solution
     * @param futures Futures in competition
     * @param <R>     Result type
     * @return Future link to result of first win given futures
     */
    public static @NotNull <R> Future<R> or(@NotNull Filter<R> filter, @NotNull Future<R>... futures)
    {
        if (futures.length == 0)
        {
            return Future.NULL;
        }

        final Promise<R>    promise       = new Promise<>();
        final AtomicInteger atomicInteger = new AtomicInteger(futures.length);
        ConsumerTask<? super R> alertPromise = result ->
        {
            if (filter.isFiltered(result))
            {
                promise.setResult(result);
            }
            else if (atomicInteger.decrementAndGet() == 0)
            {
                promise.setError(new TaskException(result + " not a valid result"));
            }
        };
        ConsumerTask<TaskException> errorManagement = taskException ->
        {
            if (atomicInteger.decrementAndGet() == 0)
            {
                promise.setError(taskException);
            }
        };

        for (Future<R> future : futures)
        {
            future.andConsume(alertPromise);
            future.onError(errorManagement);
        }

        return promise.future();
    }

    /**
     * Transform a complex result Future&lt;Future&lt;R&gt;&gt to a simple result Future&lt;R&gt;
     *
     * @param future Future to simplify
     * @return Simplified future
     */
    public static @NotNull <R> Future<R> unwrap(@NotNull Future<Future<R>> future)
    {
        Future<R> unwrap = future.thenDo(futureOfFuture -> futureOfFuture.value().value());
        unwrap.onCancel(future::requestCancel);
        return unwrap;
    }

    /**
     * Transform a complex result Future&lt;Future&lt;...&lt;R&gt;&gt;&gt; to a simple result Future&lt;R&gt;
     *
     * @param future Future to simplify
     * @return Simplified future
     */
    public static @NotNull Future unwrapMax(@NotNull Future future)
    {
        Future unwrap = future.thenDo(result ->
                                      {
                                          while (result instanceof Future)
                                          {
                                              result = ((Future) result).value();
                                          }

                                          return result;
                                      });
        unwrap.onCancel(reason -> future.requestCancel((String) reason));
        return unwrap;
    }

    /**
     * Create a future linked to the end of the end of given futures.<br>
     * The result future will be finished when all given futures are finished.
     * It is possible to get the list of futures for having their individual status.
     *
     * @param futures Futures to wait
     * @return Future linked to the end of given futures
     */
    public static @NotNull Future<List<Future<?>>> waitAll(@NotNull Future<?>... futures)
    {
        if (futures.length == 0)
        {
            return Future.of(new ArrayList<>());
        }

        Promise<List<Future<?>>> promise       = new Promise<>();
        AtomicInteger            atomicInteger = new AtomicInteger(futures.length);
        ConsumerTask<Future> decrement = future ->
        {
            if (atomicInteger.decrementAndGet() == 0)
            {
                promise.setResult(Utilities.toList(futures));
            }
        };

        for (Future<?> future : futures)
        {
            future.thenConsume(decrement);
        }

        promise.registerRequestCancelListener((promise1, reason) ->
                                              {
                                                  ForEach.forEach(futures, future -> future.requestCancel(reason));
                                                  promise1.cancel(reason);
                                              });

        return promise.future();
    }

    /**
     * Indicates if at least one thread wait for task finished
     */
    private final AtomicBoolean atLeastOneThreadWaiting = new AtomicBoolean(false);
    /**
     * Cancellation reason
     */
    private String        cancellationReason;
    /**
     * Task error
     */
    private TaskException error;
    /**
     * Tasks to play when have an error
     */
    private final Queue<TaskFuture<TaskException, Void>>  errorStack  = new Queue<>();
    /**
     * Tasks to play when have a result or an error
     */
    private final Queue<TaskFuture<? super Future<R>, ?>> finishStack = new Queue<>();
    /**
     * Promise that pilot the future
     */
    private final Promise<R> promiseParent;
    /**
     * Computed result
     */
    private       R          result;
    /**
     * Current status
     */
    private       FutureStatus                    status       = FutureStatus.COMPUTING;
    /**
     * Tasks to play when have a result
     */
    private final Queue<TaskFuture<? super R, ?>> succeedStack = new Queue<>();

    /**
     * Create a future
     *
     * @param promiseParent Promise that pilot the future
     */
    Future(@NotNull Promise<R> promiseParent)
    {
        this.promiseParent = promiseParent;
    }

    /**
     * Free all waiting threads.<br>
     * Must be called inside synchronized(this.atLeastOneThreadWaiting, { ... }) block
     */
    private void freeWaitingThreads()
    {
        if (this.atLeastOneThreadWaiting.get())
        {
            this.atLeastOneThreadWaiting.notifyAll();
            this.atLeastOneThreadWaiting.set(false);
        }
    }

    /**
     * Called when task cancelled, and launch appropriate tasks
     */
    private void nextCancel()
    {
        synchronized (this.atLeastOneThreadWaiting)
        {
            while (!this.succeedStack.empty())
            {
                this.succeedStack.outQueue().cancel(this.cancellationReason);
            }

            this.errorStack.clear();
        }

        this.nextFinished();
    }

    /**
     * Called when task failed, and launch appropriate tasks
     */
    private void nextError()
    {
        synchronized (this.atLeastOneThreadWaiting)
        {
            while (!this.succeedStack.empty())
            {
                this.succeedStack.outQueue().error(this.error);
            }

            while (!this.errorStack.empty())
            {
                this.errorStack.outQueue().launch(this.error);
            }
        }

        this.nextFinished();
    }

    /**
     * Called when task finished, and launch appropriate tasks
     */
    private void nextFinished()
    {
        synchronized (this.atLeastOneThreadWaiting)
        {
            while (!this.finishStack.empty())
            {
                this.finishStack.outQueue().launch(this);
            }
        }
    }

    /**
     * Called when task succeed, and launch appropriate tasks
     */
    private void nextSucceed()
    {
        synchronized (this.atLeastOneThreadWaiting)
        {
            while (!this.succeedStack.empty())
            {
                this.succeedStack.outQueue().launch(this.result);
            }

            this.errorStack.clear();
        }

        this.nextFinished();
    }

    /**
     * Cancel the embed task
     *
     * @param reason Cancellation reason
     */
    void cancel(@NotNull String reason)
    {
        synchronized (this.atLeastOneThreadWaiting)
        {
            if (this.status == FutureStatus.COMPUTING)
            {
                this.cancellationReason = reason;
                this.status = FutureStatus.CANCELLED;
                ThreadManager.parallel(this::nextCancel);
                this.freeWaitingThreads();
            }
        }
    }

    /**
     * Called when error happen while execute the task
     *
     * @param taskException Error happen
     */
    void setError(@NotNull TaskException taskException)
    {
        synchronized (this.atLeastOneThreadWaiting)
        {
            if (this.status == FutureStatus.COMPUTING)
            {
                this.status = FutureStatus.ERROR;
                this.error = taskException;
                ThreadManager.parallel(this::nextError);
                this.freeWaitingThreads();
            }
        }
    }

    /**
     * Called when result is known
     *
     * @param result Result computed
     */
    void setResult(@Nullable R result)
    {
        synchronized (this.atLeastOneThreadWaiting)
        {
            if (this.status == FutureStatus.COMPUTING)
            {
                this.status = FutureStatus.RESULT;
                this.result = result;
                ThreadManager.parallel(this::nextSucceed);
                this.freeWaitingThreads();
            }
        }
    }

    /**
     * Launch a task when the current task finished successfully.<br>
     * The computed result is given to the task as parameter.
     *
     * @param task Task to execute on succeed
     * @param <R2> Given task return type
     * @return Future to track/link to given task end
     */
    public @NotNull <R2> Future<R2> andCombine(@NotNull Task<? super R, Future<R2>> task)
    {
        Objects.requireNonNull(task, "task");
        return Future.unwrap(this.andDo(task));
    }

    /**
     * Link a task to do after this current task if succeed.<br>
     * Difference with {@link #andCombine(Task)}, is here the link is strong.
     * That is to say, if you cancel the returned future, it will also try to cancel this future.
     *
     * @param task Task to play after
     * @param <R2> Given task result type
     * @return Future on result of given task
     */
    public @NotNull <R2> Future<R2> andCombineLink(@NotNull Task<? super R, Future<R2>> task)
    {
        Objects.requireNonNull(task, "task");
        Future<R2> future = this.andCombine(task);
        future.onCancel(this::requestCancel);
        return future;
    }

    /**
     * Launch a task when the current task finished successfully.<br>
     * The computed result is given to the task as parameter.
     *
     * @param task Task to execute on succeed
     * @return Future to track/link to given task end
     */
    public @NotNull Future<Void> andConsume(@NotNull ConsumerTask<? super R> task)
    {
        Objects.requireNonNull(task, "task");
        return this.andDo(task);
    }

    /**
     * Link a task to do after this current task if succeed.<br>
     * Difference with {@link #andConsume(ConsumerTask)}, is here the link is strong.
     * That is to say, if you cancel the returned future, it will also try to cancel this future.
     *
     * @param task Task to play after
     * @return Future on result of given task
     */
    public @NotNull Future<Void> andConsumeLink(@NotNull ConsumerTask<? super R> task)
    {
        Objects.requireNonNull(task, "task");
        Future<Void> future = this.andConsume(task);
        future.onCancel(this::requestCancel);
        return future;
    }

    /**
     * Launch a task when the current task finished successfully.<br>
     * The computed result is given to the task as parameter.
     *
     * @param task Task to execute on succeed
     * @param <R2> Given task return type
     * @return Future to track/link to given task end
     */
    public @NotNull <R2> Future<R2> andDo(@NotNull Task<? super R, R2> task)
    {
        Objects.requireNonNull(task, "task");
        final TaskFuture<? super R, R2> taskFuture = new TaskFuture<>(task);

        synchronized (this.atLeastOneThreadWaiting)
        {
            switch (this.status)
            {
                case RESULT:
                    taskFuture.launch(this.result);
                    break;
                case ERROR:
                    taskFuture.error(this.error);
                    break;
                case CANCELLED:
                    taskFuture.cancel(this.cancellationReason);
                    break;
                case COMPUTING:
                    this.succeedStack.inQueue(taskFuture);
                    break;
            }
        }

        return taskFuture.future();
    }

    /**
     * Link a task to do after this current task if succeed.<br>
     * Difference with {@link #andDo(Task)}, is here the link is strong.
     * That is to say, if you cancel the returned future, it will also try to cancel this future.
     *
     * @param task Task to play after
     * @param <R2> Given task result type
     * @return Future on result of given task
     */
    public @NotNull <R2> Future<R2> andDoLink(@NotNull Task<? super R, R2> task)
    {
        Objects.requireNonNull(task, "task");
        Future<R2> future = this.andDo(task);
        future.onCancel(this::requestCancel);
        return future;
    }

    /**
     * Launch a task when the current task finished successfully
     *
     * @param task Task to execute on succeed
     * @return Future to track/link to given task end
     */
    public @NotNull <R2> Future<R2> andProduce(@NotNull ProducerTask<R2> task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        return this.andDo(ignored -> task.produce());
    }

    /**
     * Link a task to do after this current task if succeed.<br>
     * Difference with {@link #andProduce(ProducerTask)}, is here the link is strong.
     * That is to say, if you cancel the returned future, it will also try to cancel this future.
     *
     * @param task Task to play after
     * @param <R2> Given task result type
     * @return Future on result of given task
     */
    public @NotNull <R2> Future<R2> andProduceLink(@NotNull ProducerTask<R2> task)
    {
        Objects.requireNonNull(task, "task");
        Future<R2> future = this.andProduce(task);
        future.onCancel(this::requestCancel);
        return future;
    }

    /**
     * Launch a task when the current task finished successfully
     *
     * @param task Task to execute on succeed
     * @return Future to track/link to given task end
     */
    public @NotNull Future<Void> andRun(@NotNull RunnableTask task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        return this.andDo(ignored ->
                          {
                              task.run();
                              return null;
                          });
    }

    /**
     * Link a task to do after this current task if succeed.<br>
     * Difference with {@link #andRun(RunnableTask)}, is here the link is strong.
     * That is to say, if you cancel the returned future, it will also try to cancel this future.
     *
     * @param task Task to play after
     * @return Future on result of given task
     */
    public @NotNull Future<Void> andRunLink(@NotNull RunnableTask task)
    {
        Objects.requireNonNull(task, "task");
        Future<Void> future = this.andRun(task);
        future.onCancel(this::requestCancel);
        return future;
    }

    /**
     * Cancellation reason.<br>
     * {@code null} if future not cancelled
     *
     * @return Cancellation reason
     */
    public @Nullable String cancellationReason()
    {
        return this.cancellationReason;
    }

    /**
     * Task error<br>
     * {@code null} if future not on error
     *
     * @return Task error
     */
    public @Nullable TaskException error()
    {
        return this.error;
    }

    /**
     * Wait for the result, if result already known it return immediately.
     *
     * @return Computed result
     * @throws TaskException If error happen while execute the task
     */
    public @Nullable R get() throws TaskException
    {
        synchronized (this.atLeastOneThreadWaiting)
        {
            while (this.status == FutureStatus.COMPUTING)
            {
                try
                {
                    this.atLeastOneThreadWaiting.set(true);
                    this.atLeastOneThreadWaiting.wait();
                }
                catch (Exception ignored)
                {
                }
            }
        }

        if (this.status == FutureStatus.CANCELLED)
        {
            throw new TaskException("Future is cancelled! Reason: " + this.cancellationReason);
        }

        if (this.status == FutureStatus.ERROR)
        {
            throw this.error;
        }

        return this.result;
    }

    /**
     * Call task when this future is cancelled.
     *
     * @param cancelTask Task called, the string parameter is the cancellation reason
     * @return This future, convenient for chaining
     */
    public @NotNull Future<R> onCancel(@NotNull ConsumerTask<String> cancelTask)
    {
        Objects.requireNonNull(cancelTask, "cancelTask MUST NOT be null!");
        this.thenConsume(future ->
                         {
                             if (future.status() == FutureStatus.CANCELLED)
                             {
                                 cancelTask.consume(future.cancellationReason());
                             }
                         });
        return this;
    }

    /**
     * Launch a task when the current task have an error.<br>
     * Exception happen will be used as parameter of given task.
     *
     * @param task Task to execute
     * @return This future, convenient for chaining
     */
    public @NotNull Future<R> onError(@NotNull ConsumerTask<TaskException> task)
    {
        Objects.requireNonNull(task, "task");
        final TaskFuture<TaskException, Void> taskFuture = new TaskFuture<>(task);

        synchronized (this.atLeastOneThreadWaiting)
        {
            switch (this.status)
            {
                case ERROR:
                    taskFuture.launch(this.error);
                    break;
                case COMPUTING:
                    this.errorStack.inQueue(taskFuture);
                    break;
            }
        }

        return this;
    }

    /**
     * Try to cancel the task
     *
     * @param reason Cancellation reason
     * @return This future itself, convenient for chaining
     */
    public Future<R> requestCancel(@NotNull String reason)
    {
        Objects.requireNonNull(reason, "reason MUST NOT be null!");

        synchronized (this.atLeastOneThreadWaiting)
        {
            if (this.status == FutureStatus.COMPUTING)
            {
                this.promiseParent.cancelRequested(reason);
            }
        }

        return this;
    }

    /**
     * Current future status.<br>
     * It is just informative, don't loop over it to wait the result prefer use {@link #get()}, {@link #waitFinish()} or {@link #value()}
     *
     * @return Current future status
     */
    public @NotNull FutureStatus status()
    {
        synchronized (this.atLeastOneThreadWaiting)
        {
            return this.status;
        }
    }

    /**
     * Launch a task when the current task finished (Error, cancelled or succeed).<br>
     * This future is given to the task as parameter.
     *
     * @param task Task to execute on finished
     * @param <R2> Given task return type
     * @return Future to track/link to given task end
     */
    public @NotNull <R2> Future<R2> thenCombine(@NotNull Task<? super Future<R>, Future<R2>> task)
    {
        Objects.requireNonNull(task, "task");
        return Future.unwrap(this.thenDo(task));
    }

    /**
     * Link a task to do after this current task.<br>
     * Difference with {@link #thenCombine(Task)}, is here the link is strong.
     * That is to say, if you cancel the returned future, it will also try to cancel this future.
     *
     * @param task Task to play after
     * @param <R2> Given task result type
     * @return Future on result of given task
     */
    public @NotNull <R2> Future<R2> thenCombineLink(@NotNull Task<? super Future<R>, Future<R2>> task)
    {
        Objects.requireNonNull(task, "task");
        Future<R2> future = this.thenCombine(task);
        future.onCancel(this::requestCancel);
        return future;
    }

    /**
     * Launch a task when the current task finished (Error, cancelled or succeed).<br>
     * This future is given to the task as parameter.
     *
     * @param task Task to execute on finished
     * @return Future to track/link to given task end
     */
    public @NotNull Future<Void> thenConsume(@NotNull ConsumerTask<? super Future<R>> task)
    {
        Objects.requireNonNull(task, "task");
        return this.thenDo(task);
    }

    /**
     * Link a task to do after this current task.<br>
     * Difference with {@link #thenConsume(ConsumerTask)}, is here the link is strong.
     * That is to say, if you cancel the returned future, it will also try to cancel this future.
     *
     * @param task Task to play after
     * @return Future on result of given task
     */
    public @NotNull Future<Void> thenConsumeLink(@NotNull ConsumerTask<? super Future<R>> task)
    {
        Objects.requireNonNull(task, "task");
        Future<Void> future = this.thenConsume(task);
        future.onCancel(this::requestCancel);
        return future;
    }

    /**
     * Launch a task when the current task finished (Error, cancelled or succeed).<br>
     * This future is given to the task as parameter.
     *
     * @param task Task to execute on finished
     * @param <R2> Given task return type
     * @return Future to track/link to given task end
     */
    public @NotNull <R2> Future<R2> thenDo(@NotNull Task<? super Future<R>, R2> task)
    {
        Objects.requireNonNull(task, "task");
        final TaskFuture<? super Future<R>, R2> taskFuture = new TaskFuture<>(task);

        synchronized (this.atLeastOneThreadWaiting)
        {
            switch (this.status)
            {
                case COMPUTING:
                    this.finishStack.inQueue(taskFuture);
                    break;
                default:
                    taskFuture.launch(this);
                    break;
            }
        }

        return taskFuture.future();
    }

    /**
     * Link a task to do after this current task.<br>
     * Difference with {@link #thenDo(Task)}, is here the link is strong.
     * That is to say, if you cancel the returned future, it will also try to cancel this future.
     *
     * @param task Task to play after
     * @param <R2> Given task result type
     * @return Future on result of given task
     */
    public @NotNull <R2> Future<R2> thenDoLink(@NotNull Task<? super Future<R>, R2> task)
    {
        Objects.requireNonNull(task, "task");
        Future<R2> future = this.thenDo(task);
        future.onCancel(this::requestCancel);
        return future;
    }

    /**
     * Launch a task when the current task finished (Error, cancelled or succeed).<br>
     *
     * @param task Task to execute on succeed
     * @return Future to track/link to given task end
     */
    public @NotNull <R2> Future<R2> thenProduce(@NotNull ProducerTask<R2> task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        return this.thenDo(ignored -> task.produce());
    }

    /**
     * Launch a task when the current task finished (Error, cancelled or succeed).<br>
     * Difference with {@link #thenProduce(ProducerTask)}, is here the link is strong.
     * That is to say, if you cancel the returned future, it will also try to cancel this future.
     *
     * @param task Task to play after
     * @param <R2> Given task result type
     * @return Future on result of given task
     */
    public @NotNull <R2> Future<R2> thenProduceLink(@NotNull ProducerTask<R2> task)
    {
        Objects.requireNonNull(task, "task");
        Future<R2> future = this.thenProduce(task);
        future.onCancel(this::requestCancel);
        return future;
    }

    /**
     * Launch a task when the current task finished (Error, cancelled or succeed).<br>
     *
     * @param task Task to execute on succeed
     * @return Future to track/link to given task end
     */
    public @NotNull Future<Void> thenRun(@NotNull RunnableTask task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        return this.thenDo(ignored ->
                           {
                               task.run();
                               return null;
                           });
    }

    /**
     * Launch a task when the current task finished (Error, cancelled or succeed).<br>
     * Difference with {@link #thenRun(RunnableTask)}, is here the link is strong.
     * That is to say, if you cancel the returned future, it will also try to cancel this future.
     *
     * @param task Task to play after
     * @return Future on result of given task
     */
    public @NotNull Future<Void> thenRunLink(@NotNull RunnableTask task)
    {
        Objects.requireNonNull(task, "task");
        Future<Void> future = this.thenRun(task);
        future.onCancel(this::requestCancel);
        return future;
    }

    /**
     * Wait for task finish and get the value.<br>
     * The value exits only if future succeed
     *
     * @return Future value
     * @throws RuntimeException if future on error or cancelled
     */
    public @Nullable R value()
    {
        try
        {
            return this.get();
        }
        catch (TaskException taskException)
        {
            throw new RuntimeException(taskException);
        }
    }

    /**
     * Wait until task is finished.<br>
     * If task already finished, return immediately
     *
     * @return This future itself, convenient for chaining
     */
    public Future<R> waitFinish()
    {
        try
        {
            this.get();
        }
        catch (TaskException ignored)
        {
        }

        return this;
    }

    /**
     * Create future that wait the end of given futures for return this future result
     *
     * @param futures Futures to wait
     * @return Future link to this result and wait all given futures before return it.
     */
    public @NotNull Future<R> waitFor(@NotNull Future<?>... futures)
    {
        Objects.requireNonNull(futures, "futures MUST NOT be null!");
        return Future.waitAll(futures).andDo(ignored -> this.value());
    }
}

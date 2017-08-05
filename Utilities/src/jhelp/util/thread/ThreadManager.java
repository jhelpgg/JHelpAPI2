package jhelp.util.thread;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import jhelp.util.list.Pair;

/**
 * Pool of thread that limit the number of thread in same time
 */
public final class ThreadManager
{
    /**
     * Maximum number of thread in same time
     */
    private static final int                   NUMBER_OF_THREAD        = 1024;
    /**
     * Runnable of thread that do thread management
     */
    private static final ThreadManagerRunnable THREAD_MANAGER_RUNNABLE = new ThreadManagerRunnable();
    /**
     * Thread manager singleton instance
     */
    static final         ThreadManager         THREAD_MANAGER          = new ThreadManager();

    /**
     * Launch a task as soon as possible
     *
     * @param task      Task to play
     * @param parameter Parameter to give to task when play
     * @param <P>       Task parameter type
     * @param <R>       Task result type
     */
    public static <P, R> void doTask(@NotNull Task<P, R> task, @Nullable P parameter)
    {
        ThreadManager.doTask(task, parameter, 1L);
    }

    /**
     * Launch a task in a delay time
     *
     * @param task      Task to play
     * @param parameter Parameter to give to task when play
     * @param delay     Delay to wait before play the task
     * @param <P>       Task parameter type
     * @param <R>       Task result type
     */
    public static <P, R> void doTask(@NotNull Task<P, R> task, @Nullable P parameter, long delay)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        ThreadManager.THREAD_MANAGER.addTask(System.currentTimeMillis() + Math.max(1, delay), task, parameter);
    }

    /**
     * Launch a task in a delay time and call a callback when task is finished
     *
     * @param name      Task name
     * @param task      Task to launch
     * @param parameter Parameter to give to task when play
     * @param callback  Callback to invoke when task is finished
     * @param delay     Delay to wait before play the task
     * @param <P>       Task parameter type
     * @param <R>       Task result type
     */
    public static <P, R> void doTask(
            @NotNull String name, Task<P, R> task, @Nullable P parameter,
            @NotNull ConsumerTask<Pair<String, R>> callback,
            long delay)
    {
        Objects.requireNonNull(name, "name MUST NOT be null!");
        Objects.requireNonNull(task, "task MUST NOT be null!");
        Objects.requireNonNull(callback, "callback MUST NOT be null!");
        ThreadManager.doTask(new CallbackTask<>(name, task, callback), parameter, delay);
    }

    /**
     * Launch a task as soon as possible and call a callback when task is finished
     *
     * @param name      Task name
     * @param task      Task to launch
     * @param parameter Parameter to give to task when play
     * @param callback  Callback to invoke when task is finished
     * @param <P>       Task parameter type
     * @param <R>       Task result type
     */
    public static <P, R> void doTask(
            @NotNull String name, @NotNull Task<P, R> task, @Nullable P parameter,
            @NotNull ConsumerTask<Pair<String, R>> callback)
    {
        ThreadManager.doTask(name, task, parameter, callback, 1L);
    }

    /**
     * Launch a task in parallel
     *
     * @param task  Task to play
     * @param delay Time to wait before play
     * @param <R>   Task result type
     */
    public static <R> void parallel(@NotNull ProducerTask<R> task, long delay)
    {
        ThreadManager.doTask(task, null, delay);
    }

    /**
     * Launch a task in parallel
     *
     * @param task Task to play
     * @param <R>  Task result type
     */
    public static <R> void parallel(@NotNull ProducerTask<R> task)
    {
        ThreadManager.doTask(task, null, 1L);
    }

    /**
     * Launch a task in parallel
     *
     * @param task  Task to play
     * @param delay Time to wait before play
     */
    public static void parallel(@NotNull RunnableTask task, long delay)
    {
        ThreadManager.doTask(task, null, delay);
    }

    /**
     * Launch a task in parallel
     *
     * @param task Task to play
     */
    public static void parallel(@NotNull RunnableTask task)
    {
        ThreadManager.doTask(task, null, 1L);
    }

    /**
     * Launch a task in parallel
     *
     * @param task      Task to play
     * @param parameter Task parameter
     * @param <P>       Task parameter type
     */
    public static <P> void parallel(@NotNull ConsumerTask<P> task, @Nullable P parameter)
    {
        ThreadManager.doTask(task, parameter, 1L);
    }

    /**
     * Launch a task in parallel
     *
     * @param task      Task to play
     * @param parameter Task parameter
     * @param delay     Delay to wait before launch the task
     * @param <P>       Task parameter type
     */
    public static <P> void parallel(@NotNull ConsumerTask<P> task, @Nullable P parameter, long delay)
    {
        ThreadManager.doTask(task, parameter, delay);
    }
    /**
     * Indicates if manager is alive
     */
    private boolean alive        = false;
    /**
     * Tasks to play queue
     */
    private final PriorityQueue<TaskElement<?, ?>> priorityQueue = new PriorityQueue<>();
    /**
     * Actors to play tasks
     */
    private final ThreadActor[] threads;
    /**
     * Indicates if manager waiting
     */
    private final AtomicBoolean                    waiting       = new AtomicBoolean(false);
    /**
     * Indicates if have to wakeup as soon as possible
     */
    private boolean wakeupUrgent = false;

    /**
     * Create the manager
     */
    private ThreadManager()
    {
        this.threads = new ThreadActor[ThreadManager.NUMBER_OF_THREAD];

        for (int i = 0; i < ThreadManager.NUMBER_OF_THREAD; i++)
        {
            this.threads[i] = new ThreadActor();
        }
    }

    /**
     * Indicates if left at least one thread actor doing something
     *
     * @return {@code true} if left at least one thread actor doing something
     */
    private boolean atLeastOneWork()
    {
        for (ThreadActor threadActor : this.threads)
        {
            if (threadActor.taskElement != null)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Obtain the first free actor
     *
     * @return First free actor  OR {@code null} if no actor is free
     */
    private @Nullable ThreadActor firstFree()
    {
        for (ThreadActor threadActor : this.threads)
        {
            if (threadActor.taskElement == null)
            {
                return threadActor;
            }
        }

        return null;
    }

    /**
     * Add task to the queue
     *
     * @param time      Time whe play the task
     * @param task      Task to play
     * @param parameter Task parameter
     * @param <P>       Task parameter type
     * @param <R>       Task result type
     */
    <P, R> void addTask(long time, @NotNull Task<P, R> task, @Nullable P parameter)
    {
        synchronized (this.waiting)
        {
            this.priorityQueue.offer(new TaskElement(time, task, parameter));

            if (this.waiting.get())
            {
                this.waiting.notify();
            }

            if (!this.alive)
            {
                this.alive = true;
                (new Thread(ThreadManager.THREAD_MANAGER_RUNNABLE)).start();
            }
        }
    }

    /**
     * Called when an actor has finished its task
     */
    void anActorIsFree()
    {
        synchronized (this.waiting)
        {
            this.alive = this.alive && (this.atLeastOneWork() || !this.priorityQueue.isEmpty());

            if (this.wakeupUrgent || !this.alive)
            {
                this.wakeupUrgent = false;

                if (this.waiting.get())
                {
                    this.waiting.notify();
                }
            }
        }
    }

    /**
     * Manage the thread queue
     */
    void doRun()
    {
        TaskElement<?, ?> taskElement = null;
        long              time;
        ThreadActor       threadActor;

        while (this.alive)
        {
            while (taskElement == null)
            {
                synchronized (this.waiting)
                {
                    if (!this.priorityQueue.isEmpty())
                    {
                        taskElement = this.priorityQueue.peek();
                    }
                    else
                    {
                        this.waiting.set(true);

                        try
                        {
                            this.waiting.wait();
                        }
                        catch (Exception ignored)
                        {
                        }

                        this.waiting.set(false);
                    }
                }

                if (!this.alive)
                {
                    return;
                }
            }

            synchronized (this.waiting)
            {
                time = taskElement.time - System.currentTimeMillis();

                if (time <= 0)
                {
                    threadActor = this.firstFree();

                    if (threadActor != null)
                    {
                        this.priorityQueue.remove(taskElement);
                        threadActor.taskElement = taskElement;
                        (new Thread(threadActor)).start();
                    }
                    else
                    {
                        this.wakeupUrgent = true;
                        time = 16384;
                    }
                }

                taskElement = null;

                if (time > 0)
                {
                    this.waiting.set(true);

                    try
                    {
                        this.waiting.wait(time);
                    }
                    catch (Exception ignored)
                    {
                    }

                    this.waiting.set(false);
                }
            }
        }
    }
}

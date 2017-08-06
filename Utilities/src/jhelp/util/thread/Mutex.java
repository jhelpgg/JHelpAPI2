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
import java.util.concurrent.Semaphore;
import jhelp.util.debug.Debug;

/**
 * Mutex for do things inside a critical section.<br>
 * Only one thread can enter in critical section in same time.<br>
 * If a second thread arrive while one already in critical section,
 * the arrived thread will wait for the critical section is free (First leave it) before able to enter in critical section
 */
public final class Mutex
{
    /**
     * The mutex
     */
    private final Semaphore mutex = new Semaphore(1, true);

    /**
     * Create mutex
     */
    public Mutex()
    {
    }

    /**
     * Play a task in critical section.<br>
     * If a task is already inside the critical section, the current thread will be enqueue and asleep.
     * It will be wake up when its turn comes to enter inside the critical section.
     *
     * @param function  Task to play
     * @param parameter Parameter to give to task when its turn comes
     * @param <P>       Task parameter type
     * @param <R>       Task return type
     * @return Task result
     */
    public @Nullable <P, R> R playInCriticalSection(@NotNull Task<P, R> function, @Nullable P parameter)
    {
        Objects.requireNonNull(function);
        boolean locked = false;

        try
        {
            this.mutex.acquire();
            locked = true;
        }
        catch (Exception exception)
        {
            Debug.exception(exception, "Failed to acquire the mutex");
        }

        try
        {
            return function.playTask(parameter);
        }
        finally
        {
            if (locked)
            {
                this.mutex.release();
            }
        }
    }

    /**
     * Play a task in critical section.<br>
     * If a task is already inside the critical section, the current thread will be enqueue and asleep.
     * It will be wake up when its turn comes to enter inside the critical section.
     *
     * @param function Task to play
     * @param <R>      Task return type
     * @return Task result
     */
    public @Nullable <R> R playInCriticalSection(@NotNull ProducerTask<R> function)
    {
        Objects.requireNonNull(function);
        boolean locked = false;

        try
        {
            this.mutex.acquire();
            locked = true;
        }
        catch (Exception exception)
        {
            Debug.exception(exception, "Failed to acquire the mutex");
        }

        try
        {
            return function.produce();
        }
        finally
        {
            if (locked)
            {
                this.mutex.release();
            }
        }
    }

    /**
     * Play a task in critical section.<br>
     * If a task is already inside the critical section, the current thread will be enqueue and asleep.
     * It will be wake up when its turn comes to enter inside the critical section.
     *
     * @param function  Task to play
     * @param parameter Parameter to give to task when its turn comes
     * @param <P>       Task parameter type
     */
    public <P> void playInCriticalSectionVoid(@NotNull ConsumerTask<P> function, @Nullable P parameter)
    {
        Objects.requireNonNull(function);
        boolean locked = false;

        try
        {
            this.mutex.acquire();
            locked = true;
        }
        catch (Exception exception)
        {
            Debug.exception(exception, "Failed to acquire the mutex");
        }

        try
        {
            function.consume(parameter);
        }
        finally
        {
            if (locked)
            {
                this.mutex.release();
            }
        }
    }

    /**
     * Play a task in critical section.<br>
     * If a task is already inside the critical section, the current thread will be enqueue and asleep.
     * It will be wake up when its turn comes to enter inside the critical section.
     *
     * @param function Task to play
     */
    public void playInCriticalSectionVoid(@NotNull RunnableTask function)
    {
        Objects.requireNonNull(function);
        boolean locked = false;

        try
        {
            this.mutex.acquire();
            locked = true;
        }
        catch (Exception exception)
        {
            Debug.exception(exception, "Failed to acquire the mutex");
        }

        try
        {
            function.run();
        }
        finally
        {
            if (locked)
            {
                this.mutex.release();
            }
        }
    }
}

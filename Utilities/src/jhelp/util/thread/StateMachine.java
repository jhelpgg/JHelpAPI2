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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * State machine.<br>
 * Lot of method are protected to allow class that extends to expose in the form they need, control things, ...
 */
public class StateMachine<S extends Enum>
{
    /**
     * Current state
     */
    private S state;
    /**
     * Map of registered tasks
     */
    private final Map<S, List<ConsumerTask<S>>> tasksMap = new HashMap<>();

    /**
     * Create the state machine
     *
     * @param initialState Machine initial state
     */
    public StateMachine(@NotNull S initialState)
    {
        Objects.requireNonNull(initialState);
        this.state = initialState;
    }

    /**
     * Try to change the state.<br>
     * The change only happen if transition between current state and given one is allowed.<br>
     * If change happen, the corresponding tasks registered for the new state are launched.<br>
     * See {@link #allowedTransition(Enum, Enum)}
     *
     * @param state State to go
     * @return {@code true} if state changed
     */
    protected final boolean changeState(@NotNull S state)
    {
        Objects.requireNonNull(state);

        synchronized (this.tasksMap)
        {
            if (!this.allowedTransition(this.state, state))
            {
                return false;
            }

            this.state = state;
            final List<ConsumerTask<S>> list = this.tasksMap.get(state);

            if (list != null)
            {
                for (ConsumerTask<S> task : list)
                {
                    ThreadManager.parallel(task, state);
                }
            }
        }

        return true;
    }

    /**
     * Register a task for react to different state.<br>
     * If one off state is the current one, the task will be launch
     *
     * @param task   Task to register
     * @param states States to associate the task
     */
    protected final @SafeVarargs void register(@NotNull ConsumerTask<S> task, @NotNull S... states)
    {
        Objects.requireNonNull(task, "task");

        synchronized (this.tasksMap)
        {
            for (S state : states)
            {
                Objects.requireNonNull(state, "One of given state is null");
                List<ConsumerTask<S>> list = this.tasksMap.get(state);

                if (list == null)
                {
                    list = new ArrayList<>();
                    this.tasksMap.put(state, list);
                }

                if (!list.contains(task))
                {
                    list.add(task);

                    if (state == this.state)
                    {
                        ThreadManager.parallel(task, state);
                    }
                }
            }
        }
    }

    /**
     * Unregister a task for no more react to different state.
     *
     * @param task   Task to register
     * @param states States to associate the task
     */
    protected final @SafeVarargs void unregister(@NotNull ConsumerTask<S> task, @NotNull S... states)
    {
        synchronized (this.tasksMap)
        {
            for (S state : states)
            {
                Objects.requireNonNull(state, "One of given state is null");
                List<ConsumerTask<S>> list = this.tasksMap.get(state);

                if (list != null)
                {
                    list.remove(task);

                    if (list.isEmpty())
                    {
                        this.tasksMap.remove(state);
                    }
                }
            }
        }
    }

    /**
     * Unregister a task from all state it was registered
     *
     * @param task Task to remove
     */
    protected final void unregisterAll(@NotNull ConsumerTask<S> task)
    {
        synchronized (this.tasksMap)
        {
            List<ConsumerTask<S>> list;

            for (Map.Entry<S, List<ConsumerTask<S>>> entry : this.tasksMap.entrySet())
            {
                list = entry.getValue();
                list.remove(task);

                if (list.isEmpty())
                {
                    this.tasksMap.remove(entry.getKey());
                }
            }
        }
    }

    /**
     * Indicates if it is allow to go from a state to an other state.<br>
     * By default all transition are allowed
     *
     * @param oldState State it leave
     * @param newState State want go
     * @return {@code true} if it is allow to go from a state to an other state
     */
    public boolean allowedTransition(@NotNull S oldState, @NotNull S newState)
    {
        return true;
    }

    /**
     * Current state
     *
     * @return Current state
     */
    public final @NotNull S state()
    {
        synchronized (this.tasksMap)
        {
            return this.state;
        }
    }
}

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
     * Map of registered tasks
     */
    private final Map<S, List<Task<S, ?>>> tasksMap = new HashMap<>();
    /**
     * Current state
     */
    private S state;

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
     * Register a task for react to different state.<br>
     * If one off state is the current one, the task will be launch
     *
     * @param task   Task to register
     * @param states States to associate the task
     */
    protected final void register(@NotNull Task<S, ?> task, @NotNull S... states)
    {
        Objects.requireNonNull(task, "task");

        synchronized (this.tasksMap)
        {
            for (S state : states)
            {
                Objects.requireNonNull(state, "One of given state is null");
                List<Task<S, ?>> list = this.tasksMap.get(state);

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
                        ThreadManager.doTask(task, state);
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
    protected final void unregister(@NotNull Task<S, ?> task, @NotNull S... states)
    {
        synchronized (this.tasksMap)
        {
            for (S state : states)
            {
                Objects.requireNonNull(state, "One of given state is null");
                List<Task<S, ?>> list = this.tasksMap.get(state);

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
    protected final void unregisterAll(@NotNull Task<S, ?> task)
    {
        synchronized (this.tasksMap)
        {
            List<Task<S, ?>> list;

            for (Map.Entry<S, List<Task<S, ?>>> entry : this.tasksMap.entrySet())
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
            final List<Task<S, ?>> list = this.tasksMap.get(state);

            if (list != null)
            {
                for (Task<S, ?> task : list)
                {
                    ThreadManager.doTask(task, state);
                }
            }
        }

        return true;
    }
}

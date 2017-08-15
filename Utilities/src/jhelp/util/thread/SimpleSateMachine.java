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

/**
 * State machine that expose basic methods
 */
public class SimpleSateMachine<S extends Enum> extends StateMachine<S>
{
    /**
     * Create the state machine
     *
     * @param initialState Machine initial state
     */
    public SimpleSateMachine(final @NotNull S initialState)
    {
        super(initialState);
    }

    /**
     * Register a task for react to different state.<br>
     * If one off state is the current one, the task will be launch
     *
     * @param task   Task to register
     * @param states States to associate the task
     */
    public final @SafeVarargs void associate(@NotNull ConsumerTask<S> task, @NotNull S... states)
    {
        this.register(task, states);
    }

    /**
     * Unregister a task for no more react to different state.<br>
     * If one off state is the current one, the task will be launch
     *
     * @param task   Task to register
     * @param states States to associate the task
     */
    public final @SafeVarargs void disassociate(@NotNull ConsumerTask<S> task, @NotNull S... states)
    {
        this.unregister(task, states);
    }

    /**
     * Unregister a task from all task it lies
     *
     * @param task Task to remove
     */
    public final void disassociateAll(@NotNull ConsumerTask<S> task)
    {
        this.unregisterAll(task);
    }

    /**
     * Try to change the state.<br>
     * The change only happen if transition between current state and given one is allowed.<br>
     * If change happen, the corresponding task to new state are launched
     *
     * @param state State to go
     * @return {@code true} if state changed
     */
    public final boolean post(@NotNull S state)
    {
        return this.changeState(state);
    }
}

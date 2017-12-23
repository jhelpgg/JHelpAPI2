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
import java.util.Objects;

/**
 * Action that launch a {@link RunnableTask}
 */
public final class RunnableAction extends Action
{
    /**
     * Task to launch
     */
    private final RunnableTask runnableTask;

    /**
     * Create the action
     *
     * @param runnableTask Task to play
     */
    public RunnableAction(@NotNull RunnableTask runnableTask)
    {
        Objects.requireNonNull(runnableTask, "runnableTask MUST NOT be null!");
        this.runnableTask = runnableTask;
    }

    /**
     * Do the action
     *
     * @throws ActionException On action issue
     */
    @Override
    public void doAction() throws ActionException
    {
        try
        {
            this.runnableTask.run();
        }
        catch (Exception exception)
        {
            throw new ActionException(exception, "Runnable task issue");
        }
    }
}

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

import java.util.Objects;

public final class RunnableAction extends Action
{
    private final RunnableTask runnableTask;

    public RunnableAction(RunnableTask runnableTask)
    {
        Objects.requireNonNull(runnableTask, "runnableTask MUST NOT be null!");
        this.runnableTask = runnableTask;
    }

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

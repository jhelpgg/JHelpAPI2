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
import jhelp.util.debug.Debug;

/**
 * Represents a task
 */
public interface Task<P, R>
{
    /**
     * Play the task
     *
     * @param parameter Task parameter
     * @return Task result
     */
    @Nullable R playTask(@Nullable P parameter);

    /**
     * Called if task failed.<br>
     * Does nothing by default
     *
     * @param taskException Task exception
     */
    default void taskError(@NotNull TaskException taskException)
    {
        Debug.exception(taskException);
    }

    /**
     * Called when result is computed.<br>
     * Does nothing by default
     *
     * @param result Task result
     */
    default void taskResult(@Nullable R result)
    {
    }
}

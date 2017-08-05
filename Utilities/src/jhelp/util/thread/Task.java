package jhelp.util.thread;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

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

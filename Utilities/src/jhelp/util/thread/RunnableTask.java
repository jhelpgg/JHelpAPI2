package jhelp.util.thread;

import com.sun.istack.internal.Nullable;

/**
 * Task with no parameter and no result
 */
public interface RunnableTask extends Task<Void, Void>
{
    /**
     * Play the task
     */
    void run();

    /**
     * Play the task.<br>
     * By default call {@link #run()} and return {@code null}
     *
     * @param ignored Ignored parameter
     * @return Task result
     */
    @Override
    default @Nullable Void playTask(@Nullable Void ignored)
    {
        this.run();
        return null;
    }
}

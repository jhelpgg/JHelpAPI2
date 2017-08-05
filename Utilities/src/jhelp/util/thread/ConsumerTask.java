package jhelp.util.thread;

import com.sun.istack.internal.Nullable;

/**
 * Task with no result
 */
public interface ConsumerTask<P> extends Task<P, Void>
{
    /**
     * Consume the value
     *
     * @param parameter Task parameter
     */
    void consume(@Nullable P parameter);

    /**
     * Play the task.<br>
     * By default call {@link #consume(Object)} and return {@code null}
     *
     * @param parameter Task parameter
     * @return Task result
     */
    @Override
    default @Nullable Void playTask(P parameter)
    {
        this.consume(parameter);
        return null;
    }
}

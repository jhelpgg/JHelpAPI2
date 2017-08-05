package jhelp.util.thread;

import com.sun.istack.internal.Nullable;

/**
 * task with no parameter
 */
public interface ProducerTask<R> extends Task<Void, R>
{
    /***
     * Produce the value
     * @return Task result
     */
    @Nullable R produce();

    /**
     * Play the task.<br>
     * By default call {@link #produce()}
     *
     * @param ignored Ignored parameter
     * @return Task result
     */
    @Override
    default @Nullable R playTask(@Nullable Void ignored)
    {
        return this.produce();
    }
}

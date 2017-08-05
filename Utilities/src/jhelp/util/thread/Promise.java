package jhelp.util.thread;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.Objects;
import jhelp.util.list.ArrayObject;

/**
 * Promise task a task will give a result later.<br>
 * If at least one listener of cancel is register, the cancel is not propagate automatically to children task.
 * It is the decision of listener to propagate cancel (By call {@link #cancel(String)}) or not.<br>
 * If no listener on cancel, the cancel is propagate automatically.
 */
public final class Promise<R>
{
    /**
     * Future to track/link to the result
     */
    private final Future<R>                             future;
    /**
     * Cancellation listeners
     */
    private final ArrayObject<RequestCancelListener<R>> requestCancelListeners;

    /**
     * Create the promise
     */
    public Promise()
    {
        this.requestCancelListeners = new ArrayObject<>();
        this.future = new Future<>(this);
    }

    /**
     * Called when cancel requested
     *
     * @param reason Cancellation reason
     */
    void cancelRequested(@NotNull String reason)
    {
        synchronized (this.requestCancelListeners)
        {
            if (this.requestCancelListeners.isEmpty())
            {
                //No listener, propagate cancel automatically
                this.cancel(reason);
            }
            else
            {
                //Cancel will be propagate only if at least one listener call "cancel" method
                this.requestCancelListeners.forEach((ConsumerTask<RequestCancelListener<R>>)
                                                            requestCancelListener ->
                                                                    requestCancelListener.cancellationRequested(this,
                                                                                                                reason));
            }
        }
    }

    /**
     * Cancel the promise
     *
     * @param reason Cancellation reason
     */
    public void cancel(@NotNull String reason)
    {
        Objects.requireNonNull(reason, "reason MUST NOT be null!");
        this.future.cancel(reason);
    }

    /**
     * Future to track/link to the result
     *
     * @return Future to track/link to the result
     */
    public @NotNull Future<R> future()
    {
        return this.future;
    }

    /**
     * Register listener of cancel requests
     *
     * @param requestCancelListener Listener to register
     */
    public void registerRequestCancelListener(RequestCancelListener<R> requestCancelListener)
    {
        if (requestCancelListener != null)
        {
            synchronized (this.requestCancelListeners)
            {
                if (!this.requestCancelListeners.contains(requestCancelListener))
                {
                    this.requestCancelListeners.add(requestCancelListener);
                }
            }
        }
    }

    /**
     * Alert future that task have an error
     *
     * @param taskException Task exception
     */
    public void setError(@NotNull TaskException taskException)
    {
        Objects.requireNonNull(taskException, "taskException MUST NOT be null");
        this.future.setError(taskException);
    }

    /**
     * Alert future that a result is known
     *
     * @param result Task result
     */
    public void setResult(@Nullable R result)
    {
        this.future.setResult(result);
    }

    /**
     * Unregister listener of cancel requests
     *
     * @param requestCancelListener Listener to unregister
     */
    public void unregisterRequestCancelListener(RequestCancelListener<R> requestCancelListener)
    {
        synchronized (this.requestCancelListeners)
        {
            this.requestCancelListeners.remove(requestCancelListener);
        }
    }
}

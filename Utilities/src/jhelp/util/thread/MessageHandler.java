package jhelp.util.thread;

import com.sun.istack.internal.NotNull;
import java.util.Objects;

/**
 * Message handler is a queue of messages to play.<br>
 * Each post message are enqueued and play at its turn.<br>
 * It is guaranteed than never two messages are treat in same time,
 * it waits that the current message is treated before treat the next one.<br>
 * It is also guaranteed to be fair, if message are post in "same time" the really first played before the other one.<br>
 * It is also guaranteed that no messages are lost or wait for ever
 */
public abstract class MessageHandler<M>
{
    /**
     * Embed task to manage the message queue
     */
    private final HandlingTask<M> handlingTask;

    /**
     * Create the handler
     */
    public MessageHandler()
    {
        this.handlingTask = new HandlingTask<>(this);
    }

    /**
     * Called when a message received and treat it
     *
     * @param message Received message
     */
    protected abstract void handleMessage(@NotNull M message);

    /**
     * Post a message to be played as soon as possible
     *
     * @param message Message to play
     */
    public final void post(@NotNull M message)
    {
        this.post(message, 1L);
    }

    /**
     * Post a message delay in time
     *
     * @param message Message to ply
     * @param delay   Time to wait before play the message
     */
    public final void post(@NotNull M message, long delay)
    {
        Objects.requireNonNull(message, "message MUST NOT be null!");
        MessageInformation<M> messageToPost = new MessageInformation<>(System.currentTimeMillis() + Math.max(1L, delay),
                                                                       message);

        synchronized (this.handlingTask.working)
        {
            this.handlingTask.queueMessages.offer(messageToPost);

            if (this.handlingTask.waiting.get())
            {
                this.handlingTask.working.notify();
            }

            if (!this.handlingTask.working.get())
            {
                this.handlingTask.working.set(true);
                ThreadManager.parallel(this.handlingTask);
            }
        }
    }

    /**
     * Cancel a message.<br>
     * Each waiting message equals to given one are removed from the waiting queue.<br>
     * If message already treated or is currently treat the method does nothing for it.
     *
     * @param message Message to cancel
     */
    public final void cancel(@NotNull M message)
    {
        synchronized (this.handlingTask.working)
        {
            this.handlingTask.queueMessages.removeIf(messageInformation -> messageInformation.hasEmbedMessage(message));
        }
    }
}

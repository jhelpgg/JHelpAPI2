package jhelp.util.thread;

import com.sun.istack.internal.NotNull;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import jhelp.util.debug.Debug;

/**
 * Task to treat waiting messages queue in {@link MessageHandler}
 */
final class HandlingTask<M> implements RunnableTask
{
    /**
     * Message handler parent
     */
    private final MessageHandler<M> parent;
    /**
     * Messages queue
     */
    final PriorityQueue<MessageInformation<M>> queueMessages = new PriorityQueue<>();
    /**
     * Indicates if task still working
     */
    final AtomicBoolean                        working       = new AtomicBoolean(false);
    /**
     * Indicates if task wait something
     */
    final AtomicBoolean                        waiting       = new AtomicBoolean(false);

    /**
     * Create the task
     *
     * @param parent {@link MessageHandler} parent
     */
    HandlingTask(@NotNull MessageHandler<M> parent)
    {
        this.parent = parent;
    }

    /**
     * Do the task
     */
    @Override
    public void run()
    {
        MessageInformation<M> message;
        long                  left;

        do
        {
            synchronized (this.working)
            {
                message = this.queueMessages.peek();

                if (message == null)
                {
                    this.working.set(false);
                    return;
                }
            }

            left = message.time - System.currentTimeMillis();

            if (left <= 0)
            {
                synchronized (this.working)
                {
                    this.queueMessages.remove(message);
                }

                try
                {
                    this.parent.handleMessage(message.message);
                }
                catch (Throwable throwable)
                {
                    Debug.exception(throwable, "Issue while execute message: ", message.message);
                }
            }
            else
            {
                synchronized (this.working)
                {
                    this.waiting.set(true);

                    try
                    {
                        this.working.wait(left);
                    }
                    catch (Exception ignored)
                    {
                    }

                    this.waiting.set(false);
                }
            }
        }
        while (true);
    }
}

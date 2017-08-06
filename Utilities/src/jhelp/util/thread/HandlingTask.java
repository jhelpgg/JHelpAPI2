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
     * Indicates if task wait something
     */
    final AtomicBoolean                        waiting       = new AtomicBoolean(false);
    /**
     * Indicates if task still working
     */
    final AtomicBoolean                        working       = new AtomicBoolean(false);

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

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

import java.util.List;
import java.util.Objects;
import jhelp.util.list.ArrayObject;

public abstract class Action
{
    public static Action launch(final RunnableTask runnableTask)
    {
        Action action = new RunnableAction(runnableTask);
        action.launch();
        return action;
    }

    private       Future<Void> future;
    private final Mutex        mutex;

    public Action()
    {
        this.mutex = new Mutex();
    }

    private void play()
    {
        if (this.future == null)
        {
            this.future = Future.launchConsume(action ->
                                               {
                                                   try
                                                   {
                                                       action.doAction();
                                                   }
                                                   catch (Exception e)
                                                   {
                                                       throw new RuntimeException("Play action failed!", e);
                                                   }
                                               }, this);
        }
    }

    public abstract void doAction() throws ActionException;

    public final Action launch()
    {
        this.mutex.playInCriticalSectionVoid(this::play);
        return this;
    }

    public final Action onError(ConsumerTask<ActionException> consumerTask)
    {
        Objects.requireNonNull(consumerTask, "consumerTask MUST NOT be null!");
        this.mutex.playInCriticalSectionVoid(() ->
                                             {
                                                 this.play();
                                                 this.future = this.future.onError(exception -> consumerTask.consume(
                                                         new ActionException(exception, "Report error")));
                                             });

        return this;
    }

    public final Action onFinish(RunnableTask runnableTask)
    {
        Objects.requireNonNull(runnableTask, "runnableTask MUST NOT be null!");
        this.mutex.playInCriticalSectionVoid(() ->
                                             {
                                                 this.play();
                                                 this.future = this.future.andRun(runnableTask);
                                             });

        return this;
    }

    public final Action parallel(Action... actions)
    {
        Objects.requireNonNull(actions, "actions MUST NOT be null!");
        this.mutex.playInCriticalSectionVoid(() ->
                                             {
                                                 this.play();
                                                 final List<Future<Void>> futures = new ArrayObject<>();

                                                 for (Action action : actions)
                                                 {

                                                     if (action != null)
                                                     {
                                                         action.launch();
                                                         futures.add(action.future);
                                                     }
                                                 }

                                                 if (!futures.isEmpty())
                                                 {
                                                     this.future = this.future.waitFor(
                                                             futures.toArray(new Future[futures.size()]));
                                                 }
                                             });

        return this;
    }

    public final Action thenDo(Action action)
    {
        Objects.requireNonNull(action, "action MUST NOT be null!");
        this.mutex.playInCriticalSectionVoid(() ->
                                             {
                                                 this.play();
                                                 this.future = this.future.andCombine(ignored ->
                                                                                      {
                                                                                          action.launch();
                                                                                          return action.future;
                                                                                      });
                                             });

        return this;
    }
}

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
import java.util.List;
import java.util.Objects;
import jhelp.util.list.ArrayObject;

/**
 * Represents a action. <br>
 * Actions are tasks that can be done one after an other (That is to say: when an action A is finished do action B, then action C ...)<br>
 * They can also be executed in parallel.
 */
public abstract class Action
{
    /**
     * Create and launch immediately an action
     *
     * @param runnableTask Task to do
     * @return Created and launched action
     */
    public static @NotNull Action launch(final @NotNull RunnableTask runnableTask)
    {
        Objects.requireNonNull(runnableTask, "runnableTask MUST NOT be null!");
        Action action = new RunnableAction(runnableTask);
        action.launch();
        return action;
    }

    /**
     * Future linked to action
     */
    private       Future<Void> future;
    /**
     * Synchronization mutex
     */
    private final Mutex        mutex;

    /**
     * Create the action
     */
    public Action()
    {
        this.mutex = new Mutex();
    }

    /**
     * Play the action
     */
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

    /**
     * Do the action stuffs
     *
     * @throws ActionException On action issues
     */
    public abstract void doAction() throws ActionException;

    /**
     * Launch the action
     *
     * @return This action, convenient for chaining
     */
    public final Action launch()
    {
        this.mutex.playInCriticalSectionVoid(this::play);
        return this;
    }

    /**
     * Launch a task if action is on error
     *
     * @param consumerTask Task to do if their an error. The parameter is the exception happen
     * @return This action, convenient for chaining
     */
    public final @NotNull Action onError(@NotNull ConsumerTask<ActionException> consumerTask)
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

    /**
     * Do a task after this action. The task is play if this action is succeed or failed
     *
     * @param runnableTask Task to do
     * @return This action, convenient for chaining
     */
    public final @NotNull Action onFinish(@NotNull RunnableTask runnableTask)
    {
        Objects.requireNonNull(runnableTask, "runnableTask MUST NOT be null!");
        this.mutex.playInCriticalSectionVoid(() ->
                                             {
                                                 this.play();
                                                 this.future = this.future.thenRun(runnableTask);
                                             });

        return this;
    }

    /**
     * Launch actions in parallel to this action. <br>
     * Any future chaining (for example {@link #thenDo(Action)}) will wait for all the given actions
     *
     * @param actions Actions to play in parallel
     * @return This action, convenient for chaining
     */
    public final @NotNull Action parallel(@NotNull Action... actions)
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

    /**
     * Do an other action after this one succeed
     *
     * @param action Action to do after
     * @return This action, convenient for chaining
     */
    public final @NotNull Action thenDo(@NotNull Action action)
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

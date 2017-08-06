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

package jhelp.game.anim;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.Objects;
import jhelp.game.gui.GameAnimation;
import jhelp.util.gui.JHelpImage;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.RunnableTask;
import jhelp.util.thread.ThreadManager;

/**
 * Animation that launch a task at given frame
 */
public final class GameTaskAnimation<P> extends GameAnimation
{
    /**
     * Frame where launch the task
     */
    private final int             atFrame;
    /**
     * Task parameter
     */
    private final P               parameter;
    /**
     * Task to play
     */
    private final ConsumerTask<P> task;

    /**
     * Launch task since animation is played
     *
     * @param task Task to do
     */
    public GameTaskAnimation(@NotNull final RunnableTask task)
    {
        this(ignored -> task.run(), null, 0);
    }

    /**
     * Launch task at given frame
     *
     * @param task    Task to do
     * @param atFrame Frame when launch the task
     */
    public GameTaskAnimation(@NotNull final RunnableTask task, final int atFrame)
    {
        this(ignored -> task.run(), null, atFrame);
    }

    /**
     * Launch task since animation is played
     *
     * @param task      Task to do
     * @param parameter Parameter to give to task when played
     */
    public GameTaskAnimation(@NotNull final ConsumerTask<P> task, @Nullable final P parameter)
    {
        this(task, parameter, 0);
    }

    /**
     * Launch task at given frame
     *
     * @param task      Task to do
     * @param parameter Parameter to give to task when played
     * @param atFrame   Frame when launch the task
     */
    public GameTaskAnimation(@NotNull final ConsumerTask<P> task, @Nullable final P parameter, final int atFrame)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        this.task = task;
        this.parameter = parameter;
        this.atFrame = Math.max(0, atFrame);
    }

    /**
     * Called when animation is terminated.<br>
     * The given image parent is locked in not draw mode, to remove properly sprites linked to this animation
     *
     * @param parent Image parent to remove properly animation sprites
     */
    @Override
    protected void endAnimation(final JHelpImage parent)
    {
        //Nothing to do
    }

    /**
     * Called when animation start<br>
     * The given image parent is locked in not draw mode, to let opportunity to create some sprites if animation need them
     *
     * @param parent Image parent to create sprites if need
     */
    @Override
    protected void startAnimation(final JHelpImage parent)
    {
        //Nothing to do
    }

    /**
     * Called to update the animation
     *
     * @param frame  Animation frame
     * @param parent Image parent where draw, locked in draw mode
     * @return {@code true} to indicates animation continue. {@code false} to indicates animation is finished
     */
    @Override
    protected boolean updateAnimation(final float frame, final JHelpImage parent)
    {
        if (frame >= this.atFrame)
        {
            ThreadManager.parallel(this.task, this.parameter);
            return false;
        }

        return true;
    }
}

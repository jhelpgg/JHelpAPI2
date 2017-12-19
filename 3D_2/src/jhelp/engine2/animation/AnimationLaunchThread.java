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
package jhelp.engine2.animation;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import jhelp.engine2.render.ThreadAnimation;
import jhelp.util.thread.Task;
import jhelp.util.thread.ThreadManager;

/**
 * Special animation that launch a threaded task when its turns comes
 *
 * @param <PARAMETER> Threaded task parameter type
 * @param <RESULT>    Threaded task result type
 * @author JHelp
 */
public class AnimationLaunchThread<PARAMETER, RESULT>
        implements Animation
{
    /**
     * Threaded task parameter
     */
    private final PARAMETER               parameter;
    /**
     * Threaded task
     */
    private final Task<PARAMETER, RESULT> threadedTask;

    /**
     * Create a new instance of AnimationLaunchThread
     *
     * @param threadedTask Task to launch
     * @param parameter    Parameter give to to the task when it will be launch
     */
    public AnimationLaunchThread(
            final @NotNull Task<PARAMETER, RESULT> threadedTask, final @Nullable PARAMETER parameter)
    {
        if (threadedTask == null)
        {
            throw new NullPointerException("threadedTask mustn't be null");
        }

        this.threadedTask = threadedTask;
        this.parameter = parameter;
    }

    /**
     * Play the animation. Launch the task <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param absoluteFrame Absolute frame
     * @return {@code false} because animation is finished since task is launched
     * @see Animation#animate(float)
     */
    @Override
    @ThreadAnimation
    public boolean animate(final float absoluteFrame)
    {
        ThreadManager.doTask(this.threadedTask, this.parameter);

        return false;
    }

    /**
     * Called when animation start <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param startAbsoluteFrame Start absolute frame
     * @see Animation#startAbsoluteFrame(float)
     */
    @Override
    @ThreadAnimation
    public void startAbsoluteFrame(final float startAbsoluteFrame)
    {
    }
}
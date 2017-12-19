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

/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.engine.anim;

import javax.media.opengl.GL;
import jhelp.engine.Animation;
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
    public AnimationLaunchThread(final Task<PARAMETER, RESULT> threadedTask, final PARAMETER parameter)
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
     * @param gl            Open GL context
     * @param absoluteFrame Absolute frame
     * @return {@code false} because animation is finished since task is launched
     * @see jhelp.engine.Animation#animate(javax.media.opengl.GL, float)
     */
    @Override
    public boolean animate(final GL gl, final float absoluteFrame)
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
     * @see jhelp.engine.Animation#setStartAbsoluteFrame(float)
     */
    @Override
    public void setStartAbsoluteFrame(final float startAbsoluteFrame)
    {
    }
}
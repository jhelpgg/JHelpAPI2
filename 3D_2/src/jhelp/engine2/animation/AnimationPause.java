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

import jhelp.engine2.render.ThreadAnimation;

/**
 * Special animation that make a pause inside animation.<br>
 * Animation does nothing an amount of time
 *
 * @author JHelp
 */
public class AnimationPause
        implements Animation
{
    /**
     * Pause duration express in frame
     */
    private final int   durationPauseInFrame;
    /**
     * Start absolute frame
     */
    private       float startAbsoluteFrame;

    /**
     * Create a new instance of AnimationPause
     *
     * @param durationPauseInFrame Pause duration express in frame
     */
    public AnimationPause(final int durationPauseInFrame)
    {
        this.durationPauseInFrame = Math.max(1, durationPauseInFrame);
    }

    /**
     * Called when animation played <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param absoluteFrame Absolute frame
     * @return {@code true} if animation have to continue. {@code false} if aniumation finished
     * @see Animation#animate(float)
     */
    @Override
    @ThreadAnimation
    public boolean animate(final float absoluteFrame)
    {
        return (absoluteFrame - this.startAbsoluteFrame) <= this.durationPauseInFrame;
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
        this.startAbsoluteFrame = startAbsoluteFrame;
    }
}
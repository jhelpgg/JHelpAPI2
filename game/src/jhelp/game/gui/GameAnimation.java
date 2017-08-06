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

package jhelp.game.gui;

import jhelp.game.anim.AnimationStatus;
import jhelp.util.gui.JHelpImage;

/**
 * Generic animation
 */
public abstract class GameAnimation
{
    /**
     * Animation status
     */
    private AnimationStatus animationStatus;
    /**
     * Absolute frame where animation started
     */
    private float           startAbsoluteFrame;

    /**
     * Create animation
     */
    public GameAnimation()
    {
        this.animationStatus = AnimationStatus.NOT_LAUNCHED;
    }

    /**
     * Call by system to start animation.<br>
     * The given image parent is locked in not draw mode, to let opportunity to create some sprites if animation need them
     *
     * @param startAbsoluteFrame Absolute frame where animation started
     * @param parent             Image parent to create sprites if need
     */
    final void start(float startAbsoluteFrame, JHelpImage parent)
    {
        this.animationStatus = AnimationStatus.RUNNING;
        this.startAbsoluteFrame = startAbsoluteFrame;
        this.startAnimation(parent);
    }

    /**
     * Called by system to update the animation
     *
     * @param absoluteFrame System absolute frame
     * @param parent        Image parent where draw, locked in draw mode
     * @return {@code true} to indicates animation continue. {@code false} to indicates animation is finished
     */
    final boolean update(float absoluteFrame, JHelpImage parent)
    {
        boolean loopAgain = this.updateAnimation(absoluteFrame - this.startAbsoluteFrame, parent);

        if (!loopAgain)
        {
            this.animationStatus = AnimationStatus.FINISHED;
        }

        return loopAgain;
    }

    /**
     * Called when animation is terminated.<br>
     * The given image parent is locked in not draw mode, to remove properly sprites linked to this animation
     *
     * @param parent Image parent to remove properly animation sprites
     */
    protected abstract void endAnimation(JHelpImage parent);

    /**
     * Called when animation start<br>
     * The given image parent is locked in not draw mode, to let opportunity to create some sprites if animation need them
     *
     * @param parent Image parent to create sprites if need
     */
    protected abstract void startAnimation(JHelpImage parent);

    /**
     * Called to update the animation
     *
     * @param frame  Animation frame
     * @param parent Image parent where draw, locked in draw mode
     * @return {@code true} to indicates animation continue. {@code false} to indicates animation is finished
     */
    protected abstract boolean updateAnimation(float frame, JHelpImage parent);

    /**
     * Animation status
     *
     * @return Animation status
     */
    public final AnimationStatus animationStatus()
    {
        return this.animationStatus;
    }
}

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
import jhelp.engine2.render.ThreadAnimation;

/**
 * A generic animation for able to play animation without link with JOGL need from external program
 *
 * @author JHelp
 */
public class GenericAnimation
        implements Animation
{
    /**
     * External animation
     */
    private final ExternalAnimation externalAnimation;
    /**
     * Start absolute frame
     */
    private       float             startAbsoluteFrame;

    /**
     * Create a new instance of GenericAnimation
     *
     * @param externalAnimation External animation
     */
    public GenericAnimation(final @NotNull ExternalAnimation externalAnimation)
    {
        if (externalAnimation == null)
        {
            throw new NullPointerException("externalAnimation mustn't be null");
        }

        this.externalAnimation = externalAnimation;
    }

    /**
     * Called when animation playing <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param absoluteFrame Absolute frame
     * @return {@code true} if animation should continue
     * @see Animation#animate(float)
     */
    @Override
    @ThreadAnimation
    public final boolean animate(final float absoluteFrame)
    {
        return this.externalAnimation.playAnimation(absoluteFrame - this.startAbsoluteFrame);
    }

    /**
     * Called when animation started <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param startAbsoluteFrame Start absolute frame
     * @see Animation#startAbsoluteFrame(float)
     */
    @Override
    @ThreadAnimation
    public final void startAbsoluteFrame(final float startAbsoluteFrame)
    {
        this.startAbsoluteFrame = startAbsoluteFrame;
        this.externalAnimation.initializeAnimation();
    }
}
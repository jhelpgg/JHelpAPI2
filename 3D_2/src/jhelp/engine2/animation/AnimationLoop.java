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
import java.util.Objects;
import jhelp.engine2.render.ThreadAnimation;

/**
 * Animation that repeat a task in a loop
 */
public final class AnimationLoop implements Animation
{
    /**
     * Animation loop to play
     */
    private final AnimateLoop animateLoop;
    /**
     * Start frame
     */
    private       float       startAbsoluteFrame;

    /**
     * Create an animation loop
     *
     * @param animateLoop Animation to loop
     */
    public AnimationLoop(@NotNull AnimateLoop animateLoop)
    {
        Objects.requireNonNull(animateLoop, "animateLoop MUST NOT be null!");
        this.animateLoop = animateLoop;
    }

    /**
     * Call by the renderer each time the animation is refresh on playing
     *
     * @param absoluteFrame Actual absolute frame
     * @return {@code true} if the animation need to be refresh one more time. {@code false} if the animation is end
     */
    @Override
    @ThreadAnimation
    public boolean animate(final float absoluteFrame)
    {
        return this.animateLoop.animate(absoluteFrame - this.startAbsoluteFrame);
    }

    /**
     * Call by the renderer to indicates the start absolute frame
     *
     * @param startAbsoluteFrame Start absolute frame
     */
    @Override
    @ThreadAnimation
    public void startAbsoluteFrame(final float startAbsoluteFrame)
    {
        this.startAbsoluteFrame = startAbsoluteFrame;
    }

    /**
     * An animate loop
     */
    public interface AnimateLoop
    {
        /**
         * Play animation
         *
         * @param frame Animation relative frame
         * @return Indicates if have to loop one more time
         */
        @ThreadAnimation
        boolean animate(float frame);
    }
}

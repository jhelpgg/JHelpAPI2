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

import com.sun.istack.internal.NotNull;
import java.util.Objects;
import jhelp.game.anim.AnimationStatus;
import jhelp.util.gui.JHelpImage;

/**
 * Animation that repeat an animation several times
 */
public final class GameAnimationLoop extends GameAnimation
{
    /**
     * Animation to repeat
     */
    private final GameAnimation gameAnimation;
    /**Number loop left to do*/
    private       int           loopLeft;
    /**Number total of loop*/
    private final int           numberOfLoop;

    /**
     * Create the animation
     * @param gameAnimation Animation to repeat
     * @param numberOfLoop Number of loop
     */
    public GameAnimationLoop(@NotNull final GameAnimation gameAnimation, final int numberOfLoop)
    {
        Objects.requireNonNull(gameAnimation, "gameAnimation MUST NOT be null!");
        this.gameAnimation = gameAnimation;
        this.numberOfLoop = Math.max(1, numberOfLoop);
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
        this.loopLeft = this.numberOfLoop;
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
        if (this.gameAnimation.animationStatus() != AnimationStatus.RUNNING)
        {
            parent.playWhenExitDrawMode(image -> this.gameAnimation.start(frame, image));
            return true;
        }

        if (this.gameAnimation.update(frame, parent))
        {
            return true;
        }

        parent.playWhenExitDrawMode(this.gameAnimation::endAnimation);
        this.loopLeft--;
        return this.loopLeft > 0;
    }
}

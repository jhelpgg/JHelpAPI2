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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jhelp.game.anim.AnimationStatus;
import jhelp.util.gui.JHelpImage;

/**
 * Created by jhelp on 14/07/17.
 */
public final class GameAnimationSequence extends GameAnimation
{
    private final List<GameAnimation> animations;
    private       int                 index;

    public GameAnimationSequence()
    {
        this.animations = new ArrayList<>();
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
        this.index = 0;
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
        synchronized (this.animations)
        {
            int size = this.animations.size();

            if (this.index >= size)
            {
                return false;
            }

            final GameAnimation gameAnimation = this.animations.get(this.index);

            if (gameAnimation.animationStatus() != AnimationStatus.RUNNING)
            {
                parent.playWhenExitDrawMode(image -> gameAnimation.start(frame, image));
                return true;
            }

            if (!gameAnimation.update(frame, parent))
            {
                parent.playWhenExitDrawMode(gameAnimation::endAnimation);
                this.index++;

                if (this.index >= size)
                {
                    return false;
                }

                final GameAnimation animation = this.animations.get(this.index);
                parent.playWhenExitDrawMode(image -> animation.start(frame, image));
                return true;
            }

            return true;
        }
    }

    public void add(@NotNull GameAnimation gameAnimation)
    {
        Objects.requireNonNull(gameAnimation, "gameAnimation MUST NOT be null!");

        synchronized (this.animations)
        {
            this.animations.add(gameAnimation);
        }
    }

    public void remove(@NotNull GameAnimation gameAnimation)
    {
        synchronized (this.animations)
        {
            this.animations.remove(gameAnimation);
        }
    }
}

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
public final class GameAnimationParallel extends GameAnimation
{
    private final List<GameAnimation> animations;

    public GameAnimationParallel()
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
        synchronized (this.animations)
        {
            for (GameAnimation gameAnimation : this.animations)
            {
                if (gameAnimation.animationStatus() == AnimationStatus.RUNNING)
                {
                    gameAnimation.endAnimation(parent);
                }
            }
        }
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
        synchronized (this.animations)
        {
            for (GameAnimation gameAnimation : this.animations)
            {
                gameAnimation.start(0, parent);
            }
        }
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
        boolean otherLoop = false;

        synchronized (this.animations)
        {
            for (GameAnimation gameAnimation : this.animations)
            {
                switch (gameAnimation.animationStatus())
                {
                    case NOT_LAUNCHED:
                        parent.playWhenExitDrawMode(image -> gameAnimation.start(frame, image));
                        otherLoop |= true;
                        break;
                    case RUNNING:
                        boolean loop = gameAnimation.update(frame, parent);

                        if (!loop)
                        {
                            parent.playWhenExitDrawMode(gameAnimation::endAnimation);
                        }

                        otherLoop |= loop;
                        break;
                }
            }
        }

        return otherLoop;
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

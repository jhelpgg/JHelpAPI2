package jhelp.game.gui;

import com.sun.istack.internal.NotNull;
import java.util.Objects;
import jhelp.game.anim.AnimationStatus;
import jhelp.util.gui.JHelpImage;

/**
 * Created by jhelp on 14/07/17.
 */
public class GameAnimationLoop extends GameAnimation
{
    private final GameAnimation gameAnimation;
    private final int           numberOfLoop;
    private       int           loopLeft;

    public GameAnimationLoop(@NotNull final GameAnimation gameAnimation, final int numberOfLoop)
    {
        Objects.requireNonNull(gameAnimation, "gameAnimation MUST NOT be null!");
        this.gameAnimation = gameAnimation;
        this.numberOfLoop = Math.max(1, numberOfLoop);
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

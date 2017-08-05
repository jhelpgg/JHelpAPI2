package jhelp.game.anim;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.Objects;
import jhelp.game.gui.GameAnimation;
import jhelp.util.gui.JHelpImage;
import jhelp.util.thread.ThreadManager;
import jhelp.util.thread.ConsumerTask;

/**
 * Created by jhelp on 14/07/17.
 */
public class GameTaskAnimation<P> extends GameAnimation
{
    private final ConsumerTask<P> task;
    private final P               parameter;
    private final int             atFrame;

    public GameTaskAnimation(@NotNull final ConsumerTask<P> task)
    {
        this(task, null, 0);
    }

    public GameTaskAnimation(@NotNull final ConsumerTask<P> task, final int atFrame)
    {
        this(task, null, atFrame);
    }

    public GameTaskAnimation(@NotNull final ConsumerTask<P> task, @Nullable final P parameter)
    {
        this(task, parameter, 0);
    }

    public GameTaskAnimation(@NotNull final ConsumerTask<P> task, @Nullable final P parameter, final int atFrame)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        this.task = task;
        this.parameter = parameter;
        this.atFrame = Math.max(0, atFrame);
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
        //Nothing to do
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
        if (frame >= this.atFrame)
        {
            ThreadManager.parallel(this.task, this.parameter);
            return false;
        }

        return true;
    }
}

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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jhelp.engine2.render.ThreadAnimation;

/**
 * List of animation played sequentially.<br>
 * In other worlds, when first animation of the list is finish, second start, then third, ....<br>
 * List of animation can be loop several times
 *
 * @author JHelp
 */
public class MultiAnimation
        implements Animation
{
    /**
     * Animation list
     */
    private final List<Animation> animations;
    /**
     * Index of actual animation
     */
    private       int             index;
    /**
     * Number of loop to do at total
     */
    private final int             numberOfLoop;
    /**
     * Number of loop left
     */
    public        int             loopLeft;

    /**
     * Create a new instance of MultiAnimation that played one time
     */
    public MultiAnimation()
    {
        this(1);
    }

    /**
     * Create a new instance of MultiAnimation that played one or "infinite" time
     *
     * @param loop {@code true} for "infinite", {@code false} for one time
     */
    public MultiAnimation(final boolean loop)
    {
        this(loop ? Integer.MAX_VALUE : 1);
    }

    /**
     * Create a new instance of MultiAnimation played a determinate number of time
     *
     * @param numberOfLoop Number of time to play animation list
     */
    public MultiAnimation(final int numberOfLoop)
    {
        this.numberOfLoop = Math.max(1, numberOfLoop);
        this.loopLeft = this.numberOfLoop;
        this.animations = new ArrayList<>();
        this.index = 0;
    }

    /**
     * Add an animation to the list
     *
     * @param animation Added animation
     */
    public void addAnimation(final @NotNull Animation animation)
    {
        Objects.requireNonNull(animation, "animation MUST NOT be null!");
        this.animations.add(animation);
    }

    /**
     * Called when animation list is playing <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param absoluteFrame Absolute frame
     * @return {@code false} if animation list finished and no more loop left
     * @see Animation#animate(float)
     */
    @Override
    @ThreadAnimation
    public boolean animate(final float absoluteFrame)
    {
        final int size = this.animations.size();
        if (this.index >= size)
        {
            this.loopLeft--;
            if ((this.loopLeft <= 0) || (size <= 0))
            {
                return false;
            }

            this.index = 0;
            this.animations.get(0).startAbsoluteFrame(absoluteFrame);
        }

        boolean cont = this.animations.get(this.index).animate(absoluteFrame);

        while (!cont)
        {
            this.index++;

            if (this.index >= size)
            {
                this.loopLeft--;
                if (this.loopLeft <= 0)
                {
                    return false;
                }

                this.index = 0;
            }

            this.animations.get(this.index).startAbsoluteFrame(absoluteFrame);
            cont = this.animations.get(this.index).animate(absoluteFrame);
        }

        return true;
    }

    /**
     * Called when animation initialize <br>
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
        this.index = 0;
        this.loopLeft = this.numberOfLoop;

        if (!this.animations.isEmpty())
        {
            this.animations.get(0).startAbsoluteFrame(startAbsoluteFrame);
        }
    }
}
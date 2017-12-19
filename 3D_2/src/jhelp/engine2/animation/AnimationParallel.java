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
import com.sun.istack.internal.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import jhelp.engine2.render.ThreadAnimation;
import jhelp.util.list.Pair;

/**
 * Animation that does several animation in parallel
 *
 * @author JHelp
 */
public final class AnimationParallel
        implements Animation
{
    /**
     * List of animation, playing status pairs
     */
    private final List<Pair<Animation, Boolean>> animations;
    /**
     * Indicates if at least one animation is playing
     */
    private final AtomicBoolean playing = new AtomicBoolean(false);

    /**
     * Create a new instance of AnimationParallel
     */
    public AnimationParallel()
    {
        this.animations = new ArrayList<Pair<Animation, Boolean>>();
    }

    /**
     * Add an animation
     *
     * @param animation Animation to add
     * @return {@code true} if animation added
     * @throws IllegalStateException If animation is playing
     */
    public boolean addAnimation(final @NotNull Animation animation)
    {
        if (animation == null)
        {
            throw new NullPointerException("animation mustn't be null");
        }

        synchronized (this.playing)
        {
            if (this.playing.get())
            {
                throw new IllegalStateException("Can't add animation while playing");
            }
        }

        return this.animations.add(new Pair<>(animation, true));
    }

    /**
     * Play the animation <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param absoluteFrame Absolute frame
     * @return {@code true} if at least animation playing
     * @see Animation#animate(float)
     */
    @Override
    @ThreadAnimation
    public boolean animate(final float absoluteFrame)
    {
        boolean moreAnimation = false;

        for (final Pair<Animation, Boolean> pair : this.animations)
        {
            if (pair.second)
            {
                if (pair.first.animate(absoluteFrame))
                {
                    moreAnimation = true;
                }
                else
                {
                    pair.second = false;
                }
            }
        }

        if (!moreAnimation)
        {
            synchronized (this.playing)
            {
                this.playing.set(false);
            }
        }

        return moreAnimation;
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
    public void startAbsoluteFrame(final float startAbsoluteFrame)
    {
        synchronized (this.playing)
        {
            this.playing.set(true);
        }

        for (final Pair<Animation, Boolean> pair : this.animations)
        {
            pair.first.startAbsoluteFrame(startAbsoluteFrame);
            pair.second = true;
        }
    }

    /**
     * Indicates if animation playing
     *
     * @return {@code true} if animation playing
     */
    public boolean playing()
    {
        synchronized (this.playing)
        {
            return this.playing.get();
        }
    }

    /**
     * Remove an animation
     *
     * @param animation Animation to remove
     * @return {@code true} if remove succeed
     * @throws IllegalStateException If animation is playing
     */
    public boolean removeAnimation(final @Nullable Animation animation)
    {
        if (animation == null)
        {
            return false;
        }

        synchronized (this.playing)
        {
            if (this.playing.get())
            {
                throw new IllegalStateException("Can't remove animation while playing");
            }
        }

        final int                size = this.animations.size();
        Pair<Animation, Boolean> pair;

        for (int i = size - 1; i >= 0; i--)
        {
            pair = this.animations.get(i);

            if (pair.first.equals(animation))
            {
                this.animations.remove(i);
                return true;
            }
        }

        return false;
    }
}
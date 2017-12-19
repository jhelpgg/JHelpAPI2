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

package jhelp.engine2.render;

import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jhelp.engine2.animation.Animation;
import jhelp.util.thread.ThreadManager;

/**
 * Manage animations inside a 3D window
 */
public final class AnimationsManager
{
    /**
     * Indicates if animations are alive
     */
    private       boolean         alive;
    /**
     * Animations list
     */
    private final List<Animation> animations;
    /**
     * 3D window parent
     */
    private final Window3D        window3D;

    /**
     * Create the animation manager
     */
    AnimationsManager(@NotNull Window3D window3D)
    {
        this.window3D = window3D;
        this.animations = new ArrayList<>();
        this.alive = true;
    }

    /**
     * Play one loop of animations
     */
    private void playAnimations()
    {
        synchronized (this.animations)
        {
            final long  start         = System.currentTimeMillis();
            final float absoluteFrame = this.window3D.absoluteFrame();
            Animation   animation;

            for (int index = this.animations.size() - 1; index >= 0 && this.alive; index--)
            {
                animation = this.animations.get(index);

                if (!animation.animate(absoluteFrame))
                {
                    this.animations.remove(index);
                }
            }

            if (!this.animations.isEmpty() && this.alive)
            {
                ThreadManager.parallel(this::playAnimations, 32 - System.currentTimeMillis() + start);
            }
        }
    }

    /**
     * Destroy the manager
     */
    void destroy()
    {
        this.alive = false;
    }

    /**
     * Launch an animation
     *
     * @param animation Animation to play
     */
    public void play(@NotNull Animation animation)
    {
        Objects.requireNonNull(animation, "animation MUST NOT be null!");

        if (!this.alive)
        {
            return;
        }

        synchronized (this.animations)
        {
            final boolean wasEmpty = this.animations.isEmpty();

            if (!this.animations.contains(animation))
            {
                animation.startAbsoluteFrame(this.window3D.absoluteFrame());
                this.animations.add(animation);
            }

            if (wasEmpty)
            {
                ThreadManager.parallel(this::playAnimations);
            }
        }
    }

    /**
     * Stop an animation
     *
     * @param animation Animation to stop
     */
    public void stop(@NotNull Animation animation)
    {
        Objects.requireNonNull(animation, "animation MUST NOT be null!");

        synchronized (this.animations)
        {
            this.animations.remove(animation);
        }
    }
}

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

package jhelp.game.anim;

import com.sun.istack.internal.NotNull;
import java.util.Objects;
import jhelp.game.gui.GameAnimation;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.dynamic.Interpolation;
import jhelp.util.gui.dynamic.Interpolations;
import jhelp.util.list.Couple;
import jhelp.util.list.SortedArray;
import jhelp.util.math.Math2;

/**
 * Animation based on key frame.<br>
 * Principe is to defines a value for some frame for an object and between an interpolation is made to do the transition.
 *
 * @param <O> Object to change value type
 * @param <V> Value type
 */
public abstract class GameKeyFrameAnimation<O, V> extends GameAnimation
{
    /**
     * Key frames
     */
    private final SortedArray<KeyFrame> keyFrames;
    /**
     * Object that a property change
     */
    private final O                     object;
    /**
     * Start value
     */
    private       V                     startValue;

    /**
     * Create the animation
     *
     * @param object Object to change value
     */
    public GameKeyFrameAnimation(@NotNull O object)
    {
        Objects.requireNonNull(object, "object MUST NOT be null!");
        this.object = object;
        this.keyFrames = new SortedArray<>(KeyFrame.class, true);
    }

    /**
     * Called when animation is terminated.<br>
     * The given image parent is locked in not draw mode, to remove properly sprites linked to this animation<br>
     * Does nothing by default
     *
     * @param parent Image parent to remove properly animation sprites
     */
    protected void animationEnded(@NotNull final JHelpImage parent)
    {
        //Does nothing by default
    }

    /**
     * Called when animation start<br>
     * The given image parent is locked in not draw mode, to let opportunity to create some sprites if animation need them<br>
     * Does nothing by default
     *
     * @param parent Image parent to create sprites if need
     */
    protected void animationStarted(@NotNull final JHelpImage parent)
    {
        //Does nothing by default
    }

    /**
     * Apply a value to given object
     *
     * @param value  Value to give
     * @param object Object where apply the value
     * @param parent Image parent lovked in draw mode to draw something if need
     */
    protected abstract void apply(@NotNull V value, @NotNull O object, @NotNull JHelpImage parent);

    /**
     * Called when animation is terminated.<br>
     * The given image parent is locked in not draw mode, to remove properly sprites linked to this animation
     *
     * @param parent Image parent to remove properly animation sprites
     */
    @Override
    protected final void endAnimation(@NotNull final JHelpImage parent)
    {
        this.animationEnded(parent);
        this.startValue = null;
    }

    /**
     * Compute interpolation value between two values
     *
     * @param value1  Start value
     * @param value2  End value
     * @param percent Percent progress to go from first to second. In [0, 1]
     * @return Interpolated value
     */
    protected abstract @NotNull V interpolate(@NotNull V value1, @NotNull V value2, float percent);

    /**
     * Called to update the animation
     *
     * @param frame  Animation frame
     * @param parent Image parent where draw, locked in draw mode
     * @return {@code true} to indicates animation continue. {@code false} to indicates animation is finished
     */
    @Override
    protected final boolean updateAnimation(final float frame, @NotNull final JHelpImage parent)
    {
        final int         intFrame    = (int) frame;
        final KeyFrame<V> keyFrame    = new KeyFrame<>(intFrame, null, null);
        final Couple      interval    = this.keyFrames.intervalOf(keyFrame);
        boolean           oneMoreTurn = true;
        V                 value;

        if (interval.first < 0)
        {
            value = this.interpolate(this.startValue,
                                     (V) this.keyFrames.get(0).value,
                                     this.keyFrames.get(0).interpolation.interpolation(frame));
        }
        else if (interval.second < 0)
        {
            oneMoreTurn = false;
            value = (V) this.keyFrames.get(this.keyFrames.size() - 1).value;
        }
        else if (interval.first == interval.second)
        {
            if (Math2.equals(frame, interval.first))
            {
                value = (V) this.keyFrames.get(interval.first).value;
            }
            else if (interval.second == this.keyFrames.size() - 1)
            {
                value = (V) this.keyFrames.get(interval.second).value;
            }
            else
            {
                final KeyFrame<V> keyFrameFirst  = (KeyFrame<V>) this.keyFrames.get(interval.first);
                final KeyFrame<V> keyFrameSecond = (KeyFrame<V>) this.keyFrames.get(interval.second + 1);
                final float       first          = keyFrameFirst.frame;
                final float       second         = keyFrameSecond.frame;
                final float       percent        = (frame - first) / (second - first);
                value = this.interpolate(keyFrameFirst.value,
                                         keyFrameSecond.value,
                                         keyFrameSecond.interpolation.interpolation(percent));
            }
        }
        else
        {
            final KeyFrame<V> keyFrameFirst  = (KeyFrame<V>) this.keyFrames.get(interval.first);
            final KeyFrame<V> keyFrameSecond = (KeyFrame<V>) this.keyFrames.get(interval.second);
            final float       first          = keyFrameFirst.frame;
            final float       second         = keyFrameSecond.frame;
            final float       percent        = (frame - first) / (second - first);
            value = this.interpolate(keyFrameFirst.value,
                                     keyFrameSecond.value,
                                     keyFrameSecond.interpolation.interpolation(percent));
        }

        this.apply(value, this.object, parent);
        return oneMoreTurn;
    }

    /**
     * compute current value for given object
     *
     * @param object Object to get current value
     * @return Current value
     */
    protected abstract @NotNull V obtainValue(@NotNull O object);

    /**
     * Called when animation start<br>
     * The given image parent is locked in not draw mode, to let opportunity to create some sprites if animation need them
     *
     * @param parent Image parent to create sprites if need
     */
    @Override
    protected final void startAnimation(@NotNull final JHelpImage parent)
    {
        this.startValue = this.obtainValue(this.object);
        this.animationStarted(parent);
    }

    /**
     * Define the value for a specific frame
     *
     * @param frame Frame. Must be >= 0
     * @param value Value at frame
     */
    public final void setFrame(int frame, @NotNull V value)
    {
        this.setFrame(frame, value, Interpolations.COSINUS);
    }

    /**
     * Define the value for a specific frame
     *
     * @param frame         Frame. Must be >= 0
     * @param value         Value at frame
     * @param interpolation Interpolation to use to go to this frame
     */
    public final void setFrame(int frame, @NotNull V value, @NotNull Interpolation interpolation)
    {
        Objects.requireNonNull(value, "value MUST NOT be null!");
        Objects.requireNonNull(interpolation, "interpolation MUST NOT be null!");

        if (frame < 0)
        {
            throw new IllegalArgumentException("frame MUST be >=0 not " + frame);
        }

        final KeyFrame<V> keyFrame = new KeyFrame<>(frame, value, interpolation);
        this.keyFrames.remove(keyFrame);
        this.keyFrames.add(keyFrame);
    }
}

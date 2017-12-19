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
import jhelp.engine2.render.ThreadAnimation;
import jhelp.util.list.ArrayInt;

/**
 * Generic animation by key frames<br>
 * Use when animation is compose on (key,value) pair.<br>
 * This class says at that frame the object state must be that
 *
 * @param <ObjectType> Type of the modified object
 * @param <Value>      Type of the value change by the animation
 * @author JHelp
 */
public abstract class AnimationKeyFrame<ObjectType, Value>
        implements Animation
{
    /**
     * Keys list
     */
    private final ArrayInt    keys;
    /**
     * Object modified
     */
    private final ObjectType  object;
    /**
     * Absolute start frame
     */
    private       float       startAbsoluteFrame;
    /**
     * Start vale
     */
    private       Value       startValue;
    /**
     * Values list
     */
    private final List<Value> values;

    /**
     * Constructs AnimationKeyFrame
     *
     * @param object Object modified
     */
    public AnimationKeyFrame(final @NotNull ObjectType object)
    {
        if (object == null)
        {
            throw new NullPointerException("The object mustn't be null !");
        }

        this.keys = new ArrayInt();
        this.object = object;
        this.values = new ArrayList<>();
    }

    /**
     * Interpolate a value and change the object state
     *
     * @param object  Object to change
     * @param before  Value just before the wanted state
     * @param after   Value just after the wanted state
     * @param percent Percent of interpolation
     */
    @ThreadAnimation
    protected abstract void interpolateValue(
            @NotNull ObjectType object, @NotNull Value before, @NotNull Value after, float percent);

    /**
     * Give the actual value for an object
     *
     * @param object Object we want extract the value
     * @return The actual value
     */
    @ThreadAnimation
    protected abstract @NotNull Value obtainValue(@NotNull ObjectType object);

    /**
     * Change object state
     *
     * @param object Object to change
     * @param value  New state value
     */
    @ThreadAnimation
    protected abstract void setValue(@NotNull ObjectType object, @NotNull Value value);

    /**
     * Add a frame
     *
     * @param key   Frame key
     * @param value Value at the frame
     * @throws IllegalArgumentException If key is negative
     */
    public final void addFrame(final int key, final @NotNull Value value)
    {
        int index;
        int size;

        if (value == null)
        {
            throw new NullPointerException("The value mustn't be null !");
        }

        if (key < 0)
        {
            throw new IllegalArgumentException("The key must be >=0 not " + key);
        }

        // If the key already exists, overwrite the old one
        index = this.keys.getIndex(key);
        if (index >= 0)
        {
            this.values.set(index, value);
            return;
        }

        // Compute where insert the frame
        size = this.keys.getSize();
        for (index = 0; (index < size) && (this.keys.getInteger(index) < key); index++)
        {
        }

        // If the insertion is not the end, insert it
        if (index < size)
        {
            this.keys.insert(key, index);
            this.values.add(index, value);
            return;
        }

        // If the insertion is the end, add it at end
        this.keys.add(key);
        this.values.add(value);
    }

    /**
     * Play the animation
     *
     * @param absoluteFrame Actual absolute frame
     * @return {@code true} if the animation is not finish
     * @see Animation#animate(float)
     */
    @Override
    @ThreadAnimation
    public final boolean animate(final float absoluteFrame)
    {
        int   firstFrame;
        int   lastFrame;
        int   frame;
        int   size;
        float actualFrame;
        float percent;
        Value before;
        Value after;

        // If there are no frame, nothing to do
        size = this.keys.getSize();
        if (size < 1)
        {
            return false;
        }

        // Compute reference frames
        firstFrame = this.keys.getInteger(0);
        lastFrame = this.keys.getInteger(size - 1);
        actualFrame = absoluteFrame - this.startAbsoluteFrame;

        // If we are before the first frame (It is possible to start at a frame
        // >0, the effect is an interpolation from the actual value, to the first
        // frame)
        if (actualFrame < firstFrame)
        {
            // Interpolate actual position to first frame
            if (this.startValue == null)
            {
                this.startValue = this.obtainValue(this.object);
            }

            before = this.startValue;
            after = this.values.get(0);
            percent = actualFrame / firstFrame;

            this.interpolateValue(this.object, before, after, percent);

            return true;
        }

        this.startValue = null;

        // If we are after the last frame, just position in the last frame and the
        // animation is done
        if (actualFrame >= lastFrame)
        {
            this.setValue(this.object, this.values.get(size - 1));
            return false;
        }

        // Compute the nearest frame index from the actual frame
        for (frame = 0; (frame < size) && (this.keys.getInteger(frame) < actualFrame); frame++)
        {
        }

        // If it is the first frame, just locate to the first and the animation
        // continue
        if (frame == 0)
        {
            this.setValue(this.object, this.values.get(0));
            return true;
        }

        // If it is after the last frame, locate at last and the animation is
        // finish
        if (frame >= size)
        {
            this.setValue(this.object, this.values.get(size - 1));
            return false;
        }

        // Interpolate the value and animation continue
        before = this.values.get(frame - 1);
        after = this.values.get(frame);
        percent = (actualFrame - this.keys.getInteger(frame - 1)) /
                  (this.keys.getInteger(frame) - this.keys.getInteger(frame - 1));

        this.interpolateValue(this.object, before, after, percent);

        return true;
    }

    /**
     * Define the start absolute frame
     *
     * @param startAbsoluteFrame New start absolute frame
     * @see Animation#startAbsoluteFrame(float)
     */
    @Override
    @ThreadAnimation
    public final void startAbsoluteFrame(final float startAbsoluteFrame)
    {
        this.startAbsoluteFrame = startAbsoluteFrame;
        this.startValue = null;
    }
}
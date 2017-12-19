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

/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.util.gui.dynamic;

/**
 * Animation key frame that change an object position
 *
 * @author JHelp
 */
public final class AnimationPosition<P extends Positionable>
        extends AnimationKeyFrame<P, Position>
{
    /**
     * Create a new instance of AnimationPosition
     *
     * @param object        Object to move
     * @param numberOfLoop  Number of loop
     * @param interpolation Interpolation type
     */
    public AnimationPosition(final P object, final int numberOfLoop, final Interpolation interpolation)
    {
        super(object, numberOfLoop, interpolation);
    }

    /**
     * Obtain an object position <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param object Object to get its position
     * @return Object's position
     * @see AnimationKeyFrame#createValue(Object)
     */
    @Override
    public Position createValue(final P object)
    {
        return object.position();
    }

    /**
     * Interpolate position and locate object in interpolated value <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param object  Object to move
     * @param before  Position before
     * @param after   Position after
     * @param percent Interpolation value
     * @see AnimationKeyFrame#interpolate(Object, Object, Object, float)
     */
    @Override
    public void interpolate(final P object, final Position before, final Position after, final float percent)
    {
        object.position(Position.computeInterpolation(before, after, percent));
    }

    /**
     * Set object's position <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param object Object to place
     * @param value  New position
     * @see AnimationKeyFrame#setValue(Object, Object)
     */
    @Override
    public void setValue(final P object, final Position value)
    {
        object.position(value);
    }
}
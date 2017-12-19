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
import jhelp.engine2.render.ThreadAnimation;
import jhelp.engine2.twoD.Object2D;
import jhelp.engine2.util.PositionObject2D;

/**
 * Animation witch move a 2D object
 *
 * @author JHelp
 */
public class AnimationPositionObject2D
        extends AnimationKeyFrame<Object2D, PositionObject2D>
{

    /**
     * Constructs AnimationPositionObject2D
     *
     * @param object Object to move
     */
    public AnimationPositionObject2D(final @NotNull Object2D object)
    {
        super(object);
    }

    /**
     * Interpolate a value
     *
     * @param object  Object to move
     * @param before  Position just before the computed position
     * @param after   Position just after the computed position
     * @param percent Percent of interpolation
     * @see AnimationKeyFrame#interpolateValue(Object, Object, Object, float)
     */
    @Override
    @ThreadAnimation
    protected void interpolateValue(
            final @NotNull Object2D object,
            final @NotNull PositionObject2D before, final @NotNull PositionObject2D after,
            final float percent)
    {
        final float anti = 1f - percent;

        object.x((int) ((before.x * anti) + (after.x * percent)));
        object.y((int) ((before.y * anti) + (after.y * percent)));
        object.width((int) ((before.width * anti) + (after.width * percent)));
        object.height((int) ((before.height * anti) + (after.height * percent)));
    }

    /**
     * Compute object position
     *
     * @param object Object to get it's position
     * @return Object's position
     * @see AnimationKeyFrame#obtainValue(Object)
     */
    @Override
    @ThreadAnimation
    protected @NotNull PositionObject2D obtainValue(final @NotNull Object2D object)
    {
        return new PositionObject2D(object);
    }

    /**
     * Change object position
     *
     * @param object Object to change
     * @param value  New value
     * @see AnimationKeyFrame#setValue(Object, Object)
     */
    @Override
    @ThreadAnimation
    protected void setValue(final @NotNull Object2D object, final @NotNull PositionObject2D value)
    {
        object.x(value.x);
        object.y(value.y);
        object.width(value.width);
        object.height(value.height);
    }
}
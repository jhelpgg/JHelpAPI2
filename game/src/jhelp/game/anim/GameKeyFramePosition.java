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

import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.dynamic.Position;
import jhelp.util.gui.dynamic.Positionable;

/**
 * Animation key frame that animate position of something
 */
public final class GameKeyFramePosition<P extends Positionable> extends GameKeyFrameAnimation<P, Position>
{
    /**
     * Create the animation key frame
     *
     * @param object Object to change position
     */
    public GameKeyFramePosition(final P object)
    {
        super(object);
    }

    /**
     * Apply a value to given object
     *
     * @param value  Value to give
     * @param object Object where apply the value
     * @param parent Image parent locked in draw mode to draw something if need
     */
    @Override
    protected void apply(final Position value, final P object, final JHelpImage parent)
    {
        object.position(value);
    }

    /**
     * Compute interpolation value between two values
     *
     * @param value1  Start value
     * @param value2  End value
     * @param percent Percent progress to go from first to second. In [0, 1]
     * @return Interpolated value
     */
    @Override
    protected Position interpolate(final Position value1, final Position value2, final float percent)
    {
        final float anti = 1f - percent;
        final int   x    = Math.round(value1.getX() * anti + value2.getX() * percent);
        final int   y    = Math.round(value1.getY() * anti + value2.getY() * percent);
        return new Position(x, y);
    }

    /**
     * compute current value for given object
     *
     * @param object Object to get current value
     * @return Current value
     */
    @Override
    protected Position obtainValue(final P object)
    {
        return object.position();
    }
}

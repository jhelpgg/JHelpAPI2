package jhelp.game.anim;

import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.dynamic.Position;
import jhelp.util.gui.dynamic.Positionable;

/**
 * Created by jhelp on 14/07/17.
 */
public final class GameKeyFramePosition<P extends Positionable> extends GameKeyFrameAnimation<P, Position>
{
    public GameKeyFramePosition(final P object)
    {
        super(object);
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
     * Apply a value to given object
     *
     * @param value  Value to give
     * @param object Object where apply the value
     * @param parent Image parent lovked in draw mode to draw something if need
     */
    @Override
    protected void apply(final Position value, final P object, final JHelpImage parent)
    {
        object.position(value);
    }
}

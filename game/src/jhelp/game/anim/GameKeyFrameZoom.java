package jhelp.game.anim;

import jhelp.game.data.Zoom;
import jhelp.game.data.Zoomable;
import jhelp.util.gui.JHelpImage;

/**
 * Created by jhelp on 14/07/17.
 */
public final class GameKeyFrameZoom<Z extends Zoomable> extends GameKeyFrameAnimation<Z, Zoom>
{
    public GameKeyFrameZoom(final Z object)
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
    protected Zoom obtainValue(final Z object)
    {
        return object.zoom();
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
    protected Zoom interpolate(final Zoom value1, final Zoom value2, final float percent)
    {
        final float anti   = 1f - percent;
        final float width  = value1.zoomWidth() * anti + value2.zoomWidth() * percent;
        final float height = value1.zoomHeight() * anti + value2.zoomHeight() * percent;
        return new Zoom(width, height);
    }

    /**
     * Apply a value to given object
     *
     * @param value  Value to give
     * @param object Object where apply the value
     * @param parent Image parent lovked in draw mode to draw something if need
     */
    @Override
    protected void apply(final Zoom value, final Z object, final JHelpImage parent)
    {
        object.zoom(value);
    }
}

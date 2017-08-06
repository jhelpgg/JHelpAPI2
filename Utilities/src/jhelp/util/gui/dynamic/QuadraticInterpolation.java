package jhelp.util.gui.dynamic;

import jhelp.util.math.Math2;

/**
 * Created by jhelp on 14/07/17.
 */
public class QuadraticInterpolation implements Interpolation
{
    private final float control;

    public QuadraticInterpolation()
    {
        this(0.5f);
    }

    public QuadraticInterpolation(final float control)
    {
        this.control = control;
    }

    /**
     * Interpolate a [0, 1] value.<br>
     * The function <b>f(x)</b> MUST meet :
     * <ul>
     * <li>[0, 1] -f-> [0, 1]</li>
     * <li>f(0) = 0</li>
     * <li>f(1) = 1</li>
     * </ul>
     * <br>
     * We recommend that f is continues, else movement make some disappear/appear effects.<br>
     * We also recommends that function is increase, else transition shows some "go back" effect.
     *
     * @param percent Value (in [0, 1]) to interpolate
     * @return Interpolation result (in [0, 1])
     */
    @Override
    public float interpolation(final float percent)
    {
        return (float) Math2.quadratic(0, this.control, 1, percent);
    }
}
package jhelp.util.gui.dynamic;

import jhelp.util.math.Math2;

/**
 * Created by jhelp on 14/07/17.
 */
public final class AccelerationInterpolation implements Interpolation
{
    private final float factor;

    public AccelerationInterpolation()
    {
        this(1);
    }

    public AccelerationInterpolation(final float factor)
    {
        if (Math2.sign(factor) <= 0)
        {
            throw new IllegalArgumentException("factor MUST be >=0 not " + factor);
        }

        this.factor = 2f * factor;
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
        return (float) Math.pow(percent, this.factor);
    }
}

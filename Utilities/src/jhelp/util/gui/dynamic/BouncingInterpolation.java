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

package jhelp.util.gui.dynamic;

import jhelp.util.math.Math2;

/**
 * Created by jhelp on 14/07/17.
 */
public final class BouncingInterpolation implements Interpolation
{
    private final int numberBounce;

    public BouncingInterpolation()
    {
        this(1);
    }

    public BouncingInterpolation(final int numberBounce)
    {
        if (numberBounce < 0)
        {
            throw new IllegalArgumentException("numberBounce MUST be >=0, not " + numberBounce);
        }

        this.numberBounce = numberBounce;
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
    public float interpolation(float percent)
    {
        if (this.numberBounce == 0)
        {
            return Math2.square(percent);
        }

        float amplitude = 1f / (this.numberBounce + 1);

        if (Math2.compare(percent, amplitude) < 0)
        {
            return Math2.square(percent / amplitude);
        }

        float free    = 1f - amplitude * 0.56789f;
        float minimum = 0.56789f;
        percent -= amplitude;
        int left = this.numberBounce - 1;

        while (Math2.compare(percent, amplitude) >= 0 && !Math2.isNul(amplitude) && !Math2.isNul(minimum) &&
               !Math2.isNul(percent) && left > 0)
        {
            minimum *= 0.56789f;
            percent -= amplitude;
            free -= amplitude;
            amplitude = free * 0.56789f;
            left--;
        }

        if (left == 0)
        {
            amplitude = free / 2f;
        }

        float squareRoot = (float) Math.sqrt(minimum);
        percent = (percent - amplitude / 2f) * (squareRoot * 2f / amplitude);
        return Math.min(Math2.square(percent) + 1 - minimum, 1f);
    }
}

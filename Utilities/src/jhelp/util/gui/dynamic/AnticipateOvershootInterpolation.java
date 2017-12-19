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
public class AnticipateOvershootInterpolation implements Interpolation
{
    private final float tension;

    public AnticipateOvershootInterpolation()
    {
        this(1);
    }

    public AnticipateOvershootInterpolation(final float tension)
    {
        if (Math2.sign(tension) <= 0)
        {
            throw new IllegalArgumentException("tension MUST be >=0 not " + tension);
        }

        this.tension = tension;
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
    @Override public float interpolation(final float percent)
    {
        if (Math2.compare(percent, 0.5f) < 0)
        {
            final float value = 2f * percent;
            return 0.5f * ((this.tension + 1f) * value * value * value - this.tension * value * value);
        }

        final float value = 2f * percent - 2f;
        return 0.5f * ((this.tension + 1f) * value * value * value + this.tension * value * value) + 1f;
    }
}

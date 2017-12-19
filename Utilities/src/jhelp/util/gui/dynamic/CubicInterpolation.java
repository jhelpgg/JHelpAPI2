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
public class CubicInterpolation implements Interpolation
{
    private final float firstControl;
    private final float secondControl;

    public CubicInterpolation()
    {
        this(0.25f, 0.75f);
    }

    public CubicInterpolation(final float firstControl, final float secondControl)
    {
        this.firstControl = firstControl;
        this.secondControl = secondControl;
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
        return (float) Math2.cubic(0, this.firstControl, this.secondControl, 1, percent);
    }
}

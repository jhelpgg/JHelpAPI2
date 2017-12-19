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
 * Some interpolations
 */
public enum Interpolations implements Interpolation
{
    /**
     * Linear or identity interpolation
     */
    LINEAR
            {
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
                    return percent;
                }
            },
    /**
     * Sinusoidal interpolation
     */
    SINUS
            {
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
                    return (float) Math2.interpolationSinus(percent);
                }
            },
    /**
     * Cosinusoidal interpolation
     */
    COSINUS
            {
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
                    return (float) Math2.interpolationCosinus(percent);
                }
            },
    /**
     * Exponential interpolation
     */
    EXPONENTIAL
            {
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
                    return (float) Math2.interpolationExponential(percent);
                }
            },
    /**
     * Logarithm interpolation
     */
    LOGARITHM
            {
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
                    return (float) Math2.interpolationLogarithm(percent);
                }
            },
    /**
     * Square interpolation
     */
    SQUARE
            {
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
                    return Math2.square(percent);
                }
            },
    /**
     * Square root interpolation
     */
    SQUARE_ROOT
            {
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
                    return (float) Math.sqrt(percent);
                }
            },
    /**
     * Bounce interpolation
     */
    BOUNCE
            {
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
                    if (Math2.compare(percent, 0.31489f) < 0)
                    {
                        return 8f * Math2.square(1.1226f * percent);
                    }

                    if (Math2.compare(percent, 0.65990) < 0)
                    {
                        return 8f * Math2.square(1.1226f * percent - 0.54719f) + 0.7f;
                    }

                    if (Math2.compare(percent, 0.85908f) < 0)
                    {
                        return 8f * Math2.square(1.1226f * percent - 0.8526f) + 0.9f;
                    }

                    return 8f * Math2.square(1.1226f * percent - 1.0435f) + 0.95f;
                }
            },
    /**
     * Hesitate interpolation
     */
    HESITATE
            {
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
                    final float value = 2f * percent - 1f;
                    return 0.5f * (value * value * value + 1);
                }
            }
}

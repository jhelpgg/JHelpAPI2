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

package jhelp.gui.code;

import com.sun.istack.internal.NotNull;
import jhelp.util.math.rational.Rational;

/**
 * Text relative size
 */
public enum TextSize
{
    /**
     * Small text
     */
    SMALL(Rational.createRational(3, 4)),
    /**
     * Normal text
     */
    NORMAL(Rational.ONE),
    /**
     * Big text
     */
    BIG(Rational.createRational(5, 4));

    /**
     * Factor to apply
     */
    private final Rational factor;

    /**
     * Create text relative size
     *
     * @param factor Factor to apply
     */
    TextSize(@NotNull Rational factor)
    {
        this.factor = factor;
    }

    /**
     * Compute size
     *
     * @param size Normal text size
     * @return Computed size
     */
    public int computeSize(int size)
    {
        return (size * this.factor.getNumerator()) / this.factor.getDenominator();
    }
}

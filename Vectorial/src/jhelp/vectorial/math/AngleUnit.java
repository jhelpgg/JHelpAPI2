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

/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.vectorial.math;

import jhelp.util.math.Math2;

/**
 * Angle unit : degree, radian OR grade
 *
 * @author JHelp
 */
public enum AngleUnit
{
    /**
     * Degree angle
     */
    DEGREE("deg")
            {
                /**
                 * Modularize given angle in good range, for degree : [0, 360] <br>
                 * <br>
                 * <b>Parent documentation:</b><br>
                 * {@inheritDoc}
                 *
                 * @param angle
                 *           Angle to modularize
                 * @return Modularized angle
                 * @see AngleUnit#modularize(double)
                 */
                @Override
                public double modularize(final double angle)
                {
                    return Math2.modulo(angle, 360.0);
                }
            },
    /**
     * Grade angle
     */
    GRADE("grad")
            {
                /**
                 * Modularize given angle in good range, for grade : [0, 400] <br>
                 * <br>
                 * <b>Parent documentation:</b><br>
                 * {@inheritDoc}
                 *
                 * @param angle
                 *           Angle to modularize
                 * @return Modularized angle
                 * @see AngleUnit#modularize(double)
                 */
                @Override
                public double modularize(final double angle)
                {
                    return Math2.modulo(angle, 400.0);
                }
            },
    /**
     * Radian angle
     */
    RADIAN("rad")
            {
                /**
                 * Modularize given angle in good range, for radian : [0, 2&pi;] <br>
                 * <br>
                 * <b>Parent documentation:</b><br>
                 * {@inheritDoc}
                 *
                 * @param angle
                 *           Angle to modularize
                 * @return Modularized angle
                 * @see AngleUnit#modularize(double)
                 */
                @Override
                public double modularize(final double angle)
                {
                    return Math2.modulo(angle, Math2.TWO_PI);
                }
            };

    /**
     * Obtain angle unit by its short name : {"deg", "rad", "grad"}
     *
     * @param unitName Unit short name
     * @return Angle unit OR {@code null} if short name not known
     */
    public static AngleUnit obtainAngleUnitByName(final String unitName)
    {
        for (final AngleUnit angleUnit : AngleUnit.values())
        {
            if (angleUnit.unitName.equalsIgnoreCase(unitName) == true)
            {
                return angleUnit;
            }
        }

        return null;
    }

    /**
     * Unit short name
     */
    private final String unitName;

    /**
     * Create a new instance of AngleUnit
     *
     * @param unitName Unit short name
     */
    AngleUnit(final String unitName)
    {
        this.unitName = unitName;
    }

    /**
     * Unit short name
     *
     * @return Unit short name
     */
    public String getUnitName()
    {
        return this.unitName;
    }

    /**
     * Modularize given angle in good range
     *
     * @param angle Angle to modularize
     * @return Modularized angle
     * @throws RuntimeException if forget to override this method
     */
    public double modularize(final double angle)
    {
        throw new RuntimeException("MUST override this method for unit : " + this);
    }
}
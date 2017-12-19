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
package jhelp.vectorial.path;

import jhelp.util.io.json.ObjectJSON;
import jhelp.util.io.json.ValueJSON;
import jhelp.util.math.Math2;
import jhelp.util.util.HashCode;
import jhelp.vectorial.VectorialConstants;
import jhelp.vectorial.math.Point;

/**
 * Path element ellipse arc to <br>
 * For understand the role of <b>largeArc</b> and <b>sweep</b> launch the sample :MainLargeArcSweepExemple
 *
 * @author JHelp
 */
public class EllipticalArcTo
        extends PathElement
{
    /**
     * Large arc
     */
    private boolean largeArc;
    /**
     * Radius X
     */
    private double  radiusX;
    /**
     * Radius Y
     */
    private double  radiusY;
    /**
     * Rotation around X axis in degree
     */
    private double  rotationAxisXInDegree;
    /**
     * Sweep side
     */
    private boolean sweep;
    /**
     * Final point X
     */
    private double  x;
    /**
     * Final point Y
     */
    private double  y;

    /**
     * Create a new instance of EllipticalArcTo <br>
     * For understand the role of <b>largeArc</b> and <b>sweep</b> launch the sample : MainLargeArcSweepExemple
     *
     * @param relative              Indicates if coordinates are relative to current point ({@code true}) OR coordinates are absolute ({@code false}
     *                              )
     * @param radiusX               Radius X
     * @param radiusY               Radius Y
     * @param rotationAxisXInDegree Rotation around X axis in degree
     * @param largeArc              Large arc
     * @param sweep                 Sweep side
     * @param x                     Final point X
     * @param y                     Final point Y
     */
    public EllipticalArcTo(
            final boolean relative, final double radiusX, final double radiusY, final double rotationAxisXInDegree,
            final boolean largeArc,
            final boolean sweep, final double x, final double y)
    {
        super(relative);
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        this.rotationAxisXInDegree = rotationAxisXInDegree;
        this.largeArc = largeArc;
        this.sweep = sweep;
        this.x = x;
        this.y = y;
    }

    /**
     * Create a new instance of EllipticalArcTo
     *
     * @param relative Indicates if coordinates are relative to current point ({@code true}) OR coordinates are absolute ({@code false}
     *                 )
     * @param json     JSON to parse
     * @throws ParsePathException If JSON not valid
     */
    public EllipticalArcTo(final boolean relative, final ObjectJSON json)
            throws ParsePathException
    {
        super(relative);

        try
        {
            this.largeArc = json.get(VectorialConstants.PARAMETER_LARGE_ARC).getBoolean();
            this.radiusX = json.get(VectorialConstants.PARAMETER_RADIUS_X).getNumber();
            this.radiusY = json.get(VectorialConstants.PARAMETER_RADIUS_Y).getNumber();
            this.rotationAxisXInDegree = json.get(VectorialConstants.PARAMETER_ROTATION_AXIS_X_IN_DEGREE).getNumber();
            this.sweep = json.get(VectorialConstants.PARAMETER_SWEEP).getBoolean();
            this.x = json.get(VectorialConstants.PARAMETER_X).getNumber();
            this.y = json.get(VectorialConstants.PARAMETER_Y).getNumber();
        }
        catch (final Exception exception)
        {
            throw new ParsePathException(exception, "Failed to parse as EllipticalArcTo : ", json);
        }
    }

    /**
     * Indicates if path element equals to this element <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param pathElement Path element to compare
     * @return {@code true} if path element equals to this element
     * @see PathElement#equalsIntern(PathElement)
     */
    @Override
    protected boolean equalsIntern(final PathElement pathElement)
    {
        final EllipticalArcTo ellipticalArcTo = (EllipticalArcTo) pathElement;
        return (Math2.equals(this.radiusX, ellipticalArcTo.radiusX) == true)//
               && (Math2.equals(this.radiusY, ellipticalArcTo.radiusY) == true)//
               && (Math2.equals(this.rotationAxisXInDegree, ellipticalArcTo.rotationAxisXInDegree) == true)//
               && (this.largeArc == ellipticalArcTo.largeArc)//
               && (this.sweep == ellipticalArcTo.sweep)//
               && (Math2.equals(this.x, ellipticalArcTo.x) == true)//
               && (Math2.equals(this.y, ellipticalArcTo.y) == true)//
                ;
    }

    /**
     * Fill JSON with specific parameter value <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param json JSON to fill
     * @see PathElement#fillJSON(jhelp.util.io.json.ObjectJSON)
     */
    @Override
    protected void fillJSON(final ObjectJSON json)
    {
        json.put(VectorialConstants.PARAMETER_LARGE_ARC, ValueJSON.newValue(this.largeArc));
        json.put(VectorialConstants.PARAMETER_RADIUS_X, ValueJSON.newValue(this.radiusX));
        json.put(VectorialConstants.PARAMETER_RADIUS_Y, ValueJSON.newValue(this.radiusY));
        json.put(VectorialConstants.PARAMETER_ROTATION_AXIS_X_IN_DEGREE,
                 ValueJSON.newValue(this.rotationAxisXInDegree));
        json.put(VectorialConstants.PARAMETER_SWEEP, ValueJSON.newValue(this.sweep));
        json.put(VectorialConstants.PARAMETER_X, ValueJSON.newValue(this.x));
        json.put(VectorialConstants.PARAMETER_Y, ValueJSON.newValue(this.y));
    }

    /**
     * Complete hash code <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param hashCode Hash code to complete
     * @see PathElement#hashCodeIntern(jhelp.util.util.HashCode)
     */
    @Override
    protected void hashCodeIntern(final HashCode hashCode)
    {
        hashCode.add(this.radiusX);
        hashCode.add(this.radiusY);
        hashCode.add(this.rotationAxisXInDegree);
        hashCode.add(this.largeArc);
        hashCode.add(this.sweep);
        hashCode.add(this.x);
        hashCode.add(this.y);
    }

    /**
     * Draw path <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param pathDrawer Path drawer
     * @param referenceX Start X
     * @param referenceY Start Y
     * @return Final point
     * @see PathElement#draw(PathDrawer, double, double)
     */
    @Override
    public Point draw(final PathDrawer pathDrawer, final double referenceX, final double referenceY)
    {
        double x = this.x;
        double y = this.y;

        if (this.isRelative() == true)
        {
            x += referenceX;
            y += referenceY;
        }

        pathDrawer.ellipticalArcTo(this.radiusX, this.radiusY, this.rotationAxisXInDegree, this.largeArc, this.sweep, x,
                                   y);

        return new Point(x, y);
    }

    /**
     * Radius X
     *
     * @return Radius X
     */
    public double getRadiusX()
    {
        return this.radiusX;
    }

    /**
     * Change radius X
     *
     * @param radiusX New radius X
     */
    public void setRadiusX(final double radiusX)
    {
        if (Math2.equals(this.radiusX, radiusX) == true)
        {
            return;
        }

        this.radiusX = radiusX;
        this.firePathElementChanged();
    }

    /**
     * Radius Y
     *
     * @return Radius Y
     */
    public double getRadiusY()
    {
        return this.radiusY;
    }

    /**
     * Change radius Y
     *
     * @param radiusY New radius Y
     */
    public void setRadiusY(final double radiusY)
    {
        if (Math2.equals(this.radiusY, radiusY) == true)
        {
            return;
        }

        this.radiusY = radiusY;
        this.firePathElementChanged();
    }

    /**
     * Rotation around axis X in degree
     *
     * @return Rotation around axis X in degree
     */
    public double getRotationAxisXInDegree()
    {
        return this.rotationAxisXInDegree;
    }

    /**
     * Change rotation around X Axis
     *
     * @param rotationAxisXInDegree New rotation around X Axis
     */
    public void setRotationAxisXInDegree(final double rotationAxisXInDegree)
    {
        if (Math2.equals(this.rotationAxisXInDegree, rotationAxisXInDegree) == true)
        {
            return;
        }

        this.rotationAxisXInDegree = rotationAxisXInDegree;
        this.firePathElementChanged();
    }

    /**
     * Final point X
     *
     * @return Final point X
     */
    public double getX()
    {
        return this.x;
    }

    /**
     * Change final point X
     *
     * @param x New final point X
     */
    public void setX(final double x)
    {
        if (Math2.equals(this.x, x) == true)
        {
            return;
        }

        this.x = x;
        this.firePathElementChanged();
    }

    /**
     * Final point Y
     *
     * @return Final point Y
     */
    public double getY()
    {
        return this.y;
    }

    /**
     * Change final point Y
     *
     * @param y New final point Y
     */
    public void setY(final double y)
    {
        if (Math2.equals(this.y, y) == true)
        {
            return;
        }

        this.y = y;
        this.firePathElementChanged();
    }

    /**
     * Indicates if use the large arc<br>
     * For understand the role of <b>largeArc</b> and <b>sweep</b> launch the sample : MainLargeArcSweepExemple
     *
     * @return {@code true} if use the large arc
     */
    public boolean isLargeArc()
    {
        return this.largeArc;
    }

    /**
     * change large arc
     *
     * @param largeArc New large arc
     */
    public void setLargeArc(final boolean largeArc)
    {
        if (this.largeArc == largeArc)
        {
            return;
        }

        this.largeArc = largeArc;
        this.firePathElementChanged();
    }

    /**
     * Indicates if use the sweep side<br>
     * For understand the role of <b>largeArc</b> and <b>sweep</b> launch the sample : MainLargeArcSweepExemple
     *
     * @return {@code true} if use the sweep side
     */
    public boolean isSweep()
    {
        return this.sweep;
    }

    /**
     * Change sweep side
     *
     * @param sweep New sweep side
     */
    public void setSweep(final boolean sweep)
    {
        if (this.sweep == sweep)
        {
            return;
        }

        this.sweep = sweep;
        this.firePathElementChanged();
    }
}
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
 * Cubic path element
 *
 * @author JHelp
 */
public class CubicBezierTo
        extends PathElement
{
    /**
     * First control point X
     */
    private double firstControlPointX;
    /**
     * First control point Y
     */
    private double firstControlPointY;
    /**
     * Second control point X
     */
    private double secondControlPointX;
    /**
     * Second control point Y
     */
    private double secondControlPointY;
    /**
     * Final point X
     */
    private double x;
    /**
     * Final point Y
     */
    private double y;

    /**
     * Create a new instance of CubicBezierTo
     *
     * @param relative            Indicates if coordinates are relative to current point ({@code true}) OR coordinates are absolute ({@code false}
     *                            )
     * @param firstControlPointX  First control point X
     * @param firstControlPointY  First control point Y
     * @param secondControlPointX Second control point X
     * @param secondControlPointY Second control point Y
     * @param x                   Final point X
     * @param y                   Final point Y
     */
    public CubicBezierTo(
            final boolean relative, final double firstControlPointX, final double firstControlPointY,
            final double secondControlPointX,
            final double secondControlPointY, final double x, final double y)
    {
        super(relative);
        this.firstControlPointX = firstControlPointX;
        this.firstControlPointY = firstControlPointY;
        this.secondControlPointX = secondControlPointX;
        this.secondControlPointY = secondControlPointY;
        this.x = x;
        this.y = y;
    }

    /**
     * Create a new instance of CubicBezierTo
     *
     * @param relative Indicates if coordinates are relative to current point ({@code true}) OR coordinates are absolute ({@code false}
     *                 )
     * @param json     JSON to parse
     * @throws ParsePathException If JSON not valid
     */
    public CubicBezierTo(final boolean relative, final ObjectJSON json)
            throws ParsePathException
    {
        super(relative);

        try
        {
            this.firstControlPointX = json.get(VectorialConstants.PARAMETER_FIRST_CONTROL_POINT_X).getNumber();
            this.firstControlPointY = json.get(VectorialConstants.PARAMETER_FIRST_CONTROL_POINT_Y).getNumber();
            this.secondControlPointX = json.get(VectorialConstants.PARAMETER_SECOND_CONTROL_POINT_X).getNumber();
            this.secondControlPointY = json.get(VectorialConstants.PARAMETER_SECOND_CONTROL_POINT_Y).getNumber();
            this.x = json.get(VectorialConstants.PARAMETER_X).getNumber();
            this.y = json.get(VectorialConstants.PARAMETER_Y).getNumber();
        }
        catch (final Exception exception)
        {
            throw new ParsePathException(exception, "Failed to parse as CubicBezierTo : ", json);
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
        final CubicBezierTo cubicBezierTo = (CubicBezierTo) pathElement;
        return (Math2.equals(this.firstControlPointX, cubicBezierTo.firstControlPointX) == true)//
               && (Math2.equals(this.firstControlPointY, cubicBezierTo.firstControlPointY) == true)//
               && (Math2.equals(this.secondControlPointX, cubicBezierTo.secondControlPointX) == true) //
               && (Math2.equals(this.secondControlPointY, cubicBezierTo.secondControlPointY) == true)//
               && (Math2.equals(this.x, cubicBezierTo.x) == true) //
               && (Math2.equals(this.y, cubicBezierTo.y) == true) //
                ;
    }

    /**
     * Fill JSON with specific parameters <br>
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
        json.put(VectorialConstants.PARAMETER_FIRST_CONTROL_POINT_X, ValueJSON.newValue(this.firstControlPointX));
        json.put(VectorialConstants.PARAMETER_FIRST_CONTROL_POINT_Y, ValueJSON.newValue(this.firstControlPointY));
        json.put(VectorialConstants.PARAMETER_SECOND_CONTROL_POINT_X, ValueJSON.newValue(this.secondControlPointX));
        json.put(VectorialConstants.PARAMETER_SECOND_CONTROL_POINT_Y, ValueJSON.newValue(this.secondControlPointY));
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
        hashCode.add(this.firstControlPointX);
        hashCode.add(this.firstControlPointY);
        hashCode.add(this.secondControlPointX);
        hashCode.add(this.secondControlPointY);
        hashCode.add(this.x);
        hashCode.add(this.y);
    }

    /**
     * Draw path element <br>
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
        double firstControlPointX  = this.firstControlPointX;
        double firstControlPointY  = this.firstControlPointY;
        double secondControlPointX = this.secondControlPointX;
        double secondControlPointY = this.secondControlPointY;
        double x                   = this.x;
        double y                   = this.y;

        if (this.isRelative() == true)
        {
            firstControlPointX += referenceX;
            firstControlPointY += referenceY;
            secondControlPointX += referenceX;
            secondControlPointY += referenceY;
            x += referenceX;
            y += referenceY;
        }

        pathDrawer.cubicBezierTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY, x,
                                 y);

        return new Point(x, y);
    }

    /**
     * First control point X
     *
     * @return First control point X
     */
    public double getFirstControlPointX()
    {
        return this.firstControlPointX;
    }

    /**
     * Change first control point X
     *
     * @param firstControlPointX New first control point X
     */
    public void setFirstControlPointX(final double firstControlPointX)
    {
        if (Math2.equals(this.firstControlPointX, firstControlPointX) == true)
        {
            return;
        }

        this.firstControlPointX = firstControlPointX;
        this.firePathElementChanged();
    }

    /**
     * First control point Y
     *
     * @return First control point Y
     */
    public double getFirstControlPointY()
    {
        return this.firstControlPointY;
    }

    /**
     * Change first control point Y
     *
     * @param firstControlPointY New first control point Y
     */
    public void setFirstControlPointY(final double firstControlPointY)
    {
        if (Math2.equals(this.firstControlPointY, firstControlPointY) == true)
        {
            return;
        }

        this.firstControlPointY = firstControlPointY;
        this.firePathElementChanged();
    }

    /**
     * Second control point X
     *
     * @return Second control point X
     */
    public double getSecondControlPointX()
    {
        return this.secondControlPointX;
    }

    /**
     * Change second control point X
     *
     * @param secondControlPointX New second control point X
     */
    public void setSecondControlPointX(final double secondControlPointX)
    {
        if (Math2.equals(this.secondControlPointX, secondControlPointX) == true)
        {
            return;
        }

        this.secondControlPointX = secondControlPointX;
        this.firePathElementChanged();
    }

    /**
     * Second control point Y
     *
     * @return Second control point Y
     */
    public double getSecondControlPointY()
    {
        return this.secondControlPointY;
    }

    /**
     * Change second control point Y
     *
     * @param secondControlPointY New second control point Y
     */
    public void setSecondControlPointY(final double secondControlPointY)
    {
        if (Math2.equals(this.secondControlPointY, secondControlPointY) == true)
        {
            return;
        }

        this.secondControlPointY = secondControlPointY;
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
}
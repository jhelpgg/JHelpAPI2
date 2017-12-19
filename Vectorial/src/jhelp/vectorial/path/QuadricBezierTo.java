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
 * Quadric element
 *
 * @author JHelp
 */
public class QuadricBezierTo
        extends PathElement
{
    /**
     * Control point X
     */
    private double controlPointX;
    /**
     * Control point Y
     */
    private double controlPointY;
    /**
     * Final point X
     */
    private double x;
    /**
     * Final point Y
     */
    private double y;

    /**
     * Create a new instance of QuadricBezierTo
     *
     * @param relative      Indicates if coordinates are relative to current point ({@code true}) OR coordinates are absolute ({@code false}
     *                      )
     * @param controlPointX Control point X
     * @param controlPointY Control point Y
     * @param x             Final point X
     * @param y             Final point Y
     */
    public QuadricBezierTo(
            final boolean relative, final double controlPointX, final double controlPointY, final double x,
            final double y)
    {
        super(relative);
        this.controlPointX = controlPointX;
        this.controlPointY = controlPointY;
        this.x = x;
        this.y = y;
    }

    /**
     * Create a new instance of QuadricBezierTo
     *
     * @param relative Indicates if coordinates are relative to current point ({@code true}) OR coordinates are absolute ({@code false}
     *                 )
     * @param json     JSON to parse
     * @throws ParsePathException If JSON not valid
     */
    public QuadricBezierTo(final boolean relative, final ObjectJSON json)
            throws ParsePathException
    {
        super(relative);

        try
        {
            this.controlPointX = json.get(VectorialConstants.PARAMETER_CONTROL_POINT_X).getNumber();
            this.controlPointY = json.get(VectorialConstants.PARAMETER_CONTROL_POINT_Y).getNumber();
            this.x = json.get(VectorialConstants.PARAMETER_X).getNumber();
            this.y = json.get(VectorialConstants.PARAMETER_Y).getNumber();
        }
        catch (final Exception exception)
        {
            throw new ParsePathException(exception, "Failed to parse as QuadricBezierTo : ", json);
        }
    }

    /**
     * Indicate if given path equals to this path <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param pathElement Path element in good type
     * @return {@code true} if given path equals to this path
     * @see PathElement#equalsIntern(PathElement)
     */
    @Override
    protected boolean equalsIntern(final PathElement pathElement)
    {
        final QuadricBezierTo quadricBezierTo = (QuadricBezierTo) pathElement;
        return (Math2.equals(this.controlPointX, quadricBezierTo.controlPointX) == true)//
               && (Math2.equals(this.controlPointY, quadricBezierTo.controlPointY) == true)//
               && (Math2.equals(this.x, quadricBezierTo.x) == true) //
               && (Math2.equals(this.y, quadricBezierTo.y) == true) //
                ;
    }

    /**
     * JSON to fill with quadric parameters <br>
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
        json.put(VectorialConstants.PARAMETER_CONTROL_POINT_X, ValueJSON.newValue(this.controlPointX));
        json.put(VectorialConstants.PARAMETER_CONTROL_POINT_Y, ValueJSON.newValue(this.controlPointY));
        json.put(VectorialConstants.PARAMETER_X, ValueJSON.newValue(this.x));
        json.put(VectorialConstants.PARAMETER_Y, ValueJSON.newValue(this.y));
    }

    /**
     * Add hash code specific information <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param hashCode Hash code to fill
     * @see PathElement#hashCodeIntern(jhelp.util.util.HashCode)
     */
    @Override
    protected void hashCodeIntern(final HashCode hashCode)
    {
        hashCode.add(this.controlPointX);
        hashCode.add(this.controlPointY);
        hashCode.add(this.x);
        hashCode.add(this.y);
    }

    /**
     * Draw the element <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param pathDrawer Path drawer
     * @param referenceX Start point X
     * @param referenceY Start point Y
     * @return Final point
     * @see PathElement#draw(PathDrawer, double, double)
     */
    @Override
    public Point draw(final PathDrawer pathDrawer, final double referenceX, final double referenceY)
    {
        double controlPointX = this.controlPointX;
        double controlPointY = this.controlPointY;
        double x             = this.x;
        double y             = this.y;

        if (this.isRelative() == true)
        {
            controlPointX += referenceX;
            controlPointY += referenceY;
            x += referenceX;
            y += referenceY;
        }

        pathDrawer.quadricBezierTo(controlPointX, controlPointY, x, y);

        return new Point(x, y);
    }

    /**
     * Control point X
     *
     * @return Control point X
     */
    public double getControlPointX()
    {
        return this.controlPointX;
    }

    /**
     * New control point X
     *
     * @param controlPointX New control point X
     */
    public void setControlPointX(final double controlPointX)
    {
        if (Math2.equals(this.controlPointX, controlPointX) == true)
        {
            return;
        }

        this.controlPointX = controlPointX;
        this.firePathElementChanged();
    }

    /**
     * Control point Y
     *
     * @return Control point Y
     */
    public double getControlPointY()
    {
        return this.controlPointY;
    }

    /**
     * New control point Y
     *
     * @param controlPointY New control point Y
     */
    public void setControlPointY(final double controlPointY)
    {
        if (Math2.equals(this.controlPointY, controlPointY) == true)
        {
            return;
        }

        this.controlPointY = controlPointY;
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
     * New final point X
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
     * New final point Y
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
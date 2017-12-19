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
 * Move cursor
 *
 * @author JHelp
 */
public class MoveTo
        extends PathElement
{
    /**
     * Position X
     */
    private double x;
    /**
     * Position Y
     */
    private double y;

    /**
     * Create a new instance of MoveTo
     *
     * @param relative Indicates if coordinates are relative to current point ({@code true}) OR coordinates are absolute ({@code false}
     *                 )
     * @param x        Position X
     * @param y        Position Y
     */
    public MoveTo(final boolean relative, final double x, final double y)
    {
        super(relative);
        this.x = x;
        this.y = y;
    }

    /**
     * Create a new instance of MoveTo
     *
     * @param relative Indicates if coordinates are relative to current point ({@code true}) OR coordinates are absolute ({@code false}
     *                 )
     * @param json     JSON to parse
     * @throws ParsePathException If JSON not valid
     */
    public MoveTo(final boolean relative, final ObjectJSON json)
            throws ParsePathException
    {
        super(relative);

        try
        {
            this.x = json.get(VectorialConstants.PARAMETER_X).getNumber();
            this.y = json.get(VectorialConstants.PARAMETER_Y).getNumber();
        }
        catch (final Exception exception)
        {
            throw new ParsePathException(exception, "Failed to parse as MoveTo : ", json);
        }
    }

    /**
     * Indicates if path element equals to this element <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param pathElement Path element in good type
     * @return {@code true} if path element equals to this element
     * @see jhelp.vectorial.path.PathElement#equalsIntern(jhelp.vectorial.path.PathElement)
     */
    @Override
    protected boolean equalsIntern(final PathElement pathElement)
    {
        final MoveTo moveTo = (MoveTo) pathElement;
        return (Math2.equals(this.x, moveTo.x) == true) && (Math2.equals(this.y, moveTo.y) == true);
    }

    /**
     * Fill JSON with specific parameters <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param json JSON to fill
     * @see jhelp.vectorial.path.PathElement#fillJSON(jhelp.util.io.json.ObjectJSON)
     */
    @Override
    protected void fillJSON(final ObjectJSON json)
    {
        json.put(VectorialConstants.PARAMETER_X, ValueJSON.newValue(this.x));
        json.put(VectorialConstants.PARAMETER_Y, ValueJSON.newValue(this.y));
    }

    /**
     * Compute additional hash code part <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param hashCode Hash code
     * @see jhelp.vectorial.path.PathElement#hashCodeIntern(jhelp.util.util.HashCode)
     */
    @Override
    protected void hashCodeIntern(final HashCode hashCode)
    {
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
     * @see jhelp.vectorial.path.PathElement#draw(jhelp.vectorial.path.PathDrawer, double, double)
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

        pathDrawer.moveTo(x, y);
        return new Point(x, y);
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
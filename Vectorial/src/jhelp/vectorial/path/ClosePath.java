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

import jhelp.util.util.HashCode;
import jhelp.util.io.json.ObjectJSON;
import jhelp.vectorial.math.Point;

/**
 * Close path element
 *
 * @author JHelp
 */
public class ClosePath
        extends PathElement
{
    /** Close path singleton instance */
    public static final ClosePath CLOSE_PATH = new ClosePath();

    /**
     * Create a new instance of ClosePath
     */
    private ClosePath()
    {
        super(false);
    }

    /**
     * Indicates if path element equals to this element <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param pathElement
     *           Path element on good type
     * @return {@code true} if equals
     * @see jhelp.vectorial.path.PathElement#equalsIntern(jhelp.vectorial.path.PathElement)
     */
    @Override
    protected boolean equalsIntern(final PathElement pathElement)
    {
        return true;
    }

    /**
     * Fill specific element in JSON <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param json
     *           JSON to fill
     * @see jhelp.vectorial.path.PathElement#fillJSON(jhelp.util.io.json.ObjectJSON)
     */
    @Override
    protected void fillJSON(final ObjectJSON json)
    {
    }

    /**
     * Complete hash code <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param hashCode
     *           Hash code to complete
     * @see jhelp.vectorial.path.PathElement#hashCodeIntern(jhelp.util.util.HashCode)
     */
    @Override
    protected void hashCodeIntern(final HashCode hashCode)
    {
    }

    /**
     * Draw the element <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param pathDrawer
     *           Path drawer
     * @param referenceX
     *           Start X
     * @param referenceY
     *           Start X
     * @return Final point
     * @see jhelp.vectorial.path.PathElement#draw(jhelp.vectorial.path.PathDrawer, double, double)
     */
    @Override
    public Point draw(final PathDrawer pathDrawer, final double referenceX, final double referenceY)
    {
        pathDrawer.closePath();
        return new Point(referenceX, referenceY);
    }
}
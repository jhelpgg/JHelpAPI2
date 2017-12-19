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
package jhelp.engine.gui.layout;

import java.awt.Dimension;
import jhelp.util.debug.Debug;

/**
 * Relative layout<br>
 * <br>
 * Last modification : 6 juil. 2010<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public class RelativeLayout
        extends Layout
{

    /**
     * Constructs RelativeLayout
     */
    public RelativeLayout()
    {
    }

    /**
     * Compute Preferred size
     *
     * @param width    Space width
     * @param height   Space height
     * @param elements Elements to layout
     * @return Preferred size
     * @see Layout#computePrefferedSize(int, int, LayoutElement[])
     */
    @Override
    public Dimension computePrefferedSize(final int width, final int height, final LayoutElement... elements)
    {
        Debug.todo();
        return null;
    }

    /**
     * Layout components
     *
     * @param width    Space width
     * @param height   Space height
     * @param elements Elements to layout
     * @see Layout#layout(int, int, LayoutElement[])
     */
    @Override
    public void layout(final int width, final int height, final LayoutElement... elements)
    {
        Debug.todo();
    }
}
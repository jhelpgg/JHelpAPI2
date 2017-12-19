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
package jhelp.gui.twoD;

import java.awt.Rectangle;
import java.util.ArrayList;
import jhelp.util.list.Pair;

/**
 * Describe a layout of components
 *
 * @author JHelp
 */
public abstract class JHelpLayout
{
    /**
     * Indicates if a constraints can be use with the layout
     *
     * @param constraints Tested constraints
     * @return {@code true} if the constraints can be use
     */
    public abstract boolean compatible(JHelpConstraints constraints);

    /**
     * Compute bounds that take the layout.<br>
     * And layout the components
     *
     * @param children     Children to layout
     * @param parentWidth  Parent width, -1 to unknown
     * @param parentHeight Parent height, -1 to unknown
     * @return Computed bounds after layout
     */
    public abstract Rectangle computeBounds(
            ArrayList<Pair<JHelpComponent2D, JHelpConstraints>> children, int parentWidth, int parentHeight);
}
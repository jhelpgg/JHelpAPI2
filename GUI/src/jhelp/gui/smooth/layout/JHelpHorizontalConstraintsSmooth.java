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

package jhelp.gui.smooth.layout;

/**
 * Constraints used by {@link JHelpHorizontalLayoutSmooth} to know how place component vertically inside its container
 *
 * @author JHelp
 */
public enum JHelpHorizontalConstraintsSmooth
        implements JHelpConstraintsSmooth
{
    /**
     * Center the component in height
     */
    CENTER,
    /**
     * Put the component at down its container
     */
    DOWN,
    /**
     * Expands the component to take the all height
     */
    EXPAND,
    /**
     * Put the component at up its container
     */
    UP
}
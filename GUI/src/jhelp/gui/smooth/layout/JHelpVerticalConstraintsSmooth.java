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
 * Constraints used by {@link JHelpVerticalLayoutSmooth} to know how place component horizontally in container
 *
 * @author JHelp
 */
public enum JHelpVerticalConstraintsSmooth
        implements JHelpConstraintsSmooth
{
    /**
     * Center component in width
     */
    CENTER,
    /**
     * Expand component width to fit the container
     */
    EXPAND,
    /**
     * Put component at left
     */
    LEFT,
    /**
     * Put component at right
     */
    RIGHT
}
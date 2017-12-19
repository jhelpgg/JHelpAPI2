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

package jhelp.gui.game.builder;

/**
 * Relative position from a rectangle
 *
 * @author JHelp
 */
public enum PositionReferenceType
{
    /**
     * Center horizontal and vertical
     */
    CENTER,
    /**
     * Center horizontal and place at bottom
     */
    CENTER_BOTTOM,
    /**
     * Center horizontal and place at top
     */
    CENTER_TOP,
    /**
     * Place at left bottom
     */
    LEFT_BOTTOM,
    /**
     * Center vertical and place at left
     */
    LEFT_CENTER,
    /**
     * Place at left top
     */
    LEFT_TOP,
    /**
     * Place at right bottom
     */
    RIGHT_BOTTOM,
    /**
     * Center vertical and place at right
     */
    RIGHT_CENTER,
    /**
     * Place at right top
     */
    RIGHT_TOP
}
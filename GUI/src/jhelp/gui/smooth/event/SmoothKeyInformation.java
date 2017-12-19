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

package jhelp.gui.smooth.event;

/**
 * Describe a key event inside
 *
 * @author JHelp
 */
public class SmoothKeyInformation
{
    /**
     * Indicates if <b>ALT</b> is down
     */
    public final boolean altDown;
    /**
     * Indicates if <b>CONTROL</b> is down
     */
    public final boolean controlDown;
    /**
     * Character representation of the key
     */
    public final char    keyChar;
    /**
     * Key code
     */
    public final int     keyCode;
    /**
     * Indicates if <b>SHIFT</b> is down
     */
    public final boolean shiftDown;

    /**
     * Create a new instance of SmoothKeyInformation
     *
     * @param keyCode     Key code
     * @param keyChar     Character representation of the key
     * @param shiftDown   Indicates if <b>SHIFT</b> is down
     * @param controlDown Indicates if <b>CONTROL</b> is down
     * @param altDown     Indicates if <b>ALT</b> is down
     */
    public SmoothKeyInformation(
            final int keyCode, final char keyChar, final boolean shiftDown, final boolean controlDown,
            final boolean altDown)
    {
        this.keyCode = keyCode;
        this.keyChar = keyChar;
        this.shiftDown = shiftDown;
        this.controlDown = controlDown;
        this.altDown = altDown;
    }
}
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

/**
 * Listener of special key (Keys that contorled, shifted, alted or a combination of them) in list
 *
 * @param <INFORMATION> Information type
 * @author JHelp
 */
public interface JHelpListSpecialKeyListener<INFORMATION>
{
    /**
     * Called when special key is pressed
     *
     * @param keycode     Key code
     * @param shift       Indicates if shift is press in same time as the key
     * @param control     Indicates if control is press in same time as the key
     * @param alt         Indicates if alt is press in same time as the key
     * @param list2d      List where the event happen
     * @param information Information currently selected (Code {@code null} if no slection)
     * @param index       Index of curren,t selection (-1 if no selection)
     */
    void specialKeyClicked(
            int keycode, boolean shift, boolean control, boolean alt, JHelpList2D<INFORMATION> list2d,
            INFORMATION information, int index);
}
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
 * Listener of list changed
 *
 * @param <INFORMATION> Information type
 * @author JHelp
 */
public interface JHelpListListener<INFORMATION>
{
    /**
     * Called when list element selected
     *
     * @param list2d        List where selectio changed
     * @param selectedIndex New selected value (-1 means that nothing is selected)
     * @param information   Selected information (If nothing is selected, {@code null})
     * @param clickCount    NumberOfClick
     */
    void listSelectionChanged(
            JHelpList2D<INFORMATION> list2d, int selectedIndex, INFORMATION information, int clickCount);
}
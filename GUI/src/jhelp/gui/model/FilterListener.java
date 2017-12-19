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

package jhelp.gui.model;

/**
 * Listener of {@link Filter} changes
 *
 * @param <OBJECT> Type of filtered elements
 * @author JHelp <br>
 */
public interface FilterListener<OBJECT>
{
    /**
     * Called when a filter change so that the list of accepted element changed
     *
     * @param filter Filter that changed
     */
    void filterChange(Filter<OBJECT> filter);
}
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
package jhelp.gui.smooth.model;

import jhelp.gui.smooth.JHelpListSmooth;
import jhelp.gui.smooth.event.JHelpListSmoothModelListener;

/**
 * Model used by {@link JHelpListSmooth}.<br>
 * It give access to the list data
 *
 * @param <ELEMENT> Element type
 * @author JHelp
 */
public interface JHelpListSmoothModel<ELEMENT>
{
    /**
     * Obtain a element
     *
     * @param index Element index
     * @return Element
     */
    ELEMENT element(int index);

    /**
     * Number of elements
     *
     * @return Number of elements
     */
    int numberOfElement();

    /**
     * Register a listener to be alert on model changes
     *
     * @param listener Listener to register
     */
    void registerModelListener(JHelpListSmoothModelListener<ELEMENT> listener);

    /**
     * Unregister a listener to be no more alert on model changes
     *
     * @param listener Listener to unregister
     */
    void unregisterModelListener(JHelpListSmoothModelListener<ELEMENT> listener);
}
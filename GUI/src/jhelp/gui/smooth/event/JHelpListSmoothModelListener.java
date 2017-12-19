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

import jhelp.gui.smooth.model.JHelpListSmoothModel;

/**
 * Listener of {@link JHelpListSmoothModel} changes
 *
 * @param <ELEMENT> Element type
 * @author JHelp
 */
public interface JHelpListSmoothModelListener<ELEMENT>
{
    /**
     * Called when model as completely change
     *
     * @param model Change model
     */
    void completlyChanged(JHelpListSmoothModel<ELEMENT> model);

    /**
     * Called when one element was inserted in the model
     *
     * @param model Changed model
     * @param index Insert index
     */
    void elementAdded(JHelpListSmoothModel<ELEMENT> model, int index);

    /**
     * Called when one element was removed in the model
     *
     * @param model Changed model
     * @param index Remove index
     */
    void elementRemoved(JHelpListSmoothModel<ELEMENT> model, int index);
}
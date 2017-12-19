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

import jhelp.gui.smooth.JHelpListSmooth;

/**
 * Listener of selection in {@link JHelpListSmooth}
 *
 * @param <ELEMENT> List element type
 * @author JHelp
 */
public interface JHelpListSmoothSelectListener<ELEMENT>
{
    /**
     * Called when a item is selected inside {@link JHelpListSmooth}
     *
     * @param list    {@link JHelpListSmooth} where selection done
     * @param element Selected element
     * @param index   Selected index
     */
    void elementSelected(JHelpListSmooth<ELEMENT> list, ELEMENT element, int index);
}
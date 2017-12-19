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
 * Listener of color choose
 *
 * @author JHelp
 */
public interface JHelpColorChooserListener
{
    /**
     * Called when color changed
     *
     * @param colorChooser Color chooser source
     * @param color        New color
     */
    void colorChanged(JHelpColorChooser colorChooser, int color);
}
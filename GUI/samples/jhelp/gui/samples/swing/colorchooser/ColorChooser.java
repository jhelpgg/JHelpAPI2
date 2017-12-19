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

/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.gui.samples.swing.colorchooser;

import jhelp.gui.samples.swing.colorchooser.gui.ColorChooserFrame;
import jhelp.util.gui.UtilGUI;

/**
 * Color chooser
 *
 * @author JHelp
 */
public class ColorChooser
{
    /**
     * Launch the color chooser
     *
     * @param args
     *           Unused
     */
    public static void main(final String[] args)
    {
        UtilGUI.initializeGUI();

        final ColorChooserFrame colorChooserFrame = new ColorChooserFrame();
        colorChooserFrame.setVisible(true);
    }
}
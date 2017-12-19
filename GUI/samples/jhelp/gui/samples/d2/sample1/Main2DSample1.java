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
package jhelp.gui.samples.d2.sample1;

import jhelp.gui.samples.d2.sample1.gui.FrameSample1;

/**
 * First sample of 2D components with : folding panel, scroll panel, label image, border layout and table layout
 *
 * @author JHelp
 */
public class Main2DSample1
{
    /**
     * Launch the sample
     *
     * @param args
     *           Unused
     */
    public static void main(final String[] args)
    {
        // Create the frame
        final FrameSample1 frameSample1 = new FrameSample1();

        // Show the frame
        // It is recommend to put the visibility outside of the constructor
        frameSample1.setVisible(true);

        // Activate automatic refresh for animated component like fold panel and scroll
        // It is strongly recommend to activate the automatic refresh after the first show of the frame
        frameSample1.automaticRefresh(true);
    }
}
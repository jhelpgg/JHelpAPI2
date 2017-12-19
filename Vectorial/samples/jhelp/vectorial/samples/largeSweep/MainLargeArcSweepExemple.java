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
package jhelp.vectorial.samples.largeSweep;

import jhelp.util.gui.UtilGUI;

/**
 * Launch the frame for understand the role of <b>largeArc</b> and <b>sweep</b>
 *
 * @author JHelp
 */
public class MainLargeArcSweepExemple
{
    /**
     * Launch the frame for understand the role of <b>largeArc</b> and <b>sweep</b>
     *
     * @param args
     *           Unused
     */
    public static void main(final String[] args)
    {
        UtilGUI.initializeGUI();
        final FrameLargeArcSweepExemple frameLargeArcSweepExemple = new FrameLargeArcSweepExemple();
        frameLargeArcSweepExemple.setVisible(true);
    }
}
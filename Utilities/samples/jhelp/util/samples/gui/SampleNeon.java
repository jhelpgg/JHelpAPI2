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

package jhelp.util.samples.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.Path;
import jhelp.util.gui.UtilGUI;
import jhelp.util.samples.common.gui.SampleLabelJHelpImage;
import jhelp.util.thread.LoopTask;
import jhelp.util.thread.RunnableTask;
import jhelp.util.thread.ThreadManager;

public class SampleNeon
        implements RunnableTask
{
    static JHelpImage image;
    static int        indexEnd;
    static int        indexStart;
    static int numberStep = 256;
    static Path                 path;
    static LoopTask<Void, Void> threadID;

    /**
     * @param args
     */
    public static void main(final String[] args)
    {
        final JHelpFont font = new JHelpFont("Arial", 150);
        SampleNeon.path = new Path();
        SampleNeon.path.appendText("Neon sample", font, 20, 20);
        SampleNeon.image = new JHelpImage(1024, 512, 0xFF000000);

        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        final SampleLabelJHelpImage sampleLabelJHelpImage = new SampleLabelJHelpImage(SampleNeon.image);
        sampleLabelJHelpImage.setResize(true);
        frame.add(sampleLabelJHelpImage, BorderLayout.CENTER);
        UtilGUI.packedSize(frame);
        UtilGUI.centerOnScreen(frame);
        frame.setVisible(true);

        SampleNeon.indexStart = 0;
        SampleNeon.indexEnd = 0;
        SampleNeon.threadID = ThreadManager.repeatRun(new SampleNeon(), 1024, 1);

    }

    @Override
    public void run()
    {
        final double percentStart = (double) SampleNeon.indexStart / (double) SampleNeon.numberStep;
        final double percentEnd   = (double) SampleNeon.indexEnd / (double) SampleNeon.numberStep;

        SampleNeon.image.startDrawMode();
        SampleNeon.image.clear(0xFF000000);
        SampleNeon.image.drawNeon(SampleNeon.path, 12, 0xFF0A7E07, percentStart, percentEnd);
        SampleNeon.image.endDrawMode();

        if (SampleNeon.indexEnd < SampleNeon.numberStep)
        {
            SampleNeon.indexEnd++;
        }
        else if (SampleNeon.indexStart < SampleNeon.numberStep)
        {
            SampleNeon.indexStart++;
        }
        else
        {
            SampleNeon.indexStart = 0;
            SampleNeon.indexEnd = 0;
        }
    }
}
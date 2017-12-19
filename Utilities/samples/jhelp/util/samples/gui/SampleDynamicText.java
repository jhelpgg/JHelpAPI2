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
import jhelp.util.debug.Debug;
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpFont.Type;
import jhelp.util.gui.JHelpFont.Value;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpTextAlign;
import jhelp.util.gui.UtilGUI;
import jhelp.util.gui.dynamic.AlphaMaskAnimated;
import jhelp.util.gui.dynamic.AnimationList;
import jhelp.util.gui.dynamic.ShiftImageAnimation;
import jhelp.util.samples.common.gui.SampleLabelJHelpImage;

public class SampleDynamicText
{

    /**
     * @param args
     */
    public static void main(final String[] args)
    {
        try
        {
            UtilGUI.initializeGUI();
            final JHelpImage texture = JHelpImage.loadImage(
                    SampleDynamicText.class.getResourceAsStream("001-Fog01.png"));
            texture.startDrawMode();
            texture.tint(0xFF4080F0, 0xFFFFFFFF);
            texture.endDrawMode();
            final JHelpFont font = JHelpFont.createFont(Type.TRUE_TYPE,
                                                        SampleDynamicText.class.getResourceAsStream(
                                                                "Gretoon.ttf"), 128,
                                                        Value.FREE, Value.FREE, false);
            final AlphaMaskAnimated animated = AlphaMaskAnimated.createTextAnimated(
                    "Hello word !\nHow are you ?", font, JHelpTextAlign.CENTER, 0xFF4080F0);
            final ShiftImageAnimation shiftImageAnimation = new ShiftImageAnimation(texture, 1, 1, 1);
            final AnimationList       animationList       = new AnimationList(Integer.MAX_VALUE);
            animationList.addAnimation(shiftImageAnimation);
            animated.getDynamicImage().playAnimation(animationList);
            final JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            final SampleLabelJHelpImage sampleLabelJHelpImage = new SampleLabelJHelpImage(animated.getImage());
            sampleLabelJHelpImage.setResize(true);
            frame.add(sampleLabelJHelpImage, BorderLayout.CENTER);
            UtilGUI.packedSize(frame);
            UtilGUI.centerOnScreen(frame);
            frame.setVisible(true);
        }
        catch (final Exception exception)
        {
            Debug.exception(exception, "Failed to create the sample");
        }
    }
}
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

package jhelp.vectorial.samples;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import jhelp.util.gui.UtilGUI;
import jhelp.util.samples.common.gui.SampleLabelJHelpImage;
import jhelp.vectorial.layer.Canvas;
import jhelp.vectorial.layer.Layer;
import jhelp.vectorial.path.Path;
import jhelp.vectorial.shape.Shape;

public class Cubic
{
    public static void main(String[] args)
    {
        Canvas canvas = new Canvas(512, 512);
        Layer  layer  = canvas.createNewLayer();
        layer.setBackground(0xFFFFFFFF);

        layer.fill(new Shape(Path.createCenterCircle(64, 256, 16)), 0xFF0000FF);
        layer.fill(new Shape(Path.createCenterCircle(200, 64, 16)), 0xFF00FF00);
        layer.fill(new Shape(Path.createCenterCircle(400, 256 + 64, 16)), 0xFF00FF00);
        layer.fill(new Shape(Path.createCenterCircle(512 - 64, 256, 16)), 0xFF0000FF);

        Path path = new Path();
        path.moveTo(false, 64, 256);
        path.lineTo(false, 200, 64);
        path.lineTo(false, 400, 256 + 64);
        path.lineTo(false, 512 - 64, 256);
        Shape shape = new Shape(path);
        layer.stroke(shape, 0xFF000000, 2);

        path = new Path();
        path.moveTo(false, 64, 256);
        path.cubicBezierTo(false, 200, 64, 400, 256 + 64, 512 - 64, 256);
        shape = new Shape(path);
        shape.setPrecision(32);
        layer.stroke(shape, 0xFFFF0000, 4);

        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        SampleLabelJHelpImage sampleLabelJHelpImage = new SampleLabelJHelpImage(canvas.updateImage());
        sampleLabelJHelpImage.setResize(true);
        frame.add(sampleLabelJHelpImage, BorderLayout.CENTER);
        UtilGUI.packedSize(frame);
        UtilGUI.centerOnScreen(frame);
        frame.setVisible(true);
    }
}

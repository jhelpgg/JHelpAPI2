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
package jhelp.vectorial.samples;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import jhelp.util.debug.Debug;
import jhelp.util.gui.JHelpGradient;
import jhelp.util.gui.JHelpGradientHorizontal;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpPaint;
import jhelp.util.gui.UtilGUI;
import jhelp.util.samples.common.gui.SampleLabelJHelpImage;
import jhelp.util.util.Utilities;
import jhelp.vectorial.layer.Canvas;
import jhelp.vectorial.layer.Layer;
import jhelp.vectorial.path.ArcMode;
import jhelp.vectorial.path.Path;
import jhelp.vectorial.shape.Shape;

/**
 * A Canavs sample
 *
 * @author JHelp
 */
public class SampleCanvas1
        implements Runnable
{
    /**
     * The canvas
     */
    static Canvas                canvas;
    /**
     * Component where canvas draw
     */
    static SampleLabelJHelpImage sampleLabelJHelpImage;

    /**
     * Launch the sample
     *
     * @param args Unused
     */
    public static void main(final String[] args)
    {
        SampleCanvas1.canvas = new Canvas(1024, 1024);
        final Layer      layer   = SampleCanvas1.canvas.createNewLayer();
        JHelpPaint       paint   = new JHelpGradient(0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFFFFFFFF);
        final JHelpImage texture = new JHelpImage(64, 64, 0xEEEEEEEE);
        texture.startDrawMode();
        texture.fillEllipse(0, 0, 32, 32, 0xFFFF0000);
        texture.fillEllipse(32, 32, 16, 16, 0xFF0000FF);
        texture.drawRectangle(0, 0, 64, 64, 0xFF000000);
        texture.endDrawMode();

        Shape shape = new Shape(Path.createCenterCircle(0, 0, 1));
        shape.setPosition(220, 220);
        shape.setScale(100);
        layer.fillAndStrokeNeon(shape, texture, 0xCC0A7E07, 7, 1.23456789);

        shape = new Shape(Path.createCenterArc(0, 0, 1, 1, -Math.PI / 2, 0, ArcMode.PIE));
        shape.setPosition(400, 400);
        shape.setScale(100);
        layer.fillAndStrokeNeon(shape, paint, 0xFF7E0A07, 7, 1.23456789);

        paint = new JHelpGradientHorizontal(0xFF7E0A07, 0xFF0A077E);
        ((JHelpGradientHorizontal) paint).addColor(50, 0xFF0A7E07);
        final Path path = new Path();
        path.moveTo(false, 0, 0);
        path.cubicBezierTo(true, 1, 0.5, 2, 1.5, 3, -0.5);
        path.lineTo(true, -1, 4);
        path.closePath();
        shape = new Shape(path);
        shape.setPosition(220, 404);
        shape.setScale(50);
        shape.setPrecision(32);
        layer.fillAndStrokeNeon(shape, paint, 0xFF0A077E, 7, 1.23456789);

        layer.setTranslate(300, 300);
        layer.setZoom(0.1);

        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        SampleCanvas1.sampleLabelJHelpImage = new SampleLabelJHelpImage(SampleCanvas1.canvas.updateImage());
        SampleCanvas1.sampleLabelJHelpImage.setResize(true);
        frame.add(SampleCanvas1.sampleLabelJHelpImage, BorderLayout.CENTER);
        UtilGUI.packedSize(frame);
        UtilGUI.centerOnScreen(frame);
        frame.setVisible(true);

        final Thread thread = new Thread(new SampleCanvas1());
        thread.start();
    }

    /**
     * Thread action : zoom the view <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run()
    {
        Utilities.sleep(4096);

        final double zoom = 2;
        final int    max  = 100;
        long         time = System.nanoTime();
        long         top;

        for (int i = 1; i <= max; i++)
        {
            top = System.nanoTime();
            SampleCanvas1.canvas.getLayer(0).setZoom((zoom * i) / max);
            SampleCanvas1.sampleLabelJHelpImage.setJHelpImage(SampleCanvas1.canvas.updateImage());
            Debug.information("top=", (System.nanoTime() - top) / 1000000);
        }

        time = System.nanoTime() - time;
        Debug.information("time=", time / 1000000, "ms : ~", (time / max) / 1000000, "ms per draw");
    }
}
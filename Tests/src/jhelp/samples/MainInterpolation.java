package jhelp.samples;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import jhelp.util.gui.UtilGUI;
import jhelp.util.gui.dynamic.AccelerationInterpolation;
import jhelp.util.gui.dynamic.AnticipateInterpolation;
import jhelp.util.gui.dynamic.AnticipateOvershootInterpolation;
import jhelp.util.gui.dynamic.BouncingInterpolation;
import jhelp.util.gui.dynamic.CubicInterpolation;
import jhelp.util.gui.dynamic.DeccelerationInterpolation;
import jhelp.util.gui.dynamic.Interpolation;
import jhelp.util.gui.dynamic.Interpolations;
import jhelp.util.gui.dynamic.OvershootInterpolation;
import jhelp.util.gui.dynamic.QuadraticInterpolation;

/**
 * Created by jhelp on 14/07/17.
 */
public class MainInterpolation
{
    private static final int STEP = 128;

    public static void main(String[] args)
    {
        UtilGUI.initializeGUI();
        int x = 0;
        int y = 0;

        for (Interpolation interpolation : Interpolations.values())
        {
            JFrame frame = new JFrame(interpolation.toString());
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            DynamicInterpolation dynamicInterpolation = new DynamicInterpolation(512, 1024, interpolation);
            frame.setLayout(new BorderLayout());
            frame.add(new ComponentImage(dynamicInterpolation.image()), BorderLayout.CENTER);
            UtilGUI.packedSize(frame);
            frame.setLocation(x, y);
            x += MainInterpolation.STEP;

            if (x > 2048)
            {
                x = 0;
                y += MainInterpolation.STEP;
            }

            frame.setVisible(true);
        }

        Interpolation[] interpolations = {
                new AccelerationInterpolation(1.23456789f),
                new AnticipateInterpolation(1.23456789f),
                new AnticipateOvershootInterpolation(1.23456789f),
                new BouncingInterpolation(3),
                new CubicInterpolation(0.123456789f, 0.987654321f),
                new DeccelerationInterpolation(1.23456789f),
                new OvershootInterpolation(1.23456789f),
                new QuadraticInterpolation(0.123456789f)
        };

        for (Interpolation interpolation : interpolations)
        {
            JFrame frame = new JFrame(interpolation.getClass().getSimpleName());
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            DynamicInterpolation dynamicInterpolation = new DynamicInterpolation(512, 1024, interpolation);
            frame.setLayout(new BorderLayout());
            frame.add(new ComponentImage(dynamicInterpolation.image()), BorderLayout.CENTER);
            UtilGUI.packedSize(frame);
            frame.setLocation(x, y);
            x += MainInterpolation.STEP;

            if (x > 2048)
            {
                x = 0;
                y += MainInterpolation.STEP;
            }

            frame.setVisible(true);
        }
    }
}

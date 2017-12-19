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

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import jhelp.util.gui.UtilGUI;
import jhelp.util.samples.common.gui.SampleLabelJHelpImage;
import jhelp.vectorial.layer.Canvas;
import jhelp.vectorial.layer.Layer;
import jhelp.vectorial.path.Path;
import jhelp.vectorial.shape.Shape;

/**
 * Frame for understand the role of <b>largeArc</b> and <b>sweep</b>
 *
 * @author JHelp
 */
public class FrameLargeArcSweepExemple
        extends JFrame
{
    /**
     * Layer name and button text for <br>
     * large arc</b> disable and <b>sweep</b> disable
     */
    private static final String LARGE_ARC_FALSE_SWEEP_FALSE = "largeArc=false | sweep=false";
    /**
     * Layer name and button text for <br>
     * large arc</b> disable and <b>sweep</b> enable
     */
    private static final String LARGE_ARC_FALSE_SWEEP_TRUE  = "largeArc=false | sweep=true";
    /**
     * Layer name and button text for <br>
     * large arc</b> enable and <b>sweep</b> disable
     */
    private static final String LARGE_ARC_TRUE_SWEEP_FALSE  = "largeArc=true | sweep=false";
    /**
     * Layer name and button text for <br>
     * large arc</b> enable and <b>sweep</b> enable
     */
    private static final String LARGE_ARC_TRUE_SWEEP_TRUE   = "largeArc=true | sweep=true";
    /** Main layer name */
    private static final String MAIN_LAYER                  = "main";
    /** Main image size (width and height) */
    private static final int    SIZE                        = 1024;

    /**
     * Event manager
     *
     * @author JHelp
     */
    private class EventManager
            implements ActionListener
    {
        /**
         * Create a new instance of EventManager
         */
        EventManager()
        {
        }

        /**
         * Called when a button is pressed <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param actionEvent
         *           Event description
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        public void actionPerformed(final ActionEvent actionEvent)
        {
            FrameLargeArcSweepExemple.this.clickOn(actionEvent.getActionCommand());
        }
    }

    /** Canvas where sample is draw */
    private final Canvas                canvas;
    /** Event manager */
    private final EventManager          eventManager;
    /** Component where canvas is draw */
    private final SampleLabelJHelpImage sampleLabelJHelpImage;

    /**
     * Create a new instance of FrameLargeArcSweepExemple
     */
    public FrameLargeArcSweepExemple()
    {
        super("Large arc AND sweep");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.eventManager = new EventManager();
        this.canvas = new Canvas(FrameLargeArcSweepExemple.SIZE, FrameLargeArcSweepExemple.SIZE);
        this.createLayers();
        this.sampleLabelJHelpImage = new SampleLabelJHelpImage(this.canvas.updateImage());
        this.sampleLabelJHelpImage.setResize(true);

        this.setLayout(new BorderLayout());
        this.add(this.sampleLabelJHelpImage, BorderLayout.CENTER);

        final JPanel      panel       = new JPanel(new GridLayout(4, 1));
        final ButtonGroup buttonGroup = new ButtonGroup();

        JToggleButton button = this.createButton(FrameLargeArcSweepExemple.LARGE_ARC_FALSE_SWEEP_FALSE);
        buttonGroup.add(button);
        panel.add(button);

        button = this.createButton(FrameLargeArcSweepExemple.LARGE_ARC_FALSE_SWEEP_TRUE);
        buttonGroup.add(button);
        panel.add(button);

        button = this.createButton(FrameLargeArcSweepExemple.LARGE_ARC_TRUE_SWEEP_FALSE);
        buttonGroup.add(button);
        panel.add(button);

        button = this.createButton(FrameLargeArcSweepExemple.LARGE_ARC_TRUE_SWEEP_TRUE);
        buttonGroup.add(button);
        panel.add(button);

        this.add(panel, BorderLayout.EAST);

        UtilGUI.packedSize(this);
        UtilGUI.centerOnScreen(this);
    }

    /**
     * Create a toggle button
     *
     * @param text
     *           Button text
     * @return Created button
     */
    private JToggleButton createButton(final String text)
    {
        final JToggleButton button = new JToggleButton(text);
        button.setActionCommand(text);
        button.addActionListener(this.eventManager);
        return button;
    }

    /**
     * Create canvas layers
     */
    private void createLayers()
    {
        final double x1     = FrameLargeArcSweepExemple.SIZE * 0.3;
        final double y1     = FrameLargeArcSweepExemple.SIZE * 0.6;
        final double x2     = FrameLargeArcSweepExemple.SIZE * 0.6;
        final double y2     = FrameLargeArcSweepExemple.SIZE * 0.3;
        final double width  = Math.abs(x2 - x1);
        final double height = Math.abs(y2 - y1);
        Layer        layer  = this.canvas.createNewLayer(FrameLargeArcSweepExemple.MAIN_LAYER);
        layer.setBackground(0xFFFFFFFF);
        Path path = new Path();
        path.moveTo(false, x1, y1);
        path.ellipticalArcTo(false, width, height, 0, false, false, x2, y2);
        path.moveTo(false, x1, y1);
        path.ellipticalArcTo(false, width, height, 0, false, true, x2, y2);
        path.moveTo(false, x1, y1);
        path.ellipticalArcTo(false, width, height, 0, true, false, x2, y2);
        path.moveTo(false, x1, y1);
        path.ellipticalArcTo(false, width, height, 0, true, true, x2, y2);
        layer.stroke(new Shape(path), 0xFF000000);

        layer = this.canvas.createNewLayer(FrameLargeArcSweepExemple.LARGE_ARC_FALSE_SWEEP_FALSE);
        path = new Path();
        path.moveTo(false, x1, y1);
        path.ellipticalArcTo(false, width, height, 0, false, false, x2, y2);
        layer.stroke(new Shape(path), 0xFFFF0000, 5);

        layer = this.canvas.createNewLayer(FrameLargeArcSweepExemple.LARGE_ARC_FALSE_SWEEP_TRUE);
        path = new Path();
        path.moveTo(false, x1, y1);
        path.ellipticalArcTo(false, width, height, 0, false, true, x2, y2);
        layer.stroke(new Shape(path), 0xFFFF0000, 5);
        layer.setVisible(false);

        layer = this.canvas.createNewLayer(FrameLargeArcSweepExemple.LARGE_ARC_TRUE_SWEEP_FALSE);
        path = new Path();
        path.moveTo(false, x1, y1);
        path.ellipticalArcTo(false, width, height, 0, true, false, x2, y2);
        layer.stroke(new Shape(path), 0xFFFF0000, 5);
        layer.setVisible(false);

        layer = this.canvas.createNewLayer(FrameLargeArcSweepExemple.LARGE_ARC_TRUE_SWEEP_TRUE);
        path = new Path();
        path.moveTo(false, x1, y1);
        path.ellipticalArcTo(false, width, height, 0, true, true, x2, y2);
        layer.stroke(new Shape(path), 0xFFFF0000, 5);
        layer.setVisible(false);
    }

    /**
     * Called when a button is clicked
     *
     * @param action
     *           Clicked button action
     */
    void clickOn(final String action)
    {
        final int size = this.canvas.numberOfLayer();
        Layer     layer;
        String    name;

        for (int i = 0; i < size; i++)
        {
            layer = this.canvas.getLayer(i);
            name = layer.getName();
            layer.setVisible(
                    (FrameLargeArcSweepExemple.MAIN_LAYER.equals(name) == true) || (name.equals(action) == true));
        }

        this.sampleLabelJHelpImage.setJHelpImage(this.canvas.updateImage());
    }
}
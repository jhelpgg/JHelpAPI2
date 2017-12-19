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
package jhelp.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import jhelp.gui.action.GenericAction;
import jhelp.gui.event.ColorChooserListener;
import jhelp.gui.layout.CenterLayout;
import jhelp.util.gui.JHelpImage;
import jhelp.util.io.ByteArray;
import jhelp.util.io.UtilIO;
import jhelp.util.preference.Preferences;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.ThreadManager;

/**
 * Choose a color component
 *
 * @author JHelp
 */
public class ColorChooser
        extends JPanel
{
    static
    {
        PALETTE_SIZE = 32;
        COLOR_SIZE = ColorChooser.PALETTE_SIZE * 4;
        IMAGE_SIZE = ColorChooser.COLOR_SIZE * 2;
    }

    /**
     * Describe change event
     *
     * @author JHelp
     */
    enum ChangeMethod
    {
        /**
         * The color changed
         */
        COLOR_CHANGE,
        /**
         * No change at all
         */
        NO_CHANGE,
        /**
         * One slider change of position
         */
        SLIDER_CHANGE,
        /**
         * One spinner change value
         */
        SPINNER_CHANGE
    }

    /**
     * Color size
     */
    private static final int COLOR_SIZE;
    /**
     * Image size
     */
    private static final int IMAGE_SIZE;
    /**
     * Key for palette in preferences
     */
    private static final String KEY_PALETTE          = "palette";
    /**
     * Maximum palette size
     */
    private static final int    PALETTE_MAXIMUM_SIZE = 64;
    /**
     * Palette size
     */
    private static final int PALETTE_SIZE;

    /**
     * Cancel action
     *
     * @author JHelp
     */
    class ActionCancel
            extends GenericAction
    {
        /**
         * Create a new instance of ActionCancel
         */
        ActionCancel()
        {
            super(ResourcesGUI.OPTION_PANE_CANCEL, ResourcesGUI.RESOURCE_TEXT);
        }

        /**
         * Cancel the choose <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param actionEvent Event description
         * @see GenericAction#doActionPerformed(ActionEvent)
         */
        @Override
        protected void doActionPerformed(final ActionEvent actionEvent)
        {
            ColorChooser.this.doCancel();
        }
    }

    /**
     * Ok action
     *
     * @author JHelp
     */
    class ActionOk
            extends GenericAction
    {
        /**
         * Create a new instance of ActionOk
         */
        ActionOk()
        {
            super(ResourcesGUI.OPTION_PANE_OK, ResourcesGUI.RESOURCE_TEXT);
        }

        /**
         * Validate the color choose <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param actionEvent Event description
         * @see GenericAction#doActionPerformed(ActionEvent)
         */
        @Override
        protected void doActionPerformed(final ActionEvent actionEvent)
        {
            ColorChooser.this.doOK();
        }
    }

    /**
     * Manage user events
     *
     * @author JHelp
     */
    class EventManager
            implements MouseListener, ChangeListener
    {
        /**
         * Create a new instance of EventManager
         */
        EventManager()
        {
        }

        /**
         * Called when mouse clicked <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param mouseEvent Event description
         * @see MouseListener#mouseClicked(MouseEvent)
         */
        @Override
        public void mouseClicked(final MouseEvent mouseEvent)
        {
            ColorChooser.this.doClick(mouseEvent.getX(), mouseEvent.getY());
        }

        /**
         * Called when mouse button pressed <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param mouseEvent Event description
         * @see MouseListener#mousePressed(MouseEvent)
         */
        @Override
        public void mousePressed(final MouseEvent mouseEvent)
        {
        }

        /**
         * Called when mouse button released <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param mouseEvent Event description
         * @see MouseListener#mouseReleased(MouseEvent)
         */
        @Override
        public void mouseReleased(final MouseEvent mouseEvent)
        {
        }

        /**
         * Called when mouse entered <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param mouseEvent Event description
         * @see MouseListener#mouseEntered(MouseEvent)
         */
        @Override
        public void mouseEntered(final MouseEvent mouseEvent)
        {
        }

        /**
         * Called when mouse exit <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param mouseEvent Event description
         * @see MouseListener#mouseExited(MouseEvent)
         */
        @Override
        public void mouseExited(final MouseEvent mouseEvent)
        {
        }

        /**
         * Called when a spinner or slider changed of value <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param changeEvent Event description
         * @see ChangeListener#stateChanged(ChangeEvent)
         */
        @Override
        public void stateChanged(final ChangeEvent changeEvent)
        {
            final Object source = changeEvent.getSource();

            if (source instanceof JSlider)
            {
                ColorChooser.this.updateColorChooser(ChangeMethod.SLIDER_CHANGE);
            }
            else if (source instanceof JSpinner)
            {
                ColorChooser.this.updateColorChooser(ChangeMethod.SPINNER_CHANGE);
            }
        }
    }

    /**
     * Task that signal cancel action to one listener
     *
     * @author JHelp
     */
    class TaskFireCancel implements ConsumerTask<ColorChooserListener>
    {
        /**
         * Create a new instance of TaskFireCancel
         */
        TaskFireCancel()
        {
        }

        /**
         * Signal cancel <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param parameter Listener to alert
         * @see jhelp.util.thread.ConsumerTask#consume(Object)
         */
        @Override
        public void consume(final ColorChooserListener parameter)
        {
            parameter.colorChooseCanceled(ColorChooser.this);
        }
    }

    /**
     * Task that signal color choose to one listener
     *
     * @author JHelp
     */
    class TaskFireColorChoose implements ConsumerTask<ColorChooserListener>
    {
        /**
         * Create a new instance of TaskFireColorChoose
         */
        TaskFireColorChoose()
        {
        }

        /**
         * Signal to listener that color choose <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param parameter Listener to signal
         * @see jhelp.util.thread.ConsumerTask#consume(Object)
         */
        @Override
        public void consume(final ColorChooserListener parameter)
        {
            parameter.colorChoose(ColorChooser.this, ColorChooser.this.currentColor);
        }
    }

    /**
     * Cancel action
     */
    private final ActionCancel               actionCancel;
    /**
     * Ok action
     */
    private final ActionOk                   actionOk;
    /**
     * Color chooser listeners
     */
    private final List<ColorChooserListener> colorChooserListeners;
    /**
     * User events manager
     */
    private final EventManager               eventManager;
    /**
     * Image for choose a color
     */
    private final JHelpImage                 imageColors;
    /**
     * Label where draw the image for choose a color
     */
    private final LabelJHelpImage            labelColors;
    /**
     * Current palette
     */
    private final int[]                      palette;
    /**
     * Current palette size
     */
    private       int                        paletteSize;
    /**
     * Preferences where store the palette
     */
    private final Preferences                preferences;
    /**
     * Slider for alpha level
     */
    private final JSlider                    sliderAlpha;
    /**
     * Slider for blue level
     */
    private final JSlider                    sliderBlue;
    /**
     * Slider for green level
     */
    private final JSlider                    sliderGreen;
    /**
     * Slider for red level
     */
    private final JSlider                    sliderRed;
    /**
     * Spinner for alpha level
     */
    private final JSpinner                   spinnerAlpha;
    /**
     * Spinner for blue level
     */
    private final JSpinner                   spinnerBlue;
    /**
     * Spinner for green level
     */
    private final JSpinner                   spinnerGreen;
    /**
     * Spinner for red level
     */
    private final JSpinner                   spinnerRed;
    /**
     * Start color
     */
    private       int                        startColor;
    /**
     * Task for signal cancel
     */
    private final TaskFireCancel             taskFireCancel;
    /**
     * Task for signal choose
     */
    private final TaskFireColorChoose        taskFireColorChoose;
    /**
     * Current color
     */
    int currentColor;

    /**
     * Create a new instance of ColorChooser
     */
    public ColorChooser()
    {
        super(new BorderLayout());

        this.colorChooserListeners = new ArrayList<ColorChooserListener>();
        this.taskFireColorChoose = new TaskFireColorChoose();
        this.taskFireCancel = new TaskFireCancel();
        final File file = new File(UtilIO.obtainHomeDirectory(), "JHelp/jhelp.gui.ColorChooser/preferences.pref");
        this.preferences = new Preferences(file);
        this.paletteSize = 0;
        this.palette = new int[ColorChooser.PALETTE_MAXIMUM_SIZE];

        for (int i = 0; i < ColorChooser.PALETTE_MAXIMUM_SIZE; i++)
        {
            this.palette[i] = 0xFFFFFFFF;
        }

        final byte[] paletteData = this.preferences.getArrayValue(ColorChooser.KEY_PALETTE);

        if (paletteData != null)
        {
            final ByteArray byteArray = new ByteArray();
            byteArray.write(paletteData);
            final int[] pal = byteArray.readIntegerArray();
            this.paletteSize = pal.length;
            System.arraycopy(pal, 0, this.palette, 0, this.paletteSize);
        }

        this.startColor = 0xFF000000;
        this.currentColor = 0xFF000000;
        this.imageColors = new JHelpImage(ColorChooser.IMAGE_SIZE, ColorChooser.IMAGE_SIZE);
        this.labelColors = new LabelJHelpImage(this.imageColors);
        this.sliderAlpha = new JSlider(SwingConstants.HORIZONTAL, 0, 255, 255);
        this.spinnerAlpha = new JSpinner(new SpinnerNumberModel(255, 0, 255, 1));
        this.sliderRed = new JSlider(SwingConstants.HORIZONTAL, 0, 255, 0);
        this.spinnerRed = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        this.sliderGreen = new JSlider(SwingConstants.HORIZONTAL, 0, 255, 0);
        this.spinnerGreen = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        this.sliderBlue = new JSlider(SwingConstants.HORIZONTAL, 0, 255, 0);
        this.spinnerBlue = new JSpinner(new SpinnerNumberModel(0, 0, 255, 1));
        this.actionOk = new ActionOk();
        this.actionCancel = new ActionCancel();
        final JButton buttonOk     = new JButton(this.actionOk);
        final JButton buttonCancel = new JButton(this.actionCancel);
        this.eventManager = new EventManager();

        final JPanel center = new JPanel(new GridLayout(1, 2));
        final JPanel panel  = new JPanel(new CenterLayout());
        panel.add(this.labelColors);
        center.add(panel);

        final JPanel settings = new JPanel(new GridBagLayout());
        this.addSetting(ResourcesGUI.Color_CHOOSER_ALPHA, this.spinnerAlpha, this.sliderAlpha, 0, settings);
        this.addSetting(ResourcesGUI.Color_CHOOSER_RED, this.spinnerRed, this.sliderRed, 1, settings);
        this.addSetting(ResourcesGUI.Color_CHOOSER_GREEN, this.spinnerGreen, this.sliderGreen, 2, settings);
        this.addSetting(ResourcesGUI.COLOR_CHOOSER_BLUE, this.spinnerBlue, this.sliderBlue, 3, settings);
        center.add(settings);

        final JPanel south = new JPanel(new FlowLayout());
        south.add(buttonOk);
        south.add(buttonCancel);

        this.add(center, BorderLayout.CENTER);
        this.add(south, BorderLayout.SOUTH);

        this.labelColors.addMouseListener(this.eventManager);
        this.updateColorChooser(ChangeMethod.NO_CHANGE);
    }

    /**
     * Add setting for color part
     *
     * @param key     Text key
     * @param spinner Spinner to layout
     * @param slider  Slider to layout
     * @param y       Y position
     * @param panel   Panel where put components
     */
    private void addSetting(
            final String key, final JSpinner spinner, final JSlider slider, final int y, final JPanel panel)
    {
        panel.add(new JLabel(ResourcesGUI.RESOURCE_TEXT.getText(key)), this.createWordConstraints(y));
        panel.add(spinner, this.createSpinnerConstraints(y));
        panel.add(slider, this.createSliderConstraints(y));
    }

    /**
     * Create constraints for a slider
     *
     * @param y Y position
     * @return Created constraints
     */
    private GridBagConstraints createSliderConstraints(final int y)
    {
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 2;
        constraints.gridy = y;
        constraints.gridwidth = 8;
        constraints.gridheight = 1;
        return constraints;
    }

    /**
     * Create constraints for a spinner
     *
     * @param y Y position
     * @return Created constraints
     */
    private GridBagConstraints createSpinnerConstraints(final int y)
    {
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = y;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        return constraints;
    }

    /**
     * Create constraints for a label
     *
     * @param y Y position
     * @return Created constraints
     */
    private GridBagConstraints createWordConstraints(final int y)
    {
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = y;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        return constraints;
    }

    /**
     * Cancel the color chooser
     */
    void doCancel()
    {
        this.currentColor = this.startColor;
        this.updateColorChooser(ChangeMethod.COLOR_CHANGE);

        this.fireCancel();
    }

    /**
     * Action on mouse click on colors image
     *
     * @param x Mouse X
     * @param y Mouse Y
     */
    void doClick(final int x, final int y)
    {
        if ((x < ColorChooser.COLOR_SIZE) && (y < ColorChooser.COLOR_SIZE))
        {
            this.currentColor = this.startColor;
            this.updateColorChooser(ChangeMethod.COLOR_CHANGE);
            return;
        }

        if (y < ColorChooser.COLOR_SIZE)
        {
            return;
        }

        final int xx    = x / ColorChooser.PALETTE_SIZE;
        final int yy    = (y - ColorChooser.COLOR_SIZE) / ColorChooser.PALETTE_SIZE;
        final int index = xx + (yy * 8);
        this.currentColor = this.palette[index];
        this.updateColorChooser(ChangeMethod.COLOR_CHANGE);
    }

    /**
     * Validate current color
     */
    void doOK()
    {
        if (this.paletteSize < ColorChooser.PALETTE_MAXIMUM_SIZE)
        {
            this.palette[this.paletteSize] = this.currentColor;
            this.paletteSize++;
        }
        else
        {
            System.arraycopy(this.palette, 1, this.palette, 0, ColorChooser.PALETTE_MAXIMUM_SIZE - 1);
            this.palette[ColorChooser.PALETTE_MAXIMUM_SIZE - 1] = this.currentColor;
        }

        this.updateColorChooser(ChangeMethod.NO_CHANGE);
        this.fireColorChoose();
    }

    /**
     * Update the image colors and components
     *
     * @param changeMethod Method that change the color value
     */
    void updateColorChooser(final ChangeMethod changeMethod)
    {
        this.sliderAlpha.removeChangeListener(this.eventManager);
        this.spinnerAlpha.removeChangeListener(this.eventManager);
        this.sliderRed.removeChangeListener(this.eventManager);
        this.spinnerRed.removeChangeListener(this.eventManager);
        this.sliderGreen.removeChangeListener(this.eventManager);
        this.spinnerGreen.removeChangeListener(this.eventManager);
        this.sliderBlue.removeChangeListener(this.eventManager);
        this.spinnerBlue.removeChangeListener(this.eventManager);

        switch (changeMethod)
        {
            case COLOR_CHANGE:
                this.spinnerAlpha.setValue((this.currentColor >> 24) & 0xFF);
                this.sliderAlpha.setValue((this.currentColor >> 24) & 0xFF);
                this.spinnerRed.setValue((this.currentColor >> 16) & 0xFF);
                this.sliderRed.setValue((this.currentColor >> 16) & 0xFF);
                this.spinnerGreen.setValue((this.currentColor >> 8) & 0xFF);
                this.sliderGreen.setValue((this.currentColor >> 8) & 0xFF);
                this.spinnerBlue.setValue(this.currentColor & 0xFF);
                this.sliderBlue.setValue(this.currentColor & 0xFF);
                break;
            case NO_CHANGE:
                // No change, nothing to do :)
                break;
            case SLIDER_CHANGE:
                this.spinnerAlpha.setValue(this.sliderAlpha.getValue());
                this.spinnerRed.setValue(this.sliderRed.getValue());
                this.spinnerGreen.setValue(this.sliderGreen.getValue());
                this.spinnerBlue.setValue(this.sliderBlue.getValue());
                this.currentColor = (this.sliderAlpha.getValue() << 24) | (this.sliderRed.getValue() << 16) |
                                    (this.sliderGreen.getValue() << 8)
                                    | this.sliderBlue.getValue();
                break;
            case SPINNER_CHANGE:
                this.sliderAlpha.setValue((Integer) this.spinnerAlpha.getValue());
                this.sliderRed.setValue((Integer) this.spinnerRed.getValue());
                this.sliderGreen.setValue((Integer) this.spinnerGreen.getValue());
                this.sliderBlue.setValue((Integer) this.spinnerBlue.getValue());
                this.currentColor = (this.sliderAlpha.getValue() << 24) | (this.sliderRed.getValue() << 16) |
                                    (this.sliderGreen.getValue() << 8)
                                    | this.sliderBlue.getValue();
                break;
        }

        this.imageColors.startDrawMode();
        this.imageColors.clear(0);
        this.imageColors.fillRectangle(0, 0, ColorChooser.COLOR_SIZE, ColorChooser.COLOR_SIZE, this.startColor);
        this.imageColors.fillRectangle(ColorChooser.COLOR_SIZE, 0, ColorChooser.COLOR_SIZE, ColorChooser.COLOR_SIZE,
                                       this.currentColor);
        int index = 0;

        for (int y = 0; y < 8; y++)
        {
            for (int x = 0; x < 8; x++)
            {
                this.imageColors.fillRectangle(x * ColorChooser.PALETTE_SIZE,
                                               ColorChooser.COLOR_SIZE + (y * ColorChooser.PALETTE_SIZE),
                                               ColorChooser.PALETTE_SIZE,
                                               ColorChooser.PALETTE_SIZE, this.palette[index]);
                this.imageColors.drawRectangle(x * ColorChooser.PALETTE_SIZE,
                                               ColorChooser.COLOR_SIZE + (y * ColorChooser.PALETTE_SIZE),
                                               ColorChooser.PALETTE_SIZE,
                                               ColorChooser.PALETTE_SIZE, 0xFF000000);
                index++;
            }
        }

        this.imageColors.endDrawMode();
        this.labelColors.refresh();

        if (this.paletteSize > 0)
        {
            final ByteArray byteArray = new ByteArray();
            byteArray.writeIntegerArray(Arrays.copyOf(this.palette, this.paletteSize));
            this.preferences.setValue(ColorChooser.KEY_PALETTE, byteArray.toArray());
        }

        this.sliderAlpha.addChangeListener(this.eventManager);
        this.spinnerAlpha.addChangeListener(this.eventManager);
        this.sliderRed.addChangeListener(this.eventManager);
        this.spinnerRed.addChangeListener(this.eventManager);
        this.sliderGreen.addChangeListener(this.eventManager);
        this.spinnerGreen.addChangeListener(this.eventManager);
        this.sliderBlue.addChangeListener(this.eventManager);
        this.spinnerBlue.addChangeListener(this.eventManager);
    }

    /**
     * Signal cancel to listeners
     */
    protected void fireCancel()
    {
        synchronized (this.colorChooserListeners)
        {
            for (final ColorChooserListener colorChooserListener : this.colorChooserListeners)
            {
                ThreadManager.parallel(this.taskFireCancel, colorChooserListener);
            }
        }
    }

    /**
     * Signal choose to listeners
     */
    protected void fireColorChoose()
    {
        synchronized (this.colorChooserListeners)
        {
            for (final ColorChooserListener colorChooserListener : this.colorChooserListeners)
            {
                ThreadManager.parallel(this.taskFireColorChoose, colorChooserListener);
            }
        }
    }

    /**
     * Current color
     *
     * @return Current color
     */
    public int currentColor()
    {
        return this.currentColor;
    }

    /**
     * Register color listener
     *
     * @param colorChooserListener Listener to register
     */
    public void registerColorChooserListener(final ColorChooserListener colorChooserListener)
    {
        if (colorChooserListener == null)
        {
            throw new NullPointerException("colorChooserListener mustn't be null");
        }

        synchronized (this.colorChooserListeners)
        {
            if (!this.colorChooserListeners.contains(colorChooserListener))
            {
                this.colorChooserListeners.add(colorChooserListener);
            }
        }
    }

    /**
     * Change the start color
     *
     * @param color New start color
     */
    public void startColor(final int color)
    {
        this.startColor = color;
        this.currentColor = color;
        this.updateColorChooser(ChangeMethod.COLOR_CHANGE);
    }

    /**
     * Unregister a listener
     *
     * @param colorChooserListener Listener to unregister
     */
    public void unregisterColorChooserListener(final ColorChooserListener colorChooserListener)
    {
        synchronized (this.colorChooserListeners)
        {
            this.colorChooserListeners.remove(colorChooserListener);
        }
    }
}
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
package jhelp.gui.twoD;

import jhelp.util.gui.JHelpImage;

/**
 * Component for choose a color
 *
 * @author JHelp
 */
public class JHelpColorChooser
        extends JHelpPanel2D
        implements JHelpSpinner2DListener<Integer>
{
    /**
     * Spinner blue part ID
     */
    private static final int ID_SPINNER_BLUE  = JHelpConstants2D.ID_COLOR_CHOOSER_BLUE;
    /**
     * Spinner green part ID
     */
    private static final int ID_SPINNER_GREEN = JHelpConstants2D.ID_COLOR_CHOOSER_GREEN;
    /**
     * Spinner red part ID
     */
    private static final int ID_SPINNER_RED   = JHelpConstants2D.ID_COLOR_CHOOSER_RED;
    /**
     * Pressed time betewwen repetition
     */
    private static final int REPEAT_TIME      = 210;
    /**
     * Actual color
     */
    private       int                       color;
    /**
     * Listener of color changed
     */
    private       JHelpColorChooserListener colorChooserListener;
    /**
     * Image shows the selected color
     */
    private final JHelpImage                imageColor;

    /**
     * Create a new instance of JHelpColorChooser
     */
    public JHelpColorChooser()
    {
        this(0xFF000000);
    }

    /**
     * Create a new instance of JHelpColorChooser
     *
     * @param color Starting color
     */
    public JHelpColorChooser(final int color)
    {
        super(new JHelpTableLayout());

        this.color = color;

        this.imageColor = new JHelpImage(128, 64, color);
        this.addComponent2D(new JHelpLabelImage2D(this.imageColor),
                            new JHelpTableLayout.JHelpTableLayoutConstraints(0, 0, 3, 1));

        JHelpSpinner2D<Integer> spinner2d = new JHelpSpinner2D<Integer>(
                new JHelpSpinnerModelInteger(0, 255, (color >> 16) & 0xFF));
        spinner2d.id(JHelpColorChooser.ID_SPINNER_RED);
        spinner2d.setReapeatDelay(JHelpColorChooser.REPEAT_TIME);
        spinner2d.setSpinner2dListener(this);
        this.addComponent2D(spinner2d, new JHelpTableLayout.JHelpTableLayoutConstraints(0, 1));

        spinner2d = new JHelpSpinner2D<Integer>(new JHelpSpinnerModelInteger(0, 255, (color >> 8) & 0xFF));
        spinner2d.id(JHelpColorChooser.ID_SPINNER_GREEN);
        spinner2d.setReapeatDelay(JHelpColorChooser.REPEAT_TIME);
        spinner2d.setSpinner2dListener(this);
        this.addComponent2D(spinner2d, new JHelpTableLayout.JHelpTableLayoutConstraints(1, 1));

        spinner2d = new JHelpSpinner2D<Integer>(new JHelpSpinnerModelInteger(0, 255, color & 0xFF));
        spinner2d.id(JHelpColorChooser.ID_SPINNER_BLUE);
        spinner2d.setReapeatDelay(JHelpColorChooser.REPEAT_TIME);
        spinner2d.setSpinner2dListener(this);
        this.addComponent2D(spinner2d, new JHelpTableLayout.JHelpTableLayoutConstraints(2, 1));
    }

    /**
     * Actual color
     *
     * @return Actual color
     */
    public int color()
    {
        return this.color;
    }

    /**
     * Change the current color
     *
     * @param color New current color
     */
    @SuppressWarnings("unchecked")
    public void color(final int color)
    {
        this.color = color;

        ((JHelpSpinner2D<Integer>) UtilTwoD.findComponentByID(this, JHelpColorChooser.ID_SPINNER_RED)).setValue(
                (color >> 16) & 0xFF, false);
        ((JHelpSpinner2D<Integer>) UtilTwoD.findComponentByID(this, JHelpColorChooser.ID_SPINNER_GREEN)).setValue(
                (color >> 8) & 0xFF, false);
        ((JHelpSpinner2D<Integer>) UtilTwoD.findComponentByID(this, JHelpColorChooser.ID_SPINNER_BLUE)).setValue(
                color & 0xFF, false);

        this.imageColor.startDrawMode();
        this.imageColor.clear(this.color);
        this.imageColor.endDrawMode();

        if (this.colorChooserListener != null)
        {
            this.colorChooserListener.colorChanged(this, this.color);
        }
    }

    /**
     * Listener of color changed
     *
     * @return Listener of color changed
     */
    public JHelpColorChooserListener colorChooserListener()
    {
        return this.colorChooserListener;
    }

    /**
     * Defines the color changed listener
     *
     * @param colorChooserListener New color changed listener of {@code null} for no listener
     */
    public void colorChooserListener(final JHelpColorChooserListener colorChooserListener)
    {
        this.colorChooserListener = colorChooserListener;
    }

    /**
     * Called when one off red, green or blue spinner changed <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param spinner2d Spinner that changed
     * @param newValue  New spinner value
     * @see jhelp.gui.twoD.JHelpSpinner2DListener#spinnerValueChanged(JHelpSpinner2D, Object)
     */
    @Override
    public void spinnerValueChanged(final JHelpSpinner2D<Integer> spinner2d, final Integer newValue)
    {
        switch (spinner2d.id())
        {
            case JHelpColorChooser.ID_SPINNER_RED:
                this.color = (0xFF00FFFF & this.color) | ((newValue & 0xFF) << 16);
                break;
            case JHelpColorChooser.ID_SPINNER_GREEN:
                this.color = (0xFFFF00FF & this.color) | ((newValue & 0xFF) << 8);
                break;
            case JHelpColorChooser.ID_SPINNER_BLUE:
                this.color = (0xFFFFFF00 & this.color) | (newValue & 0xFF);
                break;
        }

        this.imageColor.startDrawMode();
        this.imageColor.clear(this.color);
        this.imageColor.endDrawMode();

        if (this.colorChooserListener != null)
        {
            this.colorChooserListener.colorChanged(this, this.color);
        }
    }
}
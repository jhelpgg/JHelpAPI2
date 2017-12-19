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
package jhelp.gui;

import java.awt.BorderLayout;
import jhelp.gui.event.ColorChooserListener;

/**
 * Frame for show color chooser
 *
 * @author JHelp
 */
public class ColorChooserFrame
        extends JHelpFrame
{
    /**
     * Singleton instance of color chooser frame
     */
    public static final ColorChooserFrame COLOR_CHOOSER_FRAME = new ColorChooserFrame();

    /**
     * Event manager
     *
     * @author JHelp
     */
    class EventManager
            implements ColorChooserListener
    {
        /**
         * Create a new instance of EventManager
         */
        EventManager()
        {
        }

        /**
         * Called when color chooser validate <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param colorChooser Color chooser source
         * @param color        Chosen color
         * @see ColorChooserListener#colorChoose(ColorChooser, int)
         */
        @Override
        public void colorChoose(final ColorChooser colorChooser, final int color)
        {
            ColorChooserFrame.this.doColorChoosed(colorChooser, color);
        }

        /**
         * Called when color chooser cancel <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param colorChooser Color chooser source
         * @see ColorChooserListener#colorChooseCanceled(ColorChooser)
         */
        @Override
        public void colorChooseCanceled(final ColorChooser colorChooser)
        {
            ColorChooserFrame.this.doColorChooseCanceled(colorChooser);
        }
    }

    /**
     * Indicates if we have to do cancel action (To avoid infinite loop)
     */
    private boolean              closeCancel;
    /**
     * Indicates if we have already do close action (To avoid infinite loop)
     */
    private boolean              closeEnter;
    /**
     * Embed color chooser
     */
    private ColorChooser         colorChooser;
    /**
     * Color chooser listener
     */
    private ColorChooserListener colorChooserListener;

    /**
     * Create a new instance of ColorChooserFrame
     */
    private ColorChooserFrame()
    {
        super();

        this.closeEnter = false;
        this.closeCancel = true;
        this.setAlwaysOnTop(true);
        this.setDisposeOnClose(false);
        this.setExitAllOnClose(false);
    }

    /**
     * Called when color chooser cancel
     *
     * @param colorChooser Color chooser source
     */
    void doColorChooseCanceled(final ColorChooser colorChooser)
    {
        if (!this.closeCancel)
        {
            return;
        }

        this.closeCancel = false;

        if (this.colorChooserListener != null)
        {
            this.colorChooserListener.colorChooseCanceled(colorChooser);
        }

        if (!this.closeEnter)
        {
            this.closeFrame();
        }
    }

    /**
     * Called when color choose
     *
     * @param colorChooser Color chooser source
     * @param color        Color choose
     */
    void doColorChoosed(final ColorChooser colorChooser, final int color)
    {
        this.closeCancel = false;

        if (this.colorChooserListener != null)
        {
            this.colorChooserListener.colorChoose(colorChooser, color);
        }

        this.closeFrame();
    }

    /**
     * Add UI listeners <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see JHelpFrame#addListeners()
     */
    @Override
    protected void addListeners()
    {
        final EventManager eventManager = new EventManager();
        this.colorChooser.registerColorChooserListener(eventManager);
    }

    /**
     * Called when frame is about to be closed <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return {@code true} to let the frame close
     * @see JHelpFrame#canCloseNow()
     */
    @Override
    protected boolean canCloseNow()
    {
        this.closeEnter = true;

        if (this.closeCancel)
        {
            this.doColorChooseCanceled(this.colorChooser);
        }

        return true;
    }

    /**
     * Create UI components <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see JHelpFrame#createComponents()
     */
    @Override
    protected void createComponents()
    {
        this.colorChooser = new ColorChooser();
    }

    /**
     * Layout UI components <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see JHelpFrame#layoutComponents()
     */
    @Override
    protected void layoutComponents()
    {
        this.setLayout(new BorderLayout());
        this.add(this.colorChooser, BorderLayout.CENTER);
    }

    /**
     * Launch properly the color chooser frame
     *
     * @param colorStart           Start color
     * @param colorChooserListener Color chooser listener to know when color choose or cancel
     */
    public void chooseColor(final int colorStart, final ColorChooserListener colorChooserListener)
    {
        this.colorChooser.startColor(colorStart);
        this.closeEnter = false;
        this.closeCancel = true;
        this.colorChooserListener = colorChooserListener;
        this.setVisible(true);
    }

    /**
     * Current color
     *
     * @return Current color
     */
    public int currentColor()
    {
        return this.colorChooser.currentColor();
    }
}
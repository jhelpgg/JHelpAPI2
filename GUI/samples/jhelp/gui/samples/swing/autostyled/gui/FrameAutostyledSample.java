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
package jhelp.gui.samples.swing.autostyled.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import jhelp.gui.JHelpAutoStyledTextArea;
import jhelp.gui.JHelpFrame;

/**
 * Frame that contains the auto styled text
 *
 * @author JHelp
 */
public class FrameAutostyledSample
        extends JHelpFrame
{
    /** Auto styled text component */
    private JHelpAutoStyledTextArea autoStyledTextArea;

    /**
     * Create a new instance of FrameAutostyledSample
     */
    public FrameAutostyledSample()
    {
        super("Text auto styled sample");
    }

    /**
     * Add listeners to components <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see jhelp.gui.JHelpFrame#addListeners()
     */
    @Override
    protected void addListeners()
    {
    }

    /**
     * Create and initialize frame internal components <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see jhelp.gui.JHelpFrame#createComponents()
     */
    @Override
    protected void createComponents()
    {
        this.autoStyledTextArea = new JHelpAutoStyledTextArea();

        // Default style
        this.autoStyledTextArea.createStyle("Default", "Arial", 14, false, false, false, Color.BLACK, Color.WHITE);
        this.autoStyledTextArea.defaultStyle("Default");

        // Symbol style
        this.autoStyledTextArea.createStyle("Symbol", "Arial", 14, true, false, false, Color.RED, Color.LIGHT_GRAY);
        this.autoStyledTextArea.symbolStyle("Symbol");

        // Word "hi" style : (Hello, hello, Hi, hi, bye, Bye)
        this.autoStyledTextArea.createStyle("Hi", "Arial", 14, true, false, true, Color.BLUE, Color.WHITE);
        this.autoStyledTextArea.associate("Hi", "Hello", "hello", "Hi", "hi", "bye", "Bye");

        // Wolrd "world" style : ("World", "world")
        this.autoStyledTextArea.createStyle("World", "Arial", 16, true, true, false, Color.GREEN, Color.WHITE);
        this.autoStyledTextArea.associate("World", "World");
        this.autoStyledTextArea.associate("World", "world");

        // Initial text
        this.autoStyledTextArea.setText(
                "This is an \"Hello world !\" for the auto styled text component.\nNow bye and try type symbols like , ? ; or 'magic' word like world, hello, bye");
    }

    /**
     * Layout components inside the frame <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see jhelp.gui.JHelpFrame#layoutComponents()
     */
    @Override
    protected void layoutComponents()
    {
        this.setLayout(new BorderLayout());

        this.add(this.autoStyledTextArea, BorderLayout.CENTER);
    }
}
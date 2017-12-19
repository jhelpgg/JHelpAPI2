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

/**
 * Frame for editing an image area
 *
 * @author JHelp
 */
public class FrameEditArea
        extends JHelpFrame
{
    /**
     * Component that edit an image area
     */
    private EditImageArea editImageArea;
    /**
     * File chooser
     */
    private FileChooser   fileChooser;

    /**
     * Create a new instance of FrameEditArea
     */
    public FrameEditArea()
    {
        super("Edit area", false, true);
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
    }

    /***
     * Create frame components <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see JHelpFrame#createComponents()
     */
    @Override
    protected void createComponents()
    {
        this.fileChooser = new FileChooser(this);
        this.editImageArea = new EditImageArea(this.fileChooser);
    }

    /**
     * Layout frame components <br>
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
        this.add(this.editImageArea, BorderLayout.CENTER);
    }
}
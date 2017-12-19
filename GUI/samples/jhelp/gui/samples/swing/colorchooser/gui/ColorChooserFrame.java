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
package jhelp.gui.samples.swing.colorchooser.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;

import jhelp.gui.ButtonChooseColor;
import jhelp.gui.ColorChooser;
import jhelp.gui.JHelpFrame;
import jhelp.gui.action.GenericAction;
import jhelp.util.gui.ClipBoardManager;
import jhelp.util.text.UtilText;

/**
 * Frame for color chooser
 *
 * @author JHelp
 */
public class ColorChooserFrame
        extends JHelpFrame
{
    /**
     * Action that copies the color to the clipboard
     *
     * @author JHelp
     */
    class CopyColorAction
            extends GenericAction
    {
        /**
         * Create a new instance of CopyColorAction
         */
        public CopyColorAction()
        {
            super("Copy color");
        }

        /**
         * Copy selected color to clip board <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param actionEvent
         *           Action description
         * @see jhelp.gui.action.GenericAction#doActionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        protected void doActionPerformed(final ActionEvent actionEvent)
        {
            ClipBoardManager.CLIP_BOARD.storeString(
                    UtilText.colorText(ColorChooserFrame.this.buttonChooseColor.color().getRGB()).toUpperCase());
        }
    }

    /** Button for launch the copy of selected color to clip board */
    private JButton         buttonCopyColor;
    /** Action to copy color to clip board */
    private CopyColorAction copyColorAction;
    /** Button for choose a color */
    ButtonChooseColor buttonChooseColor;

    /**
     * Create a new instance of ColorChooserFrame
     */
    public ColorChooserFrame()
    {
        super("Color chooser");
    }

    /**
     * Add listeners <br>
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
     * Create UI components <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see jhelp.gui.JHelpFrame#createComponents()
     */
    @Override
    protected void createComponents()
    {
        this.copyColorAction = new CopyColorAction();

        this.buttonChooseColor = new ButtonChooseColor();
        this.buttonCopyColor = new JButton(this.copyColorAction);
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
        this.add(new ColorChooser());
        this.add(this.buttonChooseColor, BorderLayout.CENTER);
        this.add(this.buttonCopyColor, BorderLayout.SOUTH);
    }
}
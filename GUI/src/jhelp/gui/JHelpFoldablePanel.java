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
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JPanel;

import jhelp.gui.smooth.JHelpConstantsSmooth;
import jhelp.util.gui.JHelpFont;

/**
 * Panel that can fold a component, it add a clickable bar that user use for hide/show carry component.
 *
 * @author JHelp <br>
 */
public final class JHelpFoldablePanel
        extends JPanel
{
    /**
     * Event manager of mouse events on bar
     */
    class EventManager
            implements MouseListener
    {
        /**
         * Create a new instance of EventManager
         */
        EventManager()
        {
        }

        /**
         * Called when mouse clicked => Change the fold status <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Event description
         * @see MouseListener#mouseClicked(MouseEvent)
         */
        @Override
        public void mouseClicked(final MouseEvent e)
        {
            JHelpFoldablePanel.this.toggleFold();
        }

        /**
         * Called when mouse pressed <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Event description
         * @see MouseListener#mousePressed(MouseEvent)
         */
        @Override
        public void mousePressed(final MouseEvent e)
        {
            // Nothing to do
        }

        /**
         * Called when wouse released <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Event description
         * @see MouseListener#mouseReleased(MouseEvent)
         */
        @Override
        public void mouseReleased(final MouseEvent e)
        {
            // Nothing to do
        }

        /**
         * Called when mouse enter in the bar => Mouse cursor becomes a hand <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Event description
         * @see MouseListener#mouseEntered(MouseEvent)
         */
        @Override
        public void mouseEntered(final MouseEvent e)
        {
            JHelpFoldablePanel.this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        /**
         * Called when mouse exit the bar => Mouse cursor returns to normal <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Event description
         * @see MouseListener#mouseExited(MouseEvent)
         */
        @Override
        public void mouseExited(final MouseEvent e)
        {
            JHelpFoldablePanel.this.setCursor(Cursor.getDefaultCursor());
        }
    }

    /**
     * Event manager
     */
    private final EventManager eventManager;
    /**
     * Component to hide/show
     */
    private final JComponent foldComponent;
    /**
     * Fold bar location
     */
    private final FoldLocation foldLocation;
    /**
     * Bar to click for fold/unfold
     */
    private final JHelpFoldingComponent foldingBarComponent;

    /**
     * Create a new instance of JHelpFoldablePanel
     *
     * @param title         Text on fold bar
     * @param foldComponent Component to hide/show
     * @param foldLocation  Fold bar location
     */
    public JHelpFoldablePanel(final String title, final JComponent foldComponent, final FoldLocation foldLocation)
    {
        this(title, foldComponent, foldLocation, JHelpConstantsSmooth.FONT_BODY_1);
    }

    /**
     * Create a new instance of JHelpFoldablePanel
     *
     * @param title         Text on fold bar
     * @param foldComponent Component to hide/show
     * @param foldLocation  Fold bar location
     * @param font          Font used by bar
     */
    public JHelpFoldablePanel(
            String title, final JComponent foldComponent, final FoldLocation foldLocation, final JHelpFont font)
    {
        super(new BorderLayout());

        if (foldComponent == null)
        {
            throw new NullPointerException("foldComponent mustn't be null");
        }

        if (foldLocation == null)
        {
            throw new NullPointerException("foldLocation mustn't be null");
        }

        this.eventManager = new EventManager();
        this.foldComponent = foldComponent;
        this.foldLocation = foldLocation;

        if ((title == null) || (title.length() == 0))
        {
            title = "*";
        }

        this.foldingBarComponent = new JHelpFoldingComponent(this.foldLocation, title, font, false);
        this.foldingBarComponent.setBackground(new Color(0xCAFEFACE, false));
        this.foldingBarComponent.addMouseListener(this.eventManager);

        switch (foldLocation)
        {
            case BOTTOM:
                this.add(this.foldComponent, BorderLayout.NORTH);
                this.add(this.foldingBarComponent, BorderLayout.SOUTH);
                break;
            case LEFT:
                this.add(this.foldComponent, BorderLayout.EAST);
                this.add(this.foldingBarComponent, BorderLayout.WEST);
                break;
            case RIGHT:
                this.add(this.foldComponent, BorderLayout.WEST);
                this.add(this.foldingBarComponent, BorderLayout.EAST);
                break;
            case TOP:
                this.add(this.foldComponent, BorderLayout.SOUTH);
                this.add(this.foldingBarComponent, BorderLayout.NORTH);
                break;
        }
    }

    /**
     * Fold the component
     */
    public void fold()
    {
        this.foldComponent.setVisible(false);
        this.foldingBarComponent.setFold(true);
    }

    /**
     * Indicates if component is fold
     *
     * @return <code>true</code if component is fold
     */
    public boolean isFold()
    {
        return !this.foldComponent.isVisible();
    }

    /**
     * Change the bar background
     *
     * @param background New bar background
     */
    public void setFoldBackground(final Color background)
    {
        this.foldingBarComponent.setBackground(background);
    }

    /**
     * Change the base font
     *
     * @param font New bar font
     */
    public void setFoldFont(final Font font)
    {
        this.foldingBarComponent.setFont(font);
    }

    /**
     * change the bar font
     *
     * @param font New bar font
     */
    public void setFoldFont(final JHelpFont font)
    {
        if (font == null)
        {
            throw new NullPointerException("font mustn't be null");
        }

        this.foldingBarComponent.setFont(font);
    }

    /**
     * Change bar foreground
     *
     * @param foreground New bar foreground
     */
    public void setFoldForeground(final Color foreground)
    {
        this.foldingBarComponent.setForeground(foreground);
    }

    /**
     * Toggle the fold status<br>
     * That is to say, if fold, it will be unfold OR if unfold it will be fold
     */
    public void toggleFold()
    {
        this.foldComponent.setVisible(!this.foldComponent.isVisible());
        this.foldingBarComponent.setFold(!this.foldComponent.isVisible());
    }

    /**
     * Unfold the component
     */
    public void unFold()
    {
        this.foldComponent.setVisible(true);
        this.foldingBarComponent.setFold(false);
    }
}
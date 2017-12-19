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

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.UtilGUI;

/**
 * Frame based on unique "full screen" image
 *
 * @author JHelp
 */
public class JHelpFrameImage
        extends JHelpFrame
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 3717150952548300651L;
    /**
     * Main component that carry the unique image
     */
    private ComponentJHelpImage componentJHelpImage;

    /**
     * Create a new instance of JHelpFrameImage
     */
    public JHelpFrameImage()
    {
        super(true);
    }

    /**
     * Create a new instance of JHelpFrameImage
     *
     * @param title Frame title
     */
    public JHelpFrameImage(final String title)
    {
        super(title, true);
    }

    /**
     * Create a new instance of JHelpFrameImage
     *
     * @param title     Frame title
     * @param resizable Indicates if frame can be resize
     */
    public JHelpFrameImage(final String title, final boolean resizable)
    {
        super(title, true, resizable);
    }

    /**
     * Remove key listener to embed image
     *
     * @param keyListener Listener to remove
     */
    void componentRemoveKeyListener(final KeyListener keyListener)
    {
        this.componentJHelpImage.removeKeyListener(keyListener);
    }

    /**
     * Remove mouse listener to embed image
     *
     * @param mouseListener Listener to remove
     */
    void componentRemoveMouseListener(final MouseListener mouseListener)
    {
        this.componentJHelpImage.removeMouseListener(mouseListener);
    }

    /**
     * Remove mouse motion listener to embed image
     *
     * @param mouseMotionListener Listener to remove
     */
    void componentRemoveMouseMotionListener(final MouseMotionListener mouseMotionListener)
    {
        this.componentJHelpImage.removeMouseMotionListener(mouseMotionListener);
    }

    /**
     * Add listeners <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see JHelpFrame#addListeners()
     */
    @Override
    protected final void addListeners()
    {
    }

    /**
     * Create inside components <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see JHelpFrame#createComponents()
     */
    @Override
    protected final void createComponents()
    {
        final Rectangle bounds = UtilGUI.getScreenBounds(0);

        this.componentJHelpImage = new ComponentJHelpImage(bounds.width, bounds.height);
    }

    /**
     * Layout components <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see JHelpFrame#layoutComponents()
     */
    @Override
    protected final void layoutComponents()
    {
        this.setLayout(null);

        this.add(this.componentJHelpImage);
    }

    /**
     * Add key listener to embed image
     *
     * @param keyListener Listener to add
     */
    protected void componentAddKeyListener(final KeyListener keyListener)
    {
        this.componentJHelpImage.setFocusable(true);
        this.componentJHelpImage.requestFocusInWindow();

        this.componentJHelpImage.addKeyListener(keyListener);
    }

    /**
     * Add mouse listener to embed image
     *
     * @param mouseListener Listener to add
     */
    protected void componentAddMouseListener(final MouseListener mouseListener)
    {
        this.componentJHelpImage.addMouseListener(mouseListener);
    }

    /**
     * Add mouse motion listener to embed image
     *
     * @param mouseMotionListener Listener to add
     */
    protected void componentAddMouseMotionListener(final MouseMotionListener mouseMotionListener)
    {
        this.componentJHelpImage.addMouseMotionListener(mouseMotionListener);
    }

    /**
     * Add a mouse wheel listener
     *
     * @param mouseWheelListener Listener to add
     */
    protected void componentAddMouseWheelListener(final MouseWheelListener mouseWheelListener)
    {
        this.componentJHelpImage.addMouseWheelListener(mouseWheelListener);
    }

    /**
     * Called each time a component is modified on size and/or position and/or visiblity <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param componentEvent Component event description
     * @see Component#processComponentEvent(ComponentEvent)
     */
    @Override
    protected void processComponentEvent(final ComponentEvent componentEvent)
    {
        switch (componentEvent.getID())
        {
            case ComponentEvent.COMPONENT_RESIZED:
                final Component component = this.rootPane;
                final Rectangle bounds = component != null
                                         ? component.getBounds()
                                         : null;
                if ((bounds != null) && (bounds.width > 0) && (bounds.height > 0) && (this.componentJHelpImage != null))
                {
                    this.componentJHelpImage.setBounds(0, 0, bounds.width, bounds.height);
                    this.updateSize();
                }
                break;
        }

        super.processComponentEvent(componentEvent);
    }

    /**
     * Update frame content size
     */
    protected void updateSize()
    {
        this.componentJHelpImage.updateSize();
    }

    /**
     * The embed image
     *
     * @return The embed image
     */
    public final JHelpImage getImage()
    {
        return this.componentJHelpImage.image();
    }

    /**
     * Image over the frame
     *
     * @return Image over the frame
     */
    public JHelpImage getImageOver()
    {
        return this.componentJHelpImage.imageOver();
    }

    /**
     * Indicates if image over active
     *
     * @return {@code true} if image over active
     */
    public boolean isImageOverActive()
    {
        return this.componentJHelpImage.imageOverActive();
    }

    /**
     * Change image over active state
     *
     * @param active New image over active state
     */
    public void setImageOverActive(final boolean active)
    {
        this.componentJHelpImage.imageOverActive(active);
    }

    /**
     * Force refresh/update the embed image
     */
    public final void refresh()
    {
        this.componentJHelpImage.image().update();
        this.invalidate();
        this.validate();
        this.repaint();
    }

    /**
     * Called when frame size is defined <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param x      X position
     * @param y      Y position
     * @param width  Width
     * @param height Height
     * @see java.awt.Window#setBounds(int, int, int, int)
     */
    @Override
    public void setBounds(final int x, final int y, final int width, final int height)
    {
        if ((width > 0) && (height > 0) && (this.componentJHelpImage != null))
        {
            this.componentJHelpImage.setBounds(0, 0, width, height);
            this.updateSize();
        }

        super.setBounds(x, y, width, height);
    }
}
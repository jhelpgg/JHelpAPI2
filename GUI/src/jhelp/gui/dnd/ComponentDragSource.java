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

package jhelp.gui.dnd;

import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JFrame;
import jhelp.gui.HaveHeader;

/**
 * Drag source generic for a component <br>
 * <br>
 * Last modification : 2 fevr. 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public class ComponentDragSource
        extends DragSource
        implements ComponentListener, PropertyChangeListener
{
    /**
     * Buffered image computed from the component
     */
    private       BufferedImage bufferedImage;
    /**
     * Component link in drag source
     */
    private final JComponent    component;
    /**
     * Information carry by the drag source
     */
    private       Object        information;

    /**
     * Constructs ComponentDragSource
     *
     * @param frame       Frame parent. Its better to use the main Frame of your application and please don't make {@code null} else you
     *                    can have bad effects
     * @param component   Component link to the drag source
     * @param information Information carry
     */
    public ComponentDragSource(final JFrame frame, final JComponent component, final Object information)
    {
        super(frame);
        if (component == null)
        {
            throw new NullPointerException("component mustn't be null");
        }
        if (information == null)
        {
            throw new NullPointerException("information mustn't be null");
        }

        this.component = component;
        this.component.addComponentListener(this);
        this.component.addPropertyChangeListener(this);
        this.information = information;

        this.updateBufferedImage();

        this.initialize();
    }

    /**
     * Update the buffered image
     */
    private void updateBufferedImage()
    {
        this.bufferedImage = null;

        final int width  = Math.max(1, this.component.getWidth());
        final int height = Math.max(1, this.component.getHeight());

        this.bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics2D = this.bufferedImage.createGraphics();
        this.component.paintAll(graphics2D);
        this.bufferedImage.flush();
        graphics2D.dispose();
    }

    /**
     * Call when drag is done
     *
     * @see DragSource#dragDone()
     */
    @Override
    protected void dragDone()
    {
    }

    /**
     * Call when drag failed
     *
     * @see DragSource#dragFailed()
     */
    @Override
    protected void dragFailed()
    {
    }

    /**
     * Call just before the drag start
     *
     * @param x Mouse X in the component
     * @param y Mouse Y in the component
     * @see DragSource#dragWillStart(int, int)
     */
    @Override
    protected void dragWillStart(final int x, final int y)
    {
    }

    /**
     * Component who detects the drag
     *
     * @return Component who detects the drag
     * @see DragSource#obtainDragComponent()
     */
    @Override
    public JComponent obtainDragComponent()
    {
        return this.component;
    }

    /**
     * Drag image
     *
     * @return Drag image
     * @see DragSource#obtainDragImage()
     */
    @Override
    public BufferedImage obtainDragImage()
    {
        return this.bufferedImage;
    }

    /**
     * Information carry
     *
     * @return Information carry
     * @see DragSource#obtainInformationValue()
     */
    @Override
    public Object obtainInformationValue()
    {
        return this.information;
    }

    /**
     * Call when linked component is resized
     *
     * @param e Event description
     * @see ComponentListener#componentResized(ComponentEvent)
     */
    @Override
    public void componentResized(final ComponentEvent e)
    {
        this.updateBufferedImage();
    }

    /**
     * Call when linked component is moved
     *
     * @param e Event description
     * @see ComponentListener#componentMoved(ComponentEvent)
     */
    @Override
    public void componentMoved(final ComponentEvent e)
    {
    }

    /**
     * Call when linked component is shown
     *
     * @param e Event description
     * @see ComponentListener#componentShown(ComponentEvent)
     */
    @Override
    public void componentShown(final ComponentEvent e)
    {
        this.updateBufferedImage();
    }

    /**
     * Call when linked component is hidden
     *
     * @param e Event description
     * @see ComponentListener#componentHidden(ComponentEvent)
     */
    @Override
    public void componentHidden(final ComponentEvent e)
    {
    }

    /**
     * Component we hang for move
     *
     * @return Component we hang for move
     * @see HaveHeader#obtainTheComponentForMove()
     */
    public JComponent obtainTheComponentForMove()
    {
        return this.component;
    }

    /**
     * Call when property change for the linked component
     *
     * @param evt Event description
     * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
     */
    @Override
    public void propertyChange(final PropertyChangeEvent evt)
    {
        this.updateBufferedImage();
    }

    /**
     * Update information carry
     *
     * @param information New information
     */
    public void updateInformation(final Object information)
    {
        if (information == null)
        {
            throw new NullPointerException("information mustn't be null");
        }

        this.information = information;
    }
}
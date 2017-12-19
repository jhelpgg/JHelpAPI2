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

/*
 * License :
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may cause.
 * You can use, modify, the code as your need for any usage.
 * But you can't do any action that avoid me or other person use, modify this code.
 * The code is free for usage and modification, you can't change that fact.
 * JHelp
 */

package jhelp.gui.twoD;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import jhelp.gui.JHelpMouseListener;

/**
 * Give a button behavior to a 2D component
 *
 * @author JHelp
 */
public class JHelpButtonBehavior
        implements JHelpMouseListener
{
    /**
     * Give to a component the button behavior
     *
     * @param identifier     Behavior identifier
     * @param component2d    Component to add the behavior
     * @param actionListener Listener to call if component is pressed
     * @return The behavior instance
     */
    public static JHelpButtonBehavior giveButtonBehavior(
            final int identifier, final JHelpComponent2D component2d,
            final JHelpActionListener actionListener)
    {
        return new JHelpButtonBehavior(identifier, component2d, actionListener);
    }

    /**
     * Listener to call if component is pressed
     */
    private final JHelpActionListener actionListener;
    /**
     * Component with the added behavior
     */
    private final JHelpComponent2D    component2d;
    /**
     * Identifier of the behavior
     */
    private final int                 identifier;

    /**
     * Create a new instance of JHelpButtonBehavior
     *
     * @param identifier     Behavior identifier
     * @param component2d    Component to add the behavior
     * @param actionListener Listener to call if component id pressed
     */
    private JHelpButtonBehavior(
            final int identifier, final JHelpComponent2D component2d, final JHelpActionListener actionListener)
    {
        if (component2d == null)
        {
            throw new NullPointerException("component2d mustn't be null");
        }

        if (actionListener == null)
        {
            throw new NullPointerException("actionListener mustn't be null");
        }

        this.identifier = identifier;
        this.component2d = component2d;
        this.actionListener = actionListener;

        this.component2d.mouseListener(this);
    }

    /**
     * Called when mouse clicked <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param e Mouse event description
     * @see java.awt.event.MouseListener#mouseClicked(MouseEvent)
     */
    @Override
    public void mouseClicked(final MouseEvent e)
    {
        this.actionListener.actionAppend(this.component2d, this.identifier);
    }

    /**
     * Called when mouse moved <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param e Mouse event description
     * @see java.awt.event.MouseListener#mousePressed(MouseEvent)
     */
    @Override
    public void mousePressed(final MouseEvent e)
    {
    }

    /**
     * Called when mouse released <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param e Mouse event description
     * @see java.awt.event.MouseListener#mouseReleased(MouseEvent)
     */
    @Override
    public void mouseReleased(final MouseEvent e)
    {
    }

    /**
     * Called when mouse entered <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param e Mouse event description
     * @see java.awt.event.MouseListener#mouseEntered(MouseEvent)
     */
    @Override
    public void mouseEntered(final MouseEvent e)
    {
    }

    /**
     * Called when mouse exited <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param e Mouse event description
     * @see java.awt.event.MouseListener#mouseExited(MouseEvent)
     */
    @Override
    public void mouseExited(final MouseEvent e)
    {
    }

    /**
     * Called when mouse dragged <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param e Mouse event description
     * @see java.awt.event.MouseMotionListener#mouseDragged(MouseEvent)
     */
    @Override
    public void mouseDragged(final MouseEvent e)
    {
    }

    /**
     * Called when mouse moved <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param e Mouse event description
     * @see java.awt.event.MouseMotionListener#mouseMoved(MouseEvent)
     */
    @Override
    public void mouseMoved(final MouseEvent e)
    {
    }

    /**
     * Called when mouse wheel moved <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param e Mouse event description
     * @see java.awt.event.MouseWheelListener#mouseWheelMoved(MouseWheelEvent)
     */
    @Override
    public void mouseWheelMoved(final MouseWheelEvent e)
    {
    }
}
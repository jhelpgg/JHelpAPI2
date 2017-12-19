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

import java.awt.event.MouseEvent;
import java.util.Stack;

/**
 * 2D utilities
 *
 * @author JHelp
 */
public class UtilTwoD
{
    /**
     * Obtain component 2D embed in mouse event
     *
     * @param mouseEvent Mouse event that contains the component
     * @return Component extract or {@code null} if mouse event dosen't contains a component
     */
    public static JHelpComponent2D component2DFromMouseEvent(final MouseEvent mouseEvent)
    {
        final Object source = mouseEvent.getSource();

        if (source == null)
        {
            return null;
        }

        if (source instanceof JHelpComponent2D)
        {
            return (JHelpComponent2D) source;
        }

        return null;
    }

    /**
     * Seach a compoent by its developer id inside a dialog
     *
     * @param dialog2d Dialog where search the component
     * @param id       Component id
     * @return Found component or {@code null}
     */
    public static JHelpComponent2D findComponentByID(final JHelpDialog2D dialog2d, final int id)
    {
        return UtilTwoD.findComponentByID(dialog2d.root(), id);
    }

    /**
     * Seach a compoent by its developer id inside a frame
     *
     * @param frame2d Frame where search the component
     * @param id      Component id
     * @return Found component or {@code null}
     */
    public static JHelpComponent2D findComponentByID(final JHelpFrame2D frame2d, final int id)
    {
        return UtilTwoD.findComponentByID(frame2d.getPanelRoot(), id);
    }

    /**
     * Seach a compoent by its developer id inside a parent component
     *
     * @param parent Parent where search the component
     * @param id     Component id
     * @return Found component or {@code null}
     */
    public static JHelpComponent2D findComponentByID(final JHelpComponent2D parent, final int id)
    {
        if (parent == null)
        {
            return null;
        }

        final Stack<JHelpComponent2D> stack = new Stack<JHelpComponent2D>();
        stack.push(parent);
        JHelpComponent2D component2d;

        while (!stack.isEmpty())
        {
            component2d = stack.pop();

            if (component2d.id() == id)
            {
                return component2d;
            }

            if (component2d instanceof JHelpContainer2D)
            {
                for (final JHelpComponent2D child : ((JHelpContainer2D) component2d).children())
                {
                    stack.push(child);
                }
            }
        }

        return null;
    }

    /**
     * Compute the component frame owner. If the component is inside a dialog, the frame owner of the dialog is returned
     *
     * @param component2d Component to get its frame owner
     * @return Frame owner or {@code null} if the component is not attach directly or indirectly to a frame or a dialog
     */
    public static JHelpFrame2D frameOwner(final JHelpComponent2D component2d)
    {
        final JHelpWindow2D window2d = component2d.obtainOwner();

        if (window2d == null)
        {
            return null;
        }

        if (window2d instanceof JHelpFrame2D)
        {
            return (JHelpFrame2D) window2d;
        }

        if (window2d instanceof JHelpDialog2D)
        {
            return ((JHelpDialog2D) window2d).owner();
        }

        throw new RuntimeException("Shouldn't arrive here ! Window class = " + window2d.getClass().getName());
    }
}
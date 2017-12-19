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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jhelp.gui.JHelpMouseListener;
import jhelp.util.gui.JHelpImage;
import jhelp.util.list.Pair;

/**
 * Container 2D that have only one child center on it
 *
 * @author JHelp
 */
public class JHelpCenterComponent
        extends JHelpContainer2D
{
    /**
     * The child
     */
    private final JHelpComponent2D component2d;

    /**
     * Create a new instance of JHelpCenterComponent
     *
     * @param component2d Component carry
     */
    public JHelpCenterComponent(final JHelpComponent2D component2d)
    {
        component2d.parent(this);

        this.component2d = component2d;
    }

    /**
     * Compute container preferred size <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param parentWidth  Parent width
     * @param parentHeight Parent height
     * @return Preferred size
     * @see JHelpComponent2D#computePreferredSize(int, int)
     */
    @Override
    protected Dimension computePreferredSize(final int parentWidth, final int parentHeight)
    {
        if (!this.component2d.visible())
        {
            return new Dimension();
        }

        return this.component2d.preferredSize(parentWidth, parentHeight);
    }

    /**
     * Get the component under the mouse <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param x Mouse X
     * @param y Mouse Y
     * @return Component and associated listener
     * @see JHelpComponent2D#mouseOver(int, int)
     */
    @Override
    protected Pair<JHelpComponent2D, JHelpMouseListener> mouseOver(final int x, final int y)
    {
        if (!this.visible())
        {
            return null;
        }

        return this.component2d.mouseOver(x, y);
    }

    /**
     * Draw the container <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param x      X
     * @param y      Y
     * @param parent Image where draw
     * @see JHelpComponent2D#paint(int, int, JHelpImage)
     */
    @Override
    protected void paint(final int x, final int y, final JHelpImage parent)
    {
        if (!this.component2d.visible())
        {
            return;
        }

        final Rectangle bounds     = this.bounds();
        final Dimension prefrerred = this.component2d.preferredSize(bounds.width, bounds.height);

        final int xx = (bounds.width - prefrerred.width) >> 1;
        final int yy = (bounds.height - prefrerred.height) >> 1;
        this.component2d.bounds(xx, yy, prefrerred.width, prefrerred.height);
        this.component2d.paintInternal(xx + x, yy + y, parent);
    }

    /**
     * Children list. Here only contain the unic child <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Children list
     * @see JHelpContainer2D#children()
     */
    @Override
    public List<JHelpComponent2D> children()
    {
        final ArrayList<JHelpComponent2D> children = new ArrayList<JHelpComponent2D>(1);
        children.add(this.component2d);

        return Collections.unmodifiableList(children);
    }
}
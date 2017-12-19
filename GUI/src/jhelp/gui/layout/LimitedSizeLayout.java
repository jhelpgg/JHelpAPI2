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
package jhelp.gui.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import jhelp.util.gui.UtilGUI;

/**
 * Layout for panel with one component.<br>
 * It can limit the child size
 *
 * @author JHelp <br>
 */
public class LimitedSizeLayout
        implements LayoutManager
{
    /**
     * Maximum height
     */
    private final int maximumHeight;
    /**
     * Maximum width
     */
    private final int maximumWidth;

    /**
     * Create a new instance of LimitedSizeLayout
     *
     * @param maximumWidth  Maximum width. Can use {@link Integer#MAX_VALUE} for no limit
     * @param maximumHeight Maximum height. Can use {@link Integer#MAX_VALUE} for no limit
     */
    public LimitedSizeLayout(final int maximumWidth, final int maximumHeight)
    {
        this.maximumWidth = maximumWidth;
        this.maximumHeight = maximumHeight;
    }

    /**
     * Add component <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param name Component name
     * @param comp Component to add
     * @see LayoutManager#addLayoutComponent(String, Component)
     */
    @Override
    public void addLayoutComponent(final String name, final Component comp)
    {
        // Nothing to do
    }

    /**
     * Remove a component <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param comp Component to remove
     * @see LayoutManager#removeLayoutComponent(Component)
     */
    @Override
    public void removeLayoutComponent(final Component comp)
    {
        // Nothing to do
    }

    /**
     * Compute preferred size of a container <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param parent Container
     * @return Preferred size
     * @see LayoutManager#preferredLayoutSize(Container)
     */
    @Override
    public Dimension preferredLayoutSize(final Container parent)
    {
        if ((parent.getComponentCount() == 0) || (!parent.getComponent(0)
                                                         .isVisible()))
        {
            return new Dimension(1, 1);
        }

        final Component component    = parent.getComponent(0);
        final Dimension preferedSize = UtilGUI.computePreferredDimension(component);
        preferedSize.width = Math.min(this.maximumWidth, preferedSize.width);
        preferedSize.height = Math.min(this.maximumHeight, preferedSize.height);
        return preferedSize;
    }

    /**
     * Compute minimum size of container <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param parent Container
     * @return Minium size
     * @see LayoutManager#minimumLayoutSize(Container)
     */
    @Override
    public Dimension minimumLayoutSize(final Container parent)
    {
        if ((parent.getComponentCount() == 0) || (!parent.getComponent(0)
                                                         .isVisible()))
        {
            return new Dimension(1, 1);
        }

        final Component component   = parent.getComponent(0);
        final Dimension minimumSize = UtilGUI.computeMinimumDimension(component);
        minimumSize.width = Math.min(this.maximumWidth, minimumSize.width);
        minimumSize.height = Math.min(this.maximumHeight, minimumSize.height);
        return minimumSize;
    }

    /**
     * Layout a container <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param parent Container to layout
     * @see LayoutManager#layoutContainer(Container)
     */
    @Override
    public void layoutContainer(final Container parent)
    {
        final Dimension parentSize = parent.getSize();
        final Insets    insets     = parent.getInsets();
        parentSize.width = parentSize.width - insets.right - insets.right;
        parentSize.height = parentSize.height - insets.top - insets.bottom;

        synchronized (parent.getTreeLock())
        {
            if (parent.getComponentCount() == 0)
            {
                return;
            }

            final Component component = parent.getComponent(0);

            if (!component.isVisible())
            {
                return;
            }

            component.setBounds(insets.left, insets.top, Math.min(this.maximumWidth, parentSize.width),
                                Math.min(this.maximumHeight, parentSize.height));
        }
    }
}
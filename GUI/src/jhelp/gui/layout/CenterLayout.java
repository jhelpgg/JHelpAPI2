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
import java.awt.LayoutManager;
import jhelp.util.gui.UtilGUI;

/**
 * Layout for center one component in container <br>
 * <br>
 * Last modification : 31 janv. 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public class CenterLayout
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
     * Constructs CenterLayout
     */
    public CenterLayout()
    {
        this(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Create a new instance of CenterLayout
     *
     * @param maximumWidth  Maximum width. Use {@link Integer#MAX_VALUE} for no limit
     * @param maximumHeight Maximum height. Use {@link Integer#MAX_VALUE} for no limit
     */
    public CenterLayout(final int maximumWidth, final int maximumHeight)
    {
        this.maximumWidth = Math.max(128, maximumWidth);
        this.maximumHeight = Math.max(128, maximumHeight);
    }

    /**
     * Add component to the layout.<br>
     * Do nothing here
     *
     * @param name Constraints description
     * @param comp Component to add
     * @see LayoutManager#addLayoutComponent(String, Component)
     */
    @Override
    public void addLayoutComponent(final String name, final Component comp)
    {
    }

    /**
     * Remove component for layout.<br>
     * Do nothing here
     *
     * @param comp Component to remove
     * @see LayoutManager#removeLayoutComponent(Component)
     */
    @Override
    public void removeLayoutComponent(final Component comp)
    {
    }

    /**
     * Compute preferred size
     *
     * @param parent Container who want's know it's preferred size
     * @return Preferred size
     * @see LayoutManager#preferredLayoutSize(Container)
     */
    @Override
    public Dimension preferredLayoutSize(final Container parent)
    {
        if (parent.getComponentCount() < 1)
        {
            return new Dimension(10, 10);
        }
        final Component component    = parent.getComponent(0);
        final Dimension preferedSize = UtilGUI.computePreferredDimension(component);
        preferedSize.width = Math.min(this.maximumWidth, preferedSize.width);
        preferedSize.height = Math.min(this.maximumHeight, preferedSize.height);
        return preferedSize;
    }

    /**
     * Compute minimum size
     *
     * @param parent Container who want's know it's minimum size
     * @return Minimum size
     * @see LayoutManager#minimumLayoutSize(Container)
     */
    @Override
    public Dimension minimumLayoutSize(final Container parent)
    {
        if (parent.getComponentCount() < 1)
        {
            return new Dimension(10, 10);
        }

        final Component component   = parent.getComponent(0);
        final Dimension minimumSize = UtilGUI.computeMinimumDimension(component);
        minimumSize.width = Math.min(this.maximumWidth, minimumSize.width);
        minimumSize.height = Math.min(this.maximumHeight, minimumSize.height);
        return minimumSize;
    }

    /**
     * Layout a container
     *
     * @param parent Container to layout
     * @see LayoutManager#layoutContainer(Container)
     */
    @Override
    public void layoutContainer(final Container parent)
    {
        if (parent.getComponentCount() < 1)
        {
            return;
        }

        final Dimension dim          = parent.getSize();
        final Component component    = parent.getComponent(0);
        final Dimension preferedSize = UtilGUI.computePreferredDimension(component);
        preferedSize.width = Math.min(this.maximumWidth, preferedSize.width);
        preferedSize.height = Math.min(this.maximumHeight, preferedSize.height);
        component.setBounds((dim.width - preferedSize.width) / 2, (dim.height - preferedSize.height) / 2,
                            preferedSize.width, preferedSize.height);
    }
}
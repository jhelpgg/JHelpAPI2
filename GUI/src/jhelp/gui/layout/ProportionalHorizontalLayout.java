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
 * Layout 2 components one next to other with a rate.<br>
 * The rate is a number in ]0, 1[ that is the rate of container with of first component. Second one will take the rest.<br>
 */
public class ProportionalHorizontalLayout
        implements LayoutManager
{
    /**
     * Proportion.
     **/
    private double proportion;

    /**
     * Create the layout.
     *
     * @param proportion Proportion : first component rate
     */
    public ProportionalHorizontalLayout(double proportion)
    {
        if (proportion <= 0 || proportion >= 1)
        {
            throw new IllegalArgumentException("The proportion MUST be in ]0, 1[, not " + proportion);
        }

        this.proportion = proportion;
    }

    /**
     * Add a component.<br>
     * Does nothing here.
     *
     * @param name      Constraints name
     * @param component Component to add
     * @see LayoutManager#addLayoutComponent(String, Component)
     */
    public void addLayoutComponent(String name, Component component)
    {
    }

    /**
     * Remove a component.<br>
     * Does nothing here.
     *
     * @param component Component to remove
     * @see LayoutManager#removeLayoutComponent(Component)
     */
    public void removeLayoutComponent(Component component)
    {
    }

    /**
     * Compute container preferred size.
     *
     * @param parent Container to compute its preferred size
     * @return Computed preferred size
     * @see LayoutManager#preferredLayoutSize(Container)
     */
    public Dimension preferredLayoutSize(Container parent)
    {
        int       width            = 0;
        int       height           = 0;
        final int numberOfChildren = parent.getComponentCount();

        if (numberOfChildren > 0)
        {
            final Dimension firstSize = UtilGUI.computePreferredDimension(parent.getComponent(0));

            if (numberOfChildren > 1)
            {
                final Dimension sercondSize = UtilGUI.computePreferredDimension(parent.getComponent(1));

                if (firstSize.width <= sercondSize.width)
                {
                    width = (int) (sercondSize.getWidth() / (1d - this.proportion));
                }
                else
                {
                    width = (int) (firstSize.getWidth() / this.proportion);
                }

                height = Math.max(firstSize.height, sercondSize.height);
            }
            else
            {
                width = firstSize.width;
                height = firstSize.height;
            }
        }

        Insets insets = parent.getInsets();
        width += insets.left + insets.right;
        height += insets.top + insets.bottom;
        return new Dimension(width, height);
    }

    /**
     * Compute container minimum size.
     *
     * @param parent Container to compute its minimum size
     * @return Computed minimum size
     * @see LayoutManager#minimumLayoutSize(Container)
     */
    public Dimension minimumLayoutSize(Container parent)
    {
        int       width            = 0;
        int       height           = 0;
        final int numberOfChildren = parent.getComponentCount();

        if (numberOfChildren > 0)
        {
            final Dimension firstSize = UtilGUI.computeMinimumDimension(parent.getComponent(0));

            if (numberOfChildren > 1)
            {
                final Dimension secondSize = UtilGUI.computePreferredDimension(parent.getComponent(1));

                if (firstSize.width <= secondSize.width)
                {
                    width = (int) (secondSize.getWidth() / (1d - this.proportion));
                }
                else
                {
                    width = (int) (firstSize.getWidth() / this.proportion);
                }

                height = Math.max(firstSize.height, secondSize.height);
            }
            else
            {
                width = firstSize.width;
                height = firstSize.height;
            }
        }

        Insets insets = parent.getInsets();
        width += insets.left + insets.right;
        height += insets.top + insets.bottom;
        return new Dimension(width, height);
    }

    /**
     * Layout components inside container.
     *
     * @param parent Container to layout
     * @see LayoutManager#layoutContainer(Container)
     */
    public void layoutContainer(Container parent)
    {
        synchronized (parent.getTreeLock())
        {
            final Dimension containerSize    = parent.getSize();
            final Insets    insets           = parent.getInsets();
            final int       width            = containerSize.width - insets.left - insets.right;
            final int       height           = containerSize.height - insets.top - insets.bottom;
            final int       x                = insets.left;
            final int       y                = insets.top;
            final int       numberOfChildren = parent.getComponentCount();

            if (numberOfChildren > 0)
            {
                final Component firstComponent = parent.getComponent(0);

                if (numberOfChildren > 1)
                {
                    final Component secondComponent = parent.getComponent(1);
                    final int       length          = (int) (width * this.proportion);
                    firstComponent.setBounds(x, y, length, height);
                    secondComponent.setBounds(x + length, y, width - length, height);
                }
                else
                {
                    firstComponent.setBounds(x, y, width, height);
                }
            }
        }
    }
}
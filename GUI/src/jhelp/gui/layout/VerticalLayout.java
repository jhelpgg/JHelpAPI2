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
 * Layout components in vertical way <br>
 * <br>
 * Last modification : 30 janv. 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public class VerticalLayout
        implements LayoutManager
{
    /**
     * Center components
     */
    public static final int ALIGN_CENTER    = 1;
    /**
     * Align components at left
     */
    public static final int ALIGN_LEFT      = 2;
    /**
     * Align components at right
     */
    public static final int ALIGN_RIGHT     = 0;
    /**
     * Extends components to take all the width
     */
    public static final int EXTENDS_WIDTH   = 3;
    /**
     * Actual alignment
     */
    private             int actualAlignment = VerticalLayout.ALIGN_CENTER;
    /**
     * Horizontal space
     */
    private             int spaceHorizontal = 1;
    /**
     * Vertical space
     */
    private             int spaceVertical   = 1;

    /**
     * Constructs VerticalLayout
     */
    public VerticalLayout()
    {
    }

    /**
     * Constructs VerticalLayout
     *
     * @param alignment Alignment of components
     */
    public VerticalLayout(final int alignment)
    {
        this.actualAlignment = alignment;
    }

    /**
     * Constructs VerticalLayout
     *
     * @param spaceHorizontal Empty space at right and left
     * @param spaceVertical   Empty space between components
     */
    public VerticalLayout(final int spaceHorizontal, final int spaceVertical)
    {
        this.spaceHorizontal = spaceHorizontal;
        this.spaceVertical = spaceVertical;
    }

    /**
     * Constructs VerticalLayout
     *
     * @param spaceHorizontal Empty space at right and left
     * @param spaceVertical   Empty space between components
     * @param aligment        Alignment of components
     */
    public VerticalLayout(final int spaceHorizontal, final int spaceVertical, final int aligment)
    {
        this.spaceHorizontal = spaceHorizontal;
        this.spaceVertical = spaceVertical;
        this.actualAlignment = aligment;
    }

    /**
     * Compute dimension of component
     *
     * @param parent Container that contains the component
     * @param index  Component index in container
     * @return Computed dimension
     */
    private Dimension computeDimension(final Container parent, final int index)
    {
        Dimension dimension = new Dimension();
        // synchronized(parent.getTreeLock())
        {
            try
            {
                dimension = UtilGUI.computePreferredDimension(parent.getComponent(index));
            }
            catch (final Exception e)
            {
                try
                {
                    dimension = parent.getComponent(index).getSize();
                }
                catch (final Exception ee)
                {
                    dimension = new Dimension(100, 100);
                }
            }
        }
        return new Dimension(dimension);
    }

    /**
     * Do nothing
     *
     * @param name Unused
     * @param comp Unused
     * @see LayoutManager#addLayoutComponent(String, Component)
     */
    @Override
    public void addLayoutComponent(final String name, final Component comp)
    {
    }

    /**
     * Do nothing
     *
     * @param comp Unused
     * @see LayoutManager#removeLayoutComponent(Component)
     */
    @Override
    public void removeLayoutComponent(final Component comp)
    {
    }

    /**
     * Compute preferred dimension
     *
     * @param parent Container to compute its preferred dimension
     * @return Preferred dimension
     * @see LayoutManager#preferredLayoutSize(Container)
     */
    @Override
    public Dimension preferredLayoutSize(final Container parent)
    {
        final Dimension preferredSize = new Dimension(0, 0);
        synchronized (parent.getTreeLock())
        {
            final int nb = parent.getComponentCount();
            for (int i = 0; i < nb; i++)
            {
                if (parent.getComponent(i)
                          .isVisible())
                {
                    final Dimension dimension = this.computeDimension(parent, i);
                    if (preferredSize.width < dimension.width)
                    {
                        preferredSize.width = dimension.width;
                    }
                    preferredSize.height += dimension.height + this.spaceVertical;
                }
            }
            final Insets insets = parent.getInsets();
            preferredSize.width += insets.right + insets.left + (this.spaceHorizontal * 2);
            preferredSize.height += insets.top + insets.bottom + (this.spaceVertical * 2);
        }
        return preferredSize;
    }

    /**
     * Compute minimum dimension
     *
     * @param parent Container to compute its minimum dimension
     * @return minimum dimension
     * @see LayoutManager#minimumLayoutSize(Container)
     */
    @Override
    public Dimension minimumLayoutSize(final Container parent)
    {
        final Dimension minimumSize = new Dimension(0, 0);
        synchronized (parent.getTreeLock())
        {
            final int nb = parent.getComponentCount();
            for (int i = 0; i < nb; i++)
            {
                if (parent.getComponent(i)
                          .isVisible())
                {
                    final Dimension dimension = UtilGUI.computeMinimumDimension(parent.getComponent(i));
                    if (minimumSize.width < dimension.width)
                    {
                        minimumSize.width = dimension.width;
                    }
                    minimumSize.height += dimension.height + this.spaceVertical;
                }
            }
            final Insets insets = parent.getInsets();
            minimumSize.width += insets.right + insets.left + (this.spaceHorizontal * 2);
            minimumSize.height += insets.top + insets.bottom + (this.spaceVertical * 2);
        }
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
        final Dimension pref = parent.getSize(); // this.preferredLayoutSize(parent);

        final Insets insets = parent.getInsets();
        pref.width = pref.width - insets.right - insets.right;
        pref.height = pref.height - insets.top - insets.bottom;
        synchronized (parent.getTreeLock())
        {
            int maxLarg = parent.getWidth();
            maxLarg = maxLarg - insets.right - insets.right;
            if (pref.width > maxLarg)
            {
                pref.width = maxLarg;
            }

            final int x = this.spaceHorizontal + insets.left;
            int       y = this.spaceVertical + insets.top;

            final int nb = parent.getComponentCount();
            for (int i = 0; i < nb; i++)
            {
                if (parent.getComponent(i).isVisible())
                {
                    final Dimension d = this.computeDimension(parent, i);
                    if (d.width > maxLarg)
                    {
                        d.width = maxLarg;
                    }
                    int plus = 0;
                    if (this.actualAlignment == VerticalLayout.ALIGN_CENTER)
                    {
                        plus = (pref.width - d.width) / 2;
                    }
                    else if (this.actualAlignment == VerticalLayout.ALIGN_RIGHT)
                    {
                        plus = pref.width - d.width;
                    }
                    else if (this.actualAlignment == VerticalLayout.EXTENDS_WIDTH)
                    {
                        d.width = maxLarg;
                    }
                    parent.getComponent(i).setSize(d);
                    parent.getComponent(i).setLocation(x + plus, y);
                    y += d.height;// + this.spaceVertical;
                }
            }
        }
    }
}
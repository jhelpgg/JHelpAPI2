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
import jhelp.util.list.Pair;

/**
 * 2D layout that put 2D components in vertical way
 *
 * @author JHelp
 */
public class JHelpVerticalLayout
        extends JHelpLayout
{
    /**
     * Constraints for place a component inside its cell
     *
     * @author JHelp
     */
    public enum JHelpVerticalLayoutConstraints
            implements JHelpConstraints
    {
        /**
         * Align the component at the left
         */
        ALIGN_LEFT,
        /**
         * Align the component at the right
         */
        ALIGN_RIGHT,
        /**
         * Center the component
         */
        CENTER,
        /**
         * Force the component to fill the container width
         */
        EXPANDED
    }

    /**
     * Empty space height in container border
     */
    private final int borderHeight;
    /**
     * Empty space width in container border
     */
    private final int borderWidth;
    /**
     * Empty space to put between each components
     */
    private final int spaceBetweenComponents;

    /**
     * Create a new instance of JHelpVerticalLayout
     */
    public JHelpVerticalLayout()
    {
        this(1, 1, 1);
    }

    /**
     * Create a new instance of JHelpVerticalLayout
     *
     * @param spaceBetweenComponents Empty space to put between each components
     */
    public JHelpVerticalLayout(final int spaceBetweenComponents)
    {
        this(1, 1, spaceBetweenComponents);
    }

    /**
     * Create a new instance of JHelpVerticalLayout
     *
     * @param borderWidth  Empty space width in container border
     * @param borderHeight Empty space height in container border
     */
    public JHelpVerticalLayout(final int borderWidth, final int borderHeight)
    {
        this(borderWidth, borderHeight, 1);
    }

    /**
     * Create a new instance of JHelpVerticalLayout
     *
     * @param borderWidth            Empty space width in container border
     * @param borderHeight           Empty space height in container border
     * @param spaceBetweenComponents Empty space to put between each components
     */
    public JHelpVerticalLayout(final int borderWidth, final int borderHeight, final int spaceBetweenComponents)
    {
        if ((borderHeight < 0) || (borderWidth < 0) || (spaceBetweenComponents < 0))
        {
            throw new IllegalArgumentException("One of specified value is negative !");
        }

        this.borderWidth = borderWidth;
        this.borderHeight = borderHeight;
        this.spaceBetweenComponents = spaceBetweenComponents;
    }

    /**
     * Empty space height in container border
     *
     * @return Empty space height in container border
     */
    public int borderHeight()
    {
        return this.borderHeight;
    }

    /**
     * Empty space width in container border
     *
     * @return Empty space width in container border
     */
    public int borderWidth()
    {
        return this.borderWidth;
    }

    /**
     * Indicates if constraints can be use with this layout.<br>
     * In other words if it is a {@link JHelpVerticalLayoutConstraints} <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param constraints Constraints tested
     * @return {@code true} if compatible
     * @see JHelpLayout#compatible(JHelpConstraints)
     */
    @Override
    public boolean compatible(final JHelpConstraints constraints)
    {
        return (constraints != null) && (constraints instanceof JHelpVerticalLayoutConstraints);
    }

    /**
     * Compute layout bounds and layout components inside the container <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param children     Children are inside the container
     * @param parentWidth  Container width
     * @param parentHeight Container height
     * @return Computed bounds
     * @see JHelpLayout#computeBounds(ArrayList, int, int)
     */
    @Override
    public Rectangle computeBounds(
            final ArrayList<Pair<JHelpComponent2D, JHelpConstraints>> children, final int parentWidth,
            final int parentHeight)
    {
        int       width  = 0;
        int       height = 0;
        Dimension preferredSize;

        for (final Pair<JHelpComponent2D, JHelpConstraints> child : children)
        {
            if (!child.first.visible())
            {
                continue;
            }

            preferredSize = child.first.preferredSize(-1, -1);

            switch ((JHelpVerticalLayoutConstraints) child.second)
            {
                case ALIGN_LEFT:
                case ALIGN_RIGHT:
                    width = Math.max(width, preferredSize.width + this.borderWidth);
                    break;
                case CENTER:
                    width = Math.max(width, preferredSize.width + (this.borderWidth * 2));
                    break;
                case EXPANDED:
                    width = Math.max(width, preferredSize.width + (this.borderWidth * 2));
                    break;
            }

            if (height > 0)
            {
                height += this.spaceBetweenComponents;
            }

            height += preferredSize.height;
        }

        width = Math.max(width, parentWidth);
        int y = this.borderHeight;

        for (final Pair<JHelpComponent2D, JHelpConstraints> child : children)
        {
            if (!child.first.visible())
            {
                continue;
            }

            preferredSize = child.first.preferredSize(-1, -1);

            switch ((JHelpVerticalLayoutConstraints) child.second)
            {
                case ALIGN_LEFT:
                    child.first.bounds(this.borderWidth, y, preferredSize.width, preferredSize.height);
                    break;
                case ALIGN_RIGHT:
                    child.first.bounds(width - this.borderWidth - preferredSize.width, y, preferredSize.width,
                                       preferredSize.height);
                    break;
                case CENTER:
                    child.first.bounds((width - preferredSize.width) >> 1, y, preferredSize.width,
                                       preferredSize.height);
                    break;
                case EXPANDED:
                    child.first.bounds(this.borderWidth, y, width - (this.borderWidth * 2), preferredSize.height);
                    break;
            }

            y += preferredSize.height + this.spaceBetweenComponents;
        }

        return new Rectangle(0, 0, width, height + (this.borderHeight * 2));
    }

    /**
     * Space between each component
     *
     * @return Space between each component
     */
    public int spaceBetweenComponents()
    {
        return this.spaceBetweenComponents;
    }
}
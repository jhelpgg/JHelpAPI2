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
 * BorderLayout for 2D.<br>
 * Imagine the panel cut like this :
 * <table border=1>
 * <tr>
 * <td>(1)TOP-LEFT</td>
 * <td>(2)TOP</td>
 * <td>(3)TOP-RIGHT</td>
 * </tr>
 * <tr>
 * <td>(4)LEFT</td>
 * <td>(5)CENTER</td>
 * <td>(6)RIGHT</td>
 * </tr>
 * <tr>
 * <td>(7)BOTTOM-LEFT</td>
 * <td>(8)BOTTOM</td>
 * <td>(9)BOTTOM-RIGHT</td>
 * </tr>
 * </table>
 * It allows you to place each component in each above cell or take all the top (1+2+3), all the left(1+4+7), all the right
 * (3+6+9), or all the bottom (7+8+9)
 *
 * @author JHelp
 */
public class JHelpBorderLayout
        extends JHelpLayout
{
    /**
     * Border layout constraints
     *
     * @author JHelp
     */
    public enum JHelpBorderLayoutConstraints
            implements JHelpConstraints
    {
        /**
         * Place the element at bottom (8)
         */
        BOTTOM,
        /**
         * The component will take all the bottom (7+8+9)
         */
        BOTTOM_EXPANDED,
        /**
         * Place the element at bottom-left (7)
         */
        BOTTOM_LEFT,
        /**
         * Place the element at bottom-right (9)
         */
        BOTTOM_RIGHT,
        /**
         * Place the element at center (5)
         */
        CENTER,
        /**
         * Place the element at left (4)
         */
        LEFT,
        /**
         * The component will take all the left (1+4+7)
         */
        LEFT_EXPANDED,
        /**
         * Place the element at right (6)
         */
        RIGHT,
        /**
         * The component will take all the right (3+6+9)
         */
        RIGHT_EXPANDED,
        /**
         * Place the element at top (2)
         */
        TOP,
        /**
         * The component will take all the top (1+2+3)
         */
        TOP_EXPANDED,
        /**
         * Place the element at top-left (1)
         */
        TOP_LEFT,
        /**
         * Place the element at top-right (3)
         */
        TOP_RIGHT
    }

    /**
     * Create a new instance of JHelpBorderLayout
     */
    public JHelpBorderLayout()
    {
    }

    /**
     * Indicates if a constraints can be use with the layout <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param constraints Tested constraints
     * @return {@code true} if a constraints can be use with the layout
     * @see JHelpLayout#compatible(JHelpConstraints)
     */
    @Override
    public boolean compatible(final JHelpConstraints constraints)
    {
        return (constraints != null) && (constraints instanceof JHelpBorderLayoutConstraints);
    }

    /**
     * Compute the component and children bounds <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param children     Children to compute bounds
     * @param parentWidth  Parent width (if -1, means not know now)
     * @param parentHeight Parent height (if -1, means not know now)
     * @return Component computed bounds
     * @see JHelpLayout#computeBounds(ArrayList, int, int)
     */
    @Override
    public Rectangle computeBounds(
            final ArrayList<Pair<JHelpComponent2D, JHelpConstraints>> children, int parentWidth, int parentHeight)
    {
        int       top          = 0;
        int       left         = 0;
        int       right        = 0;
        int       bottom       = 0;
        int       centerWidth  = 0;
        int       centerHeight = 0;
        Dimension prefferedSize;

        for (final Pair<JHelpComponent2D, JHelpConstraints> child : children)
        {
            if (!child.first.visible())
            {
                continue;
            }

            prefferedSize = child.first.preferredSize(-1, -1);

            switch ((JHelpBorderLayoutConstraints) child.second)
            {
                case BOTTOM:
                    bottom = Math.max(prefferedSize.height, bottom);
                    break;
                case BOTTOM_EXPANDED:
                    bottom = Math.max(prefferedSize.height, bottom);
                    break;
                case BOTTOM_LEFT:
                    left = Math.max(prefferedSize.width, left);
                    bottom = Math.max(prefferedSize.height, bottom);
                    break;
                case BOTTOM_RIGHT:
                    right = Math.max(prefferedSize.width, right);
                    bottom = Math.max(prefferedSize.height, bottom);
                    break;
                case LEFT:
                    left = Math.max(prefferedSize.width, left);
                    break;
                case LEFT_EXPANDED:
                    left = Math.max(prefferedSize.width, left);
                    break;
                case RIGHT:
                    right = Math.max(prefferedSize.width, right);
                    break;
                case RIGHT_EXPANDED:
                    right = Math.max(prefferedSize.width, right);
                    break;
                case TOP:
                    top = Math.max(prefferedSize.height, top);
                    break;
                case TOP_EXPANDED:
                    top = Math.max(prefferedSize.height, top);
                    break;
                case TOP_LEFT:
                    left = Math.max(prefferedSize.width, left);
                    top = Math.max(prefferedSize.height, top);
                    break;
                case TOP_RIGHT:
                    right = Math.max(prefferedSize.width, right);
                    top = Math.max(prefferedSize.height, top);
                    break;
                case CENTER:
                    if ((parentWidth < 0) && (parentHeight < 0))
                    {
                        centerWidth = Math.max(prefferedSize.width, centerWidth);
                        centerHeight = Math.max(prefferedSize.height, centerHeight);
                    }
                    break;
            }
        }

        if ((parentWidth < 0) && (parentHeight < 0))
        {
            parentWidth = left + centerWidth + right;
            parentHeight = top + centerHeight + bottom;
        }
        else
        {
            centerWidth = parentWidth - left - right;
            centerHeight = parentHeight - top - bottom;
        }

        for (final Pair<JHelpComponent2D, JHelpConstraints> child : children)
        {
            if (!child.first.visible())
            {
                continue;
            }

            switch ((JHelpBorderLayoutConstraints) child.second)
            {
                case BOTTOM:
                    child.first.bounds(left, parentHeight - bottom, centerWidth, bottom);
                    break;
                case BOTTOM_EXPANDED:
                    child.first.bounds(0, parentHeight - bottom, parentWidth, bottom);
                    break;
                case BOTTOM_LEFT:
                    child.first.bounds(0, parentHeight - bottom, left, bottom);
                    break;
                case BOTTOM_RIGHT:
                    child.first.bounds(parentWidth - right, parentHeight - bottom, right, bottom);
                    break;
                case LEFT:
                    child.first.bounds(0, top, left, centerHeight);
                    break;
                case LEFT_EXPANDED:
                    child.first.bounds(0, 0, left, parentHeight);
                    break;
                case RIGHT:
                    child.first.bounds(parentWidth - right, top, right, centerHeight);
                    break;
                case RIGHT_EXPANDED:
                    child.first.bounds(parentWidth - right, 0, right, parentHeight);
                    break;
                case TOP:
                    child.first.bounds(left, 0, centerWidth, top);
                    break;
                case TOP_EXPANDED:
                    child.first.bounds(0, 0, parentWidth, top);
                    break;
                case TOP_LEFT:
                    child.first.bounds(0, 0, left, top);
                    break;
                case TOP_RIGHT:
                    child.first.bounds(parentWidth - right, 0, right, top);
                    break;
                case CENTER:
                    child.first.bounds(left, top, centerWidth, centerHeight);
                    break;
            }
        }

        return new Rectangle(0, 0, parentWidth, parentHeight);
    }
}
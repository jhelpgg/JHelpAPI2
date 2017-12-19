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
import jhelp.util.text.UtilText;

/**
 * Layout for 2D components that use a table or grid.<br>
 * <br>
 * Imagine the panel is a grid of regular cells, each components can be placed in it on taking one ore more adjacent cell.<br>
 * For example in a table 5x5, You can place the compoaent A, b and C ,like this :
 * <table border=1>
 * <tr>
 * <td>A</td>
 * <td>A</td>
 * <td>A</td>
 * <td>B</td>
 * <td>B</td>
 * </tr>
 * <tr>
 * <td>A</td>
 * <td>A</td>
 * <td>A</td>
 * <td>B</td>
 * <td>B</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * <td>B</td>
 * <td>B</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>&nbsp;</td>
 * <td></td>
 * <td></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>C</td>
 * <td></td>
 * <td></td>
 * <td></td>
 * </tr>
 * </table>
 *
 * @author JHelp
 */
public class JHelpTableLayout
        extends JHelpLayout
{
    /**
     * Constrains to use on component
     *
     * @author JHelp
     */
    public static class JHelpTableLayoutConstraints
            implements JHelpConstraints
    {
        /**
         * Number cells take in height
         */
        final int cellHeight;
        /**
         * Number cells take in width
         */
        final int cellWidth;
        /**
         * Cell X
         */
        final int cellX;
        /**
         * Cell Y
         */
        final int cellY;

        /**
         * Take one cell in width and height
         *
         * @param cellX Cell X
         * @param cellY Cell Y
         */
        public JHelpTableLayoutConstraints(final int cellX, final int cellY)
        {
            this(cellX, cellY, 1, 1);
        }

        /**
         * Create a new instance of JHelpTableLayoutConstraints
         *
         * @param cellX      Cell X
         * @param cellY      Cell Y
         * @param cellWidth  Number cell in width
         * @param cellHeight Number cell in height
         */
        public JHelpTableLayoutConstraints(final int cellX, final int cellY, final int cellWidth, final int cellHeight)
        {
            this.cellX = Math.max(0, cellX);
            this.cellY = Math.max(0, cellY);
            this.cellWidth = Math.max(1, cellWidth);
            this.cellHeight = Math.max(1, cellHeight);
        }

        /**
         * Number cell in height
         *
         * @return Number cell in height
         */
        public int getCellHeight()
        {
            return this.cellHeight;
        }

        /**
         * Number cell in width
         *
         * @return Number cell in width
         */
        public int getCellWidth()
        {
            return this.cellWidth;
        }

        /**
         * Cell X
         *
         * @return Cell X
         */
        public int getCellX()
        {
            return this.cellX;
        }

        /**
         * Cell Y
         *
         * @return cell Y
         */
        public int getCellY()
        {
            return this.cellY;
        }

        /**
         * String representation <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @return String representation
         * @see Object#toString()
         */
        @Override
        public String toString()
        {
            return UtilText.concatenate(JHelpTableLayoutConstraints.class.getSimpleName(), " (", this.cellX, ", ",
                                        this.cellY, ") ", this.cellWidth, "x",
                                        this.cellHeight);
        }
    }

    /**
     * Create a new instance of JHelpTableLayout
     */
    public JHelpTableLayout()
    {
    }

    /**
     * Indicates if a constraints is a {@link JHelpTableLayoutConstraints} <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param constraints Constraints tested
     * @return {@code true} if the constraints is a {@link JHelpTableLayoutConstraints}
     * @see JHelpLayout#compatible(JHelpConstraints)
     */
    @Override
    public boolean compatible(final JHelpConstraints constraints)
    {
        return (constraints != null) && ((constraints instanceof JHelpTableLayoutConstraints));
    }

    /**
     * Compute layout bounds <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param children     Children list
     * @param parentWidth  Parent width
     * @param parentHeight Parent height
     * @return Computed bounds
     * @see JHelpLayout#computeBounds(ArrayList, int, int)
     */
    @Override
    public Rectangle computeBounds(
            final ArrayList<Pair<JHelpComponent2D, JHelpConstraints>> children, final int parentWidth,
            final int parentHeight)
    {
        int                         xmin         = Integer.MAX_VALUE;
        int                         xmax         = Integer.MIN_VALUE;
        int                         ymin         = Integer.MAX_VALUE;
        int                         ymax         = Integer.MIN_VALUE;
        int                         cellWidth    = 0;
        int                         cellHeight   = 0;
        int                         nbCellWidth  = 0;
        int                         nbCellHeight = 0;
        Dimension                   preferred;
        JHelpTableLayoutConstraints constraints;

        for (final Pair<JHelpComponent2D, JHelpConstraints> child : children)
        {
            if (!child.first.visible())
            {
                continue;
            }

            preferred = child.first.preferredSize(-1, -1);
            constraints = (JHelpTableLayoutConstraints) child.second;

            xmin = Math.min(xmin, constraints.cellX);
            xmax = Math.max(xmax, constraints.cellX + constraints.cellWidth);
            ymin = Math.min(ymin, constraints.cellY);
            ymax = Math.max(ymax, constraints.cellY + constraints.cellHeight);

            cellWidth = Math.max(cellWidth, (preferred.width + (constraints.cellWidth >> 1)) / constraints.cellWidth);
            cellHeight = Math.max(cellHeight,
                                  (preferred.height + (constraints.cellHeight >> 1)) / constraints.cellHeight);
        }

        nbCellWidth = Math.max(0, xmax - xmin);
        nbCellHeight = Math.max(0, ymax - ymin);

        for (final Pair<JHelpComponent2D, JHelpConstraints> child : children)
        {
            if (!child.first.visible())
            {
                continue;
            }

            constraints = (JHelpTableLayoutConstraints) child.second;

            child.first.bounds((constraints.cellX - xmin) * cellWidth, (constraints.cellY - ymin) * cellHeight,
                               constraints.cellWidth * cellWidth, constraints.cellHeight * cellHeight);
        }

        return new Rectangle(0, 0, cellWidth * nbCellWidth, cellHeight * nbCellHeight);
    }
}
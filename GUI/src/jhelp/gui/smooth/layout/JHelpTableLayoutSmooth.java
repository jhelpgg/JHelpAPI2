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
package jhelp.gui.smooth.layout;

import java.awt.Dimension;
import java.awt.Rectangle;
import jhelp.gui.smooth.JHelpComponentSmooth;
import jhelp.gui.smooth.JHelpPanelSmooth;
import jhelp.util.list.Pair;

/**
 * Constraints that layout components in table.<br>
 * It accepts only {@link JHelpTableConstraintsSmooth}<br>
 * Each component is place on a cell and take one several cell in width and height.<br>
 * Exaple if you give to A cell(0, 0) 2x2, B (0,2) 3x1 ,your screen look like :<br>
 * AA.<br>
 * AA.<br>
 * BBB
 *
 * @author JHelp
 */
public class JHelpTableLayoutSmooth
        implements JHelpLayoutSmooth
{
    /**
     * Create a new instance of JHelpTableLayoutSmooth
     */
    public JHelpTableLayoutSmooth()
    {
    }

    /**
     * Indicate if a constraints can be used with the layout <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param contraints Tested constraints
     * @return {@code true} if constraints accepted
     * @see JHelpLayoutSmooth#acceptConstraints(JHelpConstraintsSmooth)
     */
    @Override
    public boolean acceptConstraints(final JHelpConstraintsSmooth contraints)
    {
        return (contraints != null) && (contraints instanceof JHelpTableConstraintsSmooth);
    }

    /**
     * Compute container preferred size <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param container Container to have its preferred size
     * @return Computed preferred size
     * @see JHelpLayoutSmooth#computePreferredSize(jhelp.gui.smooth.JHelpPanelSmooth)
     */
    @Override
    public Dimension computePreferredSize(final JHelpPanelSmooth container)
    {
        int                                                cellWidth  = 0;
        int                                                cellHeight = 0;
        int                                                xMin       = Integer.MAX_VALUE;
        int                                                xMax       = Integer.MIN_VALUE;
        int                                                yMin       = Integer.MAX_VALUE;
        int                                                yMax       = Integer.MIN_VALUE;
        final int                                          count      = container.numberOfChildren();
        Pair<JHelpComponentSmooth, JHelpConstraintsSmooth> pair;
        JHelpComponentSmooth                               componentSmooth;
        JHelpTableConstraintsSmooth                        constraintsSmooth;
        Dimension                                          preferred;

        for (int i = 0; i < count; i++)
        {
            pair = container.childAndConstraints(i);
            componentSmooth = pair.first;

            if (componentSmooth.visible())
            {
                constraintsSmooth = (JHelpTableConstraintsSmooth) pair.second;
                preferred = componentSmooth.preferredSize();
                xMin = Math.min(xMin, constraintsSmooth.x);
                xMax = Math.max(xMax, constraintsSmooth.x + constraintsSmooth.width);
                yMin = Math.min(yMin, constraintsSmooth.y);
                yMax = Math.max(yMax, constraintsSmooth.y + constraintsSmooth.height);
                cellWidth = Math.max(cellWidth,
                                     (preferred.width + (constraintsSmooth.width >> 1)) / constraintsSmooth.width);
                cellHeight = Math.max(cellHeight,
                                      (preferred.height + (constraintsSmooth.height >> 1)) / constraintsSmooth.height);
            }
        }

        final int numberCellWidth  = Math.max(0, xMax - xMin);
        final int numberCellHeight = Math.max(0, yMax - yMin);
        return new Dimension(cellWidth * numberCellWidth, cellHeight * numberCellHeight);
    }

    /**
     * Layout components inside the container and return suggested bcontainer bounds <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param container    Container to layout
     * @param x            X start
     * @param y            Y start
     * @param parentWidth  Width start
     * @param parentHeight Height start
     * @return Suggested bounds
     * @see JHelpLayoutSmooth#layoutComponents(jhelp.gui.smooth.JHelpPanelSmooth, int, int, int, int)
     */
    @Override
    public Rectangle layoutComponents(
            final JHelpPanelSmooth container, final int x, final int y, final int parentWidth, final int parentHeight)
    {
        int                                                xMin  = Integer.MAX_VALUE;
        int                                                xMax  = Integer.MIN_VALUE;
        int                                                yMin  = Integer.MAX_VALUE;
        int                                                yMax  = Integer.MIN_VALUE;
        final int                                          count = container.numberOfChildren();
        Pair<JHelpComponentSmooth, JHelpConstraintsSmooth> pair;
        JHelpComponentSmooth                               componentSmooth;
        JHelpTableConstraintsSmooth                        constraintsSmooth;

        for (int i = 0; i < count; i++)
        {
            pair = container.childAndConstraints(i);
            componentSmooth = pair.first;

            if (componentSmooth.visible())
            {
                constraintsSmooth = (JHelpTableConstraintsSmooth) pair.second;
                xMin = Math.min(xMin, constraintsSmooth.x);
                xMax = Math.max(xMax, constraintsSmooth.x + constraintsSmooth.width);
                yMin = Math.min(yMin, constraintsSmooth.y);
                yMax = Math.max(yMax, constraintsSmooth.y + constraintsSmooth.height);
            }
        }

        final int numberCellWidth  = Math.max(1, xMax - xMin);
        final int numberCellHeight = Math.max(1, yMax - yMin);
        final int cellWidth        = (parentWidth + (numberCellWidth >> 1)) / numberCellWidth;
        final int cellHeight       = (parentHeight + (numberCellHeight >> 1)) / numberCellHeight;

        for (int i = 0; i < count; i++)
        {
            pair = container.childAndConstraints(i);
            componentSmooth = pair.first;

            if (componentSmooth.visible())
            {
                constraintsSmooth = (JHelpTableConstraintsSmooth) pair.second;
                componentSmooth.bounds(x + ((constraintsSmooth.x - xMin) * cellWidth),
                                       y + ((constraintsSmooth.y - yMin) * cellHeight), constraintsSmooth.width
                                                                                        * cellWidth,
                                       constraintsSmooth.height * cellHeight);
            }
        }

        return new Rectangle(x, y, parentWidth, parentHeight);
    }
}
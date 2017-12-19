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
import jhelp.util.math.Math2;

/**
 * Layout components in borders.<br>
 * Usually they are a center component, that take all the free space in middle. you can place components above, bellow, at left
 * and/or at right of it. <br>
 * The border components can take all width/height or not, it is aloso possble to place components at each corner.<br>
 * The only accepted constraints is {@link JHelpBorderConstraintsSmooth}
 *
 * @author JHelp
 */
public class JHelpBorderLayoutSmooth
        implements JHelpLayoutSmooth
{
    /**
     * Create a new instance of JHelpBorderLayoutSmooth
     */
    public JHelpBorderLayoutSmooth()
    {
    }

    /**
     * Indicates if constraints is accepted <br>
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
        return (contraints != null) && (contraints instanceof JHelpBorderConstraintsSmooth);
    }

    /**
     * Compute container preferred size <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param container Container to have its preferred size
     * @return Container preferred size
     * @see JHelpLayoutSmooth#computePreferredSize(jhelp.gui.smooth.JHelpPanelSmooth)
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public Dimension computePreferredSize(final JHelpPanelSmooth container)
    {
        int upLeftWidth     = 0;
        int upLeftHeight    = 0;
        int upWidth         = 0;
        int upHeight        = 0;
        int upRightWidth    = 0;
        int upRightHeight   = 0;
        int leftWidth       = 0;
        int leftHeight      = 0;
        int centerWidth     = 0;
        int centerHeight    = 0;
        int rightWidth      = 0;
        int rightHeight     = 0;
        int downLeftWidth   = 0;
        int downLeftHeight  = 0;
        int downWidth       = 0;
        int downRightWidth  = 0;
        int downRightHeight = 0;

        final int                                          count = container.numberOfChildren();
        Pair<JHelpComponentSmooth, JHelpConstraintsSmooth> pair;
        JHelpBorderConstraintsSmooth                       borderConstraints;
        Dimension                                          preferred;

        for (int index = 0; index < count; index++)
        {
            pair = container.childAndConstraints(index);

            if (pair.first.visible())
            {
                preferred = pair.first.preferredSize();
                borderConstraints = (JHelpBorderConstraintsSmooth) pair.second;

                switch (borderConstraints)
                {
                    case CENTER:
                        centerWidth = preferred.width;
                        centerHeight = preferred.height;
                        break;
                    case CORNER_LEFT_DOWN:
                        downLeftWidth = preferred.width;
                        downLeftHeight = preferred.height;
                        break;
                    case CORNER_LEFT_UP:
                        upLeftWidth = preferred.width;
                        upLeftHeight = preferred.height;
                        break;
                    case CORNER_RIGHT_DOWN:
                        downRightWidth = preferred.width;
                        downRightHeight = preferred.height;
                        break;
                    case CORNER_RIGHT_UP:
                        upRightWidth = preferred.width;
                        upRightHeight = preferred.height;
                        break;
                    case DOWN:
                        downWidth = preferred.width;
                        break;
                    case DOWN_EXPAND:
                        downWidth = preferred.width;
                        break;
                    case LEFT:
                        leftWidth = preferred.width;
                        leftHeight = preferred.height;
                        break;
                    case LEFT_EXPAND:
                        leftWidth = preferred.width;
                        leftHeight = preferred.height;
                        break;
                    case RIGHT:
                        rightWidth = preferred.width;
                        rightHeight = preferred.height;
                        break;
                    case RIGHT_EXPAND:
                        rightWidth = preferred.width;
                        rightHeight = preferred.height;
                        break;
                    case UP:
                        upWidth = preferred.width;
                        upHeight = preferred.height;
                        break;
                    case UP_EXPAND:
                        upWidth = preferred.width;
                        upHeight = preferred.height;
                        break;
                }
            }
        }

        return new Dimension(Math2.maximum(upLeftWidth + upWidth + upRightWidth, leftWidth + centerWidth + rightWidth,
                                           downLeftWidth + downWidth
                                           + downRightWidth), Math2.maximum(upLeftHeight + leftHeight + downLeftHeight,
                                                                            upHeight + centerHeight + downRightHeight,
                                                                            upRightHeight
                                                                            + rightHeight + downRightHeight));
    }

    /**
     * Layout components inside a container and return suggested container bounds <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param container    Container to layout
     * @param x            X
     * @param y            Y
     * @param parentWidth  Parent width
     * @param parentHeight Parent height
     * @return Suggested bounds
     * @see JHelpLayoutSmooth#layoutComponents(jhelp.gui.smooth.JHelpPanelSmooth, int, int, int, int)
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public Rectangle layoutComponents(
            final JHelpPanelSmooth container, final int x, final int y, final int parentWidth, final int parentHeight)
    {
        int upLeftWidth     = 0;
        int upLeftHeight    = 0;
        int upHeight        = 0;
        int upRightWidth    = 0;
        int upRightHeight   = 0;
        int leftWidth       = 0;
        int rightWidth      = 0;
        int downLeftWidth   = 0;
        int downLeftHeight  = 0;
        int downHeight      = 0;
        int downRightWidth  = 0;
        int downRightHeight = 0;

        final int                                          count = container.numberOfChildren();
        Pair<JHelpComponentSmooth, JHelpConstraintsSmooth> pair;
        JHelpBorderConstraintsSmooth                       borderConstraints;
        Dimension                                          preferred;

        for (int index = 0; index < count; index++)
        {
            pair = container.childAndConstraints(index);

            if (pair.first.visible())
            {
                preferred = pair.first.preferredSize();
                borderConstraints = (JHelpBorderConstraintsSmooth) pair.second;

                switch (borderConstraints)
                {
                    case CENTER:
                        break;
                    case CORNER_LEFT_DOWN:
                        downLeftWidth = preferred.width;
                        downLeftHeight = preferred.height;
                        break;
                    case CORNER_LEFT_UP:
                        upLeftWidth = preferred.width;
                        upLeftHeight = preferred.height;
                        break;
                    case CORNER_RIGHT_DOWN:
                        downRightWidth = preferred.width;
                        downRightHeight = preferred.height;
                        break;
                    case CORNER_RIGHT_UP:
                        upRightWidth = preferred.width;
                        upRightHeight = preferred.height;
                        break;
                    case DOWN:
                        downHeight = preferred.height;
                        break;
                    case DOWN_EXPAND:
                        downHeight = preferred.height;
                        break;
                    case LEFT:
                        leftWidth = preferred.width;
                        break;
                    case LEFT_EXPAND:
                        leftWidth = preferred.width;
                        break;
                    case RIGHT:
                        rightWidth = preferred.width;
                        break;
                    case RIGHT_EXPAND:
                        rightWidth = preferred.width;
                        break;
                    case UP:
                        upHeight = preferred.height;
                        break;
                    case UP_EXPAND:
                        upHeight = preferred.height;
                        break;
                }
            }
        }

        final int left         = Math2.maximum(upLeftWidth, leftWidth, downLeftWidth);
        final int right        = Math2.maximum(upRightWidth, rightWidth, downRightWidth);
        final int up           = Math2.maximum(upLeftHeight, upHeight, upRightHeight);
        final int down         = Math2.maximum(downLeftHeight, downHeight, downRightHeight);
        final int centerWidth  = parentWidth - left - right;
        final int centerHeight = parentHeight - up - down;
        final int x1           = x + left;
        final int y1           = y + up;
        final int x2           = x + (parentWidth - right);
        final int y2           = y + (parentHeight - down);

        for (int index = 0; index < count; index++)
        {
            pair = container.childAndConstraints(index);

            if (pair.first.visible())
            {
                preferred = pair.first.preferredSize();
                borderConstraints = (JHelpBorderConstraintsSmooth) pair.second;

                switch (borderConstraints)
                {
                    case CENTER:
                        pair.first.bounds(x1, y1, centerWidth, centerHeight);
                        break;
                    case CORNER_LEFT_DOWN:
                        pair.first.bounds(x, y2, left, down);
                        break;
                    case CORNER_LEFT_UP:
                        pair.first.bounds(x, y, left, up);
                        break;
                    case CORNER_RIGHT_DOWN:
                        pair.first.bounds(x2, y2, right, down);
                        break;
                    case CORNER_RIGHT_UP:
                        pair.first.bounds(x2, y, right, up);
                        break;
                    case DOWN:
                        pair.first.bounds(x1, y2, centerWidth, down);
                        break;
                    case DOWN_EXPAND:
                        pair.first.bounds(x, y2, parentWidth, down);
                        break;
                    case LEFT:
                        pair.first.bounds(x, y1, left, centerHeight);
                        break;
                    case LEFT_EXPAND:
                        pair.first.bounds(x, y, left, parentHeight);
                        break;
                    case RIGHT:
                        pair.first.bounds(x2, y1, right, centerHeight);
                        break;
                    case RIGHT_EXPAND:
                        pair.first.bounds(x2, y, right, parentHeight);
                        break;
                    case UP:
                        pair.first.bounds(x1, y, centerWidth, up);
                        break;
                    case UP_EXPAND:
                        pair.first.bounds(x, y, parentWidth, up);
                        break;
                }
            }
        }

        return new Rectangle(x, y, parentWidth, parentHeight);
    }
}
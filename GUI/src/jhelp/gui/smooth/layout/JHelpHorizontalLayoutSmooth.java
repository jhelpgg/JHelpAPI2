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
 * Layout component horizontally.<br>
 * It accepts only {@link JHelpHorizontalConstraintsSmooth} as constraints
 *
 * @author JHelp
 */
public class JHelpHorizontalLayoutSmooth
        implements JHelpLayoutSmooth
{
    /**
     * Create a new instance of JHelpHorizontalLayoutSmooth
     */
    public JHelpHorizontalLayoutSmooth()
    {
    }

    /**
     * Indicates if a constraints can be used with the layout <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param constraints Tested constraints
     * @return {@code true} if constraints is accepted
     * @see JHelpLayoutSmooth#acceptConstraints(JHelpConstraintsSmooth)
     */
    @Override
    public boolean acceptConstraints(final JHelpConstraintsSmooth constraints)
    {
        return (constraints != null) && (constraints instanceof JHelpHorizontalConstraintsSmooth);
    }

    /**
     * Compute a container preferred size with is current content <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param container Container to compute its preferred size
     * @return Computed preferred size
     * @see JHelpLayoutSmooth#computePreferredSize(jhelp.gui.smooth.JHelpPanelSmooth)
     */
    @Override
    public Dimension computePreferredSize(final JHelpPanelSmooth container)
    {
        int                  width  = 0;
        int                  height = 0;
        final int            count  = container.numberOfChildren();
        JHelpComponentSmooth componentSmooth;
        Dimension            preferred;

        for (int i = 0; i < count; i++)
        {
            componentSmooth = container.child(i);

            if (componentSmooth.visible())
            {
                preferred = componentSmooth.preferredSize();
                width += preferred.width;
                height = Math.max(height, preferred.height);
            }
        }

        return new Dimension(width, height);
    }

    /**
     * Layout components inside a container and return the suggested container bounds <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param container    Container to layout its children
     * @param x            X where container will be placed
     * @param y            Y where container will be placed
     * @param parentWidth  Parent width
     * @param parentHeight Parent height
     * @return Suggested container bounds
     * @see JHelpLayoutSmooth#layoutComponents(jhelp.gui.smooth.JHelpPanelSmooth, int, int, int, int)
     */
    @Override
    public Rectangle layoutComponents(
            final JHelpPanelSmooth container, final int x, final int y, final int parentWidth, final int parentHeight)
    {
        int                                                xx    = x;
        final int                                          count = container.numberOfChildren();
        JHelpComponentSmooth                               componentSmooth;
        JHelpHorizontalConstraintsSmooth                   constraintsSmooth;
        Pair<JHelpComponentSmooth, JHelpConstraintsSmooth> pair;
        Dimension                                          preferred;
        int                                                height;

        for (int i = 0; i < count; i++)
        {
            pair = container.childAndConstraints(i);
            componentSmooth = pair.first;

            if (componentSmooth.visible())
            {
                constraintsSmooth = (JHelpHorizontalConstraintsSmooth) pair.second;
                preferred = componentSmooth.preferredSize();
                height = Math.min(preferred.height, parentHeight);

                switch (constraintsSmooth)
                {
                    case CENTER:
                        componentSmooth.bounds(xx, y + ((parentHeight - height) >> 1), preferred.width, height);
                        break;
                    case EXPAND:
                        componentSmooth.bounds(xx, y, preferred.width, parentHeight);
                        break;
                    case UP:
                        componentSmooth.bounds(xx, y, preferred.width, height);
                        break;
                    case DOWN:
                        componentSmooth.bounds(xx, y + (parentHeight - height), preferred.width, height);
                        break;
                }

                xx += preferred.width;
            }
        }

        return new Rectangle(x, y, xx - x, parentHeight);
    }
}
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
package jhelp.gui.game.builder;

import java.awt.Rectangle;
import java.util.List;
import jhelp.gui.game.SensitiveElement;
import jhelp.util.list.Pair;

/**
 * Layout in sensitive animation that place components vertically
 *
 * @author JHelp
 */
public class SensitiveAnimationVerticalLayout
        implements SensitiveAnimationLayout
{
    /**
     * Bottom space
     */
    private final int bottom;
    /**
     * Left space
     */
    private final int left;
    /**
     * Right space
     */
    private final int right;
    /**
     * Space between components
     */
    private final int space;
    /**
     * Top space
     */
    private final int top;

    /**
     * Create a new instance of SensitiveAnimationVerticalLayout
     *
     * @param top    Number empty pixels in top
     * @param left   Number empty pixels in left
     * @param bottom Number empty pixels in bottom
     * @param right  Number empty pixels in right
     * @param space  Number empty pixels between components
     */
    public SensitiveAnimationVerticalLayout(
            final int top, final int left, final int bottom, final int right, final int space)
    {
        this.top = Math.max(0, top);
        this.bottom = Math.max(0, bottom);
        this.left = Math.max(0, left);
        this.right = Math.max(0, right);
        this.space = Math.max(1, space);
    }

    /**
     * Indicates if given constraints can be used with this layout <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param constraints Tested constraints
     * @return Always {@code true}
     * @see SensitiveAnimationLayout#isCompatible(SensitiveAnimationConstraints)
     */
    @Override
    public boolean isCompatible(final SensitiveAnimationConstraints constraints)
    {
        return true;
    }

    /**
     * Layout vertically given components <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param elements Elements to layout
     * @param x        X up left corner
     * @param y        Y up left corner
     * @param width    Reserved width
     * @param height   Reserved height
     * @return Rectangle take by layout components
     * @see SensitiveAnimationLayout#layout(List, int, int, int, int)
     */
    @Override
    public Rectangle layout(
            final List<Pair<SensitiveElement, SensitiveAnimationConstraints>> elements, final int x, final int y,
            final int width,
            final int height)
    {
        int xx          = x + this.left;
        int yy          = y + this.top;
        int elementWidth, elementHeight;
        int finalWidth  = 0;
        int finalHeight = this.top;

        for (final Pair<SensitiveElement, SensitiveAnimationConstraints> element : elements)
        {
            elementWidth = element.first.width();
            elementHeight = element.first.height();
            element.first.position(xx, yy);
            yy += elementHeight + this.space;
            finalWidth = Math.max(finalWidth, this.left + elementWidth + this.right);
            finalHeight += elementHeight + this.space;
        }

        for (final Pair<SensitiveElement, SensitiveAnimationConstraints> element : elements)
        {
            elementWidth = element.first.width();
            xx = x + ((finalWidth - elementWidth) >> 1);
            element.first.position(xx, element.first.y());
        }

        finalHeight += this.bottom;

        if (!elements.isEmpty())
        {
            finalHeight -= this.space;
        }

        return new Rectangle(x, y, finalWidth, finalHeight);
    }
}
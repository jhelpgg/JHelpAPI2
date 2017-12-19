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
package jhelp.gui.game;

import java.awt.Dimension;
import java.awt.Point;
import jhelp.util.gui.JHelpDrawable;
import jhelp.util.gui.JHelpImage;

/**
 * Shape of a button
 *
 * @author JHelp
 */
public enum ButtonShape
{
    /**
     * Rectangle button
     */
    RECTANGLE()
            {
                /**
                 * Compute the position of up left corner inside the shape <br>
                 * <br>
                 * <b>Parent documentation:</b><br>
                 * {@inheritDoc}
                 *
                 * @param insideWidth
                 *           Internal width desired
                 * @param insideHeight
                 *           Internal height desired
                 * @return Up left point position
                 * @see ButtonShape#computeShapeInsideUpLeftCorner(int, int)
                 */
                @Override
                public Point computeShapeInsideUpLeftCorner(final int insideWidth, final int insideHeight)
                {
                    return new Point(ButtonShape.BORDER, ButtonShape.BORDER);
                }

                /**
                 * Compute the total size of the shape <br>
                 * <br>
                 * <b>Parent documentation:</b><br>
                 * {@inheritDoc}
                 *
                 * @param insideWidth
                 *           Internal width desired
                 * @param insideHeight
                 *           Internal height desired
                 * @return Total size
                 * @see ButtonShape#computeShapeTotalSize(int, int)
                 */
                @Override
                public Dimension computeShapeTotalSize(final int insideWidth, final int insideHeight)
                {
                    return new Dimension(insideWidth + ButtonShape.BORDER_2, insideHeight + ButtonShape.BORDER_2);
                }

                /**
                 * Draw the shape <br>
                 * <br>
                 * <b>Parent documentation:</b><br>
                 * {@inheritDoc}
                 *
                 * @param x
                 *           Up left corner X
                 * @param y
                 *           Up left corner Y
                 * @param width
                 *           Shape width
                 * @param height
                 *           Shape height
                 * @param color
                 *           Border color
                 * @param parent
                 *           Image parent where draw
                 * @see ButtonShape#draw(int, int, int, int, int, JHelpImage)
                 */
                @Override
                public void draw(
                        final int x, final int y, final int width, final int height, final int color,
                        final JHelpImage parent)
                {
                    final boolean draw = parent.isDrawMode();

                    if (!draw)
                    {
                        parent.startDrawMode();
                    }

                    parent.drawRectangle(x, y, width, height, color);

                    if (!draw)
                    {
                        parent.endDrawMode();
                    }
                }

                /**
                 * Fill the shape <br>
                 * <br>
                 * <b>Parent documentation:</b><br>
                 * {@inheritDoc}
                 *
                 * @param x
                 *           Up left corner X
                 * @param y
                 *           Up left corner Y
                 * @param width
                 *           Shape width
                 * @param height
                 *           Shape height
                 * @param color
                 *           Color used for fill
                 * @param parent
                 *           Image parent where draw
                 * @see ButtonShape#fill(int, int, int, int, int, JHelpImage)
                 */
                @Override
                public void fill(
                        final int x, final int y, final int width, final int height, final int color,
                        final JHelpImage parent)
                {
                    final boolean draw = parent.isDrawMode();

                    if (!draw)
                    {
                        parent.startDrawMode();
                    }

                    parent.fillRectangle(x, y, width, height, color);

                    if (!draw)
                    {
                        parent.endDrawMode();
                    }
                }

                /**
                 * Fill the shape with a {@link JHelpDrawable} <br>
                 * <br>
                 * <b>Parent documentation:</b><br>
                 * {@inheritDoc}
                 *
                 * @param x
                 *           Up left corner X
                 * @param y
                 *           Up left corner Y
                 * @param width
                 *           Shape width
                 * @param height
                 *           Shape height
                 * @param drawable
                 *           Image drawable used for fill
                 * @param parent
                 *           Image parent where draw
                 * @see ButtonShape#fill(int, int, int, int, JHelpDrawable, JHelpImage)
                 */
                @Override
                public void fill(
                        final int x, final int y, final int width, final int height, final JHelpDrawable drawable,
                        final JHelpImage parent)
                {
                    drawable.fillRectangle(x, y, width, height, parent);
                }
            },
    /**
     * Round rectangle button
     */
    ROUND_RECTANGLE()
            {
                /**
                 * Compute the arc size
                 *
                 * @param insideWidth
                 *           Internal desired width
                 * @param insideHeight
                 *           Internal desired height
                 * @return Arc size
                 */
                private int computeArc(final int insideWidth, final int insideHeight)
                {
                    return Math.max(16, Math.min(insideWidth, insideHeight) >> 3);
                }

                /**
                 * Compute arc size from total dimension
                 *
                 * @param width
                 *           Total width
                 * @param height
                 *           Total height
                 * @return Arc size
                 */
                private int computeReverseArc(final int width, final int height)
                {
                    final int min = Math.min(width, height);
                    return Math.max(16, (7 * min) >> 4);
                }

                /**
                 * Compute the position of up left corner inside the shape <br>
                 * <br>
                 * <b>Parent documentation:</b><br>
                 * {@inheritDoc}
                 *
                 * @param insideWidth
                 *           Internal width desired
                 * @param insideHeight
                 *           Internal height desired
                 * @return Up left point position
                 * @see ButtonShape#computeShapeInsideUpLeftCorner(int, int)
                 */
                @Override
                public Point computeShapeInsideUpLeftCorner(final int insideWidth, final int insideHeight)
                {
                    final int arc = this.computeArc(insideWidth, insideHeight);
                    return new Point(arc + ButtonShape.BORDER, arc + ButtonShape.BORDER);
                }

                /**
                 * Compute the total size of the shape <br>
                 * <br>
                 * <b>Parent documentation:</b><br>
                 * {@inheritDoc}
                 *
                 * @param insideWidth
                 *           Internal width desired
                 * @param insideHeight
                 *           Internal height desired
                 * @return Total size
                 * @see ButtonShape#computeShapeTotalSize(int, int)
                 */
                @Override
                public Dimension computeShapeTotalSize(final int insideWidth, final int insideHeight)
                {
                    final int more = this.computeArc(insideWidth, insideHeight) << 1;
                    return new Dimension(insideWidth + more + ButtonShape.BORDER_2,
                                         insideHeight + more + ButtonShape.BORDER_2);
                }

                /**
                 * Draw the shape <br>
                 * <br>
                 * <b>Parent documentation:</b><br>
                 * {@inheritDoc}
                 *
                 * @param x
                 *           Up left corner X
                 * @param y
                 *           Up left corner Y
                 * @param width
                 *           Shape width
                 * @param height
                 *           Shape height
                 * @param color
                 *           Border color
                 * @param parent
                 *           Image parent where draw
                 * @see ButtonShape#draw(int, int, int, int, int, JHelpImage)
                 */
                @Override
                public void draw(
                        final int x, final int y, final int width, final int height, final int color,
                        final JHelpImage parent)
                {
                    final boolean draw = parent.isDrawMode();
                    final int arc = this.computeReverseArc(width - ButtonShape.BORDER_2,
                                                           height - ButtonShape.BORDER_2);

                    if (!draw)
                    {
                        parent.startDrawMode();
                    }

                    parent.drawRoundRectangle(x, y, width, height, arc, arc, color);

                    if (!draw)
                    {
                        parent.endDrawMode();
                    }
                }

                /**
                 * Fill the shape <br>
                 * <br>
                 * <b>Parent documentation:</b><br>
                 * {@inheritDoc}
                 *
                 * @param x
                 *           Up left corner X
                 * @param y
                 *           Up left corner Y
                 * @param width
                 *           Shape width
                 * @param height
                 *           Shape height
                 * @param color
                 *           Color used for fill
                 * @param parent
                 *           Image parent where draw
                 * @see ButtonShape#fill(int, int, int, int, int, JHelpImage)
                 */
                @Override
                public void fill(
                        final int x, final int y, final int width, final int height, final int color,
                        final JHelpImage parent)
                {
                    final boolean draw = parent.isDrawMode();
                    final int arc = this.computeReverseArc(width - ButtonShape.BORDER_2,
                                                           height - ButtonShape.BORDER_2);

                    if (!draw)
                    {
                        parent.startDrawMode();
                    }

                    parent.fillRoundRectangle(x, y, width, height, arc, arc, color);

                    if (!draw)
                    {
                        parent.endDrawMode();
                    }
                }

                /**
                 * Fill the shape with a {@link JHelpDrawable} <br>
                 * <br>
                 * <b>Parent documentation:</b><br>
                 * {@inheritDoc}
                 *
                 * @param x
                 *           Up left corner X
                 * @param y
                 *           Up left corner Y
                 * @param width
                 *           Shape width
                 * @param height
                 *           Shape height
                 * @param drawable
                 *           Image drawable used for fill
                 * @param parent
                 *           Image parent where draw
                 * @see ButtonShape#fill(int, int, int, int, JHelpDrawable, JHelpImage)
                 */
                @Override
                public void fill(
                        final int x, final int y, final int width, final int height, final JHelpDrawable drawable,
                        final JHelpImage parent)
                {
                    final int arc = this.computeReverseArc(width - ButtonShape.BORDER_2, height - ButtonShape.BORDER_2);
                    drawable.fillRoundRectangle(x, y, width, height, arc, arc, parent);
                }
            },
    /**
     * "Sausage" (Rectangle with 2 sides are half circles) button
     */
    SAUSAGE()
            {
                /**
                 * Compute the position of up left corner inside the shape <br>
                 * <br>
                 * <b>Parent documentation:</b><br>
                 * {@inheritDoc}
                 *
                 * @param insideWidth
                 *           Internal width desired
                 * @param insideHeight
                 *           Internal height desired
                 * @return Up left point position
                 * @see ButtonShape#computeShapeInsideUpLeftCorner(int, int)
                 */
                @Override
                public Point computeShapeInsideUpLeftCorner(final int insideWidth, final int insideHeight)
                {
                    if (insideWidth >= insideHeight)
                    {
                        return new Point(ButtonShape.BORDER + (insideHeight >> 1), ButtonShape.BORDER);
                    }

                    return new Point(ButtonShape.BORDER, ButtonShape.BORDER + (insideWidth >> 1));
                }

                /**
                 * Compute the total size of the shape <br>
                 * <br>
                 * <b>Parent documentation:</b><br>
                 * {@inheritDoc}
                 *
                 * @param insideWidth
                 *           Internal width desired
                 * @param insideHeight
                 *           Internal height desired
                 * @return Total size
                 * @see ButtonShape#computeShapeTotalSize(int, int)
                 */
                @Override
                public Dimension computeShapeTotalSize(final int insideWidth, final int insideHeight)
                {
                    if (insideWidth >= insideHeight)
                    {
                        return new Dimension(insideWidth + insideHeight + ButtonShape.BORDER_2,
                                             insideHeight + ButtonShape.BORDER_2);
                    }

                    return new Dimension(insideWidth + ButtonShape.BORDER_2,
                                         insideHeight + insideWidth + ButtonShape.BORDER_2);
                }

                /**
                 * Draw the shape <br>
                 * <br>
                 * <b>Parent documentation:</b><br>
                 * {@inheritDoc}
                 *
                 * @param x
                 *           Up left corner X
                 * @param y
                 *           Up left corner Y
                 * @param width
                 *           Shape width
                 * @param height
                 *           Shape height
                 * @param color
                 *           Border color
                 * @param parent
                 *           Image parent where draw
                 * @see ButtonShape#draw(int, int, int, int, int, JHelpImage)
                 */
                @Override
                public void draw(
                        final int x, final int y, final int width, final int height, final int color,
                        final JHelpImage parent)
                {
                    final boolean draw = parent.isDrawMode();
                    final int     arc  = Math.min(width, height);

                    if (!draw)
                    {
                        parent.startDrawMode();
                    }

                    parent.drawRoundRectangle(x, y, width, height, arc, arc, color);

                    if (!draw)
                    {
                        parent.endDrawMode();
                    }
                }

                /**
                 * Fill the shape <br>
                 * <br>
                 * <b>Parent documentation:</b><br>
                 * {@inheritDoc}
                 *
                 * @param x
                 *           Up left corner X
                 * @param y
                 *           Up left corner Y
                 * @param width
                 *           Shape width
                 * @param height
                 *           Shape height
                 * @param color
                 *           Color used for fill
                 * @param parent
                 *           Image parent where draw
                 * @see ButtonShape#fill(int, int, int, int, int, JHelpImage)
                 */
                @Override
                public void fill(
                        final int x, final int y, final int width, final int height, final int color,
                        final JHelpImage parent)
                {
                    final boolean draw = parent.isDrawMode();
                    final int     arc  = Math.min(width, height);

                    if (!draw)
                    {
                        parent.startDrawMode();
                    }

                    parent.fillRoundRectangle(x, y, width, height, arc, arc, color);

                    if (!draw)
                    {
                        parent.endDrawMode();
                    }

                }

                /**
                 * Fill the shape with a {@link JHelpDrawable} <br>
                 * <br>
                 * <b>Parent documentation:</b><br>
                 * {@inheritDoc}
                 *
                 * @param x
                 *           Up left corner X
                 * @param y
                 *           Up left corner Y
                 * @param width
                 *           Shape width
                 * @param height
                 *           Shape height
                 * @param drawable
                 *           Image drawable used for fill
                 * @param parent
                 *           Image parent where draw
                 * @see ButtonShape#fill(int, int, int, int, JHelpDrawable, JHelpImage)
                 */
                @Override
                public void fill(
                        final int x, final int y, final int width, final int height, final JHelpDrawable drawable,
                        final JHelpImage parent)
                {
                    final int arc = Math.min(width, height);
                    drawable.fillRoundRectangle(x, y, width, height, arc, arc, parent);
                }
            };
    /**
     * Border size
     */
    public static final int BORDER   = 3;
    /**
     * Border twice size
     */
    public static final int BORDER_2 = ButtonShape.BORDER << 1;

    /**
     * Create a new instance of ButtonShape
     */
    ButtonShape()
    {
    }

    /**
     * Compute the position of up left corner inside the shape
     *
     * @param insideWidth  Internal width desired
     * @param insideHeight Internal height desired
     * @return Up left point position
     */
    public abstract Point computeShapeInsideUpLeftCorner(final int insideWidth, final int insideHeight);

    /**
     * Compute the total size of the shape
     *
     * @param insideWidth  Internal width desired
     * @param insideHeight Internal height desired
     * @return Total size
     */
    public abstract Dimension computeShapeTotalSize(final int insideWidth, final int insideHeight);

    /**
     * Draw the shape
     *
     * @param x      Up left corner X
     * @param y      Up left corner Y
     * @param width  Shape width
     * @param height Shape height
     * @param color  Border color
     * @param parent Image parent where draw
     */
    public abstract void draw(
            final int x, final int y, final int width, final int height, final int color, final JHelpImage parent);

    /**
     * Fill the shape
     *
     * @param x      Up left corner X
     * @param y      Up left corner Y
     * @param width  Shape width
     * @param height Shape height
     * @param color  Color used for fill
     * @param parent Image parent where draw
     */
    public abstract void fill(
            final int x, final int y, final int width, final int height, final int color, final JHelpImage parent);

    /**
     * Fill the shape with a {@link JHelpDrawable}
     *
     * @param x        Up left corner X
     * @param y        Up left corner Y
     * @param width    Shape width
     * @param height   Shape height
     * @param drawable Image drawable used for fill
     * @param parent   Image parent where draw
     */
    public abstract void fill(
            final int x, final int y, final int width, final int height, final JHelpDrawable drawable,
            final JHelpImage parent);
}
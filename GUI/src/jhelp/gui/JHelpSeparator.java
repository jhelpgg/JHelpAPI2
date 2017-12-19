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

/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * Separator you can choose its color and its thick
 *
 * @author JHelp <br>
 */
public class JHelpSeparator
        extends JComponent
{
    /**
     * Separator color
     */
    private final Color     color;
    /**
     * Indicates if horizontal separation
     */
    private final boolean   horizontal;
    /**
     * Margin each side the separator
     */
    private final int       margin;
    /**
     * Computed preferred size
     */
    private final Dimension preferedSize;
    /**
     * Indicates if preferred size is valid (will not change) or need to be recomputed
     */
    private       boolean   preferedSizeValid;
    /**
     * Separator thickness
     */
    private final int       thickness;

    /**
     * Create horizontal black 1 pixel thick separator
     */
    public JHelpSeparator()
    {
        this(Color.BLACK, true, 1, 1);
    }

    /**
     * Create black 1 pixel thick separator
     *
     * @param horizontal Indicates if horizontal (<code>true</code>) or vertical (<code>false</code>)
     */
    public JHelpSeparator(final boolean horizontal)
    {
        this(Color.BLACK, horizontal, 1, 1);
    }

    /**
     * Create 1 pixel thick separator
     *
     * @param color      Separator color
     * @param horizontal Indicates if horizontal (<code>true</code>) or vertical (<code>false</code>)
     */
    public JHelpSeparator(final Color color, final boolean horizontal)
    {
        this(color, horizontal, 1, 1);
    }

    /**
     * Create a new instance of JHelpSeparator
     *
     * @param color      Separator color
     * @param horizontal Indicates if horizontal (<code>true</code>) or vertical (<code>false</code>)
     * @param thickness  Separator thickness
     */
    public JHelpSeparator(final Color color, final boolean horizontal, final int thickness)
    {
        this(color, horizontal, thickness, thickness);
    }

    /**
     * Create a new instance of JHelpSeparator
     *
     * @param color      Separator color
     * @param horizontal Indicates if horizontal (<code>true</code>) or vertical (<code>false</code>)
     * @param thickness  Separator thickness
     * @param margin     Margin each side of separator
     */
    public JHelpSeparator(final Color color, final boolean horizontal, final int thickness, final int margin)
    {
        if (color == null)
        {
            throw new NullPointerException("color mustn't be null");
        }

        this.color = color;
        this.horizontal = horizontal;
        this.thickness = Math.max(1, thickness);
        this.margin = Math.max(2, margin);

        int width, height;

        if (horizontal)
        {
            width = this.thickness;
            height = this.thickness + (this.margin << 1);
        }
        else
        {
            width = this.thickness + (this.margin << 1);
            height = this.thickness;
        }

        this.preferedSizeValid = false;
        this.preferedSize = new Dimension(width, height);
        this.setSize(this.preferedSize);
        this.setPreferredSize(this.preferedSize);
        this.setMinimumSize(this.preferedSize);
        this.setMaximumSize(this.preferedSize);
    }

    /**
     * Paint the separator <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param g Graphics for paint
     * @see JComponent#paintComponent(Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g)
    {
        final int width  = this.getWidth();
        final int height = this.getHeight();
        g.setColor(this.getBackground());
        g.fillRect(0, 0, width, height);
        g.setColor(this.color);

        if (this.horizontal)
        {
            g.fillRoundRect(0, (height - this.thickness) >> 1, width, this.thickness, this.thickness, this.thickness);
        }
        else
        {
            g.fillRoundRect((width - this.thickness) >> 1, 0, this.thickness, height, this.thickness, this.thickness);
        }
    }

    /**
     * Compute the component preferred size <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Computed preferred size
     * @see JComponent#getPreferredSize()
     */
    @Override
    public Dimension getPreferredSize()
    {
        final Container parent = this.getParent();

        if ((parent != null) && (!this.preferedSizeValid))
        {
            // Note : During layout the parent may have first a zero size, so the computed preferred size is just temporary
            // If zero size happen (depends on layouts in hierarchy) we are sure to be recalled when size is know, and this time
            // the preferred size can be consider has valid
            int width  = parent.getWidth();
            int height = parent.getHeight();

            if (this.horizontal)
            {
                height = this.thickness + (this.margin << 1);
                this.preferedSizeValid = width > 0;
            }
            else
            {
                width = this.thickness + (this.margin << 1);
                this.preferedSizeValid = height > 0;
            }

            this.preferedSize.width = width;
            this.preferedSize.height = height;
        }

        return this.preferedSize;
    }
}
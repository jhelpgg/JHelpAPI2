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
 */
package jhelp.util.samples.common.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.WithFixedSize;

/**
 * Label with a {@link JHelpImage}<br>
 * <br>
 *
 * @author JHelp
 */
public class SampleLabelJHelpImageCenter
        extends JComponent
        implements WithFixedSize
{
    /**
     * Selected color
     */
    private static final Color SELECTED = new Color(255, 128, 64, 32);

    /**
     * Label height
     */
    private int        height;
    /**
     * Image to draw
     */
    private JHelpImage image;
    /**
     * Selected state
     */
    private boolean selected = false;
    /**
     * Label width
     */
    private int width;

    /**
     * Constructs LabelBufferedImage
     */
    public SampleLabelJHelpImageCenter()
    {
        this(128, 128);
    }

    /**
     * Create a new instance of LabelBufferedImage with specified width and height
     *
     * @param width  Width
     * @param height Height
     */
    public SampleLabelJHelpImageCenter(final int width, final int height)
    {
        this.width = width;
        this.height = height;

        final Dimension dimension = new Dimension(width, height);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);
        this.setMinimumSize(dimension);
    }

    /**
     * Create a new instance of LabelJHelpImage with image
     *
     * @param image Image to draw
     */
    public SampleLabelJHelpImageCenter(final JHelpImage image)
    {
        if (image == null)
        {
            throw new NullPointerException("The image couldn't be null");
        }

        this.image = image;

        this.width = image.getWidth();
        this.height = image.getHeight();

        final Dimension dimension = new Dimension(this.width, this.height);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);
        this.setMinimumSize(dimension);
    }

    /**
     * Paint the label
     *
     * @param g Graphics environment
     * @see JComponent#paintComponent(Graphics)
     */
    @Override
    protected void paintComponent(final Graphics g)
    {
        final int width  = this.getWidth();
        final int height = this.getHeight();

        if (this.image != null)
        {
            g.drawImage(this.image.getImage(), (width - this.image.getWidth()) >> 1,
                        (height - this.image.getHeight()) >> 1, this);
        }

        if (this.selected == true)
        {
            g.setColor(SampleLabelJHelpImageCenter.SELECTED);
            g.fillRect(0, 0, width, height);
        }
    }

    /**
     * Give the label size <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Label size
     * @see WithFixedSize#fixedSize()
     */
    @Override
    public Dimension fixedSize()
    {
        return new Dimension(this.width, this.height);
    }

    /**
     * Image to draw
     *
     * @return Image to draw
     */
    public JHelpImage getJHelpImage()
    {
        return this.image;
    }

    /**
     * Change the image to draw
     *
     * @param image New image to draw
     */
    public void setJHelpImage(final JHelpImage image)
    {
        if (image == null)
        {
            throw new NullPointerException("bufferedImage mustn't be null");
        }

        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();

        final Dimension dimension = new Dimension(this.width, this.height);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);
        this.setMinimumSize(dimension);
        this.repaint();
    }

    /**
     * Indicates if label is selected
     *
     * @return {@code true} if label is selected
     */
    public boolean isSelected()
    {
        return this.selected;
    }

    /**
     * Change label selection state
     *
     * @param selected New selection state
     */
    public void setSelected(final boolean selected)
    {
        this.selected = selected;

        this.refresh();
    }

    /**
     * Refresh the label
     */
    public void refresh()
    {
        if (this.image != null)
        {
            this.image.update();
        }

        this.repaint();
    }

    /**
     * Remove current image
     */
    public void removeImage()
    {
        this.image = null;
        this.width = 128;
        this.height = 128;
        final Dimension dimension = new Dimension(128, 128);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);
        this.setMinimumSize(dimension);
        this.repaint();
    }

    /**
     * Remove current image, but keep label current size
     */
    public void removeImageWithoutChangeSize()
    {
        this.image = null;
        this.repaint();
    }
}
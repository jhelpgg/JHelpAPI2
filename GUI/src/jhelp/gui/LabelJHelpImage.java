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
package jhelp.gui;

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
public class LabelJHelpImage
        extends JComponent
        implements WithAdditionalInformation, WithFixedSize
{
    /**
     * Selected color
     */
    private static final Color SELECTED = new Color(255, 128, 64, 32);

    /**
     * Some additional information
     */
    private Object              additionalInformation;
    /**
     * Label height
     */
    private int                 height;
    /**
     * Image to draw
     */
    private JHelpImage          image;
    /**
     * Something that draw over the image
     */
    private OverLabelJHelpImage overLabelJHelpImage;
    /**
     * Indicates if image resize to fit to the component size
     */
    private boolean             resize;
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
    public LabelJHelpImage()
    {
        this(128, 128);
    }

    /**
     * Create a new instance of LabelBufferedImage with specified width and height
     *
     * @param width  Width
     * @param height Height
     */
    public LabelJHelpImage(final int width, final int height)
    {
        this.width = width;
        this.height = height;
        this.resize = false;

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
    public LabelJHelpImage(final JHelpImage image)
    {
        if (image == null)
        {
            throw new NullPointerException("The image couldn't be null");
        }

        this.image = image;

        this.width = image.getWidth();
        this.height = image.getHeight();
        this.resize = false;

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

        final int px = Math.max(1, Math.min(20, width / 10));
        final int py = Math.max(1, Math.min(20, height / 10));

        boolean blackX = true;
        boolean blackY = true;

        for (int y = height; y > 0; y -= py)
        {
            blackX = true;

            for (int x = width; x > 0; x -= px)
            {
                g.setColor(blackX == blackY
                           ? Color.BLACK
                           : Color.WHITE);
                g.fillRect(x - px, y - py, px, py);

                blackX = !blackX;
            }

            blackY = !blackY;
        }

        if (this.image != null)
        {
            if (this.resize)
            {
                g.drawImage(this.image.getImage(), 0, 0, width, height, this);
            }
            else
            {
                g.drawImage(this.image.getImage(), 0, 0, this);
            }
        }

        if (this.selected)
        {
            g.setColor(LabelJHelpImage.SELECTED);
            g.fillRect(0, 0, width, height);
        }

        if (this.overLabelJHelpImage != null)
        {
            this.overLabelJHelpImage.draw(g, width, height);
        }
    }

    /**
     * Get the additional information <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Additional information
     * @see jhelp.gui.WithAdditionalInformation#additionalInformation()
     */
    @Override
    public Object additionalInformation()
    {
        return this.additionalInformation;
    }

    /**
     * Modify additional information <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param information New information
     * @see jhelp.gui.WithAdditionalInformation#additionalInformation(Object)
     */
    @Override
    public void additionalInformation(final Object information)
    {
        this.additionalInformation = information;
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
    public JHelpImage image()
    {
        return this.image;
    }

    /**
     * Change the image to draw
     *
     * @param image New image to draw
     */
    public void image(final JHelpImage image)
    {
        if (image == null)
        {
            throw new NullPointerException("bufferedImage mustn't be null");
        }

        this.image = image;

        // if(this.resize == false)
        {
            this.width = image.getWidth();
            this.height = image.getHeight();
            final Dimension dimension = new Dimension(this.width, this.height);
            this.setSize(dimension);
            this.setPreferredSize(dimension);
            this.setMaximumSize(dimension);
            this.setMinimumSize(dimension);
        }

        this.repaint();
    }

    /**
     * Obtain the object draws over the image
     *
     * @return Object draws over the image
     */
    public OverLabelJHelpImage overLabelJHelpImage()
    {
        return this.overLabelJHelpImage;
    }

    /**
     * Define the object draws over the image.<br>
     * Use {@code null} to remove any objects
     *
     * @param overLabelJHelpImage Object to draw over
     */
    public void overLabelJHelpImage(final OverLabelJHelpImage overLabelJHelpImage)
    {
        this.overLabelJHelpImage = overLabelJHelpImage;
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

        if (!this.resize)
        {
            this.width = 128;
            this.height = 128;
            final Dimension dimension = new Dimension(128, 128);
            this.setSize(dimension);
            this.setPreferredSize(dimension);
            this.setMaximumSize(dimension);
            this.setMinimumSize(dimension);
        }

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

    /**
     * Indicates if image resize to fit to the component size
     *
     * @return {@code true} if image resize to fit to the component size
     */
    public boolean resize()
    {
        return this.resize;
    }

    /**
     * Let image draw as it own size or resize it to fit component size
     *
     * @param resize {@code true} to resize. {@code false} to not resize
     */
    public void resize(final boolean resize)
    {
        this.resize = resize;
    }

    /**
     * Indicates if label is selected
     *
     * @return {@code true} if label is selected
     */
    public boolean selected()
    {
        return this.selected;
    }

    /**
     * Change label selection state
     *
     * @param selected New selection state
     */
    public void selected(final boolean selected)
    {
        if (this.selected == selected)
        {
            return;
        }

        this.selected = selected;

        this.refresh();
    }
}
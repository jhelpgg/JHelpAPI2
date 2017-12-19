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

package jhelp.gui.smooth;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import jhelp.util.gui.JHelpImage;

/**
 * Component that draw one image
 *
 * @author JHelp
 */
public class JHelpLabelImageSmooth
        extends JHelpComponentSmooth
{
    /**
     * Lock for synchronization
     */
    private final Object lock = new Object();
    /**
     * Original image to show
     */
    private JHelpImage original;
    /**
     * Resized image to fit the current layout of the component
     */
    private JHelpImage precomputed;

    /**
     * Create a new instance of JHelpLabelImageSmooth with no image
     */
    public JHelpLabelImageSmooth()
    {
    }

    /**
     * Create a new instance of JHelpLabelImageSmooth
     *
     * @param image Image to draw
     */
    public JHelpLabelImageSmooth(final JHelpImage image)
    {
        this.image(image);
    }

    /**
     * Compute the resized image
     *
     * @param width  Resized image width
     * @param height Resized image height
     */
    private void precomputeImage(final int width, final int height)
    {
        if ((this.precomputed != null) && (this.precomputed.getWeight() == width) &&
            (this.precomputed.getHeight() == height))
        {
            return;
        }

        this.precomputed = null;

        if (this.original != null)
        {
            this.precomputed = JHelpImage.createResizedImage(this.original, width, height);
        }
    }

    /**
     * Draw the component <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param image        Image parent where draw the component
     * @param x            X
     * @param y            Y
     * @param width        Width
     * @param height       Height
     * @param parentWidth  Parent width
     * @param parentHeight Parent height
     * @see JHelpComponentSmooth#paint(JHelpImage, int, int, int, int, int, int)
     */
    @Override
    protected void paint(
            final JHelpImage image, int x, int y, int width, int height, final int parentWidth, final int parentHeight)
    {
        this.drawBackground(image, x, y, width, height);
        // Drawing background may have change the bounds
        final Rectangle bounds = this.bounds();
        x = bounds.x;
        y = bounds.y;
        width = bounds.width;
        height = bounds.height;

        synchronized (this.lock)
        {
            this.precomputeImage(Math.max(1, width), Math.max(1, height));

            if (this.precomputed != null)
            {
                image.drawImage(x, y, this.precomputed);
            }
        }
    }

    /**
     * Compute the component preferred size <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Preferred size
     * @see JHelpComponentSmooth#preferredSizeInternal()
     */
    @Override
    protected Dimension preferredSizeInternal()
    {
        synchronized (this.lock)
        {
            if (this.original == null)
            {
                return new Dimension(64, 64);
            }

            return new Dimension(this.original.getWidth(), this.original.getHeight());
        }
    }

    /**
     * The original image to draw
     *
     * @return Image to draw
     */
    public JHelpImage image()
    {
        return this.original;
    }

    /**
     * Defines/change/remove the image
     *
     * @param image New image or {@code null} for remove image
     */
    public void image(final JHelpImage image)
    {
        synchronized (this.lock)
        {
            this.original = image;
            this.precomputed = null;
        }
    }

    /**
     * Force the component to refresh to show last image changes
     */
    public void refresh()
    {
        synchronized (this.lock)
        {
            this.precomputed = null;
        }
    }

    public Point undoScale(final int mouseX, final int mouseY)
    {
        synchronized (this.lock)
        {
            if ((this.original == null) || (this.precomputed == null))
            {
                return new Point(mouseX, mouseY);
            }

            return new Point((mouseX * this.original.getWidth()) / this.precomputed.getWidth(), //
                             (mouseY * this.original.getHeight()) / this.precomputed.getHeight());
        }
    }
}
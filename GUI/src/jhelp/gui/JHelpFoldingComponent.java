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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JComponent;

import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.UtilImage;
import jhelp.util.gui.UtilImage.WayTriangle;

/**
 * Component for show folding bar in {@link JHelpFoldablePanel}
 *
 * @author JHelp <br>
 */
class JHelpFoldingComponent
        extends JComponent
{
    /**
     * Margin between text and triangle that show folding status
     */
    private static final int MARGIN_TRIANGLE = 16;
    /**
     * Triangle (that show folding status) size
     */
    private static final int TRIANGLE_SIZE   = 8;
    /**
     * Fold state
     */
    private       boolean      fold;
    /**
     * Fold location
     */
    private final FoldLocation foldLocation;
    /**
     * Font for text
     */
    private       JHelpFont    font;
    /**
     * Image with text and triangle
     */
    private       JHelpImage   image;
    /**
     * Synchronization lock
     */
    private final Object       lock;
    /**
     * Text draw
     */
    private final String       text;

    /**
     * Create a new instance of JHelpFoldingComponent
     *
     * @param foldLocation Fold location
     * @param text         Text to draw
     * @param font         Font to use
     * @param fold         Fold status
     */
    JHelpFoldingComponent(final FoldLocation foldLocation, final String text, final JHelpFont font, final boolean fold)
    {
        this.lock = new Object();
        this.foldLocation = foldLocation;
        this.text = text;
        this.font = font;
        this.fold = fold;
        this.updateImage();
    }

    /**
     * Update the image with text and triangle
     */
    private void updateImage()
    {
        if (this.font == null)
        {
            // Because of parent constructor this method can be called before the font was set in our constructor
            return;
        }

        // Collect the information
        int   foreground = 0xFF000000;
        Color color      = this.getBackground();

        if (color != null)
        {
            color.getRGB();
        }

        color = this.getForeground();

        if (color != null)
        {
            foreground = color.getRGB();
        }

        // Create the text
        JHelpImage imageText = this.font.createImage(this.text, foreground, 0);

        if (!this.foldLocation.horizontal())
        {
            // Change orientation in vertical bars
            imageText = imageText.rotate270();
        }

        // Compute witch triangle to draw and final image size
        WayTriangle wayTriangle;
        int         width;
        int         height;

        switch (this.foldLocation)
        {
            case TOP:
                width = imageText.getWidth() + JHelpFoldingComponent.MARGIN_TRIANGLE +
                        JHelpFoldingComponent.TRIANGLE_SIZE;
                height = Math.max(imageText.getHeight(), JHelpFoldingComponent.TRIANGLE_SIZE);

                if (this.fold)
                {
                    wayTriangle = WayTriangle.DOWN;
                }
                else
                {
                    wayTriangle = WayTriangle.UP;
                }
                break;
            case RIGHT:
                width = Math.max(imageText.getWidth(), JHelpFoldingComponent.TRIANGLE_SIZE);
                height = imageText.getHeight() + JHelpFoldingComponent.MARGIN_TRIANGLE +
                         JHelpFoldingComponent.TRIANGLE_SIZE;

                if (this.fold)
                {
                    wayTriangle = WayTriangle.RIGHT;
                }
                else
                {
                    wayTriangle = WayTriangle.LEFT;
                }
                break;
            case LEFT:
                width = Math.max(imageText.getWidth(), JHelpFoldingComponent.TRIANGLE_SIZE);
                height = imageText.getHeight() + JHelpFoldingComponent.MARGIN_TRIANGLE +
                         JHelpFoldingComponent.TRIANGLE_SIZE;

                if (this.fold)
                {
                    wayTriangle = WayTriangle.LEFT;
                }
                else
                {
                    wayTriangle = WayTriangle.RIGHT;
                }
                break;
            case BOTTOM:
                width = imageText.getWidth() + JHelpFoldingComponent.MARGIN_TRIANGLE +
                        JHelpFoldingComponent.TRIANGLE_SIZE;
                height = Math.max(imageText.getHeight(), JHelpFoldingComponent.TRIANGLE_SIZE);

                if (this.fold)
                {
                    wayTriangle = WayTriangle.UP;
                }
                else
                {
                    wayTriangle = WayTriangle.DOWN;
                }
                break;
            default:
                return;
        }

        synchronized (this.lock)
        {
            // Create the final image
            this.image = new JHelpImage(width, height);
            this.image.startDrawMode();

            if (this.foldLocation.horizontal())
            {
                UtilImage.fillTriangle(0, (height - JHelpFoldingComponent.TRIANGLE_SIZE) >> 1,
                                       JHelpFoldingComponent.TRIANGLE_SIZE, this.image, wayTriangle,
                                       foreground);
                this.image.drawImage(JHelpFoldingComponent.TRIANGLE_SIZE + JHelpFoldingComponent.MARGIN_TRIANGLE,
                                     (height - imageText.getHeight()) >> 1, imageText);
            }
            else
            {
                UtilImage.fillTriangle((width - JHelpFoldingComponent.TRIANGLE_SIZE) >> 1, 0,
                                       JHelpFoldingComponent.TRIANGLE_SIZE, this.image, wayTriangle,
                                       foreground);
                this.image.drawImage((width - imageText.getWidth()),
                                     JHelpFoldingComponent.TRIANGLE_SIZE + JHelpFoldingComponent.MARGIN_TRIANGLE,
                                     imageText);
            }

            this.image.endDrawMode();
        }

        // Update the bar
        final Dimension dimension = new Dimension(width, height);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);
        this.setMinimumSize(dimension);
        this.invalidate();
        this.repaint();
        this.revalidate();
    }

    /**
     * Called when bar draw <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param graphics Graphics for draw
     * @see JComponent#paintComponent(Graphics)
     */
    @Override
    protected void paintComponent(final Graphics graphics)
    {
        final int   width         = this.getWidth();
        final int   height        = this.getHeight();
        final Color background    = this.getBackground();
        int         backgroundRGB = 0xCAFEFACE;

        if (background != null)
        {
            backgroundRGB = background.getRGB();
            graphics.setColor(background);
        }
        else
        {
            graphics.setColor(new Color(backgroundRGB, true));
        }

        graphics.fillRect(0, 0, width, height);

        synchronized (this.lock)
        {
            if (this.image != null)
            {
                graphics.drawImage(this.image.getImage(), (width - this.image.getWidth()) >> 1,
                                   (height - this.image.getHeight()) >> 1, null);
            }
        }
    }

    /**
     * Change text color <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param foreground New text color
     * @see JComponent#setForeground(Color)
     */
    @Override
    public void setForeground(final Color foreground)
    {
        super.setForeground(foreground);
        this.updateImage();
    }

    /**
     * Change the bar background color <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param backround New background color
     * @see JComponent#setBackground(Color)
     */
    @Override
    public void setBackground(final Color backround)
    {
        super.setBackground(backround);
        this.updateImage();
    }

    /**
     * Change the font use for draw text <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param font New font
     * @see JComponent#setFont(Font)
     */
    @Override
    public void setFont(final Font font)
    {
        this.setFont(new JHelpFont(font, false));
    }

    /**
     * Change fold status
     *
     * @param fold New fold status
     */
    public void setFold(final boolean fold)
    {
        if (this.fold == fold)
        {
            return;
        }

        this.fold = fold;
        this.updateImage();
    }

    /**
     * Change the font use for draw text
     *
     * @param font New font
     */
    public void setFont(final JHelpFont font)
    {
        if (font == null)
        {
            throw new NullPointerException("font mustn't be null");
        }

        this.font = font;
        super.setFont(this.font.getFont());
        this.updateImage();
    }
}
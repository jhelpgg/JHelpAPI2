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
package jhelp.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JComponent;
import jhelp.util.debug.Debug;
import jhelp.util.gui.GIF;
import jhelp.util.gui.JHelpImage;

/**
 * Label with a buffered image.<br>
 * You can put an {@link GIF} animation in it<br>
 * You can also draw a rectangle over image <br>
 * <br>
 *
 * @author JHelp
 */
public class LabelBufferedImage
        extends JComponent
{
    /**
     * For synchronize if {@link GIF} animation is play
     */
    static final ReentrantLock reentrantLock = new ReentrantLock(true);

    /**
     * Refresh {@link GIF} animation <br>
     * <br>
     * Last modification : 16 juin 2010<br>
     * Version 0.0.0<br>
     *
     * @author JHelp
     */
    class Refresh
            extends TimerTask
    {
        /**
         * Refresh {@link GIF} animation
         *
         * @see TimerTask#run()
         */
        @Override
        public void run()
        {
            LabelBufferedImage.reentrantLock.lock();

            try
            {
                if ((LabelBufferedImage.this.gif != null) && (LabelBufferedImage.this.gif.isUsable()))
                {
                    LabelBufferedImage.this.index =
                            (LabelBufferedImage.this.index + 1) % LabelBufferedImage.this.gif.numberOfImage();

                    LabelBufferedImage.this.bufferedImage = LabelBufferedImage.this.gif.getImage(
                            LabelBufferedImage.this.index);
                    final Dimension dimension = new Dimension(LabelBufferedImage.this.gif.getWidth(),
                                                              LabelBufferedImage.this.gif.getHeight());
                    LabelBufferedImage.this.setSize(dimension);
                    LabelBufferedImage.this.setPreferredSize(dimension);
                    LabelBufferedImage.this.setMaximumSize(dimension);
                    LabelBufferedImage.this.repaint();
                }
                else
                {
                    LabelBufferedImage.this.gif = null;
                    LabelBufferedImage.this.bufferedImage = null;
                    LabelBufferedImage.this.timer.cancel();
                    LabelBufferedImage.this.timer.purge();
                    LabelBufferedImage.this.timer = null;

                    LabelBufferedImage.this.repaint();
                }
            }
            catch (final Exception exception)
            {
                Debug.exception(exception);
            }

            LabelBufferedImage.reentrantLock.unlock();
        }
    }

    /**
     * Rectangle border
     */
    private Color     color;
    /**
     * Rectangle back ground
     */
    private Color     colorBack;
    /**
     * Maximum height
     */
    private int       maxHeight;
    /**
     * Maximum width
     */
    private int       maxWidth;
    /**
     * Rectangle over
     */
    private Rectangle rectangle;

    /**
     * Buffered image carry
     */
    JHelpImage bufferedImage;

    /**
     * {@link GIF} animation
     */
    GIF   gif;
    /**
     * Actual index in {@link GIF} animation
     */
    int   index;
    /**
     * Timer for {@link GIF} animation
     */
    Timer timer;

    /**
     * Constructs LabelBufferedImage
     */
    public LabelBufferedImage()
    {
        this(128, 128);
    }

    /**
     * Constructs the label
     *
     * @param bufferedImage Buffered image
     */
    public LabelBufferedImage(final BufferedImage bufferedImage)
    {
        if (bufferedImage == null)
        {
            throw new NullPointerException("The bufferedImage couldn't be null");
        }
        this.bufferedImage = JHelpImage.createImage(bufferedImage);
        bufferedImage.flush();
        final Dimension dimension = new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);
        // this.setMinimumSize(dimension);

        this.maxWidth = this.maxHeight = Integer.MAX_VALUE;
    }

    /**
     * Create a new instance of LabelBufferedImage with specified width and height
     *
     * @param width  Width
     * @param height Height
     */
    public LabelBufferedImage(final int width, final int height)
    {
        final Dimension dimension = new Dimension(width, height);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);
        this.setMinimumSize(dimension);

        this.maxWidth = this.maxHeight = Integer.MAX_VALUE;
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

        if (this.bufferedImage != null)
        {
            g.drawImage(this.bufferedImage.getImage(), 0, 0, width, height, this);
        }

        if ((this.rectangle != null) && (this.color != null) && (this.colorBack != null))
        {
            g.setColor(this.colorBack);
            g.fillRect(this.rectangle.x - 1, this.rectangle.y - 1, this.rectangle.width + 2, 3);
            g.fillRect((this.rectangle.x + this.rectangle.width) - 1, this.rectangle.y - 1, 3,
                       this.rectangle.height + 2);
            g.fillRect(this.rectangle.x - 1, this.rectangle.y - 1, 3, this.rectangle.height + 2);
            g.fillRect(this.rectangle.x - 1, (this.rectangle.y + this.rectangle.height) - 1, this.rectangle.width + 2,
                       3);
            g.setColor(this.color);
            g.drawRect(this.rectangle.x, this.rectangle.y, this.rectangle.width, this.rectangle.height);
        }
    }

    /**
     * Add a rectangle
     *
     * @param rectangle Rectangle to add
     * @param color     Border color
     * @param colorBak  Background color
     */
    public void drawRectangle(final Rectangle rectangle, final Color color, final Color colorBak)
    {
        this.rectangle = rectangle;
        this.color = color;
        this.colorBack = colorBak;
        this.repaint();
    }

    /**
     * Return bufferedImage
     *
     * @return bufferedImage
     */
    public JHelpImage getBufferedImage()
    {
        return this.bufferedImage;
    }

    /**
     * Modify bufferedImage
     *
     * @param bufferedImage New bufferedImage value
     */
    public void setBufferedImage(final BufferedImage bufferedImage)
    {
        if (bufferedImage == null)
        {
            throw new NullPointerException("bufferedImage mustn't be null");
        }

        LabelBufferedImage.reentrantLock.lock();

        this.gif = null;
        if (this.timer != null)
        {
            this.timer.cancel();
            this.timer.purge();

            this.timer = null;
        }

        this.bufferedImage = JHelpImage.createImage(bufferedImage);
        final Dimension dimension = new Dimension(Math.min(bufferedImage.getWidth(), this.maxWidth),
                                                  Math.min(bufferedImage.getHeight(), this.maxHeight));
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);
        // this.setMinimumSize(dimension);
        this.repaint();

        LabelBufferedImage.reentrantLock.unlock();
    }

    /**
     * Refresh the label
     */
    public void refresh()
    {
        LabelBufferedImage.reentrantLock.lock();

        this.repaint();

        LabelBufferedImage.reentrantLock.unlock();
    }

    /**
     * Remove current image
     */
    public void removeImage()
    {
        LabelBufferedImage.reentrantLock.lock();

        if (this.timer != null)
        {
            this.timer.cancel();
            this.timer.purge();

            this.timer = null;
        }

        this.gif = null;
        this.bufferedImage = null;
        final Dimension dimension = new Dimension(128, 128);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);
        this.setMinimumSize(dimension);
        this.repaint();

        LabelBufferedImage.reentrantLock.unlock();
    }

    /**
     * Remove the rectangle
     */
    public void removeRectangle()
    {
        this.rectangle = null;
        this.color = null;
        this.colorBack = null;
        this.repaint();
    }

    /**
     * Draw a {@link GIF} animation
     *
     * @param gif {@link GIF} animation
     */
    public void setGIF(final GIF gif)
    {
        if (gif == null)
        {
            throw new NullPointerException("gif mustn't be null");
        }

        LabelBufferedImage.reentrantLock.lock();

        this.gif = gif;
        this.bufferedImage = gif.getImage(0);
        this.index = 0;

        final Dimension dimension = new Dimension(gif.getWidth(), gif.getHeight());
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);
        this.repaint();

        if ((this.timer == null) && (this.gif.numberOfImage() > 1))
        {
            this.timer = new Timer();
            this.timer.schedule(new Refresh(), 250, 250);
        }

        LabelBufferedImage.reentrantLock.unlock();
    }

    /**
     * Modify the maximum width an height
     *
     * @param maxWidth  New maximum width
     * @param maxHeight New maximum height
     */
    public void setMaxSize(final int maxWidth, final int maxHeight)
    {
        this.maxWidth = Math.max(128, maxWidth);
        this.maxHeight = Math.max(128, maxHeight);

        final Dimension dimension = this.getSize();
        dimension.width = Math.min(this.maxWidth, dimension.width);
        dimension.height = Math.min(this.maxHeight, dimension.height);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);

        this.refresh();
    }
}
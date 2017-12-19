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

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import jhelp.util.gui.JHelpImage;
import jhelp.util.thread.Mutex;

/**
 * Component with an Image
 *
 * @author JHelp
 */
public class ComponentJHelpImage
        extends JComponent
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 8891074012246116719L;
    /**
     * Image carry
     */
    private       JHelpImage image;
    /**
     * Image over
     */
    private       JHelpImage imageOver;
    /**
     * Synchronization mutex
     */
    private final Mutex      mutex;

    /**
     * Create a new instance of ComponentJHelpImage with empty image
     *
     * @param width  Image width
     * @param height Image height
     * @throws IllegalArgumentException if width or height are <=0
     */
    public ComponentJHelpImage(final int width, final int height)
    {
        this(new JHelpImage(width, height));
    }

    /**
     * Create a new instance of ComponentJHelpImage
     *
     * @param image Image to show
     * @throws NullPointerException if image is null
     */
    public ComponentJHelpImage(final JHelpImage image)
    {
        this.mutex = new Mutex();
        this.image(image);
    }

    /**
     * Create a new instance of ComponentJHelpImage with image fill of one color
     *
     * @param width  Image width
     * @param height Image height
     * @param color  Color to fill the image
     * @throws IllegalArgumentException if width or height are <=0
     */
    public ComponentJHelpImage(final int width, final int height, final int color)
    {
        this(new JHelpImage(width, height, color));
    }

    /**
     * Update component size
     */
    void updateSize()
    {
        this.mutex.playInCriticalSectionVoid(() ->
                                             {
                                                 if ((this.image.getWidth() != this.getWidth()) ||
                                                     (this.image.getHeight() != this.getHeight()))
                                                 {
                                                     JHelpImage temp = new JHelpImage(this.getWidth(),
                                                                                      this.getHeight());

                                                     temp.startDrawMode();
                                                     temp.fillRectangleScaleBetter(0, 0, this.getWidth(),
                                                                                   this.getHeight(), this.image);
                                                     temp.endDrawMode();

                                                     this.image.transfertSpritesTo(temp);

                                                     this.image(temp);

                                                     if (this.imageOver != null)
                                                     {
                                                         this.imageOver.unregister(this);

                                                         temp = new JHelpImage(this.getWidth(), this.getHeight());

                                                         temp.startDrawMode();
                                                         temp.fillRectangleScaleBetter(0, 0, this.getWidth(),
                                                                                       this.getHeight(),
                                                                                       this.imageOver);
                                                         temp.endDrawMode();

                                                         this.imageOver.transfertSpritesTo(temp);

                                                         this.imageOver = temp;
                                                         this.imageOver.register(this);

                                                         this.invalidate();
                                                         this.revalidate();
                                                         this.repaint();
                                                     }
                                                 }
                                             });
    }

    /**
     * Call by garbage collector when want free the memory <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @throws Throwable On issue
     * @see Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable
    {
        this.image = null;
        this.imageOver = null;
        super.finalize();
    }

    /**
     * Call each time the component need to be painted <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param graphics Graphics environment
     * @see JComponent#paintComponent(Graphics)
     */
    @Override
    protected final void paintComponent(final Graphics graphics)
    {
        if (this.image != null)
        {
            graphics.drawImage(this.image.getImage(), 0, 0, this);
        }

        if (this.imageOver != null)
        {
            graphics.drawImage(this.imageOver.getImage(), 0, 0, this);
        }
    }

    /**
     * The image draw
     *
     * @return The image draw
     */
    public final JHelpImage image()
    {
        return this.mutex.playInCriticalSection(() -> this.image);
    }

    /**
     * Change the image
     *
     * @param image New image
     * @throws NullPointerException if image is {@code null}
     */
    public final void image(final JHelpImage image)
    {
        if (this.image != null)
        {
            this.image.unregister(this);
        }

        this.image = image;
        this.image.register(this);

        final Dimension dimension = new Dimension(image.getWidth(), image.getHeight());

        this.setPreferredSize(dimension);
        this.setMinimumSize(dimension);
        this.setMaximumSize(dimension);

        this.invalidate();
        this.revalidate();
        this.repaint();
    }

    /**
     * Image over
     *
     * @return Image over
     */
    public JHelpImage imageOver()
    {
        return this.imageOver;
    }

    /**
     * Indicates if image over is active/visible
     *
     * @return {@code true} if image over is active/visible
     */
    public boolean imageOverActive()
    {
        return this.imageOver != null;
    }

    /**
     * Change image over active state
     *
     * @param active New image over state
     */
    public void imageOverActive(final boolean active)
    {
        if (!active)
        {
            if (this.imageOver != null)
            {
                this.imageOver.unregister(this);
            }

            this.imageOver = null;

            this.invalidate();
            this.revalidate();
            this.repaint();

            return;
        }

        if (this.imageOver != null)
        {
            return;
        }

        this.imageOver = new JHelpImage(this.image.getWidth(), this.image.getHeight(), 0);
        this.imageOver.register(this);

        this.invalidate();
        this.revalidate();
        this.repaint();
    }
}
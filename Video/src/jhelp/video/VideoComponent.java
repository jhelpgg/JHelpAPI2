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

package jhelp.video;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

/**
 * Component that show a video
 */
public class VideoComponent extends JComponent
{
    /**
     * Indicates if vdeo have fix size
     */
    private       boolean fixSize;
    /**
     * Video carry
     */
    private final Video   video;

    /**
     * Create component
     *
     * @param width  Video width
     * @param height Video height
     */
    public VideoComponent(int width, int height)
    {
        this.video = new Video(width, height);
        this.fixSize = true;
        final Dimension size = new Dimension(width, height);
        this.setSize(size);
        this.setMinimumSize(size);
        this.setMaximumSize(size);
        this.setPreferredSize(size);
    }

    /**
     * Calls the UI delegate's paint method, if the UI delegate
     * is non-<code>null</code>.  We pass the delegate a copy of the
     * <code>Graphics</code> object to protect the rest of the
     * paint code from irrevocable changes
     * (for example, <code>Graphics.translate</code>).
     * <p>
     * If you override this in a subclass you should not make permanent
     * changes to the passed in <code>Graphics</code>. For example, you
     * should not alter the clip <code>Rectangle</code> or modify the
     * transform. If you need to do these operations you may find it
     * easier to create a new <code>Graphics</code> from the passed in
     * <code>Graphics</code> and manipulate it. Further, if you do not
     * invoker super's implementation you must honor the opaque property,
     * that is
     * if this component is opaque, you must completely fill in the background
     * in a non-opaque color. If you do not honor the opaque property you
     * will likely see visual artifacts.
     * <p>
     * The passed in <code>Graphics</code> object might
     * have a transform other than the identify transform
     * installed on it.  In this case, you might get
     * unexpected results if you cumulatively apply
     * another transform.
     *
     * @param graphics the <code>Graphics</code> object to protect
     * @see #paint
     */
    @Override
    protected void paintComponent(final Graphics graphics)
    {
        if (this.fixSize)
        {
            final Dimension videoSize = this.video.size();
            final int       x         = (this.getWidth() - videoSize.width) >> 1;
            final int       y         = (this.getHeight() - videoSize.height) >> 1;
            graphics.drawImage(this.video.video().getImage(), x, y, this);
        }
        else
        {
            graphics.drawImage(this.video.video().getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }

    /**
     * Indicates if video have fix size.<br>
     * Fix size means video will keep the same size and don't care about component size.<br>
     * Not fix size means video will fit component size
     *
     * @return {@code true} if size is fix
     */
    public boolean fixSize()
    {
        return this.fixSize;
    }

    /**
     * Change fix size status<br>
     * Fix size means video will keep the same size and don't care about component size.<br>
     * Not fix size means video will fit component size
     *
     * @param fixSize New fix size status
     */
    public void fixSize(boolean fixSize)
    {
        if (this.fixSize != fixSize)
        {
            this.fixSize = fixSize;
            this.repaint();
        }
    }

    /**
     * Video access for load, play, pause or stop the embed video
     *
     * @return Video access
     */
    public Video video()
    {
        return this.video;
    }
}

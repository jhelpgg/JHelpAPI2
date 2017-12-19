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
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JFrame;
import javax.swing.JPanel;
import jhelp.util.gui.GIF;
import jhelp.util.gui.JHelpImage;
import jhelp.util.util.Utilities;

/**
 * Glass pane you can add for draw something over a frame while a long operation waiting to finish before give the hand to the
 * user <br>
 * <br>
 * Last modification : 23 avr. 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public class WaitingGlassPane
        extends JPanel
{
    /**
     * Gray semi transparent color to add over frame while animation effect
     */
    private static final Color GRAY = new Color(128, 128, 128, 128);

    /**
     * Add a waiting glass plane to a frame
     *
     * @param frame To add the waiting glass plane
     * @return The added waiting glass pane to be able initialize and manipulate it (Don't use it for an other frame, for an
     * other frame, you have to use this method again)
     */
    public static WaitingGlassPane addWaitingGlassTo(final JFrame frame)
    {
        return new WaitingGlassPane(frame);
    }

    /**
     * Repaint the glass paint <br>
     * <br>
     * Last modification : 16 juin 2010<br>
     * Version 0.0.0<br>
     *
     * @author JHelp
     */
    private class ThreadRepaint
            extends Thread
    {
        /**
         * Create a new instance of ThreadRepaint
         */
        public ThreadRepaint()
        {
        }

        /**
         * Repaint the glass paint
         *
         * @see Thread#run()
         */
        @Override
        public void run()
        {
            long time;
            while (WaitingGlassPane.this.threadRepaint != null)
            {
                time = System.currentTimeMillis();
                if (WaitingGlassPane.this.arrayListWaitingImage.size() > 0)
                {
                    WaitingGlassPane.this.index =
                            (WaitingGlassPane.this.index + 1) % WaitingGlassPane.this.arrayListWaitingImage.size();
                }

                WaitingGlassPane.this.ready = false;
                WaitingGlassPane.this.repaint();

                while (!WaitingGlassPane.this.ready)
                {
                    Utilities.sleep(10);
                }

                time = (1000 / WaitingGlassPane.this.fps) - (System.currentTimeMillis() - time);
                if (time < 1)
                {
                    time = 1;
                }

                Utilities.sleep(time);
            }
        }
    }

    /**
     * Frame under the glass pane
     */
    private final JFrame                frame;
    /**
     * Synchronization lock
     */
    private final ReentrantLock         lock;
    /**
     * Animation images
     */
    final         ArrayList<JHelpImage> arrayListWaitingImage;
    /**
     * Actual refresh FPS
     */
    int           fps;
    /**
     * Animation index
     */
    int           index;
    /**
     * Indicates if refresh is done
     */
    boolean       ready;
    /**
     * Repaint the component to update animation
     */
    ThreadRepaint threadRepaint;

    /**
     * Constructs WaitingGlassPane
     *
     * @param frame Frame under the glass pane
     */
    private WaitingGlassPane(final JFrame frame)
    {
        this.lock = new ReentrantLock();

        this.frame = frame;

        this.setOpaque(false);
        this.setDoubleBuffered(false);
        this.setIgnoreRepaint(true);
        this.frame.setGlassPane(this);
        this.arrayListWaitingImage = new ArrayList<JHelpImage>();
        this.fps = 25;
        this.index = 0;
    }

    /**
     * Add image to animation
     *
     * @param bufferedImage Image to add
     */
    public void addWaitingImage(final BufferedImage bufferedImage)
    {
        bufferedImage.flush();
        this.arrayListWaitingImage.add(JHelpImage.createImage(bufferedImage));
    }

    /**
     * Add gif in animation
     *
     * @param gif Gif to add
     */
    public void addWaitingImage(final GIF gif)
    {
        final int count = gif.numberOfImage();

        for (int i = 0; i < count; i++)
        {
            this.arrayListWaitingImage.add(gif.getImage(i));
        }
    }

    /**
     * Actual FPS
     *
     * @return FPS
     */
    public int getFPS()
    {
        return this.fps;
    }

    /**
     * Change the FPS
     *
     * @param fps new FPS
     */
    public void setFPS(int fps)
    {
        if (fps < 1)
        {
            fps = 1;
        }

        if (fps > 100)
        {
            fps = 100;
        }

        this.fps = fps;
    }

    /**
     * Draw the glass pane
     *
     * @param g Graphics context
     * @see javax.swing.JComponent#paint(Graphics)
     */
    @Override
    public void paint(final Graphics g)
    {
        super.paint(g);

        if (this.threadRepaint == null)
        {
            this.ready = true;

            return;
        }

        final int width  = this.getWidth();
        final int height = this.getHeight();

        g.setColor(WaitingGlassPane.GRAY);
        g.fillRect(0, 0, width, height);

        if (this.arrayListWaitingImage.isEmpty())
        {
            this.ready = true;

            return;
        }

        JHelpImage bufferedImage = this.arrayListWaitingImage.get(this.index);
        g.drawImage(bufferedImage.getImage(), (width - bufferedImage.getWidth()) >> 1,
                    (height - bufferedImage.getHeight()) >> 1, null);
        bufferedImage = null;

        this.ready = true;
    }

    /**
     * Start the waiting animation
     */
    public void startWaiting()
    {
        this.lock.lock();

        if (this.threadRepaint == null)
        {
            this.setVisible(true);

            this.threadRepaint = new ThreadRepaint();
            this.threadRepaint.start();

            this.frame.setEnabled(false);
        }

        this.lock.unlock();
    }

    /**
     * Stop the waiting animation
     */
    public void stopWaiting()
    {
        this.lock.lock();

        if (this.threadRepaint != null)
        {
            this.frame.setEnabled(true);
            this.threadRepaint = null;
            this.setVisible(false);

            this.frame.repaint();
        }

        this.lock.unlock();
    }
}
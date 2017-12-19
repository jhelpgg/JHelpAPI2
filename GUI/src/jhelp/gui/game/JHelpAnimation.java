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

import java.util.ArrayList;
import jhelp.util.gui.JHelpSprite;
import jhelp.util.math.Math2;

/**
 * An animation.<br>
 * Animation is cut in frames.<br>
 * Each frame is a new sprite position.<br>
 * Between each frame, its specify an interpolation way see {@link Interpolation}
 *
 * @author JHelp
 */
public final class JHelpAnimation
{
    /**
     * Represents a frame
     *
     * @author JHelp
     */
    static class Frame
    {
        /**
         * Frame position
         */
        public final int           frame;
        /**
         * Interpolation to use to go to this frame
         */
        public final Interpolation interpolation;
        /**
         * Sprite position X
         */
        public final int           x;
        /**
         * Sprite position Y
         */
        public final int           y;

        /**
         * Create a new instance of Frame
         *
         * @param frame         Frame position
         * @param x             Sprite position X
         * @param y             Sprite position Y
         * @param interpolation Interpolation to use to go to this frame
         */
        public Frame(final int frame, final int x, final int y, final Interpolation interpolation)
        {
            this.frame = frame;
            this.x = x;
            this.y = y;
            this.interpolation = interpolation;
        }
    }

    /**
     * Default exponential interpolation : <br>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;e<sup>t</sup>-1<br>f(t)=----<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;e-1</code>
     */
    public static final Interpolation INTERPOLATION_EXPONENTIAL = new Interpolation()
    {
        /**
         * <code>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;e<sup>t</sup>-1<br>f(t)=----<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;e-1</code>
         * <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param t
         *           parameter
         * @return f(t)
         * @see Interpolation#interpolation(double)
         */
        @Override
        public double interpolation(final double t)
        {
            return Math2.interpolationExponential(t);
        }
    };
    /**
     * Linear interpolation : <br>
     * <code>f(t)=t</code>
     */
    public static final Interpolation INTERPOLATION_LINEAR      = new Interpolation()
    {
        /**
         * <code>f(t)=t</code> <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param t
         *           Parameter
         * @return f(t)
         * @see Interpolation#interpolation(double)
         */
        @Override
        public double interpolation(final double t)
        {
            return t;
        }
    };
    /**
     * Default logarithm interpolation : <br>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ln(t+1)<br>f(t)=-------<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ln(2)</code>
     */
    public static final Interpolation INTERPOLATION_LOGARITHM   = new Interpolation()
    {
        /**
         * <code>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ln(t+1)<br>f(t)=-------<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ln(2)</code>
         * <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param t
         *           Parameter
         * @return f(t)
         * @see Interpolation#interpolation(double)
         */
        @Override
        public double interpolation(final double t)
        {
            return Math2.interpolationLogarithm(t);
        }
    };
    /**
     * Default sinus interpolation : <br>
     * <code>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1&nbsp;+&nbsp;sin(t*&pi; - &pi;*0.5)<br>f(t)=----------------------<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2</code>
     */
    public static final Interpolation INTERPOLATION_SINUS       = new Interpolation()
    {
        /**
         * <code>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;1&nbsp;+&nbsp;sin(t*&pi; - &pi;*0.5)<br>f(t)=----------------------<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;2</code>
         * <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param t
         *           Parameter
         * @return f(t)
         * @see Interpolation#interpolation(double)
         */
        @Override
        public double interpolation(final double t)
        {
            return Math2.interpolationSinus(t);
        }
    };
    /**
     * Animation FPS (Frame Per Second)
     */
    private       int              fps;
    /**
     * Frames list
     */
    private final ArrayList<Frame> frames;
    /**
     * Last added frame
     */
    private       int              lastFrame;
    /**
     * Animation last play time
     */
    private       long             lastTime;
    /**
     * Animation start time
     */
    private       long             startTime;
    /**
     * Sprite start X
     */
    private       int              startX;
    /**
     * Sprite start Y
     */
    private       int              startY;

    /**
     * Create a new instance of JHelpAnimation with 25 frame per second
     */
    public JHelpAnimation()
    {
        this(25);
    }

    /**
     * Create a new instance of JHelpAnimation
     *
     * @param fps Frame per second
     */
    public JHelpAnimation(final int fps)
    {
        this.lastTime = this.startTime = -1;
        this.lastFrame = -1;

        this.frames = new ArrayList<Frame>();

        this.fps(fps);
    }

    /**
     * Animate a sprite
     *
     * @param sprite Sprite to animate
     * @return {@code true} if animation have to continue. {@code false} if animation terminate
     */
    boolean animate(final JHelpSprite sprite)
    {
        if (this.startTime < 0)
        {
            return false;
        }

        final int length = this.frames.size();

        if (length == 0)
        {
            this.lastTime = this.startTime = -1;
            return false;
        }

        final int framePosition = Math.round(((System.currentTimeMillis() - this.startTime) * this.fps) / 1000);
        int       indexEnd      = 0;
        Frame     frameEnd      = null;

        for (; indexEnd < length; indexEnd++)
        {
            frameEnd = this.frames.get(indexEnd);

            if (frameEnd.frame >= framePosition)
            {
                break;
            }
        }

        if (indexEnd >= length)
        {
            frameEnd = this.frames.get(length - 1);

            sprite.setPosition(frameEnd.x, frameEnd.y);

            this.lastTime = this.startTime = -1;

            return false;
        }

        assert frameEnd != null;
        if (framePosition == frameEnd.frame)
        {
            sprite.setPosition(frameEnd.x, frameEnd.y);

            if (indexEnd >= (length - 1))
            {
                this.startTime = -1;
            }

            return indexEnd < (length - 1);
        }

        int sx    = this.startX;
        int sy    = this.startY;
        int start = 0;

        final int ex  = frameEnd.x;
        final int ey  = frameEnd.y;
        final int end = frameEnd.frame;

        if (indexEnd > 0)
        {
            final Frame f = this.frames.get(indexEnd - 1);

            sx = f.x;
            sy = f.y;
            start = f.frame;
        }

        final double t = frameEnd.interpolation.interpolation(
                ((double) framePosition - (double) start) / ((double) end - (double) start));

        sprite.setPosition((int) Math.round(sx + ((ex - sx) * t)), (int) Math.round(sy + ((ey - sy) * t)));

        this.lastTime = System.currentTimeMillis();

        return true;
    }

    /**
     * Maintain the animation in pause
     */
    void pauseMode()
    {
        final long diff = System.currentTimeMillis() - this.lastTime;

        this.startTime += diff;
        this.lastTime += diff;
    }

    /**
     * Start the animation
     *
     * @param sprite Sprite to animate
     */
    void startAnimation(final JHelpSprite sprite)
    {
        this.lastTime = this.startTime = System.currentTimeMillis();

        this.startX = sprite.getX();
        this.startY = sprite.getY();
    }

    /**
     * Add a frame
     *
     * @param frame         Frame position
     * @param x             Sprite X position
     * @param y             Sprite Y postition
     * @param interpolation Interpolation to use to go to this position
     */
    public void addFrame(final int frame, final int x, final int y, final Interpolation interpolation)
    {
        if (this.startTime > 0)
        {
            throw new IllegalStateException("Can't add frame while animation playing");
        }

        if (frame <= this.lastFrame)
        {
            throw new IllegalArgumentException("frame can't be <= last added frame : " + this.lastFrame);
        }

        if (interpolation == null)
        {
            throw new NullPointerException("interpolation mustn't be null");
        }

        this.frames.add(new Frame(frame, x, y, interpolation));

        this.lastFrame = frame;
    }

    /**
     * Animation FPS (Frame Per Second)
     *
     * @return Animation FPS (Frame Per Second)
     */
    public int fps()
    {
        return this.fps;
    }

    /**
     * Change animation FPS (Frame Per Second)
     *
     * @param fps New animation FPS (Frame Per Second)
     */
    public void fps(final int fps)
    {
        if (this.startTime > 0)
        {
            throw new IllegalStateException("Can't change FPS while animation playing");
        }

        this.fps = Math.max(10, Math.min(100, fps));
    }

    /**
     * Indicates if animation playing
     *
     * @return {@code true} if animation playing
     */
    public boolean isPlaying()
    {
        return this.startTime > 0;
    }

    /**
     * Last added frame
     *
     * @return Last added frame
     */
    public int lastFrame()
    {
        return this.lastFrame;
    }

    /**
     * Force animation to stop
     */
    public void stopAnimation()
    {
        this.startTime = -1;
    }

    /**
     * An interpolation.<br>
     * It is a function f, that <br>
     * <table border=1>
     * <tr>
     * <td><code>[0, 1] -f-> [0, 1]<br>f(0) = 0<br>f(1) = 1<br>f is strictly increase in [0, 1]<br>f continues in [0, 1]</code></td>
     * </tr>
     * </table>
     *
     * @author JHelp
     */
    public interface Interpolation
    {
        /**
         * The function f, that <br>
         * <table border=1>
         * <tr>
         * <td>
         * <code>[0, 1] -f-> [0, 1]<br>f(0) = 0<br>f(1) = 1<br>f is strictly increase in [0, 1]<br>f continues in [0, 1]</code></td>
         * </tr>
         * </table>
         *
         * @param t Parameter
         * @return f(t)
         */
        double interpolation(double t);
    }
}
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
import java.awt.Rectangle;
import jhelp.gui.smooth.shape.ShadowLevel;
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpMask;
import jhelp.util.math.Math2;
import jhelp.util.text.UtilText;

/**
 * Smooth progress bar
 *
 * @author JHelp
 */
public class JHelpProgressSmooth
        extends JHelpComponentSmooth
{
    /**
     * Starting text
     */
    private static final String START_TEXT = "000000/999999 left 99:99:99.999 .";
    /**
     * Progress bar background color
     */
    private final int       background;
    /**
     * Progress bar border color
     */
    private final int       border;
    /**
     * Font to use
     */
    private final JHelpFont font;
    /**
     * Progress bar foreground color
     */
    private final int       foreground;
    /**
     * Maximum progress value
     */
    private       int       maximum;
    /**
     * Progress bar minimum height
     */
    private final int       mimnumHeight;
    /**
     * Progress bar minimum width
     */
    private final int       minimumWidth;
    /**
     * Actual progress value
     */
    private       int       progress;
    /**
     * Progress color
     */
    private final int       progressColor;
    /**
     * Time when progress started
     */
    private       long      startTime;
    /**
     * Mask for draw progression text
     */
    private       JHelpMask text;

    /**
     * Create a new instance of JHelpProgressSmooth
     *
     * @param progressColor Progression color
     * @param background    Background color
     * @param border        Border color
     * @param foreground    Foreground color
     * @param font          Font to use
     */
    public JHelpProgressSmooth(
            final int progressColor, final int background, final int border, final int foreground, final JHelpFont font)
    {
        this.progressColor = progressColor;
        this.background = background;
        this.foreground = foreground;
        this.border = border;
        this.font = font;
        this.progress = 0;
        this.maximum = 1;

        this.minimumWidth = font.stringWidth(JHelpProgressSmooth.START_TEXT) + 6;
        this.mimnumHeight = font.getHeight() + 6;
        this.text = font.createMask(JHelpProgressSmooth.START_TEXT);
        this.shadowLevel(ShadowLevel.NEAR);
    }

    /**
     * Draw the component <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param image        Image where draw
     * @param x            X on image
     * @param y            Y on image
     * @param width        Width where draw
     * @param height       Height where draw
     * @param parentWidth  Parent width
     * @param parentHeight Parent height
     * @see jhelp.gui.smooth.JHelpComponentSmooth#paint(JHelpImage, int, int, int, int, int, int)
     */
    @Override
    protected void paint(
            final JHelpImage image, final int x, final int y, final int width, final int height, final int parentWidth,
            final int parentHeight)
    {
        this.drawBackground(image, x, y, width, height);
        final Rectangle bounds = this.bounds();
        int             xx     = bounds.x + 1;
        int             yy     = bounds.y + ((bounds.height - this.mimnumHeight) >> 1);
        final int       w      = bounds.width - 2;
        final int       h      = this.mimnumHeight;
        image.fillRectangle(xx, yy, w, h, this.background);

        if (this.progress > 0)
        {
            image.fillRectangle(xx, yy, (w * this.progress) / this.maximum, h, this.progressColor);
        }

        image.drawRectangle(xx, yy, w, h, this.border);

        xx = bounds.x + ((bounds.width - this.text.getWidth()) >> 1);
        yy = bounds.y + ((bounds.height - this.text.getHeight()) >> 1);
        image.paintMask(xx, yy, this.text, this.foreground, 0, true);
    }

    /**
     * Preferred size <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Preferred size
     * @see jhelp.gui.smooth.JHelpComponentSmooth#preferredSizeInternal()
     */
    @Override
    protected Dimension preferredSizeInternal()
    {
        return new Dimension(this.minimumWidth, this.mimnumHeight);
    }

    /**
     * Start/restart progression
     *
     * @param maximum Maximum progression value
     */
    public void startProgress(final int maximum)
    {
        this.progress = 0;
        this.maximum = Math.max(1, maximum);
        this.text = this.font.createMask(UtilText.concatenate("0/", this.maximum, " left 99:99:99.999 ."));
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Update progression
     *
     * @param progress New progression value
     */
    public void updateProgress(final int progress)
    {
        this.progress = Math2.limit(progress, this.progress, this.maximum);

        if (this.progress == 0)
        {
            return;
        }

        int timeLeft = (int) (
                ((this.maximum - this.progress) * (System.currentTimeMillis() - this.startTime)) / this.progress);
        final int milliseconds = timeLeft % 1000;
        timeLeft /= 100;
        final int seconds = timeLeft % 60;
        timeLeft /= 60;
        final int minutes = timeLeft % 60;
        final int hours   = timeLeft / 60;

        String zeroMillseconds = "";

        if (milliseconds < 10)
        {
            zeroMillseconds = "00";
        }
        else if (milliseconds < 100)
        {
            zeroMillseconds = "0";
        }

        String zeroSeconds = "";

        if (seconds < 10)
        {
            zeroSeconds = "0";
        }

        String zeroMinutes = "";

        if (minutes < 10)
        {
            zeroMinutes = "0";
        }

        this.text = this.font.createMask(
                UtilText.concatenate(this.progress, "/", this.maximum, " left ", hours, ':', zeroMinutes, minutes, ':',
                                     zeroSeconds,
                                     seconds, '.', zeroMillseconds, milliseconds, " ."));
    }
}
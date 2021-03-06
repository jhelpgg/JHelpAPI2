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

package jhelp.util.gui.dynamic;

import java.awt.Dimension;
import java.util.List;
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpTextAlign;
import jhelp.util.gui.JHelpTextLineAlpha;
import jhelp.util.list.Pair;

/**
 * Apply an alpha mask to an animation.<br>
 * The animation can only be seen throw alpha mask holes.<br>
 * See  jhelp.util.samples.gui.SampleDynamicText for an example of usage and effect
 *
 * @author JHelp
 */
public final class AlphaMaskAnimated
        implements JHelpDynamicImageListener
{
    /**
     * Create an alpha masked animated with alpha mask based on text
     *
     * @param text            Text used for alpha mask
     * @param font            Text font
     * @param textAlign       Text alignment
     * @param colorBackground Background color
     * @return Created alpha masked
     */
    public static AlphaMaskAnimated createTextAnimated(
            final String text, final JHelpFont font,
            final JHelpTextAlign textAlign, final int colorBackground)
    {
        final Pair<List<JHelpTextLineAlpha>, Dimension> pair = font.computeTextLinesAlpha(text, textAlign);
        final JHelpImage alphaMask = new JHelpImage(pair.second.width,
                                                    pair.second.height);
        alphaMask.startDrawMode();

        for (final JHelpTextLineAlpha textLine : pair.first)
        {
            alphaMask.paintAlphaMask(textLine.getX(), textLine.getY(), textLine.getMask(), 0xFFFFFFFF, 0, false);
        }

        alphaMask.endDrawMode();
        return new AlphaMaskAnimated(alphaMask, colorBackground);
    }

    /**
     * Alpha mask to use
     */
    private final JHelpImage        alphaMask;
    /**
     * Dynamic image to play the animation
     */
    private final JHelpDynamicImage dynamicImage;
    /**
     * Image to see the masked animation
     */
    private final JHelpImage        resultImage;

    /**
     * Create a new instance of AlphaMaskAnimated
     *
     * @param alphaMask       Alpha mask to use
     * @param colorBackground Background color
     */
    public AlphaMaskAnimated(final JHelpImage alphaMask, final int colorBackground)
    {
        this.alphaMask = alphaMask;
        final int width  = alphaMask.getWidth();
        final int height = alphaMask.getHeight();
        this.dynamicImage = new JHelpDynamicImage(width, height);
        this.dynamicImage.dynamicImageListener = this;
        this.resultImage = new JHelpImage(width, height, colorBackground);
        this.refreshImage();
    }

    /**
     * Refresh the image
     */
    private void refreshImage()
    {
        this.resultImage.startDrawMode();
        this.resultImage.paintAlphaMask(0, 0, this.alphaMask, this.dynamicImage.getImage());
        this.resultImage.endDrawMode();
    }

    /**
     * Called each time dynamic image update <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param dynamicImage Dynamic image
     * @see jhelp.util.gui.dynamic.JHelpDynamicImageListener#dynamicImageUpdate(jhelp.util.gui.dynamic.JHelpDynamicImage)
     */
    @Override
    public void dynamicImageUpdate(final JHelpDynamicImage dynamicImage)
    {
        this.refreshImage();
    }

    /**
     * Dynamic animation to play animation in it
     *
     * @return Dynamic animation to play animation in it
     */
    public JHelpDynamicImage getDynamicImage()
    {
        return this.dynamicImage;
    }

    /**
     * Image to draw to see animation result
     *
     * @return Image to draw to see animation result
     */
    public JHelpImage getImage()
    {
        return this.resultImage;
    }
}
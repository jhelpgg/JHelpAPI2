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
import java.util.List;
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpRichText;
import jhelp.util.gui.JHelpTextAlign;
import jhelp.util.gui.JHelpTextLineAlpha;
import jhelp.util.list.Pair;
import jhelp.util.resources.ResourceText;

/**
 * Component that show a text.<br>
 * The drawing text source can be :
 * <ul>
 * <li>A simple String</li>
 * <li>A String transformed with a {@link JHelpRichText}</li>
 * <li>A String from {@link ResourceText}, in that case only the reference key is store</li>
 * <li>A String from {@link ResourceText} transformed via a {@link JHelpRichText}, in that case only the reference key
 * is store</li>
 * </ul>
 * It's possible to change the origin of the String, just keep in mind that if a {@link ResourceText} is used, the
 * String is a
 * key in it, and if no {@link ResourceText} the String is draw (Transformed via {@link JHelpRichText} or as is)
 *
 * @author JHelp
 */
public class JHelpLabelTextSmooth
        extends JHelpComponentSmooth
{
    /**
     * Font used to draw the text
     */
    private JHelpFont font;
    /**
     * Foreground/text color
     */
    private int       foreground;
    /**
     * Lock for synchronization
     */
    private final Object lock = new Object();
    /**
     * Precomputed image with the draw text
     */
    private JHelpImage     precomputed;
    /**
     * Resource where found that String consider as a key, if not {@code null}
     */
    private ResourceText   resourceText;
    /**
     * Rich text used for transform the String if not {@code null}
     */
    private JHelpRichText  richText;
    /**
     * Text or key of text
     */
    private String         text;
    /**
     * How align text inside the component
     */
    private JHelpTextAlign textAlign;

    /**
     * Create a new instance of JHelpLabelTextSmooth with empty text as is align at left
     */
    public JHelpLabelTextSmooth()
    {
        this("");
    }

    /**
     * Create a new instance of JHelpLabelTextSmooth with a text as is align at left
     *
     * @param text Text to draw
     */
    public JHelpLabelTextSmooth(final String text)
    {
        this(text, JHelpTextAlign.LEFT, JHelpConstantsSmooth.FONT_BODY_2);
    }

    /**
     * Create a new instance of JHelpLabelTextSmooth with text as is
     *
     * @param text      Text to draw
     * @param textAlign Text alignment
     * @param font      Font to use
     */
    public JHelpLabelTextSmooth(final String text, final JHelpTextAlign textAlign, final JHelpFont font)
    {
        if (text == null)
        {
            throw new NullPointerException("text mustn't be null");
        }

        if (textAlign == null)
        {
            throw new NullPointerException("textAlign mustn't be null");
        }

        if (font == null)
        {
            throw new NullPointerException("font mustn't be null");
        }

        this.backgroundAndShadowColor(JHelpConstantsSmooth.COLOR_WHITE);
        this.foreground = JHelpConstantsSmooth.COLOR_BLACK;
        this.text = text;
        this.richText = null;
        this.resourceText = null;
        this.precomputed = null;
        this.font = font;
        this.textAlign = textAlign;
    }

    /**
     * Create a new instance of JHelpLabelTextSmooth with text as is align at left
     *
     * @param text Text to draw
     * @param font Font to use
     */
    public JHelpLabelTextSmooth(final String text, final JHelpFont font)
    {
        this(text, JHelpTextAlign.LEFT, font);
    }

    /**
     * Create a new instance of JHelpLabelTextSmooth with text as is
     *
     * @param text      Text to draw
     * @param textAlign Text alignment
     */
    public JHelpLabelTextSmooth(final String text, final JHelpTextAlign textAlign)
    {
        this(text, textAlign, JHelpConstantsSmooth.FONT_BODY_2);
    }

    /**
     * Update/compute the pre-computed image
     */
    private void computeImage()
    {
        if (this.precomputed != null)
        {
            return;
        }

        String string = this.text;

        if (this.resourceText != null)
        {
            string = this.resourceText.getText(this.text);
        }

        if (this.richText != null)
        {
            this.precomputed = this.richText.createImage(string, this.font, this.foreground);
            return;
        }

        final Pair<List<JHelpTextLineAlpha>, Dimension> pair = this.font.computeTextLinesAlpha(string, this.textAlign);
        this.precomputed = new JHelpImage(pair.second.width, pair.second.height);
        this.precomputed.startDrawMode();

        for (final JHelpTextLineAlpha textLine : pair.first)
        {
            this.precomputed.paintAlphaMask(textLine.getX(), textLine.getY(), textLine.getMask(), this.foreground, 0,
                                            false);
        }

        this.precomputed.endDrawMode();
    }

    /**
     * Draw the component <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param image        Image where draw the component
     * @param x            X position
     * @param y            Y position
     * @param width        Width
     * @param height       Height
     * @param parentWidth  Parent width
     * @param parentHeight Parent height
     * @see JHelpComponentSmooth#paint(JHelpImage, int, int, int, int, int, int)
     */
    @Override
    protected void paint(
            final JHelpImage image, int x, int y, int width, int height, final int parentWidth,
            final int parentHeight)
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
            this.computeImage();

            int xx = x;

            switch (this.textAlign)
            {
                case CENTER:
                    xx = x + ((width - this.precomputed.getWidth()) >> 1);
                    break;
                case LEFT:
                    xx = x;
                    break;
                case RIGHT:
                    xx = x + (width - this.precomputed.getWidth());
                    break;
            }

            image.drawImage(xx, y + ((height - this.precomputed.getHeight()) >> 1), this.precomputed, true);
        }
    }

    /**
     * Compute preferred size <br>
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
            this.computeImage();

            return new Dimension(this.precomputed.getWidth(), this.precomputed.getHeight());
        }
    }

    /**
     * Text font
     *
     * @return Text font
     */
    public JHelpFont font()
    {
        return this.font;
    }

    /**
     * Change the text font
     *
     * @param font New font
     */
    public void font(final JHelpFont font)
    {
        if (font == null)
        {
            throw new NullPointerException("font mustn't be null");
        }

        synchronized (this.lock)
        {
            if (this.font.equals(font))
            {
                return;
            }

            this.font = font;
            this.precomputed = null;
        }
    }

    /**
     * Foreground color
     *
     * @return Text color
     */
    public int foreground()
    {
        return this.foreground;
    }

    /**
     * Change foreground color
     *
     * @param color New foreground color
     */
    public void foreground(final int color)
    {
        synchronized (this.lock)
        {
            if (this.foreground == color)
            {
                return;
            }

            this.foreground = color;
            this.precomputed = null;
        }
    }

    /**
     * Change/define/remove the resource text.<br>
     * If not {@code null}, the text become a key inside resources and changes if language change, if {@code null} the
     * text is
     * print as is
     *
     * @param resourceText Resource text to use OR {@code null} for no use Resource text
     */
    public void resourceText(final ResourceText resourceText)
    {
        synchronized (this.lock)
        {
            if (this.resourceText == resourceText)
            {
                return;
            }

            this.resourceText = resourceText;
            this.precomputed = null;
        }
    }

    /**
     * Rich text used for transformed the text<br>
     * May be {@code null} if none
     *
     * @return Rich text used or {@code null} if none
     */
    public JHelpRichText richText()
    {
        return this.richText;
    }

    /**
     * Defines/change/remove the {@code JHelpRichText} for transform the text
     *
     * @param richText Rich text to use or {@code null} to use no transformation
     */
    public void richText(final JHelpRichText richText)
    {
        synchronized (this.lock)
        {
            if (this.richText == richText)
            {
                return;
            }

            this.richText = richText;
            this.precomputed = null;
        }
    }

    /**
     * The text draw OR the key of text if come from {@link ResourceText}
     *
     * @return The text or its key
     */
    public String text()
    {
        return this.text;
    }

    /**
     * Change the text or the key of the text
     *
     * @param text New text or key of the text
     */
    public void text(final String text)
    {
        if (text == null)
        {
            throw new NullPointerException("text mustn't be null");
        }

        synchronized (this.lock)
        {
            this.text = text;
            this.precomputed = null;
        }
    }

    /**
     * Text alignment
     *
     * @return Text alignment
     */
    public JHelpTextAlign textAlign()
    {
        return this.textAlign;
    }

    /**
     * change text alignment
     *
     * @param textAlign New text alignment
     */
    public void textAlign(final JHelpTextAlign textAlign)
    {
        if (textAlign == null)
        {
            throw new NullPointerException("textAlign mustn't be null");
        }

        synchronized (this.lock)
        {
            if (textAlign == this.textAlign)
            {
                return;
            }

            this.textAlign = textAlign;
            this.precomputed = null;
        }
    }
}
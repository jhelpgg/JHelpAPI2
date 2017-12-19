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
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any
 * damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.gui.twoD;

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
import jhelp.util.resources.ResourceTextListener;

/**
 * Label with text in it
 *
 * @author JHelp
 */
public class JHelpLabelText2D
        extends JHelpComponent2D
        implements ResourceTextListener
{
    /**
     * Actual height
     */
    private int            actualHeight;
    /**
     * Actual width
     */
    private int            actualWidth;
    /**
     * Background color
     */
    private int            background;
    /**
     * Font to use
     */
    private JHelpFont      font;
    /**
     * Foreground color
     */
    private int            foreground;
    /**
     * Image text
     */
    private JHelpImage     imageText;
    /**
     * Resource key
     */
    private String         resourceKey;
    /**
     * Resource text
     */
    private ResourceText   resourceText;
    /**
     * Rich text to use
     */
    private JHelpRichText  richText;
    /**
     * Actual text
     */
    private String         text;
    /**
     * Text align
     */
    private JHelpTextAlign textAlign;

    /**
     * Create a new instance of JHelpLabelText2D
     */
    public JHelpLabelText2D()
    {
        this(JHelpFont.DEFAULT, null, "", JHelpTextAlign.LEFT, 0xFF000000, 0xFFFFFFFF);
    }

    /**
     * Create a new instance of JHelpLabelText2D
     *
     * @param font       Font to use
     * @param richText   Rich text
     * @param text       Actual text
     * @param textAlign  Text align
     * @param foreground Foreground color
     * @param background Background color
     */
    private JHelpLabelText2D(
            final JHelpFont font, final JHelpRichText richText, final String text,
            final JHelpTextAlign textAlign, final int foreground,
            final int background)
    {
        if (font == null)
        {
            this.font = JHelpFont.DEFAULT;
        }
        else
        {
            this.font = font;
        }

        this.richText = richText;

        if (text == null)
        {
            this.text = "";
        }
        else
        {
            this.text = text;
        }

        if (textAlign == null)
        {
            this.textAlign = JHelpTextAlign.LEFT;
        }
        else
        {
            this.textAlign = textAlign;
        }

        this.foreground = foreground;
        this.background = background;

        this.actualWidth = this.actualHeight = -1;
        this.updateTextImage();
    }

    /**
     * Create a new instance of JHelpLabelText2D
     *
     * @param font     Font to use
     * @param richText Rich text to use
     */
    public JHelpLabelText2D(final JHelpFont font, final JHelpRichText richText)
    {
        this(font, richText, "", JHelpTextAlign.LEFT, 0xFF000000, 0xFFFFFFFF);
    }

    /**
     * Create a new instance of JHelpLabelText2D
     *
     * @param font       Font to use
     * @param richText   Rich text to use
     * @param foreground Foregroud color
     * @param background Background color
     */
    public JHelpLabelText2D(
            final JHelpFont font, final JHelpRichText richText, final int foreground, final int background)
    {
        this(font, richText, "", JHelpTextAlign.LEFT, foreground, background);
    }

    /**
     * Create a new instance of JHelpLabelText2D
     *
     * @param font Font to use
     * @param text Text to print
     */
    public JHelpLabelText2D(final JHelpFont font, final String text)
    {
        this(font, null, text, JHelpTextAlign.LEFT, 0xFF000000, 0xFFFFFFFF);
    }

    /**
     * Create a new instance of JHelpLabelText2D
     *
     * @param font       Font to use
     * @param text       Text to print
     * @param foreground Foreground color
     * @param background Background color
     */
    public JHelpLabelText2D(final JHelpFont font, final String text, final int foreground, final int background)
    {
        this(font, null, text, JHelpTextAlign.LEFT, foreground, background);
    }

    /**
     * Create a new instance of JHelpLabelText2D
     *
     * @param font       Font to use
     * @param text       Text to print
     * @param textAlign  Text align
     * @param foreground Foreground color
     * @param background Background color
     */
    public JHelpLabelText2D(
            final JHelpFont font, final String text, final JHelpTextAlign textAlign, final int foreground,
            final int background)
    {
        this(font, null, text, textAlign, foreground, background);
    }

    /**
     * Create a new instance of JHelpLabelText2D based on rich text (Be able transform text to image, see
     * {@link JHelpRichText}
     *
     * @param richText Rich text to use
     */
    public JHelpLabelText2D(final JHelpRichText richText)
    {
        this(JHelpFont.DEFAULT, richText, "", JHelpTextAlign.LEFT, 0xFF000000, 0xFFFFFFFF);
    }

    /**
     * Create a new instance of JHelpLabelText2D based on rich text (Be able transform text to image, see
     * {@link JHelpRichText}
     *
     * @param richText   Rich text to use
     * @param foreground Foreground color
     * @param background Background color
     */
    public JHelpLabelText2D(final JHelpRichText richText, final int foreground, final int background)
    {
        this(JHelpFont.DEFAULT, richText, "", JHelpTextAlign.LEFT, foreground, background);
    }

    /**
     * Create a new instance of JHelpLabelText2D
     *
     * @param text Text to print
     */
    public JHelpLabelText2D(final String text)
    {
        this(JHelpFont.DEFAULT, null, text, JHelpTextAlign.LEFT, 0xFF000000, 0xFFFFFFFF);
    }

    /**
     * Create a new instance of JHelpLabelText2D
     *
     * @param text      Text to print
     * @param textAlign Text align
     */
    public JHelpLabelText2D(final String text, final JHelpTextAlign textAlign)
    {
        this(JHelpFont.DEFAULT, null, text, textAlign, 0xFF000000, 0xFFFFFFFF);
    }

    /**
     * Create a new instance of JHelpLabelText2D
     *
     * @param text       Text to print
     * @param textAlign  Text align
     * @param foreground Foreground color
     * @param background Background color
     */
    public JHelpLabelText2D(
            final String text, final JHelpTextAlign textAlign, final int foreground, final int background)
    {
        this(JHelpFont.DEFAULT, null, text, textAlign, foreground, background);
    }

    /**
     * Update image text
     */
    private void updateTextImage()
    {
        if (this.richText != null)
        {
            this.imageText = this.richText.createImage(this.text, this.font, this.foreground);
        }
        else
        {
            final Pair<List<JHelpTextLineAlpha>, Dimension> pair = this.font.computeTextLinesAlpha(this.text,
                                                                                                   this.textAlign);
            if ((this.imageText == null) ||
                (this.imageText.getWeight() != pair.second.width) ||
                (this.imageText.getHeight() != pair.second.height))
            {
                this.imageText = new JHelpImage(pair.second.width, pair.second.height);
            }

            this.imageText.startDrawMode();
            this.imageText.clear(0);

            for (final JHelpTextLineAlpha textLine : pair.first)
            {
                this.imageText.paintAlphaMask(textLine.getX(), textLine.getY(), textLine.getMask(), this.foreground, 0,
                                              false);
            }

            this.imageText.endDrawMode();
        }

        if ((this.imageText.getWidth() != this.actualWidth) || (this.imageText.getHeight() != this.actualHeight))
        {
            this.actualWidth = this.imageText.getWidth();
            this.actualHeight = this.imageText.getHeight();
            this.invalidate();
        }
    }

    /**
     * Compute label text preferred size <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param parentWidth  Parent width
     * @param parentHeight Parent height
     * @return Label text preferred size
     * @see JHelpComponent2D#computePreferredSize(int, int)
     */
    @Override
    protected Dimension computePreferredSize(final int parentWidth, final int parentHeight)
    {
        return new Dimension(this.actualWidth, this.actualHeight);
    }

    /**
     * Paint the label text <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param x      X location
     * @param y      Y location
     * @param parent Parent where draw
     * @see JHelpComponent2D#paint(int, int, JHelpImage)
     */
    @Override
    protected void paint(final int x, final int y, final JHelpImage parent)
    {
        final Rectangle bounds = this.bounds();
        parent.fillRectangle(x, y, bounds.width, bounds.height, this.background);
        int xx = x;
        switch (this.textAlign)
        {
            case CENTER:
                xx += (bounds.width - this.actualWidth) >> 1;
                break;
            case LEFT:
                break;
            case RIGHT:
                xx += bounds.width - this.actualWidth;
                break;
        }
        parent.drawImage(xx, y, this.imageText);
    }

    /**
     * Background color
     *
     * @return Background color
     */
    public int background()
    {
        return this.background;
    }

    /**
     * Change background color
     *
     * @param background New background color
     */
    public void background(final int background)
    {
        this.background = background;
        this.updateTextImage();
    }

    /**
     * Label text font
     *
     * @return Label text font
     */
    public JHelpFont font()
    {
        return this.font;
    }

    /**
     * Change text font
     *
     * @param font New text font
     */
    public void font(final JHelpFont font)
    {
        if (font == null)
        {
            this.font = JHelpFont.DEFAULT;
        }
        else
        {
            this.font = font;
        }

        this.updateTextImage();
    }

    /**
     * Foreground color
     *
     * @return Foreground color
     */
    public int foreground()
    {
        return this.foreground;
    }

    /**
     * Change text foreground
     *
     * @param foreground New text foreground
     */
    public void foreground(final int foreground)
    {
        this.foreground = foreground;
        this.updateTextImage();
    }

    /**
     * Link the label to resource, to print a resource text, and update it automaticaly if language change.<br>
     * To change/remove this link before call {@link #unlinkToResourceText()}
     *
     * @param resourceText Resource text
     * @param resourceKey  Resource key
     */
    public void linkToResourceText(final ResourceText resourceText, final String resourceKey)
    {
        if (resourceText == null)
        {
            throw new NullPointerException("resourceText mustn't be null");
        }

        if (resourceKey == null)
        {
            throw new NullPointerException("resourceKey mustn't be null");
        }

        if (this.resourceKey != null)
        {
            throw new IllegalStateException("Already linked to a resource text");
        }

        this.resourceText = resourceText;
        resourceText.register(this);
        this.text = resourceText.getText(resourceKey);
        this.updateTextImage();
    }

    /**
     * Resource key
     *
     * @return Resource key
     */
    public String resourceKey()
    {
        return this.resourceKey;
    }

    /**
     * Called when resource text launguage changed <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param resourceText Resource text where language changed
     * @see ResourceTextListener#resourceTextLanguageChanged(ResourceText)
     */
    @Override
    public void resourceTextLanguageChanged(final ResourceText resourceText)
    {
        this.text = this.resourceText.getText(this.resourceKey);
        this.updateTextImage();
    }

    /**
     * Assoiciated rich text
     *
     * @return Assoiciated rich text
     */
    public JHelpRichText richText()
    {
        return this.richText;
    }

    /**
     * Defines the rich text.<br>
     * Use {@code null}to not use rich text.<br>
     * See {@link JHelpRichText}
     *
     * @param richText Rich text to associate of {@code null}
     */
    public void richText(final JHelpRichText richText)
    {
        this.richText = richText;
        this.updateTextImage();
    }

    /**
     * Printed text
     *
     * @return Printed text
     */
    public String text()
    {
        return this.text;
    }

    /**
     * Defines the new text.<br>
     * Does nothing if the label is linked to a resource text
     *
     * @param text New text
     */
    public void text(final String text)
    {
        if (this.resourceKey != null)
        {
            return;
        }

        if (text == null)
        {
            this.text = "";
        }
        else
        {
            this.text = text;
        }

        this.updateTextImage();
    }

    /**
     * Text align
     *
     * @return Text align
     */
    public JHelpTextAlign textAlign()
    {
        return this.textAlign;
    }

    /**
     * Change text align
     *
     * @param textAlign New text align
     */
    public void textAlign(final JHelpTextAlign textAlign)
    {
        if (textAlign == null)
        {
            this.textAlign = JHelpTextAlign.LEFT;
        }
        else
        {
            this.textAlign = textAlign;
        }

        this.updateTextImage();
    }

    /**
     * Unlik resouce text from label text
     */
    public void unlinkToResourceText()
    {
        if (this.resourceText != null)
        {
            this.resourceText.unregister(this);
        }

        this.resourceText = null;
        this.resourceKey = null;
    }
}
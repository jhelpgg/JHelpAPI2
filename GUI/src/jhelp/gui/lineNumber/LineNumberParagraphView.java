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
package jhelp.gui.lineNumber;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;

/**
 * Paragraph view that draw line number and additional text
 *
 * @author JHelp <br>
 */
public class LineNumberParagraphView
        extends ParagraphView
{
    /**
     * Margin to respect between normal text and additional one
     */
    private static final int       ADDITIONAL_MARGIN           = 32;
    /**
     * {@link AttributeSet} property key for specify the additional text
     */
    public static final  String    ATTRIBUTE_ADDITIONAL_TEXT   = "jhelp.gui.lineNumber.LineNumberParagraphView.ADDITIONAL_TEXT";
    /**
     * {@link AttributeSet} property key for specify background for line number
     */
    public static final  String    ATTRIBUTE_NUMBER_BACKGROUND = "jhelp.gui.lineNumber.LineNumberParagraphView.NUMBER_BACKGROUND";
    /**
     * {@link AttributeSet} property key for specify font for line number
     */
    public static final  String    ATTRIBUTE_NUMBER_FONT       = "jhelp.gui.lineNumber.LineNumberParagraphView.NUMBER_FONT";
    /**
     * {@link AttributeSet} property key for specify foreground for line number
     */
    public static final  String    ATTRIBUTE_NUMBER_FOREGROUND = "jhelp.gui.lineNumber.LineNumberParagraphView.NUMBER_FOREGROUND";
    /**
     * Default font use for line number
     */
    public static final  JHelpFont DEFAULT_NUMBER_FONT         = new JHelpFont("Courier", 18);
    /**
     * Image carry additional text
     */
    private JHelpImage additionalText;
    /**
     * Number background
     */
    private int        background;
    /**
     * Number foreground
     */
    private int        foreground;
    /**
     * Number font
     */
    private JHelpFont  numberFont;
    /**
     * Size reserved for numbers
     */
    private Dimension  numberSize;

    /**
     * Create a new instance of LineNumberParagraphView
     *
     * @param element Paragraph element
     */
    public LineNumberParagraphView(final Element element)
    {
        super(element);
        this.foreground = 0xFF000000;
        this.background = 0;

        if (this.numberFont == null)
        {
            // If not already initialize by call during parent constructor
            this.font(LineNumberParagraphView.DEFAULT_NUMBER_FONT);
        }
    }

    /**
     * Change number font
     *
     * @param numberFont New font
     */
    private void font(final JHelpFont numberFont)
    {
        this.numberFont = numberFont;
        this.numberSize = this.numberFont.stringSize("99999");
    }

    /**
     * Compute the paragraph index in parent
     *
     * @return Index in parent
     */
    private int indexInParent()
    {
        int        index  = 0;
        final View parent = this.getParent();
        final int  count  = parent.getViewCount();

        //noinspection StatementWithEmptyBody
        for (; (index < count) && (this != parent.getView(index)); index++)
        {
            // Nothing to do
        }

        return index;
    }

    /**
     * Paint a paragraph child <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param g     Graphics for paint
     * @param alloc Paragraph child rectangle
     * @param index Index of child
     * @see javax.swing.text.BoxView#paintChild(Graphics, Rectangle, int)
     */
    @Override
    protected void paintChild(final Graphics g, final Rectangle alloc, final int index)
    {
        // Draw the child normally
        super.paintChild(g, alloc, index);

        // We want add the number and additional text only on first child
        // Because we want the real number of lines (\n) not the visually number of lines
        // And we don't want repeat the same number and information on each child

        if (index > 0)
        {
            return;
        }

        // We add one, because line number start at 1 not 0
        final JHelpImage number = this.numberFont.createImage(String.valueOf(this.indexInParent() + 1), this.foreground,
                                                              this.background);
        // We center the number inside the reserved area
        g.drawImage(number.getImage(), alloc.x - ((this.getLeftInset() + number.getWidth()) >> 1),
                    alloc.y + ((alloc.height - number.getHeight()) >> 1), null);

        if (this.additionalText != null)
        {
            g.drawImage(this.additionalText.getImage(),
                        alloc.x + alloc.width + LineNumberParagraphView.ADDITIONAL_MARGIN,
                        alloc.y + ((alloc.height - this.additionalText.getHeight()) >> 1), null);
        }
    }

    /**
     * Called when insets changes <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param attributes Attributes with insets
     * @see javax.swing.text.CompositeView#setParagraphInsets(AttributeSet)
     */
    @Override
    protected void setParagraphInsets(final AttributeSet attributes)
    {
        // Here we just call our method to reserved room for line number and additional text
        this.setInsets((short) StyleConstants.getSpaceAbove(attributes),
                       (short) StyleConstants.getLeftIndent(attributes),
                       (short) StyleConstants.getSpaceBelow(attributes),
                       (short) StyleConstants.getRightIndent(attributes));
    }

    /**
     * Called when insets changes <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param top    Top margin
     * @param left   Left margin
     * @param bottom Bottom margin
     * @param right  Right margin
     * @see javax.swing.text.CompositeView#setInsets(short, short, short, short)
     */
    @Override
    protected void setInsets(final short top, final short left, final short bottom, final short right)
    {
        if (this.numberSize == null)
        {
            // Number size may be null due call during the parent class constructor
            this.font(LineNumberParagraphView.DEFAULT_NUMBER_FONT);
        }

        // Compute additional margin for additional text
        int more = 0;

        if (this.additionalText != null)
        {
            more = this.additionalText.getWidth() + LineNumberParagraphView.ADDITIONAL_MARGIN;
        }

        // Call insets with reserved room for line number and additional text
        super.setInsets(top, (short) (left + this.numberSize.width), bottom, (short) (right + more));
    }

    /**
     * Called when properties have changed or set for the first time <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see ParagraphView#setPropertiesFromAttributes()
     */
    @Override
    protected void setPropertiesFromAttributes()
    {
        super.setPropertiesFromAttributes();

        // Collect our additional attributes and update if need
        final AttributeSet attributeSet = this.getAttributes();

        if (attributeSet != null)
        {
            Object value = attributeSet.getAttribute(LineNumberParagraphView.ATTRIBUTE_NUMBER_FONT);

            if (value != null)
            {
                if (value instanceof JHelpFont)
                {
                    this.font((JHelpFont) value);
                }
                else if (value instanceof Font)
                {
                    this.font(new JHelpFont((Font) value, false));
                }
            }

            value = attributeSet.getAttribute(LineNumberParagraphView.ATTRIBUTE_NUMBER_FOREGROUND);

            if (value != null)
            {
                if (value instanceof Color)
                {
                    this.foreground = ((Color) value).getRGB();
                }
                else if (value instanceof Integer)
                {
                    this.foreground = (Integer) value;
                }
            }

            value = attributeSet.getAttribute(LineNumberParagraphView.ATTRIBUTE_NUMBER_BACKGROUND);

            if (value != null)
            {
                if (value instanceof Color)
                {
                    this.background = ((Color) value).getRGB();
                }
                else if (value instanceof Integer)
                {
                    this.background = (Integer) value;
                }
            }

            value = attributeSet.getAttribute(LineNumberParagraphView.ATTRIBUTE_ADDITIONAL_TEXT);

            if ((value != null) && (value instanceof CharSequence))
            {
                final String text = value.toString().trim();

                if (text.length() > 0)
                {
                    if (this.numberFont == null)
                    {
                        this.font(LineNumberParagraphView.DEFAULT_NUMBER_FONT);
                    }

                    this.additionalText = this.numberFont.createImage(text, this.foreground, 0x40A0A0A0);
                }
                else
                {
                    this.additionalText = null;
                }
            }
            else
            {
                this.additionalText = null;
            }
        }
    }
}
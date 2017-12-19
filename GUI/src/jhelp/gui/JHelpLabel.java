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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JComponent;

import jhelp.gui.smooth.JHelpConstantsSmooth;
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;

/**
 * Label with text, select and focus status
 *
 * @author JHelp
 */
public class JHelpLabel
        extends JComponent
{
    /**
     * Focus status
     */
    private boolean   focused;
    /**
     * Focus color
     */
    private Color     focusedColor;
    /**
     * Font for draw
     */
    private JHelpFont font;
    /**
     * Selection status
     */
    private boolean   selected;
    /**
     * Selection color
     */
    private Color     selectedColor;
    /**
     * Text to draw
     */
    private String    text;

    /**
     * Create a new instance of JHelpLabel
     */
    public JHelpLabel()
    {
        this("");
    }

    /**
     * Create a new instance of JHelpLabel
     *
     * @param text Text on label
     */
    public JHelpLabel(final String text)
    {
        if (text == null)
        {
            throw new NullPointerException("text mustn't be null");
        }

        this.selectedColor = new Color(JHelpConstantsSmooth.COLOR_ALPHA_HINT |
                                       (JHelpConstantsSmooth.COLOR_CYAN_0400 & JHelpConstantsSmooth.MASK_COLOR));
        this.selected = false;
        this.focusedColor = Color.BLACK;
        this.focused = false;
        this.text = text;
        this.font = JHelpConstantsSmooth.FONT_DISPLAY_1;
        this.setFont(this.font.getFont());
    }

    /**
     * Update text size
     */
    private void updateSize()
    {
        final Dimension dimension = this.font.stringSize(this.text);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setMinimumSize(dimension);
        this.setMaximumSize(dimension);

        this.repaint();
    }

    /**
     * Draw the label <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param graphics Graphics where draw
     * @see JComponent#paintComponent(Graphics)
     */
    @Override
    protected void paintComponent(final Graphics graphics)
    {
        final int width      = this.getWidth();
        final int height     = this.getHeight();
        Color     background = this.getBackground();

        if (background == null)
        {
            background = Color.WHITE;
        }

        graphics.setColor(background);
        graphics.fillRect(0, 0, width, height);

        Color foreground = this.getForeground();

        if (foreground == null)
        {
            foreground = Color.BLACK;
        }

        if (this.selected)
        {
            background = this.selectedColor;
        }

        final JHelpImage image = this.font.createImage(this.text, foreground.getRGB(), background.getRGB());
        final int        x     = (width - image.getWidth()) >> 1;
        final int        y     = (height - image.getHeight()) >> 1;
        graphics.drawImage(image.getImage(), x, y, null);

        if (this.focused)
        {
            graphics.setColor(this.focusedColor);
            graphics.drawRect(x, y, image.getWidth(), image.getHeight());
        }
    }

    /**
     * Change foreground <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param color New foreground
     * @see JComponent#setForeground(Color)
     */
    @Override
    public void setForeground(final Color color)
    {
        super.setForeground(color);
        this.repaint();
    }

    /**
     * Change background <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param color New background color
     * @see JComponent#setBackground(Color)
     */
    @Override
    public void setBackground(final Color color)
    {
        super.setBackground(color);
        this.repaint();
    }

    /**
     * Change font <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param font New font
     * @see JComponent#setFont(Font)
     */
    @Override
    public void setFont(final Font font)
    {
        this.font = new JHelpFont(font, false);
        super.setFont(font);
        this.updateSize();
    }

    /**
     * Focused color
     *
     * @return Focused color
     */
    public Color getFocusedColor()
    {
        return this.focusedColor;
    }

    /**
     * Change focus color.
     *
     * @param focusedColor New focus color
     */
    public void setFocusedColor(final Color focusedColor)
    {
        if (focusedColor == null)
        {
            throw new NullPointerException("focusedColor mustn't be null");
        }

        this.focusedColor = focusedColor;

        if (this.focused)
        {
            this.repaint();
        }
    }

    /**
     * Selected color
     *
     * @return Selected color
     */
    public Color getSelectedColor()
    {
        return this.selectedColor;
    }

    /**
     * Change selection color
     *
     * @param selectedColor New selection color
     */
    public void setSelectedColor(final Color selectedColor)
    {
        if (selectedColor == null)
        {
            throw new NullPointerException("selectedColor mustn't be null");
        }

        this.selectedColor = selectedColor;

        if (this.selected)
        {
            this.repaint();
        }
    }

    /**
     * Current text
     *
     * @return Current text
     */
    public String getText()
    {
        return this.text;
    }

    /**
     * Change the text
     *
     * @param text New text
     */
    public void setText(final String text)
    {
        if (text == null)
        {
            throw new NullPointerException("text mustn't be null");
        }

        this.text = text;
        this.updateSize();
    }

    /**
     * Indicates if selected
     *
     * @return {@code true} if selected
     */
    public boolean isFocused()
    {
        return this.focused;
    }

    /**
     * Change focus status
     *
     * @param focused New focus status
     */
    public void setFocused(final boolean focused)
    {
        if (this.focused == focused)
        {
            return;
        }

        this.focused = focused;
        this.repaint();
    }

    /**
     * Indicates if selected
     *
     * @return {@code true} if selected
     */
    public boolean isSelected()
    {
        return this.selected;
    }

    /**
     * Change selected status
     *
     * @param selected New selected status
     */
    public void setSelected(final boolean selected)
    {
        if (this.selected == selected)
        {
            return;
        }

        this.selected = selected;
        this.repaint();
    }
}
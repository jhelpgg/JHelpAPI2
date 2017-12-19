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
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.Action;
import jhelp.gui.action.GenericAction;
import jhelp.gui.smooth.event.SmoothMouseInformation;
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpTextAlign;
import jhelp.util.gui.JHelpTextLineAlpha;
import jhelp.util.list.Pair;

/**
 * Button component.<br>
 * Button is based on one action, action have text and may be an icon<br>
 * If action have icon {@link JHelpButtonAlignSmooth} decide how place text and icon, else text is print centered.<br>
 * The action is execute each time user click on button
 *
 * @author JHelp
 */
public class JHelpButtonSmooth
        extends JHelpComponentSmooth
        implements PropertyChangeListener
{
    /**
     * Action linked to button
     */
    private final GenericAction          action;
    /**
     * How place text and icon
     */
    private       JHelpButtonAlignSmooth buttonAlign;
    /**
     * Font to use
     */
    private       JHelpFont              font;
    /**
     * Indicates if text is upper case automatically
     */
    private       boolean                forceUpperCase;
    /**
     * Foreground color
     */
    private       int                    foreground;
    /**
     * Lock for synchronization
     */
    private final Object lock = new Object();
    /**
     * Pre-computed image button
     */
    private JHelpImage precomputed;

    /**
     * Create a new instance of JHelpButtonSmooth
     *
     * @param action Action linked
     */
    public JHelpButtonSmooth(final GenericAction action)
    {
        this(action, JHelpButtonAlignSmooth.ICON_LEFT_TEXT_RIGHT, JHelpConstantsSmooth.FONT_BUTTON);
    }

    /**
     * Create a new instance of JHelpButtonSmooth
     *
     * @param action      Action to execute
     * @param buttonAlign How place text and icon
     * @param font        Font to use
     */
    public JHelpButtonSmooth(final GenericAction action, final JHelpButtonAlignSmooth buttonAlign, final JHelpFont font)
    {
        if (action == null)
        {
            throw new NullPointerException("action mustn't be null");
        }

        if (buttonAlign == null)
        {
            throw new NullPointerException("buttonAlign mustn't be null");
        }

        if (font == null)
        {
            throw new NullPointerException("font mustn't be null");
        }

        this.backgroundAndShadowColor(JHelpConstantsSmooth.COLOR_WHITE);
        this.foreground = JHelpConstantsSmooth.COLOR_BLACK;
        this.action = action;
        this.precomputed = null;
        this.font = font;
        this.buttonAlign = buttonAlign;
        this.action.addPropertyChangeListener(this);
        this.forceUpperCase = true;
    }

    /**
     * Create a new instance of JHelpButtonSmooth
     *
     * @param action      Action to execute
     * @param buttonAlign How place text and icon
     */
    public JHelpButtonSmooth(final GenericAction action, final JHelpButtonAlignSmooth buttonAlign)
    {
        this(action, buttonAlign, JHelpConstantsSmooth.FONT_BUTTON);
    }

    /**
     * Create a new instance of JHelpButtonSmooth
     *
     * @param action Action to execute
     * @param font   Font to use
     */
    public JHelpButtonSmooth(final GenericAction action, final JHelpFont font)
    {
        this(action, JHelpButtonAlignSmooth.ICON_LEFT_TEXT_RIGHT, font);
    }

    /**
     * Compute, if need, image button
     */
    private void computeImage()
    {
        if (this.precomputed != null)
        {
            return;
        }

        int textX  = 0;
        int textY  = 0;
        int imageX = 0;
        int imageY = 0;
        int width  = 0;
        int height = 0;

        Pair<List<JHelpTextLineAlpha>, Dimension> pair  = null;
        final JHelpImage                          image = this.action.largeIcon();

        if ((this.buttonAlign != JHelpButtonAlignSmooth.ICON_ONLY_IF_EXISTS) || (image == null))
        {
            String text = this.action.printName();

            if (this.forceUpperCase)
            {
                text = text.toUpperCase();
            }

            pair = this.font.computeTextLinesAlpha(text, JHelpTextAlign.CENTER);
        }

        if (image == null)
        {
            width = pair.second.width;
            height = pair.second.height;
        }
        else
        {
            switch (this.buttonAlign)
            {
                case ICON_LEFT_TEXT_RIGHT:
                    assert pair != null;
                    width = image.getWidth() + 3 + pair.second.width;
                    height = Math.max(image.getHeight(), pair.second.height);
                    textX = image.getWidth() + 3;
                    textY = (height - pair.second.height) >> 1;
                    imageX = 0;
                    imageY = (height - image.getHeight()) >> 1;
                    break;
                case ICON_ONLY_IF_EXISTS:
                    width = image.getWidth();
                    height = image.getHeight();
                    imageX = 0;
                    imageY = 0;
                    break;
                case ICON_UP_TEXT_BOTTOM:
                    assert pair != null;
                    width = Math.max(pair.second.width, image.getWidth());
                    height = image.getHeight() + 3 + pair.second.height;
                    textX = (width - pair.second.width) >> 1;
                    textY = image.getHeight() + 3;
                    imageX = (width - image.getWidth()) >> 1;
                    imageY = 0;
                    break;
                case TEXT_LEFT_ICON_RIGHT:
                    assert pair != null;
                    width = pair.second.width + 3 + image.getWidth();
                    height = Math.max(image.getHeight(), pair.second.height);
                    textX = 0;
                    textY = (height - pair.second.height) >> 1;
                    imageX = pair.second.width + 3;
                    imageY = (height - image.getHeight()) >> 1;
                    break;
                case TEXT_OVER_ICON:
                    assert pair != null;
                    width = Math.max(pair.second.width, image.getWidth());
                    height = Math.max(image.getHeight(), pair.second.height);
                    textX = (width - pair.second.width) >> 1;
                    textY = (height - pair.second.height) >> 1;
                    imageX = (width - image.getWidth()) >> 1;
                    imageY = (height - image.getHeight()) >> 1;
                    break;
                case TEXT_UP_ICON_BOTTOM:
                    assert pair != null;
                    width = Math.max(pair.second.width, image.getWidth());
                    height = pair.second.height + 3 + image.getHeight();
                    textX = (width - pair.second.width) >> 1;
                    textY = 0;
                    imageX = (width - image.getWidth()) >> 1;
                    imageY = pair.second.height + 3;
                    break;
            }
        }

        this.precomputed = new JHelpImage(width, height);
        this.precomputed.startDrawMode();

        if (image != null)
        {
            this.precomputed.drawImage(imageX, imageY, image);
        }

        if (pair != null)
        {
            if ((this.buttonAlign == JHelpButtonAlignSmooth.TEXT_OVER_ICON) && (image != null))
            {
                int background = this.background();

                if (background == 0)
                {
                    background = 0xFF000000 | (this.foreground ^ 0x00FFFFFF);
                }

                for (int yy = -1; yy <= 1; yy++)
                {
                    for (int xx = -1; xx <= 1; xx++)
                    {
                        for (final JHelpTextLineAlpha textLine : pair.first)
                        {
                            this.precomputed.paintAlphaMask(xx + textX + textLine.getX(), yy + textY + textLine.getY(),
                                                            textLine.getMask(), background);
                        }
                    }
                }
            }

            for (final JHelpTextLineAlpha textLine : pair.first)
            {
                this.precomputed.paintAlphaMask(textX + textLine.getX(), textY + textLine.getY(), textLine.getMask(),
                                                this.foreground);
            }
        }

        this.precomputed.endDrawMode();
    }

    /**
     * Draw button <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param image        Image where draw
     * @param x            X
     * @param y            Y
     * @param width        Width
     * @param height       Height
     * @param parentWidth  Parent width
     * @param parentHeight Parent height
     * @see jhelp.gui.smooth.JHelpComponentSmooth#paint(JHelpImage, int, int, int, int, int, int)
     */
    @Override
    protected void paint(
            final JHelpImage image, final int x, final int y, final int width, final int height,
            final int parentWidth, final int parentHeight)
    {
        this.drawBackground(image, x, y, width, height);
        // Drawing background may have change the bounds
        final Rectangle bounds = this.bounds();

        synchronized (this.lock)
        {
            this.computeImage();

            image.drawImage(bounds.x + ((bounds.width - this.precomputed.getWidth()) >> 1),
                            bounds.y + ((bounds.height - this.precomputed.getHeight()) >> 1),
                            this.precomputed, true);

            if (!this.action.isEnabled())
            {
                this.drawDisable(image, x, y, width, height);
            }
        }
    }

    /**
     * Button preferred size <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Button preferred size
     * @see jhelp.gui.smooth.JHelpComponentSmooth#preferredSizeInternal()
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
     * Process mouse events before dispatching <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param mouseInformation Mouse event description
     * @see jhelp.gui.smooth.JHelpComponentSmooth#processMouseEvent(SmoothMouseInformation)
     */
    @Override
    protected void processMouseEvent(final SmoothMouseInformation mouseInformation)
    {
        if ((mouseInformation.type() == MouseEvent.MOUSE_CLICKED) && (this.action.isEnabled()))
        {
            this.action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "CLICK"));
        }

        super.processMouseEvent(mouseInformation);
    }

    /**
     * Button action
     *
     * @return Button action
     */
    public final GenericAction action()
    {
        return this.action;
    }

    /**
     * How text and icon are placed
     *
     * @return How text and icon are placed
     */
    public JHelpButtonAlignSmooth buttonAlignSmooth()
    {
        return this.buttonAlign;
    }

    /**
     * Change text and icon placement
     *
     * @param buttonAlign New text and icon placement
     */
    public void buttonAlignSmooth(final JHelpButtonAlignSmooth buttonAlign)
    {
        if (buttonAlign == null)
        {
            throw new NullPointerException("buttonAlign mustn't be null");
        }

        synchronized (this.lock)
        {
            if (buttonAlign == this.buttonAlign)
            {
                return;
            }

            this.buttonAlign = buttonAlign;
            this.precomputed = null;
        }
    }

    /**
     * Font used
     *
     * @return Font
     */
    public JHelpFont font()
    {
        return this.font;
    }

    /**
     * Change font
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
     * Indicates if text is automatically put in upper case
     *
     * @return {@code true} if text is automatically put in upper case
     */
    public boolean forceUpperCase()
    {
        return this.forceUpperCase;
    }

    /**
     * Change the force upper case
     *
     * @param forceUpperCase Indicates if force text be upper case
     */
    public void forceUpperCase(final boolean forceUpperCase)
    {
        if (forceUpperCase == this.forceUpperCase)
        {
            return;
        }

        synchronized (this.lock)
        {
            this.forceUpperCase = forceUpperCase;
            this.precomputed = null;
        }
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
     * Called when action property changes <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param propertyChangeEvent Property change event description
     * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
     */
    @Override
    public void propertyChange(final PropertyChangeEvent propertyChangeEvent)
    {
        final String name = propertyChangeEvent.getPropertyName();

        if ((Action.NAME.equals(name)) || (Action.LARGE_ICON_KEY.equals(name)))
        {
            synchronized (this.lock)
            {
                this.precomputed = null;
            }
        }
    }
}
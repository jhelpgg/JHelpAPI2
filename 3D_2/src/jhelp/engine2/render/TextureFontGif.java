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
package jhelp.engine2.render;

import com.sun.istack.internal.NotNull;
import java.awt.Color;
import java.io.IOException;
import jhelp.engine2.animation.Animation;
import jhelp.util.gui.GIF;
import jhelp.util.gui.dynamic.font.FontGif;
import jhelp.util.gui.dynamic.font.GifPosition;
import jhelp.util.gui.dynamic.font.GifText;

/**
 * Texture based on font based on GIF
 *
 * @author JHelp
 */
public class TextureFontGif
        extends Texture
        implements Animation
{
    /**
     * Font based on GIF
     */
    private final FontGif fontGif;
    /**
     * Text with GIF
     */
    private       GifText gifText;
    /**
     * Start animation absolute frame
     */
    private       float   startAbsoluteFrame;
    /**
     * Text to draw
     */
    private       String  text;

    /**
     * Create a new instance of TextureFontGif
     *
     * @param fontGifName Font based on GIF name
     */
    public TextureFontGif(final @NotNull String fontGifName)
    {
        this(fontGifName, "");
    }

    /**
     * Create a new instance of TextureFontGif
     *
     * @param fontGifName Font based on GIF
     * @param text        Text to draw
     */
    public TextureFontGif(final @NotNull String fontGifName, final @NotNull String text)
    {
        super(fontGifName, Texture.REFERENCE_FONT_GIF);

        if (text == null)
        {
            throw new NullPointerException("text mustn't be null");
        }

        try
        {
            this.fontGif = new FontGif(fontGifName);
        }
        catch (final IOException exception)
        {
            throw new IllegalArgumentException("The font GIF " + fontGifName + " can't be parsed ", exception);
        }

        this.text = text;
        this.setPixels(1, 1, new byte[4]);
        this.autoFlush(false);
        this.updateText();
        this.drawTexture(0);
    }

    /**
     * Draw/refresh the texture
     *
     * @param index GIF animation index
     */
    private void drawTexture(final int index)
    {
        synchronized (this.fontGif)
        {
            final int width  = this.gifText.width();
            final int height = this.gifText.height();

            if ((this.width() != width) || (this.height() != height))
            {
                this.setPixels(width, height, new byte[4 * width * height]);
            }

            this.clear(new Color(0, true));
            GIF gif;

            for (final GifPosition position : this.gifText.gifPositions())
            {
                gif = position.gif();
                this.drawImage(position.x(), position.y(), gif.getImage(index % gif.numberOfImage()));
            }

            this.flush();
        }
    }

    /**
     * Update the text to draw
     */
    private void updateText()
    {
        synchronized (this.fontGif)
        {
            this.gifText = this.fontGif.computeGifText(this.text);
        }
    }

    /**
     * Animate the texture text <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param absoluteFrame Current absolute frame
     * @return Indicates if animate should continue
     * @see Animation#animate(float)
     */
    @Override
    @ThreadOpenGL
    public boolean animate(final float absoluteFrame)
    {
        this.drawTexture((int) ((absoluteFrame - this.startAbsoluteFrame) / 4f));
        return true;
    }

    /**
     * Called on when animation start <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param startAbsoluteFrame Started frame
     * @see Animation#startAbsoluteFrame(float)
     */
    @Override
    @ThreadOpenGL
    public void startAbsoluteFrame(final float startAbsoluteFrame)
    {
        this.startAbsoluteFrame = startAbsoluteFrame;
        this.drawTexture(0);
    }

    /**
     * Current text
     *
     * @return Current text
     */
    public @NotNull String text()
    {
        return this.text;
    }

    /**
     * Change the text
     *
     * @param text New text
     */
    public void text(final @NotNull String text)
    {
        if (text == null)
        {
            throw new NullPointerException("text mustn't be null");
        }

        if (this.text.equals(text))
        {
            return;
        }

        this.text = text;
        this.updateText();
        this.drawTexture(0);
    }
}
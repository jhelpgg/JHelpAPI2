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
package jhelp.engine2.util;

import com.sun.istack.internal.NotNull;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.util.StringTokenizer;
import jhelp.engine2.render.Texture;
import jhelp.engine2.twoD.Object2D;
import jhelp.util.debug.Debug;

/**
 * Utilities for text <br>
 *
 * @author JHelp
 */
public class TextIn3DUtil
{
    /**
     * Next ID for texture text name
     */
    private static int NEXT_ID = 0;

    /**
     * Create texture with text
     *
     * @param text          Text to print. You can use '\n' character to print several lines
     * @param font          Font used
     * @param textAlignment Text alignment
     * @param foreGround    Color for print
     * @param backGround    Background color
     * @return Created texture
     */
    public static Texture createTextTexture(
            final @NotNull String text, final @NotNull Font font, final @NotNull TextAlignment textAlignment,
            final @NotNull Color foreGround, final @NotNull Color backGround)
    {
        String          lines[];
        StringTokenizer stringTokenizer;
        int             length;
        int             index;

        // Cut the text in lines
        stringTokenizer = new StringTokenizer(text, "\n", false);
        length = stringTokenizer.countTokens();
        lines = new String[length];
        index = 0;

        while (stringTokenizer.hasMoreTokens())
        {
            lines[index++] = stringTokenizer.nextToken();
        }

        // Create the texture
        return TextIn3DUtil.createTextTexture(lines, font, textAlignment, foreGround, backGround);
    }

    /**
     * Create a texture with a text print inside
     *
     * @param lines         Text lines.
     *                      Each element of the array is a line of the text. '\n' will be rendered with special character (depends on font choose)
     * @param font          Font used to print
     * @param textAlignment Text alignment
     * @param foreGround    Color used for the text
     * @param backGround    Color used on back ground (can be transparent ;-) )
     * @return Created texture
     */
    @SuppressWarnings("deprecation")
    public static @NotNull Texture createTextTexture(
            final @NotNull String[] lines, final @NotNull Font font, final @NotNull TextAlignment textAlignment,
            final @NotNull Color foreGround, final @NotNull Color backGround)
    {
        Texture     texture;
        int         length;
        int         index;
        FontMetrics fontMetrics;
        int         fontHeight;
        int         height;
        int         width;
        int[]       linesWidth;
        int         heightP2;
        int         widthP2;
        int         x;
        int         y;

        // No lines, so little texture is need
        length = lines.length;

        if (length < 1)
        {
            return new Texture("TEXT_" + (TextIn3DUtil.NEXT_ID++), 1, 1, backGround);
        }

        linesWidth = new int[length];

        // Compute text height
        fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(font);
        fontHeight = fontMetrics.getHeight();
        height = fontHeight * length;

        // Compute texture width and memorize each line width
        width = 0;
        for (index = 0; index < length; index++)
        {
            linesWidth[index] = fontMetrics.stringWidth(lines[index]);
            width = Math.max(width, linesWidth[index]);
        }

        // Compute near power of dimension values
        widthP2 = Math3D.computePowerOf2couple(width)[1];
        heightP2 = Math3D.computePowerOf2couple(height)[1];

        Debug.verbose(
                "TextUtil.createTextTexture() BEFORE " + width + "x" + height + " => " + widthP2 + "x" + heightP2);
        if ((widthP2 < width) && (widthP2 < 1024))
        {
            widthP2 *= 2;
        }
        if ((heightP2 < height) && (heightP2 < 1024))
        {
            heightP2 *= 2;
        }
        Debug.verbose("TextUtil.createTextTexture() AFTER " + width + "x" + height + " => " + widthP2 + "x" + heightP2);

        // For good card only ...
        // widthP2 = width;
        // heightP2 = height;
        // ... For good card only

        // Create empty texture
        texture = new Texture("TEXT_" + (TextIn3DUtil.NEXT_ID++), widthP2, heightP2, ColorsUtil.TRANSPARENT);
        texture.fillRect(0, 0, width, height, backGround, false);

        // Print each line
        y = fontMetrics.getAscent();
        for (index = 0; index < length; index++)
        {
            x = 0;
            switch (textAlignment)
            {
                case LEFT:
                    x = 0;
                    break;
                case CENTER:
                    x = (width - linesWidth[index]) / 2;
                    break;
                case RIGHT:
                    x = width - linesWidth[index];
                    break;
            }

            texture.fillString(x, y, lines[index], foreGround, font, false);
            y += fontHeight;
        }

        texture.flush();

        return texture;
    }

    /**
     * Create a 2D object with text print
     *
     * @param x             X position of the object
     * @param y             Y position of the object
     * @param text          Text to print. You can use '\n' character to print several lines
     * @param font          Font used
     * @param textAlignment Text alignment
     * @param foreGround    Color for print
     * @param backGround    Background color
     * @return Created object
     */
    public static @NotNull Object2D creteTextObject2D(
            final int x, final int y,
            final @NotNull String text, final @NotNull Font font, final @NotNull TextAlignment textAlignment,
            final @NotNull Color foreGround, final @NotNull Color backGround)
    {
        final Texture  texture  = TextIn3DUtil.createTextTexture(text, font, textAlignment, foreGround, backGround);
        final Object2D object2D = new Object2D(x, y, texture.width(), texture.height());
        object2D.texture(texture);
        return object2D;
    }

    /**
     * Create a 2D object with text print
     *
     * @param x             X position of the object
     * @param y             Y position of the object
     * @param lines         Text lines.
     *                      Each element of the array is a line of the text. '\n' will be rendered with special character (depends on font choose)
     * @param font          Font used
     * @param textAlignment Text alignment
     * @param foreGround    Color for print
     * @param backGround    Background color
     * @return Created object
     */
    public static @NotNull Object2D creteTextObject2D(
            final int x, final int y,
            final @NotNull String[] lines, final @NotNull Font font, final @NotNull TextAlignment textAlignment,
            final @NotNull Color foreGround, final @NotNull Color backGround)
    {
        Texture  texture;
        Object2D object2D;

        texture = TextIn3DUtil.createTextTexture(lines, font, textAlignment, foreGround, backGround);
        object2D = new Object2D(x, y, texture.width(), texture.height());
        object2D.texture(texture);

        return object2D;
    }
}
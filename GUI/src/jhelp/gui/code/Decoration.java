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

package jhelp.gui.code;

import com.sun.istack.internal.NotNull;
import java.awt.Color;

/**
 * Describe how decorate a part of text
 */
public class Decoration
{
    /**
     * Text background
     */
    private final Color    background;
    /**
     * Indicates if bold the text
     */
    private final boolean  bold;
    /**
     * Text color
     */
    private final Color    foreground;
    /**
     * Indicates if italic the text
     */
    private final boolean  italic;
    /**
     * Text size
     */
    private final TextSize textSize;
    /**
     * Indicates if underline the text
     */
    private final boolean  underline;

    /**
     * Create the decoration
     *
     * @param textSize   Text size
     * @param bold       Indicates if bold the text
     * @param italic     Indicates if italic the text
     * @param underline  Indicates if underline the text
     * @param foreground Text color
     * @param background Text background
     */
    public Decoration(
            @NotNull TextSize textSize, boolean bold, boolean italic, boolean underline,
            @NotNull Color foreground, @NotNull Color background)
    {
        if (textSize == null)
        {
            throw new NullPointerException("textSize MUST NOT be null !");
        }

        if (foreground == null)
        {
            throw new NullPointerException("foreground MUST NOT be null !");
        }

        if (background == null)
        {
            throw new NullPointerException("background MUST NOT be null !");
        }

        this.textSize = textSize;
        this.bold = bold;
        this.italic = italic;
        this.underline = underline;
        this.foreground = foreground;
        this.background = background;
    }

    /**
     * Obtain background value
     *
     * @return background value
     */
    public @NotNull Color background()
    {
        return this.background;
    }

    /**
     * Obtain bold value
     *
     * @return bold value
     */
    public boolean bold()
    {
        return this.bold;
    }

    /**
     * Obtain foreground value
     *
     * @return foreground value
     */
    public @NotNull Color foreground()
    {
        return this.foreground;
    }

    /**
     * Obtain italic value
     *
     * @return italic value
     */
    public boolean italic()
    {
        return this.italic;
    }

    /**
     * Text relative size
     *
     * @return Text relative size
     */
    public @NotNull TextSize textSize()
    {
        return this.textSize;
    }

    /**
     * Obtain underline value
     *
     * @return underline value
     */
    public boolean underline()
    {
        return this.underline;
    }
}

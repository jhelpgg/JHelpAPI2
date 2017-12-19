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
package jhelp.util.gui.dynamic.font;

import com.sun.istack.internal.NotNull;
import java.util.Objects;
import jhelp.util.gui.GIF;

/**
 * Describe a gif an its position
 *
 * @author JHelp
 */
public final class GifPosition
{
    /**
     * GIF
     */
    private final GIF gif;
    /**
     * X
     */
    private final int x;
    /**
     * Y
     */
    private final int y;

    /**
     * Create a new instance of GifPosition
     *
     * @param gif GIF
     * @param x   X
     * @param y   Y
     */
    public GifPosition(final @NotNull GIF gif, final int x, final int y)
    {
        Objects.requireNonNull(gif, "gif MUST NOT be null!");
        this.gif = gif;
        this.x = x;
        this.y = y;
    }

    /**
     * Actual gif value
     *
     * @return Actual gif value
     */
    public GIF gif()
    {
        return this.gif;
    }

    /**
     * Actual x value
     *
     * @return Actual x value
     */
    public int x()
    {
        return this.x;
    }

    /**
     * Actual y value
     *
     * @return Actual y value
     */
    public int y()
    {
        return this.y;
    }
}
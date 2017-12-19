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
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Text create with GIF images
 *
 * @author JHelp
 */
public final class GifText
{
    /**
     * List of gifs and their position
     */
    private final List<GifPosition> gifPositions;
    /**
     * Total width
     */
    private final int               height;
    /**
     * Total height
     */
    private final int               width;

    /**
     * Create a new instance of GifText
     *
     * @param width        Total width
     * @param height       Total height
     * @param gifPositions List of gifs and their position
     */
    public GifText(final int width, final int height, final @NotNull List<GifPosition> gifPositions)
    {
        Objects.requireNonNull(gifPositions, "gifPositions MUST NOT be null!");
        this.width = width;
        this.height = height;
        this.gifPositions = gifPositions;
    }

    /**
     * List of gifs and their position
     *
     * @return List of gifs and their position
     */
    public @NotNull List<GifPosition> gifPositions()
    {
        return Collections.unmodifiableList(this.gifPositions);
    }

    /**
     * Total height
     *
     * @return Total height
     */
    public int height()
    {
        return this.height;
    }

    /**
     * Total width
     *
     * @return Total width
     */
    public int width()
    {
        return this.width;
    }
}
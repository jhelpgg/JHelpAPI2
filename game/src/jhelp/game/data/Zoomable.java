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

package jhelp.game.data;

import com.sun.istack.internal.NotNull;

/**
 * Object with zoom information
 */
public interface Zoomable
{
    /**
     * Zoom information
     *
     * @return Zoom information
     */
    @NotNull Zoom zoom();

    /**
     * Change zoom information
     *
     * @param zoom New zoom information
     */
    void zoom(@NotNull Zoom zoom);
}

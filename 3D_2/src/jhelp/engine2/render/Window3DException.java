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
import jhelp.util.text.UtilText;

/**
 * Generic exception may happen in window 3D
 */
public class Window3DException extends RuntimeException
{
    /**
     * Create the exception
     *
     * @param message Exception message
     */
    public Window3DException(@NotNull Object... message)
    {
        super(UtilText.concatenate(message));
    }
}

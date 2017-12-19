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

/**
 * Window 3D exception that happen if the system can't create the window
 */
public class Window3DCantBeCreatedException extends Window3DException
{
    /**
     * Create the exception
     *
     * @param message Exception message
     */
    public Window3DCantBeCreatedException(@NotNull Object... message)
    {
        super(message);
    }
}

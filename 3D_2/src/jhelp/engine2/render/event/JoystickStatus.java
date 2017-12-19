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

package jhelp.engine2.render.event;

import com.sun.istack.internal.NotNull;

public enum JoystickStatus
{
    PRESSED,
    REPEATED,
    RELEASED;

    /**
     * Compute new status after a press
     *
     * @return Computed status
     */
    public @NotNull JoystickStatus press()
    {
        switch (this)
        {
            case PRESSED:
            case REPEATED:
                return JoystickStatus.REPEATED;
            case RELEASED:
                return JoystickStatus.PRESSED;
            default:
                return JoystickStatus.RELEASED;
        }
    }
}
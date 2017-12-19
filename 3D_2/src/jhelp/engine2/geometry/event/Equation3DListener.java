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
package jhelp.engine2.geometry.event;

import com.sun.istack.internal.NotNull;
import jhelp.engine2.geometry.Equation3D;

/**
 * Listener to know when equation 3D is ready
 *
 * @author JHelp
 */
public interface Equation3DListener
{
    /**
     * Call when an equation 3D is ready
     *
     * @param equation3D Equation ready
     */
    void equation3Dready(@NotNull Equation3D equation3D);
}
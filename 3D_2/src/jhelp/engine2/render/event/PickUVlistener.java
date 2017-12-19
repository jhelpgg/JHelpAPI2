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
import jhelp.engine2.render.Node;

/**
 * Listener for UV picking <br>
 * <br>
 * Last modification : 26 juil. 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public interface PickUVlistener
{
    /**
     * Call when UV is pick
     *
     * @param u    U pick : [0, 255]
     * @param v    V pick : [0, 255]
     * @param node Node pick
     */
    void pickUV(int u, int v, @NotNull Node node);
}
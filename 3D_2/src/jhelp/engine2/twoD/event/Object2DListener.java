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
package jhelp.engine2.twoD.event;

import com.sun.istack.internal.NotNull;
import jhelp.engine2.twoD.Object2D;

/**
 * Listener of 2D object'd event <br>
 *
 * @author JHelp
 */
public interface Object2DListener
{
    /**
     * Call when mouse click on a object
     *
     * @param object2D    Object under mouse
     * @param x           Mouse X
     * @param y           Mouse Y
     * @param leftButton  Indicates if the left button is down
     * @param rightButton Indicates if the right button is down
     */
    void mouseClick(@NotNull Object2D object2D, int x, int y, boolean leftButton, boolean rightButton);

    /**
     * Call when mouse drag on a object
     *
     * @param object2D    Object under mouse
     * @param x           Mouse X
     * @param y           Mouse Y
     * @param leftButton  Indicates if the left button is down
     * @param rightButton Indicates if the right button is down
     */
    void mouseDrag(@NotNull Object2D object2D, int x, int y, boolean leftButton, boolean rightButton);

    /**
     * Call when mouse enter on a object
     *
     * @param object2D Object enter
     * @param x        Mouse X
     * @param y        Mouse Y
     */
    void mouseEnter(@NotNull Object2D object2D, int x, int y);

    /**
     * Call when mouse exit on a object
     *
     * @param object2D Object exit
     * @param x        Mouse X
     * @param y        Mouse Y
     */
    void mouseExit(@NotNull Object2D object2D, int x, int y);

    /**
     * Call when mouse move on a object
     *
     * @param object2D Object under mouse
     * @param x        Mouse X
     * @param y        Mouse Y
     */
    void mouseMove(@NotNull Object2D object2D, int x, int y);
}
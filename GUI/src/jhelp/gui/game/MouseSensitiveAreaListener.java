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
package jhelp.gui.game;

/**
 * Area mouse sensitive listener
 *
 * @author JHelp
 */
public interface MouseSensitiveAreaListener
{
    /**
     * Called when mouse enter in an area
     *
     * @param mouseSensitiveArea Area where mouse enter in
     * @param relativeX          Mouse X relative to the area
     * @param relativeY          Mouse y relative to the area
     * @param absoluteX          Mouse X absolute from main frame
     * @param absoluteY          Mouse Y absolute from main frame
     * @param leftButtonDown     Indicates if mouse left button is down
     * @param middleButtonDown   Indicates if mouse middle button is down
     * @param rightButtonDown    Indicates if mouse right button is down
     */
    void mouseEnterArea(
            MouseSensitiveArea mouseSensitiveArea, int relativeX, int relativeY, int absoluteX, int absoluteY,
            boolean leftButtonDown,
            boolean middleButtonDown, boolean rightButtonDown);

    /**
     * Called when mouse exit of an area
     *
     * @param mouseSensitiveArea Area where mouse exit from
     * @param relativeX          Mouse X relative to the area
     * @param relativeY          Mouse y relative to the area
     * @param absoluteX          Mouse X absolute from main frame
     * @param absoluteY          Mouse Y absolute from main frame
     * @param leftButtonDown     Indicates if mouse left button is down
     * @param middleButtonDown   Indicates if mouse middle button is down
     * @param rightButtonDown    Indicates if mouse right button is down
     */
    void mouseExitArea(
            MouseSensitiveArea mouseSensitiveArea, int relativeX, int relativeY, int absoluteX, int absoluteY,
            boolean leftButtonDown,
            boolean middleButtonDown, boolean rightButtonDown);

    /**
     * Called when mouse move over an area
     *
     * @param mouseSensitiveArea Area where mouse move over
     * @param relativeX          Mouse X relative to the area
     * @param relativeY          Mouse y relative to the area
     * @param absoluteX          Mouse X absolute from main frame
     * @param absoluteY          Mouse Y absolute from main frame
     * @param leftButtonDown     Indicates if mouse left button is down
     * @param middleButtonDown   Indicates if mouse middle button is down
     * @param rightButtonDown    Indicates if mouse right button is down
     */
    void mouseOverArea(
            MouseSensitiveArea mouseSensitiveArea, int relativeX, int relativeY, int absoluteX, int absoluteY,
            boolean leftButtonDown,
            boolean middleButtonDown, boolean rightButtonDown);
}
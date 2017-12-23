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

/**
 * Codes for joystick inputs
 */
public enum JoystickCode
{
    /**
     * Joystick axis 1 positive press
     */
    AXIS_1_POSITIVE(0, JoystickInputType.AXIS_POSITIVE),
    /**
     * Joystick axis 1 negative press
     */
    AXIS_1_NEGATIVE(0, JoystickInputType.AXIS_NEGATIVE),
    /**
     * Joystick axis 2 positive press
     */
    AXIS_2_POSITIVE(1, JoystickInputType.AXIS_POSITIVE),
    /**
     * Joystick axis 2 negative press
     */
    AXIS_2_NEGATIVE(1, JoystickInputType.AXIS_NEGATIVE),
    /**
     * Joystick axis 3 positive press
     */
    AXIS_3_POSITIVE(2, JoystickInputType.AXIS_POSITIVE),
    /**
     * Joystick axis 3 negative press
     */
    AXIS_3_NEGATIVE(2, JoystickInputType.AXIS_NEGATIVE),
    /**
     * Joystick axis 4 positive press
     */
    AXIS_4_POSITIVE(3, JoystickInputType.AXIS_POSITIVE),
    /**
     * Joystick axis 4 negative press
     */
    AXIS_4_NEGATIVE(3, JoystickInputType.AXIS_NEGATIVE),
    /**
     * Joystick button 1
     */
    BUTTON_1(0, JoystickInputType.BUTTON),
    /**
     * Joystick button 2
     */
    BUTTON_2(1, JoystickInputType.BUTTON),
    /**
     * Joystick button 3
     */
    BUTTON_3(2, JoystickInputType.BUTTON),
    /**
     * Joystick button 4
     */
    BUTTON_4(3, JoystickInputType.BUTTON),
    /**
     * Joystick button 5
     */
    BUTTON_5(4, JoystickInputType.BUTTON),
    /**
     * Joystick button 6
     */
    BUTTON_6(5, JoystickInputType.BUTTON),
    /**
     * Joystick button 7
     */
    BUTTON_7(6, JoystickInputType.BUTTON),
    /**
     * Joystick button 8
     */
    BUTTON_8(7, JoystickInputType.BUTTON),
    /**
     * Joystick button 9
     */
    BUTTON_9(8, JoystickInputType.BUTTON),
    /**
     * Joystick button 10
     */
    BUTTON_10(9, JoystickInputType.BUTTON),
    /**
     * Joystick button 11
     */
    BUTTON_11(10, JoystickInputType.BUTTON),
    /**
     * Joystick button 12
     */
    BUTTON_12(11, JoystickInputType.BUTTON),
    /**
     * Joystick button 13
     */
    BUTTON_13(12, JoystickInputType.BUTTON),
    /**
     * Joystick button 14
     */
    BUTTON_14(13, JoystickInputType.BUTTON),
    /**
     * Joystick button 15
     */
    BUTTON_15(14, JoystickInputType.BUTTON),
    /**
     * Joystick button 16
     */
    BUTTON_16(15, JoystickInputType.BUTTON),
    /**
     * No joystick event
     */
    NONE(-1, JoystickInputType.NONE);

    /**
     * Maximum index of managed joystick axis
     */
    public static final int MAX_AXIS_INDEX   = 3;
    /**
     * Maximum index of managed joystick button
     */
    public static final int MAX_BUTTON_INDEX = 15;

    /**
     * Obtain Joystick code associate to given index and joystick input type
     *
     * @param index             Joystick element index
     * @param joystickInputType Joystick input type (Axis positive, axis negative or button)
     * @return Associated Joystick code OR {@link #NONE} if none corresponds
     */
    private static @NotNull JoystickCode obtain(int index, @NotNull JoystickInputType joystickInputType)
    {
        for (JoystickCode joystickCode : JoystickCode.values())
        {
            if (joystickCode.index == index && joystickCode.joystickInputType == joystickInputType)
            {
                return joystickCode;
            }
        }

        return JoystickCode.NONE;
    }

    /**
     * Obtain Joystick code associate to an axis
     *
     * @param index    Axis index
     * @param positive Positive or negative press ?
     * @return Associated Joystick code OR {@link #NONE} if none corresponds
     */
    public static @NotNull JoystickCode obtainAxis(int index, boolean positive)
    {
        if (positive)
        {
            return JoystickCode.obtain(index, JoystickInputType.AXIS_POSITIVE);
        }

        return JoystickCode.obtain(index, JoystickInputType.AXIS_NEGATIVE);
    }

    /**
     * Obtain Joystick code associate to a button
     *
     * @param index Button index
     * @return Associated Joystick code OR {@link #NONE} if none corresponds
     */
    public static @NotNull JoystickCode obtainButton(int index)
    {
        return JoystickCode.obtain(index, JoystickInputType.BUTTON);
    }

    /**
     * Joystick input index
     */
    private final int               index;
    /**
     * Joystick input type
     */
    private final JoystickInputType joystickInputType;

    /**
     * Create a joystick code
     *
     * @param index             Joystick input index
     * @param joystickInputType Joystick input type
     */
    JoystickCode(int index, JoystickInputType joystickInputType)
    {
        this.index = index;
        this.joystickInputType = joystickInputType;
    }

    /**
     * Joystick input index
     *
     * @return Joystick input index
     */
    public int index()
    {
        return this.index;
    }

    /**
     * Joystick input type
     *
     * @return Joystick input type
     */
    public @NotNull JoystickInputType joystickInputType()
    {
        return this.joystickInputType;
    }
}

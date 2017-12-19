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

/**
 * Codes for joystick inputs
 */
public enum JoystickCode
{
    AXIS_1_POSITIVE(0, JoystickInputType.AXIS_POSITIVE),
    AXIS_1_NEGATIVE(0, JoystickInputType.AXIS_NEGATIVE),
    AXIS_2_POSITIVE(1, JoystickInputType.AXIS_POSITIVE),
    AXIS_2_NEGATIVE(1, JoystickInputType.AXIS_NEGATIVE),
    AXIS_3_POSITIVE(2, JoystickInputType.AXIS_POSITIVE),
    AXIS_3_NEGATIVE(2, JoystickInputType.AXIS_NEGATIVE),
    AXIS_4_POSITIVE(3, JoystickInputType.AXIS_POSITIVE),
    AXIS_4_NEGATIVE(3, JoystickInputType.AXIS_NEGATIVE),
    BUTTON_1(0, JoystickInputType.BUTTON),
    BUTTON_2(1, JoystickInputType.BUTTON),
    BUTTON_3(2, JoystickInputType.BUTTON),
    BUTTON_4(3, JoystickInputType.BUTTON),
    BUTTON_5(4, JoystickInputType.BUTTON),
    BUTTON_6(5, JoystickInputType.BUTTON),
    BUTTON_7(6, JoystickInputType.BUTTON),
    BUTTON_8(7, JoystickInputType.BUTTON),
    BUTTON_9(8, JoystickInputType.BUTTON),
    BUTTON_10(9, JoystickInputType.BUTTON),
    BUTTON_11(10, JoystickInputType.BUTTON),
    BUTTON_12(11, JoystickInputType.BUTTON),
    BUTTON_13(12, JoystickInputType.BUTTON),
    BUTTON_14(13, JoystickInputType.BUTTON),
    BUTTON_15(14, JoystickInputType.BUTTON),
    BUTTON_16(15, JoystickInputType.BUTTON),
    NONE(-1, JoystickInputType.NONE);

    public static final int MAX_AXIS_INDEX   = 3;
    public static final int MAX_BUTTON_INDEX = 15;

    private static JoystickCode obtain(int index, JoystickInputType joystickInputType)
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

    public static JoystickCode obtainAxis(int index, boolean positive)
    {
        if (positive)
        {
            return JoystickCode.obtain(index, JoystickInputType.AXIS_POSITIVE);
        }

        return JoystickCode.obtain(index, JoystickInputType.AXIS_NEGATIVE);
    }

    public static JoystickCode obtainButton(int index)
    {
        return JoystickCode.obtain(index, JoystickInputType.BUTTON);
    }

    private final int               index;
    private final JoystickInputType joystickInputType;

    JoystickCode(int index, JoystickInputType joystickInputType)
    {
        this.index = index;
        this.joystickInputType = joystickInputType;
    }

    public int index()
    {
        return this.index;
    }

    public JoystickInputType joystickInputType()
    {
        return this.joystickInputType;
    }
}

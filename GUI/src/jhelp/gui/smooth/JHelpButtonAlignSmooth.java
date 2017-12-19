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
package jhelp.gui.smooth;

/**
 * Alignment of icon and text in {@link JHelpButtonSmooth}
 *
 * @author JHelp
 */
public enum JHelpButtonAlignSmooth
{
    /**
     * Place icon at left, text at right
     */
    ICON_LEFT_TEXT_RIGHT,
    /**
     * If there has an icon, the icon is draw and the text is not draw
     */
    ICON_ONLY_IF_EXISTS,
    /**
     * Icon up, text bottom
     */
    ICON_UP_TEXT_BOTTOM,
    /**
     * Text left, icon right
     */
    TEXT_LEFT_ICON_RIGHT,
    /**
     * Text is draw over the icon
     */
    TEXT_OVER_ICON,
    /**
     * Text up icon down
     */
    TEXT_UP_ICON_BOTTOM
}
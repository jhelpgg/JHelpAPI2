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
package jhelp.gui.serializer;

/**
 * Defines the constants
 */
public interface Constants
{
    /**
     * Main markup for describe a font:
     * <ul>
     * <li><b>family</b> : Font family</li>
     * <li><b>size</b> : Font size</li>
     * <li><b>bold</b> : Indicates if its bold (false by default)</li>
     * <li><b>italic</b> : Indicates if its italic (false by default)</li>
     * <li><b>underline</b> : Indicates if its underline (false by default)</li>
     * </ul>
     */
    String MARKUP_FONT                = "Font";
    // **************************
    // *** Markups for parser ***
    // **************************
    /**
     * Main markup for describe a list of a gradient:
     * <ul>
     * <li><b>upLeft</b> : Up left color</li>
     * <li><b>upRight</b> : Up right color</li>
     * <li><b>downLeft</b> : Down left color</li>
     * <li><b>downRight</b> : Down right color</li>
     * </ul>
     */
    String MARKUP_GRADIENT            = "Gradient";
    /**
     * Main markup for describe a gradient horizontal.It have Percent ({@link #MARKUP_PERCENT}) as children
     */
    String MARKUP_GRADIENT_HORIZONTAL = "GradientHorizontal";
    /**
     * Main markup for describe a gradient vertical.It have Percent ({@link #MARKUP_PERCENT}) as children
     */
    String MARKUP_GRADIENT_VERTICAL   = "GradientVertical";
    /**
     * A percent, color couple
     * <ul>
     * <li><b>color</b> : the color</li>
     * <li><b>percent</b> : the percent</li>
     * </ul>
     */
    String MARKUP_PERCENT             = "Percent";
    /**
     * Bold status. Used by : {@link #MARKUP_FONT}
     */
    String PARAMETER_BOLD             = "bold";
    /**
     * A color. Used by : {@link #MARKUP_PERCENT}
     */
    String PARAMETER_COLOR            = "color";
    /**
     * Down left color. Used by : {@link #MARKUP_GRADIENT}
     */
    String PARAMETER_DOWN_LEFT        = "downLeft";
    /**
     * Down right color. Used by : {@link #MARKUP_GRADIENT}
     */
    String PARAMETER_DOWN_RIGHT       = "downRight";
    /**
     * Font family name. Used by : {@link #MARKUP_FONT}
     */
    String PARAMETER_FAMILY           = "family";
    /**
     * Italic status. Used by : {@link #MARKUP_FONT}
     */
    String PARAMETER_ITALIC           = "italic";
    /**
     * A percent. Used by : {@link #MARKUP_PERCENT}
     */
    String PARAMETER_PERCENT          = "percent";
    /**
     * Font size. Used by : {@link #MARKUP_FONT}
     */
    String PARAMETER_SIZE             = "size";
    /**
     * Underline status. Used by : {@link #MARKUP_FONT}
     */
    String PARAMETER_UNDERLINE        = "underline";
    // *****************************
    // *** Parameters for parser ***
    // *****************************
    /**
     * Up left color. Used by : {@link #MARKUP_GRADIENT}
     */
    String PARAMETER_UP_LEFT          = "upLeft";
    /**
     * Up right color. Used by : {@link #MARKUP_GRADIENT}
     */
    String PARAMETER_UP_RIGHT         = "upRight";
}
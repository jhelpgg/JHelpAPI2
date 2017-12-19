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
package jhelp.gui.xml;

/**
 * Defines the constants
 */
public interface Constants
{
    // *********************
    // *** Layout values ***
    // *********************

    /**
     * Represents the {@link jhelp.gui.twoD.JHelpBorderLayout} class. It layout components one in center, other in "borders"
     */
    String JHelpBorderLayout                  = "JHelpBorderLayout";
    /**
     * Represents the {@link jhelp.gui.twoD.JHelpHorizontalLayout} class. It layout components one side other one let to right"
     */
    String JHelpHorizontalLayout              = "JHelpHorizontalLayout";
    /**
     * Represents the {@link jhelp.gui.twoD.JHelpTableLayout} class. It layout components in table with cells
     */
    String JHelpTableLayout                   = "JHelpTableLayout";
    /**
     * Represents the {@link jhelp.gui.twoD.JHelpVerticalLayout} class. It layout components one bellow other
     */
    String JHelpVerticalLayout                = "JHelpVerticalLayout";
    /**
     * Markup for describe a background component with round rectangle shape. It must contains a component as sub_markup
     * <ul>
     * <li><b>{@link #PARAMETER_NAME name}</b> : Name of the component [Mandatory]</li>
     * <li><b>arc</b> : Arc size (Default 20) [Optional]</li>
     * <li><b>background</b> : Background color in hexadecimal (AARRGGBB) (Default 80808080) [Optional]</li>
     * </ul>
     */
    String MARKUP_BACKGROUND_ROUND_REACTANLGE = "JHelpBackgroundRoundRectangle";
    /**
     * Markup for describe a background component with round sausage shape. It must contains a component as sub_markup
     * <ul>
     * <li><b>{@link #PARAMETER_NAME name}</b> : Name of the component [Mandatory]</li>
     * <li><b>space</b> : Empty space (Default 10) [Optional]</li>
     * <li><b>background</b> : Background color in hexadecimal (AARRGGBB) (Default 80808080) [Optional]</li>
     * </ul>
     */
    String MARKUP_BACKGROUND_SAUSSAGE         = "JHelpBackgroundSaussage";

    // ***************
    // *** Markups ***
    // ***************

    /**
     * Markup for describe the main frame
     * <ul>
     * <li><b>{@link #PARAMETER_NAME name}</b> : Name of the class (With the package name) [Mandatory]</li>
     * <li><b>{@link #PARAMETER_TITLE title}</b> : Frame title [Optional]</li>
     * <li><b>{@link #PARAMETER_LAYOUT layout}</b> : Layout class to use in {{@link #JHelpBorderLayout} (Default value),
     * {@link #JHelpHorizontalLayout}, {@link #JHelpVerticalLayout}, {@link #JHelpTableLayout} [Optional]</li>
     * </ul>
     */
    String MARKUP_FRAME     = "JHelpFrame2D";
    /**
     * Layout class to use in {{@link #JHelpBorderLayout} (Default value), {@link #JHelpHorizontalLayout},
     * {@link #JHelpVerticalLayout}, {@link #JHelpTableLayout} .<br>
     * Used by : {@link #MARKUP_FRAME}
     */
    String PARAMETER_LAYOUT = "layout";

    // ******************
    // *** Parameters ***
    // ******************

    /**
     * Name of component.<br>
     * Used by : {@link #MARKUP_FRAME}, {@link #MARKUP_BACKGROUND_ROUND_REACTANLGE} and {@link #MARKUP_BACKGROUND_SAUSSAGE}
     */
    String PARAMETER_NAME  = "name";
    /**
     * Title of the main frame.<br>
     * Used by : {@link #MARKUP_FRAME}
     */
    String PARAMETER_TITLE = "title";
}
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

package jhelp.util.preference;

/**
 * Defines the constants
 */
public interface PreferencesFileConstants
{
    /**
     * Markup use for one preference
     */
    String MARKUP_PREFERENCE  = "Preference";
    /**
     * Main markup to encapsulate all preferences
     */
    String MARKUP_PREFERENCES = "Preferences";
    /**
     * Parameter name of preference
     */
    String PARAMETER_NAME     = "name";
    /**
     * Parameter type of preference.<br>Can be ARRAY (byte[]), BOOLEAN (boolean), FILE (java.io.File), INTEGER (int) or STRING (java.lang.String)
     */
    String PARAMETER_TYPE     = "type";
    /**
     * Parameter value of preference
     */
    String PARAMETER_VALUE    = "value";
}
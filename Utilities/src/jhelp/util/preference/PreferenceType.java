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

import java.io.File;
import java.util.Locale;

/**
 * Preference type
 *
 * @author JHelp
 */
public enum PreferenceType
{
    /**
     * Type byte[]
     */
    ARRAY,
    /**
     * Type boolean
     */
    BOOLEAN,
    /**
     * Type {@link File}
     */
    FILE,
    /**
     * Type int
     */
    INTEGER,
    /**
     * Type {@link Locale}
     */
    LOCALE,
    /**
     * Type {@link String}
     */
    STRING,
    /**
     * Type {@link Enum}
     */
    ENUM
}
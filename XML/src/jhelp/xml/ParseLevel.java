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

/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.xml;

/**
 * Level of parsing<br>
 * <br>
 * Last modification : 21 fevr. 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public enum ParseLevel
{
    /** Only exception are detect */
    EXCEPTION(false, false, false),
    /** Detect exception and open/close markup */
    EXCEPTION_AND_MARKUP(true, false, false),
    /** Detect exception and open/close markup and parameters */
    EXCEPTION_AND_MARKUP_AND_PARAMETER(true, true, false),
    /** Detect exception and open/close markup and parameters and texts */
    EXCEPTION_AND_MARKUP_AND_PARAMETER_AND_TEXT(true, true, true),
    /** Detect exception and open/close markup and texts */
    EXCEPTION_AND_MARKUP_AND_TEXT(true, false, true),
    /** Detect exception and parameters */
    EXCEPTION_AND_PARAMETER(false, true, false),
    /** Detect exception and parameters and texts */
    EXCEPTION_AND_PARAMETER_AND_TEXT(false, true, true),
    /** Detect exception and texts */
    EXCEPTION_AND_TEXT(false, false, true);

    /** Indicates if open/close markup is detect */
    private final boolean makup;
    /** Indicates if parameters are detect */
    private final boolean parameter;
    /** Indicates if texts are detect */
    private final boolean text;

    /**
     * Constructs ParseLevel
     *
     * @param makup
     *           Indicates if markup open/close is detect
     * @param parameter
     *           Indicates if parameters are detect
     * @param text
     *           Indicates if texts are detect
     */
    ParseLevel(final boolean makup, final boolean parameter, final boolean text)
    {
        this.makup = makup;
        this.parameter = parameter;
        this.text = text;
    }

    /**
     * Indicates if markup open/close is detect
     *
     * @return {@code true} if markup open/close is detect
     */
    public boolean isMakup()
    {
        return this.makup;
    }

    /**
     * Indicates if parameters are detect
     *
     * @return {@code true} if parameters are detect
     */
    public boolean isParameter()
    {
        return this.parameter;
    }

    /**
     * Indicates if texts are detect
     *
     * @return {@code true} if texts are detect
     */
    public boolean isText()
    {
        return this.text;
    }
}
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
 * Indicates that a require parameter miss in Markup XML <br>
 * <br>
 * Last modification : 21 fevr. 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public class MissingRequiredParameterException
        extends ExceptionXML
{
    /** serialVersionUID */
    private static final long serialVersionUID = -3394387170589763395L;

    /**
     * Constructs MissingRequiredParameterException
     *
     * @param parameterName
     *           Name of missing parameter
     * @param markupName
     *           Markup name
     */
    public MissingRequiredParameterException(final String parameterName, final String markupName)
    {
        this(parameterName, markupName, null);
    }

    /**
     * Constructs MissingRequiredParameterException
     *
     * @param parameterName
     *           Name of missing parameter
     * @param markupName
     *           Markup name
     * @param cause
     *           Cause of the exception
     */
    public MissingRequiredParameterException(final String parameterName, final String markupName, final Throwable cause)
    {
        super("The markup '" + markupName + "' requiered the parameter " + parameterName, cause);
    }
}
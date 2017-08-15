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

import jhelp.util.text.UtilText;

/**
 * Exception if a parameter is not valid <br>
 * <br>
 * Last modification : 21 fevr. 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public class InvalidParameterValueException
        extends ExceptionXML
{
    /** serialVersionUID */
    private static final long serialVersionUID = 1780708232370499648L;

    /**
     * Constructs InvalidParameterValueException
     *
     * @param parameterName
     *           Parameter name
     * @param markupName
     *           Markup where append
     */
    public InvalidParameterValueException(final String parameterName, final String markupName)
    {
        this(parameterName, markupName, null, null);
    }

    /**
     * Constructs InvalidParameterValueException
     *
     * @param parameterName
     *           Parameter name
     * @param markupName
     *           Markup where append
     * @param message
     *           Message
     */
    public InvalidParameterValueException(final String parameterName, final String markupName, final String message)
    {
        this(parameterName, markupName, message, null);
    }

    /**
     * Constructs InvalidParameterValueException
     *
     * @param parameterName
     *           Parameter name
     * @param markupName
     *           Markup where append
     * @param message
     *           Message
     * @param cause
     *           Cause
     */
    public InvalidParameterValueException(
            final String parameterName, final String markupName, final String message, final Throwable cause)
    {
        super(UtilText.concatenate("The parameter ", parameterName, " in the markup ", markupName,
                                   " have invalid value", (message == null
                                                           ? ""
                                                           : " : " + message)), cause);
    }

    /**
     * Constructs InvalidParameterValueException
     *
     * @param parameterName
     *           Parameter name
     * @param markupName
     *           Markup where append
     * @param cause
     *           Cause
     */
    public InvalidParameterValueException(final String parameterName, final String markupName, final Throwable cause)
    {
        this(parameterName, markupName, null, cause);
    }
}
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
 * Exception if a text is not valid <br>
 * <br>
 * Last modification : 21 fevr. 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public class InvalidTextException
        extends ExceptionXML
{
    /** serialVersionUID */
    private static final long serialVersionUID = 4289961699877912162L;

    /**
     * Constructs InvalidTextException
     */
    public InvalidTextException()
    {
    }

    /**
     * Constructs InvalidTextException
     *
     * @param message
     *           Message
     */
    public InvalidTextException(final String message)
    {
        super(message);
    }

    /**
     * Constructs InvalidTextException
     *
     * @param message
     *           Message
     * @param cause
     *           Cause
     */
    public InvalidTextException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructs InvalidTextException
     *
     * @param cause
     *           Cause
     */
    public InvalidTextException(final Throwable cause)
    {
        super(cause);
    }
}
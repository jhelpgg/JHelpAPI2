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
package jhelp.vectorial.path;

import jhelp.util.text.UtilText;

/**
 * Exception when parse failed
 *
 * @author JHelp
 */
public class ParsePathException
        extends Exception
{
    /**
     * Create a new instance of ParsePathException
     *
     * @param message
     *           Exception message
     */
    public ParsePathException(final Object... message)
    {
        super(UtilText.concatenate(message));
    }

    /**
     * Create a new instance of ParsePathException
     *
     * @param cause
     *           Parent cause
     */
    public ParsePathException(final Throwable cause)
    {
        super(cause);
    }

    /**
     * Create a new instance of ParsePathException
     *
     * @param cause
     *           Parent cause
     * @param message
     *           Exception message
     */
    public ParsePathException(final Throwable cause, final Object... message)
    {
        super(UtilText.concatenate(message), cause);
    }
}
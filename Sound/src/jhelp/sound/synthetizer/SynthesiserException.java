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

package jhelp.sound.synthetizer;

/**
 * Exception for synthetizer
 */
public class SynthesiserException
        extends Exception
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 6141770925190083665L;

    /**
     * Constructs SynthesiserException
     */
    public SynthesiserException()
    {
    }

    /**
     * Constructs SynthesiserException
     *
     * @param message Message
     */
    public SynthesiserException(final String message)
    {
        super(message);
    }

    /**
     * Constructs SynthesiserException
     *
     * @param message Message
     * @param cause   Cause of exception
     */
    public SynthesiserException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructs SynthesiserException
     *
     * @param cause Cause of exception
     */
    public SynthesiserException(final Throwable cause)
    {
        super(cause);
    }
}
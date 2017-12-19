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

package jhelp.security.pulicPrivateKey;

import jhelp.util.text.UtilText;

/**
 * Throw if an algorithm is unknown
 */
public class NoSuchAlgorithmException
        extends RuntimeException
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -5787604001507253790L;

    /**
     * Constructs NoSuchAlgorithmException
     *
     * @param algorithm Algorithm name
     * @param cause     Cause
     */
    public NoSuchAlgorithmException(final String algorithm, final Throwable cause)
    {
        super(UtilText.concatenate("Algorithm ", algorithm, " doesn't exits !"), cause);
    }
}
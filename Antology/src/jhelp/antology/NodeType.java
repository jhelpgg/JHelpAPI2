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

package jhelp.antology;

import jhelp.util.io.Binarizable;

/**
 * Node content type
 */
public enum NodeType
{
    /**
     * String
     */
    STRING,
    /**
     * int
     */
    INT,
    /**
     * double
     */
    DOUBLE,
    /**
     * {@link Binarizable}
     */
    BINARIZABLE,
    /**
     * Special type for {@link Node#WILDCARD}
     */
    WILDCARD
}

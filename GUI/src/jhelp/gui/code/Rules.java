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

package jhelp.gui.code;

/**
 * Rules type
 */
public enum Rules
{
    /**
     * Default rule, applied when nothing match
     */
    DEFAULT,
    /**
     * Rule for comment
     */
    COMMENT,
    /**
     * Rule for key words
     */
    KEY_WORD,
    /**
     * Rule for literals
     */
    STRING,
    /**
     * Rule for symbols
     */
    SYMBOL,
    /**
     * Rule  for primitive
     */
    PRIMITIVE,
    /**
     * Rule for operand
     */
    OPERAND
}

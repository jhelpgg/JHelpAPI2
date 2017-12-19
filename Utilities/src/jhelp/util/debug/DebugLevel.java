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

package jhelp.util.debug;

import com.sun.istack.internal.NotNull;

/**
 * Level of debugging
 */
public enum DebugLevel
{
    /**
     * Verbose level
     */
    VERBOSE("VERBOSE ", (byte) 4),
    /**
     * Debug level
     */
    DEBUG("DEBUG ", (byte) 3),
    /**
     * Information level
     */
    INFORMATION("INFORMATION ", (byte) 2),
    /**
     * Warning level
     */
    WARNING("/!\\ WARNING /!\\ ", (byte) 1),
    /**
     * Error level
     */
    ERROR("$@-FAILED-@$ ", (byte) 0);

    /**
     * Debug level header
     */
    private final String header;
    /**
     * Debug level order
     */
    private final byte   order;

    /**
     * Create debug level
     *
     * @param header Debug level header
     * @param order  Debug level order
     */
    DebugLevel(String header, byte order)
    {
        this.header = header;
        this.order = order;
    }

    /**
     * Debug level header
     *
     * @return Debug level header
     */
    public @NotNull String header()
    {
        return this.header;
    }

    /**
     * Debug level order
     *
     * @return Debug level order
     */
    public byte order()
    {
        return this.order;
    }
}

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

package jhelp.util.resources;

/**
 * Generic resource element (File or directory)
 *
 * @author JHelp
 */
public abstract class ResourceElement
{
    /**
     * Resource name
     *
     * @return Resource name
     */
    public abstract String getName();

    /**
     * Resource path
     *
     * @return Resource path
     */
    public abstract String getPath();

    /**
     * Indicates if the resource element is a directory
     *
     * @return {@code true} if the resource element is a directory
     */
    public abstract boolean isDirectory();
}
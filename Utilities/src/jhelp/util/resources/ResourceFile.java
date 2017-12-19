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
 * Represents a resource file
 *
 * @author JHelp
 */
public class ResourceFile
        extends ResourceElement
{
    /**
     * File path
     */
    private final String path;

    /**
     * Create a new instance of ResourceFile
     *
     * @param path File path
     */
    public ResourceFile(String path)
    {
        if (path == null)
        {
            throw new NullPointerException("path MUST NOT be null");
        }

        path = path.trim();
        this.path = path.endsWith("/")
                    ? path.substring(0, path.length() - 1)
                    : path;
    }

    /**
     * File name <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return File name
     * @see jhelp.util.resources.ResourceElement#getName()
     */
    @Override
    public String getName()
    {
        final int index = this.path.lastIndexOf('/');

        if (index < 0)
        {
            return this.path;
        }

        return this.path.substring(index + 1);
    }

    /**
     * File path <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return File path
     * @see jhelp.util.resources.ResourceElement#getPath()
     */
    @Override
    public String getPath()
    {
        return this.path;
    }

    /**
     * Indicates if its a directory <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return {@code false}
     * @see jhelp.util.resources.ResourceElement#isDirectory()
     */
    @Override
    public boolean isDirectory()
    {
        return false;
    }
}
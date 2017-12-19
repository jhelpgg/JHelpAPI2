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

package jhelp.gui.smooth.resources;

import jhelp.gui.smooth.JHelpConstantsSmooth;
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpFont.Type;
import jhelp.util.gui.JHelpFont.Value;
import jhelp.util.gui.JHelpImage;
import jhelp.util.resources.ResourceText;
import jhelp.util.resources.Resources;

/**
 * Helper for access smooth resources
 *
 * @author JHelp
 */
public class JHelpResourcesSmooth
        implements JHelpConstantsSmooth
{
    static
    {
        RESOURCES = new Resources(JHelpResourcesSmooth.class);
        RESOURCE_TEXT = JHelpResourcesSmooth.RESOURCES.obtainResourceText("texts/texts");
    }

    /**
     * Smooth resources access
     */
    public static final Resources    RESOURCES;
    /**
     * Smooth text access
     */
    public static final ResourceText RESOURCE_TEXT;

    /**
     * Obtain a smooth font
     *
     * @param name Font relative path
     * @param size Size
     * @return Resource font
     */
    public static JHelpFont font(final String name, final int size)
    {
        return JHelpResourcesSmooth.RESOURCES.obtainJHelpFont(Type.TRUE_TYPE, name, size, Value.FREE, Value.FREE,
                                                              false);
    }

    /**
     * Obtain an image from resource
     *
     * @param path Resource path
     * @return The image
     */
    public static JHelpImage obtainJHelpImage(final String path)
    {
        try
        {
            return JHelpResourcesSmooth.RESOURCES.obtainJHelpImage(path);
        }
        catch (final Exception exception)
        {
            return JHelpImage.DUMMY;
        }
    }
}
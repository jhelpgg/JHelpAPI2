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

package samples.jhelp.util.cache;

import java.io.File;
import jhelp.util.cache.CacheElement;
import jhelp.util.gui.JHelpImage;

/**
 * Image element of cache
 */
public class ImageElement extends CacheElement<JHelpImage>
{
    /**
     * File where lies the image
     */
    private final File file;

    /**
     * Create the cacheElement
     *
     * @param path Image path
     */
    public ImageElement(String path)
    {
        this.file = new File(path);
    }

    /**
     * Create the image
     *
     * @return Created image
     */
    @Override
    protected JHelpImage create()
    {
        try
        {
            return JHelpImage.loadImage(this.file);
        }
        catch (Exception exception)
        {
            return JHelpImage.DUMMY;
        }
    }
}

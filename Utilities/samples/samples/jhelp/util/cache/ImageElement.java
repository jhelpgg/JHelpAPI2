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

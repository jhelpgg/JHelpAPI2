package samples.jhelp.util.cache;

import jhelp.util.cache.Cache;
import jhelp.util.gui.JHelpImage;

public class MainImageCache
{
    private static final Cache<JHelpImage> CACHE_IMAGE = new Cache<>();

    public static void main(String[] arguments)
    {
        final String imageSmilePath = "/home/user/Images/smiley.png";
        final String imageSadPath   = "/home/user/Images/sad.png";
        //Store image
        MainImageCache.CACHE_IMAGE.add("Smiley", new ImageElement(imageSmilePath));
        //...
        //Get image to show it
        JHelpImage imageSmile = MainImageCache.CACHE_IMAGE.get("Smiley");

        JHelpImage imageSad = MainImageCache.CACHE_IMAGE.get("Sad", new ImageElement(imageSadPath));
    }
}

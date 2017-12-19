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

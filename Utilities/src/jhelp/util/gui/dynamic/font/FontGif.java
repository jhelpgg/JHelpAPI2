/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any
 * damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.util.gui.dynamic.font;

import com.sun.istack.internal.NotNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jhelp.util.debug.Debug;
import jhelp.util.gui.GIF;
import jhelp.util.io.UtilIO;
import jhelp.util.list.Pair;
import jhelp.util.resources.ResourceElement;
import jhelp.util.resources.Resources;
import jhelp.util.resources.ResourcesSystem;
import jhelp.util.text.StringCutter;
import jhelp.util.thread.Pointer;

/**
 * Font base on GIF animations
 *
 * @author JHelp
 */
public final class FontGif
{
    /**
     * List of available font GIF names
     */
    public static final  List<String> FONT_GIF_NAMES;
    /**
     * File that describe a font
     */
    private static final String       CHARACTERS;
    /**
     * Resources to access GIF images
     */
    private static final Resources    RESOURCES;

    static
    {
        final List<String> names = new ArrayList<String>();
        CHARACTERS = "characters.txt";
        RESOURCES = new Resources(FontGif.class);

        try
        {
            final ResourcesSystem resourcesSystem = FontGif.RESOURCES.obtainResourcesSystem();
            boolean               valid;

            for (final ResourceElement resourceElement : resourcesSystem.obtainList(ResourcesSystem.ROOT))
            {
                if (resourceElement.isDirectory())
                {
                    valid = false;

                    for (final ResourceElement element : resourcesSystem.obtainList(resourceElement))
                    {
                        if ((!element.isDirectory()) && FontGif.CHARACTERS.equals(element.getName()))
                        {
                            valid = true;
                            break;
                        }
                    }

                    if (valid)
                    {
                        names.add(resourceElement.getName());
                    }
                }
            }
        }
        catch (final IOException exception)
        {
            Debug.exception(exception);
        }

        Collections.sort(names);
        FONT_GIF_NAMES = Collections.unmodifiableList(names);
    }

    /**
     * List of association between a list of character and a GIF image
     */
    private final List<Pair<String, GIF>> gifs;
    /**
     * Font height
     */
    private       int                     height;
    /**
     * Space size
     */
    private       int                     space;

    /**
     * Create a new instance of FontGif
     *
     * @param font Font folder name
     * @throws IOException On creation issue
     */
    public FontGif(final @NotNull String font)
            throws IOException
    {
        this.gifs = new ArrayList<Pair<String, GIF>>();
        this.parseFont(font);
    }

    /**
     * Parse font description
     *
     * @param font Font folder
     * @throws IOException On parsing issue
     */
    private void parseFont(final @NotNull String font) throws IOException
    {
        BufferedReader bufferedReader = null;
        final String   header         = font + "/";
        this.space = Integer.MAX_VALUE;
        this.height = 0;
        Pointer<IOException> pointer = new Pointer<>();

        UtilIO.readLines(() -> FontGif.RESOURCES.obtainResourceStream(header + FontGif.CHARACTERS),
                         line ->
                         {
                             if ((line.length() > 0) && (line.charAt(0) != '#'))
                             {
                                 int index = line.indexOf('\t');

                                 if (index > 0)
                                 {
                                     String key = line.substring(0, index);

                                     index = line.lastIndexOf('\t');

                                     if (index > 0)
                                     {
                                         String image = line.substring(index + 1);
                                         GIF    gif;

                                         try
                                         {
                                             final InputStream inputStream = FontGif.RESOURCES.obtainResourceStream(
                                                     header + image);
                                             gif = new GIF(inputStream);
                                             inputStream.close();
                                         }
                                         catch (IOException io)
                                         {
                                             pointer.data(io);
                                             return;
                                         }

                                         this.gifs.add(new Pair<String, GIF>(key, gif));
                                         this.space = Math.min(this.space, gif.getWidth());
                                         this.height = Math.max(this.height, gif.getHeight());
                                     }
                                 }
                             }
                         },
                         pointer::data);

        if (pointer.data() != null)
        {
            throw pointer.data();
        }
    }

    /**
     * Compute text description from a String
     *
     * @param text String to get text description
     * @return Text description
     */
    public @NotNull GifText computeGifText(final @NotNull String text)
    {
        int                     width        = 0;
        int                     y            = 0;
        int                     x, index;
        final List<GifPosition> gifPositions = new ArrayList<GifPosition>();
        final StringCutter      stringCutter = new StringCutter(text, '\n');
        String                  line         = stringCutter.next();
        char[]                  characters;
        final int               size         = this.gifs.size();
        Pair<String, GIF>       pair;
        GIF                     gif;

        while (line != null)
        {
            x = 0;
            characters = line.toCharArray();

            for (final char character : characters)
            {
                gif = null;

                for (index = 0; index < size; index++)
                {
                    pair = this.gifs.get(index);

                    if (pair.first.indexOf(character) >= 0)
                    {
                        gif = pair.second;
                        break;
                    }
                }

                if (gif != null)
                {
                    gifPositions.add(new GifPosition(gif, x, y));
                    x += gif.getWidth();
                }
                else
                {
                    x += this.space;
                }
            }

            width = Math.max(x, width);
            y += this.height;
            line = stringCutter.next();
        }

        return new GifText(width, y, gifPositions);
    }
}
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

package jhelp.util.gui;

import com.sun.istack.internal.NotNull;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.Icon;
import jhelp.util.debug.Debug;
import jhelp.util.gui.JHelpAnimatedImage.AnimationMode;
import jhelp.util.gui.transformation.Transformation;
import jhelp.util.gui.transformation.Vector;
import jhelp.util.image.pcx.PCX;
import jhelp.util.image.raster.RasterImage;
import jhelp.util.image.raster.RasterImageType;
import jhelp.util.io.FileImageInformation;
import jhelp.util.list.HeavyObject;
import jhelp.util.list.Pair;
import jhelp.util.list.Queue;
import jhelp.util.list.SortedArray;
import jhelp.util.math.Math2;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.Mutex;

/**
 * Represents an image.<br>
 * You can draw on image only this image if it is on draw mode, see {@link #startDrawMode()}, {@link #endDrawMode()} and
 * {@link #isDrawMode()}.<br>
 * You can also create {@link JHelpSprite}, that are small image that can be easy animated.<br>
 * The image is refresh at screen , only if exit of draw mode {@link #endDrawMode()} or call {@link #update()}
 *
 * @author JHelp
 */
public class JHelpImage
        implements ConstantsGUI, HeavyObject, RasterImage, Icon
{
    /**
     * Palette to use
     */
    private static final int[]      PALETTE      =
            {
                    0xFFFFFFFF, 0xFFFFFFC0, 0xFFFFFF80, 0xFFFFFF40, 0xFFFFFF00,       //
                    0xFFFFC0FF, 0xFFFFC0C0, 0xFFFC0F80, 0xFFFFC040, 0xFFFFC000,       //
                    0xFFFF80FF, 0xFFFF80C0, 0xFFFF8080, 0xFFFF8040, 0xFFFF8000,       //
                    0xFFFF40FF, 0xFFFF40C0, 0xFFFF4080, 0xFFFF4040, 0xFFFF4000,       //
                    0xFFFF00FF, 0xFFFF00C0, 0xFFFF0080, 0xFFFF0040, 0xFFFF0000,       //
                    //
                    0xFFC0FFFF, 0xFFC0FFC0, 0xFFC0FF80, 0xFFC0FF40, 0xFFC0FF00,       //
                    0xFFC0C0FF, 0xFFC0C0C0, 0xFFFC0F80, 0xFFC0C040, 0xFFC0C000,       //
                    0xFFC080FF, 0xFFC080C0, 0xFFC08080, 0xFFC08040, 0xFFC08000,       //
                    0xFFC040FF, 0xFFC040C0, 0xFFC04080, 0xFFC04040, 0xFFC04000,       //
                    0xFFC000FF, 0xFFC000C0, 0xFFC00080, 0xFFC00040, 0xFFC00000,       //
                    //
                    0xFF80FFFF, 0xFF80FFC0, 0xFF80FF80, 0xFF80FF40, 0xFF80FF00,       //
                    0xFF80C0FF, 0xFF80C0C0, 0xFFFC0F80, 0xFF80C040, 0xFF80C000,       //
                    0xFF8080FF, 0xFF8080C0, 0xFF808080, 0xFF808040, 0xFF808000,       //
                    0xFF8040FF, 0xFF8040C0, 0xFF804080, 0xFF804040, 0xFF804000,       //
                    0xFF8000FF, 0xFF8000C0, 0xFF800080, 0xFF800040, 0xFF800000,       //
                    //
                    0xFF40FFFF, 0xFF40FFC0, 0xFF40FF80, 0xFF40FF40, 0xFF40FF00,       //
                    0xFF40C0FF, 0xFF40C0C0, 0xFFFC0F80, 0xFF40C040, 0xFF40C000,       //
                    0xFF4080FF, 0xFF4080C0, 0xFF408080, 0xFF408040, 0xFF408000,       //
                    0xFF4040FF, 0xFF4040C0, 0xFF404080, 0xFF404040, 0xFF404000,       //
                    0xFF4000FF, 0xFF4000C0, 0xFF400080, 0xFF400040, 0xFF400000,       //
                    //
                    0xFF00FFFF, 0xFF00FFC0, 0xFF00FF80, 0xFF00FF40, 0xFF00FF00,       //
                    0xFF00C0FF, 0xFF00C0C0, 0xFFFC0F80, 0xFF00C040, 0xFF00C000,       //
                    0xFF0080FF, 0xFF0080C0, 0xFF008080, 0xFF008040, 0xFF008000,       //
                    0xFF0040FF, 0xFF0040C0, 0xFF004080, 0xFF004040, 0xFF004000,       //
                    0xFF0000FF, 0xFF0000C0, 0xFF000080, 0xFF000040, 0xFF000000
            };
    /**
     * Dummy image 1x1
     */
    public static final  JHelpImage DUMMY        = new JHelpImage(1, 1);
    /**
     * Palette size
     */
    public static final  int        PALETTE_SIZE = JHelpImage.PALETTE.length;

    /**
     * Compute blue part of color from YUV<br>
     * B = Y + 1.7790 * (U - 128)
     *
     * @param y Y
     * @param u U
     * @param v V
     * @return Blue part
     */
    public static int computeBlue(final double y, final double u, final double v)
    {
        return Math2.limit0_255((int) (y + (1.7721604 * (u - 128)) + (0.0009902 * (v - 128))));
    }

    /**
     * Compute green part of color from YUV<br>
     * G = Y - 0.3455 * (U - 128) - (0.7169 * (V - 128))
     *
     * @param y Y
     * @param u U
     * @param v V
     * @return Green part
     */
    public static int computeGreen(final double y, final double u, final double v)
    {
        return Math2.limit0_255((int) (y - (0.3436954 * (u - 128)) - (0.7141690 * (v - 128))));
    }

    /**
     * Compute red part of color from YUV<br>
     * R = Y + 1.4075 * (V - 128)
     *
     * @param y Y
     * @param u U
     * @param v V
     * @return Red part
     */
    public static int computeRed(final double y, final double u, final double v)
    {
        return Math2.limit0_255((int) ((y - (0.0009267 * (u - 128))) + (1.4016868 * (v - 128))));
    }

    /**
     * Compute U of a color<br>
     * U = R * -0.168736 + G * -0.331264 + B * 0.500000 + 128
     *
     * @param red   Red part
     * @param green Green part
     * @param blue  Blue part
     * @return U
     */
    public static double computeU(final int red, final int green, final int blue)
    {
        return ((-0.169 * red) - (0.331 * green)) + (0.500 * blue) + 128.0;
    }

    /**
     * Compute V of a color<br>
     * V = R * 0.500000 + G * -0.418688 + B * -0.081312 + 128
     *
     * @param red   Red part
     * @param green Green part
     * @param blue  Blue part
     * @return V
     */
    public static double computeV(final int red, final int green, final int blue)
    {
        return ((0.500 * red) - (0.419 * green) - (0.081 * blue)) + 128.0;
    }

    /**
     * Compute Y of a color<br>
     * Y = R * 0.299000 + G * 0.587000 + B * 0.114000
     *
     * @param red   Red part
     * @param green Green part
     * @param blue  Blue part
     * @return Y
     */
    public static double computeY(final int red, final int green, final int blue)
    {
        return (red * 0.299) + (green * 0.587) + (blue * 0.114);
    }

    /**
     * Create a bump image with 0.75 contrast, 12 dark, 1 shift X and 1 shift Y<br>
     * Note : If one of image is not in draw mode, all visible sprite (of this image) will be consider as a part of the
     * image
     *
     * @param source Image source
     * @param bump   Image used for bump
     * @return Bumped image
     */
    public static JHelpImage createBumpedImage(final JHelpImage source, final JHelpImage bump)
    {
        return JHelpImage.createBumpedImage(source, bump, 0.75, 12, 1, 1);
    }

    /**
     * Create a bump image<br>
     * Note : If one of image is not in draw mode, all visible sprite (of this image) will be consider as a part of the
     * image
     *
     * @param source   Image source
     * @param bump     Image used for bump
     * @param contrast Contrast to use in [0, 1].
     * @param dark     Dark to use. in [0, 255].
     * @param shiftX   Shift X [-3, 3].
     * @param shiftY   Shift Y [-3, 3].
     * @return Bumped image
     */
    public static JHelpImage createBumpedImage(
            final JHelpImage source, final JHelpImage bump, double contrast,
            final int dark, final int shiftX,
            final int shiftY)
    {
        final int width  = source.getWidth();
        final int height = source.getHeight();

        if ((width != bump.getWidth()) || (height != bump.getHeight()))
        {
            throw new IllegalArgumentException("Images must have the same size");
        }

        if (contrast < 0.5)
        {
            contrast *= 2;
        }
        else
        {
            contrast = (contrast * 18) - 8;
        }

        source.update();
        bump.update();
        final JHelpImage bumped = new JHelpImage(width, height);
        final JHelpImage temp   = new JHelpImage(width, height);

        bumped.startDrawMode();
        temp.startDrawMode();

        bumped.copy(bump);
        bumped.gray();
        bumped.contrast(contrast);

        temp.copy(bumped);
        temp.multiply(source);
        temp.darker(dark);

        bumped.invertColors();
        bumped.multiply(source);
        bumped.darker(dark);
        bumped.shift(shiftX, shiftY);
        bumped.addition(temp);

        bumped.endDrawMode();
        temp.endDrawMode();

        return bumped;
    }

    /**
     * Create a bump image with 0.75 contrast, 12 dark, -1 shift X and -1 shift Y<br>
     * Note : If one of image is not in draw mode, all visible sprite (of this image) will be consider as a part of the
     * image
     *
     * @param source Image source
     * @param bump   Image used for bump
     * @return Bumped image
     */
    public static JHelpImage createBumpedImage2(final JHelpImage source, final JHelpImage bump)
    {
        return JHelpImage.createBumpedImage(source, bump, 0.75, 12, -1, -1);
    }

    /**
     * Create an image from a buffered image
     *
     * @param bufferedImage Buffered image source
     * @return Created image
     */
    public static JHelpImage createImage(final BufferedImage bufferedImage)
    {
        final int width  = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        int[]     pixels = new int[width * height];

        pixels = bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);

        return new JHelpImage(width, height, pixels);
    }

    /**
     * Create a resized image from a given one in parameter.<br>
     * If the desired size is exactly the same has the given image, the image itself is return.<br>
     * In case of different size, if the given image is not in draw mode, visible sprites on it will be a part of resized
     * image
     *
     * @param image  Image to resize
     * @param width  New width
     * @param height New height
     * @return Resized image
     */
    public static JHelpImage createResizedImage(final JHelpImage image, final int width, final int height)
    {
        if ((width < 1) || (height < 1))
        {
            throw new IllegalArgumentException(
                    "width and height must be > 1, but it is specify : " + width + "x" + height);
        }

        if ((image.getWidth() == width) && (image.getHeight() == height))
        {
            return image;
        }

        final JHelpImage result = new JHelpImage(width, height);
        result.startDrawMode();
        result.fillRectangleScaleBetter(0, 0, width, height, image, false);
        result.endDrawMode();

        return result;
    }

    /**
     * Create an image resized to specify size from a buffered image
     *
     * @param bufferedImage Buffered image source
     * @param width         Result image width
     * @param height        Result image height
     * @return Created image
     */
    public static JHelpImage createThumbImage(final BufferedImage bufferedImage, final int width, final int height)
    {
        final BufferedImage thumb = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        final Graphics2D graphics2d = thumb.createGraphics();
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2d.drawImage(bufferedImage, 0, 0, width, height, null);

        final JHelpImage image = JHelpImage.createImage(thumb);

        graphics2d.dispose();
        thumb.flush();

        return image;
    }

    /**
     * Comput distance betwwen 2 colors
     *
     * @param color1 First color
     * @param color2 Second color
     * @return Color distance
     */
    private static int distanceColor(final int color1, final int color2)
    {
        return Math2.maximum(Math.abs(((color1 >> 16) & 0xFF) - ((color2 >> 16) & 0xFF)),
                             Math.abs(((color1 >> 8) & 0xFF) - ((color2 >> 8) & 0xFF)),
                             Math.abs((color1 & 0xFF) - (color2 & 0xFF)));
    }

    /**
     * Compare 2 images and compute if they "look" the same in compare the image border. That is to say if we obtain
     * border of
     * objects inside the image<br>
     * The precision is to determine the accepted distance in border limit, and percent to know the percent of accepted
     * pixels
     * doesn't match to precision.<br>
     * Note : if images have'nt same dimension, the smallest is firstly scale to fit to the biggest<br>
     * Note : if one image is not in draw mode, the visible sprites of this image will be consider like a part of the image
     *
     * @param image1                    First image
     * @param image2                    Second image
     * @param precision                 Difference accepted in border limit
     * @param percentDifferenceAccepted Percent of accepted different pixels (Pixels doesn't match to the precision)
     * @return {@code true} if images "look" the same
     */
    public static boolean doesImagesLookSamePerBorder(
            JHelpImage image1, JHelpImage image2, final int precision,
            final int percentDifferenceAccepted)
    {
        final int width1  = image1.width;
        final int height1 = image1.height;
        final int width2  = image2.width;
        final int height2 = image2.height;

        if ((width1 != width2) || (height1 != height2))
        {
            if ((width1 * height1) >= (width2 * height2))
            {
                final JHelpImage image = new JHelpImage(width1, height1);
                image.startDrawMode();
                image.fillRectangleScaleBetter(0, 0, width1, height1, image2);
                image.endDrawMode();

                image2 = image;
            }
            else
            {
                final JHelpImage image = new JHelpImage(width2, height2);
                image.startDrawMode();
                image.fillRectangleScaleBetter(0, 0, width2, height2, image1);
                image.endDrawMode();

                image1 = image;
            }
        }

        final JHelpImage img1 = JHelpImage.extractBorder(image1, 3, 1);
        final JHelpImage img2 = JHelpImage.extractBorder(image2, 3, 1);

        return JHelpImage.doesImagesLookSamePerPixel(img1, img2, precision, percentDifferenceAccepted);
    }

    /**
     * Compare 2 images and compute if they "look" the same in compare the image luminosity<br>
     * The precision is to determine the accepted distance in luminosity part, and percent to know the percent of
     * accepted pixels
     * doesn't match to precision.<br>
     * Note : if images have'nt same dimension, the smallest is firstly scale to fit to the biggest<br>
     * Note : if one image is not in draw mode, the visible sprites of this image will be consider like a part of the image
     *
     * @param image1                    First image
     * @param image2                    Second image
     * @param precision                 Difference accepted in luminosity
     * @param percentDifferenceAccepted Percent of accepted different pixels (Pixels doesn't match to the precision)
     * @return {@code true} if images "look" the same
     */
    public static boolean doesImagesLookSamePerLuminosity(
            JHelpImage image1, JHelpImage image2, final int precision,
            final int percentDifferenceAccepted)
    {
        final int width1  = image1.width;
        final int height1 = image1.height;
        final int width2  = image2.width;
        final int height2 = image2.height;

        if ((width1 != width2) || (height1 != height2))
        {
            if ((width1 * height1) >= (width2 * height2))
            {
                final JHelpImage image = new JHelpImage(width1, height1);
                image.startDrawMode();
                image.fillRectangleScaleBetter(0, 0, width1, height1, image2);
                image.endDrawMode();

                image2 = image;
            }
            else
            {
                final JHelpImage image = new JHelpImage(width2, height2);
                image.startDrawMode();
                image.fillRectangleScaleBetter(0, 0, width2, height2, image1);
                image.endDrawMode();

                image1 = image;
            }
        }

        final JHelpImage img1 = image1.createCopy();
        img1.startDrawMode();
        img1.gray();
        img1.endDrawMode();

        final JHelpImage img2 = image2.createCopy();
        img2.startDrawMode();
        img2.gray();
        img2.endDrawMode();

        return JHelpImage.doesImagesLookSamePerPixel(img1, img2, precision, percentDifferenceAccepted);
    }

    /**
     * Compare 2 images and compute if they "look" the same in compare the image pixels.<br>
     * The precision is to determine the accepted distance in alpha, red, green and blue part, and percent to know the
     * percent of
     * accepted pixels doesn't match to precision.<br>
     * Note : if images have'nt same dimension, the smallest is firstly scale to fit to the biggest<br>
     * Note : if one image is not in draw mode, the visible sprites of this image will be consider like a part of the image
     *
     * @param image1                    First image
     * @param image2                    Second image
     * @param colorPartPrecision        Difference accepted in pixel parts
     * @param percentDifferenceAccepted Percent of accepted different pixels (Pixels doesn't match to the precision)
     * @return {@code true} if images "look" the same
     */
    public static boolean doesImagesLookSamePerPixel(
            JHelpImage image1, JHelpImage image2, int colorPartPrecision,
            int percentDifferenceAccepted)
    {
        colorPartPrecision = Math.max(0, Math.min(255, colorPartPrecision));
        percentDifferenceAccepted = Math.max(0, Math.min(100, percentDifferenceAccepted));

        int       width1  = image1.width;
        int       height1 = image1.height;
        final int width2  = image2.width;
        final int height2 = image2.height;

        if ((width1 != width2) || (height1 != height2))
        {
            if ((width1 * height1) >= (width2 * height2))
            {
                final JHelpImage image = new JHelpImage(width1, height1);
                image.startDrawMode();
                image.fillRectangleScaleBetter(0, 0, width1, height1, image2);
                image.endDrawMode();

                image2 = image;
            }
            else
            {
                final JHelpImage image = new JHelpImage(width2, height2);
                image.startDrawMode();
                image.fillRectangleScaleBetter(0, 0, width2, height2, image1);
                image.endDrawMode();

                image1 = image;
                width1 = width2;
                height1 = height2;
            }
        }

        final int length     = width1 * height1;
        int       color1;
        int       color2;
        int       difference = 0;

        for (int pix = length - 1; pix >= 0; pix--)
        {
            color1 = image1.pixels[pix];
            color2 = image2.pixels[pix];

            if (Math.abs(((color1 >> 24) & 0xFF) - ((color2 >> 24) & 0xFF)) > colorPartPrecision)
            {
                difference++;
            }

            if (Math.abs(((color1 >> 16) & 0xFF) - ((color2 >> 16) & 0xFF)) > colorPartPrecision)
            {
                difference++;
            }

            if (Math.abs(((color1 >> 8) & 0xFF) - ((color2 >> 8) & 0xFF)) > colorPartPrecision)
            {
                difference++;
            }

            if (Math.abs((color1 & 0xFF) - (color2 & 0xFF)) > colorPartPrecision)
            {
                difference++;
            }
        }

        return ((difference * 100) / (length << 2)) <= percentDifferenceAccepted;
    }

    /**
     * Extract the border of the objects inside the image. Width 1, step 1<br>
     * Note : If the image is not in draw mode, all visible sprite will be consider as a part of the image
     *
     * @param source Image source
     * @return Image border
     */
    public static JHelpImage extractBorder(final JHelpImage source)
    {
        return JHelpImage.extractBorder(source, 1);
    }

    /**
     * Extract the border of the objects inside the image. Step 1<br>
     * Note : If the image is not in draw mode, all visible sprite will be consider as a part of the image
     *
     * @param source Image source
     * @param width  Line width
     * @return Image border
     */
    public static JHelpImage extractBorder(final JHelpImage source, final int width)
    {
        return JHelpImage.extractBorder(source, width, 1);
    }

    /**
     * Extract the border of the objects inside the image.<br>
     * Note : If the image is not in draw mode, all visible sprite will be consider as a part of the image
     *
     * @param source Image source
     * @param width  Line width
     * @param step   Step to jump between width : [1, width]
     * @return Image border
     */
    public static JHelpImage extractBorder(final JHelpImage source, final int width, final int step)
    {
        if (width < 0)
        {
            throw new IllegalArgumentException("width can't be negative");
        }

        if (step < 1)
        {
            throw new IllegalArgumentException("step must be >=1");
        }

        source.update();

        final JHelpImage result = source.createCopy();

        result.startDrawMode();

        result.gray();
        final JHelpImage temporary = result.createCopy();
        result.invertColors();
        final JHelpImage temp = result.createCopy();
        temp.startDrawMode();

        result.shift(1, 1);
        result.addition(temporary);
        final JHelpImage image = temp.createCopy();

        for (int y = -width; y <= width; y += step)
        {
            for (int x = -width; x <= width; x += step)
            {
                temp.copy(image);
                temp.shift(x, y);
                temp.addition(temporary);
                result.minimum(temp);
            }
        }

        temp.endDrawMode();
        result.endDrawMode();

        return result;
    }

    /**
     * Extract the border of the objects inside the image. Width 2, step 1<br>
     * Note : If the image is not in draw mode, all visible sprite will be consider as a part of the image
     *
     * @param source Image source
     * @return Image border
     */
    public static JHelpImage extractBorder2(final JHelpImage source)
    {
        return JHelpImage.extractBorder(source, 2);
    }

    /**
     * Load a buffered image
     *
     * @param image Image file
     * @return Buffered image loaded
     * @throws IOException On reading file issue
     */
    private static BufferedImage loadBufferedImage(final File image) throws IOException
    {
        final String name = image.getName()
                                 .toLowerCase();

        final FileImageInformation fileImageInformation = new FileImageInformation(image);
        String                     suffix               = fileImageInformation.getFormatName();

        if (suffix == null)
        {
            final int index = name.lastIndexOf('.');
            if (index > 0)
            {
                suffix = name.substring(index + 1);
            }
        }

        if (suffix != null)
        {
            ImageInputStream            stream        = null;
            ImageReader                 imageReader   = null;
            BufferedImage               bufferedImage;
            final Iterator<ImageReader> imagesReaders = ImageIO.getImageReadersBySuffix(suffix);

            while (imagesReaders.hasNext())
            {
                try
                {
                    stream = ImageIO.createImageInputStream(image);
                    imageReader = imagesReaders.next();

                    imageReader.setInput(stream);
                    bufferedImage = imageReader.read(0);
                    imageReader.dispose();

                    return bufferedImage;
                }
                catch (final Exception exception)
                {
                    Debug.exception(exception);
                }
                finally
                {
                    if (stream != null)
                    {
                        try
                        {
                            stream.close();
                        }
                        catch (final Exception ignored)
                        {
                        }
                    }
                    stream = null;

                    if (imageReader != null)
                    {
                        imageReader.dispose();
                    }
                    imageReader = null;
                }
            }
        }

        return ImageIO.read(image);
    }

    /**
     * Load an image from file.<br>
     * This method also manage {@link PCX} image files
     *
     * @param image Image file
     * @return Loaded image
     * @throws IOException On file reading issue
     */
    public static JHelpImage loadImage(final File image) throws IOException
    {
        if (PCX.isPCX(image))
        {
            InputStream inputStream = null;

            try
            {
                inputStream = new FileInputStream(image);
                final PCX pcx = new PCX(inputStream);
                return pcx.createImage();
            }
            catch (final Exception exception)
            {
                throw new IOException(image.getAbsolutePath() + " not PCX well formed", exception);
            }
            finally
            {
                if (inputStream != null)
                {
                    try
                    {
                        inputStream.close();
                    }
                    catch (final Exception ignored)
                    {
                    }
                }
            }
        }

        final BufferedImage bufferedImage = JHelpImage.loadBufferedImage(image);
        final int           width         = bufferedImage.getWidth();
        final int           height        = bufferedImage.getHeight();
        int[]               pixels        = new int[width * height];

        pixels = bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);

        final JHelpImage imageLoaded = new JHelpImage(width, height, pixels);

        pixels = null;
        bufferedImage.flush();

        return imageLoaded;
    }

    /**
     * Load an image from a stream
     *
     * @param inputStream Stream to read
     * @return Read image
     * @throws IOException On reading issue
     */
    public static JHelpImage loadImage(final InputStream inputStream) throws IOException
    {
        BufferedImage bufferedImage = ImageIO.read(inputStream);

        if (bufferedImage == null)
        {
            throw new IOException("Failed to load image");
        }

        final int width  = bufferedImage.getWidth();
        final int height = bufferedImage.getHeight();
        int[]     pixels = new int[width * height];

        pixels = bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);

        final JHelpImage image = new JHelpImage(width, height, pixels);

        pixels = null;
        bufferedImage.flush();
        bufferedImage = null;

        return image;
    }

    /**
     * Load an image and resize it to have specific dimension.<br>
     * This method also manage {@link PCX} image files
     *
     * @param image  Image file
     * @param width  Final width
     * @param height Final height
     * @return Loaded image resized to corresponds to specified dimension
     * @throws IOException On reading file issue
     */
    public static JHelpImage loadImageThumb(final File image, final int width, final int height) throws IOException
    {
        if (PCX.isPCX(image))
        {
            InputStream inputStream = null;

            try
            {
                inputStream = new FileInputStream(image);
                final PCX pcx = new PCX(inputStream);
                return JHelpImage.createResizedImage(pcx.createImage(), width, height);
            }
            catch (final Exception exception)
            {
                throw new IOException(image.getAbsolutePath() + " not PCX well formed", exception);
            }
            finally
            {
                if (inputStream != null)
                {
                    try
                    {
                        inputStream.close();
                    }
                    catch (final Exception ignored)
                    {
                    }
                }
            }
        }

        BufferedImage bufferedImage = JHelpImage.loadBufferedImage(image);

        if (bufferedImage == null)
        {
            return null;
        }

        final int imageWidth  = bufferedImage.getWidth();
        final int imageHeight = bufferedImage.getHeight();

        if ((imageWidth != width) || (imageHeight != height))
        {
            final BufferedImage bufferedImageTemp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            final Graphics2D graphics2d = bufferedImageTemp.createGraphics();

            graphics2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                                        RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            graphics2d.drawImage(bufferedImage, 0, 0, width, height, null);

            bufferedImage.flush();
            bufferedImage = bufferedImageTemp;
        }

        int[] pixels = new int[width * height];

        pixels = bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);

        final JHelpImage imageLoaded = new JHelpImage(width, height, pixels);

        pixels = null;
        bufferedImage.flush();
        bufferedImage = null;

        return imageLoaded;
    }

    /**
     * Load an image and resize it to have specific dimension
     *
     * @param inputStream Stream where lies the image
     * @param width       Final width
     * @param height      Final height
     * @return Loaded image resized to corresponds to specified dimension
     * @throws IOException On reading stream issue
     */
    public static JHelpImage loadImageThumb(final InputStream inputStream, final int width, final int height) throws
                                                                                                              IOException
    {
        BufferedImage bufferedImage = ImageIO.read(inputStream);

        if (bufferedImage == null)
        {
            return null;
        }

        final int imageWidth  = bufferedImage.getWidth();
        final int imageHeight = bufferedImage.getHeight();

        if ((imageWidth != width) || (imageHeight != height))
        {
            final BufferedImage bufferedImageTemp = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            final Graphics2D graphics2d = bufferedImageTemp.createGraphics();

            graphics2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                                        RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
            graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            graphics2d.drawImage(bufferedImage, 0, 0, width, height, null);

            bufferedImage.flush();
            bufferedImage = bufferedImageTemp;
        }

        int[] pixels = new int[width * height];

        pixels = bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);

        final JHelpImage image = new JHelpImage(width, height, pixels);

        pixels = null;
        bufferedImage.flush();
        bufferedImage = null;

        return image;
    }

    /**
     * Save an image to a stream in PNG format<br>
     * Note : If the image is not in draw mode, all visible sprite will be consider as a part of the image
     *
     * @param outputStream Stream where write, not closed by this method
     * @param image        Image to save
     * @throws IOException On writing issue
     */
    public static void saveImage(final OutputStream outputStream, final JHelpImage image) throws IOException
    {
        BufferedImage bufferedImage = new BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB);

        bufferedImage.setRGB(0, 0, image.width, image.height, image.pixels, 0, image.width);

        ImageIO.write(bufferedImage, "PNG", outputStream);

        outputStream.flush();
        bufferedImage.flush();

        bufferedImage = null;
    }

    /**
     * Save the image as JPEG
     *
     * @param outputStream Stream where write, not closed by this method
     * @param image        Image to save
     * @throws IOException On writing issue
     */
    public static void saveImageJPG(final OutputStream outputStream, final JHelpImage image) throws IOException
    {
        BufferedImage    bufferedImage = new BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB);
        final Graphics2D graphics2d    = bufferedImage.createGraphics();
        graphics2d.drawImage(image.getImage(), 0, 0, null);

        ImageIO.write(bufferedImage, "JPG", outputStream);

        outputStream.flush();
        bufferedImage.flush();
        graphics2d.dispose();

        bufferedImage = null;
    }

    /**
     * Convert an {@link Icon} to a {@link JHelpImage}.<br>
     * If the {@link Icon} is already a {@link JHelpImage}, it is returned good casted, else a new {@link JHelpImage} is
     * created
     * and the {@link Icon} is draw on it
     *
     * @param icon Icon to convert
     * @return Converted image
     */
    public static JHelpImage toJHelpImage(final Icon icon)
    {
        if (icon == null)
        {
            return null;
        }

        if (icon instanceof JHelpImage)
        {
            return (JHelpImage) icon;
        }

        final int width  = icon.getIconWidth();
        final int height = icon.getIconHeight();

        if ((width <= 0) || (height <= 0))
        {
            return JHelpImage.DUMMY;
        }

        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D    graphics2d    = bufferedImage.createGraphics();
        icon.paintIcon(null, graphics2d, 0, 0);
        bufferedImage.flush();
        graphics2d.dispose();
        return JHelpImage.createImage(bufferedImage);
    }

    /**
     * Actual clip to apply
     */
    private final Clip                 clip;
    /**
     * Clips stack
     */
    private final Stack<Clip>          clips;
    /**
     * List of registered components to alert if image update
     */
    private       ArrayList<Component> componentsListeners;
    /**
     * Actual draw mode
     */
    private       boolean              drawMode;
    /**
     * Indicates that draw mode status can change or not
     */
    private       boolean              drawModeLocked;
    /**
     * Image height
     */
    private final int                  height;
    /**
     * Image for draw in a swing component
     */
    private       Image                image;
    /**
     * Image source
     */
    private       MemoryImageSource    memoryImageSource;
    /**
     * For synchronize
     */
    private final Mutex mutex = new Mutex();
    /**
     * Image name
     */
    private String name;
    /**
     * Image pixels
     */
    private int[]  pixels;
    /**
     * Tasks to play when image enter in draw mode
     */
    private final Queue<ConsumerTask<JHelpImage>> playInDrawMode  = new Queue<>();
    /**
     * Tasks to play when image exit from draw mode
     */
    private final Queue<ConsumerTask<JHelpImage>> playOutDrawMode = new Queue<>();
    /**
     * List of sprite
     */
    private       ArrayList<JHelpSprite> sprites;
    /**
     * Last sprite visibility information collected on {@link #startDrawMode()} to draw sprite in good state when
     * {@link #endDrawMode()} is call
     */
    private       boolean[]              visibilities;
    /**
     * Image width
     */
    private final int                    width;

    /**
     * Create a new instance of JHelpImage full of one color
     *
     * @param width  Width
     * @param height Height
     * @param color  Background color to use
     */
    public JHelpImage(final int width, final int height, final int color)
    {
        this(width, height);

        this.drawMode = true;
        this.clear(color);
        this.drawMode = false;
    }

    /**
     * Create a new instance of JHelpImage empty
     *
     * @param width  Width
     * @param height Height
     */
    public JHelpImage(final int width, final int height)
    {
        this(width, height, new int[width * height]);
    }

    /**
     * Create a new instance of JHelpImage
     *
     * @param width  Width
     * @param height Height
     * @param pixels Image pixels
     */
    public JHelpImage(final int width, final int height, final int[] pixels)
    {
        if ((width < 1) || (height < 1))
        {
            throw new IllegalArgumentException(
                    "width and height must be > 1, but it is specify : " + width + "x" + height);
        }

        if ((width * height) != pixels.length)
        {
            throw new IllegalArgumentException(
                    "The pixels array size must be width*height, but it is specify width=" + width + " height=" + height
                    + " pixels.length=" + pixels.length);
        }

        this.drawModeLocked = false;
        this.width = width;
        this.height = height;

        this.pixels = pixels;

        this.memoryImageSource = new MemoryImageSource(width, height, pixels, 0, width);
        this.memoryImageSource.setAnimated(true);
        this.memoryImageSource.setFullBufferUpdates(true);

        this.image = Toolkit.getDefaultToolkit()
                            .createImage(this.memoryImageSource);

        this.sprites = new ArrayList<>();
        this.componentsListeners = new ArrayList<>();

        this.clip = new Clip(0, this.width - 1, 0, this.height - 1);
        this.clips = new Stack<>();
        this.clips.push(this.clip);
        this.drawMode = false;
    }

    /**
     * Create a new instance of JHelpImage fill with a pixels array scales to fill all the image
     *
     * @param width       Width of image inside pixels array
     * @param height      Height of image inside pixels array
     * @param pixels      Pixels array
     * @param imageWidth  Image created width
     * @param imageHeight Image created height
     */
    public JHelpImage(
            final int width, final int height, final int[] pixels, final int imageWidth, final int imageHeight)
    {
        this(imageWidth, imageHeight);

        this.fillRectangleScale(0, 0, imageWidth, imageHeight, pixels, width, height);
    }

    /**
     * Create Path for a thick line
     *
     * @param x1        Line start X
     * @param y1        Line start Y
     * @param x2        Line end X
     * @param y2        Line end Y
     * @param thickness Line thickness
     * @return Created path
     */
    private Path2D createThickLine(final int x1, final int y1, final int x2, final int y2, final int thickness)
    {
        final Path2D path   = new Path2D.Double();
        double       vx     = x2 - x1;
        double       vy     = y2 - y1;
        final double length = Math.sqrt((vx * vx) + (vy * vy));

        if (Math2.isNul(length))
        {
            return path;
        }

        final double theta = Math.atan2(vy, vx);
        final double thick = thickness * 0.5;
        vx = (vx * thick) / length;
        vy = (vy * thick) / length;
        double       angle = theta + Math2.PI_2;
        final double x     = x1 + (thick * Math.cos(angle));
        final double y     = y1 + (thick * Math.sin(angle));
        path.moveTo(x, y);
        path.lineTo(x2 + (thick * Math.cos(angle)), y2 + (thick * Math.sin(angle)));
        angle = theta - Math2.PI_2;
        path.quadTo(x2 + vx, y2 + vy, x2 + (thick * Math.cos(angle)), y2 + (thick * Math.sin(angle)));
        path.lineTo(x1 + (thick * Math.cos(angle)), y1 + (thick * Math.sin(angle)));
        path.quadTo(x1 - vx, y1 - vy, x, y);
        return path;
    }

    /**
     * Draw a shape on center it<br>
     * MUST be in draw mode
     *
     * @param shape      Shape to draw
     * @param color      Color to use
     * @param doAlphaMix Indicates if alpha mix is on
     */
    private void drawShapeCenter(final Shape shape, final int color, final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        final PathIterator pathIterator = shape.getPathIterator(ConstantsGUI.AFFINE_TRANSFORM, ConstantsGUI.FLATNESS);

        final double[] info   = new double[6];
        int            x      = 0;
        int            y      = 0;
        int            xStart = 0;
        int            yStart = 0;
        int            xx, yy;

        final Rectangle bounds = shape.getBounds();
        final int       vx     = bounds.width >> 1;
        final int       vy     = bounds.height >> 1;

        while (!pathIterator.isDone())
        {
            switch (pathIterator.currentSegment(info))
            {
                case PathIterator.SEG_MOVETO:
                    xStart = x = (int) Math.round(info[0]);
                    yStart = y = (int) Math.round(info[1]);

                    break;
                case PathIterator.SEG_LINETO:
                    xx = (int) Math.round(info[0]);
                    yy = (int) Math.round(info[1]);

                    this.drawLine(x - vx, y - vy, xx - vx, yy - vy, color, doAlphaMix);

                    x = xx;
                    y = yy;

                    break;
                case PathIterator.SEG_CLOSE:
                    this.drawLine(x - vx, y - vy, xStart - vx, yStart - vy, color, doAlphaMix);

                    x = xStart;
                    y = yStart;

                    break;
            }

            pathIterator.next();
        }
    }

    /**
     * Fill a rectangle with an array of pixels
     *
     * @param x            X of up-left corner
     * @param y            Y of up-left corner
     * @param width        Rectangle width
     * @param height       Rectangle height
     * @param pixels       Pixels array
     * @param pixelsWidth  Image width inside pixels array
     * @param pixelsHeight Image height inside pixels array
     */
    private void fillRectangleScale(
            final int x, final int y, final int width, final int height,
            final int[] pixels, final int pixelsWidth, final int pixelsHeight)
    {
        if ((width <= 0) || (height <= 0))
        {
            return;
        }

        final int x2 = (x + width) - 1;
        final int y2 = (y + height) - 1;

        final int startX = Math.max(this.clip.xMin, x);
        final int endX   = Math.min(this.clip.xMax, x2);
        final int startY = Math.max(this.clip.yMin, y);
        final int endY   = Math.min(this.clip.yMax, y2);

        if ((startX > endX) || (startY > endY))
        {
            return;
        }

        int       line     = startX + (startY * this.width);
        int       pix;
        int       yTexture = 0;
        int       pixTexture;
        final int w        = (endX - startX) + 1;
        final int h        = (endY - startY) + 1;

        for (int yy = startY, yt = 0; yy <= endY; yy++, yt++, yTexture = (yt * pixelsHeight) / h)
        {
            pixTexture = yTexture * pixelsWidth;
            pix = line;

            for (int xx = startX, xt = 0, xTexture = 0; xx < endX; xx++, xt++, pix++, xTexture = (xt * pixelsWidth) / w)
            {
                this.pixels[pix] = pixels[pixTexture + xTexture];
            }

            line += this.width;
        }
    }

    /**
     * Change a sprite visibility
     *
     * @param index   Sprite index
     * @param visible New visibility state
     */
    void changeSpriteVisibility(final int index, final boolean visible)
    {
        if (this.drawMode)
        {
            this.visibilities[index] = visible;

            return;
        }

        final int   length = this.sprites.size();
        JHelpSprite sprite;

        final boolean[] visibilities = new boolean[length];
        for (int i = 0; i < length; i++)
        {
            visibilities[i] = false;
        }

        boolean isVisible;

        for (int i = length - 1; i > index; i--)
        {
            sprite = this.sprites.get(i);
            isVisible = visibilities[i] = sprite.isVisible();

            if (isVisible)
            {
                sprite.changeVisible(false);
            }
        }

        this.sprites.get(index)
                    .changeVisible(visible);

        for (int i = index + 1; i < length; i++)
        {
            if (visibilities[i])
            {
                this.sprites.get(i)
                            .changeVisible(true);
            }
        }
    }

    /**
     * Draw a part of an image on this image<br>
     * MUST be in draw mode
     *
     * @param x          X on this image
     * @param y          Y on this image
     * @param image      Image to draw
     * @param xImage     X on given image
     * @param yImage     Y on given image
     * @param width      Part width
     * @param height     Part height
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    void drawImageInternal(
            int x, int y, final JHelpImage image, int xImage, int yImage, int width, int height, final
    boolean doAlphaMix)
    {
        if (!doAlphaMix)
        {
            this.drawImageOver(x, y, image, xImage, yImage, width, height);
            return;
        }

        if (xImage < 0)
        {
            x -= xImage;
            width += xImage;
            xImage = 0;
        }

        if (x < this.clip.xMin)
        {
            xImage -= x - this.clip.xMin;
            width += x - this.clip.xMin;
            x = this.clip.xMin;
        }

        if (yImage < 0)
        {
            y -= yImage;
            height += yImage;
            yImage = 0;
        }

        if (y < this.clip.yMin)
        {
            yImage -= y - this.clip.yMin;
            height += y - this.clip.yMin;
            y = this.clip.yMin;
        }

        final int w = Math2.minimum((this.clip.xMax + 1) - x, image.width - xImage, width, this.width - x);
        final int h = Math2.minimum((this.clip.yMax + 1) - y, image.height - yImage, height, this.height - y);

        if ((w <= 0) || (h <= 0))
        {
            return;
        }

        int lineThis = x + (y * this.width);
        int pixThis;

        int lineImage = xImage + (yImage * image.width);
        int pixImage;

        int colorThis;
        int colorImage;
        int alpha;
        int ahpla;

        for (int yy = 0; yy < h; yy++)
        {
            pixThis = lineThis;
            pixImage = lineImage;

            for (int xx = 0; xx < w; xx++)
            {
                colorImage = image.pixels[pixImage];

                alpha = (colorImage >> 24) & 0xFF;

                if (alpha == 255)
                {
                    this.pixels[pixThis] = colorImage;
                }
                else if (alpha > 0)
                {
                    ahpla = 256 - alpha;

                    colorThis = this.pixels[pixThis];

                    this.pixels[pixThis] = (Math.min(255, alpha + ((colorThis >> 24) & 0xFF)) << 24) | //
                                           ((((((colorImage >> 16) & 0xFF) * alpha)
                                              + (((colorThis >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                                           ((((((colorImage >> 8) & 0xFF) * alpha) +
                                              (((colorThis >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                                           ((((colorImage & 0xFF) * alpha) + ((colorThis & 0xFF) * ahpla)) >> 8);
                }

                pixThis++;
                pixImage++;
            }

            lineThis += this.width;
            lineImage += image.width;
        }
    }

    /**
     * Draw a part of image on using a specific alpha value<br>
     * MUST be in draw mode
     *
     * @param x      X to draw image
     * @param y      Y to draw image
     * @param image  Image to draw
     * @param xImage Start X of image part
     * @param yImage Start Y of image part
     * @param width  Width of image part
     * @param height Height of image part
     * @param alpha  Alpha to use
     */
    void drawImageInternal(
            int x, int y, final JHelpImage image,
            int xImage, int yImage, int width, int height, final int alpha)
    {
        if (alpha == 255)
        {
            this.drawImageOver(x, y, image, xImage, yImage, width, height);
            return;
        }

        if (alpha == 0)
        {
            return;
        }

        if (xImage < 0)
        {
            x -= xImage;
            width += xImage;
            xImage = 0;
        }

        if (x < this.clip.xMin)
        {
            xImage -= x - this.clip.xMin;
            width += x - this.clip.xMin;
            x = this.clip.xMin;
        }

        if (yImage < 0)
        {
            y -= yImage;
            height += yImage;
            yImage = 0;
        }

        if (y < this.clip.yMin)
        {
            yImage -= y - this.clip.yMin;
            height += y - this.clip.yMin;
            y = this.clip.yMin;
        }

        final int w = Math2.minimum((this.clip.xMax + 1) - x, image.width - xImage, width);
        final int h = Math2.minimum((this.clip.yMax + 1) - y, image.height - yImage, height);

        if ((w <= 0) || (h <= 0))
        {
            return;
        }

        int lineThis = x + (y * this.width);
        int pixThis;

        int lineImage = xImage + (yImage * image.width);
        int pixImage;

        int colorThis;
        int colorImage;

        final int ahpla = 256 - alpha;

        for (int yy = 0; yy < h; yy++)
        {
            pixThis = lineThis;
            pixImage = lineImage;

            for (int xx = 0; xx < w; xx++)
            {
                colorImage = image.pixels[pixImage];

                colorThis = this.pixels[pixThis];

                this.pixels[pixThis] = (Math.min(255, alpha + ((colorThis >> 24) & 0xFF)) << 24) | //
                                       ((((((colorImage >> 16) & 0xFF) * alpha) +
                                          (((colorThis >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                                       ((((((colorImage >> 8) & 0xFF) * alpha) + (((colorThis >> 8) & 0xFF) * ahpla)) >>
                                         8) << 8) | //
                                       ((((colorImage & 0xFF) * alpha) + ((colorThis & 0xFF) * ahpla)) >> 8);

                pixThis++;
                pixImage++;
            }

            lineThis += this.width;
            lineImage += image.width;
        }
    }

    /**
     * Draw apart of image over this image (just override)<br>
     * MUST be in draw mode
     *
     * @param x      X on this image
     * @param y      Y on this image
     * @param image  Image to draw
     * @param xImage X on image
     * @param yImage Y on image
     * @param width  Part width
     * @param height Part height
     */
    void drawImageOver(int x, int y, final JHelpImage image, int xImage, int yImage, int width, int height)
    {
        if (xImage < 0)
        {
            x -= xImage;
            width += xImage;
            xImage = 0;
        }

        if (x < this.clip.xMin)
        {
            xImage -= x - this.clip.xMin;
            width += x - this.clip.xMin;
            x = this.clip.xMin;
        }

        if (yImage < 0)
        {
            y -= yImage;
            height += yImage;
            yImage = 0;
        }

        if (y < this.clip.yMin)
        {
            yImage -= y - this.clip.yMin;
            height += y - this.clip.yMin;
            y = this.clip.yMin;
        }

        final int w = Math2.minimum((this.clip.xMax + 1) - x, image.width - xImage, width);
        final int h = Math2.minimum((this.clip.yMax + 1) - y, image.height - yImage, height);

        if ((w <= 0) || (h <= 0))
        {
            return;
        }

        int lineThis  = x + (y * this.width);
        int lineImage = xImage + (yImage * image.width);

        for (int yy = 0; yy < h; yy++)
        {
            System.arraycopy(image.pixels, lineImage, this.pixels, lineThis, w);

            lineThis += this.width;
            lineImage += image.width;
        }
    }

    /**
     * Refresh the image
     */
    void refresh()
    {
        this.memoryImageSource.newPixels();
    }

    /**
     * Call by garbage collector to free some memory <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @throws Throwable On issue
     * @see Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable
    {
        this.pixels = null;
        this.memoryImageSource = null;

        if (this.image != null)
        {
            this.image.flush();
        }
        this.image = null;

        if (this.sprites != null)
        {
            this.sprites.clear();
        }
        this.sprites = null;

        this.visibilities = null;

        this.mutex.playInCriticalSectionVoid(() ->
                                             {
                                                 if (this.componentsListeners != null)
                                                 {
                                                     this.componentsListeners.clear();
                                                 }
                                                 this.componentsListeners = null;
                                             });

        super.finalize();
    }

    /**
     * Add an other image<br>
     * This image and the given one MUST have same dimension<br>
     * Note : if this image or given one not in draw mode, all visible sprites (of the image) are consider like a part
     * of the
     * image
     *
     * @param image Image to add
     */
    public void addition(final JHelpImage image)
    {
        if ((this.width != image.width) || (this.height != image.height))
        {
            throw new IllegalArgumentException("We can only add with an image of same size");
        }

        int colorThis, colorImage;

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            colorThis = this.pixels[pix];
            colorImage = image.pixels[pix];

            this.pixels[pix] = (colorThis & 0xFF000000) | //
                               (Math2.limit0_255(((colorThis >> 16) & 0xFF) + ((colorImage >> 16) & 0xFF)) << 16) | //
                               (Math2.limit0_255(((colorThis >> 8) & 0xFF) + ((colorImage >> 8) & 0xFF)) << 8) | //
                               Math2.limit0_255((colorThis & 0xFF) + (colorImage & 0xFF));
        }
    }

    /**
     * Apply Gauss filter 3x3 in the image.<br>
     * MUST be in draw mode<br>
     * Note filter is
     * <table border=1>
     * <caption>Gauss</caption>
     * <tr>
     * <td> 1 </td>
     * <td> 2 </td>
     * <td> 1 </td>
     * </tr>
     * <tr>
     * <td> 2 </td>
     * <td> 4 </td>
     * <td> 2 </td>
     * </tr>
     * <tr>
     * <td> 1 </td>
     * <td> 2 </td>
     * <td> 1 </td>
     * </tr>
     * </table>
     */
    public void applyGauss3x3()
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        final int   w   = this.width + 2;
        final int   h   = this.height + 2;
        final int[] pix = new int[w * h];

        int lineThis = 0;
        int linePix  = 1 + w;
        for (int y = 0; y < this.height; y++)
        {
            pix[linePix - 1] = this.pixels[lineThis];
            System.arraycopy(this.pixels, lineThis, pix, linePix, this.width);
            lineThis += this.width;
            linePix += w;
            pix[linePix - 2] = this.pixels[lineThis - 1];
        }

        System.arraycopy(this.pixels, 0, pix, 1, this.width);
        System.arraycopy(this.pixels, (this.width * this.height) - this.width, pix, ((w * h) - w) + 1, this.width);

        int l0 = 0;
        int l1 = w;
        int l2 = w + w;
        int p20;
        int p21;
        int p22;
        int c00, c10, c20, c01, c11, c21, c02, c12, c22;
        int p  = 0;

        for (int y = 0; y < this.height; y++)
        {
            p20 = l0 + 2;
            p21 = l1 + 2;
            p22 = l2 + 2;

            c00 = pix[p20 - 2];
            c10 = pix[p20 - 1];

            c01 = pix[p21 - 2];
            c11 = pix[p21 - 1];

            c02 = pix[p22 - 2];
            c12 = pix[p22 - 1];

            for (int x = 0; x < this.width; x++)
            {
                c20 = pix[p20];
                c21 = pix[p21];
                c22 = pix[p22];

                this.pixels[p] =
                        // Alpha
                        (((((c00 >> 24) & 0xFF) + (((c10 >> 24) & 0xFF) << 1) + ((c20 >> 24) & 0xFF)
                           + (((c01 >> 24) & 0xFF) << 1) + (((c11 >> 24) & 0xFF) << 2)
                           + (((c21 >> 24) & 0xFF) << 1) + ((c02 >> 24) & 0xFF)
                           + (((c12 >> 24) & 0xFF) << 1) + ((c22 >> 24) & 0xFF)) >> 4) << 24)
                        |
                        // Red
                        (((((c00 >> 16) & 0xFF) + (((c10 >> 16) & 0xFF) << 1) + ((c20 >> 16) & 0xFF)
                           + (((c01 >> 16) & 0xFF) << 1) + (((c11 >> 16) & 0xFF) << 2)
                           + (((c21 >> 16) & 0xFF) << 1) + ((c02 >> 16) & 0xFF) +
                           (((c12 >> 16) & 0xFF) << 1) + ((c22 >> 16) & 0xFF)) >> 4) << 16)
                        |
                        // Green
                        (((((c00 >> 8) & 0xFF) + (((c10 >> 8) & 0xFF) << 1) + ((c20 >> 8) & 0xFF)
                           + (((c01 >> 8) & 0xFF) << 1) + (((c11 >> 8) & 0xFF) << 2)
                           + (((c21 >> 8) & 0xFF) << 1) + ((c02 >> 8) & 0xFF)
                           + (((c12 >> 8) & 0xFF) << 1) + ((c22 >> 8) & 0xFF)) >> 4) << 8)
                        |
                        // Blue
                        (((c00 & 0xFF) + ((c10 & 0xFF) << 1) + (c20 & 0xFF) + ((c01 & 0xFF) << 1)
                          + ((c11 & 0xFF) << 2) + ((c21 & 0xFF) << 1) + (c02 & 0xFF)
                          + ((c12 & 0xFF) << 1) + (c22 & 0xFF)) >> 4);

                c00 = c10;
                c10 = c20;

                c01 = c11;
                c11 = c21;

                c02 = c12;
                c12 = c22;

                p20++;
                p21++;
                p22++;

                p++;
            }

            l0 += w;
            l1 += w;
            l2 += w;
        }
    }

    /**
     * Fill with the palette different area<br>
     * MUST be in draw mode
     *
     * @param precision Precision to use for distinguish 2 area
     */
    public void applyPalette(final int precision)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        final SortedArray<Color> colors = new SortedArray<>(Color.class);
        final int                size   = this.pixels.length - 1;
        Color.precision = precision;
        int   index, col;
        Color color;

        for (int i = size; i >= 0; i--)
        {
            color = new Color(this.pixels[i]);
            index = colors.indexOf(color);

            if (index < 0)
            {
                col = color.info = colors.size();
                colors.add(color);
            }
            else
            {
                col = colors.get(index).info;
            }

            this.pixels[i] = JHelpImage.PALETTE[col % JHelpImage.PALETTE_SIZE];
        }
    }

    /**
     * Put the image brighter<br>
     * MUST be in draw mode
     *
     * @param factor Factor of bright
     */
    public void brighter(final int factor)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int color;

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            color = this.pixels[pix];

            this.pixels[pix] = (color & 0xFF000000) | //
                               (Math2.limit0_255(((color >> 16) & 0xFF) + factor) << 16) | //
                               (Math2.limit0_255(((color >> 8) & 0xFF) + factor) << 8) | //
                               Math2.limit0_255((color & 0xFF) + factor);
        }
    }

    /**
     * Change image brightness<br>
     * MUST be in draw mode
     *
     * @param factor Brightness factor
     */
    public void brightness(final double factor)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int    color;
        int    red, green, blue;
        double y, u, v;

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            color = this.pixels[pix];

            red = (color >> 16) & 0xFF;
            green = (color >> 8) & 0xFF;
            blue = color & 0xFF;

            y = JHelpImage.computeY(red, green, blue) * factor;
            u = JHelpImage.computeU(red, green, blue);
            v = JHelpImage.computeV(red, green, blue);

            this.pixels[pix] = (color & 0xFF000000) | //
                               (JHelpImage.computeRed(y, u, v) << 16) | //
                               (JHelpImage.computeGreen(y, u, v) << 8) | //
                               JHelpImage.computeBlue(y, u, v);
        }
    }

    /**
     * Colorize all near color with same color<br>
     * MUST be in draw mode
     *
     * @param precision Precision to use
     */
    public void categorizeByColor(final int precision)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        final SortedArray<Color> colors = new SortedArray<>(Color.class);
        final int                size   = this.pixels.length - 1;
        Color.precision = precision;
        Color color;
        int   index;

        for (int i = size; i >= 0; i--)
        {
            color = new Color(this.pixels[i]);

            index = colors.indexOf(color);

            if (index < 0)
            {
                colors.add(color);
                this.pixels[i] = color.color;
            }
            else
            {
                this.pixels[i] = colors.get(index).color;
            }
        }
    }

    /**
     * Colorize with 3 colors, one used for "dark" colors, one for "gray" colors and last for "white" colors<br>
     * MUST be in draw mode
     *
     * @param colorLow    Color for dark
     * @param colorMiddle Color for gray
     * @param colorHigh   Color for white
     * @param precision   Precision for decide witch are gray
     */
    public void categorizeByY(final int colorLow, final int colorMiddle, final int colorHigh, final double precision)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int    color, red, green, blue, index;
        double yAverage, y;

        index = this.pixels.length - 1;
        color = this.pixels[index];
        red = (color >> 16) & 0xFF;
        green = (color >> 8) & 0xFF;
        blue = color & 0xFF;

        yAverage = JHelpImage.computeY(red, green, blue);

        index--;
        while (index >= 0)
        {
            color = this.pixels[index];
            red = (color >> 16) & 0xFF;
            green = (color >> 8) & 0xFF;
            blue = color & 0xFF;

            yAverage += JHelpImage.computeY(red, green, blue);

            index--;
        }

        final double ymil = yAverage / this.pixels.length;

        for (int i = this.pixels.length - 1; i >= 0; i--)
        {
            color = this.pixels[i];
            red = (color >> 16) & 0xFF;
            green = (color >> 8) & 0xFF;
            blue = color & 0xFF;

            y = JHelpImage.computeY(red, green, blue);

            if (Math.abs(y - ymil) <= precision)
            {
                this.pixels[i] = colorMiddle;
            }
            else if (y < ymil)
            {
                this.pixels[i] = colorLow;
            }
            else
            {
                this.pixels[i] = colorHigh;
            }
        }
    }

    /**
     * Fill the entire image with same color<br>
     * MUST be in draw mode
     *
     * @param color Color to use
     */
    public void clear(final int color)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            this.pixels[pix] = color;
        }
    }

    /**
     * Clear the image to be totally transparent <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see jhelp.util.image.raster.RasterImage#clear()
     */
    @Override
    public void clear()
    {
        final boolean mode = this.drawMode;

        if (!mode)
        {
            this.startDrawMode();
        }

        this.clear(0);

        if (!mode)
        {
            this.endDrawMode();
        }
    }

    /**
     * Create a couple of sprite and associated animated image<br>
     * MUST NOT be in draw mode
     *
     * @param x             X position
     * @param y             Y position
     * @param width         Sprite width
     * @param height        Sprite height
     * @param animationMode Animation mode to use
     * @return Created couple
     */
    public Pair<JHelpSprite, JHelpAnimatedImage> createAnimatedSprite(
            final int x, final int y,
            final int width, final int height,
            final AnimationMode animationMode)
    {
        final JHelpSprite        sprite        = this.createSprite(x, y, width, height);
        final JHelpAnimatedImage animatedImage = new JHelpAnimatedImage(sprite.getImage(), animationMode);
        return new Pair<>(sprite, animatedImage);
    }

    /**
     * Create a sprite<br>
     * MUST NOT be in draw mode
     *
     * @param x      Start X of sprite
     * @param y      Start Y of sprite
     * @param width  Sprite width
     * @param height Sprite height
     * @return Created sprite
     */
    public JHelpSprite createSprite(final int x, final int y, final int width, final int height)
    {
        if (this.drawMode)
        {
            throw new IllegalStateException("MUST NOT be in draw mode !");
        }

        final int         index  = this.sprites.size();
        final JHelpSprite sprite = new JHelpSprite(x, y, width, height, this, index);
        this.sprites.add(sprite);
        return sprite;
    }

    /**
     * Draw a line<br>
     * MUST be in draw mode
     *
     * @param x1         X of first point
     * @param y1         Y first point
     * @param x2         X second point
     * @param y2         Y second point
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    @SuppressWarnings("ConstantConditions")
    public void drawLine(
            final int x1, final int y1, final int x2, final int y2, final int color, final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if (y1 == y2)
        {
            this.drawHorizontalLine(x1, x2, y1, color, doAlphaMix);

            return;
        }

        if (x1 == x2)
        {
            this.drawVerticalLine(x1, y1, y2, color, doAlphaMix);

            return;
        }

        final int alpha = (color >> 24) & 0xFF;

        if ((alpha == 0) && (doAlphaMix))
        {
            return;
        }

        int       error = 0;
        final int dx    = Math.abs(x2 - x1);
        final int sx    = Math2.sign(x2 - x1);
        final int dy    = Math.abs(y2 - y1);
        final int sy    = Math2.sign(y2 - y1);
        int       x     = x1;
        int       y     = y1;

        if (dx >= dy)
        {
            while (((x < this.clip.xMin) || (x > this.clip.xMax) || (y < this.clip.yMin) || (y > this.clip.yMax)) && ((x
                                                                                                                       !=
                                                                                                                       x2) ||
                                                                                                                      (y !=
                                                                                                                       y2)))
            {
                x += sx;

                error += dy;
                if (error >= dx)
                {
                    y += sy;

                    error -= dx;
                }
            }
        }
        else
        {
            while (((x < this.clip.xMin) || (x > this.clip.xMax) || (y < this.clip.yMin) || (y > this.clip.yMax)) && ((x
                                                                                                                       !=
                                                                                                                       x2) ||
                                                                                                                      (y !=
                                                                                                                       y2)))
            {
                y += sy;

                error += dx;
                if (error >= dy)
                {
                    x += sx;

                    error -= dy;
                }
            }
        }

        if (((x < this.clip.xMin) || (x > this.clip.xMax) || (y < this.clip.yMin) || (y > this.clip.yMax)) && (x == x2)
            && (y == y2))
        {
            return;
        }

        int       pix   = x + (y * this.width);
        final int moreY = sy * this.width;

        if ((alpha == 255) || (!doAlphaMix))
        {
            if (dx >= dy)
            {
                while ((x >= this.clip.xMin) && (x <= this.clip.xMax) && (x != x2) && (y >= this.clip.yMin) && (y <=
                                                                                                                this.clip.yMax) &&
                       (y != y2))
                {
                    this.pixels[pix] = color;

                    pix += sx;
                    x += sx;

                    error += dy;
                    if (error >= dx)
                    {
                        pix += moreY;
                        y += sy;

                        error -= dx;
                    }
                }
            }
            else
            {
                while ((x >= this.clip.xMin) && (x <= this.clip.xMax) && (x != x2) && (y >= this.clip.yMin) && (y <=
                                                                                                                this.clip.yMax) &&
                       (y != y2))
                {
                    this.pixels[pix] = color;

                    pix += moreY;
                    y += sy;

                    error += dx;
                    if (error >= dy)
                    {
                        pix += sx;
                        x += sx;

                        error -= dy;
                    }
                }
            }

            return;
        }

        final int ahpla = 256 - alpha;
        final int red   = ((color >> 16) & 0xFF) * alpha;
        final int green = ((color >> 8) & 0xFF) * alpha;
        final int blue  = (color & 0xFF) * alpha;
        int       col;

        if (dx >= dy)
        {
            while ((x >= this.clip.xMin) && (x <= this.clip.xMax) && (x != x2) && (y >= this.clip.yMin) && (y <= this
                    .clip.yMax) && ((x != x2) || (y != y2)))
            {
                col = this.pixels[pix];

                this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                                   (((red + (((col >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                                   (((green + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                                   ((blue + ((col & 0xFF) * ahpla)) >> 8);

                pix += sx;
                x += sx;

                error += dy;
                if (error >= dx)
                {
                    pix += moreY;
                    y += sy;

                    error -= dx;
                }
            }
        }
        else
        {
            while ((x >= this.clip.xMin) && (x <= this.clip.xMax) && (x != x2) && (y >= this.clip.yMin) && (y <= this
                    .clip.yMax) && ((x != x2) || (y != y2)))
            {
                col = this.pixels[pix];

                this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                                   (((red + (((col >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                                   (((green + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                                   ((blue + ((col & 0xFF) * ahpla)) >> 8);

                pix += moreY;
                y += sy;

                error += dx;
                if (error >= dy)
                {
                    pix += sx;
                    x += sx;

                    error -= dy;
                }
            }
        }
    }

    /**
     * Draw a line<br>
     * MUST be in draw mode
     *
     * @param x1    X of first point
     * @param y1    Y first point
     * @param x2    X second point
     * @param y2    Y second point
     * @param color Color to use
     */
    public void drawLine(final int x1, final int y1, final int x2, final int y2, final int color)
    {
        this.drawLine(x1, y1, x2, y2, color, true);
    }

    /**
     * Colorize with automatic palette<br>
     * MUST be in draw mode
     *
     * @param precision Precision to use
     * @return Number of different color
     */
    public int colorizeWithPalette(final int precision)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        final int          size         = this.pixels.length;
        final int[]        result       = new int[size];
        int                indexPalette = 0xFF000000;// 0;
        int                color, reference, red, green, blue;
        int                p;
        final Stack<Point> stack        = new Stack<>();
        Point              point;
        int                x            = this.width - 1;
        int                y            = this.height - 1;

        for (int pix = size - 1; pix >= 0; pix--)
        {
            if (result[pix] == 0)
            {
                color = indexPalette;
                indexPalette++;

                reference = this.pixels[pix];
                red = (reference >> 16) & 0xFF;
                green = (reference >> 8) & 0xFF;
                blue = reference & 0xFF;

                stack.push(new Point(x, y));

                while (!stack.isEmpty())
                {
                    point = stack.pop();
                    p = point.x + (point.y * this.width);

                    result[p] = color;

                    if ((point.x > 0) && (result[p - 1] == 0)
                        && (Color.isNear(red, green, blue, this.pixels[p - 1], precision)))
                    {
                        stack.push(new Point(point.x - 1, point.y));
                    }

                    if ((point.y > 0) && (result[p - this.width] == 0)
                        && (Color.isNear(red, green, blue, this.pixels[p - this.width], precision)))
                    {
                        stack.push(new Point(point.x, point.y - 1));
                    }

                    if ((point.x < (this.width - 1)) && (result[p + 1] == 0)
                        && (Color.isNear(red, green, blue, this.pixels[p + 1], precision)))
                    {
                        stack.push(new Point(point.x + 1, point.y));
                    }

                    if ((point.y < (this.height - 1)) && (result[p + this.width] == 0)
                        && (Color.isNear(red, green, blue, this.pixels[p + this.width], precision)))
                    {
                        stack.push(new Point(point.x, point.y + 1));
                    }
                }
            }

            x--;
            if (x < 0)
            {
                x = this.width - 1;
                y--;
            }
        }

        System.arraycopy(result, 0, this.pixels, 0, size);

        return indexPalette & 0x00FFFFFF;
    }

    /**
     * Change image contrast by using the middle of the minimum and maximum<br>
     * MUST be in draw mode
     *
     * @param factor Factor to apply to the contrast
     */
    public void contrast(final double factor)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int    color, red, green, blue, index;
        double yMin, yMax, y;

        index = this.pixels.length - 1;
        color = this.pixels[index];
        red = (color >> 16) & 0xFF;
        green = (color >> 8) & 0xFF;
        blue = color & 0xFF;

        yMin = yMax = JHelpImage.computeY(red, green, blue);

        index--;
        while (index >= 0)
        {
            color = this.pixels[index];
            red = (color >> 16) & 0xFF;
            green = (color >> 8) & 0xFF;
            blue = color & 0xFF;

            y = JHelpImage.computeY(red, green, blue);

            yMin = Math.min(yMin, y);
            yMax = Math.max(yMax, y);

            index--;
        }

        final double yMil = (yMin + yMax) / 2;
        double       u, v;

        for (int i = this.pixels.length - 1; i >= 0; i--)
        {
            color = this.pixels[i];
            red = (color >> 16) & 0xFF;
            green = (color >> 8) & 0xFF;
            blue = color & 0xFF;

            y = JHelpImage.computeY(red, green, blue);
            u = JHelpImage.computeU(red, green, blue);
            v = JHelpImage.computeV(red, green, blue);

            y = (yMil + (factor * (y - yMil)));

            this.pixels[i] = (color & 0xFF000000)
                             | (JHelpImage.computeRed(y, u, v) << 16)
                             | (JHelpImage.computeGreen(y, u, v) << 8)
                             | JHelpImage.computeBlue(y, u, v);
        }
    }

    /**
     * Change image contrast by using the average of all values<br>
     * MUST be in draw mode
     *
     * @param factor Factor to apply to the contrast
     */
    public void contrastAverage(final double factor)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int    color, red, green, blue, index;
        double yAverage, y;

        index = this.pixels.length - 1;
        color = this.pixels[index];
        red = (color >> 16) & 0xFF;
        green = (color >> 8) & 0xFF;
        blue = color & 0xFF;

        yAverage = JHelpImage.computeY(red, green, blue);

        index--;
        while (index >= 0)
        {
            color = this.pixels[index];
            red = (color >> 16) & 0xFF;
            green = (color >> 8) & 0xFF;
            blue = color & 0xFF;

            yAverage += JHelpImage.computeY(red, green, blue);

            index--;
        }

        final double ymil = yAverage / this.pixels.length;
        double       u, v;

        for (int i = this.pixels.length - 1; i >= 0; i--)
        {
            color = this.pixels[i];
            red = (color >> 16) & 0xFF;
            green = (color >> 8) & 0xFF;
            blue = color & 0xFF;

            y = JHelpImage.computeY(red, green, blue);
            u = JHelpImage.computeU(red, green, blue);
            v = JHelpImage.computeV(red, green, blue);

            y = (ymil + (factor * (y - ymil)));

            this.pixels[i] = (color & 0xFF000000)
                             | (JHelpImage.computeRed(y, u, v) << 16)
                             | (JHelpImage.computeGreen(y, u, v) << 8)
                             | JHelpImage.computeBlue(y, u, v);
        }
    }

    /**
     * Copy the image is this one<br>
     * This image and the given one MUST have same dimension<br>
     * Note : if this image or given one not in draw mode, all visible sprites (of the image) are consider like a part
     * of the
     * image
     *
     * @param image Image to copy
     */
    public void copy(final JHelpImage image)
    {
        if ((this.width != image.width) || (this.height != image.height))
        {
            throw new IllegalArgumentException("We can only multiply with an image of same size");
        }

        System.arraycopy(image.pixels, 0, this.pixels, 0, this.pixels.length);
    }

    /**
     * Indicates if draw mode is locked.<br>
     * If the draw mode is locked, it is impossible to change the draw mode status
     *
     * @return {@code true} if draw mode is locked.
     */
    public boolean drawModeLocked()
    {
        return this.drawModeLocked;
    }

    /**
     * Create an image copy<br>
     * Note : if this image is not in draw mode, al visible sprites will be consider like a part of this image
     *
     * @return The copy
     */
    public JHelpImage createCopy()
    {
        final JHelpImage copy = new JHelpImage(this.width, this.height);

        copy.startDrawMode();
        copy.copy(this);
        copy.endDrawMode();

        return copy;
    }

    /**
     * Create a mask from the image
     *
     * @param positiveColor Color that consider as light on (other colors are consider as light off)
     * @param precision     Precision or distance minimum for consider colors equals
     * @return Created mask
     */
    public JHelpMask createMask(final int positiveColor, int precision)
    {
        precision = Math.max(0, precision);
        final JHelpMask mask  = new JHelpMask(this.width, this.height);
        final int       alpha = (positiveColor >> 24) & 0xFF;
        final int       red   = (positiveColor >> 16) & 0xFF;
        final int       green = (positiveColor >> 8) & 0xFF;
        final int       blue  = positiveColor & 0xFF;
        int             pix   = 0;
        int             color, a, r, g, b;

        for (int y = 0; y < this.height; y++)
        {
            for (int x = 0; x < this.width; x++)
            {
                color = this.pixels[pix];
                a = (color >> 24) & 0xFF;
                r = (color >> 16) & 0xFF;
                g = (color >> 8) & 0xFF;
                b = color & 0xFF;

                if ((Math.abs(alpha - a) <= precision) && (Math.abs(red - r) <= precision)
                    && (Math.abs(green - g) <= precision) && (Math.abs(blue - b) <= precision))
                {
                    mask.setValue(x, y, true);
                }

                pix++;
            }
        }

        return mask;
    }

    /**
     * Draw an empty shape<br>
     * MUST be in draw mode
     *
     * @param shape      Shape to draw
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void drawShape(final Shape shape, final int color, final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        final PathIterator pathIterator = shape.getPathIterator(ConstantsGUI.AFFINE_TRANSFORM, ConstantsGUI.FLATNESS);

        final double[] info   = new double[6];
        int            x      = 0;
        int            y      = 0;
        int            xStart = 0;
        int            yStart = 0;
        int            xx, yy;

        while (!pathIterator.isDone())
        {
            switch (pathIterator.currentSegment(info))
            {
                case PathIterator.SEG_MOVETO:
                    xStart = x = (int) Math.round(info[0]);
                    yStart = y = (int) Math.round(info[1]);

                    break;
                case PathIterator.SEG_LINETO:
                    xx = (int) Math.round(info[0]);
                    yy = (int) Math.round(info[1]);

                    this.drawLine(x, y, xx, yy, color, doAlphaMix);

                    x = xx;
                    y = yy;

                    break;
                case PathIterator.SEG_CLOSE:
                    this.drawLine(x, y, xStart, yStart, color, doAlphaMix);

                    x = xStart;
                    y = yStart;

                    break;
            }

            pathIterator.next();
        }
    }

    /**
     * Create sprite with initial image inside<br>
     * MUST NOT be in draw mode
     *
     * @param x      X
     * @param y      Y
     * @param source Initial image
     * @return Created sprite
     */
    public JHelpSprite createSprite(final int x, final int y, final JHelpImage source)
    {
        if (this.drawMode)
        {
            throw new IllegalStateException("MUST NOT be in draw mode !");
        }

        if (source == null)
        {
            throw new NullPointerException("source MUST NOT be null");
        }

        final int         index  = this.sprites.size();
        final JHelpSprite sprite = new JHelpSprite(x, y, source, this, index);
        this.sprites.add(sprite);
        return sprite;
    }

    /**
     * Make image darker<br>
     * MUST be in draw mode
     *
     * @param factor Darker factor in [0, 255]
     */
    public void darker(final int factor)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int color;

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            color = this.pixels[pix];

            this.pixels[pix] = (color & 0xFF000000) | //
                               (Math2.limit0_255(((color >> 16) & 0xFF) - factor) << 16) | //
                               (Math2.limit0_255(((color >> 8) & 0xFF) - factor) << 8) | //
                               Math2.limit0_255((color & 0xFF) - factor);
        }
    }

    /**
     * Divide an other image<br>
     * This image and the given one MUST have same dimension<br>
     * Note : if this image or given one not in draw mode, all visible sprites (of the image) are consider like a part
     * of the
     * image
     *
     * @param image Image to divide with
     */
    public void divide(final JHelpImage image)
    {
        if ((this.width != image.width) || (this.height != image.height))
        {
            throw new IllegalArgumentException("We can only multiply with an image of same size");
        }

        int colorThis, colorImage;

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            colorThis = this.pixels[pix];
            colorImage = image.pixels[pix];

            this.pixels[pix] = (colorThis & 0xFF000000) | //
                               ((((((colorThis >> 16) & 0xFF) * 256) / (((colorImage >> 16) & 0xFF) + 1))) << 16) | //
                               ((((((colorThis >> 8) & 0xFF) * 256) / (((colorImage >> 8) & 0xFF) + 1))) << 8) | //
                               (((colorThis & 0xFF) * 256) / ((colorImage & 0xFF) + 1));
        }
    }

    /**
     * Draw an ellipse<br>
     * MUST be in draw mode
     *
     * @param x      X of upper left corner
     * @param y      Y of upper left corner
     * @param width  Width
     * @param height Height
     * @param color  Color to use
     */
    public void drawEllipse(final int x, final int y, final int width, final int height, final int color)
    {
        this.drawEllipse(x, y, width, height, color, true);
    }

    /**
     * Draw an ellipse<br>
     * MUST be in draw mode
     *
     * @param x          X of upper left corner
     * @param y          Y of upper left corner
     * @param width      Width
     * @param height     Height
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void drawEllipse(
            final int x, final int y, final int width, final int height, final int color
            , final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.drawShape(new Ellipse2D.Double(x, y, width, height), color, doAlphaMix);
    }

    /**
     * Draw horizontal line<br>
     * MUST be in draw mode
     *
     * @param x1         X start
     * @param x2         End X
     * @param y          Y
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void drawHorizontalLine(final int x1, final int x2, final int y, final int color, final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if ((y < this.clip.yMin) || (y > this.clip.yMax))
        {
            return;
        }

        final int xMin = Math.max(this.clip.xMin, Math.min(x1, x2));
        final int xMax = Math.min(this.clip.xMax, Math.max(x1, x2));

        if ((xMin > xMax) || (xMin > this.clip.xMax) || (xMax < this.clip.xMin))
        {
            return;
        }

        int start = xMin;
        int end   = xMax;

        final int alpha = (color >> 24) & 0xFF;

        if ((alpha == 0) && (doAlphaMix))
        {
            return;
        }

        final int yy = y * this.width;
        start += yy;
        end += yy;

        if ((alpha == 255) || (!doAlphaMix))
        {
            for (int pix = start; pix <= end; pix++)
            {
                this.pixels[pix] = color;
            }

            return;
        }

        final int ahpla = 256 - alpha;
        final int red   = ((color >> 16) & 0xFF) * alpha;
        final int green = ((color >> 8) & 0xFF) * alpha;
        final int blue  = (color & 0xFF) * alpha;
        int       col;

        for (int pix = start; pix <= end; pix++)
        {
            col = this.pixels[pix];

            this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                               (((red + (((col >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                               (((green + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                               ((blue + ((col & 0xFF) * ahpla)) >> 8);
        }
    }

    /**
     * Draw an horizontal line<br>
     * MUST be in draw mode
     *
     * @param x1    Start X
     * @param x2    End X
     * @param y     Y
     * @param color Color to use
     */
    public void drawHorizontalLine(final int x1, final int x2, final int y, final int color)
    {
        this.drawHorizontalLine(x1, x2, y, color, true);
    }

    /**
     * Draw an image<br>
     * MUST be in draw mode
     *
     * @param x     X
     * @param y     Y
     * @param image image to draw
     */
    public void drawImage(final int x, final int y, final JHelpImage image)
    {
        this.drawImage(x, y, image, true);
    }

    /**
     * Draw an image<br>
     * MUST be in draw mode
     *
     * @param x          X
     * @param y          Y
     * @param image      Image to draw
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void drawImage(final int x, final int y, final JHelpImage image, final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.drawImage(x, y, image, 0, 0, image.width, image.height, doAlphaMix);
    }

    /**
     * Draw a part off image<br>
     * MUST be in draw mode
     *
     * @param x          X on this
     * @param y          Y on this
     * @param image      Image to draw
     * @param xImage     X on image
     * @param yImage     Y on image
     * @param width      Part width
     * @param height     Part height
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void drawImage(
            final int x, final int y, final JHelpImage image, final int xImage, final int yImage, final int
            width, final int height,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.drawImageInternal(x, y, image, xImage, yImage, width, height, doAlphaMix);
    }

    /**
     * Draw an image over this image with specific alpha<br>
     * MUST be in draw mode
     *
     * @param x     X position
     * @param y     Y position
     * @param image Image to draw
     * @param alpha Alpha to use
     */
    public void drawImage(final int x, final int y, final JHelpImage image, final int alpha)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.drawImage(x, y, image, 0, 0, image.width, image.height, alpha);
    }

    /**
     * Draw a part of image with a specific alpha<br>
     * MUST be in draw mode
     *
     * @param x      X position
     * @param y      Y position
     * @param image  Image to draw
     * @param xImage X of image part
     * @param yImage Y of image part
     * @param width  Image part width
     * @param height Image part height
     * @param alpha  Alpha to use
     */
    public void drawImage(
            final int x, final int y, final JHelpImage image,
            final int xImage, final int yImage, final int width, final int height,
            final int alpha)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.drawImageInternal(x, y, image, xImage, yImage, width, height, alpha);
    }

    /**
     * Draw a part off image<br>
     * MUST be in draw mode
     *
     * @param x      X on this
     * @param y      Y on this
     * @param image  Image to draw
     * @param xImage X on image
     * @param yImage Y on image
     * @param width  Part width
     * @param height Part height
     */
    public void drawImage(
            final int x, final int y, final JHelpImage image, final int xImage, final int yImage,
            final int width, final int height)
    {
        this.drawImage(x, y, image, xImage, yImage, width, height, true);
    }

    /**
     * Draw an image or using a pixel combination
     *
     * @param x                X where locate up-left corner of image to draw
     * @param y                Y where locate up-left corner of image to draw
     * @param image            Image to draw
     * @param pixelCombination Pixel combination to use
     */
    public void drawImage(final int x, final int y, final JHelpImage image, final PixelCombination pixelCombination)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.drawImage(x, y, image, 0, 0, image.width, image.height, pixelCombination);
    }

    /**
     * Draw a part of image or using a pixel combination
     *
     * @param x                X where locate up-left corner of image to draw
     * @param y                Y where locate up-left corner of image to draw
     * @param image            Image to draw
     * @param xImage           X of up-left corner of image part
     * @param yImage           Y of up-left corner of image part
     * @param width            Part width
     * @param height           Part height
     * @param pixelCombination Pixel combination to use
     */
    public void drawImage(
            int x, int y, final JHelpImage image,
            int xImage, int yImage, int width, int height,
            final PixelCombination pixelCombination)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if (xImage < 0)
        {
            x -= xImage;
            width += xImage;
            xImage = 0;
        }

        if (x < this.clip.xMin)
        {
            xImage -= x - this.clip.xMin;
            width += x - this.clip.xMin;
            x = this.clip.xMin;
        }

        if (yImage < 0)
        {
            y -= yImage;
            height += yImage;
            yImage = 0;
        }

        if (y < this.clip.yMin)
        {
            yImage -= y - this.clip.yMin;
            height += y - this.clip.yMin;
            y = this.clip.yMin;
        }

        final int w = Math2.minimum((this.clip.xMax + 1) - x, image.width - xImage, width, this.width - x);
        final int h = Math2.minimum((this.clip.yMax + 1) - y, image.height - yImage, height, this.height - y);

        if ((w <= 0) || (h <= 0))
        {
            return;
        }

        int lineThis = x + (y * this.width);
        int pixThis;

        int lineImage = xImage + (yImage * image.width);
        int pixImage;

        for (int yy = 0; yy < h; yy++)
        {
            pixThis = lineThis;
            pixImage = lineImage;

            for (int xx = 0; xx < w; xx++)
            {
                this.pixels[pixThis] = pixelCombination.combine(this.pixels[pixThis], image.pixels[pixImage]);
                pixThis++;
                pixImage++;
            }

            lineThis += this.width;
            lineImage += image.width;
        }
    }

    /**
     * Draw an image with a given transformation.<br>
     * Image MUST be in draw mode.<br>
     * Given image and transformation MUST have same sizes
     *
     * @param x              X
     * @param y              Y
     * @param image          Image to draw
     * @param transformation Transformation to apply
     * @param doAlphaMix     Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void drawImage(
            int x, int y, final JHelpImage image, final Transformation transformation,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int width  = image.getWidth();
        int height = image.getHeight();

        if ((width != transformation.getWidth()) || (height != transformation.getHeight()))
        {
            throw new IllegalArgumentException("Image and transformation MUST have same size");
        }

        int xImage = 0;

        if (x < this.clip.xMin)
        {
            xImage -= x - this.clip.xMin;
            width += x - this.clip.xMin;
            x = this.clip.xMin;
        }

        int yImage = 0;

        if (y < this.clip.yMin)
        {
            yImage -= y - this.clip.yMin;
            height += y - this.clip.yMin;
            y = this.clip.yMin;
        }

        final int w = Math2.minimum((this.clip.xMax + 1) - x, image.width - xImage, width, this.width - x);
        final int h = Math2.minimum((this.clip.yMax + 1) - y, image.height - yImage, height, this.height - y);

        if ((w <= 0) || (h <= 0))
        {
            return;
        }

        int    lineImage = xImage + (yImage * image.width);
        int    pixImage;
        int    pixThis;
        int    colorThis;
        int    colorImage;
        int    alpha;
        int    ahpla;
        Vector vector;
        int    tx, ty;

        for (int yy = 0, yyy = y; yy < h; yy++, yyy++)
        {
            pixImage = lineImage;

            for (int xx = 0, xxx = x; xx < w; xx++, xxx++)
            {
                vector = transformation.getVector(xImage + xx, yImage + yy);
                tx = xxx + vector.vx;
                ty = yyy + vector.vy;

                if ((tx >= this.clip.xMin) && (tx <= this.clip.xMax) && (ty >= this.clip.yMin) &&
                    (ty <= this.clip.yMax))
                {
                    pixThis = tx + (ty * this.width);
                    colorImage = image.pixels[pixImage];

                    alpha = (colorImage >> 24) & 0xFF;

                    if ((alpha == 255) || (!doAlphaMix))
                    {
                        this.pixels[pixThis] = colorImage;
                    }
                    else if (alpha > 0)
                    {
                        ahpla = 256 - alpha;

                        colorThis = this.pixels[pixThis];

                        this.pixels[pixThis] = (Math.min(255, alpha + ((colorThis >> 24) & 0xFF)) << 24) | //
                                               ((((((colorImage >> 16) & 0xFF) * alpha)
                                                  + (((colorThis >> 16) & 0xFF) * ahpla)) >> 8) << 16) |
                                               ((((((colorImage >> 8) & 0xFF) * alpha)
                                                  + (((colorThis >> 8) & 0xFF) * ahpla)) >> 8) << 8) |
                                               ((((colorImage & 0xFF) * alpha) + ((colorThis & 0xFF) * ahpla)) >> 8);
                    }
                }

                pixImage++;
            }

            lineImage += image.width;
        }
    }

    /**
     * Draw a thick line
     *
     * @param x1        First point X
     * @param y1        First point Y
     * @param x2        Second point X
     * @param y2        Second point Y
     * @param thickness Thick of the line
     * @param color     Color to use on line
     */
    public void drawThickLine(
            final int x1, final int y1, final int x2, final int y2, final int thickness, final int color)
    {
        if (thickness < 2)
        {
            this.drawLine(x1, y1, x2, y2, color);
            return;
        }

        this.fillShape(this.createThickLine(x1, y1, x2, y2, thickness), color);
    }

    /**
     * Draw a thick line
     *
     * @param x1        First point X
     * @param y1        First point Y
     * @param x2        Second point X
     * @param y2        Second point Y
     * @param thickness Thick of the line
     * @param texture   Texture to use on line
     */
    public void drawThickLine(
            final int x1, final int y1, final int x2, final int y2, final int thickness,
            final JHelpImage texture)
    {
        this.fillShape(this.createThickLine(x1, y1, x2, y2, thickness), texture);
    }

    /**
     * Draw shape with thick border
     *
     * @param shape     Shape to draw
     * @param thickness Border thick
     * @param color     Color to use on border
     */
    public void drawThickShape(final Shape shape, final int thickness, final int color)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if (thickness < 1)
        {
            return;
        }

        final PathIterator pathIterator = shape.getPathIterator(ConstantsGUI.AFFINE_TRANSFORM, ConstantsGUI.FLATNESS);

        final double[] info   = new double[6];
        int            x      = 0;
        int            y      = 0;
        int            xStart = 0;
        int            yStart = 0;
        int            xx, yy;

        while (!pathIterator.isDone())
        {
            switch (pathIterator.currentSegment(info))
            {
                case PathIterator.SEG_MOVETO:
                    xStart = x = (int) Math.round(info[0]);
                    yStart = y = (int) Math.round(info[1]);

                    break;
                case PathIterator.SEG_LINETO:
                    xx = (int) Math.round(info[0]);
                    yy = (int) Math.round(info[1]);

                    this.drawThickLine(x, y, xx, yy, thickness, color);

                    x = xx;
                    y = yy;

                    break;
                case PathIterator.SEG_CLOSE:
                    this.drawThickLine(x, y, xStart, yStart, thickness, color);

                    x = xStart;
                    y = yStart;

                    break;
            }

            pathIterator.next();
        }
    }

    /**
     * Draw a neon path.<br>
     * Image MUST be in draw mode
     *
     * @param path         Path to draw
     * @param thin         Neon thick
     * @param color        Color to use
     * @param percentStart Path percent to start drawing in [0, 1]
     * @param percentEnd   Path percent to stop drawing in [0, 1]
     */
    public void drawNeon(final Path path, int thin, int color, final double percentStart, final double percentEnd)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        final int size = path.numberOfSegment();

        if (size <= 0)
        {
            return;
        }

        int          alpha = color & 0xFF000000;
        int          red   = (color >> 16) & 0xFF;
        int          green = (color >> 8) & 0xFF;
        int          blue  = color & 0xFF;
        double       y     = JHelpImage.computeY(red, green, blue);
        final double u     = JHelpImage.computeU(red, green, blue);
        final double v     = JHelpImage.computeV(red, green, blue);
        final int    start = Math2.limit((int) (size * Math.min(percentStart, percentEnd)), 0, size);
        final int    limit = Math2.limit((int) (size * Math.max(percentStart, percentEnd)), 0, size);
        Segment      segment;

        do
        {
            for (int index = start; index < limit; index++)
            {
                segment = path.getSegment(index);
                this.drawThickLine((int) Math.round(segment.x1), (int) Math.round(segment.y1),
                                   (int) Math.round(segment.x2), (int) Math.round(segment.y2), thin, color);
            }

            y *= 2;
            red = JHelpImage.computeRed(y, u, v);
            green = JHelpImage.computeGreen(y, u, v);
            blue = JHelpImage.computeBlue(y, u, v);
            color = alpha | (red << 16) | (green << 8) | blue;
            thin >>= 1;
        }
        while (thin > 1);
    }

    /**
     * Repeat an image along a path.<br>
     * Image MUST be in draw mode
     *
     * @param path         Path to follow
     * @param elementDraw  Image to repeat
     * @param percentStart Path percent to start drawing in [0, 1]
     * @param percentEnd   Path percent to stop drawing in [0, 1]
     * @param doAlphaMix   Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void drawPath(
            final Path path, final JHelpImage elementDraw, final double percentStart, final double percentEnd,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        path.drawPath(this, elementDraw, doAlphaMix, percentStart, percentEnd);
    }

    /**
     * Draw a polygon<br>
     * MUST be in draw mode
     *
     * @param xs      Polygon X list
     * @param offsetX Where start read the X list
     * @param ys      Polygon Y list
     * @param offsetY Where start read the Y list
     * @param length  Number of point
     * @param color   Color to use
     */
    public void drawPolygon(
            final int[] xs, final int offsetX, final int[] ys, final int offsetY, final int length,
            final int color)
    {
        this.drawPolygon(xs, offsetX, ys, offsetY, length, color, true);
    }

    /**
     * Draw a polygon<br>
     * MUST be in draw mode
     *
     * @param xs         Polygon X list
     * @param offsetX    Where start read the X list
     * @param ys         Polygon Y list
     * @param offsetY    Where start read the Y list
     * @param length     Number of point
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void drawPolygon(
            final int[] xs, int offsetX, final int[] ys, int offsetY, int length, final int color,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if (offsetX < 0)
        {
            length += offsetX;

            offsetX = 0;
        }

        if (offsetY < 0)
        {
            length += offsetY;

            offsetY = 0;
        }

        length = Math2.minimum(length, xs.length - offsetX, ys.length - offsetY);

        if (length < 3)
        {
            return;
        }

        int       x      = xs[offsetX];
        final int xStart = x;
        int       y      = ys[offsetY];
        final int yStart = y;
        int       xx, yy;

        for (int i = 1; i < length; i++)
        {
            offsetX++;
            offsetY++;

            xx = xs[offsetX];
            yy = ys[offsetY];

            this.drawLine(x, y, xx, yy, color, doAlphaMix);

            x = xx;
            y = yy;
        }

        this.drawLine(x, y, xStart, yStart, color, doAlphaMix);
    }

    /**
     * Draw a polygon<br>
     * MUST be in draw mode
     *
     * @param xs    X list
     * @param ys    Y list
     * @param color Color to use
     */
    public void drawPolygon(final int[] xs, final int[] ys, final int color)
    {
        this.drawPolygon(xs, ys, color, true);
    }

    /**
     * Draw a polygon<br>
     * MUST be in draw mode
     *
     * @param xs         X list
     * @param ys         Y list
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void drawPolygon(final int[] xs, final int[] ys, final int color, final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.drawPolygon(xs, 0, ys, 0, Math.min(xs.length, ys.length), color, doAlphaMix);
    }

    /**
     * Draw an empty rectangle<br>
     * MUST be in draw mode
     *
     * @param x      X of top-left
     * @param y      Y of top-left
     * @param width  Rectangle width
     * @param height Rectangle height
     * @param color  Color to use
     */
    public void drawRectangle(final int x, final int y, final int width, final int height, final int color)
    {
        this.drawRectangle(x, y, width, height, color, true);
    }

    /**
     * Draw an empty rectangle<br>
     * MUST be in draw mode
     *
     * @param x          X of top-left
     * @param y          Y of top-left
     * @param width      Rectangle width
     * @param height     Rectangle height
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void drawRectangle(
            final int x, final int y, final int width, final int height, final int color,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if ((width <= 0) || (height <= 0))
        {
            return;
        }

        final int x2 = (x + width) - 1;
        final int y2 = (y + height) - 1;

        this.drawHorizontalLine(x, x2, y, color, doAlphaMix);
        this.drawHorizontalLine(x, x2, y2, color, doAlphaMix);
        this.drawVerticalLine(x, y, y2, color, doAlphaMix);
        this.drawVerticalLine(x2, y, y2, color, doAlphaMix);
    }

    /**
     * Draw round corner rectangle<br>
     * MUST be in draw mode
     *
     * @param x         X
     * @param y         Y
     * @param width     Width
     * @param height    Height
     * @param arcWidth  Arc width
     * @param arcHeight Arc height
     * @param color     Color to use
     */
    public void drawRoundRectangle(
            final int x, final int y, final int width, final int height,
            final int arcWidth, final int arcHeight, final int color)
    {
        this.drawRoundRectangle(x, y, width, height, arcWidth, arcHeight, color, true);
    }

    /**
     * Draw round corner rectangle<br>
     * MUST be in draw mode
     *
     * @param x          X
     * @param y          Y
     * @param width      Width
     * @param height     Height
     * @param arcWidth   Arc width
     * @param arcHeight  Arc height
     * @param color      Color to use
     * @param doAlphaMix Indicates if do alpha mixing or just overwrite
     */
    public void drawRoundRectangle(
            final int x, final int y, final int width, final int height,
            final int arcWidth, final int arcHeight, final int color, final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.drawShape(new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight), color, doAlphaMix);
    }

    /**
     * Draw shape with thick border
     *
     * @param shape     Shape to draw
     * @param thickness Border thick
     * @param texture   Texture to use on border
     */
    public void drawThickShape(final Shape shape, final int thickness, final JHelpImage texture)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if (thickness < 1)
        {
            return;
        }

        final PathIterator pathIterator = shape.getPathIterator(ConstantsGUI.AFFINE_TRANSFORM, ConstantsGUI.FLATNESS);

        final double[] info   = new double[6];
        int            x      = 0;
        int            y      = 0;
        int            xStart = 0;
        int            yStart = 0;
        int            xx, yy;

        while (!pathIterator.isDone())
        {
            switch (pathIterator.currentSegment(info))
            {
                case PathIterator.SEG_MOVETO:
                    xStart = x = (int) Math.round(info[0]);
                    yStart = y = (int) Math.round(info[1]);

                    break;
                case PathIterator.SEG_LINETO:
                    xx = (int) Math.round(info[0]);
                    yy = (int) Math.round(info[1]);

                    this.drawThickLine(x, y, xx, yy, thickness, texture);

                    x = xx;
                    y = yy;

                    break;
                case PathIterator.SEG_CLOSE:
                    this.drawThickLine(x, y, xStart, yStart, thickness, texture);

                    x = xStart;
                    y = yStart;

                    break;
            }

            pathIterator.next();
        }
    }

    /**
     * Draw an empty shape<br>
     * MUST be in draw mode
     *
     * @param shape Shape to draw
     * @param color Color to use
     */
    public void drawShape(final Shape shape, final int color)
    {
        this.drawShape(shape, color, true);
    }

    /**
     * Draw a string<br>
     * MUST be in draw mode
     *
     * @param x      X of top-left
     * @param y      Y of top-left
     * @param string String to draw
     * @param font   Font to use
     * @param color  Color to use
     */
    public void drawString(final int x, final int y, final String string, final JHelpFont font, final int color)
    {
        this.drawString(x, y, string, font, color, true);
    }

    /**
     * Draw a string<br>
     * MUST be in draw mode
     *
     * @param x          X of top-left
     * @param y          Y of top-left
     * @param string     String to draw
     * @param font       Font to use
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void drawString(
            final int x, final int y, final String string, final JHelpFont font, final int color,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        final Shape     shape  = font.computeShape(string, x, y);
        final Rectangle bounds = shape.getBounds();

        this.drawShape(shape, color, doAlphaMix);

        if (font.isUnderline())
        {
            this.drawHorizontalLine(x, x + bounds.width, font.underlinePosition(string, y), color, doAlphaMix);
        }
    }

    /**
     * Draw a string center on given point<br>
     * MUST be in draw mode
     *
     * @param x      String center X
     * @param y      String center Y
     * @param string String to draw
     * @param font   Font to use
     * @param color  Color to use
     */
    public void drawStringCenter(final int x, final int y, final String string, final JHelpFont font, final int color)
    {
        this.drawStringCenter(x, y, string, font, color, true);
    }

    /**
     * Draw a string center on given point<br>
     * MUST be in draw mode
     *
     * @param x          String center X
     * @param y          String center Y
     * @param string     String to draw
     * @param font       Font to use
     * @param color      Color to use
     * @param doAlphaMix Indicates if use alpha mix
     */
    public void drawStringCenter(
            final int x, final int y, final String string, final JHelpFont font, final int color,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        final Shape     shape  = font.computeShape(string, x, y);
        final Rectangle bounds = shape.getBounds();

        this.drawShapeCenter(shape, color, doAlphaMix);

        if (font.isUnderline())
        {
            this.drawHorizontalLine(x - (bounds.width >> 1), x + (bounds.width >> 1), //
                                    font.underlinePosition(string, y - (bounds.height >> 1)), color, doAlphaMix);
        }
    }

    /**
     * Draw an ellipse with thick border
     *
     * @param x         Up left corner X
     * @param y         Up left corner Y
     * @param width     Width
     * @param height    Height
     * @param thickness Thick of the border
     * @param color     Color to use on border
     */
    public void drawThickEllipse(
            final int x, final int y, final int width, final int height, final int thickness,
            final int color)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.drawThickShape(new Ellipse2D.Double(x, y, width, height), thickness, color);
    }

    /**
     * Draw an ellipse with thick border
     *
     * @param x         Up left corner X
     * @param y         Up left corner Y
     * @param width     Width
     * @param height    Height
     * @param thickness Thick of the border
     * @param texture   Texture to use on border
     */
    public void drawThickEllipse(
            final int x, final int y, final int width, final int height, final int thickness,
            final JHelpImage texture)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.drawThickShape(new Ellipse2D.Double(x, y, width, height), thickness, texture);
    }

    /**
     * Draw an ellipse with thick border
     *
     * @param x         Up left corner X
     * @param y         Up left corner Y
     * @param width     Width
     * @param height    Height
     * @param thickness Thick of the border
     * @param paint     Paint to use on border
     */
    public void drawThickEllipse(
            final int x, final int y, final int width, final int height, final int thickness,
            final JHelpPaint paint)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.drawThickShape(new Ellipse2D.Double(x, y, width, height), thickness, paint);
    }

    /**
     * Draw a vertical line<br>
     * MUST be in draw mode
     *
     * @param x     X
     * @param y1    Start Y
     * @param y2    End Y
     * @param color Color to use
     */
    public void drawVerticalLine(final int x, final int y1, final int y2, final int color)
    {
        this.drawVerticalLine(x, y1, y2, color, true);
    }

    /**
     * Stop the draw mode and refresh the image<br>
     * Don't call this method if image is locked. Use {@link #drawModeLocked()} to know.<br>
     * The image is locked if we are inside a task launch by {@link #playInDrawMode(ConsumerTask)} or {@link #playOutDrawMode(ConsumerTask)}
     *
     * @throws IllegalStateException If draw mode is locked
     */
    public void endDrawMode()
    {
        if (this.drawModeLocked)
        {
            throw new IllegalStateException("Draw mode is locked");
        }

        if (this.drawMode)
        {
            this.drawMode = false;

            final int length = this.sprites.size();

            for (int index = 0; index < length; index++)
            {
                if (this.visibilities[index])
                {
                    this.sprites.get(index)
                                .changeVisible(true);
                }
            }

            synchronized (this.playOutDrawMode)
            {
                this.drawModeLocked = true;

                while (!this.playOutDrawMode.empty())
                {
                    this.playOutDrawMode.outQueue().consume(this);
                }

                this.drawModeLocked = false;
            }
        }

        this.update();
    }

    /**
     * Draw a thick line
     *
     * @param x1        First point X
     * @param y1        First point Y
     * @param x2        Second point X
     * @param y2        Second point Y
     * @param thickness Thick of the line
     * @param paint     Paint to use on line
     */
    public void drawThickLine(
            final int x1, final int y1, final int x2, final int y2,
            final int thickness, final JHelpPaint paint)
    {
        this.fillShape(this.createThickLine(x1, y1, x2, y2, thickness), paint);
    }

    /**
     * Draw a polygon<br>
     * MUST be in draw mode
     *
     * @param xs        X list
     * @param ys        Y list
     * @param thickness Thickness
     * @param color     Color to use
     */
    public void drawThickPolygon(final int[] xs, final int[] ys, final int thickness, final int color)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.drawThickPolygon(xs, 0, ys, 0, Math.min(xs.length, ys.length), thickness, color);
    }

    /**
     * Draw a thick polygon<br>
     * MUST be in draw mode
     *
     * @param xs        Polygon X list
     * @param offsetX   Where start read the X list
     * @param ys        Polygon Y list
     * @param offsetY   Where start read the Y list
     * @param length    Number of point
     * @param thickness Thickness
     * @param color     Color to use
     */
    public void drawThickPolygon(
            final int[] xs, int offsetX, final int[] ys, int offsetY, int length,
            final int thickness, final int color)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if (offsetX < 0)
        {
            length += offsetX;

            offsetX = 0;
        }

        if (offsetY < 0)
        {
            length += offsetY;

            offsetY = 0;
        }

        length = Math2.minimum(length, xs.length - offsetX, ys.length - offsetY);

        if ((length < 3) || (thickness < 1))
        {
            return;
        }

        int       x      = xs[offsetX];
        final int xStart = x;
        int       y      = ys[offsetY];
        final int yStart = y;
        int       xx, yy;

        for (int i = 1; i < length; i++)
        {
            offsetX++;
            offsetY++;

            xx = xs[offsetX];
            yy = ys[offsetY];

            this.drawThickLine(x, y, xx, yy, thickness, color);

            x = xx;
            y = yy;
        }

        this.drawThickLine(x, y, xStart, yStart, thickness, color);
    }

    /**
     * Draw a polygon with thick border<br>
     * MUST be in draw mode
     *
     * @param xs        X list
     * @param ys        Y list
     * @param thickness Thickness
     * @param texture   Texture to use
     */
    public void drawThickPolygon(final int[] xs, final int[] ys, final int thickness, final JHelpImage texture)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.drawThickPolygon(xs, 0, ys, 0, Math.min(xs.length, ys.length), thickness, texture);
    }

    /**
     * Draw a polygon with thick border
     *
     * @param xs        Xs of polygon points
     * @param offsetX   Offset where start read the Xs
     * @param ys        Ys of polygon points
     * @param offsetY   Offset where start read Ys
     * @param length    Number of polygon point
     * @param thickness Polygon border thick
     * @param texture   Texture to use on polygon
     */
    public void drawThickPolygon(
            final int[] xs, int offsetX, final int[] ys, int offsetY, int length, final int thickness,
            final JHelpImage texture)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if (offsetX < 0)
        {
            length += offsetX;

            offsetX = 0;
        }

        if (offsetY < 0)
        {
            length += offsetY;

            offsetY = 0;
        }

        length = Math2.minimum(length, xs.length - offsetX, ys.length - offsetY);

        if ((length < 3) || (thickness < 1))
        {
            return;
        }

        int       x      = xs[offsetX];
        final int xStart = x;
        int       y      = ys[offsetY];
        final int yStart = y;
        int       xx, yy;

        for (int i = 1; i < length; i++)
        {
            offsetX++;
            offsetY++;

            xx = xs[offsetX];
            yy = ys[offsetY];

            this.drawThickLine(x, y, xx, yy, thickness, texture);

            x = xx;
            y = yy;
        }

        this.drawThickLine(x, y, xStart, yStart, thickness, texture);
    }

    /**
     * Draw a polygon with thick border<br>
     * MUST be in draw mode
     *
     * @param xs        X list
     * @param ys        Y list
     * @param thickness Thickness
     * @param paint     Paint to use
     */
    public void drawThickPolygon(final int[] xs, final int[] ys, final int thickness, final JHelpPaint paint)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.drawThickPolygon(xs, 0, ys, 0, Math.min(xs.length, ys.length), thickness, paint);
    }

    /**
     * Draw a polygon with thick border
     *
     * @param xs        Xs of polygon points
     * @param offsetX   Offset where start read the Xs
     * @param ys        Ys of polygon points
     * @param offsetY   Offset where start read Ys
     * @param length    Number of polygon point
     * @param thickness Polygon border thick
     * @param paint     Paint to use on polygon
     */
    public void drawThickPolygon(
            final int[] xs, int offsetX, final int[] ys, int offsetY, int length, final int thickness,
            final JHelpPaint paint)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if (offsetX < 0)
        {
            length += offsetX;

            offsetX = 0;
        }

        if (offsetY < 0)
        {
            length += offsetY;

            offsetY = 0;
        }

        length = Math2.minimum(length, xs.length - offsetX, ys.length - offsetY);

        if ((length < 3) || (thickness < 1))
        {
            return;
        }

        int       x      = xs[offsetX];
        final int xStart = x;
        int       y      = ys[offsetY];
        final int yStart = y;
        int       xx, yy;

        for (int i = 1; i < length; i++)
        {
            offsetX++;
            offsetY++;

            xx = xs[offsetX];
            yy = ys[offsetY];

            this.drawThickLine(x, y, xx, yy, thickness, paint);

            x = xx;
            y = yy;
        }

        this.drawThickLine(x, y, xStart, yStart, thickness, paint);
    }

    /**
     * Draw rectangle with thick border
     *
     * @param x         Up left corner X
     * @param y         Up left corner Y
     * @param width     Width
     * @param height    Height
     * @param thickness Border thick
     * @param color     Color to use on border
     */
    public void drawThickRectangle(
            final int x, final int y, final int width, final int height, final int thickness,
            final int color)
    {
        final int x2 = x + width;
        final int y2 = y + height;
        this.drawThickLine(x, y, x2, y, thickness, color);
        this.drawThickLine(x2, y, x2, y2, thickness, color);
        this.drawThickLine(x2, y2, x, y2, thickness, color);
        this.drawThickLine(x, y2, x, y, thickness, color);
    }

    /**
     * Draw rectangle with thick border
     *
     * @param x         Up left corner X
     * @param y         Up left corner Y
     * @param width     Width
     * @param height    Height
     * @param thickness Border thick
     * @param texture   texture to use on border
     */
    public void drawThickRectangle(
            final int x, final int y, final int width, final int height, final int thickness,
            final JHelpImage texture)
    {
        final int x2 = x + width;
        final int y2 = y + height;
        this.drawThickLine(x, y, x2, y, thickness, texture);
        this.drawThickLine(x2, y, x2, y2, thickness, texture);
        this.drawThickLine(x2, y2, x, y2, thickness, texture);
        this.drawThickLine(x, y2, x, y, thickness, texture);
    }

    /**
     * Draw rectangle with thick border
     *
     * @param x         Up left corner X
     * @param y         Up left corner Y
     * @param width     Width
     * @param height    Height
     * @param thickness Border thick
     * @param paint     texture to use on border
     */
    public void drawThickRectangle(
            final int x, final int y, final int width, final int height, final int thickness,
            final JHelpPaint paint)
    {
        final int x2 = x + width;
        final int y2 = y + height;
        this.drawThickLine(x, y, x2, y, thickness, paint);
        this.drawThickLine(x2, y, x2, y2, thickness, paint);
        this.drawThickLine(x2, y2, x, y2, thickness, paint);
        this.drawThickLine(x, y2, x, y, thickness, paint);
    }

    /**
     * Draw round rectangle rectangle with thick border
     *
     * @param x         Up left corner X
     * @param y         Up left corner Y
     * @param width     Width
     * @param height    Height
     * @param arcWidth  Arc width
     * @param arcHeight Arc height
     * @param thickness Border thick
     * @param color     Color to use on border
     */
    public void drawThickRoundRectangle(
            final int x, final int y, final int width, final int height,
            final int arcWidth, final int arcHeight,
            final int thickness, final int color)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.drawThickShape(new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight), thickness, color);
    }

    /**
     * Draw round rectangle rectangle with thick border
     *
     * @param x         Up left corner X
     * @param y         Up left corner Y
     * @param width     Width
     * @param height    Height
     * @param arcWidth  Arc width
     * @param arcHeight Arc height
     * @param thickness Border thick
     * @param texture   Texture to use on border
     */
    public void drawThickRoundRectangle(
            final int x, final int y, final int width, final int height,
            final int arcWidth, final int arcHeight,
            final int thickness, final JHelpImage texture)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.drawThickShape(new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight), thickness, texture);
    }

    /**
     * Draw round rectangle rectangle with thick border
     *
     * @param x         Up left corner X
     * @param y         Up left corner Y
     * @param width     Width
     * @param height    Height
     * @param arcWidth  Arc width
     * @param arcHeight Arc height
     * @param thickness Border thick
     * @param paint     Paint to use on border
     */
    public void drawThickRoundRectangle(
            final int x, final int y, final int width, final int height,
            final int arcWidth, final int arcHeight,
            final int thickness, final JHelpPaint paint)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.drawThickShape(new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight), thickness, paint);
    }

    /**
     * Extract a sub image from the image<br>
     * Note : If one of image is not in draw mode, all visible sprite (of this image) will be consider as a part of the
     * image
     *
     * @param x      X of upper left corner of the area to extract
     * @param y      Y of upper left corner of the area to extract
     * @param width  Area to extract width
     * @param height Area to extract height
     * @return Extracted image
     */
    public JHelpImage extractSubImage(int x, int y, int width, int height)
    {
        if (x < 0)
        {
            width += x;
            x = 0;
        }

        if (y < 0)
        {
            height += y;
            y = 0;
        }

        if ((x + width) > this.width)
        {
            width = this.width - x;
        }

        if ((y + height) > this.height)
        {
            height = this.height - y;
        }

        if ((width < 1) || (height < 1))
        {
            return JHelpImage.DUMMY;
        }

        final JHelpImage part = new JHelpImage(width, height);

        part.startDrawMode();
        part.drawImageOver(0, 0, this, x, y, width, height);
        part.endDrawMode();

        return part;
    }

    /**
     * Fill image with texture on take count original alpha, but replace other colors part
     *
     * @param texture Texture to fill
     */
    public void fillRespectAlpha(final JHelpImage texture)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        final int textureWidth = texture.width;
        int       lineTexture;
        int       pix          = 0;
        int       color;

        for (int y = 0, yTexture = 0; y < this.height; y++, yTexture = (yTexture + 1) % texture.height)
        {
            lineTexture = yTexture * textureWidth;

            for (int x = 0, xTexture = 0; x < this.width; x++, xTexture = (xTexture + 1) % textureWidth)
            {
                color = texture.pixels[lineTexture + xTexture];
                this.pixels[pix] = ((((this.pixels[pix] >>> 24) * (color >>> 24)) >> 8) << 24) | (color & 0x00FFFFFF);
                pix++;
            }
        }
    }

    /**
     * Draw shape with thick border
     *
     * @param shape     Shape to draw
     * @param thickness Border thick
     * @param paint     Paint to use on border
     */
    public void drawThickShape(final Shape shape, final int thickness, final JHelpPaint paint)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if (thickness < 1)
        {
            return;
        }

        final PathIterator pathIterator = shape.getPathIterator(ConstantsGUI.AFFINE_TRANSFORM, ConstantsGUI.FLATNESS);

        final double[] info   = new double[6];
        int            x      = 0;
        int            y      = 0;
        int            xStart = 0;
        int            yStart = 0;
        int            xx, yy;

        while (!pathIterator.isDone())
        {
            switch (pathIterator.currentSegment(info))
            {
                case PathIterator.SEG_MOVETO:
                    xStart = x = (int) Math.round(info[0]);
                    yStart = y = (int) Math.round(info[1]);

                    break;
                case PathIterator.SEG_LINETO:
                    xx = (int) Math.round(info[0]);
                    yy = (int) Math.round(info[1]);

                    this.drawThickLine(x, y, xx, yy, thickness, paint);

                    x = xx;
                    y = yy;

                    break;
                case PathIterator.SEG_CLOSE:
                    this.drawThickLine(x, y, xStart, yStart, thickness, paint);

                    x = xStart;
                    y = yStart;

                    break;
            }

            pathIterator.next();
        }
    }

    /**
     * Draw a vertical line<br>
     * MUST be in draw mode
     *
     * @param x          X
     * @param y1         Start Y
     * @param y2         End Y
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void drawVerticalLine(final int x, final int y1, final int y2, final int color, final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if ((x < this.clip.xMin) || (x > this.clip.xMax))
        {
            return;
        }

        final int yMin = Math.max(this.clip.yMin, Math.min(y1, y2));
        final int yMax = Math.min(this.clip.yMax, Math.max(y1, y2));

        if ((yMin > yMax) || (yMin > this.clip.yMax) || (yMax < this.clip.yMin))
        {
            return;
        }

        final int start = (yMin * this.width) + x;
        final int end   = (yMax * this.width) + x;

        if (start > end)
        {
            return;
        }

        final int alpha = (color >> 24) & 0xFF;

        if ((alpha == 0) && (doAlphaMix))
        {
            return;
        }

        if ((alpha == 255) || (!doAlphaMix))
        {
            for (int pix = start; pix <= end; pix += this.width)
            {
                this.pixels[pix] = color;
            }

            return;
        }

        final int ahpla = 256 - alpha;
        final int red   = ((color >> 16) & 0xFF) * alpha;
        final int green = ((color >> 8) & 0xFF) * alpha;
        final int blue  = (color & 0xFF) * alpha;
        int       col;

        for (int pix = start; pix <= end; pix += this.width)
        {
            col = this.pixels[pix];

            this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                               (((red + (((col >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                               (((green + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                               ((blue + ((col & 0xFF) * ahpla)) >> 8);
        }
    }

    /**
     * Fill a shape<br>
     * MUST be in draw mode
     *
     * @param shape Shape to fill
     * @param color Color to use
     */
    public void fillShape(final Shape shape, final int color)
    {
        this.fillShape(shape, color, true);
    }

    /**
     * Fill a shape<br>
     * MUST be in draw mode
     *
     * @param shape      Shape to fill
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void fillShape(final Shape shape, final int color, final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        final Rectangle rectangle = shape.getBounds();

        final int x      = rectangle.x;
        final int y      = rectangle.y;
        final int width  = rectangle.width;
        final int height = rectangle.height;

        if ((width <= 0) || (height <= 0))
        {
            return;
        }

        final int x2 = (x + width) - 1;
        final int y2 = (y + height) - 1;

        final int startX = Math.max(this.clip.xMin, x);
        final int endX   = Math.min(this.clip.xMax, x2);
        final int startY = Math.max(this.clip.yMin, y);
        final int endY   = Math.min(this.clip.yMax, y2);

        if ((startX > endX) || (startY > endY))
        {
            return;
        }

        final int alpha = (color >> 24) & 0xFF;

        if ((alpha == 0) && (doAlphaMix))
        {
            return;
        }

        int line = startX + (startY * this.width);
        int pix;

        if ((alpha == 255) || (!doAlphaMix))
        {
            for (int yy = startY; yy <= endY; yy++)
            {
                pix = line;

                for (int xx = startX; xx <= endX; xx++)
                {
                    if (shape.contains(xx, yy))
                    {
                        this.pixels[pix] = color;
                    }

                    pix++;
                }

                line += this.width;
            }

            return;
        }

        final int ahpla = 256 - alpha;
        final int red   = ((color >> 16) & 0xFF) * alpha;
        final int green = ((color >> 8) & 0xFF) * alpha;
        final int blue  = (color & 0xFF) * alpha;
        int       col;

        for (int yy = startY; yy <= endY; yy++)
        {
            pix = line;

            for (int xx = startX; xx <= endX; xx++)
            {
                if (shape.contains(xx, yy))
                {
                    col = this.pixels[pix];

                    this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                                       (((red + (((col >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                                       (((green + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                                       ((blue + ((col & 0xFF) * ahpla)) >> 8);
                }

                pix++;
            }

            line += this.width;
        }
    }

    /**
     * Filter on using a palette color<br>
     * MUST be on draw mode
     *
     * @param index   Palette color indes
     * @param colorOK Color if match
     * @param colorKO Color if not match
     */
    public void filterPalette(final int index, final int colorOK, final int colorKO)
    {
        this.filterOn(JHelpImage.PALETTE[index % JHelpImage.PALETTE_SIZE], 0x10, colorOK, colorKO);
    }

    /**
     * Fill pixels of image withc color.<br>
     * The start point indicates the color to fill, and all neighboards pixels with color distance of precision will be
     * colored
     * <br>
     * Must be in draw mode
     *
     * @param x         Start X
     * @param y         Start Y
     * @param color     Color to use
     * @param precision Precision for color difference
     * @param alphaMix  Indicates if alpha mix or replace
     */
    public void fillColor(final int x, final int y, final int color, int precision, final boolean alphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if ((x < 0) || (x > this.width) || (y < 0) || (y >= this.height))
        {
            return;
        }

        final int alpha = (color >> 24) & 0xFF;
        if ((alpha == 0) && (alphaMix))
        {
            return;
        }

        precision = Math.max(0, precision);
        final int start = this.pixels[x + (y * this.width)];
        if (JHelpImage.distanceColor(start, color) <= precision)
        {
            return;
        }

        if ((alpha == 255) || (!alphaMix))
        {
            final Stack<Point> stack = new Stack<>();
            stack.push(new Point(x, y));
            Point point;

            while (!stack.isEmpty())
            {
                point = stack.pop();
                this.pixels[point.x + (point.y * this.width)] = color;

                if ((point.x > 0)
                    && (JHelpImage.distanceColor(start,
                                                 this.pixels[(point.x - 1) + (point.y * this.width)]) <= precision))
                {
                    stack.push(new Point(point.x - 1, point.y));
                }

                if ((point.x < (this.width - 1))
                    && (JHelpImage.distanceColor(start,
                                                 this.pixels[point.x + 1 + (point.y * this.width)]) <= precision))
                {
                    stack.push(new Point(point.x + 1, point.y));
                }

                if ((point.y > 0)
                    && (JHelpImage.distanceColor(start,
                                                 this.pixels[point.x + ((point.y - 1) * this.width)]) <= precision))
                {
                    stack.push(new Point(point.x, point.y - 1));
                }

                if ((point.y < (this.height - 1))
                    && (JHelpImage.distanceColor(start,
                                                 this.pixels[point.x + ((point.y + 1) * this.width)]) <= precision))
                {
                    stack.push(new Point(point.x, point.y + 1));
                }
            }

            return;
        }

        final Stack<Point> stack = new Stack<>();
        stack.push(new Point(x, y));
        Point     point;
        final int ahpla = 256 - alpha;
        final int red   = ((color >> 16) & 0xFF) * alpha;
        final int green = ((color >> 8) & 0xFF) * alpha;
        final int blue  = (color & 0xFF) * alpha;
        int       col, pix;

        while (!stack.isEmpty())
        {
            point = stack.pop();

            pix = point.x + (point.y * this.width);
            col = this.pixels[pix];
            this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                               (((red + (((col >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                               (((green + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                               ((blue + ((col & 0xFF) * ahpla)) >> 8);

            if ((point.x > 0)
                && (JHelpImage.distanceColor(start,
                                             this.pixels[(point.x - 1) + (point.y * this.width)]) <= precision))
            {
                stack.push(new Point(point.x - 1, point.y));
            }

            if ((point.x < (this.width - 1))
                && (JHelpImage.distanceColor(start,
                                             this.pixels[point.x + 1 + (point.y * this.width)]) <= precision))
            {
                stack.push(new Point(point.x + 1, point.y));
            }

            if ((point.y > 0)
                && (JHelpImage.distanceColor(start,
                                             this.pixels[point.x + ((point.y - 1) * this.width)]) <= precision))
            {
                stack.push(new Point(point.x, point.y - 1));
            }

            if ((point.y < (this.height - 1))
                && (JHelpImage.distanceColor(start,
                                             this.pixels[point.x + ((point.y + 1) * this.width)]) <= precision))
            {
                stack.push(new Point(point.x, point.y + 1));
            }
        }

    }

    /**
     * Fill an ellipse<br>
     * MUST be in draw mode
     *
     * @param x      X of bounds top-left
     * @param y      Y of bounds top-left
     * @param width  Ellipse width
     * @param height Ellipse height
     * @param color  Color to use
     */
    public void fillEllipse(final int x, final int y, final int width, final int height, final int color)
    {
        this.fillEllipse(x, y, width, height, color, true);
    }

    /**
     * Fill an ellipse<br>
     * MUST be in draw mode
     *
     * @param x          X of bounds top-left
     * @param y          Y of bounds top-left
     * @param width      Ellipse width
     * @param height     Ellipse height
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void fillEllipse(
            final int x, final int y, final int width, final int height,
            final int color, final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.fillShape(new Ellipse2D.Double(x, y, width, height), color, doAlphaMix);
    }

    /**
     * Fill ellipse with a texture<br>
     * Note : if the texture is not in draw moe, all of it's visible sprte will be condider like a part of he texture<br>
     * MUST be in draw mode
     *
     * @param x       X of bounds top-left
     * @param y       Y of bounds top-left
     * @param width   Ellipse width
     * @param height  Ellipse height
     * @param texture Texture to use
     */
    public void fillEllipse(final int x, final int y, final int width, final int height, final JHelpImage texture)
    {
        this.fillEllipse(x, y, width, height, texture, true);
    }

    /**
     * Fill ellipse with a texture<br>
     * Note : if the texture is not in draw moe, all of it's visible sprte will be condider like a part of he texture<br>
     * MUST be in draw mode
     *
     * @param x          X of bounds top-left
     * @param y          Y of bounds top-left
     * @param width      Ellipse width
     * @param height     Ellipse height
     * @param texture    Texture to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void fillEllipse(
            final int x, final int y, final int width, final int height,
            final JHelpImage texture, final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.fillShape(new Ellipse2D.Double(x, y, width, height), texture, doAlphaMix);
    }

    /**
     * Fill an ellipse<br>
     * MUST be in draw mode
     *
     * @param x      X
     * @param y      Y
     * @param width  Width
     * @param height Height
     * @param paint  Paint to use
     */
    public void fillEllipse(final int x, final int y, final int width, final int height, final JHelpPaint paint)
    {
        this.fillEllipse(x, y, width, height, paint, true);
    }

    /**
     * Fill an ellipse<br>
     * MUST be in draw mode
     *
     * @param x          X
     * @param y          Y
     * @param width      Width
     * @param height     Height
     * @param paint      Paint to use
     * @param doAlphaMix Indicates if do alpha mixing or just overwrite
     */
    public void fillEllipse(
            final int x, final int y, final int width, final int height, final JHelpPaint paint,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.fillShape(new Ellipse2D.Double(x, y, width, height), paint, doAlphaMix);
    }

    /**
     * Fill a polygon
     *
     * @param xs      X list
     * @param offsetX X list start offset
     * @param ys      Y list
     * @param offsetY Y list start offset
     * @param length  Number of points
     * @param color   Color to use
     */
    public void fillPolygon(
            final int[] xs, final int offsetX, final int[] ys, final int offsetY,
            final int length, final int color)
    {
        this.fillPolygon(xs, offsetX, ys, offsetY, length, color, true);
    }

    /**
     * Fill a polygon<br>
     * MUST be in draw mode
     *
     * @param xs         X list
     * @param offsetX    X list start offset
     * @param ys         Y list
     * @param offsetY    Y list start offset
     * @param length     Number of points
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void fillPolygon(
            final int[] xs, int offsetX, final int[] ys, int offsetY,
            int length, final int color, final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if (offsetX < 0)
        {
            length += offsetX;

            offsetX = 0;
        }

        if (offsetY < 0)
        {
            length += offsetY;

            offsetY = 0;
        }

        length = Math2.minimum(length, xs.length - offsetX, ys.length - offsetY);

        if (length < 3)
        {
            return;
        }

        final Polygon polygon = new Polygon(Arrays.copyOfRange(xs, offsetX, offsetX + length), //
                                            Arrays.copyOfRange(ys, offsetY, offsetY + length), length);

        this.fillShape(polygon, color, doAlphaMix);
    }

    /**
     * Fill a polygon<br>
     * Note : if the texture is not in draw moe, all of it's visible sprte will be condider like a part of he texture<br>
     * MUST be in draw mode
     *
     * @param xs      X list
     * @param offsetX X list start offset
     * @param ys      Y list
     * @param offsetY Y list offset
     * @param length  Number of points
     * @param texture Texture to use
     */
    public void fillPolygon(
            final int[] xs, final int offsetX, final int[] ys, final int offsetY, final int length,
            final JHelpImage texture)
    {
        this.fillPolygon(xs, offsetX, ys, offsetY, length, texture, true);
    }

    /**
     * Fill a polygon<br>
     * Note : if the texture is not in draw moe, all of it's visible sprte will be condider like a part of he texture<br>
     * MUST be in draw mode
     *
     * @param xs         X list
     * @param offsetX    X list start offset
     * @param ys         Y list
     * @param offsetY    Y list offset
     * @param length     Number of points
     * @param texture    Texture to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void fillPolygon(
            final int[] xs, int offsetX, final int[] ys, int offsetY, int length,
            final JHelpImage texture, final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if (offsetX < 0)
        {
            length += offsetX;

            offsetX = 0;
        }

        if (offsetY < 0)
        {
            length += offsetY;

            offsetY = 0;
        }

        length = Math2.minimum(length, xs.length - offsetX, ys.length - offsetY);

        if (length < 3)
        {
            return;
        }

        final Polygon polygon = new Polygon(Arrays.copyOfRange(xs, offsetX, offsetX + length), //
                                            Arrays.copyOfRange(ys, offsetY, offsetY + length), length);

        this.fillShape(polygon, texture, doAlphaMix);
    }

    /**
     * Fill a polygon<br>
     * MUST be in draw mode
     *
     * @param xs      X coordinates
     * @param offsetX Start read offset of xs
     * @param ys      Y coordinates
     * @param offsetY Start read offset of ys
     * @param length  Number of point
     * @param paint   Paint to use
     */
    public void fillPolygon(
            final int[] xs, final int offsetX, final int[] ys, final int offsetY, final int length,
            final JHelpPaint paint)
    {
        this.fillPolygon(xs, offsetX, ys, offsetY, length, paint, true);
    }

    /**
     * Fill a polygon<br>
     * MUST be in draw mode
     *
     * @param xs         X coordinates
     * @param offsetX    Start read offset of xs
     * @param ys         Y coordinates
     * @param offsetY    Start read offset of ys
     * @param length     Number of point
     * @param paint      Paint to use
     * @param doAlphaMix Indicates if do alpha mixing or just overwrite
     */
    public void fillPolygon(
            final int[] xs, int offsetX, final int[] ys, int offsetY, int length, final JHelpPaint paint,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if (offsetX < 0)
        {
            length += offsetX;

            offsetX = 0;
        }

        if (offsetY < 0)
        {
            length += offsetY;

            offsetY = 0;
        }

        length = Math2.minimum(length, xs.length - offsetX, ys.length - offsetY);

        if (length < 3)
        {
            return;
        }

        final Polygon polygon = new Polygon(Arrays.copyOfRange(xs, offsetX, offsetX + length), //
                                            Arrays.copyOfRange(ys, offsetY, offsetY + length), length);

        this.fillShape(polygon, paint, doAlphaMix);
    }

    /**
     * Fill a polygon<br>
     * MUST be in draw mode
     *
     * @param xs    X list
     * @param ys    Y list
     * @param color Color to use
     */
    public void fillPolygon(final int[] xs, final int[] ys, final int color)
    {
        this.fillPolygon(xs, ys, color, true);
    }

    /**
     * Fill a polygon<br>
     * MUST be in draw mode
     *
     * @param xs         X list
     * @param ys         Y list
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void fillPolygon(final int[] xs, final int[] ys, final int color, final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.fillPolygon(xs, 0, ys, 0, Math.min(xs.length, ys.length), color, doAlphaMix);
    }

    /**
     * Fill a polygon<br>
     * Note : if the texture is not in draw moe, all of it's visible sprte will be condider like a part of he texture<br>
     * MUST be in draw mode
     *
     * @param xs      X list
     * @param ys      Y list
     * @param texture Texture to use
     */
    public void fillPolygon(final int[] xs, final int[] ys, final JHelpImage texture)
    {
        this.fillPolygon(xs, ys, texture, true);
    }

    /**
     * Fill a polygon<br>
     * Note : if the texture is not in draw moe, all of it's visible sprte will be condider like a part of he texture<br>
     * MUST be in draw mode
     *
     * @param xs         X list
     * @param ys         Y list
     * @param texture    Texture to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void fillPolygon(final int[] xs, final int[] ys, final JHelpImage texture, final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.fillPolygon(xs, 0, ys, 0, Math.min(xs.length, ys.length), texture, doAlphaMix);
    }

    /**
     * Fill a polygon<br>
     * MUST be in draw mode
     *
     * @param xs    X coordinates
     * @param ys    Y coordinates
     * @param paint Paint to use
     */
    public void fillPolygon(final int[] xs, final int[] ys, final JHelpPaint paint)
    {
        this.fillPolygon(xs, ys, paint, true);
    }

    /**
     * Fill a polygon<br>
     * MUST be in draw mode
     *
     * @param xs         X coordinates
     * @param ys         Y coordinates
     * @param paint      Paint to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void fillPolygon(final int[] xs, final int[] ys, final JHelpPaint paint, final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.fillPolygon(xs, 0, ys, 0, Math.min(xs.length, ys.length), paint, doAlphaMix);
    }

    /**
     * Fill a rectangle<br>
     * MUST be in draw mode
     *
     * @param x      X top-left
     * @param y      U top-left
     * @param width  Rectangle width
     * @param height Rectangle height
     * @param color  Color to use
     */
    public void fillRectangle(final int x, final int y, final int width, final int height, final int color)
    {
        this.fillRectangle(x, y, width, height, color, true);
    }

    /**
     * Fill a rectangle<br>
     * MUST be in draw mode
     *
     * @param x          X top-left
     * @param y          U top-left
     * @param width      Rectangle width
     * @param height     Rectangle height
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void fillRectangle(
            final int x, final int y, final int width, final int height, final int color,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if ((width <= 0) || (height <= 0))
        {
            return;
        }

        final int x2 = (x + width) - 1;
        final int y2 = (y + height) - 1;

        final int startX = Math2.maximum(this.clip.xMin, x, 0);
        final int endX   = Math2.minimum(this.clip.xMax, x2, this.width - 1);
        final int startY = Math2.maximum(this.clip.yMin, y, 0);
        final int endY   = Math2.minimum(this.clip.yMax, y2, this.height - 1);

        if ((startX > endX) || (startY > endY))
        {
            return;
        }

        final int alpha = (color >> 24) & 0xFF;

        if ((alpha == 0) && (doAlphaMix))
        {
            return;
        }

        int line = startX + (startY * this.width);
        int pix;

        if ((alpha == 255) || (!doAlphaMix))
        {
            for (int yy = startY; yy <= endY; yy++)
            {
                pix = line;

                for (int xx = startX; xx <= endX; xx++)
                {
                    this.pixels[pix] = color;

                    pix++;
                }

                line += this.width;
            }

            return;
        }

        final int ahpla = 256 - alpha;
        final int red   = ((color >> 16) & 0xFF) * alpha;
        final int green = ((color >> 8) & 0xFF) * alpha;
        final int blue  = (color & 0xFF) * alpha;
        int       col;

        for (int yy = startY; yy <= endY; yy++)
        {
            pix = line;

            for (int xx = startX; xx < endX; xx++)
            {
                col = this.pixels[pix];

                this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                                   (((red + (((col >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                                   (((green + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                                   ((blue + ((col & 0xFF) * ahpla)) >> 8);

                pix++;
            }

            line += this.width;
        }
    }

    /**
     * Fill a rectangle<br>
     * Note : if the texture is not in draw moe, all of it's visible sprte will be condider like a part of he texture<br>
     * MUST be in draw mode
     *
     * @param x       X top-left
     * @param y       Y top-left
     * @param width   Rectangle width
     * @param height  Rectangle height
     * @param texture Texture to use
     */
    public void fillRectangle(final int x, final int y, final int width, final int height, final JHelpImage texture)
    {
        this.fillRectangle(x, y, width, height, texture, true);
    }

    /**
     * Fill a rectangle<br>
     * Note : if the texture is not in draw moe, all of it's visible sprte will be condider like a part of he texture<br>
     * MUST be in draw mode
     *
     * @param x          X top-left
     * @param y          Y top-left
     * @param width      Rectangle width
     * @param height     Rectangle height
     * @param texture    Texture to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void fillRectangle(
            final int x, final int y, final int width, final int height, final JHelpImage texture,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if ((width <= 0) || (height <= 0))
        {
            return;
        }

        final int x2 = (x + width) - 1;
        final int y2 = (y + height) - 1;

        final int startX = Math.max(this.clip.xMin, x);
        final int endX   = Math.min(this.clip.xMax, x2);
        final int startY = Math.max(this.clip.yMin, y);
        final int endY   = Math.min(this.clip.yMax, y2);

        if ((startX > endX) || (startY > endY))
        {
            return;
        }

        int line = startX + (startY * this.width);
        int pix, color;

        final int startXTexture = (startX - x) % texture.width;
        int       yTexture      = (startY - y) % texture.height;
        int       pixTexture, colorTexture;

        int alpha, ahpla;

        for (int yy = startY; yy <= endY; yy++, yTexture = (yTexture + 1) % texture.height)
        {
            pixTexture = yTexture * texture.width;
            pix = line;

            for (int xx = startX, xTexture = startXTexture; xx < endX; xx++, xTexture = (xTexture + 1) % texture.width)
            {
                colorTexture = texture.pixels[pixTexture + xTexture];

                alpha = (colorTexture >> 24) & 0xFF;

                if ((alpha == 255) || (!doAlphaMix))
                {
                    this.pixels[pix] = colorTexture;
                }
                else if (alpha > 0)
                {
                    ahpla = 256 - alpha;

                    color = this.pixels[pix];

                    this.pixels[pix] = (Math.min(255, alpha + ((color >> 24) & 0xFF)) << 24) | //
                                       ((((((colorTexture >> 16) & 0xFF) * alpha) + (((color >> 16) & 0xFF) * ahpla)) >>
                                         8) << 16) |
                                       ((((((colorTexture >> 8) & 0xFF) * alpha) + (((color >> 8) & 0xFF) * ahpla)) >>
                                         8) << 8) | //
                                       ((((colorTexture & 0xFF) * alpha) + ((color & 0xFF) * ahpla)) >> 8);
                }

                pix++;
            }

            line += this.width;
        }
    }

    /**
     * Fill a rectangle<br>
     * MUST be in draw mode
     *
     * @param x      X
     * @param y      Y
     * @param width  Width
     * @param height Height
     * @param paint  Paint to use
     */
    public void fillRectangle(final int x, final int y, final int width, final int height, final JHelpPaint paint)
    {
        this.fillRectangle(x, y, width, height, paint, true);
    }

    /**
     * Fill a rectangle<br>
     * MUST be in draw mode
     *
     * @param x          X
     * @param y          Y
     * @param width      Width
     * @param height     Height
     * @param paint      Paint to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void fillRectangle(
            final int x, final int y, final int width, final int height, final JHelpPaint paint,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if ((width <= 0) || (height <= 0))
        {
            return;
        }

        final int x2 = (x + width) - 1;
        final int y2 = (y + height) - 1;

        final int startX = Math.max(this.clip.xMin, x);
        final int endX   = Math.min(this.clip.xMax, x2);
        final int startY = Math.max(this.clip.yMin, y);
        final int endY   = Math.min(this.clip.yMax, y2);

        if ((startX > endX) || (startY > endY))
        {
            return;
        }

        paint.initializePaint(width, height);

        int line = startX + (startY * this.width);
        int pix, color;

        final int startXPaint = startX - x;
        int       yPaint      = startY - y;
        int       colorPaint;

        int alpha, ahpla;

        for (int yy = startY; yy <= endY; yy++, yPaint++)
        {
            pix = line;

            for (int xx = startX, xPaint = startXPaint; xx <= endX; xx++, xPaint++)
            {
                colorPaint = paint.obtainColor(xPaint, yPaint);

                alpha = (colorPaint >> 24) & 0xFF;

                if ((alpha == 255) || (!doAlphaMix))
                {
                    this.pixels[pix] = colorPaint;
                }
                else if (alpha > 0)
                {
                    ahpla = 256 - alpha;

                    color = this.pixels[pix];

                    this.pixels[pix] = (Math.min(255, alpha + ((color >> 24) & 0xFF)) << 24) | //
                                       ((((((colorPaint >> 16) & 0xFF) * alpha) + (((color >> 16) & 0xFF) * ahpla)) >>
                                         8) << 16) | //
                                       ((((((colorPaint >> 8) & 0xFF) * alpha) + (((color >> 8) & 0xFF) * ahpla)) >>
                                         8) << 8) | //
                                       ((((colorPaint & 0xFF) * alpha) + ((color & 0xFF) * ahpla)) >> 8);
                }

                pix++;
            }

            line += this.width;
        }
    }

    /**
     * Fill rectangle and invert colors
     *
     * @param x      Up left corner X
     * @param y      Up left corner Y
     * @param width  Rectangle width
     * @param height Rectangle height
     */
    public void fillRectangleInverseColor(final int x, final int y, final int width, final int height)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if ((width <= 0) || (height <= 0))
        {
            return;
        }

        final int x2 = (x + width) - 1;
        final int y2 = (y + height) - 1;

        final int startX = Math2.maximum(this.clip.xMin, x, 0);
        final int endX   = Math2.minimum(this.clip.xMax, x2, this.width - 1);
        final int startY = Math2.maximum(this.clip.yMin, y, 0);
        final int endY   = Math2.minimum(this.clip.yMax, y2, this.height - 1);

        if ((startX > endX) || (startY > endY))
        {
            return;
        }

        int line = startX + (startY * this.width);
        int pix, color;

        for (int yy = startY; yy <= endY; yy++)
        {
            pix = line;

            for (int xx = startX; xx <= endX; xx++)
            {
                color = this.pixels[pix];
                this.pixels[pix] = (color & 0xFF000000) | ((~color) & 0x00FFFFFF);

                pix++;
            }

            line += this.width;
        }
    }

    /**
     * Fill a rectangle with an image.<br>
     * The image is scaled to fit rectangle size<br>
     * Note : if the texture is not in draw moe, all of it's visible sprte will be condider like a part of he texture<br>
     * MUST be in draw mode
     *
     * @param x       X
     * @param y       Y
     * @param width   Width
     * @param height  Height
     * @param texture Image to draw
     */
    public void fillRectangleScale(
            final int x, final int y, final int width, final int height, final JHelpImage texture)
    {
        this.fillRectangleScale(x, y, width, height, texture, true);
    }

    /**
     * Fill a rectangle with an image.<br>
     * The image is scaled to fit rectangle size<br>
     * Note : if the texture is not in draw mode, all of it's visible sprite will be consider like a part of he texture<br>
     * MUST be in draw mode
     *
     * @param x          X
     * @param y          Y
     * @param width      Width
     * @param height     Height
     * @param texture    Image to draw
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void fillRectangleScale(
            final int x, final int y, final int width, final int height, final JHelpImage texture,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if ((width <= 0) || (height <= 0))
        {
            return;
        }

        final int x2 = (x + width) - 1;
        final int y2 = (y + height) - 1;

        final int startX = Math.max(this.clip.xMin, x);
        final int endX   = Math.min(this.clip.xMax, x2);
        final int startY = Math.max(this.clip.yMin, y);
        final int endY   = Math.min(this.clip.yMax, y2);

        if ((startX > endX) || (startY > endY))
        {
            return;
        }

        int line = startX + (startY * this.width);
        int pix, color;

        final int startXT  = startX - x;
        int       yt       = startY - y;
        int       yTexture = (yt * texture.height) / height;
        int       pixTexture, colorTexture;

        int alpha, ahpla;

        for (int yy = startY; yy <= endY; yy++, yt++, yTexture = (yt * texture.height) / height)
        {
            pixTexture = yTexture * texture.width;
            pix = line;

            for (int xx = startX, xt = startXT, xTexture = 0; xx < endX;
                 xx++, xt++, xTexture = (xt * texture.width) / width)
            {
                colorTexture = texture.pixels[pixTexture + xTexture];

                alpha = (colorTexture >> 24) & 0xFF;

                if ((alpha == 255) || (!doAlphaMix))
                {
                    this.pixels[pix] = colorTexture;
                }
                else if (alpha > 0)
                {
                    ahpla = 256 - alpha;

                    color = this.pixels[pix];

                    this.pixels[pix] = (Math.min(255, alpha + ((color >> 24) & 0xFF)) << 24) | //
                                       ((((((colorTexture >> 16) & 0xFF) * alpha) + (((color >> 16) & 0xFF) * ahpla)) >>
                                         8) << 16) |
                                       ((((((colorTexture >> 8) & 0xFF) * alpha) + (((color >> 8) & 0xFF) * ahpla)) >>
                                         8) << 8) | //
                                       ((((colorTexture & 0xFF) * alpha) + ((color & 0xFF) * ahpla)) >> 8);
                }

                pix++;
            }

            line += this.width;
        }
    }

    /**
     * Fill a rectangle with an image.<br>
     * The image is scaled to fit rectangle size.<br>
     * The result is nicer than {@link #fillRectangleScale(int, int, int, int, JHelpImage)} but it is slower and take
     * temporary
     * more memory<br>
     * Note : if the texture is not in draw moe, all of it's visible sprte will be condider like a part of he texture<br>
     * MUST be in draw mode
     *
     * @param x       X
     * @param y       Y
     * @param width   Width
     * @param height  Height
     * @param texture Image to draw
     */
    public void fillRectangleScaleBetter(
            final int x, final int y, final int width, final int height,
            final JHelpImage texture)
    {
        this.fillRectangleScaleBetter(x, y, width, height, texture, true);
    }

    /**
     * Fill a rectangle with an image.<br>
     * The image is scaled to fit rectangle size.<br>
     * The result is nicer than {@link #fillRectangleScale(int, int, int, int, JHelpImage, boolean)} but it is slower
     * and take temporary more memory<br>
     * Note : if the texture is not in draw mode, all of it's visible sprite will be consider like a part of the texture<br>
     * MUST be in draw mode
     *
     * @param x          X
     * @param y          Y
     * @param width      Width
     * @param height     Height
     * @param texture    Image to draw
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void fillRectangleScaleBetter(
            final int x, final int y, final int width, final int height,
            final JHelpImage texture, final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if ((width <= 0) || (height <= 0))
        {
            return;
        }

        final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D    graphics2d    = bufferedImage.createGraphics();

        graphics2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                                    RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        graphics2d.drawImage(texture.getImage(), 0, 0, width, height, null);

        int[] pixels = new int[width * height];
        pixels = bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);

        final JHelpImage image = new JHelpImage(width, height, pixels);

        bufferedImage.flush();

        this.fillRectangle(x, y, width, height, image, doAlphaMix);
    }

    /**
     * Image height
     *
     * @return Image height
     */
    public int getHeight()
    {
        return this.height;
    }

    /**
     * Fill the image with a color on respect the alpha.<br>
     * That is to say the given color alpha is no use, but original image alpha for given a pixel
     *
     * @param color Color for fill
     */
    public void fillRespectAlpha(final int color)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        final int pure = color & 0x00FFFFFF;

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            this.pixels[pix] = (this.pixels[pix] & 0xFF000000) | pure;
        }
    }

    /**
     * Fill image with pain on respect original alpha, but replace other color parts
     *
     * @param paint Paint to fill with
     */
    public void fillRespectAlpha(final JHelpPaint paint)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        paint.initializePaint(this.width, this.height);
        int pix = 0;
        int color;

        for (int y = 0; y < this.height; y++)
        {
            for (int x = 0; x < this.width; x++)
            {
                color = paint.obtainColor(x, y);
                this.pixels[pix] = ((((this.pixels[pix] >>> 24) * (color >>> 24)) >> 8) << 24) | (color & 0x00FFFFFF);
                pix++;
            }
        }
    }

    /**
     * Fill a round rectangle<br>
     * MUST be in draw mode
     *
     * @param x         X
     * @param y         Y
     * @param width     Width
     * @param height    Height
     * @param arcWidth  Arc width
     * @param arcHeight Arc height
     * @param color     Color to use
     */
    public void fillRoundRectangle(
            final int x, final int y, final int width, final int height, final int arcWidth,
            final int arcHeight, final int color)
    {
        this.fillRoundRectangle(x, y, width, height, arcWidth, arcHeight, color, true);
    }

    /**
     * Fill a round rectangle<br>
     * MUST be in draw mode
     *
     * @param x          X
     * @param y          Y
     * @param width      Width
     * @param height     Height
     * @param arcWidth   Arc width
     * @param arcHeight  Arc height
     * @param color      Color to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void fillRoundRectangle(
            final int x, final int y, final int width, final int height, final int arcWidth,
            final int arcHeight, final int color,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.fillShape(new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight), color, doAlphaMix);
    }

    /**
     * Fill a round rectangle<br>
     * Note : if the texture is not in draw mode, all of it's visible sprite will be consider like a part of he texture<br>
     * MUST be in draw mode
     *
     * @param x         X
     * @param y         Y
     * @param width     Width
     * @param height    Height
     * @param arcWidth  Arc width
     * @param arcHeight Arc height
     * @param texture   Texture to use
     */
    public void fillRoundRectangle(
            final int x, final int y, final int width, final int height,
            final int arcWidth, final int arcHeight,
            final JHelpImage texture)
    {
        this.fillRoundRectangle(x, y, width, height, arcWidth, arcHeight, texture, true);
    }

    /**
     * Fill a round rectangle<br>
     * Note : if the texture is not in draw mode, all of it's visible sprite will be consider like a part of he texture<br>
     * MUST be in draw mode
     *
     * @param x          X
     * @param y          Y
     * @param width      Width
     * @param height     Height
     * @param arcWidth   Arc width
     * @param arcHeight  Arc height
     * @param texture    Texture to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void fillRoundRectangle(
            final int x, final int y, final int width, final int height,
            final int arcWidth, final int arcHeight,
            final JHelpImage texture, final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.fillShape(new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight), texture, doAlphaMix);
    }

    /**
     * Fill a round rectangle<br>
     * MUST be in draw mode
     *
     * @param x         X
     * @param y         Y
     * @param width     Width
     * @param height    Height
     * @param arcWidth  Arc width
     * @param arcHeight Arc height
     * @param paint     Paint to use
     */
    public void fillRoundRectangle(
            final int x, final int y, final int width, final int height,
            final int arcWidth, final int arcHeight, final JHelpPaint paint)
    {
        this.fillRoundRectangle(x, y, width, height, arcWidth, arcHeight, paint, true);
    }

    /**
     * Fill a round rectangle<br>
     * MUST be in draw mode
     *
     * @param x          X
     * @param y          Y
     * @param width      Width
     * @param height     Height
     * @param arcWidth   Arc width
     * @param arcHeight  Arc height
     * @param paint      Paint to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void fillRoundRectangle(
            final int x, final int y, final int width, final int height,
            final int arcWidth, final int arcHeight, final JHelpPaint paint,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        this.fillShape(new RoundRectangle2D.Double(x, y, width, height, arcWidth, arcHeight), paint, doAlphaMix);
    }

    /**
     * Raster image type <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Raster image type
     * @see jhelp.util.image.raster.RasterImage#getImageType()
     */
    @Override
    public RasterImageType getImageType()
    {
        return RasterImageType.JHELP_IMAGE;
    }

    /**
     * Image width
     *
     * @return Image width
     */
    public int getWidth()
    {
        return this.width;
    }

    /**
     * Fill a shape<br>
     * Note : if the texture is not in draw mode, all of it's visible sprite will be consider like a part of he texture<br>
     * MUST be in draw mode
     *
     * @param shape   Shape to fill
     * @param texture Texture to use
     */
    public void fillShape(final Shape shape, final JHelpImage texture)
    {
        this.fillShape(shape, texture, true);
    }

    /**
     * Fill a shape<br>
     * Note : if the texture is not in draw mode, all of it's visible sprite will be consider like a part of he texture<br>
     * MUST be in draw mode
     *
     * @param shape      Shape to fill
     * @param texture    Texture to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void fillShape(final Shape shape, final JHelpImage texture, final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        final Rectangle rectangle = shape.getBounds();

        final int x      = rectangle.x;
        final int y      = rectangle.y;
        final int width  = rectangle.width;
        final int height = rectangle.height;

        if ((width <= 0) || (height <= 0))
        {
            return;
        }

        final int x2 = (x + width) - 1;
        final int y2 = (y + height) - 1;

        final int startX = Math.max(this.clip.xMin, x);
        final int endX   = Math.min(this.clip.xMax, x2);
        final int startY = Math.max(this.clip.yMin, y);
        final int endY   = Math.min(this.clip.yMax, y2);

        if ((startX > endX) || (startY > endY))
        {
            return;
        }

        int line = startX + (startY * this.width);
        int pix, color;

        final int startTextureX = (startX - x) % texture.width;
        int       yTexture      = (startY - y) % texture.height;
        int       pixTexture, colorTexture;

        int alpha, ahpla;

        for (int yy = startY; yy <= endY; yy++, yTexture = (yTexture + 1) % texture.height)
        {
            pixTexture = yTexture * texture.width;
            pix = line;

            for (int xx = startX, xTexture = startTextureX; xx <= endX; xx++, xTexture = (xTexture + 1) % texture.width)
            {
                if (shape.contains(xx, yy))
                {
                    colorTexture = texture.pixels[pixTexture + xTexture];

                    alpha = (colorTexture >> 24) & 0xFF;

                    if ((alpha == 255) || (!doAlphaMix))
                    {
                        this.pixels[pix] = colorTexture;
                    }
                    else if (alpha > 0)
                    {
                        ahpla = 256 - alpha;

                        color = this.pixels[pix];

                        this.pixels[pix] = (Math.min(255, alpha + ((color >> 24) & 0xFF)) << 24) |
                                           ((((((colorTexture >> 16) & 0xFF) * alpha)
                                              + (((color >> 16) & 0xFF) * ahpla)) >> 8) << 16) |
                                           ((((((colorTexture >> 8) & 0xFF) * alpha) +
                                              (((color >> 8) & 0xFF) * ahpla)) >> 8) << 8) |
                                           ((((colorTexture & 0xFF) * alpha) + ((color & 0xFF) * ahpla)) >> 8);
                    }
                }

                pix++;
            }

            line += this.width;
        }
    }

    /**
     * Fill a shape<br>
     * MUST be in draw mode
     *
     * @param shape Shape to fill
     * @param paint Paint to use
     */
    public void fillShape(final Shape shape, final JHelpPaint paint)
    {
        this.fillShape(shape, paint, true);
    }

    /**
     * Fill a shape<br>
     * MUST be in draw mode
     *
     * @param shape      Shape to fill
     * @param paint      Paint to use
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     */
    public void fillShape(final Shape shape, final JHelpPaint paint, final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        final Rectangle rectangle = shape.getBounds();

        final int x      = rectangle.x;
        final int y      = rectangle.y;
        final int width  = rectangle.width;
        final int height = rectangle.height;

        if ((width <= 0) || (height <= 0))
        {
            return;
        }

        final int x2 = (x + width) - 1;
        final int y2 = (y + height) - 1;

        final int startX = Math.max(this.clip.xMin, x);
        final int endX   = Math.min(this.clip.xMax, x2);
        final int startY = Math.max(this.clip.yMin, y);
        final int endY   = Math.min(this.clip.yMax, y2);

        if ((startX > endX) || (startY > endY))
        {
            return;
        }

        paint.initializePaint(width, height);

        int line = startX + (startY * this.width);
        int pix, color;

        final int startXPaint = startX - x;
        int       yPaint      = startY - y;
        int       colorPaint;

        int alpha, ahpla;

        for (int yy = startY; yy <= endY; yy++, yPaint++)
        {
            pix = line;

            for (int xx = startX, xPaint = startXPaint; xx <= endX; xx++, xPaint++)
            {
                if (shape.contains(xx, yy))
                {
                    colorPaint = paint.obtainColor(xPaint, yPaint);

                    alpha = (colorPaint >> 24) & 0xFF;

                    if ((alpha == 255) || (!doAlphaMix))
                    {
                        this.pixels[pix] = colorPaint;
                    }
                    else if (alpha > 0)
                    {
                        ahpla = 256 - alpha;

                        color = this.pixels[pix];

                        this.pixels[pix] = (Math.min(255, alpha + ((color >> 24) & 0xFF)) << 24) | //
                                           ((((((colorPaint >> 16) & 0xFF) * alpha)
                                              + (((color >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                                           ((((((colorPaint >> 8) & 0xFF) * alpha) + (((color >> 8) & 0xFF) * ahpla)) >>
                                             8) << 8) | //
                                           ((((colorPaint & 0xFF) * alpha) + ((color & 0xFF) * ahpla)) >> 8);
                    }
                }

                pix++;
            }

            line += this.width;
        }
    }

    /**
     * Fill a string<br>
     * MUST be in draw mode
     *
     * @param x      X top-left
     * @param y      Y top-left
     * @param string String to draw
     * @param font   Font to use
     * @param color  Color for fill
     * @return Bounds where string just draw
     */
    public Rectangle fillString(final int x, final int y, final String string, final JHelpFont font, final int color)
    {
        return this.fillString(x, y, string, font, color, true);
    }

    /**
     * Fill a string<br>
     * MUST be in draw mode
     *
     * @param x          X top-left
     * @param y          Y top-left
     * @param string     String to draw
     * @param font       Font to use
     * @param color      Color for fill
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     * @return Bounds where string just draw
     */
    public Rectangle fillString(
            final int x, final int y, final String string, final JHelpFont font, final int color,
            final boolean doAlphaMix)
    {
        return this.fillString(x, y, string, font, color, JHelpTextAlign.LEFT, doAlphaMix);
    }

    /**
     * Fill a string<br>
     * MUST be in draw mode
     *
     * @param x         X top-left
     * @param y         Y top-left
     * @param string    String to draw
     * @param font      Font to use
     * @param color     Color for fill
     * @param textAlign Text alignment if several lines (\n)
     * @return Bounds where string just draw
     */
    public Rectangle fillString(
            final int x, final int y, final String string, final JHelpFont font, final int color,
            final JHelpTextAlign textAlign)
    {
        return this.fillString(x, y, string, font, color, textAlign, true);
    }

    /**
     * Fill a string<br>
     * MUST be in draw mode
     *
     * @param x          X top-left
     * @param y          Y top-left
     * @param string     String to draw
     * @param font       Font to use
     * @param color      Color for fill
     * @param textAlign  Text alignment if several lines (\n)
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     * @return Bounds where string just draw
     */
    public Rectangle fillString(
            final int x, final int y, final String string, final JHelpFont font, final int color,
            final JHelpTextAlign textAlign,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        final Pair<List<JHelpTextLineAlpha>, Dimension> lines = font.computeTextLinesAlpha(string, textAlign,
                                                                                           this.width -
                                                                                           x, this.height - y, true);
        JHelpImage mask;

        for (final JHelpTextLineAlpha textLineAlpha : lines.first)
        {
            mask = textLineAlpha.getMask();
            mask.startDrawMode();
            mask.fillRespectAlpha(color);
            mask.endDrawMode();
            this.drawImage(x + textLineAlpha.getX(), y + textLineAlpha.getY(), mask, true);
        }

        return new Rectangle(x, y, lines.second.width, lines.second.height);
    }

    /**
     * Fill a string<br>
     * Note : if the texture is not in draw mode, all of it's visible sprite will be consider like a part of he texture<br>
     * MUST be in draw mode
     *
     * @param x       X top-left
     * @param y       Y top-left
     * @param string  String to fill
     * @param font    Font to use
     * @param texture Texture to use
     * @param color   Color if underline
     * @return Bounds where string just draw
     */
    public Rectangle fillString(
            final int x, final int y, final String string, final JHelpFont font,
            final JHelpImage texture, final int color)
    {
        return this.fillString(x, y, string, font, texture, color, JHelpTextAlign.LEFT, true);
    }

    /**
     * Fill a string<br>
     * Note : if the texture is not in draw mode, all of it's visible sprite will be consider like a part of he texture<br>
     * MUST be in draw mode
     *
     * @param x          X top-left
     * @param y          Y top-left
     * @param string     String to fill
     * @param font       Font to use
     * @param texture    Texture to use
     * @param color      Color if underline
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     * @return Bounds where string just draw
     */
    public Rectangle fillString(
            final int x, final int y, final String string, final JHelpFont font,
            final JHelpImage texture, final int color, final boolean doAlphaMix)
    {
        return this.fillString(x, y, string, font, texture, color, JHelpTextAlign.LEFT, doAlphaMix);
    }

    /**
     * Fill a string<br>
     * Note : if the texture is not in draw mode, all of it's visible sprite will be consider like a part of he texture<br>
     * MUST be in draw mode
     *
     * @param x         X top-left
     * @param y         Y top-left
     * @param string    String to fill
     * @param font      Font to use
     * @param texture   Texture to use
     * @param color     Color if underline
     * @param textAlign Text alignment if several lines (\n)
     * @return Bounds where string just draw
     */
    public Rectangle fillString(
            final int x, final int y, final String string, final JHelpFont font,
            final JHelpImage texture, final int color, final JHelpTextAlign textAlign)
    {
        return this.fillString(x, y, string, font, texture, color, textAlign, true);
    }

    /**
     * Fill a string<br>
     * Note : if the texture is not in draw mode, all of it's visible sprite will be consider like a part of he texture<br>
     * MUST be in draw mode
     *
     * @param x          X top-left
     * @param y          Y top-left
     * @param string     String to fill
     * @param font       Font to use
     * @param texture    Texture to use
     * @param color      Color if underline
     * @param textAlign  Text alignment if several lines (\n)
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     * @return Bounds where string just draw
     */
    public Rectangle fillString(
            final int x, final int y, final String string, final JHelpFont font,
            final JHelpImage texture, final int color,
            final JHelpTextAlign textAlign, final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        final Pair<List<JHelpTextLineAlpha>, Dimension> lines = font.computeTextLinesAlpha(string, textAlign,
                                                                                           this.width - x,
                                                                                           this.height - y,
                                                                                           true);
        JHelpImage mask;

        for (final JHelpTextLineAlpha textLineAlpha : lines.first)
        {
            mask = textLineAlpha.getMask();
            mask.startDrawMode();
            mask.fillRespectAlpha(texture);
            mask.endDrawMode();
            this.drawImage(x + textLineAlpha.getX(), y + textLineAlpha.getY(), mask, true);
        }

        if (font.isUnderline())
        {
            this.drawHorizontalLine(x, x + lines.second.width, font.underlinePosition(string, y), color, doAlphaMix);
        }

        return new Rectangle(x, y, lines.second.width, lines.second.height);
    }

    /**
     * Fill a string<br>
     * MUST be on draw mode
     *
     * @param x      X
     * @param y      Y
     * @param string String to fill
     * @param font   Font to use
     * @param paint  Paint to use
     * @param color  Color for underline
     * @return Bounds where string just draw
     */
    public Rectangle fillString(
            final int x, final int y, final String string, final JHelpFont font,
            final JHelpPaint paint, final int color)
    {
        return this.fillString(x, y, string, font, paint, color, JHelpTextAlign.LEFT, true);
    }

    /**
     * Fill a string<br>
     * MUST be on draw mode
     *
     * @param x          X
     * @param y          Y
     * @param string     String to fill
     * @param font       Font to use
     * @param paint      Paint to use
     * @param color      Color for underline
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     * @return Bounds where string just draw
     */
    public Rectangle fillString(
            final int x, final int y, final String string, final JHelpFont font,
            final JHelpPaint paint, final int color,
            final boolean doAlphaMix)
    {
        return this.fillString(x, y, string, font, paint, color, JHelpTextAlign.LEFT, doAlphaMix);
    }

    /**
     * Fill a string<br>
     * MUST be on draw mode
     *
     * @param x         X
     * @param y         Y
     * @param string    String to fill
     * @param font      Font to use
     * @param paint     Paint to use
     * @param color     Color for underline
     * @param textAlign Text alignment if several lines (\n)
     * @return Bounds where string just draw
     */
    public Rectangle fillString(
            final int x, final int y, final String string, final JHelpFont font,
            final JHelpPaint paint, final int color,
            final JHelpTextAlign textAlign)
    {
        return this.fillString(x, y, string, font, paint, color, textAlign, true);
    }

    /**
     * Fill a string<br>
     * MUST be on draw mode
     *
     * @param x          X
     * @param y          Y
     * @param string     String to fill
     * @param font       Font to use
     * @param paint      Paint to use
     * @param color      Color for underline
     * @param textAlign  Text alignment if several lines (\n)
     * @param doAlphaMix Indicates if we do the mixing {@code true}, or we just override {@code false}
     * @return Bounds where string just draw
     */
    public Rectangle fillString(
            final int x, final int y, final String string, final JHelpFont font,
            final JHelpPaint paint, final int color,
            final JHelpTextAlign textAlign, final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        final Pair<List<JHelpTextLineAlpha>, Dimension> lines = font.computeTextLinesAlpha(string, textAlign,
                                                                                           this.width -
                                                                                           x, this.height - y, true);
        JHelpImage mask;

        for (final JHelpTextLineAlpha textLineAlpha : lines.first)
        {
            mask = textLineAlpha.getMask();
            mask.startDrawMode();
            mask.fillRespectAlpha(paint);
            mask.endDrawMode();
            this.drawImage(x + textLineAlpha.getX(), y + textLineAlpha.getY(), mask, true);
        }

        if (font.isUnderline())
        {
            this.drawHorizontalLine(x, x + lines.second.width, font.underlinePosition(string, y), color, doAlphaMix);
        }

        return new Rectangle(x, y, lines.second.width, lines.second.height);
    }

    /**
     * Filter image on blue channel<br>
     * MUST be on draw mode
     */
    public void filterBlue()
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int color, blue;

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            color = this.pixels[pix];
            blue = color & 0xFF;

            this.pixels[pix] = (color & 0xFF0000FF) | (blue << 16) | (blue << 8);
        }
    }

    /**
     * Filter image on green channel<br>
     * MUST be on draw mode
     */
    public void filterGreen()
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int color, green;

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            color = this.pixels[pix];
            green = (color >> 8) & 0xFF;

            this.pixels[pix] = (color & 0xFF00FF00) | (green << 16) | green;
        }
    }

    /**
     * Filter image on a specific color<br>
     * MUST be on draw mode
     *
     * @param color   Color search
     * @param colorOK Color to use if corresponds
     * @param colorKO Colo to use if failed
     */
    public void filterOn(final int color, final int colorOK, final int colorKO)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        for (int i = this.pixels.length - 1; i >= 0; i--)
        {
            if (color == this.pixels[i])
            {
                this.pixels[i] = colorOK;
            }
            else
            {
                this.pixels[i] = colorKO;
            }
        }
    }

    /**
     * filter image on a specific color<br>
     * MUST be on draw mode
     *
     * @param color     Color search
     * @param precision Precision to use
     * @param colorOK   Color if corresponds
     * @param colorKO   Color if not corresponds
     */
    public void filterOn(final int color, final int precision, final int colorOK, final int colorKO)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        final Color refrence = new Color(color);

        for (int i = this.pixels.length - 1; i >= 0; i--)
        {
            if (refrence.isNear(new Color(this.pixels[i]), precision))
            {
                this.pixels[i] = colorOK;
            }
            else
            {
                this.pixels[i] = colorKO;
            }
        }
    }

    /**
     * Convert image to JHelp image <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return This image itself (It is already a JHelp image)
     * @see jhelp.util.image.raster.RasterImage#toJHelpImage()
     */
    @Override
    public JHelpImage toJHelpImage()
    {
        return this;
    }

    /**
     * Filter image on red channel<br>
     * MUST be on draw mode
     */
    public void filterRed()
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int color, red;

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            color = this.pixels[pix];
            red = (color >> 16) & 0xFF;

            this.pixels[pix] = (color & 0xFFFF0000) | (red << 8) | red;
        }
    }

    /**
     * Filter image on U part<br>
     * MUST be on draw mode
     */
    public void filterU()
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int color;
        int u;

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            color = this.pixels[pix];

            u = Math2.limit0_255((int) JHelpImage.computeU((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF));

            this.pixels[pix] = (color & 0xFF000000) | (u << 16) | (u << 8) | u;
        }
    }

    /**
     * Filter image on V part<br>
     * MUST be on draw mode
     */
    public void filterV()
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int color;
        int v;

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            color = this.pixels[pix];

            v = Math2.limit0_255((int) JHelpImage.computeV((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF));

            this.pixels[pix] = (color & 0xFF000000) | (v << 16) | (v << 8) | v;
        }
    }

    /**
     * Filter image on Y part<br>
     * MUST be on draw mode
     */
    public void filterY()
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int color;
        int y;

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            color = this.pixels[pix];

            y = Math2.limit0_255((int) JHelpImage.computeY((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF));

            this.pixels[pix] = (color & 0xFF000000) | (y << 16) | (y << 8) | y;
        }
    }

    /**
     * Flip the image horizontally and vertically in same time. <br>
     * Visually its same result as : <code lang="java">
     * image.flipHorizontal();
     * image.flipVertical();
     * </code> But its done faster<br>
     * MUST be on draw mode
     */
    public void flipBoth()
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        final int length = this.pixels.length;
        final int mpix   = length >> 1;
        int       color;

        for (int pixS = 0, pixE = length - 1; pixS < mpix; pixS++, pixE--)
        {
            color = this.pixels[pixS];
            this.pixels[pixS] = this.pixels[pixE];
            this.pixels[pixE] = color;
        }
    }

    /**
     * Flip the image horizontally<br>
     * MUST be on draw mode
     */
    public void flipHorizontal()
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        final int mx   = this.width >> 1;
        int       line = 0;
        int       pixL;
        int       pixR;
        int       color;

        for (int y = 0; y < this.height; y++)
        {
            pixL = line;
            pixR = (line + this.width) - 1;

            for (int x = 0; x < mx; x++)
            {
                color = this.pixels[pixL];
                this.pixels[pixL] = this.pixels[pixR];
                this.pixels[pixR] = color;

                pixL++;
                pixR--;
            }

            line += this.width;
        }
    }

    /**
     * Flip the image vertically<br>
     * MUST be on draw mode
     */
    public void flipVertical()
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        final int   my    = this.height >> 1;
        int         lineU = 0;
        int         lineB = (this.height - 1) * this.width;
        final int[] line  = new int[this.width];

        for (int y = 0; y < my; y++)
        {
            System.arraycopy(this.pixels, lineU, line, 0, this.width);
            System.arraycopy(this.pixels, lineB, this.pixels, lineU, this.width);
            System.arraycopy(line, 0, this.pixels, lineB, this.width);

            lineU += this.width;
            lineB -= this.width;
        }
    }

    /**
     * Current clip
     *
     * @return Current clip
     */
    public Clip getClip()
    {
        return this.clip.copy();
    }

    /**
     * Image for draw in graphics environment
     *
     * @return Image for draw in graphics environment
     */
    public Image getImage()
    {
        return this.image;
    }

    /**
     * Image name
     *
     * @return Image name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Change image name
     *
     * @param name New name
     */
    public void setName(final String name)
    {
        this.name = name;
    }

    /**
     * Extract an array of pixels from the image.<br>
     * If the image is no in draw mode, sprites will be consider as part of image
     *
     * @param x      X up-left corner
     * @param y      Y up-left corner
     * @param width  Rectangle width
     * @param height Rectangle height
     * @return Extracted pixels
     */
    public int[] getPixels(final int x, final int y, final int width, final int height)
    {
        return this.getPixels(x, y, width, height, 0);
    }

    /**
     * Extract an array of pixels from the image.<br>
     * The returned array will have some additional free integer at start, the number depends on the given offset.<br>
     * If the image is no in draw mode, sprites will be consider as part of image
     *
     * @param x      X up-left corner
     * @param y      Y up-left corner
     * @param width  Rectangle width
     * @param height Rectangle height
     * @param offset Offset where start copy the pixels, so before integers are "free", so it could be see also as the
     *               number of free
     *               integers
     * @return Extracted pixels
     */
    public int[] getPixels(int x, int y, int width, int height, final int offset)
    {
        if (offset < 0)
        {
            throw new IllegalArgumentException("offset must be >=0 not " + offset);
        }

        if (x < 0)
        {
            width += x;
            x = 0;
        }

        if ((x + width) > this.width)
        {
            width = this.width - x;
        }

        if ((x > this.width) || (width < 1))
        {
            return new int[0];
        }

        if (y < 0)
        {
            height += y;
            y = 0;
        }

        if ((y + height) > this.height)
        {
            height = this.height - y;
        }

        if ((y > this.height) || (height < 1))
        {
            return new int[0];
        }

        final int   size   = width * height;
        final int[] result = new int[size + offset];
        int         pix    = x + (y * this.width);
        int         pixImg = offset;

        for (int yy = 0; yy < height; yy++)
        {
            System.arraycopy(this.pixels, pix, result, pixImg, width);

            pix += this.width;
            pixImg += width;
        }

        return result;
    }

    /**
     * Image weight <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Image weight
     * @see jhelp.util.list.HeavyObject#getWeight()
     */
    @Override
    public long getWeight()
    {
        return this.width * this.height;
    }

    /**
     * Convert image in gray version<br>
     * MUST be on draw mode
     */
    public void gray()
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int color;
        int y;
        for (int i = this.pixels.length - 1; i >= 0; i--)
        {
            color = this.pixels[i];

            y = Math2.limit0_255((int) (JHelpImage.computeY((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF)));

            this.pixels[i] = (color & 0xFF000000) | (y << 16) | (y << 8) | y;
        }
    }

    /**
     * Convert image in gray invert version<br>
     * MUST be on draw mode
     */
    public void grayInvert()
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int color;
        int y;
        for (int i = this.pixels.length - 1; i >= 0; i--)
        {
            color = this.pixels[i];

            y = 255 - Math2.limit0_255((int) (JHelpImage.computeY((color >> 16) & 0xFF,
                                                                  (color >> 8) & 0xFF,
                                                                  color & 0xFF)));

            this.pixels[i] = (color & 0xFF000000) | (y << 16) | (y << 8) | y;
        }
    }

    /**
     * Invert image colors<br>
     * MUST be on draw mode
     */
    public void invertColors()
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int color;
        for (int i = this.pixels.length - 1; i >= 0; i--)
        {
            color = this.pixels[i];

            this.pixels[i] = (color & 0xFF000000) | //
                             ((255 - ((color >> 16) & 0xFF)) << 16) | //
                             ((255 - ((color >> 8) & 0xFF)) << 8) | //
                             (255 - (color & 0xFF));
        }
    }

    /**
     * Invert U and V parts<br>
     * MUST be on draw mode
     */
    public void invertUV()
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int    color, red, green, blue;
        double y, u, v;

        for (int i = this.pixels.length - 1; i >= 0; i--)
        {
            color = this.pixels[i];
            red = (color >> 16) & 0xFF;
            green = (color >> 8) & 0xFF;
            blue = color & 0xFF;

            y = JHelpImage.computeY(red, green, blue);
            u = JHelpImage.computeU(red, green, blue);
            v = JHelpImage.computeV(red, green, blue);

            this.pixels[i] = (color & 0xFF000000)
                             | (JHelpImage.computeRed(y, v, u) << 16)
                             | (JHelpImage.computeGreen(y, v, u) << 8)
                             | JHelpImage.computeBlue(y, v, u);
        }
    }

    /**
     * Indicates if we are in draw mode
     *
     * @return Draw mode status
     */
    public boolean isDrawMode()
    {
        return this.drawMode;
    }

    /**
     * Remove all color part except blue<br>
     * MUST be on draw mode
     */
    public void keepBlue()
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            this.pixels[pix] &= 0xFF0000FF;
        }
    }

    /**
     * Remove all color part except green<br>
     * MUST be on draw mode
     */
    public void keepGreen()
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            this.pixels[pix] &= 0xFF00FF00;
        }
    }

    /**
     * Remove all color part except red<br>
     * MUST be on draw mode
     */
    public void keepRed()
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            this.pixels[pix] &= 0xFFFF0000;
        }
    }

    /**
     * Take the maximum between this image and given one<br>
     * Note : if the given image is not in draw mode, all of it's visible sprite will be consider like a part of the
     * given image
     * <br>
     * Given image MUST have same dimension of this <br>
     * MUST be in draw mode
     *
     * @param image Image reference
     */
    public void maximum(final JHelpImage image)
    {
        if ((this.width != image.width) || (this.height != image.height))
        {
            throw new IllegalArgumentException("We can only maximize with an image of same size");
        }

        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int colorThis, colorImage;

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            colorThis = this.pixels[pix];
            colorImage = image.pixels[pix];

            this.pixels[pix] = (colorThis & 0xFF000000) | //
                               (Math.max((colorThis >> 16) & 0xFF, (colorImage >> 16) & 0xFF) << 16) | //
                               (Math.max((colorThis >> 8) & 0xFF, (colorImage >> 8) & 0xFF) << 8) | //
                               Math.max(colorThis & 0xFF, colorImage & 0xFF);
        }
    }

    /**
     * Take the middle between this image and given one<br>
     * Note : if the given image is not in draw mode, all of it's visible sprite will be consider like a part of the
     * given image
     * <br>
     * Given image MUST have same dimension of this <br>
     * MUST be in draw mode
     *
     * @param image Image reference
     */
    public void middle(final JHelpImage image)
    {
        if ((this.width != image.width) || (this.height != image.height))
        {
            throw new IllegalArgumentException("We can only take middle with an image of same size");
        }

        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int colorThis, colorImage;

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            colorThis = this.pixels[pix];
            colorImage = image.pixels[pix];

            this.pixels[pix] = (colorThis & 0xFF000000) | //
                               (((((colorThis >> 16) & 0xFF) + ((colorImage >> 16) & 0xFF)) >> 1) << 16) | //
                               (((((colorThis >> 8) & 0xFF) + ((colorImage >> 8) & 0xFF)) >> 1) << 8) | //
                               (((colorThis & 0xFF) + (colorImage & 0xFF)) >> 1);
        }
    }

    /**
     * Take the minimum between this image and given one<br>
     * Note : if the given image is not in draw mode, all of it's visible sprite will be consider like a part of the
     * given image
     * <br>
     * Given image MUST have same dimension of this <br>
     * MUST be in draw mode
     *
     * @param image Image reference
     */
    public void minimum(final JHelpImage image)
    {
        if ((this.width != image.width) || (this.height != image.height))
        {
            throw new IllegalArgumentException("We can only minimize with an image of same size");
        }

        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int colorThis, colorImage;

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            colorThis = this.pixels[pix];
            colorImage = image.pixels[pix];

            this.pixels[pix] = (colorThis & 0xFF000000) | //
                               (Math.min((colorThis >> 16) & 0xFF, (colorImage >> 16) & 0xFF) << 16) | //
                               (Math.min((colorThis >> 8) & 0xFF, (colorImage >> 8) & 0xFF) << 8) | //
                               Math.min(colorThis & 0xFF, colorImage & 0xFF);
        }
    }

    /**
     * Multiply the image with an other one<br>
     * Note : if the given image is not in draw mode, all of it's visible sprite will be consider like a part of the
     * given image
     * <br>
     * Given image MUST have same dimension of this <br>
     * MUST be in draw mode
     *
     * @param image Image to multiply
     */
    public void multiply(final JHelpImage image)
    {
        if ((this.width != image.width) || (this.height != image.height))
        {
            throw new IllegalArgumentException("We can only multiply with an image of same size");
        }

        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int colorThis, colorImage;

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            colorThis = this.pixels[pix];
            colorImage = image.pixels[pix];

            this.pixels[pix] = (colorThis & 0xFF000000) | //
                               (((((colorThis >> 16) & 0xFF) * ((colorImage >> 16) & 0xFF)) / 255) << 16) | //
                               (((((colorThis >> 8) & 0xFF) * ((colorImage >> 8) & 0xFF)) / 255) << 8) | //
                               (((colorThis & 0xFF) * (colorImage & 0xFF)) / 255);
        }
    }

    /**
     * Paint the image on using an other as alpha mask<br>
     * Alpha mask image can be imagine like a paper with holes that we put on the main image, we paint, and remove the
     * image,
     * only holes are paint on final image.<br>
     * Holes are here pixels with alpha more than 0x80
     *
     * @param x          Where put the left corner X of alpha mask
     * @param y          Where put the left corner Y of alpha mask
     * @param alphaMask  Alpha mask to use
     * @param color      Color to fill holes
     * @param background Color for fill background
     * @param doAlphaMix Indicates if do alpha mixing ({@code true}) or just overwrite ({@code false})
     */
    public void paintAlphaMask(
            int x, int y, final JHelpImage alphaMask, final int color, final int background,
            final boolean doAlphaMix)
    {
        this.fillRectangle(x, y, alphaMask.width, alphaMask.height, background, doAlphaMix);
        this.paintAlphaMask(x, y, alphaMask, color);
    }

    /**
     * Paint the image on using an other as alpha mask<br>
     * Alpha mask image can be imagine like a paper with holes that we put on the main image, we paint, and remove the
     * image,
     * only holes are paint on final image.<br>
     * Holes are here pixels with alpha more than 0x80
     *
     * @param x         Where put the left corner X of alpha mask
     * @param y         Where put the left corner Y of alpha mask
     * @param alphaMask Alpha mask to use
     * @param color     Color to fill holes
     */
    public void paintAlphaMask(int x, int y, final JHelpImage alphaMask, final int color)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int w  = this.clip.xMax + 1;
        int xx = 0;
        if (x < this.clip.xMin)
        {
            xx = -x + this.clip.xMin;
            w += x - this.clip.xMin;
            x = this.clip.xMin;
        }

        int h  = this.clip.yMax + 1;
        int yy = 0;
        if (y < this.clip.yMin)
        {
            yy = -y + this.clip.yMin;
            h += y - this.clip.yMin;
            y = this.clip.yMin;
        }

        final int widthAlpha = alphaMask.getWidth();
        final int width      = Math2.minimum(w - x, widthAlpha, this.width - x);
        final int height     = Math2.minimum(h - y, alphaMask.getHeight(), this.height - y);

        if ((width < 1) || (height < 1))
        {
            return;
        }

        int line      = x + (y * this.width);
        int lineAlpha = xx + (yy * widthAlpha);
        int pix, pixAlpha, alphaAlpha;

        for (int yyy = yy; yyy < height; yyy++)
        {
            pix = line;
            pixAlpha = lineAlpha;

            for (int xxx = xx; xxx < width; xxx++)
            {
                alphaAlpha = (alphaMask.pixels[pixAlpha] >> 24) & 0xFF;

                if (alphaAlpha > 0x80)
                {
                    this.pixels[pix] = color;
                }

                pix++;
                pixAlpha++;
            }

            line += this.width;
            lineAlpha += widthAlpha;
        }
    }

    /**
     * Paint the image on using an other as alpha mask<br>
     * Alpha mask image can be imagine like a paper with holes that we put on the main image, we paint, and remove the
     * image,
     * only holes are paint on final image.<br>
     * Holes are here pixels with alpha more than 0x80
     *
     * @param x         Where put the left corner X of alpha mask
     * @param y         Where put the left corner Y of alpha mask
     * @param alphaMask Alpha mask to use
     * @param texture   Texture to fill holes
     */
    public void paintAlphaMask(int x, int y, final JHelpImage alphaMask, final JHelpImage texture)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int w  = this.clip.xMax + 1;
        int xx = 0;
        if (x < this.clip.xMin)
        {
            xx = -x + this.clip.xMin;
            w += x - this.clip.xMin;
            x = this.clip.xMin;
        }

        int h  = this.clip.yMax + 1;
        int yy = 0;
        if (y < this.clip.yMin)
        {
            yy = -y + this.clip.yMin;
            h += y - this.clip.yMin;
            y = this.clip.yMin;
        }

        final int widthAlpha  = alphaMask.getWidth();
        final int heightAlpha = alphaMask.getHeight();
        final int width       = Math2.minimum(w - x, widthAlpha, this.width - x);
        final int height      = Math2.minimum(h - y, heightAlpha, this.height - y);

        if ((width < 1) || (height < 1))
        {
            return;
        }

        final int widthTexture  = texture.getWidth();
        final int heightTexture = texture.getHeight();
        final int xStart        = xx % widthTexture;
        int       line          = x + (y * this.width);
        int       lineAlpha     = xx + (yy * widthAlpha);
        int       pix, pixAlpha, alphaAlpha, lineTexture;

        for (int yyy = yy; yyy < height; yyy++)
        {
            lineTexture = xStart + ((yyy % heightTexture) * widthTexture);
            pix = line;
            pixAlpha = lineAlpha;

            for (int xxx = xx; xxx < width; xxx++)
            {
                alphaAlpha = (alphaMask.pixels[pixAlpha] >> 24) & 0xFF;

                if (alphaAlpha > 0x80)
                {
                    this.pixels[pix] = texture.pixels[lineTexture + (xxx % widthTexture)];
                }

                pix++;
                pixAlpha++;
            }

            line += this.width;
            lineAlpha += widthAlpha;
        }
    }

    /**
     * Paint the image on using an other as alpha mask<br>
     * Alpha mask image can be imagine like a paper with holes that we put on the main image, we paint, and remove the
     * image,
     * only holes are paint on final image.<br>
     * Holes are here pixxels with alpha more than 0x80
     *
     * @param x         Where put the left corner X of alpha mask
     * @param y         Where put the left corner Y of alpha mask
     * @param alphaMask Alpha mask to use
     * @param paint     Paint to fill holes
     */
    public void paintAlphaMask(int x, int y, final JHelpImage alphaMask, final JHelpPaint paint)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int w  = this.clip.xMax + 1;
        int xx = 0;
        if (x < this.clip.xMin)
        {
            xx = -x + this.clip.xMin;
            w += x - this.clip.xMin;
            x = this.clip.xMin;
        }

        int h  = this.clip.yMax + 1;
        int yy = 0;
        if (y < this.clip.yMin)
        {
            yy = -y + this.clip.yMin;
            h += y - this.clip.yMin;
            y = this.clip.yMin;
        }

        final int widthAlpha  = alphaMask.getWidth();
        final int heightAlpha = alphaMask.getHeight();
        final int width       = Math2.minimum(w - x, widthAlpha, this.width - x);
        final int height      = Math2.minimum(h - y, heightAlpha, this.height - y);

        if ((width < 1) || (height < 1))
        {
            return;
        }

        paint.initializePaint(widthAlpha, heightAlpha);
        int line      = x + (y * this.width);
        int lineAlpha = xx + (yy * widthAlpha);
        int pix, pixAlpha, alphaAlpha;

        for (int yyy = yy; yyy < height; yyy++)
        {
            pix = line;
            pixAlpha = lineAlpha;

            for (int xxx = xx; xxx < width; xxx++)
            {
                alphaAlpha = (alphaMask.pixels[pixAlpha] >> 24) & 0xFF;

                if (alphaAlpha > 0x80)
                {
                    this.pixels[pix] = paint.obtainColor(xxx, yyy);
                }

                pix++;
                pixAlpha++;
            }

            line += this.width;
            lineAlpha += widthAlpha;
        }
    }

    /**
     * Draw the image like an {@link Icon} <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param component Reference component
     * @param graphics  Graphics where paint
     * @param x         X position
     * @param y         Y position
     * @see Icon#paintIcon(Component, Graphics, int, int)
     */
    @Override
    public void paintIcon(final Component component, final Graphics graphics, final int x, final int y)
    {
        this.update();
        graphics.drawImage(this.image, x, y, null);
    }

    /**
     * Image width <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Image width
     * @see Icon#getIconWidth()
     */
    @Override
    public int getIconWidth()
    {
        return this.width;
    }

    /**
     * Image height <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Image height
     * @see Icon#getIconHeight()
     */
    @Override
    public int getIconHeight()
    {
        return this.height;
    }

    /**
     * Paint a mask<br>
     * MUST be in draw mode
     *
     * @param x          X
     * @param y          Y
     * @param mask       Mask to paint
     * @param foreground Foreground color
     * @param background Background color
     * @param doAlphaMix Indicates if do alpha mixing ({@code true}) or just overwrite ({@code false})
     */
    public void paintMask(
            int x, int y, final JHelpMask mask, final int foreground, final int background,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int w  = this.clip.xMax + 1;
        int xx = 0;
        if (x < this.clip.xMin)
        {
            xx = -x + this.clip.xMin;
            w += x - this.clip.xMin;
            x = this.clip.xMin;
        }

        int h  = this.clip.yMax + 1;
        int yy = 0;
        if (y < this.clip.yMin)
        {
            yy = -y + this.clip.yMin;
            h += y - this.clip.yMin;
            y = this.clip.yMin;
        }

        final int width  = Math2.minimum(w - x, mask.getWidth(), this.width - x);
        final int height = Math2.minimum(h - y, mask.getHeight(), this.height - y);

        if ((width < 1) || (height < 1))
        {
            return;
        }

        int line = x + (y * this.width);
        int pix, color, col, alpha, ahpla, red, blue, green;

        final int alphaFore = (foreground >> 24) & 0xFF;
        final int redFore   = ((foreground >> 16) & 0xFF) * alphaFore;
        final int greenFore = ((foreground >> 8) & 0xFF) * alphaFore;
        final int blueFore  = (foreground & 0xFF) * alphaFore;

        final int alphaBack = (background >> 24) & 0xFF;
        final int redBack   = ((background >> 16) & 0xFF) * alphaBack;
        final int greenBack = ((background >> 8) & 0xFF) * alphaBack;
        final int blueBack  = (background & 0xFF) * alphaBack;

        for (int yyy = yy; yyy < height; yyy++)
        {
            pix = line;

            for (int xxx = xx; xxx < width; xxx++)
            {
                if (mask.getValue(xxx, yyy))
                {
                    color = foreground;
                    alpha = alphaFore;
                    red = redFore;
                    green = greenFore;
                    blue = blueFore;
                }
                else
                {
                    color = background;
                    alpha = alphaBack;
                    red = redBack;
                    green = greenBack;
                    blue = blueBack;
                }

                if ((alpha == 255) || (!doAlphaMix))
                {
                    this.pixels[pix] = color;
                }
                else if (alpha > 0)
                {
                    ahpla = 256 - alpha;
                    col = this.pixels[pix];

                    this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                                       (((red + (((col >> 16) & 0xFF) * ahpla)) >> 8) << 16) | //
                                       (((green + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) | //
                                       ((blue + ((col & 0xFF) * ahpla)) >> 8);
                }

                pix++;
            }

            line += this.width;
        }
    }

    /**
     * Paint a mask with unify foreground color and part of image in background<br>
     * Note : if the background is not in draw mode, all of it's visible sprite will be consider like a part of the
     * background
     * <br>
     * MUST be in draw mode
     *
     * @param x           X
     * @param y           Y
     * @param mask        Mask to paint
     * @param foreground  Foreground color
     * @param background  Background image
     * @param backgroundX X start in background image
     * @param backgroundY Y start in background image
     * @param doAlphaMix  Indicates if do alpha mixing ({@code true}) or just overwrite ({@code false})
     */
    public void paintMask(
            int x, int y, final JHelpMask mask, final int foreground, final JHelpImage background,
            int backgroundX, int backgroundY,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int bw = background.width;
        if (backgroundX < 0)
        {
            bw += backgroundX;
            backgroundX = 0;
        }

        int bh = background.height;
        if (backgroundY < 0)
        {
            bh += backgroundY;
            backgroundY = 0;
        }

        int w  = this.clip.xMax + 1;
        int xx = 0;
        if (x < this.clip.xMin)
        {
            xx = -x + this.clip.xMin;
            w += x - this.clip.xMin;
            x = this.clip.xMin;
        }

        int h  = this.clip.yMax + 1;
        int yy = 0;
        if (y < this.clip.yMin)
        {
            yy = -y + this.clip.yMin;
            h += y - this.clip.yMin;
            y = this.clip.yMin;
        }

        final int width  = Math2.minimum(w - x, mask.getWidth(), bw - backgroundX);
        final int height = Math2.minimum(h - y, mask.getHeight(), bh - backgroundY);

        if ((width < 1) || (height < 1))
        {
            return;
        }

        int lineBack = backgroundX + (backgroundY * background.width);
        int line     = x + (y * this.width);
        int pixBack, pix, color, col, alpha, ahpla;

        for (int yyy = yy; yyy < height; yyy++)
        {
            pixBack = lineBack;
            pix = line;

            for (int xxx = xx; xxx < width; xxx++)
            {
                color = mask.getValue(xxx, yyy)
                        ? foreground
                        : background.pixels[pixBack];
                alpha = (color >> 24) & 0xFF;

                if ((alpha == 255) || (!doAlphaMix))
                {
                    this.pixels[pix] = color;
                }
                else if (alpha > 0)
                {
                    ahpla = 256 - alpha;
                    col = this.pixels[pix];

                    this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                                       ((((((color >> 16) & 0xFF) * alpha) + (((col >> 16) & 0xFF) * ahpla)) >> 8) <<
                                        16) | //
                                       ((((((color >> 8) & 0xFF) * alpha) + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) |
                                       //
                                       ((((color & 0xFF) * alpha) + ((col & 0xFF) * ahpla)) >> 8);
                }

                pixBack++;
                pix++;
            }

            lineBack += background.width;
            line += this.width;
        }
    }

    /**
     * Paint a mask with unify color as foreground and paint as background<br>
     * MUST be in draw mode
     *
     * @param x          X
     * @param y          Y
     * @param mask       Mask to paint
     * @param foreground Foreground color
     * @param background Background paint
     * @param doAlphaMix Indicates if do alpha mixing ({@code true}) or just overwrite ({@code false})
     */
    public void paintMask(
            int x, int y, final JHelpMask mask, final int foreground, final JHelpPaint background,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int w  = this.clip.xMax + 1;
        int xx = 0;
        if (x < this.clip.xMin)
        {
            xx = -x + this.clip.xMin;
            w += x - this.clip.xMin;
            x = this.clip.xMin;
        }

        int h  = this.clip.yMax + 1;
        int yy = 0;
        if (y < this.clip.yMin)
        {
            yy = -y + this.clip.yMin;
            h += y - this.clip.yMin;
            y = this.clip.yMin;
        }

        final int width  = Math2.minimum(w - x, mask.getWidth());
        final int height = Math2.minimum(h - y, mask.getHeight());

        if ((width < 1) || (height < 1))
        {
            return;
        }

        background.initializePaint(width, height);

        int line = x + (y * this.width);
        int pix, color, col, alpha, ahpla;

        for (int yyy = yy; yyy < height; yyy++)
        {
            pix = line;

            for (int xxx = xx; xxx < width; xxx++)
            {
                color = mask.getValue(xxx, yyy)
                        ? foreground
                        : background.obtainColor(xxx, yyy);
                alpha = (color >> 24) & 0xFF;

                if ((alpha == 255) || (!doAlphaMix))
                {
                    this.pixels[pix] = color;
                }
                else if (alpha > 0)
                {
                    ahpla = 256 - alpha;
                    col = this.pixels[pix];

                    this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                                       ((((((color >> 16) & 0xFF) * alpha) + (((col >> 16) & 0xFF) * ahpla)) >> 8) <<
                                        16) | //
                                       ((((((color >> 8) & 0xFF) * alpha) + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) |
                                       //
                                       ((((color & 0xFF) * alpha) + ((col & 0xFF) * ahpla)) >> 8);
                }

                pix++;
            }

            line += this.width;
        }
    }

    /**
     * Paint a mask with part of image as foreground and unify color as background<br>
     * Note : if the foreground is not in draw mode, all of it's visible sprite will be consider like a part of the
     * foreground
     * <br>
     * MUST be in draw mode
     *
     * @param x           X
     * @param y           Y
     * @param mask        Mask to paint
     * @param foreground  Foreground image
     * @param foregroundX X start on foreground image
     * @param foregroundY Y start on foreground image
     * @param background  Background color
     * @param doAlphaMix  Indicates if do alpha mixing ({@code true}) or just overwrite ({@code false})
     */
    public void paintMask(
            int x, int y, final JHelpMask mask, final JHelpImage foreground, int foregroundX,
            int foregroundY, final int background,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int fw = foreground.width;
        if (foregroundX < 0)
        {
            fw += foregroundX;
            foregroundX = 0;
        }

        int fh = foreground.height;
        if (foregroundY < 0)
        {
            fh += foregroundY;
            foregroundY = 0;
        }

        int w  = this.clip.xMax + 1;
        int xx = 0;
        if (x < this.clip.xMin)
        {
            xx = -x + this.clip.xMin;
            w += x - this.clip.xMin;
            x = this.clip.xMin;
        }

        int h  = this.clip.yMax + 1;
        int yy = 0;
        if (y < this.clip.yMin)
        {
            yy = -y + this.clip.yMin;
            h += y - this.clip.yMin;
            y = this.clip.yMin;
        }

        final int width  = Math2.minimum(w - x, mask.getWidth(), fw - foregroundX);
        final int height = Math2.minimum(h - y, mask.getHeight(), fh - foregroundY);

        if ((width < 1) || (height < 1))
        {
            return;
        }

        int lineFore = foregroundX + (foregroundY * foreground.width);
        int line     = x + (y * this.width);
        int pixFore, pix, color, col, alpha, ahpla;

        for (int yyy = yy; yyy < height; yyy++)
        {
            pixFore = lineFore;
            pix = line;

            for (int xxx = xx; xxx < width; xxx++)
            {
                color = mask.getValue(xxx, yyy)
                        ? foreground.pixels[pixFore]
                        : background;
                alpha = (color >> 24) & 0xFF;

                if ((alpha == 255) || (!doAlphaMix))
                {
                    this.pixels[pix] = color;
                }
                else if (alpha > 0)
                {
                    ahpla = 256 - alpha;
                    col = this.pixels[pix];

                    this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                                       ((((((color >> 16) & 0xFF) * alpha) + (((col >> 16) & 0xFF) * ahpla)) >> 8) <<
                                        16) | //
                                       ((((((color >> 8) & 0xFF) * alpha) + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) |
                                       //
                                       ((((color & 0xFF) * alpha) + ((col & 0xFF) * ahpla)) >> 8);
                }

                pixFore++;
                pix++;
            }

            lineFore += foreground.width;
            line += this.width;
        }
    }

    /**
     * Paint a mask with 2 images, one for "foreground" pixels, one for "background" ones<br>
     * Note : if the foreground or background is not in draw mode, all of it's visible sprite will be consider like a
     * part of the
     * foreground or background<br>
     * MUST be in draw mode
     *
     * @param x           X position for the mask
     * @param y           Y position for the mask
     * @param mask        Mask to paint
     * @param foreground  Foreground image
     * @param foregroundX X start position in foreground image
     * @param foregroundY Y start position in foreground image
     * @param background  Background image
     * @param backgroundX X start position in background image
     * @param backgroundY Y start position in background image
     * @param doAlphaMix  Indicates if do alpha mixing ({@code true}) or just overwrite ({@code false})
     */
    public void paintMask(
            int x, int y, final JHelpMask mask, final JHelpImage foreground, int foregroundX,
            int foregroundY, final JHelpImage background,
            int backgroundX, int backgroundY, final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int fw = foreground.width;
        if (foregroundX < 0)
        {
            fw += foregroundX;
            foregroundX = 0;
        }

        int fh = foreground.height;
        if (foregroundY < 0)
        {
            fh += foregroundY;
            foregroundY = 0;
        }

        int bw = background.width;
        if (backgroundX < 0)
        {
            bw += backgroundX;
            backgroundX = 0;
        }

        int bh = background.height;
        if (backgroundY < 0)
        {
            bh += backgroundY;
            backgroundY = 0;
        }

        int w  = this.clip.xMax + 1;
        int xx = 0;
        if (x < this.clip.xMin)
        {
            xx = -x + this.clip.xMin;
            w += x - this.clip.xMin;
            x = this.clip.xMin;
        }

        int h  = this.clip.yMax + 1;
        int yy = 0;
        if (y < this.clip.yMin)
        {
            yy = -y + this.clip.yMin;
            h += y - this.clip.yMin;
            y = this.clip.yMin;
        }

        final int width  = Math2.minimum(w - x, mask.getWidth(), fw - foregroundX, bw - backgroundX);
        final int height = Math2.minimum(h - y, mask.getHeight(), fh - foregroundY, bh - backgroundY);

        if ((width < 1) || (height < 1))
        {
            return;
        }

        int lineFore = foregroundX + (foregroundY * foreground.width);
        int lineBack = backgroundX + (backgroundY * background.width);
        int line     = x + (y * this.width);
        int pixFore, pixBack, pix, color, col, alpha, ahpla;

        for (int yyy = yy; yyy < height; yyy++)
        {
            pixFore = lineFore;
            pixBack = lineBack;
            pix = line;

            for (int xxx = xx; xxx < width; xxx++)
            {
                color = mask.getValue(xxx, yyy)
                        ? foreground.pixels[pixFore]
                        : background.pixels[pixBack];
                alpha = (color >> 24) & 0xFF;

                if ((alpha == 255) || (!doAlphaMix))
                {
                    this.pixels[pix] = color;
                }
                else if (alpha > 0)
                {
                    ahpla = 256 - alpha;
                    col = this.pixels[pix];

                    this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                                       ((((((color >> 16) & 0xFF) * alpha) + (((col >> 16) & 0xFF) * ahpla)) >> 8) <<
                                        16) | //
                                       ((((((color >> 8) & 0xFF) * alpha) + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) |
                                       //
                                       ((((color & 0xFF) * alpha) + ((col & 0xFF) * ahpla)) >> 8);
                }

                pixFore++;
                pixBack++;
                pix++;
            }

            lineFore += foreground.width;
            lineBack += background.width;
            line += this.width;
        }
    }

    /**
     * Paint a mask with image in foreground and paint in background<br>
     * Note : if the foreground is not in draw mode, all of it's visible sprite will be consider like a part of the
     * foreground
     * <br>
     * MUST be in draw mode
     *
     * @param x           X where paint the mask
     * @param y           Y where paint the mask
     * @param mask        Mask to paint
     * @param foreground  Image in foreground
     * @param foregroundX X start on foreground image
     * @param foregroundY Y start on foreground image
     * @param background  Background paint
     * @param doAlphaMix  Indicates if do alpha mixing ({@code true}) or just overwrite ({@code false})
     */
    public void paintMask(
            int x, int y, final JHelpMask mask, final JHelpImage foreground, int foregroundX,
            int foregroundY, final JHelpPaint background,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int fw = foreground.width;
        if (foregroundX < 0)
        {
            fw += foregroundX;
            foregroundX = 0;
        }

        int fh = foreground.height;
        if (foregroundY < 0)
        {
            fh += foregroundY;
            foregroundY = 0;
        }

        int w  = this.clip.xMax + 1;
        int xx = 0;
        if (x < this.clip.xMin)
        {
            xx = -x + this.clip.xMin;
            w += x - this.clip.xMin;
            x = this.clip.xMin;
        }

        int h  = this.clip.yMax + 1;
        int yy = 0;
        if (y < this.clip.yMin)
        {
            yy = -y + this.clip.yMin;
            h += y - this.clip.yMin;
            y = this.clip.yMin;
        }

        final int width  = Math2.minimum(w - x, mask.getWidth(), fw - foregroundX);
        final int height = Math2.minimum(h - y, mask.getHeight(), fh - foregroundY);

        if ((width < 1) || (height < 1))
        {
            return;
        }

        int lineFore = foregroundX + (foregroundY * foreground.width);
        background.initializePaint(width, height);
        int line = x + (y * this.width);
        int pixFore, pix, color, col, alpha, ahpla;

        for (int yyy = yy; yyy < height; yyy++)
        {
            pixFore = lineFore;
            pix = line;

            for (int xxx = xx; xxx < width; xxx++)
            {
                color = mask.getValue(xxx, yyy)
                        ? foreground.pixels[pixFore]
                        : background.obtainColor(xxx, yyy);
                alpha = (color >> 24) & 0xFF;

                if ((alpha == 255) || (!doAlphaMix))
                {
                    this.pixels[pix] = color;
                }
                else if (alpha > 0)
                {
                    ahpla = 256 - alpha;
                    col = this.pixels[pix];

                    this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                                       ((((((color >> 16) & 0xFF) * alpha) + (((col >> 16) & 0xFF) * ahpla)) >> 8) <<
                                        16) | //
                                       ((((((color >> 8) & 0xFF) * alpha) + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) |
                                       //
                                       ((((color & 0xFF) * alpha) + ((col & 0xFF) * ahpla)) >> 8);
                }

                pixFore++;
                pix++;
            }

            lineFore += foreground.width;
            line += this.width;
        }
    }

    /**
     * Paint mask with paint in foreground and color in background<br>
     * MUST be in draw mode
     *
     * @param x          X where paint the mask
     * @param y          Y where paint the mask
     * @param mask       Mask to paint
     * @param foreground Foreground paint
     * @param background Background color
     * @param doAlphaMix Indicates if do alpha mixing ({@code true}) or just overwrite ({@code false})
     */
    public void paintMask(
            int x, int y, final JHelpMask mask, final JHelpPaint foreground, final int background,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int w  = this.clip.xMax + 1;
        int xx = 0;
        if (x < this.clip.xMin)
        {
            xx = -x + this.clip.xMin;
            w += x - this.clip.xMin;
            x = this.clip.xMin;
        }

        int h  = this.clip.yMax + 1;
        int yy = 0;
        if (y < this.clip.yMin)
        {
            yy = -y + this.clip.yMin;
            h += y - this.clip.yMin;
            y = this.clip.yMin;
        }

        final int width  = Math2.minimum(w - x, mask.getWidth());
        final int height = Math2.minimum(h - y, mask.getHeight());

        if ((width < 1) || (height < 1))
        {
            return;
        }

        foreground.initializePaint(width, height);
        int line = x + (y * this.width);
        int pix, color, col, alpha, ahpla;

        for (int yyy = yy; yyy < height; yyy++)
        {
            pix = line;

            for (int xxx = xx; xxx < width; xxx++)
            {
                color = mask.getValue(xxx, yyy)
                        ? foreground.obtainColor(xxx, yyy)
                        : background;
                alpha = (color >> 24) & 0xFF;

                if ((alpha == 255) || (!doAlphaMix))
                {
                    this.pixels[pix] = color;
                }
                else if (alpha > 0)
                {
                    ahpla = 256 - alpha;
                    col = this.pixels[pix];

                    this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                                       ((((((color >> 16) & 0xFF) * alpha) + (((col >> 16) & 0xFF) * ahpla)) >> 8) <<
                                        16) | //
                                       ((((((color >> 8) & 0xFF) * alpha) + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) |
                                       //
                                       ((((color & 0xFF) * alpha) + ((col & 0xFF) * ahpla)) >> 8);
                }

                pix++;
            }

            line += this.width;
        }
    }

    /**
     * Paint a mask with paint in foreground and image in background<br>
     * Note : if the background is not in draw mode, all of it's visible sprite will be consider like a part of the
     * background
     * <br>
     * MUST be in draw mode
     *
     * @param x           X position for mask
     * @param y           Y position for mask
     * @param mask        Mask to paint
     * @param foreground  Foreground paint
     * @param background  Background image
     * @param backgroundX X start in background image
     * @param backgroundY Y start in background image
     * @param doAlphaMix  Indicates if do alpha mixing ({@code true}) or just overwrite ({@code false})
     */
    public void paintMask(
            int x, int y, final JHelpMask mask, final JHelpPaint foreground, final JHelpImage background,
            int backgroundX, int backgroundY,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int bw = background.width;
        if (backgroundX < 0)
        {
            bw += backgroundX;
            backgroundX = 0;
        }

        int bh = background.height;
        if (backgroundY < 0)
        {
            bh += backgroundY;
            backgroundY = 0;
        }

        int w  = this.clip.xMax + 1;
        int xx = 0;
        if (x < this.clip.xMin)
        {
            xx = -x + this.clip.xMin;
            w += x - this.clip.xMin;
            x = this.clip.xMin;
        }

        int h  = this.clip.yMax + 1;
        int yy = 0;
        if (y < this.clip.yMin)
        {
            yy = -y + this.clip.yMin;
            h += y - this.clip.yMin;
            y = this.clip.yMin;
        }

        final int width  = Math2.minimum(w - x, mask.getWidth(), bw - backgroundX);
        final int height = Math2.minimum(h - y, mask.getHeight(), bh - backgroundY);

        if ((width < 1) || (height < 1))
        {
            return;
        }

        foreground.initializePaint(width, height);
        int lineBack = backgroundX + (backgroundY * background.width);
        int line     = x + (y * this.width);
        int pixBack, pix, color, col, alpha, ahpla;

        for (int yyy = yy; yyy < height; yyy++)
        {
            pixBack = lineBack;
            pix = line;

            for (int xxx = xx; xxx < width; xxx++)
            {
                color = mask.getValue(xxx, yyy)
                        ? foreground.obtainColor(xxx, yyy)
                        : background.pixels[pixBack];
                alpha = (color >> 24) & 0xFF;

                if ((alpha == 255) || (!doAlphaMix))
                {
                    this.pixels[pix] = color;
                }
                else if (alpha > 0)
                {
                    ahpla = 256 - alpha;
                    col = this.pixels[pix];

                    this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                                       ((((((color >> 16) & 0xFF) * alpha) + (((col >> 16) & 0xFF) * ahpla)) >> 8) <<
                                        16) | //
                                       ((((((color >> 8) & 0xFF) * alpha) + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) |
                                       //
                                       ((((color & 0xFF) * alpha) + ((col & 0xFF) * ahpla)) >> 8);
                }

                pixBack++;
                pix++;
            }

            lineBack += background.width;
            line += this.width;
        }
    }

    /**
     * Paint mask with paint in foreground and background<br>
     * MUST be in draw mode
     *
     * @param x          X position for mask
     * @param y          Y position for mask
     * @param mask       Mask to paint
     * @param foreground Foreground paint
     * @param background Background paint
     * @param doAlphaMix Indicates if do alpha mixing ({@code true}) or just overwrite ({@code false})
     */
    public void paintMask(
            int x, int y, final JHelpMask mask, final JHelpPaint foreground, final JHelpPaint background,
            final boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int w  = this.clip.xMax + 1;
        int xx = 0;
        if (x < this.clip.xMin)
        {
            xx = -x + this.clip.xMin;
            w += x - this.clip.xMin;
            x = this.clip.xMin;
        }

        int h  = this.clip.yMax + 1;
        int yy = 0;
        if (y < this.clip.yMin)
        {
            yy = -y + this.clip.yMin;
            h += y - this.clip.yMin;
            y = this.clip.yMin;
        }

        final int width  = Math2.minimum(w - x, mask.getWidth());
        final int height = Math2.minimum(h - y, mask.getHeight());

        if ((width < 1) || (height < 1))
        {
            return;
        }

        foreground.initializePaint(width, height);
        background.initializePaint(width, height);
        int line = x + (y * this.width);
        int pix, color, col, alpha, ahpla;

        for (int yyy = yy; yyy < height; yyy++)
        {
            pix = line;

            for (int xxx = xx; xxx < width; xxx++)
            {
                color = mask.getValue(xxx, yyy)
                        ? foreground.obtainColor(xxx, yyy)
                        : background.obtainColor(xxx, yyy);
                alpha = (color >> 24) & 0xFF;

                if ((alpha == 255) || (!doAlphaMix))
                {
                    this.pixels[pix] = color;
                }
                else if (alpha > 0)
                {
                    ahpla = 256 - alpha;
                    col = this.pixels[pix];

                    this.pixels[pix] = (Math.min(255, alpha + ((col >> 24) & 0xFF)) << 24) | //
                                       ((((((color >> 16) & 0xFF) * alpha) + (((col >> 16) & 0xFF) * ahpla)) >> 8) <<
                                        16) | //
                                       ((((((color >> 8) & 0xFF) * alpha) + (((col >> 8) & 0xFF) * ahpla)) >> 8) << 8) |
                                       //
                                       ((((color & 0xFF) * alpha) + ((col & 0xFF) * ahpla)) >> 8);
                }

                pix++;
            }

            line += this.width;
        }
    }

    /**
     * Pick a color inside the image<br>
     * Note : if the image is not in draw mode, all visible sprite are consider as a part of image, so may obtain a
     * sprite pixel
     *
     * @param x X position
     * @param y Y position
     * @return Picked color
     */
    public int pickColor(final int x, final int y)
    {
        if ((x < 0) || (x >= this.width) || (y < 0) || (y >= this.height))
        {
            throw new IllegalArgumentException(
                    "Coordinates of peek point must be in [0, " + this.width + "[ x [0, " + this.height + "[ not (" + x
                    + ", " + y + ")");
        }

        return this.pixels[x + (y * this.width)];
    }

    /**
     * Do a task in draw mode.<br>
     * Don't call this method if image is locked. Use {@link #drawModeLocked()} to know.<br>
     * The image is locked if we are inside a task launch by {@link #playInDrawMode(ConsumerTask)} or {@link #playOutDrawMode(ConsumerTask)}
     *
     * @param task Task to do. The parameter will be this image locked in draw mode
     * @throws IllegalStateException If draw mode is locked
     */
    public void playInDrawMode(ConsumerTask<JHelpImage> task)
    {
        if (this.drawModeLocked)
        {
            throw new IllegalStateException("Draw mode is locked");
        }

        final boolean drawMode = this.drawMode;

        if (!drawMode)
        {
            this.startDrawMode();
        }

        this.drawModeLocked = true;
        task.consume(this);
        this.drawModeLocked = false;

        if (!drawMode)
        {
            this.endDrawMode();
        }
    }

    /**
     * Do a task not in draw mode.<br>
     * Don't call this method if image is locked. Use {@link #drawModeLocked()} to know.<br>
     * The image is locked if we are inside a task launch by {@link #playInDrawMode(ConsumerTask)} or {@link #playOutDrawMode(ConsumerTask)}
     *
     * @param task Task to do. The parameter will be this image locked in not draw mode
     * @throws IllegalStateException If draw mode is locked
     */
    public void playOutDrawMode(ConsumerTask<JHelpImage> task)
    {
        if (this.drawModeLocked)
        {
            throw new IllegalStateException("Draw mode is locked");
        }

        final boolean drawMode = this.drawMode;

        if (drawMode)
        {
            this.endDrawMode();
        }

        this.drawModeLocked = true;
        task.consume(this);
        this.drawModeLocked = false;

        if (drawMode)
        {
            this.startDrawMode();
        }
    }

    /**
     * Play a task when image enter in draw mode.<br>
     * If image already in draw mode, the task is played immediately.<br>
     * If image not in draw mode, task will be played next time someone call {@link #startDrawMode()}
     *
     * @param task Task to play in draw mode
     */
    public void playWhenEnterDrawMode(@NotNull ConsumerTask<JHelpImage> task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");

        if (this.drawMode)
        {
            task.consume(this);
        }
        else
        {
            synchronized (this.playInDrawMode)
            {
                this.playInDrawMode.inQueue(task);
            }
        }
    }

    /**
     * Play task when image exit from draw mode.<br>
     * If image already not in draw mode, the task is played immediately.<br>
     * If image in draw mode, task will be played next time someone call {@link #endDrawMode()}
     *
     * @param task Task to play in draw mode
     */
    public void playWhenExitDrawMode(@NotNull ConsumerTask<JHelpImage> task)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");

        if (this.drawMode)
        {
            synchronized (this.playOutDrawMode)
            {
                this.playOutDrawMode.inQueue(task);
            }
        }
        else
        {
            task.consume(this);
        }
    }

    /**
     * Pop clip from the stack
     */
    public void popClip()
    {
        if (this.clips.size() > 1)
        {
            this.clip.set(this.clips.pop());
        }
        else
        {
            this.clip.set(this.clips.peek());
        }
    }

    /**
     * Push clip to stack
     *
     * @param x      X up-left corner
     * @param y      Y up-left corner
     * @param width  Clip width
     * @param height Clip height
     */
    public void pushClip(final int x, final int y, final int width, final int height)
    {
        this.pushClip(new Clip(Math.max(x, 0), Math.min((x + width) - 1, this.width - 1), Math.max(y, 0), Math.min((y +
                                                                                                                    height) -
                                                                                                                   1,
                                                                                                                   this
                                                                                                                           .height -
                                                                                                                   1)));
    }

    /**
     * Push clip in the stack
     *
     * @param clip Clip to push
     */
    public void pushClip(final Clip clip)
    {
        if (clip == null)
        {
            throw new NullPointerException("clip MUST NOT be null");
        }

        this.clips.push(this.clip.copy());
        this.clip.set(new Clip(Math.max(clip.xMin, 0), Math.min(clip.xMax, this.width - 1), Math.max(clip.yMin, 0),
                               Math.min(clip.yMax, this.height - 1)));
    }

    /**
     * Push intersection of current clip and given one
     *
     * @param x      X up-left corner
     * @param y      Y up-left corner
     * @param width  Clip width
     * @param height Clip height
     */
    public void pushClipIntersect(final int x, final int y, final int width, final int height)
    {
        this.pushClipIntersect(new Clip(x, (x + width) - 1, y, (y + height) - 1));
    }

    /**
     * Push intersection of current clip and given one
     *
     * @param clip Given clip
     */
    public void pushClipIntersect(final Clip clip)
    {
        final Clip intersect = new Clip(Math.max(this.clip.xMin, clip.xMin), Math.min(this.clip.xMax, clip.xMax),
                                        Math.max(this.clip.yMin, clip.yMin),
                                        Math.min(this.clip.yMax, clip.yMax));
        this.clips.push(this.clip.copy());
        this.clip.set(intersect);
    }

    /**
     * Register a component to update on image change
     *
     * @param component Component to register
     */
    public void register(final Component component)
    {
        this.mutex.playInCriticalSectionVoid(() ->
                                             {
                                                 if (!this.componentsListeners.contains(component))
                                                 {
                                                     this.componentsListeners.add(component);
                                                 }
                                             });
    }

    /**
     * Remove a sprite from linked sprites.<br>
     * The sprite is no more usable<br>
     * MUST NOT be in draw mode
     *
     * @param sprite Sprite to remove
     */
    public void removeSprite(final JHelpSprite sprite)
    {
        if (this.drawMode)
        {
            throw new IllegalStateException("MUST NOT be in draw mode !");
        }

        sprite.setVisible(false);

        if (this.sprites.remove(sprite))
        {
            final int index = sprite.getSpriteIndex();
            if ((index >= 0) && (this.visibilities != null) && (this.visibilities.length > index))
            {
                System.arraycopy(this.visibilities, index + 1, this.visibilities, index,
                                 this.visibilities.length - index - 1);
            }

            for (int i = this.sprites.size() - 1; i >= 0; i--)
            {
                this.sprites.get(i)
                            .setSpriteIndex(i);
            }

            this.update();
        }
    }

    /**
     * Repeat an image on following a line
     *
     * @param x1    First point X
     * @param y1    First point Y
     * @param x2    Second point X
     * @param y2    Second point Y
     * @param image Image to repeat
     */
    public void repeatOnLine(final int x1, final int y1, final int x2, final int y2, final JHelpImage image)
    {
        this.repeatOnLine(x1, y1, x2, y2, image, true);
    }

    /**
     * Repeat an image on following a line
     *
     * @param x1         First point X
     * @param y1         First point Y
     * @param x2         Second point X
     * @param y2         Second point Y
     * @param image      Image to repeat
     * @param doAlphaMix Indicates if we want do alpha mixing
     */
    public void repeatOnLine(
            final int x1, final int y1, final int x2, final int y2, final JHelpImage image, final
    boolean doAlphaMix)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if (x1 == x2)
        {
            this.repeatOnLineVertical(x1, y1, y2, image, doAlphaMix);
            return;
        }

        if (y1 == y2)
        {
            this.repeatOnLineHorizontal(x1, x2, y1, image, doAlphaMix);
            return;
        }

        int       error = 0;
        final int dx    = Math.abs(x2 - x1);
        final int sx    = Math2.sign(x2 - x1);
        final int dy    = Math.abs(y2 - y1);
        final int sy    = Math2.sign(y2 - y1);
        int       x     = x1;
        int       y     = y1;
        final int xx    = -(image.getWidth() >> 1);
        final int yy    = -(image.getHeight() >> 1);

        if (dx >= dy)
        {
            while (((x < this.clip.xMin) || (x > this.clip.xMax) || (y < this.clip.yMin) || (y > this.clip.yMax))
                   && ((x != x2) || (y != y2)))
            {
                x += sx;

                error += dy;
                if (error >= dx)
                {
                    y += sy;

                    error -= dx;
                }
            }

            while ((x >= this.clip.xMin) && (x <= this.clip.xMax) && (x != x2)
                   && (y >= this.clip.yMin) && (y <= this.clip.yMax) && (y != y2))
            {
                this.drawImage(xx + x, yy + y, image, doAlphaMix);

                x += sx;

                error += dy;
                if (error >= dx)
                {
                    y += sy;

                    error -= dx;
                }
            }
        }
        else
        {
            while (((x < this.clip.xMin) || (x > this.clip.xMax) || (y < this.clip.yMin) || (y > this.clip.yMax))
                   && ((x != x2) || (y != y2)))
            {
                y += sy;

                error += dx;
                if (error >= dy)
                {
                    x += sx;

                    error -= dy;
                }
            }

            while ((x >= this.clip.xMin) && (x <= this.clip.xMax) && (x != x2)
                   && (y >= this.clip.yMin) && (y <= this.clip.yMax) && (y != y2))
            {
                this.drawImage(xx + x, yy + y, image, doAlphaMix);

                y += sy;

                error += dx;
                if (error >= dy)
                {
                    x += sx;

                    error -= dy;
                }
            }
        }
    }

    /**
     * Repeat an image on following a horizontal line
     *
     * @param x1    First point X
     * @param x2    Second point X
     * @param y     of line
     * @param image Image to repeat
     */
    public void repeatOnLineHorizontal(final int x1, final int x2, final int y, final JHelpImage image)
    {
        this.repeatOnLineHorizontal(x1, x2, y, image, true);
    }

    /**
     * Repeat an image on following a horizontal line
     *
     * @param x1         First point X
     * @param x2         Second point X
     * @param y          of line
     * @param image      Image to repeat
     * @param doAlphaMix Indicates if we do alpha mixing
     */
    public void repeatOnLineHorizontal(
            final int x1, final int x2, final int y, final JHelpImage image,
            final boolean doAlphaMix)
    {
        final int xx   = -(image.getWidth() >> 1);
        final int yy   = y - (image.getHeight() >> 1);
        final int xMin = xx + Math.min(x1, x2);
        final int xMax = xx + Math.max(x1, x2);

        for (int x = xMin; x <= xMax; x++)
        {
            this.drawImage(x, yy, image, doAlphaMix);
        }
    }

    /**
     * Repeat an image on following a vertical line
     *
     * @param x     Line X
     * @param y1    First point y
     * @param y2    Second point Y
     * @param image Image to repeat
     */
    public void repeatOnLineVertical(final int x, final int y1, final int y2, final JHelpImage image)
    {
        this.repeatOnLineVertical(x, y1, y2, image, true);
    }

    /**
     * Repeat an image on following a vertical line
     *
     * @param x          Line X
     * @param y1         First point y
     * @param y2         Second point Y
     * @param image      Image to repeat
     * @param doAlphaMix Indicates if we do alpha mixing
     */
    public void repeatOnLineVertical(
            final int x, final int y1, final int y2, final JHelpImage image,
            final boolean doAlphaMix)
    {
        final int xx   = x - (image.getWidth() >> 1);
        final int yy   = -(image.getHeight() >> 1);
        final int yMin = yy + Math.min(y1, y2);
        final int yMax = yy + Math.max(y1, y2);

        for (int y = yMin; y <= yMax; y++)
        {
            this.drawImage(xx, y, image, doAlphaMix);
        }
    }

    /**
     * Replace all pixels near a color by an other color<br>
     * MUST be in draw mode
     *
     * @param colorToReplace Color searched
     * @param newColor       New color
     * @param near           Distance maximum from color searched to consider to color is near
     */
    public void replaceColor(final int colorToReplace, final int newColor, final int near)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int color;
        for (int i = this.pixels.length - 1; i >= 0; i--)
        {
            color = this.pixels[i];

            if ((Math.abs(((colorToReplace >> 24) & 0xFF) - ((color >> 24) & 0xFF)) <= near)
                && (Math.abs(((colorToReplace >> 16) & 0xFF) - ((color >> 16) & 0xFF)) <= near)
                && (Math.abs(((colorToReplace >> 8) & 0xFF) - ((color >> 8) & 0xFF)) <= near)
                && (Math.abs((colorToReplace & 0xFF) - (color & 0xFF)) <= near))
            {
                this.pixels[i] = newColor;
            }
        }
    }

    /**
     * Compute the image rotated from 180 degree<br>
     * If the image is not in draw mode, visible sprites are consider like a part of image
     *
     * @return Rotated image
     */
    public JHelpImage rotate180()
    {
        final int   width  = this.width;
        final int   height = this.height;
        final int   length = width * height;
        final int[] pixels = new int[length];

        for (int pix = 0, pixR = length - 1; pixR >= 0; pix++, pixR--)
        {
            pixels[pixR] = this.pixels[pix];
        }

        return new JHelpImage(width, height, pixels);
    }

    /**
     * Compute the image rotated from 270 degree<br>
     * If the image is not in draw mode, visible sprites are consider like a part of image
     *
     * @return Rotated image
     */
    public JHelpImage rotate270()
    {
        @SuppressWarnings("SuspiciousNameCombination") final int width  = this.height;
        @SuppressWarnings("SuspiciousNameCombination") final int height = this.width;
        final int[]                                              pixels = new int[width * height];

        int       xr     = width - 1;
        final int yr     = 0;
        final int startR = yr * width;
        int       pixR   = startR + xr;

        int pix = 0;

        for (int y = 0; y < this.height; y++)
        {
            for (int x = 0; x < this.width; x++)
            {
                pixels[pixR] = this.pixels[pix];

                pix++;
                pixR += width;
            }

            xr--;
            pixR = startR + xr;
        }

        return new JHelpImage(width, height, pixels);
    }

    /**
     * Compute the image rotated from 90 degree<br>
     * If the image is not in draw mode, visible sprites are consider like a part of image
     *
     * @return Rotated image
     */
    public JHelpImage rotate90()
    {
        @SuppressWarnings("SuspiciousNameCombination") final int width  = this.height;
        @SuppressWarnings("SuspiciousNameCombination") final int height = this.width;
        final int[]                                              pixels = new int[width * height];

        int       xr     = 0;
        final int yr     = height - 1;
        final int stepR  = -width;
        final int startR = yr * width;
        int       pixR   = startR + xr;

        int pix = 0;

        for (int y = 0; y < this.height; y++)
        {
            for (int x = 0; x < this.width; x++)
            {
                pixels[pixR] = this.pixels[pix];

                pix++;
                pixR += stepR;
            }

            xr++;
            pixR = startR + xr;
        }

        return new JHelpImage(width, height, pixels);
    }

    /**
     * Extract a sub image and then rotate it from 180 degree<br>
     * If the image is not in draw mode, visible sprites are consider like a part of image
     *
     * @param x      Upper left area corner X
     * @param y      Upper left area corner Y
     * @param width  Area to extract width
     * @param height Area to extract height
     * @return Result image
     */
    public JHelpImage rotatedPart180(final int x, final int y, final int width, final int height)
    {
        return this.extractSubImage(x, y, width, height)
                   .rotate180();
    }

    /**
     * Extract a sub image and then rotate it from 270 degree<br>
     * If the image is not in draw mode, visible sprites are consider like a part of image
     *
     * @param x      Upper left area corner X
     * @param y      Upper left area corner Y
     * @param width  Area to extract width
     * @param height Area to extract height
     * @return Result image
     */
    public JHelpImage rotatedPart270(final int x, final int y, final int width, final int height)
    {
        return this.extractSubImage(x, y, width, height)
                   .rotate270();
    }

    /**
     * Extract a sub image and then rotate it from 90 degree<br>
     * If the image is not in draw mode, visible sprites are consider like a part of image
     *
     * @param x      Upper left area corner X
     * @param y      Upper left area corner Y
     * @param width  Area to extract width
     * @param height Area to extract height
     * @return Result image
     */
    public JHelpImage rotatedPart90(final int x, final int y, final int width, final int height)
    {
        return this.extractSubImage(x, y, width, height)
                   .rotate90();
    }

    /**
     * Change one pixel color.<br>
     * Must be in draw mode
     *
     * @param x     X
     * @param y     Y
     * @param color Color
     */
    public void setPixel(final int x, final int y, final int color)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if ((x < 0) || (x >= this.width) || (y < 0) || (y >= this.height))
        {
            return;
        }

        this.pixels[x + (y * this.width)] = color;
    }

    /**
     * Change a pixels area.<br>
     * MUST be in draw mode
     *
     * @param x      X up-left corner
     * @param y      Y up-left corner
     * @param width  Width of image in pixels array
     * @param height Height of image in pixels array
     * @param pixels Pixels array
     */
    public void setPixels(final int x, final int y, final int width, final int height, final int[] pixels)
    {
        this.setPixels(x, y, width, height, pixels, 0);
    }

    /**
     * Change a pixels area.<br>
     * MUST be in draw mode
     *
     * @param x      X up-left corner
     * @param y      Y up-left corner
     * @param width  Width of image in pixels array
     * @param height Height of image in pixels array
     * @param pixels Pixels array
     * @param offset Offset where start read pixels data
     */
    public void setPixels(int x, int y, int width, int height, final int[] pixels, final int offset)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        if ((x == 0) && (y == 0) && (width == this.width) && (height == this.height) && (offset == 0))
        {
            System.arraycopy(pixels, 0, this.pixels, 0, this.pixels.length);
            return;
        }

        if (x < 0)
        {
            width += x;
            x = 0;
        }

        if (y < 0)
        {
            height += y;
            y = 0;
        }

        final int w = Math2.minimum(this.width - x, width);
        final int h = Math2.minimum(this.height - y, height);

        if ((w <= 0) || (h <= 0))
        {
            return;
        }

        int lineThis  = x + (y * this.width);
        int lineImage = offset;

        for (int yy = 0; yy < h; yy++)
        {
            System.arraycopy(pixels, lineImage, this.pixels, lineThis, w);

            lineThis += this.width;
            lineImage += width;
        }
    }

    /**
     * Change image global transparency<br>
     * MUST be in draw mode
     *
     * @param alpha New global transparency
     */
    public void setTransparency(final int alpha)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        final int alphaPart = Math2.limit0_255(alpha) << 24;
        int       color;

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            color = this.pixels[pix];
            this.pixels[pix] = alphaPart | (color & 0x00FFFFFF);
        }
    }

    /**
     * Shift (translate) the image<br>
     * MUST be in draw mode
     *
     * @param x X shift
     * @param y Y shift
     */
    public void shift(final int x, final int y)
    {
        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int       index = x + (y * this.width);
        final int size  = this.pixels.length;
        index = index % size;

        if (index < 0)
        {
            index += size;
        }

        final int[] temp = new int[size];
        System.arraycopy(this.pixels, 0, temp, 0, size);

        for (int i = 0; i < size; i++)
        {
            this.pixels[i] = temp[index];

            index = (index + 1) % size;
        }
    }

    /**
     * Force a sprite to be over the others
     *
     * @param sprite Sprite to put at top
     */
    public void spriteAtTop(final JHelpSprite sprite)
    {
        if (sprite == null)
        {
            return;
        }

        final int index = this.sprites.indexOf(sprite);

        if (index < 0)
        {
            return;
        }

        final boolean oldDrawMode = this.drawMode;

        if (oldDrawMode)
        {
            this.endDrawMode();
        }

        final int       size     = this.sprites.size();
        final boolean[] visibles = new boolean[size];

        for (int i = 0; i < size; i++)
        {
            visibles[i] = this.sprites.get(i)
                                      .isVisible();
            this.sprites.get(i)
                        .setVisible(false);
        }

        final boolean visible = visibles[index];
        System.arraycopy(visibles, index + 1, visibles, index, size - index - 1);
        visibles[size - 1] = visible;

        this.sprites.remove(index);
        this.sprites.add(sprite);

        for (int i = 0; i < size; i++)
        {
            this.sprites.get(i)
                        .setSpriteIndex(i);
            this.sprites.get(i)
                        .setVisible(visibles[i]);
        }

        if (oldDrawMode)
        {
            this.startDrawMode();
        }
    }

    /**
     * Start the draw mode.<br>
     * Don't call this method if image is locked. Use {@link #drawModeLocked()} to know.<br>
     * The image is locked if we are inside a task launch by {@link #playInDrawMode(ConsumerTask)} or {@link #playOutDrawMode(ConsumerTask)}
     *
     * @throws IllegalStateException If draw mode is locked
     */
    public void startDrawMode()
    {
        if (this.drawModeLocked)
        {
            throw new IllegalStateException("Draw mode is locked");
        }

        if (!this.drawMode)
        {
            this.drawMode = true;

            final int length = this.sprites.size();

            if ((this.visibilities == null) || (this.visibilities.length != length))
            {
                this.visibilities = new boolean[length];
            }

            boolean     visible;
            JHelpSprite sprite;

            for (int index = length - 1; index >= 0; index--)
            {
                sprite = this.sprites.get(index);
                visible = this.visibilities[index] = sprite.isVisible();

                if (visible)
                {
                    sprite.changeVisible(false);
                }
            }

            synchronized (this.playInDrawMode)
            {
                this.drawModeLocked = true;

                while (!this.playInDrawMode.empty())
                {
                    this.playInDrawMode.outQueue().consume(this);
                }

                this.drawModeLocked = false;
            }
        }
    }

    /**
     * Subtract the image by an other one<br>
     * Note : if the given image is not in draw mode, all of it's visible sprite will be consider like a part of the
     * given image
     * <br>
     * Given image MUST have same dimension of this <br>
     * MUST be in draw mode
     *
     * @param image Image to subtract
     */
    public void subtract(final JHelpImage image)
    {
        if ((this.width != image.width) || (this.height != image.height))
        {
            throw new IllegalArgumentException("We can only subtract with an image of same size");
        }

        if (!this.drawMode)
        {
            throw new IllegalStateException("Must be in draw mode !");
        }

        int colorThis, colorImage;

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            colorThis = this.pixels[pix];
            colorImage = image.pixels[pix];

            this.pixels[pix] = (colorThis & 0xFF000000) | //
                               (Math2.limit0_255(((colorThis >> 16) & 0xFF) - ((colorImage >> 16) & 0xFF)) << 16) | //
                               (Math2.limit0_255(((colorThis >> 8) & 0xFF) - ((colorImage >> 8) & 0xFF)) << 8) | //
                               Math2.limit0_255((colorThis & 0xFF) - (colorImage & 0xFF));
        }
    }

    /**
     * Tint image.<br>
     * MUST be in draw mode
     *
     * @param color Color to tint with
     */
    public void tint(final int color)
    {
        this.gray();
        final int red   = (color >> 16) & 0xFF;
        final int green = (color >> 8) & 0xFF;
        final int blue  = color & 0xFF;
        int       col;
        int       gray;

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            col = this.pixels[pix];
            gray = col & 0xFF;
            this.pixels[pix] = (col & 0xFF000000) | (((red * gray) >> 8) << 16) | (((green * gray) >> 8) << 8) | ((blue
                                                                                                                   *
                                                                                                                   gray) >>
                                                                                                                  8);
        }
    }

    /**
     * Tint image.<br>
     * MUST be in draw mode
     *
     * @param colorHigh Color for "high" value
     * @param colorLow  Color for "low" value
     */
    public void tint(final int colorHigh, final int colorLow)
    {
        this.gray();
        final int redHigh   = (colorHigh >> 16) & 0xFF;
        final int greenHigh = (colorHigh >> 8) & 0xFF;
        final int blueHigh  = colorHigh & 0xFF;
        final int redLow    = (colorLow >> 16) & 0xFF;
        final int greenLow  = (colorLow >> 8) & 0xFF;
        final int blueLow   = colorLow & 0xFF;
        int       col;
        int       gray;
        int       yarg;

        for (int pix = this.pixels.length - 1; pix >= 0; pix--)
        {
            col = this.pixels[pix];
            gray = col & 0xFF;
            yarg = 256 - gray;
            this.pixels[pix] = (col & 0xFF000000)
                               | ((((redHigh * gray) + (redLow * yarg)) >> 8) << 16)
                               | ((((greenHigh * gray) + (greenLow * yarg)) >> 8) << 8)
                               | (((blueHigh * gray) + (blueLow * yarg)) >> 8);
        }
    }

    /**
     * Give all sprites of this image to an other image
     *
     * @param image Image will receive this image sprites
     */
    public void transfertSpritesTo(final JHelpImage image)
    {
        final boolean drawMode = image.drawMode;
        final boolean draw     = this.drawMode;

        image.endDrawMode();
        this.endDrawMode();
        boolean visible;

        for (final JHelpSprite sprite : this.sprites)
        {
            visible = sprite.isVisible();
            sprite.setVisible(false);
            sprite.setParent(image);
            image.sprites.add(sprite);
            sprite.setVisible(visible);
        }

        this.sprites.clear();

        image.update();

        if (drawMode)
        {
            image.startDrawMode();
        }

        if (draw)
        {
            this.startDrawMode();
        }
    }

    /**
     * Unregister a component
     *
     * @param component Component to unregister
     */
    public void unregister(final Component component)
    {
        this.mutex.playInCriticalSectionVoid(() -> this.componentsListeners.remove(component));
    }

    /**
     * Update the image, to see last changes
     */
    public void update()
    {
        final boolean onDraw = this.drawMode;

        if (onDraw)
        {
            this.endDrawMode();
        }

        this.memoryImageSource.newPixels();

        if (onDraw)
        {
            this.startDrawMode();
        }

        this.mutex.playInCriticalSectionVoid(() ->
                                             {
                                                 for (final Component component : this.componentsListeners)
                                                 {
                                                     component.invalidate();
                                                     component.validate();
                                                     component.repaint();
                                                 }
                                             });
    }
}
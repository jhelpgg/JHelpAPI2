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
package jhelp.engine2.render;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import jhelp.util.math.JHelpRandom;
import jhelp.util.math.Math2;
import jhelp.util.util.Utilities;

/**
 * Interpolator of 2 textures in a result one
 *
 * @author JHelp
 */
public class TextureInterpolator
{
    /**
     * Interpolation type
     *
     * @author JHelp
     */
    public enum InterpolationType
    {
        /**
         * Start by image borders
         */
        BORDER,
        /**
         * Start by image corners
         */
        CORNER,
        /**
         * Melt the two texture
         */
        MELTED,
        /**
         * Randomly replace pixels
         */
        RANDOM,
        /**
         * Replace linearly
         */
        REPLACEMENT,
        /**
         * Choose the mode randomly each time a transition is started or end
         */
        UNDEFINED
    }

    /**
     * Actual interpolation type
     */
    @NotNull
    private       InterpolationType actualInterpolationType;
    /**
     * Actual factor
     */
    private       double            factor;
    /**
     * Texture height
     */
    private final int               height;
    /**
     * Indexes of pixels in random order
     */
    private final int[]             indexes;
    /**
     * Type of interpolation defined
     */
    @NotNull
    private final InterpolationType interpolationType;
    /**
     * Size of textures in bytes
     */
    private final int               length;
    /**
     * Size of texture in pixels
     */
    private final int               lengthSmall;
    /**
     * Target texture
     */
    private final Texture           textureEnd;
    /**
     * Interpolated texture
     */
    private final Texture           textureInterpolated;
    /**
     * Source texture
     */
    private final Texture           textureStart;
    /**
     * Texture width
     */
    private final int               width;

    /**
     * Create a new instance of TextureInterpolator
     *
     * @param textureStart Texture start
     * @param textureEnd   Texture end
     * @param name         Interpolation name
     */
    public TextureInterpolator(
            final @NotNull Texture textureStart, final @NotNull Texture textureEnd, final @NotNull String name)
    {
        this(textureStart, textureEnd, name, 0, InterpolationType.UNDEFINED);
    }

    /**
     * Create a new instance of TextureInterpolator
     *
     * @param textureStart      Texture start
     * @param textureEnd        Texture end
     * @param name              Interpolation name
     * @param factor            Starting factor in [0, 1]
     * @param interpolationType Interpolation type
     * @throws IllegalArgumentException If factor not in [0, 1]
     */
    public TextureInterpolator(
            final @NotNull Texture textureStart, final @NotNull Texture textureEnd, final @NotNull String name,
            final double factor, final @Nullable InterpolationType interpolationType)
    {
        this.interpolationType = interpolationType == null
                                 ? InterpolationType.UNDEFINED
                                 : interpolationType;
        this.actualInterpolationType = this.interpolationType;
        this.width = textureStart.width();
        this.height = textureStart.height();

        if ((textureEnd.width() != this.width) || (textureEnd.height() != this.height))
        {
            throw new IllegalArgumentException("The textures must have same dimensions");
        }

        this.lengthSmall = this.width * this.height;
        this.length = this.width * this.height * 4;
        this.textureStart = textureStart;
        this.textureEnd = textureEnd;
        this.textureInterpolated = new Texture(name, this.width, this.height);

        this.indexes = new int[this.lengthSmall];
        for (int i = 0; i < this.lengthSmall; i++)
        {
            this.indexes[i] = i;
        }

        Utilities.scramble(this.indexes);

        this.factor(factor);
    }

    /**
     * Create a new instance of TextureInterpolator
     *
     * @param textureStart      Texture start
     * @param textureEnd        Texture end
     * @param name              Interpolation name
     * @param interpolationType Interpolation type
     */
    public TextureInterpolator(
            final @NotNull Texture textureStart, final @NotNull Texture textureEnd, final @NotNull String name,
            final @Nullable InterpolationType interpolationType)
    {
        this(textureStart, textureEnd, name, 0, interpolationType);
    }

    /**
     * Change interpolation factor
     *
     * @param factor New factor in [0, 1]
     * @return Interpolated texture
     * @throws IllegalArgumentException If factor not in [0, 1]
     */
    public @NotNull Texture factor(final double factor)
    {
        if ((factor < 0) || (factor > 1))
        {
            throw new IllegalArgumentException("Factor must be in [0, 1], not " + factor);
        }

        this.factor = factor;

        if (Math2.isNul(factor) || Math2.equals(factor, 1))
        {
            Utilities.scramble(this.indexes);
            this.actualInterpolationType = this.interpolationType;
        }

        return this.updateTextureInterpolated();
    }

    /**
     * Interpolated texture
     *
     * @return Interpolated texture
     */
    public @NotNull Texture textureInterpolated()
    {
        return this.textureInterpolated;
    }

    /**
     * Force refresh the interpolated texture
     *
     * @return Interpolated texture
     */
    public @NotNull Texture updateTextureInterpolated()
    {
        final byte[] pixelsStart        = this.textureStart.pixels;
        final byte[] pixelsEnd          = this.textureEnd.pixels;
        final byte[] pixelsInterpolated = this.textureInterpolated.pixels;

        while (this.actualInterpolationType == InterpolationType.UNDEFINED)
        {
            this.actualInterpolationType = JHelpRandom.random(InterpolationType.class);
        }

        final double rotcaf = 1.0 - this.factor;
        int          nb, bn, index, minX, maxX, minY, maxY, w, h, pix;

        switch (this.actualInterpolationType)
        {
            case MELTED:
                for (int i = this.length - 1; i >= 0; i--)
                {
                    pixelsInterpolated[i] = (byte) (((pixelsStart[i] & 0xFF) * rotcaf) +
                                                    ((pixelsEnd[i] & 0xFF) * this.factor));
                }
                break;
            case RANDOM:
                System.arraycopy(pixelsStart, 0, pixelsInterpolated, 0, this.length);
                nb = (int) (this.factor * this.lengthSmall);
                for (int i = 0; i < nb; i++)
                {
                    index = this.indexes[i] * 4;
                    pixelsInterpolated[index] = pixelsEnd[index];
                    index++;
                    pixelsInterpolated[index] = pixelsEnd[index];
                    index++;
                    pixelsInterpolated[index] = pixelsEnd[index];
                    index++;
                    pixelsInterpolated[index] = pixelsEnd[index];
                }
                break;
            case REPLACEMENT:
                nb = (int) (rotcaf * this.length);
                bn = this.length - nb;

                if (nb > 0)
                {
                    System.arraycopy(pixelsStart, 0, pixelsInterpolated, 0, nb);
                }

                if (bn > 0)
                {
                    System.arraycopy(pixelsEnd, nb, pixelsInterpolated, nb, bn);
                }
                break;
            case CORNER:
                System.arraycopy(pixelsStart, 0, pixelsInterpolated, 0, this.length);
                w = (int) (this.factor * this.width * 0.5);
                h = (int) (this.factor * this.height * 0.5);

                if ((w > 0) && (h > 0))
                {
                    minX = w;
                    maxX = this.width - w;
                    minY = h;
                    maxY = this.height - h;

                    pix = 0;
                    for (int y = 0; y < this.height; y++)
                    {
                        if ((y <= minY) || (y >= maxY))
                        {
                            for (int x = 0; x < this.width; x++)
                            {
                                if ((x <= minX) || (x >= maxX))
                                {
                                    index = pix * 4;
                                    pixelsInterpolated[index] = pixelsEnd[index];
                                    index++;
                                    pixelsInterpolated[index] = pixelsEnd[index];
                                    index++;
                                    pixelsInterpolated[index] = pixelsEnd[index];
                                    index++;
                                    pixelsInterpolated[index] = pixelsEnd[index];
                                }

                                pix++;
                            }
                        }
                        else
                        {
                            pix += this.width;
                        }
                    }
                }
                break;
            case BORDER:
                System.arraycopy(pixelsStart, 0, pixelsInterpolated, 0, this.length);
                w = (int) (this.factor * this.width * 0.5);
                h = (int) (this.factor * this.height * 0.5);

                if ((w > 0) && (h > 0))
                {
                    minX = w;
                    maxX = this.width - w;
                    minY = h;
                    maxY = this.height - h;

                    pix = 0;
                    for (int y = 0; y < this.height; y++)
                    {
                        for (int x = 0; x < this.width; x++)
                        {
                            if ((y <= minY) || (y >= maxY) || (x <= minX) || (x >= maxX))
                            {
                                index = pix * 4;
                                pixelsInterpolated[index] = pixelsEnd[index];
                                index++;
                                pixelsInterpolated[index] = pixelsEnd[index];
                                index++;
                                pixelsInterpolated[index] = pixelsEnd[index];
                                index++;
                                pixelsInterpolated[index] = pixelsEnd[index];
                            }

                            pix++;
                        }
                    }
                }
                break;
            case UNDEFINED:
                // Already treat above
                break;
            default:
                // Should never arrive
                break;
        }

        this.textureInterpolated.flush();

        return this.textureInterpolated;
    }
}
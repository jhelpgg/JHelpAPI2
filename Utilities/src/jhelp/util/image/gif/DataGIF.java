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

package jhelp.util.image.gif;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import jhelp.util.gui.JHelpImage;
import jhelp.util.io.UtilIO;
import jhelp.util.math.rational.Rational;
import jhelp.util.thread.Pointer;

/**
 * Represents data of GIF<br>
 *
 * @author JHelp
 * @see <a href="http://www.w3.org/Graphics/GIF/spec-gif89a.txt">GIF specification</a>
 */
public class DataGIF
        implements GIFConstants
{
    /**
     * Compute size of an GIF image.<br>
     * If the given file is not a GIF image file, {@code null} is return
     *
     * @param file Image GIF file
     * @return GIF image size OR {@code null} if given file not a valid GIF image file
     */
    public static Dimension computeGifSize(final File file)
    {
        if ((file == null) || (!file.exists()) || (file.isDirectory()) || (!file.canRead()))
        {
            return null;
        }

        Pointer<Dimension> pointer = new Pointer<>();
        UtilIO.treatInputStream(() -> new FileInputStream(file),
                                inputStream ->
                                {
                                    final DataGIF dataGIF = new DataGIF();
                                    dataGIF.readHeader(inputStream);
                                    dataGIF.readLogicalScreen(inputStream);
                                    pointer.data(new Dimension(dataGIF.getWidth(), dataGIF.getHeight()));
                                });
        return pointer.data();
    }

    /**
     * Indicates if a file is a GIF image file
     *
     * @param file Tested file
     * @return {@code true} if the file is a GIF image file
     */
    public static boolean isGIF(final File file)
    {
        return DataGIF.computeGifSize(file) != null;
    }

    /**
     * Aspect ratio
     */
    private Rational      aspectRatio;
    /**
     * Background color index
     */
    private int           backgroundColorIndex;
    /**
     * Blocks read from stream
     */
    private List<Block>   blocks;
    /**
     * Indicates if color table is ordered
     */
    private boolean       colorOrdered;
    /**
     * Color resolution
     */
    private int           colorResolution;
    /**
     * Global color table
     */
    private GIFColorTable globalColorTable;
    /**
     * Indicates if color table follows
     */
    private boolean       globalTableColorFollow;
    /**
     * Global color table size
     */
    private int           globalTableSize;
    /**
     * Image height
     */
    private int           height;
    /**
     * GIF version
     */
    private String        version;
    /**
     * Image width
     */
    private int           width;

    /**
     * Create a new instance of DataGIF
     */
    public DataGIF()
    {
    }

    /**
     * Read image header
     *
     * @param inputStream Stream to read
     * @throws IOException If header is invalid
     */
    private void readHeader(final InputStream inputStream) throws IOException
    {
        final String header = UtilGIF.readString(3, inputStream);

        if (!GIFConstants.HEADER_GIF.equals(header))
        {
            throw new IOException("Invalid GIF file, wrong header : " + header);
        }

        this.version = UtilGIF.readString(3, inputStream);

        if ((!GIFConstants.VERSION_87_A.equals(this.version)) && (!GIFConstants.VERSION_89_A.equals(this.version)))
        {
            throw new IOException("Invalid GIF file, wrong version : " + this.version);
        }
    }

    /**
     * Read logical screen information
     *
     * @param inputStream Stream to read
     * @throws IOException If stream contains invalid data for logical screen
     */
    private void readLogicalScreen(final InputStream inputStream) throws IOException
    {
        this.width = UtilGIF.read2ByteInt(inputStream);
        this.height = UtilGIF.read2ByteInt(inputStream);
        final int flags = inputStream.read();

        if (flags < 0)
        {
            throw new IOException("No enough data to read logical screen flags");
        }

        this.globalTableColorFollow = (flags & GIFConstants.MASK_COLOR_TABLE_FOLLOW) != 0;
        this.colorResolution =
                ((flags & GIFConstants.MASK_COLOR_RESOLUTION) >> GIFConstants.SHIFT_COLOR_RESOLUTION) + 1;
        this.colorOrdered = (flags & GIFConstants.MASK_GLOBAL_COLOR_TABLE_ORDERED) != 0;
        this.globalTableSize = 1 << ((flags & GIFConstants.MASK_GLOBAL_COLOR_TABLE_SIZE) + 1);

        this.backgroundColorIndex = inputStream.read();

        if (this.backgroundColorIndex < 0)
        {
            throw new IOException("No enough data to read background index");
        }

        final int pixelAspectRatio = inputStream.read();

        if (pixelAspectRatio < 0)
        {
            throw new IOException("No enough data to read pixel Aspect Ratio");
        }

        this.aspectRatio = Rational.createRational(pixelAspectRatio + 15, 64);

        this.globalColorTable = new GIFColorTable(this.colorResolution, this.colorOrdered, this.globalTableSize);

        if (!this.globalTableColorFollow)
        {
            this.globalColorTable.initializeDefault();
        }
        else
        {
            this.globalColorTable.read(inputStream);
        }
    }

    /**
     * Collect images from data GIF
     *
     * @param dataGIFVisitor Visitor to signal progression
     */
    public void collectImages(final DataGIFVisitor dataGIFVisitor)
    {
        dataGIFVisitor.startCollecting(this.width, this.height);

        final JHelpImage     baseImage            = new JHelpImage(this.width, this.height);
        GIFColorTable        colorTable           = this.globalColorTable;
        GIFColorTable        localColorTable      = null;
        GraphicControlBlock  graphicControlBlock  = null;
        ImageDescriptorBlock imageDescriptorBlock = null;
        int                  disposalMethod, transparencyIndex;
        int[]                pixels, indexes;
        long                 time;
        JHelpImage           image;
        int                  imageX, imageY, imageWidth, imageHeight;
        int                  background;

        for (final Block block : this.blocks)
        {
            switch (block.getType())
            {
                case GIFConstants.BLOCK_IMAGE_DESCRIPTOR:
                    imageDescriptorBlock = (ImageDescriptorBlock) block;
                    break;
                case GIFConstants.BLOCK_EXTENSION:
                    switch (((BlockExtension) block).getSubType())
                    {
                        case GIFConstants.BLOCK_EXTENSION_GRAPHIC_CONTROL:
                            graphicControlBlock = (GraphicControlBlock) block;
                            break;
                    }
                    break;
            }

            if (imageDescriptorBlock != null)
            {
                disposalMethod = GIFConstants.DISPOSAL_METHOD_UNSPECIFIED;
                transparencyIndex = -1;
                time = GIFConstants.DEFAULT_TIME;
                colorTable = this.globalColorTable;

                if (graphicControlBlock != null)
                {
                    disposalMethod = graphicControlBlock.getDisposalMethod();
                    transparencyIndex = graphicControlBlock.getTransparencyIndex();
                    time = graphicControlBlock.getTime(GIFConstants.DEFAULT_TIME);
                }

                imageX = imageDescriptorBlock.getX();
                imageY = imageDescriptorBlock.getY();
                imageWidth = imageDescriptorBlock.getWidth();
                imageHeight = imageDescriptorBlock.getHeight();
                localColorTable = imageDescriptorBlock.getLocalColorTable();

                if (localColorTable != null)
                {
                    colorTable = localColorTable;
                }

                if ((this.backgroundColorIndex == transparencyIndex) ||
                    ((this.backgroundColorIndex == 0) && (transparencyIndex >= 0)))
                {
                    background = 0;
                }
                else
                {
                    background = colorTable.getColor(this.backgroundColorIndex);
                }

                image = baseImage.createCopy();
                pixels = image.getPixels(imageX, imageY, imageWidth, imageHeight);
                final int length = pixels.length;
                int       index;
                indexes = imageDescriptorBlock.getColorIndexes();

                for (int pix = 0; pix < length; pix++)
                {
                    index = indexes[pix];

                    if (index != transparencyIndex)
                    {
                        pixels[pix] = colorTable.getColor(index);
                    }
                }

                image.startDrawMode();
                image.setPixels(imageX, imageY, imageWidth, imageHeight, pixels);
                image.endDrawMode();

                dataGIFVisitor.nextImage(time, image);

                switch (disposalMethod)
                {
                    case GIFConstants.DISPOSAL_METHOD_UNSPECIFIED:
                    case GIFConstants.DISPOSAL_METHOD_NOT_DISPOSE:
                        baseImage.startDrawMode();
                        baseImage.drawImage(0, 0, image);
                        baseImage.endDrawMode();
                        break;
                    case GIFConstants.DISPOSAL_METHOD_RESTORE_BACKGROUND_COLOR:
                        baseImage.startDrawMode();
                        baseImage.fillRectangle(imageX, imageY, imageWidth, imageHeight, background, false);
                        baseImage.endDrawMode();
                        break;
                    case GIFConstants.DISPOSAL_METHOD_RESTORE_PREVIOUS:
                        break;
                }

                graphicControlBlock = null;
            }

            imageDescriptorBlock = null;
        }

        dataGIFVisitor.endCollecting();
    }

    /**
     * Aspect ratio
     *
     * @return Aspect ratio
     */
    public Rational getAspectRatio()
    {
        return this.aspectRatio;
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
     * GIF version
     *
     * @return GIF version
     */
    public String getVersion()
    {
        return this.version;
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
     * Read stream to fill data GIF
     *
     * @param inputStream Stream to read
     * @throws IOException If stream not a valid GIF
     */
    public void read(final InputStream inputStream) throws IOException
    {
        this.blocks = new ArrayList<>();
        this.readHeader(inputStream);
        this.readLogicalScreen(inputStream);

        Block block = Block.readBlock(inputStream, this.colorResolution);

        while (block != EndBlock.END_BLOCK)
        {
            this.blocks.add(block);

            block = Block.readBlock(inputStream, this.colorResolution);
        }
    }
}
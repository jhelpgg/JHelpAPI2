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
package jhelp.gui.twoD;

import java.awt.Dimension;
import java.io.File;
import java.util.Arrays;
import jhelp.gui.ResourcesGUI;
import jhelp.util.cache.Cache;
import jhelp.util.cache.CacheElement;
import jhelp.util.filter.FileFilter;
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpMask;
import jhelp.util.gui.TextCutter;
import jhelp.util.io.ComparatorFile;
import jhelp.util.list.LimitedWeightHashMapTime;
import jhelp.util.resources.Resources;

/**
 * Model used by file explorer
 *
 * @author JHelp
 */
public class JHelpFileExplorerModel
        extends JHelpListModel<File>
{
    static
    {
        FILTER_IMAGES = FileFilter.createFilterForImageByFileImageInformation();
        JHelpFileExplorerModel.FILTER_IMAGES.acceptDirectory(false);

        FILTER_SOUNDS = new FileFilter();
        JHelpFileExplorerModel.FILTER_SOUNDS.acceptDirectory(false);
        JHelpFileExplorerModel.FILTER_SOUNDS.addExtension("mp3");
        JHelpFileExplorerModel.FILTER_SOUNDS.addExtension("mid");
        JHelpFileExplorerModel.FILTER_SOUNDS.addExtension("au");
        JHelpFileExplorerModel.FILTER_SOUNDS.addExtension("wav");

        FONT = new JHelpFont("Monospaced", 18);
        TEXT_WIDTH = JHelpFileExplorerModel.FONT.getMaximumCharacterWidth() << 4;
        WIDTH_LIMITER = new TextCutter(JHelpFileExplorerModel.TEXT_WIDTH, JHelpFileExplorerModel.FONT.getFont());
        CELL_SIZE = new Dimension(JHelpFileExplorerModel.TEXT_WIDTH + 64, 64);
    }

    /**
     * Preview element for icons or previews
     *
     * @author JHelp
     */
    static class PreviewElement
            extends CacheElement<JHelpImage>
    {
        /**
         * Preview source file
         */
        private final File   file;
        /**
         * Icon resource name
         */
        private final String resource;

        /**
         * Create a new instance of PreviewElement for image file
         *
         * @param file Image source file
         */
        PreviewElement(final File file)
        {
            this.file = file;
            this.resource = null;
        }

        /**
         * Create a new instance of PreviewElement for icon resource
         *
         * @param resource Icon resource name
         */
        PreviewElement(final String resource)
        {
            this.file = null;
            this.resource = resource;
        }

        /**
         * Create associated image <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @return Created image
         * @see CacheElement#create()
         */
        @Override
        protected JHelpImage create()
        {
            JHelpImage image = null;

            if (this.resource != null)
            {
                if (JHelpFileExplorerModel.resources != null)
                {
                    try
                    {
                        image = JHelpFileExplorerModel.resources.obtainJHelpImage(this.resource);
                    }
                    catch (final Exception exception)
                    {
                        image = null;
                    }
                }

                if (image == null)
                {
                    try
                    {
                        image = ResourcesGUI.RESOURCES.obtainJHelpImage(this.resource);
                    }
                    catch (final Exception exception)
                    {
                        image = null;
                    }
                }

                return image;
            }

            try
            {
                image = JHelpImage.loadImageThumb(this.file, 64, 64);

                if (image != null)
                {
                    return image;
                }
            }
            catch (final Exception ignored)
            {
            }

            return JHelpFileExplorerModel.PREVIEWS.get("file.png", JHelpFileExplorerModel.FILE);
        }
    }

    /**
     * List cell size
     */
    private static final Dimension CELL_SIZE;
    /**
     * Map of created images
     */
    private static final LimitedWeightHashMapTime<File, JHelpImage> CREATED_IMAGES = new LimitedWeightHashMapTime<File, JHelpImage>(

            128L * 1024L * 1024L);
    /**
     * Preview for directories
     */
    private static final PreviewElement                             DIRECTORY      = new PreviewElement(
            "directory.png");
    /**
     * Image filter, to know if a file is an image
     */
    private static final FileFilter FILTER_IMAGES;
    /**
     * Sound filter, to know if a file is a sound
     */
    private static final FileFilter FILTER_SOUNDS;
    /**
     * Font to use
     */
    private static final JHelpFont  FONT;
    /**
     * Foreground color
     */
    private static final int            FOREGROUND = 0xFF000000;
    /**
     * Prview for music/sound file
     */
    private static final PreviewElement MUSIC      = new PreviewElement("music.png");
    /**
     * Text width
     */
    private static final int        TEXT_WIDTH;
    /**
     * Limiter of text width
     */
    private static final TextCutter WIDTH_LIMITER;
    /**
     * Preview defualt for files
     */
    static final PreviewElement    FILE     = new PreviewElement("file.png");
    /**
     * Go parent special fime
     */
    static final File              PARENT   = new File("..");
    /**
     * Previews cache
     */
    static final Cache<JHelpImage> PREVIEWS = new Cache<JHelpImage>();
    /**
     * Resources to use
     */
    static Resources resources;

    /**
     * Compute associated image from a file
     *
     * @param file File to get the image
     * @return Computed image
     */
    private static JHelpImage obtainImage(final File file)
    {
        if (file == JHelpFileExplorerModel.PARENT)
        {
            return JHelpFileExplorerModel.PREVIEWS.get("directory.png", JHelpFileExplorerModel.DIRECTORY);
        }

        if (!JHelpFileExplorerModel.FILTER_IMAGES.accept(file))
        {
            if (!JHelpFileExplorerModel.FILTER_SOUNDS.accept(file))
            {
                if (file.isDirectory())
                {
                    return JHelpFileExplorerModel.PREVIEWS.get("directory.png", JHelpFileExplorerModel.DIRECTORY);
                }

                return JHelpFileExplorerModel.PREVIEWS.get("file.png", JHelpFileExplorerModel.FILE);
            }

            return JHelpFileExplorerModel.PREVIEWS.get("music.png", JHelpFileExplorerModel.MUSIC);
        }

        return JHelpFileExplorerModel.PREVIEWS.get(file.getAbsolutePath(), new PreviewElement(file));
    }

    /**
     * Change resources access for icons.<br>
     * The set of resources MUST contains the following keys/ressource pair :
     * <table border=1>
     * <tr>
     * <th>Key</th>
     * <th>Description</th>
     * </tr>
     * <tr>
     * <td>directory.png</td>
     * <td>Image used for represents a directory. Size 64x64</td>
     * </tr>
     * <tr>
     * <td>file.png</td>
     * <td>Image used for represents a default file. Size 64x64</td>
     * </tr>
     * <tr>
     * <td>music.png</td>
     * <td>Image used for represents a music/sound file. Size 64x64</td>
     * </tr>
     * </table>
     * <br>
     * It recommends to call this method before any usage of file explorer, else it can be make time to refresh previews
     *
     * @param resources New Resources access
     */
    public static void resources(final Resources resources)
    {
        if (resources == null)
        {
            throw new NullPointerException("resources mustn't be null");
        }

        JHelpFileExplorerModel.resources = resources;
    }

    /**
     * Actual directory files
     */
    private File[]     content;
    /**
     * Actual directory
     */
    private File       directory;
    /**
     * Actual file filter
     */
    private FileFilter fileFilter;

    /**
     * Create a new instance of JHelpFileExplorerModel for all ordinary files and directories, starting in root of computer
     */
    public JHelpFileExplorerModel()
    {
        this(null, new FileFilter(false, false));
    }

    /**
     * Create a new instance of JHelpFileExplorerModel for all ordinary files and directories
     *
     * @param directory Starting directory (Use {@code null} for computer root)
     */
    public JHelpFileExplorerModel(final File directory)
    {
        this(directory, new FileFilter(false, false));
    }

    /**
     * Create a new instance of JHelpFileExplorerModel
     *
     * @param directory Starting directory (Use {@code null} for computer root)
     * @param filter    File filter to use (Use {@code null} for all files (includes hidden and virual link))
     */
    public JHelpFileExplorerModel(final File directory, final FileFilter filter)
    {
        if ((directory != null) && (!directory.isDirectory()))
        {
            this.directory = directory.getParentFile();
        }
        else
        {
            this.directory = directory;
        }

        this.fileFilter = filter;
        this.refreshContent();
    }

    /**
     * Create a new instance of JHelpFileExplorerModel starting in root of computer
     *
     * @param filter File filter to use (Use {@code null} for all files (includes hidden and virual link))
     */
    public JHelpFileExplorerModel(final FileFilter filter)
    {
        this(null, filter);
    }

    /**
     * Obtain cell size of an information <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param information Iformation to get its cell size
     * @return Cell size of the information
     * @see JHelpListModel#cellSize(Object)
     */
    @Override
    public Dimension cellSize(final File information)
    {
        return JHelpFileExplorerModel.CELL_SIZE;
    }

    /**
     * Obtain a file element <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param index Element index
     * @return File at given index
     * @see JHelpListModel#element(int)
     */
    @Override
    public File element(final int index)
    {
        return this.content[index % this.content.length];
    }

    /**
     * Number of elements inside the list <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Number of elements inside the list
     * @see JHelpListModel#numberOfElement()
     */
    @Override
    public int numberOfElement()
    {
        return this.content.length;
    }

    /**
     * Compute information image representation <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param information Information to get its representation
     * @return Information's image representation
     * @see JHelpListModel#obtainImageRepresentation(Object)
     */
    @Override
    public JHelpImage obtainImageRepresentation(final File information)
    {
        JHelpImage image = JHelpFileExplorerModel.CREATED_IMAGES.get(information);
        if (image != null)
        {
            return image;
        }

        final JHelpImage icon = JHelpFileExplorerModel.obtainImage(information);
        String           text = information.getName().trim();
        if (text.length() == 0)
        {
            text = information.toString();
        }

        final JHelpMask mask = JHelpFileExplorerModel.FONT.createMask(
                JHelpFileExplorerModel.WIDTH_LIMITER.convert(text));

        final int width  = icon.getWidth() + mask.getWidth();
        final int height = Math.max(icon.getHeight(), mask.getHeight());

        image = new JHelpImage(width, height);
        image.startDrawMode();
        image.drawImage(0, (height - icon.getHeight()) >> 1, icon);
        image.paintMask(icon.getWidth(), (height - mask.getHeight()) >> 1, mask, JHelpFileExplorerModel.FOREGROUND, 0,
                        false);
        image.endDrawMode();

        JHelpFileExplorerModel.CREATED_IMAGES.put(information, image);

        return image;
    }

    /**
     * Information's text representation <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param information Information to get text
     * @return Information's text representation
     * @see JHelpListModel#obtainTextRepresentation(Object)
     */
    @Override
    public String obtainTextRepresentation(final File information)
    {
        String text = information.getName().trim();
        if (text.length() == 0)
        {
            text = information.toString();
        }

        return text;
    }

    /**
     * Tooltip of information.<br>
     * {@code null} if no toooltip <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param information Information to get tooltip
     * @return Information's tooltip or {@code null} if no tooltip
     * @see JHelpListModel#toolTip(Object)
     */
    @Override
    public String toolTip(final File information)
    {
        if (information == JHelpFileExplorerModel.PARENT)
        {
            return "GO PARENT";
        }

        return information.getAbsolutePath();
    }

    /**
     * Indicates if information use image representation <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param information Information
     * @return {@code true}, always use image representation
     * @see JHelpListModel#useImageRepresentation(Object)
     */
    @Override
    public boolean useImageRepresentation(final File information)
    {
        return true;
    }

    /**
     * Current directory
     *
     * @return Current directory
     */
    public File directory()
    {
        return this.directory;
    }

    /**
     * Change current directory
     *
     * @param directory New directory
     */
    public void directory(final File directory)
    {
        if ((directory != null) && (!directory.isDirectory()))
        {
            this.directory = directory.getParentFile();
        }
        else
        {
            this.directory = directory;
        }

        this.refreshContent();
    }

    /**
     * Change file filter
     *
     * @param filter New file filter
     */
    public void filter(final FileFilter filter)
    {
        this.fileFilter = filter;

        this.refreshContent();
    }

    /**
     * Force refresh the file explorer
     */
    public void refreshContent()
    {
        File[] files;
        if (this.directory == null)
        {
            files = File.listRoots();
        }
        else if (this.fileFilter == null)
        {
            files = this.directory.listFiles();
        }
        else
        {
            files = this.directory.listFiles((java.io.FileFilter) this.fileFilter);
        }

        if (files == null)
        {
            files = File.listRoots();
        }

        Arrays.sort(files, ComparatorFile.COMPARATOR_FILE);
        if ((this.directory != null) && ((this.fileFilter == null) || (this.fileFilter.acceptDirectory())))
        {
            final File[] temp = new File[files.length + 1];
            System.arraycopy(files, 0, temp, 1, files.length);
            temp[0] = JHelpFileExplorerModel.PARENT;
            files = temp;
        }

        this.content = files;
        this.fireModelChanged();
    }
}
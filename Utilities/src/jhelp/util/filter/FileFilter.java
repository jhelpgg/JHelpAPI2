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

package jhelp.util.filter;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import jhelp.util.debug.Debug;
import jhelp.util.io.FileImageInformation;

/**
 * File filter on extension.<br>
 * You can add a second filter to filter more<br>
 * <br>
 * Last modification : 23 avr. 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public final class FileFilter
        extends javax.swing.filechooser.FileFilter
        implements FilenameFilter, java.io.FileFilter
{
    /**
     * Create filter initialize to filter images
     *
     * @return Created filter
     */
    public static @NotNull FileFilter createFilterForImage()
    {
        return FileFilter.createFilterForImage(false, false);
    }

    /**
     * Create a filter for image
     *
     * @param acceptHidden      Indicates if hidden files are accepted
     * @param acceptVirtualLink Indicates if virtual links are accepted
     * @return Created filter
     */
    public static @NotNull FileFilter createFilterForImage(final boolean acceptHidden, final boolean acceptVirtualLink)
    {
        final FileFilter fileFilter = new FileFilter(acceptHidden, acceptVirtualLink);

        fileFilter.addExtension("png");
        fileFilter.addExtension("gif");
        fileFilter.addExtension("jpg");
        fileFilter.addExtension("bmp");
        fileFilter.addExtension("pcx");

        fileFilter.information("Images");

        return fileFilter;
    }

    /**
     * Create a file filter for image based on image information not on file extension.<br>
     * Same as {@link #createFilterForImageByFileImageInformation(boolean, boolean)
     * createFilterForImageByFileImageInformation(false, false)}
     *
     * @return Created file filter
     */
    public static @NotNull FileFilter createFilterForImageByFileImageInformation()
    {
        return FileFilter.createFilterForImageByFileImageInformation(false, false);
    }

    /**
     * Create a file filter for image based on image information not on file extension.
     *
     * @param acceptHidden      Indicates if hidden files are accepted
     * @param acceptVirtualLink Indicates if virtual link are accepted
     * @return Created file filter
     */
    public static @NotNull FileFilter createFilterForImageByFileImageInformation(
            final boolean acceptHidden,
            final boolean acceptVirtualLink)
    {
        final FileFilter fileFilter = new FileFilter(acceptHidden, acceptVirtualLink);

        fileFilter.secondFileFilter(FileImageInformation.FILTER_BY_FILE_INFORMATION);
        fileFilter.information("Images");

        return fileFilter;
    }

    /**
     * Create a file filter for sounds (Supported by JHelpSounds) that didn't accepts hidden files neither virtual links
     *
     * @return Created filter
     */
    public static @NotNull FileFilter createFilterForSound()
    {
        return FileFilter.createFilterForSound(false, false);
    }

    /**
     * Create a file filter for sounds (Supported by JHelpSounds)
     *
     * @param acceptHidden      Indicates if hidden files or directory are allowed
     * @param acceptVirtualLink Indicated if virtual links are allowed
     * @return Created filter
     */
    public static @NotNull FileFilter createFilterForSound(final boolean acceptHidden, final boolean acceptVirtualLink)
    {
        final FileFilter fileFilter = new FileFilter(acceptHidden, acceptVirtualLink);

        fileFilter.addExtension("mp3");
        fileFilter.addExtension("au");
        fileFilter.addExtension("wav");
        fileFilter.addExtension("mid");

        fileFilter.information("Sounds");

        return fileFilter;
    }

    /**
     * Create filter initialize to filter videos (Supported by JHelpMultimedia)
     *
     * @return Created filter
     */
    public static @NotNull FileFilter createFilterForVideos()
    {
        return FileFilter.createFilterForVideos(false, false);
    }

    /**
     * Create a filter for videos (Supported by JHelpMultimedia)
     *
     * @param acceptHidden      Indicates if hidden files are accepted
     * @param acceptVirtualLink Indicates if virtual links are accepted
     * @return Created filter
     */
    public static @NotNull FileFilter createFilterForVideos(final boolean acceptHidden, final boolean acceptVirtualLink)
    {
        final FileFilter fileFilter = new FileFilter(acceptHidden, acceptVirtualLink);

        fileFilter.addExtension("avi");
        fileFilter.addExtension("mkv");
        fileFilter.addExtension("mov");
        fileFilter.addExtension("mp4");
        fileFilter.addExtension("mpg");
        fileFilter.addExtension("flv");

        fileFilter.information("Videos");

        return fileFilter;
    }

    /**
     * Indicates if directories are accepted
     */
    private       boolean            acceptDirectory;
    /**
     * Indicates if hidden files are accepted
     */
    private final boolean            acceptHidden;
    /**
     * Indicates if virtual links are accepted
     */
    private final boolean            acceptVirtualLink;
    /**
     * Filtered extensions
     */
    private final ArrayList<String>  extensions;
    /**
     * Filter information
     */
    private       String             information;
    /**
     * Second filter
     */
    private       java.io.FileFilter secondFileFilter;

    /**
     * Constructs FileFilter
     */
    public FileFilter()
    {
        this(false, false);
    }

    /**
     * Create a new instance of FileFilter
     *
     * @param acceptHidden      Indicates if hidden files are accepted
     * @param acceptVirtualLink Indicates if virtual links files are accepted
     */
    public FileFilter(final boolean acceptHidden, final boolean acceptVirtualLink)
    {
        this.extensions = new ArrayList<>();
        this.information = "All";

        this.acceptHidden = acceptHidden;
        this.acceptVirtualLink = acceptVirtualLink;
        this.acceptDirectory = true;
    }

    /**
     * Indicates if a file pass this filter
     *
     * @param directory Directory path
     * @param name      File name
     * @return {@code true} if the file pass this filter
     * @see FilenameFilter#accept(File, String)
     */
    @Override
    public boolean accept(final @NotNull File directory, final @NotNull String name)
    {
        return this.accept(new File(directory, name));
    }

    /**
     * Indicates if a file pass this filter
     *
     * @param file File test
     * @return {@code true} if the file pass
     * @see javax.swing.filechooser.FileFilter#accept(File)
     * @see java.io.FileFilter#accept(File)
     */
    @Override
    public boolean accept(final @Nullable File file)
    {
        try
        {
            if ((file == null) //
                || ((!this.acceptHidden) && (file.isHidden()))//
                || ((!this.acceptVirtualLink) && (!file.getCanonicalPath()
                                                       .equals(file.getAbsolutePath())))//
                || (!file.exists())//
                || (!file.canRead()))
            {
                return false;
            }
        }
        catch (final Exception exception)
        {
            Debug.exception(exception, "Issue while filter : ", file.getAbsolutePath());

            return false;
        }

        if ((this.secondFileFilter != null) && (!this.secondFileFilter.accept(file)))
        {
            return false;
        }

        if (file.isDirectory())
        {
            return this.acceptDirectory;
        }

        return this.filtered(file.getName());
    }

    /**
     * Filter description
     *
     * @return Filter description
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    @Override
    public @NotNull String getDescription()
    {
        final StringBuilder stringBuffer = new StringBuilder();
        if (this.information != null)
        {
            stringBuffer.append(this.information);
            stringBuffer.append(" [");
        }

        stringBuffer.append(this.filter());

        if (this.information != null)
        {
            stringBuffer.append("]");
        }

        return stringBuffer.toString();
    }

    /**
     * Indicates if directories are accepted
     *
     * @return {@code true} if directories are accepted
     */
    public boolean acceptDirectory()
    {
        return this.acceptDirectory;
    }

    /**
     * Change the accept directories value
     *
     * @param acceptDirectory Accept or not directories
     */
    public void acceptDirectory(final boolean acceptDirectory)
    {
        this.acceptDirectory = acceptDirectory;
    }

    /**
     * Add an extension in the filter
     *
     * @param extension Extension added
     */
    public void addExtension(@NotNull String extension)
    {
        if (extension == null)
        {
            throw new NullPointerException("extension MUST NOT be null");
        }

        extension = extension.trim()
                             .toLowerCase();

        if (extension.length() == 0)
        {
            throw new IllegalArgumentException("extension MUST NOT be empty");
        }

        this.extensions.add(extension);
    }

    /**
     * Get an extension in the filter
     *
     * @param index Extension index
     * @return Extension
     */
    public @NotNull String extension(final int index)
    {
        return this.extensions.get(index);
    }

    /**
     * Indicates if an extension is filter
     *
     * @param extension Extension test
     * @return {@code true} if the extension is filter
     */
    public boolean extensionFiltered(@NotNull String extension)
    {
        if (extension == null)
        {
            throw new NullPointerException("extension MUST NOT be null");
        }

        extension = extension.trim()
                             .toLowerCase();

        if (extension.length() == 0)
        {
            throw new NullPointerException("extension MUST NOT be empty");
        }

        return this.extensions.contains(extension);
    }

    /**
     * Filter string
     *
     * @return Filter string
     */
    public @NotNull String filter()
    {
        final int size = this.extensions.size();

        if (size == 0)
        {
            return "*";
        }

        final StringBuilder stringBuffer = new StringBuilder();

        stringBuffer.append("*.");
        stringBuffer.append(this.extensions.get(0));

        for (int i = 1; i < size; i++)
        {
            stringBuffer.append(";*.");
            stringBuffer.append(this.extensions.get(i));
        }

        return stringBuffer.toString();
    }

    /**
     * Indicates if a file is filter
     *
     * @param fileName File name
     * @return {@code true} if the file is filter
     */
    public boolean filtered(@NotNull String fileName)
    {
        if (this.extensions.isEmpty())
        {
            return true;
        }

        final int index = fileName.lastIndexOf('.');
        if (index < 0)
        {
            return false;
        }

        fileName = fileName.substring(index + 1)
                           .toLowerCase();

        for (final String extention : this.extensions)
        {
            if (extention.equals(fileName))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Return information
     *
     * @return information
     */
    public @Nullable String information()
    {
        return this.information;
    }

    /**
     * Modify information
     *
     * @param information New information value
     */
    public void information(final @Nullable String information)
    {
        this.information = information;
    }

    /**
     * Number of extensions
     *
     * @return Number of extensions
     */
    public int numberOfExtensions()
    {
        return this.extensions.size();
    }

    /**
     * Remove an extension
     *
     * @param extension Extension to remove
     */
    public void removeExtension(@NotNull String extension)
    {
        if (extension == null)
        {
            throw new NullPointerException("extension MUST NOT be null");
        }

        extension = extension.trim()
                             .toLowerCase();

        if (extension.length() == 0)
        {
            throw new IllegalArgumentException("extension MUST NOT be empty");
        }

        this.extensions.remove(extension);
    }

    /**
     * Return secondFileFilter
     *
     * @return secondFileFilter
     */
    public @Nullable java.io.FileFilter secondFileFilter()
    {
        return this.secondFileFilter;
    }

    /**
     * Modify secondFileFilter
     *
     * @param secondFileFilter New secondFileFilter value
     */
    public void secondFileFilter(final @Nullable java.io.FileFilter secondFileFilter)
    {
        this.secondFileFilter = secondFileFilter;
    }
}
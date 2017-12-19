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
package jhelp.gui;

import java.awt.FileDialog;
import java.io.File;
import javax.swing.JFrame;
import jhelp.util.filter.FileFilter;
import jhelp.util.io.UtilIO;

/**
 * File chooser base on native file chooser : {@link FileDialog}
 */
public class FileChooser
{
    /**
     * File image chooser
     */
    private static FileChooser imageChooser;

    /**
     * Open dialog to open/save an image
     *
     * @param title     Dialog title
     * @param load      Indicates if it is open ({@code true}) or save ( {@code false}) mode
     * @param startFile Start file/directory
     * @return file image or {@code null} if no selection
     */
    public static File chooseAnImage(final String title, final boolean load, final File startFile)
    {
        if (FileChooser.imageChooser == null)
        {
            FileChooser.imageChooser = new FileChooser();

            final FileFilter fileFilter = FileChooser.imageChooser.fileFilter();

            fileFilter.addExtension("gif");
            fileFilter.addExtension("jpg");
            fileFilter.addExtension("png");
            fileFilter.addExtension("bmp");
            fileFilter.addExtension("pcx");

            fileFilter.information("Image");

            FileChooser.imageChooser.fileFilter(fileFilter);
        }

        if (title == null)
        {
            FileChooser.imageChooser.title("Choose an image");
        }
        else
        {
            FileChooser.imageChooser.title(title);
        }

        if (startFile == null)
        {
            if (load)
            {
                return FileChooser.imageChooser.showOpenFile();
            }

            return FileChooser.imageChooser.showSaveFile();
        }

        if (load)
        {
            return FileChooser.imageChooser.showOpenFile(startFile);
        }

        return FileChooser.imageChooser.showSaveFile(startFile);
    }

    /**
     * Open dialog to choose an image
     *
     * @return Chosen file image or {@code null} if no selection
     */
    public static File loadAnImage()
    {
        return FileChooser.chooseAnImage(null, true, null);
    }

    /**
     * Open dialog to choose an image
     *
     * @param startFile Start file/directory
     * @return Chosen file image or {@code null} if no selection
     */
    public static File loadAnImage(final File startFile)
    {
        return FileChooser.chooseAnImage(null, true, startFile);
    }

    /**
     * Open dialog to choose an image
     *
     * @param title Dialog title
     * @return Chosen file image or {@code null} if no selection
     */
    public static File loadAnImage(final String title)
    {
        return FileChooser.chooseAnImage(title, true, null);
    }

    /**
     * Open dialog to choose an image
     *
     * @param title     Dialog title
     * @param startFile Start file/directory
     * @return Chosen file image or {@code null} if no selection
     */
    public static File loadAnImage(final String title, final File startFile)
    {
        return FileChooser.chooseAnImage(title, true, startFile);
    }

    /**
     * Open dialog to save an image
     *
     * @return Chosen file image or {@code null} if no selection
     */
    public static File saveAnImage()
    {
        return FileChooser.chooseAnImage(null, false, null);
    }

    /**
     * Open dialog to save an image
     *
     * @param startFile startFile Start file/directory
     * @return Chosen file image or {@code null} if no selection
     */
    public static File saveAnImage(final File startFile)
    {
        return FileChooser.chooseAnImage(null, false, startFile);
    }

    /**
     * Open dialog to save an image
     *
     * @param title Dialog title
     * @return Chosen file image or {@code null} if no selection
     */
    public static File saveAnImage(final String title)
    {
        return FileChooser.chooseAnImage(title, false, null);
    }

    /**
     * Open dialog to save an image
     *
     * @param title     Dialog title
     * @param startFile Start file/directory
     * @return Chosen file image or {@code null} if no selection
     */
    public static File saveAnImage(final String title, final File startFile)
    {
        return FileChooser.chooseAnImage(title, false, startFile);
    }

    /**
     * Linked dialog
     */
    private final FileDialog fileDialog;
    /**
     * Actual filter
     */
    private       FileFilter fileFilter;

    /**
     * Constructs FileChooser
     */
    public FileChooser()
    {
        this.fileDialog = new FileDialog(new JFrame());
        this.fileFilter(new FileFilter());
    }

    /**
     * Constructs FileChooser
     *
     * @param parent Frame parent
     */
    public FileChooser(final JFrame parent)
    {
        this.fileDialog = new FileDialog(parent);
        this.fileFilter(new FileFilter());
    }

    /**
     * Actual filter
     *
     * @return Actual filter
     */
    public FileFilter fileFilter()
    {
        return this.fileFilter;
    }

    /**
     * Change filter
     *
     * @param fileFilter New filter
     */
    public void fileFilter(final FileFilter fileFilter)
    {
        if (fileFilter == null)
        {
            throw new NullPointerException("fileFilter mustn't be null");
        }

        this.fileFilter = fileFilter;
        this.fileDialog.setFilenameFilter(this.fileFilter);
        this.fileDialog.setFile(this.fileFilter.filter());
    }

    /**
     * Open the dialog in open mode
     *
     * @return Selected file or {@code null} if no selection
     */
    public File showOpenFile()
    {
        this.fileDialog.setMode(FileDialog.LOAD);

        this.fileDialog.setVisible(true);

        final String fileName      = this.fileDialog.getFile();
        final String directoryName = this.fileDialog.getDirectory();

        if ((fileName == null) || (directoryName == null) || (!this.fileFilter.filtered(fileName)))
        {
            return null;
        }

        final File file = new File(directoryName, fileName);
        if (!file.exists())
        {
            return null;
        }

        return file;
    }

    /**
     * Open the dialog in open mode
     *
     * @param startFile Start file/directory
     * @return Selected file or {@code null} if no selection
     */
    public File showOpenFile(final File startFile)
    {
        this.startDirectory(startFile);

        return this.showOpenFile();
    }

    /**
     * Open the dialog in save mode
     *
     * @return Selected file or {@code null} if no selection
     */
    public File showSaveFile()
    {
        this.fileDialog.setMode(FileDialog.SAVE);

        this.fileDialog.setVisible(true);

        final String fileName      = this.fileDialog.getFile();
        final String directoryName = this.fileDialog.getDirectory();

        if ((fileName == null) || (directoryName == null) || (!this.fileFilter.filtered(fileName)))
        {
            return null;
        }

        return new File(directoryName, fileName);
    }

    /**
     * Open the dialog in save mode
     *
     * @param startFile Start file/directory
     * @return Selected file or {@code null} if no selection
     */
    public File showSaveFile(final File startFile)
    {
        this.startDirectory(startFile);

        return this.showSaveFile();
    }

    /**
     * Change the start directory
     *
     * @param file Start directory
     */
    public void startDirectory(File file)
    {
        if (file == null)
        {
            file = UtilIO.obtainOutsideDirectory();
        }

        if (!file.exists())
        {
            throw new IllegalArgumentException("file must exists : " + file.getAbsolutePath());
        }

        if (file.isFile())
        {
            file = file.getParentFile();
        }

        this.fileDialog.setDirectory(file.getAbsolutePath());
    }

    /**
     * Dialog title
     *
     * @return Dialog title
     */
    public String title()
    {
        return this.fileDialog.getTitle();
    }

    /**
     * Change dialog title
     *
     * @param title New title
     */
    public void title(final String title)
    {
        this.fileDialog.setTitle(title);
    }
}
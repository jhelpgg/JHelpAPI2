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

import java.awt.Component;
import java.io.File;
import javax.swing.JFileChooser;

/**
 * Chooser of directory
 */
public class DirectoryChooser
{
    /**
     * Chooser of directory singleton
     */
    public static final DirectoryChooser DIRECTORY_CHOOSER = new DirectoryChooser();
    /**
     * Linked file chooser
     */
    private final JFileChooser fileChooser;

    /**
     * Constructs DirectoryChooser
     */
    private DirectoryChooser()
    {
        this.fileChooser = new JFileChooser("Choose directory");
        this.fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    /**
     * Change the ok button text
     *
     * @param text New OK button text
     */
    public void approveButtonText(final String text)
    {
        if (text == null)
        {
            throw new NullPointerException("text mustn't be null");
        }

        this.fileChooser.setApproveButtonText(text);
    }

    /**
     * Change the ok button
     *
     * @param text    OK button text
     * @param tooltip OK button tooltip
     */
    public void approveButtonText(final String text, final String tooltip)
    {
        if (text == null)
        {
            throw new NullPointerException("text mustn't be null");
        }

        if (tooltip == null)
        {
            throw new NullPointerException("tooltip mustn't be null");
        }

        this.fileChooser.setApproveButtonText(text);
        this.fileChooser.setApproveButtonToolTipText(tooltip);
    }

    /**
     * Open the dialog for choose a directory
     *
     * @param parent Component parent
     * @return Chosen directory or {@code null}
     */
    public File chooseOneDirectory(final Component parent)
    {
        this.fileChooser.setMultiSelectionEnabled(false);

        if (this.fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
        {
            return this.fileChooser.getSelectedFile();
        }

        return null;
    }

    /**
     * Choose one directory
     *
     * @param parent         Component parent
     * @param startDirectory Start directory
     * @return Choosen directory or {@code null} if user cancel the operation
     */
    public File chooseOneDirectory(final Component parent, final File startDirectory)
    {
        this.fileChooser.setMultiSelectionEnabled(false);
        this.fileChooser.setCurrentDirectory(startDirectory);

        if (this.fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
        {
            return this.fileChooser.getSelectedFile();
        }

        return null;
    }

    /**
     * Open dialog for choose several directories
     *
     * @param parent Component parent
     * @return Chosen directories
     */
    public File[] chooseSeveralDirectories(final Component parent)
    {
        this.fileChooser.setMultiSelectionEnabled(true);

        if (this.fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
        {
            return this.fileChooser.getSelectedFiles();
        }

        return null;
    }

    /**
     * Choose a list of directories
     *
     * @param parent         Component parent
     * @param startDirectory Starting directory
     * @return Selected file list or {@code null} if user cancel operation
     */
    public File[] chooseSeveralDirectories(final Component parent, final File startDirectory)
    {
        this.fileChooser.setMultiSelectionEnabled(true);
        this.fileChooser.setCurrentDirectory(startDirectory);

        if (this.fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
        {
            return this.fileChooser.getSelectedFiles();
        }

        return null;
    }

    /**
     * Change the dialog title
     *
     * @param title New title
     */
    public void title(final String title)
    {
        if (title == null)
        {
            throw new NullPointerException("title mustn't be null");
        }

        this.fileChooser.setDialogTitle(title);
    }
}
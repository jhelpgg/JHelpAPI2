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

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import javax.swing.ImageIcon;
import jhelp.util.debug.Debug;
import jhelp.util.gui.JHelpRichText;
import jhelp.util.resources.ResourceText;
import jhelp.util.resources.Resources;

/**
 * Default/Common GUI resources
 *
 * @author JHelp
 */
public class ResourcesGUI
{
    static
    {
        RESOURCES = new Resources(ResourcesGUI.class);

        final Dimension dimension = Toolkit.getDefaultToolkit().getBestCursorSize(32, 32);
        IS_CURSOR_SIZE_32x32 = ((dimension.width == 32) && (dimension.height == 32));

        if (ResourcesGUI.IS_CURSOR_SIZE_32x32)
        {
            TRANSLATE_CURSOR = -16;
        }
        else
        {
            TRANSLATE_CURSOR = -8;
        }

        CAN_DROP_CURSOR = ResourcesGUI.createCursor(ResourcesGUI.BASE_NAME_CAN_DROP);
        CANT_DROP_CURSOR = ResourcesGUI.createCursor(ResourcesGUI.BASE_NAME_CANT_DROP);
        ICON_ATTACH_WINDOW = ResourcesGUI.RESOURCES.obtainImageIcon("AttachWindow.png");
        ICON_CLOSE_OVER = ResourcesGUI.RESOURCES.obtainImageIcon("CloseOver.png");
        ICON_COSE_NORMAL = ResourcesGUI.RESOURCES.obtainImageIcon("CloseNormal.png");
        ICON_DETACH_WINDOW = ResourcesGUI.RESOURCES.obtainImageIcon("DetachWindow.png");
        ICON_HIDE_NORMAL = ResourcesGUI.RESOURCES.obtainImageIcon("HideNormal.png");
        ICON_HIDE_OVER = ResourcesGUI.RESOURCES.obtainImageIcon("HideOver.png");
        ICON_SHOW_NORMAL = ResourcesGUI.RESOURCES.obtainImageIcon("ShowNormal.png");
        ICON_SHOW_OVER = ResourcesGUI.RESOURCES.obtainImageIcon("ShowOver.png");
        ICON_PLUS = ResourcesGUI.RESOURCES.obtainImageIcon("plus.gif");

        STANDARD_SMILEYS = new JHelpRichText(ResourcesGUI.RESOURCES);

        ResourcesGUI.STANDARD_SMILEYS.associate(";)", "clien_d_oeil.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(";-)", "clien_d_oeil.png");
        ResourcesGUI.STANDARD_SMILEYS.associate("8)", "cool.png");
        ResourcesGUI.STANDARD_SMILEYS.associate("8-)", "cool.png");
        ResourcesGUI.STANDARD_SMILEYS.associate("B)", "cool.png");
        ResourcesGUI.STANDARD_SMILEYS.associate("B-)", "cool.png");
        ResourcesGUI.STANDARD_SMILEYS.associate("8)", "cool.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":S", "depite.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":-S", "depite.png");
        ResourcesGUI.STANDARD_SMILEYS.associate("<:-)", "fou.png");
        ResourcesGUI.STANDARD_SMILEYS.associate("<:)", "fou.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":-E", "frappe.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":E", "frappe.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":-K", "malade.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":K", "malade.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":-k", "malade.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":k", "malade.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":-|", "neutre.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":|", "neutre.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":,-(", "pleure.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":,(", "pleure.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":'-(", "pleure.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":'(", "pleure.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":-X", "silence.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":-x", "silence.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":X", "silence.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":x", "silence.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":-)", "sourire.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":)", "sourire.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":-O", "surpris.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":-o", "surpris.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":O", "surpris.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":o", "surpris.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":-(", "triste.png");
        ResourcesGUI.STANDARD_SMILEYS.associate(":(", "triste.png");

        RESOURCE_TEXT = ResourcesGUI.RESOURCES.obtainResourceText("TextsGUI");
    }

    /**
     * Base name for "Can't drop" cursor
     */
    private static final String BASE_NAME_CANT_DROP = "cantDrop";
    /**
     * Base name for "Can drop" cursor
     */
    private static final String BASE_NAME_CAN_DROP  = "canDrop";
    /**
     * End file name for 16x16 cursors
     */
    private static final String SIZE_16x16          = "16x16.png";
    /**
     * End file name for 32x32 cursors
     */
    private static final String SIZE_32x32          = "32x32.png";
    /**
     * Can't drop cursor
     */
    public static final Cursor CANT_DROP_CURSOR;
    /**
     * Can drop cursor
     */
    public static final Cursor CAN_DROP_CURSOR;
    public static final String COLOR_CHOOSER_BLUE                     = "colorChooser.blue";
    public static final String Color_CHOOSER_ALPHA                    = "colorChooser.alpha";
    public static final String Color_CHOOSER_GREEN                    = "colorChooser.green";
    public static final String Color_CHOOSER_RED                      = "colorChooser.red";
    /**
     * File chooser title header
     */
    public static final String FILE_CHOOSER_TITLE_HEADER              = "FileChooserTitleHeader";
    /**
     * Delete file/directory message <br>
     * {0} : File to delete path
     */
    public static final String FILE_EXPLORER_DELETE_FILE_MESSAGE      = "FileExplorerDeleteFileMessage";
    /**
     * Delete file/directory title
     */
    public static final String FILE_EXPLORER_DELETE_FILE_TITLE        = "FileExplorerDeleteFileTitle";
    /**
     * Error text for indicates that a rename file failed. <br>
     * {0} : source fila path <br>
     * {1} : destination file path
     */
    public static final String FILE_EXPLORER_ERROR_FAILED_TO_RENAME   = "FileExplorerErrorFailedToRename";
    /**
     * Ask to choose a new directory name on creation of a new directory
     */
    public static final String FILE_EXPLORER_NEW_DIRECTORY_NAME       = "FileExplorerNewDirectoryName";
    /**
     * Ask to choose a new name on file renaming
     */
    public static final String FILE_EXPLORER_OTHER_NAME               = "FileExplorerOtherName";
    /**
     * Alert user that the renaming will overwrite an already existing file/directory message <br>
     * {0} : File/directory will be overwrite path
     */
    public static final String FILE_EXPLORER_OVERWRITE_FILE_MESSAGE   = "FileExplorerOverwriteFileMessage";
    /**
     * Alert user that the renaming will overwrite an already existing file/directory title
     */
    public static final String FILE_EXPLORER_OVERWRITE_FILE_TITLE     = "FileExplorerOverwriteFileTitle";
    /**
     * Alert user that the renaming can't append, the overwriting is not possible
     */
    public static final String FILE_EXPLORER_OVERWRITE_IMPOSSIBLE     = "FileExplorerOverwriteImpossible";
    /**
     * Alert user that the directory already exists, so the creation have failed <br>
     * {0} : Already existing directory path
     */
    public static final String FILE_EXPLORER_WARNING_DIRECTORY_EXISTS = "FileExplorerWarningDirectoryExists";
    /**
     * Attach window icon
     */
    public static final ImageIcon ICON_ATTACH_WINDOW;
    /**
     * Over close icon
     */
    public static final ImageIcon ICON_CLOSE_OVER;
    /**
     * Close icon
     */
    public static final ImageIcon ICON_COSE_NORMAL;
    /**
     * Detach window icon
     */
    public static final ImageIcon ICON_DETACH_WINDOW;
    /**
     * Hide icon
     */
    public static final ImageIcon ICON_HIDE_NORMAL;
    /**
     * Hide over icon
     */
    public static final ImageIcon ICON_HIDE_OVER;
    /**
     * Plus + icon
     */
    public static final ImageIcon ICON_PLUS;
    /**
     * Show icon
     */
    public static final ImageIcon ICON_SHOW_NORMAL;
    /**
     * Show over icon
     */
    public static final ImageIcon ICON_SHOW_OVER;
    /**
     * Indicates if we use 32x32 cursors
     */
    public static final boolean   IS_CURSOR_SIZE_32x32;
    /**
     * Cancel button text on option pane
     */
    public static final String OPTION_PANE_CANCEL = "OptionPaneCancel";
    /**
     * No button text on option pane
     */
    public static final String OPTION_PANE_NO     = "OptionPaneNo";
    /**
     * Ok button text on option pane
     */
    public static final String OPTION_PANE_OK     = "OptionPaneOk";
    /**
     * Yes button text on option pane
     */
    public static final String OPTION_PANE_YES    = "OptionPaneYes";
    /**
     * "Free" access to GUI resources
     */
    public static final Resources     RESOURCES;
    /**
     * GUI texts resources
     */
    public static final ResourceText  RESOURCE_TEXT;
    /**
     * Rich text with standard smiley replacement
     */
    public static final JHelpRichText STANDARD_SMILEYS;
    /**
     * Translate for cursor
     */
    public static final int           TRANSLATE_CURSOR;

    /**
     * Create a cursor
     *
     * @param baseNameCursor Base name cursor
     * @return Created cursor
     */
    private static Cursor createCursor(final String baseNameCursor)
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(baseNameCursor);
        int x = 0;
        int y = 0;

        if (ResourcesGUI.IS_CURSOR_SIZE_32x32)
        {
            stringBuffer.append(ResourcesGUI.SIZE_32x32);
            x = 16;
            y = 16;
        }
        else
        {
            stringBuffer.append(ResourcesGUI.SIZE_16x16);
            x = 8;
            y = 8;
        }

        try
        {
            return Toolkit.getDefaultToolkit()
                          .createCustomCursor(ResourcesGUI.RESOURCES.obtainBufferedImage(stringBuffer.toString()),
                                              new Point(x, y),
                                              baseNameCursor);
        }
        catch (final IOException e)
        {
            stringBuffer = null;
            Debug.exception(e);
            return null;
        }
    }
}
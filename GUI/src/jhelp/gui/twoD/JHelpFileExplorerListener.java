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

import java.io.File;

/**
 * Listener of file explorer
 *
 * @author JHelp
 */
public interface JHelpFileExplorerListener
{
    /**
     * Called when file is choosen
     *
     * @param fileExplorer2D File explorer source
     * @param selectedFile   Choosen file
     * @param selectedIndex  Choosen index
     */
    void fileExplorerFileChoosen(JHelpFileExplorer2D fileExplorer2D, File selectedFile, int selectedIndex);

    /**
     * Called when file selection changed
     *
     * @param fileExplorer2D File explorer source
     * @param selectedFile   Selected file or {@code null} if selection is removed
     * @param selectedIndex  Selected index or -1 if selection is removed
     */
    void fileExplorerSelectionChange(JHelpFileExplorer2D fileExplorer2D, File selectedFile, int selectedIndex);
}
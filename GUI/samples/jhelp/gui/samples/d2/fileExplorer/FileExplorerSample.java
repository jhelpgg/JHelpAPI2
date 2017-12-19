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

/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.gui.samples.d2.fileExplorer;

public class FileExplorerSample
{

    /**
     * TODO Explains what does the method main in jhelp.gui.samples.d2.fileExplorer [JHelpGUI]
     *
     * @param args
     */
    public static void main(final String[] args)
    {
        final FrameFileExplorer frameFileExplorer = new FrameFileExplorer();
        frameFileExplorer.setVisible(true);
        frameFileExplorer.automaticRefresh(true);
    }
}
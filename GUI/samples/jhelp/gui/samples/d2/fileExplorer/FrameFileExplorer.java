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

import jhelp.gui.twoD.JHelpBorderLayout;
import jhelp.gui.twoD.JHelpBorderLayout.JHelpBorderLayoutConstraints;
import jhelp.gui.twoD.JHelpFileExplorer2D;
import jhelp.gui.twoD.JHelpFrame2D;

public class FrameFileExplorer
        extends JHelpFrame2D
{
    public FrameFileExplorer()
    {
        super(new JHelpBorderLayout());

        final JHelpFileExplorer2D fileExplorer2D = new JHelpFileExplorer2D();
        this.addComponent2D(fileExplorer2D, JHelpBorderLayoutConstraints.LEFT);
    }
}
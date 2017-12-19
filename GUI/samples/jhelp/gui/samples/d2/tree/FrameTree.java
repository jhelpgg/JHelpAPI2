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
package jhelp.gui.samples.d2.tree;

import jhelp.gui.twoD.JHelpBorderLayout;
import jhelp.gui.twoD.JHelpBorderLayout.JHelpBorderLayoutConstraints;
import jhelp.gui.twoD.JHelpFrame2D;
import jhelp.gui.twoD.JHelpScrollPane2D;
import jhelp.gui.twoD.JHelpTree2D;

/**
 * Frame with a tree
 *
 * @author JHelp
 */
public class FrameTree
        extends JHelpFrame2D
{
    /**
     * Create a new instance of FrameTree
     */
    public FrameTree()
    {
        super(new JHelpBorderLayout());

        final JHelpTree2D<TreeNodeSample> tree = new JHelpTree2D<TreeNodeSample>(new TreeModelSample());
        this.addComponent2D(new JHelpScrollPane2D(tree), JHelpBorderLayoutConstraints.CENTER);
    }
}
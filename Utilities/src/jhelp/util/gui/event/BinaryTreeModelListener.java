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

package jhelp.util.gui.event;

import jhelp.util.gui.model.BinaryTreeModel;

/**
 * Listener of binary solver model changes
 *
 * @param <ELEMENT> Element type
 * @author JHelp
 */
public interface BinaryTreeModelListener<ELEMENT>
{
    /**
     * Called when binary solver model changed
     *
     * @param binaryTreeModel Binary solver model changed
     */
    void binaryTreeModelChanged(BinaryTreeModel<ELEMENT> binaryTreeModel);
}
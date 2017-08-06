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

package jhelp.util.gui.renderer;

import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.model.BinaryTreeModel;

/**
 * Renderer of elements in binary solver model.<br>
 * It describes how draw a binary solver element
 *
 * @author JHelp
 * @param <ELEMENT>
 *           Elements in model type
 */
public interface BinaryTreeModelElementRenderer<ELEMENT>
{
    /**
     * Create image for a specific solver element
     *
     * @param binaryTreeModel Model where element from
     * @param element         Element to draw
     * @return Image representation
     */
    JHelpImage obtainBinaryTreeModelElementImage(BinaryTreeModel<ELEMENT> binaryTreeModel, ELEMENT element);
}
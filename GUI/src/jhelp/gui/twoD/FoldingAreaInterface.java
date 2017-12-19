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

import java.awt.Dimension;
import jhelp.gui.twoD.JHelpFoldable2D.FoldingAreaPosition;
import jhelp.util.gui.JHelpImage;

/**
 * Describe how draw folding panel {@link JHelpFoldable2D} header
 *
 * @author JHelp
 */
public interface FoldingAreaInterface
{
    /**
     * Header minimum size
     *
     * @return Header minimum size
     */
    Dimension minimumSize();

    /**
     * Draw the header
     *
     * @param x                   X location
     * @param y                   Y location
     * @param width               Width
     * @param height              Height
     * @param parent              Image where draw
     * @param fold                Indicates if the panel is fold or not
     * @param foldingAreaPosition Header position relative to content component
     */
    void paintArea(
            final int x, final int y, final int width, final int height, final JHelpImage parent, final boolean fold,
            final FoldingAreaPosition foldingAreaPosition);
}
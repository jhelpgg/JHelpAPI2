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

/**
 * Listener of folding change in {@link JHelpFoldable2D} component
 *
 * @author JHelp
 */
public interface FoldListener
{
    /**
     * Called when the fold state of a {@link JHelpFoldable2D} just changed
     *
     * @param foldable2d {@link JHelpFoldable2D} source
     * @param isFold     The new fold state
     */
    void foldChanged(JHelpFoldable2D foldable2d, boolean isFold);

    /**
     * Called just before the fold state of a {@link JHelpFoldable2D} will change
     *
     * @param foldable2d {@link JHelpFoldable2D} source
     * @param futureFold The future fold state
     */
    void willFoldChanged(JHelpFoldable2D foldable2d, boolean futureFold);
}
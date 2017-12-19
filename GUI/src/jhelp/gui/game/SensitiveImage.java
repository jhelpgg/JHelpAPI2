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
package jhelp.gui.game;

import jhelp.util.gui.JHelpImage;

/**
 * Image that is mouse sensitive
 *
 * @author JHelp
 */
public class SensitiveImage
        extends SensitiveElement
{
    /**
     * Create a new instance of SensitiveImage
     *
     * @param x     X location
     * @param y     Y location
     * @param image Image to draw
     */
    public SensitiveImage(final int x, final int y, final JHelpImage image)
    {
        super(x, y, image);
    }

    /**
     * Called when enable state changed.<br>
     * Does nothing by default, override it to do something <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param enable New enable state
     * @see jhelp.gui.game.SensitiveElement#enableChanged(boolean)
     */
    @Override
    protected void enableChanged(final boolean enable)
    {
    }

    /**
     * Called when mouse click.<br>
     * Does nothing by default, override it to do something <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param parent Animation parent
     * @see jhelp.gui.game.SensitiveElement#mouseClick(jhelp.gui.game.SensitiveAnimation)
     */
    @Override
    protected void mouseClick(final SensitiveAnimation parent)
    {
    }

    /**
     * Called when mouse enter.<br>
     * Does nothing by default, override it to do something <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param parent Animation parent
     * @see jhelp.gui.game.SensitiveElement#mouseEnter(jhelp.gui.game.SensitiveAnimation)
     */
    @Override
    protected void mouseEnter(final SensitiveAnimation parent)
    {
    }

    /**
     * Called when mouse exit.<br>
     * Does nothing by default, override it to do something <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param parent Animation parent
     * @see jhelp.gui.game.SensitiveElement#mouseExit(jhelp.gui.game.SensitiveAnimation)
     */
    @Override
    protected void mouseExit(final SensitiveAnimation parent)
    {
    }
}
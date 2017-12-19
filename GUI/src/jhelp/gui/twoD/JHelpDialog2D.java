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
import jhelp.gui.JHelpMouseListener;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpSprite;
import jhelp.util.list.Pair;

/**
 * Dialog 2D.<br>
 * Dialog are created by {@link JHelpFrame2D#createDialog(JHelpComponent2D)}.<br>
 * By default, click outside the dialog, close it, you can avoid this behavior with {@link #clickOutClose(boolean)}, but in
 * this case don't forget to add an other way to close it
 *
 * @author JHelp
 */
public class JHelpDialog2D
        implements JHelpWindow2D
{
    /**
     * Indicates if clicking outside the dialog, it's close it
     */
    private       boolean          clikOutClose;
    /**
     * Main component of the dialog
     */
    private final JHelpComponent2D component2d;
    /**
     * Frame parent where dialog draw
     */
    private final JHelpFrame2D     parent;
    /**
     * Sprite for draw the dialog
     */
    private       JHelpSprite      sprite;

    /**
     * Create a new instance of JHelpDialog2D
     *
     * @param component2d Main component
     * @param parent      Frame parent
     */
    JHelpDialog2D(final JHelpComponent2D component2d, final JHelpFrame2D parent)
    {
        this.component2d = component2d;
        this.parent = parent;
        this.clikOutClose = true;

        component2d.owner(this);
        final Dimension preferred = component2d.preferredSize(-1, -1);

        final JHelpImage parentImage = parent.getImage();

        this.sprite = parentImage.createSprite((parentImage.getWidth() - preferred.width) >> 1,
                                               (parentImage.getHeight() - preferred.height) >> 1,
                                               preferred.width, preferred.height);
        this.updateImage();
    }

    /**
     * Compute component under the mouse
     *
     * @param x Mouse X
     * @param y Mouse Y
     * @return Component and associate listener
     */
    Pair<JHelpComponent2D, JHelpMouseListener> mouseOver(final int x, final int y)
    {
        return this.component2d.mouseOver(x, y);
    }

    /**
     * Update the dialog image
     */
    void updateImage()
    {
        final boolean visible = this.sprite.isVisible();

        if (visible)
        {
            this.sprite.setVisible(false);
        }

        JHelpImage image = this.sprite.getImage();
        image.startDrawMode();
        image.clear(0);
        image.endDrawMode();

        if (this.component2d.visible())
        {
            final Dimension preferred = this.component2d.preferredSize(-1, -1);
            this.component2d.bounds(0, 0, preferred.width, preferred.height);

            if ((this.sprite.getWidth() != preferred.width) || (this.sprite.getHeight() != preferred.getHeight()))
            {
                final JHelpImage parentImage = this.parent.getImage();
                //noinspection SynchronizationOnLocalVariableOrMethodParameter
                synchronized (parentImage)
                {
                    final boolean drawMode = parentImage.isDrawMode();

                    if (drawMode)
                    {
                        parentImage.endDrawMode();
                    }
                    parentImage.removeSprite(this.sprite);
                    this.sprite = parentImage.createSprite((parentImage.getWidth() - preferred.width) >> 1,
                                                           (parentImage.getHeight() - preferred.height) >> 1,
                                                           preferred.width, preferred.height);
                    if (drawMode)
                    {
                        parentImage.startDrawMode();
                    }

                    image = this.sprite.getImage();
                }
            }

            image.startDrawMode();
            this.component2d.paintInternal(0, 0, image);
            image.endDrawMode();
        }

        if (visible)
        {
            this.sprite.setVisible(true);
        }
    }

    /**
     * Update dialog visibility
     *
     * @param visible New visibility status
     */
    void updateVisible(final boolean visible)
    {
        if (visible)
        {
            final JHelpImage parentImage = this.parent.getImage();
            this.sprite.setPosition((parentImage.getWidth() - this.sprite.getWidth()) >> 1,
                                    (parentImage.getHeight() - this.sprite.getHeight()) >> 1);
        }

        this.sprite.setVisible(visible);
    }

    /**
     * Dialog root component
     *
     * @return Dialog root component
     */
    protected JHelpComponent2D root()
    {
        return this.component2d;
    }

    /**
     * Indicates if dialog will be close if user click outside it
     *
     * @return {@code true} if dialog will be close if user click outside it
     */
    public boolean clickOutClose()
    {
        return this.clikOutClose;
    }

    /**
     * Change the state of click out close
     *
     * @param clikOutClose New click out close status
     */
    public void clickOutClose(final boolean clikOutClose)
    {
        this.clikOutClose = clikOutClose;
    }

    /**
     * Compute dialog main component size
     *
     * @return Dialog main component size
     */
    public Dimension componentInternSize()
    {
        return new Dimension(this.component2d.preferredSize(-1, -1));
    }

    /**
     * Dialog height
     *
     * @return Dialog height
     */
    public int height()
    {
        return this.sprite.getHeight();
    }

    /**
     * Dialog's frame owner
     *
     * @return Dialog's frame owner
     */
    public JHelpFrame2D owner()
    {
        return this.parent;
    }

    /**
     * Indicates if dialog is visible
     *
     * @return {@code true} if dialog is visible
     */
    public boolean visible()
    {
        return this.sprite.isVisible();
    }

    /**
     * Change dialog visibility
     *
     * @param visible New visibility status
     */
    public void visible(final boolean visible)
    {
        this.parent.dialogVisible(this, visible);
    }

    /**
     * Dialog width
     *
     * @return Dialog width
     */
    public int width()
    {
        return this.sprite.getWidth();
    }

    /**
     * Dialog X location
     *
     * @return Dialog x location
     */
    public int x()
    {
        return this.sprite.getX();
    }

    /**
     * Dialog Y location
     *
     * @return Dialog Y location
     */
    public int y()
    {
        return this.sprite.getY();
    }
}
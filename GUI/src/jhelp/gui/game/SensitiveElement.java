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
import jhelp.util.gui.dynamic.ImageTransition;
import jhelp.util.gui.dynamic.Position;
import jhelp.util.gui.dynamic.Positionable;

/**
 * Generic mouse sensitive element
 *
 * @author JHelp
 */
public abstract class SensitiveElement
        implements Positionable
{
    /**
     * Default time (in millisecond) for do transition between enable and disable state
     */
    public static final int DEFAULT_TRANSITION_TIME = 1024;

    /**
     * Indicates if element is enable
     */
    private       boolean         enable;
    /**
     * Image to draw
     */
    private final JHelpImage      image;
    /**
     * Transition between grey and normal
     */
    private       ImageTransition imageTransition;
    /**
     * Indicates if we are in grey side
     */
    private       boolean         isGrey;
    /**
     * Synchronization lock
     */
    private final Object lock = new Object();
    /**
     * Indicates if element is visible
     */
    private boolean visible;
    /**
     * Element X position in parent
     */
    private int     x;
    /**
     * Element Y position in parent
     */
    private int     y;

    /**
     * Create a new instance of SensitiveElement
     *
     * @param x     X position in parent
     * @param y     Y position in parent
     * @param image Image to draw
     */
    public SensitiveElement(final int x, final int y, final JHelpImage image)
    {
        if (image == null)
        {
            throw new NullPointerException("image mustn't be null");
        }

        this.x = x;
        this.y = y;
        this.isGrey = false;
        this.image = image;
        this.visible = true;
        this.enable = true;
    }

    /**
     * Called when enable state changed
     *
     * @param enable New enable state
     */
    protected abstract void enableChanged(boolean enable);

    /**
     * Called when mouse clicked
     *
     * @param parent Animation parent
     */
    protected abstract void mouseClick(SensitiveAnimation parent);

    /**
     * Called when mouse enter
     *
     * @param parent Animation parent
     */
    protected abstract void mouseEnter(SensitiveAnimation parent);

    /**
     * Called when mouse exit
     *
     * @param parent Animation parent
     */
    protected abstract void mouseExit(SensitiveAnimation parent);

    /**
     * Indicates if given position is inside the element
     *
     * @param x X position in parent
     * @param y Y position in parent
     * @return {@code true} if given position is inside the element
     */
    public final boolean containsPosition(final int x, final int y)
    {
        return (x >= this.x) && (y >= this.y) && (x < (this.x + this.image.getWidth())) &&
               (y < (this.y + this.image.getHeight()));
    }

    /**
     * Actual enable value
     *
     * @return Actual enable value
     */
    public final boolean enable()
    {
        return this.enable;
    }

    /**
     * Change immediately the enable state (without animation)
     *
     * @param enable New enable state
     */
    public final void enable(final boolean enable)
    {
        this.enable(null, enable, 1);
    }

    /**
     * Change enable state and playing animation with default time
     *
     * @param gameDynamic Game where animation played. If {@code null}, no animation played
     * @param enable      New enable state
     */
    public final void enable(final JHelpGameDynamic gameDynamic, final boolean enable)
    {
        this.enable(gameDynamic, enable, SensitiveElement.DEFAULT_TRANSITION_TIME);
    }

    /**
     * Change enable state and playing animation with given time
     *
     * @param gameDynamic Game where animation played. If {@code null}, no animation played
     * @param enable      New enable state
     * @param millisecond Time, in millisecond, for do the animation
     */
    public final void enable(final JHelpGameDynamic gameDynamic, final boolean enable, final int millisecond)
    {
        if (this.enable == enable)
        {
            return;
        }

        this.enable = enable;

        if (gameDynamic != null)
        {
            if (this.enable)
            {
                this.toColor(gameDynamic, millisecond);
            }
            else
            {
                this.toGrey(gameDynamic, millisecond);
            }
        }

        this.enableChanged(this.enable);
    }

    /**
     * Element height
     *
     * @return Element height
     */
    public final int height()
    {
        return this.image.getHeight();
    }

    /**
     * Current image to draw
     *
     * @return Current image to draw
     */
    public final JHelpImage image()
    {
        synchronized (this.lock)
        {
            if (this.imageTransition != null)
            {
                if (this.imageTransition.isAnimating())
                {
                    return this.imageTransition.getInterpolated();
                }

                this.imageTransition = null;
            }

            if (this.isGrey)
            {
                final JHelpImage grey = new JHelpImage(this.image.getWidth(), this.image.getHeight());
                grey.startDrawMode();
                grey.copy(this.image);
                grey.gray();
                grey.endDrawMode();
                return grey;
            }

            return this.image;
        }
    }

    /**
     * Position in parent <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Position in parent
     * @see Positionable#position()
     */
    @Override
    public final Position position()
    {
        return new Position(this.x, this.y);
    }

    /**
     * Change element position <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param position New position
     * @see Positionable#position(Position)
     */
    @Override
    public final void position(final Position position)
    {
        this.position(position.getX(), position.getY());
    }

    /**
     * Change element position
     *
     * @param x X
     * @param y Y
     */
    public final void position(final int x, final int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Convert, if need, to color version
     *
     * @param gameDynamic Game where play the animation
     * @param millisecond Time to do the animation in millisecond
     */
    public final void toColor(final JHelpGameDynamic gameDynamic, final int millisecond)
    {
        synchronized (this.lock)
        {
            if (!this.isGrey)
            {
                return;
            }

            if (this.imageTransition != null)
            {
                gameDynamic.stopAnimation(this.imageTransition);
            }

            final JHelpImage grey = new JHelpImage(this.image.getWidth(), this.image.getHeight());
            grey.startDrawMode();
            grey.copy(this.image);
            grey.gray();
            grey.endDrawMode();

            this.imageTransition = new ImageTransition(UtilDynamic.millisecondToNumberOfFrame(millisecond), grey,
                                                       this.image, 1, false);
            this.isGrey = false;
            gameDynamic.playAnimation(this.imageTransition);
        }
    }

    /**
     * Convert, if need, to grey version
     *
     * @param gameDynamic Game where play the animation
     * @param millisecond Time to do the animation in millisecond
     */
    public final void toGrey(final JHelpGameDynamic gameDynamic, final int millisecond)
    {
        synchronized (this.lock)
        {
            if (this.isGrey)
            {
                return;
            }

            if (this.imageTransition != null)
            {
                gameDynamic.stopAnimation(this.imageTransition);
            }

            final JHelpImage grey = new JHelpImage(this.image.getWidth(), this.image.getHeight());
            grey.startDrawMode();
            grey.copy(this.image);
            grey.gray();
            grey.endDrawMode();

            this.imageTransition = new ImageTransition(UtilDynamic.millisecondToNumberOfFrame(millisecond), this.image,
                                                       grey, 1, false);
            this.isGrey = true;
            gameDynamic.playAnimation(this.imageTransition);
        }
    }

    /**
     * Translate the element
     *
     * @param tx Translation X
     * @param ty Translation Y
     */
    public final void translate(final int tx, final int ty)
    {
        this.x += tx;
        this.y += ty;
    }

    /**
     * Change visible
     *
     * @param visible New visible value
     */
    public final void visible(final boolean visible)
    {
        this.visible = visible;
    }

    /**
     * Actual visible value
     *
     * @return Actual visible value
     */
    public final boolean visible()
    {
        return this.visible;
    }

    /**
     * Element width
     *
     * @return Element width
     */
    public final int width()
    {
        return this.image.getWidth();
    }

    /**
     * Element X position
     *
     * @return Element X position
     */
    public final int x()
    {
        return this.x;
    }

    /**
     * Element Y position
     *
     * @return Element Y position
     */
    public final int y()
    {
        return this.y;
    }
}
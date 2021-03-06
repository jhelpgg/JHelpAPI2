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

package jhelp.game.gui;

import jhelp.game.data.Zoom;
import jhelp.game.data.Zoomable;
import jhelp.util.data.Observable;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpSprite;
import jhelp.util.gui.dynamic.Position;
import jhelp.util.gui.dynamic.Positionable;

/**
 * Created by jhelp on 20/07/17.
 */
public final class GameSprite implements Positionable, Zoomable
{
    private final JHelpImage           image;
    private final Observable<Position> positionObservable;
    private       JHelpSprite          sprite;
    private final int                  tx;
    private final int                  ty;
    private final Observable<Boolean>  visibleObservable;
    private final Observable<Zoom>     zoomObservable;

    GameSprite(int width, int height)
    {
        this.tx = width >> 1;
        this.ty = height >> 1;
        this.image = new JHelpImage(width, height);
        this.image.playWhenExitDrawMode(this::refreshImage);
        this.positionObservable = new Observable<>(new Position(this.tx, this.ty));
        this.zoomObservable = new Observable<>(new Zoom(1, 1));
        this.visibleObservable = new Observable<>(false);
        this.positionObservable.startObserve(this::positionChanged);
        this.zoomObservable.startObserve(this::zoomChanged);
        this.visibleObservable.startObserve(this::visibilityChanged);
    }

    private void positionChanged(Observable<Position> positionObservable, Position position)
    {
        synchronized (this.image)
        {
            if (this.sprite != null)
            {
                this.sprite.setPosition(position.getX() - this.ty, position.getY() - this.ty);
            }
        }
    }

    private void refreshImage(JHelpImage image)
    {
        this.refreshSprite();
        this.image.playWhenExitDrawMode(this::refreshImage);
    }

    private void refreshSprite()
    {
        synchronized (this.image)
        {
            if (this.sprite == null)
            {
                return;
            }

            int  imageWidth  = this.image.getWidth();
            int  imageHeight = this.image.getHeight();
            Zoom zoom        = this.zoomObservable.value();
            int  width       = (int) Math.ceil(imageWidth * zoom.zoomWidth());
            int  height      = (int) Math.ceil(imageHeight * zoom.zoomHeight());
            int  x           = (imageWidth - width) >> 1;
            int  y           = (imageHeight - height) >> 1;

            boolean visible = this.sprite.isVisible();
            this.sprite.setVisible(false);
            JHelpImage imageSprite = this.sprite.getImage();
            imageSprite.startDrawMode();
            imageSprite.clear(0);
            imageSprite.fillRectangleScaleBetter(x, y, width, height, this.image, false);
            imageSprite.endDrawMode();
            this.sprite.setVisible(visible);
        }
    }

    private void visibilityChanged(Observable<Boolean> visibleObservable, Boolean visible)
    {
        synchronized (this.image)
        {
            if (this.sprite != null)
            {
                this.sprite.setVisible(visible);
            }
        }
    }

    private void zoomChanged(Observable<Zoom> zoomObservable, Zoom zoom)
    {
        this.refreshSprite();
    }

    void ensureInitialized(JHelpImage parent)
    {
        synchronized (this.image)
        {
            if (this.sprite == null)
            {
                Position position = this.positionObservable.value();
                this.sprite = parent.createSprite(position.getX() - this.tx, position.getY() - this.ty,
                                                  this.image.getWidth(),
                                                  this.image.getHeight());
                this.sprite.setVisible(this.visibleObservable.value());
                this.refreshSprite();
            }
        }
    }

    public JHelpImage image()
    {
        return this.image;
    }

    /**
     * Current position
     *
     * @return Current position
     */
    @Override
    public Position position()
    {
        return this.positionObservable.value();
    }

    /**
     * Change position
     *
     * @param position New position
     */
    @Override
    public void position(final Position position)
    {
        this.positionObservable.value(position);
    }

    public Observable<Position> positionObservable()
    {
        return this.positionObservable;
    }

    public boolean visible()
    {
        return this.visibleObservable.value();
    }

    public void visible(boolean visible)
    {
        this.visibleObservable.value(visible);
    }

    public Observable<Boolean> visibleObservable()
    {
        return this.visibleObservable;
    }

    @Override
    public Zoom zoom()
    {
        return this.zoomObservable.value();
    }

    @Override
    public void zoom(final Zoom zoom)
    {
        this.zoomObservable.value(zoom);
    }

    public Observable<Zoom> zoomObservable()
    {
        return this.zoomObservable;
    }
}

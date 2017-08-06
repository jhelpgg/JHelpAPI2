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

import com.sun.istack.internal.NotNull;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Objects;
import javax.swing.JComponent;
import jhelp.util.debug.Debug;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpSprite;
import jhelp.util.list.ArrayObject;
import jhelp.util.list.Queue;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.Future;
import jhelp.util.thread.ThreadManager;

/**
 * Created by jhelp on 11/07/17.
 */
final class GameScreen extends JComponent implements KeyListener
{
    private       float                      absoluteFrame;
    private       boolean                    alive;
    private final ArrayObject<GameAnimation> animations;
    private final Queue<GameAnimation>       animationsToEnd;
    private final Queue<GameAnimation>       animationsToStart;
    private final JHelpImage                 image;
    private final KeyMap                     keyMap;
    private final Screen                     screen;
    private final ArrayObject<GameSprite>    sprites;
    private final Queue<SpriteCreator>       spritesToCreate;
    private final Queue<JHelpSprite>         spritesToRemove;

    GameScreen(@NotNull Screen screen, int width, int height, KeyMap keyMap)
    {
        Objects.requireNonNull(screen, "screen MUST NOT be null!");
        Debug.debug(width, "x", height);
        Dimension dimension = new Dimension(width, height);
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);
        this.setMinimumSize(dimension);
        this.sprites = new ArrayObject<>();
        this.spritesToCreate = new Queue<>();
        this.animationsToStart = new Queue<>();
        this.animations = new ArrayObject<>();
        this.animationsToEnd = new Queue<>();
        this.spritesToRemove = new Queue<>();
        this.image = new JHelpImage(width, height);
        this.alive = true;
        this.screen = screen;
        this.keyMap = keyMap;
        ThreadManager.parallel(this::pipeline, 128);
        this.setFocusable(true);
        this.requestFocus();
        this.requestFocusInWindow();
        this.addKeyListener(this);
    }

    private void createSpritesAndStartAnimations(JHelpImage image)
    {
        synchronized (this.spritesToCreate)
        {
            while (!this.spritesToCreate.empty())
            {
                this.spritesToCreate.outQueue().createSprite(image);
            }
        }

        synchronized (this.sprites)
        {
            for (GameSprite gameSprite : this.sprites)
            {
                gameSprite.ensureInitialized(image);
            }
        }

        synchronized (this.animationsToStart)
        {
            GameAnimation gameAnimation;

            while (!this.animationsToStart.empty())
            {
                gameAnimation = this.animationsToStart.outQueue();
                gameAnimation.start(this.absoluteFrame, image);
                this.animations.add(gameAnimation);
            }
        }
    }

    private void pipeline()
    {
        final long                     startTime                        = System.currentTimeMillis();
        final ConsumerTask<JHelpImage> createSpritesAndStartAnimations  = this::createSpritesAndStartAnimations;
        final ConsumerTask<JHelpImage> refreshScreenAndUpdateAnimations = this::refreshScreenAndUpdateAnimations;
        final ConsumerTask<JHelpImage> removeSpritesAndEndAnimations    = this::removeSpritesAndEndAnimations;

        while (this.alive)
        {
            this.absoluteFrame = ((float) (System.currentTimeMillis() - startTime)) / 40f;
            this.image.playOutDrawMode(createSpritesAndStartAnimations);
            this.image.playInDrawMode(refreshScreenAndUpdateAnimations);
            this.image.playOutDrawMode(removeSpritesAndEndAnimations);
        }
    }

    private void refreshScreenAndUpdateAnimations(JHelpImage image)
    {
        for (int index = this.animations.size() - 1; index >= 0; index--)
        {
            if (!this.animations.get(index).update(this.absoluteFrame, image))
            {
                this.animationsToEnd.inQueue(this.animations.remove(index));
            }
        }

        this.screen.updateScreen(image);
    }

    private void removeSpritesAndEndAnimations(JHelpImage image)
    {
        while (!this.animationsToEnd.empty())
        {
            this.animationsToEnd.outQueue().endAnimation(image);
        }

        synchronized (this.spritesToRemove)
        {
            while (!this.spritesToRemove.empty())
            {
                image.removeSprite(this.spritesToRemove.outQueue());
            }
        }
    }

    @Override
    protected void paintComponent(final Graphics graphics)
    {
        graphics.drawImage(this.image.getImage(), 0, 0, this);
    }

    public Future<JHelpSprite> createSprite(int x, int y, int width, int height)
    {
        SpriteCreator spriteCreator = new SpriteCreator(x, y, width, height);

        synchronized (this.spritesToCreate)
        {
            this.spritesToCreate.inQueue(spriteCreator);
        }

        return spriteCreator.future();
    }

    public GameSprite createSprite(int width, int height)
    {
        GameSprite gameSprite = new GameSprite(width, height);

        synchronized (this.sprites)
        {
            this.sprites.add(gameSprite);
        }

        return gameSprite;
    }

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param keyEvent Key event description
     */
    @Override
    public void keyTyped(final KeyEvent keyEvent)
    {
    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param keyEvent Key event description
     */
    @Override
    public void keyPressed(final KeyEvent keyEvent)
    {
    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param keyEvent Key event description
     */
    @Override
    public void keyReleased(final KeyEvent keyEvent)
    {
    }

    /**
     * Play an animation
     *
     * @param gameAnimation Animation to play
     */
    public void playAnimation(@NotNull GameAnimation gameAnimation)
    {
        synchronized (this.animationsToStart)
        {
            this.animationsToStart.inQueue(gameAnimation);
        }
    }

    public void removeSprite(@NotNull JHelpSprite sprite)
    {
        synchronized (this.spritesToRemove)
        {
            this.spritesToRemove.inQueue(sprite);
        }
    }

    public void stop()
    {
        this.alive = false;
    }
}

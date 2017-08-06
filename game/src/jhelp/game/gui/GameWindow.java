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
import jhelp.util.gui.JHelpSprite;
import jhelp.util.preference.Preferences;
import jhelp.util.thread.Future;

/**
 * Created by jhelp on 11/07/17.
 */
public final class GameWindow
{
    private final GameFrame gameFrame;

    public GameWindow(@NotNull String gameName, @NotNull Screen screen)
    {
        this.gameFrame = new GameFrame(gameName, screen);
    }

    public Future<JHelpSprite> createSprite(int x, int y, int width, int height)
    {
        return this.gameFrame.gameScreen().createSprite(x, y, width, height);
    }

    public GameSprite createSprite(int width, int height)
    {
        return this.gameFrame.gameScreen().createSprite(width, height);
    }

    public void exit()
    {
        this.gameFrame.exitGame();
    }

    public KeyMap keyMap()
    {
        return this.gameFrame.keyMap();
    }

    public void playAnimation(GameAnimation gameAnimation)
    {
        this.gameFrame.gameScreen().playAnimation(gameAnimation);
    }

    public Preferences preferences()
    {
        return this.gameFrame.preferences();
    }

    public void removeSprite(JHelpSprite sprite)
    {
        this.gameFrame.gameScreen().removeSprite(sprite);
    }

    public void show()
    {
        this.gameFrame.setVisible(true);
    }
}

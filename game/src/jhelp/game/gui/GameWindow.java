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

    public void show()
    {
        this.gameFrame.setVisible(true);
    }

    public void exit()
    {
        this.gameFrame.exitGame();
    }

    public Preferences preferences()
    {
        return this.gameFrame.preferences();
    }

    public Future<JHelpSprite> createSprite(int x, int y, int width, int height)
    {
        return this.gameFrame.gameScreen().createSprite(x, y, width, height);
    }

    public GameSprite createSprite(int width, int height)
    {
        return this.gameFrame.gameScreen().createSprite(width, height);
    }

    public void removeSprite(JHelpSprite sprite)
    {
        this.gameFrame.gameScreen().removeSprite(sprite);
    }

    public void playAnimation(GameAnimation gameAnimation)
    {
        this.gameFrame.gameScreen().playAnimation(gameAnimation);
    }

    public KeyMap keyMap()
    {
        return this.gameFrame.keyMap();
    }
}

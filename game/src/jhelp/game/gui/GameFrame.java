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
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.util.Objects;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import jhelp.util.debug.Debug;
import jhelp.util.gui.UtilGUI;
import jhelp.util.io.UtilIO;
import jhelp.util.preference.Preferences;
import jhelp.util.text.UtilText;

/**
 * Created by jhelp on 11/07/17.
 */
final class GameFrame extends JFrame
{
    private final String      gameName;
    private final GameScreen  gameScreen;
    private final KeyMap      keyMap;
    private final Preferences preferences;

    GameFrame(@NotNull String gameName, @NotNull Screen screen)
    {
        Objects.requireNonNull(gameName, "gameName MUST NOT be null!");
        Objects.requireNonNull(screen, "screen MUST NOT be null!");

        if (gameName.isEmpty())
        {
            throw new IllegalArgumentException("Game name MUST NOT be empty");
        }

        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setUndecorated(true);
        this.setResizable(false);

        if (this.isAlwaysOnTopSupported())
        {
            this.setAlwaysOnTop(true);
        }

        this.gameName = gameName;
        this.setTitle("--- " + this.gameName + " ---");
        String preferencesName = "JHelp/games/" +
                                 UtilText.removeAccent(this.gameName.toLowerCase()).replaceAll("[^a-z0-9]+", "_") +
                                 ".preferences";
        this.preferences = new Preferences(UtilIO.obtainExternalFile(preferencesName));
        this.keyMap = new KeyMap(this.preferences);
        Rectangle rectangle = UtilGUI.getScreenBounds(0);
        this.gameScreen = new GameScreen(screen, rectangle.width, rectangle.height, this.keyMap);
        this.setLayout(new BorderLayout());
        this.add(this.gameScreen, BorderLayout.CENTER);
        UtilGUI.packedSize(this);
        this.setLocation(rectangle.x, rectangle.y);
    }

    @Override
    protected void processWindowEvent(final WindowEvent windowEvent)
    {
        switch (windowEvent.getID())
        {
            case WindowEvent.WINDOW_CLOSING:
                this.exitGame();
                return;
        }

        super.processWindowEvent(windowEvent);
    }

    public void exitGame()
    {
        // TODO Check if exit is allowed
        Debug.todo("Check if exit is allowed");

        //Real exit
        this.gameScreen.stop();
        this.setVisible(false);
        this.dispose();
    }

    public GameScreen gameScreen()
    {
        return this.gameScreen;
    }

    public KeyMap keyMap()
    {
        return this.keyMap;
    }

    public Preferences preferences()
    {
        return this.preferences;
    }
}

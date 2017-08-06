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

package jhelp.game;

import jhelp.game.gui.GameWindow;
import jhelp.game.gui.Screen;
import jhelp.util.gui.JHelpImage;

/**
 * Created by jhelp on 11/07/17.
 */
public class MainSample implements Screen
{
    private static final int SIZE = 64;

    public static void main(String[] arguments)
    {
        GameWindow gameWindow = new GameWindow("Test", new MainSample());
        gameWindow.show();
    }
    private int sx = 1;
    private int sy = 1;
    private int x  = 0;
    private int y  = 0;

    @Override
    public void updateScreen(final JHelpImage image)
    {
        image.clear(0xFF000000);
        image.fillRectangle(this.x, this.y, MainSample.SIZE, MainSample.SIZE, 0xCAFEFACE);
        int maxX = image.getWidth() - MainSample.SIZE;
        int maxY = image.getHeight() - MainSample.SIZE;

        if (this.x + this.sx > maxX || this.x + this.sx < 0)
        {
            this.sx = -this.sx;
        }

        if (this.y + this.sy > maxY || this.y + this.sy < 0)
        {
            this.sy = -this.sy;
        }

        this.x += this.sx;
        this.y += this.sy;
    }
}

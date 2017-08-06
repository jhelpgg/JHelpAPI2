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

import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpSprite;
import jhelp.util.thread.Future;
import jhelp.util.thread.Promise;

/**
 * Created by jhelp on 12/07/17.
 */
class SpriteCreator
{
    private final int                  height;
    private       Promise<JHelpSprite> spritePromise;
    private final int                  width;
    private final int                  x;
    private final int                  y;

    SpriteCreator(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = Math.max(1, width);
        this.height = Math.max(1, height);
        this.spritePromise = new Promise<>();
    }

    void createSprite(JHelpImage parent)
    {
        this.spritePromise.setResult(parent.createSprite(this.x, this.y, this.width, this.height));
        this.spritePromise = null;
    }

    Future<JHelpSprite> future()
    {
        return this.spritePromise.future();
    }
}

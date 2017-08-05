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
    private       Promise<JHelpSprite> spritePromise;
    private final int                  x;
    private final int                  y;
    private final int                  width;
    private final int                  height;

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

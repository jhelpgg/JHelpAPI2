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

package jhelp.ia.game.tictactoe;

/**
 * Created by jhelp on 24/06/17.
 */
public enum Status
{
    UNKNOWN(0),
    WIN(100),
    LOOSE(0),
    DRAW(50);
    private final int valueComputer;

    Status(int valueComputer)
    {
        this.valueComputer = valueComputer;
    }

    public int value()
    {
        return this.valueComputer;
    }
}

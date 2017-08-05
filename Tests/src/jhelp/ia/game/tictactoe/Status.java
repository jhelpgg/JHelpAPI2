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

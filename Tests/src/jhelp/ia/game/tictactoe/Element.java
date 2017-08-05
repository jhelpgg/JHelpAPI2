package jhelp.ia.game.tictactoe;

/**
 * Created by jhelp on 24/06/17.
 */
public enum Element
{
    EMPTY(' '),
    COMPUTER('X'),
    PLAYER('O');
    private final char representation;

    Element(char representation)
    {
        this.representation = representation;
    }

    public char representation()
    {
        return this.representation;
    }
}

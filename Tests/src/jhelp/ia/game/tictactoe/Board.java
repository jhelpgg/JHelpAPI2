package jhelp.ia.game.tictactoe;

import com.sun.istack.internal.NotNull;
import java.awt.Point;
import java.util.Arrays;
import java.util.Objects;

/**
 * Created by jhelp on 24/06/17.
 */
public class Board
{
    private final Element[] board;
    private       Point     position;

    public Board()
    {
        this.board = new Element[9];

        for (int i = 0; i < 9; i++)
        {
            this.board[i] = Element.EMPTY;
        }

        this.position = new Point(-1, -1);
    }

    public Point position()
    {
        return new Point(this.position);
    }

    public void position(int x, int y)
    {
        this.check(x, y);
        this.position.x = x;
        this.position.y = y;
    }

    @Override
    public String toString()
    {
        char[] draw = new char[30];

        draw[0] = this.board[0].representation();
        draw[1] = '|';
        draw[2] = this.board[1].representation();
        draw[3] = '|';
        draw[4] = this.board[2].representation();
        draw[5] = '\n';

        draw[6] = '-';
        draw[7] = '+';
        draw[8] = '-';
        draw[9] = '+';
        draw[10] = '-';
        draw[11] = '\n';

        draw[12] = this.board[3].representation();
        draw[13] = '|';
        draw[14] = this.board[4].representation();
        draw[15] = '|';
        draw[16] = this.board[5].representation();
        draw[17] = '\n';

        draw[18] = '-';
        draw[19] = '+';
        draw[20] = '-';
        draw[21] = '+';
        draw[22] = '-';
        draw[23] = '\n';

        draw[24] = this.board[6].representation();
        draw[25] = '|';
        draw[26] = this.board[7].representation();
        draw[27] = '|';
        draw[28] = this.board[8].representation();
        draw[29] = '\n';

        return new String(draw);
    }

    public Board copy()
    {
        Board copy = new Board();
        System.arraycopy(this.board, 0, copy.board, 0, 9);
        return copy;
    }

    private void check(int x, int y)
    {
        if (x < 0 || x > 2 || y < 0 || y > 2)
        {
            throw new IllegalArgumentException("(" + x + ", " + y + ") outside of the board!");
        }
    }

    public Element get(int x, int y)
    {
        this.check(x, y);
        return this.board[x + y * 3];
    }

    public void set(int x, int y, @NotNull Element element)
    {
        Objects.requireNonNull(element, "element MUST NOT be null!");
        this.check(x, y);
        this.board[x + y * 3] = element;
    }

    public @NotNull Status status()
    {
        for (int x = 0, y = 0; x < 3; x++, y += 3)
        {
            if (this.board[x] == this.board[x + 3] && this.board[x] == this.board[x + 6])
            {
                switch (this.board[x])
                {
                    case COMPUTER:
                        return Status.WIN;
                    case PLAYER:
                        return Status.LOOSE;
                }
            }

            if (this.board[y] == this.board[y + 1] && this.board[y] == this.board[y + 2])
            {
                switch (this.board[y])
                {
                    case COMPUTER:
                        return Status.WIN;
                    case PLAYER:
                        return Status.LOOSE;
                }
            }
        }

        if (this.board[0] == this.board[4] && this.board[0] == this.board[8])
        {
            switch (this.board[0])
            {
                case COMPUTER:
                    return Status.WIN;
                case PLAYER:
                    return Status.LOOSE;
            }
        }

        if (this.board[2] == this.board[4] && this.board[2] == this.board[6])
        {
            switch (this.board[2])
            {
                case COMPUTER:
                    return Status.WIN;
                case PLAYER:
                    return Status.LOOSE;
            }
        }

        final boolean haveEmpty = Arrays.stream(this.board).anyMatch(element -> element == Element.EMPTY);

        if (haveEmpty)
        {
            return Status.UNKNOWN;
        }

        return Status.DRAW;
    }
}

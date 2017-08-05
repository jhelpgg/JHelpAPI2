package jhelp.ia.game.tictactoe;

import jhelp.ia.astar.AStarNode;
import jhelp.ia.astar.Quantifier;

/**
 * Created by jhelp on 24/06/17.
 */
public class QuantifierBoard implements Quantifier<Board>
{
    private static int computerTest = 0;

    public static void computer(boolean first)
    {
        if (first)
        {
            QuantifierBoard.computerTest = 0;
        }
        else
        {
            QuantifierBoard.computerTest = 1;
        }
    }

    public static boolean isComputer(AStarNode<Board> starNode)
    {
        return (starNode.depth() & 1) == QuantifierBoard.computerTest;
    }

    public static final QuantifierBoard QUANTIFIER_BOARD = new QuantifierBoard();

    private QuantifierBoard()
    {
    }

    @Override
    public int value(final AStarNode<Board> starNode)
    {
        Status status = starNode.data().status();
        int    value  = status.value();

        if (status == Status.UNKNOWN)
        {
            if (QuantifierBoard.isComputer(starNode))
            {
                value = Status.LOOSE.value();

                for (AStarNode<Board> child : starNode)
                {
                    value = Math.max(value, this.value(child));
                }
            }
            else
            {
                value = Status.WIN.value();

                for (AStarNode<Board> child : starNode)
                {
                    value = Math.min(value, this.value(child));
                }
            }
        }

        return value;
    }
}

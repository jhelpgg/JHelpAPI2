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

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import jhelp.ia.astar.AStarNode;
import jhelp.ia.astar.AStarTree;
import jhelp.util.gui.UtilGUI;

/**
 * Created by jhelp on 24/06/17.
 */
public class TicTacToe
{
    private static AStarTree<Board> evaluate(Board board)
    {
        AStarTree<Board> tree = new AStarTree<>(board.copy(), QuantifierBoard.QUANTIFIER_BOARD);
        AStarNode<Board> root = tree.root();
        TicTacToe.evaluate(board, root);
        return tree;
    }

    private static void evaluate(Board board, AStarNode<Board> node)
    {
        Board            copy;
        AStarNode<Board> child;

        for (int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 3; x++)
            {
                if (board.get(x, y) == Element.EMPTY)
                {
                    copy = board.copy();

                    if (QuantifierBoard.isComputer(node))
                    {
                        copy.set(x, y, Element.COMPUTER);
                    }
                    else
                    {
                        copy.set(x, y, Element.PLAYER);
                    }

                    copy.position(x, y);
                    child = node.addChild(copy, QuantifierBoard.QUANTIFIER_BOARD);

                    if (copy.status() == Status.UNKNOWN)
                    {
                        TicTacToe.evaluate(copy, child);
                    }
                }
            }
        }
    }

    public static void main(String[] arguments)
    {
        Board board = new Board();
        QuantifierBoard.computer(false);
        AStarTree<Board> tree           = TicTacToe.evaluate(board);
        EvaluationTree   evaluationTree = new EvaluationTree(tree);
        JTree            treeUI         = new JTree(evaluationTree);
        treeUI.setCellRenderer(new NodeRenderer());
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(treeUI), BorderLayout.CENTER);
        JButton button = new JButton("SORT");
        button.addActionListener(event -> evaluationTree.sort());
        frame.add(button, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        UtilGUI.packedSize(frame);
        UtilGUI.centerOnScreen(frame);
        frame.setVisible(true);
    }
}

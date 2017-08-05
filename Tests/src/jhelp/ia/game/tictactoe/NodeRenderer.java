package jhelp.ia.game.tictactoe;

import java.awt.Component;
import java.awt.Point;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;
import jhelp.ia.astar.AStarNode;
import jhelp.util.gui.JHelpFont;

/**
 * Created by jhelp on 24/06/17.
 */
public class NodeRenderer extends JTextArea implements TreeCellRenderer
{
    public NodeRenderer()
    {
        this.setColumns(7);
        this.setRows(6);
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setFont(JHelpFont.DEFAULT.getFont());
    }

    /**
     * Sets the value of the current tree cell to <code>value</code>.
     * If <code>selected</code> is true, the cell will be drawn as if
     * selected. If <code>expanded</code> is true the node is currently
     * expanded and if <code>leaf</code> is true the node represents a
     * leaf and if <code>hasFocus</code> is true the node currently has
     * focus. <code>tree</code> is the <code>JTree</code> the receiver is being
     * configured for.  Returns the <code>Component</code> that the renderer
     * uses to draw the value.
     * <p>
     * The <code>TreeCellRenderer</code> is also responsible for rendering the
     * the cell representing the tree's current DnD drop location if
     * it has one. If this renderer cares about rendering
     * the DnD drop location, it should query the tree directly to
     * see if the given row represents the drop location:
     * <pre>
     *     JTree.DropLocation dropLocation = tree.getDropLocation();
     *     if (dropLocation != null
     *             &amp;&amp; dropLocation.getChildIndex() == -1
     *             &amp;&amp; tree.getRowForPath(dropLocation.getPath()) == row) {
     *
     *         // this row represents the current drop location
     *         // so render it specially, perhaps with a different color
     *     }
     * </pre>
     *
     * @param tree
     * @param value
     * @param selected
     * @param expanded
     * @param leaf
     * @param row
     * @param hasFocus
     * @return the <code>Component</code> that the renderer uses to draw the value
     */
    @Override public Component getTreeCellRendererComponent(
            final JTree tree, final Object value, final boolean selected, final boolean expanded, final boolean leaf,
            final int row, final boolean hasFocus)
    {
        final AStarNode<Board> node     = (AStarNode<Board>) value;
        final Board            board    = node.data();
        final Point            position = board.position();
        this.setText(board.toString() + node.number() + "\n(" + position.x + ", " + position.y + ")");
        return this;
    }
}

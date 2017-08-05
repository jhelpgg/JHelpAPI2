package jhelp.ia.game.tictactoe;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import jhelp.ia.astar.AStarNode;
import jhelp.ia.astar.AStarTree;

/**
 * Created by jhelp on 24/06/17.
 */
public class EvaluationTree implements TreeModel
{
    private final List<TreeModelListener> listeners;
    private       AStarNode<Board>        root;

    public EvaluationTree(AStarTree<Board> tree)
    {
        this.listeners = new ArrayList<>();
        tree.sort();
        this.root = tree.root();
    }

    /**
     * Returns the root of the tree.  Returns <code>null</code>
     * only if the tree has no nodes.
     *
     * @return the root of the tree
     */
    @Override
    public Object getRoot()
    {
        return this.root;
    }

    /**
     * Returns the child of <code>parent</code> at index <code>index</code>
     * in the parent's
     * child array.  <code>parent</code> must be a node previously obtained
     * from this data source. This should not return <code>null</code>
     * if <code>index</code>
     * is a valid index for <code>parent</code> (that is <code>index &gt;= 0 &amp;&amp;
     * index &lt; getChildCount(parent</code>)).
     *
     * @param parent a node in the tree, obtained from this data source
     * @param index
     * @return the child of <code>parent</code> at index <code>index</code>
     */
    @Override
    public Object getChild(final Object parent, final int index)
    {
        return ((AStarNode<Board>) parent).child(index);
    }

    /**
     * Returns the number of children of <code>parent</code>.
     * Returns 0 if the node
     * is a leaf or if it has no children.  <code>parent</code> must be a node
     * previously obtained from this data source.
     *
     * @param parent a node in the tree, obtained from this data source
     * @return the number of children of the node <code>parent</code>
     */
    @Override public int getChildCount(final Object parent)
    {
        return ((AStarNode<Board>) parent).numberOfChildren();
    }

    /**
     * Returns <code>true</code> if <code>node</code> is a leaf.
     * It is possible for this method to return <code>false</code>
     * even if <code>node</code> has no children.
     * A directory in a filesystem, for example,
     * may contain no files; the node representing
     * the directory is not a leaf, but it also has no children.
     *
     * @param node a node in the tree, obtained from this data source
     * @return true if <code>node</code> is a leaf
     */
    @Override
    public boolean isLeaf(final Object node)
    {
        return this.getChildCount(node) == 0;
    }

    /**
     * Messaged when the user has altered the value for the item identified
     * by <code>path</code> to <code>newValue</code>.
     * If <code>newValue</code> signifies a truly new value
     * the model should post a <code>treeNodesChanged</code> event.
     *
     * @param path     path to the node that the user has altered
     * @param newValue the new value from the TreeCellEditor
     */
    @Override
    public void valueForPathChanged(final TreePath path, final Object newValue)
    {
        //Nothing to do
    }

    /**
     * Returns the index of child in parent.  If either <code>parent</code>
     * or <code>child</code> is <code>null</code>, returns -1.
     * If either <code>parent</code> or <code>child</code> don't
     * belong to this tree model, returns -1.
     *
     * @param parent a node in the tree, obtained from this data source
     * @param child  the node we are interested in
     * @return the index of the child in the parent, or -1 if either
     * <code>child</code> or <code>parent</code> are <code>null</code>
     * or don't belong to this tree model
     */
    @Override
    public int getIndexOfChild(final Object parent, final Object child)
    {
        AStarNode<Board> node  = (AStarNode<Board>) parent;
        int              count = node.numberOfChildren();

        for (int index = 0; index < count; index++)
        {
            if (node.child(index).equals(child))
            {
                return index;
            }
        }

        return -1;
    }

    /**
     * Adds a listener for the <code>TreeModelEvent</code>
     * posted after the tree changes.
     *
     * @param treeModelListener the listener to add
     * @see #removeTreeModelListener
     */
    @Override public void addTreeModelListener(final TreeModelListener treeModelListener)
    {
        if (treeModelListener != null)
        {
            synchronized (this.listeners)
            {
                if (!this.listeners.contains(treeModelListener))
                {
                    this.listeners.add(treeModelListener);
                }
            }
        }
    }

    /**
     * Removes a listener previously added with
     * <code>addTreeModelListener</code>.
     *
     * @param treeModelListener the listener to remove
     * @see #addTreeModelListener
     */
    @Override public void removeTreeModelListener(final TreeModelListener treeModelListener)
    {
        synchronized (this.listeners)
        {
            this.listeners.remove(treeModelListener);
        }
    }

    public void sort()
    {
        this.root.sort();

        synchronized (this.listeners)
        {
            TreeModelEvent treeModelEvent = new TreeModelEvent(this, new Object[]{this.root}, null, null);

            for (TreeModelListener listener : this.listeners)
            {
                listener.treeStructureChanged(treeModelEvent);
            }
        }
    }
}

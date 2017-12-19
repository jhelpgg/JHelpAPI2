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

/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.gui.samples.d2.tree;

import jhelp.gui.twoD.JHelpTreeModel;
import jhelp.util.gui.JHelpImage;
import jhelp.util.list.Tree;
import jhelp.util.list.Tree.SearchMode;

/**
 * Tree model sample
 *
 * @author JHelp
 */
public class TreeModelSample
        extends JHelpTreeModel<TreeNodeSample>
{
    /** Tree */
    private final Tree<TreeNodeSample> tree;

    /**
     * Create a new instance of TreeModelSample
     */
    public TreeModelSample()
    {
        this.tree = new Tree<TreeNodeSample>(new TreeNodeSample("ROOT"));

        final Tree<TreeNodeSample> branch1 = this.tree.addBranch(new TreeNodeSample("Branch 1"));
        final Tree<TreeNodeSample> branch2 = this.tree.addBranch(new TreeNodeSample("Branch 2"));
        this.tree.addBranch(new TreeNodeSample("Branch 3"));
        final Tree<TreeNodeSample> branch4 = this.tree.addBranch(new TreeNodeSample("Branch 4"));
        final Tree<TreeNodeSample> branch5 = this.tree.addBranch(new TreeNodeSample("Branch 5"));

        this.tree.addBranch(new TreeNodeSample("Leaf"));

        Tree<TreeNodeSample> branch;
        for (int i = 0; i < 10; i++)
        {
            branch1.addBranch(new TreeNodeSample("Branch 1-" + i));
            branch2.addBranch(new TreeNodeSample("Branch 2-" + i));
            branch4.addBranch(new TreeNodeSample("Branch 4-" + i));
            branch = branch5.addBranch(new TreeNodeSample("Branch 5-" + i));

            for (int j = 0; j < i; j++)
            {
                branch.addBranch(new TreeNodeSample("Sub branch " + i + "*" + j + "=" + (i * j)));
            }
        }
    }

    /**
     * Obtain child from parent <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param parent
     *           Node parent
     * @param index
     *           Child index
     * @return Child
     * @see jhelp.gui.twoD.JHelpTreeModel#child(java.lang.Object, int)
     */
    @Override
    public TreeNodeSample child(final TreeNodeSample parent, final int index)
    {
        final Tree<TreeNodeSample> node = this.tree.searchBranch(new SearchByID(parent.getID()),
                                                                 SearchMode.LEFT_TO_RIGHT_DEPTH, false);

        if (node == null)
        {
            return null;
        }

        return node.getBranch(index).getInformation();
    }

    /**
     * Change node expand state <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param information
     *           Node
     * @param expand
     *           New expand state
     * @see jhelp.gui.twoD.JHelpTreeModel#expand(java.lang.Object, boolean)
     */
    @Override
    public void expand(final TreeNodeSample information, final boolean expand)
    {
        if (information.isExpand() == expand)
        {
            return;
        }

        information.setExpand(expand);
        this.fireModelChanged();
    }

    /**
     * Indicates if node is expand <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param node
     *           Node tested
     * @return {@code true} if expand
     * @see jhelp.gui.twoD.JHelpTreeModel#expanded(java.lang.Object)
     */
    @Override
    public boolean expanded(final TreeNodeSample node)
    {
        return node.isExpand();
    }

    /**
     * Obtain node number of children <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param parent
     *           Node
     * @return Number of children
     * @see jhelp.gui.twoD.JHelpTreeModel#numberOfChildren(java.lang.Object)
     */
    @Override
    public int numberOfChildren(final TreeNodeSample parent)
    {
        final Tree<TreeNodeSample> node = this.tree.searchBranch(new SearchByID(parent.getID()),
                                                                 SearchMode.LEFT_TO_RIGHT_DEPTH, false);

        if (node == null)
        {
            return 0;
        }

        return node.numberOfBranch();
    }

    /**
     * Node image representation <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param information
     *           Node
     * @return {@code null}
     * @see jhelp.gui.twoD.JHelpTreeModel#obtainImageRepresentation(java.lang.Object)
     */
    @Override
    public JHelpImage obtainImageRepresentation(final TreeNodeSample information)
    {
        return null;
    }

    /**
     * Node text representation <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param information
     *           Node
     * @return Node text representation
     * @see jhelp.gui.twoD.JHelpTreeModel#obtainTextRepresentation(java.lang.Object)
     */
    @Override
    public String obtainTextRepresentation(final TreeNodeSample information)
    {
        return information.getMessage();
    }

    /**
     * Obtain child's parent <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param child
     *           Child
     * @return Child'sparent or {@code null} if none
     * @see jhelp.gui.twoD.JHelpTreeModel#parent(java.lang.Object)
     */
    @Override
    public TreeNodeSample parent(final TreeNodeSample child)
    {
        final Tree<TreeNodeSample> node = this.tree.searchBranch(new SearchByID(child.getID()),
                                                                 SearchMode.LEFT_TO_RIGHT_DEPTH, false);

        if (node == null)
        {
            return null;
        }

        final Tree<TreeNodeSample> parent = node.getTrunk();

        if (parent == null)
        {
            return null;
        }

        return parent.getInformation();
    }

    /**
     * Tree root <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Tree root
     * @see jhelp.gui.twoD.JHelpTreeModel#root()
     */
    @Override
    public TreeNodeSample root()
    {
        return this.tree.getInformation();
    }

    /**
     * Indicates if use image representation for a node <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param information
     *           Node
     * @return {@code false}
     * @see jhelp.gui.twoD.JHelpTreeModel#useImageRepresentation(java.lang.Object)
     */
    @Override
    public boolean useImageRepresentation(final TreeNodeSample information)
    {
        return false;
    }
}
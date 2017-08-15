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

package jhelp.ontology;

import java.util.Stack;
import jhelp.util.list.Tree;

/**
 * Created by jhelp on 23/07/17.
 */
public class TreeCreator
{
    class InternalVisitor implements GraphVisitor
    {
        InternalVisitor()
        {
        }

        @Override
        public void enterNode(final Node node)
        {
            TreeCreator.this.enterNode(node);
        }

        @Override
        public void exitNode(final Node node)
        {
            TreeCreator.this.exitNode(node);
        }
    }

    private final InternalVisitor   internalVisitor;
    private final Stack<Tree<Node>> stack;

    public TreeCreator()
    {
        this.internalVisitor = new InternalVisitor();
        this.stack = new Stack<>();
    }

    void enterNode(final Node node)
    {
        Tree<Node> branch = this.stack.peek();
        this.stack.push(branch.addBranch(node));
    }

    void exitNode(final Node node)
    {
        this.stack.pop();
    }

    public Tree<Node> createTree(Graph graph)
    {
        final Tree<Node> tree = new Tree<>(Node.WILDCARD);
        this.stack.clear();
        this.stack.push(tree);
        graph.visit(this.internalVisitor);
        return tree;
    }
}

package jhelp.antology;

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

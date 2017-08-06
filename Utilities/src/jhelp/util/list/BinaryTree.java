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

package jhelp.util.list;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import jhelp.util.list.Tree.TestFoundListener;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.Filter;
import jhelp.util.thread.Future;
import jhelp.util.thread.Task;

/**
 * A binary solver
 *
 * @param <ELEMENT> Element carry type
 * @author JHelp
 */
public class BinaryTree<ELEMENT> implements ParallelList<ELEMENT, BinaryTree<ELEMENT>>, SizedIterable<ELEMENT>
{
    /**
     * A solver branch
     *
     * @param <TYPE> Element carry type
     * @author JHelp
     */
    public class Branch<TYPE>
    {
        /**
         * Element carry
         */
        TYPE         element;
        /**
         * Left branch
         */
        Branch<TYPE> left;
        /**
         * Parent branch
         */
        Branch<TYPE> parent;
        /**
         * Right branch
         */
        Branch<TYPE> right;

        /**
         * Create a new instance of Branch
         */
        Branch()
        {
        }

        /**
         * Element carry
         *
         * @return Element carry
         */
        public TYPE getElement()
        {
            return this.element;
        }

        /**
         * Left branch
         *
         * @return Left branch
         */
        public Branch<TYPE> getLeft()
        {
            return this.left;
        }

        /**
         * Branch parent.<br>
         * {@code null} if the branch is the root
         *
         * @return Parent branch or {@code null} if it is the root
         */
        public Branch<TYPE> getParent()
        {
            return this.parent;
        }

        /**
         * Right branch
         *
         * @return Right branch
         */
        public Branch<TYPE> getRight()
        {
            return this.right;
        }

        /**
         * String representation <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @return String representation
         * @see Object#toString()
         */
        @Override
        public String toString()
        {
            return this.element + " [ " + this.left + " | " + this.right + " ]";
        }
    }

    /**
     * Tree content
     */
    private final SortedArray<ELEMENT> content;
    /**
     * Indicates if solver is updated
     */
    private       boolean              isUpdated;
    /**
     * Main branch
     */
    private final Branch<ELEMENT>      trunk;

    /**
     * Create a new instance of BinaryTree
     *
     * @param elementClass Element carry class type
     * @param comparator   Comparator used to know how sort the solver
     */
    public BinaryTree(final Class<ELEMENT> elementClass, final Comparator<ELEMENT> comparator)
    {
        if (comparator == null)
        {
            throw new NullPointerException("comparator MUST NOT be null");
        }

        this.trunk = new Branch<>();
        this.content = new SortedArray<>(elementClass, comparator, true);
        this.isUpdated = false;
        this.updateTree();
    }

    /**
     * Fill a string builder with a branch description
     *
     * @param branch        Branch to add
     * @param stringBuilder String builder to fill
     * @param number        Number of branch level
     */
    private void fill(final Branch<ELEMENT> branch, final StringBuilder stringBuilder, final int number)
    {
        stringBuilder.append('\n');

        for (int i = 0; i < number; i++)
        {
            stringBuilder.append("| ");
        }

        stringBuilder.append(branch.element);

        if (branch.left != null)
        {
            this.fill(branch.left, stringBuilder, number + 1);
        }

        if (branch.right != null)
        {
            this.fill(branch.right, stringBuilder, number + 1);
        }
    }

    /**
     * Fill a solver with a branch
     *
     * @param branch Branch to put in the solver
     * @param tree   Tree to fill
     */
    private void fillTree(final Branch<ELEMENT> branch, final Tree<ELEMENT> tree)
    {
        if (branch.left != null)
        {
            this.fillTree(branch.left, tree.addBranch(branch.left.element));
        }

        if (branch.right != null)
        {
            this.fillTree(branch.right, tree.addBranch(branch.right.element));
        }
    }

    /**
     * Insert content interval inside a branch
     *
     * @param branch Branch where insert interval
     * @param start  Interval start index
     * @param end    Interval end index
     */
    private void insert(final Branch<ELEMENT> branch, final int start, final int end)
    {
        final int middle = (start + end) >> 1;
        branch.element = this.content.get(middle);

        if (middle > start)
        {
            branch.left = new Branch<>();
            this.insert(branch.left, start, middle - 1);
        }

        if (middle < end)
        {
            branch.right = new Branch<>();
            this.insert(branch.right, middle + 1, end);
        }
    }

    /**
     * Update the solver
     */
    private void updateTree()
    {
        if (this.isUpdated)
        {
            return;
        }

        this.trunk.element = null;
        this.trunk.left = null;
        this.trunk.right = null;
        final int size = this.content.size();

        if (size > 0)
        {
            this.insert(this.trunk, 0, size - 1);
        }

        this.isUpdated = true;
    }

    /**
     * Obtain an element from the list that match the given filter.<br>
     * The result can be any element in list that match.<br>
     * If no element found, the result future will be on error.<br>
     * The search is do in separate thread
     *
     * @param filter Filter to use for search
     * @return Future link to the search. It will contains the found element or be on error if no elements match
     */
    @Override
    public Future<ELEMENT> any(final Filter<ELEMENT> filter)
    {
        return Future.firstMatch(this, filter);
    }

    /**
     * Execute a task in parallel on each element (filtered gby given filter) of the list.<br>
     * Tasks are launch, and method return immediately<br>
     * Note:
     * <ul>
     * <li> Their no guarantee about the order of elements' list meet by the task. If order mandatory use {@link #consumeAsync(ConsumerTask, Filter)}</li>
     * <li> Since order is not important, thread management is simplified, so it is faster than {@link #consumeAsync(ConsumerTask, Filter)} </li>
     * </ul>
     *
     * @param task   Task to play
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @return Future to track/link to the end of all parallel tasks
     */
    @Override
    public Future<Void> async(final ConsumerTask<ELEMENT> task, final Filter<ELEMENT> filter)
    {
        this.updateTree();
        return this.content.async(task, filter);
    }

    /**
     * Execute task for each element (that respects the given filter) of the list
     *
     * @param task   Task to do
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @return This list itself, convenient for chaining
     */
    @Override
    public BinaryTree<ELEMENT> consume(final ConsumerTask<ELEMENT> task, final Filter<ELEMENT> filter)
    {
        for (ELEMENT element : this)
        {
            if (filter == null || filter.isFiltered(element))
            {
                task.consume(element);
            }
        }

        return this;
    }

    /**
     * Create list composed of filtered elements of the list
     *
     * @param filter Filter to select elements
     * @return List of filtered elements
     */
    @Override
    public BinaryTree<ELEMENT> filter(final Filter<ELEMENT> filter)
    {
        final BinaryTree<ELEMENT> binaryTree = new BinaryTree<>(this.content.typeClass(), this.content.comparator());
        this.consume(binaryTree::putElement, filter);
        return binaryTree;
    }

    /**
     * Execute task for each element (that respects the given filter) of the list and collect the result in other list
     *
     * @param task   Task to do
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @param <R>    Result list element type
     * @return List with transformed elements
     */
    @Override
    public <R> ParallelList<R, ?> flatMap(
            final Task<ELEMENT, ParallelList<R, ?>> task, final Filter<ELEMENT> filter)
    {
        final ArrayObject<R>   arrayObject = new ArrayObject<>();
        final Task<R, Boolean> add         = arrayObject::add;

        for (ELEMENT element : this)
        {
            if (filter == null || filter.isFiltered(element))
            {
                task.playTask(element).map(add);
            }
        }

        return arrayObject;
    }

    /**
     * Execute a task in parallel on each element (filtered gby given filter) of the list.<br>
     * Tasks are launch, and method return immediately<br>
     * Note:
     * <ul>
     * <li> Their no guarantee about the order of elements' list meet by the task. If order mandatory use {@link #consumeAsync(ConsumerTask, Filter)}</li>
     * <li> Since order is not important, thread management is simplified, so it is faster than {@link #consumeAsync(ConsumerTask, Filter)} </li>
     * </ul>
     *
     * @param task   Task to play
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @return The list itself, convenient for chaining
     */
    @Override
    public BinaryTree<ELEMENT> forEach(final ConsumerTask<ELEMENT> task, final Filter<ELEMENT> filter)
    {
        this.updateTree();
        this.content.forEach(task, filter);
        return this;
    }

    /**
     * Execute task for each element (that respects the given filter) of the list and collect the result in other list
     *
     * @param task   Task to do
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @param <R>    Result list element type
     * @return List with transformed elements
     */
    @Override
    public <R> ParallelList<R, ?> map(final Task<ELEMENT, R> task, final Filter<ELEMENT> filter)
    {
        final ArrayObject<R> arrayObject = new ArrayObject<>();

        for (ELEMENT element : this)
        {
            if (filter == null || filter.isFiltered(element))
            {
                arrayObject.add(task.playTask(element));
            }
        }

        return arrayObject;
    }

    /**
     * Execute a task in parallel on each element (filtered gby given filter) of the list.<br>
     * The method will wait all parallel task finished before return<br>
     * Note:
     * <ul>
     * <li> Their no guarantee about the order of elements' list meet by the task. If order mandatory use {@link #consumeAsync(ConsumerTask, Filter)} and {@link Future#waitFinish()}</li>
     * <li> Since order is not important, thread management is simplified, so it is faster than {@link #consumeAsync(ConsumerTask, Filter)} and {@link Future#waitFinish()} </li>
     * </ul>
     *
     * @param task   Task to play
     * @param filter Filter to select elements. If {@code null}, all elements are taken
     * @return The list itself, convenient for chaining
     */
    @Override
    public BinaryTree<ELEMENT> sync(final ConsumerTask<ELEMENT> task, final Filter<ELEMENT> filter)
    {
        this.updateTree();
        this.content.sync(task, filter);
        return this;
    }

    /**
     * Clear the solver
     */
    public void clear()
    {
        this.trunk.element = null;
        this.trunk.left = null;
        this.trunk.right = null;
        this.content.clear();
        this.isUpdated = true;
    }

    /**
     * Collect elements of the solver left to right, deep first
     *
     * @param testFoundListener Filter to use to know if one element is inside searched ones
     * @param list              List to fill with founded elements
     */
    public void collectLeftToRightDepth(final TestFoundListener<ELEMENT> testFoundListener, final List<ELEMENT> list)
    {
        if (testFoundListener == null)
        {
            throw new NullPointerException("testFoundListener MUST NOT be null");
        }

        if (list == null)
        {
            throw new NullPointerException("list MUST NOT be null");
        }

        this.updateTree();

        final Stack<Branch<ELEMENT>> stack = new Stack<>();
        stack.push(this.trunk);
        Branch<ELEMENT> branch;

        while (!stack.isEmpty())
        {
            branch = stack.pop();

            if (testFoundListener.isElementSearched(branch.element))
            {
                list.add(branch.element);
            }

            if (branch.right != null)
            {
                stack.push(branch.right);
            }

            if (branch.left != null)
            {
                stack.push(branch.left);
            }
        }
    }

    /**
     * Collect elements of the solver left to right, high first
     *
     * @param testFoundListener Filter to use to know if one element is inside searched ones
     * @param list              List to fill with founded elements
     */
    public void collectLeftToRightHigh(final TestFoundListener<ELEMENT> testFoundListener, final List<ELEMENT> list)
    {
        if (testFoundListener == null)
        {
            throw new NullPointerException("testFoundListener MUST NOT be null");
        }

        if (list == null)
        {
            throw new NullPointerException("list MUST NOT be null");
        }

        this.updateTree();

        final Queue<Branch<ELEMENT>> queue = new Queue<>();
        queue.inQueue(this.trunk);
        Branch<ELEMENT> branch;

        while (!queue.empty())
        {
            branch = queue.outQueue();

            if (testFoundListener.isElementSearched(branch.element))
            {
                list.add(branch.element);
            }

            if (branch.left != null)
            {
                queue.inQueue(branch.left);
            }

            if (branch.right != null)
            {
                queue.inQueue(branch.right);
            }
        }
    }

    /**
     * Collect elements of the solver right to left, deep first
     *
     * @param testFoundListener Filter to use to know if one element is inside searched ones
     * @param list              List to fill with founded elements
     */
    public void collectRightToLeftDepth(final TestFoundListener<ELEMENT> testFoundListener, final List<ELEMENT> list)
    {
        if (testFoundListener == null)
        {
            throw new NullPointerException("testFoundListener MUST NOT be null");
        }

        if (list == null)
        {
            throw new NullPointerException("list MUST NOT be null");
        }

        this.updateTree();

        final Stack<Branch<ELEMENT>> stack = new Stack<>();
        stack.push(this.trunk);
        Branch<ELEMENT> branch;

        while (!stack.isEmpty())
        {
            branch = stack.pop();

            if (testFoundListener.isElementSearched(branch.element))
            {
                list.add(branch.element);
            }

            if (branch.left != null)
            {
                stack.push(branch.left);
            }

            if (branch.right != null)
            {
                stack.push(branch.right);
            }
        }
    }

    /**
     * Collect elements of the solver right to left, high first
     *
     * @param testFoundListener Filter to use to know if one element is inside searched ones
     * @param list              List to fill with founded elements
     */
    public void collectRightToLeftHigh(final TestFoundListener<ELEMENT> testFoundListener, final List<ELEMENT> list)
    {
        if (testFoundListener == null)
        {
            throw new NullPointerException("testFoundListener MUST NOT be null");
        }

        if (list == null)
        {
            throw new NullPointerException("list MUST NOT be null");
        }

        this.updateTree();

        final Queue<Branch<ELEMENT>> queue = new Queue<>();
        queue.inQueue(this.trunk);
        Branch<ELEMENT> branch;

        while (!queue.empty())
        {
            branch = queue.outQueue();

            if (testFoundListener.isElementSearched(branch.element))
            {
                list.add(branch.element);
            }

            if (branch.right != null)
            {
                queue.inQueue(branch.right);
            }

            if (branch.left != null)
            {
                queue.inQueue(branch.left);
            }
        }
    }

    /**
     * Obtain the solver trunk
     *
     * @return Tree trunk
     */
    public Branch<ELEMENT> getTrunk()
    {
        this.updateTree();
        return this.trunk;
    }

    /**
     * Indicates if solver is empty
     *
     * @return {@code true} if solver is empty
     */
    public boolean isEmpty()
    {
        this.updateTree();
        return this.trunk.element == null;
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<ELEMENT> iterator()
    {
        this.updateTree();
        return this.content.iterator();
    }

    /**
     * Add element inside the solver.<br>
     * Only different element can be added
     *
     * @param element Element to add
     * @return {@code true} if the element is added. {@code false} if element is considers equals (With the given
     * comparator at
     * constructor) and not added
     */
    public boolean putElement(final ELEMENT element)
    {
        if (element == null)
        {
            throw new NullPointerException("element MUST NOT be null");
        }

        if (this.content.add(element))
        {
            this.isUpdated = false;
            return true;
        }

        return false;
    }

    /**
     * Remove an element considers equals (With the given comparator at constructor) to given one
     *
     * @param element Element to remove
     * @return {@code true} if element removed
     */
    public boolean reomveElement(final ELEMENT element)
    {
        if (element == null)
        {
            return false;
        }

        if (this.content.remove(element) != null)
        {
            this.isUpdated = false;
            return true;
        }

        return false;
    }

    /**
     * Search first element of the solver left to right, deep first that corresponds to defined test
     *
     * @param testFoundListener Filter to use to know if one element is the search one
     * @return Found element or {@code null} if not found
     */
    public ELEMENT searchLeftToRightDepth(final TestFoundListener<ELEMENT> testFoundListener)
    {
        if (testFoundListener == null)
        {
            throw new NullPointerException("testFoundListener MUST NOT be null");
        }

        this.updateTree();

        final Stack<Branch<ELEMENT>> stack = new Stack<>();
        stack.push(this.trunk);
        Branch<ELEMENT> branch;

        while (!stack.isEmpty())
        {
            branch = stack.pop();

            if (testFoundListener.isElementSearched(branch.element))
            {
                return branch.element;
            }

            if (branch.right != null)
            {
                stack.push(branch.right);
            }

            if (branch.left != null)
            {
                stack.push(branch.left);
            }
        }

        return null;
    }

    /**
     * Search first element of the solver left to right, deep high that corresponds to defined test
     *
     * @param testFoundListener Filter to use to know if one element is the search one
     * @return Found element or {@code null} if not found
     */
    public ELEMENT searchLeftToRightHigh(final TestFoundListener<ELEMENT> testFoundListener)
    {
        if (testFoundListener == null)
        {
            throw new NullPointerException("testFoundListener MUST NOT be null");
        }

        this.updateTree();

        final Queue<Branch<ELEMENT>> queue = new Queue<>();
        queue.inQueue(this.trunk);
        Branch<ELEMENT> branch;

        while (!queue.empty())
        {
            branch = queue.outQueue();

            if (testFoundListener.isElementSearched(branch.element))
            {
                return branch.element;
            }

            if (branch.left != null)
            {
                queue.inQueue(branch.left);
            }

            if (branch.right != null)
            {
                queue.inQueue(branch.right);
            }
        }

        return null;
    }

    /**
     * Search first element of the solver right to left, deep first that corresponds to defined test
     *
     * @param testFoundListener Filter to use to know if one element is the search one
     * @return Found element or {@code null} if not found
     */
    public ELEMENT searchRightToLeftDepth(final TestFoundListener<ELEMENT> testFoundListener)
    {
        if (testFoundListener == null)
        {
            throw new NullPointerException("testFoundListener MUST NOT be null");
        }

        this.updateTree();

        final Stack<Branch<ELEMENT>> stack = new Stack<>();
        stack.push(this.trunk);
        Branch<ELEMENT> branch;

        while (!stack.isEmpty())
        {
            branch = stack.pop();

            if (testFoundListener.isElementSearched(branch.element))
            {
                return branch.element;
            }

            if (branch.left != null)
            {
                stack.push(branch.left);
            }

            if (branch.right != null)
            {
                stack.push(branch.right);
            }
        }

        return null;
    }

    /**
     * Search first element of the solver right to left, high first that corresponds to defined test
     *
     * @param testFoundListener Filter to use to know if one element is the search one
     * @return Found element or {@code null} if not found
     */
    public ELEMENT searchRightToLeftHigh(final TestFoundListener<ELEMENT> testFoundListener)
    {
        if (testFoundListener == null)
        {
            throw new NullPointerException("testFoundListener MUST NOT be null");
        }

        this.updateTree();

        final Queue<Branch<ELEMENT>> queue = new Queue<>();
        queue.inQueue(this.trunk);
        Branch<ELEMENT> branch;

        while (!queue.empty())
        {
            branch = queue.outQueue();

            if (testFoundListener.isElementSearched(branch.element))
            {
                return branch.element;
            }

            if (branch.right != null)
            {
                queue.inQueue(branch.right);
            }

            if (branch.left != null)
            {
                queue.inQueue(branch.left);
            }
        }

        return null;
    }

    /**
     * Iterable size
     *
     * @return Iterable size
     */
    @Override
    public int size()
    {
        this.updateTree();
        return this.content.size();
    }

    /**
     * Convert binary solver to generic solver
     *
     * @return Generic solver
     */
    public Tree<ELEMENT> toTree()
    {
        this.updateTree();

        if (this.isEmpty())
        {
            return null;
        }

        final Tree<ELEMENT> tree = new Tree<>(this.trunk.element);
        this.fillTree(this.trunk, tree);
        return tree;
    }

    /**
     * String representation <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return String representation
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        this.updateTree();

        if (this.trunk.element == null)
        {
            return "BinaryTree <EMPTY>";
        }

        final StringBuilder stringBuilder = new StringBuilder("BinaryTree :");
        this.fill(this.trunk, stringBuilder, 0);

        return stringBuilder.toString();
    }


}
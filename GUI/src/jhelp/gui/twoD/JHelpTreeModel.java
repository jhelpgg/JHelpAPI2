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
package jhelp.gui.twoD;

import java.util.ArrayList;
import jhelp.util.gui.JHelpImage;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.ThreadManager;

/**
 * Model for tree
 *
 * @param <INFORMATION> Information type
 * @author JHelp
 */
public abstract class JHelpTreeModel<INFORMATION>
{
    /**
     * Task for signal to listener that tree model changed
     */
    private final ConsumerTask<JHelpTreeModelListener<INFORMATION>> taskFireModelChanged =
            new ConsumerTask<JHelpTreeModelListener<INFORMATION>>()
            {
                /**
                 * Called when task tur come.<br>
                 * Here it signal to listener
                 * that model changed <br>
                 * <br>
                 * <b>Parent documentation:</b><br>
                 * {@inheritDoc}
                 *
                 * @param parameter
                 *           Listener to alert
                 * @see jhelp.util.thread.ConsumerTask#consume(Object)
                 */
                @Override
                public void consume(final JHelpTreeModelListener<INFORMATION> parameter)
                {
                    parameter.treeModelChanged(JHelpTreeModel.this);
                }
            };
    /**
     * Listeners of tree model changes
     */
    private final ArrayList<JHelpTreeModelListener<INFORMATION>> treeModelListeners;

    /**
     * Create a new instance of JHelpTreeModel
     */
    public JHelpTreeModel()
    {
        this.treeModelListeners = new ArrayList<JHelpTreeModelListener<INFORMATION>>();
    }

    /**
     * Call this method to signal to listeners that tree model has changed
     */
    protected final synchronized void fireModelChanged()
    {
        for (final JHelpTreeModelListener<INFORMATION> treeModelListener : this.treeModelListeners)
        {
            ThreadManager.parallel(this.taskFireModelChanged, treeModelListener);
        }
    }

    /**
     * Obtain a child of a parent
     *
     * @param parent Parent
     * @param index  Child index
     * @return Child
     */
    public abstract INFORMATION child(INFORMATION parent, int index);

    /**
     * Change node expand state
     *
     * @param information Information
     * @param expand      New expand state
     */
    public abstract void expand(INFORMATION information, final boolean expand);

    /**
     * Indicates if a node is expanded
     *
     * @param node Tested node
     * @return {@code true} if node is expanded
     */
    public abstract boolean expanded(INFORMATION node);

    /**
     * Force refresh the model
     */
    public void forceRefresh()
    {
        this.fireModelChanged();
    }

    /**
     * Number of children in a parent node
     *
     * @param parent Parent node
     * @return Number of children
     */
    public abstract int numberOfChildren(INFORMATION parent);

    /**
     * Image representation of a node.<br>
     * Can be {@code null} for no image
     *
     * @param information Information
     * @return Image represention or {@code null} if theire are no image representation
     */
    public abstract JHelpImage obtainImageRepresentation(INFORMATION information);

    /**
     * Text representation of a node.<br>
     * Can be {@code null} if no text representation
     *
     * @param information Information
     * @return Text representation or {@code null} if there are no text representation
     */
    public abstract String obtainTextRepresentation(INFORMATION information);

    /**
     * Get information parent
     *
     * @param child Child to get its parent
     * @return Parent or {@code null} if child is root, so haven't parent
     */
    public abstract INFORMATION parent(INFORMATION child);

    /**
     * Register a tree model listener
     *
     * @param treeModelListener Listener to register
     */
    public final synchronized void registerTreeModelListener(
            final JHelpTreeModelListener<INFORMATION> treeModelListener)
    {
        if (treeModelListener == null)
        {
            throw new NullPointerException("treeModelListener mustn't be null");
        }

        this.treeModelListeners.add(treeModelListener);
    }

    /**
     * Tree root
     *
     * @return Tree root
     */
    public abstract INFORMATION root();

    /**
     * Unregister a tree model listener
     *
     * @param treeModelListener Listener to unregister
     */
    public final synchronized void unregisterTreeModelListener(
            final JHelpTreeModelListener<INFORMATION> treeModelListener)
    {
        this.treeModelListeners.remove(treeModelListener);
    }

    /**
     * Imdicates if have to use image representation for a node .<br>
     * If not image is used, that text is used
     *
     * @param information Informatio tested
     * @return {@code true} for use image representation. {@code false} for text representation
     */
    public abstract boolean useImageRepresentation(INFORMATION information);
}
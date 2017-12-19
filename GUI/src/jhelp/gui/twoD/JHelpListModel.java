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

import java.awt.Dimension;
import java.util.ArrayList;
import jhelp.util.gui.JHelpImage;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.ThreadManager;

/**
 * List model
 *
 * @param <INFORMATION> Information type
 * @author JHelp
 */
public abstract class JHelpListModel<INFORMATION>
{
    /**
     * List model change listener
     */
    private final ArrayList<JHelpListModelListener<INFORMATION>> listeners;
    /**
     * Task for signal that model changed
     */
    private final ConsumerTask<JHelpListModelListener<INFORMATION>> taskFireModelChanged =
            new ConsumerTask<JHelpListModelListener<INFORMATION>>()
            {
                /**
                 * Signal that model changed <br>
                 * <br>
                 * <b>Parent documentation:</b><br>
                 * {@inheritDoc}
                 *
                 * @param parameter
                 *           Listener to alert
                 * @see jhelp.util.thread.ConsumerTask#consume(Object)
                 */
                @Override
                public void consume(final JHelpListModelListener<INFORMATION> parameter)
                {
                    parameter.listModelChanged(JHelpListModel.this);
                }
            };

    /**
     * Create a new instance of JHelpListModel
     */
    public JHelpListModel()
    {
        this.listeners = new ArrayList<JHelpListModelListener<INFORMATION>>();
    }

    /**
     * Signal to listeners that model changed
     */
    protected final synchronized void fireModelChanged()
    {
        for (final JHelpListModelListener<INFORMATION> listener : this.listeners)
        {
            ThreadManager.parallel(this.taskFireModelChanged, listener);
        }
    }

    /**
     * List item size.<br>
     * If need to create the item itself before know the size, just return {@code null}.<br>
     * Return {@code null} by default
     *
     * @param information Information to get its size
     * @return List item size, if known without create item itself
     */
    public Dimension cellSize(final INFORMATION information)
    {
        return null;
    }

    /**
     * Obtain a list element
     *
     * @param index Element index
     * @return Element asked
     */
    public abstract INFORMATION element(int index);

    /**
     * Force the model to refresh
     */
    public void forceRefresh()
    {
        this.fireModelChanged();
    }

    /**
     * Number of elements
     *
     * @return Number of elements
     */
    public abstract int numberOfElement();

    /**
     * Image representation of information.<br>
     * Use {@code null} if no image representation
     *
     * @param information Information
     * @return Image representation or {@code null} if no image representation
     */
    public abstract JHelpImage obtainImageRepresentation(INFORMATION information);

    /**
     * Text representation of information.<br>
     * Use {@code null} if no text representation
     *
     * @param information Information
     * @return Text representation or {@code null} if no text representation
     */
    public abstract String obtainTextRepresentation(INFORMATION information);

    /**
     * Register listener of model changed
     *
     * @param listener Listener to register
     */
    public final synchronized void registerJHelpListModelListener(final JHelpListModelListener<INFORMATION> listener)
    {
        if (listener == null)
        {
            throw new NullPointerException("listener mustn't be null");
        }

        this.listeners.add(listener);
    }

    /**
     * Obtain item tooltip.<br>
     * Return {@code null} if no tooltip
     *
     * @param information Item information
     * @return Associated tooltip
     */
    public abstract String toolTip(INFORMATION information);

    /**
     * Unregister to model changed
     *
     * @param listener Listener to unregister
     */
    public final synchronized void unregisterJHelpListModelListener(final JHelpListModelListener<INFORMATION> listener)
    {
        this.listeners.add(listener);
    }

    /**
     * Indicates if image representation is to use for an information
     *
     * @param information Information
     * @return {@code true} if use image for information
     */
    public abstract boolean useImageRepresentation(INFORMATION information);
}
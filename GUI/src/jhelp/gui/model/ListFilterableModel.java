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
package jhelp.gui.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import jhelp.util.list.SortedArray;

/**
 * Model for list of elements that can be filtered (Show or hide depends on filter)
 *
 * @param <ELEMENT> Elements type
 * @author JHelp <br>
 */
public class ListFilterableModel<ELEMENT>
        implements ListModel<ELEMENT>, FilterListener<ELEMENT>
{
    /**
     * Embed element information
     *
     * @param <TYPE> Element type
     * @author JHelp <br>
     */
    static class Element<TYPE>
    {
        /**
         * Embed element
         */
        final TYPE element;
        /**
         * Current index (-1 if hide)
         */
        int index;

        /**
         * Create a new instance of Element
         *
         * @param element Embed element
         */
        Element(final TYPE element)
        {
            this.element = element;
        }
    }

    /**
     * List of all elements
     */
    private final List<Element<ELEMENT>> elements;
    /**
     * Filter to use for show/hide
     */
    private       Filter<ELEMENT>        filter;
    /**
     * Listeners of model changes
     */
    private final List<ListDataListener> listeners;
    /**
     * Current number of visible elements
     */
    private       int                    size;

    /**
     * Create a new instance of ListFilterableModel
     */
    public ListFilterableModel()
    {
        this.elements = new ArrayList<Element<ELEMENT>>();
        this.listeners = new ArrayList<ListDataListener>();
        this.filter = null;
        this.size = -1;
        this.update();
    }

    /**
     * Compare 2 elements
     *
     * @param element1 First element
     * @param element2 Second element
     * @return {@code true} if equals
     */
    private boolean equals(final ELEMENT element1, final ELEMENT element2)
    {
        if (element1 == null)
        {
            return element2 == null;
        }

        return element1.equals(element2);
    }

    /**
     * Update (if need) the number of visible elements and their index
     */
    private void update()
    {
        synchronized (this.elements)
        {
            if (this.size < 0)
            {
                this.size = 0;

                for (final Element<ELEMENT> element : this.elements)
                {
                    if ((this.filter == null) || (this.filter.filtered(element.element)))
                    {
                        element.index = this.size;
                        this.size++;
                    }
                    else
                    {
                        element.index = -1;
                    }
                }
            }
        }
    }

    /**
     * Signal to listeners that all list changed
     */
    protected final void fireContentChanged()
    {
        this.fireContentChanged(0, this.size);
    }

    /**
     * Signal to listeners that a part of list changed
     *
     * @param index1 Start index of change
     * @param index2 End index of change
     */
    protected final void fireContentChanged(final int index1, final int index2)
    {
        final ListDataEvent listDataEvent = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED,
                                                              Math.min(index1, index2), Math.max(index1, index2));

        synchronized (this.listeners)
        {
            for (final ListDataListener listener : this.listeners)
            {
                listener.contentsChanged(listDataEvent);
            }
        }
    }

    /**
     * Signal to listeners that one element is added
     *
     * @param index Element index
     */
    protected final void fireElementAdded(final int index)
    {
        this.fireIntervalAdded(index, index);
    }

    /**
     * Signal to listeners that one element is removed
     *
     * @param index Element index
     */
    protected final void fireElementRemoved(final int index)
    {
        this.fireIntervalRemoved(index, index);
    }

    /**
     * Signal to listeners that several elements are add
     *
     * @param index1 Start index add
     * @param index2 End index add
     */
    protected final void fireIntervalAdded(final int index1, final int index2)
    {
        final ListDataEvent listDataEvent = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED,
                                                              Math.min(index1, index2), Math.max(index1, index2));

        synchronized (this.listeners)
        {
            for (final ListDataListener listener : this.listeners)
            {
                listener.intervalAdded(listDataEvent);
            }
        }
    }

    /**
     * Signal to listeners that several elements are removed
     *
     * @param index1 Start index removed
     * @param index2 End index removed
     */
    protected final void fireIntervalRemoved(final int index1, final int index2)
    {
        final ListDataEvent listDataEvent = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED,
                                                              Math.min(index1, index2), Math.max(index1, index2));

        synchronized (this.listeners)
        {
            for (final ListDataListener listener : this.listeners)
            {
                listener.intervalRemoved(listDataEvent);
            }
        }
    }

    /**
     * Add one element to the model
     *
     * @param element Element to add
     */
    public final void addElement(final ELEMENT element)
    {
        final Element<ELEMENT> elt = new Element<ELEMENT>(element);

        synchronized (this.elements)
        {
            this.elements.add(elt);
        }

        if ((this.filter == null) || (this.filter.filtered(element)))
        {
            elt.index = this.size;
            this.size++;
            this.fireElementAdded(elt.index);
        }
        else
        {
            elt.index = -1;
        }
    }

    /**
     * Add several elements to the model
     *
     * @param elementClass Class of element
     * @param elements     Collection to add
     */
    @SuppressWarnings("unchecked")
    public final void addElements(final Class<ELEMENT> elementClass, final Collection<ELEMENT> elements)
    {
        this.addElements(elements.toArray((ELEMENT[]) Array.newInstance(elementClass, this.size)));
    }

    /**
     * Add several elements to the model
     *
     * @param elements Elements to add
     */
    @SuppressWarnings("unchecked")
    public final void addElements(final ELEMENT... elements)
    {
        int              min = Integer.MAX_VALUE;
        int              max = Integer.MIN_VALUE;
        Element<ELEMENT> elt;

        synchronized (this.elements)
        {
            for (final ELEMENT element : elements)
            {
                elt = new Element<ELEMENT>(element);
                this.elements.add(elt);

                if ((this.filter == null) || (this.filter.filtered(element)))
                {
                    elt.index = this.size;
                    min = Math.min(min, this.size);
                    max = Math.max(max, this.size);
                    this.size++;
                }
                else
                {
                    elt.index = -1;
                }
            }
        }

        if (min <= max)
        {
            this.fireIntervalAdded(min, max);
        }
    }

    /**
     * Add several elements to model
     *
     * @param elements Elements to add
     */
    public final void addElements(final SortedArray<ELEMENT> elements)
    {
        this.addElements(elements.toArray());
    }

    /**
     * Clear the model.<br>
     * It will become empty
     */
    public final void clear()
    {
        synchronized (this.elements)
        {
            this.elements.clear();
        }

        this.size = 0;
        this.fireContentChanged();
    }

    /**
     * Called when filter change, so the list of show may change <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param filter Changed filter
     * @see FilterListener#filterChange(Filter)
     */
    @Override
    public final void filterChange(final Filter<ELEMENT> filter)
    {
        if (filter == this.filter)
        {
            this.size = -1;
            this.update();
            this.fireContentChanged();
        }
    }

    /**
     * Current model size. Number of visibles <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Model size
     * @see ListModel#getSize()
     */
    @Override
    public final int getSize()
    {
        this.update();
        return this.size;
    }

    /**
     * Obtain an element among visible <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param index Element index
     * @return Element or <code>null</code> if no element at desired index
     * @see ListModel#getElementAt(int)
     */
    @Override
    public final ELEMENT getElementAt(final int index)
    {
        this.update();

        synchronized (this.elements)
        {
            for (final Element<ELEMENT> element : this.elements)
            {
                if (element.index == index)
                {
                    return element.element;
                }
            }
        }

        return null;
    }

    /**
     * Register listener to model events <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param listener Listener to register
     * @see ListModel#addListDataListener(ListDataListener)
     */
    @Override
    public final void addListDataListener(final ListDataListener listener)
    {
        if (listener == null)
        {
            return;
        }

        synchronized (this.listeners)
        {
            if (!this.listeners.contains(listener))
            {
                this.listeners.add(listener);
            }
        }
    }

    /**
     * Unregister a lister from model events <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param listener Listener to remove
     * @see ListModel#removeListDataListener(ListDataListener)
     */
    @Override
    public final void removeListDataListener(final ListDataListener listener)
    {
        synchronized (this.listeners)
        {
            this.listeners.remove(listener);
        }
    }

    /**
     * Remove one element
     *
     * @param element Element to remove
     */
    public final void removeElement(final ELEMENT element)
    {
        synchronized (this.elements)
        {
            int index = -1;

            for (int i = this.elements.size() - 1; i >= 0; i--)
            {
                if (this.equals(element, this.elements.get(i).element))
                {
                    index = i;
                    break;
                }
            }

            if (index >= 0)
            {
                final Element<ELEMENT> elt = this.elements.remove(index);

                if (elt.index >= 0)
                {
                    this.size--;
                    this.fireElementRemoved(elt.index);
                }
            }
        }
    }

    /**
     * Change the filter
     *
     * @param filter New filter. Can use <code>null</code> for show all elements
     */
    public final void setFilter(final Filter<ELEMENT> filter)
    {
        if (this.filter == filter)
        {
            return;
        }

        if (this.filter != null)
        {
            this.filter.unregisterFilterListener(this);
        }

        this.filter = filter;

        if (this.filter != null)
        {
            this.filter.registerFilterListener(this);
        }

        this.size = -1;
        this.update();
        this.fireContentChanged();
    }
}
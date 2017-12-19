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

import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import jhelp.util.list.SortedArray;

/**
 * Model of system available font<br>
 * It can be use in {@link JList} and {@link JComboBox} <br>
 * <br>
 * Last modification : 12 mai 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public class FontModel
        implements ComboBoxModel<String>, Comparator<String>
{
    /**
     * Font model
     */
    private static FontModel FONT_MODEL;

    /**
     * Obtain the unique model
     *
     * @return The model
     */
    public static FontModel obtainFontModel()
    {
        if (FontModel.FONT_MODEL != null)
        {
            return FontModel.FONT_MODEL;
        }

        FontModel.FONT_MODEL = new FontModel();

        return FontModel.FONT_MODEL;
    }

    /**
     * Font name list
     */
    private final SortedArray<String>         fontNames;
    /**
     * Listeners of change
     */
    private final ArrayList<ListDataListener> listeners;
    /**
     * Actaul select font
     */
    private       String                      selectFont;

    /**
     * Constructs FontModel
     */
    private FontModel()
    {
        this.listeners = new ArrayList<ListDataListener>();
        this.fontNames = new SortedArray<String>(String.class, this, true);
        this.updateFontList();
        this.selectFont("Arial");
    }

    /**
     * Select font
     *
     * @param font Font familly name
     * @return Font index
     */
    private int selectFont(String font)
    {
        int index = this.fontNames.indexOf(font);

        if (index < 0)
        {
            if (!font.equalsIgnoreCase("Arial"))
            {
                index = this.fontNames.indexOf("Arial");
            }
        }

        if (index < 0)
        {
            index = 0;
            font = this.fontNames.get(0);
        }

        this.selectFont = font;

        return index;
    }

    /**
     * Signal to listeners that the content changed
     *
     * @param index0 First index
     * @param index1 Second index
     */
    protected void fireContentChanged(final int index0, final int index1)
    {
        ListDataEvent listDataEvent = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, index0, index1);
        for (final ListDataListener listDataListener : this.listeners)
        {
            listDataListener.contentsChanged(listDataEvent);
        }

        listDataEvent = null;
    }

    /**
     * Compare two font
     *
     * @param o1 First font family name
     * @param o2 Second font family name
     * @return Compare result
     * @see Comparator#compare(Object, Object)
     */
    @Override
    public int compare(final String o1, final String o2)
    {
        return o1.compareToIgnoreCase(o2);
    }

    /**
     * Number of font
     *
     * @return Number of font
     * @see javax.swing.ListModel#getSize()
     */
    @Override
    public int getSize()
    {
        return this.fontNames.size();
    }

    /**
     * Get a font
     *
     * @param index Index
     * @return Font name
     * @see javax.swing.ListModel#getElementAt(int)
     */
    @Override
    public String getElementAt(final int index)
    {
        return this.fontNames.get(index);
    }

    /**
     * Add listener
     *
     * @param l Listener to add
     * @see javax.swing.ListModel#addListDataListener(ListDataListener)
     */
    @Override
    public void addListDataListener(final ListDataListener l)
    {
        this.listeners.add(l);
    }

    /**
     * Remove a listener
     *
     * @param l Listener to remove
     * @see javax.swing.ListModel#removeListDataListener(ListDataListener)
     */
    @Override
    public void removeListDataListener(final ListDataListener l)
    {
        this.listeners.remove(l);
    }

    /**
     * Update the list of fonts
     */
    public void updateFontList()
    {
        this.fontNames.clear();

        for (final String name : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames())
        {
            this.fontNames.add(name);
        }

        this.fireContentChanged(0, this.fontNames.size() - 1);
    }

    /**
     * Select item
     *
     * @return Select item
     * @see ComboBoxModel#getSelectedItem()
     */
    @Override
    public Object getSelectedItem()
    {
        return this.selectFont;
    }

    /**
     * Change the select item
     *
     * @param anItem New selection
     * @see ComboBoxModel#setSelectedItem(Object)
     */
    @Override
    public void setSelectedItem(final Object anItem)
    {
        final int index = this.selectFont((String) anItem);

        this.fireContentChanged(index, index);
    }
}
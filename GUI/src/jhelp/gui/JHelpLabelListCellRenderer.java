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
package jhelp.gui;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * List cell renderer based on {@link JHelpLabel} .<br>
 * It automatically set focus and selection status
 *
 * @author JHelp
 * @param <ELEMENT>
 *           Element to draw
 */
public abstract class JHelpLabelListCellRenderer<ELEMENT>
        extends JHelpLabel
        implements ListCellRenderer<ELEMENT>
{
    /**
     * Create a new instance of JHelpLabelListCellRenderer
     */
    public JHelpLabelListCellRenderer()
    {
    }

    /**
     * Update the label with an element
     *
     * @param element
     *           Element to draw
     * @param index
     *           Element index
     */
    protected abstract void update(ELEMENT element, int index);

    /**
     * Obtain component for given cell <br>
     * <br>
     * <b>Parent documentation :</b><br>
     * {@inheritDoc}
     *
     * @param list
     *           List where component draw
     * @param value
     *           Value on cell
     * @param index
     *           Cell index
     * @param isSelected
     *           Indicates if cell selected
     * @param cellHasFocus
     *           Indicates if cell have focus
     * @return Component for draw
     * @see ListCellRenderer#getListCellRendererComponent(JList, Object, int, boolean, boolean)
     */
    @Override
    public final Component getListCellRendererComponent(
            final JList<? extends ELEMENT> list, final ELEMENT value, final int index, final boolean isSelected,
            final boolean cellHasFocus)
    {
        this.update(value, index);
        this.setSelected(isSelected);
        this.setFocused(cellHasFocus);
        return this;
    }
}
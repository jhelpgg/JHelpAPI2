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

import java.awt.Font;

import jhelp.gui.model.SuggestionElement;

/**
 * Default renderer of cells in suggestion list
 *
 * @param <INFORMATION> Information carry with the suggestion
 * @author JHelp <br>
 */
public class JHelpSuggestionDefaultListCellRenderer<INFORMATION>
        extends JHelpLabelListCellRenderer<SuggestionElement<INFORMATION>>
{
    /**
     * Default font use
     */
    public static final Font DEFAULT_FONT = new Font("Arial", Font.BOLD, 18);

    /**
     * Create a new instance of JHelpSuggestionDefaultListCellRenderer
     */
    public JHelpSuggestionDefaultListCellRenderer()
    {
        this.setFont(JHelpSuggestionDefaultListCellRenderer.DEFAULT_FONT);
    }

    /**
     * Update the cell if a suggestion element <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param element Suggestion element to renderer
     * @param index   Index in list
     * @see JHelpLabelListCellRenderer#update(Object, int)
     */
    @Override
    protected void update(final SuggestionElement<INFORMATION> element, final int index)
    {
        final StringBuilder text        = new StringBuilder(element.keyWord());
        final INFORMATION   information = element.information();

        if (information != null)
        {
            text.append(" - ");
            text.append(information);
        }

        this.setText(text.toString());
    }
}
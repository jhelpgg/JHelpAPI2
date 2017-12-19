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
package jhelp.gui.samples.d2.list;

import jhelp.gui.twoD.JHelpListModel;
import jhelp.util.gui.JHelpImage;

/**
 * Model for list sample
 *
 * @author JHelp
 */
public class ListModelSample
        extends JHelpListModel<String>
{
    static
    {
        LINES = new String[]
                {
                        "apple", "bannana", "car", "dolmen", "elephant", "fruit", "garlic", "helium", "illegal", "java",
                        "kwinaman", "lemon", "no", "operation", "program",
                        "quit", "real", "string", "type", "upper", "velocity", "waggon", "xylophone", "yell", "zebra"
                };
        LINES_LENGTH = ListModelSample.LINES.length;
    }

    /** Model content */
    private static final String[] LINES;
    /** Number of elelents in model */
    private static final int      LINES_LENGTH;

    /**
     * Create a new instance of ListModelSample
     */
    public ListModelSample()
    {
    }

    /**
     * Obtain element at specific index <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param index
     *           Index
     * @return Element at the index
     * @see jhelp.gui.twoD.JHelpListModel#element(int)
     */
    @Override
    public String element(final int index)
    {
        return ListModelSample.LINES[index];
    }

    /**
     * Number of element <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Number of element
     * @see jhelp.gui.twoD.JHelpListModel#numberOfElement()
     */
    @Override
    public int numberOfElement()
    {
        return ListModelSample.LINES_LENGTH;
    }

    /**
     * Image representation of an element <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param information
     *           Element
     * @return {@code null}
     * @see jhelp.gui.twoD.JHelpListModel#obtainImageRepresentation(java.lang.Object)
     */
    @Override
    public JHelpImage obtainImageRepresentation(final String information)
    {
        return null;
    }

    /**
     * Text representation of an element <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param information
     *           Information
     * @return Text representation
     * @see jhelp.gui.twoD.JHelpListModel#obtainTextRepresentation(java.lang.Object)
     */
    @Override
    public String obtainTextRepresentation(final String information)
    {
        return information;
    }

    @Override
    public String toolTip(final String information)
    {
        return null;
    }

    /**
     * Indicates if use image for an element <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param information
     *           Element
     * @return {@code false}
     * @see jhelp.gui.twoD.JHelpListModel#useImageRepresentation(java.lang.Object)
     */
    @Override
    public boolean useImageRepresentation(final String information)
    {
        return false;
    }
}
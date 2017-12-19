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

import javax.swing.JComponent;
import javax.swing.JPanel;

import jhelp.gui.layout.LimitedSizeLayout;

/**
 * Panel that can carry only one component, but size can be limit to maximum size
 *
 * @author JHelp <br>
 */
public class JHelpLimitSizePanel
        extends JPanel
{

    /**
     * Create a new instance of JHelpLimitSizePanel
     *
     * @param component Component to limit size
     * @param maxWidth  Maximum width (Can use {@link Integer#MAX_VALUE} for no limit
     * @param maxHeight Maximum height (Can use {@link Integer#MAX_VALUE} for no limit
     */
    public JHelpLimitSizePanel(final JComponent component, final int maxWidth, final int maxHeight)
    {
        super(new LimitedSizeLayout(maxWidth, maxHeight));
        this.add(component);
    }
}
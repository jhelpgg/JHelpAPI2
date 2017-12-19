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

import jhelp.gui.layout.FixedSizeLayout;

/**
 * Panel that can carry only one component, but size fix to given size
 *
 * @author JHelp <br>
 */
public class JHelpFixedSizePanel
        extends JPanel
{

    /**
     * Create a new instance of JHelpLimitSizePanel
     *
     * @param component   Component to limit size
     * @param fixedWidth  Fixed width
     * @param fixedHeight Fixed height
     */
    public JHelpFixedSizePanel(final JComponent component, final int fixedWidth, final int fixedHeight)
    {
        super(new FixedSizeLayout(fixedWidth, fixedHeight));
        this.add(component);
    }
}
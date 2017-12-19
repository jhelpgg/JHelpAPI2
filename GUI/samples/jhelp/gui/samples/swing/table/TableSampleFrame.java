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

/*
 * License :
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may cause.
 * You can use, modify, the code as your need for any usage.
 * But you can't do any action that avoid me or other person use, modify this code.
 * The code is free for usage and modification, you can't change that fact.
 * JHelp
 */

/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any
 * damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.gui.samples.swing.table;

import java.awt.BorderLayout;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JComponent;
import javax.swing.JEditorPane;

import jhelp.gui.JHelpFrame;
import jhelp.gui.JHelpTable;

/**
 * Created by jhelp on 03/04/17.
 */
public class TableSampleFrame extends JHelpFrame implements JHelpTable.CellComponentCreator
{
    private static final AtomicInteger EDITOR_NEXT_ID = new AtomicInteger(0);
    private JHelpTable table;

    public TableSampleFrame()
    {
        super("Table sample", false, true);
    }

    /**
     * Add listeners to components
     */
    @Override
    protected void addListeners()
    {
    }

    /**
     * Create components
     */
    @Override
    protected void createComponents()
    {
        this.table = new JHelpTable(3, 5, this);
        this.table.setShowColumnsRowsManipulation(true);
    }

    /**
     * Layout components inside the frame
     */
    @Override
    protected void layoutComponents()
    {
        this.setLayout(new BorderLayout());
        this.add(this.table, BorderLayout.CENTER);
    }

    @Override
    public JComponent createComponentForCell(int x, int y)
    {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setText(TableSampleFrame.EDITOR_NEXT_ID.getAndIncrement() + " : (" + x + ", " + y + ")");
        return editorPane;
    }
}

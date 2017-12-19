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
package jhelp.gui.lineNumber;

import javax.swing.text.StyledEditorKit;
import javax.swing.text.ViewFactory;

/**
 * Editor kit for line number and additional text
 *
 * @author JHelp <br>
 */
public class LineNumberEditorKit
        extends StyledEditorKit
{
    /**
     * Line number factory
     */
    private final LineNumberViewFactory lineNumberViewFactory;

    /**
     * Create a new instance of LineNumberEditorKit
     */
    public LineNumberEditorKit()
    {
        this.lineNumberViewFactory = new LineNumberViewFactory();
    }

    /**
     * Get factory to use <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Factory to use
     * @see StyledEditorKit#getViewFactory()
     */
    @Override
    public ViewFactory getViewFactory()
    {
        return this.lineNumberViewFactory;
    }
}
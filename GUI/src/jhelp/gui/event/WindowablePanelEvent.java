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

package jhelp.gui.event;

import java.awt.AWTEvent;
import jhelp.gui.WindowablePanel;

/**
 * Events can append to a windowable panel
 */
public class WindowablePanelEvent
        extends AWTEvent
{
    /**
     * Attach event
     */
    public static final int EVENT_WINDOWABLE_PANEL_ATTACH = AWTEvent.RESERVED_ID_MAX + 6;
    /**
     * Close event
     */
    public static final int EVENT_WINDOWABLE_PANEL_CLOSE  = AWTEvent.RESERVED_ID_MAX + 1;
    /**
     * Detach event
     */
    public static final int EVENT_WINDOWABLE_PANEL_DETACH = AWTEvent.RESERVED_ID_MAX + 5;
    /**
     * Hide event
     */
    public static final int EVENT_WINDOWABLE_PANEL_HIDE   = AWTEvent.RESERVED_ID_MAX + 3;
    /**
     * Open event
     */
    public static final int EVENT_WINDOWABLE_PANEL_OPEN   = AWTEvent.RESERVED_ID_MAX + 2;
    /**
     * Show event
     */
    public static final int EVENT_WINDOWABLE_PANEL_SHOW   = AWTEvent.RESERVED_ID_MAX + 4;

    /**
     * Constructs WindowablePanelEvent
     *
     * @param source Windowable panel where append the event
     * @param id     Event ID
     */
    public WindowablePanelEvent(final WindowablePanel source, final int id)
    {
        super(source, id);
    }

    /**
     * The windowable panel where event append
     *
     * @return The windowable panel where event append
     */
    public WindowablePanel windowablePanelSource()
    {
        return (WindowablePanel) this.source;
    }
}
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

import java.util.EventListener;

/**
 * Listener of windowable panel events
 */
public interface WindowablePanelListener
        extends EventListener
{
    /**
     * Call when the windowable panel is attach
     *
     * @param windowablePanelEvent Event description
     */
    void windowablePanelAttach(WindowablePanelEvent windowablePanelEvent);

    /**
     * Call when the windowable panel is close
     *
     * @param windowablePanelEvent Event description
     */
    void windowablePanelClose(WindowablePanelEvent windowablePanelEvent);

    /**
     * Call when the windowable panel is detach
     *
     * @param windowablePanelEvent Event description
     */
    void windowablePanelDetach(WindowablePanelEvent windowablePanelEvent);

    /**
     * Call when the windowable panel is hide
     *
     * @param windowablePanelEvent Event description
     */
    void windowablePanelHide(WindowablePanelEvent windowablePanelEvent);

    /**
     * Call when the windowable panel is open
     *
     * @param windowablePanelEvent Event description
     */
    void windowablePanelOpen(WindowablePanelEvent windowablePanelEvent);

    /**
     * Call when the windowable panel is show
     *
     * @param windowablePanelEvent Event description
     */
    void windowablePanelShow(WindowablePanelEvent windowablePanelEvent);
}
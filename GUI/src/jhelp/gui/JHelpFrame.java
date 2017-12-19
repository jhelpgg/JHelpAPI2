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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import jhelp.util.gui.UtilGUI;

/**
 * Generic frame, extend it avoid to do some usual common tasks and suggest strongly to code good
 *
 * @author JHelp
 */
public abstract class JHelpFrame
        extends JFrame
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 9154375270442341171L;
    /**
     * Indicates if have to dispose frame on close
     */
    private boolean disposeOnClose;
    /**
     * Indicates if closing the frame, will also stop all process and the current application
     */
    private boolean exitAllOnClose;

    /**
     * Create a new instance of JHelpFrame Center of the screen and fit size to inside components
     */
    public JHelpFrame()
    {
        this("JHelp Frame", false);
    }

    /**
     * Create a new instance of JHelpFrame
     *
     * @param full Indicates if frame have to take all screen ({@code true}) or center of the screen and fit size to inside
     *             components
     */
    public JHelpFrame(final boolean full)
    {
        this("JHelp Frame", full);
    }

    /**
     * Create a new instance of JHelpFrame center of the screen and fit size to contains components
     *
     * @param title Frame title
     */
    public JHelpFrame(final String title)
    {
        this(title, false);
    }

    /**
     * Create a new instance of JHelpFrame
     *
     * @param title Frame title
     * @param full  Indicates if frame have to take all screen ({@code true}) or center of the screen and fit size to inside
     *              components
     */
    public JHelpFrame(final String title, final boolean full)
    {
        this(title, full, true);
    }

    /**
     * Create a new instance of JHelpFrame
     *
     * @param title     Frame title
     * @param full      Indicates if frame full screen
     * @param resizable Indicate if frame is resizable
     */
    public JHelpFrame(final String title, final boolean full, final boolean resizable)
    {
        super(title);

        this.setResizable(resizable);
        this.setUndecorated(!resizable);
        this.setMinimumSize(new Dimension(512, 512));

        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        this.createComponents();
        this.layoutComponents();
        this.addListeners();

        final Rectangle bounds = UtilGUI.getScreenBounds(0);

        if (full)
        {
            this.setLocation(bounds.x, bounds.y);
            this.setSize(bounds.width, bounds.height);
        }
        else
        {
            this.pack();

            final Dimension dimension = this.getSize();

            dimension.width = Math.min(dimension.width, bounds.width);
            dimension.height = Math.min(dimension.height, bounds.height);

            this.setLocation(((bounds.width - dimension.width) >> 1) + bounds.x,
                             ((bounds.height - dimension.height) >> 1) + bounds.y);
            this.setSize(dimension);
        }

        this.exitAllOnClose = true;
        this.disposeOnClose = true;
    }

    /**
     * Add listeners to components
     */
    protected abstract void addListeners();

    /**
     * Indicates if the frame can be close.<br>
     * By default it returns always {@code true}, if you need ask user about, by example, some unsaved change before quit,
     * override this method and return only {@code true} when user is allowed to close the frame.<br>
     * It is called each time the user try to close the frame
     *
     * @return {@code true} if frame can close
     */
    protected boolean canCloseNow()
    {
        return true;
    }

    /**
     * Create components
     */
    protected abstract void createComponents();

    /**
     * Layout components inside the frame
     */
    protected abstract void layoutComponents();

    /**
     * Call at each window event <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param e Event description
     * @see JFrame#processWindowEvent(WindowEvent)
     */
    @Override
    protected final void processWindowEvent(final WindowEvent e)
    {
        switch (e.getID())
        {
            case WindowEvent.WINDOW_CLOSED:
            case WindowEvent.WINDOW_CLOSING:
                this.closeFrame();
                break;
            default:
                super.processWindowEvent(e);
                break;
        }
    }

    /**
     * Close the current frame
     */
    public final void closeFrame()
    {
        if (!this.canCloseNow())
        {
            return;
        }

        this.setVisible(false);

        if ((this.disposeOnClose) || (this.exitAllOnClose))
        {
            this.dispose();
        }

        if (this.exitAllOnClose)
        {
            System.exit(0);
        }
    }

    /**
     * Indicates if frame will be dispose on close.<br>
     * A disposed frame can't be showed again
     *
     * @return {@code true} if frame is dispose on close
     */
    public boolean isDisposeOnClose()
    {
        return this.disposeOnClose;
    }

    /**
     * Change dispose on close status.<br>
     * A disposed frame can't be showed again
     *
     * @param disposeOnClose New dispose status
     */
    public void setDisposeOnClose(final boolean disposeOnClose)
    {
        this.disposeOnClose = disposeOnClose;
    }

    /**
     * Indicates if we have to exit all process and application when closing the frame
     *
     * @return {@code true} if we have to exit all process and application when closing the frame
     */
    public final boolean isExitAllOnClose()
    {
        return this.exitAllOnClose;
    }

    /**
     * Change the state of exit of all.<br>
     * Indicates if we have to exit all process and application when closing the frame
     *
     * @param exitAllOnClose New exit all status
     */
    public final void setExitAllOnClose(final boolean exitAllOnClose)
    {
        this.exitAllOnClose = exitAllOnClose;
    }
}
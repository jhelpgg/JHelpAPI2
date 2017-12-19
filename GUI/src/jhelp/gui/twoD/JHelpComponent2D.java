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
package jhelp.gui.twoD;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import jhelp.gui.JHelpMouseListener;
import jhelp.util.gui.Bounds;
import jhelp.util.gui.JHelpImage;
import jhelp.util.list.Pair;

/**
 * Generic component 2D.<br>
 * All components 2D have to extends this class
 *
 * @author JHelp
 */
public abstract class JHelpComponent2D
{
    /**
     * Indicates if a component can be add inside a container to avoid cyclic inclusion
     *
     * @param parent      Parent where we want put the component
     * @param component2d Component to add
     * @return {@code true} if component can be add
     */
    static boolean validHerarchy(final JHelpContainer2D parent, final JHelpComponent2D component2d)
    {
        if (parent.equals(component2d))
        {
            return false;
        }

        if (!(component2d instanceof JHelpContainer2D))
        {
            return true;
        }

        final JHelpContainer2D container2d = (JHelpContainer2D) component2d;

        for (final JHelpComponent2D child : container2d.children())
        {
            if (!JHelpComponent2D.validHerarchy(parent, child))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Developer additional information
     */
    private       Object             aditionalInformation;
    /**
     * Last computed bounds
     */
    private final Rectangle          bounds;
    /**
     * Indicates if the component i s focusable
     */
    private       boolean            focusable;
    /**
     * Indicates if the component have the focus
     */
    private       boolean            haveFocus;
    /**
     * Comonent developer ID
     */
    private       int                id;
    /**
     * Current key listener
     */
    private       KeyListener        keyListener;
    /**
     * Associated mouse listener
     */
    private       JHelpMouseListener mouseListener;
    /**
     * Actual parent
     */
    private       JHelpContainer2D   parent;
    /**
     * Computed preferred size
     */
    private       Dimension          preferred;
    /**
     * Bounds in screen
     */
    private       Bounds             screenBounds;
    /**
     * Tooltip to print if mouse go over the component
     */
    private       String             toolTip;
    /**
     * Indicates if component size still valid
     */
    private       boolean            valid;
    /**
     * Indicates if component is visible
     */
    private       boolean            visible;
    /**
     * Owner window
     */
    private       JHelpWindow2D      window2d;
    /**
     * X absolute position
     */
    private       int                xAbsolute;
    /**
     * Y absolute position
     */
    private       int                yAbsolute;

    /**
     * Create a new instance of JHelpComponent2D
     */
    public JHelpComponent2D()
    {
        this.bounds = new Rectangle();
        this.visible = true;
        this.focusable = false;
        this.haveFocus = false;
        this.valid = false;
        this.screenBounds = Bounds.FULL;
    }

    /**
     * Change components bounds
     *
     * @param x      New X
     * @param y      New Y
     * @param width  New width
     * @param height New height
     */
    final void bounds(final int x, final int y, final int width, final int height)
    {
        this.bounds.x = x;
        this.bounds.y = y;
        this.bounds.width = width;
        this.bounds.height = height;

        this.onBoundsChanged(this.bounds());
    }

    /**
     * Invalidate only the component, not its ancestors
     */
    final void fastInvalidate()
    {
        this.valid = false;
    }

    /**
     * Change focus status
     *
     * @param hasFocus New focus status
     */
    void haveFocus(final boolean hasFocus)
    {
        this.haveFocus = hasFocus;
    }

    /**
     * Givewindow owner to the component
     *
     * @param window2d Window owner
     */
    final void owner(final JHelpWindow2D window2d)
    {
        this.window2d = window2d;
    }

    /**
     * Paint the component it self inside a clip
     *
     * @param x          X location on parent
     * @param y          Y location on parent
     * @param parent     Parent where draw
     * @param clipX      Clip up-left corner X
     * @param clipY      Clip up-left corner Y
     * @param clipWidth  Clip width
     * @param clipHeight Clip height
     */
    final void paintInternal(
            final int x, final int y, final JHelpImage parent, final int clipX, final int clipY, final int clipWidth,
            final int clipHeight)
    {
        if (!this.visible)
        {
            return;
        }

        this.xAbsolute = x;
        this.yAbsolute = y;

        parent.pushClipIntersect(clipX, clipY, clipWidth, clipHeight);
        final Rectangle rectangle = this.bounds();
        this.screenBounds = new Bounds(x, x + rectangle.width, y, y + rectangle.height);
        this.screenBounds = this.screenBounds.intersect(parent.getClip());
        this.paint(x, y, parent);
        parent.popClip();
    }

    /**
     * Paint the component it self
     *
     * @param x      X location on parent
     * @param y      Y location on parent
     * @param parent Parent where draw
     */
    final void paintInternal(final int x, final int y, final JHelpImage parent)
    {
        this.paintInternal(x, y, parent, x, y, this.bounds.width, this.bounds.height);
    }

    /**
     * Define the parent.Can only be called once
     *
     * @param parent The parent
     */
    final void parent(final JHelpContainer2D parent)
    {
        if (this.parent != null)
        {
            throw new IllegalStateException(
                    "The component " + this + " have already as parent " + this.parent + " so can't give it " + parent +
                    " as parent");
        }

        if (!JHelpComponent2D.validHerarchy(parent, this))
        {
            throw new IllegalStateException(
                    "The component " + this + " or one of its children contains the container " + parent +
                    " where you try to add it");
        }

        this.parent = parent;
    }

    /**
     * Detach the component from its parent
     */
    final void removeParent()
    {
        this.parent = null;
    }

    /**
     * Compute the component preferred
     *
     * @param parentWidth  Parent width, <0 if not already known
     * @param parentHeight Parent height, <0 if not already known
     * @return Preferred size
     */
    protected abstract Dimension computePreferredSize(int parentWidth, int parentHeight);

    /**
     * Invalidate the component and all of its ancestors
     */
    protected final void invalidate()
    {
        JHelpComponent2D component = this;

        while (component != null)
        {
            component.valid = false;

            component = component.parent;
        }
    }

    /**
     * Test if mouse is over the component.<br>
     * If over it returns the component itself and associated listener else {@code null} is return
     *
     * @param x Mouse X
     * @param y Mouse Y
     * @return This component and its mouse listener OR {@code null}
     */
    protected Pair<JHelpComponent2D, JHelpMouseListener> mouseOver(final int x, final int y)
    {
        if ((!this.visible) //
            || ((this.mouseListener == null) && (!this.focusable) && (!(this instanceof JHelpContainer2D)))//
            || (!this.screenBounds.inside(x, y)))
        {
            return null;
        }

        if ((this.mouseListener == null) && (!this.focusable) && ((this instanceof JHelpContainer2D)))
        {
            Pair<JHelpComponent2D, JHelpMouseListener> pair;

            for (final JHelpComponent2D child : ((JHelpContainer2D) this).children())
            {
                pair = child.mouseOver(x, y);

                if (pair != null)
                {
                    return pair;
                }
            }

            return null;
        }

        return new Pair<JHelpComponent2D, JHelpMouseListener>(this, this.mouseListener);
    }

    /**
     * Called when component bounds changed.<br>
     * Do nothing by default
     *
     * @param bounds New bounds
     */
    protected void onBoundsChanged(final Rectangle bounds)
    {
    }

    /**
     * Draw the component itself
     *
     * @param x      X location on parent
     * @param y      Y location on parent
     * @param parent Parent where draw
     */
    protected abstract void paint(int x, int y, JHelpImage parent);

    /**
     * Obtain component preferered size
     *
     * @param parrentWidth Parent width, <0 if not already known
     * @param parentHeight Parent height, <0 if not already known
     * @return Preferred size
     */
    protected final Dimension preferredSize(final int parrentWidth, final int parentHeight)
    {
        if ((this.preferred != null) && (this.valid))
        {
            return this.preferred;
        }

        this.preferred = this.computePreferredSize(parrentWidth, parentHeight);
        this.valid = true;

        return this.preferred;
    }

    /**
     * Bounds of component in screen
     *
     * @return Bounds of component in screen
     */
    protected final Bounds screenBounds()
    {
        return this.screenBounds;
    }

    /**
     * Translate the components bounds just after the draw, it used if the component is draw inside a temporary image that is
     * really draw
     *
     * @param x Translation X
     * @param y Translation Y
     */
    final protected void translateAfterDraw(final int x, final int y)
    {
        if (this instanceof JHelpContainer2D)
        {
            for (final JHelpComponent2D child : ((JHelpContainer2D) this).children())
            {
                child.translateAfterDraw(x, y);
            }
        }

        this.screenBounds = new Bounds(this.screenBounds.getxMin() + x, this.screenBounds.getxMax() + x,
                                       this.screenBounds.getyMin() + y,
                                       this.screenBounds.getyMax() + y);
    }

    /**
     * Validate the component
     */
    protected final void validate()
    {
        this.valid = true;
    }

    /**
     * Called just before component removed, do nothing by default
     */
    protected void willRemove()
    {
    }

    /**
     * Absolute component position
     *
     * @return Absolute component position
     */
    public Point absolutePosition()
    {
        final Point      point       = new Point();
        JHelpComponent2D component2d = this;
        Rectangle        rectangle;

        while (component2d != null)
        {
            rectangle = component2d.bounds();
            point.translate(rectangle.x, rectangle.y);
            component2d = component2d.parent();
        }

        return point;
    }

    /**
     * X absolute position
     *
     * @return X absolute position
     */
    public final int absoluteX()
    {
        return this.xAbsolute;
    }

    /**
     * Y absolute position
     *
     * @return Y absolute position
     */
    public final int absoluteY()
    {
        return this.yAbsolute;
    }

    /**
     * Developer additional information
     *
     * @return Developer additional information
     */
    public final Object aditionalInformation()
    {
        return this.aditionalInformation;
    }

    /**
     * Change developer additional information
     *
     * @param aditionalInformation New developer additional information
     */
    public final void aditionalInformation(final Object aditionalInformation)
    {
        this.aditionalInformation = aditionalInformation;
    }

    /**
     * Last computed bounds
     *
     * @return Last computed bounds
     */
    public final Rectangle bounds()
    {
        return new Rectangle(this.bounds);
    }

    /**
     * Indicates if component is focusable
     *
     * @return {@code true} if component is focusable
     */
    public boolean focusable()
    {
        return this.focusable;
    }

    /**
     * Indicates if component have the focus
     *
     * @return {@code true} if component have the focus
     */
    public boolean haveFocus()
    {
        return this.haveFocus;
    }

    /**
     * Comonent developer ID
     *
     * @return Comonent developer ID
     */
    public final int id()
    {
        return this.id;
    }

    /**
     * Change comonent developer ID
     *
     * @param id New comonent developer ID
     */
    public final void id(final int id)
    {
        this.id = id;
    }

    /**
     * Associated key listener
     *
     * @return Associated key listener
     */
    public KeyListener keyListener()
    {
        return this.keyListener;
    }

    /**
     * Define the key listener.<br>
     * Use {@code null} for no keylistener
     *
     * @param keyListener New key listaner
     */
    public void keyListener(final KeyListener keyListener)
    {
        this.keyListener = keyListener;

        this.focusable = keyListener != null;
    }

    /**
     * Associated mouse listener
     *
     * @return Associated mouse listener
     */
    public final JHelpMouseListener mouseListener()
    {
        return this.mouseListener;
    }

    /**
     * Define, change, or remove (On using {@code null}) the mouse listener
     *
     * @param mouseListener New mouse listener OR {@code null} to remove mouse listener
     */
    public void mouseListener(final JHelpMouseListener mouseListener)
    {
        this.mouseListener = mouseListener;
    }

    /**
     * Obtain the window owner
     *
     * @return Windo owner or {@code null} if component not attcach directly or indirectly to a window
     */
    public JHelpWindow2D obtainOwner()
    {
        if (this.window2d != null)
        {
            return this.window2d;
        }

        if (this.parent == null)
        {
            return null;
        }

        return this.parent.obtainOwner();
    }

    /**
     * Container parent
     *
     * @return Container parent
     */
    public final JHelpContainer2D parent()
    {
        return this.parent;
    }

    /**
     * Show a dialog for get some text from user<br>
     * If the component is not attach directly or indirectly in a frame or a diloag nothing append
     *
     * @param optionPaneMessageType Message type
     * @param message               Message to show to the user, to explain him what type of text he have to type
     * @param hasCancel             Indicates if theire a cancel button in the option pane
     * @param hasNo                 Indicates if theire a no button in the option pane
     * @param optionPaneListener    Listner to call back when user click yes/ok, cancel or no
     * @param actionID              Action ID, its an integer give back to the listener when user answers
     * @param developerInformation  Parameter give back to the listener when user answers
     */
    public void showOptionPaneInput(
            final OptionPaneMessageType optionPaneMessageType, final String message, final boolean hasCancel,
            final boolean hasNo,
            final JHelpOptionPaneListener optionPaneListener, final int actionID, final Object developerInformation)
    {
        final JHelpFrame2D frame = UtilTwoD.frameOwner(this);
        if (frame == null)
        {
            return;
        }

        frame.showOptionPaneInput(optionPaneMessageType, message, hasCancel, hasNo, optionPaneListener, actionID,
                                  developerInformation);
    }

    /**
     * Show a dialog for get some text from user<br>
     * If the component is not attach directly or indirectly in a frame or a diloag nothing append
     *
     * @param optionPaneMessageType Message type
     * @param message               Message to show to the user, to explain him what type of text he have to type
     * @param editText              Start message
     * @param hasCancel             Indicates if theire a cancel button in the option pane
     * @param hasNo                 Indicates if theire a no button in the option pane
     * @param optionPaneListener    Listner to call back when user click yes/ok, cancel or no
     * @param actionID              Action ID, its an integer give back to the listener when user answers
     * @param developerInformation  Parameter give back to the listener when user answers
     */
    public void showOptionPaneInput(
            final OptionPaneMessageType optionPaneMessageType, final String message, final String editText,
            final boolean hasCancel,
            final boolean hasNo, final JHelpOptionPaneListener optionPaneListener, final int actionID,
            final Object developerInformation)
    {
        final JHelpFrame2D frame = UtilTwoD.frameOwner(this);
        if (frame == null)
        {
            return;
        }

        frame.showOptionPaneInput(optionPaneMessageType, message, editText, hasCancel, hasNo, optionPaneListener,
                                  actionID, developerInformation);
    }

    /**
     * Show a dialog for get some text from user<br>
     * If the component is not attach directly or indirectly in a frame or a diloag nothing append
     *
     * @param optionPaneMessageType Message type
     * @param title                 Dialog title
     * @param message               Message to show to the user, to explain him what type of text he have to type
     * @param editText              Start message
     * @param hasCancel             Indicates if theire a cancel button in the option pane
     * @param hasNo                 Indicates if theire a no button in the option pane
     * @param optionPaneListener    Listner to call back when user click yes/ok, cancel or no
     * @param actionID              Action ID, its an integer give back to the listener when user answers
     * @param developerInformation  Parameter give back to the listener when user answers
     */
    public void showOptionPaneInput(
            final OptionPaneMessageType optionPaneMessageType, final String title, final String message,
            final String editText,
            final boolean hasCancel, final boolean hasNo, final JHelpOptionPaneListener optionPaneListener,
            final int actionID, final Object developerInformation)
    {
        final JHelpFrame2D frame = UtilTwoD.frameOwner(this);
        if (frame == null)
        {
            return;
        }

        frame.showOptionPaneInput(optionPaneMessageType, title, message, editText, hasCancel, hasNo, optionPaneListener,
                                  actionID, developerInformation);
    }

    /**
     * Show a dialog for get some text from user<br>
     * If the component is not attach directly or indirectly in a frame or a diloag nothing append
     *
     * @param message              Message to show to the user, to explain him what type of text he have to type
     * @param hasCancel            Indicates if theire a cancel button in the option pane
     * @param hasNo                Indicates if theire a no button in the option pane
     * @param optionPaneListener   Listner to call back when user click yes/ok, cancel or no
     * @param actionID             Action ID, its an integer give back to the listener when user answers
     * @param developerInformation Parameter give back to the listener when user answers
     */
    public void showOptionPaneInput(
            final String message, final boolean hasCancel, final boolean hasNo,
            final JHelpOptionPaneListener optionPaneListener,
            final int actionID, final Object developerInformation)
    {
        final JHelpFrame2D frame = UtilTwoD.frameOwner(this);
        if (frame == null)
        {
            return;
        }

        frame.showOptionPaneInput(message, hasCancel, hasNo, optionPaneListener, actionID, developerInformation);
    }

    /**
     * Show a dialog for get some text from user<br>
     * If the component is not attach directly or indirectly in a frame or a diloag nothing append
     *
     * @param message              Message to show to the user, to explain him what type of text he have to type
     * @param editText             Start edit text
     * @param hasCancel            Indicates if theire a cancel button in the option pane
     * @param hasNo                Indicates if theire a no button in the option pane
     * @param optionPaneListener   Listner to call back when user click yes/ok, cancel or no
     * @param actionID             Action ID, its an integer give back to the listener when user answers
     * @param developerInformation Parameter give back to the listener when user answers
     */
    public void showOptionPaneInput(
            final String message, final String editText, final boolean hasCancel, final boolean hasNo,
            final JHelpOptionPaneListener optionPaneListener, final int actionID, final Object developerInformation)
    {
        final JHelpFrame2D frame = UtilTwoD.frameOwner(this);
        if (frame == null)
        {
            return;
        }

        frame.showOptionPaneInput(message, editText, hasCancel, hasNo, optionPaneListener, actionID,
                                  developerInformation);
    }

    /**
     * Show a dialog for get some text from user<br>
     * If the component is not attach directly or indirectly in a frame or a diloag nothing append
     *
     * @param title                Dialog title
     * @param message              Message to show to the user, to explain him what type of text he have to type
     * @param editText             Start edit text
     * @param hasCancel            Indicates if theire a cancel button in the option pane
     * @param hasNo                Indicates if theire a no button in the option pane
     * @param optionPaneListener   Listner to call back when user click yes/ok, cancel or no
     * @param actionID             Action ID, its an integer give back to the listener when user answers
     * @param developerInformation Parameter give back to the listener when user answers
     */
    public void showOptionPaneInput(
            final String title, final String message, final String editText, final boolean hasCancel,
            final boolean hasNo,
            final JHelpOptionPaneListener optionPaneListener, final int actionID, final Object developerInformation)
    {
        final JHelpFrame2D frame = UtilTwoD.frameOwner(this);
        if (frame == null)
        {
            return;
        }

        frame.showOptionPaneInput(title, message, editText, hasCancel, hasNo, optionPaneListener, actionID,
                                  developerInformation);
    }

    /**
     * Show a dialog for show a message to the user<br>
     * If the component is not attach directly or indirectly in a frame or a diloag nothing append
     *
     * @param optionPaneMessageType Message type
     * @param message               Message to show
     */
    public void showOptionPaneMessage(final OptionPaneMessageType optionPaneMessageType, final String message)
    {
        final JHelpFrame2D frame = UtilTwoD.frameOwner(this);
        if (frame == null)
        {
            return;
        }

        frame.showOptionPaneMessage(optionPaneMessageType, message);
    }

    /**
     * Show a dialog for show a message to the user<br>
     * If the component is not attach directly or indirectly in a frame or a diloag nothing append
     *
     * @param optionPaneMessageType Message type
     * @param message               Message to show
     * @param optionPaneListener    Listener to call back when ok button is pressed
     * @param actionID              Action id give back to the listener
     * @param developerInformation  Object give back to the listener
     */
    public void showOptionPaneMessage(
            final OptionPaneMessageType optionPaneMessageType, final String message,
            final JHelpOptionPaneListener optionPaneListener,
            final int actionID, final Object developerInformation)
    {
        final JHelpFrame2D frame = UtilTwoD.frameOwner(this);
        if (frame == null)
        {
            return;
        }

        frame.showOptionPaneMessage(optionPaneMessageType, message, optionPaneListener, actionID, developerInformation);
    }

    /**
     * Show a dialog for show a message to the user<br>
     * If the component is not attach directly or indirectly in a frame or a diloag nothing append
     *
     * @param optionPaneMessageType Message type
     * @param title                 Dialog title
     * @param message               Message to show
     */
    public void showOptionPaneMessage(
            final OptionPaneMessageType optionPaneMessageType, final String title, final String message)
    {
        final JHelpFrame2D frame = UtilTwoD.frameOwner(this);
        if (frame == null)
        {
            return;
        }

        frame.showOptionPaneMessage(optionPaneMessageType, title, message);
    }

    /**
     * Show a dialog for show a message to the user<br>
     * If the component is not attach directly or indirectly in a frame or a diloag nothing append
     *
     * @param optionPaneMessageType Message type
     * @param title                 Dialog title
     * @param message               Message to show
     * @param optionPaneListener    Listener to call back when ok button is pressed
     * @param actionID              Action id give back to the listener
     * @param developerInformation  Object give back to the listener
     */
    public void showOptionPaneMessage(
            final OptionPaneMessageType optionPaneMessageType, final String title, final String message,
            final JHelpOptionPaneListener optionPaneListener, final int actionID, final Object developerInformation)
    {
        final JHelpFrame2D frame = UtilTwoD.frameOwner(this);
        if (frame == null)
        {
            return;
        }

        frame.showOptionPaneMessage(optionPaneMessageType, title, message, optionPaneListener, actionID,
                                    developerInformation);
    }

    /**
     * Show a dialog for show a message to the user<br>
     * If the component is not attach directly or indirectly in a frame or a diloag nothing append
     *
     * @param message Message to show
     */
    public void showOptionPaneMessage(final String message)
    {
        final JHelpFrame2D frame = UtilTwoD.frameOwner(this);
        if (frame == null)
        {
            return;
        }

        frame.showOptionPaneMessage(message);
    }

    /**
     * Show a dialog for show a message to the user<br>
     * If the component is not attach directly or indirectly in a frame or a diloag nothing append
     *
     * @param message              Message to show
     * @param optionPaneListener   Listener to call back when ok button is pressed
     * @param actionID             Action id give back to the listener
     * @param developerInformation Object give back to the listener
     */
    public void showOptionPaneMessage(
            final String message, final JHelpOptionPaneListener optionPaneListener, final int actionID,
            final Object developerInformation)
    {
        final JHelpFrame2D frame = UtilTwoD.frameOwner(this);
        if (frame == null)
        {
            return;
        }

        frame.showOptionPaneMessage(message, optionPaneListener, actionID, developerInformation);
    }

    /**
     * Show a dialog for ask a question to the user<br>
     * If the component is not attach directly or indirectly in a frame or a diloag nothing append
     *
     * @param optionPaneMessageType Message type
     * @param message               Question to the user
     * @param hasCancel             Indicates if theire are a cancel button
     * @param hasNo                 Indicate if theire are a no button
     * @param optionPaneListener    Listener to call back when a button is pressed
     * @param actionID              Action id give back to the listener
     * @param developerInformation  Object give back to the listener
     */
    public void showOptionPaneQuestion(
            final OptionPaneMessageType optionPaneMessageType, final String message, final boolean hasCancel,
            final boolean hasNo,
            final JHelpOptionPaneListener optionPaneListener, final int actionID, final Object developerInformation)
    {
        final JHelpFrame2D frame = UtilTwoD.frameOwner(this);
        if (frame == null)
        {
            return;
        }

        frame.showOptionPaneQuestion(optionPaneMessageType, message, hasCancel, hasNo, optionPaneListener, actionID,
                                     developerInformation);
    }

    /**
     * Show a dialog for ask a question to the user<br>
     * If the component is not attach directly or indirectly in a frame or a diloag nothing append
     *
     * @param optionPaneMessageType Message type
     * @param title                 Dialog title
     * @param message               Question to the user
     * @param hasCancel             Indicates if theire are a cancel button
     * @param hasNo                 Indicate if theire are a no button
     * @param optionPaneListener    Listener to call back when a button is pressed
     * @param actionID              Action id give back to the listener
     * @param developerInformation  Object give back to the listener
     */
    public void showOptionPaneQuestion(
            final OptionPaneMessageType optionPaneMessageType, final String title, final String message,
            final boolean hasCancel,
            final boolean hasNo, final JHelpOptionPaneListener optionPaneListener, final int actionID,
            final Object developerInformation)
    {
        final JHelpFrame2D frame = UtilTwoD.frameOwner(this);
        if (frame == null)
        {
            return;
        }

        frame.showOptionPaneQuestion(optionPaneMessageType, title, message, hasCancel, hasNo, optionPaneListener,
                                     actionID, developerInformation);
    }

    /**
     * Show a dialog for ask a question to the user<br>
     * If the component is not attach directly or indirectly in a frame or a diloag nothing append
     *
     * @param message              Question to the user
     * @param hasCancel            Indicates if theire are a cancel button
     * @param hasNo                Indicate if theire are a no button
     * @param optionPaneListener   Listener to call back when a button is pressed
     * @param actionID             Action id give back to the listener
     * @param developerInformation Object give back to the listener
     */
    public void showOptionPaneQuestion(
            final String message, final boolean hasCancel, final boolean hasNo,
            final JHelpOptionPaneListener optionPaneListener,
            final int actionID, final Object developerInformation)
    {
        final JHelpFrame2D frame = UtilTwoD.frameOwner(this);
        if (frame == null)
        {
            return;
        }

        frame.showOptionPaneQuestion(message, hasCancel, hasNo, optionPaneListener, actionID, developerInformation);
    }

    /**
     * Show a dialog for ask a question to the user<br>
     * If the component is not attach directly or indirectly in a frame or a diloag nothing append
     *
     * @param title                Dialog title
     * @param message              Question to the user
     * @param hasCancel            Indicates if theire are a cancel button
     * @param hasNo                Indicate if theire are a no button
     * @param optionPaneListener   Listener to call back when a button is pressed
     * @param actionID             Action id give back to the listener
     * @param developerInformation Object give back to the listener
     */
    public void showOptionPaneQuestion(
            final String title, final String message, final boolean hasCancel, final boolean hasNo,
            final JHelpOptionPaneListener optionPaneListener, final int actionID, final Object developerInformation)
    {
        final JHelpFrame2D frame = UtilTwoD.frameOwner(this);
        if (frame == null)
        {
            return;
        }

        frame.showOptionPaneQuestion(title, message, hasCancel, hasNo, optionPaneListener, actionID,
                                     developerInformation);
    }

    /**
     * Tooltip to show when mouse is over the component
     *
     * @return Tooltip to show when mouse is over the component
     */
    public String toolTip()
    {
        return this.toolTip;
    }

    /**
     * Get tooltip for a specific position inside the component.<br>
     * By defaut it calls {@link #toolTip()}, that is to say all the same tooltip any any X,Y
     *
     * @param x X mouse position
     * @param y Y mouse position
     * @return Tooltip to use
     */
    public String toolTip(final int x, final int y)
    {
        return this.toolTip();
    }

    /**
     * Change component tooltip
     *
     * @param toolTip New tooltip, use {@code null} for no tooltip
     */
    public void toolTip(final String toolTip)
    {
        this.toolTip = toolTip;
    }

    /**
     * Indicates if component is valid
     *
     * @return {@code true} if component is valid
     */
    public boolean valid()
    {
        return this.valid;
    }

    /**
     * Indicates if component is visible
     *
     * @return {@code true} if component is visible
     */
    public final boolean visible()
    {
        return this.visible;
    }

    /**
     * Change component visibility
     *
     * @param visible New visibility state
     */
    public final void visible(final boolean visible)
    {
        if (this.visible == visible)
        {
            return;
        }

        this.visible = visible;
        this.invalidate();
    }
}
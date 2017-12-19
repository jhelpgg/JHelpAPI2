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
package jhelp.engine2.twoD;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.ArrayList;
import java.util.List;
import jhelp.engine2.render.Texture;
import jhelp.engine2.twoD.event.Object2DListener;
import jhelp.util.list.Queue;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.ThreadManager;

/**
 * 2D object.<br>
 * It is an image on the 3D.<br>
 * You are able to link listeners to react when event append.<br>
 * To optimize the drawing, it is a good idea to disable the detection for objects who don't need signal events.<br>
 * 2D object can be on foreground or background of the 3D. <br>
 *
 * @author JHelp
 */
public class Object2D
{
    /**
     * Mouse information
     *
     * @author JHelp
     */
    static class MouseInformation
    {
        /**
         * Indicates if mouse left button is down
         */
        final boolean          left;
        /**
         * Event nature
         */
        final int              nature;
        /**
         * Listener to alert
         */
        final Object2DListener object2DListener;
        /**
         * Object 2D where mouse event happen
         */
        final Object2D         object2d;
        /**
         * Indicates if mouse right button is down
         */
        final boolean          right;
        /**
         * Mouse position X on object 2D
         */
        final int              x;
        /**
         * Mouse position Y on object 2D
         */
        final int              y;

        /**
         * Create a new instance of MouseInformation
         *
         * @param object2d         Object 2D where mouse event happen
         * @param object2DListener Listener to alert
         * @param nature           Event nature
         * @param x                Mouse position X on object 2D
         * @param y                Mouse position Y on object 2D
         */
        MouseInformation(
                final @NotNull Object2D object2d, final @NotNull Object2DListener object2DListener,
                final int nature, final int x, final int y)
        {
            this(object2d, object2DListener, nature, x, y, false, false);
        }

        /**
         * Create a new instance of MouseInformation
         *
         * @param object2d         Object 2D where mouse event happen
         * @param object2DListener Listener to alert
         * @param nature           Event nature
         * @param x                Mouse position X on object 2D
         * @param y                Mouse position Y on object 2D
         * @param left             Indicates if mouse left button is down
         * @param right            Indicates if mouse right button is down
         */
        MouseInformation(
                final @NotNull Object2D object2d, final @NotNull Object2DListener object2DListener,
                final int nature, final int x, final int y, final boolean left, final boolean right)
        {
            this.object2d = object2d;
            this.object2DListener = object2DListener;
            this.nature = nature;
            this.x = x;
            this.y = y;
            this.left = left;
            this.right = right;
        }
    }

    /**
     * Signal to listener a mouse event
     *
     * @author JHelp
     */
    static class TaskFireMouse
            implements ConsumerTask<MouseInformation>
    {
        /**
         * Create a new instance of TaskFireMouse
         */
        TaskFireMouse()
        {

        }

        /**
         * Do the task <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param parameter Mouse event description
         * @see ConsumerTask#consume(Object)
         */
        @Override
        public void consume(final @NotNull MouseInformation parameter)
        {
            switch (parameter.nature)
            {
                case Object2D.CLICK:
                    parameter.object2DListener.mouseClick(parameter.object2d,
                                                          parameter.x, parameter.y, parameter.left, parameter.right);
                    break;
                case Object2D.DRAG:
                    parameter.object2DListener.mouseDrag(parameter.object2d,
                                                         parameter.x, parameter.y, parameter.left, parameter.right);
                    break;
                case Object2D.ENTER:
                    parameter.object2DListener.mouseEnter(parameter.object2d, parameter.x, parameter.y);
                    break;
                case Object2D.EXIT:
                    parameter.object2DListener.mouseExit(parameter.object2d, parameter.x, parameter.y);
                    break;
                case Object2D.MOVE:
                    parameter.object2DListener.mouseMove(parameter.object2d, parameter.x, parameter.y);
                    break;
            }
        }
    }

    /**
     * Task for signal mouse events
     */
    private static final TaskFireMouse TASK_FIRE_MOUSE = new TaskFireMouse();
    /**
     * Nature : mouse clicked
     */
    static final         int           CLICK           = 0;
    /**
     * Nature : mouse dragged
     */
    static final         int           DRAG            = 1;
    /**
     * Nature : mouse entered
     */
    static final         int           ENTER           = 2;
    /**
     * Nature : mouse exited
     */
    static final         int           EXIT            = 3;
    /**
     * Nature : mouse moved
     */
    static final         int           MOVE            = 4;
    /**
     * Developer additional information
     */
    private       Object                  additionalInformation;
    /**
     * Listeners register for this object
     */
    private final List<Object2DListener>  arrayListListeners;
    /**
     * Indicates if the object signal events
     */
    private       boolean                 canBeDetected;
    /**
     * Indicates if we are in firing events to listeners
     */
    private       boolean                 firing;
    /**
     * Object's height
     */
    private       int                     height;
    /**
     * Indicates if the mouse is over the object
     */
    private       boolean                 over;
    /**
     * List of listeners to add
     */
    private final Queue<Object2DListener> toAdd;
    /**
     * List of listeners to remove
     */
    private final Queue<Object2DListener> toRemove;
    /**
     * Indicates if the object is visible
     */
    private       boolean                 visible;
    /**
     * Object's width
     */
    private       int                     width;
    /**
     * Object's abscissa
     */
    private       int                     x;
    /**
     * Object's ordinate
     */
    private       int                     y;
    /**
     * Object's texture
     */
    protected     Texture                 texture;

    /**
     * Create an object
     *
     * @param x      Abscissa
     * @param y      Ordinate
     * @param width  Width
     * @param height Height
     */
    public Object2D(final int x, final int y, final int width, final int height)
    {
        this.x = x;
        this.y = y;
        this.width = Math.max(1, width);
        this.height = Math.max(1, height);
        this.canBeDetected = true;
        this.visible = true;
        this.arrayListListeners = new ArrayList<>();
        this.firing = false;
        this.toRemove = new Queue<>();
        this.toAdd = new Queue<>();
        this.over = false;
    }

    /**
     * Reaction on mouse state
     *
     * @param x           Mouse X
     * @param y           Mouse Y
     * @param buttonLeft  Indicates if the left button is down
     * @param buttonRight Indicates if the right button is down
     * @param drag        Indicates if drag mode is on
     * @param over        Indicates if the mouse is over the object
     */
    void mouseState(
            int x, int y, final boolean buttonLeft, final boolean buttonRight, final boolean drag, final boolean over)
    {
        this.firing = true;

        try
        {
            // Compute relative object position
            x -= this.x;
            y -= this.y;

            // If the over state change, then the mouse enter or exit
            if (this.over != over)
            {
                this.over = over;

                if (this.over)
                {
                    this.fireMouseEnter(x, y);
                }
                else
                {
                    this.fireMouseExit(x, y);
                }

                return;
            }

            // If the mouse is not on the object, do nothing
            if (!over)
            {
                return;
            }

            // Drag mode test
            if (drag)
            {
                this.fireMouseDrag(x, y, buttonLeft, buttonRight);
                return;
            }

            // Click mode test
            if (buttonLeft || buttonRight)
            {
                this.fireMouseClick(x, y, buttonLeft, buttonRight);
                return;
            }

            // Move mode
            this.fireMouseMove(x, y);
        }
        finally
        {
            while (!this.toRemove.empty())
            {
                this.arrayListListeners.remove(this.toRemove.outQueue());
            }

            while (!this.toAdd.empty())
            {
                this.arrayListListeners.add(this.toAdd.outQueue());
            }

            this.firing = false;
        }
    }

    /**
     * Signals to listeners that mouse click on the object
     *
     * @param x           Mouse X
     * @param y           Mouse Y
     * @param leftButton  Indicates if the left button is down
     * @param rightButton Indicates if the right button is down
     */
    protected void fireMouseClick(final int x, final int y, final boolean leftButton, final boolean rightButton)
    {
        for (final Object2DListener object2DListener : this.arrayListListeners)
        {
            ThreadManager.parallel(Object2D.TASK_FIRE_MOUSE,
                                   new MouseInformation(this, object2DListener, Object2D.CLICK,
                                                        x, y, leftButton, rightButton));
        }
    }

    /**
     * Signals to listeners that mouse drag on the object
     *
     * @param x           Mouse X
     * @param y           Mouse Y
     * @param leftButton  Indicates if the left button is down
     * @param rightButton Indicates if the right button is down
     */
    protected void fireMouseDrag(final int x, final int y, final boolean leftButton, final boolean rightButton)
    {
        for (final Object2DListener object2DListener : this.arrayListListeners)
        {
            ThreadManager.parallel(Object2D.TASK_FIRE_MOUSE,
                                   new MouseInformation(this, object2DListener, Object2D.DRAG,
                                                        x, y, leftButton, rightButton));
        }
    }

    /**
     * Signals to listeners that mouse enter on the object
     *
     * @param x Mouse X
     * @param y Mouse Y
     */
    protected void fireMouseEnter(final int x, final int y)
    {
        for (final Object2DListener object2DListener : this.arrayListListeners)
        {
            ThreadManager.parallel(Object2D.TASK_FIRE_MOUSE,
                                   new MouseInformation(this, object2DListener, Object2D.ENTER, x, y));
        }
    }

    /**
     * Signals to listeners that mouse exit from the object
     *
     * @param x Mouse X
     * @param y Mouse Y
     */
    protected void fireMouseExit(final int x, final int y)
    {
        for (final Object2DListener object2DListener : this.arrayListListeners)
        {
            ThreadManager.parallel(Object2D.TASK_FIRE_MOUSE,
                                   new MouseInformation(this, object2DListener, Object2D.EXIT, x, y));
        }
    }

    /**
     * Signals to listeners that mouse move on the object
     *
     * @param x Mouse X
     * @param y Mouse Y
     */
    protected void fireMouseMove(final int x, final int y)
    {
        for (final Object2DListener object2DListener : this.arrayListListeners)
        {
            ThreadManager.parallel(Object2D.TASK_FIRE_MOUSE,
                                   new MouseInformation(this, object2DListener, Object2D.MOVE, x, y));
        }
    }

    /**
     * Actual additionalInformation value
     *
     * @return Actual additionalInformation value
     */
    public @Nullable Object additionalInformation()
    {
        return this.additionalInformation;
    }

    /**
     * Change additionalInformation
     *
     * @param additionalInformation New additionalInformation value
     */
    public void additionalInformation(final @Nullable Object additionalInformation)
    {
        this.additionalInformation = additionalInformation;
    }

    /**
     * Indicates if the detection of this object is enable
     *
     * @return {@code true} if the detection of this object is enable
     */
    public boolean canBeDetected()
    {
        return this.canBeDetected;
    }

    /**
     * Change the detection sates
     *
     * @param canBeDetected New detection state
     */
    public void canBeDetected(final boolean canBeDetected)
    {
        this.canBeDetected = canBeDetected;
    }

    /**
     * Indicates if the object contains a point
     *
     * @param x Point's X
     * @param y Point's Y
     * @return {@code true} if the point is over the object
     */
    public boolean detected(final int x, final int y)
    {
        if (!this.canBeDetected || !this.visible || this.texture == null)
        {
            return false;
        }

        return (x >= this.x) && (y >= this.y) && (x < (this.x + this.width)) && (y < (this.y + this.height));
    }

    /**
     * Object's height
     *
     * @return Object's height
     */
    public int height()
    {
        return this.height;
    }

    /**
     * Change object's height
     *
     * @param height New height
     */
    public void height(final int height)
    {
        this.height = Math.max(1, height);
    }

    /**
     * Add object event listener
     *
     * @param object2DListener Listener to add
     */
    public void registerObject2DListener(final @NotNull Object2DListener object2DListener)
    {
        if (this.firing)
        {
            this.toAdd.inQueue(object2DListener);
            return;
        }

        this.arrayListListeners.add(object2DListener);
    }

    /**
     * Object's texture.<br>
     * If the object is not visible, the method return {@code null}
     *
     * @return Object's texture
     */
    public @Nullable Texture texture()
    {
        if (!this.visible)
        {
            return null;
        }

        return this.texture;
    }

    /**
     * Change object's texture.
     *
     * @param texture New object's texture. If {@code null} object will be consider as not visible
     */
    public void texture(final @Nullable Texture texture)
    {
        this.texture = texture;
    }

    /**
     * Remove object event listener
     *
     * @param object2DListener Listener to remove
     */
    public void unregisterObject2DListener(final @NotNull Object2DListener object2DListener)
    {
        if (this.firing)
        {
            this.toRemove.inQueue(object2DListener);
            return;
        }

        this.arrayListListeners.remove(object2DListener);
    }

    /**
     * Indicates if the object is visible
     *
     * @return {@code true} if the object is visible
     */
    public boolean visible()
    {
        return this.visible && this.texture != null;
    }

    /**
     * Change object's visibility.<br>
     * Note: if the texture is {@code null} the object still invisible
     *
     * @param visible New visibility
     */
    public void visible(final boolean visible)
    {
        this.visible = visible;
    }

    /**
     * Object's width
     *
     * @return Object's width
     */
    public int width()
    {
        return this.width;
    }

    /**
     * Change object's width
     *
     * @param width New width
     */
    public void width(final int width)
    {
        this.width = Math.max(1, width);
    }

    /**
     * Object's X
     *
     * @return Object's X
     */
    public int x()
    {
        return this.x;
    }

    /**
     * Change object's X
     *
     * @param x New X
     */
    public void x(final int x)
    {
        this.x = x;
    }

    /**
     * Object's Y
     *
     * @return Object's Y
     */
    public int y()
    {
        return this.y;
    }

    /**
     * Change object's Y
     *
     * @param y New Y
     */
    public void y(final int y)
    {
        this.y = y;
    }
}
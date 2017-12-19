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
import java.util.ArrayList;
import java.util.List;
import jhelp.engine2.render.Texture;
import jhelp.engine2.twoD.event.Button2DListener;
import jhelp.engine2.twoD.event.Object2DListener;
import jhelp.util.gui.JHelpImage;
import jhelp.util.list.Pair;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.ThreadManager;

/**
 * Button 2D
 *
 * @author JHelp
 */
public class Button2D
        extends Object2D
        implements Object2DListener
{
    /**
     * Task for fire button click event
     *
     * @author JHelp
     */
    static class TaskFireButtonClick
            implements ConsumerTask<Pair<Button2D, Button2DListener>>
    {
        /**
         * Create a new instance of TaskFireButtonClick
         */
        TaskFireButtonClick()
        {
        }

        /**
         * Do the task : Fire click event <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param parameter Couple of click button and listener to alert
         * @see ConsumerTask#consume(Object)
         */
        @Override
        public void consume(final Pair<Button2D, Button2DListener> parameter)
        {
            parameter.second.button2DClicked(parameter.first, parameter.first.buttonID());
        }
    }

    /**
     * Task for fire button click event
     */
    private static final TaskFireButtonClick TASK_FIRE_BUTTON_CLICK = new TaskFireButtonClick();

    /**
     * Button ID
     */
    private final int                    buttonID;
    /**
     * Image for clicked state
     */
    private final JHelpImage             clicked;
    /**
     * Listeners of click event
     */
    private final List<Button2DListener> listeners;
    /**
     * Image for normal state
     */
    private final JHelpImage             normal;
    /**
     * Image for over state
     */
    private final JHelpImage             over;
    /**
     * Button texture
     */
    private final Texture                texture;

    /**
     * Create a new instance of Button2D.<br>
     * All states (normal, over, clicked) will use the same image
     *
     * @param buttonID Button ID
     * @param x        X position
     * @param y        Y position
     * @param width    Button width
     * @param height   Button height
     * @param image    Image to use on any state (normal, over, clicked)
     */
    public Button2D(
            final int buttonID,
            final int x, final int y, final int width, final int height,
            final @NotNull JHelpImage image)
    {
        this(buttonID, x, y, width, height, image, image, image);
    }

    /**
     * Create a new instance of Button2D.<br>
     * Normal state have its specified image.<br>
     * Over and clicked state shared the same image
     *
     * @param buttonID       Button ID
     * @param x              X position
     * @param y              Y position
     * @param width          Button width
     * @param height         Button height
     * @param normal         Image for normal state
     * @param overAndClicked Image for over and clicked state
     */
    public Button2D(
            final int buttonID,
            final int x, final int y, final int width, final int height,
            final @NotNull JHelpImage normal, final @NotNull JHelpImage overAndClicked)
    {
        this(buttonID, x, y, width, height, normal, overAndClicked, overAndClicked);
    }

    /**
     * Create a new instance of Button2D.<br>
     * Each state (normal, over, clicked) have its own image
     *
     * @param buttonID Button ID
     * @param x        X position
     * @param y        Y position
     * @param width    Button width
     * @param height   Button height
     * @param normal   Image for normal state
     * @param over     Image for over state
     * @param clicked  Image for clicked state
     */
    public Button2D(
            final int buttonID,
            final int x, final int y, final int width, final int height,
            final @NotNull JHelpImage normal, final @NotNull JHelpImage over, final @NotNull JHelpImage clicked)
    {
        super(x, y, width, height);

        if (normal == null)
        {
            throw new NullPointerException("normal mustn't be null");
        }

        if (over == null)
        {
            throw new NullPointerException("over mustn't be null");
        }

        if (clicked == null)
        {
            throw new NullPointerException("clicked mustn't be null");
        }

        this.listeners = new ArrayList<>();
        this.buttonID = buttonID;
        this.normal = normal;
        this.over = over;
        this.clicked = clicked;
        this.texture = new Texture("Button2D_" + Math.random(), this.normal);
        this.texture(this.texture);

        this.registerObject2DListener(this);
    }

    /**
     * Signal to listeners a click on button
     */
    protected void fireClick()
    {
        synchronized (this.listeners)
        {
            for (final Button2DListener listener : this.listeners)
            {
                ThreadManager.parallel(Button2D.TASK_FIRE_BUTTON_CLICK,
                                       new Pair<>(this, listener));
            }
        }
    }

    /**
     * Button ID
     *
     * @return Button ID
     */
    public int buttonID()
    {
        return this.buttonID;
    }

    /**
     * Called when mouse click on button <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param object2d    Object reference
     * @param x           Mouse X
     * @param y           Mouse Y
     * @param leftButton  Indicates if left button is down
     * @param rightButton Indicates if right button is down
     * @see Object2DListener#mouseClick(Object2D, int, int, boolean, boolean)
     */
    @Override
    public void mouseClick(
            final @NotNull Object2D object2d,
            final int x, final int y, final boolean leftButton, final boolean rightButton)
    {
        this.texture.setImage(this.clicked);
        this.fireClick();
    }

    /**
     * Called when mouse drag on button <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param object2d    Object reference
     * @param x           Mouse X
     * @param y           Mouse Y
     * @param leftButton  Indicates if left button is down
     * @param rightButton Indicates if right button is down
     * @see Object2DListener#mouseDrag(Object2D, int, int, boolean, boolean)
     */
    @Override
    public void mouseDrag(
            final @NotNull Object2D object2d,
            final int x, final int y, final boolean leftButton, final boolean rightButton)
    {
        this.texture.setImage(this.clicked);
    }

    /**
     * Called when mouse enter on button <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param object2d Object reference
     * @param x        Mouse X
     * @param y        Mouse Y
     * @see Object2DListener#mouseEnter(Object2D, int, int)
     */
    @Override
    public void mouseEnter(final @NotNull Object2D object2d, final int x, final int y)
    {
        this.texture.setImage(this.over);
    }

    /**
     * Called when mouse exit from button <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param object2d Object reference
     * @param x        Mouse X
     * @param y        Mouse Y
     * @see Object2DListener#mouseExit(Object2D, int, int)
     */
    @Override
    public void mouseExit(final @NotNull Object2D object2d, final int x, final int y)
    {
        this.texture.setImage(this.normal);
    }

    /**
     * Called when mouse move over button <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param object2d Object reference
     * @param x        Mouse X
     * @param y        Mouse Y
     * @see Object2DListener#mouseMove(Object2D, int, int)
     */
    @Override
    public void mouseMove(final @NotNull Object2D object2d, final int x, final int y)
    {
        this.texture.setImage(this.over);
    }

    /**
     * Register button click listener
     *
     * @param listener Listener to register
     */
    public void registerButton2DListener(final @NotNull Button2DListener listener)
    {
        if (listener == null)
        {
            return;
        }

        synchronized (this.listeners)
        {
            if (!this.listeners.contains(listener))
            {
                this.listeners.add(listener);
            }
        }
    }

    /**
     * Unregister button click listener
     *
     * @param listener Listener to unregister
     */
    public void unregisterButton2DListener(final @NotNull Button2DListener listener)
    {
        synchronized (this.listeners)
        {
            this.listeners.remove(listener);
        }
    }
}
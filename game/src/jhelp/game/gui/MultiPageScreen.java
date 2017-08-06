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

package jhelp.game.gui;

import com.sun.istack.internal.NotNull;
import java.util.HashMap;
import java.util.Map;
import jhelp.util.gui.JHelpImage;
import jhelp.util.thread.StateMachine;

/**
 * Created by jhelp on 12/07/17.
 */
public abstract class MultiPageScreen<S extends Enum> extends StateMachine<S> implements Screen
{
    private       Screen         screen;
    private final Map<S, Screen> screens;

    /**
     * Create the state machine
     *
     * @param initialState Machine initial state
     */
    public MultiPageScreen(final @NotNull S initialState)
    {
        super(initialState);
        this.screens = new HashMap<>();
        this.screen = this.obtainScreen(initialState);
    }

    private @NotNull Screen obtainScreen(@NotNull S state)
    {
        return this.screens.computeIfAbsent(state, this::createScreen);
    }

    protected abstract @NotNull Screen createScreen(@NotNull S state);

    public final void screen(@NotNull S state)
    {
        if (this.changeState(state))
        {
            this.screen = this.obtainScreen(state);
        }
    }

    @Override
    public final void updateScreen(final JHelpImage image)
    {
        this.screen.updateScreen(image);
    }
}

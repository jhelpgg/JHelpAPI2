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

    @Override
    public final void updateScreen(final JHelpImage image)
    {
        this.screen.updateScreen(image);
    }

    public final void screen(@NotNull S state)
    {
        if (this.changeState(state))
        {
            this.screen = this.obtainScreen(state);
        }
    }

    private @NotNull Screen obtainScreen(@NotNull S state)
    {
        return this.screens.computeIfAbsent(state, this::createScreen);
    }

    protected abstract @NotNull Screen createScreen(@NotNull S state);
}

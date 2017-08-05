package jhelp.game.data;

import com.sun.istack.internal.NotNull;

/**
 * Created by jhelp on 20/07/17.
 */
public interface Zoomable
{
    @NotNull Zoom zoom();

    void zoom(@NotNull Zoom zoom);
}

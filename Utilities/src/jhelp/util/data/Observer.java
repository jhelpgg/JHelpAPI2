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

package jhelp.util.data;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

/**
 * Observer of {@link Observable} value changes
 */
public interface Observer<T>
{
    /**
     * Called each time watched observable change.<br>
     * It is also called when register to {@link Observable} via {@link Observable#startObserve(Observer)} to do something with initial value
     *
     * @param observable {@link Observable} that value changed
     * @param newValue   New value give
     */
    void valueChanged(@NotNull Observable<T> observable, @Nullable T newValue);
}

package jhelp.util.data;

import com.sun.istack.internal.NotNull;

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
    void valueChanged(@NotNull Observable<T> observable, @NotNull T newValue);
}

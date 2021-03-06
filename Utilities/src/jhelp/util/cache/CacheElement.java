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

package jhelp.util.cache;

import com.sun.istack.internal.NotNull;
import java.lang.ref.SoftReference;

/**
 * Element of a {@link Cache}
 *
 * @param <T> Element type
 * @author JHelp
 */
public abstract class CacheElement<T>
{
    /**
     * Hide reference
     */
    private SoftReference<T> softReference;

    /**
     * Constructs CacheElement
     */
    public CacheElement()
    {
    }

    /**
     * Called when element is cleared.<br>
     * Does nothing by default
     *
     * @param element Cleared value
     */
    protected void cleared(final @NotNull T element)
    {
        //Does nothing by default
    }

    /**
     * Create the element
     *
     * @return Created element
     */
    protected abstract @NotNull T create();

    /**
     * Remove the element
     */
    public final void clear()
    {
        if (this.softReference != null)
        {
            final T element = this.softReference.get();

            if (element != null)
            {
                this.cleared(element);
            }

            this.softReference.clear();
        }

        this.softReference = null;
    }

    /**
     * Obtain the element
     *
     * @return The element
     */
    public final @NotNull T element()
    {
        T element = null;

        // Check if element is already store
        if (this.softReference != null)
        {
            element = this.softReference.get();
        }

        if (element != null)
        {
            // If already store, return it
            return element;
        }

        // Create element and store it
        element = this.create();

        if (element == null)
        {
            throw new NullPointerException("Created element is null!");
        }

        this.softReference = new SoftReference<>(element);
        return element;
    }
}
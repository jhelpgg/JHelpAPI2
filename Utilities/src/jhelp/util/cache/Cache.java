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
import com.sun.istack.internal.Nullable;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

/**
 * Manage a only RAM cache
 *
 * @param <T> Element type
 */
public final class Cache<T>
{
    /**
     * The cache itself
     */
    private final HashMap<String, CacheElement<T>> cache;

    /**
     * Constructs Cache
     */
    public Cache()
    {
        this.cache = new HashMap<>();
    }

    /**
     * Add element inside the cache
     *
     * @param key     Key associate
     * @param element Describe how create element
     */
    public void add(@NotNull final String key, @NotNull final CacheElement<T> element)
    {
        if (key == null)
        {
            throw new NullPointerException("key MUST NOT be null");
        }

        if (element == null)
        {
            throw new NullPointerException("element MUST NOT be null");
        }

        final CacheElement<T> previous = this.cache.put(key, element);

        if ((previous != null) && (!previous.equals(element)))
        {
            previous.clear();
        }
    }

    /**
     * Clear the cache
     */
    public void clear()
    {
        CacheElement<T> cacheElement;

        for (final String key : this.cache.keySet())
        {
            cacheElement = this.cache.get(key);

            if (cacheElement != null)
            {
                cacheElement.clear();
            }
        }

        this.cache.clear();
    }

    /**
     * Obtain an element and give a default value if key is not already present
     *
     * @param key          Key to get
     * @param cacheElement Cache element to store and use if the key is not already defined. Can be {@code null} if sure key exists
     * @return The element
     */
    public @NotNull T get(final @NotNull String key, final @Nullable CacheElement<T> cacheElement)
    {
        Objects.requireNonNull(key, "key MUST NOT be null!");
        final T element = this.get(key);

        if (element != null)
        {
            return element;
        }

        this.add(key, cacheElement);
        return this.get(key);
    }

    /**
     * Obtain an element
     *
     * @param key Element key
     * @return Element OR {@code null} if no element attached to given key
     */
    public @Nullable T get(final @NotNull String key)
    {
        if (key == null)
        {
            throw new NullPointerException("key MUST NOT be null");
        }

        final CacheElement<T> cacheElement = this.cache.get(key);

        if (cacheElement != null)
        {
            return cacheElement.element();
        }

        return null;
    }

    /**
     * List of keys in cache
     *
     * @return List of keys in cache
     */
    public @NotNull Set<String> keys()
    {
        return this.cache.keySet();
    }

    /**
     * Remove an element from cache
     *
     * @param key Element key
     */
    public void remove(final @NotNull String key)
    {
        final CacheElement<T> cacheElement = this.cache.get(key);

        if (cacheElement != null)
        {
            cacheElement.clear();
            this.cache.remove(key);
        }
    }
}
package jhelp.util.list;

/**
 * Limited map one number of element, if full some elements are removed to make room
 *
 * @param <KEY>   Key type
 * @param <VALUE> Value type
 * @author JHelp
 */
public interface LimitedSizeHashMap<KEY, VALUE>
{
    /**
     * Obtain an element of the map
     *
     * @param key Element key
     * @return Element value
     */
    VALUE get(KEY key);

    /**
     * Map maximum size
     *
     * @return Map maximum size
     */
    int getLimit();

    /**
     * Add/update an element
     *
     * @param key   Key
     * @param value Value
     */
    void put(final KEY key, final VALUE value);

    /**
     * Remove an element of the map
     *
     * @param key Key of element to remove
     */
    void remove(final KEY key);

    /**
     * Number of elements
     *
     * @return Number of elements
     */
    int size();
}
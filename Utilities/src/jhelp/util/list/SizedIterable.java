package jhelp.util.list;

/**
 * Iterable with a known size
 */
public interface SizedIterable<T> extends Iterable<T>
{
    /**
     * Iterable size
     *
     * @return Iterable size
     */
    int size();
}

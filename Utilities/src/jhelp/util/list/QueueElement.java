package jhelp.util.list;

/**
 * Element of the queue
 */
class QueueElement<T>
{
    /**
     * Carried value
     */
    final T element;
    /**
     * Next element
     */
    QueueElement<T> next;

    /**
     * Create the element
     *
     * @param element Value to carry
     */
    public QueueElement(final T element)
    {
        this.element = element;
    }
}

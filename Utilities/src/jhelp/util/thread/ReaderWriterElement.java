package jhelp.util.thread;

/**
 * Element carry task to do in {@link ReaderWriter}
 */
class ReaderWriterElement<D>
{
    /**
     * Data reader
     */
    final ConsumerTask<D> reader;
    /**
     * Data writer
     */
    final ProducerTask<D> writer;
    /**
     * Promise link to know when task is finished
     */
    final Promise<Void>   promise;

    /**
     * Create element for reader
     *
     * @param reader Reader
     */
    ReaderWriterElement(ConsumerTask<D> reader)
    {
        this(reader, null);
    }

    /**
     * Create element for writer
     *
     * @param writer Writer
     */
    ReaderWriterElement(ProducerTask<D> writer)
    {
        this(null, writer);
    }

    /**
     * Create element
     *
     * @param reader Reader
     * @param writer Writer
     */
    private ReaderWriterElement(final ConsumerTask<D> reader, final ProducerTask<D> writer)
    {
        this.reader = reader;
        this.writer = writer;
        this.promise = new Promise<>();
    }
}

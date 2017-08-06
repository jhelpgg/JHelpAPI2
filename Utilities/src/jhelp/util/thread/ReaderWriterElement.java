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

package jhelp.util.thread;

/**
 * Element carry task to do in {@link ReaderWriter}
 */
class ReaderWriterElement<D>
{
    /**
     * Promise link to know when task is finished
     */
    final Promise<Void>   promise;
    /**
     * Data reader
     */
    final ConsumerTask<D> reader;
    /**
     * Data writer
     */
    final ProducerTask<D> writer;

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

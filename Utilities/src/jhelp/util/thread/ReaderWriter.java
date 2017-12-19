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

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import jhelp.util.debug.Debug;
import jhelp.util.list.Queue;

/**
 * Protected data with reader/writer algorithm.<br>
 * Multiple reader can read the data in same time.<br>
 * Only one writer can write in same time.<br>
 * If there at least one reader, the writer wait that all readers registered before him have finished before write.<br>
 * If there one writer writing, following readers and writers wait it finished before take their turn.<br>
 * It guaranties to be fair: all readers and writers will play at their turns, no orphan.
 */
public final class ReaderWriter<D>
{
    /**
     * Indicates if management thread is alive
     */
    private final AtomicBoolean                 alive;
    /**
     * Data to protect
     */
    private       D                             data;
    /**
     * Number of reading readers
     */
    private final AtomicInteger                 numberReader;
    /**
     * Indicates if one writer writing
     */
    private final AtomicBoolean                 oneWriter;
    /**
     * Tasks queue
     */
    private final Queue<ReaderWriterElement<D>> tasks;

    /**
     * Create the reader writer
     *
     * @param data Initial value
     */
    public ReaderWriter(@Nullable D data)
    {
        this.data = data;
        this.alive = new AtomicBoolean(false);
        this.tasks = new Queue<>();
        this.numberReader = new AtomicInteger(0);
        this.oneWriter = new AtomicBoolean(false);
    }

    /**
     * Launch the main thread if necessary
     */
    private void play()
    {
        synchronized (this.alive)
        {
            if (!this.alive.get())
            {
                this.alive.set(true);
                ThreadManager.parallel(this::playTasks);
            }
        }
    }

    /**
     * Launch a reader
     *
     * @param readerWriterElement Element with the reader
     */
    private void playReader(@NotNull ReaderWriterElement<D> readerWriterElement)
    {
        try
        {
            readerWriterElement.reader.consume(this.data);
        }
        catch (Exception exception)
        {
            Debug.exception(exception, "Read failed !");
        }

        synchronized (this.numberReader)
        {
            if (this.numberReader.decrementAndGet() == 0)
            {
                this.numberReader.notifyAll();
            }
        }

        readerWriterElement.promise.setResult(null);
    }

    /**
     * Main thread manager
     */
    private void playTasks()
    {
        ReaderWriterElement<D> readerWriterElement;

        do
        {
            //Obtain next task to do
            synchronized (this.tasks)
            {
                if (this.tasks.empty())
                {
                    //Nothing to do, goodbye
                    this.alive.set(false);
                    return;
                }

                readerWriterElement = this.tasks.outQueue();
            }

            if (readerWriterElement.reader != null)
            {
                //If task is a reader, wait the current writer (if their one) finish to write
                synchronized (this.oneWriter)
                {
                    while (this.oneWriter.get())
                    {
                        try
                        {
                            this.oneWriter.wait();
                        }
                        catch (Exception ignored)
                        {
                        }
                    }
                }

                //Launch the reader
                synchronized (this.numberReader)
                {
                    this.numberReader.incrementAndGet();
                    ThreadManager.parallel(this::playReader, readerWriterElement);
                }
            }
            else if (readerWriterElement.writer != null)
            {
                //If task is a writer, wait the current writer (if their one) finish to write
                synchronized (this.oneWriter)
                {
                    while (this.oneWriter.get())
                    {
                        try
                        {
                            this.oneWriter.wait();
                        }
                        catch (Exception ignored)
                        {
                        }
                    }
                }

                //Wait all currents readers (if their at least one) finish to read
                synchronized (this.numberReader)
                {
                    while (this.numberReader.get() > 0)
                    {
                        try
                        {
                            this.numberReader.wait();
                        }
                        catch (Exception ignored)
                        {
                        }
                    }
                }

                //Launch the writer
                synchronized (this.oneWriter)
                {
                    this.oneWriter.set(true);
                    ThreadManager.parallel(this::playWriter, readerWriterElement);
                }
            }
        }
        while (true);
    }

    /**
     * Launch a writer
     *
     * @param readerWriterElement Element with the writer
     */
    private void playWriter(@NotNull ReaderWriterElement<D> readerWriterElement)
    {
        try
        {
            this.data = readerWriterElement.writer.produce();
        }
        catch (Exception exception)
        {
            Debug.exception(exception, "Write failed !");
        }

        synchronized (this.oneWriter)
        {
            this.oneWriter.set(false);
            this.oneWriter.notifyAll();
        }

        readerWriterElement.promise.setResult(null);
    }

    /**
     * Enqueue a reader, for read the data
     *
     * @param reader Reader to enqueue
     * @return Future for indicates when the read is finished
     */
    public Future<Void> read(@NotNull ConsumerTask<D> reader)
    {
        Objects.requireNonNull(reader, "reader MUST NOT be null!");
        ReaderWriterElement<D> readerWriterElement = new ReaderWriterElement<>(reader);

        synchronized (this.tasks)
        {
            this.tasks.inQueue(readerWriterElement);
        }

        this.play();
        return readerWriterElement.promise.future();
    }

    /**
     * Enqueue a writer, for write the data
     *
     * @param writer Writer to enqueue
     * @return Future for indicates when the write is finished
     */
    public Future<Void> write(@NotNull ProducerTask<D> writer)
    {
        Objects.requireNonNull(writer, "writer MUST NOT be null!");
        ReaderWriterElement<D> readerWriterElement = new ReaderWriterElement<>(writer);

        synchronized (this.tasks)
        {
            this.tasks.inQueue(readerWriterElement);
        }

        this.play();
        return readerWriterElement.promise.future();
    }
}

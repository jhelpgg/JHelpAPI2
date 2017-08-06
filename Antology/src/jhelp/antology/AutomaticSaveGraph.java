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

package jhelp.antology;

import java.io.File;
import java.io.IOException;
import jhelp.util.debug.Debug;
import jhelp.util.io.ByteArray;
import jhelp.util.io.UtilIO;
import jhelp.util.thread.RunnableTask;
import jhelp.util.thread.ThreadManager;

/**
 * Save graph automatically on each change
 */
final class AutomaticSaveGraph implements GraphListener
{
    /**
     * Obtain graph from a file.<br>
     * If the file doesn't exits, it tries to create the file and return an empty graph.
     *
     * @param file File where read/save the graph.
     * @return The graph
     */
    static Graph obtainGraph(File file)
    {
        try
        {
            AutomaticSaveGraph automaticSaveGraph = new AutomaticSaveGraph(file);
            return automaticSaveGraph.graph();
        }
        catch (IOException exception)
        {
            Debug.exception(exception, "Failed to get graph for file: ", file.getAbsolutePath());
        }

        return new Graph();
    }

    /**
     * Graph's file
     */
    private final File         file;
    /**
     * Graph to save
     */
    private final Graph        graph;
    /**
     * Indicates that the current saving is no more consistent, to have to save again when just finished
     */
    private       boolean      saveAgain;
    /**
     * Indicates if graph is saving
     */
    private       boolean      saving;
    /**
     * Task for save the graph
     */
    private final RunnableTask taskSave;

    /**
     * Create the graph automatic saver
     *
     * @param file File where load/save graph
     * @throws IOException On file creation or on load graph issue
     */
    AutomaticSaveGraph(final File file) throws IOException
    {
        if (!UtilIO.createFile(file))
        {
            throw new IOException("Can't create the file: " + file.getAbsolutePath());
        }

        if (!file.canRead() || !file.canWrite())
        {
            throw new IOException("Ths file must allow read/write access: " + file.getAbsolutePath());
        }

        this.file = file;
        this.graph = this.load();
        this.taskSave = this::saveTask;
        this.saving = false;
        this.saveAgain = false;
    }

    /**
     * Load graph from file
     *
     * @return Loaded graph or empty graph if file empty
     * @throws IOException On reading issue
     */
    private Graph load() throws IOException
    {
        ByteArray byteArray = new ByteArray();
        UtilIO.write(this.file, byteArray.getOutputStream());
        Graph graph = null;

        if (byteArray.getSize() > 0)
        {
            try
            {
                graph = Graph.parse(byteArray);
            }
            catch (Exception exception)
            {
                throw new IOException("Can't create graph!", exception);
            }
        }

        if (graph == null)
        {
            graph = new Graph();
        }

        graph.register(this);
        return graph;
    }

    /**
     * Save the graph
     */
    private void saveTask()
    {
        ByteArray byteArray = new ByteArray();
        this.graph.serialize(byteArray);

        try
        {
            UtilIO.write(byteArray.getInputStream(), this.file);
        }
        catch (IOException ioException)
        {
            Debug.exception(ioException, "Failed to save graph in ", this.file.getAbsolutePath());
        }

        synchronized (this.taskSave)
        {
            if (this.saveAgain)
            {
                this.saveAgain = false;
                ThreadManager.parallel(this.taskSave);
            }
            else
            {
                this.saving = false;
            }
        }
    }

    /**
     * Embedded graph
     *
     * @return Embedded graph
     */
    public Graph graph()
    {
        return this.graph;
    }

    /**
     * Called when graph changed
     *
     * @param graph Graph that changed
     */
    @Override
    public void graphChanged(final Graph graph)
    {
        this.save();
    }

    /**
     * Require to save the graph
     */
    public void save()
    {
        synchronized (this.taskSave)
        {
            if (this.saving)
            {
                this.saveAgain = true;
            }
            else
            {
                this.saving = true;
                ThreadManager.parallel(this.taskSave);
            }
        }
    }
}

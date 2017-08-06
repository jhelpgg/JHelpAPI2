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
 * Created by jhelp on 22/07/17.
 */
final class AutomaticSaveGraph implements GraphListener
{
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

    private final File         file;
    private final Graph        graph;
    private       boolean      saveAgain;
    private       boolean      saving;
    private final RunnableTask taskSave;

    public AutomaticSaveGraph(final File file) throws IOException
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

    public Graph graph()
    {
        return this.graph;
    }

    @Override
    public void graphChanged(final Graph graph)
    {
        this.save();
    }

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

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

/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import jhelp.util.debug.Debug;
import jhelp.util.gui.GIF;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.RunnableTask;
import jhelp.util.thread.ThreadManager;

/**
 * Texture with GIF image.<br>
 * If GIF is animated, the animtaion is played
 *
 * @author JHelp
 */
public class TextureGif
        extends Texture
{
    /**
     * Task to load the GIF in background thread
     *
     * @author JHelp
     */
    class TaskObtainGIF
            implements ConsumerTask<File>
    {
        /**
         * Create a new instance of TaskObtainGIF
         */
        TaskObtainGIF()
        {
        }

        /**
         * Do the task to get the GIF image <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param parameter Image GIF file
         * @see jhelp.util.thread.ConsumerTask#consume(Object)
         */
        @Override
        public void consume(final File parameter)
        {
            TextureGif.this.obtainGIF(parameter);
        }
    }

    /**
     * Task for refresh GIF animation
     *
     * @author JHelp
     */
    class TaskRefreshGIF
            implements RunnableTask
    {
        /**
         * Create a new instance of TaskRefreshGIF
         */
        TaskRefreshGIF()
        {
        }

        /**
         * Refresh the texture in GIF animation <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @see RunnableTask#run()
         */
        @Override
        public void run()
        {
            TextureGif.this.refreshGIF();
        }
    }

    /**
     * Indicates if GIF is animated on the texture
     */
    private       boolean        animate;
    /**
     * Associated GIF
     */
    private       GIF            gif;
    /**
     * Image index in animation
     */
    private       int            index;
    /**
     * Indicates if next refresh is launched
     */
    private       boolean        launch;
    /**
     * Synchronization lock
     */
    private final Object         lock;
    /**
     * Task for load the GIF
     */
    private final TaskObtainGIF  taskObtainGIF;
    /**
     * Task for refresh GIF animation
     */
    private final TaskRefreshGIF taskRefreshGIF;

    /**
     * Create a new instance of TextureGif
     *
     * @param file Image GIF file
     */
    public TextureGif(final File file)
    {
        super(file.getAbsolutePath(), Texture.REFERENCE_IMAGE_GIF);

        if (GIF.isGIF(file) == false)
        {
            throw new IllegalArgumentException("The file " + file.getAbsolutePath() + " is not a valid GIF");
        }

        this.index = -1;
        this.animate = true;
        this.launch = false;
        this.taskObtainGIF = new TaskObtainGIF();
        this.taskRefreshGIF = new TaskRefreshGIF();
        this.lock = new Object();
        this.setPixels(1, 1, new byte[4]);
        ThreadManager.parallel(this.taskObtainGIF, file);
    }

    /**
     * Load the GIF image<br>
     * Called by a background thread
     *
     * @param file Image GIF file
     */
    void obtainGIF(final File file)
    {
        InputStream inputStream = null;

        try
        {
            inputStream = new FileInputStream(file);
            this.gif = new GIF(inputStream);

            synchronized (this.lock)
            {
                this.animate = true;
                this.launch = true;
            }

            ThreadManager.parallel(this.taskRefreshGIF);
        }
        catch (final Exception exception)
        {
            Debug.exception(exception, "Failed to load GIF : ", file.getAbsolutePath());
            this.gif = null;
            this.setPixels(1, 1, new byte[4]);
        }
        finally
        {
            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch (final Exception exception)
                {
                }
            }
        }
    }

    /**
     * Refresh GIF animation.<br>
     * Called by a background thread
     */
    void refreshGIF()
    {
        if (this.gif == null)
        {
            return;
        }

        synchronized (this.lock)
        {
            if (this.animate == false)
            {
                this.launch = false;
                return;
            }
        }

        this.index = (this.index + 1) % this.gif.numberOfImage();
        this.setImage(this.gif.getImage(this.index));
        final long time = Math.max(this.gif.getDelay(this.index), 16);

        synchronized (this.lock)
        {
            if (this.animate == false)
            {
                this.launch = false;
                return;
            }

            this.launch = true;
            ThreadManager.parallel(this.taskRefreshGIF, time);
        }
    }

    /**
     * Called by garbage collector when object is destroy to free some memory <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @throws Throwable On issue
     * @see Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable
    {
        if (this.gif != null)
        {
            this.setAnimate(false);
            this.gif.destroy();
        }

        super.finalize();
    }

    /**
     * Change the GIF image file.<br>
     * Nothing happen if file not a valid GIF image file
     *
     * @param gifFile New GIF image file
     */
    public void changeGifFile(final File gifFile)
    {
        if (GIF.isGIF(gifFile) == false)
        {
            return;
        }

        this.setAnimate(false);
        ThreadManager.parallel(this.taskObtainGIF, gifFile);
    }

    /**
     * Indicates if GIF is animated
     *
     * @return {@code true} if GIF is animated
     */
    public boolean isAnimate()
    {
        synchronized (this.lock)
        {
            return this.animate;
        }
    }

    /**
     * Start/stop the animation
     *
     * @param animate New animation state
     */
    public void setAnimate(final boolean animate)
    {
        synchronized (this.lock)
        {
            if (this.animate == animate)
            {
                return;
            }

            this.animate = animate;

            if ((this.animate == true) && (this.launch == false) && (this.gif != null))
            {
                this.launch = true;
                ThreadManager.parallel(this.taskRefreshGIF);
            }
        }
    }
}
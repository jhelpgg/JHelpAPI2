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
package jhelp.engine2.render;

import com.sun.istack.internal.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import jhelp.engine2.animation.Animation;
import jhelp.util.debug.Debug;
import jhelp.util.gui.GIF;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.ThreadManager;

/**
 * Texture with GIF image.<br>
 * If GIF is animated, the animation is played
 *
 * @author JHelp
 */
public class TextureGif
        extends Texture
        implements Animation
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
         * @see ConsumerTask#consume(Object)
         */
        @Override
        public void consume(final File parameter)
        {
            TextureGif.this.obtainGIF(parameter);
        }
    }

    /**
     * Associated GIF
     */
    private       GIF           gif;
    /**
     * Indicates if texture is initialized
     */
    private       boolean       initialized;
    /**
     * Animation start time
     */
    private       long          startTime;
    /**
     * Task for load the GIF
     */
    private final TaskObtainGIF taskObtainGIF;

    /**
     * Create a new instance of TextureGif
     *
     * @param file Image GIF file
     */
    public TextureGif(final @NotNull File file)
    {
        super(file.getAbsolutePath(), Texture.REFERENCE_IMAGE_GIF);

        if (!GIF.isGIF(file))
        {
            throw new IllegalArgumentException("The file " + file.getAbsolutePath() + " is not a valid GIF");
        }

        this.initialized = false;
        this.taskObtainGIF = new TaskObtainGIF();
        this.setPixels(1, 1, new byte[4]);
        ThreadManager.parallel(this.taskObtainGIF, file);
    }

    /**
     * Load the GIF image<br>
     * Called by a background thread
     *
     * @param file Image GIF file
     */
    void obtainGIF(final @NotNull File file)
    {
        InputStream inputStream = null;

        try
        {
            inputStream = new FileInputStream(file);
            this.gif = new GIF(inputStream);
            this.initialized = true;
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
                catch (final Exception ignored)
                {
                }
            }
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
            this.gif.destroy();
        }

        super.finalize();
    }

    /**
     * Call by the renderer each time the animation is refresh on playing
     *
     * @param absoluteFrame Actual absolute frame
     * @return {@code true} if the animation need to be refresh one more time. {@code false} if the animation is end
     */
    @Override
    @ThreadOpenGL
    public boolean animate(final float absoluteFrame)
    {
        if (!this.initialized)
        {
            return true;
        }

        final int numberOfImages = this.gif.numberOfImage();

        if (numberOfImages == 0)
        {
            this.initialized = false;
            return true;
        }

        if (numberOfImages == 1)
        {
            this.setImage(this.gif.getImage(0));
            this.initialized = false;
            return true;
        }

        long time      = System.currentTimeMillis() - this.startTime;
        int  index     = 0;
        int  imageTime = Math.max(16, this.gif.getDelay(index));

        while (time > imageTime)
        {
            time -= imageTime;
            index = (index + 1) % numberOfImages;
            imageTime = Math.max(16, this.gif.getDelay(index));

            if (index == 0)
            {
                //We have done a loop, so we can consider as animation just started
                this.startTime = System.currentTimeMillis();
            }
        }

        this.setImage(this.gif.getImage(index));
        return true;
    }

    /**
     * Call by the renderer to indicates the start absolute frame
     *
     * @param startAbsoluteFrame Start absolute frame
     */
    @Override
    @ThreadOpenGL
    public void startAbsoluteFrame(final float startAbsoluteFrame)
    {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Change the GIF image file.<br>
     * Nothing happen if file not a valid GIF image file
     *
     * @param gifFile New GIF image file
     */
    public void changeGifFile(final @NotNull File gifFile)
    {
        if (!GIF.isGIF(gifFile))
        {
            return;
        }

        this.initialized = false;
        ThreadManager.parallel(this.taskObtainGIF, gifFile);
    }
}
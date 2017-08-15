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

package jhelp.video;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import jhelp.linux.LinuxCommand;
import jhelp.sound.JHelpSound;
import jhelp.sound.SoundFactory;
import jhelp.util.debug.Debug;
import jhelp.util.gui.JHelpImage;
import jhelp.util.io.UtilIO;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.StateMachine;
import jhelp.util.thread.ThreadManager;
import jhelp.util.util.Utilities;

/**
 * Area that shows a video<br>
 * The sound is automatically managed.<br>
 * The image is draw in a {@link JHelpImage} that automatically refreshed
 */
public class Video extends StateMachine<VideoState>
{
    static
    {
        DIRECTORY = UtilIO.obtainExternalFile("JHelp/Video/temporary");
        UtilIO.delete(Video.DIRECTORY);
        UtilIO.createDirectory(Video.DIRECTORY);
    }

    /**
     * Temporary directory where uncompress video samples
     */
    private static final File DIRECTORY;
    /**
     * Next player ID
     */
    private static final AtomicInteger NEXT_ID     = new AtomicInteger(0);
    /**
     * Size (in width and height) of the pause button
     */
    private static final int           PAUSE_SIZE  = 126;
    /**
     * Pause element thickness
     */
    private static final int           PAUSE_THICK = 3;

    /**
     * Video audio
     */
    private       JHelpSound          audio;
    /**
     * Audio extracted file
     */
    private       File                audioFile;
    /**
     * Indicates if video have sound
     */
    private       boolean             hasAudio;
    /**
     * Video height
     */
    private final int                 height;
    /**
     * Next video to play
     */
    private       File                nextVideo;
    /**
     * Current video process
     */
    private       Process             process;
    /**
     * Raw video fifo file
     */
    private       File                rawVideo;
    /**
     * Image where video draw
     */
    private final JHelpImage          video;
    /**
     * Video file source
     */
    private       File                videoFile;
    /**
     * Extract raw video information
     */
    private       VideoRawInformation videoRawInformation;
    /**
     * Indicates if video wait for end of pause
     */
    private final AtomicBoolean       waiting;
    /**
     * Video width
     */
    private final int                 width;

    /**
     * Create video area
     *
     * @param width  Video width
     * @param height Video height
     */
    public Video(int width, int height)
    {
        super(VideoState.NO_FILE);
        this.width = Math.max(32, width);
        this.height = Math.max(32, height);
        this.video = new JHelpImage(this.width, this.height, 0xCAFEFACE);
        this.waiting = new AtomicBoolean(false);
        this.register(this::stateChanged,
                      VideoState.NO_FILE,
                      VideoState.LOADING,
                      VideoState.PLAYING,
                      VideoState.PAUSE,
                      VideoState.STOP);
        final int id = Video.NEXT_ID.getAndIncrement();
        this.rawVideo = new File(Video.DIRECTORY, "video_" + id);
        this.audioFile = new File(Video.DIRECTORY, "audio_" + id + ".wav");
    }

    /**
     * Draw pause over video
     */
    private void drawPause()
    {
        final int x     = (this.width - Video.PAUSE_SIZE) >> 1;
        final int y     = (this.height - Video.PAUSE_SIZE) >> 1;
        final int width = Video.PAUSE_SIZE / 3;
        final int space = width << 1;
        final int thick = Video.PAUSE_THICK << 1;

        this.video.startDrawMode();

        this.video.fillRectangle(x - Video.PAUSE_THICK, y - Video.PAUSE_THICK,
                                 width + thick, Video.PAUSE_SIZE + thick,
                                 0xFF000000);
        this.video.fillRectangle((x + space) - Video.PAUSE_THICK, y - Video.PAUSE_THICK,
                                 width + thick, Video.PAUSE_SIZE + thick,
                                 0xFF000000);
        this.video.fillRectangle(x, y, width, Video.PAUSE_SIZE, 0xFFFFFFFF);
        this.video.fillRectangle(x + space, y, width, Video.PAUSE_SIZE, 0xFFFFFFFF);

        this.video.endDrawMode();
    }

    /**
     * Fill pixels array from RGBA input stream
     *
     * @param inputStream Stream to read
     * @param pixels      Pixels array to fill
     * @return {@code true} if fill succeed
     * @throws IOException On read stream issue
     */
    private boolean fillRGBA(InputStream inputStream, int[] pixels) throws IOException
    {
        final int    size     = this.width * this.height;
        final int    fourSize = size << 2;
        final byte[] rgba     = new byte[fourSize];
        final int    read     = inputStream.read(rgba);

        if (read != fourSize)
        {
            return false;
        }

        for (int pix = 0, p = 0; pix < size; pix++, p += 4)
        {
            pixels[pix] = ((rgba[p + 3] & 0xFF) << 24) |
                          ((rgba[p] & 0xFF) << 16) |
                          ((rgba[p + 1] & 0xFF) << 8) |
                          (rgba[p + 2] & 0xFF);
        }

        return true;
    }

    /**
     * Fill pixels array from YUV 410 P input stream
     *
     * @param inputStream Stream to read
     * @param pixels      Pixels array to fill
     * @return {@code true} if fill succeed
     * @throws IOException On read stream issue
     */
    private boolean fillYUV410P(InputStream inputStream, int[] pixels) throws IOException
    {
        final int fourWidth = this.width << 2;
        final int width11   = this.width + 1;
        final int width12   = this.width + 2;
        final int width13   = this.width + 3;
        final int width20   = this.width << 1;
        final int width21   = width20 + 1;
        final int width22   = width20 + 2;
        final int width23   = width20 + 3;
        final int width30   = this.width * 3;
        final int width31   = width30 + 1;
        final int width32   = width30 + 2;
        final int width33   = width30 + 3;

        final int quarterWidth  = this.width >> 2;
        final int quarterHeight = this.height >> 2;
        final int size          = this.width * this.height;
        final int quarterSize   = quarterWidth * quarterHeight;

        final byte[] y = new byte[size];
        final byte[] u = new byte[quarterSize];
        final byte[] v = new byte[quarterSize];

        int    read, pix, pixUV, line;
        double yy, uu, vv;

        read = UtilIO.readStream(inputStream, y);

        if (read != size)
        {
            return false;
        }

        read = UtilIO.readStream(inputStream, u);

        if (read != quarterSize)
        {
            return false;
        }

        read = UtilIO.readStream(inputStream, v);

        if (read != quarterSize)
        {
            return false;
        }

        line = 0;
        pixUV = 0;

        for (int yyy = 0; yyy < quarterHeight; yyy++)
        {
            pix = line;

            for (int xxx = 0; xxx < quarterWidth; xxx++)
            {
                uu = u[pixUV] & 0xFF;
                vv = v[pixUV] & 0xFF;

                yy = y[pix] & 0xFF;
                pixels[pix] = 0xFF000000 |
                              (JHelpImage.computeRed(yy, uu, vv) << 16) |
                              (JHelpImage.computeGreen(yy, uu, vv) << 8) |
                              JHelpImage.computeBlue(yy, uu, vv);
                yy = y[pix + 1] & 0xFF;
                pixels[pix + 1] = 0xFF000000 |
                                  (JHelpImage.computeRed(yy, uu, vv) << 16) |
                                  (JHelpImage.computeGreen(yy, uu, vv) << 8) |
                                  JHelpImage.computeBlue(yy, uu, vv);
                yy = y[pix + 2] & 0xFF;
                pixels[pix + 2] = 0xFF000000 |
                                  (JHelpImage.computeRed(yy, uu, vv) << 16) |
                                  (JHelpImage.computeGreen(yy, uu, vv) << 8) |
                                  JHelpImage.computeBlue(yy, uu, vv);
                yy = y[pix + 3] & 0xFF;
                pixels[pix + 3] = 0xFF000000 |
                                  (JHelpImage.computeRed(yy, uu, vv) << 16) |
                                  (JHelpImage.computeGreen(yy, uu, vv) << 8) |
                                  JHelpImage.computeBlue(yy, uu, vv);

                yy = y[pix + this.width] & 0xFF;
                pixels[pix + this.width] = 0xFF000000 |
                                           (JHelpImage.computeRed(yy, uu, vv) << 16) |
                                           (JHelpImage.computeGreen(yy, uu, vv) << 8) |
                                           JHelpImage.computeBlue(yy, uu, vv);
                yy = y[pix + width11] & 0xFF;
                pixels[pix + width11] = 0xFF000000 |
                                        (JHelpImage.computeRed(yy, uu, vv) << 16) |
                                        (JHelpImage.computeGreen(yy, uu, vv) << 8) |
                                        JHelpImage.computeBlue(yy, uu, vv);
                yy = y[pix + width12] & 0xFF;
                pixels[pix + width12] = 0xFF000000 |
                                        (JHelpImage.computeRed(yy, uu, vv) << 16) |
                                        (JHelpImage.computeGreen(yy, uu, vv) << 8) |
                                        JHelpImage.computeBlue(yy, uu, vv);
                yy = y[pix + width13] & 0xFF;
                pixels[pix + width13] = 0xFF000000 |
                                        (JHelpImage.computeRed(yy, uu, vv) << 16) |
                                        (JHelpImage.computeGreen(yy, uu, vv) << 8) |
                                        JHelpImage.computeBlue(yy, uu, vv);

                yy = y[pix + width20] & 0xFF;
                pixels[pix + width20] = 0xFF000000 |
                                        (JHelpImage.computeRed(yy, uu, vv) << 16) |
                                        (JHelpImage.computeGreen(yy, uu, vv) << 8) |
                                        JHelpImage.computeBlue(yy, uu, vv);
                yy = y[pix + width21] & 0xFF;
                pixels[pix + width21] = 0xFF000000 |
                                        (JHelpImage.computeRed(yy, uu, vv) << 16) |
                                        (JHelpImage.computeGreen(yy, uu, vv) << 8) |
                                        JHelpImage.computeBlue(yy, uu, vv);
                yy = y[pix + width22] & 0xFF;
                pixels[pix + width22] = 0xFF000000 |
                                        (JHelpImage.computeRed(yy, uu, vv) << 16) |
                                        (JHelpImage.computeGreen(yy, uu, vv) << 8) |
                                        JHelpImage.computeBlue(yy, uu, vv);
                yy = y[pix + width23] & 0xFF;
                pixels[pix + width23] = 0xFF000000 |
                                        (JHelpImage.computeRed(yy, uu, vv) << 16) |
                                        (JHelpImage.computeGreen(yy, uu, vv) << 8) |
                                        JHelpImage.computeBlue(yy, uu, vv);

                yy = y[pix + width30] & 0xFF;
                pixels[pix + width30] = 0xFF000000 |
                                        (JHelpImage.computeRed(yy, uu, vv) << 16) |
                                        (JHelpImage.computeGreen(yy, uu, vv) << 8) |
                                        JHelpImage.computeBlue(yy, uu, vv);
                yy = y[pix + width31] & 0xFF;
                pixels[pix + width31] = 0xFF000000 |
                                        (JHelpImage.computeRed(yy, uu, vv) << 16) |
                                        (JHelpImage.computeGreen(yy, uu, vv) << 8) |
                                        JHelpImage.computeBlue(yy, uu, vv);
                yy = y[pix + width32] & 0xFF;
                pixels[pix + width32] = 0xFF000000 |
                                        (JHelpImage.computeRed(yy, uu, vv) << 16) |
                                        (JHelpImage.computeGreen(yy, uu, vv) << 8) |
                                        JHelpImage.computeBlue(yy, uu, vv);
                yy = y[pix + width33] & 0xFF;
                pixels[pix + width33] = 0xFF000000 |
                                        (JHelpImage.computeRed(yy, uu, vv) << 16) |
                                        (JHelpImage.computeGreen(yy, uu, vv) << 8) |
                                        JHelpImage.computeBlue(yy, uu, vv);

                pix += 4;
                pixUV++;
            }

            line += fourWidth;
        }

        return true;
    }

    /**
     * Fill pixels array from YUV 420 P input stream
     *
     * @param inputStream Stream to read
     * @param pixels      Pixels array to fill
     * @return {@code true} if fill succeed
     * @throws IOException On read stream issue
     */
    private boolean fillYUV420P(InputStream inputStream, int[] pixels) throws IOException
    {
        final int    twiceWidth = this.width << 1;
        final int    width1     = this.width + 1;
        final int    halfWidth  = this.width >> 1;
        final int    halfHeight = this.height >> 1;
        final int    size       = this.width * this.height;
        final int    halfSize   = halfWidth * halfHeight;
        final byte[] y          = new byte[size];
        final byte[] u          = new byte[halfSize];
        final byte[] v          = new byte[halfSize];
        int          read, pix, pixUV, line;
        double       yy, uu, vv;

        read = UtilIO.readStream(inputStream, y);

        if (read != size)
        {
            return false;
        }

        read = UtilIO.readStream(inputStream, u);

        if (read != halfSize)
        {
            return false;
        }

        read = UtilIO.readStream(inputStream, v);

        if (read != halfSize)
        {
            return false;
        }

        line = 0;
        pixUV = 0;

        for (int yyy = 0; yyy < halfHeight; yyy++)
        {
            pix = line;

            for (int xxx = 0; xxx < halfWidth; xxx++)
            {
                uu = u[pixUV] & 0xFF;
                vv = v[pixUV] & 0xFF;

                yy = y[pix] & 0xFF;
                pixels[pix] = 0xFF000000 |
                              (JHelpImage.computeRed(yy, uu, vv) << 16) |
                              (JHelpImage.computeGreen(yy, uu, vv) << 8) |
                              JHelpImage.computeBlue(yy, uu, vv);
                yy = y[pix + 1] & 0xFF;
                pixels[pix + 1] = 0xFF000000 |
                                  (JHelpImage.computeRed(yy, uu, vv) << 16) |
                                  (JHelpImage.computeGreen(yy, uu, vv) << 8) |
                                  JHelpImage.computeBlue(yy, uu, vv);
                yy = y[pix + this.width] & 0xFF;
                pixels[pix + this.width] = 0xFF000000 |
                                           (JHelpImage.computeRed(yy, uu, vv) << 16) |
                                           (JHelpImage.computeGreen(yy, uu, vv) << 8) |
                                           JHelpImage.computeBlue(yy, uu, vv);
                yy = y[pix + width1] & 0xFF;
                pixels[pix + width1] = 0xFF000000 |
                                       (JHelpImage.computeRed(yy, uu, vv) << 16) |
                                       (JHelpImage.computeGreen(yy, uu, vv) << 8) |
                                       JHelpImage.computeBlue(yy, uu, vv);

                pix += 2;
                pixUV++;
            }

            line += twiceWidth;
        }

        return true;
    }

    /**
     * Load and launch a video file
     *
     * @param file Video file to load
     */
    private void load(File file)
    {
        if (this.process != null)
        {
            try
            {
                this.process.destroy();
            }
            catch (Exception ignored)
            {
            }

            this.process = null;
        }

        UtilIO.delete(this.audioFile);
        UtilIO.delete(this.rawVideo);

        try
        {
            int result = LinuxCommand.mkfifo(this.rawVideo);

            if (result != 0)
            {
                throw new IOException("Issue while create fifo: " + this.rawVideo.getAbsolutePath());
            }

            result = LinuxCommand.extractAudioWav(file, this.audioFile);
            this.hasAudio = result == 0;

            if (!this.hasAudio)
            {
                Debug.warning("Failed to extract audio from video: ", file.getAbsolutePath());
            }

            VideoRawProcessReader videoRawProcessReader = new VideoRawProcessReader();
            this.process = LinuxCommand.extractRawVideo(file, this.rawVideo, this.width,
                                                        this.height,
                                                        videoRawProcessReader);

            this.videoRawInformation = videoRawProcessReader.videoRawInformation();
            boolean pause = this.state() == VideoState.NO_FILE;
            this.changeState(VideoState.PLAYING);

            if (pause)
            {
                ThreadManager.parallel(() -> this.changeState(VideoState.PAUSE), 256);
            }

            result = this.process.waitFor();

            if (result != 0)
            {
                throw new IOException("Failed to extract raw video information from video: " + file.getAbsolutePath());
            }
        }
        catch (Exception exception)
        {
            switch (this.state())
            {
                case PLAYING:
                case PAUSE:
                case LOADING:
                    Debug.exception(exception, "Failed to load video: " + file.getAbsolutePath());
                    break;
            }

            this.changeState(VideoState.NO_FILE);
        }
    }

    /**
     * Play the video
     */
    private void playing()
    {
        InputStream  inputStream = null;
        final long   wait        = 1000 / this.videoRawInformation.fps;
        long         time, left;
        PixelsFiller fill;

        switch (this.videoRawInformation.videoRawFormat)
        {
            case RGBA:
                fill = this::fillRGBA;
                break;
            case YUV410P:
                fill = this::fillYUV410P;
                break;
            case YUV420P:
                fill = this::fillYUV420P;
                break;
            default:
                Debug.warning("Unknown format:", this.videoRawInformation.videoRawFormat);
                return;
        }

        try
        {
            final int[] pixels = new int[this.width * this.height];
            inputStream = new FileInputStream(this.rawVideo);

            if (this.hasAudio)
            {
                if (this.audio == null)
                {
                    this.audio = SoundFactory.getSoundFromFileNoCache(this.audioFile);
                }

                this.audio.play();
            }

            time = System.currentTimeMillis();

            while ((this.state() == VideoState.PLAYING || this.state() == VideoState.PAUSE) &&
                   fill.fillPixels(inputStream, pixels))
            {
                this.video.startDrawMode();
                this.video.setPixels(0, 0, this.width, this.height, pixels);
                this.video.endDrawMode();

                left = wait - (System.currentTimeMillis() - time);
                System.out.println(left);

                if (left > 0)
                {
                    Utilities.sleep(left);
                }

                synchronized (this.waiting)
                {
                    while (this.state() == VideoState.PAUSE)
                    {
                        if (this.hasAudio)
                        {
                            this.audio.pause();
                        }

                        this.drawPause();

                        this.waiting.set(true);

                        try
                        {
                            this.waiting.wait();
                        }
                        catch (Exception ignored)
                        {
                        }

                        this.waiting.set(false);
                    }

                    if (this.state() == VideoState.STOP)
                    {
                        return;
                    }
                }

                time = System.currentTimeMillis();
            }
        }
        catch (Exception exception)
        {
            Debug.exception(exception, "Issue while playing video: " + this.videoFile.getAbsolutePath());
        }
        finally
        {
            if (this.hasAudio && this.audio != null)
            {
                this.audio.stop();
                this.audio.destroy();
                this.audio = null;
            }

            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch (Exception ignored)
                {
                }
            }

            this.changeState(VideoState.STOP);
        }
    }

    /**
     * Called when video state changed
     *
     * @param videoState New state
     */
    private void stateChanged(VideoState videoState)
    {
        switch (videoState)
        {
            case NO_FILE:
                if (this.nextVideo != null)
                {
                    this.videoFile = this.nextVideo;
                    this.nextVideo = null;
                    this.changeState(VideoState.LOADING);
                }
                break;
            case LOADING:
                ThreadManager.parallel(this::load, this.videoFile);
                break;
            case PLAYING:
                synchronized (this.waiting)
                {
                    if (this.waiting.get())
                    {
                        this.waiting.notify();
                    }
                    else
                    {
                        ThreadManager.parallel(this::playing);
                    }
                }
                break;
            case PAUSE:
                break;
            case STOP:
                synchronized (this.waiting)
                {
                    if (this.waiting.get())
                    {
                        this.waiting.notify();
                    }
                    else
                    {
                        this.changeState(VideoState.NO_FILE);
                    }
                }
                break;
        }
    }

    /**
     * Indicates if it is allow to go from a state to an other state.<br>
     * By default all transition are allowed
     *
     * @param oldState State it leave
     * @param newState State want go
     * @return {@code true} if it is allow to go from a state to an other state
     */
    @Override
    public boolean allowedTransition(final VideoState oldState, final VideoState newState)
    {
        switch (oldState)
        {
            case NO_FILE:
                return newState == VideoState.LOADING || newState == VideoState.NO_FILE ||
                       newState == VideoState.PLAYING;
            case LOADING:
                return newState == VideoState.PLAYING || newState == VideoState.NO_FILE;
            case PLAYING:
                return newState == VideoState.PAUSE || newState == VideoState.STOP || newState == VideoState.NO_FILE;
            case PAUSE:
                return newState == VideoState.PLAYING || newState == VideoState.STOP;
            case STOP:
                return newState == VideoState.NO_FILE || newState == VideoState.STOP;
            default:
                return false;
        }
    }

    /**
     * Stop the video and forget the initial file.<br>
     * Have to call {@link #play(File)} for load an other video
     */
    public void definitiveStop()
    {
        this.nextVideo = null;
        this.changeState(VideoState.STOP);
    }

    /**
     * Current video frame per second.<br>
     * FPS have meaning only if video {@link #state()} is {@link VideoState#PAUSE} or {@link VideoState#PLAYING}
     *
     * @return Current video FPS. Meaningless number if no current video or video loading
     */
    public int fps()
    {
        if (this.videoRawInformation != null)
        {
            return this.videoRawInformation.fps;
        }

        return -1;
    }

    /**
     * Pause the current video
     */
    public void pause()
    {
        this.changeState(VideoState.PAUSE);
    }

    /**
     * Play as soon as possible a given video file
     *
     * @param videoFile Video file to play
     * @return {@code true} if video will be played. {@code false} if currently impossible for launch the video file
     */
    public boolean play(File videoFile)
    {
        if (!videoFile.exists() || !videoFile.isFile() || !videoFile.canRead())
        {
            throw new IllegalArgumentException(
                    videoFile.getAbsolutePath() + " not exist, not a file or can't be read!");
        }

        switch (this.state())
        {
            case NO_FILE:
                this.nextVideo = videoFile;
                this.stateChanged(VideoState.NO_FILE);
                return true;
            case LOADING:
                return false;
            case PAUSE:
            case PLAYING:
            case STOP:
                this.nextVideo = videoFile;
                this.changeState(VideoState.STOP);
                return true;
            default:
                return false;
        }
    }

    /**
     * Register listener of chang video states: {@link VideoState#PLAYING}, {@link VideoState#PAUSE} and {@link VideoState#STOP}
     *
     * @param listener Listener to register
     */
    public void registerVideoListener(ConsumerTask<VideoState> listener)
    {
        Objects.requireNonNull(listener, "listener MUST NOT be null!");
        this.register(listener, VideoState.PLAYING, VideoState.PAUSE, VideoState.STOP);
    }

    /**
     * Resume video after a pause or a stop
     */
    public void resumePlay()
    {
        this.changeState(VideoState.PLAYING);
    }

    /**
     * Video size
     *
     * @return Video size
     */
    public Dimension size()
    {
        return new Dimension(this.width, this.height);
    }

    /**
     * Stop the video.<br>
     * Video will restart at zero
     */
    public void stop()
    {
        this.nextVideo = this.videoFile;
        this.changeState(VideoState.STOP);
    }

    /**
     * Unregister a video listener
     *
     * @param listener
     */
    public void unregisterVideoListener(ConsumerTask<VideoState> listener)
    {
        this.unregisterAll(listener);
    }

    /**
     * Image where video draw
     *
     * @return Image where video draw
     */
    public JHelpImage video()
    {
        return this.video;
    }
}
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

package jhelp.linux;

import java.io.File;
import java.io.IOException;
import jhelp.util.io.UtilIO;
import jhelp.util.process.ProcessManager;
import jhelp.util.process.ProcessStreamReader;

public class LinuxCommand
{
    public static int extractAudioWav(File videoSource, File audioDestination) throws IOException, InterruptedException
    {
        if (!videoSource.exists() || !videoSource.isFile() || !videoSource.canRead())
        {
            throw new IllegalArgumentException(videoSource.getAbsolutePath() + " not found or can't read");
        }

        if (!audioDestination.getPath().endsWith(".wav"))
        {
            throw new IllegalArgumentException(
                    "Destination must be end with .wav. Not the case of " + audioDestination.getAbsolutePath());
        }

        if (!UtilIO.createFile(audioDestination))
        {
            throw new IOException("Can't create audio destination: " + audioDestination.getAbsolutePath());
        }

        return ProcessManager.callCommand("/usr/bin/ffmpeg",
                                          "-i", videoSource.getAbsolutePath(),
                                          "-y", audioDestination.getAbsolutePath());
    }

    public static Process extractRawVideo(
            File videoSource, File rawTarget, int width, int height, ProcessStreamReader videoInformation)
            throws IOException, InterruptedException
    {
        if (!videoSource.exists() || !videoSource.isFile() || !videoSource.canRead())
        {
            throw new IllegalArgumentException(videoSource.getAbsolutePath() + " not found or can't read");
        }

        return ProcessManager.launchProcess(null,
                                            videoInformation,
                                            "/usr/bin/ffmpeg",
                                            "-i", videoSource.getAbsolutePath(),
                                            "-s", Math.max(16, width) + "x" + Math.max(16, height),
                                            "-f", "rawvideo",
                                            "-y", rawTarget.getAbsolutePath());
    }

    public static int mkfifo(File fifoFile) throws IOException, InterruptedException
    {
        if (!UtilIO.createFile(fifoFile) ||
            !fifoFile.canRead() || !fifoFile.canWrite() ||
            UtilIO.isVirtualLink(fifoFile))
        {
            throw new IllegalArgumentException(fifoFile.getAbsolutePath() + " not a valid path or can't read/write");
        }

        UtilIO.delete(fifoFile);
        return ProcessManager.callCommand("mkfifo", fifoFile.getAbsolutePath());
    }

    public static int pico2wave(PicoLanguage picoLanguage, String text, File fileTarget)
            throws IOException, InterruptedException
    {
        if (!UtilIO.delete(fileTarget) && !UtilIO.createFile(fileTarget))
        {
            throw new IOException("Can't create file: " + fileTarget.getAbsolutePath());
        }

        return ProcessManager.callCommand("/usr/bin/pico2wave",
                                          "-w", fileTarget.getAbsolutePath(),
                                          "-l", picoLanguage.getLanguage(),
                                          text);
    }

    public static int playSoundInVLC(File fileSource) throws IOException, InterruptedException
    {
        return ProcessManager.callCommand("/usr/bin/cvlc",
                                          "--no-loop",
                                          "--play-and-exit",
                                          fileSource.getAbsolutePath());
    }

    public static int speak(PicoLanguage picoLanguage, String text) throws IOException, InterruptedException
    {
        File target = UtilIO.createTemporaryFile("speak.wav");
        int  result = LinuxCommand.pico2wave(picoLanguage, text, target);

        if (result != 0)
        {
            return result;
        }

        result = LinuxCommand.playSoundInVLC(target);
        UtilIO.delete(target);
        return result;
    }
}

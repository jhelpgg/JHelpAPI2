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

package jhelp.engine.util;

import java.io.File;
import java.io.IOException;
import jhelp.util.debug.Debug;
import jhelp.util.io.UtilIO;

/**
 * Install libraries need for communicate with Open GL
 *
 * @author JHelp
 */
public class LibraryInstaller
{
    /**
     * Indicates that the libraries are all ready installed
     */
    private static boolean alreadyInstall = false;

    /**
     * Install the libraries
     */
    public static void install()
    {
        if (LibraryInstaller.alreadyInstall == true)
        {
            return;
        }

        // Get the jar name where extract the good libraries for the local OS
        final String jarFileName = NativeLibInfo.getJarFileName();
        Debug.verbose("jarFileName=", jarFileName);
        if (jarFileName == null)
        {
            return;
        }

        // Open the jar file
        final File file = UtilIO.obtainExternalFile("jar/" + jarFileName);
        Debug.verbose("file=", file.getAbsolutePath(), " | ", file.exists());
        if (file.exists() == false)
        {
            return;
        }

        Debug.verbose("", System.getProperties());
        Debug.mark("COPY START");

        try
        {
            UtilIO.unzip(UtilIO.obtainExternalFile("jar"), file);
        }
        catch (final IOException exception)
        {
            Debug.exception(exception, "Issue while extracting the jar : ", file.getAbsolutePath(), " into : ",
                            UtilIO.obtainExternalFile("jar").getAbsolutePath());
        }

        Debug.mark("COPY END");

        LibraryInstaller.alreadyInstall = true;
    }
}
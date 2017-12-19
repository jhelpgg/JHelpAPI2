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

package jhelp.samples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Stack;
import jhelp.util.debug.Debug;
import jhelp.util.filter.FileFilter;

public class Search
{
    public static void main(final String[] args)
    {
        final File       directory  = new File(args[0]);
        final String     searched   = args[1];
        final FileFilter fileFilter = new FileFilter();
        fileFilter.addExtension("java");
        fileFilter.addExtension("h");
        fileFilter.addExtension("hpp");
        fileFilter.addExtension("hxx");
        fileFilter.addExtension("c");
        fileFilter.addExtension("cpp");
        final Stack<File> files = new Stack<>();
        files.push(directory);
        File   file;
        File[] children;

        while (!files.empty())
        {
            file = files.pop();

            if (file.isDirectory())
            {
                children = file.listFiles((java.io.FileFilter) fileFilter);

                if (children != null)
                {
                    for (File child : children)
                    {
                        files.push(child);
                    }
                }
            }
            else
            {
                Search.searchInside(file, searched);
            }
        }
    }

    private static void searchInside(final File file, final String searched)
    {
        BufferedReader bufferedReader = null;

        try
        {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            final String filePath = file.getAbsolutePath();
            int          count    = 1;
            String       line     = bufferedReader.readLine();

            while (line != null)
            {
                if (line.contains(searched))
                {
                    System.out.println(filePath + ":" + count + ": " + line);
                }

                count++;
                line = bufferedReader.readLine();
            }
        }
        catch (final Exception exception)
        {
            Debug.exception(exception);
        }
        finally
        {
            if (bufferedReader != null)
            {
                try
                {
                    bufferedReader.close();
                }
                catch (final Exception ignored)
                {
                }
            }
        }
    }
}

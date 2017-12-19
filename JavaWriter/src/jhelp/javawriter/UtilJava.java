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

package jhelp.javawriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import jhelp.util.io.TextWriter;
import jhelp.util.io.UtilIO;

/**
 * Created by jhelp on 24/06/17.
 */
public class UtilJava
{
    static TextWriter createJavaFile(File baseDirectory, String packageName, String className) throws IOException
    {
        if (!packageName.isEmpty())
        {
            baseDirectory = new File(baseDirectory, packageName.replace('.', File.separatorChar));
        }

        File file = new File(baseDirectory, className + ".java");

        if (!UtilIO.createFile(file))
        {
            throw new IOException("Can't create file: " + file.getAbsolutePath());
        }

        return new TextWriter(new FileOutputStream(file));
    }

    static void writeComment(TextWriter textWriter, String spaces, String comment)
    {
        if (comment == null)
        {
            return;
        }

        textWriter.print(spaces);
        textWriter.println("/**");
        textWriter.print(spaces);
        textWriter.print(" * ");
        textWriter.println(comment.replace("\n", "<br>\n" + spaces + " *"));
        textWriter.print(spaces);
        textWriter.println("*/");
    }
}

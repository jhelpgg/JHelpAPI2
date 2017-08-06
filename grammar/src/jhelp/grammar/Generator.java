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

package jhelp.grammar;

import com.sun.istack.internal.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import jhelp.javawriter.JavaClass;
import jhelp.javawriter.JavaEnum;
import jhelp.util.debug.Debug;
import jhelp.util.io.UtilIO;
import jhelp.util.list.ForEach;

/**
 * Created by jhelp on 23/06/17.
 */
public class Generator
{
    private static final String SPACES = "   ";

    private static void fillElementsType(JavaEnum elementTypes, Grammar grammar)
    {
        ForEach.sync(grammar, rule -> elementTypes.addChoice(rule.getName()));
    }

    public static void generate(
            @NotNull String grammarName, @NotNull Grammar grammar, @NotNull File baseDirectory,
            @NotNull String packageName)
            throws IOException
    {
        Objects.requireNonNull(grammarName, "grammarName MUST NOT be null!");
        Objects.requireNonNull(grammar, "grammar MUST NOT be null!");
        Objects.requireNonNull(baseDirectory, "baseDirectory MUST NOT be null!");
        Objects.requireNonNull(packageName, "packageName MUST NOT be null!");

        grammarName = grammarName.trim()
                                 .replace(' ', '_')
                                 .replace('\\', '_')
                                 .replace('\t', '_')
                                 .replace('.', '_')
                                 .replace('/', '_');

        if (grammarName.isEmpty())
        {
            throw new IllegalArgumentException("grammarName MUST NOT be empty!");
        }

        baseDirectory = new File(baseDirectory, packageName.replace('.', File.separatorChar));

        if (!UtilIO.delete(baseDirectory) && !UtilIO.createDirectory(baseDirectory))
        {
            throw new IOException("Can't create directory: " + baseDirectory.getAbsolutePath());
        }

        JavaEnum  elementTypes = new JavaEnum(packageName + ".ElementTypes_" + grammarName, true, true);
        JavaClass parser       = new JavaClass(packageName + ".Parser_" + grammarName, true, true);

        Generator.fillElementsType(elementTypes, grammar);
        // TODO Generates the grammar
        Debug.todo("Generates the grammar");

        elementTypes.write(baseDirectory, Generator.SPACES);
        parser.write(baseDirectory, Generator.SPACES);
    }
}

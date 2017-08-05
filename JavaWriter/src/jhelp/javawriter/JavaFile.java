package jhelp.javawriter;

import java.io.File;
import java.io.IOException;
import jhelp.util.io.TextWriter;
import jhelp.util.list.SortedArray;

/**
 * Created by jhelp on 24/06/17.
 */
public abstract class JavaFile
{
    private final String              packageName;
    private final String              className;
    private final boolean             isPublic;
    private final boolean             isFinal;
    private       String              comment;
    private       String              nature;
    private final SortedArray<String> imports;
    private final String              parent;
    private final SortedArray<String> implementedList;

    public JavaFile(String nature, String fullName, boolean isPublic, boolean isFinal)
    {
        this(nature, fullName, isPublic, isFinal, null);
    }

    public JavaFile(String nature, String fullName, boolean isPublic, boolean isFinal, String parent)
    {
        this.imports = new SortedArray<>(String.class, true);
        this.implementedList = new SortedArray<>(String.class, true);
        this.nature = nature;
        int index = fullName.lastIndexOf('.');

        if (index >= 0)
        {
            this.packageName = fullName.substring(0, index);
            this.className = fullName.substring(index + 1);
        }
        else
        {
            this.packageName = "";
            this.className = fullName;
        }

        this.isPublic = isPublic;
        this.isFinal = isFinal;
        this.parent = parent;
    }

    public final void addImport(String completeName)
    {
        this.imports.add(completeName);
    }

    public final void addImplements(String completeName)
    {
        this.implementedList.add(completeName);
    }

    public final String comment()
    {
        return this.comment;
    }

    public final void comment(final String comment)
    {
        this.comment = comment;
    }

    public final boolean isPublic()
    {
        return this.isPublic;
    }

    public final boolean isFinal()
    {
        return this.isFinal;
    }

    public final String className()
    {
        return this.className;
    }

    public final String packageName()
    {
        return this.packageName;
    }

    protected abstract void writeInternal(TextWriter textWriter, String spaces);

    public final void write(File directory, String spaces) throws IOException
    {
        TextWriter textWriter = UtilJava.createJavaFile(directory, this.packageName, this.className);

        if (!this.packageName.isEmpty())
        {
            textWriter.print(JavaConstants.PACKAGE);
            textWriter.print(" ");
            textWriter.print(this.packageName);
            textWriter.println(";");
            textWriter.println();
        }

        for (String toImport : this.imports)
        {
            textWriter.print(JavaConstants.IMPORT);
            textWriter.print(" ");
            textWriter.print(toImport);
            textWriter.println(";");
        }

        if (!this.imports.empty())
        {
            textWriter.println();
        }

        UtilJava.writeComment(textWriter, "", this.comment);

        if (this.isPublic)
        {
            textWriter.print(JavaConstants.PUBLIC);
            textWriter.print(" ");
        }

        if (this.isFinal)
        {
            textWriter.print(JavaConstants.FINAL);
            textWriter.print(" ");
        }

        textWriter.print(this.nature);
        textWriter.print(" ");
        textWriter.println(this.className);

        if (this.parent != null)
        {
            textWriter.print(spaces);
            textWriter.print(JavaConstants.EXTENDS);
            textWriter.print(" ");
            textWriter.println(this.parent);
        }

        if (!this.implementedList.empty())
        {
            textWriter.print(spaces);
            textWriter.print(JavaConstants.IMPLEMENTS);
            textWriter.print(" ");
            boolean first = true;

            for (String implemented : this.implementedList)
            {
                if (!first)
                {
                    textWriter.print(", ");
                }

                first = false;
                textWriter.print(implemented);
            }

            textWriter.println();
        }

        textWriter.println("{");

        this.writeInternal(textWriter, spaces);

        textWriter.println("}");
        textWriter.close();
    }
}

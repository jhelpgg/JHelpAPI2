package jhelp.javawriter;

import jhelp.util.debug.Debug;
import jhelp.util.io.TextWriter;

/**
 * Created by jhelp on 24/06/17.
 */
public class JavaClass extends JavaFile
{
    public JavaClass(String fullName, boolean isPublic, boolean isFinal)
    {
        super(JavaConstants.CLASS, fullName, isPublic, isFinal);
    }

    public JavaClass(String fullName, boolean isPublic, boolean isFinal, String parent)
    {
        super(JavaConstants.CLASS, fullName, isPublic, isFinal, parent);
    }

    protected void writeInternal(TextWriter textWriter, String spaces)
    {
        // TODO Add fields
        Debug.todo("Add fields");

        // TODO Change constructor
        Debug.todo("Change constructor");
        textWriter.print(spaces);

        if (this.isPublic())
        {
            textWriter.print(JavaConstants.PUBLIC);
            textWriter.print(" ");
        }

        textWriter.print(this.className());
        textWriter.println("()");
        textWriter.print(spaces);
        textWriter.println("{");
        textWriter.print(spaces);
        textWriter.println("}");

        // TODO Add methods
        Debug.todo("Add methods");
    }
}

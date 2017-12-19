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

package jhelp.gui.code;

import java.util.regex.Pattern;
import jhelp.gui.JHelpAutoStyledTextArea;

/**
 * Descriptor of Java language.<br>
 * It defines keywords, comments, ...<br>
 * Use {@link JHelpAutoStyledTextArea#describeLanguage(LanguageDescriptor)} to apply it
 */
public class JavaDescriptor extends LanguageDescriptor
{
    /**
     * Create Java descriptor
     */
    public JavaDescriptor()
    {
        this.associate(Rules.COMMENT, Pattern.compile("//.*"), 0);
        this.associate(Rules.COMMENT, Pattern.compile("/\\*([^*]|\\*[^/]|\\n)*\\*/"), 0);
        this.associate(Rules.STRING, Pattern.compile("\".*\""), 0);
        this.associate(Rules.STRING, Pattern.compile("'.*'"), 0);
        this.associate(Rules.PRIMITIVE, "boolean", "char", "byte", "short", "int", "long", "float", "double");
        this.associate(Rules.KEY_WORD, "public", "protected", "private", "abstract", "class", "interface", "enum",
                       "static", "transient", "final", "if", "else", "for", "while", "do", "return", "new", "switch",
                       "case", "break", "continue", "default", "throw", "throws", "try", "catch", "finally", "void",
                       "true", "false", "package", "import", "extends", "implements", "null", "this", "super");

    }
}

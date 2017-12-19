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
 * Descriptor of XML language.<br>
 * It defines keywords, comments, ...<br>
 * Use {@link JHelpAutoStyledTextArea#describeLanguage(LanguageDescriptor)} to apply it
 */
public class XMLDescriptor extends LanguageDescriptor
{
    /**
     * Create the descriptor
     */
    public XMLDescriptor()
    {
        this.associate(Rules.COMMENT, Pattern.compile("<!--([^-]|-[^-]|--[^>]|\\n)*-->/"), 0);
        this.associate(Rules.KEY_WORD, Pattern.compile("</?\\s*([a-zA-Z0-9_][a-zA-Z0-9_.$:]*)(\\s|>)"), 1);
        this.associate(Rules.STRING, Pattern.compile("\".*\""), 0);
        this.associate(Rules.PRIMITIVE,
                       Pattern.compile("<\\s*([a-zA-Z0-9_][a-zA-Z0-9_.$:]*)(\\s*([a-zA-Z0-9_][a-zA-Z0-9_.$:]*)\\s*=)*"),
                       3);
    }
}

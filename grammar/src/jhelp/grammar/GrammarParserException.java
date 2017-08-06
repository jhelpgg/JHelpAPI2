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

import jhelp.util.text.UtilText;

/**
 * Created by jhelp on 23/06/17.
 */
public class GrammarParserException extends Exception
{
    public GrammarParserException(int line, int start, int end, String message)
    {
        super(UtilText.concatenate("Failed to parse the grammar at line ", line, " inside ", start, "<->", end, ": ",
                                   message));
    }

    public GrammarParserException(int line, int start, int end, Throwable cause)
    {
        super(UtilText.concatenate("Failed to parse the grammar at line ", line, " inside ", start, "<->", end), cause);
    }
}

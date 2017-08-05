package jhelp.grammar;

import jhelp.util.text.UtilText;

/**
 * Created by jhelp on 23/06/17.
 */
public class GrammarParserException extends Exception
{
    public GrammarParserException(int line, int start, int end, String  message)
    {
        super(UtilText.concatenate("Failed to parse the grammar at line ",line, " inside ",start,"<->",end, ": ",message));
    }
    public GrammarParserException(int line, int start, int end, Throwable cause)
    {
        super(UtilText.concatenate("Failed to parse the grammar at line ",line, " inside ",start,"<->",end),cause);
    }
}

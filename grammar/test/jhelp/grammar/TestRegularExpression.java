package jhelp.grammar;

import java.util.regex.MatchResult;
import jhelp.util.debug.Debug;
import org.junit.Test;

/**
 * Created by jhelp on 21/06/17.
 */
public class TestRegularExpression
{
    @Test
    public void testMatching()
    {
        RegularExpression regularExpression = new RegularExpression("\".*\"");
        for (MatchResult match : regularExpression.matching("\"test\" \"\" \"z\\\"u\\\"\" pouf"))
        {
            Debug.information("REGULAR: ", match, ":", match.start(), "<->", match.end());
        }

        regularExpression = new RegularExpression("a+");

        for (MatchResult match : regularExpression.matching("a aa aaa aaaa"))
        {
            Debug.information("A: ", match, ":", match.start(), "<->", match.end());
        }

        regularExpression = new RegularExpression("b*");

        for (MatchResult match : regularExpression.matching("bbbbbb a b"))
        {
            Debug.information("B: ", match, ":", match.start(), "<->", match.end());
        }

        for (MatchResult match : regularExpression.matching("ccc"))
        {
            Debug.information("numberOfCombination: ", match, ":", match.start(), "<->", match.end());
        }
    }

    @Test
    public void test()
    {
        GrammarParser grammarParser = new GrammarParser();
        try
        {
            Grammar grammar = grammarParser.parse(Grammar.class.getResourceAsStream("grammar.grammar"));
            Debug.information("\n", grammar.toString());
        }
        catch (GrammarParserException e)
        {
            Debug.exception(e);
        }
    }
}

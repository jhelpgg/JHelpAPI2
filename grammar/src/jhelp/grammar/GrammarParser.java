package jhelp.grammar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jhelp.util.text.StringExtractor;
import jhelp.util.text.UtilText;

/**
 * Created by jhelp on 23/06/17.
 */
public class GrammarParser
{
    private static final Pattern REGULAR_EXPRESSION       = Pattern.compile("\"(.*)\"");
    private static final int     GROUP_REGULAR_EXPRESSION = 1;
    private static final Pattern QUANTITY                 = Pattern.compile("\\{([0-9]+)(\\s*,\\s*([0-9]+))?}");
    private static final int     GROUP_QUANTITY_MINIMUM   = 1;
    private static final int     GROUP_QUANTITY_MAXIMUM   = 3;

    public GrammarParser()
    {
    }

    public Grammar parse(InputStream inputStream) throws GrammarParserException
    {
        int            lineNumber     = 0;
        int            start          = -1;
        int            end            = -1;
        BufferedReader bufferedReader = null;

        try
        {
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            final Grammar   grammar                = new Grammar();
            String          line                   = bufferedReader.readLine();
            String          ruleName               = null;
            RuleDescription currentRuleDescription = null;
            RuleDescription ruleDescription;
            int             index, more;
            String          before;

            while (line != null)
            {
                lineNumber++;
                before = line;
                line = line.trim();
                more = before.indexOf(line);

                if (!line.isEmpty() && !line.startsWith("#"))
                {
                    start = more;
                    end = more + line.length();
                    index = UtilText.indexOfIgnoreString(line, '=');

                    if (index < 0)
                    {
                        if (currentRuleDescription == null)
                        {
                            throw new IllegalArgumentException("No first description");
                        }

                        if (!line.startsWith("|"))
                        {
                            throw new IllegalArgumentException("Not a valid alternative choice, MUST start with |");
                        }

                        if (currentRuleDescription.getDescriptionType() != DescriptionType.ALTERNATIVE)
                        {
                            ruleDescription = new Alternative();
                            ((Alternative) ruleDescription).addAlternative(currentRuleDescription);
                            currentRuleDescription = ruleDescription;
                        }

                        ruleDescription = this.parseRuleDescription(more, 1, line.length(), lineNumber, line);
                        ((Alternative) currentRuleDescription).addAlternative(ruleDescription);
                    }
                    else
                    {
                        if (ruleName != null && currentRuleDescription != null)
                        {
                            grammar.addRule(new Rule(ruleName, currentRuleDescription));
                        }

                        ruleName = line.substring(0, index).trim();

                        if (ruleName.isEmpty())
                        {
                            throw new IllegalArgumentException("Rule name is empty");
                        }

                        currentRuleDescription = this.parseRuleDescription(more, index + 1, line.length(), lineNumber,
                                                                           line);
                    }
                }

                line = bufferedReader.readLine();
                start = -1;
                end = -1;
            }

            if (ruleName != null && currentRuleDescription != null)
            {
                grammar.addRule(new Rule(ruleName, currentRuleDescription));
            }

            return grammar;
        }
        catch (Exception exception)
        {
            throw new GrammarParserException(lineNumber, start, end, exception);
        }
        finally
        {
            if (bufferedReader != null)
            {
                try
                {
                    bufferedReader.close();
                }
                catch (Exception ignored)
                {
                }
            }
        }
    }

    private RuleDescription append(RuleDescription main, RuleDescription toAdd)
    {
        if (toAdd == null)
        {
            return main;
        }

        if (main == null)
        {
            return toAdd;
        }

        if (main.getDescriptionType() == DescriptionType.GROUP)
        {
            ((GroupDescription) main).addElement(toAdd);
            return main;
        }

        GroupDescription groupDescription = new GroupDescription();
        groupDescription.addElement(main);
        groupDescription.addElement(toAdd);
        return groupDescription;
    }

    private RuleDescription parseRuleDescription(int more, int start, int end, int lineNumber, String line)
            throws GrammarParserException
    {
        StringExtractor stringExtractor = new StringExtractor(line.substring(start, end), " \t()*?{},", "'\"", "\\",
                                                              true);
        stringExtractor.setCanReturnEmptyString(false);
        stringExtractor.setStopAtString(false);
        String          word                = stringExtractor.next();
        RuleDescription lastRuleDescription = null;
        RuleDescription ruleDescription     = null;
        Matcher         matcher;
        int             index, minimum, maximum;
        String          group;

        try
        {
            while (word != null)
            {
                word = word.trim();

                if (!word.isEmpty())
                {
                    matcher = GrammarParser.REGULAR_EXPRESSION.matcher(word);

                    if (matcher.matches())
                    {
                        ruleDescription = this.append(ruleDescription, lastRuleDescription);
                        lastRuleDescription = new RegularExpression(
                                matcher.group(GrammarParser.GROUP_REGULAR_EXPRESSION));
                    }
                    else if ("(".equals(word))
                    {
                        ruleDescription = this.append(ruleDescription, lastRuleDescription);
                        index = UtilText.indexOfIgnoreString(line, ')', stringExtractor.currentWordEnd()+start);

                        if (index < 0)
                        {
                            throw new GrammarParserException(lineNumber, stringExtractor.currentWordStart() + more,
                                                             end + more, "Corresponding ) not found");
                        }

                        lastRuleDescription = new GroupDescription();
                        ((GroupDescription) lastRuleDescription).addElement(
                                this.parseRuleDescription(more, stringExtractor.currentWordStart() + 1 + start,
                                                          index,
                                                          lineNumber, line));
                        stringExtractor = new StringExtractor(
                                line.substring(index + 1, Math.min(end + start, line.length())),
                                " \t()*?{},", "'\"", "\\",
                                true);
                        start = index + 1;
                    }
                    else if ("{".equals(word))
                    {
                        if (lastRuleDescription == null)
                        {
                            throw new IllegalArgumentException("No rule to quantify!");
                        }

                        index = UtilText.indexOfIgnoreString(line, '}', stringExtractor.currentWordEnd()+start);

                        if (index < 0)
                        {
                            throw new GrammarParserException(lineNumber, stringExtractor.currentWordStart() + more,
                                                             end + more, "Corresponding } not found");
                        }

                        matcher = GrammarParser.QUANTITY.matcher(
                                line.substring(stringExtractor.currentWordStart(), index + 1));

                        if (matcher.matches())
                        {
                            minimum = Integer.parseInt(matcher.group(GrammarParser.GROUP_QUANTITY_MINIMUM));
                            maximum = minimum;

                            if (matcher.groupCount() > GrammarParser.GROUP_QUANTITY_MAXIMUM)
                            {
                                group = matcher.group(GrammarParser.GROUP_QUANTITY_MAXIMUM);

                                if (group != null && !group.isEmpty())
                                {
                                    maximum = Integer.parseInt(group);
                                }
                            }

                            if (minimum == maximum)
                            {
                                lastRuleDescription = QuantifiedDescription.exactly(lastRuleDescription, minimum);
                            }
                            else
                            {
                                lastRuleDescription = QuantifiedDescription.between(lastRuleDescription, minimum,
                                                                                    maximum);
                            }

                            stringExtractor = new StringExtractor(
                                    line.substring(index + 1, Math.min(end + start, line.length())),
                                    " \t()*?{},", "'\"",
                                    "\\",
                                    true);
                            start = index + 1;
                        }
                        else
                        {
                            throw new GrammarParserException(lineNumber, stringExtractor.currentWordStart() + more,
                                                             index + more, "Invalid quantity specification");
                        }
                    }
                    else if ("*".equals(word))
                    {
                        if (lastRuleDescription == null)
                        {
                            throw new IllegalArgumentException("No rule to quantify!");
                        }

                        lastRuleDescription = QuantifiedDescription.anyNumber(lastRuleDescription);
                    }
                    else if ("?".equals(word))
                    {
                        if (lastRuleDescription == null)
                        {
                            throw new IllegalArgumentException("No rule to quantify!");
                        }

                        lastRuleDescription = QuantifiedDescription.zeroOrOne(lastRuleDescription);
                    }
                    else if ("+".equals(word))
                    {
                        if (lastRuleDescription == null)
                        {
                            throw new IllegalArgumentException("No rule to quantify!");
                        }

                        lastRuleDescription = QuantifiedDescription.atLeastOne(lastRuleDescription);
                    }
                    else
                    {
                        ruleDescription = this.append(ruleDescription, lastRuleDescription);
                        lastRuleDescription = new RuleReference(word);
                    }
                }

                word = stringExtractor.next();
            }
        }
        catch (GrammarParserException exception)
        {
            throw exception;
        }
        catch (Exception exception)
        {
            throw new GrammarParserException(lineNumber, start + more, end + more, exception);
        }

        ruleDescription = this.append(ruleDescription, lastRuleDescription);

        if (ruleDescription == null)
        {
            throw new GrammarParserException(lineNumber, start + more, end + more, "Can't parse find a valid rule!");
        }

        return ruleDescription;
    }
}

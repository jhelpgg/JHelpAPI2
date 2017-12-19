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

package jhelp.bot;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jhelp.util.list.ArrayObject;
import jhelp.util.text.regex.RegularExpression;
import jhelp.util.util.Utilities;

public final class ListInstruction implements BotInstruction
{
    static
    {
        RegularExpression regularExpression = new RegularExpression();
        regularExpression.appendString("L:");
        regularExpression.appendRegularExpression(RegularExpression.SPACE);
        //List name capture
        regularExpression.appendCapture(RegularExpression.WORD);
        regularExpression.appendRegularExpression(RegularExpression.SPACE);
        regularExpression.appendString("\\{");
        regularExpression.appendRegularExpression(RegularExpression.SPACE);
        {
            RegularExpression listElement = new RegularExpression();
            listElement.appendRegularExpression(RegularExpression.STRING);
            listElement.appendRegularExpression(RegularExpression.SPACE);

            RegularExpression list = new RegularExpression();
            list.appendAny(listElement);

            //List content capture
            regularExpression.appendCapture(list);
        }
        regularExpression.appendString("\\}");

        REGEX = regularExpression.pattern();
        GROUP_NAME = regularExpression.groupOfCapture(0);
        GROUP_LIST = regularExpression.groupOfCapture(1);
    }

    public static final int     GROUP_LIST;
    public static final int     GROUP_NAME;
    public static final Pattern REGEX;

    public static ListInstruction parse(final Matcher matcher)
    {
        if (!ListInstruction.REGEX.equals(matcher.pattern()))
        {
            throw new IllegalArgumentException("Matcher not from pattern for ListInstruction");
        }

        String              name    = matcher.group(ListInstruction.GROUP_NAME);
        String              content = matcher.group(ListInstruction.GROUP_LIST);
        ArrayObject<String> list    = new ArrayObject<>();

        if (content != null && content.length() > 0)
        {
            Matcher elements = RegularExpression.STRING.pattern().matcher(content);
            String  element;

            while (elements.find())
            {
                element = elements.group();
                list.add(element.substring(1, element.length() - 1));
            }
        }

        return new ListInstruction(name, list.toArray(new String[list.size()]));
    }

    private final String[] list;
    private final String   name;

    public ListInstruction(String name, String... list)
    {
        Objects.requireNonNull(name, "name MUST NOT be null!");
        Objects.requireNonNull(list, "list MUST NOT be null!");

        for (String element : list)
        {
            if (element == null)
            {
                throw new NullPointerException("One of list element is null!");
            }
        }

        this.name = name;
        this.list = Utilities.createCopy(list);
    }

    public String get(int index)
    {
        return this.list[index];
    }

    @Override
    public InstructionType instructionType()
    {
        return InstructionType.LIST;
    }

    @Override
    public Pattern regex()
    {
        return ListInstruction.REGEX;
    }

    public String name()
    {
        return this.name;
    }

    public int size()
    {
        return this.list.length;
    }
}

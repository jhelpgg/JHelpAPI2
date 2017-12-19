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
import java.util.Objects;
import jhelp.util.io.ByteArray;

/**
 * Created by jhelp on 21/06/17.
 */
public abstract class RuleDescription
{
    public static @NotNull RuleDescription parse(@NotNull ByteArray byteArray) throws Exception
    {
        DescriptionType descriptionType = byteArray.readEnum(DescriptionType.class);
        RuleDescription ruleDescription;
        int             minimum, maximum;

        switch (descriptionType)
        {
            case ALTERNATIVE:
                ruleDescription = new Alternative();
                RuleDescription.parseAndFill(byteArray, (Alternative) ruleDescription);
                break;
            case GROUP:
                ruleDescription = new GroupDescription();
                RuleDescription.parseAndFill(byteArray, (GroupDescription) ruleDescription);
                break;
            case QUANTIFIED:
                minimum = byteArray.readInteger();
                maximum = byteArray.readInteger();
                ruleDescription = RuleDescription.parse(byteArray);
                ruleDescription = QuantifiedDescription.between(ruleDescription, minimum, maximum);
                break;
            case REFERENCE:
                ruleDescription = new RuleReference(byteArray.readString());
                break;
            case REGULAR_EXPRESSION:
                ruleDescription = new RegularExpression(byteArray.readString());
                break;
            default:
                throw new IllegalArgumentException("Unknown descriptionType=" + descriptionType);
        }

        return ruleDescription;
    }

    private static void parseAndFill(@NotNull ByteArray byteArray, @NotNull Alternative alternative) throws Exception
    {
        int size = byteArray.readInteger();

        for (int i = 0; i < size; i++)
        {
            alternative.addAlternative(RuleDescription.parse(byteArray));
        }
    }

    private static void parseAndFill(@NotNull ByteArray byteArray, @NotNull GroupDescription groupDescription)
            throws Exception
    {
        int size = byteArray.readInteger();

        for (int i = 0; i < size; i++)
        {
            groupDescription.addElement(RuleDescription.parse(byteArray));
        }
    }

    private final DescriptionType descriptionType;

    public RuleDescription(@NotNull DescriptionType descriptionType)
    {
        Objects.requireNonNull(descriptionType, "descriptionType MUST NOT be null!");
        this.descriptionType = descriptionType;
    }

    public final @NotNull DescriptionType getDescriptionType()
    {
        return this.descriptionType;
    }

    public final void serialize(@NotNull ByteArray byteArray)
    {
        byteArray.writeEnum(this.descriptionType);

        switch (this.descriptionType)
        {
            case ALTERNATIVE:
                Alternative alternative = (Alternative) this;
                byteArray.writeInteger(alternative.numberOfAlternative());

                for (RuleDescription ruleDescription : alternative)
                {
                    ruleDescription.serialize(byteArray);
                }
                break;
            case GROUP:
                GroupDescription groupDescription = (GroupDescription) this;
                byteArray.writeInteger(groupDescription.numberOfElement());

                for (RuleDescription ruleDescription : groupDescription)
                {
                    ruleDescription.serialize(byteArray);
                }
                break;
            case QUANTIFIED:
                QuantifiedDescription quantifiedDescription = (QuantifiedDescription) this;
                byteArray.writeInteger(quantifiedDescription.getMinimum());
                byteArray.writeInteger(quantifiedDescription.getMaximum());
                quantifiedDescription.getQuantifiedRule().serialize(byteArray);
                break;
            case REFERENCE:
                byteArray.writeString(((RuleReference) this).getRuleName());
                break;
            case REGULAR_EXPRESSION:
                byteArray.writeString(((RegularExpression) this).getPattern());
                break;
        }
    }
}

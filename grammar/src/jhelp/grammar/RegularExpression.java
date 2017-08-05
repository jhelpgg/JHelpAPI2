package jhelp.grammar;

import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jhelp on 21/06/17.
 */
public class RegularExpression extends RuleDescription
{
    private final Pattern regularExpression;

    public RegularExpression(@NotNull String regularExpression)
    {
        super(DescriptionType.REGULAR_EXPRESSION);
        Objects.requireNonNull(regularExpression, "regularExpression MUST NOT be null!");
        this.regularExpression = Pattern.compile(regularExpression);
    }

    public @NotNull List<MatchResult> matching(@NotNull String string)
    {
        Objects.requireNonNull(string, "string MUST NOT be null!");
        List<MatchResult> matches = new ArrayList<>();
        Matcher           matcher = this.regularExpression.matcher(string);

        while (matcher.find())
        {
            matches.add(matcher.toMatchResult());
        }

        return Collections.unmodifiableList(matches);
    }

    public String getPattern()
    {
        return this.regularExpression.pattern();
    }

    @Override
    public String toString()
    {
        return "\"" + this.regularExpression.pattern() + "\"";
    }
}

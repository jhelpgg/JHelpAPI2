package jhelp.util.filter;

import org.junit.Test;

public class TestRegexFileFilter
{
    @Test
    public void test()
    {
        RegexFileFilter regexFileFilter = new RegexFileFilter(".*a+b+.*");
    }
}

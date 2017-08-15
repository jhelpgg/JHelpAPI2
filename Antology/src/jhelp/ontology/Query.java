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

package jhelp.ontology;

public class Query
{
    private final QueryElement information;
    private final QueryElement predicate;
    private final QueryElement subject;

    public Query(final QueryElement subject, final QueryElement predicate, final QueryElement information)
    {
        this.subject = subject;
        this.predicate = predicate;
        this.information = information;
    }

    public QueryElement information()
    {
        return this.information;
    }

    public QueryElement predicate()
    {
        return this.predicate;
    }

    public QueryElement subject()
    {
        return this.subject;
    }
}

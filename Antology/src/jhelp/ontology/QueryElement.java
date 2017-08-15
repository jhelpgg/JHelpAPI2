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

import java.util.Objects;
import jhelp.util.io.Binarizable;

public class QueryElement
{
    public static <B extends Binarizable> QueryElement newBinarizable(B binarizable)
    {
        Objects.requireNonNull(binarizable, "binarizable MUST NOT be null!");
        return new QueryElement(QueryElementType.BINARIZABLE, binarizable);
    }

    public static QueryElement newDouble(double number)
    {
        return new QueryElement(QueryElementType.DOUBLE, number);
    }

    public static QueryElement newInt(int integer)
    {
        return new QueryElement(QueryElementType.INT, integer);
    }

    public static QueryElement newQuery(Query query)
    {
        Objects.requireNonNull(query, "query MUST NOT be null!");
        return new QueryElement(QueryElementType.QUERY, query);
    }

    public static QueryElement newString(String string)
    {
        Objects.requireNonNull(string, "string MUST NOT be null!");
        return new QueryElement(QueryElementType.STRING, string);
    }

    public static QueryElement newTriplet(Triplet triplet)
    {
        Objects.requireNonNull(triplet, "triplet MUST NOT be null!");
        return new QueryElement(QueryElementType.TRIPLET, triplet);
    }

    public static QueryElement newVariable(String name)
    {
        Objects.requireNonNull(name, "name MUST NOT be null!");
        return new QueryElement(QueryElementType.VARIABLE, name);
    }

    private final QueryElementType type;
    private final Object           value;

    private QueryElement(final QueryElementType type, final Object value)
    {
        this.type = type;
        this.value = value;
    }

    private void checkType(QueryElementType type)
    {
        if (this.type != type)
        {
            throw new IllegalStateException("Element is type " + this.type + " not " + type);
        }
    }

    public <B extends Binarizable> B binarizable()
    {
        this.checkType(QueryElementType.BINARIZABLE);
        return (B) this.value;
    }

    public int integer()
    {
        this.checkType(QueryElementType.INT);
        return (Integer) this.value;
    }

    public double number()
    {
        this.checkType(QueryElementType.DOUBLE);
        return (Double) this.value;
    }

    public Query query()
    {
        this.checkType(QueryElementType.QUERY);
        return (Query) this.value;
    }

    public String string()
    {
        this.checkType(QueryElementType.STRING);
        return (String) this.value;
    }

    public Triplet triplet()
    {
        this.checkType(QueryElementType.TRIPLET);
        return (Triplet) this.value;
    }

    public QueryElementType type()
    {
        return this.type;
    }

    public String variableName()
    {
        this.checkType(QueryElementType.VARIABLE);
        return (String) this.value;
    }
}

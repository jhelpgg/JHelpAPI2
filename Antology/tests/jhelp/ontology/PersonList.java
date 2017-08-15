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

import java.util.List;
import jhelp.util.list.ArrayObject;

public class PersonList
{
    @ResultField("subject")
    private final List<Person> list;

    public PersonList()
    {
        this.list = new ArrayObject<>();
    }

    public List<Person> list()
    {
        return this.list;
    }
}

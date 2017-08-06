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

package jhelp.util.io.json;

/**
 * {@link JSONList} element.<br>
 * Public to be able to serialize it with {@link JSONWriter} or parse it with {@link JSONReader}
 * .<br>
 * It is useless to instantiate it outside the {@link JSONList} context.<br>
 *
 * @param <TYPE> Carried element type
 */
@JSONObject
public class Element<TYPE>
{
    /**
     * Element value
     */
    @JSONElement
    TYPE          element;
    /**
     * Next element
     */
    @JSONElement
    Element<TYPE> next;

    /**
     * Create an element.<br>
     * Public for  {@link JSONWriter} or  {@link JSONReader}
     */
    public Element()
    {
    }
}
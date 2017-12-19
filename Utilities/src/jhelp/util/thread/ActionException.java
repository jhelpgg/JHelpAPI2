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

package jhelp.util.thread;

import jhelp.util.text.UtilText;

public class ActionException extends Exception
{
    public ActionException(Object... message)
    {
        super(UtilText.concatenate(message));
    }

    public ActionException(Throwable cause, Object... message)
    {
        super(UtilText.concatenate(message), cause);
    }
}

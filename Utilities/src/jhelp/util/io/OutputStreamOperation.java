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

package jhelp.util.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by jhelp on 24/07/17.
 */
public interface OutputStreamOperation<O extends OutputStream>
{
    void operate(O outputStream) throws IOException;
}

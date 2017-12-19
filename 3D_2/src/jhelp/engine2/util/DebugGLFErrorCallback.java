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

package jhelp.engine2.util;

import java.util.Map;
import jhelp.util.debug.Debug;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWErrorCallbackI;
import org.lwjgl.system.APIUtil;

/**
 * Tool for write GLF (Window manager we used from LWJGL) error in console
 */
public class DebugGLFErrorCallback implements GLFWErrorCallbackI
{
    /**
     * Map of error codes and their name
     */
    private static final Map<Integer, String>  ERROR_CODES              =
            APIUtil.apiClassTokens((field, value) -> 0x10000 < value && value < 0x20000, null, GLFW.class);
    /**
     * Tool singleton instance
     */
    public static final  DebugGLFErrorCallback DEBUG_GLF_ERROR_CALLBACK = new DebugGLFErrorCallback();

    /**
     * Create the instance
     */
    private DebugGLFErrorCallback()
    {
    }

    /**
     * Will be called with an error code and a human-readable description when a GLFW error occurs.
     *
     * @param error       the error code
     * @param description a pointer to a UTF-8 encoded string describing the error
     */
    @Override
    public void invoke(final int error, final long description)
    {
        final String errorType = DebugGLFErrorCallback.ERROR_CODES.get(error);
        final String message   = GLFWErrorCallback.getDescription(description);
        Debug.error("GLF -", errorType, "- ", message);
    }
}

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

package jhelp.util.samples.resources;

import java.io.IOException;
import java.util.Stack;
import jhelp.util.debug.Debug;
import jhelp.util.resources.ResourceElement;
import jhelp.util.resources.Resources;
import jhelp.util.resources.ResourcesSystem;

public class ResourcesSystemSample
{

    /**
     * @param args
     */
    public static void main(final String[] args)
    {
        final Resources resources = new Resources(ResourcesSystemSample.class);
        try
        {
            final ResourcesSystem        resourcesSystem = resources.obtainResourcesSystem();
            final Stack<ResourceElement> stack           = new Stack<ResourceElement>();
            stack.push(ResourcesSystem.ROOT);
            ResourceElement resourceElement;

            while (stack.empty() == false)
            {
                resourceElement = stack.pop();
                Debug.mark(resourceElement.getPath());

                for (final ResourceElement element : resourcesSystem.obtainList(resourceElement))
                {
                    if (element.isDirectory() == true)
                    {
                        stack.push(element);
                    }
                    else
                    {
                        Debug.information(element.getPath());
                    }
                }
            }
        }
        catch (final IOException exception)
        {
            Debug.exception(exception);
        }
    }
}
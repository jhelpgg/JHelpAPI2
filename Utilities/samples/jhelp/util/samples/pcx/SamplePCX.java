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

package jhelp.util.samples.pcx;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.io.InputStream;
import javax.swing.JFrame;
import jhelp.util.debug.Debug;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.UtilGUI;
import jhelp.util.image.pcx.PCX;
import jhelp.util.resources.ResourceElement;
import jhelp.util.resources.ResourceFile;
import jhelp.util.resources.Resources;
import jhelp.util.resources.ResourcesSystem;
import jhelp.util.samples.common.gui.SampleLabelJHelpImage;
import jhelp.util.text.UtilText;

public class SamplePCX
{
    /**
     * @param args
     */
    public static void main(final String[] args)
    {
        InputStream inputStream = null;

        try
        {
            final Resources       resources       = new Resources(SamplePCX.class);
            final ResourcesSystem resourcesSystem = resources.obtainResourcesSystem();
            final Rectangle       bounds          = UtilGUI.getScreenBounds(0);
            int                   x               = bounds.x;
            int                   y               = bounds.y;
            final int             xx              = x + bounds.width;
            final int             yy              = y + bounds.height;
            int                   jump            = 0;
            int                   width, height;

            for (final ResourceElement resourceElement : resourcesSystem.obtainList(ResourcesSystem.ROOT))
            {
                if ((resourceElement.isDirectory() == false) &&
                    (resourceElement.getName().toLowerCase().endsWith(".pcx") == true))
                {
                    inputStream = resourcesSystem.obtainInputStream((ResourceFile) resourceElement);
                    final PCX pcx = new PCX(inputStream);
                    inputStream.close();
                    inputStream = null;
                    Debug.information(pcx);
                    final JHelpImage            image           = pcx.createImage();
                    final SampleLabelJHelpImage labelJHelpImage = new SampleLabelJHelpImage(image);
                    final JFrame frame = new JFrame(
                            UtilText.concatenate(resourceElement.getName(), " : ", image.getWidth(), 'x',
                                                 image.getHeight()));
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setLayout(new BorderLayout());
                    frame.add(labelJHelpImage, BorderLayout.CENTER);
                    frame.pack();
                    width = frame.getWidth();
                    height = frame.getHeight();

                    if ((x + width) > xx)
                    {
                        x = bounds.x;

                        if ((y + jump) > yy)
                        {
                            x += 16;
                            bounds.x += 16;
                            bounds.y += 16;
                            y = bounds.y;
                        }
                        else
                        {
                            y += jump;
                        }

                        jump = 0;
                    }

                    frame.setLocation(x, y);
                    x += width;
                    jump = Math.max(jump, height);

                    frame.setVisible(true);
                }
            }
        }
        catch (final Exception exception)
        {
            Debug.exception(exception, "Failed to load PCX image");
        }
        finally
        {
            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch (final Exception exception)
                {
                }
            }
        }
    }
}
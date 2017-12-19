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

/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.gui;

import java.awt.Graphics;

/**
 * Something that can be draw over the image of {@link LabelJHelpImage}
 *
 * @author JHelp
 */
public interface OverLabelJHelpImage
{
    /**
     * Draw the object over the image
     *
     * @param g
     *           Graphics context
     * @param width
     *           Area width
     * @param height
     *           Area height
     */
    void draw(Graphics g, int width, int height);
}
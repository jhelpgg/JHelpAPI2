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

package jhelp.samples;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import jhelp.util.gui.JHelpImage;

/**
 * Created by jhelp on 14/07/17.
 */
public class ComponentImage extends JComponent
{
    private boolean    expand;
    private JHelpImage image;

    public ComponentImage(final JHelpImage image)
    {
        this.image = image;
        this.expand = false;
        Dimension dimension = new Dimension(this.image.getWidth(), this.image.getHeight());
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);
        this.setMinimumSize(dimension);
    }

    @Override
    protected void paintComponent(final Graphics graphics)
    {
        if (this.expand)
        {
            graphics.drawImage(this.image.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
        }
        else
        {
            int x = (this.getWidth() - this.image.getWidth()) >> 1;
            int y = (this.getHeight() - this.image.getHeight()) >> 1;
            graphics.drawImage(this.image.getImage(), x, y, this);
        }
    }

    public void expand(boolean expand)
    {
        this.expand = expand;
    }

    public boolean expand()
    {
        return this.expand;
    }

    public void image(JHelpImage image)
    {
        this.image.unregister(this);

        this.image = image;
        Dimension dimension = new Dimension(this.image.getWidth(), this.image.getHeight());
        this.setSize(dimension);
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);
        this.setMinimumSize(dimension);
        this.invalidate();
        this.revalidate();
        this.repaint();
    }
}

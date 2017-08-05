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
    private JHelpImage image;
    private boolean    expand;

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

    public void expand(boolean expand)
    {
        this.expand = expand;
    }

    public boolean expand()
    {
        return this.expand;
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
}

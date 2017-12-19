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
package jhelp.gui.twoD;

import java.awt.Dimension;
import jhelp.util.gui.JHelpImage;

/**
 * Check box for 2D components
 *
 * @author JHelp
 */
public class JHelpCheckbox2D
        extends JHelpComponent2D
        implements JHelpActionListener
{
    /**
     * Box border default color
     */
    public static final int COLOR_BOX   = 0xFF000000;
    /**
     * Check cross default color
     */
    public static final int COLOR_CHECK = 0xFFFF0000;
    /**
     * Check box default size
     */
    public static final int SIZE        = 16;
    /**
     * Listener of check box events
     */
    private CheckBox2DListener checkBox2DListener;
    /**
     * Indicates if box is checked
     */
    private boolean            checked;
    /**
     * Border box color
     */
    private int                colorBox;
    /**
     * Check cross color
     */
    private int                colorCheck;
    /**
     * Check box size
     */
    private int                size;

    /**
     * Create a new instance of JHelpCheckbox2D.<br>
     * Not checked, with default colors and size
     */
    public JHelpCheckbox2D()
    {
        this(JHelpCheckbox2D.COLOR_BOX, JHelpCheckbox2D.COLOR_CHECK, false, JHelpCheckbox2D.SIZE);
    }

    /**
     * Create a new instance of JHelpCheckbox2D.<br>
     * With default colors and size
     *
     * @param checked Indicates if start check
     */
    public JHelpCheckbox2D(final boolean checked)
    {
        this(JHelpCheckbox2D.COLOR_BOX, JHelpCheckbox2D.COLOR_CHECK, checked, JHelpCheckbox2D.SIZE);
    }

    /**
     * Create a new instance of JHelpCheckbox2D.<br>
     * With default colors
     *
     * @param checked Indicates if start checked
     * @param size    Check box size
     */
    public JHelpCheckbox2D(final boolean checked, final int size)
    {
        this(JHelpCheckbox2D.COLOR_BOX, JHelpCheckbox2D.COLOR_CHECK, checked, size);
    }

    /**
     * Create a new instance of JHelpCheckbox2D.<br>
     * Not checked, with default size
     *
     * @param colorBox   Border color
     * @param colorCheck Cross color
     */
    public JHelpCheckbox2D(final int colorBox, final int colorCheck)
    {
        this(colorBox, colorCheck, false, JHelpCheckbox2D.SIZE);
    }

    /**
     * Create a new instance of JHelpCheckbox2D.<br>
     * With default size
     *
     * @param colorBox   Border color
     * @param colorCheck Cross color
     * @param checked    Indicates if start check
     */
    public JHelpCheckbox2D(final int colorBox, final int colorCheck, final boolean checked)
    {
        this(colorBox, colorCheck, checked, JHelpCheckbox2D.SIZE);
    }

    /**
     * Create a new instance of JHelpCheckbox2D
     *
     * @param colorBox   Border color
     * @param colorCheck Cross color
     * @param checked    Indicates if start check
     * @param size       Check box size
     */
    public JHelpCheckbox2D(final int colorBox, final int colorCheck, final boolean checked, final int size)
    {
        this.colorBox(colorBox);
        this.colorCheck(colorCheck);
        this.checked(checked);
        this.size(size);
        JHelpButtonBehavior.giveButtonBehavior(42, this, this);
    }

    /**
     * Compute checkbox preferred size <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param parentWidth  Parent width
     * @param parentHeight Parent height
     * @return Preferred size
     * @see jhelp.gui.twoD.JHelpComponent2D#computePreferredSize(int, int)
     */
    @Override
    protected Dimension computePreferredSize(final int parentWidth, final int parentHeight)
    {
        return new Dimension(this.size, this.size);
    }

    /**
     * Draw the checkbox <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param x      X position
     * @param y      Y position
     * @param parent Image where draw
     * @see jhelp.gui.twoD.JHelpComponent2D#paint(int, int, JHelpImage)
     */
    @Override
    protected void paint(final int x, final int y, final JHelpImage parent)
    {
        parent.drawRectangle(x, y, this.size, this.size, this.colorBox);

        if (this.checked)
        {
            parent.drawLine(x, y, x + this.size, y + this.size, this.colorCheck);
            parent.drawLine(x, y + this.size, x + this.size, y, this.colorCheck);
        }
    }

    /**
     * Called when user click on the checkbox <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param component2d Component clicked
     * @param identifier  Action identifier
     * @see jhelp.gui.twoD.JHelpActionListener#actionAppend(jhelp.gui.twoD.JHelpComponent2D, int)
     */
    @Override
    public void actionAppend(final JHelpComponent2D component2d, final int identifier)
    {
        this.checked = !this.checked;

        if (this.checkBox2DListener != null)
        {
            this.checkBox2DListener.checkBoxCheckChange(this, this.checked);
        }

        this.invalidate();
    }

    /**
     * Current checkbox check change listener
     *
     * @return Current checkbox check change listener
     */
    public CheckBox2DListener checkBox2DListener()
    {
        return this.checkBox2DListener;
    }

    /**
     * Change/define the checkbox action listener
     *
     * @param checkBox2DListener New listener
     */
    public void checkBox2DListener(final CheckBox2DListener checkBox2DListener)
    {
        this.checkBox2DListener = checkBox2DListener;
    }

    /**
     * Indicates if checkbox is checked
     *
     * @return {@code true} if checkbox is checked
     */
    public boolean checked()
    {
        return this.checked;
    }

    /**
     * Change check state
     *
     * @param checked New check state
     */
    public void checked(final boolean checked)
    {
        this.checked = checked;
        this.invalidate();
    }

    /**
     * Border color
     *
     * @return Border color
     */
    public int colorBox()
    {
        return this.colorBox;
    }

    /**
     * Change border clolr
     *
     * @param colorBox New border color
     */
    public void colorBox(final int colorBox)
    {
        this.colorBox = colorBox;
        this.invalidate();
    }

    /**
     * Cross color
     *
     * @return Cross color
     */
    public int colorCheck()
    {
        return this.colorCheck;
    }

    /**
     * Change cross color
     *
     * @param colorCheck New cross color
     */
    public void colorCheck(final int colorCheck)
    {
        this.colorCheck = colorCheck;
        this.invalidate();
    }

    /**
     * Checkbox size
     *
     * @return Checkbox size
     */
    public int size()
    {
        return this.size;
    }

    /**
     * Change checkbox size
     *
     * @param size New checkbox size
     */
    public void size(final int size)
    {
        this.size = Math.max(size, JHelpCheckbox2D.SIZE);
        this.invalidate();
    }
}
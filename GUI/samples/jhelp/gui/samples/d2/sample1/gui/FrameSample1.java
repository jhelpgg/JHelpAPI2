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
package jhelp.gui.samples.d2.sample1.gui;

import jhelp.gui.twoD.FoldingAreaTitle;
import jhelp.gui.twoD.JHelpBorderLayout;
import jhelp.gui.twoD.JHelpBorderLayout.JHelpBorderLayoutConstraints;
import jhelp.gui.twoD.JHelpFoldable2D;
import jhelp.gui.twoD.JHelpFoldable2D.FoldingAreaPosition;
import jhelp.gui.twoD.JHelpFrame2D;
import jhelp.gui.twoD.JHelpLabelImage2D;
import jhelp.gui.twoD.JHelpPanel2D;
import jhelp.gui.twoD.JHelpScrollPane2D;
import jhelp.gui.twoD.JHelpTableLayout;
import jhelp.gui.twoD.JHelpTableLayout.JHelpTableLayoutConstraints;
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpTextAlign;
import jhelp.util.text.UtilText;

/**
 * The frame of first sample
 *
 * @author JHelp
 */
public class FrameSample1
        extends JHelpFrame2D
{
    /** Font used by text */
    private static final JHelpFont FONT = new JHelpFont("Arial", 14);
    /** Font used by fold panel title */
    private static final JHelpFont FONT_FOLD = new JHelpFont("Arial", 14, true);

    /**
     * Create a new instance of FrameSample1
     */
    public FrameSample1()
    {
        super("2D sample 1", new JHelpBorderLayout());

        this.createTop();
        this.createLeft();
        this.createCenter();
        this.createRight();
        this.createBottom();

        // Try remove comment bellow :)
        // this.addComponent2D(JHelpLabelImage2D.createTextLabel("Bottom\nLeft", FrameSample1.FONT, 0xFF000000, 0xFFFFFFFF,
        // JHelpTextAlign.CENTER), JHelpBorderLayoutConstraints.BOTTOM_LEFT);
    }

    /**
     * Create bottom component
     */
    private void createBottom()
    {
        this.addComponent2D(this.createFoldable("Bottom", "bottom", true, FoldingAreaPosition.TOP),
                            JHelpBorderLayoutConstraints.BOTTOM);
    }

    /**
     * Create center component
     */
    private void createCenter()
    {
        final JHelpPanel2D panel2d = new JHelpPanel2D(new JHelpTableLayout());

        panel2d.addComponent2D(
                JHelpLabelImage2D.createTextLabel("(0, 0)\n3 x 2", FrameSample1.FONT, 0xFF000000, 0xFFFFFFFF,
                                                  JHelpTextAlign.CENTER),
                new JHelpTableLayoutConstraints(0, 0, 3, 2));
        panel2d.addComponent2D(
                JHelpLabelImage2D.createTextLabel("(1, 2)\n4 x 1", FrameSample1.FONT, 0xFF000000, 0xFFFFFFFF,
                                                  JHelpTextAlign.CENTER),
                new JHelpTableLayoutConstraints(1, 2, 4, 1));
        panel2d.addComponent2D(
                JHelpLabelImage2D.createTextLabel("Drag mouse for scroll\n(0, 3)\n1 x 1", FrameSample1.FONT, 0xFF000000,
                                                  0xFFFFFFFF, JHelpTextAlign.CENTER),
                new JHelpTableLayoutConstraints(0, 3, 1, 1));
        panel2d.addComponent2D(new JHelpLabelImage2D(new JHelpImage(500, 500, 0xFFFF0000)),
                               new JHelpTableLayoutConstraints(0, 4, 10, 10));
        panel2d.addComponent2D(new JHelpLabelImage2D(new JHelpImage(1000, 1000, 0xFF0000FF)),
                               new JHelpTableLayoutConstraints(1, 5, 20, 20));

        final JHelpScrollPane2D scrollPane2D = new JHelpScrollPane2D(panel2d);

        this.addComponent2D(scrollPane2D, JHelpBorderLayoutConstraints.CENTER);
    }

    /**
     * Create a fold panel
     *
     * @param title
     *           Title
     * @param nature
     *           Nature of the panel
     * @param horizontal
     *           Indicate is panel is horizontal ({@code true}) or vertical ({@code false})
     * @param foldingAreaPosition
     *           Position of click zone
     * @return Create fold panel
     */
    private JHelpFoldable2D createFoldable(
            final String title, final String nature, final boolean horizontal,
            final FoldingAreaPosition foldingAreaPosition)
    {
        return new JHelpFoldable2D(JHelpLabelImage2D.createTextLabel(
                UtilText.replaceHole(
                        "This is the {0} of the border layout.\nThis text can be hide or show\nby click on yellow bar",
                        nature), FrameSample1.FONT,
                0xFF000000, 0xFFFFFFFF, JHelpTextAlign.CENTER), new FoldingAreaTitle(0xFF0000FF, horizontal
                                                                                                 ? JHelpFoldable2D.DEFAULT_PAINT_HORIZONTAL
                                                                                                 : JHelpFoldable2D.DEFAULT_PAINT_VERTICAL,
                                                                                     title, FrameSample1.FONT_FOLD,
                                                                                     0xFF000000, null),
                                   foldingAreaPosition);
    }

    /**
     * Create left component
     */
    private void createLeft()
    {
        this.addComponent2D(this.createFoldable("Left", "left", false, FoldingAreaPosition.RIGHT),
                            JHelpBorderLayoutConstraints.LEFT);
    }

    /**
     * Create right component
     */
    private void createRight()
    {
        // Try on replace RIGHT_EXPANDED by RIGHT to see the difference
        this.addComponent2D(this.createFoldable("Right", "right", false, FoldingAreaPosition.LEFT),
                            JHelpBorderLayoutConstraints.RIGHT_EXPANDED);
    }

    /**
     * Create top component
     */
    private void createTop()
    {
        this.addComponent2D(this.createFoldable("Top", "top", true, FoldingAreaPosition.BOTTOM),
                            JHelpBorderLayoutConstraints.TOP);
    }
}
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
package jhelp.gui.smooth;

import java.awt.Dimension;
import java.awt.Rectangle;
import jhelp.gui.smooth.layout.JHelpConstraintsSmooth;
import jhelp.gui.smooth.layout.JHelpLayoutSmooth;
import jhelp.gui.smooth.shape.SmoothRoundRectangle;
import jhelp.util.gui.JHelpImage;
import jhelp.util.math.Math2;

/**
 * Dialog smooth.<br>
 * Dialogs are created with {@link DialogDescription} in {@link JHelpFrameSmooth#createDialog(int)}
 *
 * @author JHelp
 */
class JHelpDialogSmooth
{
    /**
     * Animation time in milliseconds
     */
    private static final long                   ANIMATION_TIME = 2048L;
    /**
     * Constriants special dialog
     */
    private static final JHelpConstraintsSmooth CONSTRAINTS    = new JHelpConstraintsSmooth()
    {
    };
    /**
     * Additional pixels to size
     */
    static final         int                    MORE_SIZE      = 64;

    /**
     * Special layout for dialogs
     *
     * @author JHelp
     */
    class DialogLayout
            implements JHelpLayoutSmooth
    {
        /**
         * Create a new instance of DialogLayout
         */
        DialogLayout()
        {
        }

        /**
         * Indicates if constraints are accepted <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param contraints Tested constraints
         * @return {@code true}
         * @see JHelpLayoutSmooth#acceptConstraints(JHelpConstraintsSmooth)
         */
        @Override
        public boolean acceptConstraints(final JHelpConstraintsSmooth contraints)
        {
            return true;
        }

        /**
         * Compute container preferred size <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param container Container
         * @return Container preferred size
         * @see JHelpLayoutSmooth#computePreferredSize(JHelpPanelSmooth)
         */
        @Override
        public Dimension computePreferredSize(final JHelpPanelSmooth container)
        {
            final Dimension preferred = container.child(0).preferredSize();
            return new Dimension(preferred.width + JHelpDialogSmooth.MORE_SIZE,
                                 preferred.height + JHelpDialogSmooth.MORE_SIZE);
        }

        /**
         * Layout components inside container <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param container    Container to layout
         * @param x            X
         * @param y            Y
         * @param parentWidth  Parent width
         * @param parentHeight Parent height
         * @return Suggested container bounds
         * @see JHelpLayoutSmooth#layoutComponents(JHelpPanelSmooth, int, int, int, int)
         */
        @Override
        public Rectangle layoutComponents(
                final JHelpPanelSmooth container, final int x, final int y, final int parentWidth,
                final int parentHeight)
        {
            final JHelpComponentSmooth componentSmooth = container.child(0);
            componentSmooth.bounds(x + (JHelpDialogSmooth.MORE_SIZE >> 1), y + (JHelpDialogSmooth.MORE_SIZE >> 1),
                                   parentWidth - JHelpDialogSmooth.MORE_SIZE,
                                   parentHeight - JHelpDialogSmooth.MORE_SIZE);
            return new Rectangle(x, y, parentWidth, parentHeight);
        }
    }

    /**
     * Main panel that carry dialog main component
     */
    private final JHelpPanelSmooth mainPanel;
    /**
     * Start animation time
     */
    private       long             startTime;
    /**
     * Dialog X
     */
    private final int              x;
    /**
     * Dailog Y
     */
    private final int              y;

    /**
     * Create a new instance of JHelpDialogSmooth
     *
     * @param dialogDescription Describe the dialog
     */
    JHelpDialogSmooth(final DialogDescription dialogDescription)
    {
        this.x = dialogDescription.x;
        this.y = dialogDescription.y;
        this.mainPanel = new JHelpPanelSmooth(new DialogLayout());
        this.mainPanel.addComponent(dialogDescription.mainComponent, JHelpDialogSmooth.CONSTRAINTS);

        if (dialogDescription.textureBackground != null)
        {
            this.mainPanel.textureBackground(dialogDescription.textureBackground);
        }
        else if (dialogDescription.paintBackground != null)
        {
            this.mainPanel.paintBackground(dialogDescription.paintBackground);
        }
        else
        {
            this.mainPanel.background(dialogDescription.background);
        }

        this.mainPanel.shadowColor(dialogDescription.shadow);
        this.mainPanel.shadowLevel(dialogDescription.shadowLevel);
        this.mainPanel.shape(new SmoothRoundRectangle());
    }

    /**
     * Draw the dialog
     *
     * @param parent Image where draw
     */
    void drawDialog(final JHelpImage parent)
    {
        final Dimension size   = this.mainPanel.preferredSize();
        final int       width  = Math.min(parent.getWidth(), size.width);
        final int       height = Math.min(parent.getHeight(), size.height);

        double     factor = 1;
        final long time   = System.currentTimeMillis() - this.startTime;

        if (time < JHelpDialogSmooth.ANIMATION_TIME)
        {
            factor = Math2.interpolationSinus((double) time / ((double) JHelpDialogSmooth.ANIMATION_TIME));
        }

        int       x = this.x;
        int       y = this.y;
        final int w = (int) (width * factor);
        final int h = (int) (height * factor);

        if (x == DialogDescription.CENTER_IN_PARENT)
        {
            x = (parent.getWidth() - w) >> 1;
        }

        if (y == DialogDescription.CENTER_IN_PARENT)
        {
            y = (parent.getHeight() - h) >> 1;
        }

        if ((x + w) > parent.getWidth())
        {
            x = parent.getWidth() - w;
        }

        if ((y + h) > parent.getHeight())
        {
            y = parent.getHeight() - h;
        }

        this.mainPanel.bounds(x, y, w, h);

        parent.pushClip(x - 10, y - 10, w + 20, h + 20);
        this.mainPanel.paint(parent, Math.max(0, x - 10), Math.max(0, y - 10), w, h, width + 20, height + 20);
        parent.popClip();
    }

    /**
     * Search component under mouse position
     *
     * @param x           Mouse X
     * @param y           Mouse Y
     * @param rightButton Indicates if mouse right button is down
     * @return Component or {@code null} if not found
     */
    JHelpComponentSmooth obtainComponentUnder(final int x, final int y, final boolean rightButton)
    {
        return this.mainPanel.obtainComponentUnder(x, y, rightButton);
    }

    /**
     * Dialog root panel
     *
     * @return Dialog root panel
     */
    JHelpContainerSmooth rootPanel()
    {
        return this.mainPanel;
    }

    /**
     * Start animation dialog show at zero
     */
    void startAnimationComeToScreen()
    {
        this.startTime = System.currentTimeMillis();
    }
}
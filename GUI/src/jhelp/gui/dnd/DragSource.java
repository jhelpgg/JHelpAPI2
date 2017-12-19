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
package jhelp.gui.dnd;

import com.sun.istack.internal.NotNull;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.event.MouseInputListener;
import jhelp.gui.HaveHeader;
import jhelp.gui.LabelBufferedImage;
import jhelp.util.gui.UtilGUI;
import jhelp.util.math.Math2;
import jhelp.util.util.Utilities;

/**
 * Source of drag.<br>
 * Extends this class for put drag effect to your object.<br>
 * Remember to call the initialize at the end of your constructor. Be sure that methods :
 * <code>obtainDragComponent, obtainDragImage and obtainInformationValue</code> returns non {@code null} value every time and
 * before the call of <code>initialize</code> method else you will have NullPointerException append<br>
 * Effects will work if you have call <code>GUIUtil.initializeGUI();</code> as the first instruction in your main. <br>
 * <br>
 * Last modification : 2 fevr. 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public abstract class DragSource
        implements MouseInputListener, Runnable
{
    /**
     * Sensibility of the drag start. That is to say the number of drag pixel before the drag is consider to start
     */
    private static final int DETACH_SENSIBILITY = 10;
    /**
     * Animation time in re-attach animation
     */
    private static final int WAIT_FAILED        = 1;

    /**
     * Header for the drag animation <br>
     * <br>
     * Last modification : 3 fevr. 2009<br>
     * Version 0.0.0<br>
     *
     * @author JHelp
     */
    public class HaveHeaderLabelBufferedImage
            extends LabelBufferedImage
            implements HaveHeader
    {
        /**
         * Constructs HaveHeaderLabelBufferedImage
         *
         * @param bufferedImage Image show on drag animation
         */
        public HaveHeaderLabelBufferedImage(final BufferedImage bufferedImage)
        {
            super(bufferedImage);
        }

        /**
         * The linked drag source
         *
         * @return Drag source parent
         */
        public DragSource obtainDragSource()
        {
            return DragSource.this;
        }

        /**
         * Component where the drag occurs
         *
         * @return Component where the drag is detect
         * @see HaveHeader#obtainTheComponentForMove()
         */
        @Override
        public JComponent obtainTheComponentForMove()
        {
            return this;
        }
    }

    /**
     * Indicates if the drag is attach or not. In other words, if the dragging is now
     */
    private       boolean                      attach;
    /**
     * Dialog for the drag animation
     */
    private final JDialog                      dialog;
    /**
     * The component for the drag animation
     */
    private       HaveHeaderLabelBufferedImage labelBufferedImage;
    /**
     * Mouse X
     */
    private       int                          mouseX;
    /**
     * Mouse Y
     */
    private       int                          mouseY;
    /**
     * Old position for know where to go on re-attach animation
     */
    private       Point                        oldPosition;
    /**
     * Thread for re-attach animation
     */
    private       Thread                       thread;

    /**
     * Constructs DragSource
     *
     * @param frame Frame parent. Its better to use the main Frame of your application and please don't make {@code null} else you
     *              can have bad effects
     */
    public DragSource(final @NotNull JFrame frame)
    {
        this.attach = true;
        this.mouseX = this.mouseY = -1;
        this.dialog = new JDialog(frame, null, false);
        this.dialog.setUndecorated(true);
        this.dialog.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
        this.dialog.setResizable(false);
    }

    /**
     * Detach and start dragging
     */
    private void detach()
    {
        this.attach = false;

        // Update the drag image
        this.labelBufferedImage.setBufferedImage(this.obtainDragImage());
        // Position and size of dialog for the drag
        final JComponent component = this.obtainDragComponent();
        final Point      position  = component.getLocationOnScreen();
        UtilGUI.packedSize(this.dialog);
        this.dialog.setLocation(position);
        // Show the dialog
        this.dialog.setVisible(true);
        this.dialog.getRootPane().updateUI();

        // The trick to continue the drag, but we change the frame
        // This trick consists on simulate a release click off the mouse
        // We position the mouse to avoid border problem and be sure the up-left
        // corner is the mouse position
        UtilGUI.locateMouseAt(position.x + 5, position.y + 5);
        UtilGUI.simulateReleasedPressed(1);

        this.oldPosition = position;
    }

    /**
     * Launch the re-attach animation
     */
    private void launchReattachAnimation()
    {
        if (this.thread == null)
        {
            this.thread = new Thread(this);
            this.thread.start();
        }
    }

    /**
     * Call when drag is done with success
     */
    protected abstract void dragDone();

    /**
     * Call when drag failed.<br>
     * It's append when the user release mouse and the mouse is not over a drop zone or the drop zone don't accept the
     * information of the drag source
     */
    protected abstract void dragFailed();

    /**
     * Call just before the start of the drag.<br>
     * It can be usefull if you have to update information or drag image depends on where the drag append
     *
     * @param x Mouse X on the component
     * @param y Mouse Y on the component
     */
    protected abstract void dragWillStart(int x, int y);

    /**
     * Initialize the drag source.<br>
     * Call it at the end of the the constructor when you are sure all is initiated.<br>
     * But don't forget to call it, else the drag will not work
     */
    protected void initialize()
    {
        this.labelBufferedImage = new HaveHeaderLabelBufferedImage(this.obtainDragImage());
        this.obtainDragComponent().addMouseListener(this);
        this.obtainDragComponent().addMouseMotionListener(this);
        this.dialog.setContentPane(this.labelBufferedImage);

        // Trick to correct the "first drag" bug
        // This bug consist to drag fast for the first time
        // The result in the drag element don't follow the mouse
        // This because when the dialog is show for the first time, it's take
        // time, so if we go fast, the mouse is out of the zone before dialog is
        // show, so the trick of release/click don't work
        // To avoid this, we show quickly the dialog on initialization, so when
        // user start is first drag, it is not the first time that the dialog is
        // show, so user can go fast
        this.dialog.setVisible(true);
        Utilities.sleep(1);
        this.dialog.setVisible(false);
    }

    /**
     * Call by the drag system when the drag is done with success.<br>
     * You should never need to call it
     */
    public void fireDragDone()
    {
        this.dialog.setVisible(false);
        this.dialog.getRootPane().getUI().uninstallUI(this.dialog.getRootPane());
        this.attach = true;
        this.dragDone();
    }

    /**
     * Call by the drag system when the drag failed.<br>
     * You should never need to call it
     */
    public void fireDragFailed()
    {
        this.launchReattachAnimation();
        this.dragFailed();
    }

    /**
     * Call when mouse click on the component witch detect the drag
     *
     * @param e Event description
     * @see java.awt.event.MouseListener#mouseClicked(MouseEvent)
     */
    @Override
    public void mouseClicked(final MouseEvent e)
    {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }

    /**
     * Call when a mouse button pressed
     *
     * @param e Event description
     * @see java.awt.event.MouseListener#mousePressed(MouseEvent)
     */
    @Override
    public void mousePressed(final MouseEvent e)
    {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }

    /**
     * Call when a mouse button released
     *
     * @param e Event description
     * @see java.awt.event.MouseListener#mouseReleased(MouseEvent)
     */
    @Override
    public void mouseReleased(final MouseEvent e)
    {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }

    /**
     * Call when mouse enter
     *
     * @param e Event description
     * @see java.awt.event.MouseListener#mouseEntered(MouseEvent)
     */
    @Override
    public void mouseEntered(final MouseEvent e)
    {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }

    /**
     * Call when mouse exit
     *
     * @param e Event description
     * @see java.awt.event.MouseListener#mouseExited(MouseEvent)
     */
    @Override
    public void mouseExited(final MouseEvent e)
    {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }

    /**
     * Call on mouse drag
     *
     * @param e Event description
     * @see java.awt.event.MouseMotionListener#mouseDragged(MouseEvent)
     */
    @Override
    public void mouseDragged(final MouseEvent e)
    {
        final int x = e.getX();
        final int y = e.getY();

        if ((this.attach) && ((Math.abs(x - this.mouseX) + Math.abs(y - this.mouseY)) > DragSource.DETACH_SENSIBILITY))
        {
            this.dragWillStart(this.mouseX, this.mouseY);
            this.detach();
        }
        else if (!this.attach)
        {
            this.mouseX = x;
            this.mouseY = y;
        }
    }

    /**
     * Call when mouse move
     *
     * @param e Event description
     * @see java.awt.event.MouseMotionListener#mouseMoved(MouseEvent)
     */
    @Override
    public void mouseMoved(final MouseEvent e)
    {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }

    /**
     * Component detect the drag start.<br>
     * NEVER {@code null}
     *
     * @return Component detect the drag start
     */
    public abstract JComponent obtainDragComponent();

    /**
     * Image use by the drag.<br>
     * NEVER {@code null}
     *
     * @return Image for drag
     */
    public abstract BufferedImage obtainDragImage();

    /**
     * Information carry by the drag source.<br>
     * NEVER {@code null}
     *
     * @return Information carry
     */
    public abstract Object obtainInformationValue();

    /**
     * Do the re-attach animation.<br>
     * Don't call it
     *
     * @see Runnable#run()
     */
    @Override
    public void run()
    {
        final Point position = this.dialog.getLocation();
        int         wayX     = Math2.sign(this.oldPosition.x - position.x);
        int         wayY     = Math2.sign(this.oldPosition.y - position.y);
        while ((position.x != this.oldPosition.x) || (position.y != this.oldPosition.y))
        {
            Utilities.sleep(DragSource.WAIT_FAILED);
            position.x += wayX;
            position.y += wayY;
            this.dialog.setLocation(position);
            wayX = Math2.sign(this.oldPosition.x - position.x);
            wayY = Math2.sign(this.oldPosition.y - position.y);
        }

        this.dialog.setVisible(false);
        this.dialog.getRootPane().getUI().uninstallUI(this.dialog.getRootPane());
        this.attach = true;

        this.thread = null;
    }
}
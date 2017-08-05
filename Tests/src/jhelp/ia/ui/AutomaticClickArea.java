package jhelp.ia.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import jhelp.samples.ComponentImage;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.UtilGUI;
import jhelp.util.gui.dynamic.Position;
import jhelp.util.list.ArrayObject;
import jhelp.util.text.UtilText;
import jhelp.util.thread.ThreadManager;
import jhelp.util.util.Utilities;

public class AutomaticClickArea implements MouseListener, MouseMotionListener, ActionListener
{
    static final String ADD = "Add";
    static final String GO  = "Go";

    public static void main(String[] args)
    {
        UtilGUI.initializeGUI();
        JHelpImage         screen             = UtilGUI.screenShotJHelpImage();
        JFrame             frame              = new JFrame();
        AutomaticClickArea automaticClickArea = new AutomaticClickArea(screen, frame);
        ComponentImage     componentImage     = new ComponentImage(screen);
        componentImage.addMouseListener(automaticClickArea);
        componentImage.addMouseMotionListener(automaticClickArea);
        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(componentImage), BorderLayout.CENTER);
        frame.add(automaticClickArea.controlPanel, BorderLayout.SOUTH);
        UtilGUI.packedSize(frame);
        UtilGUI.centerOnScreen(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private JHelpImage            screen;
    private JPanel                controlPanel;
    private JLabel                mousePosition;
    private JButton               addLocation;
    private ArrayObject<Position> clicks;
    private boolean               waitNextClick;
    private JButton               go;
    private JFrame                frame;
    private boolean               playing;

    AutomaticClickArea(JHelpImage screen, JFrame frame)
    {
        this.screen = screen;
        this.frame = frame;
        this.clicks = new ArrayObject<>();
        this.waitNextClick = false;
        this.controlPanel = new JPanel(new FlowLayout());
        this.mousePosition = new JLabel("9999, 9999");
        this.addLocation = new JButton(AutomaticClickArea.ADD);
        this.addLocation.setActionCommand(AutomaticClickArea.ADD);
        this.addLocation.addActionListener(this);
        this.go = new JButton(AutomaticClickArea.GO);
        this.go.setActionCommand(AutomaticClickArea.GO);
        this.go.addActionListener(this);
        this.controlPanel.add(this.mousePosition);
        this.controlPanel.add(this.addLocation);
        this.controlPanel.add(this.go);
        this.playing = false;
    }

    JPanel controlPanel()
    {
        return this.controlPanel;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param actionEvent
     */
    @Override
    public void actionPerformed(final ActionEvent actionEvent)
    {
        switch (actionEvent.getActionCommand())
        {
            case AutomaticClickArea.ADD:
                this.addLocation.setEnabled(false);
                this.waitNextClick = true;
                break;
            case AutomaticClickArea.GO:
                if (!this.playing)
                {
                    this.playing = true;
                    this.go.setText("Stop");
                    ThreadManager.parallel(this::playAction);
                }
                else
                {
                    this.playing = false;
                    this.go.setText("Go");
                }
                break;
        }
    }

    private void playAction()
    {
        this.frame.setState(JFrame.ICONIFIED);
        Utilities.sleep(1024);

        for (int i = 0; i < 1024 && this.playing; i++)
        {
            Utilities.sleep(1024);

            for (Position position : this.clicks)
            {
                UtilGUI.locateMouseAt(position.getX(), position.getY());
                UtilGUI.simulateMouseClick(128);

                if (!this.playing)
                {
                    return;
                }

                Utilities.sleep(1024);

                if (!this.playing)
                {
                    return;
                }
            }
        }
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param mouseEvent
     */
    @Override
    public void mouseClicked(final MouseEvent mouseEvent)
    {
        if (this.waitNextClick)
        {
            this.waitNextClick = false;
            final int x = mouseEvent.getX();
            final int y = mouseEvent.getY();
            this.screen.startDrawMode();
            this.screen.drawThickLine(x - 32, y - 32, x + 32, y + 32, 3, 0xFFFF0000);
            this.screen.drawThickLine(x - 32, y + 32, x + 32, y - 32, 3, 0xFFFF0000);
            this.screen.endDrawMode();
            this.clicks.add(new Position(x, y));
            this.addLocation.setEnabled(true);
        }
    }

    /**
     * Invoked when a mouse button is pressed on a component and then
     * dragged.  <code>MOUSE_DRAGGED</code> events will continue to be
     * delivered to the component where the drag originated until the
     * mouse button is released (regardless of whether the mouse position
     * is within the bounds of the component).
     * <p>
     * Due to platform-dependent Drag&amp;Drop implementations,
     * <code>MOUSE_DRAGGED</code> events may not be delivered during a native
     * Drag&amp;Drop operation.
     *
     * @param e
     */
    @Override
    public void mouseDragged(final MouseEvent e)
    {

    }

    /**
     * Invoked when the mouse cursor has been moved onto a component
     * but no buttons have been pushed.
     *
     * @param mouseEvent
     */
    @Override
    public void mouseMoved(final MouseEvent mouseEvent)
    {
        this.mousePosition.setText(UtilText.concatenate(mouseEvent.getX(), ", ", mouseEvent.getY()));
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e
     */
    @Override
    public void mousePressed(final MouseEvent e)
    {

    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e
     */
    @Override
    public void mouseReleased(final MouseEvent e)
    {

    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e
     */
    @Override
    public void mouseEntered(final MouseEvent e)
    {

    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e
     */
    @Override
    public void mouseExited(final MouseEvent e)
    {

    }
}

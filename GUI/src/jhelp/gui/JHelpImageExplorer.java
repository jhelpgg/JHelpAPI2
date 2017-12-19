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
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any
 * damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import jhelp.gui.action.GenericAction;
import jhelp.gui.layout.CenterLayout;
import jhelp.util.debug.Debug;
import jhelp.util.filter.FileFilter;
import jhelp.util.gui.GIF;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.UtilGUI;
import jhelp.util.io.UtilIO;
import jhelp.util.text.UtilText;

/**
 * Explorer for images
 *
 * @author JHelp
 */
public class JHelpImageExplorer
        extends JPanel
{
    /**
     * Maximum seen image in same time
     */
    private static final int NUMBER_MAXIMUM_OF_IMAGES = 100;

    /**
     * Action for change current directory
     *
     * @author JHelp
     */
    private class ActionChooseDirectory
            extends GenericAction
    {
        /**
         * Create a new instance of ActionChooseDirectory
         */
        public ActionChooseDirectory()
        {
            super("Choose directory");
        }

        /**
         * Action of changing current directory <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param actionEvent Action event description
         * @see GenericAction#doActionPerformed(ActionEvent)
         */
        @Override
        protected void doActionPerformed(final ActionEvent actionEvent)
        {
            JHelpImageExplorer.this.chooseDirectory();
        }
    }

    /**
     * Action of deleting the current selected image
     *
     * @author JHelp
     */
    private class ActionDeleteSelection
            extends GenericAction
    {
        /**
         * Create a new instance of ActionDeleteSelection
         */
        public ActionDeleteSelection()
        {
            super("Delete");
        }

        /**
         * Action delete the current selected image <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param actionEvent Action event description
         * @see GenericAction#doActionPerformed(ActionEvent)
         */
        @Override
        protected void doActionPerformed(final ActionEvent actionEvent)
        {
            JHelpImageExplorer.this.actionDeleteSelection();
        }
    }

    /**
     * Action go down in images list
     *
     * @author JHelp
     */
    private class ActionDown
            extends GenericAction
    {
        /**
         * Create a new instance of ActionDown
         */
        public ActionDown()
        {
            super("Down");
        }

        /**
         * Action go down in the list <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param actionEvent Action event description
         * @see GenericAction#doActionPerformed(ActionEvent)
         */
        @Override
        protected void doActionPerformed(final ActionEvent actionEvent)
        {
            JHelpImageExplorer.this.actionDown();
        }
    }

    /**
     * Action move selected image in an other directory
     *
     * @author JHelp
     */
    private class ActionMoveSelection
            extends GenericAction
    {
        /**
         * Create a new instance of ActionMoveSelection
         */
        public ActionMoveSelection()
        {
            super("Move");
        }

        /**
         * Action move selected image in an other directory <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param actionEvent Action event description
         * @see GenericAction#doActionPerformed(ActionEvent)
         */
        @Override
        protected void doActionPerformed(final ActionEvent actionEvent)
        {
            JHelpImageExplorer.this.actionMoveSelection();
        }
    }

    /**
     * Action go up in image list
     *
     * @author JHelp
     */
    private class ActionUp
            extends GenericAction
    {
        /**
         * Create a new instance of ActionUp
         */
        public ActionUp()
        {
            super("UP");
        }

        /**
         * Action go up in image list <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param actionEvent Action event description
         * @see GenericAction#doActionPerformed(ActionEvent)
         */
        @Override
        protected void doActionPerformed(final ActionEvent actionEvent)
        {
            JHelpImageExplorer.this.actionUp();
        }
    }

    /**
     * Event manager.<br>
     * It manage for now only mouse events
     *
     * @author JHelp
     */
    class EventManager
            implements MouseListener, MouseMotionListener
    {
        /**
         * Called when mouse clicked <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param mouseEvent Mouse event description
         * @see MouseListener#mouseClicked(MouseEvent)
         */
        @Override
        public void mouseClicked(final MouseEvent mouseEvent)
        {
            JHelpImageExplorer.this.removeSelection();

            final LabelJHelpImage labelJHelpImage = (LabelJHelpImage) mouseEvent.getComponent();

            final int information = (Integer) labelJHelpImage.additionalInformation();

            if (JHelpImageExplorer.this.currentFiles[information] != null)
            {
                JHelpImageExplorer.this.selected = information;
                labelJHelpImage.selected(true);

                if (SwingUtilities.isRightMouseButton(mouseEvent))
                {
                    JHelpImageExplorer.this.popupMenu.show(labelJHelpImage, 50, 50);
                }
            }

            JHelpImageExplorer.this.updateSelection();
        }

        /**
         * Called when mouse pressed <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param mouseEvent Mouse event description
         * @see MouseListener#mousePressed(MouseEvent)
         */
        @Override
        public void mousePressed(final MouseEvent mouseEvent)
        {
        }

        /**
         * Called when mouse released <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param mouseEvent Mouse event description
         * @see MouseListener#mouseReleased(MouseEvent)
         */
        @Override
        public void mouseReleased(final MouseEvent mouseEvent)
        {
        }

        /**
         * Called when mouse entered <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param mouseEvent Mouse event description
         * @see MouseListener#mouseEntered(MouseEvent)
         */
        @Override
        public void mouseEntered(final MouseEvent mouseEvent)
        {
        }

        /**
         * Called when mouse exited <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param mouseEvent Mouse event description
         * @see MouseListener#mouseExited(MouseEvent)
         */
        @Override
        public void mouseExited(final MouseEvent mouseEvent)
        {
        }

        /**
         * Called when mouse dragged <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param mouseEvent Mouse event description
         * @see MouseMotionListener#mouseDragged(MouseEvent)
         */
        @Override
        public void mouseDragged(final MouseEvent mouseEvent)
        {
        }

        /**
         * Called when mouse moved <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param mouseEvent Mouse event description
         * @see MouseMotionListener#mouseMoved(MouseEvent)
         */
        @Override
        public void mouseMoved(final MouseEvent mouseEvent)
        {
        }
    }

    /**
     * Action for choose a directory
     */
    private final ActionChooseDirectory actionChooseDirectory;
    /**
     * Action for delete a selection
     */
    private final ActionDeleteSelection actionDeleteSelection;
    /**
     * Action for go down in image list
     */
    private final ActionDown            actionDown;
    /**
     * Action for move selected image
     */
    private final ActionMoveSelection   actionMoveSelection;
    /**
     * Action for go up in image list
     */
    private final ActionUp              actionUp;
    /**
     * Button for choose a directory
     */
    private final JButton               buttonChooseDirectory;
    /**
     * Button for go down in image list
     */
    private final JButton               buttonDown;
    /**
     * Button for go up in image list
     */
    private final JButton               buttonUp;
    /**
     * Event manager
     */
    private final EventManager          eventManager;
    /**
     * List of files
     */
    private final ArrayList<File>       files;
    /**
     * Index of the first shown image in file list
     */
    private       int                   firstIndex;
    /**
     * Filter to get only images
     */
    private final FileFilter            imageFileFilter;
    /**
     * Label for show some information
     */
    private final JLabel                labelInformation;
    /**
     * Panel carry the images
     */
    private final JPanel                panelImages;
    /**
     * Preview of selected image
     */
    private final LabelJHelpImage       preview;
    /**
     * Glass pane for waiting images loading
     */
    private       WaitingGlassPane      waitingGlassPane;
    /**
     * Current directory
     */
    File currentDirectory;
    /**
     * Shown images
     */
    final File[]            currentFiles;
    /**
     * Labels for show images
     */
    final LabelJHelpImage[] labelJHelpImages;
    /**
     * Action popup
     */
    final JPopupMenu        popupMenu;
    /**
     * Selected image index
     */
    int selected;

    /**
     * Create a new instance of JHelpImageExplorer
     */
    public JHelpImageExplorer()
    {
        super(new BorderLayout());

        this.eventManager = new EventManager();
        this.actionUp = new ActionUp();
        this.actionDown = new ActionDown();
        this.actionChooseDirectory = new ActionChooseDirectory();
        this.actionMoveSelection = new ActionMoveSelection();
        this.actionDeleteSelection = new ActionDeleteSelection();

        this.actionUp.setEnabled(false);
        this.actionDown.setEnabled(false);

        this.buttonChooseDirectory = new JButton(this.actionChooseDirectory);
        this.buttonUp = new JButton(this.actionUp);
        this.buttonDown = new JButton(this.actionDown);
        this.labelJHelpImages = new LabelJHelpImage[JHelpImageExplorer.NUMBER_MAXIMUM_OF_IMAGES];
        this.panelImages = new JPanel(new GridLayout(10, 10, 5, 5));
        this.preview = new LabelJHelpImage(256, 256);
        this.labelInformation = new JLabel("No selection", SwingConstants.CENTER);

        this.currentFiles = new File[JHelpImageExplorer.NUMBER_MAXIMUM_OF_IMAGES];

        for (int i = 0; i < JHelpImageExplorer.NUMBER_MAXIMUM_OF_IMAGES; i++)
        {
            this.labelJHelpImages[i] = new LabelJHelpImage(100, 100);
            this.panelImages.add(this.labelJHelpImages[i]);

            this.labelJHelpImages[i].additionalInformation(i);

            this.labelJHelpImages[i].addMouseListener(this.eventManager);
            this.labelJHelpImages[i].addMouseMotionListener(this.eventManager);
        }

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(this.buttonChooseDirectory);
        panel.add(this.buttonUp);

        this.add(panel, BorderLayout.NORTH);
        this.add(this.panelImages, BorderLayout.CENTER);
        this.add(this.buttonDown, BorderLayout.SOUTH);

        panel = new JPanel(new BorderLayout());

        JPanel panel2 = new JPanel(new CenterLayout());
        panel2.add(this.preview);

        panel.add(panel2, BorderLayout.NORTH);
        panel.add(this.labelInformation, BorderLayout.SOUTH);

        panel2 = new JPanel(new CenterLayout());
        panel2.add(panel);

        this.add(panel2, BorderLayout.WEST);

        this.imageFileFilter = FileFilter.createFilterForImage();
        this.imageFileFilter.acceptDirectory(false);
        this.selected = -1;
        this.firstIndex = 0;
        this.files = new ArrayList<File>();
        this.changeCurrentDirectory(UtilIO.obtainOutsideDirectory());

        this.popupMenu = new JPopupMenu();
        this.popupMenu.add(this.actionMoveSelection);
        this.popupMenu.add(this.actionDeleteSelection);
    }

    /**
     * Delete selected image
     */
    void actionDeleteSelection()
    {
        if (this.selected < 0)
        {
            return;
        }

        final File file = this.currentFiles[this.selected];

        if (file == null)
        {
            return;
        }

        UtilIO.delete(file);
        this.files.remove(this.firstIndex + this.selected);

        if (this.firstIndex >= this.files.size())
        {
            this.firstIndex = 100 * (this.files.size() / 100);
        }

        this.updateFiles(true);
    }

    /**
     * Go down in image list
     */
    final void actionDown()
    {
        this.firstIndex += 100;

        this.updateFiles(false);
    }

    /**
     * Change the selected image of directory
     */
    void actionMoveSelection()
    {
        if (this.selected < 0)
        {
            return;
        }

        final File file = this.currentFiles[this.selected];

        if (file == null)
        {
            return;
        }

        final File directory = DirectoryChooser.DIRECTORY_CHOOSER.chooseOneDirectory(this);

        if ((directory == null) || (directory.getAbsolutePath()
                                             .equals(this.currentDirectory.getAbsolutePath())))
        {
            return;
        }

        try
        {
            UtilIO.rename(file, new File(directory, file.getName()));
        }
        catch (final Exception exception)
        {
            Debug.exception(exception);
        }

        this.files.remove(this.firstIndex + this.selected);

        if (this.firstIndex >= this.files.size())
        {
            this.firstIndex = 100 * (this.files.size() / 100);
        }

        this.updateFiles(true);
    }

    /**
     * Go up in image list
     */
    final void actionUp()
    {
        this.firstIndex -= 100;

        this.updateFiles(false);
    }

    /**
     * Change current directory
     *
     * @param directory New directory
     */
    final void changeCurrentDirectory(final File directory)
    {
        this.currentDirectory = directory;
        this.actionChooseDirectory.name(directory.getAbsolutePath());
        this.firstIndex = 0;

        this.files.clear();

        final File[] images = directory.listFiles((java.io.FileFilter) this.imageFileFilter);

        if (images != null)
        {
            Collections.addAll(this.files, images);
        }

        this.updateFiles(false);
    }

    /**
     * Choose a directory to become the current one
     */
    void chooseDirectory()
    {
        final File directory = DirectoryChooser.DIRECTORY_CHOOSER.chooseOneDirectory(this);

        if (directory != null)
        {
            this.changeCurrentDirectory(directory);
        }
    }

    /**
     * Remove selection of image
     */
    final void removeSelection()
    {
        if (JHelpImageExplorer.this.selected >= 0)
        {
            JHelpImageExplorer.this.labelJHelpImages[JHelpImageExplorer.this.selected].selected(false);
        }

        this.selected = -1;
        this.updateSelection();
    }

    /**
     * Update file list
     *
     * @param startSelected Indicates if its enough to start with the selected image (Image before didn't need to be updated)
     */
    final void updateFiles(final boolean startSelected)
    {
        if (this.waitingGlassPane == null)
        {
            final JFrame frame = UtilGUI.getFrameParent(this);

            if (frame != null)
            {
                this.waitingGlassPane = WaitingGlassPane.addWaitingGlassTo(frame);
                this.waitingGlassPane.setFPS(10);

                try
                {
                    final GIF gif = new GIF(ResourcesGUI.RESOURCES.obtainResourceStream("loader.gif"));
                    this.waitingGlassPane.addWaitingImage(gif);
                    gif.destroy();
                }
                catch (final IOException exception)
                {
                    Debug.exception(exception);
                }
            }
        }

        if (this.waitingGlassPane != null)
        {
            this.waitingGlassPane.startWaiting();
        }

        this.actionUp.setEnabled(false);
        this.actionDown.setEnabled(false);

        int        start = 0;
        JHelpImage image;

        int number = Math.min(JHelpImageExplorer.NUMBER_MAXIMUM_OF_IMAGES, this.files.size() - this.firstIndex);

        if ((startSelected) && (this.selected >= 0))
        {
            for (int i = this.selected + 1; i < number; i++)
            {
                image = this.labelJHelpImages[i].image();

                if (image == null)
                {
                    this.labelJHelpImages[i - 1].removeImageWithoutChangeSize();
                }
                else
                {
                    this.labelJHelpImages[i - 1].image(image);
                }

                this.currentFiles[i - 1] = this.currentFiles[i];
            }

            start = Math.max(0, number - 1);
        }

        this.removeSelection();

        for (int i = start; i < number; i++)
        {
            this.currentFiles[i] = this.files.get(i + this.firstIndex);

            try
            {
                image = JHelpImage.loadImageThumb(this.currentFiles[i], 100, 100);

                if (image == null)
                {
                    Debug.warning("Delete file : ", this.currentFiles[i].getAbsolutePath());

                    UtilIO.delete(this.currentFiles[i]);
                    this.files.remove(i + this.firstIndex);

                    i--;
                    number = Math.min(JHelpImageExplorer.NUMBER_MAXIMUM_OF_IMAGES, this.files.size() - this.firstIndex);

                }
                else
                {
                    this.labelJHelpImages[i].image(image);
                }
            }
            catch (final Exception exception)
            {
                Debug.exception(exception, "Can't load : ", this.currentFiles[i].getAbsolutePath());

                this.labelJHelpImages[i].removeImageWithoutChangeSize();
            }
            catch (final Error error)
            {
                Debug.error(error, "Can't load : ", this.currentFiles[i].getAbsolutePath());

                this.labelJHelpImages[i].removeImageWithoutChangeSize();
            }
        }

        for (int i = number; i < JHelpImageExplorer.NUMBER_MAXIMUM_OF_IMAGES; i++)
        {
            this.currentFiles[i] = null;
            this.labelJHelpImages[i].removeImageWithoutChangeSize();
        }

        final int total        = this.files.size();
        final int numberOfPage = total / 100;

        this.actionUp.name(UtilText.concatenate("UP (", this.firstIndex / 100, "/", numberOfPage, ")"));
        this.actionDown.name(
                UtilText.concatenate("DOWN (", Math.max(0, (this.files.size() - this.firstIndex - 100) / 100), "/",
                                     numberOfPage, ")"));

        this.actionUp.setEnabled(this.firstIndex > 0);
        this.actionDown.setEnabled((this.files.size() - this.firstIndex) > 100);

        if (this.waitingGlassPane != null)
        {
            this.waitingGlassPane.stopWaiting();
        }
    }

    /**
     * Update the selection
     */
    void updateSelection()
    {
        if (this.selected < 0)
        {
            this.preview.removeImageWithoutChangeSize();
            this.labelInformation.setText("No selection");

            return;
        }

        try
        {
            final File file = this.currentFiles[this.selected];

            this.preview.image(JHelpImage.loadImageThumb(file, 256, 256));
            this.labelInformation.setText(file.getName());
        }
        catch (final Exception exception)
        {
            Debug.exception(exception);

            this.preview.removeImageWithoutChangeSize();
            this.labelInformation.setText("Failed to compute preview");
        }
    }
}
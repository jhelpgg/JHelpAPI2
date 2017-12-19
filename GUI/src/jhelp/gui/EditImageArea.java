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
package jhelp.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import jhelp.gui.action.GenericAction;
import jhelp.util.debug.Debug;
import jhelp.util.filter.FileFilter;
import jhelp.util.gui.ImageArea;
import jhelp.util.gui.JHelpImage;
import jhelp.util.io.UtilIO;
import jhelp.util.text.UtilText;

/**
 * Component that edit an image area
 *
 * @author JHelp
 */
public class EditImageArea
        extends JPanel
{
    /**
     * File name extension
     */
    private static final String END_FILE_NAME = "." + ImageArea.EXTENSION;

    /**
     * Action for add an area
     *
     * @author JHelp
     */
    class ActionAdd
            extends GenericAction
    {
        /**
         * Create a new instance of ActionAdd
         */
        ActionAdd()
        {
            super("EditImageArea.add", ResourcesGUI.RESOURCE_TEXT);
        }

        /**
         * Called when action required <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param actionEvent Event description
         * @see GenericAction#doActionPerformed(ActionEvent)
         */
        @Override
        protected void doActionPerformed(final ActionEvent actionEvent)
        {
            EditImageArea.this.doAdd();
        }
    }

    /**
     * Action for divide image area in regular part
     *
     * @author JHelp
     */
    class ActionDivide
            extends GenericAction
    {
        /**
         * Create a new instance of ActionDivide
         */
        ActionDivide()
        {
            super("EditImageArea.divide", ResourcesGUI.RESOURCE_TEXT);
        }

        /**
         * Called when action happen <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param actionEvent Event description
         * @see GenericAction#doActionPerformed(ActionEvent)
         */
        @Override
        protected void doActionPerformed(final ActionEvent actionEvent)
        {
            EditImageArea.this.doDivide();
        }
    }

    /**
     * Action for create a grid
     *
     * @author JHelp
     */
    class ActionGrid
            extends GenericAction
    {
        /**
         * Create a new instance of ActionGrid
         */
        ActionGrid()
        {
            super("EditImageArea.grid", ResourcesGUI.RESOURCE_TEXT);
        }

        /**
         * Called when action happen <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param actionEvent Event description
         * @see GenericAction#doActionPerformed(ActionEvent)
         */
        @Override
        protected void doActionPerformed(final ActionEvent actionEvent)
        {
            EditImageArea.this.doGrid();
        }
    }

    /**
     * Action for load an image area
     *
     * @author JHelp
     */
    class ActionLoad
            extends GenericAction
    {
        /**
         * Create a new instance of ActionLoad
         */
        ActionLoad()
        {
            super("EditImageArea.load", ResourcesGUI.RESOURCE_TEXT);
        }

        /**
         * Called when action triggered <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param actionEvent Event description
         * @see GenericAction#doActionPerformed(ActionEvent)
         */
        @Override
        protected void doActionPerformed(final ActionEvent actionEvent)
        {
            EditImageArea.this.doLoad();
        }
    }

    /**
     * Action that make a color transparent
     *
     * @author JHelp
     */
    class ActionMakeTransparent
            extends GenericAction
    {
        /**
         * Create a new instance of ActionMakeTransparent
         */
        ActionMakeTransparent()
        {
            super("EditImageArea.transparent", ResourcesGUI.RESOURCE_TEXT);
        }

        /**
         * Called when action targeted <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param actionEvent Event description
         * @see GenericAction#doActionPerformed(ActionEvent)
         */
        @Override
        protected void doActionPerformed(final ActionEvent actionEvent)
        {
            EditImageArea.this.doMakeTransparent();
        }
    }

    /**
     * Action to open a image
     *
     * @author JHelp
     */
    class ActionOpen
            extends GenericAction
    {
        /**
         * Create a new instance of ActionOpen
         */
        ActionOpen()
        {
            super("EditImageArea.open", ResourcesGUI.RESOURCE_TEXT);
        }

        /**
         * Called when action happen <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param actionEvent Event description
         * @see GenericAction#doActionPerformed(ActionEvent)
         */
        @Override
        protected void doActionPerformed(final ActionEvent actionEvent)
        {
            EditImageArea.this.doOpen();
        }
    }

    /**
     * Action that save current image area
     *
     * @author JHelp
     */
    class ActionSave
            extends GenericAction
    {
        /**
         * Create a new instance of ActionSave
         */
        ActionSave()
        {
            super("EditImageArea.save", ResourcesGUI.RESOURCE_TEXT);
        }

        /**
         * Called when action happen <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param actionEvent Event description
         * @see GenericAction#doActionPerformed(ActionEvent)
         */
        @Override
        protected void doActionPerformed(final ActionEvent actionEvent)
        {
            EditImageArea.this.doSave();
        }
    }

    /**
     * Component that carray the image area to edit
     */
    private final ComponentImageArea componentImageArea;
    /**
     * File chooser
     */
    private final FileChooser        fileChooser;
    /**
     * Image area file filter
     */
    private final FileFilter         filterArea;
    /**
     * Image file filter
     */
    private final FileFilter         filterImage;
    /**
     * Spinner for choose a number apply in horizontal
     */
    private final JSpinner           spinnerHorizontal;
    /**
     * Spinner for choose a number apply in vertical
     */
    private final JSpinner           spinnerVertical;

    /**
     * Create a new instance of EditImageArea
     */
    public EditImageArea()
    {
        this(new FileChooser());
    }

    /**
     * Create a new instance of EditImageArea
     *
     * @param fileChooser File chooser to use
     */
    public EditImageArea(final FileChooser fileChooser)
    {
        super(new BorderLayout());

        if (fileChooser == null)
        {
            throw new NullPointerException("fileChooser mustn't be null");
        }

        this.fileChooser = fileChooser;
        this.filterArea = new FileFilter();
        this.filterArea.addExtension(ImageArea.EXTENSION);
        this.filterImage = FileFilter.createFilterForImageByFileImageInformation();

        this.componentImageArea = new ComponentImageArea();
        this.add(new JScrollPane(this.componentImageArea), BorderLayout.CENTER);

        this.spinnerHorizontal = new JSpinner(new SpinnerNumberModel(1, 1, 128, 1));
        this.spinnerVertical = new JSpinner(new SpinnerNumberModel(1, 1, 128, 1));

        final JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JButton(new ActionOpen()));
        panel.add(new JButton(new ActionSave()));
        panel.add(new JButton(new ActionLoad()));
        panel.add(new JButton(new ActionAdd()));
        panel.add(new JButton(new ActionMakeTransparent()));
        panel.add(this.spinnerHorizontal);
        panel.add(new JLabel("x"));
        panel.add(this.spinnerVertical);
        panel.add(new JButton(new ActionDivide()));
        panel.add(new JButton(new ActionGrid()));
        this.add(panel, BorderLayout.SOUTH);
    }

    /**
     * Compute a message to display
     *
     * @param key        Key of resource text (with eventually some "hole")
     * @param parameters "Holes" replacement
     * @return Computed message
     */
    private String obtainMessage(final String key, final String... parameters)
    {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(UtilText.replaceHole(ResourcesGUI.RESOURCE_TEXT.getText(key), parameters));

        return stringBuilder.toString();
    }

    /**
     * Show message on error
     *
     * @param key       Key of resource text (with "hole" : path file, exception message) of the error message
     * @param file      Actual file where error happen
     * @param exception Exception happen
     */
    private void showFailedMessage(final String key, final File file, final Exception exception)
    {
        final String message = this.obtainMessage(key, file.getAbsolutePath(), exception.getMessage());
        JOptionPane.showMessageDialog(this, message, "KO", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Show message when succeed
     *
     * @param key  Key of resource text (with "hole" : path file) of the succeed message
     * @param file Actual file where succeed happen
     */
    private void showSucceedMessage(final String key, final File file)
    {
        final String message = this.obtainMessage(key, file.getAbsolutePath());
        JOptionPane.showMessageDialog(this, message, "OK", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Add actual over rectangle as area
     */
    void doAdd()
    {
        final ImageArea imageArea = this.componentImageArea.imageArea();

        if (imageArea == null)
        {
            this.componentImageArea.requestFocus();
            this.componentImageArea.requestFocusInWindow();
            return;
        }

        imageArea.addActualOverAsArea();
        this.componentImageArea.requestFocus();
        this.componentImageArea.requestFocusInWindow();
    }

    /**
     * Divide the area in regular part
     */
    void doDivide()
    {
        final int horizontal = (Integer) this.spinnerHorizontal.getValue();
        final int vertical   = (Integer) this.spinnerVertical.getValue();
        this.componentImageArea.divide(horizontal, vertical);
        this.componentImageArea.requestFocus();
        this.componentImageArea.requestFocusInWindow();
    }

    /**
     * Create a grid
     */
    void doGrid()
    {
        final int horizontal = (Integer) this.spinnerHorizontal.getValue();
        final int vertical   = (Integer) this.spinnerVertical.getValue();
        this.componentImageArea.grid(horizontal, vertical);
        this.componentImageArea.requestFocus();
        this.componentImageArea.requestFocusInWindow();
    }

    /**
     * Load an image
     */
    void doLoad()
    {
        this.fileChooser.fileFilter(this.filterImage);
        final File file = this.fileChooser.showOpenFile();

        if (file != null)
        {
            try
            {
                final JHelpImage image = JHelpImage.loadImage(file);
                this.componentImageArea.image(image);
                this.showSucceedMessage("EditImageArea.loadSucceed", file);
            }
            catch (final Exception exception)
            {
                Debug.exception(exception, "Failed to load image  : ", file.getAbsolutePath());
                this.showFailedMessage("EditImageArea.loadFailed", file, exception);
            }
        }

        this.componentImageArea.requestFocus();
        this.componentImageArea.requestFocusInWindow();
    }

    /**
     * The next clicked color will be transparent
     */
    void doMakeTransparent()
    {
        this.componentImageArea.makeNextClickColorTransparent();
        this.componentImageArea.requestFocus();
        this.componentImageArea.requestFocusInWindow();
    }

    /**
     * Open an other image area
     */
    void doOpen()
    {
        this.fileChooser.fileFilter(this.filterArea);
        final File file = this.fileChooser.showOpenFile();

        if (file != null)
        {
            InputStream inputStream = null;

            try
            {
                inputStream = new FileInputStream(file);
                final ImageArea imageArea = ImageArea.loadImageArea(inputStream);
                this.componentImageArea.imageArea(imageArea);
                this.showSucceedMessage("EditImageArea.openSucceed", file);
            }
            catch (final Exception exception)
            {
                Debug.exception(exception, "Failed to load image area  : ", file.getAbsolutePath());
                this.showFailedMessage("EditImageArea.openFailed", file, exception);
            }
            finally
            {
                if (inputStream != null)
                {
                    try
                    {
                        inputStream.close();
                    }
                    catch (final Exception ignored)
                    {
                    }
                }
            }
        }

        this.componentImageArea.requestFocus();
        this.componentImageArea.requestFocusInWindow();
    }

    /**
     * Save current image area
     */
    void doSave()
    {
        final ImageArea imageArea = this.componentImageArea.imageArea();

        if (imageArea == null)
        {
            this.componentImageArea.requestFocus();
            this.componentImageArea.requestFocusInWindow();
            return;
        }

        this.fileChooser.fileFilter(this.filterArea);
        File file = this.fileChooser.showSaveFile();

        if (file != null)
        {
            final String name = file.getName();

            if (!name.endsWith(EditImageArea.END_FILE_NAME))
            {
                file = new File(file.getParent(), name + EditImageArea.END_FILE_NAME);
            }

            UtilIO.createFile(file);
            OutputStream outputStream = null;

            try
            {
                outputStream = new FileOutputStream(file);
                imageArea.saveImageArea(outputStream);
                this.showSucceedMessage("EditImageArea.saveSucceed", file);
            }
            catch (final Exception exception)
            {
                Debug.exception(exception, "Failed to save image area  : ", file.getAbsolutePath());
                this.showFailedMessage("EditImageArea.saveFailed", file, exception);
            }
            finally
            {
                if (outputStream != null)
                {
                    try
                    {
                        outputStream.flush();
                    }
                    catch (final Exception ignored)
                    {
                    }

                    try
                    {
                        outputStream.close();
                    }
                    catch (final Exception ignored)
                    {
                    }
                }
            }
        }

        this.componentImageArea.requestFocus();
        this.componentImageArea.requestFocusInWindow();
    }
}
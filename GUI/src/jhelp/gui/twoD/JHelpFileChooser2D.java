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
package jhelp.gui.twoD;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;
import jhelp.gui.ResourcesGUI;
import jhelp.gui.twoD.JHelpBorderLayout.JHelpBorderLayoutConstraints;
import jhelp.sound.JHelpSound;
import jhelp.sound.SoundFactory;
import jhelp.util.filter.FileFilter;
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpTextAlign;
import jhelp.util.io.UtilIO;
import jhelp.util.list.Triplet;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.ThreadManager;

/**
 * File chooser dialog.<br>
 * Special keys :
 * <ul>
 * <li><b>F2</b> Rename selected file/directory</li>
 * <li><b>F3</b> Create new directory</li>
 * <li><b>F4</b> go in parent directory</li>
 * <li><b>Suppr/Delete</b> Delete selected file/directory</li>
 * </ul>
 *
 * @author JHelp
 */
public class JHelpFileChooser2D
        extends JHelpFrame2D
        implements JHelpActionListener, JHelpFileExplorerListener
{
    static
    {
        SHOWING = new AtomicBoolean(false);

        FILTER_IMAGE = FileFilter.createFilterForImageByFileImageInformation();
        JHelpFileChooser2D.FILTER_IMAGE.acceptDirectory(false);

        FILTER_SOUND = new FileFilter();
        JHelpFileChooser2D.FILTER_SOUND.acceptDirectory(false);
        JHelpFileChooser2D.FILTER_SOUND.addExtension("mp3");
        JHelpFileChooser2D.FILTER_SOUND.addExtension("mid");
        JHelpFileChooser2D.FILTER_SOUND.addExtension("au");
        JHelpFileChooser2D.FILTER_SOUND.addExtension("wav");

        FONT = new JHelpFont("Monospaced", 28);

        FILE_CHOOSER = new JHelpFileChooser2D();
    }

    /**
     * File chooser instance
     */
    private static final JHelpFileChooser2D FILE_CHOOSER;
    /**
     * Filter to know if file is an image
     */
    private static final FileFilter         FILTER_IMAGE;
    /**
     * Filter to know if filis sound
     */
    private static final FileFilter         FILTER_SOUND;
    /**
     * Font to use
     */
    private static final JHelpFont          FONT;
    /**
     * Cancel button ID
     */
    private static final int ID_CANCEL_BUTTON = 1;
    /**
     * OK button ID
     */
    private static final int ID_OK_BUTTON     = 2;
    /**
     * Indicates if file chooser is showing
     */
    private static final AtomicBoolean SHOWING;
    /**
     * Signal to a listener ok or cancel button pressed
     */
    private static final ConsumerTask<Triplet<Boolean, JHelpFileChooser2DListener, File>> SIGNAL_CHOOSE_OR_CANCEL =
            new ConsumerTask<Triplet<Boolean, JHelpFileChooser2DListener, File>>()
            {
                /**
                 * Signal to a listener ok or cancel button pressed
                 *
                 * @param parameter
                 *           Triplet[{@code true} for ok ({@code false} for cancel), listener to alert, selected file]
                 * @see jhelp.util.thread.ConsumerTask#consume(Object)
                 */
                @Override
                public void consume(
                        final Triplet<Boolean, JHelpFileChooser2DListener, File> parameter)
                {
                    if (parameter.element1)
                    {
                        parameter.element2.fileChoose(parameter.element3);
                    }
                    else
                    {
                        parameter.element2.cancelChoose();
                    }
                }
            };
    /**
     * Lock for synchronize image loading
     */
    static final         Object                                                           LOCK                    = new Object();
    /**
     * Lock for synchronize sound loading
     */
    static final         Object                                                           LOCK_SOUND              = new Object();
    /**
     * Special file for indicates no image
     */
    static final         File                                                             NO_IMAGE                = new File(
            "");
    /**
     * Special file for indicates no sound
     */
    static final         File                                                             NO_SOUND                = new File(
            "");

    /**
     * Indicates if file chooser currently showing
     *
     * @return {@code true} if file chooser currently showing
     */
    public static boolean fileChooserShowing()
    {
        synchronized (JHelpFileChooser2D.SHOWING)
        {
            return JHelpFileChooser2D.SHOWING.get();
        }
    }

    /**
     * Show file chooser.<br>
     * Start at "home", filter all files except hidden and virtual link
     *
     * @param fileChooser2DListener Listener to callback when user make his choice
     */
    public static void showFileChooser(final JHelpFileChooser2DListener fileChooser2DListener)
    {
        JHelpFileChooser2D.showFileChooser(fileChooser2DListener, new FileFilter(), UtilIO.obtainHomeDirectory());
    }

    /**
     * Show file chooser.<br>
     * Filter all files except hidden and virtual link
     *
     * @param fileChooser2DListener Listener to callback when user make his choice
     * @param directory             Starting directory
     */
    public static void showFileChooser(final JHelpFileChooser2DListener fileChooser2DListener, final File directory)
    {
        JHelpFileChooser2D.showFileChooser(fileChooser2DListener, new FileFilter(), directory);
    }

    /**
     * Show file chooser.<br>
     * Start at "home"
     *
     * @param fileChooser2DListener Listener to callback when user make his choice
     * @param fileFilter            Filter to use
     */
    public static void showFileChooser(
            final JHelpFileChooser2DListener fileChooser2DListener, final FileFilter fileFilter)
    {
        JHelpFileChooser2D.showFileChooser(fileChooser2DListener, fileFilter, UtilIO.obtainHomeDirectory());
    }

    /**
     * Show file chooser
     *
     * @param fileChooser2DListener Listener to callback when user make his choice
     * @param fileFilter            Filter to use
     * @param directory             Starting directory
     */
    public static void showFileChooser(
            final JHelpFileChooser2DListener fileChooser2DListener, final FileFilter fileFilter, final File directory)
    {
        if (fileChooser2DListener == null)
        {
            throw new NullPointerException("fileChooser2DListener mustn't be null");
        }

        synchronized (JHelpFileChooser2D.SHOWING)
        {
            if (JHelpFileChooser2D.SHOWING.get())
            {
                throw new IllegalStateException("The file chooser is already showing");
            }

            JHelpFileChooser2D.SHOWING.set(true);
        }

        JHelpFileChooser2D.FILE_CHOOSER.initialize(fileChooser2DListener, fileFilter, directory);
        JHelpFileChooser2D.FILE_CHOOSER.setVisible(true);
        JHelpFileChooser2D.FILE_CHOOSER.automaticRefresh(true);
    }

    /**
     * Image loader
     *
     * @author JHelp
     */
    class LoadImage
            extends Thread
    {
        /**
         * Indicates if loadder living
         */
        private boolean alive;
        /**
         * Next file to load
         */
        private File    nextFile;
        /**
         * Indicates if loading is in waiting mode
         */
        private boolean waiting;

        /**
         * Create a new instance of LoadImage
         */
        LoadImage()
        {
            this.alive = this.waiting = false;
        }

        /**
         * Do the job : wait for next image and load it <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @see Thread#run()
         */
        @Override
        public void run()
        {
            while (this.alive)
            {
                File file = null;

                synchronized (JHelpFileChooser2D.LOCK)
                {
                    file = this.nextFile;
                    this.nextFile = null;
                }

                JHelpFileChooser2D.this.labelImage2DPreview.image(JHelpImage.DUMMY);

                if (file != JHelpFileChooser2D.NO_IMAGE)
                {
                    JHelpFileChooser2D.this.labelImage2DPreview.file(file);
                }

                synchronized (JHelpFileChooser2D.LOCK)
                {
                    while ((this.nextFile == null) && (this.alive))
                    {
                        synchronized (JHelpFileChooser2D.LOCK)
                        {
                            this.waiting = true;

                            try
                            {
                                JHelpFileChooser2D.LOCK.wait();
                            }
                            catch (final Exception ignored)
                            {
                            }

                            this.waiting = false;
                        }
                    }
                }
            }
        }

        /**
         * Set next image to load
         *
         * @param file Next image file
         */
        public void setImage(final File file)
        {
            synchronized (JHelpFileChooser2D.LOCK)
            {
                this.nextFile = file;

                if (!this.alive)
                {
                    this.alive = true;
                    this.start();
                }
                else if (this.waiting)
                {
                    JHelpFileChooser2D.LOCK.notify();
                }
            }
        }

        /**
         * Stop properly the loader
         */
        public void stopLoading()
        {
            synchronized (JHelpFileChooser2D.LOCK)
            {
                this.alive = false;
                if (this.waiting)
                {
                    JHelpFileChooser2D.LOCK.notify();
                }
            }
        }
    }

    /**
     * Loader of sounds
     *
     * @author JHelp
     */
    class LoadSound
            extends Thread
    {
        /**
         * Indicates if loader is alive
         */
        private boolean    alive;
        /**
         * Next file to load
         */
        private File       nextFile;
        /**
         * Current playing sound
         */
        private JHelpSound sound;
        /**
         * Indicates if its waiting mode
         */
        private boolean    waiting;

        /**
         * Create a new instance of LoadSound
         */
        LoadSound()
        {
            this.alive = this.waiting = false;
        }

        /**
         * Set next sound file
         *
         * @param file New sound file
         */
        public void setSound(final File file)
        {
            synchronized (JHelpFileChooser2D.LOCK_SOUND)
            {
                this.nextFile = file;

                if (!this.alive)
                {
                    this.alive = true;
                    this.start();
                }
                else if (this.waiting)
                {
                    JHelpFileChooser2D.LOCK_SOUND.notify();
                }
            }
        }

        /**
         * Stop properly the loader
         */
        public void stopLoading()
        {
            synchronized (JHelpFileChooser2D.LOCK_SOUND)
            {
                this.alive = false;
                if (this.waiting)
                {
                    JHelpFileChooser2D.LOCK_SOUND.notify();
                }
            }
        }

        /**
         * Do the job : wait for next sound, load it and play it <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @see Thread#run()
         */
        @Override
        public void run()
        {
            while (this.alive)
            {
                File file = null;

                synchronized (JHelpFileChooser2D.LOCK_SOUND)
                {
                    file = this.nextFile;
                    this.nextFile = null;
                }

                if (this.sound != null)
                {
                    this.sound.stop();
                    this.sound.destroy();
                    this.sound = null;
                }

                if (file != JHelpFileChooser2D.NO_SOUND)
                {
                    this.sound = SoundFactory.getSoundFromFile(file);
                    if (this.sound != null)
                    {
                        this.sound.loop();
                    }
                }

                synchronized (JHelpFileChooser2D.LOCK_SOUND)
                {
                    final long lastTime = System.currentTimeMillis();

                    while ((this.nextFile == null) && (this.alive))
                    {
                        synchronized (JHelpFileChooser2D.LOCK_SOUND)
                        {
                            this.waiting = true;

                            try
                            {
                                JHelpFileChooser2D.LOCK_SOUND.wait();
                            }
                            catch (final Exception ignored)
                            {
                            }

                            this.waiting = false;
                        }
                    }

                    if (this.sound != null)
                    {
                        this.sound.stop();
                        this.sound.destroy();
                        this.sound = null;
                    }

                    while ((System.currentTimeMillis() - lastTime) < 4096)
                    {
                        synchronized (JHelpFileChooser2D.LOCK_SOUND)
                        {
                            this.waiting = true;

                            try
                            {
                                JHelpFileChooser2D.LOCK_SOUND.wait(512);
                            }
                            catch (final Exception ignored)
                            {
                            }

                            this.waiting = false;
                        }
                    }
                }
            }

            if (this.sound != null)
            {
                this.sound.stop();
                this.sound.destroy();
                this.sound = null;
            }
        }


    }

    /**
     * Indicates if action is valid one (ok)
     */
    private       boolean                    actionValid;
    /**
     * Background of ok button
     */
    private final JHelpBackgroundSaussage    backGroundOk;
    /**
     * Current listener to callback
     */
    private       JHelpFileChooser2DListener fileChooser2DListener;
    /**
     * File explorer
     */
    private final JHelpFileExplorer2D        fileExplorer2D;
    /**
     * Thread for load image preview
     */
    private       LoadImage                  loadImage;
    /**
     * Thread for load an play sound
     */
    private       LoadSound                  loadSound;
    /**
     * Current sound
     */
    private       JHelpSound                 sound;
    /**
     * Image preview
     */
    final         JHelpLabelImage2D          labelImage2DPreview;

    /**
     * Create a new instance of JHelpFileChooser2D
     */
    private JHelpFileChooser2D()
    {
        super(ResourcesGUI.RESOURCE_TEXT.getText(ResourcesGUI.FILE_CHOOSER_TITLE_HEADER), new JHelpBorderLayout());

        this.setExitAllOnClose(false);
        this.setDisposeOnClose(false);

        this.fileExplorer2D = new JHelpFileExplorer2D();
        this.labelImage2DPreview = new JHelpLabelImage2D(JHelpImage.DUMMY);

        this.addComponent2D(this.fileExplorer2D, JHelpBorderLayoutConstraints.LEFT);
        this.addComponent2D(new JHelpScrollPane2D(this.labelImage2DPreview), JHelpBorderLayoutConstraints.CENTER);

        final JHelpPanel2D panel2d = new JHelpPanel2D(new JHelpTableLayout());

        JHelpLabelText2D button = new JHelpLabelText2D(JHelpFileChooser2D.FONT, "Ok", JHelpTextAlign.CENTER, 0xFF000000,
                                                       0);
        button.linkToResourceText(ResourcesGUI.RESOURCE_TEXT, ResourcesGUI.OPTION_PANE_OK);
        JHelpButtonBehavior.giveButtonBehavior(JHelpFileChooser2D.ID_OK_BUTTON, button, this);
        this.backGroundOk = new JHelpBackgroundSaussage(button, 0x80808080);
        JHelpButtonBehavior.giveButtonBehavior(JHelpFileChooser2D.ID_OK_BUTTON, this.backGroundOk, this);
        panel2d.addComponent2D(this.backGroundOk, new JHelpTableLayout.JHelpTableLayoutConstraints(0, 0));

        button = new JHelpLabelText2D(JHelpFileChooser2D.FONT, "Cancel", JHelpTextAlign.CENTER, 0xFF000000, 0);
        button.linkToResourceText(ResourcesGUI.RESOURCE_TEXT, ResourcesGUI.OPTION_PANE_CANCEL);
        JHelpButtonBehavior.giveButtonBehavior(JHelpFileChooser2D.ID_CANCEL_BUTTON, button, this);
        final JHelpBackgroundSaussage saussage = new JHelpBackgroundSaussage(button, 0xFFFFFFFF);
        JHelpButtonBehavior.giveButtonBehavior(JHelpFileChooser2D.ID_CANCEL_BUTTON, saussage, this);
        panel2d.addComponent2D(saussage, new JHelpTableLayout.JHelpTableLayoutConstraints(2, 0));

        this.addComponent2D(panel2d, JHelpBorderLayoutConstraints.BOTTOM);

        this.fileExplorer2D.registerFileExplorerListener(this);

        this.setAlwaysOnTop(true);
    }

    /**
     * Do cancel action
     */
    private void doCancel()
    {
        this.doCancel(false);
    }

    /**
     * Do cancel action
     *
     * @param onClosing Indicates if already by {@link #canCloseNow()}
     */
    private void doCancel(final boolean onClosing)
    {
        this.actionValid = true;
        ThreadManager.parallel(JHelpFileChooser2D.SIGNAL_CHOOSE_OR_CANCEL,
                               new Triplet<>(false, this.fileChooser2DListener, null));

        if (!onClosing)
        {
            this.closeFrame();
        }
    }

    /**
     * Do ok action
     */
    private void doOk()
    {
        this.actionValid = true;
        ThreadManager.parallel(JHelpFileChooser2D.SIGNAL_CHOOSE_OR_CANCEL,
                               new Triplet<>(true, this.fileChooser2DListener, this.fileExplorer2D.selectedFile()));
        this.closeFrame();
    }

    /**
     * Called when file chooser about to close <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return {@code true}, close is allowed
     * @see jhelp.gui.JHelpFrame#canCloseNow()
     */
    @Override
    protected boolean canCloseNow()
    {
        if (this.loadImage != null)
        {
            this.loadImage.setImage(JHelpFileChooser2D.NO_IMAGE);
            this.loadImage.stopLoading();
        }
        this.loadImage = null;

        if (this.loadSound != null)
        {
            this.loadSound.setSound(JHelpFileChooser2D.NO_SOUND);
            this.loadSound.stopLoading();
        }
        this.loadSound = null;

        synchronized (JHelpFileChooser2D.LOCK_SOUND)
        {
            if (this.sound != null)
            {
                this.sound.stop();
                this.sound.destroy();
                this.sound = null;
            }
        }

        this.labelImage2DPreview.image(JHelpImage.DUMMY);
        this.automaticRefresh(false);

        if (!this.actionValid)
        {
            this.doCancel(true);
        }

        synchronized (JHelpFileChooser2D.SHOWING)
        {
            JHelpFileChooser2D.SHOWING.set(false);
        }
        return true;
    }

    /**
     * Called when one button Ok or Cancel is pressed <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param component2d Button clicked
     * @param identifier  Action identifier
     * @see jhelp.gui.twoD.JHelpActionListener#actionAppend(JHelpComponent2D, int)
     */
    @Override
    public void actionAppend(final JHelpComponent2D component2d, final int identifier)
    {
        switch (identifier)
        {
            case JHelpFileChooser2D.ID_CANCEL_BUTTON:
                this.doCancel();
                break;
            case JHelpFileChooser2D.ID_OK_BUTTON:
                if ((this.fileExplorer2D.selectedIndex() >= 0) && (!this.fileExplorer2D.selectedFile()
                                                                                       .isDirectory()))
                {
                    this.doOk();
                }
                break;
        }
    }

    /**
     * Called if a file is choosen <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param fileExplorer2D File explorer source
     * @param selectedFile   Choosen file
     * @param selectedIndex  Choosen fille index
     * @see jhelp.gui.twoD.JHelpFileExplorerListener#fileExplorerFileChoosen(jhelp.gui.twoD.JHelpFileExplorer2D, File,
     * int)
     */
    @Override
    public void fileExplorerFileChoosen(
            final JHelpFileExplorer2D fileExplorer2D, final File selectedFile, final int selectedIndex)
    {
        this.doOk();
    }

    /**
     * Called if file is selected <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param fileExplorer2D File explorer source
     * @param selectedFile   Selected file
     * @param selectedIndex  Selected file index
     * @see jhelp.gui.twoD.JHelpFileExplorerListener#fileExplorerSelectionChange(jhelp.gui.twoD.JHelpFileExplorer2D,
     * File, int)
     */
    @Override
    public void fileExplorerSelectionChange(
            final JHelpFileExplorer2D fileExplorer2D, final File selectedFile, final int selectedIndex)
    {
        String title;
        if ((selectedFile == null) || (selectedFile == JHelpFileExplorerModel.PARENT))
        {
            title = ResourcesGUI.RESOURCE_TEXT.getText(ResourcesGUI.FILE_CHOOSER_TITLE_HEADER) + " : "
                    + this.fileExplorer2D.currentDirectory().getAbsolutePath();
        }
        else
        {
            title = ResourcesGUI.RESOURCE_TEXT.getText(ResourcesGUI.FILE_CHOOSER_TITLE_HEADER) + " : " +
                    selectedFile.getAbsolutePath();
        }

        this.setTitle(title);

        if ((selectedFile == null) || (selectedFile.isDirectory()))
        {
            this.backGroundOk.colorBackground(0xC0C0C0C0);
        }
        else
        {
            this.backGroundOk.colorBackground(0xFFFFFFFF);
        }

        if ((selectedFile == null) || (!JHelpFileChooser2D.FILTER_IMAGE.accept(selectedFile)))
        {
            this.loadImage.setImage(JHelpFileChooser2D.NO_IMAGE);

            if ((selectedFile != null) && (JHelpFileChooser2D.FILTER_SOUND.accept(selectedFile)))
            {
                this.loadSound.setSound(selectedFile);
            }
            else
            {
                this.loadSound.setSound(JHelpFileChooser2D.NO_SOUND);
            }

            return;
        }
        this.loadSound.setSound(JHelpFileChooser2D.NO_SOUND);
        this.loadImage.setImage(selectedFile);
    }

    /**
     * Initialize the file chooser
     *
     * @param fileChooser2DListener Listener to call back
     * @param fileFilter            Filter to use
     * @param directory             Start directory
     */
    public void initialize(
            final JHelpFileChooser2DListener fileChooser2DListener, final FileFilter fileFilter, final File directory)
    {
        this.loadImage = new LoadImage();
        this.loadSound = new LoadSound();
        this.fileChooser2DListener = fileChooser2DListener;
        this.fileExplorer2D.fileFilter(fileFilter);
        this.fileExplorer2D.directory(directory);
        this.actionValid = false;
        this.backGroundOk.colorBackground(0xC0C0C0C0);
    }
}
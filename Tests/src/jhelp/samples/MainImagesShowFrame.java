package jhelp.samples;

import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import jhelp.util.debug.Debug;
import jhelp.util.filter.FileFilter;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.UtilGUI;
import jhelp.util.io.UtilIO;
import jhelp.util.list.ThrowSet;
import jhelp.util.thread.ThreadManager;
import jhelp.util.thread.RunnableTask;

/**
 * Created by jhelp on 17/07/17.
 */
public class MainImagesShowFrame extends JFrame implements RunnableTask
{
    private static final long IMAGE_TIME = 2048;
    private       long           startImageTime;
    private final ComponentImage componentImage;
    private final ThrowSet<File> images;
    private final ThrowSet<File> directories;
    private final FileFilter     fileFilter;

    public MainImagesShowFrame()
    {
        this.componentImage = new ComponentImage(JHelpImage.DUMMY);
        this.componentImage.expand(true);
        this.setLayout(new BorderLayout());
        this.add(this.componentImage, BorderLayout.CENTER);
        UtilGUI.takeAllScreen(this);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.fileFilter = FileFilter.createFilterForImage();
        this.images = new ThrowSet<>();
        this.directories = new ThrowSet<>();
        ThreadManager.parallel(this);
    }

    /**
     * Play the task
     */
    @Override
    public void run()
    {
        if (!this.images.isEmpty() &&
            System.currentTimeMillis() - this.startImageTime > MainImagesShowFrame.IMAGE_TIME)
        {
            File imageFile = this.images.take();
            Debug.verbose(imageFile.getAbsolutePath());

            try
            {
                JHelpImage image = JHelpImage.loadImage(imageFile);
                this.componentImage.image(image);
                this.startImageTime = System.currentTimeMillis();
            }
            catch (Throwable throwable)
            {
                Debug.exception(throwable, "Failed to load: ", imageFile.getAbsolutePath());
            }
        }

        if (this.directories.isEmpty())
        {
            for (File root : File.listRoots())
            {
                if (root.exists() && root.canRead())
                {
                    this.directories.throwElement(root);
                }
            }

            File directory = UtilIO.obtainOutsideDirectory();

            while (directory != null && directory.exists() && directory.canRead())
            {
                this.directories.throwElement(directory);
                directory = directory.getParentFile();
            }

            directory = UtilIO.obtainHomeDirectory();

            while (directory != null && directory.exists() && directory.canRead())
            {
                this.directories.throwElement(directory);
                directory = directory.getParentFile();
            }

            directory = new File(UtilIO.obtainHomeDirectory(), "Images");

            while (directory != null && directory.exists() && directory.canRead())
            {
                this.directories.throwElement(directory);
                directory = directory.getParentFile();
            }
        }

        File   directory = this.directories.take();
        File[] children  = directory.listFiles((java.io.FileFilter) this.fileFilter);

        if (children != null)
        {
            for (File child : children)
            {
                if (child.isDirectory())
                {
                    this.directories.throwElement(child);
                }
                else
                {
                    this.images.throwElement(child);
                }
            }
        }

        ThreadManager.parallel(this);
    }
}

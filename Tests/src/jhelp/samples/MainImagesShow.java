package jhelp.samples;

import jhelp.util.gui.UtilGUI;

/**
 * Created by jhelp on 17/07/17.
 */
public class MainImagesShow
{
    public static void main(String[] arguments)
    {
        UtilGUI.initializeGUI();
        MainImagesShowFrame mainImagesShowFrame = new MainImagesShowFrame();
        mainImagesShowFrame.setVisible(true);
    }
}

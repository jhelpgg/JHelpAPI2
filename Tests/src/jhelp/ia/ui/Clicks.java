package jhelp.ia.ui;

import jhelp.util.gui.UtilGUI;
import jhelp.util.util.Utilities;

/**
 * Created by jhelp on 02/07/17.
 */
public class Clicks
{
    public static void main(String[] args)
    {
        int time = 10;

        if (args != null && args.length > 0)
        {
            try
            {
                time = Integer.parseInt(args[0]);
            }
            catch (Exception ignored)
            {
            }
        }

        Utilities.sleep(4096);

        for (int i = 0; i < time; i++)
        {
            UtilGUI.simulateMouseClick(16);
            Utilities.sleep(16);
        }
    }
}

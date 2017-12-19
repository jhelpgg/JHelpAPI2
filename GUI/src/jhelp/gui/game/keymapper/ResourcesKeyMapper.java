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
package jhelp.gui.game.keymapper;

import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.util.HashMap;
import jhelp.gui.game.ActionKey;
import jhelp.util.debug.Debug;
import jhelp.util.gui.JHelpMask;
import jhelp.util.resources.ResourceText;
import jhelp.util.resources.Resources;

/**
 * Resources used for {@link KeyMapper}
 *
 * @author JHelp
 */
public class ResourcesKeyMapper
{
    static
    {
        RESOURCES = new Resources(ResourcesKeyMapper.class);
        RESOURCE_TEXT = ResourcesKeyMapper.RESOURCES.obtainResourceText("keymapperTexts");
    }

    /**
     * List of known mappings
     */
    private static final HashMap<Integer, JHelpMask> KNOWN_MAPPING = new HashMap<Integer, JHelpMask>();
    /**
     * Resources access
     */
    public static final Resources    RESOURCES;
    /**
     * Resources text to use
     */
    public static final ResourceText RESOURCE_TEXT;

    /**
     * Obtain the mask to use for represents a key code
     *
     * @param keyCode Key code
     * @return Mask to use or {@code null} if not found
     */
    public static JHelpMask obtainMask(final int keyCode)
    {
        JHelpMask mask = ResourcesKeyMapper.KNOWN_MAPPING.get(keyCode);

        if (mask != null)
        {
            return mask;
        }

        String resourceName = null;

        switch (keyCode)
        {
            case KeyEvent.VK_0:
                resourceName = "0.mask";
                break;
            case KeyEvent.VK_1:
                resourceName = "1.mask";
                break;
            case KeyEvent.VK_2:
                resourceName = "2.mask";
                break;
            case KeyEvent.VK_3:
                resourceName = "3.mask";
                break;
            case KeyEvent.VK_4:
                resourceName = "4.mask";
                break;
            case KeyEvent.VK_5:
                resourceName = "5.mask";
                break;
            case KeyEvent.VK_6:
                resourceName = "6.mask";
                break;
            case KeyEvent.VK_7:
                resourceName = "7.mask";
                break;
            case KeyEvent.VK_8:
                resourceName = "8.mask";
                break;
            case KeyEvent.VK_9:
                resourceName = "9.mask";
                break;
            case KeyEvent.VK_NUMPAD0:
                resourceName = "0.mask";
                break;
            case KeyEvent.VK_NUMPAD1:
                resourceName = "1.mask";
                break;
            case KeyEvent.VK_NUMPAD2:
                resourceName = "2.mask";
                break;
            case KeyEvent.VK_NUMPAD3:
                resourceName = "3.mask";
                break;
            case KeyEvent.VK_NUMPAD4:
                resourceName = "4.mask";
                break;
            case KeyEvent.VK_NUMPAD5:
                resourceName = "5.mask";
                break;
            case KeyEvent.VK_NUMPAD6:
                resourceName = "6.mask";
                break;
            case KeyEvent.VK_NUMPAD7:
                resourceName = "7.mask";
                break;
            case KeyEvent.VK_NUMPAD8:
                resourceName = "8.mask";
                break;
            case KeyEvent.VK_NUMPAD9:
                resourceName = "9.mask";
                break;
            case KeyEvent.VK_A:
                resourceName = "A.mask";
                break;
            case KeyEvent.VK_B:
                resourceName = "B.mask";
                break;
            case KeyEvent.VK_C:
                resourceName = "C.mask";
                break;
            case KeyEvent.VK_D:
                resourceName = "D.mask";
                break;
            case KeyEvent.VK_E:
                resourceName = "E.mask";
                break;
            case KeyEvent.VK_F:
                resourceName = "F.mask";
                break;
            case KeyEvent.VK_G:
                resourceName = "G.mask";
                break;
            case KeyEvent.VK_H:
                resourceName = "H.mask";
                break;
            case KeyEvent.VK_I:
                resourceName = "I.mask";
                break;
            case KeyEvent.VK_J:
                resourceName = "J.mask";
                break;
            case KeyEvent.VK_K:
                resourceName = "K.mask";
                break;
            case KeyEvent.VK_L:
                resourceName = "L.mask";
                break;
            case KeyEvent.VK_M:
                resourceName = "M.mask";
                break;
            case KeyEvent.VK_N:
                resourceName = "N.mask";
                break;
            case KeyEvent.VK_O:
                resourceName = "O.mask";
                break;
            case KeyEvent.VK_P:
                resourceName = "P.mask";
                break;
            case KeyEvent.VK_Q:
                resourceName = "Q.mask";
                break;
            case KeyEvent.VK_R:
                resourceName = "R.mask";
                break;
            case KeyEvent.VK_S:
                resourceName = "S.mask";
                break;
            case KeyEvent.VK_T:
                resourceName = "T.mask";
                break;
            case KeyEvent.VK_U:
                resourceName = "U.mask";
                break;
            case KeyEvent.VK_V:
                resourceName = "V.mask";
                break;
            case KeyEvent.VK_W:
                resourceName = "W.mask";
                break;
            case KeyEvent.VK_X:
                resourceName = "X.mask";
                break;
            case KeyEvent.VK_Y:
                resourceName = "Y.mask";
                break;
            case KeyEvent.VK_Z:
                resourceName = "Z.mask";
                break;
            case KeyEvent.VK_ALT:
                resourceName = "Alt.mask";
                break;
            case KeyEvent.VK_DOWN:
                resourceName = "ArrowDown.mask";
                break;
            case KeyEvent.VK_LEFT:
                resourceName = "ArrowLeft.mask";
                break;
            case KeyEvent.VK_RIGHT:
                resourceName = "ArrowRight.mask";
                break;
            case KeyEvent.VK_UP:
                resourceName = "ArrowUp.mask";
                break;
            case KeyEvent.VK_CONTROL:
                resourceName = "Ctrl.mask";
                break;
            case KeyEvent.VK_SHIFT:
                resourceName = "Shift.mask";
                break;
            case KeyEvent.VK_SPACE:
                resourceName = "SPACE.mask";
                break;
            case KeyEvent.VK_TAB:
                resourceName = "TAB.mask";
                break;
            case KeyEvent.VK_ESCAPE:
                resourceName = "Echap.mask";
                break;
            case KeyEvent.VK_PAGE_UP:
                resourceName = "Previous.mask";
                break;
            case KeyEvent.VK_PAGE_DOWN:
                resourceName = "Next.mask";
                break;
        }

        if (resourceName == null)
        {
            return null;
        }

        InputStream inputStream = null;

        try
        {
            inputStream = ResourcesKeyMapper.RESOURCES.obtainResourceStream(resourceName);

            mask = JHelpMask.load(inputStream);

            ResourcesKeyMapper.KNOWN_MAPPING.put(keyCode, mask);
        }
        catch (final Exception exception)
        {
            Debug.exception(exception, "Load resource ", resourceName, " for key code ", keyCode, " failed !");
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

        return mask;
    }

    /**
     * Obtain text associate to an action key
     *
     * @param actionKey Action key
     * @return Associated text
     */
    public static String obtainText(final ActionKey actionKey)
    {
        return ResourcesKeyMapper.RESOURCE_TEXT.getText(actionKey.name());
    }
}
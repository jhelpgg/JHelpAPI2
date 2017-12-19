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
package jhelp.gui.samples.game.ui;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import jhelp.gui.action.GenericAction;
import jhelp.gui.game.ButtonShape;
import jhelp.gui.game.JHelpGameDynamic;
import jhelp.gui.game.SensitiveAnimation;
import jhelp.gui.game.SensitiveButton;
import jhelp.util.debug.Debug;
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;

public class SampleDynamicFrame
        extends JHelpGameDynamic
{
    class ButtonAction
            extends GenericAction
    {
        ButtonAction()
        {
            super("Action");
        }

        /**
         * <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param actionEvent
         * @see jhelp.gui.action.GenericAction#doActionPerformed(java.awt.event.ActionEvent)
         */
        @Override
        protected void doActionPerformed(final ActionEvent actionEvent)
        {
            JOptionPane.showMessageDialog(SampleDynamicFrame.this, "Action !");
        }
    }

    private JHelpImage imageUI;

    public SampleDynamicFrame()
    {
        super("Sample Dynamic");
    }

    @Override
    protected void constructGameInstance()
    {
        try
        {
            this.imageUI = JHelpImage.loadImage(SampleDynamicFrame.class.getResourceAsStream("574.png"));

        }
        catch (final Exception exception)
        {
            Debug.exception(exception);
        }

        final SensitiveButton button = new SensitiveButton(this, 0, 0, new ButtonAction(), JHelpFont.DEFAULT,
                                                           0xFFFFFFFF, 0xFF000000,
                                                           ButtonShape.ROUND_RECTANGLE);
        final SensitiveAnimation animation = new SensitiveAnimation(0, 0, 0, 512, 512);
        animation.addSensitiveElement(button);
        this.addSensitiveAnimation(animation);
    }
}
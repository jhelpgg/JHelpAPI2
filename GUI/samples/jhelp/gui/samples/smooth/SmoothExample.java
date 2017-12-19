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
package jhelp.gui.samples.smooth;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import jhelp.gui.action.GenericAction;
import jhelp.gui.smooth.DialogDescription;
import jhelp.gui.smooth.JHelpButtonAlignSmooth;
import jhelp.gui.smooth.JHelpButtonSmooth;
import jhelp.gui.smooth.JHelpComboBoxSmooth;
import jhelp.gui.smooth.JHelpComponentSmooth;
import jhelp.gui.smooth.JHelpConstantsSmooth;
import jhelp.gui.smooth.JHelpEditTextSmooth;
import jhelp.gui.smooth.JHelpFrameSmooth;
import jhelp.gui.smooth.JHelpLabelImageSmooth;
import jhelp.gui.smooth.JHelpLabelTextSmooth;
import jhelp.gui.smooth.JHelpListSmooth;
import jhelp.gui.smooth.JHelpOptionPaneSmooth;
import jhelp.gui.smooth.JHelpPanelSmooth;
import jhelp.gui.smooth.JHelpProgressSmooth;
import jhelp.gui.smooth.JHelpScrollModeSmooth;
import jhelp.gui.smooth.JHelpScrollPaneSmooth;
import jhelp.gui.smooth.OptionPaneButton;
import jhelp.gui.smooth.OptionPaneType;
import jhelp.gui.smooth.ShortCut;
import jhelp.gui.smooth.event.JHelpListSmoothSelectListener;
import jhelp.gui.smooth.event.JHelpOptionPaneSmoothListener;
import jhelp.gui.smooth.event.SmoothEditTextListener;
import jhelp.gui.smooth.layout.JHelpBorderConstraintsSmooth;
import jhelp.gui.smooth.layout.JHelpBorderLayoutSmooth;
import jhelp.gui.smooth.layout.JHelpTableConstraintsSmooth;
import jhelp.gui.smooth.layout.JHelpTableLayoutSmooth;
import jhelp.gui.smooth.model.JHelpListSmoothModelDefault;
import jhelp.gui.smooth.renderer.JHelpListSmoothRenderer;
import jhelp.gui.smooth.shape.ShadowLevel;
import jhelp.gui.smooth.shape.SmoothEllipse;
import jhelp.gui.smooth.shape.SmoothRoundRectangle;
import jhelp.gui.smooth.shape.SmoothSausage;
import jhelp.util.debug.Debug;
import jhelp.util.gui.JHelpGradient;
import jhelp.util.gui.JHelpGradientAlphaCircle;
import jhelp.util.gui.JHelpGradientHorizontal;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpTextAlign;
import jhelp.util.gui.UtilGUI;
import jhelp.util.math.JHelpRandom;
import jhelp.util.util.Utilities;

public class SmoothExample
        extends JHelpFrameSmooth
{
    static final int SIZE = 6;
    static SmoothExample smoothExample;

    public static void main(final String[] args)
    {
        UtilGUI.initializeGUI();
        SmoothExample.smoothExample = new SmoothExample();
        SmoothExample.smoothExample.setVisible(true);
    }

    class ActionClick
            extends GenericAction
    {
        private final int id;

        ActionClick(final int id)
        {
            super("Button " + id);
            this.id = id;
        }

        @Override
        protected void doActionPerformed(final ActionEvent actionEvent)
        {
            Debug.information("Click on ", this.id);
        }
    }

    class ActionCloseDialog
            extends GenericAction
    {
        ActionCloseDialog()
        {
            super("Close dialog");
        }

        @Override
        protected void doActionPerformed(final ActionEvent actionEvent)
        {
            SmoothExample.this.hideDialog(42);
        }
    }

    class ActionCloseFrame
            extends GenericAction
    {
        ActionCloseFrame()
        {
            super("Close");

            final JHelpGradientAlphaCircle gradientAlphaCircle = new JHelpGradientAlphaCircle(
                    JHelpConstantsSmooth.COLOR_LIME_0500,
                    JHelpGradientAlphaCircle.MULTIPLIER_VERY_THICK);

            final JHelpImage image = new JHelpImage(64, 64, 0x00FFFFFF);
            image.startDrawMode();
            image.fillRoundRectangle(0, 0, 64, 64, 16, 16, gradientAlphaCircle);
            image.drawThickLine(16, 16, 48, 48, SmoothExample.SIZE, JHelpConstantsSmooth.COLOR_RED_0500);
            image.drawThickLine(16, 48, 48, 16, SmoothExample.SIZE, JHelpConstantsSmooth.COLOR_RED_0500);
            image.endDrawMode();

            this.icons(image);
        }

        @Override
        protected void doActionPerformed(final ActionEvent actionEvent)
        {
            SmoothExample.smoothExample.closeFrame();
        }
    }

    class ActionPrintSomething
            extends GenericAction
            implements SmoothEditTextListener, JHelpListSmoothSelectListener<String>, JHelpOptionPaneSmoothListener
    {
        ActionPrintSomething()
        {
            super("Print");
        }

        @Override
        public void editTextEnterTyped(final JHelpEditTextSmooth editTextSmooth)
        {
            // {@todo} TODO Implements editTextEnterTyped
            Debug.todo("Implements editTextEnterTyped : ", editTextSmooth.getText());
        }

        @Override
        public void elementSelected(final JHelpListSmooth<String> list, final String element, final int index)
        {
            // {@todo} TODO Implements elementSelected
            Debug.todo("Implements elementSelected : ", element);
        }

        @Override
        protected void doActionPerformed(final ActionEvent actionEvent)
        {
            final JHelpButtonAlignSmooth alignSmooth = JHelpRandom.random(JHelpButtonAlignSmooth.class);
            SmoothExample.this.buttonSmooth.buttonAlignSmooth(alignSmooth);
            Debug.information("Something ! : ", alignSmooth);
            SmoothExample.this.actionPrintSomething.setEnabled(!SmoothExample.this.actionPrintSomething.isEnabled());
            SmoothExample.this.showDialog(123);
        }

        @Override
        public void optionPaneButtonClicked(final JHelpOptionPaneSmooth optionPane, final OptionPaneButton button)
        {
            switch (button)
            {
                case YES:
                    if (optionPane.id() == SmoothExample.this.optionPaneSmooth.id())
                    {
                        SmoothExample.this.showDialog(42);
                    }
                    else if (optionPane.id() == SmoothExample.this.optionPaneExit.id())
                    {
                        SmoothExample.this.sureToExit = true;
                        SmoothExample.this.closeFrame();
                    }
                    break;
            }
        }


    }

    class ProgressThread
            extends Thread
    {
        ProgressThread()
        {
        }

        @Override
        public void run()
        {
            Utilities.sleep(16386);
            SmoothExample.this.progressSmooth.startProgress(1000);
            Utilities.sleep(1024);

            for (int i = 0; i <= 1000; i++)
            {
                SmoothExample.this.progressSmooth.updateProgress(i);
                Utilities.sleep(16);
            }
        }
    }

    class TextRenderer
            implements JHelpListSmoothRenderer<String>
    {
        TextRenderer()
        {
        }

        @Override
        public JHelpComponentSmooth createComponent(final String element)
        {
            final JHelpLabelTextSmooth text = new JHelpLabelTextSmooth(element);
            text.shadowLevel(ShadowLevel.NO_SHADOW);
            return text;
        }

        @Override
        public void transformComponent(
                final JHelpComponentSmooth component, final String element, final int indexInList,
                final boolean selected)
        {
            if (selected)
            {
                component.background(JHelpConstantsSmooth.COLOR_RED_0400);
            }
            else if ((indexInList % 2) == 0)
            {
                component.background(JHelpConstantsSmooth.COLOR_CYAN_0300);
            }
            else
            {
                component.background(JHelpConstantsSmooth.COLOR_WHITE);
            }
        }
    }

    final ActionPrintSomething actionPrintSomething;

    final JHelpButtonSmooth     buttonSmooth;
    final JHelpComboBoxSmooth   comboBoxSmooth;
    final JHelpOptionPaneSmooth optionPaneExit;
    final JHelpOptionPaneSmooth optionPaneSmooth;
    final JHelpProgressSmooth   progressSmooth;
    final JHelpScrollPaneSmooth scrollPaneSmooth;
    boolean sureToExit = false;

    public SmoothExample()
    {
        super("Example");

        final ActionCloseFrame actionCloseFrame = new ActionCloseFrame();
        this.actionPrintSomething = new ActionPrintSomething();

        try
        {
            this.backgroundImage(JHelpImage.loadImage(SmoothExample.class.getResourceAsStream("duck.jpg")));
        }
        catch (final Exception exception)
        {
            Debug.exception(exception, "Failed to load background image");
        }

        this.layout(new JHelpBorderLayoutSmooth());
        final JHelpEditTextSmooth editTextSmooth = new JHelpEditTextSmooth(JHelpConstantsSmooth.FONT_BODY_1, 30,
                                                                           JHelpConstantsSmooth.COLOR_BLACK,
                                                                           JHelpConstantsSmooth.COLOR_TEAL_0500);
        editTextSmooth.registerEditTextListener(this.actionPrintSomething);
        this.addComponent(editTextSmooth, JHelpBorderConstraintsSmooth.UP);

        final JHelpButtonSmooth buttonSmooth = new JHelpButtonSmooth(actionCloseFrame,
                                                                     JHelpButtonAlignSmooth.ICON_ONLY_IF_EXISTS);
        buttonSmooth.backgroundAndShadowColor(0);
        buttonSmooth.shadowLevel(ShadowLevel.NO_SHADOW);
        this.addComponent(buttonSmooth, JHelpBorderConstraintsSmooth.CORNER_RIGHT_UP);
        this.addComponent(new JHelpLabelTextSmooth("LEFT"), JHelpBorderConstraintsSmooth.LEFT);
        JHelpLabelTextSmooth textSmooth;

        final JHelpPanelSmooth panel = new JHelpPanelSmooth(new JHelpTableLayoutSmooth());
        textSmooth = new JHelpLabelTextSmooth("(0, 0) 1x1", JHelpTextAlign.CENTER);
        textSmooth.backgroundAndShadowColor(JHelpConstantsSmooth.COLOR_AMBER_0500);
        panel.addComponent(textSmooth, new JHelpTableConstraintsSmooth(0, 0));
        textSmooth = new JHelpLabelTextSmooth("- (5, 5) 2x1 -", JHelpTextAlign.CENTER);
        textSmooth.backgroundAndShadowColor(JHelpConstantsSmooth.COLOR_GREEN_0500);
        panel.addComponent(textSmooth, new JHelpTableConstraintsSmooth(5, 5, 2, 1));
        textSmooth = new JHelpLabelTextSmooth(
                "A phrase for test the 'ellipse size'\nklll \n (0, 3) 3x2 \n kll\nA phrase for test the 'ellipse size'",
                JHelpTextAlign.CENTER);
        textSmooth.shape(SmoothEllipse.ELLIPSE);
        textSmooth.backgroundAndShadowColor(JHelpConstantsSmooth.COLOR_RED_0500);
        panel.addComponent(textSmooth, new JHelpTableConstraintsSmooth(0, 3, 3, 2));
        this.progressSmooth = new JHelpProgressSmooth(JHelpConstantsSmooth.COLOR_GREEN_0800,
                                                      JHelpConstantsSmooth.COLOR_RED_0800,
                                                      JHelpConstantsSmooth.COLOR_BLACK,
                                                      JHelpConstantsSmooth.COLOR_BLACK,
                                                      JHelpConstantsSmooth.FONT_BODY_1);
        panel.addComponent(this.progressSmooth, new JHelpTableConstraintsSmooth(3, 0, 5, 1));
        final ProgressThread progressThread = new ProgressThread();
        progressThread.start();
        final JHelpLabelImageSmooth imageSmooth = new JHelpLabelImageSmooth();

        try
        {
            final JHelpImage image = JHelpImage.loadImage(SmoothExample.class.getResourceAsStream("elemental.gif"));
            imageSmooth.image(image);
            textSmooth.textureBackground(image);
            this.actionPrintSomething.largeIcon(image);
        }
        catch (final Exception exception)
        {
            Debug.exception(exception, "Failed to load other image");
        }

        textSmooth = new JHelpLabelTextSmooth("(1, 1) 2x2", JHelpTextAlign.CENTER);
        textSmooth.backgroundAndShadowColor(JHelpConstantsSmooth.COLOR_BLUE_0500);
        textSmooth.shape(new SmoothRoundRectangle());
        textSmooth.paintBackground(
                new JHelpGradient(JHelpConstantsSmooth.COLOR_RED_0500, JHelpConstantsSmooth.COLOR_GREEN_0500,
                                  JHelpConstantsSmooth.COLOR_BLUE_0500, JHelpConstantsSmooth.COLOR_BROWN_0500));
        panel.addComponent(textSmooth, new JHelpTableConstraintsSmooth(1, 1, 2, 2));

        imageSmooth.backgroundAndShadowColor(JHelpConstantsSmooth.COLOR_BLUE_GREY_0200);
        imageSmooth.shadowLevel(ShadowLevel.FAR);
        imageSmooth.shape(SmoothSausage.SAUSAGE);
        panel.addComponent(imageSmooth, new JHelpTableConstraintsSmooth(5, 7, 10, 2));

        this.buttonSmooth = new JHelpButtonSmooth(this.actionPrintSomething, JHelpButtonAlignSmooth.TEXT_OVER_ICON);
        this.buttonSmooth.foreground(JHelpConstantsSmooth.COLOR_DEEP_ORANGE_0500);
        panel.addComponent(this.buttonSmooth, new JHelpTableConstraintsSmooth(2, 6, 1, 1));

        this.comboBoxSmooth = new JHelpComboBoxSmooth(123456, this, JHelpConstantsSmooth.FONT_BUTTON, 0, "Test", "Try",
                                                      "Example", "Sample", "Banana", "Pear");
        panel.addComponent(this.comboBoxSmooth, new JHelpTableConstraintsSmooth(2, 8, 1, 1));

        this.scrollPaneSmooth = new JHelpScrollPaneSmooth(panel, JHelpScrollModeSmooth.SCROLL_BOTH);
        this.addComponent(/**/this.scrollPaneSmooth /* /panel /* */, JHelpBorderConstraintsSmooth.CENTER);

        this.addComponent(new JHelpLabelTextSmooth("RIGHT"), JHelpBorderConstraintsSmooth.RIGHT);

        textSmooth = new JHelpLabelTextSmooth("Down expand", JHelpTextAlign.CENTER);
        final JHelpGradientHorizontal gradientHorizontal = new JHelpGradientHorizontal(
                JHelpConstantsSmooth.COLOR_GREEN_A700,
                JHelpConstantsSmooth.COLOR_GREEN_A700);
        gradientHorizontal.addColor(50, JHelpConstantsSmooth.COLOR_GREEN_0050);
        textSmooth.paintBackground(gradientHorizontal);
        this.addComponent(textSmooth, JHelpBorderConstraintsSmooth.DOWN_EXPAND);

        this.defineShortCut(new ShortCut(KeyEvent.VK_P, true, true, true), this.actionPrintSomething);

        textSmooth = new JHelpLabelTextSmooth("Launch the second dialog ?");
        textSmooth.shape(SmoothSausage.SAUSAGE);
        textSmooth.background(JHelpConstantsSmooth.COLOR_ALPHA_HINT |
                              (JHelpConstantsSmooth.COLOR_WHITE & JHelpConstantsSmooth.MASK_COLOR));
        this.optionPaneSmooth = new JHelpOptionPaneSmooth(123, this, JHelpConstantsSmooth.ICON_QUESTION, textSmooth,
                                                          JHelpConstantsSmooth.COLOR_DEEP_PURPLE_0300,
                                                          JHelpConstantsSmooth.COLOR_DEEP_PURPLE_0300,
                                                          OptionPaneType.YES_NO);
        this.optionPaneSmooth.registerJHelpOptionPaneSmoothListener(this.actionPrintSomething);

        textSmooth = new JHelpLabelTextSmooth("Do you really want exit this example ?");
        textSmooth.shape(SmoothSausage.SAUSAGE);
        textSmooth.background(JHelpConstantsSmooth.COLOR_ALPHA_HINT |
                              (JHelpConstantsSmooth.COLOR_WHITE & JHelpConstantsSmooth.MASK_COLOR));
        this.optionPaneExit = new JHelpOptionPaneSmooth(321, this, JHelpConstantsSmooth.ICON_QUESTION, textSmooth,
                                                        JHelpConstantsSmooth.COLOR_DEEP_PURPLE_0300,
                                                        JHelpConstantsSmooth.COLOR_DEEP_PURPLE_0300,
                                                        OptionPaneType.YES_NO);
        this.optionPaneExit.registerJHelpOptionPaneSmoothListener(this.actionPrintSomething);
    }

    @Override
    protected boolean canCloseNow()
    {
        if (!this.sureToExit)
        {
            this.showDialog(this.optionPaneExit.id());

            return false;
        }

        return true;
    }

    @Override
    protected DialogDescription createDialog(final int dialogID)
    {
        if (dialogID == this.optionPaneSmooth.id())
        {
            return this.optionPaneSmooth.createDialogDescriDecsription();
        }

        if (dialogID == this.optionPaneExit.id())
        {
            return this.optionPaneExit.createDialogDescriDecsription();
        }

        if (dialogID == this.comboBoxSmooth.id())
        {
            return this.comboBoxSmooth.dialogDescription();
        }

        switch (dialogID)
        {
            case 42:
                final JHelpPanelSmooth panelSmooth = new JHelpPanelSmooth(new JHelpBorderLayoutSmooth());
                final JHelpButtonSmooth buttonSmooth = new JHelpButtonSmooth(new ActionCloseDialog());
                buttonSmooth.shape(SmoothSausage.SAUSAGE);
                buttonSmooth.shadowLevel(ShadowLevel.NEAR);
                panelSmooth.addComponent(buttonSmooth, JHelpBorderConstraintsSmooth.UP_EXPAND);

                final JHelpListSmoothModelDefault<String> model = new JHelpListSmoothModelDefault<String>();

                for (int i = 0; i < 128; i++)
                {
                    model.addElement("Text " + (i + 1));
                }

                final JHelpListSmooth<String> list = new JHelpListSmooth<String>(model, new TextRenderer(),
                                                                                 JHelpScrollModeSmooth.SCROLL_VERTICAL);
                list.registerSelectListener(this.actionPrintSomething);
                panelSmooth.addComponent(list, JHelpBorderConstraintsSmooth.CENTER);

                return new DialogDescription(512, 256, panelSmooth, JHelpConstantsSmooth.COLOR_BLUE_GREY_0200,
                                             JHelpConstantsSmooth.COLOR_GREY_0300,
                                             ShadowLevel.FAR);
        }

        return null;
    }
}
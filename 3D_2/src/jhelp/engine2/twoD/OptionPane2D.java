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
package jhelp.engine2.twoD;

import com.sun.istack.internal.NotNull;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import jhelp.engine2.render.Texture;
import jhelp.engine2.render.Window3D;
import jhelp.engine2.twoD.event.Object2DListener;
import jhelp.engine2.twoD.event.OptionPane2DListener;
import jhelp.gui.ResourcesGUI;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpTextAlign;
import jhelp.util.gui.alphabet.AlphabetGraffiti;
import jhelp.util.gui.alphabet.AlphabetGreen8x16;
import jhelp.util.gui.alphabet.AlphabetText;

/**
 * Option pane object 2D
 *
 * @author JHelp
 */
public class OptionPane2D
        extends Object2D
        implements Object2DListener
{
    /**
     * Alphabet for draw the text
     */
    private final AlphabetText         alphabetText;
    /**
     * Cancel button area
     */
    private final Rectangle            cancelArea;
    /**
     * No button area
     */
    private final Rectangle            noArea;
    /**
     * OK button area
     */
    private final Rectangle            okArea;
    /**
     * Listener of option pane events
     */
    private final OptionPane2DListener optionPane2DListener;
    /**
     * Current option pane ID
     */
    private       int                  optionPaneID;
    /**
     * Number of columns and lines maximum in text
     */
    private final Dimension            size;
    /**
     * Texture where draw
     */
    private final Texture              texture;
    /**
     * Window that draw scene parent
     */
    private final Window3D             window3D;
    /**
     * Yes button area
     */
    private final Rectangle            yesArea;

    /**
     * Create a new instance of OptionPane2D
     *
     * @param window3D             Window where option pane draw
     * @param x                    X option pane position
     * @param y                    Y option pane position
     * @param width                Option pane width
     * @param height               Option pane height
     * @param optionPane2DListener Listener of option pane events
     */
    public OptionPane2D(
            final @NotNull Window3D window3D,
            final int x, final int y, final int width, final int height,
            final @NotNull OptionPane2DListener optionPane2DListener)
    {
        super(x, y, width, height);

        if (window3D == null)
        {
            throw new NullPointerException("sceneRenderer mustn't be null");
        }

        if (optionPane2DListener == null)
        {
            throw new NullPointerException("optionPane2DListener mustn't be null");
        }

        this.window3D = window3D;
        this.optionPane2DListener = optionPane2DListener;
        final Dimension character = AlphabetGraffiti.NORMAL.getCharacterDimension();
        this.size = AlphabetText.obtainNumberColumnsLines(AlphabetGreen8x16.ALPHABET_GREEN8X16, width,
                                                          height - character.height - 8);
        this.alphabetText = new AlphabetText(AlphabetGreen8x16.ALPHABET_GREEN8X16, this.size.width, this.size.height,
                                             "", JHelpTextAlign.CENTER, 0xFF0A7E07,
                                             0x800A7E07);
        final JHelpImage image = this.alphabetText.image();
        this.texture = new Texture("Game3DOptionPane", image.getWidth(), image.getHeight() + character.height + 8);
        this.texture.autoFlush(false);
        this.texture(this.texture);
        this.okArea = new Rectangle(-1, -1, -1, -1);
        this.yesArea = new Rectangle(-1, -1, -1, -1);
        this.noArea = new Rectangle(-1, -1, -1, -1);
        this.cancelArea = new Rectangle(-1, -1, -1, -1);
        this.window3D.gui2d().addOver3D(this);
        this.registerObject2DListener(this);
        this.visible(false);
    }

    /**
     * Create an option pane button
     *
     * @param text Button text
     * @return Created button
     */
    private @NotNull JHelpImage createOptionPaneButton(final @NotNull String text)
    {
        final Dimension  dimension = AlphabetGraffiti.NORMAL.computeTextDimension(text);
        final JHelpImage image     = new JHelpImage(dimension.width + 8, dimension.height + 8, 0xAAFFFFFF);
        image.startDrawMode();
        image.drawRectangle(0, 0, image.getWidth() - 1, image.getHeight() - 1, 0xFF000000);
        AlphabetGraffiti.NORMAL.drawOn(text, JHelpTextAlign.CENTER, image,
                                       image.getWidth() >> 1, image.getHeight() >> 1, true);
        image.endDrawMode();
        return image;
    }

    /**
     * Update option pane
     *
     * @param text              New text
     * @param optionPaneButtons Option pane options. Must be one of : {@link OptionPaneButtons#OK}, {@link OptionPaneButtons#YES_NO} or
     *                          {@link OptionPaneButtons#YES_NO_CANCEL}
     */
    private void updateOptionPane(final @NotNull String text, final @NotNull OptionPaneButtons optionPaneButtons)
    {
        if (text == null)
        {
            throw new NullPointerException("text mustn't be null");
        }

        if (!optionPaneButtons.validForSpecifyOptionPaneButtons())
        {
            throw new IllegalArgumentException(optionPaneButtons + " not valid");
        }

        this.okArea.setBounds(-1, -1, -1, -1);
        this.yesArea.setBounds(-1, -1, -1, -1);
        this.noArea.setBounds(-1, -1, -1, -1);
        this.cancelArea.setBounds(-1, -1, -1, -1);
        this.alphabetText.text(text);
        this.texture.clear(new Color(0x44123456, true));
        this.texture.drawImage(0, 0, this.alphabetText.image());
        final int  width  = this.texture.width();
        final int  height = this.texture.height();
        int        x1, x2, x3;
        int        space;
        JHelpImage imageButton1, imageButton2, imageButton3;

        switch (optionPaneButtons)
        {
            case OK:
                imageButton1 = this.createOptionPaneButton(
                        ResourcesGUI.RESOURCE_TEXT.getText(ResourcesGUI.OPTION_PANE_OK));
                this.texture.drawImage((width - imageButton1.getWidth()) >> 1, height - imageButton1.getHeight(),
                                       imageButton1);
                this.okArea.setBounds((width - imageButton1.getWidth()) >> 1, height - imageButton1.getHeight(),
                                      imageButton1.getWidth(), imageButton1.getHeight());
                break;
            case YES_NO:
                imageButton1 = this.createOptionPaneButton(
                        ResourcesGUI.RESOURCE_TEXT.getText(ResourcesGUI.OPTION_PANE_YES));
                imageButton2 = this.createOptionPaneButton(
                        ResourcesGUI.RESOURCE_TEXT.getText(ResourcesGUI.OPTION_PANE_NO));
                space = (width - imageButton1.getWidth() - imageButton2.getWidth()) / 3;
                x1 = space;
                x2 = x1 + imageButton1.getWidth() + space;
                this.texture.drawImage(x1, height - imageButton1.getHeight(), imageButton1);
                this.texture.drawImage(x2, height - imageButton2.getHeight(), imageButton2);
                this.yesArea.setBounds(x1, height - imageButton1.getHeight(),
                                       imageButton1.getWidth(), imageButton1.getHeight());
                this.noArea.setBounds(x2, height - imageButton2.getHeight(),
                                      imageButton2.getWidth(), imageButton2.getHeight());
                break;
            case YES_NO_CANCEL:
                imageButton1 = this.createOptionPaneButton(
                        ResourcesGUI.RESOURCE_TEXT.getText(ResourcesGUI.OPTION_PANE_YES));
                imageButton2 = this.createOptionPaneButton(
                        ResourcesGUI.RESOURCE_TEXT.getText(ResourcesGUI.OPTION_PANE_NO));
                imageButton3 = this.createOptionPaneButton(
                        ResourcesGUI.RESOURCE_TEXT.getText(ResourcesGUI.OPTION_PANE_CANCEL));
                space = (width - imageButton1.getWidth() - imageButton2.getWidth() - imageButton3.getWidth()) >> 2;
                x1 = space;
                x2 = x1 + imageButton1.getWidth() + space;
                x3 = x2 + imageButton2.getWidth() + space;
                this.texture.drawImage(x1, height - imageButton1.getHeight(), imageButton1);
                this.texture.drawImage(x2, height - imageButton2.getHeight(), imageButton2);
                this.texture.drawImage(x3, height - imageButton3.getHeight(), imageButton3);
                this.yesArea.setBounds(x1, height - imageButton1.getHeight(),
                                       imageButton1.getWidth(), imageButton1.getHeight());
                this.noArea.setBounds(x2, height - imageButton2.getHeight(),
                                      imageButton2.getWidth(), imageButton2.getHeight());
                this.cancelArea.setBounds(x3, height - imageButton3.getHeight(),
                                          imageButton3.getWidth(), imageButton3.getHeight());
                break;
            default:
                break;
        }

        this.texture.flush();
    }

    /**
     * Called when mouse click <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param object2d    Object reference
     * @param x           Mouse X
     * @param y           Mouse Y
     * @param leftButton  Indicates if left button is down
     * @param rightButton Indicates if right button is down
     * @see Object2DListener#mouseClick(Object2D, int, int, boolean, boolean)
     */
    @Override
    public void mouseClick(
            final @NotNull Object2D object2d,
            final int x, final int y, final boolean leftButton, final boolean rightButton)
    {
        OptionPaneButtons optionPaneButtons;

        if (this.okArea.contains(x, y))
        {
            optionPaneButtons = OptionPaneButtons.OK;
        }
        else if (this.yesArea.contains(x, y))
        {
            optionPaneButtons = OptionPaneButtons.YES;
        }
        else if (this.noArea.contains(x, y))
        {
            optionPaneButtons = OptionPaneButtons.NO;
        }
        else if (this.cancelArea.contains(x, y))
        {
            optionPaneButtons = OptionPaneButtons.CANCEL;
        }
        else
        {
            return;
        }

        this.window3D.gui2d().allCanBeDetected();
        this.visible(false);
        this.optionPane2DListener.optionPaneClicked(this.optionPaneID, optionPaneButtons);
    }

    /**
     * Called when mouse drag <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param object2d    Object reference
     * @param x           Mouse X
     * @param y           Mouse Y
     * @param leftButton  Indicates if left button is down
     * @param rightButton Indicates if right button is down
     * @see Object2DListener#mouseDrag(Object2D, int, int, boolean, boolean)
     */
    @Override
    public void mouseDrag(
            final @NotNull Object2D object2d,
            final int x, final int y, final boolean leftButton, final boolean rightButton)
    {
    }

    /**
     * Called when mouse enter <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param object2d Object reference
     * @param x        Mouse X
     * @param y        Mouse Y
     * @see Object2DListener#mouseEnter(Object2D, int, int)
     */
    @Override
    public void mouseEnter(final @NotNull Object2D object2d, final int x, final int y)
    {
    }

    /**
     * Called when mouse exit <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param object2d Object reference
     * @param x        Mouse X
     * @param y        Mouse Y
     * @see Object2DListener#mouseExit(Object2D, int, int)
     */
    @Override
    public void mouseExit(final @NotNull Object2D object2d, final int x, final int y)
    {
    }

    /**
     * Called when mouse move <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param object2d Object reference
     * @param x        Mouse X
     * @param y        Mouse Y
     * @see Object2DListener#mouseMove(Object2D, int, int)
     */
    @Override
    public void mouseMove(final @NotNull Object2D object2d, final int x, final int y)
    {
    }

    /**
     * Show option pane
     *
     * @param text              Option pane text
     * @param optionPaneButtons Option pane buttons : must be : {@link OptionPaneButtons#OK}, {@link OptionPaneButtons#YES_NO} or
     *                          {@link OptionPaneButtons#YES_NO_CANCEL}
     * @param optionPaneID      Option pane ID
     */
    public void showOptionPane(
            final @NotNull String text, final @NotNull OptionPaneButtons optionPaneButtons, final int optionPaneID)
    {
        this.updateOptionPane(text, optionPaneButtons);
        this.window3D.gui2d().exclusiveDetection(this);
        this.visible(true);
        this.optionPaneID = optionPaneID;
    }
}
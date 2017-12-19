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

import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.util.Map;
import jhelp.gui.game.ActionKey;
import jhelp.gui.game.CaptureKeyCodeListener;
import jhelp.gui.game.EventManager;
import jhelp.gui.game.JHelpGame2D;
import jhelp.gui.game.MouseSensitiveArea;
import jhelp.gui.game.MouseSensitiveAreaListener;
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpGradient;
import jhelp.util.gui.JHelpGradientVertical;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpMask;
import jhelp.util.gui.JHelpSprite;

/**
 * Dialog in {@link JHelpGame2D} for be able to change action key mapping with keyboard.<br>
 * In other words, change the short cut associated to action key
 *
 * @author JHelp
 */
public class KeyMapper
        implements MouseSensitiveAreaListener, CaptureKeyCodeListener, EventManager
{
    static
    {
        JHelpImage image = new JHelpImage(KeyMapper.SIZE_MASK, KeyMapper.SIZE_MASK);

        image.startDrawMode();

        image.fillRectangle(0, 0, KeyMapper.SIZE_MASK, KeyMapper.SIZE_MASK,
                            new JHelpGradient(0xFFF0F000, 0xFF00F0F0, 0xFFF000F0, 0xFFFFFFFF));

        image.endDrawMode();
        image.update();

        GRADIENT = image;

        //

        final int size = KeyMapper.SIZE_MASK << 3;
        image = new JHelpImage(1, size);

        image.startDrawMode();

        final JHelpGradientVertical gradientHorizontal = new JHelpGradientVertical(0xC0000000, 0xC0000000);
        gradientHorizontal.addColor(25, 0xC0808080);
        gradientHorizontal.addColor(50, 0xC0FFFFFF);
        gradientHorizontal.addColor(75, 0xC0808080);

        image.fillRectangle(0, 0, 1, size, gradientHorizontal);

        image.endDrawMode();
        image.update();

        GRADIENT2 = image;
    }

    /**
     * Image with gradient
     */
    private static final JHelpImage GRADIENT;
    /**
     * Image with gradient
     */
    private static final JHelpImage GRADIENT2;
    /**
     * Mask size
     */
    private static final int SIZE_MASK      = 32;
    /**
     * Space between columns
     */
    private static final int SPACE_COLUMN   = 9;
    /**
     * Space between "image" and text
     */
    private static final int SPACE_TEXT_KEY = 3;
    /**
     * Event manager
     */
    private       EventManager                           eventManager;
    /**
     * Linked game 2D
     */
    private       JHelpGame2D                            game2d;
    /**
     * Background image for action key
     */
    private final JHelpImage                             imageKeyBackground;
    /**
     * Foreground image for action key
     */
    private final JHelpImage                             imageKeyForeground;
    /**
     * Image that contans the dialog
     */
    private final JHelpImage                             imageKeyMapper;
    /**
     * Sort cut background
     */
    private final int                                    keyBackground;
    /**
     * Sort cut foreground
     */
    private final int                                    keyForeground;
    /**
     * Normal border color
     */
    private final int                                    normal;
    /**
     * Selected border color
     */
    private final int                                    select;
    /**
     * Actual selected action
     */
    private       ActionKey                              selected;
    /**
     * Map of action key associated to its area
     */
    private final HashMap<ActionKey, MouseSensitiveArea> sensitiveAreas;
    /**
     * Sprite for draw all the dialog
     */
    private       JHelpSprite                            sprite;

    /**
     * Create a new instance of KeyMapper with default settings
     */
    public KeyMapper()
    {
        this(KeyMapper.GRADIENT2, 0x80000000, null, 0xFFFFFFFF, KeyMapper.GRADIENT, 0xFFFFFFFF, null, 0x40000000,
             0xFFFF0000, 0xFF808080,
             new JHelpFont("Arial", 16));
    }

    /**
     * Create a new instance of KeyMapper
     *
     * @param colorBackground Background color
     * @param colorText       Foreground color
     * @param keyForeground   Key foreground
     * @param keyBackground   Key background
     * @param select          Select color
     * @param normal          Normal color
     * @param font            Font to use
     */
    public KeyMapper(
            final int colorBackground, final int colorText, final int keyForeground, final int keyBackground,
            final int select, final int normal,
            final JHelpFont font)
    {
        this(null, colorBackground, null, colorText, null, keyForeground, null, keyBackground, select, normal, font);
    }

    /**
     * Create a new instance of KeyMapper
     *
     * @param imageBackground    Image background (can be {@code null})
     * @param colorBackground    Color background, used if <b>imageBackground</b> is {@code null}
     * @param imageText          Image foreground (can be {@code null})
     * @param colorText          Color foreground, used if <b>imageText</b> is {@code null}
     * @param imageKeyForeground Image key foreground (can be {@code null})
     * @param keyForeground      Color key foreground, used if <b>imageKeyForeground</b> is {@code null}
     * @param imageKeyBackground Image key background (can be {@code null})
     * @param keyBackground      Color key background, used if <b>imageKeyBackground</b> is {@code null}
     * @param select             Selected border color
     * @param normal             Normal border color
     * @param font               Font to use
     */
    public KeyMapper(
            final JHelpImage imageBackground, final int colorBackground, final JHelpImage imageText,
            final int colorText,
            final JHelpImage imageKeyForeground, final int keyForeground, final JHelpImage imageKeyBackground,
            final int keyBackground, final int select,
            final int normal, final JHelpFont font)
    {
        this.sensitiveAreas = new HashMap<ActionKey, MouseSensitiveArea>();

        this.imageKeyForeground = imageKeyForeground;
        this.keyForeground = keyForeground;
        this.imageKeyBackground = imageKeyBackground;
        this.keyBackground = keyBackground;
        this.select = select;
        this.normal = normal;

        final HashMap<ActionKey, Rectangle> bounds = new HashMap<ActionKey, Rectangle>();

        for (final ActionKey actionKey : ActionKey.values())
        {
            bounds.put(actionKey, font.computeShape(ResourcesKeyMapper.obtainText(actionKey), 0, 0).getBounds());
        }

        int fontHeight       = 0;
        int firstColumWidth  = 0;
        int secondColumWidth = 0;
        int thirdColumnWith  = 0;
        int fourthColumWidth = 0;
        int fithcolumnWidth  = 0;

        Rectangle rectangle;

        for (final ActionKey actionKey : ActionKey.values())
        {
            rectangle = bounds.get(actionKey);

            fontHeight = Math.max(fontHeight, rectangle.height);

            switch (actionKey)
            {
                case ACTION_ACTION:
                    firstColumWidth = Math.max(firstColumWidth, rectangle.width);
                    break;
                case ACTION_CANCEL:
                    firstColumWidth = Math.max(firstColumWidth, rectangle.width);
                    break;
                case ACTION_DOWN:
                    thirdColumnWith = Math.max(thirdColumnWith, rectangle.width);
                    break;
                case ACTION_EXIT:
                    firstColumWidth = Math.max(firstColumWidth, rectangle.width);
                    break;
                case ACTION_FIRE:
                    firstColumWidth = Math.max(firstColumWidth, rectangle.width);
                    break;
                case ACTION_FIRE_2:
                    thirdColumnWith = Math.max(thirdColumnWith, rectangle.width);
                    break;
                case ACTION_LEFT:
                    secondColumWidth = Math.max(secondColumWidth, rectangle.width);
                    break;
                case ACTION_MENU:
                    firstColumWidth = Math.max(firstColumWidth, rectangle.width);
                    break;
                case ACTION_NEXT:
                    fithcolumnWidth = Math.max(fithcolumnWidth, rectangle.width);
                    break;
                case ACTION_PAUSE:
                    fithcolumnWidth = Math.max(fithcolumnWidth, rectangle.width);
                    break;
                case ACTION_PREVIOUS:
                    fithcolumnWidth = Math.max(fithcolumnWidth, rectangle.width);
                    break;
                case ACTION_RIGHT:
                    fourthColumWidth = Math.max(fourthColumWidth, rectangle.width);
                    break;
                case ACTION_SPECIAL:
                    fithcolumnWidth = Math.max(fithcolumnWidth, rectangle.width);
                    break;
                case ACTION_START:
                    fithcolumnWidth = Math.max(fithcolumnWidth, rectangle.width);
                    break;
                case ACTION_UP:
                    thirdColumnWith = Math.max(thirdColumnWith, rectangle.width);
                    break;
                default:
                    break;
            }
        }

        final int totalWidth =
                KeyMapper.SPACE_TEXT_KEY + firstColumWidth + KeyMapper.SPACE_TEXT_KEY + KeyMapper.SIZE_MASK +
                KeyMapper.SPACE_COLUMN + //
                secondColumWidth + KeyMapper.SPACE_TEXT_KEY + KeyMapper.SIZE_MASK + KeyMapper.SPACE_COLUMN + //
                thirdColumnWith + KeyMapper.SPACE_TEXT_KEY + KeyMapper.SIZE_MASK + KeyMapper.SPACE_COLUMN + //
                fourthColumWidth + KeyMapper.SPACE_TEXT_KEY + KeyMapper.SIZE_MASK + KeyMapper.SPACE_COLUMN + //
                fithcolumnWidth + KeyMapper.SPACE_TEXT_KEY + KeyMapper.SIZE_MASK + KeyMapper.SPACE_TEXT_KEY;
        final int height      = Math.max(KeyMapper.SIZE_MASK, fontHeight);
        final int totalHeight = ((height + KeyMapper.SPACE_COLUMN) * 5) + KeyMapper.SPACE_COLUMN;

        this.imageKeyMapper = new JHelpImage(totalWidth, totalHeight, 0x00000000);

        this.imageKeyMapper.startDrawMode();

        if (imageBackground != null)
        {
            this.imageKeyMapper.fillShape(new RoundRectangle2D.Double(0, 0, totalWidth, totalHeight, 20, 20),
                                          imageBackground);
        }
        else
        {
            this.imageKeyMapper.fillShape(new RoundRectangle2D.Double(0, 0, totalWidth, totalHeight, 20, 20),
                                          colorBackground);
        }

        // Column 1
        int x = KeyMapper.SPACE_TEXT_KEY;
        int y = KeyMapper.SPACE_COLUMN;
        this.drawButton(x, y, firstColumWidth, height, bounds.get(ActionKey.ACTION_MENU), ActionKey.ACTION_MENU,
                        imageText, colorText, font);
        y += height + KeyMapper.SPACE_COLUMN;
        this.drawButton(x, y, firstColumWidth, height, bounds.get(ActionKey.ACTION_ACTION), ActionKey.ACTION_ACTION,
                        imageText, colorText, font);
        y += height + KeyMapper.SPACE_COLUMN;
        this.drawButton(x, y, firstColumWidth, height, bounds.get(ActionKey.ACTION_CANCEL), ActionKey.ACTION_CANCEL,
                        imageText, colorText, font);
        y += height + KeyMapper.SPACE_COLUMN;
        this.drawButton(x, y, firstColumWidth, height, bounds.get(ActionKey.ACTION_EXIT), ActionKey.ACTION_EXIT,
                        imageText, colorText, font);
        y += height + KeyMapper.SPACE_COLUMN;
        this.drawButton(x, y, firstColumWidth, height, bounds.get(ActionKey.ACTION_FIRE), ActionKey.ACTION_FIRE,
                        imageText, colorText, font);
        y += height + KeyMapper.SPACE_COLUMN;

        // Column 2
        x += firstColumWidth + KeyMapper.SPACE_TEXT_KEY + KeyMapper.SIZE_MASK + KeyMapper.SPACE_COLUMN;
        y = KeyMapper.SPACE_COLUMN + height + KeyMapper.SPACE_COLUMN;
        this.drawButton(x, y, secondColumWidth, height, bounds.get(ActionKey.ACTION_LEFT), ActionKey.ACTION_LEFT,
                        imageText, colorText, font);
        y += height + KeyMapper.SPACE_COLUMN;

        // Column 3
        x += secondColumWidth + KeyMapper.SPACE_TEXT_KEY + KeyMapper.SIZE_MASK + KeyMapper.SPACE_COLUMN;
        y = KeyMapper.SPACE_COLUMN;
        this.drawButton(x, y, thirdColumnWith, height, bounds.get(ActionKey.ACTION_UP), ActionKey.ACTION_UP, imageText,
                        colorText, font);
        y += height + KeyMapper.SPACE_COLUMN;
        y += height + KeyMapper.SPACE_COLUMN;
        this.drawButton(x, y, thirdColumnWith, height, bounds.get(ActionKey.ACTION_DOWN), ActionKey.ACTION_DOWN,
                        imageText, colorText, font);
        y += height + KeyMapper.SPACE_COLUMN;
        y += height + KeyMapper.SPACE_COLUMN;
        this.drawButton(x, y, thirdColumnWith, height, bounds.get(ActionKey.ACTION_FIRE_2), ActionKey.ACTION_FIRE_2,
                        imageText, colorText, font);

        // Column 4
        x += thirdColumnWith + KeyMapper.SPACE_TEXT_KEY + KeyMapper.SIZE_MASK + KeyMapper.SPACE_COLUMN;
        y = KeyMapper.SPACE_COLUMN + height + KeyMapper.SPACE_COLUMN;
        this.drawButton(x, y, fourthColumWidth, height, bounds.get(ActionKey.ACTION_RIGHT), ActionKey.ACTION_RIGHT,
                        imageText, colorText, font);
        y += height + KeyMapper.SPACE_COLUMN;

        // Column 5
        x += fourthColumWidth + KeyMapper.SPACE_TEXT_KEY + KeyMapper.SIZE_MASK + KeyMapper.SPACE_COLUMN;
        y = KeyMapper.SPACE_COLUMN;
        this.drawButton(x, y, fithcolumnWidth, height, bounds.get(ActionKey.ACTION_PREVIOUS), ActionKey.ACTION_PREVIOUS,
                        imageText, colorText, font);
        y += height + KeyMapper.SPACE_COLUMN;
        this.drawButton(x, y, fithcolumnWidth, height, bounds.get(ActionKey.ACTION_NEXT), ActionKey.ACTION_NEXT,
                        imageText, colorText, font);
        y += height + KeyMapper.SPACE_COLUMN;
        this.drawButton(x, y, fithcolumnWidth, height, bounds.get(ActionKey.ACTION_START), ActionKey.ACTION_START,
                        imageText, colorText, font);
        y += height + KeyMapper.SPACE_COLUMN;
        this.drawButton(x, y, fithcolumnWidth, height, bounds.get(ActionKey.ACTION_PAUSE), ActionKey.ACTION_PAUSE,
                        imageText, colorText, font);
        y += height + KeyMapper.SPACE_COLUMN;
        this.drawButton(x, y, fithcolumnWidth, height, bounds.get(ActionKey.ACTION_SPECIAL), ActionKey.ACTION_SPECIAL,
                        imageText, colorText, font);
        y += height + KeyMapper.SPACE_COLUMN;

        this.imageKeyMapper.endDrawMode();
    }

    /**
     * Draw a button
     *
     * @param x           X
     * @param y           Y
     * @param columnWidth Column width
     * @param height      Button height
     * @param bounds      Dialog bounds
     * @param actionKey   Action key to draw
     * @param imageText   Image used for text
     * @param colorText   Color used for text
     * @param font        Font used
     */
    private void drawButton(
            final int x, final int y, final int columnWidth, final int height, final Rectangle bounds,
            final ActionKey actionKey,
            final JHelpImage imageText, final int colorText, final JHelpFont font)
    {
        if (imageText == null)
        {
            this.imageKeyMapper.fillString((x + columnWidth) - bounds.width, y + ((height - bounds.height) >> 1),
                                           ResourcesKeyMapper.obtainText(actionKey), font,
                                           colorText);
        }
        else
        {
            this.imageKeyMapper.fillString((x + columnWidth) - bounds.width, y + ((height - bounds.height) >> 1),
                                           ResourcesKeyMapper.obtainText(actionKey), font,
                                           imageText, colorText);
        }

        this.imageKeyMapper.drawRectangle(x + columnWidth + KeyMapper.SPACE_TEXT_KEY,
                                          y + ((height - KeyMapper.SIZE_MASK) >> 1), KeyMapper.SIZE_MASK,
                                          KeyMapper.SIZE_MASK, this.normal);

        this.sensitiveAreas.put(actionKey, MouseSensitiveArea.createRectangleArea(actionKey.ordinal(), x + columnWidth +
                                                                                                       KeyMapper.SPACE_TEXT_KEY,
                                                                                  y +
                                                                                  ((height - KeyMapper.SIZE_MASK) >> 1),
                                                                                  KeyMapper.SIZE_MASK,
                                                                                  KeyMapper.SIZE_MASK));
    }

    /**
     * Update dialog image
     */
    private void updateImage()
    {
        Rectangle        bounds;
        JHelpMask        mask;
        final JHelpImage image = this.sprite.getImage();
        image.startDrawMode();
        MouseSensitiveArea mouseSensitiveArea;

        for (final ActionKey actionKey : ActionKey.values())
        {
            mouseSensitiveArea = this.sensitiveAreas.get(actionKey);

            if (mouseSensitiveArea == null)
            {
                continue;
            }

            bounds = mouseSensitiveArea.bounds();
            mask = ResourcesKeyMapper.obtainMask(this.game2d.keyCode(actionKey));

            if (this.imageKeyBackground == null)
            {
                image.fillRectangle(bounds.x, bounds.y, bounds.width, bounds.height, this.keyBackground, false);
            }
            else
            {
                image.fillRectangle(bounds.x, bounds.y, bounds.width, bounds.height, this.imageKeyBackground, false);
            }

            if (mask != null)
            {
                if (this.imageKeyForeground == null)
                {
                    if (this.imageKeyBackground == null)
                    {
                        image.paintMask(bounds.x, bounds.y, mask, this.keyForeground, this.keyBackground, true);
                    }
                    else
                    {
                        image.paintMask(bounds.x, bounds.y, mask, this.keyForeground, this.imageKeyBackground, 0, 0,
                                        true);
                    }
                }
                else
                {
                    if (this.imageKeyBackground == null)
                    {
                        image.paintMask(bounds.x, bounds.y, mask, this.imageKeyForeground, 0, 0, this.keyBackground,
                                        true);
                    }
                    else
                    {
                        image.paintMask(bounds.x, bounds.y, mask, this.imageKeyForeground, 0, 0,
                                        this.imageKeyBackground, 0, 0, true);
                    }
                }
            }

            if (actionKey == this.selected)
            {
                image.drawRectangle(bounds.x, bounds.y, bounds.width, bounds.height, this.select);
            }
            else
            {
                image.drawRectangle(bounds.x, bounds.y, bounds.width, bounds.height, this.normal);
            }
        }

        image.endDrawMode();
        image.update();

        this.sprite.setVisible(false);
        this.sprite.setVisible(true);

        this.game2d.refresh();
    }

    /**
     * Call at each refresh and react to actions <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param actionsStates Action keys state
     * @param mouseX        Mouse X
     * @param mouseY        Mouse Y
     * @param buttonLeft    Indicates if mouse left button is down
     * @param buttonMiddle  Indicates if mouse middle button is down
     * @param buttonRight   Indicates if mouse right button is down
     * @return {@code true} because actions are consumed
     * @see jhelp.gui.game.EventManager#actionState(Map, int, int, boolean, boolean, boolean)
     */
    @Override
    public boolean actionState(
            final Map<ActionKey, Boolean> actionsStates, final int mouseX, final int mouseY, final boolean buttonLeft,
            final boolean buttonMiddle, final boolean buttonRight)
    {
        if (actionsStates.get(ActionKey.ACTION_EXIT))
        {
            this.removeFromJHelpGame2D();
        }

        return true;
    }

    /**
     * Indicates if it is possible to loose focus right now <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return {@code true} if it is possible to loose focus right now
     * @see jhelp.gui.game.CaptureKeyCodeListener#canLooseFocus()
     */
    @Override
    public boolean canLooseFocus()
    {
        return this.selected == null;
    }

    /**
     * Call when a key is captured <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param keycode Key code captured
     * @see jhelp.gui.game.CaptureKeyCodeListener#capturedkeyCode(int)
     */
    @Override
    public void capturedkeyCode(final int keycode)
    {
        if (ResourcesKeyMapper.obtainMask(keycode) == null)
        {
            this.game2d.captureNextPressedKey(this);

            return;
        }

        this.selected = this.game2d.keyCode(this.selected, keycode);

        this.updateImage();

        if (this.selected != null)
        {
            this.game2d.captureNextPressedKey(this);
        }
    }

    /**
     * Called when mouse enter in area <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param mouseSensitiveArea Area enter
     * @param relativeX          Relative X
     * @param relativeY          Relative Y
     * @param absoluteX          Absolute X
     * @param absoluteY          Absolute Y
     * @param leftButtonDown     Left button is down ?
     * @param middleButtonDown   Middle button is down ?
     * @param rightButtonDown    Right button is down ?
     * @see jhelp.gui.game.MouseSensitiveAreaListener#mouseEnterArea(jhelp.gui.game.MouseSensitiveArea, int, int, int, int,
     * boolean, boolean, boolean)
     */
    @Override
    public void mouseEnterArea(
            final MouseSensitiveArea mouseSensitiveArea, final int relativeX, final int relativeY, final int absoluteX,
            final int absoluteY,
            final boolean leftButtonDown, final boolean middleButtonDown, final boolean rightButtonDown)
    {
    }

    /**
     * Called when mouse exit from area <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param mouseSensitiveArea Area exit
     * @param relativeX          Relative X
     * @param relativeY          Relative Y
     * @param absoluteX          Absolute X
     * @param absoluteY          Absolute Y
     * @param leftButtonDown     Left button is down ?
     * @param middleButtonDown   Middle button is down ?
     * @param rightButtonDown    Right button is down ?
     * @see jhelp.gui.game.MouseSensitiveAreaListener#mouseExitArea(jhelp.gui.game.MouseSensitiveArea, int, int, int, int,
     * boolean, boolean, boolean)
     */
    @Override
    public void mouseExitArea(
            final MouseSensitiveArea mouseSensitiveArea, final int relativeX, final int relativeY, final int absoluteX,
            final int absoluteY,
            final boolean leftButtonDown, final boolean middleButtonDown, final boolean rightButtonDown)
    {
    }

    /**
     * Called when mouse move over an area <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param mouseSensitiveArea Area under mouse
     * @param relativeX          Relative X
     * @param relativeY          Relative Y
     * @param absoluteX          Absolute X
     * @param absoluteY          Absolute Y
     * @param leftButtonDown     Left button is down ?
     * @param middleButtonDown   Middle button is down ?
     * @param rightButtonDown    Right button is down ?
     * @see jhelp.gui.game.MouseSensitiveAreaListener#mouseOverArea(jhelp.gui.game.MouseSensitiveArea, int, int, int, int,
     * boolean, boolean, boolean)
     */
    @Override
    public void mouseOverArea(
            final MouseSensitiveArea mouseSensitiveArea, final int relativeX, final int relativeY, final int absoluteX,
            final int absoluteY,
            final boolean leftButtonDown, final boolean middleButtonDown, final boolean rightButtonDown)
    {
        if (leftButtonDown)
        {
            this.selected = ActionKey.values()[mouseSensitiveArea.identifier()];

            this.updateImage();

            this.game2d.captureNextPressedKey(this);
        }
    }

    /**
     * Detach the key mapper dialog from the game frame
     */
    public void removeFromJHelpGame2D()
    {
        if (this.game2d == null)
        {
            return;
        }

        this.game2d.eventManager(this.eventManager);
        this.game2d.removeSprite(this.sprite);

        for (final ActionKey actionKey : ActionKey.values())
        {
            this.game2d.removeArea(this.sensitiveAreas.get(actionKey));
        }

        this.game2d.unregisterMouseSensitiveAreaListener(this);

        this.sprite = null;
        this.game2d = null;
    }

    /**
     * Show the dialog in game frame
     *
     * @param game2d Game frame to attach dialog in
     */
    public void showInJHelpGame2D(final JHelpGame2D game2d)
    {
        if (game2d == null)
        {
            throw new NullPointerException("game2d mustn't be null");
        }

        if (this.game2d != null)
        {
            throw new IllegalStateException("Already linked to a JHelpGame2D");
        }

        this.game2d = game2d;
        this.eventManager = game2d.eventManager();
        game2d.eventManager(this);
        final JHelpImage main = game2d.getImage();
        final int        x    = (main.getWidth() - this.imageKeyMapper.getWidth()) >> 1;
        final int        y    = (main.getHeight() - this.imageKeyMapper.getHeight()) >> 1;
        this.sprite = this.game2d.createSprite(x, y, this.imageKeyMapper);
        this.sprite.setVisible(true);
        MouseSensitiveArea mouseSensitiveArea;

        for (final ActionKey actionKey : ActionKey.values())
        {
            mouseSensitiveArea = this.sensitiveAreas.get(actionKey);

            if (mouseSensitiveArea != null)
            {
                game2d.addArea(mouseSensitiveArea.translation(x, y));
            }
        }

        game2d.registerMouseSensitiveAreaListener(this);

        this.updateImage();
    }
}
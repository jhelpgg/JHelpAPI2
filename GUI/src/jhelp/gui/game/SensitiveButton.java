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
package jhelp.gui.game;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import jhelp.gui.action.GenericAction;
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpTextAlign;
import jhelp.util.gui.JHelpTextLineAlpha;
import jhelp.util.list.Pair;

/**
 * Sensitive button
 *
 * @author JHelp
 */
public class SensitiveButton
        extends SensitiveElement
        implements PropertyChangeListener
{
    /**
     * Background default color
     */
    public static final int       BACKGROUND          = 0x12345678;
    /**
     * Default cursor used when over the button
     */
    public static final Cursor    DEFAULT_OVER_CURSOR = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    /**
     * Default font
     */
    public static final JHelpFont FONT                = new JHelpFont("Arial", 32);
    /**
     * Default foreground color
     */
    public static final int       FOREGROUND          = 0xFF000000;

    /**
     * Compute button image
     *
     * @param genericAction Action played when button click
     * @param font          Text font
     * @param foreground    Text color
     * @param background    Background color
     * @param buttonShape   Button shape
     * @return Created image
     */
    private static JHelpImage obtainImage(
            final GenericAction genericAction, final JHelpFont font, final int foreground, final int background,
            final ButtonShape buttonShape)
    {
        JHelpImage image = genericAction.largeIcon();

        if (image == null)
        {
            image = genericAction.smallIcon();

            if (image == null)
            {
                final Pair<List<JHelpTextLineAlpha>, Dimension> textLines = font.computeTextLinesAlpha(
                        genericAction.printName(), JHelpTextAlign.CENTER,
                        Integer.MAX_VALUE, Integer.MAX_VALUE, true);
                final int       insideWidth  = textLines.second.width;
                final int       insideHeight = textLines.second.height;
                final Dimension dimension    = buttonShape.computeShapeTotalSize(insideWidth, insideHeight);
                final Point     position     = buttonShape.computeShapeInsideUpLeftCorner(insideWidth, insideHeight);
                image = new JHelpImage(dimension.width, dimension.height, 0);
                image.startDrawMode();

                buttonShape.fill(0, 0, dimension.width, dimension.height, background, image);

                for (final JHelpTextLineAlpha textLine : textLines.first)
                {
                    image.drawImage(position.x + textLine.getX(), position.y + textLine.getY(), textLine.getMask(),
                                    true);
                }

                image.endDrawMode();
            }
        }

        return image;
    }

    /**
     * Cursor to use over the button
     */
    private       Cursor           cursor;
    /**
     * Game dynamic parent
     */
    private final JHelpGameDynamic gameDynamic;
    /**
     * Action played when button clicked
     */
    private final GenericAction    genericAction;

    /**
     * Create a new instance of SensitiveButton
     *
     * @param gameDynamic   Game dynamic parent
     * @param x             X position
     * @param y             Y position
     * @param genericAction Action played when button click
     * @param font          Text font
     * @param foreground    Text color
     * @param background    Background color
     * @param buttonShape   Button shape
     */
    public SensitiveButton(
            final JHelpGameDynamic gameDynamic, final int x, final int y, final GenericAction genericAction,
            final JHelpFont font,
            final int foreground, final int background, final ButtonShape buttonShape)
    {
        super(x, y, SensitiveButton.obtainImage(genericAction, font, foreground, background, buttonShape));

        if (gameDynamic == null)
        {
            throw new NullPointerException("gameDynamic mustn't be null");
        }

        this.cursor = SensitiveButton.DEFAULT_OVER_CURSOR;
        this.gameDynamic = gameDynamic;
        this.genericAction = genericAction;
        this.genericAction.addPropertyChangeListener(this);
    }

    /**
     * Called if enable state changed <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param enable New enable state
     * @see jhelp.gui.game.SensitiveElement#enableChanged(boolean)
     */
    @Override
    protected void enableChanged(final boolean enable)
    {
        this.genericAction.setEnabled(enable);
    }

    /**
     * Called when button clicked <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param parent Animation parent
     * @see jhelp.gui.game.SensitiveElement#mouseClick(jhelp.gui.game.SensitiveAnimation)
     */
    @Override
    protected void mouseClick(final SensitiveAnimation parent)
    {
        this.genericAction.actionPerformed(
                new ActionEvent(this, ActionEvent.ACTION_PERFORMED, this.genericAction.name()));
    }

    /**
     * Called when mouse enter <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param parent Animation parent
     * @see jhelp.gui.game.SensitiveElement#mouseEnter(jhelp.gui.game.SensitiveAnimation)
     */
    @Override
    protected void mouseEnter(final SensitiveAnimation parent)
    {
        this.gameDynamic.setCursor(this.cursor);
    }

    /**
     * Called when muse exit <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param parent Animation parent
     * @see jhelp.gui.game.SensitiveElement#mouseExit(jhelp.gui.game.SensitiveAnimation)
     */
    @Override
    protected void mouseExit(final SensitiveAnimation parent)
    {
        this.gameDynamic.setCursor(Cursor.getDefaultCursor());
    }

    /**
     * Cursor when mouse over
     *
     * @return Cursor when mouse over
     */
    public Cursor cursor()
    {
        return this.cursor;
    }

    /**
     * Change cursor over the button
     *
     * @param cursor New cursor
     */
    public void cursor(final Cursor cursor)
    {
        if (cursor == null)
        {
            throw new NullPointerException("cursor mustn't be null");
        }

        this.cursor = cursor;
    }

    /**
     * Called when property changed on played action <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param propertyChangeEvent Event description
     * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
     */
    @Override
    public void propertyChange(final PropertyChangeEvent propertyChangeEvent)
    {
        this.enable(this.gameDynamic, this.genericAction.isEnabled());
    }
}
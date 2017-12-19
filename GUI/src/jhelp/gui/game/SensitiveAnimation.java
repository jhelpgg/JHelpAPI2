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

import java.util.ArrayList;
import java.util.List;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpSprite;
import jhelp.util.gui.dynamic.DynamicAnimation;
import jhelp.util.gui.dynamic.Position;
import jhelp.util.gui.dynamic.Positionable;
import jhelp.util.math.JHelpRandom;

/**
 * Animation with area that are mouse sensitive
 *
 * @author JHelp
 */
public class SensitiveAnimation
        implements Positionable
{
    /**
     * Color used in shine effect
     */
    private static final int SHINE_COLOR      = 0x33FFFFFF;
    /**
     * Shine effect height
     */
    private static final int SHINE_HEIGHT     = 32;
    /**
     * Shine effect width
     */
    private static final int SHINE_WIDTH      = 16;
    /**
     * Number step in shine effect
     */
    private static final int TOTAL_SHINE_STEP = 64;

    /**
     * Describe a sensitive area, mouse reaction and animate
     *
     * @author JHelp
     */
    class SensitiveArea
            implements MouseSensitiveAreaListener, DynamicAnimation
    {
        /**
         * Absolute frame when animation started
         */
        private float startAbslouteFrame;

        /**
         * Create a new instance of SensitiveArea
         */
        SensitiveArea()
        {
        }

        /**
         * Play the animation <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param absoluteFrame Absolute frame
         * @param image         Image parent
         * @return {@code true} if aniation will continue OR {@code false} if animation finished
         * @see DynamicAnimation#animate(float, JHelpImage)
         */
        @Override
        public boolean animate(final float absoluteFrame, final JHelpImage image)
        {
            return SensitiveAnimation.this.doAnimate(absoluteFrame - this.startAbslouteFrame, image);
        }

        /**
         * Called when animation finished <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param image Image parent
         * @see DynamicAnimation#endAnimation(JHelpImage)
         */
        @Override
        public void endAnimation(final JHelpImage image)
        {
            SensitiveAnimation.this.destroySprite(image);
        }

        /**
         * Called when animation started <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param startAbslouteFrame Absolute frame where animation started
         * @param image              Image parent
         * @see DynamicAnimation#startAnimation(float, JHelpImage)
         */
        @Override
        public void startAnimation(final float startAbslouteFrame, final JHelpImage image)
        {
            this.startAbslouteFrame = startAbslouteFrame;
            SensitiveAnimation.this.createSprite(image);
        }

        /**
         * Called when mouse enter inside the area <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param mouseSensitiveArea Area where mouse interact
         * @param relativeX          Mouse X relative to the area
         * @param relativeY          Mouse Y relative to the area
         * @param absoluteX          Mouse X
         * @param absoluteY          Mouse Y
         * @param leftButtonDown     Indicates if left button is down
         * @param middleButtonDown   Indicates if middle button is down
         * @param rightButtonDown    Indicates if right button is down
         * @see jhelp.gui.game.MouseSensitiveAreaListener#mouseEnterArea(MouseSensitiveArea, int, int, int, int,
         * boolean, boolean, boolean)
         */
        @Override
        public void mouseEnterArea(
                final MouseSensitiveArea mouseSensitiveArea, final int relativeX, final int relativeY,
                final int absoluteX,
                final int absoluteY, final boolean leftButtonDown, final boolean middleButtonDown,
                final boolean rightButtonDown)
        {
            if (mouseSensitiveArea.identifier() == SensitiveAnimation.this.id)
            {
                SensitiveAnimation.this.enterArea(relativeX, relativeY);
            }
        }

        /**
         * Called when mouse exit outside the area <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param mouseSensitiveArea Area where mouse interact
         * @param relativeX          Mouse X relative to the area
         * @param relativeY          Mouse Y relative to the area
         * @param absoluteX          Mouse X
         * @param absoluteY          Mouse Y
         * @param leftButtonDown     Indicates if left button is down
         * @param middleButtonDown   Indicates if middle button is down
         * @param rightButtonDown    Indicates if right button is down
         * @see jhelp.gui.game.MouseSensitiveAreaListener#mouseExitArea(MouseSensitiveArea, int, int, int, int,
         * boolean, boolean, boolean)
         */
        @Override
        public void mouseExitArea(
                final MouseSensitiveArea mouseSensitiveArea, final int relativeX, final int relativeY,
                final int absoluteX,
                final int absoluteY, final boolean leftButtonDown, final boolean middleButtonDown,
                final boolean rightButtonDown)
        {
            if (mouseSensitiveArea.identifier() == SensitiveAnimation.this.id)
            {
                SensitiveAnimation.this.exitArea(relativeX, relativeY);
            }
        }

        /**
         * Called when mouse move over the area <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param mouseSensitiveArea Area where mouse interact
         * @param relativeX          Mouse X relative to the area
         * @param relativeY          Mouse Y relative to the area
         * @param absoluteX          Mouse X
         * @param absoluteY          Mouse Y
         * @param leftButtonDown     Indicates if left button is down
         * @param middleButtonDown   Indicates if middle button is down
         * @param rightButtonDown    Indicates if right button is down
         * @see jhelp.gui.game.MouseSensitiveAreaListener#mouseOverArea(MouseSensitiveArea, int, int, int, int,
         * boolean, boolean, boolean)
         */
        @Override
        public void mouseOverArea(
                final MouseSensitiveArea mouseSensitiveArea, final int relativeX, final int relativeY,
                final int absoluteX,
                final int absoluteY, final boolean leftButtonDown, final boolean middleButtonDown,
                final boolean rightButtonDown)
        {
            if (mouseSensitiveArea.identifier() == SensitiveAnimation.this.id)
            {
                SensitiveAnimation.this.moveArea(relativeX, relativeY, leftButtonDown, middleButtonDown,
                                                 rightButtonDown);
            }
        }
    }

    /**
     * Background color (Used if {@link #backgroundImage} is {@code null}
     */
    private       int                    backgroundColor;
    /**
     * Background image
     */
    private       JHelpImage             backgroundImage;
    /**
     * Image where animation is draw
     */
    private final JHelpImage             image;
    /**
     * Last element that the user clicked
     */
    private       SensitiveElement       lastDownSensitiveElement;
    /**
     * Mouse sensitive area for mouse capture
     */
    private final MouseSensitiveArea     mouseSensitiveArea;
    /**
     * Indicates if animation is already registered inside a {@link JHelpGameDynamic}
     */
    private       boolean                registered;
    /**
     * Sensitive area that manage mouse
     */
    private final SensitiveArea          sensitiveArea;
    /**
     * Last over element
     */
    private       SensitiveElement       sensitiveElementOver;
    /**
     * Sensitive element where shine effect playing
     */
    private       SensitiveElement       sensitiveElementShine;
    /**
     * All sensitive elements
     */
    private final List<SensitiveElement> sensitiveElements;
    /**
     * Actual shine effect step
     */
    private       int                    shineStep;
    /**
     * Sprite where animation draw
     */
    private       JHelpSprite            sprite;
    /**
     * X location in parent
     */
    private       int                    x;
    /**
     * Y location in parent
     */
    private       int                    y;
    /**
     * Animation ID
     */
    final         int                    id;

    /**
     * Create a new instance of SensitiveAnimation
     *
     * @param id     Animation ID
     * @param x      X location in parent
     * @param y      Y location in parent
     * @param width  Animation width
     * @param height Animation height
     */
    public SensitiveAnimation(final int id, final int x, final int y, final int width, final int height)
    {
        this.id = id;
        this.x = x;
        this.y = y;
        this.registered = false;
        this.backgroundColor = 0x44444444;
        this.image = new JHelpImage(width, height);
        this.mouseSensitiveArea = MouseSensitiveArea.createRectangleArea(id, 0, 0, width, height);
        this.mouseSensitiveArea.translation(x, y);
        this.sensitiveArea = new SensitiveArea();
        this.sensitiveElements = new ArrayList<SensitiveElement>();
    }

    /**
     * Compute sensitive element under a given position
     *
     * @param x X position
     * @param y Y position
     * @return Element under the position OR {@code null} if no element there
     */
    private SensitiveElement obtainSensitiveElement(final int x, final int y)
    {
        synchronized (this.sensitiveElements)
        {
            final int        size = this.sensitiveElements.size();
            SensitiveElement sensitiveElement;

            for (int index = size - 1; index >= 0; index--)
            {
                sensitiveElement = this.sensitiveElements.get(index);

                if ((sensitiveElement.visible()) && (sensitiveElement.enable())
                    && (sensitiveElement.containsPosition(x - this.x, y - this.y)))
                {
                    return sensitiveElement;
                }
            }
        }

        return null;
    }

    /**
     * Update element over from a given position
     *
     * @param x Position X
     * @param y Position Y
     */
    private void updateOverElement(final int x, final int y)
    {
        final SensitiveElement sensitiveElement = this.obtainSensitiveElement(x, y);

        if (this.sensitiveElementOver != sensitiveElement)
        {
            if (this.sensitiveElementOver != null)
            {
                this.sensitiveElementOver.mouseExit(this);
            }

            this.sensitiveElementOver = sensitiveElement;

            if (this.sensitiveElementOver != null)
            {
                this.sensitiveElementOver.mouseEnter(this);
            }
        }
    }

    /**
     * Create the sprite for the animation
     *
     * @param parent Image parent
     */
    void createSprite(final JHelpImage parent)
    {
        this.sprite = parent.createSprite(this.x, this.y, this.image);
        this.sprite.setVisible(true);
    }

    /**
     * Destroy properly the sprite
     *
     * @param parent Image parent
     */
    void destroySprite(final JHelpImage parent)
    {
        this.sprite.setVisible(false);
        parent.removeSprite(this.sprite);
        this.sprite = null;
    }

    /**
     * Play the animation
     *
     * @param frame Relative frame
     * @param image Image where draw
     * @return {@code true} if animation continue OR {@code false} if animation finished
     */
    boolean doAnimate(final float frame, final JHelpImage image)
    {
        synchronized (this.image)
        {
            this.image.startDrawMode();

            if (this.backgroundImage == null)
            {
                this.image.clear(this.backgroundColor);
            }
            else
            {
                this.image.clear(0);
                this.image.drawImage(0, 0, this.backgroundImage);
            }

            synchronized (this.sensitiveElements)
            {
                final List<SensitiveElement> canShine = new ArrayList<SensitiveElement>();

                for (final SensitiveElement sensitiveElement : this.sensitiveElements)
                {
                    if (sensitiveElement.visible())
                    {
                        this.image.drawImage(sensitiveElement.x(), sensitiveElement.y(),
                                             sensitiveElement.image());

                        if (sensitiveElement.enable())
                        {
                            canShine.add(sensitiveElement);
                        }
                    }
                }

                if (((this.sensitiveElementShine == null) || (!this.sensitiveElementShine.enable()) || (!this
                        .sensitiveElementShine.visible()))
                    && (!canShine.isEmpty()))
                {
                    this.shineStep = 0;
                    this.sensitiveElementShine = JHelpRandom.random(canShine);
                }

                if (this.sensitiveElementShine != null)
                {
                    final int shineX = (this.sensitiveElementShine.x() +
                                        (((SensitiveAnimation.TOTAL_SHINE_STEP - this.shineStep) *
                                          this.sensitiveElementShine.width()) / SensitiveAnimation.TOTAL_SHINE_STEP))
                                       - (SensitiveAnimation.SHINE_WIDTH >> 1);
                    final int shineY = (this.sensitiveElementShine.y() +
                                        (((SensitiveAnimation.TOTAL_SHINE_STEP - this.shineStep) *
                                          this.sensitiveElementShine.height()) /
                                         SensitiveAnimation.TOTAL_SHINE_STEP))
                                       - (SensitiveAnimation.SHINE_HEIGHT >> 1);
                    this.image.fillEllipse(shineX, shineY, SensitiveAnimation.SHINE_WIDTH,
                                           SensitiveAnimation.SHINE_HEIGHT, SensitiveAnimation.SHINE_COLOR);
                    this.shineStep++;

                    if (this.shineStep > SensitiveAnimation.TOTAL_SHINE_STEP)
                    {
                        this.shineStep = 0;
                        this.sensitiveElementShine = null;
                    }
                }
            }

            this.image.endDrawMode();
        }

        // this.sprite.visible(false);
        // this.sprite.visible(true);

        return true;
    }

    /**
     * Called when mouse enter the area
     *
     * @param x Mouse X
     * @param y Mouse Y
     */
    void enterArea(final int x, final int y)
    {
        this.updateOverElement(x, y);
    }

    /**
     * Called when mouse exit the area
     *
     * @param x Mouse X
     * @param y Mouse Y
     */
    void exitArea(final int x, final int y)
    {
        this.updateOverElement(x, y);
    }

    /**
     * Called when mouse move over the area
     *
     * @param x                Mouse X
     * @param y                Mouse Y
     * @param leftButtonDown   Indicates if left button is down
     * @param middleButtonDown Indicates if middle button is down
     * @param rightButtonDown  Indicates if right button is down
     */
    void moveArea(
            final int x, final int y, final boolean leftButtonDown, final boolean middleButtonDown,
            final boolean rightButtonDown)
    {
        this.updateOverElement(x, y);

        if (this.sensitiveElementOver != null)
        {
            if (leftButtonDown)
            {
                if (this.lastDownSensitiveElement == null)
                {
                    this.lastDownSensitiveElement = this.sensitiveElementOver;
                }
            }
            else if (this.lastDownSensitiveElement == this.sensitiveElementOver)
            {
                this.sensitiveElementOver.mouseClick(this);
                this.lastDownSensitiveElement = null;
            }
            else
            {
                this.lastDownSensitiveElement = null;
            }
        }
        else if (!leftButtonDown)
        {
            this.lastDownSensitiveElement = null;
        }
    }

    /**
     * Add a sensitive element inside the animation.<br>
     * It will be callback on mouse event over it
     *
     * @param sensitiveElement Sensitive element to add
     */
    public void addSensitiveElement(final SensitiveElement sensitiveElement)
    {
        if (sensitiveElement == null)
        {
            throw new NullPointerException("sensitiveElement mustn't be null");
        }

        synchronized (this.sensitiveElements)
        {
            if (!this.sensitiveElements.contains(sensitiveElement))
            {
                this.sensitiveElements.add(sensitiveElement);
            }
        }
    }

    /**
     * Make the background to unify color
     *
     * @param color New background color
     */
    public void background(final int color)
    {
        synchronized (this.image)
        {
            this.backgroundImage = null;
            this.backgroundColor = color;
        }
    }

    /**
     * Make the background as a texture image
     *
     * @param background Image background ({@code null} will return to last unify color)
     */
    public void background(JHelpImage background)
    {
        synchronized (this.image)
        {
            if (background != null)
            {
                background = JHelpImage.createResizedImage(background, this.image.getWidth(), this.image.getHeight());
            }

            this.backgroundImage = background;
        }
    }

    /**
     * Animation height
     *
     * @return Animation height
     */
    public int height()
    {
        return this.image.getHeight();
    }

    /**
     * Animation position on parent <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Animation position on parent
     * @see Positionable#position()
     */
    @Override
    public Position position()
    {
        return new Position(this.x, this.y);
    }

    /**
     * Change animation position in parent <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param position New position
     * @see Positionable#position(Position)
     */
    @Override
    public void position(final Position position)
    {
        this.x = position.getX();
        this.y = position.getY();
        this.mouseSensitiveArea.translation(this.x, this.y);

        if (this.sprite != null)
        {
            this.sprite.setPosition(this.x, this.y);
        }
    }

    /**
     * Register/link the animation inside a game dynamic to be visible and played
     *
     * @param gameDynamic Game dynamic to link with
     */
    public void registerInside(final JHelpGameDynamic gameDynamic)
    {
        if (this.registered)
        {
            return;
        }

        this.registered = true;
        gameDynamic.addArea(this.mouseSensitiveArea);
        gameDynamic.registerMouseSensitiveAreaListener(this.sensitiveArea);
        gameDynamic.playAnimation(this.sensitiveArea);
    }

    /**
     * Animation width
     *
     * @return Animation width
     */
    public int width()
    {
        return this.image.getWidth();
    }
}
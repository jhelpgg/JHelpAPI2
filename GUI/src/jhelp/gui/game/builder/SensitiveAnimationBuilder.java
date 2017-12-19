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
package jhelp.gui.game.builder;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import jhelp.gui.game.ButtonShape;
import jhelp.gui.game.SensitiveAnimation;
import jhelp.gui.game.SensitiveElement;
import jhelp.gui.game.SensitiveImage;
import jhelp.util.gui.JHelpDrawable;
import jhelp.util.gui.JHelpImage;
import jhelp.util.list.Pair;

/**
 * Builder helper for create sensitive animation draw inside a "window".<br>
 * First create the builder
 * {@link #SensitiveAnimationBuilder(int, int, int, PositionReferenceType, ButtonShape, JHelpDrawable, SensitiveAnimationLayout)}
 * , then use {@link #append(SensitiveElement, SensitiveAnimationConstraints)} for add elements inside the animation, finally
 * call {@link #build(int, int)} for create the animation
 *
 * @author JHelp
 */
public class SensitiveAnimationBuilder
{
    /**
     * Background drawable
     */
    private final JHelpDrawable                                               background;
    /**
     * Elements of the animation
     */
    private final List<Pair<SensitiveElement, SensitiveAnimationConstraints>> elements;
    /**
     * Animation ID
     */
    private final int                                                         id;
    /**
     * Relative position when build
     */
    private final PositionReferenceType                                       positionReferenceType;
    /**
     * Layout to use
     */
    private final SensitiveAnimationLayout                                    sensitiveAnimationLayout;
    /**
     * Window shape
     */
    private final ButtonShape                                                 windowShape;
    /**
     * X position
     */
    private final int                                                         x;
    /**
     * Y position
     */
    private final int                                                         y;

    /**
     * Create a new instance of SensitiveAnimationBuilder
     *
     * @param id                       Animation ID
     * @param x                        X location
     * @param y                        Y location
     * @param positionReferenceType    Relative position when build
     * @param windowShape              Shape to use for the "window"
     * @param background               Background to draw inside the "window"
     * @param sensitiveAnimationLayout Layout to use when add element
     */
    public SensitiveAnimationBuilder(
            final int id, final int x, final int y, final PositionReferenceType positionReferenceType,
            final ButtonShape windowShape,
            final JHelpDrawable background, final SensitiveAnimationLayout sensitiveAnimationLayout)
    {
        if (positionReferenceType == null)
        {
            throw new NullPointerException("positionReferenceType mustn't be null");
        }

        if (windowShape == null)
        {
            throw new NullPointerException("windowShape mustn't be null");
        }

        if (background == null)
        {
            throw new NullPointerException("background mustn't be null");
        }

        if (sensitiveAnimationLayout == null)
        {
            throw new NullPointerException("sensitiveAnimationLayout mustn't be null");
        }

        this.id = id;
        this.x = x;
        this.y = y;
        this.positionReferenceType = positionReferenceType;
        this.windowShape = windowShape;
        this.background = background;
        this.sensitiveAnimationLayout = sensitiveAnimationLayout;
        this.elements = new ArrayList<Pair<SensitiveElement, SensitiveAnimationConstraints>>();
    }

    /**
     * Add element inside the future animation
     *
     * @param sensitiveElement              Element to add
     * @param sensitiveAnimationConstraints Constraints to use
     */
    public void append(
            final SensitiveElement sensitiveElement, final SensitiveAnimationConstraints sensitiveAnimationConstraints)
    {
        if (!this.sensitiveAnimationLayout.isCompatible(sensitiveAnimationConstraints))
        {
            throw new IllegalArgumentException("The given constraints is not compatible with the current layout");
        }

        this.elements.add(new Pair<SensitiveElement, SensitiveAnimationConstraints>(sensitiveElement,
                                                                                    sensitiveAnimationConstraints));
    }

    /**
     * Build the animation
     *
     * @param width  Parent width
     * @param height Parent height
     * @return Created animation "window"
     */
    public SensitiveAnimation build(final int width, final int height)
    {
        final Rectangle bounds = this.sensitiveAnimationLayout.layout(this.elements, 0, 0, width,
                                                                      height);
        final PositionReference positionReference = new PositionReference(this.x, this.y, width, height,
                                                                          this.positionReferenceType);
        final Point location = positionReference.computeLocation(bounds.width, bounds.height);
        final SensitiveAnimation sensitiveAnimation = new SensitiveAnimation(this.id, location.x, location.y,
                                                                             bounds.width, bounds.height);
        sensitiveAnimation.background(0);

        final JHelpImage imageBackground = new JHelpImage(bounds.width, bounds.height);
        this.windowShape.fill(0, 0, bounds.width, bounds.height, this.background, imageBackground);
        final SensitiveElement elementBackground = new SensitiveImage(0, 0, imageBackground);
        sensitiveAnimation.addSensitiveElement(elementBackground);

        for (final Pair<SensitiveElement, SensitiveAnimationConstraints> element : this.elements)
        {
            sensitiveAnimation.addSensitiveElement(element.first);
        }

        return sensitiveAnimation;
    }
}
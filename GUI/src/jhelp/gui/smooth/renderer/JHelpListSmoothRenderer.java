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

package jhelp.gui.smooth.renderer;

import jhelp.gui.smooth.JHelpComponentSmooth;
import jhelp.gui.smooth.JHelpListSmooth;

/**
 * Renderer for {@link JHelpListSmooth}.<br>
 * It defines the component to draw for each list item and how draw them in different situation.<br>
 * When a component need to be created {@link #createComponent(Object)} is called.<br>
 * When a component need to be refresh {@link #transformComponent(JHelpComponentSmooth, Object, int, boolean)} is called. The
 * given component here is the same returned by {@link #createComponent(Object)} for the same element, so you can cast it as
 * appropriate.
 *
 * @param <ELEMENT> Element type
 * @author JHelp
 */
public interface JHelpListSmoothRenderer<ELEMENT>
{
    /**
     * Create a component for a given element
     *
     * @param element Element to have its component
     * @return Created component
     */
    JHelpComponentSmooth createComponent(ELEMENT element);

    /**
     * Transform a component previously created by {@link #createComponent(Object)} to give it the right appearance.<br>
     * Transform can be change : background, shadow, shadow level ,...
     *
     * @param component   Component to transform
     * @param element     Element on component
     * @param indexInList Index inside the list
     * @param selected    Indicates if component is selected
     */
    void transformComponent(JHelpComponentSmooth component, ELEMENT element, int indexInList, boolean selected);
}
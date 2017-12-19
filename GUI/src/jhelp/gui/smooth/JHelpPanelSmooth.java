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

package jhelp.gui.smooth;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import jhelp.gui.smooth.layout.JHelpConstraintsSmooth;
import jhelp.gui.smooth.layout.JHelpLayoutSmooth;
import jhelp.gui.smooth.shape.ShadowLevel;
import jhelp.util.gui.JHelpImage;
import jhelp.util.list.Pair;

/**
 * Panel for layout components.<br>
 * Panel use its {@link JHelpLayoutSmooth} to layout components inside it.<br>
 * Its possible to change the panel layout, but the change is so important, that it removes all the components inside the panel,
 * you have to add them again with the new appropriate {@link JHelpConstraintsSmooth}
 *
 * @author JHelp
 */
public class JHelpPanelSmooth
        extends JHelpContainerSmooth
{
    /**
     * Panel content : couple of component and the associated constraints
     */
    private final List<Pair<JHelpComponentSmooth, JHelpConstraintsSmooth>> children;
    /**
     * Layout to use
     */
    private       JHelpLayoutSmooth                                        layout;

    /**
     * Create a new instance of JHelpPanelSmooth
     *
     * @param layout Layout to use
     */
    public JHelpPanelSmooth(final JHelpLayoutSmooth layout)
    {
        if (layout == null)
        {
            throw new NullPointerException("layout mustn't be null");
        }

        this.children = new ArrayList<Pair<JHelpComponentSmooth, JHelpConstraintsSmooth>>();
        this.layout = layout;
        this.shadowLevel(ShadowLevel.NO_SHADOW);
    }

    /**
     * Draw the panel <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param image        Image where draw
     * @param x            X position
     * @param y            Y position
     * @param width        Panel width
     * @param height       Panel height
     * @param parentWidth  Parent width
     * @param parentHeight Parent height
     * @see jhelp.gui.smooth.JHelpComponentSmooth#paint(JHelpImage, int, int, int, int, int, int)
     */
    @Override
    protected void paint(
            final JHelpImage image, int x, int y, int width, int height, final int parentWidth, final int parentHeight)
    {
        final Insets insets = this.shape().computeInsets(this.shadowLevel(), width, height);
        x -= insets.left;
        y -= insets.top;
        width += insets.left + insets.right;
        height += insets.top + insets.bottom;

        final Rectangle place = this.layout.layoutComponents(this, x, y, width,
                                                             height);
        final int                                          count = this.numberOfChildren();
        Pair<JHelpComponentSmooth, JHelpConstraintsSmooth> pair;
        Rectangle                                          bounds;

        this.bounds(place.x, place.y, place.width, place.height);
        this.drawBackground(image, place.x, place.y, place.width, place.height);

        for (int index = 0; index < count; index++)
        {
            pair = this.children.get(index);

            if (pair.first.visible())
            {
                bounds = pair.first.bounds();
                image.pushClipIntersect(Math.max(bounds.x, place.x), Math.max(bounds.y, place.y),
                                        Math.min(bounds.width, place.width),
                                        Math.min(bounds.height, place.height));
                pair.first.paint(image, bounds.x, bounds.y, bounds.width, bounds.height, place.width, place.height);
                image.popClip();
            }
        }
    }

    /**
     * Compute preferred size <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Computed preferred size
     * @see jhelp.gui.smooth.JHelpComponentSmooth#preferredSizeInternal()
     */
    @Override
    protected Dimension preferredSizeInternal()
    {
        return this.layout.computePreferredSize(this);
    }

    /**
     * Obtain component under an absolute mouse position <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param x Mouse X on main frame
     * @param y Mouse Y on main frame
     * @return Component in panel under mouse OR {@code null} if none
     * @see jhelp.gui.smooth.JHelpComponentSmooth#obtainComponentUnder(int, int, boolean)
     */
    @Override
    public final JHelpComponentSmooth obtainComponentUnder(final int x, final int y, final boolean rightButton)
    {
        final Rectangle bounds = this.bounds();

        if (bounds.contains(x, y))
        {
            JHelpComponentSmooth componentSmooth;

            for (final Pair<JHelpComponentSmooth, JHelpConstraintsSmooth> child : this.children)
            {
                componentSmooth = child.first.obtainComponentUnder(x, y, rightButton);

                if (componentSmooth != null)
                {
                    return componentSmooth;
                }
            }
        }

        return null;
    }

    /**
     * Search component by ID <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param id Component ID searched
     * @return The component or {@code null} if not found
     * @see jhelp.gui.smooth.JHelpComponentSmooth#searchComponent(int)
     */
    @Override
    public final JHelpComponentSmooth searchComponent(final int id)
    {
        if (id == this.id())
        {
            return this;
        }

        JHelpComponentSmooth componentSmooth;

        for (final Pair<JHelpComponentSmooth, JHelpConstraintsSmooth> child : this.children)
        {
            componentSmooth = child.first.searchComponent(id);

            if (componentSmooth != null)
            {
                return componentSmooth;
            }
        }

        return null;
    }

    /**
     * Add component inside the panel.<br>
     * The component is associate to a constraints, that MUST be accurate to the panel layout
     *
     * @param component   Component to add
     * @param constraints Constraints to use
     */
    public void addComponent(final JHelpComponentSmooth component, final JHelpConstraintsSmooth constraints)
    {
        if (component == null)
        {
            throw new NullPointerException("component mustn't be null");
        }

        if (constraints == null)
        {
            throw new NullPointerException("constraints mustn't be null");
        }

        if (!this.layout.acceptConstraints(constraints))
        {
            throw new IllegalArgumentException(
                    "The given constraints " + constraints.getClass().getName() + " don't fit with the layout "
                    + this.layout.getClass().getName());
        }

        component.parent(this, this.children.size());
        this.children.add(new Pair<JHelpComponentSmooth, JHelpConstraintsSmooth>(component, constraints));
    }

    /**
     * Obtain one panel child <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param index Child index
     * @return Child component
     * @see jhelp.gui.smooth.JHelpContainerSmooth#child(int)
     */
    @Override
    public JHelpComponentSmooth child(final int index)
    {
        return this.children.get(index).first;
    }

    /**
     * Number of children <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Number of children
     * @see jhelp.gui.smooth.JHelpContainerSmooth#numberOfChildren()
     */
    @Override
    public int numberOfChildren()
    {
        return this.children.size();
    }

    /**
     * Get couple component and associate constraints
     *
     * @param index Couple index
     * @return Couple of component and associate constraints
     */
    public Pair<JHelpComponentSmooth, JHelpConstraintsSmooth> childAndConstraints(final int index)
    {
        return this.children.get(index);
    }

    /**
     * Clear the panel. Remove all its children
     */
    public void clearPanel()
    {
        final int count = this.children.size();

        for (int i = count - 1; i >= 0; i--)
        {
            this.children.get(i).first.detachFromParent();
        }

        this.children.clear();
    }

    /**
     * Search index of component
     *
     * @param component Component searched
     * @return Component index or -1 if not inside the panel
     */
    public int componentIndex(final JHelpComponentSmooth component)
    {
        final int                                          count = this.children.size();
        Pair<JHelpComponentSmooth, JHelpConstraintsSmooth> pair;

        for (int i = count - 1; i >= 0; i--)
        {
            pair = this.children.get(i);

            if (pair.first.equals(component))
            {
                return i;
            }
        }

        return -1;
    }

    /**
     * Insert a component inside the panel at a specific index
     *
     * @param index       Insert where insert
     * @param component   Component to insert
     * @param constraints Associated constraints
     */
    public void insertComponent(
            int index, final JHelpComponentSmooth component, final JHelpConstraintsSmooth constraints)
    {
        if (component == null)
        {
            throw new NullPointerException("component mustn't be null");
        }

        if (constraints == null)
        {
            throw new NullPointerException("constraints mustn't be null");
        }

        if (!this.layout.acceptConstraints(constraints))
        {
            throw new IllegalArgumentException(
                    "The given constraints " + constraints.getClass().getName() + " don't fit with the layout "
                    + this.layout.getClass().getName());
        }

        final int count = this.children.size();
        index = Math.max(0, index);

        if (index >= count)
        {
            this.addComponent(component, constraints);
            return;
        }

        component.parent(this, index);
        this.children.add(index, new Pair<JHelpComponentSmooth, JHelpConstraintsSmooth>(component, constraints));

        for (int i = count; i > index; i--)
        {
            this.children.get(i).first.incrementIndexInParent();
        }
    }

    /**
     * Change panel layout<br>
     * WARNING, the change is so important, that it remove all panel children. You have to add them again
     *
     * @param layout New layout to use
     */
    public void layout(final JHelpLayoutSmooth layout)
    {
        if (layout == null)
        {
            throw new NullPointerException("layout mustn't be null");
        }

        this.layout = layout;
        this.children.clear();
    }

    /**
     * Remove a component
     *
     * @param component Component to remove
     */
    public void removeComponent(final JHelpComponentSmooth component)
    {
        final int index = this.componentIndex(component);

        if (index < 0)
        {
            return;
        }

        this.removeComponent(index);
    }

    /**
     * Remove a component
     *
     * @param index Component index to remove
     */
    public void removeComponent(final int index)
    {
        final int count = this.children.size();

        if ((index < 0) || (index >= count))
        {
            return;
        }

        Pair<JHelpComponentSmooth, JHelpConstraintsSmooth> pair = this.children.remove(index);
        pair.first.detachFromParent();

        for (int i = count - 2; i >= index; i--)
        {
            pair = this.children.get(i);
            pair.first.decrementIndexInParent();
        }
    }
}
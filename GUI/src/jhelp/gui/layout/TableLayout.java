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
package jhelp.gui.layout;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import jhelp.util.gui.UtilGUI;

/**
 * Layout components in table.<br>
 * Imagine the panel cut in cells like a grid. You decide then which component goes on which cell and the number of cell
 * it take in width and height.<br>
 * For exemple you can layout like this : <br>
 * <table border=1>
 * <tr><th colspan=2 rowspan=2>A</th><th>&nbsp;</th><th>&nbsp;</th></tr>
 * <tr><th>&nbsp;</th><th>&nbsp;</th></tr>
 * <tr><th>&nbsp;</th><th colspan=3 rowSpan=2>B</th></tr>
 * <tr><th>&nbsp;</th></tr>
 * <tr><th>&nbsp;</th><th>&nbsp;</th><th>&nbsp;</th><th>&nbsp;</th></tr>
 * </table>
 * In this example:
 * <ul>
 * <li>A is on cell (0, 0) and take 2x2 cells.</li>
 * <li>B is on cell (1, 2) and take 3x2 cells.</li>
 * </ul>
 * Exemple of code for this :
 * <pre>
 * <code lang="java">
 * // ...
 * panel.layout(new TableLayout());
 * panel.add(componentA, new TableLayout.Constraints(0, 0, 2, 2));
 * panel.add(componentB, new TableLayout.Constraints(1, 2, 3, 2));
 * </code>
 * </pre>
 */
public class TableLayout implements LayoutManager2
{
    /**
     * Canstraints to use with {@link TableLayout}
     */
    public static class Constraints
    {
        /**
         * Current height
         */
        int currentHeight;
        /**
         * Current width
         */
        int currentWidth;
        /**
         * Number of cell in height
         */
        public final int height;
        /**
         * Number of cell in width
         */
        public final int width;
        /**
         * Cell x
         */
        public final int x;
        /**
         * Cell y
         */
        public final int y;

        /**
         * Create contraints for component that take 1 cell per 1 cell
         *
         * @param x Cell X
         * @param y Cell Y
         */
        public Constraints(int x, int y)
        {
            this(x, y, 1, 1);
        }

        /**
         * Create constraints
         *
         * @param x      Cell X
         * @param y      Cell Y
         * @param width  Number of cell in width
         * @param height Number of cell in height
         */
        public Constraints(int x, int y, int width, int height)
        {
            this.x = x;
            this.y = y;
            this.width = Math.max(1, width);
            this.height = Math.max(1, height);
        }

        /**
         * Indicates if cell is inside the constraints
         *
         * @param point Cell coordinate
         * @return {@code true} if cell is inside the constraints
         */
        public boolean contains(Point point)
        {
            if (point == null)
            {
                return false;
            }

            return this.contains(point.x, point.y);
        }

        /**
         * Indicates if cell is inside the constraints
         *
         * @param x Cell X
         * @param y Cell Y
         * @return {@code true} if cell is inside the constraints
         */
        public boolean contains(int x, int y)
        {
            return x >= this.x && y >= this.y && x < this.x + this.width && y < this.y + this.height;
        }

        /**
         * Indicates if constraints intersects a group of cells
         *
         * @param x      Cell X of  group of cells
         * @param y      Cell Y of  group of cells
         * @param width  Number of cell in width of  group of cells
         * @param height Number of cell in height  of  group of cells
         * @return {@code true} if constraints intersects a group of cells
         */
        public boolean intersects(int x, int y, int width, int height)
        {
            return this.intersects(new Rectangle(x, y, width, height));
        }

        /**
         * Indicates if constraints intersects a group of cells
         *
         * @param rectangle Rectangle of cells
         * @return {@code true} if constraints intersects a group of cells
         */
        public boolean intersects(Rectangle rectangle)
        {
            if (rectangle.width <= 0 || rectangle.height <= 0)
            {
                return false;
            }

            return rectangle.intersects(this.x, this.y, this.width, this.height);
        }

        /**
         * String representation
         *
         * @return String representation
         */
        @Override
        public String toString()
        {
            return "TableLayout.Constraints (" + this.x + ", " + this.y + ") " + this.width + "x" + this.height;
        }
    }

    /**
     * Last cell height computed by {@link #layoutContainer(Container)}
     */
    private       int                         cellHeight;
    /**
     * Last cell width computed by {@link #layoutContainer(Container)}
     */
    private       int                         cellWidth;
    /**
     * Associate components to theire layout
     */
    private final Map<Component, Constraints> components;
    /**
     * Fixed number of cells
     */
    private       Dimension                   fixedNumberOfCells;
    /**
     * Margins around components
     */
    private final Insets                      margins;
    /**
     * Last minimum cell X computed by {@link #layoutContainer(Container)}
     */
    private       int                         minimumX;
    /**
     * Last minimum cell Y computed by {@link #layoutContainer(Container)}
     */
    private       int                         minimumY;

    /**
     * Create empty table layout
     */
    public TableLayout()
    {
        this.components = new HashMap<>();
        this.margins = new Insets(0, 0, 0, 0);
    }

    /**
     * Compute the size with current collected information
     *
     * @return Computed size
     */
    private Dimension computeSize()
    {
        int minX       = Integer.MAX_VALUE;
        int maxX       = Integer.MIN_VALUE;
        int minY       = Integer.MAX_VALUE;
        int maxY       = Integer.MIN_VALUE;
        int cellWidth  = 1;
        int cellHeight = 1;

        if (this.fixedNumberOfCells != null)
        {
            minX = 0;
            minY = 0;
            maxX = this.fixedNumberOfCells.width;
            maxY = this.fixedNumberOfCells.height;
        }

        for (Constraints constraints : this.components.values())
        {
            if (constraints.currentWidth > 0 && constraints.currentHeight > 0)
            {
                minX = Math.min(minX, constraints.x);
                maxX = Math.max(maxX, constraints.x + constraints.width);
                minY = Math.min(minY, constraints.y);
                maxY = Math.max(maxY, constraints.y + constraints.height);
                cellWidth = Math.max(cellWidth, constraints.currentWidth / constraints.width);
                cellHeight = Math.max(cellHeight, constraints.currentHeight / constraints.height);
            }
        }

        if (minX >= maxX || minY >= maxY)
        {
            return new Dimension(1, 1);
        }

        if (this.fixedNumberOfCells != null)
        {
            minX = 0;
            minY = 0;
            maxX = this.fixedNumberOfCells.width;
            maxY = this.fixedNumberOfCells.height;
        }

        return new Dimension((maxX - minX) * (this.margins.left + cellWidth + this.margins.right),
                             (maxY - minY) * (this.margins.top + cellHeight + this.margins.bottom));
    }

    /**
     * Initialize the size computing
     */
    private void initializeComputingSizes()
    {
        for (Constraints constraints : this.components.values())
        {
            constraints.currentWidth = 0;
            constraints.currentHeight = 0;
        }
    }

    /**
     * Adds the specified component to the layout, using the specified
     * constraint object.
     *
     * @param component   the component to be added
     * @param constraints where/how the component is added to the layout.
     */
    @Override
    public void addLayoutComponent(Component component, Object constraints)
    {
        if (component == null || constraints == null)
        {
            return;
        }

        if (!(constraints instanceof Constraints))
        {
            throw new IllegalArgumentException(
                    "constraints MUST be an instance of " + Constraints.class.getName() + " not " +
                    constraints.getClass()
                               .getName());
        }

        this.components.put(component, (Constraints) constraints);
    }

    /**
     * Calculates the maximum size dimensions for the specified container,
     * given the components it contains.
     *
     * @param parent Parent of compnents
     * @see Component#getMaximumSize
     * @see LayoutManager2
     */
    @Override
    public Dimension maximumLayoutSize(Container parent)
    {
        this.initializeComputingSizes();

        int         nb = parent.getComponentCount();
        Dimension   dimension;
        Component   component;
        Constraints constraints;

        for (int i = 0; i < nb; i++)
        {
            component = parent.getComponent(i);
            constraints = this.components.get(component);

            if (component.isVisible() && constraints != null)
            {
                dimension = UtilGUI.computeMaximumDimension(component);
                constraints.currentWidth = dimension.width;
                constraints.currentHeight = dimension.height;
            }
        }

        return this.computeSize();
    }

    /**
     * Returns the alignment along the x axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     *
     * @param target Parent to have its alignement
     */
    @Override
    public float getLayoutAlignmentX(Container target)
    {
        return 0;
    }

    /**
     * Returns the alignment along the y axis.  This specifies how
     * the component would like to be aligned relative to other
     * components.  The value should be a number between 0 and 1
     * where 0 represents alignment along the origin, 1 is aligned
     * the furthest away from the origin, 0.5 is centered, etc.
     *
     * @param target Parent to have its alignement
     */
    @Override
    public float getLayoutAlignmentY(Container target)
    {
        return 0;
    }

    /**
     * Invalidates the layout, indicating that if the layout manager
     * has cached information it should be discarded.
     *
     * @param target Parent of components
     */
    @Override
    public void invalidateLayout(Container target)
    {
        // Nothing to do
    }

    /**
     * If the layout manager uses a per-component string,
     * adds the component <code>comp</code> to the layout,
     * associating it with the string specified by <code>name</code>.
     *
     * @param name the string to be associated with the component
     * @param comp the component to be added
     */
    @Override
    public void addLayoutComponent(String name, Component comp)
    {
        //Nothiong to do, we don't manage String name
    }

    /**
     * Removes the specified component from the layout.
     *
     * @param component the component to be removed
     */
    @Override
    public void removeLayoutComponent(Component component)
    {
        this.components.remove(component);
    }

    /**
     * Calculates the preferred size dimensions for the specified
     * container, given the components it contains.
     *
     * @param parent the container to be laid out
     * @see #minimumLayoutSize
     */
    @Override
    public Dimension preferredLayoutSize(Container parent)
    {
        this.initializeComputingSizes();

        int         nb = parent.getComponentCount();
        Dimension   dimension;
        Component   component;
        Constraints constraints;

        for (int i = 0; i < nb; i++)
        {
            component = parent.getComponent(i);
            constraints = this.components.get(component);

            if (component.isVisible() && constraints != null)
            {
                dimension = UtilGUI.computePreferredDimension(component);
                constraints.currentWidth = dimension.width;
                constraints.currentHeight = dimension.height;
            }
        }

        return this.computeSize();
    }

    /**
     * Calculates the minimum size dimensions for the specified
     * container, given the components it contains.
     *
     * @param parent the component to be laid out
     * @see #preferredLayoutSize
     */
    @Override
    public Dimension minimumLayoutSize(Container parent)
    {
        this.initializeComputingSizes();

        int         nb = parent.getComponentCount();
        Dimension   dimension;
        Component   component;
        Constraints constraints;

        for (int i = 0; i < nb; i++)
        {
            component = parent.getComponent(i);
            constraints = this.components.get(component);

            if (component.isVisible() && constraints != null)
            {
                dimension = UtilGUI.computeMinimumDimension(component);
                constraints.currentWidth = dimension.width;
                constraints.currentHeight = dimension.height;
            }
        }

        return this.computeSize();
    }

    /**
     * Lays out the specified container.
     *
     * @param parent the container to be laid out
     */
    @Override
    public void layoutContainer(Container parent)
    {
        final Dimension parentSize = parent.getSize();

        final Insets insets = parent.getInsets();
        parentSize.width = parentSize.width - insets.right - insets.right;
        parentSize.height = parentSize.height - insets.top - insets.bottom;

        synchronized (parent.getTreeLock())
        {
            int         minX = Integer.MAX_VALUE;
            int         maxX = Integer.MIN_VALUE;
            int         minY = Integer.MAX_VALUE;
            int         maxY = Integer.MIN_VALUE;
            int         nb   = parent.getComponentCount();
            Component   component;
            Constraints constraints;

            if (this.fixedNumberOfCells != null)
            {
                minX = 0;
                minY = 0;
                maxX = this.fixedNumberOfCells.width;
                maxY = this.fixedNumberOfCells.height;
            }
            else
            {
                for (int i = 0; i < nb; i++)
                {
                    component = parent.getComponent(i);
                    constraints = this.components.get(component);

                    if (component.isVisible() && constraints != null)
                    {
                        minX = Math.min(minX, constraints.x);
                        maxX = Math.max(maxX, constraints.x + constraints.width);
                        minY = Math.min(minY, constraints.y);
                        maxY = Math.max(maxY, constraints.y + constraints.height);
                    }
                }
            }

            this.minimumX = minX;
            this.minimumY = minY;

            if (minX < maxX && minY < maxY)
            {
                this.cellWidth = parentSize.width / (maxX - minX);
                this.cellHeight = parentSize.height / (maxY - minY);

                for (int i = 0; i < nb; i++)
                {
                    component = parent.getComponent(i);
                    constraints = this.components.get(component);

                    if (component.isVisible() && constraints != null)
                    {
                        component.setLocation(this.margins.left + (minX + constraints.x) * this.cellWidth,
                                              this.margins.top + (minY + constraints.y) * this.cellHeight);
                        component.setSize(constraints.width * this.cellWidth - this.margins.left - this.margins.right,
                                          constraints.height * this.cellHeight - this.margins.top -
                                          this.margins.bottom);
                    }
                }
            }
        }
    }

    /**
     * Let the number of cells and the minimums computed by the components and theire constraints
     */
    public void dynamicStructure()
    {
        this.fixedNumberOfCells = null;
    }

    /**
     * Force the number of cells.<br>
     * After this the minimum cell X and Y will be 0
     *
     * @param numberOfColums Number of columns
     * @param numberOfRows   Number of rows
     */
    public void fixStructure(int numberOfColums, int numberOfRows)
    {
        this.fixedNumberOfCells = new Dimension(Math.max(1, numberOfColums), Math.max(1, numberOfRows));
    }

    /**
     * Cell size in pixels computed by last call to {@link #layoutContainer(Container)}.<br>
     * Note : {@link #layoutContainer(Container)} is called by the UI each time container is render
     *
     * @return Last cell size
     */
    public Dimension getCellSize()
    {
        return new Dimension(this.cellWidth, this.cellHeight);
    }

    /**
     * Margin around components
     *
     * @return Margin around components
     */
    public @NotNull Insets getMargins()
    {
        return new Insets(this.margins.top, this.margins.left, this.margins.bottom, this.margins.right);
    }

    /**
     * Modify margins around components
     *
     * @param margins New margins
     */
    public void setMargins(@NotNull Insets margins)
    {
        this.margins.top = Math.max(0, margins.top);
        this.margins.left = Math.max(0, margins.left);
        this.margins.bottom = Math.max(0, margins.bottom);
        this.margins.right = Math.max(0, margins.right);
    }

    /**
     * Minimum cell X and Y computed by last call to {@link #layoutContainer(Container)}.<br>
     * Note : {@link #layoutContainer(Container)} is called by the UI each time container is render
     *
     * @return Last minimum cell X and Y
     */
    public Point getMinimums()
    {
        return new Point(this.minimumX, this.minimumY);
    }

    /**
     * Obtain component constarints
     *
     * @param component Component
     * @return Associate constraints OR {@code null} if component not managed by the layout
     */
    public @Nullable
    Constraints obtainConstraints(Component component)
    {
        return this.components.get(component);
    }

    /**
     * Change the contraints of a component
     *
     * @param component   Component to change its contraints
     * @param constraints New constraints to apply
     */
    public void setContraints(Component component, Constraints constraints)
    {
        if (component == null || constraints == null)
        {
            return;
        }

        this.components.put(component, constraints);
    }

    /**
     * Modify margins arround component
     *
     * @param top    Margin to add on top
     * @param left   Margin to add on left
     * @param bottom Margin to add on bottom
     * @param right  Margin to add on right
     */
    public void setMargins(int top, int left, int bottom, int right)
    {
        this.margins.top = Math.max(0, top);
        this.margins.left = Math.max(0, left);
        this.margins.bottom = Math.max(0, bottom);
        this.margins.right = Math.max(0, right);
    }
}

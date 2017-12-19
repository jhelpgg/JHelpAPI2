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

/*
 * License :
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may cause.
 * You can use, modify, the code as your need for any usage.
 * But you can't do any action that avoid me or other person use, modify this code.
 * The code is free for usage and modification, you can't change that fact.
 * JHelp
 */

/*
 * License :
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may cause.
 * You can use, modify, the code as your need for any usage.
 * But you can't do any action that avoid me or other person use, modify this code.
 * The code is free for usage and modification, you can't change that fact.
 * JHelp
 */

/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any
 * damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.gui;

import com.sun.istack.internal.Nullable;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import jhelp.gui.action.GenericAction;
import jhelp.gui.layout.TableLayout;
import jhelp.util.gui.UtilGUI;
import jhelp.util.math.Math2;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.ThreadManager;

/**
 * Table with capacity to easly add/remove columns or rows, unify/split/move cells.<br>
 * It is possible to control the components creation on giving a {@link CellComponentCreator}
 */
public class JHelpTable extends JPanel
{
    /**
     * Text key for action to move one cell
     */
    static final String TEXT_KEY_ACTION_MOVE  = "table.action.move";
    /**
     * Text key for action to split one cell
     */
    static final String TEXT_KEY_ACTION_SPLIT = "table.action.cellSplit";
    /**
     * Text key for action to unify cells
     */
    static final String TEXT_KEY_ACTION_UNION = "table.action.cellUnion";

    /**
     * Action to start moving a cell
     */
    class ActionMoveCell extends GenericAction
    {
        /**
         * Create action
         */
        ActionMoveCell()
        {
            super(JHelpTable.TEXT_KEY_ACTION_MOVE, ResourcesGUI.RESOURCE_TEXT);
        }

        /**
         * Called when action have to do its action
         *
         * @param actionEvent Action event description
         */
        @Override
        protected void doActionPerformed(ActionEvent actionEvent)
        {
            JHelpTable.this.doActionMoveCell();
        }
    }

    /**
     * Action for unify/split
     */
    class ActionUnifyOrSplit extends GenericAction
    {
        /**
         * Create action
         */
        ActionUnifyOrSplit()
        {
            super(JHelpTable.TEXT_KEY_ACTION_UNION, ResourcesGUI.RESOURCE_TEXT);
        }

        /**
         * Called when action have to do its action
         *
         * @param actionEvent Action event description
         */
        @Override
        protected void doActionPerformed(ActionEvent actionEvent)
        {
            JHelpTable.this.doActionUnifyOrSplit();
        }
    }

    /**
     * Manager of mouse events
     */
    class EventManager implements MouseListener, MouseMotionListener
    {
        /**
         * Create the manager
         */
        EventManager()
        {
        }

        /**
         * Invoked when the mouse button has been clicked (pressed
         * and released) on a component.
         *
         * @param mouseEvent Event description
         */
        @Override
        public void mouseClicked(MouseEvent mouseEvent)
        {
            Point     point     = new Point();
            Component component = mouseEvent.getComponent();

            if (component != null)
            {
                point = UtilGUI.getLocationOn(component, JHelpTable.this);
            }

            assert point != null;

            if (SwingUtilities.isLeftMouseButton(mouseEvent))
            {
                Dimension sizeInCell = JHelpTable.this.cellSize();
                int       x          = (mouseEvent.getX() + point.x) / sizeInCell.width;
                int       y          = (mouseEvent.getY() + point.y) / sizeInCell.height;

                if (mouseEvent.isShiftDown())
                {
                    JHelpTable.this.addSelection(x, y);
                }
                else
                {
                    JHelpTable.this.setSelection(x, y);
                }

                JHelpTable.this.revalidate();
                JHelpTable.this.repaint();
                JHelpTable.this.clickOn(mouseEvent.getX(), mouseEvent.getY());
            }
            else if (SwingUtilities.isRightMouseButton(mouseEvent))
            {
                JHelpTable.this.decideIfShowPopup(mouseEvent.getX() + point.x, mouseEvent.getY() + point.y);
            }
        }

        /**
         * Invoked when a mouse button has been pressed on a component.
         *
         * @param mouseEvent Event description
         */
        @Override
        public void mousePressed(MouseEvent mouseEvent)
        {
            if (JHelpTable.this.highlightArea == null)
            {
                return;
            }

            if (SwingUtilities.isRightMouseButton(mouseEvent))
            {
                JHelpTable.this.highlightArea = null;
                JHelpTable.this.repaint();
                return;
            }

            JHelpTable.this.validateMove();
        }

        /**
         * Invoked when a mouse button has been released on a component.
         *
         * @param mouseEvent Event description
         */
        @Override
        public void mouseReleased(MouseEvent mouseEvent)
        {
            // Nothing to do
        }

        /**
         * Invoked when the mouse enters a component.
         *
         * @param mouseEvent Event description
         */
        @Override
        public void mouseEntered(MouseEvent mouseEvent)
        {
            // Nothing to do
        }

        /**
         * Invoked when the mouse exits a component.
         *
         * @param mouseEvent Event description
         */
        @Override
        public void mouseExited(MouseEvent mouseEvent)
        {
            // Nothing to do
        }

        /**
         * Invoked when a mouse button is pressed on a component and then
         * dragged.  <code>MOUSE_DRAGGED</code> events will continue to be
         * delivered to the component where the drag originated until the
         * mouse button is released (regardless of whether the mouse position
         * is within the bounds of the component).
         * <p>
         * Due to platform-dependent Drag&amp;Drop implementations,
         * <code>MOUSE_DRAGGED</code> events may not be delivered during a native
         * Drag&amp;Drop operation.
         *
         * @param mouseEvent Event description
         */
        @Override
        public void mouseDragged(MouseEvent mouseEvent)
        {
            // Nothing to do
        }

        /**
         * Invoked when the mouse cursor has been moved onto a component
         * but no buttons have been pushed.
         *
         * @param mouseEvent Event description
         */
        @Override
        public void mouseMoved(MouseEvent mouseEvent)
        {
            if (JHelpTable.this.highlightArea == null)
            {
                return;
            }

            Component component = mouseEvent.getComponent();

            if (component != null)
            {
                Point point = UtilGUI.getLocationOn(component, JHelpTable.this);
                assert point != null;
                Dimension sizeInCell   = JHelpTable.this.cellSize();
                int       x            = (mouseEvent.getX() + point.x) / sizeInCell.width;
                int       y            = (mouseEvent.getY() + point.y) / sizeInCell.height;
                int       nbCellWidth  = JHelpTable.this.highlightArea.width / sizeInCell.width;
                int       nbCellHeight = JHelpTable.this.highlightArea.height / sizeInCell.height;
                int       nbColumns    = JHelpTable.this.getNumberOfColumns();
                int       nbRows       = JHelpTable.this.getNumberOfRows();

                if (x + nbCellWidth > nbColumns)
                {
                    x = Math.max(0, nbColumns - nbCellWidth);
                }

                if (y + nbCellHeight > nbRows)
                {
                    y = Math.max(0, nbRows - nbCellHeight);
                }

                x *= sizeInCell.width;
                y *= sizeInCell.height;

                if (JHelpTable.this.highlightArea.x != x || JHelpTable.this.highlightArea.y != y)
                {
                    JHelpTable.this.highlightArea.x = x;
                    JHelpTable.this.highlightArea.y = y;
                    JHelpTable.this.repaint();
                }
            }
        }
    }

    /**
     * Task that fill holes
     */
    class TaskFillHoles implements ConsumerTask<List<Point>>
    {
        /**
         * Create task
         */
        TaskFillHoles()
        {
        }

        /**
         * Fill the holes
         *
         * @param points List of holes
         */
        @Override
        public void consume(List<Point> points)
        {
            for (Point point : points)
            {
                JHelpTable.this.addComponent(point.x, point.y,
                                             JHelpTable.this.cellComponentCreator.createComponentForCell(point.x,
                                                                                                         point.y));
            }
        }
    }

    /**
     * Action for move a cell
     */
    private final ActionMoveCell     actionMoveCell;
    /**
     * Action for unify or split cells
     */
    private final ActionUnifyOrSplit actionUnifyOrSplit;
    /*/Indicates if unify, split and move are allowed*/
    private       boolean            canUnifySplitOrMove;
    /**
     * User events manager
     */
    private final EventManager       eventManager;
    /**
     * High light color
     */
    private       Color              highlightColor;
    /**
     * Grid line color
     */
    private       Color              lineColor;
    /**
     * Grid line thick
     */
    private       int                lineThick;
    /**
     * Synchronization lock
     */
    private final Object lock = new Object();
    /**
     * Manipulated component for move, split or unify
     */
    private       Component               manipulatedComponent;
    /**
     * Number of columns
     */
    private       int                     numberOfColumns;
    /**
     * Number of rows
     */
    private       int                     numberOfRows;
    /**
     * Popop menu
     */
    private final JPopupMenu              popupMenu;
    /**
     * Constraints to give to {@link #manipulatedComponent} if split or unify action happen
     */
    private       TableLayout.Constraints resultConstraints;
    /**
     * Select color
     */
    private       Color                   selectColor;
    /**
     * Current selected cells
     */
    private final List<Point>             selection;
    /**
     * Indicates if have to show UI for add/remove columns and rows
     */
    private       boolean                 showColumnsRowsManipulation;
    /**
     * Layout to use
     */
    private final TableLayout             tableLayout;
    /**
     * Task for fill "holes" inside table
     */
    private final TaskFillHoles           taskFillHoles;
    /**
     * Creator of cell for fill "holes"
     */
    final         CellComponentCreator    cellComponentCreator;
    /**
     * Area to highlight
     */
    Rectangle highlightArea;

    /**
     * Create empty table
     *
     * @param numberOfColumns Number of columns
     * @param numberOfRows    Number of rows
     */
    public JHelpTable(final int numberOfColumns, final int numberOfRows)
    {
        this(numberOfColumns, numberOfRows, null);
    }

    /**
     * Create a table and fill it on using the given creator
     *
     * @param numberOfColumns      Number of columns
     * @param numberOfRows         Number of rows
     * @param cellComponentCreator Describe how create a component on empty cell. If {@code null} table will be empty at
     *                             start
     */
    public JHelpTable(
            final int numberOfColumns, final int numberOfRows,
            @Nullable
                    CellComponentCreator cellComponentCreator)
    {
        this.tableLayout = new TableLayout();
        this.setLayout(this.tableLayout);
        this.cellComponentCreator = cellComponentCreator;
        this.taskFillHoles = new TaskFillHoles();
        this.numberOfColumns = Math.max(1, numberOfColumns);
        this.numberOfRows = Math.max(1, numberOfRows);
        this.tableLayout.fixStructure(this.numberOfColumns, this.numberOfRows);
        this.canUnifySplitOrMove = true;
        this.lineColor = Color.LIGHT_GRAY;
        this.lineThick = 3;
        this.selectColor = Color.BLUE;
        this.highlightColor = Color.RED;
        this.selection = new ArrayList<>();
        this.actionUnifyOrSplit = new ActionUnifyOrSplit();
        this.actionMoveCell = new ActionMoveCell();
        this.popupMenu = new JPopupMenu();
        this.popupMenu.add(this.actionUnifyOrSplit);
        this.popupMenu.add(this.actionMoveCell);
        this.eventManager = new EventManager();
        this.addMouseListener(this.eventManager);
        this.addMouseMotionListener(this.eventManager);
        this.initialize();
    }

    /**
     * Initialize the table content
     */
    private void initialize()
    {
        if (this.cellComponentCreator == null)
        {
            return;
        }

        for (int y = 0; y < this.numberOfRows; y++)
        {
            for (int x = 0; x < this.numberOfColumns; x++)
            {
                this.addComponent(x, y, this.cellComponentCreator.createComponentForCell(x, y));
            }
        }
    }

    /**
     * Remove frome list of cells, the cells inside given constraints
     *
     * @param points      List of cells
     * @param constraints Contraints for filter
     */
    private void removePointsInsideConstraints(List<Point> points, TableLayout.Constraints constraints)
    {
        for (int i = points.size() - 1; i >= 0; i--)
        {
            if (constraints.contains(points.get(i)))
            {
                points.remove(i);
            }
        }
    }

    /**
     * Compute area selection
     *
     * @return Area selection
     */
    private Rectangle selectedArea()
    {
        int                     minX = Integer.MAX_VALUE;
        int                     minY = Integer.MAX_VALUE;
        int                     maxX = 0;
        int                     maxY = 0;
        int                     nb   = this.getComponentCount();
        Component               component;
        TableLayout.Constraints constraints;

        for (int i = 0; i < nb; i++)
        {
            component = this.getComponent(i);
            constraints = this.tableLayout.obtainConstraints(component);

            if (constraints != null && this.isSelected(constraints))
            {
                minX = Math.min(minX, constraints.x);
                minY = Math.min(minY, constraints.y);
                maxX = Math.max(maxX, constraints.x + constraints.width - 1);
                maxY = Math.max(maxY, constraints.y + constraints.height - 1);
            }
        }

        for (Point point : this.selection)
        {
            minX = Math.min(minX, point.x);
            minY = Math.min(minY, point.y);
            maxX = Math.max(maxX, point.x);
            maxY = Math.max(maxY, point.y);
        }

        if (minX > maxX || minY > maxY)
        {
            return new Rectangle();
        }

        return new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }

    /**
     * Add one cell to selection
     *
     * @param x Cell X
     * @param y Cell Y
     */
    void addSelection(int x, int y)
    {
        this.selection.add(new Point(x, y));
    }

    /**
     * Size of a cell in pixels
     *
     * @return Size of a cell in pixels
     */
    Dimension cellSize()
    {
        return this.tableLayout.getCellSize();
    }

    /**
     * Do action when user click.<br>
     * Here it test if click on special + or - button for add/remove columns/rows
     *
     * @param x Mouse X
     * @param y Mouse Y
     */
    void clickOn(int x, int y)
    {
        if (!this.showColumnsRowsManipulation || (x > 16 && y > 16))
        {
            return;
        }

        Dimension cellSize = this.tableLayout.getCellSize();

        if (x <= 16)
        {
            int line     = y / cellSize.height;
            int relative = y % cellSize.height;

            if (Math.abs((cellSize.height >> 1) - relative) <= 16)
            {
                this.removeRow(line);
                return;
            }

            if (relative >= cellSize.height - 16)
            {
                this.addRow(line + 1);
            }
        }
        else
        {
            assert y <= 16;
            int column   = x / cellSize.width;
            int relative = x % cellSize.width;

            if (Math.abs((cellSize.width >> 1) - relative) <= 16)
            {
                this.removeColumn(column);
                return;
            }

            if (relative >= cellSize.width - 16)
            {
                this.addColumn(column + 1);
            }
        }
    }

    /**
     * Decide if popup is allow to be show
     *
     * @param x X for popup if shown
     * @param y Y for popup if shown
     */
    void decideIfShowPopup(int x, int y)
    {
        int size = this.selection.size();

        if (size == 0 || !this.canUnifySplitOrMove)
        {
            return;
        }

        int nb = this.getComponentCount();

        if (size == 1)
        {
            //Split case
            Component               component;
            TableLayout.Constraints constraints;

            for (int i = 0; i < nb; i++)
            {
                component = this.getComponent(i);
                constraints = this.tableLayout.obtainConstraints(component);

                if (component.isVisible() && constraints != null && this.isSelected(constraints))
                {
                    this.manipulatedComponent = component;
                    this.resultConstraints = new TableLayout.Constraints(constraints.x, constraints.y);
                    this.actionUnifyOrSplit.name(JHelpTable.TEXT_KEY_ACTION_SPLIT);
                    this.actionUnifyOrSplit.setEnabled(constraints.width > 1 || constraints.height > 1);
                    this.actionMoveCell.setEnabled(true);
                    this.popupMenu.show(this, x, y);
                    return;
                }
            }

            return;
        }

        //Unify case
        this.manipulatedComponent = null;
        int                     minX          = Integer.MAX_VALUE;
        int                     minY          = Integer.MAX_VALUE;
        int                     maxX          = 0;
        int                     maxY          = 0;
        Component               component;
        TableLayout.Constraints constraints;
        List<Point>             selectionCopy = new ArrayList<>();
        selectionCopy.addAll(this.selection);
        int selectedIndex = Integer.MAX_VALUE;
        int selectIndex;

        for (int i = 0; i < nb; i++)
        {
            component = this.getComponent(i);
            constraints = this.tableLayout.obtainConstraints(component);

            if (component.isVisible() && constraints != null && this.isSelected(constraints))
            {
                minX = Math.min(minX, constraints.x);
                minY = Math.min(minY, constraints.y);
                maxX = Math.max(maxX, constraints.x + constraints.width);
                maxY = Math.max(maxY, constraints.y + constraints.height);
                this.removePointsInsideConstraints(selectionCopy, constraints);

                if (this.manipulatedComponent == null)
                {
                    this.manipulatedComponent = component;
                    selectedIndex = this.selectIndex(constraints);
                }
                else
                {
                    selectIndex = this.selectIndex(constraints);

                    if (selectIndex < selectedIndex)
                    {
                        this.manipulatedComponent = component;
                        selectedIndex = selectIndex;
                    }
                }
            }
        }

        if (this.manipulatedComponent == null)
        {
            return;
        }

        for (Point point : selectionCopy)
        {
            minX = Math.min(minX, point.x);
            minY = Math.min(minY, point.y);
            maxX = Math.max(maxX, point.x + 1);
            maxY = Math.max(maxY, point.y + 1);
        }

        selectionCopy.clear();
        this.resultConstraints = new TableLayout.Constraints(minX, minY, maxX - minX, maxY - minY);
        this.actionMoveCell.setEnabled(false);
        this.actionUnifyOrSplit.name(JHelpTable.TEXT_KEY_ACTION_UNION);
        this.actionUnifyOrSplit.setEnabled(true);
        this.popupMenu.show(this, x, y);
    }

    /**
     * Start the action of moving a cell
     */
    void doActionMoveCell()
    {
        if (this.manipulatedComponent == null)
        {
            return;
        }

        TableLayout.Constraints constraints = this.tableLayout.obtainConstraints(this.manipulatedComponent);

        if (constraints == null)
        {
            return;
        }

        Dimension cellSize = this.tableLayout.getCellSize();
        this.highlightArea = new Rectangle(constraints.x * cellSize.width, constraints.y * cellSize.height,
                                           constraints.width * cellSize.width, constraints.height * cellSize.height);
        this.selection.clear();
        this.revalidate();
        this.repaint();
    }

    /**
     * Do action for unify or split
     */
    void doActionUnifyOrSplit()
    {
        if (this.manipulatedComponent == null || this.resultConstraints == null)
        {
            return;
        }

        TableLayout.Constraints original = this.tableLayout.obtainConstraints(this.manipulatedComponent);
        this.tableLayout.setContraints(this.manipulatedComponent, this.resultConstraints);
        int                     nb   = this.getComponentCount();
        Component               component;
        TableLayout.Constraints constraints;
        Rectangle               area = this.selectedArea();

        for (int i = nb - 1; i >= 0; i--)
        {
            component = this.getComponent(i);
            constraints = this.tableLayout.obtainConstraints(component);

            if (component != this.manipulatedComponent && constraints != null && constraints.intersects(area))
            {
                this.remove(component);
            }
        }

        this.manipulatedComponent = null;
        this.resultConstraints = null;
        this.selection.clear();
        this.revalidate();
        this.repaint();
    }

    /**
     * Indicates if constraints is selected
     *
     * @param constraints Tested constraints
     * @return {@code true} if constraints is selected
     */
    boolean isSelected(TableLayout.Constraints constraints)
    {
        for (Point selected : this.selection)
        {
            if (constraints.contains(selected))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Indicates if cell is selected
     *
     * @param x Cell X
     * @param y Cell Y
     * @return {@code true} if cell is selected
     */
    boolean isSelected(int x, int y)
    {
        for (Point selected : this.selection)
        {
            if (selected.x == x && selected.y == y)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Compute index of constraints inside selection
     *
     * @param constraints Constriants search
     * @return Index of constraints OR -1 if not found
     */
    int selectIndex(TableLayout.Constraints constraints)
    {
        int index = 0;

        for (Point selected : this.selection)
        {
            if (constraints.contains(selected))
            {
                return index;
            }

            index++;
        }

        return -1;
    }

    /**
     * Select one cell
     *
     * @param x Cell X
     * @param y Cell Y
     */
    void setSelection(int x, int y)
    {
        this.selection.clear();
        this.selection.add(new Point(x, y));
    }

    /**
     * Validate the current move. Finalize move action
     */
    void validateMove()
    {
        if (this.highlightArea == null || this.manipulatedComponent == null)
        {
            return;
        }

        TableLayout.Constraints constraints = this.tableLayout.obtainConstraints(this.manipulatedComponent);

        if (constraints == null)
        {
            return;
        }

        Dimension sizeInCell = this.cellSize();
        Rectangle destination = new Rectangle(this.highlightArea.x / sizeInCell.width,
                                              this.highlightArea.y / sizeInCell.height,
                                              constraints.width, constraints.height);
        int       nb = this.getComponentCount();
        Component component;

        for (int i = nb - 1; i >= 0; i--)
        {
            component = this.getComponent(i);
            constraints = this.tableLayout.obtainConstraints(component);

            if (component != this.manipulatedComponent && constraints != null && constraints.intersects(destination))
            {
                int x  = constraints.x;
                int w  = destination.x - constraints.x;
                int y  = constraints.y;
                int h  = destination.y - constraints.y;
                int ww = w;

                if (w < 0 || (w == 0 && h <= 0))
                {
                    x = destination.x + destination.width;
                    w = constraints.x + constraints.width - x;
                }
                else if (w == 0 && h > 0)
                {
                    w = constraints.width;
                }

                if (h < 0 || (h == 0 && ww <= 0))
                {
                    y = destination.y + destination.height;
                    h = constraints.y + constraints.height - y;
                }
                else if (h == 0 && ww > 0)
                {
                    h = constraints.height;
                }

                if (w > 0 && h > 0)
                {
                    this.tableLayout.setContraints(component, new TableLayout.Constraints(x, y, w, h));
                }
                else
                {
                    this.remove(component);
                }
            }
        }

        this.tableLayout.setContraints(this.manipulatedComponent,
                                       new TableLayout.Constraints(destination.x, destination.y,
                                                                   destination.width, destination.height));
        this.manipulatedComponent = null;
        this.resultConstraints = null;
        this.highlightArea = null;
        this.selection.clear();
        this.revalidate();
        this.repaint();
    }

    /**
     * Alert listeners that a column is deleted.<br>
     * It is called just after deletion.
     *
     * @param column Deleted column
     */
    protected void fireTableColumnDeleted(int column)
    {
        synchronized (this.lock)
        {
            for (ColumnAndRowsModificationListener columnAndRowsModificationListener :
                    this.listenerList.getListeners(ColumnAndRowsModificationListener.class))
            {
                columnAndRowsModificationListener.tableColumnDeleted(this, column);
            }
        }
    }

    /**
     * Alert listeners that a new column inserted.<br>
     * It called just after insertion
     *
     * @param column Inserted column
     */
    protected void fireTableColumnInserted(int column)
    {
        synchronized (this.lock)
        {
            for (ColumnAndRowsModificationListener columnAndRowsModificationListener :
                    this.listenerList.getListeners(ColumnAndRowsModificationListener.class))
            {
                columnAndRowsModificationListener.tableColumnInserted(this, column);
            }
        }
    }

    /**
     * Alert listeners that a column will be delete.<br>
     * It is called just before deletion
     *
     * @param column Column will be delete
     */
    protected void fireTableColumnWillBeDelete(int column)
    {
        synchronized (this.lock)
        {
            for (ColumnAndRowsModificationListener columnAndRowsModificationListener :
                    this.listenerList.getListeners(ColumnAndRowsModificationListener.class))
            {
                columnAndRowsModificationListener.tableColumnWillBeDelete(this, column);
            }
        }
    }

    /**
     * Allert listeners that a new column will be insert.<br>
     * It called just before insertion
     *
     * @param column Column will be insert
     */
    protected void fireTableColumnWillBeInsert(int column)
    {
        synchronized (this.lock)
        {
            for (ColumnAndRowsModificationListener columnAndRowsModificationListener :
                    this.listenerList.getListeners(ColumnAndRowsModificationListener.class))
            {
                columnAndRowsModificationListener.tableColumnWillBeInsert(this, column);
            }
        }
    }

    /**
     * Alert listeners that a row is deleted.<br>
     * It is called just after row deleted
     *
     * @param row Deleted row
     */
    protected void fireTableRowDeleted(int row)
    {
        synchronized (this.lock)
        {
            for (ColumnAndRowsModificationListener columnAndRowsModificationListener :
                    this.listenerList.getListeners(ColumnAndRowsModificationListener.class))
            {
                columnAndRowsModificationListener.tableRowDeleted(this, row);
            }
        }
    }

    /**
     * Alert listeners that a row inserted.<br>
     * It is called just after insertion.
     *
     * @param row Row inserted
     */
    protected void fireTableRowInserted(int row)
    {
        synchronized (this.lock)
        {
            for (ColumnAndRowsModificationListener columnAndRowsModificationListener :
                    this.listenerList.getListeners(ColumnAndRowsModificationListener.class))
            {
                columnAndRowsModificationListener.tableRowInserted(this, row);
            }
        }
    }

    /**
     * Alert listeners that a row will be delete.<br>
     * It is called just before delete row
     *
     * @param row Row will be delete
     */
    protected void fireTableRowWillBeDelete(int row)
    {
        synchronized (this.lock)
        {
            for (ColumnAndRowsModificationListener columnAndRowsModificationListener :
                    this.listenerList.getListeners(ColumnAndRowsModificationListener.class))
            {
                columnAndRowsModificationListener.tableRowWillBeDelete(this, row);
            }
        }
    }

    /**
     * Alert listeners that a row will be insert.<br>
     * It is called just before insertion
     *
     * @param row Row to be insert
     */
    protected void fireTableRowWillBeInsert(int row)
    {
        synchronized (this.lock)
        {
            for (ColumnAndRowsModificationListener columnAndRowsModificationListener :
                    this.listenerList.getListeners(ColumnAndRowsModificationListener.class))
            {
                columnAndRowsModificationListener.tableRowWillBeInsert(this, row);
            }
        }
    }

    /**
     * Paint component children
     *
     * @param graphics Graphics to use for draw
     */
    @Override
    protected void paintChildren(Graphics graphics)
    {
        super.paintChildren(graphics);

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setColor(this.lineColor);
        graphics2D.setStroke(new BasicStroke(this.lineThick, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        boolean[]               cells    = new boolean[this.numberOfColumns * this.numberOfRows];
        int                     nb       = this.getComponentCount();
        Point                   minimums = this.tableLayout.getMinimums();
        Component               component;
        TableLayout.Constraints constraints;

        for (int i = 0; i < nb; i++)
        {
            component = this.getComponent(i);
            constraints = this.tableLayout.obtainConstraints(component);

            if (component.isVisible() && constraints != null)
            {
                graphics2D.drawRect(component.getX(), component.getY(), component.getWidth(), component.getHeight());

                for (int yy = 0; yy < constraints.height; yy++)
                {
                    for (int xx = 0; xx < constraints.width; xx++)
                    {
                        cells[xx + constraints.x - minimums.x + (yy + constraints.y - minimums.y) *
                                                                this.numberOfColumns] =
                                true;
                    }
                }
            }
        }

        List<Point> holes    = new ArrayList<>();
        int         index    = 0;
        Dimension   cellSize = this.tableLayout.getCellSize();
        Insets      margins  = this.tableLayout.getMargins();

        for (int yy = 0; yy < this.numberOfRows; yy++)
        {
            for (int xx = 0; xx < this.numberOfColumns; xx++)
            {
                if (!cells[index])
                {
                    holes.add(new Point(xx, yy));
                    graphics2D.drawRect(xx * cellSize.width + margins.left,
                                        yy * cellSize.height + margins.top,
                                        cellSize.width - margins.left - margins.right,
                                        cellSize.height - margins.top - margins.bottom);
                }

                index++;
            }
        }

        graphics2D.setColor(this.selectColor);
        graphics2D.setStroke(new BasicStroke(this.lineThick + 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for (int i = 0; i < nb; i++)
        {
            component = this.getComponent(i);
            constraints = this.tableLayout.obtainConstraints(component);

            if (component.isVisible() && constraints != null && this.isSelected(constraints))
            {
                graphics2D.drawRect(component.getX(), component.getY(), component.getWidth(), component.getHeight());
            }
        }

        index = 0;

        for (int yy = 0; yy < this.numberOfRows; yy++)
        {
            for (int xx = 0; xx < this.numberOfColumns; xx++)
            {
                if (!cells[index] && this.isSelected(xx, yy))
                {
                    graphics2D.drawRect(xx * cellSize.width + margins.left,
                                        yy * cellSize.height + margins.top,
                                        cellSize.width - margins.left - margins.right,
                                        cellSize.height - margins.top - margins.bottom);
                }

                index++;
            }
        }

        if (this.highlightArea != null)
        {
            graphics2D.setColor(this.highlightColor);
            graphics2D.drawRect(this.highlightArea.x, this.highlightArea.y,
                                this.highlightArea.width, this.highlightArea.height);
        }

        if (this.showColumnsRowsManipulation)
        {
            int step   = cellSize.width;
            int midle  = cellSize.width >> 1;
            int border = cellSize.width - 16;

            for (int column = 0, x = 0; column < this.numberOfColumns; column++, x += step)
            {
                graphics2D.setColor(Color.RED);
                graphics2D.drawLine(x + midle - 8, 8, x + midle + 8, 8);
                graphics2D.setColor(Color.BLUE);
                graphics2D.drawLine(x + border, 8, x + border + 16, 8);
                graphics2D.drawLine(x + border + 8, 0, x + border + 8, 16);
            }

            step = cellSize.height;
            midle = cellSize.height >> 1;
            border = cellSize.height - 16;

            for (int row = 0, y = 0; row < this.numberOfRows; row++, y += step)
            {
                graphics2D.setColor(Color.RED);
                graphics2D.drawLine(0, y + midle, 16, y + midle);
                graphics2D.setColor(Color.BLUE);
                graphics2D.drawLine(0, y + border + 8, 16, y + border + 8);
                graphics2D.drawLine(8, y + border, 8, y + border + 16);
            }
        }

        if (this.cellComponentCreator != null && holes.size() > 0)
        {
            ThreadManager.parallel(this.taskFillHoles, holes, 128);
        }
    }

    /**
     * Add a column
     *
     * @param index Column index where insert new column
     */
    public void addColumn(int index)
    {
        this.fireTableColumnWillBeInsert(Math2.limit(index, 0, this.numberOfColumns));
        Component               component;
        TableLayout.Constraints constraints;
        int                     nb = this.getComponentCount();

        for (int i = 0; i < nb; i++)
        {
            component = this.getComponent(i);
            constraints = this.tableLayout.obtainConstraints(component);

            if (constraints != null && constraints.x >= index)
            {
                this.tableLayout.setContraints(component, new TableLayout.Constraints(constraints.x + 1,
                                                                                      constraints.y,
                                                                                      constraints.width,
                                                                                      constraints.height));
            }
        }

        this.numberOfColumns++;
        this.tableLayout.fixStructure(this.numberOfColumns, this.numberOfRows);
        this.revalidate();
        this.repaint();
        this.fireTableColumnInserted(Math2.limit(index, 0, this.numberOfColumns));
    }

    /**
     * Add a component a given position
     *
     * @param x         X
     * @param y         Y
     * @param component Component to add
     */
    public void addComponent(int x, int y, JComponent component)
    {
        this.addComponent(x, y, 1, 1, component);
    }

    /**
     * Add component
     *
     * @param x         X
     * @param y         Y
     * @param width     Number of cell in width
     * @param height    Number of cell in height
     * @param component Component to add
     */
    public void addComponent(int x, int y, int width, int height, JComponent component)
    {
        if (component == null)
        {
            return;
        }

        x = Math2.limit(x, 0, this.numberOfColumns - 1);
        y = Math2.limit(y, 0, this.numberOfRows - 1);
        width = Math.max(1, width);
        height = Math.max(1, height);

        if (x + width > this.numberOfColumns)
        {
            width = this.numberOfColumns - x;
        }

        if (y + height > this.numberOfRows)
        {
            height = this.numberOfRows - y;
        }

        component.addMouseListener(this.eventManager);
        component.addMouseMotionListener(this.eventManager);
        this.add(component, new TableLayout.Constraints(x, y, width, height));
        this.revalidate();
        this.repaint();
    }

    /**
     * Add a row
     *
     * @param index Index to insert new row
     */
    public void addRow(int index)
    {
        this.fireTableRowWillBeInsert(Math2.limit(index, 0, this.numberOfRows));
        Component               component;
        TableLayout.Constraints constraints;
        int                     nb = this.getComponentCount();

        for (int i = 0; i < nb; i++)
        {
            component = this.getComponent(i);
            constraints = this.tableLayout.obtainConstraints(component);

            if (constraints != null && constraints.y >= index)
            {
                this.tableLayout.setContraints(component, new TableLayout.Constraints(constraints.x,
                                                                                      constraints.y + 1,
                                                                                      constraints.width,
                                                                                      constraints.height));
            }
        }

        this.numberOfRows++;
        this.tableLayout.fixStructure(this.numberOfColumns, this.numberOfRows);
        this.revalidate();
        this.repaint();
        this.fireTableRowInserted(Math2.limit(index, 0, this.numberOfRows));
    }

    /**
     * Obtain highlightColor value
     *
     * @return highlightColor value
     */
    public Color getHighlightColor()
    {
        return this.highlightColor;
    }

    /**
     * Modify highlightColor value
     *
     * @param highlightColor New highlightColor value
     */
    public void setHighlightColor(Color highlightColor)
    {
        if (highlightColor == null)
        {
            throw new NullPointerException("highlightColor MUST NOT be null !");
        }

        this.highlightColor = highlightColor;
    }

    /**
     * Obtain lineColor value
     *
     * @return lineColor value
     */
    public Color getLineColor()
    {
        return this.lineColor;
    }

    /**
     * Modify lineColor value
     *
     * @param lineColor New lineColor value
     */
    public void setLineColor(Color lineColor)
    {
        if (lineColor == null)
        {
            throw new NullPointerException("lineColor MUST NOT be null !");
        }

        this.lineColor = lineColor;
        this.revalidate();
        this.repaint();
    }

    /**
     * Obtain lineThick value
     *
     * @return lineThick value
     */
    public int getLineThick()
    {
        return this.lineThick;
    }

    /**
     * Modify lineThick value
     *
     * @param lineThick New lineThick value
     */
    public void setLineThick(int lineThick)
    {
        this.lineThick = Math.max(1, lineThick);
        this.revalidate();
        this.repaint();
    }

    /**
     * Obtain numberOfColumns value
     *
     * @return numberOfColumns value
     */
    public int getNumberOfColumns()
    {
        return this.numberOfColumns;
    }

    /**
     * Obtain numberOfRows value
     *
     * @return numberOfRows value
     */
    public int getNumberOfRows()
    {
        return this.numberOfRows;
    }

    /**
     * Obtain selectColor value
     *
     * @return selectColor value
     */
    public Color getSelectColor()
    {
        return this.selectColor;
    }

    /**
     * Modify selectColor value
     *
     * @param selectColor New selectColor value
     */
    public void setSelectColor(Color selectColor)
    {
        if (selectColor == null)
        {
            throw new NullPointerException("selectColor MUST NOT be null !");
        }

        this.selectColor = selectColor;
        this.revalidate();
        this.repaint();
    }

    /**
     * Indicates if split, unify and move are allowed
     *
     * @return {@code true} if split, unify and move are allowed
     */
    public boolean isCanUnifySplitOrMove()
    {
        return this.canUnifySplitOrMove;
    }

    /**
     * Enable/disable split, unify and move
     *
     * @param canUnifySplitOrMove {@code true} if split, unify and move are allowed
     */
    public void setCanUnifySplitOrMove(boolean canUnifySplitOrMove)
    {
        this.canUnifySplitOrMove = canUnifySplitOrMove;
    }

    /**
     * Indicates if the special buttons for add/remove columns/rows are shown
     *
     * @return {@code true} if the special buttons for add/remove columns/rows are shown
     */
    public boolean isShowColumnsRowsManipulation()
    {
        return this.showColumnsRowsManipulation;
    }

    /**
     * Show/hide the special buttons for add/remove columns/rows
     *
     * @param showColumnsRowsManipulation {@code true} to show the special buttons for add/remove columns/rows
     */
    public void setShowColumnsRowsManipulation(boolean showColumnsRowsManipulation)
    {
        if (this.showColumnsRowsManipulation == showColumnsRowsManipulation)
        {
            return;
        }

        this.showColumnsRowsManipulation = showColumnsRowsManipulation;

        if (this.showColumnsRowsManipulation)
        {
            this.tableLayout.setMargins(16, 16, 16, 16);
        }
        else
        {
            this.tableLayout.setMargins(0, 0, 0, 0);
        }

        this.revalidate();
        this.repaint();
    }

    /**
     * Register listener of add/remove columns/rows
     *
     * @param columnAndRowsModificationListener Listener to register
     */
    public void registerColumnAndRowsModificationListener(
            ColumnAndRowsModificationListener columnAndRowsModificationListener)
    {
        if (columnAndRowsModificationListener == null)
        {
            return;
        }

        synchronized (this.lock)
        {
            this.listenerList.add(ColumnAndRowsModificationListener.class, columnAndRowsModificationListener);
        }
    }

    /**
     * Remove a component
     *
     * @param index Component index
     */
    @Override
    public void remove(int index)
    {
        Component component = this.getComponent(index);
        component.removeMouseListener(this.eventManager);
        component.removeMouseMotionListener(this.eventManager);
        super.remove(index);
    }

    /**
     * Remove a component
     *
     * @param component Component to remove
     */
    @Override
    public void remove(Component component)
    {
        component.removeMouseListener(this.eventManager);
        component.removeMouseMotionListener(this.eventManager);
        super.remove(component);
    }

    /**
     * Remove all components
     */
    public void removeAllComponents()
    {
        for (int i = this.getComponentCount() - 1; i >= 0; i--)
        {
            this.remove(i);
        }
    }

    /**
     * Remove a column
     *
     * @param column Column to remove
     */
    public void removeColumn(int column)
    {
        if (this.numberOfColumns <= 1 || column < 0 || column >= this.numberOfColumns)
        {
            return;
        }

        this.fireTableColumnWillBeDelete(column);
        Component               component;
        TableLayout.Constraints constraints;
        int                     nb = this.getComponentCount();

        for (int i = nb - 1; i >= 0; i--)
        {
            component = this.getComponent(i);
            constraints = this.tableLayout.obtainConstraints(component);

            if (constraints != null)
            {
                if (column >= constraints.x && column < constraints.x + constraints.width)
                {
                    if (constraints.width <= 1)
                    {
                        this.remove(component);
                    }
                    else
                    {
                        this.tableLayout.setContraints(component, new TableLayout.Constraints(constraints.x,
                                                                                              constraints.y,
                                                                                              constraints.width - 1,
                                                                                              constraints.height));
                    }
                }
                else if (constraints.x > column)
                {
                    this.tableLayout.setContraints(component, new TableLayout.Constraints(constraints.x - 1,
                                                                                          constraints.y,
                                                                                          constraints.width,
                                                                                          constraints.height));
                }
            }
        }

        this.numberOfColumns--;
        this.tableLayout.fixStructure(this.numberOfColumns, this.numberOfRows);
        this.revalidate();
        this.repaint();
        this.fireTableColumnDeleted(column);
    }

    /**
     * Remove a row
     *
     * @param row Row to remove
     */
    public void removeRow(int row)
    {
        if (this.numberOfRows <= 1 || row < 0 || row >= this.numberOfRows)
        {
            return;
        }

        this.fireTableRowWillBeDelete(row);
        Component               component;
        TableLayout.Constraints constraints;
        int                     nb = this.getComponentCount();

        for (int i = nb - 1; i >= 0; i--)
        {
            component = this.getComponent(i);
            constraints = this.tableLayout.obtainConstraints(component);

            if (constraints != null)
            {
                if (row >= constraints.y && row < constraints.y + constraints.height)
                {
                    if (constraints.height <= 1)
                    {
                        this.remove(component);
                    }
                    else
                    {
                        this.tableLayout.setContraints(component, new TableLayout.Constraints(constraints.x,
                                                                                              constraints.y,
                                                                                              constraints.width,
                                                                                              constraints.height - 1));
                    }
                }
                else if (constraints.y > row)
                {
                    this.tableLayout.setContraints(component, new TableLayout.Constraints(constraints.x,
                                                                                          constraints.y - 1,
                                                                                          constraints.width,
                                                                                          constraints.height));
                }
            }
        }

        this.numberOfRows--;
        this.tableLayout.fixStructure(this.numberOfColumns, this.numberOfRows);
        this.revalidate();
        this.repaint();
        this.fireTableRowDeleted(row);
    }

    /**
     * Unregister listener of add/remove columns/rows
     *
     * @param columnAndRowsModificationListener Listener to unregister
     */
    public void unregisterColumnAndRowsModificationListener(
            ColumnAndRowsModificationListener columnAndRowsModificationListener)
    {
        synchronized (this.lock)
        {
            this.listenerList.remove(ColumnAndRowsModificationListener.class, columnAndRowsModificationListener);
        }
    }

    /**
     * Update a component
     *
     * @param component Component to update
     * @param x         New X
     * @param y         New Y
     * @param width     New number of cell in width
     * @param height    New number of cell in height
     */
    public void updateComponent(Component component, int x, int y, int width, int height)
    {
        TableLayout.Constraints constraints = this.tableLayout.obtainConstraints(component);

        if (constraints == null)
        {
            return;
        }

        this.tableLayout.setContraints(component, new TableLayout.Constraints(x, y, width, height));
        this.revalidate();
        this.repaint();
    }

    /**
     * Automatic creator of components.<br>
     * Used to initiliaze the table and fill holes
     */
    public interface CellComponentCreator
    {
        /**
         * Create component for given cell
         *
         * @param x Cell X
         * @param y Cell Y
         * @return Created component
         */
        JComponent createComponentForCell(int x, int y);
    }

    /**
     * Listener of column/rows insertion/deletion
     */
    public interface ColumnAndRowsModificationListener extends EventListener
    {
        /**
         * Called when a column is deleted.<br>
         * It is called ust after deletion
         *
         * @param table  Modified table
         * @param column Deleted column
         */
        void tableColumnDeleted(JHelpTable table, int column);

        /**
         * Called when a column is inserted.<br>
         * It is called ust after insertion
         *
         * @param table  Modified table
         * @param column Inserted column
         */
        void tableColumnInserted(JHelpTable table, int column);

        /**
         * Called when a column is about to be delete.<br>
         * It is called just before deletion
         *
         * @param table  Table will be modified
         * @param column Column will be delete
         */
        void tableColumnWillBeDelete(JHelpTable table, int column);

        /**
         * Called when a column is about to be insert.<br>
         * It is called just before insertion
         *
         * @param table  Table will be modified
         * @param column Column will be insert
         */
        void tableColumnWillBeInsert(JHelpTable table, int column);

        /**
         * Called when a row is deleted.<br>
         * It is called ust after deletion
         *
         * @param table Modified table
         * @param row   Deleted row
         */
        void tableRowDeleted(JHelpTable table, int row);

        /**
         * Called when a row is inserted.<br>
         * It is called ust after insertion
         *
         * @param table Modified table
         * @param row   Inserted row
         */
        void tableRowInserted(JHelpTable table, int row);

        /**
         * Called when a row is about to be delete.<br>
         * It is called just before deletion
         *
         * @param table Table will be modified
         * @param row   Row will be delete
         */
        void tableRowWillBeDelete(JHelpTable table, int row);

        /**
         * Called when a row is about to be insert.<br>
         * It is called just before insertion
         *
         * @param table Table will be modified
         * @param row   Row will be insert
         */
        void tableRowWillBeInsert(JHelpTable table, int row);
    }
}

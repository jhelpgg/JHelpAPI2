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
package jhelp.gui.samples.game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Map;
import jhelp.gui.game.ActionKey;
import jhelp.gui.game.EventManager;
import jhelp.gui.game.JHelpGame2D;
import jhelp.gui.game.keymapper.KeyMapper;
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;
import jhelp.util.math.JHelpRandom;
import jhelp.util.math.Math2;
import jhelp.util.text.UtilText;

/**
 * The snake frame.<br>
 * By default arrow for control Snake, E for exit, W for menu and change key mapping
 *
 * @author JHelp
 */
public class SnakeFrame
        extends JHelpGame2D
        implements EventManager
{
    /**
     * Number of cell in height
     */
    private static final int       BOARD_HEIGHT        = 64;
    /**
     * Number of cell in width
     */
    private static final int       BOARD_WIDTH         = 64;
    /**
     * Game background color
     */
    private static final int       COLOR_BACKGROUND    = 0xFF0000FF;
    /**
     * Color of snake food
     */
    private static final int       COLOR_FOOD          = 0xFFFF0000;
    /**
     * Color for draw points
     */
    private static final int       COLOR_POINTS        = 0xFFFFFF00;
    /**
     * Color of walls border
     */
    private static final int       COLOR_WALL_BORDER   = 0xFFFFFFFF;
    /**
     * Caolor of walls center
     */
    private static final int       COLOR_WALL_INSIDE   = 0xFF000000;
    /**
     * Indicates an empty tile
     */
    private static final int       EMPTY               = -1;
    /**
     * Font used for draw points
     */
    private static final JHelpFont FONT_POINTS         = new JHelpFont("Arial", 24, true);
    /**
     * Points gain if snake eat food
     */
    private static final int       POINTS_EAT          = 128;
    /**
     * Points gain if level up
     */
    private static final int       POINTS_UP_LEVEL     = 1024;
    /**
     * Size of snake need for level up
     */
    private static final int       SIZE_FOR_NEXT_LEVEL = 100;
    /**
     * Size gain each time snake eat
     */
    private static final int       SIZE_GROWTH_PER_EAT = 10;
    /**
     * Indicates a wall in tile
     */
    private static final int       WALL                = -2;
    /**
     * Game titles
     */
    private final int[]            board;
    /**
     * Food X location
     */
    private       int              foodX;
    /**
     * Food Y location
     */
    private       int              foodY;
    /**
     * For change key mapping. Appear when press menu (W by default)
     */
    private final KeyMapper        keyMapper;
    /**
     * Actual level
     */
    private       int              level;
    private       int              max;
    /**
     * Actual points
     */
    private       int              points;
    /**
     * The snake
     */
    private final ArrayList<Point> snake;
    /**
     * Way snake go in X
     */
    private       int              wayX;
    /**
     * Way snake go in Y
     */
    private       int              wayY;

    /**
     * Create a new instance of SnakeFrame
     */
    public SnakeFrame()
    {
        // Use parent construction
        super("Snake");

        // Create/initialize game stuffs
        this.board = new int[SnakeFrame.BOARD_WIDTH * SnakeFrame.BOARD_HEIGHT];
        this.snake = new ArrayList<Point>();
        this.level = 0;
        this.points = 0;
        this.keyMapper = new KeyMapper();

        // Remove window border (To look like full screen)
        this.setUndecorated(true);

        // Defines the action key manager
        this.eventManager(this);
    }

    /**
     * Initialize the snake board game.<br>
     * Called at first initialization, when level up or when loose
     */
    private void initBoard()
    {
        // Put it empty
        for (int position = this.board.length - 1; position >= 0; position--)
        {
            this.board[position] = SnakeFrame.EMPTY;
        }

        // Add walls
        for (int l = 0; l < this.level; l++)
        {
            this.board[JHelpRandom.random(this.board.length)] = SnakeFrame.WALL;
        }

        // Locate the snake
        int position = JHelpRandom.random(this.board.length);
        while (this.board[position] != SnakeFrame.EMPTY)
        {
            position = JHelpRandom.random(this.board.length);
        }

        final int x = position % SnakeFrame.BOARD_WIDTH;
        final int y = position / SnakeFrame.BOARD_WIDTH;
        this.snake.clear();

        for (int i = 0; i < 5; i++)
        {
            this.snake.add(new Point(x, y));
        }

        // Initialize food and snake ways (Snkae not move until next direction key)
        this.foodX = this.foodY = -1;
        this.wayX = this.wayY = 0;
    }

    /**
     * Restart game at zero.<br>
     * Called when loose
     */
    private void restartFromZero()
    {
        this.level = 0;
        this.points = 25 + 8;
        this.max = 0;
        this.initBoard();
    }

    /**
     * Level up<br>
     * Called when snake enough long to change level
     */
    private void upLevel()
    {
        this.level += 8;
        this.initBoard();
    }

    /**
     * Create game sprites <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see jhelp.gui.game.JHelpGame2D#createGameSrpties()
     */
    @Override
    protected void createGameSrpties()
    {
    }

    /**
     * Called at game initialization <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param image Image where draw (Already in draw mode)
     * @see jhelp.gui.game.JHelpGame2D#gameInitialize(jhelp.util.gui.JHelpImage)
     */
    @Override
    protected void gameInitialize(final JHelpImage image)
    {
        this.restartFromZero();
        this.initBoard();

        // Change game FPS to not be too fast
        this.fps(10);
    }

    /**
     * Called each time game refresh.<br>
     * Here we draw the new situation <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param image Image where draw already in draw mode
     * @see jhelp.gui.game.JHelpGame2D#gameRefresh(jhelp.util.gui.JHelpImage)
     */
    @Override
    protected void gameRefresh(final JHelpImage image)
    {
        // Image and tiles size
        final int width  = image.getWidth();
        final int height = image.getHeight();
        final int w      = width / SnakeFrame.BOARD_WIDTH;
        final int h      = height / SnakeFrame.BOARD_HEIGHT;

        // Clear the image
        image.clear(SnakeFrame.COLOR_BACKGROUND);

        // Draw tiles
        int tile;
        int x, y;

        for (int pos = this.board.length - 1; pos >= 0; pos--)
        {
            tile = this.board[pos];

            if (tile != SnakeFrame.EMPTY)
            {
                x = pos % SnakeFrame.BOARD_WIDTH;
                y = pos / SnakeFrame.BOARD_WIDTH;

                switch (tile)
                {
                    case SnakeFrame.WALL:
                        image.fillRectangle(x * w, y * h, w, h, SnakeFrame.COLOR_WALL_BORDER);
                        image.fillRectangle((x * w) + (w / 10), (y * h) + (h / 10), w - (w / 5), h - (h / 5),
                                            SnakeFrame.COLOR_WALL_INSIDE);
                        break;
                }
            }
        }

        // Draw the snake
        int       start = 255;
        final int size  = this.snake.size();
        final int step = size <= 1
                         ? 255
                         : 255 / (size - 1);
        int color;
        int xxx = Integer.MAX_VALUE;
        int yyy = Integer.MAX_VALUE;

        for (final Point point : this.snake)
        {
            color = 0xFF000000 | (start << 16) | (start << 8) | start;

            if ((xxx != point.x) || (yyy != point.y))
            {
                image.fillEllipse(point.x * w, point.y * h, w, h, color);
            }

            xxx = point.x;
            yyy = point.y;
            start -= step;
        }

        // Locate and draw the food
        while ((this.foodX < 0) || (this.foodY < 0))
        {
            int pos = JHelpRandom.random(this.board.length);

            while (this.board[pos] != SnakeFrame.EMPTY)
            {
                pos = JHelpRandom.random(this.board.length);
            }

            this.foodX = pos % SnakeFrame.BOARD_WIDTH;
            this.foodY = pos / SnakeFrame.BOARD_WIDTH;

            for (final Point point : this.snake)
            {
                if ((point.x == this.foodX) && (point.y == this.foodY))
                {
                    this.foodX = this.foodY = -1;

                    break;
                }
            }
        }

        int xx = this.foodX * w;
        int yy = this.foodY * h;
        image.fillPolygon(new int[]
                                  {
                                          xx + (w >> 1), xx, xx + w
                                  }, new int[]
                                  {
                                          yy, yy + h, yy + h
                                  }, SnakeFrame.COLOR_FOOD);

        this.max = Math.max(this.max, this.points);
        image.fillString(5, 5, //
                         UtilText.concatenate((this.snake.size() * 100) / (SnakeFrame.SIZE_FOR_NEXT_LEVEL + this.level),
                                              "% : LIFE=", this.points, " SCORE=", this.max),
                         SnakeFrame.FONT_POINTS, SnakeFrame.COLOR_POINTS);

        // Animate the snake
        if ((this.wayX == 0) && (this.wayY == 0))
        {
            return;
        }

        final Point position = this.snake.get(0);
        xx = Math2.modulo(position.x + this.wayX, SnakeFrame.BOARD_WIDTH);
        yy = Math2.modulo(position.y + this.wayY, SnakeFrame.BOARD_HEIGHT);

        // If eat food, grow or level up
        if ((xx == this.foodX) && (yy == this.foodY))
        {
            this.points += SnakeFrame.POINTS_EAT;

            this.foodX = this.foodY = -1;

            this.snake.add(0, new Point(xx, yy));

            final Point pt = this.snake.get(this.snake.size() - 1);

            for (int i = 1; i < SnakeFrame.SIZE_GROWTH_PER_EAT; i++)
            {
                this.snake.add(new Point(pt));
            }

            if (this.snake.size() >= (SnakeFrame.SIZE_FOR_NEXT_LEVEL + this.level))
            {
                this.points += SnakeFrame.POINTS_UP_LEVEL;

                this.upLevel();
            }

            return;
        }

        this.points = Math.max(this.points - (int) Math.sqrt(this.snake.size()), 0);

        if (this.points == 0)
        {
            if (this.snake.size() > 1)
            {
                this.snake.remove(this.snake.size() - 1);
                this.points = 16 + (2 * this.snake.size());
                this.max--;
            }
            else
            {
                this.restartFromZero();
                return;
            }
        }

        // If hurt wall, loose
        if (this.board[position.x + (position.y * SnakeFrame.BOARD_WIDTH)] == SnakeFrame.WALL)
        {
            this.restartFromZero();

            return;
        }

        // If bit itself, loose
        for (final Point point : this.snake)
        {
            if ((point.x == xx) && (point.y == yy))
            {
                this.restartFromZero();

                return;
            }
        }

        // Just move the snake
        this.snake.add(0, new Point(xx, yy));
        this.snake.remove(this.snake.size() - 1);
    }

    /**
     * Called to do action on action key <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param actionsStates Action key current states (Several key may be down in same time)
     * @param mouseX        Mouse X position
     * @param mouseY        Mouse Y position
     * @param buttonLeft    Indicates if mouse left button is down
     * @param buttonMiddle  Indicates if mouse middle button is down
     * @param buttonRight   Indicates if mouse right button is down
     * @return {@code true} beacause whe consume keys
     * @see jhelp.gui.game.EventManager#actionState(java.util.Map, int, int, boolean, boolean, boolean)
     */
    @Override
    public boolean actionState(
            final Map<ActionKey, Boolean> actionsStates, final int mouseX, final int mouseY, final boolean buttonLeft,
            final boolean buttonMiddle, final boolean buttonRight)
    {
        // Exit action => Exit game
        if (actionsStates.get(ActionKey.ACTION_EXIT))
        {
            this.closeFrame();
        }

        // Menu action => Show the change of key association (Changes are autmatically register in preferences)
        if (actionsStates.get(ActionKey.ACTION_MENU))
        {
            this.keyMapper.showInJHelpGame2D(this);
        }

        // /Arrows change snake direction
        if ((actionsStates.get(ActionKey.ACTION_LEFT)) && (this.wayX == 0))
        {
            this.wayX = -1;
            this.wayY = 0;
        }
        else if ((actionsStates.get(ActionKey.ACTION_RIGHT)) && (this.wayX == 0))
        {
            this.wayX = 1;
            this.wayY = 0;
        }
        else if ((actionsStates.get(ActionKey.ACTION_UP)) && (this.wayY == 0))
        {
            this.wayY = -1;
            this.wayX = 0;
        }
        else if ((actionsStates.get(ActionKey.ACTION_DOWN)) && (this.wayY == 0))
        {
            this.wayY = 1;
            this.wayX = 0;
        }

        return true;
    }
}
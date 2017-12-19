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

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import jhelp.gui.JHelpFrame;
import jhelp.gui.LabelJHelpImage;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpSprite;
import jhelp.util.gui.UtilGUI;
import jhelp.util.gui.dynamic.DynamicAnimation;
import jhelp.util.gui.dynamic.JHelpDynamicImage;
import jhelp.util.io.UtilIO;
import jhelp.util.preference.Preferences;
import jhelp.util.resources.Resources;
import jhelp.util.text.UtilText;
import jhelp.util.thread.Mutex;
import jhelp.util.thread.RunnableTask;
import jhelp.util.thread.ThreadManager;

/**
 * Frame that carry "dynamic game"
 *
 * @author JHelp
 */
public class JHelpGameDynamic
        extends JHelpFrame
{
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -229625270965052131L;

    /**
     * Embed listener of mouse and keys
     *
     * @author JHelp
     */
    class MouseKeyListener
            implements KeyListener, MouseListener, MouseMotionListener
    {
        /**
         * Create a new instance of MouseKeyListener
         */
        MouseKeyListener()
        {
        }

        /**
         * Change mouse state
         *
         * @param mouseEvent Mouse event description
         */
        private void mouseState(final MouseEvent mouseEvent)
        {
            JHelpGameDynamic.this.mouseX = mouseEvent.getX();
            JHelpGameDynamic.this.mouseY = mouseEvent.getY();

            final int modifiers = mouseEvent.getModifiersEx();

            JHelpGameDynamic.this.buttonLeft =
                    (modifiers & InputEvent.BUTTON1_DOWN_MASK) == InputEvent.BUTTON1_DOWN_MASK;
            JHelpGameDynamic.this.buttonMiddle =
                    (modifiers & InputEvent.BUTTON2_DOWN_MASK) == InputEvent.BUTTON2_DOWN_MASK;
            JHelpGameDynamic.this.buttonRight =
                    (modifiers & InputEvent.BUTTON3_DOWN_MASK) == InputEvent.BUTTON3_DOWN_MASK;
        }

        /**
         * Called when key typed <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param event Event description
         * @see KeyListener#keyTyped(KeyEvent)
         */
        @Override
        public void keyTyped(final KeyEvent event)
        {
        }

        /**
         * Called when key pressed <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param event Event description
         * @see KeyListener#keyPressed(KeyEvent)
         */
        @Override
        public void keyPressed(final KeyEvent event)
        {
            if (JHelpGameDynamic.this.captureKeyCodeListener != null)
            {
                JHelpGameDynamic.this.captureKeyCodeListener.capturedkeyCode(event.getKeyCode());

                return;
            }

            JHelpGameDynamic.this.changeKeyState(event.getKeyCode(), true);
        }

        /**
         * Called when key released <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param event Event description
         * @see KeyListener#keyReleased(KeyEvent)
         */
        @Override
        public void keyReleased(final KeyEvent event)
        {
            if ((JHelpGameDynamic.this.captureKeyCodeListener != null) && (JHelpGameDynamic.this.captureKeyCodeListener
                    .canLooseFocus()))
            {
                JHelpGameDynamic.this.captureKeyCodeListener = null;

                return;
            }

            JHelpGameDynamic.this.changeKeyState(event.getKeyCode(), false);
        }

        /**
         * Called when mouse button clicked <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param event Event description
         * @see MouseListener#mouseClicked(MouseEvent)
         */
        @Override
        public void mouseClicked(final MouseEvent event)
        {
            this.mouseState(event);
        }

        /**
         * Called when mouse button pressed <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param event Event description
         * @see MouseListener#mousePressed(MouseEvent)
         */
        @Override
        public void mousePressed(final MouseEvent event)
        {
            this.mouseState(event);
        }

        /**
         * Called when mouse button released <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param event Event description
         * @see MouseListener#mouseReleased(MouseEvent)
         */
        @Override
        public void mouseReleased(final MouseEvent event)
        {
            this.mouseState(event);
        }

        /**
         * Called when mouse entered <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param event Event description
         * @see MouseListener#mouseEntered(MouseEvent)
         */
        @Override
        public void mouseEntered(final MouseEvent event)
        {
            this.mouseState(event);
        }

        /**
         * Called when mouse exited <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param event Event description
         * @see MouseListener#mouseExited(MouseEvent)
         */
        @Override
        public void mouseExited(final MouseEvent event)
        {
            this.mouseState(event);
        }

        /**
         * Called when mouse dragged <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param event Event description
         * @see MouseMotionListener#mouseDragged(MouseEvent)
         */
        @Override
        public void mouseDragged(final MouseEvent event)
        {
            this.mouseState(event);
        }

        /**
         * Called when mouse moved <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param event Event description
         * @see MouseMotionListener#mouseMoved(MouseEvent)
         */
        @Override
        public void mouseMoved(final MouseEvent event)
        {
            this.mouseState(event);
        }
    }

    /**
     * Actual action key state
     */
    private final HashMap<ActionKey, Boolean>           actionStates;
    /**
     * Base name for external directory
     */
    private       String                                baseName;
    /**
     * Dynamic image shown
     */
    private       JHelpDynamicImage                     dynamicImage;
    /**
     * Event manager to react to actual action key state, mouse position and mouse buttons state
     */
    private       EventManager                          eventManager;
    /**
     * Association of key code and action key
     */
    private final HashMap<Integer, ActionKey>           keyAssociations;
    /**
     * Component that carry the image
     */
    private       LabelJHelpImage                       labelJHelpImage;
    /**
     * Mouse and key listener
     */
    private       MouseKeyListener                      mouseKeyListener;
    /**
     * Listeners of mouse sensitive areas
     */
    private final ArrayList<MouseSensitiveAreaListener> mouseSensitiveAreaListeners;
    /**
     * Mouse sensitive areas
     */
    private final ArrayList<MouseSensitiveArea>         mouseSensitiveAreas;
    /**
     * Synchronization mutex
     */
    private final Mutex                                 mutex;
    /**
     * Area under the mouse
     */
    private       MouseSensitiveArea                    overArea;
    /**
     * Associated preferences
     */
    private final Preferences                           preferences;
    /**
     * Recommended external directory where found external resources
     */
    private       File                                  recommendedExternalDirectory;
    /**
     * Resources access link
     */
    private final Resources                             resources;
    /**
     * Sprite that shows areas
     */
    private       JHelpSprite                           spriteShowAreas;
    /**
     * Indicates if mouse button left is actually down
     */
    boolean                buttonLeft;
    /**
     * Indicates if mouse button middle is actually down
     */
    boolean                buttonMiddle;
    /**
     * Indicates if mouse button right is actually down
     */
    boolean                buttonRight;
    /**
     * Listener that received the next pressed key code
     */
    CaptureKeyCodeListener captureKeyCodeListener;
    /**
     * Indicates if game is initialized
     */
    final boolean gameInitialzed;
    /**
     * Actual mouse X position
     */
    int mouseX;
    /**
     * Actual mouse Y position
     */
    int mouseY;
    /**
     * Manage key and mouse events
     */
    private final RunnableTask eventManagerTask = new RunnableTask()
    {
        /**
         * Manage key and mouse events in separate thread
         * <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @see RunnableTask#run()
         */
        @Override
        public void run()
        {
            JHelpGameDynamic.this.manageEvents();
        }
    };

    /**
     * Create a new instance of JHelpGameDynamic with default title
     */
    public JHelpGameDynamic()
    {
        this("Game");
    }

    /**
     * Create a new instance of JHelpGameDynamic
     *
     * @param title Game title/name
     */
    public JHelpGameDynamic(final String title)
    {
        this(title, null);
    }

    /**
     * Create a new instance of JHelpGameDynamic
     *
     * @param title     Game title/name
     * @param resources Resources to use
     */
    public JHelpGameDynamic(final String title, final Resources resources)
    {
        super(title, false);

        this.mutex = new Mutex();
        this.gameInitialzed = false;
        this.baseName = title;

        if (this.baseName == null)
        {
            this.baseName = "Game";
        }
        else
        {
            this.baseName = UtilText.removeWhiteCharacters(title)
                                    .replace('-', '_')
                                    .replace('/', '_')
                                    .replace('\\', '_')
                                    .replace('\'', '_')
                                    .replace('"',
                                             '_')
                                    .replace('$', '_');

            if (this.baseName.length() == 0)
            {
                this.baseName = "Game";
            }
        }

        this.mouseSensitiveAreas = new ArrayList<MouseSensitiveArea>();
        this.mouseSensitiveAreaListeners = new ArrayList<MouseSensitiveAreaListener>();

        this.preferences = new Preferences(
                UtilIO.obtainExternalFile(this.baseName + File.separator + "preferences.pref"));

        this.actionStates = new HashMap<ActionKey, Boolean>();
        this.keyAssociations = new HashMap<Integer, ActionKey>();

        this.keyAssociations.put(this.preferences.getValue(ActionKey.ACTION_ACTION.name(), KeyEvent.VK_C),
                                 ActionKey.ACTION_ACTION);
        this.keyAssociations.put(this.preferences.getValue(ActionKey.ACTION_CANCEL.name(), KeyEvent.VK_X),
                                 ActionKey.ACTION_CANCEL);
        this.keyAssociations.put(this.preferences.getValue(ActionKey.ACTION_DOWN.name(), KeyEvent.VK_DOWN),
                                 ActionKey.ACTION_DOWN);
        this.keyAssociations.put(this.preferences.getValue(ActionKey.ACTION_FIRE.name(), KeyEvent.VK_SPACE),
                                 ActionKey.ACTION_FIRE);
        this.keyAssociations.put(this.preferences.getValue(ActionKey.ACTION_FIRE_2.name(), KeyEvent.VK_CONTROL),
                                 ActionKey.ACTION_FIRE_2);
        this.keyAssociations.put(this.preferences.getValue(ActionKey.ACTION_LEFT.name(), KeyEvent.VK_LEFT),
                                 ActionKey.ACTION_LEFT);
        this.keyAssociations.put(this.preferences.getValue(ActionKey.ACTION_MENU.name(), KeyEvent.VK_M),
                                 ActionKey.ACTION_MENU);
        this.keyAssociations.put(this.preferences.getValue(ActionKey.ACTION_NEXT.name(), KeyEvent.VK_PAGE_DOWN),
                                 ActionKey.ACTION_NEXT);
        this.keyAssociations.put(this.preferences.getValue(ActionKey.ACTION_PAUSE.name(), KeyEvent.VK_P),
                                 ActionKey.ACTION_PAUSE);
        this.keyAssociations.put(this.preferences.getValue(ActionKey.ACTION_PREVIOUS.name(), KeyEvent.VK_PAGE_UP),
                                 ActionKey.ACTION_PREVIOUS);
        this.keyAssociations.put(this.preferences.getValue(ActionKey.ACTION_RIGHT.name(), KeyEvent.VK_RIGHT),
                                 ActionKey.ACTION_RIGHT);
        this.keyAssociations.put(this.preferences.getValue(ActionKey.ACTION_SPECIAL.name(), KeyEvent.VK_W),
                                 ActionKey.ACTION_SPECIAL);
        this.keyAssociations.put(this.preferences.getValue(ActionKey.ACTION_START.name(), KeyEvent.VK_S),
                                 ActionKey.ACTION_START);
        this.keyAssociations.put(this.preferences.getValue(ActionKey.ACTION_EXIT.name(), KeyEvent.VK_ESCAPE),
                                 ActionKey.ACTION_EXIT);
        this.keyAssociations.put(this.preferences.getValue(ActionKey.ACTION_UP.name(), KeyEvent.VK_UP),
                                 ActionKey.ACTION_UP);

        for (final ActionKey actionKey : ActionKey.values())
        {
            this.actionStates.put(actionKey, false);
        }

        this.mouseX = this.mouseY = Integer.MIN_VALUE;
        this.buttonLeft = this.buttonMiddle = this.buttonRight = false;

        if (resources == null)
        {
            this.resources = new Resources(this.obtainRecommendedExternalDirectory());
        }
        else
        {
            this.resources = resources;
        }

        this.constructGameInstance();

        ThreadManager.parallel(this.eventManagerTask, 1024);
    }

    /**
     * Create an empty sprite
     *
     * @param x      X start position
     * @param y      Y start position
     * @param width  Sprite width
     * @param height Sprite height
     * @return Created sprite
     */
    private JHelpSprite createSprite(final int x, final int y, final int width, final int height)
    {
        final JHelpImage image = this.dynamicImage.getImage();

        final boolean drawMode = image.isDrawMode();

        if (drawMode)
        {
            image.endDrawMode();
        }

        final JHelpSprite sprite = image.createSprite(x, y, width, height);

        if (drawMode)
        {
            image.startDrawMode();
        }

        return sprite;
    }

    /**
     * Remove a sprite
     *
     * @param sprite Sprite to remove
     */
    private void removeSprite(final JHelpSprite sprite)
    {

        final JHelpImage image = this.dynamicImage.getImage();

        final boolean drawMode = image.isDrawMode();

        if (drawMode)
        {
            image.endDrawMode();
        }

        image.removeSprite(sprite);

        if (drawMode)
        {
            image.startDrawMode();
        }
    }

    /**
     * Change actual key state
     *
     * @param keyCode Key code
     * @param down    Indicates if key is down or not
     */
    void changeKeyState(final int keyCode, final boolean down)
    {
        this.mutex.playInCriticalSectionVoid(() ->
                                             {
                                                 final ActionKey actionKey = this.keyAssociations.get(keyCode);

                                                 if (actionKey != null)
                                                 {
                                                     this.actionStates.put(actionKey, down);
                                                 }
                                             });
    }

    /**
     * Manage mouse and key events
     */
    void manageEvents()
    {
        if (this.captureKeyCodeListener != null)
        {
            return;
        }

        MouseSensitiveArea over = null;

        for (final MouseSensitiveArea mouseSensitiveArea : this.mouseSensitiveAreas)
        {
            if ((mouseSensitiveArea.enable()) && (mouseSensitiveArea.contains(this.mouseX, this.mouseY)))
            {
                over = mouseSensitiveArea;

                break;
            }
        }

        if ((this.overArea != null) && (this.overArea != over))
        {
            final Rectangle bounds    = this.overArea.bounds();
            final int       relativeX = this.mouseX - bounds.x;
            final int       relativeY = this.mouseY - bounds.y;

            for (final MouseSensitiveAreaListener mouseSensitiveAreaListener : this.mouseSensitiveAreaListeners)
            {
                mouseSensitiveAreaListener.mouseExitArea(this.overArea, relativeX, relativeY, this.mouseX, this.mouseY,
                                                         this.buttonLeft, this.buttonMiddle,
                                                         this.buttonRight);
            }
        }

        if ((over != null) && (over == this.overArea))
        {
            final Rectangle bounds    = over.bounds();
            final int       relativeX = this.mouseX - bounds.x;
            final int       relativeY = this.mouseY - bounds.y;

            for (final MouseSensitiveAreaListener mouseSensitiveAreaListener : this.mouseSensitiveAreaListeners)
            {
                mouseSensitiveAreaListener.mouseOverArea(over, relativeX, relativeY, this.mouseX, this.mouseY,
                                                         this.buttonLeft, this.buttonMiddle,
                                                         this.buttonRight);
            }
        }

        if ((over != null) && (over != this.overArea))
        {
            final Rectangle bounds    = over.bounds();
            final int       relativeX = this.mouseX - bounds.x;
            final int       relativeY = this.mouseY - bounds.y;

            for (final MouseSensitiveAreaListener mouseSensitiveAreaListener : this.mouseSensitiveAreaListeners)
            {
                mouseSensitiveAreaListener.mouseEnterArea(over, relativeX, relativeY, this.mouseX, this.mouseY,
                                                          this.buttonLeft, this.buttonMiddle,
                                                          this.buttonRight);
            }
        }

        this.overArea = over;

        this.mutex.playInCriticalSectionVoid(() ->
                                             {
                                                 if (this.eventManager != null)
                                                 {
                                                     if (this.eventManager.actionState(
                                                             Collections.unmodifiableMap(this.actionStates),
                                                             this.mouseX, this.mouseY,
                                                             this.buttonLeft, this.buttonMiddle,
                                                             this.buttonRight))
                                                     {
                                                         for (final ActionKey actionKey : this.actionStates.keySet())
                                                         {
                                                             this.actionStates.put(actionKey, false);
                                                         }
                                                     }
                                                 }
                                             });

        ThreadManager.parallel(this.eventManagerTask, 1);
    }

    /**
     * Add listeners to components
     */
    @Override
    protected void addListeners()
    {
        this.mouseKeyListener = new MouseKeyListener();
        this.labelJHelpImage.addMouseListener(this.mouseKeyListener);
        this.labelJHelpImage.addMouseMotionListener(this.mouseKeyListener);
        this.labelJHelpImage.addKeyListener(this.mouseKeyListener);
    }

    /**
     * Create components
     */
    @Override
    protected void createComponents()
    {
        final Rectangle bounds = UtilGUI.getScreenBounds(0);
        final Insets    insets = this.getInsets();
        this.dynamicImage = new JHelpDynamicImage(bounds.width - insets.left - insets.right,
                                                  bounds.height - insets.top - insets.bottom);

        this.labelJHelpImage = new LabelJHelpImage(this.dynamicImage.getImage());
        this.labelJHelpImage.setFocusable(true);
        this.labelJHelpImage.requestFocus();
        this.labelJHelpImage.requestFocusInWindow();
    }

    /**
     * Layout components inside the frame
     */
    @Override
    protected void layoutComponents()
    {
        this.setLayout(new BorderLayout());
        this.add(this.labelJHelpImage, BorderLayout.CENTER);
    }

    /**
     * Called when the object is construct (Just before starting threads).<br>
     * It allow you to add stufs at good place on object construction.<br>
     * Does nothing by default
     */
    protected void constructGameInstance()
    {
    }

    /**
     * add a mouse sensitive area
     *
     * @param mouseSensitiveArea Mouse sensitive area to add
     */
    public void addArea(final MouseSensitiveArea mouseSensitiveArea)
    {
        if (mouseSensitiveArea == null)
        {
            throw new NullPointerException("clickableArea mustn't be null");
        }

        this.mouseSensitiveAreas.add(0, mouseSensitiveArea);
    }

    /**
     * Add a sensitive animation
     *
     * @param sensitiveAnimation Animation to add
     */
    public void addSensitiveAnimation(final SensitiveAnimation sensitiveAnimation)
    {
        sensitiveAnimation.registerInside(this);
    }

    /**
     * Change areas visible status
     *
     * @param visible New areas visible status
     */
    public void areasVisible(final boolean visible)
    {
        if ((this.spriteShowAreas == null) && (!visible))
        {
            return;
        }

        if (!visible)
        {
            this.spriteShowAreas.setVisible(false);
            this.removeSprite(this.spriteShowAreas);
            this.spriteShowAreas = null;

            return;
        }

        if (this.spriteShowAreas == null)
        {
            this.spriteShowAreas = this.createSprite(0, 0, this.dynamicImage.getWidth(), this.dynamicImage.getHeight());
        }

        final JHelpImage image = this.spriteShowAreas.getImage();

        image.startDrawMode();

        image.clear(0);

        Rectangle bounds;

        for (final MouseSensitiveArea mouseSensitiveArea : this.mouseSensitiveAreas)
        {
            bounds = mouseSensitiveArea.detection();

            image.fillRectangle(bounds.x, bounds.y, bounds.width, bounds.height, mouseSensitiveArea.color());
        }

        image.endDrawMode();
        image.update();

        this.spriteShowAreas.setVisible(false);
        this.spriteShowAreas.setVisible(true);
    }

    /**
     * The base name of the game.<br>
     * It is the recommended external directory name to use for game data, use {@link UtilIO#obtainExternalFile(String)
     * UtilIO.obtainExternalFile(jhelpGame2D.baseName())} or {@link #obtainRecommendedExternalDirectory()} to have the
     * directory
     *
     * @return The base name of the game
     */
    public String baseName()
    {
        return this.baseName;
    }

    /**
     * Pass in special mode to be able to capture any next pressed key.<br>
     * Use full for settings action key by example
     *
     * @param captureKeyCodeListener Listener to call back when key is pressed
     */
    public void captureNextPressedKey(final CaptureKeyCodeListener captureKeyCodeListener)
    {
        this.captureKeyCodeListener = captureKeyCodeListener;
    }

    /**
     * The key action state and mouse state listener
     *
     * @return The key action state and mouse state listener
     */
    public EventManager eventManager()
    {
        return this.eventManager;
    }

    /**
     * Change the key action state and mouse state listener
     *
     * @param eventManager New key action state and mouse state listener
     */
    public void eventManager(final EventManager eventManager)
    {
        this.mutex.playInCriticalSectionVoid(() ->
                                             {
                                                 this.eventManager = eventManager;
                                             });
    }

    /**
     * Find area by ID
     *
     * @param identifier ID searched
     * @return Area found
     */
    public MouseSensitiveArea findArea(final int identifier)
    {
        for (final MouseSensitiveArea mouseSensitiveArea : this.mouseSensitiveAreas)
        {
            if (mouseSensitiveArea.identifier() == identifier)
            {
                return mouseSensitiveArea;
            }
        }

        return null;
    }

    /**
     * Internal desired height
     *
     * @return Internal desired height
     */
    public int internalHeight()
    {
        return this.labelJHelpImage.getHeight();
    }

    /**
     * Internal desired width
     *
     * @return Internal desired width
     */
    public int internalWidth()
    {
        return this.labelJHelpImage.getWidth();
    }

    /**
     * Obtain the associate key code of a key action
     *
     * @param actionKey Key action to have its key code
     * @return Associated key code or -1 if not associated
     */
    public int keyCode(final ActionKey actionKey)
    {
        for (final Entry<Integer, ActionKey> entry : this.keyAssociations.entrySet())
        {
            if (entry.getValue()
                     .equals(actionKey))
            {
                return entry.getKey();
            }
        }

        return -1;
    }

    /**
     * Change key code associated to a action key.<br>
     * If the key code is already mapped to a different action, then the mapped action is unmapped (that is to say that the
     * action have no more key code associated), then the desired association is done. We return the action that have loose is
     * association (or {@code null} if none) to let a chance to developer to alert the user that a key an action is unmapped and
     * do appropriate action.
     *
     * @param actionKey Action key to change its key code
     * @param keyCode   New associated key code
     * @return Previous action key associated to the key code (This action have no more key code associated).Return {@code null}
     * if the action have already this key code or no action is previously associated to this key code
     */
    public ActionKey keyCode(final ActionKey actionKey, final int keyCode)
    {
        final ActionKey oldAssociated = this.keyAssociations.get(keyCode);

        if (oldAssociated == actionKey)
        {
            return null;
        }

        int remove = 0;

        for (final Entry<Integer, ActionKey> entry : this.keyAssociations.entrySet())
        {
            if (entry.getValue() == actionKey)
            {
                remove = entry.getKey();

                break;
            }
        }

        this.keyAssociations.remove(remove);
        this.keyAssociations.put(keyCode, actionKey);

        this.preferences.setValue(actionKey.name(), keyCode);

        return oldAssociated;
    }

    /**
     * Obtain the textual representation of the key code associated to a key action
     *
     * @param actionKey Key action to have its key code textual representation
     * @return Key code textual representation
     */
    public String keyName(final ActionKey actionKey)
    {
        return KeyEvent.getKeyText(this.keyCode(actionKey));
    }

    /**
     * Recommended external directory where found external resources
     *
     * @return Recommended external directory where found external resources
     */
    public File obtainRecommendedExternalDirectory()
    {
        if (this.recommendedExternalDirectory == null)
        {
            this.recommendedExternalDirectory = UtilIO.obtainExternalFile(this.baseName());
        }

        UtilIO.createDirectory(this.recommendedExternalDirectory);
        return this.recommendedExternalDirectory;
    }

    /**
     * Add and play an animation
     *
     * @param dynamicAnimation Animation to play
     */
    public void playAnimation(final DynamicAnimation dynamicAnimation)
    {
        if (dynamicAnimation == null)
        {
            throw new NullPointerException("dynamicAnimation mustn't be null");
        }

        this.dynamicImage.playAnimation(dynamicAnimation);
    }

    /**
     * Preferences associates to the game.
     *
     * @return Preferences associates to the game
     */
    public Preferences preferences()
    {
        return this.preferences;
    }

    /**
     * Register a mouse sensitive area listener
     *
     * @param mouseSensitiveAreaListener Listener to register
     */
    public void registerMouseSensitiveAreaListener(final MouseSensitiveAreaListener mouseSensitiveAreaListener)
    {
        if (mouseSensitiveAreaListener == null)
        {
            throw new NullPointerException("mouseAreaListener mustn't be null");
        }

        this.mouseSensitiveAreaListeners.add(mouseSensitiveAreaListener);
    }

    /**
     * Remove a mouse sensitive area
     *
     * @param mouseSensitiveArea Area to remove
     */
    public void removeArea(final MouseSensitiveArea mouseSensitiveArea)
    {
        this.mouseSensitiveAreas.remove(mouseSensitiveArea);
    }

    /**
     * Resources access link
     *
     * @return Resources access link
     */
    public Resources resources()
    {
        return this.resources;
    }

    /**
     * Stop an animation.<br>
     * If the animation not playing, it does nothing
     *
     * @param dynamicAnimation Animation to stop
     */
    public void stopAnimation(final DynamicAnimation dynamicAnimation)
    {
        if (dynamicAnimation == null)
        {
            throw new NullPointerException("dynamicAnimation mustn't be null");
        }

        this.dynamicImage.stopAnimation(dynamicAnimation);
    }

    /**
     * Unregister a mouse area listener
     *
     * @param mouseSensitiveAreaListener Listener to remove
     */
    public void unregisterMouseSensitiveAreaListener(final MouseSensitiveAreaListener mouseSensitiveAreaListener)
    {
        this.mouseSensitiveAreaListeners.remove(mouseSensitiveAreaListener);
    }
}
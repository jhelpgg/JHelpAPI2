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

package jhelp.engine2.render;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.io.File;
import java.nio.IntBuffer;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import jhelp.engine2.animation.Animation;
import jhelp.engine2.animation.AnimationLoop;
import jhelp.engine2.geometry.Plane;
import jhelp.engine2.render.event.ClickInSpaceListener;
import jhelp.engine2.sound.SoundManager;
import jhelp.engine2.twoD.GUI2D;
import jhelp.engine2.twoD.Object2D;
import jhelp.engine2.util.BufferUtils;
import jhelp.engine2.util.DebugGLFErrorCallback;
import jhelp.engine2.util.GLU;
import jhelp.util.debug.Debug;
import jhelp.util.gui.JHelpTextAlign;
import jhelp.util.gui.alphabet.AlphabetBlue16x16;
import jhelp.util.io.UtilIO;
import jhelp.util.list.ArrayObject;
import jhelp.util.list.QueueSynchronized;
import jhelp.util.preference.Preferences;
import jhelp.util.text.UtilText;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.ThreadManager;
import jhelp.util.util.HashCode;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

/**
 * Window for show 3D.
 */
public class Window3D
{
    /**
     * Information about last object 2D or 3D detected
     *
     * @author JHelp
     */
    static class DetectionInfo
    {
        /**
         * X detection position
         */
        int      detectX;
        /**
         * Y detection position
         */
        int      detectY;
        /**
         * 2D manipulator
         */
        GUI2D    gui2d;
        /**
         * Indicates if mouse left is down
         */
        boolean  mouseButtonLeft;
        /**
         * Indicates if mouse right is down
         */
        boolean  mouseButtonRight;
        /**
         * Indicates if its is a drag (mouse move while at least one button is down)
         */
        boolean  mouseDrag;
        /**
         * Node detected
         */
        Node     nodeDetect;
        /**
         * Object 2D detected
         */
        Object2D object2DDetect;
        /**
         * Reference scene
         */
        Scene    scene;

        /**
         * Create a new instance of DetectionInfo
         *
         * @param object2dDetect   Object 2D detected
         * @param gui2d            2D manipulator
         * @param detectX          X detection position
         * @param detectY          Y detection position
         * @param mouseButtonLeft  Indicates if mouse left is down
         * @param mouseButtonRight Indicates if mouse right is down
         * @param mouseDrag        Indicates if its is a drag (mouse move while at least one button is down)
         * @param scene            Reference scene
         * @param nodeDetect       Node detected
         */
        DetectionInfo(
                final @Nullable Object2D object2dDetect, final @Nullable GUI2D gui2d,
                final int detectX, final int detectY,
                final boolean mouseButtonLeft, final boolean mouseButtonRight, final boolean mouseDrag,
                final @Nullable Scene scene, final @Nullable Node nodeDetect)
        {
            this.object2DDetect = object2dDetect;
            this.gui2d = gui2d;
            this.detectX = detectX;
            this.detectY = detectY;
            this.mouseButtonLeft = mouseButtonLeft;
            this.mouseButtonRight = mouseButtonRight;
            this.mouseDrag = mouseDrag;
            this.scene = scene;
            this.nodeDetect = nodeDetect;
        }

        /**
         * Copy the given detection information
         *
         * @param detectionInfo Detection information to copy
         */
        void copy(@NotNull DetectionInfo detectionInfo)
        {
            this.gui2d = detectionInfo.gui2d;
            this.detectX = detectionInfo.detectX;
            this.detectY = detectionInfo.detectY;
            this.mouseButtonLeft = detectionInfo.mouseButtonLeft;
            this.mouseButtonRight = detectionInfo.mouseButtonRight;
            this.mouseDrag = detectionInfo.mouseDrag;
            this.scene = detectionInfo.scene;
            this.nodeDetect = detectionInfo.nodeDetect;
        }

        /**
         * Hash code
         *
         * @return Hash code
         */
        @Override
        public int hashCode()
        {
            return HashCode.computeHashCode(this.detectX, this.detectY,
                                            this.mouseButtonLeft, this.mouseButtonRight, this.mouseDrag,
                                            this.object2DDetect, this.gui2d, this.scene, this.nodeDetect);
        }

        /**
         * Indicates if an object equals to this detection information
         *
         * @param object Object to compare with
         * @return {@code true} if they are equals
         */
        @Override
        public boolean equals(final Object object)
        {
            if (object == this)
            {
                return true;
            }

            if (object == null)
            {
                return false;
            }

            if (!DetectionInfo.class.equals(object.getClass()))
            {
                return false;
            }

            DetectionInfo detectionInfo = (DetectionInfo) object;

            return this.detectX == detectionInfo.detectX &&
                   this.detectY == detectionInfo.detectY &&
                   this.mouseButtonLeft == detectionInfo.mouseButtonLeft &&
                   this.mouseButtonRight == detectionInfo.mouseButtonRight &&
                   this.mouseDrag == detectionInfo.mouseDrag &&
                   this.object2DDetect == detectionInfo.object2DDetect &&
                   this.gui2d == detectionInfo.gui2d &&
                   this.scene == detectionInfo.scene &&
                   this.nodeDetect == detectionInfo.nodeDetect;
        }
    }

    /**
     * Name of the data directory
     */
    private static final String DATA_DIRECTORY_NAME = "data";
    /**
     * Name of the preference file
     */
    private static final String PREFERENCES_NAME    = "preferences.pref";

    /**
     * Create a widow that take the all screen
     *
     * @param title Window title
     * @return Created window
     * @throws NullPointerException           If the title is {@code null}
     * @throws Window3DCantBeCreatedException If system can't create the window
     */
    public static Window3D createFullWidow(@NotNull String title)
    {
        Objects.requireNonNull(title, "title MUST NOT be null!");
        return new Window3D(800, 600, title, false, true);
    }

    /**
     * Create a window with initial specified size.<br>
     * Window size must be at least 16x16.
     *
     * @param width     Initial width
     * @param height    Initial height
     * @param title     Window title
     * @param decorated Indicated if have to show window decoration
     * @return Created window
     * @throws NullPointerException           If the title is {@code null}
     * @throws IllegalArgumentException       If window size too small
     * @throws Window3DCantBeCreatedException If system can't create the window
     */
    public static Window3D createSizedWindow(
            int width, int height, @NotNull String title, boolean decorated)
    {
        Objects.requireNonNull(title, "title MUST NOT be null!");

        if (width < 16 || height < 16)
        {
            throw new IllegalArgumentException(
                    "Dimensions of window MUST be at least 16x16, not " + width + "x" + height);
        }

        return new Window3D(width, height, title, decorated, false);
    }

    /**
     * Obtain/create a data file
     *
     * @param fileName File name
     * @return Data file
     */
    private static File dataFile(String fileName)
    {
        return UtilIO.obtainExternalFile(UtilText.concatenate(Window3D.DATA_DIRECTORY_NAME, "/", fileName));
    }

    /**
     * Task for signal that mouse exit a 2D object
     *
     * @author JHelp
     */
    class OutObject2D
            implements ConsumerTask<DetectionInfo>
    {
        /**
         * Create a new instance of OutObject2D
         */
        OutObject2D()
        {
        }

        /**
         * Signal to clickInSpaceListeners the last detection <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param detectionInfo Last detection information
         * @see jhelp.util.thread.ConsumerTask#consume(Object)
         */
        @Override
        public void consume(final DetectionInfo detectionInfo)
        {
            detectionInfo.gui2d.mouseState(detectionInfo.detectX, detectionInfo.detectY,
                                           detectionInfo.mouseButtonLeft,
                                           detectionInfo.mouseButtonRight,
                                           detectionInfo.mouseDrag,
                                           null);
        }
    }

    /**
     * Task for signal to clickInSpaceListeners the last detection
     *
     * @author JHelp
     */
    class UpdateMouseDetection
            implements ConsumerTask<DetectionInfo>
    {
        private DetectionInfo lastDetection;

        /**
         * Create a new instance of UpdateMouseDetection
         */
        UpdateMouseDetection()
        {
            this.lastDetection = new DetectionInfo(null, null, -1, -1, false, false, false, null, null);
        }

        /**
         * Signal to clickInSpaceListeners the last detection <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param detectionInfo Last detection information
         * @see jhelp.util.thread.ConsumerTask#consume(Object)
         */
        @Override
        public void consume(final DetectionInfo detectionInfo)
        {
            if (this.lastDetection.equals(detectionInfo))
            {
                return;
            }

            this.lastDetection.copy(detectionInfo);

            // If a 2D object is detect
            if (detectionInfo.object2DDetect != null)
            {
                // Update mouse state for 2D objects
                detectionInfo.gui2d.mouseState(detectionInfo.detectX, detectionInfo.detectY,
                                               detectionInfo.mouseButtonLeft, detectionInfo.mouseButtonRight,
                                               detectionInfo.mouseDrag, detectionInfo.object2DDetect);
            }
            else if (!detectionInfo.mouseDrag)
            {
                // If it is not a mouse drag, update mouse state for scene
                detectionInfo.scene.mouseState(detectionInfo.mouseButtonLeft, detectionInfo.mouseButtonRight,
                                               detectionInfo.nodeDetect);

                if ((detectionInfo.nodeDetect == null) &&
                    (detectionInfo.mouseButtonLeft || detectionInfo.mouseButtonRight))
                {
                    Window3D.this.fireClickInSpace(detectionInfo.detectX, detectionInfo.detectY,
                                                   detectionInfo.mouseButtonLeft,
                                                   detectionInfo.mouseButtonRight);
                }
            }
        }
    }

    /**
     * Actual absolute frame
     */
    private       float                             absoluteFrame;
    /**
     * Actions manager
     */
    private final ActionManager                     actionManager;
    /**
     * Time to synchronize animations
     */
    private       long                              animationTime;
    /**
     * FPS for play animations
     */
    private       int                               animationsFps;
    /**
     * Animation manager
     */
    private final AnimationsManager                 animationsManager;
    /**
     * Listeners of click outside any 3D/2D object (in empty space)
     */
    private final ArrayObject<ClickInSpaceListener> clickInSpaceListeners;
    /**
     * Current rendered scene
     */
    private       Scene                             currentScene;
    /**
     * X of detection point
     */
    private       int                               detectX;
    /**
     * Y of detection point
     */
    private       int                               detectY;
    /**
     * Indicates if detection is activate
     */
    private       boolean                           detectionActivate;
    /**
     * Current fps
     */
    private       int                               fps;
    /**
     * 2D manager
     */
    private final GUI2D                             gui2d;
    /**
     * Initial height
     */
    private final int                               height;
    /**
     * Last U pick
     */
    private       int                               lastPickU;
    /**
     * Last V pick
     */
    private       int                               lastPickV;
    /**
     * Lights access
     */
    private       Lights                            lights;
    /**
     * Material use for 2D objects
     */
    private       Material                          material2D;
    /**
     * Temporary matrix for convert object space to view space
     */
    private       double[]                          modelView;
    /**
     * Indicates if mouse left button is down
     */
    private       boolean                           mouseButtonLeft;
    /**
     * Indicates if mouse right button is down
     */
    private       boolean                           mouseButtonRight;
    /**
     * Indicated if the mouse drag (Move with at least button down)
     */
    private       boolean                           mouseDrag;
    /**
     * Scene will replace current one in OpenGL loop
     */
    private       Scene                             newScene;
    /**
     * Actual detected node : (detectX, detectY) say the location of the detection
     */
    private       Node                              nodeDetect;
    /**
     * Actual detected 2D object : (detectX, detectY) say the location of the detection
     */
    private       Object2D                          object2DDetect;
    /**
     * Object where FPS is draw
     */
    private final Object2D                          objectFPS;
    /**
     * Task for signal mouse goes outside a 2D object
     */
    private final OutObject2D                       outObject2D;
    /**
     * Actual pick color
     */
    private       Color4f                           pickColor;
    /**
     * Last UV node pick
     */
    private       Node                              pickUVnode;
    /**
     * Plane use for 2D objects
     */
    private final Plane                             planeFor2D;
    /**
     * Preferences associated to this window
     */
    private final Preferences                       preferences;
    /**
     * Projection matrix for pass view to screen
     */
    private       double[]                          projection;
    /**
     * Indicates if widow is ready
     */
    private final AtomicBoolean                     ready;
    /**
     * Indicates if window is currently showing
     */
    private final AtomicBoolean                     showing;
    /**
     * Sound manager
     */
    private       SoundManager                      soundManager;
    /**
     * Texture for draw FPS
     */
    private final TextureAlphabetText               textureFPS;
    /**
     * List of textures to remove from video memory
     */
    private final QueueSynchronized<Texture>        texturesToRemove;
    /**
     * Task for signal to clickInSpaceListeners the last detection
     */
    private final UpdateMouseDetection              updateMouseDetection;
    /**
     * View port to consider the FOV
     */
    private       int[]                             viewPort;
    /**
     * Initial width
     */
    private final int                               width;
    /**
     * Reference for GLFW to this window
     */
    private final long                              window;

    /**
     * Create a window for show 3D
     *
     * @param width     Initial width
     * @param height    Initial height
     * @param title     Widow title
     * @param decorated Indicates if window is decorated
     * @param maximized Indicates if window takes the all screen
     */
    private Window3D(int width, int height, @NotNull String title, boolean decorated, boolean maximized)
    {
        this.showing = new AtomicBoolean(true);
        this.ready = new AtomicBoolean(false);
        this.texturesToRemove = new QueueSynchronized<>();
        this.planeFor2D = new Plane(false, true);
        this.gui2d = new GUI2D();
        this.currentScene = new Scene();
        this.clickInSpaceListeners = new ArrayObject<>();
        this.outObject2D = new OutObject2D();
        this.updateMouseDetection = new UpdateMouseDetection();
        this.animationsManager = new AnimationsManager(this);
        this.preferences = new Preferences(Window3D.dataFile(Window3D.PREFERENCES_NAME));
        this.actionManager = new ActionManager(this.preferences);
        this.animationsFps = 25;
        this.detectionActivate = true;
        this.detectX = -1;
        this.detectY = -1;

        this.textureFPS = new TextureAlphabetText(AlphabetBlue16x16.ALPHABET_BLUE16X16, 7, 1,
                                                  "0 FPS", JHelpTextAlign.CENTER, 0x11000000, 0x456789AB);
        this.objectFPS = new Object2D(5, 5, this.textureFPS.width(), this.textureFPS.height());
        this.objectFPS.texture(this.textureFPS);
        this.objectFPS.visible(false);
        this.gui2d.addOver3D(this.objectFPS);

        // Setup the error callback.
        GLFWErrorCallback.create(DebugGLFErrorCallback.DEBUG_GLF_ERROR_CALLBACK).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!GLFW.glfwInit())
        {
            throw new Window3DCantBeCreatedException("Initialization of GLFW failed");
        }

        // Configure GLFW
        GLFW.glfwDefaultWindowHints();
        // the window not show for the moment, need some initialization to do before
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);

        GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, decorated ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_MAXIMIZED, maximized ? GLFW.GLFW_TRUE : GLFW.GLFW_FALSE);

        // Create the window
        this.window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);

        if (this.window == MemoryUtil.NULL)
        {
            throw new Window3DCantBeCreatedException("Failed to create the GLFW window");
        }

        //Register UI events
        GLFW.glfwSetKeyCallback(this.window, this::keyEvent);
        GLFW.glfwSetCursorEnterCallback(this.window, this::mouseEntered);
        GLFW.glfwSetJoystickCallback(this::joystickConnected);
        GLFW.glfwSetMouseButtonCallback(this.window, this::mouseButton);
        GLFW.glfwSetCursorPosCallback(this.window, this::mousePosition);
        GLFW.glfwSetWindowCloseCallback(this.window, this::closeWindow);

        // Get the thread stack and push a new frame

        try (MemoryStack stack = MemoryStack.stackPush())
        {
            final IntBuffer pWidth  = stack.mallocInt(1);
            final IntBuffer pHeight = stack.mallocInt(1);

            // Get the window size passed to GLFW.glfwCreateWindow
            GLFW.glfwGetWindowSize(this.window, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            // Center the window
            GLFW.glfwSetWindowPos(
                    this.window,
                    (videoMode.width() - pWidth.get(0)) / 2,
                    (videoMode.height() - pHeight.get(0)) / 2);
        }

        // Make the window visible
        GLFW.glfwShowWindow(this.window);

        // Get the thread stack and compute window size

        try (MemoryStack stack = MemoryStack.stackPush())
        {
            final IntBuffer pWidth  = stack.mallocInt(1);
            final IntBuffer pHeight = stack.mallocInt(1);

            // Get the real window size
            GLFW.glfwGetWindowSize(this.window, pWidth, pHeight);
            width = pWidth.get();
            height = pHeight.get();
        }

        this.width = width;
        this.height = height;

        //Launch the OpenGL thread and render the 3D in it
        ThreadManager.parallel(this::render3D, 128);
    }

    /**
     * Will be called when the user attempts to close the specified window, for example by clicking the close widget in the title bar.
     *
     * @param window the window that the user attempted to close
     */
    private void closeWindow(long window)
    {
        // TODO Implements the method
        Debug.todo("Implements the method");
        //Bellow, trick for avoid exit immediately after the method
        //GLFW.glfwSetWindowShouldClose(this.window, false);

        //Closing
        GLFW.glfwSetWindowShouldClose(this.window, true);
        this.animationsManager.destroy();
        this.showing.set(false);
    }

    /**
     * Compute actual model view
     */
    @ThreadOpenGL
    private void computeModelView()
    {
        // Create, if need, temporary model view
        if (this.modelView == null)
        {
            this.modelView = new double[16];
        }

        // Get model view
        BufferUtils.TEMPORARY_DOUBLE_BUFFER.rewind();
        GL11.glGetDoublev(GL11.GL_MODELVIEW_MATRIX, BufferUtils.TEMPORARY_DOUBLE_BUFFER);
        BufferUtils.fill(this.modelView);
    }

    /**
     * Compute actual projection
     */
    @ThreadOpenGL
    private void computeProjection()
    {
        // Create, if need, temporary projection
        if (this.projection == null)
        {
            this.projection = new double[16];
        }

        // Get projection
        BufferUtils.TEMPORARY_DOUBLE_BUFFER.rewind();
        GL11.glGetDoublev(GL11.GL_PROJECTION_MATRIX, BufferUtils.TEMPORARY_DOUBLE_BUFFER);
        BufferUtils.fill(this.projection);
    }

    /**
     * Compute actual view port
     */
    @ThreadOpenGL
    private void computeViewPort()
    {
        // Create, if need, temporary view port
        if (this.viewPort == null)
        {
            this.viewPort = new int[16];
        }

        // Get view port
        BufferUtils.TEMPORARY_INT_BUFFER.rewind();
        GL11.glGetIntegerv(GL11.GL_VIEWPORT, BufferUtils.TEMPORARY_INT_BUFFER);
        BufferUtils.fill(this.viewPort);
    }

    /**
     * Draw object 2D witch are over 3D
     */
    @ThreadOpenGL
    private void drawOver3D()
    {
        // Get all 2D objects over 3D
        final Iterator<Object2D> iterator = this.gui2d.iteratorOver3D();
        Object2D                 object2D;
        Texture                  texture;

        // For each object
        while (iterator.hasNext())
        {
            // Draw the object
            object2D = iterator.next();
            texture = object2D.texture();
            if (texture != null)
            {
                this.show2D(texture, object2D.x(), object2D.y(), object2D.width(), object2D.height());
            }
        }
    }

    /**
     * Draw object 2D witch are under 3D
     */
    @ThreadOpenGL
    private void drawUnder3D()
    {
        // Get all 2D objects uder 3D
        final Iterator<Object2D> iterator = this.gui2d.iteratorUnder3D();
        Object2D                 object2D;
        Texture                  texture;

        // For each object
        while (iterator.hasNext())
        {
            // Draw the object
            object2D = iterator.next();
            texture = object2D.texture();
            if (texture != null)
            {
                this.show2D(texture, object2D.x(), object2D.y(), object2D.width(), object2D.height());
            }
        }
    }

    /**
     * Project a 3D point to a screen point
     *
     * @param x X
     * @param y Y
     * @param z Z
     * @return Projected point
     */
    @ThreadOpenGL
    private @NotNull Point2D gluProject(final float x, final float y, final float z)
    {
        this.computeModelView();
        this.computeProjection();
        this.computeViewPort();
        final double[] point = new double[3];
        GLU.gluProject(x, y, z, this.modelView, 0, this.projection, 0, this.viewPort, 0, point, 0);
        return new Point2D(Math.round(point[0] / point[2]), Math.round((this.height - point[1]) / point[2]));
    }

    /**
     * Convert a screen point to 3D point.<br>
     * You have to specify the Z of the 3D point you want
     *
     * @param x X
     * @param y Y
     * @param z Wanted Z
     * @return Converted point
     */
    @ThreadOpenGL
    private @NotNull Point3D gluUnProject(final float x, final float y, final float z)
    {
        this.computeModelView();
        this.computeProjection();
        this.computeViewPort();
        final double[] point = new double[4];
        GLU.gluUnProject(x, this.viewPort[3] - y, z, this.modelView, 0, this.projection, 0, this.viewPort, 0, point, 0);
        final double rate = z / point[2];
        return new Point3D((float) (point[0] * rate), (float) (point[1] * rate), z);
    }

    /**
     * Initialize the 3D
     **/
    @ThreadOpenGL
    private void initialize3D()
    {
        // Make the OpenGL context current
        GLFW.glfwMakeContextCurrent(this.window);
        // Enable v-sync
        GLFW.glfwSwapInterval(1);

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        BufferUtils.TEMPORARY_INT_BUFFER.rewind();
        GL11.glGetIntegerv(GL11.GL_MAX_LIGHTS, BufferUtils.TEMPORARY_INT_BUFFER);
        BufferUtils.TEMPORARY_INT_BUFFER.rewind();
        this.lights = new Lights(BufferUtils.TEMPORARY_INT_BUFFER.get());

        // *************************
        // *** Initialize OpenGL ***
        // *************************
        // Alpha enable
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        // Set alpha precision
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.01f);
        // Material can be colored
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        // For performance disable texture, we enable them only on need
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        // Way to compute alpha
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        // We accept blinding
        GL11.glEnable(GL11.GL_BLEND);
        // Fix the view port
        GL11.glViewport(0, 0, this.width, this.height);
        // Normalization is enable
        GL11.glEnable(GL11.GL_NORMALIZE);
        // Fix the view port. Yes again, I don't know why, but it work better on
        // doing that
        GL11.glViewport(0, 0, this.width, this.height);

        // Set the "3D feeling".
        // That is to say how the 3D looks like
        // Here we want just see the depth, but not have fish eye effect
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        final float ratio = (float) this.width / (float) this.height;
        GLU.gluPerspective(45.0f, ratio, 0.1f, 5000f);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        // Initialize background
        GL11.glClearColor(1f, 1f, 1f, 1f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        // Enable see and hide face
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_FRONT);

        // Light base adjustment for smooth effect
        GL11.glLightModeli(GL11.GL_LIGHT_MODEL_LOCAL_VIEWER, GL11.GL_TRUE);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glLightModeli(GL12.GL_LIGHT_MODEL_COLOR_CONTROL, GL12.GL_SEPARATE_SPECULAR_COLOR);
        GL11.glLightModeli(GL11.GL_LIGHT_MODEL_TWO_SIDE, 1);

        // Enable lights and default light
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    /**
     * Will be called when a joystick is connected to or disconnected from the system.
     *
     * @param joystickID the joystick that was connected or disconnected
     * @param event      one of {@link GLFW#GLFW_CONNECTED CONNECTED} or {@link GLFW#GLFW_DISCONNECTED DISCONNECTED}
     */
    @ThreadOpenGL
    private void joystickConnected(final int joystickID, final int event)
    {
        // TODO Implements the method
        Debug.todo("Implements the method");
    }

    /**
     * Will be called when a key is pressed, repeated or released.
     *
     * @param window    the window that received the event
     * @param key       the keyboard key that was pressed or released
     * @param scanCode  the system-specific scanCode of the key
     * @param action    the key action. One of:<br><table><tr><td>{@link GLFW#GLFW_PRESS PRESS}</td><td>{@link GLFW#GLFW_RELEASE RELEASE}</td><td>{@link GLFW#GLFW_REPEAT REPEAT}</td></tr></table>
     * @param modifiers bit field describing which modifiers keys were held down
     */
    @ThreadOpenGL
    private void keyEvent(final long window, final int key, final int scanCode, final int action, final int modifiers)
    {
        this.actionManager.keyEvent(key, action);
    }

    /**
     * Will be called when a mouse button is pressed or released.
     *
     * @param window    the window that received the event
     * @param button    the mouse button that was pressed or released
     * @param action    the button action. One of:<br><table><tr><td>{@link GLFW#GLFW_PRESS PRESS}</td><td>{@link GLFW#GLFW_RELEASE RELEASE}</td><td>{@link GLFW#GLFW_REPEAT REPEAT}</td></tr></table>
     * @param modifiers bit field describing which modifiers keys were held down
     */
    @ThreadOpenGL
    private void mouseButton(final long window, final int button, final int action, final int modifiers)
    {
        if (action == GLFW.GLFW_PRESS)
        {
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT)
            {
                this.mouseButtonLeft = true;
            }
            else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT)
            {
                this.mouseButtonRight = true;
            }
        }
        else if (action == GLFW.GLFW_RELEASE)
        {
            if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT)
            {
                this.mouseButtonLeft = false;
            }
            else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT)
            {
                this.mouseButtonRight = false;
            }
        }

        if (!this.mouseButtonRight && !this.mouseButtonLeft)
        {
            this.mouseDrag = false;
        }
    }

    /**
     * Will be called if cursor enter or exit a window
     *
     * @param window  Entered/exited window pointer
     * @param entered Indicates if entered ({@code true}) or exited ({@code false})
     */
    @ThreadOpenGL
    private void mouseEntered(final long window, final boolean entered)
    {
        if (!entered)
        {
            this.detectX = -1;
            this.detectY = -1;
        }
    }

    /**
     * Will be called when the cursor is moved.
     * <p>
     * <p>The callback function receives the cursor position, measured in screen coordinates but relative to the top-left corner of the window client area. On
     * platforms that provide it, the full sub-pixel cursor position is passed on.</p>
     *
     * @param window  the window that received the event
     * @param cursorX the new cursor x-coordinate, relative to the left edge of the client area
     * @param cursorY the new cursor y-coordinate, relative to the top edge of the client area
     */
    @ThreadOpenGL
    private void mousePosition(final long window, double cursorX, double cursorY)
    {
        final int mouseX = (int) cursorX;
        final int mouseY = (int) cursorY;

        if (this.detectX != mouseX || this.detectY != mouseY)
        {
            this.detectX = mouseX;
            this.detectY = mouseY;

            if (this.mouseButtonRight || this.mouseButtonLeft)
            {
                this.mouseDrag = true;
            }
        }
    }

    /**
     * Compute actual pick color
     *
     * @param x X
     * @param y Y
     */
    @ThreadOpenGL
    private void pickColor(final int x, final int y)
    {
        if (this.pickColor == null)
        {
            this.pickColor = new Color4f();
        }

        // Get picking color
        BufferUtils.TEMPORARY_FLOAT_BUFFER.rewind();
        GL11.glReadPixels(x, this.height - y, 1, 1, GL11.GL_RGBA, GL11.GL_FLOAT, BufferUtils.TEMPORARY_FLOAT_BUFFER);
        BufferUtils.TEMPORARY_FLOAT_BUFFER.rewind();

        // Convert in RGB value
        final float red   = BufferUtils.TEMPORARY_FLOAT_BUFFER.get();
        final float green = BufferUtils.TEMPORARY_FLOAT_BUFFER.get();
        final float blue  = BufferUtils.TEMPORARY_FLOAT_BUFFER.get();
        BufferUtils.TEMPORARY_FLOAT_BUFFER.rewind();

        // Update picking color
        this.pickColor.set(red, green, blue);
    }

    /**
     * Initialize material for 2D
     */
    @ThreadOpenGL
    private void prepareMaterial2D()
    {
        if (this.material2D == null)
        {
            this.material2D = new Material(Material.MATERIAL_FOR_2D_NAME);
            this.material2D.colorEmissive().set(1f);
            this.material2D.specularLevel(1f);
            this.material2D.shininess(128);
            this.material2D.colorDiffuse().set(1f);
            this.material2D.colorSpecular().set();
            this.material2D.colorAmbient().set(1f);
            this.material2D.twoSided(true);
        }
        this.material2D.prepareMaterial();
    }

    /**
     * 3D rendering
     */
    @ThreadOpenGL
    private void render3D()
    {
        this.soundManager = new SoundManager();
        this.initialize3D();
        this.animationTime = System.currentTimeMillis();

        synchronized (this.ready)
        {
            this.ready.set(true);
            this.ready.notifyAll();
        }

        int  frameCount = 0;
        long startTime  = System.currentTimeMillis();
        long laps;

        while (!GLFW.glfwWindowShouldClose(this.window))
        {
            // clear the framebuffer
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            this.renderLoop();

            GL11.glEnable(GL11.GL_DEPTH_TEST);
            // swap the color buffers
            GLFW.glfwSwapBuffers(this.window);

            // Poll for window events. The key callback will only be invoked during this call.
            GLFW.glfwPollEvents();

            this.actionManager.publishActions();

            frameCount++;
            laps = System.currentTimeMillis() - startTime;

            if (laps >= 1000)
            {
                this.fps = (int) Math.round((frameCount * 1000d) / laps);

                if (this.objectFPS.visible())
                {
                    this.textureFPS.text(this.fps + " FPS");
                }

                frameCount = 0;
                startTime = System.currentTimeMillis();
            }
        }

        Debug.information("Good bye!");

        this.soundManager.destroy();
        // Free the window callbacks and destroy the window
        Callbacks.glfwFreeCallbacks(this.window);
        GLFW.glfwDestroyWindow(this.window);

        // Terminate GLFW and free the error callback
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

    /**
     * One render loop
     */
    @ThreadOpenGL
    private void renderLoop()
    {
        // Remove one texture from memory
        // We don't remove all in once to avoid fix effect
        if (!this.texturesToRemove.isEmpty())
        {
            this.texturesToRemove.outQueue().removeFromMemory();
        }

        if (this.newScene != null)
        {
            this.currentScene = this.newScene;
            this.currentScene = null;
        }

        this.absoluteFrame = (float) (((System.currentTimeMillis() - this.animationTime) * this.animationsFps) / 1000d);

        // Render picking mode
        if (this.detectionActivate)
        {
            final Node pickUVnode = this.pickUVnode;

            if (pickUVnode != null)
            {
                this.renderPickUV(pickUVnode);
            }
            else
            {
                this.renderPicking();
            }
        }

        // Render the lights
        this.lights.render();

        // Render the scene
        this.renderScene();
    }

    /**
     * Render for pick UV
     *
     * @param pickUVnode Node to render the pick UV
     */
    @ThreadOpenGL
    private void renderPickUV(@NotNull Node pickUVnode)
    {
        // Prepare for "picking rendering"
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glClearColor(1f, 1f, 1f, 1f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glPushMatrix();

        // Render the scene in picking mode
        this.currentScene.renderPickingUV(pickUVnode);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);

        // If detection point is on the screen
        if ((this.detectX >= 0) && (this.detectX < this.width) && (this.detectY >= 0) && (this.detectY < this.height))
        {
            // Compute pick color and node pick
            this.pickColor(this.detectX, this.detectY);

            final int red   = (int) (this.pickColor.red() * 255);
            final int green = (int) (this.pickColor.green() * 255);
            final int blue  = (int) (this.pickColor.blue() * 255);

            this.lastPickU = blue;
            this.lastPickV = green;

            if ((this.pickUVnode.pickUVlistener != null) && (red < 128))
            {
                this.pickUVnode.pickUVlistener.pickUV(this.lastPickU, this.lastPickV, this.pickUVnode);
            }
        }
    }

    /**
     * Render the scene on picking mode
     */
    @ThreadOpenGL
    private void renderPicking()
    {
        // Prepare for "picking rendering"
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glClearColor(1f, 1f, 1f, 1f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glPushMatrix();

        // Render the scene in picking mode
        this.currentScene.renderTheScenePicking(this);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);

        if (this.object2DDetect != null)
        {
            if (!this.object2DDetect.detected(this.detectX, this.detectY))
            {
                ThreadManager.parallel(this.outObject2D,
                                       new DetectionInfo(null, this.gui2d, this.detectX, this.detectY,
                                                         this.mouseButtonLeft, this.mouseButtonRight, this.mouseDrag,
                                                         null, null));
            }
        }

        // If detection point is on the screen
        if ((this.detectX >= 0) && (this.detectX < this.width) && (this.detectY >= 0) && (this.detectY < this.height))
        {
            // Compute pick color and node pick
            this.pickColor(this.detectX, this.detectY);

            this.nodeDetect = this.currentScene.pickingNode(this.pickColor);
            if (this.nodeDetect != null)
            {
                // If node is detect, verify if a 2D object over the 3D can be
                // detect too
                this.object2DDetect = this.gui2d.detectOver3D(this.detectX, this.detectY);
            }
            else
            {
                // If no node detect, verify if a 2D object is detect
                this.object2DDetect = this.gui2d.detectOver3DorUnder3D(this.detectX, this.detectY);
            }
        }
        else
        {
            this.nodeDetect = null;
            this.object2DDetect = null;
        }

        ThreadManager.parallel(this.updateMouseDetection,
                               new DetectionInfo(this.object2DDetect, this.gui2d,
                                                 this.detectX, this.detectY,
                                                 this.mouseButtonLeft, this.mouseButtonRight, this.mouseDrag,
                                                 this.currentScene, this.nodeDetect));
    }

    /**
     * Render the scene
     */
    @ThreadOpenGL
    private void renderScene()
    {
        try
        {
            //this.renderMirors();

            // Draw the background and clear Z-Buffer
            this.currentScene.drawBackground();
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

            // Draw 2D objects under 3D
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            this.drawUnder3D();
            GL11.glEnable(GL11.GL_DEPTH_TEST);

            // Render the scene
            GL11.glPushMatrix();
            this.currentScene.renderTheScene(this);
            GL11.glPopMatrix();

            // Draw 2D objects over 3D
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            this.drawOver3D();
        }
        catch (final Exception | Error ignored)
        {
        }
    }

    /**
     * Show a 2D object or hotspot
     *
     * @param texture Texture to draw
     * @param x       X
     * @param y       Y
     * @param width   Width
     * @param height  Height
     */
    @ThreadOpenGL
    private void show2D(final @NotNull Texture texture, final int x, final int y, final int width, final int height)
    {
        // Make the material for 2D
        this.prepareMaterial2D();

        // Compute up-left and down-right corner in 3D
        final Point3D point1 = this.gluUnProject(x, y, -1f);
        final Point3D point2 = this.gluUnProject(x + width, y + height, -1f);

        // get new positions and size
        final float x1 = point1.x();
        final float y1 = point1.y();
        final float x2 = point2.x();
        final float y2 = point2.y();
        final float w  = Math.abs(x1 - x2);
        final float h  = Math.abs(y1 - y2);
        // Compute middle point
        final float xx = Math.min(x1, x2) + (0.5f * w);
        final float yy = Math.min(y1, y2) + (0.5f * h);

        // Draw the object on the 2D plane
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        texture.bind();
        GL11.glPushMatrix();
        GL11.glTranslatef(xx, yy, -1f);
        GL11.glScalef(w, h, 1);
        this.planeFor2D.drawObject();
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    /**
     * Make the current thread wait until window is ready
     */
    private void waitReady()
    {
        synchronized (this.ready)
        {
            while (!this.ready.get())
            {
                try
                {
                    this.ready.wait();
                }
                catch (Exception ignored)
                {
                }
            }
        }
    }

    /**
     * Draw a hotspot in picking mode (That is to say fill with it's node parent picking color)
     *
     * @param node  Node witch carry hotspot
     * @param red   Red value of picking color
     * @param green Green value of picking color
     * @param blue  Blue value of picking color
     */
    @ThreadOpenGL
    void drawPickHotspot(final @NotNull Node node, final float red, final float green, final float blue)
    {
        final Texture textureHotspot = node.textureHotspot();
        // If no hotspot texture, do nothing
        if (textureHotspot == null)
        {
            return;
        }

        // Project center node in the model view
        final Point3D center = node.center();
        this.computeModelView();
        final double cx = center.x();
        final double cy = center.y();
        final double cz = center.z();
        final double px =
                (cx * this.modelView[0]) + (cy * this.modelView[4]) + (cz * this.modelView[8]) + this.modelView[12];
        final double py =
                (cx * this.modelView[1]) + (cy * this.modelView[5]) + (cz * this.modelView[9]) + this.modelView[13];
        final double pz =
                (cx * this.modelView[2]) + (cy * this.modelView[6]) + (cz * this.modelView[10]) + this.modelView[14];

        // Project the new center in the screen
        final float   z              = (float) pz;
        final Point2D centerOnScreen = this.gluProject((float) px, (float) py, z);
        float         x1             = centerOnScreen.x() - (textureHotspot.width() / 2f);
        float         y1             = centerOnScreen.y() - (textureHotspot.height() / 2f);
        float         x2             = centerOnScreen.x() + (textureHotspot.width() / 2f);
        float         y2             = centerOnScreen.y() + (textureHotspot.height() / 2f);
        // Now we know where the hotspot must be on the screen

        // Project this position on 3D
        final Point3D point1 = this.gluUnProject(x1, y1, z);
        final Point3D point2 = this.gluUnProject(x2, y2, z);
        x1 = point1.x();
        y1 = point1.y();
        x2 = point2.x();
        y2 = point2.y();
        final float w  = Math.abs(x1 - x2);
        final float h  = Math.abs(y1 - y2);
        final float xx = Math.min(x1, x2) + (0.5f * w);
        final float yy = Math.min(y1, y2) + (0.5f * h);

        // We have all information, so we can draw the hotspot
        GL11.glPushMatrix();

        GL11.glLoadIdentity();
        GL11.glColor4f(red, green, blue, 1f);
        GL11.glTranslatef(xx, yy, z);
        GL11.glScalef(w, h, 1);
        this.planeFor2D.drawObject();

        GL11.glPopMatrix();
    }

    /**
     * Signal to clickInSpaceListeners that user click on nothing (space)
     *
     * @param mouseX      Mouse X
     * @param mouseY      Mouse Y
     * @param leftButton  Indicates if left mouse button is down
     * @param rightButton Indicates if right mouse button is down
     */
    void fireClickInSpace(
            final int mouseX, final int mouseY, final boolean leftButton, final boolean rightButton)
    {
        synchronized (this.clickInSpaceListeners)
        {
            this.clickInSpaceListeners.forEach(
                    (ConsumerTask<ClickInSpaceListener>) clickInSpaceListener ->
                            clickInSpaceListener.clickInSpace(mouseX, mouseY, leftButton, rightButton));
        }
    }

    /**
     * Draw a hotspot
     *
     * @param node Node witch carry hotspot
     */
    @ThreadOpenGL
    void showHotspot(final @NotNull Node node)
    {
        final Texture textureHotspot = node.textureHotspot();
        // If no hotspot texture, do nothing
        if (textureHotspot == null)
        {
            return;
        }

        // Use material for 2D
        this.prepareMaterial2D();

        // Project center node in the model view
        final Point3D center = node.center();
        this.computeModelView();
        final double cx = center.x();
        final double cy = center.y();
        final double cz = center.z();
        final double px =
                (cx * this.modelView[0]) + (cy * this.modelView[4]) + (cz * this.modelView[8]) + this.modelView[12];
        final double py =
                (cx * this.modelView[1]) + (cy * this.modelView[5]) + (cz * this.modelView[9]) + this.modelView[13];
        final double pz =
                (cx * this.modelView[2]) + (cy * this.modelView[6]) + (cz * this.modelView[10]) + this.modelView[14];

        // Project the new center in the screen
        final float   z              = (float) pz;
        final Point2D centerOnScreen = this.gluProject((float) px, (float) py, z);
        float         x1             = centerOnScreen.x() - (textureHotspot.width() / 2f);
        float         y1             = centerOnScreen.y() - (textureHotspot.height() / 2f);
        float         x2             = centerOnScreen.x() + (textureHotspot.width() / 2f);
        float         y2             = centerOnScreen.y() + (textureHotspot.height() / 2f);
        final Point3D point1         = this.gluUnProject(x1, y1, z);
        final Point3D point2         = this.gluUnProject(x2, y2, z);
        // Now we know where the hotspot must be on the screen

        // Project this position on 3D
        x1 = point1.x();
        y1 = point1.y();
        x2 = point2.x();
        y2 = point2.y();
        final float w  = Math.abs(x1 - x2) * 1000;
        final float h  = Math.abs(y1 - y2) * 1000;
        final float xx = Math.min(x1, x2) + (0.5f * w);
        final float yy = Math.min(y1, y2) + (0.5f * h);

        // We have all information, so we can draw the hotspot
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();

        textureHotspot.bind();
        GL11.glTranslatef(xx, yy, z);
        GL11.glScalef(w, h, 1);
        this.planeFor2D.drawObject();

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    /**
     * Actual absolute frame
     *
     * @return Actual absolute frame
     */
    public float absoluteFrame()
    {
        return this.absoluteFrame;
    }

    /**
     * Action manager to register action listeners and manage association key/joystick to action
     *
     * @return Action manager
     */
    public ActionManager actionManager()
    {
        return this.actionManager;
    }

    /**
     * Actual animation FPS
     *
     * @return Actual animation FPS
     */
    public int animationsFps()
    {
        return this.animationsFps;
    }

    /**
     * Change animation FPS
     *
     * @param animationsFps New animation FPS
     */
    public void animationsFps(final int animationsFps)
    {
        this.animationsFps = Math.max(1, animationsFps);
    }

    /**
     * Animation manager
     *
     * @return Animation manager
     */
    public @NotNull AnimationsManager animationsManager()
    {
        return this.animationsManager;
    }

    /**
     * Close the window.<br>
     * Can't be used later
     */
    public void close()
    {
        this.waitReady();
        this.closeWindow(this.window);
    }

    /**
     * Return detectionActivate
     *
     * @return detectionActivate
     */
    public boolean detectionActivate()
    {
        return this.detectionActivate;
    }

    /**
     * Modify detectionActivate
     *
     * @param detectionActivate New detectionActivate value
     */
    public void detectionActivate(final boolean detectionActivate)
    {
        this.detectionActivate = detectionActivate;
    }

    /**
     * Disable the UV picking
     */
    public void disablePickUV()
    {
        this.pickUVnode = null;
    }

    /**
     * Current fps
     *
     * @return Current fps
     */
    public int fps()
    {
        return this.fps;
    }

    /**
     * Indicates if FPS is shown
     *
     * @return {@code true} if FPS is shown
     */
    public boolean fpsShown()
    {
        return this.objectFPS.visible();
    }

    /**
     * Convert a number of frame to a time in millisecond .<br>
     * Warning: The result depends on current {@link #animationsFps() animationsFps}.
     * If {@link #animationsFps(int)  animationsFps} change after the call of this method the obtained result becomes obsolete.
     *
     * @param frame Number of frame
     * @return Time in animation
     */
    public int frameAnimationTotimeAnimation(int frame)
    {
        return (frame * 1000) / this.animationsFps;
    }

    /**
     * 2D manager
     *
     * @return 2D manager
     */
    public @NotNull GUI2D gui2d()
    {
        return this.gui2d;
    }

    /**
     * Window height
     *
     * @return Window height
     */
    public int height()
    {
        return this.height;
    }

    /**
     * Access to lights
     *
     * @return Lights manager
     */
    public @NotNull Lights lights()
    {
        this.waitReady();
        return this.lights;
    }

    /**
     * Last detect node.<br>
     * Beware it becomes often {@code null}.<br>
     * Prefer use listener to detected mouse on node
     *
     * @return Last detect node
     */
    public @Nullable Node nodeDetect()
    {
        return this.nodeDetect;
    }

    /**
     * Last detect 2D object<br>
     * Beware it becomes often {@code null}.<br>
     * Prefer use listener to detected mouse on 2D objects
     *
     * @return Last detect 2D object
     */
    public @Nullable Object2D object2DDetect()
    {
        return this.object2DDetect;
    }

    /**
     * Return pickUVnode
     *
     * @return pickUVnode
     */
    public @Nullable Node pickUVnode()
    {
        return this.pickUVnode;
    }

    /**
     * Modify pickUVnode
     *
     * @param pickUVnode New pickUVnode value
     */
    public void pickUVnode(final @Nullable Node pickUVnode)
    {
        this.pickUVnode = pickUVnode;
    }

    /**
     * Play animation .<br>
     * The animation is played as soon as possible
     *
     * @param animation Animation to play
     */
    public void playAnimation(final @NotNull Animation animation)
    {
        this.animationsManager.play(animation);
    }

    /**
     * Create and launch an animation loop.<br>
     * It returns the associated animation to be able stop it later with {@link #stopAnimation(Animation)}
     *
     * @param animateLoop Animation loop to play
     * @return Associate animation
     */
    public Animation playAnimation(final @NotNull AnimationLoop.AnimateLoop animateLoop)
    {
        final Animation animation = new AnimationLoop(animateLoop);
        this.playAnimation(animation);
        return animation;
    }

    /**
     * Preferences associated to this window
     *
     * @return Preferences associated to this window
     */
    public Preferences preferences()
    {
        return this.preferences;
    }

    /**
     * Register click in space listener
     *
     * @param clickInSpaceListener Listener to register
     */
    public void registerClickInSpaceListener(final @NotNull ClickInSpaceListener clickInSpaceListener)
    {
        if (clickInSpaceListener == null)
        {
            return;
        }

        synchronized (this.clickInSpaceListeners)
        {
            if (!this.clickInSpaceListeners.contains(clickInSpaceListener))
            {
                this.clickInSpaceListeners.add(clickInSpaceListener);
            }
        }
    }

    /**
     * Add texture to remove from memory list, the real remove will append in OpenGL thread
     *
     * @param texture Texture to remove
     */
    public void removeFromMemory(final @NotNull Texture texture)
    {
        Objects.requireNonNull(texture, "texture MUST NOT be null!");
        this.texturesToRemove.inQueue(texture);
    }

    /**
     * Add texture to remove from memory list, the real remove will append in OpenGL thread
     *
     * @param textureName Texture to remove
     */
    public void removeFromMemory(final @NotNull String textureName)
    {
        final Texture texture = Texture.obtainTexture(textureName);

        if (texture != null)
        {
            this.texturesToRemove.inQueue(texture);
        }
    }

    /**
     * Current rendered scene
     *
     * @return Current scene
     */
    public @NotNull Scene scene()
    {
        final Scene scene = this.newScene;

        if (scene != null)
        {
            return scene;
        }

        return this.currentScene;
    }

    /**
     * Change the current scene
     *
     * @param scene New scene to draw
     */
    public void scene(@NotNull Scene scene)
    {
        Objects.requireNonNull(scene, "scene MUST NOT be null!");
        this.newScene = scene;
    }

    /**
     * Show/hide the FPS
     *
     * @param show Indicates if show FPS
     */
    public void showFPS(boolean show)
    {
        this.objectFPS.visible(show);
    }

    /**
     * Indicates if window currently showing
     *
     * @return {@code} true if window currently showing
     */
    public boolean showing()
    {
        return this.showing.get();
    }

    /**
     * Associated sound manager for play sounds
     *
     * @return Sound manager for play sounds
     */
    public SoundManager soundManager()
    {
        this.waitReady();
        return this.soundManager;
    }

    /**
     * Stop an animation
     *
     * @param animation Animation to stop
     */
    public void stopAnimation(final @NotNull Animation animation)
    {
        this.animationsManager.stop(animation);
    }

    /**
     * Convert a time in millisecond to a number of frame.<br>
     * Warning: The result depends on current {@link #animationsFps() animationsFps}.
     * If {@link #animationsFps(int)  animationsFps} change after the call of this method the obtained result becomes obsolete.
     *
     * @param timeAnimation Animation time in milliseconds
     * @return Animation frame
     */
    public int timeAnimationToFrameAnimation(int timeAnimation)
    {
        return (timeAnimation * this.animationsFps) / 1000;
    }

    /**
     * Unregister click in space listener
     *
     * @param clickInSpaceListener Listener to register
     */
    public void unregisterClickInSpaceListener(final @NotNull ClickInSpaceListener clickInSpaceListener)
    {
        synchronized (this.clickInSpaceListeners)
        {
            this.clickInSpaceListeners.remove(clickInSpaceListener);
        }
    }

    /**
     * Window width
     *
     * @return Window width
     */
    public int width()
    {
        return this.width;
    }
}

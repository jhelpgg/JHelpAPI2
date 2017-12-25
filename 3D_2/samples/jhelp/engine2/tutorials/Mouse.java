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

package jhelp.engine2.tutorials;

import com.sun.istack.internal.NotNull;
import java.util.Locale;
import jhelp.engine2.geometry.Box;
import jhelp.engine2.render.ActionManager;
import jhelp.engine2.render.Material;
import jhelp.engine2.render.Node;
import jhelp.engine2.render.NodeWithMaterial;
import jhelp.engine2.render.Scene;
import jhelp.engine2.render.Texture;
import jhelp.engine2.render.Window3D;
import jhelp.engine2.render.event.ActionCode;
import jhelp.engine2.render.event.ClickInSpaceListener;
import jhelp.engine2.render.event.NodeListener;
import jhelp.engine2.render.event.PickUVlistener;
import jhelp.engine2.twoD.GUI2D;
import jhelp.engine2.twoD.Object2D;
import jhelp.engine2.twoD.event.Object2DListener;
import jhelp.util.debug.Debug;

/**
 * Code for "08_Mouse" tutorial
 */
public class Mouse
{
    private static Window3D window3D;
    private static Node     mainNode;

    /**
     * Listener of different mouse events
     */
    static class EventManager implements Object2DListener, NodeListener, PickUVlistener, ClickInSpaceListener
    {

        /**
         * Called when user click not in 3D object, nor 2D object
         *
         * @param mouseX      Mouse X
         * @param mouseY      Mouse Y
         * @param leftButton  Indicates if left mouse button is down
         * @param rightButton Indicates if right mouse button is down
         */
        @Override public void clickInSpace(
                final int mouseX, final int mouseY, final boolean leftButton, final boolean rightButton)
        {
            Debug.debug(" mouseX=", mouseX, " mouseY=", mouseY, " leftButton=", leftButton, " rightButton=",
                        rightButton);
        }

        /**
         * Call when mouse click on a node
         *
         * @param node        Node click
         * @param leftButton  Indicates if the left button is down
         * @param rightButton Indicates if the right button is down
         */
        @Override public void mouseClick(final Node node, final boolean leftButton, final boolean rightButton)
        {
            Debug.debug(" node=", node, " leftButton=", leftButton, " rightButton=", rightButton);
            node.pickUVlistener = this;
            Mouse.window3D.pickUVnode(node);
        }

        /**
         * Call when mouse click on a object
         *
         * @param object2D    Object under mouse
         * @param x           Mouse X
         * @param y           Mouse Y
         * @param leftButton  Indicates if the left button is down
         * @param rightButton Indicates if the right button is down
         */
        @Override public void mouseClick(
                final Object2D object2D, final int x, final int y, final boolean leftButton, final boolean rightButton)
        {
            Debug.debug(" object2D=", object2D, " x=", x, " y=", y, " leftButton=", leftButton, " rightButton=",
                        rightButton);
        }

        /**
         * Call when mouse drag on a object
         *
         * @param object2D    Object under mouse
         * @param x           Mouse X
         * @param y           Mouse Y
         * @param leftButton  Indicates if the left button is down
         * @param rightButton Indicates if the right button is down
         */
        @Override public void mouseDrag(
                final Object2D object2D, final int x, final int y, final boolean leftButton, final boolean rightButton)
        {
            Debug.debug(" object2D=", object2D, " x=", x, " y=", y, " leftButton=", leftButton, " rightButton=",
                        rightButton);
        }

        /**
         * Call when mouse enter on a object
         *
         * @param object2D Object enter
         * @param x        Mouse X
         * @param y        Mouse Y
         */
        @Override public void mouseEnter(final Object2D object2D, final int x, final int y)
        {
            Debug.debug(" object2D=", object2D, " x=", x, " y=", y);
        }

        /**
         * Call when mouse exit on a object
         *
         * @param object2D Object exit
         * @param x        Mouse X
         * @param y        Mouse Y
         */
        @Override public void mouseExit(final Object2D object2D, final int x, final int y)
        {
            Debug.debug(" object2D=", object2D, " x=", x, " y=", y);
        }

        /**
         * Call when mouse move on a object
         *
         * @param object2D Object under mouse
         * @param x        Mouse X
         * @param y        Mouse Y
         */
        @Override public void mouseMove(final Object2D object2D, final int x, final int y)
        {
            Debug.debug(" object2D=", object2D, " x=", x, " y=", y);
        }

        /**
         * Call when mouse enter on a node
         *
         * @param node Node enter
         */
        @Override public void mouseEnter(final Node node)
        {
            Debug.debug(" node=", node);
        }

        /**
         * Call when mouse exit on a node
         *
         * @param node Node exit
         */
        @Override public void mouseExit(final Node node)
        {
            Debug.debug(" node=", node);
        }

        /**
         * Call when UV is pick
         *
         * @param u    U pick : [0, 255]
         * @param v    V pick : [0, 255]
         * @param node Node pick
         */
        @Override public void pickUV(final int u, final int v, final Node node)
        {
            Mouse.window3D.disablePickUV();
            node.pickUVlistener = null;
            Debug.information(node.nodeName, " (", u, "<>", u / 255f, " ; ", v, "<>", v / 255f, ")");

            if (node instanceof NodeWithMaterial)
            {
                NodeWithMaterial nodeWithMaterial = (NodeWithMaterial) node;
                Material         material         = nodeWithMaterial.material();

                if (material != null)
                {
                    Texture texture = material.textureDiffuse();

                    if (texture != null)
                    {
                        float width  = texture.width();
                        float height = texture.height();
                        int   x      = (int) ((u * width) / 255f);
                        int   y      = (int) ((v * height) / 255f);
                        Debug.information("On texture: x=", x, "~", (u * width) / 255f,
                                          " , y=", y, "~", (v * height) / 255f);
                    }
                }
            }
        }
    }

    /**
     * Launch the sample
     *
     * @param args Ignored arguments
     */
    public static void main(String[] args)
    {
        Locale.setDefault(Locale.ENGLISH);
        //Step 1: Create the window for show the 3D
        Mouse.window3D = Window3D.createFullWidow("Mouse detection");

        //Step 2: Obtain the 3D scene
        Scene scene = Mouse.window3D.scene();

        EventManager eventManager = new EventManager();

        //Step 3: Create the box
        Box box = new Box();
        box.nodeName = "BOX";
        Mouse.mainNode = box;
        box.addNodeListener(eventManager);

        //Step 4: Place the box in the scene
        scene.add(box);

        //Step 5: Place the box in visible position
        box.position(0, 0, -5);

        //Step 6: Rotate the box
        box.angleX(12);
        box.angleY(25);

        //Create material
        Material material = new Material("Rock");

        try
        {
            //Load the texture
            Texture texture = new Texture("Rock", Texture.REFERENCE_RESOURCES,
                                          Mouse.class.getResourceAsStream("TextureRock.png"));

            //Define material texture:
            material.textureDiffuse(texture);
        }
        catch (Exception exception)
        {
            Debug.exception(exception, "Failed to load the texture");
        }

        //Apply material
        box.material(material);

        //Get action manager instance
        ActionManager actionManager = Mouse.window3D.actionManager();

        //Make the ACTION_BUTTON_1 consumable to avoid repetition
        actionManager.consumable(ActionCode.ACTION_BUTTON_1, true);

        //Register an action events listener
        actionManager.registerActionListener(Mouse::actionEvent);

        // **************
        // *** GUI 2D ***
        // **************

        //Obtain object for manage 2D
        GUI2D gui2D = Mouse.window3D.gui2d();

        //Get window 3D size
        int width  = Mouse.window3D.width();
        int height = Mouse.window3D.height();

        // Create a 2D object and put it over the 3D
        Object2D over = new Object2D(64, 64, 512, 512);
        gui2D.addOver3D(over);

        over.registerObject2DListener(eventManager);

        // Create a 2D object and put it under the 3D
        Object2D under = new Object2D((width - 512) >> 1, (height - 512) >> 1, 512, 512);
        gui2D.addUnder3D(under);

        under.registerObject2DListener(eventManager);

        try
        {
            //Load textures and apply them to 2D objects
            Texture texture = new Texture("Floor", Texture.REFERENCE_RESOURCES,
                                          Mouse.class.getResourceAsStream("floor.jpg"));
            over.texture(texture);

            texture = new Texture("Tile", Texture.REFERENCE_RESOURCES,
                                  Mouse.class.getResourceAsStream("tile1.jpg"));
            under.texture(texture);
        }
        catch (Exception exception)
        {
            Debug.exception(exception, "Failed to load textures");
        }

        Mouse.window3D.registerClickInSpaceListener(eventManager);
    }

    /**
     * React to current active actions
     *
     * @param actionCodes Current actives actions
     */
    private static void actionEvent(@NotNull ActionCode... actionCodes)
    {
        for (ActionCode actionCode : actionCodes)
        {
            switch (actionCode)
            {
                case ACTION_UP:
                    Mouse.mainNode.translate(0, 0.1f, 0);
                    break;
                case ACTION_DOWN:
                    Mouse.mainNode.translate(0, -0.1f, 0);
                    break;
                case ACTION_LEFT:
                    Mouse.mainNode.translate(-0.1f, 0f, 0);
                    break;
                case ACTION_RIGHT:
                    Mouse.mainNode.translate(0.1f, 0f, 0);
                    break;
                case ACTION_BUTTON_5:
                    Mouse.mainNode.translate(0, 0, 0.1f);
                    break;
                case ACTION_BUTTON_6:
                    Mouse.mainNode.translate(0, 0, -0.1f);
                    break;

                case ACTION_EXIT:
                    Mouse.window3D.close();
                    break;

                case ACTION_BUTTON_1:
                    Mouse.window3D.actionManager()
                                  .captureKeyCode()
                                  .andConsume(keyCode -> Debug.information("keyCode=", keyCode));
            }
        }
    }
}

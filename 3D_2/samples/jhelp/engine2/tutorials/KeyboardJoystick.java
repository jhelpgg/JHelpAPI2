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
import jhelp.engine2.geometry.Box;
import jhelp.engine2.render.ActionManager;
import jhelp.engine2.render.Material;
import jhelp.engine2.render.Node;
import jhelp.engine2.render.Scene;
import jhelp.engine2.render.Texture;
import jhelp.engine2.render.Window3D;
import jhelp.engine2.render.event.ActionCode;
import jhelp.util.debug.Debug;

/**
 * Code for "05_KeyboardJoystick" tutorial
 */
public class KeyboardJoystick
{
    private static Window3D window3D;
    private static Node     mainNode;

    /**
     * Launch the sample
     *
     * @param args Ignored arguments
     */
    public static void main(String[] args)
    {
        //Step 1: Create the window for show the 3D
        KeyboardJoystick.window3D = Window3D.createFullWidow("Keyboard and Joystick");

        //Step 2: Obtain the 3D scene
        Scene scene = KeyboardJoystick.window3D.scene();

        //Step 3: Create the box
        Box box = new Box();
        KeyboardJoystick.mainNode = box;

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
                                          KeyboardJoystick.class.getResourceAsStream("TextureRock.png"));

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
        ActionManager actionManager = KeyboardJoystick.window3D.actionManager();

        //Make the ACTION_BUTTON_1 consumable to avoid repetition
        actionManager.consumable(ActionCode.ACTION_BUTTON_1, true);

        //Register an action events listener
        actionManager.registerActionListener(KeyboardJoystick::actionEvent);
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
                    KeyboardJoystick.mainNode.translate(0, 0.1f, 0);
                    break;
                case ACTION_DOWN:
                    KeyboardJoystick.mainNode.translate(0, -0.1f, 0);
                    break;
                case ACTION_LEFT:
                    KeyboardJoystick.mainNode.translate(-0.1f, 0f, 0);
                    break;
                case ACTION_RIGHT:
                    KeyboardJoystick.mainNode.translate(0.1f, 0f, 0);
                    break;
                case ACTION_BUTTON_5:
                    KeyboardJoystick.mainNode.translate(0, 0, 0.1f);
                    break;
                case ACTION_BUTTON_6:
                    KeyboardJoystick.mainNode.translate(0, 0, -0.1f);
                    break;

                case ACTION_EXIT:
                    KeyboardJoystick.window3D.close();
                    break;

                case ACTION_BUTTON_1:
                    KeyboardJoystick.window3D.actionManager()
                                             .captureKeyCode()
                                             .andConsume(keyCode -> Debug.information("keyCode=", keyCode));
            }
        }
    }
}

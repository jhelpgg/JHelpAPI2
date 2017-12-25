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
import jhelp.engine2.render.Scene;
import jhelp.engine2.render.Texture;
import jhelp.engine2.render.Window3D;
import jhelp.engine2.render.event.ActionCode;
import jhelp.engine2.twoD.Button2D;
import jhelp.engine2.twoD.GUI2D;
import jhelp.engine2.twoD.Object2D;
import jhelp.engine2.twoD.OptionPane2D;
import jhelp.engine2.twoD.OptionPaneButtons;
import jhelp.util.debug.Debug;
import jhelp.util.gui.JHelpImage;

/**
 * Code for "07_UI2D" tutorial
 */
public class UI2D
{
    private static final int BUTTON_ID      = 73;
    private static final int OPTION_PANE_ID = 42;
    private static Window3D window3D;
    private static Node     mainNode;

    /**
     * Launch the sample
     *
     * @param args Ignored arguments
     */
    public static void main(String[] args)
    {
        Locale.setDefault(Locale.ENGLISH);
        //Step 1: Create the window for show the 3D
        UI2D.window3D = Window3D.createFullWidow("User Interface 2D");

        //Step 2: Obtain the 3D scene
        Scene scene = UI2D.window3D.scene();

        //Step 3: Create the box
        Box box = new Box();
        UI2D.mainNode = box;

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
                                          UI2D.class.getResourceAsStream("TextureRock.png"));

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
        ActionManager actionManager = UI2D.window3D.actionManager();

        //Make the ACTION_BUTTON_1 consumable to avoid repetition
        actionManager.consumable(ActionCode.ACTION_BUTTON_1, true);

        //Register an action events listener
        actionManager.registerActionListener(UI2D::actionEvent);

        // **************
        // *** GUI 2D ***
        // **************

        //Obtain object for manage 2D
        GUI2D gui2D = UI2D.window3D.gui2d();

        //Get window 3D size
        int width  = UI2D.window3D.width();
        int height = UI2D.window3D.height();

        // Create a 2D object and put it over the 3D
        Object2D over = new Object2D(64, 64, 512, 512);
        gui2D.addOver3D(over);

        // Create a 2D object and put it under the 3D
        Object2D under = new Object2D((width - 1024) >> 1, (height - 1024) >> 1, 1024, 1024);
        gui2D.addUnder3D(under);

        try
        {
            //Load textures and apply them to 2D objects
            Texture texture = new Texture("Floor", Texture.REFERENCE_RESOURCES,
                                          UI2D.class.getResourceAsStream("floor.jpg"));
            over.texture(texture);

            texture = new Texture("Tile", Texture.REFERENCE_RESOURCES,
                                  UI2D.class.getResourceAsStream("tile1.jpg"));
            under.texture(texture);
        }
        catch (Exception exception)
        {
            Debug.exception(exception, "Failed to load textures");
        }

        // Create a button:
        try
        {
            JHelpImage buttonNormal = JHelpImage.loadImage(UI2D.class.getResourceAsStream("Button_normal.png"));
            JHelpImage buttonOver   = JHelpImage.loadImage(UI2D.class.getResourceAsStream("Button_over.png"));
            JHelpImage buttonClick  = JHelpImage.loadImage(UI2D.class.getResourceAsStream("Button_click.png"));
            Button2D button2D = new Button2D(UI2D.BUTTON_ID,
                                             width - 256 - 32, 32, 256, 64,
                                             buttonNormal, buttonOver, buttonClick);
            gui2D.addOver3D(button2D);
        }
        catch (Exception exception)
        {
            Debug.exception(exception, "Failed to create and add the button");
        }

        // Option pane

        //Add an option pane
        OptionPane2D optionPane2D = new OptionPane2D(UI2D.window3D,
                                                     (width - 512) >> 1, (height - 256) >> 1, 512, 256,
                                                     UI2D::optionPaneClicked);

        //Show option pane dialog
        optionPane2D.showOptionPane(
                "Example of option pane.\nOption pane can have several lines.\nEach line is separate with the character \\n.\n\nExample of a long line, to show how long line are treated. That is to say, long line are automatically separate in several lines. That's a cool stuff for long line treatment. Ok it seems this line is enough long now.",
                OptionPaneButtons.YES_NO_CANCEL,
                UI2D.OPTION_PANE_ID);
    }

    /**
     * Called on option pane button click
     *
     * @param optionPaneID Option pane ID
     * @param button       Button clicked
     */
    private static void optionPaneClicked(int optionPaneID, @NotNull OptionPaneButtons button)
    {
        Debug.information("optionPaneID=", optionPaneID, " button=", button);
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
                    UI2D.mainNode.translate(0, 0.1f, 0);
                    break;
                case ACTION_DOWN:
                    UI2D.mainNode.translate(0, -0.1f, 0);
                    break;
                case ACTION_LEFT:
                    UI2D.mainNode.translate(-0.1f, 0f, 0);
                    break;
                case ACTION_RIGHT:
                    UI2D.mainNode.translate(0.1f, 0f, 0);
                    break;
                case ACTION_BUTTON_5:
                    UI2D.mainNode.translate(0, 0, 0.1f);
                    break;
                case ACTION_BUTTON_6:
                    UI2D.mainNode.translate(0, 0, -0.1f);
                    break;

                case ACTION_EXIT:
                    UI2D.window3D.close();
                    break;

                case ACTION_BUTTON_1:
                    UI2D.window3D.actionManager()
                                 .captureKeyCode()
                                 .andConsume(keyCode -> Debug.information("keyCode=", keyCode));
            }
        }
    }
}

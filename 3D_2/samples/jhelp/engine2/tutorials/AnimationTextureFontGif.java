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

import java.awt.Color;
import java.io.File;
import jhelp.engine2.geometry.Box;
import jhelp.engine2.render.Material;
import jhelp.engine2.render.Scene;
import jhelp.engine2.render.TextureFontGif;
import jhelp.engine2.render.Window3D;
import jhelp.util.debug.Debug;
import jhelp.util.io.UtilIO;

/**
 * Sample for tutorial 09_Animations: texture GIF
 */
public class AnimationTextureFontGif
{
    /**
     * Launch the sample
     *
     * @param args Ignored arguments
     */
    public static void main(String[] args)
    {
        //Step 1: Create the window for show the 3D
        Window3D window3D = Window3D.createSizedWindow(800, 600, "09_Animations texture GIF", true);

        //Step 2: Obtain the 3D scene
        Scene scene = window3D.scene();
        scene.background(Color.CYAN);

        //Step 3: Create the box
        Box box = new Box();

        //Step 4: Place the box in the scene
        scene.add(box);

        //Step 5: Place the box in visible position
        box.position(0, 0, -3);

        //Step 6: Rotate the box
        box.angleX(12);
        box.angleY(25);

        //Create material
        Material material = new Material("Animated");
        material.twoSided(true);


        try
        {
            TextureFontGif texture = new TextureFontGif("bells", "Hello\nWorld");
            window3D.playAnimation(texture);
            //Define material texture:
            material.textureDiffuse(texture);
        }
        catch (Exception exception)
        {
            Debug.exception(exception, "Failed to load the texture");
        }

        //Apply material
        box.material(material);

        File directory = new File("/home/jhelp/IdeaProjects/ia/3D_2/doc/tutorials/images/animTextureFontGif");
        UtilIO.delete(directory);
        UtilIO.createDirectory(directory);
        window3D.screenShots(100, directory).andConsume(directory1 -> Debug.mark("Capture Done"));
    }
}

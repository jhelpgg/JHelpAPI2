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

import java.io.File;
import jhelp.engine2.geometry.Box;
import jhelp.engine2.render.Material;
import jhelp.engine2.render.Texture;
import jhelp.engine2.render.Window3D;
import jhelp.util.debug.Debug;
import jhelp.util.io.UtilIO;

/**
 * Animation loop sample
 */
public class AnimationAnimationLoop
{
    /**
     * Launch the animation loop sample
     *
     * @param args Ignored
     */
    public static void main(String[] args)
    {
        Window3D window3D = Window3D.createSizedWindow(800, 600, "Animation loop", true);
        Box      box      = new Box();
        box.position(0, 0, -5);
        box.angleX(12);
        window3D.scene().add(box);
        Material material = new Material("Rock");

        try
        {
            //Load the texture
            Texture texture = new Texture("Rock", Texture.REFERENCE_RESOURCES,
                                          MaterialTexture.class.getResourceAsStream("TextureRock.png"));

            //Define material texture:
            material.textureDiffuse(texture);
        }
        catch (Exception exception)
        {
            Debug.exception(exception, "Failed to load the texture");
        }

        //Apply material
        box.material(material);

        File directory = new File("/home/jhelp/IdeaProjects/ia/3D_2/doc/tutorials/images/animAnimationLoop");
        UtilIO.delete(directory);
        UtilIO.createDirectory(directory);
        window3D.screenShots(100, directory).andConsume(directory1 -> Debug.mark("Capture Done"));

        window3D.playAnimation(frame ->
                               {
                                   box.angleY(frame);
                                   return true;
                               });
    }
}

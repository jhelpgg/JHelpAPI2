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

import jhelp.engine2.geometry.Box;
import jhelp.engine2.render.CubeMap;
import jhelp.engine2.render.Material;
import jhelp.engine2.render.Scene;
import jhelp.engine2.render.Texture;
import jhelp.engine2.render.Window3D;
import jhelp.util.debug.Debug;

/**
 * Code for "03_Materials" tutorial exercise
 */
public class MaterialTextureCubeMap
{
    /**
     * Launch the sample
     *
     * @param args Ignored arguments
     */
    public static void main(String[] args)
    {
        //Step 1: Create the window for show the 3D
        Window3D window3D = Window3D.createSizedWindow(800, 600, "03_Materials cube map reflexion", true);

        //Step 2: Obtain the 3D scene
        Scene scene = window3D.scene();

        //Step 3: Create the box
        Box box = new Box();

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
                                          MaterialTextureCubeMap.class.getResourceAsStream("TextureRock.png"));

            //Define material texture:
            material.textureDiffuse(texture);

            //Load texture for spherical reflexion
            texture = new Texture("Emerald", Texture.REFERENCE_RESOURCES,
                                  MaterialTextureCubeMap.class.getResourceAsStream("emerald.jpg"));

            //Create cube map
            CubeMap cubeMap = new CubeMap();
            cubeMap.crossTexture(texture);
            material.cubeMap(cubeMap);

            //Specify cube map reflexion influence
            material.cubeMapRate(0.5f);
        }
        catch (Exception exception)
        {
            Debug.exception(exception, "Failed to load the texture");
        }

        //Apply material
        box.material(material);

        //Animate the box to able see reflexion effect.
        //More details in chapter for animations
        window3D.playAnimation(frame ->
                               {
                                   //On each animation refresh we rotate the box
                                   box.rotateAngleY(1f);
                                   //We continue the animation (Animation is not finished)
                                   return true;
                               });
    }
}

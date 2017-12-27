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
import jhelp.engine2.animation.AnimationTexture;
import jhelp.engine2.geometry.Box;
import jhelp.engine2.render.Material;
import jhelp.engine2.render.ObjectClone;
import jhelp.engine2.render.Scene;
import jhelp.engine2.render.Texture;
import jhelp.engine2.render.TextureInterpolator;
import jhelp.engine2.render.Window3D;
import jhelp.util.debug.Debug;
import jhelp.util.gui.JHelpImage;
import jhelp.util.io.UtilIO;

/**
 * Sample for tutorial 09_Animations: texture transtion
 */
public class AnimationTextureTransition
{
    /**
     * Launch the sample
     *
     * @param args Ignored arguments
     */
    public static void main(String[] args)
    {
        //Step 1: Create the window for show the 3D
        Window3D window3D = Window3D.createSizedWindow(800, 600, "09_Animations texture transition", true);

        //Step 2: Obtain the 3D scene
        Scene scene = window3D.scene();

        //Step 3: Create the box
        Box boxBorder = new Box();

        //Step 4: Place the box in the scene
        scene.add(boxBorder);

        //Step 5: Place the box in visible position
        boxBorder.position(-1.25f, 1.25f, -5);

        //Step 6: Rotate the box
        boxBorder.angleX(12);
        boxBorder.angleY(25);

        ObjectClone boxCorner = new ObjectClone(boxBorder);
        scene.add(boxCorner);
        boxCorner.position(0f, 0, -5);
        boxCorner.angleX(12);
        boxCorner.angleY(25);

        ObjectClone boxMelted = new ObjectClone(boxBorder);
        scene.add(boxMelted);
        boxMelted.position(1.25f, 1.25f, -5);
        boxMelted.angleX(12);
        boxMelted.angleY(25);

        ObjectClone boxRandom = new ObjectClone(boxBorder);
        scene.add(boxRandom);
        boxRandom.position(-1.25f, -1.25f, -5);
        boxRandom.angleX(12);
        boxRandom.angleY(25);

        ObjectClone boxReplacement = new ObjectClone(boxBorder);
        scene.add(boxReplacement);
        boxReplacement.position(1.25f, -1.25f, -5);
        boxReplacement.angleX(12);
        boxReplacement.angleY(25);

        try
        {
            //Load the texture
            JHelpImage image = JHelpImage.loadImageThumb(
                    AnimationTextureTransition.class.getResourceAsStream("TextureRock.png"),
                    256, 256);
            Texture textureRock = new Texture("Rock", image);
            image = JHelpImage.loadImageThumb(
                    AnimationTextureTransition.class.getResourceAsStream("floor.jpg"),
                    256, 256);
            Texture textureFloor = new Texture("Floor", image);

            //Create material
            Material material = new Material("border");
            AnimationTexture animationTexture = new AnimationTexture(50,
                                                                     textureRock, textureFloor,
                                                                     true, Integer.MAX_VALUE,
                                                                     TextureInterpolator.InterpolationType.BORDER);
            //Define material texture:
            material.textureDiffuse(animationTexture.getInterpolatedTexture());
            window3D.playAnimation(animationTexture);
            //Apply material
            boxBorder.material(material);

            material = new Material("corner");
            animationTexture = new AnimationTexture(50,
                                                    textureRock, textureFloor,
                                                    true, Integer.MAX_VALUE,
                                                    TextureInterpolator.InterpolationType.CORNER);
            //Define material texture:
            material.textureDiffuse(animationTexture.getInterpolatedTexture());
            window3D.playAnimation(animationTexture);
            //Apply material
            boxCorner.material(material);

            material = new Material("melted");
            animationTexture = new AnimationTexture(50,
                                                    textureRock, textureFloor,
                                                    true, Integer.MAX_VALUE,
                                                    TextureInterpolator.InterpolationType.MELTED);
            //Define material texture:
            material.textureDiffuse(animationTexture.getInterpolatedTexture());
            window3D.playAnimation(animationTexture);
            //Apply material
            boxMelted.material(material);

            material = new Material("random");
            animationTexture = new AnimationTexture(50,
                                                    textureRock, textureFloor,
                                                    true, Integer.MAX_VALUE,
                                                    TextureInterpolator.InterpolationType.RANDOM);
            //Define material texture:
            material.textureDiffuse(animationTexture.getInterpolatedTexture());
            window3D.playAnimation(animationTexture);
            //Apply material
            boxRandom.material(material);

            material = new Material("replacement");
            animationTexture = new AnimationTexture(50,
                                                    textureRock, textureFloor,
                                                    true, Integer.MAX_VALUE,
                                                    TextureInterpolator.InterpolationType.REPLACEMENT);
            //Define material texture:
            material.textureDiffuse(animationTexture.getInterpolatedTexture());
            window3D.playAnimation(animationTexture);
            //Apply material
            boxReplacement.material(material);
        }
        catch (Exception exception)
        {
            Debug.exception(exception, "Failed to load the texture");
        }

        File directory = new File("/home/jhelp/IdeaProjects/ia/3D_2/doc/tutorials/images/animTextureTransition");
        UtilIO.delete(directory);
        UtilIO.createDirectory(directory);
        window3D.screenShots(100, directory).andConsume(directory1 -> Debug.mark("Capture Done"));
    }
}

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
import jhelp.engine2.animation.AnimationPositionNode;
import jhelp.engine2.animation.AnimationPositionObject2D;
import jhelp.engine2.geometry.Box;
import jhelp.engine2.render.Material;
import jhelp.engine2.render.Texture;
import jhelp.engine2.render.Window3D;
import jhelp.engine2.twoD.Object2D;
import jhelp.engine2.util.PositionNode;
import jhelp.engine2.util.PositionObject2D;
import jhelp.util.debug.Debug;
import jhelp.util.io.UtilIO;

/**
 * Animation loop sample
 */
public class AnimationAnimationKeyFrame
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

        Object2D object2D = new Object2D(32, 32, 128, 128);

        try
        {
            //Load the texture
            Texture texture = new Texture("Floor", Texture.REFERENCE_RESOURCES,
                                          MaterialTexture.class.getResourceAsStream("floor.jpg"));

            object2D.texture(texture);
            window3D.gui2d().addOver3D(object2D);
        }
        catch (Exception exception)
        {
            Debug.exception(exception, "Failed to load the texture");
        }

        AnimationPositionNode animationPositionNode = new AnimationPositionNode(box);
        animationPositionNode.addFrame(10, new PositionNode(0.2f, 0.3f, -5f, 12, 1, 0, 0.8f, 1, 1.2f));
        animationPositionNode.addFrame(50, new PositionNode(-0.9f, -0.3f, -5f, 12, 0, 0, 0.4f, 1, 2f));
        animationPositionNode.addFrame(100, new PositionNode(0f, 0f, -5f, 12, 0, 0, 1, 1, 1));

        AnimationPositionObject2D animationPositionObject2D = new AnimationPositionObject2D(object2D);
        animationPositionObject2D.addFrame(10, new PositionObject2D(400, 300, 75, 75));
        animationPositionObject2D.addFrame(50, new PositionObject2D(555, 111, 36, 36));
        animationPositionObject2D.addFrame(100, new PositionObject2D(32, 32, 128, 128));

        File directory = new File("/home/jhelp/IdeaProjects/ia/3D_2/doc/tutorials/images/animAnimationKeyFrame");
        UtilIO.delete(directory);
        UtilIO.createDirectory(directory);
        window3D.screenShots(100, directory).andConsume(directory1 -> Debug.mark("Capture Done"));

        window3D.playAnimation(animationPositionNode);
        window3D.playAnimation(animationPositionObject2D);
    }
}

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
import jhelp.engine2.geometry.Revolution;
import jhelp.engine2.render.Color4f;
import jhelp.engine2.render.Material;
import jhelp.engine2.render.Node;
import jhelp.engine2.render.ObjectClone;
import jhelp.engine2.render.Point2D;
import jhelp.engine2.render.Scene;
import jhelp.engine2.render.Texture;
import jhelp.engine2.render.Window3D;
import jhelp.util.debug.Debug;

/**
 * Show a "bicycle" to illustrate scene node hierarchy organization
 */
public class Bicycle
{
    /**
     * Launch the sample
     *
     * @param args Ignored arguments
     */
    public static void main(String[] args)
    {
        //Create the window for show the 3D
        Window3D window3D = Window3D.createSizedWindow(800, 600, "Bicycle", true);

        //Create material for wheels
        Material materialWheel = new Material("Wheel");

        try
        {
            //Load the texture
            Texture texture = new Texture("Rock", Texture.REFERENCE_RESOURCES,
                                          MaterialTexture.class.getResourceAsStream("TextureRock.png"));

            //Define material texture:
            materialWheel.textureDiffuse(texture);

            //Load texture for spherical reflexion
            texture = new Texture("Emerald", Texture.REFERENCE_RESOURCES,
                                  MaterialTextureSpheric.class.getResourceAsStream("emerald.jpg"));
            //Define the spherical texture
            materialWheel.textureSpheric(texture);
            //Specify spherical reflexion influence
            materialWheel.sphericRate(0.25f);
        }
        catch (Exception exception)
        {
            Debug.exception(exception, "Failed to load the texture");
        }

        //Create material for body
        Material materialBody = new Material("Body");
        materialBody.colorDiffuse(Color4f.DARK_RED);

        //Obtain the 3D scene
        Scene scene = window3D.scene();

        //Node parent of bicycle
        Node bicycle = new Node();

        // First wheel
        //Wheel is a torus: a circle turn around Y axes, that's why we choose a revolution
        Revolution wheel1 = new Revolution(360f, 16, 32, 1f);
        //Add one semicircle
        wheel1.appendQuad(new Point2D(0.25f, 0f),
                          new Point2D(0.375f, 0.25f),
                          new Point2D(0.5f, 0f));
        //Add other semicircle
        wheel1.appendQuad(new Point2D(0.5f, 0f),
                          new Point2D(0.375f, -0.25f),
                          new Point2D(0.25f, 0f));
        //Now compute the rotation around Y axis
        wheel1.refreshRevolution();
        //Position the wheel
        wheel1.angleX(90);
        wheel1.position(-1, 0, 0);
        //Apply material
        wheel1.material(materialWheel);

        //Add wheel to the bicycle
        bicycle.addChild(wheel1);

        //Second wheel
        //Since second wheel have same shape as first one, we just clone the first wheel
        ObjectClone wheel2 = new ObjectClone(wheel1);
        //Position the wheel
        wheel2.angleX(90);
        wheel2.position(1, 0, 0);
        //Apply material
        wheel2.material(materialWheel);

        //Add wheel to the bicycle
        bicycle.addChild(wheel2);

        //Body
        Box body = new Box();
        body.setScale(2, 0.25f, 0.25f);
        //Apply material
        body.material(materialBody);

        //Add body to the bicycle
        bicycle.addChild(body);

        //Position the "bicycle"
        bicycle.position(0, 0, -5);
        //Add bicycle to scene
        scene.add(bicycle);

        //Animate wheels and bicycle nodes
        window3D.playAnimation(frame ->
                               {
                                   //On each animation refresh we rotate wheels and entire bicycle
                                   wheel1.angleY(frame * 2f);
                                   wheel2.angleY(frame * 2f);
                                   bicycle.angleY(frame * 0.5f);
                                   //We continue the animation (Animation is not finished)
                                   return true;
                               });
    }
}

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

import jhelp.engine2.geometry.Revolution;
import jhelp.engine2.render.Material;
import jhelp.engine2.render.NodeWithMaterial;
import jhelp.engine2.render.Point2D;
import jhelp.engine2.render.Scene;
import jhelp.engine2.render.Texture;
import jhelp.engine2.render.Window3D;
import jhelp.util.debug.Debug;

/**
 * Code for Revolution sample
 */
public class GeometryRevolution
{
    /**
     * Launch the sample
     *
     * @param args Ignored arguments
     */
    public static void main(String[] args)
    {
        Window3D window3D = Window3D.createSizedWindow(800, 600, "Revolution", true);
        Scene    scene    = window3D.scene();

        Revolution revolution = new Revolution();
        revolution.appendLine(new Point2D(0, -0.5f), new Point2D(0.3f, -0.5f));
        revolution.appendLine(new Point2D(0.3f, -0.5f), new Point2D(0.3f, 0.25f));
        revolution.appendLine(new Point2D(0.3f, 0.25f), new Point2D(0.1f, 0.4f));
        revolution.appendLine(new Point2D(0.1f, 0.4f), new Point2D(0.1f, 0.5f));
        revolution.refreshRevolution();
        revolution.twoSidedState(NodeWithMaterial.TwoSidedState.FORCE_TWO_SIDE);

        scene.add(revolution);
        revolution.position(0, 0, -2);
        revolution.angleX(12);

        //Create material
        Material material = new Material("Rock");

        try
        {
            //Load the texture
            Texture texture = new Texture("Rock", Texture.REFERENCE_RESOURCES,
                                          GeometryRevolution.class.getResourceAsStream("TextureRock.png"));

            //Define material texture:
            material.textureDiffuse(texture);
        }
        catch (Exception exception)
        {
            Debug.exception(exception, "Failed to load the texture");
        }

        //Apply material
        revolution.material(material);

        window3D.playAnimation(frame ->
                               {
                                   revolution.angleY(frame);
                                   return true;
                               });
    }
}

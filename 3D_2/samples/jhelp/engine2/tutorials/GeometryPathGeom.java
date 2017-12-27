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

import jhelp.engine2.geometry.PathGeom;
import jhelp.engine2.render.Material;
import jhelp.engine2.render.NodeWithMaterial;
import jhelp.engine2.render.Point2D;
import jhelp.engine2.render.Scene;
import jhelp.engine2.render.Texture;
import jhelp.engine2.render.Window3D;
import jhelp.util.debug.Debug;

/**
 * Code for PathGeom sample
 */
public class GeometryPathGeom
{
    /**
     * Launch the sample
     *
     * @param args Ignored arguments
     */
    public static void main(String[] args)
    {
        Window3D window3D = Window3D.createSizedWindow(800, 600, "Path geom", true);
        Scene    scene    = window3D.scene();

        PathGeom pathGeom = new PathGeom();

        pathGeom.appendQuadU(new Point2D(-0.5f, 0.5f), new Point2D(0, 0.75f), new Point2D(0.5f, 0.5f));
        pathGeom.appendQuadU(new Point2D(0.5f, 0.5f), new Point2D(0.75f, 0f), new Point2D(0.5f, -0.5f));
        pathGeom.appendQuadU(new Point2D(0.5f, -0.5f), new Point2D(0, -0.75f), new Point2D(-0.5f, -0.5f));
        pathGeom.appendQuadU(new Point2D(-0.5f, -0.5f), new Point2D(-0.75f, 0), new Point2D(-0.5f, 0.5f));

        pathGeom.appendQuadV(new Point2D(-0.5f, 0.5f), new Point2D(0, 0.75f), new Point2D(0.5f, 0.5f));
        pathGeom.appendQuadV(new Point2D(0.5f, 0.5f), new Point2D(0.75f, 0f), new Point2D(0.5f, -0.5f));
        pathGeom.appendQuadV(new Point2D(0.5f, -0.5f), new Point2D(0, -0.75f), new Point2D(-0.5f, -0.5f));

        pathGeom.refreshJoinedPath(1, false, true);
        pathGeom.twoSidedState(NodeWithMaterial.TwoSidedState.FORCE_TWO_SIDE);

        scene.add(pathGeom);
        pathGeom.position(0, 0, -5);

        //Create material
        Material material = new Material("Rock");

        try
        {
            //Load the texture
            Texture texture = new Texture("Rock", Texture.REFERENCE_RESOURCES,
                                          GeometryPathGeom.class.getResourceAsStream("TextureRock.png"));

            //Define material texture:
            material.textureDiffuse(texture);
        }
        catch (Exception exception)
        {
            Debug.exception(exception, "Failed to load the texture");
        }

        //Apply material
        pathGeom.material(material);

        window3D.playAnimation(frame ->
                               {
                                   pathGeom.angleY(frame);
                                   return true;
                               });
    }
}

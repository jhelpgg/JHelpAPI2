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
import jhelp.engine2.render.Color4f;
import jhelp.engine2.render.Material;
import jhelp.engine2.render.Scene;
import jhelp.engine2.render.Window3D;

/**
 * Code for "03_Materials" tutorial for red cube
 */
public class MaterialRedCube
{
    /**
     * Launch the sample
     *
     * @param args Ignored arguments
     */
    public static void main(String[] args)
    {
        //Step 1: Create the window for show the 3D
        Window3D window3D = Window3D.createSizedWindow(800, 600, "03_Materials red cube", true);

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
        Material material = new Material("Red");
        //Use red color
        material.colorDiffuse(Color4f.DARK_RED);
        //Apply material
        box.material(material);
    }
}

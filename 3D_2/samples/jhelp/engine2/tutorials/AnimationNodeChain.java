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
import java.io.File;
import jhelp.engine2.geometry.Sphere;
import jhelp.engine2.render.ActionManager;
import jhelp.engine2.render.Color4f;
import jhelp.engine2.render.Material;
import jhelp.engine2.render.Node;
import jhelp.engine2.render.NodeChain;
import jhelp.engine2.render.NodeWithMaterial;
import jhelp.engine2.render.ObjectClone;
import jhelp.engine2.render.Scene;
import jhelp.engine2.render.Window3D;
import jhelp.engine2.render.event.ActionCode;
import jhelp.util.debug.Debug;
import jhelp.util.io.UtilIO;

/**
 * Animation loop sample
 */
public class AnimationNodeChain
{
    private static Node     mainNode;
    private static Window3D window3D;

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
                    AnimationNodeChain.mainNode.translate(0, 0.01f, 0);
                    break;
                case ACTION_DOWN:
                    AnimationNodeChain.mainNode.translate(0, -0.01f, 0);
                    break;
                case ACTION_LEFT:
                    AnimationNodeChain.mainNode.translate(-0.01f, 0f, 0);
                    break;
                case ACTION_RIGHT:
                    AnimationNodeChain.mainNode.translate(0.01f, 0f, 0);
                    break;
                case ACTION_BUTTON_5:
                    AnimationNodeChain.mainNode.translate(0, 0, 0.01f);
                    break;
                case ACTION_BUTTON_6:
                    AnimationNodeChain.mainNode.translate(0, 0, -0.01f);
                    break;

                case ACTION_BUTTON_1:
                    File directory = new File("/home/jhelp/IdeaProjects/ia/3D_2/doc/tutorials/images/animNodeChain");
                    UtilIO.delete(directory);
                    UtilIO.createDirectory(directory);
                    AnimationNodeChain.window3D.screenShots(100, directory)
                                               .andConsume(directory1 -> Debug.mark("Capture Done"));
                    break;
            }
        }
    }

    /**
     * Launch the animation loop sample
     *
     * @param args Ignored
     */
    public static void main(String[] args)
    {
        AnimationNodeChain.window3D = Window3D.createSizedWindow(800, 600, "Animation loop", true);
        Scene scene = AnimationNodeChain.window3D.scene();

        Material materialHead = new Material("head");
        materialHead.colorDiffuse(Color4f.DARK_RED);

        Material materialBody = new Material("body");
        materialBody.colorDiffuse(Color4f.BLUE);

        Sphere head = new Sphere();
        AnimationNodeChain.mainNode = head;
        head.material(materialHead);
        head.setScale(0.1f, 0.1f, 0.1f);
        head.position(0, 0, -2);
        scene.add(head);

        int                bodySize = 10;
        NodeWithMaterial[] body     = new NodeWithMaterial[bodySize];
        float              x        = 0.2f;

        for (int i = 0; i < bodySize; i++)
        {
            body[i] = new ObjectClone(head);
            body[i].material(materialBody);
            body[i].position(x, 0, -2);
            body[i].setScale(0.1f, 0.1f, 0.1f);
            scene.add(body[i]);
            x += 0.2f;
        }

        NodeChain nodeChain = new NodeChain(head, body);
        AnimationNodeChain.window3D.playAnimation(nodeChain);

        ActionManager actionManager = AnimationNodeChain.window3D.actionManager();
        actionManager.consumable(ActionCode.ACTION_BUTTON_1, true);
        actionManager.registerActionListener(AnimationNodeChain::actionEvent);
    }
}

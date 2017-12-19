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
package jhelp.engine2.render;

/**
 * Node type <br>
 * <br>
 * Last modification : 7 fevr. 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public enum NodeType
{
    /**
     * Box
     */
    BOX,
    /**
     * Clone
     */
    CLONE,
    /**
     * Equation
     */
    EQUATION,
    /**
     * Node
     */
    NODE,
    /**
     * 3D object
     */
    OBJECT3D,
    /**
     * Path geometry (Build from tow path)
     */
    PATH_GEOM,
    /**
     * Plane
     */
    PLANE,
    /**
     * Revolution (Path rotate around Y axis)
     */
    REVOLUTION,
    /**
     * Sphere
     */
    SPHERE
}
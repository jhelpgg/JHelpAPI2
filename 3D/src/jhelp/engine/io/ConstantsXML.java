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

/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.engine.io;

/**
 * Constant for XML<br>
 * <br>
 * Last modification : 7 fevr. 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public interface ConstantsXML
{
    /** Markup name for camera */
    String MARKUP_CAMERA              = "CAMERA";
    /** Parameter for camera "Keep look at me" */
    String MARKUP_CAMERA_keepLookOnMe = "keepLookOnMe";
    /** Parameter for camera Look */
    String MARKUP_CAMERA_look         = "look";
    /** Parameter for camera position */
    String MARKUP_CAMERA_position     = "position";
    /** Parameter for camera UP */
    String MARKUP_CAMERA_up           = "up";

    /** Markup name for face */
    String MARKUP_FACE         = "FACE";
    /** Markup name for face normals */
    String MARKUP_FACE_NORMALS = "FACE_NORMALS";
    /** Markup name for face points */
    String MARKUP_FACE_POINTS  = "FACE_POINTS";
    /** Markup name for face UV */
    String MARKUP_FACE_UV      = "FACE_UV";

    /** Markup for material */
    String MARKUP_MATERIAL                = "Material";
    /** Parameter of ambiant color in material markup */
    String MARKUP_MATERIAL_colorAmbiant   = "colorAmbiant";
    /** Parameter of diffuse color in material markup */
    String MARKUP_MATERIAL_colorDiffuse   = "colorDiffuse";
    /** Parameter of emissive color in material markup */
    String MARKUP_MATERIAL_colorEmissive  = "colorEmissive";
    /** Parameter of specular color in material markup */
    String MARKUP_MATERIAL_colorSpecular  = "colorSpecular";
    /** Parameter of name in material markup */
    String MARKUP_MATERIAL_name           = "name";
    /** Parameter of shininess in material markup */
    String MARKUP_MATERIAL_shininess      = "shininess";
    /** Parameter of specular level in material markup */
    String MARKUP_MATERIAL_specularLevel  = "specularLevel";
    /** Parameter of spherical rate in material markup */
    String MARKUP_MATERIAL_sphericRate    = "sphericRate";
    /** Parameter of diffuse texture in material markup */
    String MARKUP_MATERIAL_textureDiffuse = "textureDiffuse";
    /** Parameter of spheric texture in material markup */
    String MARKUP_MATERIAL_textureSpheric = "textureSpheric";
    /** Parameter of transparency in material markup */
    String MARKUP_MATERIAL_transparency   = "transparency";
    /** Parameter of two sided in material markup */
    String MARKUP_MATERIAL_twoSided       = "twoSided";

    /** Markup name for mesh */
    String MARKUP_MESH = "MESH";

    /** Markup name for node */
    String MARKUP_NODE                   = "NODE";
    /** Parameter angle */
    String MARKUP_NODE_angle             = "angle";
    /** Markup parameter name for node X angle */
    String MARKUP_NODE_angleX            = "angleX";
    /** Markup parameter name for node Y angle */
    String MARKUP_NODE_angleY            = "angleY";
    /** Markup parameter name for node Z angle */
    String MARKUP_NODE_angleZ            = "angleZ";
    /** Markup parameter name for node can be pick property */
    String MARKUP_NODE_canBePick         = "canBePick";
    /** Parameter horizontal */
    String MARKUP_NODE_horizontal        = "horizontal";
    /** Parameter U inverted */
    String MARKUP_NODE_invU              = "invU";
    /** Parameter V inverted */
    String MARKUP_NODE_invV              = "invV";
    /** Parameter joined */
    String MARKUP_NODE_joined            = "joined";
    /** Parameter linearize */
    String MARKUP_NODE_linearize         = "linearize";
    /** Markup parameter name for material */
    String MARKUP_NODE_material          = "material";
    /** Markup parameter name for material selection */
    String MARKUP_NODE_materialSelection = "materialSelection";
    /** Parameter U multiplier */
    String MARKUP_NODE_multU             = "multU";
    /** Parameter V multiplier */
    String MARKUP_NODE_multV             = "multV";
    /** Markup parameter name for node name */
    String MARKUP_NODE_nodeName          = "nodeName";
    /** Parameter path precision */
    String MARKUP_NODE_pathPrecision     = "pathPrecision";
    /** Parameter U precision */
    String MARKUP_NODE_precisionU        = "precisionU";
    /** Parameter V precision */
    String MARKUP_NODE_precisionV        = "precisionV";
    /** Markup parameter name for reference */
    String MARKUP_NODE_reference         = "reference";
    /** Parameter rotation precision */
    String MARKUP_NODE_rotationPrecision = "rotationPrecision";
    /** Markup parameter name for node X scale */
    String MARKUP_NODE_scaleX            = "scaleX";
    /** Markup parameter name for node Y scale */
    String MARKUP_NODE_scaleY            = "scaleY";
    /** Markup parameter name for node Z scale */
    String MARKUP_NODE_scaleZ            = "scaleZ";
    /** Markup parameter name for node show wire property */
    String MARKUP_NODE_showWire          = "showWire";
    /** Parameter slice */
    String MARKUP_NODE_slice             = "slice";
    /** Parameter stack */
    String MARKUP_NODE_stack             = "stack";
    /** Markup parameter name for node texture hotspot name */
    String MARKUP_NODE_textureHotspot    = "textureHotspot";
    /** Markup parameter name for two sided */
    String MARKUP_NODE_twoSided          = "twoSided";
    /** Markup parameter name for node type */
    String MARKUP_NODE_type              = "type";
    /** Parameter vertical */
    String MARKUP_NODE_vertical          = "vertical";
    /** Markup parameter name for node visible property */
    String MARKUP_NODE_visible           = "visible";
    /** Markup parameter name for node wire color */
    String MARKUP_NODE_wireColor         = "wireColor";
    /** Markup parameter name for node X position */
    String MARKUP_NODE_x                 = "x";
    /** Markup parameter name for node angle X limited property */
    String MARKUP_NODE_xAngleLimited     = "xAngleLimited";
    /** Markup parameter name for node maximum angle X */
    String MARKUP_NODE_xAngleMax         = "xAngleMax";
    /** Markup parameter name for node minimum angle X */
    String MARKUP_NODE_xAngleMin         = "xAngleMin";
    /** Markup parameter name for node X position limited property */
    String MARKUP_NODE_xLimited          = "xLimited";
    /** Markup parameter name for node maximum position X */
    String MARKUP_NODE_xMax              = "xMax";
    /** Markup parameter name for node minimum position X */
    String MARKUP_NODE_xMin              = "xMin";
    /** Markup parameter name for node position Y */
    String MARKUP_NODE_y                 = "y";
    /** Markup parameter name for node Y position limited property */
    String MARKUP_NODE_yAngleLimited     = "yAngleLimited";
    /** Markup parameter name for node maximum angle Y */
    String MARKUP_NODE_yAngleMax         = "yAngleMax";
    /** Markup parameter name for node minimum angle Y */
    String MARKUP_NODE_yAngleMin         = "yAngleMin";
    /** Markup parameter name for node angle Y limited property */
    String MARKUP_NODE_yLimited          = "yLimited";
    /** Markup parameter name for node maximum Y position */
    String MARKUP_NODE_yMax              = "yMax";
    /** Markup parameter name for node minimum Y position */
    String MARKUP_NODE_yMin              = "yMin";
    /** Markup parameter name for node Z position */
    String MARKUP_NODE_z                 = "z";
    /** Markup parameter name for node angle Z limited property */
    String MARKUP_NODE_zAngleLimited     = "zAngleLimited";
    /** Markup parameter name for node maximum angle Z */
    String MARKUP_NODE_zAngleMax         = "zAngleMax";
    /** Markup parameter name for node minimum angle Z */
    String MARKUP_NODE_zAngleMin         = "zAngleMin";
    /** Markup parameter name for node position Z limited property */
    String MARKUP_NODE_zLimited          = "zLimited";
    /** Markup parameter name for node maximum Z position */
    String MARKUP_NODE_zMax              = "zMax";
    /** Markup parameter name for node minimum Z position */
    String MARKUP_NODE_zMin              = "zMin";

    /** Markup name for normal */
    String MARKUP_NORMALS = "NORMALS";

    /** Markup name for path */
    String MARKUP_PATH = "PATH";

    /** Markup name for path element */
    String MARKUP_PATH_ELEMENT          = "PATH_ELEMENT";
    /** Parameter path type in path element markup */
    String MARKUP_PATH_ELEMENT_pathType = "pathType";

    /** Markup name for U path */
    String MARKUP_PATH_U = "PATH_U";

    /** Markup name for V path */
    String MARKUP_PATH_V = "PATH_V";

    /** Markup name for points */
    String MARKUP_POINTS = "POINTS";

    /** Markup name for scene */
    String MARKUP_SCENE            = "SCENE";
    /** Parameter for background */
    String MARKUP_SCENE_background = "background";

    /** Markup name for UV */
    String MARKUP_UV = "UV";
}
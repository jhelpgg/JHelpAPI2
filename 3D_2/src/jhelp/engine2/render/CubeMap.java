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

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

/**
 * Cube map.<br>
 * A cube map is compose on six textures, place on each face of a cube.<br>
 * It use for having reflection, or simulate "mirror environment" in objects
 *
 * @author JHelp
 */
public class CubeMap
{
    /**
     * For place a texture in the "back" face of the cube
     */
    public static final int BACK   = GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
    /**
     * For place a texture in the "bottom" face of the cube
     */
    public static final int BOTTOM = GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
    /**
     * For place a texture in the "face" face of the cube
     */
    public static final int FACE   = GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;
    /**
     * For place a texture in the "left" face of the cube
     */
    public static final int LEFT   = GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
    /**
     * For place a texture in the "right" face of the cube
     */
    public static final int RIGHT  = GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
    /**
     * For place a texture in the "top" face of the cube
     */
    public static final int TOP    = GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;

    /**
     * Cross texture
     */
    private Texture crossTexture;
    /**
     * Indicate if cube map need to be refresh
     */
    private boolean needRefresh;
    /**
     * Video memory ID
     */
    private int     videoMemoryID;
    /**
     * Left face
     */
    private Texture xNegative;
    /**
     * Right face
     */
    private Texture xPositive;
    /**
     * Bottom face
     */
    private Texture yNegative;
    /**
     * Top face
     */
    private Texture yPositive;
    /**
     * Back face
     */
    private Texture zNegative;
    /**
     * Face face
     */
    private Texture zPositive;

    /**
     * Constructs CubeMap
     */
    public CubeMap()
    {
        this.videoMemoryID = -1;
        this.needRefresh = true;
    }

    /**
     * Apply the cube map.<br>
     * If the cube map is not complete, nothing is done
     */
    @ThreadOpenGL
    public void bind()
    {
        if (!this.isComplete())
        {
            // Not complete, so quit
            return;
        }

        //        if (this.videoMemoryID < 0)
        //        {
        //            // Not in video memory, so put it in
        //            try (MemoryStack stack = MemoryStack.stackPush())
        //            {
        //                final IntBuffer cubeMapID = stack.mallocInt(1);
        //                GL11.glGenTextures(cubeMapID);
        //                this.videoMemoryID = cubeMapID.get();
        //            }
        //        }

        //        if (this.needRefresh)
        //        {
        //            // If the cube map need to be refresh, refresh it
        //            GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, this.videoMemoryID);
        //
        //            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, GL11.GL_RGBA, this.xPositive.width,
        //                              this.xPositive.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
        //                              BufferUtils.transferByte(this.xPositive.pixels));
        //            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, GL11.GL_RGBA, this.xNegative.width,
        //                              this.xNegative.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
        //                              BufferUtils.transferByte(this.xNegative.pixels));
        //
        //            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, GL11.GL_RGBA, this.yPositive.width,
        //                              this.yPositive.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
        //                              BufferUtils.transferByte(this.yPositive.pixels));
        //            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, GL11.GL_RGBA, this.yNegative.width,
        //                              this.yNegative.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
        //                              BufferUtils.transferByte(this.yNegative.pixels));
        //
        //            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, GL11.GL_RGBA, this.zPositive.width,
        //                              this.zPositive.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
        //                              BufferUtils.transferByte(this.zPositive.pixels));
        //            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, GL11.GL_RGBA, this.zNegative.width,
        //                              this.zNegative.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,
        //                              BufferUtils.transferByte(this.zNegative.pixels));
        //
        //            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        //            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        //            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_WRAP_R, GL11.GL_REPEAT);
        //            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        //            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        //
        //            // Cube map has been refresh
        //            this.needRefresh = false;
        //        }

        // Activate cube map
        //        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, this.videoMemoryID);
        //        GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
        GL11.glEnable(GL13.GL_TEXTURE_CUBE_MAP);
        GL11.glEnable(GL11.GL_TEXTURE_GEN_S);
        GL11.glEnable(GL11.GL_TEXTURE_GEN_T);
        GL11.glEnable(GL11.GL_TEXTURE_GEN_R);
        GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL13.GL_REFLECTION_MAP);
        GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL13.GL_REFLECTION_MAP);
        GL11.glTexGeni(GL11.GL_R, GL11.GL_TEXTURE_GEN_MODE, GL13.GL_REFLECTION_MAP);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        //Strange ????
        // TODO Check the cube map !!!!
        if (this.crossTexture != null)
        {
            this.crossTexture.bind();
        }
    }

    /**
     * Cut a cross texture for fill the cube map.<br>
     * Cross suppose be like : <code><br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;+X<br>
     * &nbsp;+Y&nbsp;&nbsp;+Z&nbsp;&nbsp;-Y<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-X<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-Z<br>
     * </code>
     *
     * @param texture Texture to cut. Use {@code null} to clear the cube map
     */
    public void crossTexture(final @Nullable Texture texture)
    {
        this.crossTexture = texture;

        if (texture == null)
        {
            this.xPositive = null;
            this.xNegative = null;
            this.yPositive = null;
            this.yNegative = null;
            this.zPositive = null;
            this.zNegative = null;

            return;
        }

        final int width  = texture.width / 3;
        final int height = texture.height >> 2;

        // X positive
        this.xPositive = Texture.obtainTexture(texture.textureName() + "_CUBE_MAP_X_POSITIVE");
        if (this.xPositive == null)
        {
            this.xPositive = texture.obtainParcel(width, 0, width, height, "_CUBE_MAP_X_POSITIVE");
        }

        // X negative
        this.xNegative = Texture.obtainTexture(texture.textureName() + "_CUBE_MAP_X_NEGATIVE");
        if (this.xNegative == null)
        {
            this.xNegative = texture.obtainParcel(width, height << 1, width, height, "_CUBE_MAP_X_NEGATIVE");
        }

        // Y positive
        this.yPositive = Texture.obtainTexture(texture.textureName() + "_CUBE_MAP_Y_POSITIVE");
        if (this.yPositive == null)
        {
            this.yPositive = texture.obtainParcel(0, height, width, height, "_CUBE_MAP_Y_POSITIVE");
        }

        // Y negative
        this.yNegative = Texture.obtainTexture(texture.textureName() + "_CUBE_MAP_Y_NEGATIVE");
        if (this.yNegative == null)
        {
            this.yNegative = texture.obtainParcel(width << 1, height, width, height, "_CUBE_MAP_Y_NEGATIVE");
        }

        // Z positive
        this.zPositive = Texture.obtainTexture(texture.textureName() + "_CUBE_MAP_Z_POSITIVE");
        if (this.zPositive == null)
        {
            this.zPositive = texture.obtainParcel(width, height, width, height, "_CUBE_MAP_Z_POSITIVE");
        }

        // Z negative
        this.zNegative = Texture.obtainTexture(texture.textureName() + "_CUBE_MAP_Z_NEGATIVE");
        if (this.zNegative == null)
        {
            this.zNegative = texture.obtainParcel(width, height * 3, width, height, "_CUBE_MAP_Z_NEGATIVE");
        }

        this.needRefresh = true;
    }

    /**
     * Original cross texture (Last Texture use in {@link #crossTexture(Texture)})
     *
     * @return Original cross texture
     */
    public @Nullable Texture crossTexture()
    {
        return this.crossTexture;
    }

    /**
     * End application of cube map
     */
    @ThreadOpenGL
    public void endCubeMap()
    {
        GL11.glDisable(GL13.GL_TEXTURE_CUBE_MAP);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_S);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_T);
        GL11.glDisable(GL11.GL_TEXTURE_GEN_R);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    /**
     * Flush last changes.<br>
     * Use it if you have modified one of its texture pixels
     */
    public void flush()
    {
        this.needRefresh = true;
    }

    /**
     * Indicates if the cube map is complete defined (If it is not, it can't be used)
     *
     * @return {@code true} if the cube map is complete defined
     */
    public boolean isComplete()
    {
        return (this.xNegative != null) && (this.xPositive != null) &&
               (this.yNegative != null) && (this.yPositive != null) &&
               (this.zNegative != null) && (this.zPositive != null);
    }

    /**
     * Change a face of the cube map.<br>
     * The face parameter must be one of the following : {@link #RIGHT}, {@link #LEFT}, {@link #TOP}, {@link #BOTTOM},
     * {@link #FACE} or {@link #BACK}
     *
     * @param face    Face to change
     * @param texture Texture apply
     */
    public void setFace(final int face, final @NotNull Texture texture)
    {
        if (texture == null)
        {
            throw new NullPointerException("texture mustn't be null");
        }

        switch (face)
        {
            case CubeMap.RIGHT:
                this.xPositive = texture;
                break;
            case CubeMap.LEFT:
                this.xNegative = texture;
                break;
            case CubeMap.TOP:
                this.yPositive = texture;
                break;
            case CubeMap.BOTTOM:
                this.yNegative = texture;
                break;
            case CubeMap.FACE:
                this.zPositive = texture;
                break;
            case CubeMap.BACK:
                this.zNegative = texture;
                break;
            default:
                throw new IllegalArgumentException("face must be RIGHT, LEFT, TOP, BOTTOM, FACE or BACK");
        }

        this.needRefresh = true;
    }

    /**
     * -X texture
     *
     * @return -X texture
     */
    public @Nullable Texture xNegative()
    {
        return this.xNegative;
    }

    /**
     * Change define the -X texture (Remember that the result will be better and more cross computer if you use same sizes for
     * each part and sizes are power of 2).<br>
     * Can Use {@code null} to remove (Remember that cube map be be show only if all textures are sets)
     *
     * @param xNegative New -X texture
     */
    public void xNegative(final @Nullable Texture xNegative)
    {
        this.xNegative = xNegative;
    }

    /**
     * +X texture
     *
     * @return +X texture
     */
    public @Nullable Texture xPositive()
    {
        return this.xPositive;
    }

    /**
     * Change define the +X texture (Remember that the result will be better and more cross computer if you use same sizes for
     * each part and sizes are power of 2).<br>
     * Can Use {@code null} to remove (Remember that cube map be be show only if all textures are sets)
     *
     * @param xPositive New +X texture
     */
    public void xPositive(final @Nullable Texture xPositive)
    {
        this.xPositive = xPositive;
    }

    /**
     * -Y texture
     *
     * @return -Y texture
     */
    public @Nullable Texture yNegative()
    {
        return this.yNegative;
    }

    /**
     * Change define the -Y texture (Remember that the result will be better and more cross computer if you use same sizes for
     * each part and sizes are power of 2).<br>
     * Can Use {@code null} to remove (Remember that cube map be be show only if all textures are sets)
     *
     * @param yNegative New -Y texture
     */
    public void yNegative(final @Nullable Texture yNegative)
    {
        this.yNegative = yNegative;
    }

    /**
     * +Y texture
     *
     * @return +Y texture
     */
    public @Nullable Texture yPositive()
    {
        return this.yPositive;
    }

    /**
     * Change define the +Y texture (Remember that the result will be better and more cross computer if you use same sizes for
     * each part and sizes are power of 2).<br>
     * Can Use {@code null} to remove (Remember that cube map be be show only if all textures are sets)
     *
     * @param yPositive New +Y texture
     */
    public void yPositive(final @Nullable Texture yPositive)
    {
        this.yPositive = yPositive;
    }

    /**
     * -Z texture
     *
     * @return -Z texture
     */
    public @Nullable Texture zNegative()
    {
        return this.zNegative;
    }

    /**
     * Change define the -Z texture (Remember that the result will be better and more cross computer if you use same sizes for
     * each part and sizes are power of 2).<br>
     * Can Use {@code null} to remove (Remember that cube map be be show only if all textures are sets)
     *
     * @param zNegative New -Z texture
     */
    public void zNegative(final @Nullable Texture zNegative)
    {
        this.zNegative = zNegative;
    }

    /**
     * +Z texture
     *
     * @return +Z texture
     */
    public @Nullable Texture zPositive()
    {
        return this.zPositive;
    }

    /**
     * Change define the +Z texture (Remember that the result will be better and more cross computer if you use same sizes for
     * each part and sizes are power of 2).<br>
     * Can Use {@code null} to remove (Remember that cube map be be show only if all textures are sets)
     *
     * @param zPositive New +Z texture
     */
    public void zPositive(final @Nullable Texture zPositive)
    {
        this.zPositive = zPositive;
    }
}
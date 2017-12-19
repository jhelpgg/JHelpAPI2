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
import jhelp.engine2.util.BufferUtils;
import org.lwjgl.opengl.GL11;

/**
 * A OpenGL Light
 *
 * @author JHelp
 */
public final class Light
{
    /**
     * Light 0 name
     */
    public static final String LIGHT_O = "LIGHT_0";
    /**
     * Ambient color
     */
    private Color4f ambient;
    /**
     * Indicates that a change as just append
     */
    private boolean asChanged;
    /**
     * Constant attenuation
     */
    private float   constantAttenuation;
    /**
     * Diffuse color
     */
    private Color4f diffuse;
    /**
     * Indicates if light is on
     */
    private boolean enable;

    /**
     * OpenGL light id
     */
    private final int    id;
    /**
     * Linear attenuation
     */
    private       float  linearAttenuation;
    /**
     * Light name
     */
    private final String name;

    /**
     * Indicates if we need refresh the light
     */
    private boolean needRefresh;
    /**
     * Quadric attenuation
     */
    private float   quadricAttenuation;
    /**
     * Specular color
     */
    private Color4f specular;
    /**
     * Spot cut off
     */
    private int     spotCutOff;

    /**
     * Spot direction
     */
    private Point3D spotDirection;
    /**
     * Spot exponent
     */
    private int     spotExponent;
    /**
     * Position/Direction W
     */
    private float   w;

    /**
     * Position/Direction X
     */
    private float x;
    /**
     * Position/Direction Y
     */
    private float y;
    /**
     * Position/Direction Z
     */
    private float z;

    /**
     * Constructs Light
     *
     * @param name Light name
     * @param id   Light index
     */
    Light(final @NotNull String name, final int id)
    {
        this.name = name;
        this.id = GL11.GL_LIGHT0 + id;

        this.reset();
    }

    /**
     * Reset the light to default settings of light 0
     */
    private void reset0()
    {
        this.enable = true;

        if (this.diffuse == null)
        {
            this.diffuse = Color4f.WHITE.copy();
        }
        else
        {
            this.diffuse.set(1, 1, 1, 1);
        }

        if (this.specular == null)
        {
            this.specular = Color4f.WHITE.copy();
        }
        else
        {
            this.specular.set(1, 1, 1, 1);
        }
    }

    /**
     * Reset the light to the default setting of all lights except the light 0
     */
    private void resetOthers()
    {
        this.enable = false;

        if (this.diffuse == null)
        {
            this.diffuse = Color4f.BLACK.copy();
        }
        else
        {
            this.diffuse.set(0, 0, 0, 1);
        }

        if (this.specular == null)
        {
            this.specular = Color4f.BLACK.copy();
        }
        else
        {
            this.specular.set(0, 0, 0, 1);
        }
    }

    /**
     * Render the light
     */
    @ThreadOpenGL
    void render()
    {
        if (this.asChanged)
        {
            this.asChanged = false;

            if (this.needRefresh)
            {
                this.needRefresh = false;

                GL11.glLightfv(this.id, GL11.GL_AMBIENT, this.ambient.putInFloatBuffer());
                GL11.glLightfv(this.id, GL11.GL_DIFFUSE, this.diffuse.putInFloatBuffer());
                GL11.glLightfv(this.id, GL11.GL_SPECULAR, this.specular.putInFloatBuffer());

                GL11.glLightfv(this.id, GL11.GL_POSITION, BufferUtils.transferFloat(this.x, this.y, this.z, this.w));

                GL11.glLightfv(this.id, GL11.GL_SPOT_DIRECTION,
                               BufferUtils.transferFloat(this.spotDirection.x, this.spotDirection.y,
                                                         this.spotDirection.z));
                GL11.glLighti(this.id, GL11.GL_SPOT_EXPONENT, this.spotExponent);
                GL11.glLighti(this.id, GL11.GL_SPOT_CUTOFF, this.spotCutOff);

                GL11.glLightf(this.id, GL11.GL_CONSTANT_ATTENUATION, this.constantAttenuation);
                GL11.glLightf(this.id, GL11.GL_LINEAR_ATTENUATION, this.linearAttenuation);
                GL11.glLightf(this.id, GL11.GL_QUADRATIC_ATTENUATION, this.quadricAttenuation);
            }

            if (this.enable)
            {
                GL11.glEnable(this.id);
            }
            else
            {
                GL11.glDisable(this.id);
            }
        }
    }

    /**
     * Return ambient
     *
     * @return ambient
     */
    public @NotNull Color4f ambient()
    {
        return this.ambient.copy();
    }

    /**
     * Modify ambient
     *
     * @param ambient New ambient value
     */
    public void ambient(final @NotNull Color4f ambient)
    {
        this.ambient.set(ambient);

        this.asChanged = this.needRefresh = true;
    }

    /**
     * Return constantAttenuation
     *
     * @return constantAttenuation
     */
    public float constantAttenuation()
    {
        return this.constantAttenuation;
    }

    /**
     * Modify constantAttenuation
     *
     * @param constantAttenuation New constantAttenuation value
     */
    public void constantAttenuation(final float constantAttenuation)
    {
        if (constantAttenuation < 0)
        {
            throw new IllegalArgumentException("No negative value");
        }

        this.constantAttenuation = constantAttenuation;

        this.asChanged = this.needRefresh = true;
    }

    /**
     * Return diffuse
     *
     * @return diffuse
     */
    public @NotNull Color4f diffuse()
    {
        return this.diffuse.copy();
    }

    /**
     * Modify diffuse
     *
     * @param diffuse New diffuse value
     */
    public void diffuse(final @NotNull Color4f diffuse)
    {
        this.diffuse.set(diffuse);

        this.asChanged = this.needRefresh = true;
    }

    /**
     * Return enable
     *
     * @return enable
     */
    public boolean enable()
    {
        return this.enable;
    }

    /**
     * Modify enable
     *
     * @param enable New enable value
     */
    public void enable(final boolean enable)
    {
        if (this.enable != enable)
        {
            this.enable = enable;

            this.asChanged = true;
        }
    }

    /**
     * Return linearAttenuation
     *
     * @return linearAttenuation
     */
    public float linearAttenuation()
    {
        return this.linearAttenuation;
    }

    /**
     * Modify linearAttenuation
     *
     * @param linearAttenuation New linearAttenuation value
     */
    public void linearAttenuation(final float linearAttenuation)
    {
        if (linearAttenuation < 0)
        {
            throw new IllegalArgumentException("No negative value");
        }

        this.linearAttenuation = linearAttenuation;

        this.asChanged = this.needRefresh = true;
    }

    /**
     * Set the light to be a directional light
     *
     * @param direction Direction
     */
    public void makeDirectional(final @NotNull Point3D direction)
    {
        this.x = direction.x;
        this.y = direction.y;
        this.z = direction.z;
        this.w = 0;

        this.spotDirection.set(0, 0, -1);

        this.spotExponent = 0;
        this.spotCutOff = 180;

        this.constantAttenuation = 1;
        this.linearAttenuation = 0;
        this.quadricAttenuation = 0;

        this.asChanged = this.needRefresh = true;
    }

    /**
     * Set the light to be a ponctual light
     *
     * @param position            Position
     * @param exponent            Exponent attenuation
     * @param constantAttenuation Constant attenuation
     * @param linearAttenuation   Linear attenuation
     * @param quadricAttenuation  Quadric attenuation
     */
    public void makePonctualLight(
            final @NotNull Point3D position, final int exponent,
            final float constantAttenuation, final float linearAttenuation, final float quadricAttenuation)
    {
        if ((exponent < 0) || (exponent > 128))
        {
            throw new IllegalArgumentException("exponent must be in [0, 128], not " + exponent);
        }

        if (constantAttenuation < 0)
        {
            throw new IllegalArgumentException("No negative value");
        }

        if (linearAttenuation < 0)
        {
            throw new IllegalArgumentException("No negative value");
        }

        if (quadricAttenuation < 0)
        {
            throw new IllegalArgumentException("No negative value");
        }

        this.x = position.x;
        this.y = position.y;
        this.z = position.z;
        this.w = 1;

        this.spotDirection.set(0, 0, -1);

        this.spotExponent = exponent;
        this.spotCutOff = 180;

        this.constantAttenuation = constantAttenuation;
        this.linearAttenuation = linearAttenuation;
        this.quadricAttenuation = quadricAttenuation;

        this.asChanged = this.needRefresh = true;
    }

    /**
     * Set the light to be a spot
     *
     * @param position            Position
     * @param direction           Light direction
     * @param exponent            Exponent attenuation
     * @param cutOff              Cut off
     * @param constantAttenuation Constant attenuation
     * @param linearAttenuation   Linear attenuation
     * @param quadricAttenuation  Quadric attenuation
     */
    public void makeSpot(
            final @NotNull Point3D position, final @NotNull Point3D direction,
            final int exponent, final int cutOff,
            final float constantAttenuation, final float linearAttenuation, final float quadricAttenuation)
    {
        if ((exponent < 0) || (exponent > 128))
        {
            throw new IllegalArgumentException("exponent must be in [0, 128], not " + exponent);
        }

        if (constantAttenuation < 0)
        {
            throw new IllegalArgumentException("No negative value");
        }

        if (linearAttenuation < 0)
        {
            throw new IllegalArgumentException("No negative value");
        }

        if (quadricAttenuation < 0)
        {
            throw new IllegalArgumentException("No negative value");
        }

        if (((cutOff < 0) || (cutOff > 90)) && (cutOff != 180))
        {
            throw new IllegalArgumentException("cutOff must be in [0, 90] or the special 180, not " + cutOff);
        }

        this.x = position.x;
        this.y = position.y;
        this.z = position.z;
        this.w = 1;

        this.spotDirection.set(direction);

        this.spotExponent = exponent;
        this.spotCutOff = cutOff;

        this.constantAttenuation = constantAttenuation;
        this.linearAttenuation = linearAttenuation;
        this.quadricAttenuation = quadricAttenuation;

        this.asChanged = this.needRefresh = true;
    }

    /**
     * Light name
     *
     * @return Light name
     */
    public @NotNull String name()
    {
        return this.name;
    }

    /**
     * Change the position/direction
     *
     * @param x X
     * @param y Y
     * @param z Z
     * @param w W
     */
    public void position(final float x, final float y, final float z, final float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;

        this.asChanged = this.needRefresh = true;
    }

    /**
     * Return quadricAttenuation
     *
     * @return quadricAttenuation
     */
    public float quadricAttenuation()
    {
        return this.quadricAttenuation;
    }

    /**
     * Modify quadricAttenuation
     *
     * @param quadricAttenuation New quadricAttenuation value
     */
    public void quadricAttenuation(final float quadricAttenuation)
    {
        if (quadricAttenuation < 0)
        {
            throw new IllegalArgumentException("No negative value");
        }

        this.quadricAttenuation = quadricAttenuation;

        this.asChanged = this.needRefresh = true;
    }

    /**
     * Reset the light to default settings
     */
    public void reset()
    {
        if (this.ambient == null)
        {
            this.ambient = Color4f.BLACK.copy();
        }
        else
        {
            this.ambient.set(0, 0, 0, 1);
        }

        this.x = 0;
        this.y = 0;
        this.z = 1;
        this.w = 0;

        if (this.spotDirection == null)
        {
            this.spotDirection = new Point3D(0, 0, -1);
        }
        else
        {
            this.spotDirection.set(0, 0, -1);
        }

        this.spotExponent = 0;
        this.spotCutOff = 180;

        this.constantAttenuation = 1;
        this.linearAttenuation = 0;
        this.quadricAttenuation = 0;

        if (this.id == GL11.GL_LIGHT0)
        {
            this.reset0();
        }
        else
        {
            this.resetOthers();
        }

        this.asChanged = this.needRefresh = true;
    }

    /**
     * Return specular
     *
     * @return specular
     */
    public @NotNull Color4f specular()
    {
        return this.specular.copy();
    }

    /**
     * Modify specular
     *
     * @param specular New specular value
     */
    public void specular(final Color4f specular)
    {
        this.specular.set(specular);

        this.asChanged = this.needRefresh = true;
    }

    /**
     * Return spotCutOff
     *
     * @return spotCutOff
     */
    public int spotCutOff()
    {
        return this.spotCutOff;
    }

    /**
     * Modify spotCutOff
     *
     * @param spotCutOff New spotCutOff value
     */
    public void spotCutOff(final int spotCutOff)
    {
        if (((spotCutOff < 0) || (spotCutOff > 90)) && (spotCutOff != 180))
        {
            throw new IllegalArgumentException("Spot cut off must be in [0, 90] or the special 180, not " + spotCutOff);
        }

        if (this.spotCutOff != spotCutOff)
        {
            this.spotCutOff = spotCutOff;

            this.asChanged = this.needRefresh = true;
        }
    }

    /**
     * Return spotDirection
     *
     * @return spotDirection
     */
    public @NotNull Point3D spotDirection()
    {
        return new Point3D(this.spotDirection);
    }

    /**
     * Modify spotDirection
     *
     * @param spotDirection New spotDirection value
     */
    public void spotDirection(final Point3D spotDirection)
    {
        this.spotDirection.set(spotDirection);

        this.asChanged = this.needRefresh = true;
    }

    /**
     * Return spotExponent
     *
     * @return spotExponent
     */
    public int spotExponent()
    {
        return this.spotExponent;
    }

    /**
     * Modify spotExponent
     *
     * @param spotExponent New spotExponent value
     */
    public void spotExponent(final int spotExponent)
    {
        if ((spotExponent < 0) || (spotExponent > 128))
        {
            throw new IllegalArgumentException("Spot exponent must be in [0, 128], not " + spotExponent);
        }

        if (this.spotExponent != spotExponent)
        {
            this.spotExponent = spotExponent;

            this.asChanged = this.needRefresh = true;
        }
    }

    /**
     * Return w
     *
     * @return w
     */
    public float w()
    {
        return this.w;
    }

    /**
     * Return x
     *
     * @return x
     */
    public float x()
    {
        return this.x;
    }

    /**
     * Return y
     *
     * @return y
     */
    public float y()
    {
        return this.y;
    }

    /**
     * Return z
     *
     * @return z
     */
    public float z()
    {
        return this.z;
    }
}
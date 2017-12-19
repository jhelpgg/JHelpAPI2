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

/**
 * Light list for manipulate lights <br>
 *
 * @author JHelp
 */
public class Lights
{
    /**
     * Actual number of created lights
     */
    private       int     actualNumberOfLights;
    /**
     * Created lights
     */
    private final Light[] lights;
    /**
     * Maximum number of lights that the video card can manage
     */
    private final int     maximumNumberOfLights;

    /**
     * Constructs Lights
     *
     * @param maximumNumberOfLights Maximum number of lights that the video card can manage
     */
    Lights(final int maximumNumberOfLights)
    {
        this.maximumNumberOfLights = maximumNumberOfLights;
        this.lights = new Light[this.maximumNumberOfLights];

        this.lights[0] = new Light(Light.LIGHT_O, 0);
        this.actualNumberOfLights = 1;
    }

    /**
     * Create a new light
     *
     * @param name Light name
     * @return Created light index
     */
    private int createNewLight(@NotNull String name)
    {
        if (!this.canCreateNewLight())
        {
            throw new IllegalStateException("Maximum of lights is reach");
        }

        if (name == null)
        {
            name = "LIGHT_" + this.actualNumberOfLights;
        }

        this.lights[this.actualNumberOfLights] = new Light(name, this.actualNumberOfLights);
        this.actualNumberOfLights++;

        return this.actualNumberOfLights - 1;
    }

    /**
     * Render the lights
     */
    @ThreadOpenGL
    void render()
    {
        for (int i = 0; i < this.actualNumberOfLights; i++)
        {
            this.lights[i].render();
        }
    }

    /**
     * Number of created lights
     *
     * @return Number of created lights
     */
    public int actualNumberOfLights()
    {
        return this.actualNumberOfLights;
    }

    /**
     * Indicates if we are able to create a new light
     *
     * @return {@code true} if able to create a new light
     */
    public boolean canCreateNewLight()
    {
        return this.actualNumberOfLights < this.maximumNumberOfLights;
    }

    /**
     * Create a new directional light
     *
     * @param name      Light name
     * @param ambient   Ambient color
     * @param diffuse   Diffuse color
     * @param specular  Specular color
     * @param direction Direction of light
     * @return Created light index
     */
    public int createNewDirectional(
            final @NotNull String name,
            final @NotNull Color4f ambient, final @NotNull Color4f diffuse, final @NotNull Color4f specular,
            final @NotNull Point3D direction)
    {
        final int id = this.createNewLight(name);

        final Light light = this.lights[id];

        light.ambient(ambient);
        light.diffuse(diffuse);
        light.specular(specular);

        light.makeDirectional(direction);

        return id;
    }

    /**
     * Create a new ponctual light
     *
     * @param name                Light name
     * @param ambient             Ambient color
     * @param diffuse             Diffuse color
     * @param specular            Specular color
     * @param position            Position
     * @param exponent            Exponent in [0, 128]
     * @param constantAttenuation Constant attenuation
     * @param linearAttenuation   Linear attenuation
     * @param quadricAttenuation  Quadric attenuation
     * @return Created light index
     */
    public int createNewPonctual(
            final @NotNull String name,
            final @NotNull Color4f ambient, final @NotNull Color4f diffuse, final @NotNull Color4f specular,
            final @NotNull Point3D position,
            final int exponent, final float constantAttenuation, final float linearAttenuation,
            final float quadricAttenuation)
    {
        final int id = this.createNewLight(name);

        final Light light = this.lights[id];

        light.ambient(ambient);
        light.diffuse(diffuse);
        light.specular(specular);

        light.makePonctualLight(position, exponent, constantAttenuation, linearAttenuation, quadricAttenuation);

        return id;
    }

    /**
     * Create a new spot light
     *
     * @param name                Light name
     * @param ambient             Ambient color
     * @param diffuse             Diffuse color
     * @param specular            Specular color
     * @param position            Position
     * @param direction           Spot direction
     * @param exponent            Exponent in [0, 128]
     * @param cutOff              Cut off in [0, 90] or special 180
     * @param constantAttenuation Constant attenuation
     * @param linearAttenuation   Linear attenuation
     * @param quadricAttenuation  Quadric attenuation
     * @return Created light index
     */
    public int createNewSpot(
            final @NotNull String name,
            final @NotNull Color4f ambient, final @NotNull Color4f diffuse, final @NotNull Color4f specular,
            final @NotNull Point3D position, final @NotNull Point3D direction,
            final int exponent, final int cutOff, final float constantAttenuation, final float linearAttenuation,
            final float quadricAttenuation)
    {
        final int id = this.createNewLight(name);

        final Light light = this.lights[id];

        light.ambient(ambient);
        light.diffuse(diffuse);
        light.specular(specular);

        light.makeSpot(position, direction, exponent, cutOff, constantAttenuation, linearAttenuation,
                       quadricAttenuation);

        return id;
    }

    /**
     * Maximum number of lights that the video card can manage
     *
     * @return Maximum number of lights that the video card can manage
     */
    public int maximumNumberOfLights()
    {
        return this.maximumNumberOfLights;
    }

    /**
     * Obtain light by its index
     *
     * @param id Light index
     * @return Light
     */
    public @NotNull Light obtainLight(final int id)
    {
        if ((id < 0) || (id >= this.actualNumberOfLights))
        {
            throw new IllegalArgumentException("id must be in [0, " + this.actualNumberOfLights + "[ not " + id);
        }

        return this.lights[id];
    }

    /**
     * Obtain light by its name
     *
     * @param name Light name
     * @return Light or {@code null}
     */
    public @Nullable Light obtainLight(final String name)
    {
        for (int i = 0; i < this.actualNumberOfLights; i++)
        {
            if (this.lights[i].name().equals(name) == true)
            {
                return this.lights[i];
            }
        }

        return null;
    }
}
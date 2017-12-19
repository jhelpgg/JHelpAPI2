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
import java.util.Hashtable;
import jhelp.engine2.io.ConstantsXML;
import jhelp.engine2.util.Math3D;
import jhelp.util.list.EnumerationIterator;
import jhelp.util.text.UtilText;
import jhelp.xml.MarkupXML;
import org.lwjgl.opengl.GL11;

/**
 * Material for 3D object<br>
 * It's a mix with diffuse and environment<br>
 * <br>
 *
 * @author JHelp
 */
public class Material
{
    /**
     * New material default header name
     */
    private static final String NEW_MATERIAL_HEADER = "MATERIAL_";
    /**
     * Materials table
     */
    private static Hashtable<String, Material> hashtableMaterials;
    /**
     * Material use for pick UV
     */
    private static Material                    materialForPickUV;
    /**
     * Next default ID name
     */
    private static      int      nextID           = 0;
    /**
     * Default material
     */
    public static final Material DEFAULT_MATERIAL = new Material("Default");

    /**
     * Name for material 2D
     */
    public static final String MATERIAL_FOR_2D_NAME = "MATERIAL_FOR_2D";

    /**
     * New default named material
     *
     * @return New default named material
     */
    public static @NotNull Material createNewMaterial()
    {
        if (Material.hashtableMaterials == null)
        {
            Material.hashtableMaterials = new Hashtable<>();
        }

        String name = Material.NEW_MATERIAL_HEADER + (Material.nextID++);

        while (Material.hashtableMaterials.containsKey(name) == true)
        {
            name = Material.NEW_MATERIAL_HEADER + (Material.nextID++);
        }

        return new Material(name);
    }

    /**
     * Create a new material with a specific base name.<br>
     * Note if the name is alred given to an other material, the name is little changed to be unique
     *
     * @param name Base name. If {@code null} or empty, a name is automatic given
     * @return Created material
     */
    public static @NotNull Material createNewMaterial(@Nullable String name)
    {
        if (Material.hashtableMaterials == null)
        {
            Material.hashtableMaterials = new Hashtable<>();
        }

        if ((name == null) || ((name = name.trim()).length() == 0))
        {
            name = Material.NEW_MATERIAL_HEADER + "0";
        }

        name = UtilText.computeNotInsideName(name, Material.hashtableMaterials.keySet());

        return new Material(name);
    }

    /**
     * Obtain material with its name
     *
     * @param name Material name
     * @return The material or {@link #DEFAULT_MATERIAL} if the material not exists
     */
    public static @NotNull Material obtainMaterial(final @NotNull String name)
    {
        if (name == null)
        {
            throw new NullPointerException("name mustn't be null");
        }

        if (Material.hashtableMaterials == null)
        {
            return Material.DEFAULT_MATERIAL;
        }

        final Material material = Material.hashtableMaterials.get(name);

        if (material == null)
        {
            return Material.DEFAULT_MATERIAL;
        }

        return material;
    }

    /**
     * Material use for pick UV
     *
     * @return Material use for pick UV
     */
    public static @NotNull Material obtainMaterialForPickUV()
    {
        if (Material.materialForPickUV != null)
        {
            return Material.materialForPickUV;
        }

        Material.materialForPickUV = new Material("JHELP_MATERIAL_FOR_PICK_UV");
        Material.materialForPickUV.colorEmissive().set(1f);
        Material.materialForPickUV.specularLevel(1f);
        Material.materialForPickUV.shininess(128);
        Material.materialForPickUV.colorDiffuse().set(1f);
        Material.materialForPickUV.colorSpecular().set();
        Material.materialForPickUV.colorAmbient().set(1f);
        Material.materialForPickUV.twoSided(true);
        Material.materialForPickUV.textureDiffuse(Texture.obtainTextureForPickUV());

        return Material.materialForPickUV;
    }

    /**
     * Obtain a material or create a new one if not exists
     *
     * @param name Material name
     * @return Searched material or newly created
     */
    public static @NotNull Material obtainMaterialOrCreate(final @NotNull String name)
    {
        if (name == null)
        {
            throw new NullPointerException("name mustn't be null");
        }

        if (Material.hashtableMaterials == null)
        {
            return new Material(name);
        }

        final Material material = Material.hashtableMaterials.get(name);
        if (material == null)
        {
            return new Material(name);
        }

        return material;
    }

    /**
     * Parse a XML markup to create a material
     *
     * @param markupXML Markup XML to parse
     * @return Created material
     */
    public static @NotNull Material parseXML(final @NotNull MarkupXML markupXML)
    {
        final Material material = Material.obtainMaterialOrCreate(markupXML.obtainParameter(
                ConstantsXML.MARKUP_MATERIAL_name));

        material.colorAmbient.parseString(markupXML.obtainParameter(ConstantsXML.MARKUP_MATERIAL_colorAmbiant));
        material.colorDiffuse.parseString(markupXML.obtainParameter(ConstantsXML.MARKUP_MATERIAL_colorDiffuse));
        material.colorEmissive.parseString(markupXML.obtainParameter(ConstantsXML.MARKUP_MATERIAL_colorEmissive));
        material.colorSpecular.parseString(markupXML.obtainParameter(ConstantsXML.MARKUP_MATERIAL_colorSpecular));
        material.shininess = markupXML.obtainParameter(ConstantsXML.MARKUP_MATERIAL_shininess, 12);
        material.specularLevel = markupXML.obtainParameter(ConstantsXML.MARKUP_MATERIAL_specularLevel, 0.1f);
        material.sphericRate = markupXML.obtainParameter(ConstantsXML.MARKUP_MATERIAL_sphericRate, 1f);
        material.transparency = markupXML.obtainParameter(ConstantsXML.MARKUP_MATERIAL_sphericRate, 1f);
        material.twoSided = markupXML.obtainParameter(ConstantsXML.MARKUP_MATERIAL_twoSided, false);

        String name = markupXML.obtainParameter(ConstantsXML.MARKUP_MATERIAL_textureDiffuse);
        if (name != null)
        {
            material.textureDiffuse = Texture.obtainTexture(name);
        }

        name = markupXML.obtainParameter(ConstantsXML.MARKUP_MATERIAL_textureSpheric);
        if (name != null)
        {
            material.textureSpheric = Texture.obtainTexture(name);
        }

        return material;
    }

    /**
     * Force refresh all materials
     */
    public static void refreshAllMaterials()
    {
        if (Material.hashtableMaterials == null)
        {
            return;
        }

        for (final Material material : new EnumerationIterator<Material>(Material.hashtableMaterials.elements()))
        {
            if (material.textureDiffuse != null)
            {
                material.textureDiffuse.flush();
            }

            if (material.textureSpheric != null)
            {
                material.textureSpheric.flush();
            }

            if (material.cubeMap != null)
            {
                material.cubeMap.flush();
            }
        }
    }

    /**
     * Register a material
     *
     * @param material Material to register
     */
    private static void registerMaterial(final @NotNull Material material)
    {
        if (Material.hashtableMaterials == null)
        {
            Material.hashtableMaterials = new Hashtable<>();
        }

        Material.hashtableMaterials.put(material.name, material);
    }

    /**
     * Rename a material
     *
     * @param material Material to rename
     * @param newName  New name
     */
    public static void rename(final @NotNull Material material, @NotNull String newName)
    {
        if (material == null)
        {
            throw new NullPointerException("material mustn't be null");
        }
        if (newName == null)
        {
            throw new NullPointerException("newName mustn't be null");
        }
        newName = newName.trim();
        if (newName.length() < 1)
        {
            throw new IllegalArgumentException("newName mustn't be empty");
        }
        if (material.name.equals(newName))
        {
            return;
        }
        Material.hashtableMaterials.remove(material.name);
        material.name = newName;
        Material.hashtableMaterials.put(newName, material);
    }

    /**
     * Ambiant color
     */
    private Color4f colorAmbient;
    /**
     * Diffuse color
     */
    private Color4f colorDiffuse;
    /**
     * Emissive color
     */
    private Color4f colorEmissive;
    /**
     * Specular color
     */
    private Color4f colorSpecular;
    /**
     * Cube map
     */
    private CubeMap cubeMap;
    /**
     * Rate for cube map
     */
    private float   cubeMapRate;
    /**
     * Material name
     */
    private String  name;
    /**
     * Shininess (0 <-> 128)
     */
    private int     shininess;
    /**
     * Specular level (0 <-> 1)
     */
    private float   specularLevel;
    /**
     * Rate for environment
     */
    private float   sphericRate;
    /**
     * Texture diffuse
     */
    private Texture textureDiffuse;
    /**
     * Texture environment
     */
    private Texture textureSpheric;
    /**
     * Transparency (0 <-> 1)
     */
    private float   transparency;
    /**
     * Indicates if the material is two sided
     */
    private boolean twoSided;

    /**
     * Constructs the material
     *
     * @param name Material name
     */
    public Material(@NotNull String name)
    {
        if (name == null)
        {
            throw new NullPointerException("name mustn't be null");
        }
        name = name.trim();
        if (name.length() < 1)
        {
            throw new IllegalArgumentException("name mustn't be empty");
        }
        this.name = name;
        this.colorAmbient = Color4f.BLACK.copy();
        this.colorDiffuse = Color4f.GRAY.copy();
        this.colorEmissive = Color4f.DARK_GRAY.copy();
        this.colorSpecular = Color4f.LIGHT_GRAY.copy();
        this.specularLevel = 0.1f;
        this.shininess = 12;
        this.transparency = 1f;
        this.twoSided = false;
        this.sphericRate = 1f;
        this.cubeMapRate = 1f;
        Material.registerMaterial(this);
    }

    /**
     * Render the material for a 3D object
     *
     * @param object3D Object to render
     */
    @ThreadOpenGL
    void renderMaterial(final Object3D object3D)
    {
        this.prepareMaterial();
        //
        if (this.textureDiffuse != null)
        {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            this.textureDiffuse.bind();
            object3D.drawObject();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
        }
        else
        {
            object3D.drawObject();
        }

        if (this.textureSpheric != null)
        {
            final float transparency = this.transparency;
            this.transparency *= this.sphericRate;
            //
            this.prepareMaterial();
            GL11.glDepthFunc(GL11.GL_LEQUAL);
            //
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_S);
            GL11.glEnable(GL11.GL_TEXTURE_GEN_T);
            //
            GL11.glTexGeni(GL11.GL_S, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP);
            GL11.glTexGeni(GL11.GL_T, GL11.GL_TEXTURE_GEN_MODE, GL11.GL_SPHERE_MAP);
            //
            this.textureSpheric.bind();
            object3D.drawObject();
            //
            GL11.glDisable(GL11.GL_TEXTURE_GEN_T);
            GL11.glDisable(GL11.GL_TEXTURE_GEN_S);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            //
            GL11.glDepthFunc(GL11.GL_LESS);
            //
            this.transparency = transparency;
        }

        if (this.cubeMap != null)
        {
            final float transparency = this.transparency;
            this.transparency *= this.cubeMapRate;

            this.prepareMaterial();
            GL11.glDepthFunc(GL11.GL_LEQUAL);

            this.cubeMap.bind();
            object3D.drawObject();
            this.cubeMap.endCubeMap();

            GL11.glDepthFunc(GL11.GL_LESS);
            this.transparency = transparency;
        }

        if (object3D.showWire())
        {
            GL11.glDisable(GL11.GL_LIGHTING);
            object3D.wireColor().glColor4f();
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);
            object3D.drawObject();
            GL11.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            GL11.glEnable(GL11.GL_LIGHTING);
        }
    }

    /**
     * Ambient color
     *
     * @return Ambient color
     */
    public @NotNull Color4f colorAmbient()
    {
        return this.colorAmbient;
    }

    /**
     * Change ambient color
     *
     * @param colorAmbient New ambient color
     */
    public void colorAmbient(@NotNull Color4f colorAmbient)
    {
        if (colorAmbient == null)
        {
            throw new NullPointerException("The colorAmbient couldn't be null");
        }
        if (colorAmbient.defaultColor())
        {
            colorAmbient = colorAmbient.copy();
        }
        this.colorAmbient = colorAmbient;
    }

    /**
     * Diffuse color
     *
     * @return Diffuse color
     */
    public @NotNull Color4f colorDiffuse()
    {
        return this.colorDiffuse;
    }

    /**
     * Change diffuse color
     *
     * @param colorDiffuse New diffuse color
     */
    public void colorDiffuse(@NotNull Color4f colorDiffuse)
    {
        if (colorDiffuse == null)
        {
            throw new NullPointerException("The colorDiffuse couldn't be null");
        }
        if (colorDiffuse.defaultColor())
        {
            colorDiffuse = colorDiffuse.copy();
        }
        this.colorDiffuse = colorDiffuse;
    }

    /**
     * Emissive color
     *
     * @return Emissive color
     */
    public @NotNull Color4f colorEmissive()
    {
        return this.colorEmissive;
    }

    /**
     * Change emissive color
     *
     * @param colorEmissive New emissive color
     */
    public void colorEmissive(@NotNull Color4f colorEmissive)
    {
        if (colorEmissive == null)
        {
            throw new NullPointerException("The colorEmissive couldn't be null");
        }
        if (colorEmissive.defaultColor())
        {
            colorEmissive = colorEmissive.copy();
        }
        this.colorEmissive = colorEmissive;
    }

    /**
     * Specular color
     *
     * @return Specular color
     */
    public @NotNull Color4f colorSpecular()
    {
        return this.colorSpecular;
    }

    /**
     * Change specular color
     *
     * @param colorSpecular New specular color
     */
    public void colorSpecular(@NotNull Color4f colorSpecular)
    {
        if (colorSpecular == null)
        {
            throw new NullPointerException("The colorSpecular couldn't be null");
        }
        if (colorSpecular.defaultColor())
        {
            colorSpecular = colorSpecular.copy();
        }
        this.colorSpecular = colorSpecular;
    }

    /**
     * Return cubeMap
     *
     * @return cubeMap
     */
    public @Nullable CubeMap cubeMap()
    {
        return this.cubeMap;
    }

    /**
     * Modify cubeMap
     *
     * @param cubeMap New cubeMap value
     */
    public void cubeMap(final @Nullable CubeMap cubeMap)
    {
        this.cubeMap = cubeMap;
    }

    /**
     * Return cubeMapRate
     *
     * @return cubeMapRate
     */
    public float cubeMapRate()
    {
        return this.cubeMapRate;
    }

    /**
     * Modify cubeMapRate
     *
     * @param cubeMapRate New cubeMapRate value
     */
    public void cubeMapRate(final float cubeMapRate)
    {
        this.cubeMapRate = cubeMapRate;
    }

    /**
     * Indicates if an Object is the same as the material
     *
     * @param object Object to compare
     * @return {@code true} if an Object is the same as the material
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object == null)
        {
            return false;
        }
        if (super.equals(object))
        {
            return true;
        }
        if (!(object instanceof Material))
        {
            return false;
        }
        final Material material = (Material) object;
        if (material.name.equals(this.name))
        {
            return true;
        }
        if (!material.colorAmbient.equals(this.colorAmbient))
        {
            return false;
        }
        if (!material.colorDiffuse.equals(this.colorDiffuse))
        {
            return false;
        }
        if (!material.colorEmissive.equals(this.colorEmissive))
        {
            return false;
        }
        if (!material.colorSpecular.equals(this.colorSpecular))
        {
            return false;
        }
        if (material.shininess != this.shininess)
        {
            return false;
        }
        if (material.twoSided != this.twoSided)
        {
            return false;
        }
        if (!Math3D.equal(material.specularLevel, this.specularLevel))
        {
            return false;
        }
        if (!Math3D.equal(material.sphericRate, this.sphericRate))
        {
            return false;
        }
        if (!Math3D.equal(material.transparency, this.transparency))
        {
            return false;
        }
        if (((this.textureDiffuse == null) && (material.textureDiffuse != null)) ||
            ((this.textureDiffuse != null) && (material.textureDiffuse == null)))
        {
            return false;
        }
        if ((this.textureDiffuse != null) && !this.textureDiffuse.equals(material.textureDiffuse))
        {
            return false;
        }

        if (((this.textureSpheric == null) && (material.textureSpheric != null)) ||
            ((this.textureSpheric != null) && (material.textureSpheric == null)))
        {
            return false;
        }

        return (this.textureSpheric == null) || this.textureSpheric.equals(material.textureSpheric);
    }

    /**
     * Material name
     *
     * @return Material name
     */
    public String name()
    {
        return this.name;
    }

    /**
     * Reset all settings to put as default
     */
    public void originalSettings()
    {
        this.colorAmbient = Color4f.BLACK.copy();
        this.colorDiffuse = Color4f.GRAY.copy();
        this.colorEmissive = Color4f.DARK_GRAY.copy();
        this.colorSpecular = Color4f.LIGHT_GRAY.copy();
        this.specularLevel = 0.1f;
        this.shininess = 12;
        this.transparency = 1f;
        this.twoSided = false;
        this.sphericRate = 1f;
        this.cubeMapRate = 1f;
    }

    /**
     * Prepare material for OpenGL render.<br>
     * Use by the renderer, don't call it directly
     */
    @ThreadOpenGL
    public void prepareMaterial()
    {
        if (this.twoSided)
        {
            GL11.glDisable(GL11.GL_CULL_FACE);
        }
        else
        {
            GL11.glEnable(GL11.GL_CULL_FACE);
        }
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        final float alpha = this.colorDiffuse.alpha();
        this.colorDiffuse.alpha(this.transparency);
        this.colorDiffuse.glColor4f();
        this.colorDiffuse.alpha(alpha);
        //
        GL11.glMaterialfv(GL11.GL_FRONT_AND_BACK, GL11.GL_DIFFUSE, this.colorDiffuse.putInFloatBuffer());
        //
        GL11.glMaterialfv(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, this.colorEmissive.putInFloatBuffer());
        //
        GL11.glMaterialfv(GL11.GL_FRONT_AND_BACK, GL11.GL_SPECULAR,
                          this.colorSpecular.putInFloatBuffer(this.specularLevel));
        //
        GL11.glMaterialfv(GL11.GL_FRONT_AND_BACK, GL11.GL_AMBIENT, this.colorAmbient.putInFloatBuffer());
        //
        GL11.glMateriali(GL11.GL_FRONT_AND_BACK, GL11.GL_SHININESS, this.shininess);
    }

    /**
     * Serialize the material in XML markup
     *
     * @return Markup represents the material
     */
    public @NotNull MarkupXML saveToXML()
    {
        final MarkupXML markupXML = new MarkupXML(ConstantsXML.MARKUP_MATERIAL);

        markupXML.addParameter(ConstantsXML.MARKUP_MATERIAL_colorAmbiant, this.colorAmbient.serialize());
        markupXML.addParameter(ConstantsXML.MARKUP_MATERIAL_colorDiffuse, this.colorDiffuse.serialize());
        markupXML.addParameter(ConstantsXML.MARKUP_MATERIAL_colorEmissive, this.colorEmissive.serialize());
        markupXML.addParameter(ConstantsXML.MARKUP_MATERIAL_colorSpecular, this.colorSpecular.serialize());
        markupXML.addParameter(ConstantsXML.MARKUP_MATERIAL_name, this.name);
        markupXML.addParameter(ConstantsXML.MARKUP_MATERIAL_shininess, this.shininess);
        markupXML.addParameter(ConstantsXML.MARKUP_MATERIAL_specularLevel, this.specularLevel);
        markupXML.addParameter(ConstantsXML.MARKUP_MATERIAL_sphericRate, this.sphericRate);

        if (this.textureDiffuse != null)
        {
            markupXML.addParameter(ConstantsXML.MARKUP_MATERIAL_textureDiffuse, this.textureDiffuse.textureName());
        }

        markupXML.addParameter(ConstantsXML.MARKUP_MATERIAL_transparency, this.transparency);

        if (this.textureSpheric != null)
        {
            markupXML.addParameter(ConstantsXML.MARKUP_MATERIAL_textureSpheric, this.textureSpheric.textureName());
        }

        markupXML.addParameter(ConstantsXML.MARKUP_MATERIAL_twoSided, this.twoSided);

        return markupXML;
    }

    /**
     * Do settings for 2D
     */
    public void settingAsFor2D()
    {
        this.colorEmissive.set(1f);
        this.specularLevel = 1f;
        this.shininess = 128;
        this.colorDiffuse.set(1f);
        this.colorSpecular.set();
        this.colorAmbient.set(1f);
        this.twoSided = true;
    }

    /**
     * Shininess
     *
     * @return Shininess
     */
    public int shininess()
    {
        return this.shininess;
    }

    /**
     * Change shininess (0 <-> 128)
     *
     * @param shininess New shininess (0 <-> 128)
     */
    public void shininess(final int shininess)
    {
        if ((shininess < 0) || (shininess > 128))
        {
            throw new IllegalArgumentException("The shininess must be on [0, 128], not : " + shininess);
        }
        this.shininess = shininess;
    }

    /**
     * Specular level
     *
     * @return Specular level
     */
    public float specularLevel()
    {
        return this.specularLevel;
    }

    /**
     * Change specular level
     *
     * @param specularLevel New specular level
     */
    public void specularLevel(final float specularLevel)
    {
        this.specularLevel = specularLevel;
    }

    /**
     * Environment rate
     *
     * @return Environment rate
     */
    public float sphericRate()
    {
        return this.sphericRate;
    }

    /**
     * Change environment rate
     *
     * @param sphericRate New environment rate
     */
    public void sphericRate(final float sphericRate)
    {
        this.sphericRate = sphericRate;
    }

    /**
     * Diffuse texture
     *
     * @return Diffuse texture
     */
    public @Nullable Texture textureDiffuse()
    {
        return this.textureDiffuse;
    }

    /**
     * Change diffuse texture<br>
     * Use {@code null} to remove diffuse texture
     *
     * @param textureDiffuse New diffuse texture
     */
    public void textureDiffuse(final @Nullable Texture textureDiffuse)
    {
        this.textureDiffuse = textureDiffuse;
    }

    /**
     * Environment texture
     *
     * @return Environment texture
     */
    public @Nullable Texture textureSpheric()
    {
        return this.textureSpheric;
    }

    /**
     * Change environment texture<br>
     * Use {@code null} to remove environment texture
     *
     * @param textureSpheric New environment texture
     */
    public void textureSpheric(final @Nullable Texture textureSpheric)
    {
        this.textureSpheric = textureSpheric;
    }

    /**
     * Transparency
     *
     * @return Transparency
     */
    public float transparency()
    {
        return this.transparency;
    }

    /**
     * Change transparency
     *
     * @param transparency New transparency
     */
    public void transparency(final float transparency)
    {
        this.transparency = transparency;
    }

    /**
     * Indicates if the material is two sided
     *
     * @return {@code true} if the material is two sided
     */
    public boolean twoSided()
    {
        return this.twoSided;
    }

    /**
     * Change two sided state
     *
     * @param twoSided New two sided state
     */
    public void twoSided(final boolean twoSided)
    {
        this.twoSided = twoSided;
    }
}
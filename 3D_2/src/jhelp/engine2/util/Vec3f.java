/*
 * Copy from JOGL
 *
 */

package jhelp.engine2.util;

import com.sun.javafx.geom.Vec3d;

public class Vec3f
{
    public static final Vec3f NEG_X_AXIS = new Vec3f(-1.0F, 0.0F, 0.0F);
    public static final Vec3f NEG_Y_AXIS = new Vec3f(0.0F, -1.0F, 0.0F);
    public static final Vec3f NEG_Z_AXIS = new Vec3f(0.0F, 0.0F, -1.0F);
    public static final Vec3f X_AXIS     = new Vec3f(1.0F, 0.0F, 0.0F);
    public static final Vec3f Y_AXIS     = new Vec3f(0.0F, 1.0F, 0.0F);
    public static final Vec3f Z_AXIS     = new Vec3f(0.0F, 0.0F, 1.0F);
    private float x;
    private float y;
    private float z;

    public Vec3f()
    {
    }

    public Vec3f(Vec3f var1)
    {
        this.set(var1);
    }

    public Vec3f(float var1, float var2, float var3)
    {
        this.set(var1, var2, var3);
    }

    public void add(Vec3f var1)
    {
        this.add(this, var1);
    }

    public void add(Vec3f var1, Vec3f var2)
    {
        this.x = var1.x + var2.x;
        this.y = var1.y + var2.y;
        this.z = var1.z + var2.z;
    }

    public Vec3f addScaled(float var1, Vec3f var2)
    {
        Vec3f var3 = new Vec3f();
        var3.addScaled(this, var1, var2);
        return var3;
    }

    public void addScaled(Vec3f var1, float var2, Vec3f var3)
    {
        this.x = var1.x + var2 * var3.x;
        this.y = var1.y + var2 * var3.y;
        this.z = var1.z + var2 * var3.z;
    }

    public void componentMul(Vec3f var1)
    {
        this.x *= var1.x;
        this.y *= var1.y;
        this.z *= var1.z;
    }

    public Vec3f copy()
    {
        return new Vec3f(this);
    }

    public Vec3f cross(Vec3f var1)
    {
        Vec3f var2 = new Vec3f();
        var2.cross(this, var1);
        return var2;
    }

    public void cross(Vec3f var1, Vec3f var2)
    {
        this.x = var1.y * var2.z - var1.z * var2.y;
        this.y = var1.z * var2.x - var1.x * var2.z;
        this.z = var1.x * var2.y - var1.y * var2.x;
    }

    public float dot(Vec3f var1)
    {
        return this.x * var1.x + this.y * var1.y + this.z * var1.z;
    }

    public float get(int var1)
    {
        switch (var1)
        {
            case 0:
                return this.x;
            case 1:
                return this.y;
            case 2:
                return this.z;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    public float length()
    {
        return (float) Math.sqrt((double) this.lengthSquared());
    }

    public float lengthSquared()
    {
        return this.dot(this);
    }

    public Vec3f minus(Vec3f var1)
    {
        Vec3f var2 = new Vec3f();
        var2.sub(this, var1);
        return var2;
    }

    public void normalize()
    {
        float var1 = this.length();
        if (var1 != 0.0F)
        {
            this.scale(1.0F / var1);
        }
    }

    public Vec3f plus(Vec3f var1)
    {
        Vec3f var2 = new Vec3f();
        var2.add(this, var1);
        return var2;
    }

    public void scale(float var1)
    {
        this.x *= var1;
        this.y *= var1;
        this.z *= var1;
    }

    public void set(Vec3f var1)
    {
        this.set(var1.x, var1.y, var1.z);
    }

    public void set(float var1, float var2, float var3)
    {
        this.x = var1;
        this.y = var2;
        this.z = var3;
    }

    public void set(int var1, float var2)
    {
        switch (var1)
        {
            case 0:
                this.x = var2;
                break;
            case 1:
                this.y = var2;
                break;
            case 2:
                this.z = var2;
                break;
            default:
                throw new IndexOutOfBoundsException();
        }

    }

    public void setX(float var1)
    {
        this.x = var1;
    }

    public void setY(float var1)
    {
        this.y = var1;
    }

    public void setZ(float var1)
    {
        this.z = var1;
    }

    public void sub(Vec3f var1)
    {
        this.sub(this, var1);
    }

    public void sub(Vec3f var1, Vec3f var2)
    {
        this.x = var1.x - var2.x;
        this.y = var1.y - var2.y;
        this.z = var1.z - var2.z;
    }

    public Vec3f times(float var1)
    {
        Vec3f var2 = new Vec3f(this);
        var2.scale(var1);
        return var2;
    }

    public Vec3d toDouble()
    {
        return new Vec3d((double) this.x, (double) this.y, (double) this.z);
    }

    public String toString()
    {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    public float x()
    {
        return this.x;
    }

    public float y()
    {
        return this.y;
    }

    public float z()
    {
        return this.z;
    }
}

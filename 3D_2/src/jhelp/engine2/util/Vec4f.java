/*
 *Copy from JOGL
 *
 */

package jhelp.engine2.util;

public class Vec4f
{
    private float w;
    private float x;
    private float y;
    private float z;

    public Vec4f()
    {
    }

    public Vec4f(Vec4f var1)
    {
        this.set(var1);
    }

    public Vec4f(float var1, float var2, float var3, float var4)
    {
        this.set(var1, var2, var3, var4);
    }

    public void add(Vec4f var1)
    {
        this.add(this, var1);
    }

    public void add(Vec4f var1, Vec4f var2)
    {
        this.x = var1.x + var2.x;
        this.y = var1.y + var2.y;
        this.z = var1.z + var2.z;
        this.w = var1.w + var2.w;
    }

    public Vec4f addScaled(float var1, Vec4f var2)
    {
        Vec4f var3 = new Vec4f();
        var3.addScaled(this, var1, var2);
        return var3;
    }

    public void addScaled(Vec4f var1, float var2, Vec4f var3)
    {
        this.x = var1.x + var2 * var3.x;
        this.y = var1.y + var2 * var3.y;
        this.z = var1.z + var2 * var3.z;
        this.w = var1.w + var2 * var3.w;
    }

    public void componentMul(Vec4f var1)
    {
        this.x *= var1.x;
        this.y *= var1.y;
        this.z *= var1.z;
        this.w *= var1.w;
    }

    public Vec4f copy()
    {
        return new Vec4f(this);
    }

    public float dot(Vec4f var1)
    {
        return this.x * var1.x + this.y * var1.y + this.z * var1.z + this.w * var1.w;
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
            case 3:
                return this.w;
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

    public Vec4f minus(Vec4f var1)
    {
        Vec4f var2 = new Vec4f();
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

    public Vec4f plus(Vec4f var1)
    {
        Vec4f var2 = new Vec4f();
        var2.add(this, var1);
        return var2;
    }

    public void scale(float var1)
    {
        this.x *= var1;
        this.y *= var1;
        this.z *= var1;
        this.w *= var1;
    }

    public void set(Vec4f var1)
    {
        this.set(var1.x, var1.y, var1.z, var1.w);
    }

    public void set(float var1, float var2, float var3, float var4)
    {
        this.x = var1;
        this.y = var2;
        this.z = var3;
        this.w = var4;
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
            case 3:
                this.w = var2;
                break;
            default:
                throw new IndexOutOfBoundsException();
        }

    }

    public void setW(float var1)
    {
        this.w = var1;
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

    public void sub(Vec4f var1)
    {
        this.sub(this, var1);
    }

    public void sub(Vec4f var1, Vec4f var2)
    {
        this.x = var1.x - var2.x;
        this.y = var1.y - var2.y;
        this.z = var1.z - var2.z;
        this.w = var1.w - var2.w;
    }

    public Vec4f times(float var1)
    {
        Vec4f var2 = new Vec4f(this);
        var2.scale(var1);
        return var2;
    }

    public String toString()
    {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    public float w()
    {
        return this.w;
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

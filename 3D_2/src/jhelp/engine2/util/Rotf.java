/*
 * Copy from JOGL
 *
 */

package jhelp.engine2.util;

public class Rotf
{
    private static float EPSILON = 1.0E-7F;
    private float q0;
    private float q1;
    private float q2;
    private float q3;

    public Rotf()
    {
        this.init();
    }

    public Rotf(Rotf var1)
    {
        this.set(var1);
    }

    public Rotf(Vec3f var1, float var2)
    {
        this.set(var1, var2);
    }

    public Rotf(Vec3f var1, Vec3f var2)
    {
        this.set(var1, var2);
    }

    private void setQ(int var1, float var2)
    {
        switch (var1)
        {
            case 0:
                this.q0 = var2;
                break;
            case 1:
                this.q1 = var2;
                break;
            case 2:
                this.q2 = var2;
                break;
            case 3:
                this.q3 = var2;
                break;
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    public float get(Vec3f var1)
    {
        float var2 = (float) (2.0D * Math.acos((double) this.q0));
        var1.set(this.q1, this.q2, this.q3);
        float var3 = var1.length();
        if (var3 == 0.0F)
        {
            var1.set(0.0F, 0.0F, 1.0F);
        }
        else
        {
            var1.scale(1.0F / var3);
        }

        return var2;
    }

    public void init()
    {
        this.q0 = 1.0F;
        this.q1 = this.q2 = this.q3 = 0.0F;
    }

    public Rotf inverse()
    {
        Rotf var1 = new Rotf(this);
        var1.invert();
        return var1;
    }

    public void invert()
    {
        this.q1 = -this.q1;
        this.q2 = -this.q2;
        this.q3 = -this.q3;
    }

    public float length()
    {
        return (float) Math.sqrt((double) this.lengthSquared());
    }

    public float lengthSquared()
    {
        return this.q0 * this.q0 + this.q1 * this.q1 + this.q2 * this.q2 + this.q3 * this.q3;
    }

    public void mul(Rotf var1, Rotf var2)
    {
        this.q0 = var1.q0 * var2.q0 - var1.q1 * var2.q1 - var1.q2 * var2.q2 - var1.q3 * var2.q3;
        this.q1 = var1.q0 * var2.q1 + var1.q1 * var2.q0 + var1.q2 * var2.q3 - var1.q3 * var2.q2;
        this.q2 = var1.q0 * var2.q2 + var1.q2 * var2.q0 - var1.q1 * var2.q3 + var1.q3 * var2.q1;
        this.q3 = var1.q0 * var2.q3 + var1.q3 * var2.q0 + var1.q1 * var2.q2 - var1.q2 * var2.q1;
    }

    public void normalize()
    {
        float var1 = this.length();
        this.q0 /= var1;
        this.q1 /= var1;
        this.q2 /= var1;
        this.q3 /= var1;
    }

    public void rotateVector(Vec3f var1, Vec3f var2)
    {
        Vec3f var3 = new Vec3f(this.q1, this.q2, this.q3);
        Vec3f var4 = var3.cross(var1);
        Vec3f var5 = var4.cross(var3);
        var4.scale(2.0F * this.q0);
        var5.scale(-2.0F);
        var2.add(var1, var4);
        var2.add(var2, var5);
    }

    public Vec3f rotateVector(Vec3f var1)
    {
        Vec3f var2 = new Vec3f();
        this.rotateVector(var1, var2);
        return var2;
    }

    public void set(Vec3f var1, float var2)
    {
        float var3 = var2 / 2.0F;
        this.q0 = (float) Math.cos((double) var3);
        float var4 = (float) Math.sin((double) var3);
        Vec3f var5 = new Vec3f(var1);
        var5.normalize();
        this.q1 = var5.x() * var4;
        this.q2 = var5.y() * var4;
        this.q3 = var5.z() * var4;
    }

    public void set(Rotf var1)
    {
        this.q0 = var1.q0;
        this.q1 = var1.q1;
        this.q2 = var1.q2;
        this.q3 = var1.q3;
    }

    public void set(Vec3f var1, Vec3f var2)
    {
        Vec3f var3 = var1.cross(var2);
        if (var3.lengthSquared() < Rotf.EPSILON)
        {
            this.init();
        }
        else
        {
            float var4 = var1.dot(var2);
            float var5 = var1.length() * var2.length();
            if (var5 < Rotf.EPSILON)
            {
                this.init();
            }
            else
            {
                var4 /= var5;
                this.set(var3, (float) Math.acos((double) var4));
            }
        }
    }

    public Rotf times(Rotf var1)
    {
        Rotf var2 = new Rotf();
        var2.mul(this, var1);
        return var2;
    }

    public String toString()
    {
        return "(" + this.q0 + ", " + this.q1 + ", " + this.q2 + ", " + this.q3 + ")";
    }

    public boolean withinEpsilon(Rotf var1, float var2)
    {
        return Math.abs(this.q0 - var1.q0) < var2 && Math.abs(this.q1 - var1.q1) < var2 &&
               Math.abs(this.q2 - var1.q2) < var2 && Math.abs(this.q3 - var1.q3) < var2;
    }
}

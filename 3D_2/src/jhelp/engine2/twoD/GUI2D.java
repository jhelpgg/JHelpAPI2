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
package jhelp.engine2.twoD;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import jhelp.util.thread.Mutex;
import jhelp.util.thread.Pointer;

/**
 * 2D manager.<br>
 * It have two layer, one under the 3D, other over the 3D <br>
 *
 * @author JHelp
 */
public class GUI2D
{
    /**
     * Over 3D layer
     */
    private final List<Object2D> arrayListObject2DOver3D;
    /**
     * Under 3D layer
     */
    private final List<Object2D> arrayListObject2DUnder3D;
    /**
     * If not {@code null} only this object can be detected
     */
    private       Object2D       exclusiveObject;
    /**
     * Mutex for synchronization
     */
    private final Mutex          mutex;

    /**
     * Constructs GUI2D
     */
    public GUI2D()
    {
        this.mutex = new Mutex();
        this.arrayListObject2DOver3D = new ArrayList<>();
        this.arrayListObject2DUnder3D = new ArrayList<>();
    }

    /**
     * Add object over the 3D
     *
     * @param object2D Object to add
     */
    public void addOver3D(final @NotNull Object2D object2D)
    {
        this.mutex.playInCriticalSectionVoid(() -> this.arrayListObject2DOver3D.add(object2D));
    }

    /**
     * Add object under 3D
     *
     * @param object2D Object to add
     */
    public void addUnder3D(final Object2D object2D)
    {
        this.mutex.playInCriticalSectionVoid(() -> this.arrayListObject2DUnder3D.add(object2D));
    }

    /**
     * Put detection to normal, that is to say all objects will be able to be detected
     */
    public void allCanBeDetected()
    {
        this.mutex.playInCriticalSectionVoid(() -> this.exclusiveObject = null);
    }

    /**
     * Clear the GUI2D, all over and under 3D, 2D objects are removed
     */
    public void clearAll()
    {
        this.mutex.playInCriticalSectionVoid(() ->
                                             {
                                                 this.arrayListObject2DUnder3D.clear();
                                                 this.arrayListObject2DOver3D.clear();
                                             });
    }

    /**
     * Remove all over 3D, 2D objects
     */
    public void clearOver3D()
    {
        this.mutex.playInCriticalSectionVoid(this.arrayListObject2DOver3D::clear);
    }

    /**
     * Remove all under 3D, 2D objects
     */
    public void clearUnder3D()
    {
        this.mutex.playInCriticalSectionVoid(this.arrayListObject2DUnder3D::clear);
    }

    /**
     * Looking for an object over 3D and under a position
     *
     * @param x X
     * @param y Y
     * @return The found object or {@code null}
     */
    public @Nullable Object2D detectOver3D(final int x, final int y)
    {
        return this.mutex.playInCriticalSection(() ->
                                                {
                                                    if (this.exclusiveObject != null)
                                                    {
                                                        if (this.exclusiveObject.detected(x, y))
                                                        {
                                                            return this.exclusiveObject;
                                                        }

                                                        return null;
                                                    }

                                                    final int nb = this.arrayListObject2DOver3D.size();
                                                    Object2D  object2D;

                                                    for (int i = nb - 1; i >= 0; i--)
                                                    {
                                                        object2D = this.arrayListObject2DOver3D.get(i);

                                                        if (object2D.detected(x, y))
                                                        {
                                                            return object2D;
                                                        }
                                                    }

                                                    return null;
                                                });
    }

    /**
     * Looking for an object under a position
     *
     * @param x X
     * @param y Y
     * @return The found object or {@code null}
     */
    public @Nullable Object2D detectOver3DorUnder3D(final int x, final int y)
    {
        final AtomicBoolean     shouldReturn = new AtomicBoolean(false);
        final Pointer<Object2D> valueReturn  = new Pointer<>();

        this.mutex.playInCriticalSectionVoid(() ->
                                             {
                                                 if (this.exclusiveObject != null)
                                                 {
                                                     shouldReturn.set(true);

                                                     if (this.exclusiveObject.detected(x, y))
                                                     {
                                                         valueReturn.data(this.exclusiveObject);
                                                     }
                                                 }
                                             });

        if (shouldReturn.get())
        {
            return valueReturn.data();
        }

        // search over first
        final Object2D object2D = this.detectOver3D(x, y);

        if (object2D != null)
        {
            return object2D;
        }

        return this.mutex.playInCriticalSection(() ->
                                                {
                                                    // Search under
                                                    final int nb = this.arrayListObject2DUnder3D.size();
                                                    Object2D  object;

                                                    for (int i = nb - 1; i >= 0; i--)
                                                    {
                                                        object = this.arrayListObject2DUnder3D.get(i);

                                                        if (object.detected(x, y))
                                                        {
                                                            return object;
                                                        }
                                                    }

                                                    return null;
                                                });
    }

    /**
     * For the detection restricted to only one object<br>
     * This object will be the only one detected
     *
     * @param object2d Object to detect exclusively (Can use {@code null} for detect all objects)
     */
    public void exclusiveDetection(final @Nullable Object2D object2d)
    {
        this.mutex.playInCriticalSectionVoid(() -> this.exclusiveObject = object2d);
    }

    /**
     * Iterator for list all objects over 3D
     *
     * @return Iterator for list all objects over 3D
     */
    public Iterator<Object2D> iteratorOver3D()
    {
        return this.mutex.playInCriticalSection(this.arrayListObject2DOver3D::iterator);
    }

    /**
     * Iterator for list all objects under 3D
     *
     * @return Iterator for list all objects under 3D
     */
    public Iterator<Object2D> iteratorUnder3D()
    {
        return this.mutex.playInCriticalSection(this.arrayListObject2DUnder3D::iterator);
    }

    /**
     * Call when mouse state changed
     *
     * @param x           Mouse X
     * @param y           Mouse Y
     * @param buttonLeft  Indicates if the button left is down
     * @param buttonRight Indicates if the button right is down
     * @param drag        Indicates if we are on drag mode
     * @param over        Object under the mouse
     */
    public void mouseState(
            final int x, final int y, final boolean buttonLeft, final boolean buttonRight, final boolean drag,
            final @Nullable Object2D over)
    {
        this.mutex.playInCriticalSectionVoid(() ->
                                             {
                                                 for (final Object2D object2D : this.arrayListObject2DUnder3D)
                                                 {
                                                     object2D.mouseState(x, y, buttonLeft, buttonRight, drag,
                                                                         over == object2D);
                                                 }

                                                 for (final Object2D object2D : this.arrayListObject2DOver3D)
                                                 {
                                                     object2D.mouseState(x, y, buttonLeft, buttonRight, drag,
                                                                         over == object2D);
                                                 }
                                             });
    }

    /**
     * Remove object over the 3D
     *
     * @param object2D Object to remove
     */
    public void removeOver3D(final @NotNull Object2D object2D)
    {
        this.mutex.playInCriticalSectionVoid(() -> this.arrayListObject2DOver3D.remove(object2D));
    }

    /**
     * Remove object under 3D
     *
     * @param object2D Object to remove
     */
    public void removeUnder3D(final @NotNull Object2D object2D)
    {
        this.mutex.playInCriticalSectionVoid(() -> this.arrayListObject2DUnder3D.remove(object2D));
    }
}
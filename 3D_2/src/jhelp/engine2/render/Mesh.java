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
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import jhelp.engine2.io.ConstantsXML;
import jhelp.engine2.io.obj.ObjLoader;
import jhelp.engine2.util.BarycenterPoint3D;
import jhelp.engine2.util.Math3D;
import jhelp.engine2.util.Triangle;
import jhelp.engine2.util.Triangles;
import jhelp.util.debug.Debug;
import jhelp.util.list.ArrayInt;
import jhelp.util.list.EnumerationIterator;
import jhelp.util.text.UtilText;
import jhelp.util.thread.ConsumerTask;
import jhelp.xml.MarkupXML;
import org.lwjgl.opengl.GL11;

/**
 * Object's mesh
 *
 * @author JHelp
 */
public class Mesh
{
    /**
     * Indicates if have to print "OUPS" messages (Unexpected error while rendering)
     */
    private static final boolean PRINT_OUPS = false;

    /**
     * Compute UV in separate process
     */
    static final ConsumerTask<Object3D> delayedComputeUV = object ->
    {
        Debug.verbose("Just flush");                             // "Delayed
        // compute
        // for OBJ start");
        // LapsTime.startMeasure();
        // object.computeUVfromMax(1, 1);
        // final LapsTime lapsTime = LapsTime.endMeasure();
        // Debug.println(DebugLevel.VERBOSE,
        // "Delayed compute for OBJ END. Time=", lapsTime);

        object.flush();
    };

    /**
     * Fill an object with a format OBJ informations
     *
     * @param object3D    Object to fill
     * @param points      Points of object
     * @param normals     Normals
     * @param uv          UV list
     * @param pointFace   Faces description
     * @param normalFace  Normals face reference
     * @param uvFace      UV face references
     * @param startPoint  Offset where start read points
     * @param startUV     Offset where start read UV
     * @param startNormal Offset where start read normals
     * @see ObjLoader
     */
    public static void fillObjectOBJ(
            final @NotNull Object3D object3D,
            final @NotNull List<Point3D> points, final @NotNull List<Point3D> normals, final @NotNull List<Point2D> uv,
            final @NotNull ArrayInt pointFace, final @NotNull ArrayInt normalFace, final @NotNull ArrayInt uvFace,
            final int startPoint, final int startUV, final int startNormal)
    {
        final Mesh mesh = object3D.mesh;

        mesh.points.clear();
        mesh.normals.clear();
        mesh.uv.clear();

        mesh.points.addAll(points);
        mesh.normals.addAll(normals);
        mesh.uv.addAll(uv);

        mesh.facesPoints.clear();
        mesh.facesNormals.clear();
        mesh.facesUV.clear();

        mesh.actualFacePoints = new ArrayInt();
        mesh.actualFaceNormals = new ArrayInt();
        mesh.actualFaceUV = new ArrayInt();

        mesh.facesPoints.add(mesh.actualFacePoints);
        mesh.facesNormals.add(mesh.actualFaceNormals);
        mesh.facesUV.add(mesh.actualFaceUV);

        final int length = pointFace.getSize();
        int       index  = 0;

        final boolean hasUV = uvFace.getSize() > 0;
        if (!hasUV)
        {
            mesh.uv.add(new Point2D());
        }

        final boolean hasNormal = normalFace.getSize() > 0;
        if (!hasNormal)
        {
            mesh.normals.add(new Point3D());
        }

        int code;
        for (int i = 0; i < length; i++)
        {
            code = pointFace.getInteger(i);

            if (code >= 0)
            {
                mesh.actualFacePoints.insert(code - startPoint, 0);

                if (hasUV)
                {
                    mesh.actualFaceUV.insert(uvFace.getInteger(index) - startUV, 0);
                }
                else
                {
                    mesh.actualFaceUV.insert(0, 0);
                }

                if (hasNormal)
                {
                    mesh.actualFaceNormals.insert(normalFace.getInteger(index) - startNormal, 0);
                }
                else
                {
                    mesh.actualFaceNormals.insert(0, 0);
                }

                index++;
            }
            else
            {
                mesh.actualFacePoints = new ArrayInt();
                mesh.actualFaceNormals = new ArrayInt();
                mesh.actualFaceUV = new ArrayInt();

                mesh.facesPoints.add(mesh.actualFacePoints);
                mesh.facesNormals.add(mesh.actualFaceNormals);
                mesh.facesUV.add(mesh.actualFaceUV);
            }
        }

        if (!hasUV)
        {
            Debug.warning("No uv !");

            // Debug.println(DebugLevel.VERBOSE, "Delayed compute for OBJ
            // start");
            // LapsTime.startMeasure();
            // try
            // {
            // object3D.computeUVfromMax(1, 1);
            // }
            // catch(final Exception exception)
            // {
            // Debug.printException(exception);
            // }
            // catch(final Error error)
            // {
            // Debug.printError(error);
            // }
            // final LapsTime lapsTime = LapsTime.endMeasure();
            // Debug.println(DebugLevel.VERBOSE, "Delayed compute for OBJ END.
            // Time=", lapsTime);
            //
            // object3D.flush();
            //
            // ThreadManager.THREAD_MANAGER.delayedThread(Mesh.delayedComputeUV,
            // object3D, 1234L);
        }

        if (!hasNormal)
        {
            Debug.warning("No normals !");

        }

        mesh.mayBeUnvalid = false;

        object3D.flush();
    }

    /**
     * Actual face normals
     */
    private ArrayInt       actualFaceNormals;
    /**
     * Actual face points
     */
    private ArrayInt       actualFacePoints;
    /**
     * Actual face UV
     */
    private ArrayInt       actualFaceUV;
    /**
     * Bounding box
     */
    private VirtualBox     box;
    /**
     * Face of normals set
     */
    private List<ArrayInt> facesNormals;
    /**
     * Face of points set
     */
    private List<ArrayInt> facesPoints;
    /**
     * Face of UV set
     */
    private List<ArrayInt> facesUV;
    /**
     * Indicates if the mesh could be unvalid
     */
    private boolean        mayBeUnvalid;
    /**
     * Normals set
     */
    private List<Point3D>  normals;

    /**
     * Points set
     */
    private List<Point3D> points;

    /**
     * UV set
     */
    private List<Point2D> uv;

    /**
     * Constructs an empty mesh
     */
    public Mesh()
    {
        this.points = new ArrayList<>();
        this.uv = new ArrayList<>();
        this.normals = new ArrayList<>();
        //
        this.facesPoints = new ArrayList<>();
        this.facesUV = new ArrayList<>();
        this.facesNormals = new ArrayList<>();
        //
        this.actualFacePoints = new ArrayInt();
        this.actualFaceUV = new ArrayInt();
        this.actualFaceNormals = new ArrayInt();
        //
        this.facesPoints.add(this.actualFacePoints);
        this.facesUV.add(this.actualFaceUV);
        this.facesNormals.add(this.actualFaceNormals);
        //
        this.mayBeUnvalid = false;
        this.box = new VirtualBox();
    }

    /**
     * Automatic compute of UV for a face.<br>
     * This compute choose the XY, XZ or YZ plane
     *
     * @param facePoints Face point
     * @param faceUV     Face UV
     * @param multU      Multiply U
     * @param multV      Multiply V
     */
    private void computeUVfromMax(
            final @NotNull ArrayInt facePoints, final @NotNull ArrayInt faceUV, final float multU, final float multV)
    {
        VirtualBox box = new VirtualBox();
        final int  nb  = facePoints.getSize();
        for (int i = 0; i < nb; i++)
        {
            box.add(this.points.get(facePoints.getInteger(i)));
        }

        Debug.verbose(box);

        final float minX = box.minX();
        final float minY = box.minY();
        final float minZ = box.minZ();
        final float maxX = box.maxX();
        final float maxY = box.maxY();
        final float maxZ = box.maxZ();
        final float gapX = maxX - minX;
        final float gapY = maxY - minY;
        final float gapZ = maxZ - minZ;
        //
        this.box = null;
        box = this.computeBox();

        if ((gapX >= gapZ) && (gapY >= gapZ))
        {
            this.computeUVfromPlaneXY(box, facePoints, faceUV, multU, multV);
            return;
        }
        if ((gapY >= gapX) && (gapZ >= gapX))
        {
            this.computeUVfromPlaneYZ(box, facePoints, faceUV, multU, multV);
            return;
        }
        if ((gapX >= gapY) && (gapZ >= gapY))
        {
            this.computeUVfromPlaneXZ(box, facePoints, faceUV, multU, multV);
            return;
        }
        this.computeUVfromPlaneXY(box, facePoints, faceUV, multU, multV);
    }

    /**
     * Automatic compute of UV for a face.<br>
     * This compute use the XY plane
     *
     * @param box        Bounding box
     * @param facePoints Face point
     * @param faceUV     Face UV
     * @param multU      Multiply U
     * @param multV      Multiply V
     */
    private void computeUVfromPlaneXY(
            final @NotNull VirtualBox box,
            final @NotNull ArrayInt facePoints, final @NotNull ArrayInt faceUV,
            final float multU, final float multV)
    {
        Debug.verbose(box);
        final float minX = box.minX();
        final float minY = box.minY();
        final float maxX = box.maxX();
        final float maxY = box.maxY();
        final float gapX = maxX - minX;
        final float gapY = maxY - minY;
        faceUV.clear();
        final int nb = facePoints.getSize();
        int       index;
        Point3D   point;
        Point2D   uv;
        for (int i = 0; i < nb; i++)
        {
            point = this.points.get(facePoints.getInteger(i));
            uv = new Point2D(//
                             multU * ((point.x() - minX) / gapX), //
                             multV * ((point.y() - minY) / gapY));
            index = this.uv.indexOf(uv);
            if (index < 0)
            {
                index = this.uv.size();
                this.uv.add(uv);
            }
            faceUV.add(index);
        }
    }

    /**
     * Automatic compute of UV far a face.<br>
     * This compute use the XZ plane
     *
     * @param box        Bounding box
     * @param facePoints Face point
     * @param faceUV     Face UV
     * @param multU      Multiply U
     * @param multV      Multiply V
     */
    private void computeUVfromPlaneXZ(
            final @NotNull VirtualBox box,
            final @NotNull ArrayInt facePoints, final @NotNull ArrayInt faceUV,
            final float multU, final float multV)
    {
        Debug.verbose(box);
        final float minX = box.minX();
        final float minZ = box.minZ();
        final float maxX = box.maxX();
        final float maxZ = box.maxZ();
        final float gapX = maxX - minX;
        final float gapZ = maxZ - minZ;
        faceUV.clear();
        final int nb = facePoints.getSize();
        int       index;
        Point3D   point;
        Point2D   uv;
        for (int i = 0; i < nb; i++)
        {
            point = this.points.get(facePoints.getInteger(i));
            uv = new Point2D(//
                             multU * ((point.x() - minX) / gapX), //
                             multV * ((point.z() - minZ) / gapZ));
            index = this.uv.indexOf(uv);
            if (index < 0)
            {
                index = this.uv.size();
                this.uv.add(uv);
            }
            faceUV.add(index);
        }
    }

    /**
     * Automatic compute of UV far a face.<br>
     * This compute use the YZ plane
     *
     * @param box        Bounding box
     * @param facePoints Face point
     * @param faceUV     Face UV
     * @param multU      Multiply U
     * @param multV      Multiply V
     */
    private void computeUVfromPlaneYZ(
            final @NotNull VirtualBox box,
            final @NotNull ArrayInt facePoints, final @NotNull ArrayInt faceUV,
            final float multU, final float multV)
    {
        Debug.verbose(box);
        final float minY = box.minY();
        final float minZ = box.minZ();
        final float maxY = box.maxY();
        final float maxZ = box.maxZ();
        final float gapY = maxY - minY;
        final float gapZ = maxZ - minZ;
        faceUV.clear();
        final int nb = facePoints.getSize();
        int       index;
        Point3D   point;
        Point2D   uv;
        for (int i = 0; i < nb; i++)
        {
            point = this.points.get(facePoints.getInteger(i));
            uv = new Point2D(//
                             multU * ((point.y() - minY) / gapY), //
                             multV * ((point.z() - minZ) / gapZ));
            index = this.uv.indexOf(uv);
            if (index < 0)
            {
                index = this.uv.size();
                this.uv.add(uv);
            }
            faceUV.add(index);
        }
    }

    /**
     * Automatic compute of UV for a face.<br>
     * This compute is spherical
     *
     * @param sphere     Bounding sphere
     * @param facePoints Face points
     * @param faceUV     Face UV
     * @param multU      Multiply U
     * @param multV      Multiply V
     */
    private void computeUVspherical(
            final @NotNull VirtualSphere sphere,
            final @NotNull ArrayInt facePoints, final @NotNull ArrayInt faceUV,
            final float multU, final float multV)
    {
        float length;
        faceUV.clear();
        final int     nb     = facePoints.getSize();
        int           index;
        Point3D       point;
        Point2D       uv;
        final Point3D center = sphere.center();
        for (int i = 0; i < nb; i++)
        {
            point = this.points.get(facePoints.getInteger(i)).substract(center);
            length = point.length();
            uv = new Point2D(//
                             multU * (float) ((Math.atan2(point.y(), point.x()) + Math.PI) / (2d * Math.PI)), //
                             multV * (float) (Math.acos(point.z() / length) / Math.PI));
            // Debug.println("Mesh.computeUVspherical(" + uv + ")");
            index = this.uv.indexOf(uv);
            if (index < 0)
            {
                index = this.uv.size();
                this.uv.add(uv);
            }
            faceUV.add(index);
        }
    }

    /**
     * Create a copy of an array of integers
     *
     * @param list Array to copy
     * @return Copy
     */
    private @NotNull List<ArrayInt> copy(final @NotNull List<ArrayInt> list)
    {
        final ArrayList<ArrayInt> copy = new ArrayList<>();

        for (final ArrayInt arrayInt : list)
        {
            copy.add(arrayInt.createCopy());
        }

        copy.trimToSize();
        return Collections.unmodifiableList(copy);
    }

    /**
     * Create a face markup
     *
     * @param face Face to convert
     * @return Created markup
     */
    private @NotNull MarkupXML createFaceXML(final @NotNull ArrayInt face)
    {
        final MarkupXML     faceXML       = new MarkupXML(ConstantsXML.MARKUP_FACE);
        final StringBuilder stringBuilder = new StringBuilder();

        final int length = face.getSize();
        for (int index = 0; index < length; index++)
        {
            stringBuilder.append(face.getInteger(index));
            stringBuilder.append(' ');
        }

        faceXML.setText(stringBuilder.toString());

        return faceXML;
    }

    /**
     * Translate a vertex in the mesh.<br>
     * This translation can translate neighbor vertex, the translation apply to them depends of the solidity.<br>
     * If you specify a 0 solidity, then neighbor don't move, 1, all vertex translate in the same translate, some where between,
     * the object morph
     *
     * @param indexPoint Vertex index to translate
     * @param forbiden   List of vertex index to not consider (They have already move)
     * @param vx         X
     * @param vy         Y
     * @param vz         Z
     * @param solidity   Solidity
     * @param near       Level of neighbor move with specified point. 0 the point, 1 : one level neighbor, ...
     */
    private void internMovePoint(
            final int indexPoint, final @NotNull ArrayInt forbiden,
            float vx, float vy, float vz,
            final float solidity, int near)
    {
        // Move the vertex
        this.points.get(indexPoint).translate(vx, vy, vz);
        ArrayInt neighbors = new ArrayInt();

        // Collect vertex neighbors able to move
        int     index;
        boolean isGoodFace;
        for (final ArrayInt arrayInt : this.facesPoints)
        {
            isGoodFace = false;
            for (int i = arrayInt.getSize() - 1; i >= 0; i--)
            {
                if (arrayInt.getInteger(i) == indexPoint)
                {
                    isGoodFace = true;
                    break;
                }
            }

            if (isGoodFace)
            {
                for (int i = arrayInt.getSize() - 1; i >= 0; i--)
                {
                    index = arrayInt.getInteger(i);
                    if (!forbiden.contains(index))
                    {
                        forbiden.add(index);
                        neighbors.add(index);
                    }
                }
            }
        }

        // While there are vertex to move
        ArrayInt temp;
        int      neig;
        float    solid = 1f;
        while (neighbors.getSize() > 0)
        {
            // Compute the new translation
            if (near > 0)
            {
                near--;
            }
            else
            {
                solid *= Math3D.linearToSinusoidal(solidity);

                vx *= solid;
                vy *= solid;
                vz *= solid;
            }

            temp = new ArrayInt();
            // For each neighbor
            for (int i = neighbors.getSize() - 1; i >= 0; i--)
            {
                // Translate the neighbor
                neig = neighbors.getInteger(i);
                this.points.get(neig).translate(vx, vy, vz);

                // Collect neighbor's neighbors able to move
                for (final ArrayInt arrayInt : this.facesPoints)
                {
                    isGoodFace = false;
                    for (int ii = arrayInt.getSize() - 1; ii >= 0; ii--)
                    {
                        if (arrayInt.getInteger(ii) == neig)
                        {
                            isGoodFace = true;
                            break;
                        }
                    }

                    if (isGoodFace)
                    {
                        for (int ii = arrayInt.getSize() - 1; ii >= 0; ii--)
                        {
                            index = arrayInt.getInteger(ii);
                            if (!forbiden.contains(index))
                            {
                                forbiden.add(index);
                                temp.add(index);
                            }
                        }
                    }
                }
            }
            // Next loop, treat new neighbors
            neighbors = temp;
            temp = null;
        }

        neighbors.destroy();
        neighbors = null;
    }

    /**
     * Force the mesh to be valid
     */
    private void makeMeshValid()
    {
        if (!this.mayBeUnvalid)
        {
            return;
        }
        Debug.verbose("revalidate");
        this.mayBeUnvalid = false;
        if (this.points.size() == 0)
        {
            this.points.add(new Point3D());
        }
        if (this.uv.size() == 0)
        {
            this.uv.add(new Point2D());
        }
        if (this.normals.size() == 0)
        {
            this.normals.add(new Point3D(0, 0, 1));
        }
        final int sizePoints  = this.facesPoints.size();
        final int sizeUV      = this.facesUV.size();
        final int sizeNormals = this.facesNormals.size();
        if (sizeUV < sizePoints)
        {
            for (int i = sizeUV; i < sizePoints; i++)
            {
                this.facesUV.add(new ArrayInt());
            }
        }
        if (sizeNormals < sizePoints)
        {
            for (int i = sizeNormals; i < sizePoints; i++)
            {
                this.facesNormals.add(new ArrayInt());
            }
        }
        for (int i = 0; i < sizePoints; i++)
        {
            final ArrayInt facePoint      = this.facesPoints.get(i);
            final ArrayInt faceUV         = this.facesUV.get(i);
            final ArrayInt faceNormal     = this.facesNormals.get(i);
            final int      sizeFacePoint  = facePoint.getSize();
            final int      sizeFaceUV     = faceUV.getSize();
            final int      sizeFaceNormal = faceNormal.getSize();
            if (sizeFaceUV < sizeFacePoint)
            {
                for (int j = sizeFaceUV; j < sizeFacePoint; j++)
                {
                    faceUV.add(0);
                }
            }
            if (sizeFaceNormal < sizeFacePoint)
            {
                for (int j = sizeFaceNormal; j < sizeFacePoint; j++)
                {
                    faceNormal.add(0);
                }
            }
        }
    }

    /**
     * Render a face of the mesh
     *
     * @param facePoints  Face points
     * @param faceUV      Face UV
     * @param faceNormals Face normals
     */
    @ThreadOpenGL
    private void render(
            final @NotNull ArrayInt facePoints, final @NotNull ArrayInt faceUV, final @NotNull ArrayInt faceNormals)
    {
        final int size = facePoints.getSize();

        if (size < 3)
        {
            return;
        }

        GL11.glBegin(GL11.GL_POLYGON);

        try
        {
            for (int i = 0; i < size; i++)
            {
                this.render(this.points.get(facePoints.getInteger(i)),
                            this.uv.get(faceUV.getInteger(i)),
                            this.normals.get(faceNormals.getInteger(i)));
            }
        }
        catch (final Exception exception)
        {
            if (Mesh.PRINT_OUPS)
            {
                Debug.exception(exception, "Oups in rendering");
            }
        }
        catch (final Error error)
        {
            if (Mesh.PRINT_OUPS)
            {
                Debug.error(error, "Oups in rendering");
            }
        }

        GL11.glEnd();
    }

    /**
     * Render a vertex
     *
     * @param point  Point
     * @param uv     UV
     * @param normal Normal
     */
    @ThreadOpenGL
    private void render(final @NotNull Point3D point, final @NotNull Point2D uv, final @NotNull Point3D normal)
    {
        normal.glNormal3f();
        uv.glTexCoord2f();
        point.glVertex3f();
    }

    /**
     * Obtain UV shapes
     *
     * @param width  With of desired bound
     * @param height Height of desired bound
     * @return UV shapes
     */
    synchronized @NotNull List<TextureGirdUV.Shape> obtainUVshapes(final int width, final int height)
    {
        final List<TextureGirdUV.Shape> shapeList = new ArrayList<>();

        int[]               x;
        int[]               y;
        int                 nb, i, index;
        Point2D             point2D;
        TextureGirdUV.Shape shape;

        for (final ArrayInt arrayInt : this.facesUV)
        {
            nb = arrayInt.getSize();
            x = new int[nb];
            y = new int[nb];

            for (i = 0; i < nb; i++)
            {
                index = arrayInt.getInteger(i);
                point2D = this.uv.get(index);

                x[i] = (int) (point2D.x() * width);
                y[i] = (int) (point2D.y() * height);
            }

            shape = new TextureGirdUV.Shape();
            shape.polygon = new Polygon(x, y, nb);

            shapeList.add(shape);
        }

        return shapeList;
    }

    /**
     * Render the mesh
     */
    @ThreadOpenGL
    synchronized void render()
    {
        this.makeMeshValid();
        final int size = this.facesPoints.size();
        for (int i = 0; i < size; i++)
        {
            this.render(this.facesPoints.get(i), this.facesUV.get(i), this.facesNormals.get(i));
        }
    }

    /**
     * Add a face of normals.<br>
     * beware on using this method directly.<br>
     * It may a strange result if you don't know what you doing
     *
     * @param faceNormals Face of normals to add
     */
    public synchronized void addFaceNormals(final @NotNull ArrayInt faceNormals)
    {
        Objects.requireNonNull(faceNormals, "faceNormals MUST NOT be null!");
        this.facesNormals.add(faceNormals);
        this.mayBeUnvalid = true;
    }

    /**
     * Add a face of points.<br>
     * beware on using this method directly.<br>
     * It may a strange result if you don't know what you doing
     *
     * @param facePoints Face point to add
     */
    public synchronized void addFacePoints(final @NotNull ArrayInt facePoints)
    {
        Objects.requireNonNull(facePoints, "facePoints MUST NOT be null!");
        this.facesPoints.add(facePoints);
        this.mayBeUnvalid = true;
    }

    /**
     * Add a face of UV.<br>
     * beware on using this method directly.<br>
     * It may a strange result if you don't know what you doing
     *
     * @param faceUV Face UV to add
     */
    public synchronized void addFaceUV(final @NotNull ArrayInt faceUV)
    {
        Objects.requireNonNull(faceUV, "faceUV MUST NOT be null!");
        this.facesUV.add(faceUV);
        this.mayBeUnvalid = true;
    }

    /**
     * Add a normal.<br>
     * beware on using this method directly.<br>
     * It may a strange result if you don't know what you doing
     *
     * @param normal Normal to add
     * @return Normal's index
     */
    public synchronized int addNormal(final @NotNull Point3D normal)
    {
        Objects.requireNonNull(normal, "normal MUST NOT be null!");
        this.normals.add(normal);
        this.mayBeUnvalid = true;
        return this.normals.size() - 1;
    }

    /**
     * Add a position.<br>
     * beware on using this method directly.<br>
     * It may a strange result if you don't know what you doing
     *
     * @param point Point to add
     * @return Point's index
     */
    public synchronized int addPosition(final @NotNull Point3D point)
    {
        Objects.requireNonNull(point, "point MUST NOT be null!");
        this.box.add(point);
        this.points.add(point);
        this.mayBeUnvalid = true;
        return this.points.size() - 1;
    }

    /**
     * Add a UV.<br>
     * beware on using this method directly.<br>
     * It may a strange result if you don't know what you doing
     *
     * @param uv UV to add
     * @return UV's index
     */
    public synchronized int addUV(final @NotNull Point2D uv)
    {
        Objects.requireNonNull(uv, "uv MUST NOT be null!");
        this.uv.add(uv);
        this.mayBeUnvalid = true;
        return this.uv.size() - 1;
    }

    /**
     * Add a vertex to the actual face
     *
     * @param vertex Vertex to add
     */
    public synchronized void addVertexToTheActualFace(final @NotNull Vertex vertex)
    {
        this.computeBox();

        final Point3D position = vertex.position();
        final Point2D uv       = vertex.uv();
        final Point3D normal   = vertex.normal();
        //
        this.box.add(position);
        //
        int index = this.points.indexOf(position);
        if (index < 0)
        {
            index = this.points.size();
            this.points.add(position);
        }
        this.actualFacePoints.add(index);
        //
        index = this.uv.indexOf(uv);
        if (index < 0)
        {
            index = this.uv.size();
            this.uv.add(uv);
        }
        this.actualFaceUV.add(index);
        //
        index = this.normals.indexOf(normal);
        if (index < 0)
        {
            index = this.normals.size();
            this.normals.add(normal);
        }
        this.actualFaceNormals.add(index);
    }

    /**
     * Translate mesh's vertex in order to their center become the center of the object
     */
    public void centerMesh()
    {
        final VirtualBox virtualBox = this.computeBox();
        final float      centerX    = (virtualBox.maxX() + virtualBox.minX()) / 2f;
        final float      centerY    = (virtualBox.maxY() + virtualBox.minY()) / 2f;
        final float      centerZ    = (virtualBox.maxZ() + virtualBox.minZ()) / 2f;

        for (final Point3D point3d : this.points)
        {
            point3d.translate(-centerX, -centerY, -centerZ);
        }

        this.recomputeTheBox();
    }

    /**
     * Compute bounding box
     *
     * @return Bounding box
     */
    public synchronized VirtualBox computeBox()
    {
        if (this.box != null)
        {
            return this.box;
        }
        this.box = new VirtualBox();
        for (final Point3D point : this.points)
        {
            this.box.add(point);
        }
        return this.box;
    }

    /**
     * Compute bounding sphere
     *
     * @return Bounding sphere
     */
    public synchronized VirtualSphere computeSphere()
    {
        final BarycenterPoint3D barycenterPoint3D = new BarycenterPoint3D();
        for (final Point3D point : this.points)
        {
            barycenterPoint3D.add(point);
        }
        final Point3D center = barycenterPoint3D.barycenter();
        if (center == null)
        {
            return new VirtualSphere(0, 0, 0, 0);
        }
        float       radius = 0;
        final float x      = center.x();
        final float y      = center.y();
        final float z      = center.z();
        for (final Point3D point : this.points)
        {
            radius = Math.max(radius, Point3D.distance(point, x, y, z));
        }
        return new VirtualSphere(x, y, z, radius);
    }

    /**
     * Automatic compute of UV.<br>
     * This compute use the XY, YZ or XZ plane for each face.<br>
     * The choose of the plane depends where the points are located
     *
     * @param multU Multiply U
     * @param multV Multiply V
     */
    public synchronized void computeUVfromMax(final float multU, final float multV)
    {
        this.makeMeshValid();
        final int size = this.facesPoints.size();
        this.uv.clear();
        for (int i = 0; i < size; i++)
        {
            this.computeUVfromMax(this.facesPoints.get(i), this.facesUV.get(i), multU, multV);
        }
    }

    /**
     * Automatic compute of UV.<br>
     * This compute is based on XY plane
     *
     * @param multU Multiply U
     * @param multV Multiply V
     */
    public synchronized void computeUVfromPlaneXY(final float multU, final float multV)
    {
        this.makeMeshValid();
        final int        size = this.facesPoints.size();
        final VirtualBox box  = this.computeBox();
        this.uv.clear();
        for (int i = 0; i < size; i++)
        {
            this.computeUVfromPlaneXY(box, this.facesPoints.get(i), this.facesUV.get(i), multU, multV);
        }
    }

    /**
     * Automatic compute of UV.<br>
     * This compute is based on XZ plane
     *
     * @param multU Multiply U
     * @param multV Multiply V
     */
    public synchronized void computeUVfromPlaneXZ(final float multU, final float multV)
    {
        this.makeMeshValid();
        final int        size = this.facesPoints.size();
        final VirtualBox box  = this.computeBox();
        this.uv.clear();
        for (int i = 0; i < size; i++)
        {
            this.computeUVfromPlaneXZ(box, this.facesPoints.get(i), this.facesUV.get(i), multU, multV);
        }
    }

    /**
     * Automatic compute of UV.<br>
     * This compute is based on YZ plane
     *
     * @param multU Multiply U
     * @param multV Multiply V
     */
    public synchronized void computeUVfromPlaneYZ(final float multU, final float multV)
    {
        this.makeMeshValid();
        final int        size = this.facesPoints.size();
        final VirtualBox box  = this.computeBox();
        this.uv.clear();
        for (int i = 0; i < size; i++)
        {
            this.computeUVfromPlaneYZ(box, this.facesPoints.get(i), this.facesUV.get(i), multU, multV);
        }
    }

    /**
     * Automatic compute of UV.<br>
     * This compute is spherical
     *
     * @param multU Multiply U
     * @param multV Multiply V
     */
    public void computeUVspherical(final float multU, final float multV)
    {
        this.makeMeshValid();
        final int           size   = this.facesPoints.size();
        final VirtualSphere sphere = this.computeSphere();
        this.uv.clear();
        for (int i = 0; i < size; i++)
        {
            this.computeUVspherical(sphere, this.facesPoints.get(i), this.facesUV.get(i), multU, multV);
        }
    }

    /**
     * End the actual face.<br>
     * Create a new face
     */
    public synchronized void endFace()
    {
        this.actualFacePoints = new ArrayInt();
        this.actualFaceUV = new ArrayInt();
        this.actualFaceNormals = new ArrayInt();
        //
        this.facesPoints.add(this.actualFacePoints);
        this.facesUV.add(this.actualFaceUV);
        this.facesNormals.add(this.actualFaceNormals);
    }

    /**
     * Normals index list for each face
     *
     * @return Normals index list for each face
     */
    public @NotNull List<ArrayInt> faceNormals()
    {
        return this.copy(this.facesNormals);
    }

    /**
     * Points index list for each face
     *
     * @return Points index list for each face
     */
    public @NotNull List<ArrayInt> facePoints()
    {
        return this.copy(this.facesPoints);
    }

    /**
     * UV index list for each face
     *
     * @return UV index list for each face
     */
    public @NotNull List<ArrayInt> faceUV()
    {
        return this.copy(this.facesUV);
    }

    /**
     * Actual last point index
     *
     * @return Actual last point index
     */
    public synchronized int lastIndexPoint()
    {
        return this.points.size() - 1;
    }

    /**
     * Load mesh from XML
     *
     * @param markupXML Markup to parse
     * @throws Exception On parsing problem
     */
    public synchronized void loadFromXML(final @NotNull MarkupXML markupXML) throws Exception
    {
        // Initialize
        this.box = null;
        this.mayBeUnvalid = false;

        this.facesPoints.clear();
        this.facesUV.clear();
        this.facesNormals.clear();

        this.points.clear();
        this.uv.clear();
        this.normals.clear();

        // Point list
        EnumerationIterator<MarkupXML> enumerationIterator = markupXML.obtainChildren(ConstantsXML.MARKUP_POINTS);
        if (enumerationIterator.hasMoreElements() == false)
        {
            throw new IllegalArgumentException(
                    UtilText.concatenate("Missing mendatory child ", ConstantsXML.MARKUP_POINTS, " in ",
                                         markupXML.getName()));
        }
        MarkupXML markup = enumerationIterator.nextElement();
        try
        {
            final StringTokenizer stringTokenizer = new StringTokenizer(markup.getText());
            Point3D               point;
            while (stringTokenizer.hasMoreElements() == true)
            {
                point = new Point3D(Float.parseFloat(stringTokenizer.nextToken()),
                                    Float.parseFloat(stringTokenizer.nextToken()),
                                    Float.parseFloat(stringTokenizer.nextToken()));
                this.points.add(point);
            }
        }
        catch (final Exception exception)
        {
            throw new Exception("Problem on parsing points list", exception);
        }

        // UV list
        enumerationIterator = markupXML.obtainChildren(ConstantsXML.MARKUP_UV);
        if (enumerationIterator.hasMoreElements() == false)
        {
            throw new IllegalArgumentException(
                    UtilText.concatenate("Missing mendatory child ", ConstantsXML.MARKUP_UV, " in ",
                                         markupXML.getName()));
        }

        markup = enumerationIterator.nextElement();
        try
        {
            final StringTokenizer stringTokenizer = new StringTokenizer(markup.getText());
            Point2D               point;
            while (stringTokenizer.hasMoreElements() == true)
            {
                point = new Point2D(Float.parseFloat(stringTokenizer.nextToken()),
                                    Float.parseFloat(stringTokenizer.nextToken()));
                this.uv.add(point);
            }
        }
        catch (final Exception exception)
        {
            throw new Exception("Problem on parsing UV list", exception);
        }

        // Normals list
        enumerationIterator = markupXML.obtainChildren(ConstantsXML.MARKUP_NORMALS);
        if (enumerationIterator.hasMoreElements() == false)
        {
            throw new IllegalArgumentException(
                    UtilText.concatenate("Missing mendatory child ", ConstantsXML.MARKUP_NORMALS, " in ",
                                         markupXML.getName()));
        }

        markup = enumerationIterator.nextElement();
        try
        {
            final StringTokenizer stringTokenizer = new StringTokenizer(markup.getText());
            Point3D               point;
            while (stringTokenizer.hasMoreElements())
            {
                point = new Point3D(Float.parseFloat(stringTokenizer.nextToken()),
                                    Float.parseFloat(stringTokenizer.nextToken()),
                                    Float.parseFloat(stringTokenizer.nextToken()));
                this.normals.add(point);
            }
        }
        catch (final Exception exception)
        {
            throw new Exception("Problem on parsing normals list", exception);
        }

        // Points face
        enumerationIterator = markupXML.obtainChildren(ConstantsXML.MARKUP_FACE_POINTS);
        if (enumerationIterator.hasMoreElements() == false)
        {
            throw new IllegalArgumentException(
                    UtilText.concatenate("Missing mendatory child ", ConstantsXML.MARKUP_FACE_POINTS, " in ",
                                         markupXML.getName()));
        }

        markup = enumerationIterator.nextElement();
        try
        {
            this.actualFacePoints = new ArrayInt();
            this.facesPoints.add(this.actualFacePoints);

            for (final MarkupXML face : markup.obtainChildren(ConstantsXML.MARKUP_FACE))
            {
                final StringTokenizer stringTokenizer = new StringTokenizer(face.getText());
                while (stringTokenizer.hasMoreTokens() == true)
                {
                    this.actualFacePoints.add(Integer.parseInt(stringTokenizer.nextToken()));
                }
                this.actualFacePoints = new ArrayInt();
                this.facesPoints.add(this.actualFacePoints);
            }
        }
        catch (final Exception exception)
        {
            throw new Exception("Problem on parsing points face", exception);
        }

        // UV face

        enumerationIterator = markupXML.obtainChildren(ConstantsXML.MARKUP_FACE_UV);
        if (enumerationIterator.hasMoreElements() == false)
        {
            throw new IllegalArgumentException(
                    UtilText.concatenate("Missing mendatory child ", ConstantsXML.MARKUP_FACE_UV, " in ",
                                         markupXML.getName()));
        }

        markup = enumerationIterator.nextElement();
        try
        {
            this.actualFaceUV = new ArrayInt();
            this.facesUV.add(this.actualFaceUV);

            for (final MarkupXML face : markup.obtainChildren(ConstantsXML.MARKUP_FACE))
            {
                final StringTokenizer stringTokenizer = new StringTokenizer(face.getText());
                while (stringTokenizer.hasMoreTokens() == true)
                {
                    this.actualFaceUV.add(Integer.parseInt(stringTokenizer.nextToken()));
                }
                this.actualFaceUV = new ArrayInt();
                this.facesUV.add(this.actualFaceUV);
            }
        }
        catch (final Exception exception)
        {
            throw new Exception("Problem on parsing UV face", exception);
        }

        // Normals face

        enumerationIterator = markupXML.obtainChildren(ConstantsXML.MARKUP_FACE_NORMALS);
        if (enumerationIterator.hasMoreElements() == false)
        {
            throw new IllegalArgumentException(
                    UtilText.concatenate("Missing mendatory child ", ConstantsXML.MARKUP_FACE_NORMALS, " in ",
                                         markupXML.getName()));
        }

        markup = enumerationIterator.nextElement();
        try
        {
            this.actualFaceNormals = new ArrayInt();
            this.facesNormals.add(this.actualFaceNormals);

            for (final MarkupXML face : markup.obtainChildren(ConstantsXML.MARKUP_FACE))
            {
                final StringTokenizer stringTokenizer = new StringTokenizer(face.getText());
                while (stringTokenizer.hasMoreTokens() == true)
                {
                    this.actualFaceNormals.add(Integer.parseInt(stringTokenizer.nextToken()));
                }
                this.actualFaceNormals = new ArrayInt();
                this.facesNormals.add(this.actualFaceNormals);
            }
        }
        catch (final Exception exception)
        {
            throw new Exception("Problem on parsing normals face", exception);
        }
    }

    /**
     * Translate a vertex in the mesh.<br>
     * This translation can translate neighbor vertex, the translation apply to them depends of the solidity.<br>
     * If you specify a 0 solidity, then neighbor don't move, 1, all vertex translate in the same translate, some where between,
     * the object morph
     *
     * @param indexPoint Vertex index to translate
     * @param vx         X
     * @param vy         Y
     * @param vz         Z
     * @param solidity   Solidity
     */
    public synchronized void movePoint(
            final int indexPoint, final float vx, final float vy, final float vz, final float solidity)
    {
        this.movePoint(indexPoint, vx, vy, vz, solidity, 0);
    }

    /**
     * Translate some vertex in the mesh.<br>
     * This translation can translate neighbor vertex, the translation apply to them depends of the solidity.<br>
     * If you specify a 0 solidity, then neighbor don't move, 1, all vertex translate in the same translate, some where between,
     * the object morph<br>
     * You specify a near deep to determine the level of points are translate the same way as the specified index
     *
     * @param indexPoint Vertex index to translate
     * @param vx         X
     * @param vy         Y
     * @param vz         Z
     * @param solidity   Solidity
     * @param near       Level of neighbor move with specified point. 0 the point, 1 : one level neighbor, ...
     */
    public synchronized void movePoint(
            final int indexPoint, final float vx, final float vy, final float vz, final float solidity, final int near)
    {
        if ((indexPoint < 0) || (indexPoint >= this.points.size()))
        {
            throw new IllegalArgumentException(
                    "The indexPoint " + indexPoint + " is not in [0, " + this.points.size() + "[");
        }
        // Initialize and launch the move
        ArrayInt forbiden = new ArrayInt();
        forbiden.add(indexPoint);
        this.internMovePoint(indexPoint, forbiden, vx, vy, vz, solidity, near);
        forbiden.destroy();
        forbiden = null;
    }

    /**
     * Multiply UV
     *
     * @param multU U multiplier
     * @param multV V multiplier
     */
    public synchronized void multUV(final float multU, final float multV)
    {
        for (final Point2D point2D : this.uv)
        {
            point2D.set(point2D.x() * multU, point2D.y() * multV);
        }
    }

    /**
     * Normals list
     *
     * @return Normals list
     */
    public @NotNull List<Point3D> normals()
    {
        return Collections.unmodifiableList(this.normals);
    }

    /**
     * Triangularize the mesh
     *
     * @return Triangle set
     */
    public @NotNull Triangles obtainTriangles()
    {
        final Triangles triangles = new Triangles();

        final int size = this.facesPoints.size();
        int       nb;
        ArrayInt  facePoints, faceUV, faceNormals;
        Vertex[]  vertexs;

        for (int i = 0; i < size; i++)
        {
            facePoints = this.facesPoints.get(i);
            faceUV = this.facesUV.get(i);
            faceNormals = this.facesNormals.get(i);

            nb = facePoints.getSize();
            vertexs = new Vertex[nb];

            for (int p = 0; p < nb; p++)
            {
                vertexs[p] = new Vertex(this.points.get(facePoints.getInteger(p)), this.uv.get(faceUV.getInteger(p)),
                                        this.normals.get(faceNormals.getInteger(p)));
            }

            triangles.convertInTriangles(vertexs);

            vertexs = null;
            facePoints = faceUV = faceNormals = null;
        }

        return triangles;
    }

    /**
     * Points list
     *
     * @return Points list
     */
    public @NotNull List<Point3D> points()
    {
        return Collections.unmodifiableList(this.points);
    }

    /**
     * Force to recompute the bounding box the next time we demand to compute it
     */
    public synchronized void recomputeTheBox()
    {
        this.box = null;
    }

    /**
     * Reset the mesh to empty
     */
    public synchronized void reset()
    {
        this.points = new ArrayList<>();
        this.uv = new ArrayList<>();
        this.normals = new ArrayList<>();
        //
        this.facesPoints = new ArrayList<>();
        this.facesUV = new ArrayList<>();
        this.facesNormals = new ArrayList<>();
        //
        this.actualFacePoints = new ArrayInt();
        this.actualFaceUV = new ArrayInt();
        this.actualFaceNormals = new ArrayInt();
        //
        this.facesPoints.add(this.actualFacePoints);
        this.facesUV.add(this.actualFaceUV);
        this.facesNormals.add(this.actualFaceNormals);
        //
        this.mayBeUnvalid = false;
        this.box = new VirtualBox();
    }

    /**
     * Save the mesh to XML
     *
     * @return Markup created
     */
    public synchronized @NotNull MarkupXML saveToXML()
    {
        final MarkupXML markupXMLMesh = new MarkupXML(ConstantsXML.MARKUP_MESH);

        // Save points
        MarkupXML    markupXML    = new MarkupXML(ConstantsXML.MARKUP_POINTS);
        StringBuffer stringBuffer = new StringBuffer();
        for (final Point3D point3D : this.points)
        {
            stringBuffer.append(point3D.x());
            stringBuffer.append(' ');
            stringBuffer.append(point3D.y());
            stringBuffer.append(' ');
            stringBuffer.append(point3D.z());
            stringBuffer.append(' ');
        }
        markupXML.setText(stringBuffer.toString());
        markupXMLMesh.addChild(markupXML);

        // Save UV
        markupXML = new MarkupXML(ConstantsXML.MARKUP_UV);
        stringBuffer = new StringBuffer();
        for (final Point2D point2D : this.uv)
        {
            stringBuffer.append(point2D.x());
            stringBuffer.append(' ');
            stringBuffer.append(point2D.y());
            stringBuffer.append(' ');
        }
        markupXML.setText(stringBuffer.toString());
        markupXMLMesh.addChild(markupXML);

        // Save normals
        markupXML = new MarkupXML(ConstantsXML.MARKUP_NORMALS);
        stringBuffer = new StringBuffer();
        for (final Point3D point3D : this.normals)
        {
            stringBuffer.append(point3D.x());
            stringBuffer.append(' ');
            stringBuffer.append(point3D.y());
            stringBuffer.append(' ');
            stringBuffer.append(point3D.z());
            stringBuffer.append(' ');
        }
        markupXML.setText(stringBuffer.toString());
        markupXMLMesh.addChild(markupXML);

        // Save face points
        markupXML = new MarkupXML(ConstantsXML.MARKUP_FACE_POINTS);
        for (final ArrayInt arrayInt : this.facesPoints)
        {
            markupXML.addChild(this.createFaceXML(arrayInt));
        }
        markupXMLMesh.addChild(markupXML);

        // Save face UV
        markupXML = new MarkupXML(ConstantsXML.MARKUP_FACE_UV);
        for (final ArrayInt arrayInt : this.facesUV)
        {
            markupXML.addChild(this.createFaceXML(arrayInt));
        }
        markupXMLMesh.addChild(markupXML);

        // Save face normals
        markupXML = new MarkupXML(ConstantsXML.MARKUP_FACE_NORMALS);
        for (final ArrayInt arrayInt : this.facesNormals)
        {
            markupXML.addChild(this.createFaceXML(arrayInt));
        }
        markupXMLMesh.addChild(markupXML);

        return markupXMLMesh;
    }

    /**
     * Triangularize the mesh
     */
    public void triangularize()
    {
        final Triangles triangles = this.obtainTriangles();
        this.reset();

        for (final Triangle triangle : triangles.obtainTriangleList())
        {
            this.addVertexToTheActualFace(triangle.first);
            this.addVertexToTheActualFace(triangle.second);
            this.addVertexToTheActualFace(triangle.third);
            this.endFace();
        }
    }

    /**
     * UV list
     *
     * @return UV list
     */
    public @NotNull List<Point2D> uv()
    {
        return Collections.unmodifiableList(this.uv);
    }
}
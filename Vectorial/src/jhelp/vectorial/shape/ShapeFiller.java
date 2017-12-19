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
package jhelp.vectorial.shape;

/**
 * Generic filler of shape.<br>
 * It is called inside {@link Shape#fillShape(ShapeFiller)} to know how fill the shape
 *
 * @author JHelp
 */
public interface ShapeFiller
{
    /**
     * Called when filling is finished
     *
     * @param shape
     *           Shape filled
     */
    void fillShapeEnd(Shape shape);

    /**
     * Called when a pixel have to be draw<br>
     * The given point is sure to be inside the shape and given area at {@link #obtainDrawingAreaBounds(Shape)}
     *
     * @param shape
     *           Filling shape
     * @param x
     *           Point X
     * @param y
     *           Point Y
     */
    void fillShapePixel(Shape shape, int x, int y);

    /**
     * Called when filling started
     *
     * @param shape
     *           Filling shape
     * @param filledArea
     *           The area intersection between shape and given area at {@link #obtainDrawingAreaBounds(Shape)}
     */
    void fillShapeStart(Shape shape, Rectangle filledArea);

    /**
     * Obtain the maximum area can be filled, if shape have part outside area, only the intersection will be filled
     *
     * @param shape
     *           Shape to fill
     * @return Maximum area can be filled
     */
    Rectangle obtainDrawingAreaBounds(Shape shape);
}
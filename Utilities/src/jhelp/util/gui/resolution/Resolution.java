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

package jhelp.util.gui.resolution;

import jhelp.util.math.Math2;

/**
 * Screen resolution
 *
 * @author JHelp
 */
public final class Resolution
{
    /**
     * Number of pixels per inch
     */
    private final int pixelPerInch;

    /**
     * Create a new instance of Resolution
     *
     * @param value          Screen resolution
     * @param resolutionUnit Resolution unit of given value
     */
    public Resolution(final int value, final ResolutionUnit resolutionUnit)
    {
        if (resolutionUnit == null)
        {
            throw new NullPointerException("resolutionUnit MUST NOT be null");
        }

        switch (resolutionUnit)
        {
            case PIXEL_PER_INCH:
                this.pixelPerInch = value;
                break;
            case PIXEL_PER_CENTIMETER:
                this.pixelPerInch = (int) Math2.centimeterToInch(value);
                break;
            default:
                throw new IllegalArgumentException("resolutionUnit not managed : " + resolutionUnit);
        }
    }

    /**
     * Obtain resolution in given resolution unit
     *
     * @param resolutionUnit Resolution unit
     * @return Resolution in given resolution unit
     */
    public int getResolution(final ResolutionUnit resolutionUnit)
    {
        if (resolutionUnit == null)
        {
            throw new NullPointerException("resolutionUnit MUST NOT be null");
        }

        switch (resolutionUnit)
        {
            case PIXEL_PER_INCH:
                return this.pixelPerInch;
            case PIXEL_PER_CENTIMETER:
                return (int) Math2.inchToCentimeter(this.pixelPerInch);
            default:
                throw new IllegalArgumentException("resolutionUnit not managed : " + resolutionUnit);
        }
    }

    /**
     * Number of pixels inside a distance
     *
     * @param value       Distance value
     * @param measureUnit Distance unit
     * @return Number of Pixels
     */
    public int numberOfPixels(double value, final MeasureUnit measureUnit)
    {
        if (measureUnit == null)
        {
            throw new NullPointerException("measureUnit MUST NOT be null");
        }

        switch (measureUnit)
        {
            case CENTIMETER:
                value = Math2.centimeterToInch(value);
                break;
            case INCH:
                // Nothing to change already in good measure
                break;
            case MILLIMETER:
                value = Math2.millimeterToInch(value);
                break;
            case PICA:
                value = Math2.picaToInch(value);
                break;
            case POINT:
                value = Math2.pointToInch(value);
                break;
            default:
                throw new IllegalArgumentException("measureUnit not managed : " + measureUnit);
        }

        return (int) (this.pixelPerInch * value);
    }

    /**
     * Convert a number of pixels to a measure unit
     *
     * @param pixels      Number of pixels
     * @param measureUnit Measure to convert
     * @return Converted value
     */
    public double pixelsToMeasure(final double pixels, final MeasureUnit measureUnit)
    {
        final double inch = pixels / this.pixelPerInch;

        switch (measureUnit)
        {
            case CENTIMETER:
                return Math2.inchToCentimeter(inch);
            case INCH:
                return inch;
            case MILLIMETER:
                return Math2.inchToMillimeter(inch);
            case PICA:
                return Math2.inchToPica(inch);
            case POINT:
                return Math2.inchToPoint(inch);
            default:
                throw new IllegalArgumentException("measureUnit not managed : " + measureUnit);
        }
    }
}
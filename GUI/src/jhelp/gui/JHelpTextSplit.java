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
package jhelp.gui;

import java.io.File;

import javax.swing.JLabel;

/**
 * Label with image, and texts above, left, right and or bellow the image
 *
 * @author JHelp
 */
public class JHelpTextSplit
        extends JLabel
{
    /** Image file */
    private File   image;
    /** Text bellow the image */
    private String textDown;
    /** Text left the image */
    private String textLeft;
    /** Text right the image */
    private String textRight;
    /** Text above the image */
    private String textUp;

    /**
     * Create a new instance of JHelpTextSplit
     *
     * @param textUp
     *           Text above ({@code null} for no text)
     * @param textLeft
     *           Text left ({@code null} for no text)
     * @param textRight
     *           Text right ({@code null} for no text)
     * @param textDown
     *           Text bellow ({@code null} for no text)
     * @param image
     *           Image to draw ({@code null} for no image)
     */
    public JHelpTextSplit(
            final String textUp, final String textLeft, final String textRight, final String textDown, final File image)
    {
        this.textUp = textUp;
        this.textLeft = textLeft;
        this.textRight = textRight;
        this.textDown = textDown;
        this.image = image;

        this.updateTextSplit();
    }

    /**
     * Update the label
     */
    private void updateTextSplit()
    {
        int nbCellVertical   = 0;
        int nbCellHorizontal = 0;

        if (this.textUp != null)
        {
            nbCellVertical++;
        }

        if ((this.textLeft != null) || (this.image != null) || (this.textRight != null))
        {
            nbCellVertical++;
        }

        if (this.textDown != null)
        {
            nbCellVertical++;
        }

        if (this.textLeft != null)
        {
            nbCellHorizontal++;
        }

        if ((this.image != null) && (this.image.exists()))
        {
            nbCellHorizontal++;
        }

        if (this.textRight != null)
        {
            nbCellHorizontal++;
        }

        if (((this.textUp != null) || (this.textDown != null) || ((this.image != null) && (this.image.exists()))) &&
            (nbCellHorizontal == 0))
        {
            nbCellHorizontal = 1;
        }

        String text = "";

        if ((nbCellHorizontal != 0) || (nbCellVertical != 0))
        {
            final StringBuilder stringBuilder = new StringBuilder("<html><table>");

            if (this.textUp != null)
            {
                stringBuilder.append("<tr><td align=\"CENTER\" valign=\"MIDDLE\" colspan=\"");
                stringBuilder.append(nbCellHorizontal);
                stringBuilder.append("\">");
                stringBuilder.append(this.textUp);
                stringBuilder.append("</td></tr>");
            }

            if ((this.textLeft != null) || (this.image != null) || (this.textRight != null))
            {
                stringBuilder.append("<tr>");

                if (this.textLeft != null)
                {
                    stringBuilder.append("<td align=\"CENTER\" valign=\"MIDDLE\" >");
                    stringBuilder.append(this.textLeft);
                    stringBuilder.append("</td>");
                }

                if ((this.image != null) && (this.image.exists()))
                {
                    stringBuilder.append("<td align=\"CENTER\" valign=\"MIDDLE\" ><img src=\"file://");
                    stringBuilder.append(this.image.getAbsolutePath());
                    stringBuilder.append("\" /></td>");
                }

                if (this.textRight != null)
                {
                    stringBuilder.append("<td align=\"CENTER\" valign=\"MIDDLE\" >");
                    stringBuilder.append(this.textRight);
                    stringBuilder.append("</td>");
                }

                stringBuilder.append("</tr>");
            }

            if (this.textDown != null)
            {
                stringBuilder.append("<tr><td  align=\"CENTER\" valign=\"MIDDLE\" colspan=\"");
                stringBuilder.append(nbCellHorizontal);
                stringBuilder.append("\">");
                stringBuilder.append(this.textDown);
                stringBuilder.append("</td></tr>");
            }

            stringBuilder.append("</table></html>");

            text = stringBuilder.toString();
        }

        this.setText(text);

        this.revalidate();
        this.doLayout();
        this.repaint();
    }

    /**
     * Image file
     *
     * @return Image file
     */
    public File getImage()
    {
        return this.image;
    }

    /**
     * Change image file
     *
     * @param image
     *           New image file
     */
    public void setImage(final File image)
    {
        this.image = image;
        this.updateTextSplit();
    }

    /**
     * Text bellow
     *
     * @return Text bellow
     */
    public String getTextDown()
    {
        return this.textDown;
    }

    /**
     * Change text bellow ({@code null} for no text)
     *
     * @param textDown
     *           New text bellow
     */
    public void setTextDown(final String textDown)
    {
        this.textDown = textDown;
        this.updateTextSplit();
    }

    /**
     * Text left
     *
     * @return Text left
     */
    public String getTextLeft()
    {
        return this.textLeft;
    }

    /**
     * Change text left ({@code null} for no text)
     *
     * @param textLeft
     *           New text left
     */
    public void setTextLeft(final String textLeft)
    {
        this.textLeft = textLeft;
        this.updateTextSplit();
    }

    /**
     * Text right
     *
     * @return Text right
     */
    public String getTextRight()
    {
        return this.textRight;
    }

    /**
     * Change text right ({@code null} for no text)
     *
     * @param textRight
     *           New text right
     */
    public void setTextRight(final String textRight)
    {
        this.textRight = textRight;
        this.updateTextSplit();
    }

    /**
     * Text above
     *
     * @return Text above
     */
    public String getTextUp()
    {
        return this.textUp;
    }

    /**
     * Change text above ({@code null} for no text)
     *
     * @param textUp
     *           New text above
     */
    public void setTextUp(final String textUp)
    {
        this.textUp = textUp;
        this.updateTextSplit();
    }
}
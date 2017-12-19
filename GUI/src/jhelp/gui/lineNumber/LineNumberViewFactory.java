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
package jhelp.gui.lineNumber;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

/**
 * Factory for show line number and additional text
 *
 * @author JHelp <br>
 */
public class LineNumberViewFactory
        implements ViewFactory
{
    /**
     * Create a new instance of LineNumberViewFactory
     */
    public LineNumberViewFactory()
    {
    }

    /**
     * Create view for element <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param element Element to have its view
     * @return Created view
     * @see ViewFactory#create(Element)
     */
    @Override
    public View create(final Element element)
    {
        // here we do all by default, except for paragraph for add line number and additional text
        final String name = element.getName();

        if (name != null)
        {
            if (name.equals(AbstractDocument.ContentElementName))
            {
                return new LabelView(element);
            }
            else if (name.equals(AbstractDocument.ParagraphElementName))
            {
                // Here we use our paragraph view
                return new LineNumberParagraphView(element);
            }
            else if (name.equals(AbstractDocument.SectionElementName))
            {
                return new BoxView(element, View.Y_AXIS);
            }
            else if (name.equals(StyleConstants.ComponentElementName))
            {
                return new ComponentView(element);
            }
            else if (name.equals(StyleConstants.IconElementName))
            {
                return new IconView(element);
            }
        }

        // default to text display
        return new LabelView(element);
    }
}
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

/*
 * License :
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may cause.
 * You can use, modify, the code as your need for any usage.
 * But you can't do any action that avoid me or other person use, modify this code.
 * The code is free for usage and modification, you can't change that fact.
 * JHelp
 */

/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any
 * damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.gui;

import com.sun.istack.internal.NotNull;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JEditorPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import jhelp.gui.code.Decoration;
import jhelp.gui.code.Decorator;
import jhelp.gui.code.DefaultDecorator;
import jhelp.gui.code.LanguageDescriptor;
import jhelp.gui.code.Rules;
import jhelp.gui.lineNumber.LineNumberEditorKit;
import jhelp.gui.lineNumber.LineNumberParagraphView;
import jhelp.util.gui.JHelpFont;
import jhelp.util.list.EnumerationIterator;
import jhelp.util.list.Pair;
import jhelp.util.list.SortedArray;
import jhelp.util.math.Math2;
import jhelp.util.text.UtilText;
import jhelp.util.thread.ConsumerTask;
import jhelp.util.thread.RunnableTask;
import jhelp.util.thread.ThreadManager;

/**
 * Text area with automatic styling while typing text.<br>
 * It detects each words. If a word have an associated style, we use the associated style else the default style.<br>
 * Symbols <code>[](){}&|\"'-@=/\*+.,?;:!<></code> can have also their own style<br>
 * You have first register a style with
 * {@link #createStyle(String, String, int, boolean, boolean, boolean, Color, Color)} then
 * associate words with {@link #associate(String, String...)}, {@link #symbolStyle(String)} (for symbol) or
 * {@link #defaultStyle(String)} (for change default style) <br>
 * The key short cut format is actually hard coded to Ctrl+Shift+F<br>
 * It have also number on lines, the capacity to change temporary line number background or add additional text at the
 * right
 * Last modification : 23 sept. 2010<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public class JHelpAutoStyledTextArea
        extends JEditorPane
{
    /**
     * Area in text where apply a style
     *
     * @author JHelp <br>
     */
    static class StyleArea
            implements Comparable<StyleArea>
    {
        /**
         * Area text end position
         */
        final int    end;
        /**
         * Area text start position
         */
        final int    start;
        /**
         * Style to apply to area
         */
        final String styleName;

        /**
         * Create a new instance of StyleArea
         *
         * @param start     Area text start position
         * @param end       Area text end position
         * @param styleName Style to apply to area
         */
        public StyleArea(final int start, final int end, final String styleName)
        {
            this.start = start;
            this.end = end;
            this.styleName = styleName;
        }

        /**
         * Compare with an other area <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param styleArea Area to compare with
         * @return Comparison result
         * @see Comparable#compareTo(Object)
         */
        @Override
        public int compareTo(final StyleArea styleArea)
        {
            return this.start - styleArea.start;
        }

        /**
         * String representation <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @return String representation
         * @see Object#toString()
         */
        @Override
        public String toString()
        {
            return UtilText.concatenate(this.styleName, ":[", this.start, ", ", this.end, ']');
        }
    }

    /**
     * List of area in text where apply a style
     *
     * @author JHelp <br>
     */
    static class StyleAreaList
            implements Iterable<StyleArea>
    {
        /**
         * List of area
         */
        private final SortedArray<StyleArea> list;

        /**
         * Create a new instance of StyleAreaList
         */
        StyleAreaList()
        {
            this.list = new SortedArray<StyleArea>(StyleArea.class);
        }

        /**
         * Add an area.<br>
         * If area inside an other present, it will not add.<br>
         * If area contains a present area, the present area will be remove
         *
         * @param start     Area text start position
         * @param end       Area text end position
         * @param styleName Style to apply to area
         */
        public void addArea(final int start, final int end, final String styleName)
        {
            for (final StyleArea styleArea : this.list)
            {
                if ((styleArea.start <= start) && (styleArea.end >= end) &&
                    ((styleArea.start != start) || (styleArea.end != end)))
                {
                    // Inside an area, so not add
                    return;
                }
            }

            StyleArea styleArea;

            for (int index = this.list.size() - 1; index >= 0; index--)
            {
                styleArea = this.list.get(index);

                if ((start <= styleArea.start) && (end >= styleArea.end))
                {
                    // Contains this area, so eat it
                    this.list.remove(index);
                }
            }

            this.list.add(new StyleArea(start, end, styleName));
        }

        /**
         * List iterator <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @return List iterator
         * @see Iterable#iterator()
         */
        @Override
        public Iterator<StyleArea> iterator()
        {
            return this.list.iterator();
        }
    }

    /**
     *
     */
    private static final long   serialVersionUID = -6141838702983157203L;
    /**
     * Symbols list
     */
    static final         char[] SYMBOLS          =
            {
                    '[', ']', '(', ')', '{', '}', '&', '|', '\\', '"', '\'', '-', '@', '=', '/', '*', '+', '.', ',',
                    '?', ';', ':', '!', '<', '>'
            };
    /**
     * Default name of default style
     */
    public static final  String DEFAULT_STYLE    = "jhelp.DEFAULT_STYLE";

    /**
     * Manage text area events <br>
     * <br>
     * Last modification : 15 janv. 2011<br>
     * Version 0.0.0<br>
     *
     * @author JHelp
     */
    class EventManager
            implements KeyListener, DocumentListener
    {
        /**
         * Called when text insert <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Event description
         * @see DocumentListener#insertUpdate(DocumentEvent)
         */
        @Override
        public void insertUpdate(final DocumentEvent e)
        {
            // Restore paragraphs attributes when insert something
            int       offset = e.getOffset();
            final int length = e.getLength();

            for (int i = 0; i <= length; i++, offset++)
            {
                JHelpAutoStyledTextArea.this.willRestoreParagraph(offset);
            }
        }

        /**
         * Called when something removed <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Event description
         * @see DocumentListener#removeUpdate(DocumentEvent)
         */
        @Override
        public void removeUpdate(final DocumentEvent e)
        {
            JHelpAutoStyledTextArea.this.willRestoreParagraph(e.getOffset());
        }

        /**
         * Called when document change <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param e Event description
         * @see DocumentListener#changedUpdate(DocumentEvent)
         */
        @Override
        public void changedUpdate(final DocumentEvent e)
        {
            // Nothing to do
        }

        /**
         * Call if key type
         *
         * @param keyEvent Event description
         * @see KeyListener#keyTyped(KeyEvent)
         */
        @Override
        public void keyTyped(final KeyEvent keyEvent)
        {
            // Nothing to do
        }

        /**
         * Call if key press
         *
         * @param keyEvent Event description
         * @see KeyListener#keyPressed(KeyEvent)
         */
        @Override
        public void keyPressed(final KeyEvent keyEvent)
        {
            if ((keyEvent.getKeyCode() == KeyEvent.VK_F) && (keyEvent.isControlDown()) && (keyEvent.isShiftDown()))
            {
                ThreadManager.parallel(JHelpAutoStyledTextArea.this.refreshFormat, 123L);
            }
        }

        /**
         * Call if key release
         *
         * @param keyEvent Event description
         * @see KeyListener#keyReleased(KeyEvent)
         */
        @Override
        public void keyReleased(final KeyEvent keyEvent)
        {
            // Nothing to do
        }
    }

    /**
     * Refresh format background thread <br>
     * <br>
     * Last modification : 15 janv. 2011<br>
     * Version 0.0.0<br>
     *
     * @author JHelp
     */
    class RefreshFormat implements RunnableTask
    {
        /**
         * Refresh the formating <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @see RunnableTask#run()
         */
        @Override
        public void run()
        {
            final String        text          = JHelpAutoStyledTextArea.this.getText();
            int                 length        = text.length();
            final StringBuilder stringBuilder = new StringBuilder(length + (length / 10) + 1);

            final StringTokenizer stringTokenizer = new StringTokenizer(text, "\n", true);
            String                line;
            int                   start;
            int                   tabCount        = 0;
            int                   index1, index2, index;
            char                  character;
            int                   read            = 0;
            int                   end             = 0;

            while (stringTokenizer.hasMoreTokens())
            {
                read = end;
                line = stringTokenizer.nextToken();
                end += line.length();

                line = line.trim();

                start = 0;
                length = line.length();

                if (length > 0)
                {
                    index1 = line.indexOf('{');
                    index2 = line.indexOf('}');
                    character = ' ';

                    while ((index1 >= 0) || (index2 >= 0))
                    {
                        index = -1;
                        if ((index1 >= 0) && (index2 >= 0))
                        {
                            index = Math.min(index1, index2);
                        }
                        else if (index1 >= 0)
                        {
                            index = index1;
                        }
                        else
                        {
                            index = index2;
                        }

                        character = line.charAt(index);

                        if (start < index)
                        {
                            for (int i = 0; i < tabCount; i++)
                            {
                                stringBuilder.append("   ");
                            }

                            stringBuilder.append(line.substring(start, index));
                            stringBuilder.append('\n');
                        }

                        if (character == '}')
                        {
                            tabCount--;
                        }

                        for (int i = 0; i < tabCount; i++)
                        {
                            stringBuilder.append("   ");
                        }
                        stringBuilder.append(character);

                        if ((index + 1) < length)
                        {
                            stringBuilder.append('\n');
                        }

                        if (character == '{')
                        {
                            tabCount++;
                        }

                        start = index + 1;

                        index1 = line.indexOf('{', start);
                        index2 = line.indexOf('}', start);
                    }

                    for (int i = 0; i < tabCount; i++)
                    {
                        stringBuilder.append("   ");
                    }

                    stringBuilder.append(line.substring(start));

                    if (stringTokenizer.hasMoreElements())
                    {
                        stringBuilder.append('\n');
                    }
                }
                else if ((read > 0) && (text.charAt(read - 1) == '\n'))
                {
                    stringBuilder.append('\n');
                }
            }

            final int carret = JHelpAutoStyledTextArea.this.getCaretPosition();

            JHelpAutoStyledTextArea.this.setText(stringBuilder.toString());

            JHelpAutoStyledTextArea.this.setCaretPosition((carret * stringBuilder.length()) / text.length());
        }
    }

    /**
     * Refresh style background thread <br>
     * <br>
     * Last modification : 15 janv. 2011<br>
     * Version 0.0.0<br>
     *
     * @author JHelp
     */
    class RefreshStyle implements RunnableTask
    {
        /**
         * Refresh the style <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @see RunnableTask#run()
         */
        @Override
        public void run()
        {
            final String        text          = JHelpAutoStyledTextArea.this.getText();
            final StyleAreaList styleAreaList = new StyleAreaList();
            String              styleName;
            Matcher             matcher;
            Style               style;

            // Get default style
            styleName = JHelpAutoStyledTextArea.this.defaultStyle;

            if (styleName == null)
            {
                styleName = JHelpAutoStyledTextArea.DEFAULT_STYLE;
            }

            final Style defaultStyle = JHelpAutoStyledTextArea.this.autoStyledDocument.getStyle(styleName);

            // Compute symbols areas, if the symbol style is not the default one
            if ((JHelpAutoStyledTextArea.this.symbolStyle != null) && (!styleName.equals(
                    JHelpAutoStyledTextArea.this.symbolStyle)))
            {
                int index = UtilText.indexOf(text, JHelpAutoStyledTextArea.SYMBOLS);

                while (index >= 0)
                {
                    styleAreaList.addArea(index, index + 1, JHelpAutoStyledTextArea.this.symbolStyle);
                    index = UtilText.indexOf(text, index + 1, JHelpAutoStyledTextArea.SYMBOLS);
                }
            }

            // Compute regular expressions areas
            for (final HashMap.Entry<String, List<Pair<Pattern, Integer>>> entry : JHelpAutoStyledTextArea.this
                    .associatedStyle.entrySet())
            {
                styleName = entry.getKey();

                for (final Pair<Pattern, Integer> pattern : entry.getValue())
                {
                    matcher = pattern.first.matcher(text);

                    while (matcher.find())
                    {
                        styleAreaList.addArea(matcher.start(pattern.second), matcher.end(pattern.second), styleName);
                    }
                }
            }

            // Aplly style on area
            int start = 0;

            for (final StyleArea styleArea : styleAreaList)
            {
                // If there a space without style, thats means it is the default style to use on this space
                if (start < styleArea.start)
                {
                    JHelpAutoStyledTextArea.this.autoStyledDocument.setCharacterAttributes(start,
                                                                                           styleArea.start - start,
                                                                                           defaultStyle, true);
                }

                style = JHelpAutoStyledTextArea.this.autoStyledDocument.getStyle(styleArea.styleName);

                if (style == null)
                {
                    style = defaultStyle;
                }

                // Apply style to area
                JHelpAutoStyledTextArea.this.autoStyledDocument.setCharacterAttributes(styleArea.start,
                                                                                       styleArea.end - styleArea.start,
                                                                                       style,
                                                                                       true);

                start = styleArea.end;
            }

            // If left characters at end without style, they have to use default style
            if (start < text.length())
            {
                JHelpAutoStyledTextArea.this.autoStyledDocument.setCharacterAttributes(start, text.length() - start,
                                                                                       defaultStyle, true);
            }

            JHelpAutoStyledTextArea.this.invalidate();
            JHelpAutoStyledTextArea.this.repaint();
        }
    }

    /**
     * Restore a paragraph task
     *
     * @author JHelp <br>
     */
    class TaskRestoreParagraph implements ConsumerTask<Integer>
    {
        /**
         * Create a new instance of TaskRestoreParagraph
         */
        TaskRestoreParagraph()
        {
        }

        /**
         * Called when its task turn <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param parameter Paragraph to restore
         * @see jhelp.util.thread.ConsumerTask#consume(Object)
         */
        @Override
        public void consume(final Integer parameter)
        {
            JHelpAutoStyledTextArea.this.restoreParagraph(parameter);
        }
    }

    /**
     * Editor kit linked
     */
    private final StyledEditorKit                               autoStyledEditorKit;
    /**
     * Event manager
     */
    private final EventManager                                  eventManager;
    /**
     * Task for restore a paragraph
     */
    private final TaskRestoreParagraph                          taskRestoreParagraph;
    /**
     * Style association map
     */
    // HashMap<String, String> associatedStyle;
    final         HashMap<String, List<Pair<Pattern, Integer>>> associatedStyle;
    /**
     * Document to add style
     */
    final         DefaultStyledDocument                         autoStyledDocument;
    /**
     * Default style name
     */
    String defaultStyle;
    /**
     * Automatic refresh
     */
    final RefreshFormat refreshFormat;
    /**
     * Symbol style name
     */
    String symbolStyle;

    /**
     * Constructs JHelpAutoStyledTextArea
     */
    public JHelpAutoStyledTextArea()
    {
        this.autoStyledEditorKit = new LineNumberEditorKit();
        this.setEditorKit(this.autoStyledEditorKit);
        this.autoStyledDocument = (DefaultStyledDocument) this.getDocument();
        this.createStyle(JHelpAutoStyledTextArea.DEFAULT_STYLE, "Arial", 12, false, false, false, Color.BLACK,
                         Color.WHITE);

        this.associatedStyle = new HashMap<String, List<Pair<Pattern, Integer>>>();
        this.defaultStyle = JHelpAutoStyledTextArea.DEFAULT_STYLE;
        this.symbolStyle = JHelpAutoStyledTextArea.DEFAULT_STYLE;
        this.taskRestoreParagraph = new TaskRestoreParagraph();

        this.updateDefaultStyle();

        ThreadManager.repeatRun(new RefreshStyle(), 1234L, 1234L);

        this.refreshFormat = new RefreshFormat();

        this.eventManager = new EventManager();
        this.addKeyListener(this.eventManager);
        this.autoStyledDocument.addDocumentListener(this.eventManager);
    }

    /**
     * Restore a paragraph
     *
     * @param element Paragraph element to restore
     */
    private void restoreParagraph(final Element element)
    {
        // Check if paragraph contains temporary information
        // If some, remove them
        final AttributeSet attributeSet = element.getAttributes();
        final Object additionalText = attributeSet.getAttribute(
                LineNumberParagraphView.ATTRIBUTE_ADDITIONAL_TEXT);
        final Object background = attributeSet.getAttribute(
                LineNumberParagraphView.ATTRIBUTE_NUMBER_BACKGROUND);

        if ((additionalText != null) || //
            ((background != null) && //
             ((!(background instanceof Integer)) || (((Integer) background) != 0))))
        {
            final SimpleAttributeSet mutableAttributeSet = new SimpleAttributeSet(attributeSet);
            mutableAttributeSet.addAttribute(LineNumberParagraphView.ATTRIBUTE_NUMBER_BACKGROUND, 0);
            mutableAttributeSet.removeAttribute(LineNumberParagraphView.ATTRIBUTE_ADDITIONAL_TEXT);
            JHelpAutoStyledTextArea.this.autoStyledDocument.setParagraphAttributes(element.getStartOffset(),
                                                                                   element.getEndOffset() - element
                                                                                           .getStartOffset(),
                                                                                   mutableAttributeSet, true);
        }
    }

    /**
     * Update the default style to make properties coherent
     */
    private void updateDefaultStyle()
    {
        String defaultStyle = this.defaultStyle;

        if (defaultStyle == null)
        {
            defaultStyle = JHelpAutoStyledTextArea.DEFAULT_STYLE;
        }

        final Style style = this.autoStyledDocument.getStyle(defaultStyle);
        this.setBackground(StyleConstants.getBackground(style));
        this.setForeground(StyleConstants.getForeground(style));
        int flag = 0;

        if (StyleConstants.isBold(style))
        {
            flag |= Font.BOLD;
        }

        if (StyleConstants.isItalic(style))
        {
            flag |= Font.ITALIC;
        }

        final Font font = new Font(StyleConstants.getFontFamily(style), flag, StyleConstants.getFontSize(style));
        this.setFont(font);

        final Style styleDefault = this.autoStyledDocument.getStyle("default");

        if (styleDefault != null)
        {
            StyleConstants.setBackground(styleDefault, StyleConstants.getBackground(style));
            StyleConstants.setForeground(styleDefault, StyleConstants.getForeground(style));
            StyleConstants.setFontSize(styleDefault, StyleConstants.getFontSize(style));
            StyleConstants.setFontFamily(styleDefault, StyleConstants.getFontFamily(style));
            StyleConstants.setBold(styleDefault, StyleConstants.isBold(style));
            StyleConstants.setItalic(styleDefault, StyleConstants.isItalic(style));
            StyleConstants.setUnderline(styleDefault, StyleConstants.isUnderline(style));
        }
    }

    /**
     * Restore the paragraph under a position
     *
     * @param position Position in text
     */
    void restoreParagraph(final int position)
    {
        final Element element = this.autoStyledDocument.getParagraphElement(position);

        if (element == null)
        {
            return;
        }

        this.restoreParagraph(element);
    }

    /**
     * Delay action to restore a paragraph to not restore it while the notification
     *
     * @param position Position in text
     */
    void willRestoreParagraph(final int position)
    {
        ThreadManager.parallel(this.taskRestoreParagraph, position, 1024);
    }

    /**
     * Add additional text information at the right.<br>
     * These information is temporary in the way if user delete the line or press enter in the line the information will
     * disappear.<br>
     * Add temporary things (this and {@link #changeTemporaryLineNumberBackground(int, Color)} can be clean in one time
     * with
     * {@link #removeAllTemporaryModification()}
     *
     * @param lineNumber Line number where show additional information
     * @param text       Text to show at right of the line
     */
    public void addTemporaryTextInformation(final int lineNumber, final String text)
    {
        final Element element = this.autoStyledDocument.getParagraphElement(this.lineNumberToPosition(lineNumber));

        if (element == null)
        {
            return;
        }

        final SimpleAttributeSet mutableAttributeSet = new SimpleAttributeSet(element.getAttributes());
        mutableAttributeSet.addAttribute(LineNumberParagraphView.ATTRIBUTE_ADDITIONAL_TEXT, text);
        JHelpAutoStyledTextArea.this.autoStyledDocument.setParagraphAttributes(element.getStartOffset(),
                                                                               element.getEndOffset() - element
                                                                                       .getStartOffset(),
                                                                               mutableAttributeSet, true);
    }

    /**
     * Associate a pattern to a style
     *
     * @param styleName Style name
     * @param pattern   Pattern to associate
     * @param group     Group number to apply style in given pattern (For all String use 0), see {@link Pattern}
     *                  documentation about
     *                  capturing group. You can see also the code of {@link #associate(String, String...)}
     */
    public void associate(final String styleName, final Pattern pattern, final int group)
    {
        if (pattern == null)
        {
            throw new NullPointerException("pattern mustn't be null");
        }

        final Style style = this.autoStyledDocument.getStyle(styleName);
        if (style == null)
        {
            throw new IllegalArgumentException("The style " + styleName + " doesn't exists !");
        }

        List<Pair<Pattern, Integer>> patterns = this.associatedStyle.get(styleName);

        if (patterns == null)
        {
            patterns = new ArrayList<Pair<Pattern, Integer>>();
            this.associatedStyle.put(styleName, patterns);
        }

        patterns.add(new Pair<Pattern, Integer>(pattern, group));
    }

    /**
     * Associated a style to words
     *
     * @param styleName Style name
     * @param keyWords  Words list
     */
    public void associate(final String styleName, final String... keyWords)
    {
        final Style style = this.autoStyledDocument.getStyle(styleName);
        if (style == null)
        {
            throw new IllegalArgumentException("The style " + styleName + " doesn't exists !");
        }

        List<Pair<Pattern, Integer>> patterns = this.associatedStyle.get(styleName);

        if (patterns == null)
        {
            patterns = new ArrayList<Pair<Pattern, Integer>>();
            this.associatedStyle.put(styleName, patterns);
        }

        for (final String keyWord : keyWords)
        {
            // We want get the word if it is alone, so we have to check if it in middle a word or not.
            // Thats why we add something before and something after.
            // But we want apply style on only the second thing between parenthesis : the second group (Remember put
            // something between parenthesis in regular expression make a capturing group)
            // So we precise to take care the second group for style
            patterns.add(new Pair<Pattern, Integer>(
                    Pattern.compile(UtilText.concatenate("([^a-zA-Z0-9_]|^)(", //
                                                         Pattern.quote(keyWord), //
                                                         ")([^a-zA-Z0-9_]|$)")), //
                    2));
        }
    }

    /**
     * Change an existing style
     *
     * @param name       Style name
     * @param fontFamily New font family (Can use <code>null</code> for no change)
     * @param fontSize   New font size (Can use a number <=0 for no change)
     * @param bold       New bold state
     * @param italic     New italic state
     * @param underline  New underline state
     * @param foreground New foreground color (Can use <code>null</code> for no change)
     * @param background New background color (Can use <code>null</code> for no change)
     */
    public void changeStyle(
            final String name, final String fontFamily, final int fontSize, final boolean bold, final
    boolean italic, final boolean underline,
            final Color foreground, final Color background)
    {
        if (name == null)
        {
            throw new NullPointerException("name mustn't be null");
        }

        final Style style = this.autoStyledDocument.getStyle(name);

        if (style == null)
        {
            throw new IllegalArgumentException(name + " style not defined !");
        }

        if (fontFamily != null)
        {
            StyleConstants.setFontFamily(style, fontFamily);
        }

        if (fontSize > 0)
        {
            StyleConstants.setFontSize(style, fontSize);
        }

        StyleConstants.setBold(style, bold);
        StyleConstants.setItalic(style, italic);
        StyleConstants.setUnderline(style, underline);

        if (foreground != null)
        {
            StyleConstants.setForeground(style, foreground);
        }

        if (background != null)
        {
            StyleConstants.setBackground(style, background);
        }

        if ((name.equals(this.defaultStyle)) || (name.equals(JHelpAutoStyledTextArea.DEFAULT_STYLE)))
        {
            this.updateDefaultStyle();
        }
    }

    /**
     * Change temporary in line number background color.<br>
     * These information is temporary in the way if user delete the line or press enter in the line the information will
     * disappear.<br>
     * Add temporary things (this and {@link #addTemporaryTextInformation(int, String)} can be clean in one time with
     * {@link #removeAllTemporaryModification()}
     *
     * @param lineNumber Line number
     * @param color      New background color
     */
    public void changeTemporaryLineNumberBackground(final int lineNumber, final Color color)
    {
        if (color == null)
        {
            throw new NullPointerException("color mustn't be null");
        }

        final Element element = this.autoStyledDocument.getParagraphElement(this.lineNumberToPosition(lineNumber));

        if (element == null)
        {
            return;
        }

        final SimpleAttributeSet mutableAttributeSet = new SimpleAttributeSet(element.getAttributes());
        mutableAttributeSet.addAttribute(LineNumberParagraphView.ATTRIBUTE_NUMBER_BACKGROUND, color);
        JHelpAutoStyledTextArea.this.autoStyledDocument.setParagraphAttributes(element.getStartOffset(),
                                                                               element.getEndOffset() - element
                                                                                       .getStartOffset(),
                                                                               mutableAttributeSet, true);
    }

    /**
     * Remove all associations
     */
    public void clearAssociations()
    {
        this.associatedStyle.clear();
    }

    /**
     * Remove all defined styles, execpt the default one
     */
    public void clearStyles()
    {
        for (Object name : new EnumerationIterator<>(this.autoStyledDocument.getStyleNames()))
        {
            if (name != null)
            {
                this.removeStyle(name.toString());
            }
        }

        this.updateDefaultStyle();
    }

    /**
     * Create a style
     *
     * @param name       Style name
     * @param fontFamily Font family
     * @param fontSize   Font size
     * @param bold       Indicates if bold
     * @param italic     Indicates if italic
     * @param underline  Indicates if underline
     * @param foreground Foreground color
     * @param background Background color
     */
    public void createStyle(
            final String name, final String fontFamily, final int fontSize, final boolean bold, final
    boolean italic, final boolean underline,
            final Color foreground, final Color background)
    {
        if (name == null)
        {
            throw new NullPointerException("name mustn't be null");
        }

        if (fontFamily == null)
        {
            throw new NullPointerException("fontFamily mustn't be null");
        }

        if (foreground == null)
        {
            throw new NullPointerException("foreground mustn't be null");
        }

        if (background == null)
        {
            throw new NullPointerException("background mustn't be null");
        }

        final Style style = this.autoStyledDocument.addStyle(name, null);

        StyleConstants.setFontFamily(style, fontFamily);
        StyleConstants.setFontSize(style, fontSize);
        StyleConstants.setBold(style, bold);
        StyleConstants.setItalic(style, italic);
        StyleConstants.setUnderline(style, underline);
        StyleConstants.setForeground(style, foreground);
        StyleConstants.setBackground(style, background);
    }

    /**
     * Apply the decorator
     *
     * @param decorator Decorator to use
     */
    public void decorate(
            @NotNull
                    Decorator decorator)
    {
        this.defaultStyle(JHelpAutoStyledTextArea.DEFAULT_STYLE);
        this.clearStyles();
        final String familly  = decorator.fontFamily();
        final int    fontSzie = decorator.normalTextSize();
        Decoration   decoration;

        for (Rules rules : Rules.values())
        {
            decoration = decorator.obtainDecoration(rules);
            this.createStyle(rules.name(), familly, decoration.textSize()
                                                              .computeSize(fontSzie),
                             decoration.bold(), decoration.italic(), decoration.underline(),
                             decoration.foreground(), decoration.background());
        }

        this.symbolStyle(Rules.SYMBOL.name());
        this.defaultStyle(Rules.DEFAULT.name());
    }

    /**
     * Return defaultStyle
     *
     * @return defaultStyle
     */
    public String defaultStyle()
    {
        return this.defaultStyle;
    }

    /**
     * Modify defaultStyle
     *
     * @param defaultStyle New defaultStyle value
     */
    public void defaultStyle(final String defaultStyle)
    {
        this.defaultStyle = defaultStyle;
        this.updateDefaultStyle();
    }

    /**
     * Change the langauge description (Previous associations are removed)
     *
     * @param languageDescriptor Language descriptor to use
     */
    public void describeLanguage(LanguageDescriptor languageDescriptor)
    {
        this.clearAssociations();

        for (Rules rules : Rules.values())
        {
            if (!this.styleDefined(rules.name()))
            {
                this.decorate(new DefaultDecorator());
                break;
            }
        }

        languageDescriptor.addToAutoStyledText(this);
    }

    /**
     * Convert a line number to a text position
     *
     * @param lineNumber Line number (Line numbers start with 1, not 0)
     * @return Position where line start OR 0 if line number < 1 OR text length if given line number not exists (too big)
     */
    public int lineNumberToPosition(int lineNumber)
    {
        int          position = 0;
        final String text     = this.getText();
        final int    length   = text.length();
        int          index    = text.indexOf('\n', position);

        while ((lineNumber > 1) && (index >= 0) && (position < length))
        {
            lineNumber--;
            position = index + 1;
            index = text.indexOf('\n', position);
        }

        return position;
    }

    /**
     * Remove all temporary modification add with {@link #addTemporaryTextInformation(int, String)} and
     * {@link #changeTemporaryLineNumberBackground(int, Color)}
     */
    public void removeAllTemporaryModification()
    {
        int start = 0;
        final int length = this.getText()
                               .length();
        Element element;

        while (start < length)
        {
            element = this.autoStyledDocument.getParagraphElement(start);

            if (element == null)
            {
                return;
            }

            this.restoreParagraph(element);
            start = element.getEndOffset() + 1;
        }
    }

    /**
     * Remove one style.<br>
     * The default style will no be removed
     *
     * @param name Style name to remove
     */
    public void removeStyle(String name)
    {
        if (name.equals("default") || name.equals(this.defaultStyle) ||
            name.equals(JHelpAutoStyledTextArea.DEFAULT_STYLE))
        {
            //Can't remove default style
            return;
        }

        this.autoStyledDocument.removeStyle(name);
    }

    /**
     * Scroll the text to line number
     *
     * @param lineNumber Line number to go (Line numbers start with 1)
     */
    public void scrollToLine(final int lineNumber)
    {
        this.scrollToPosition(this.lineNumberToPosition(lineNumber));
    }

    /**
     * Scroll to text position
     *
     * @param position Text position
     */
    public void scrollToPosition(int position)
    {
        // Be sure position accurate, and place the caret
        position = Math2.limit(position, 0, this.getText()
                                                .length());
        this.setCaretPosition(position);
        // The caret is on good place, but may not visible, so have to scroll to make it visible

        Point location = this.getCaret()
                             .getMagicCaretPosition();

        if (location == null)
        {
            // Sometimes caret answer null for its position, here we try an other way to get it
            try
            {
                final Rectangle box = this.modelToView(position);
                location = new Point(box.x, box.y);
            }
            catch (final Exception exception)
            {
                // Note : Since position is sure to be correct, it normally never have exception
                // But in case of thread concurrency (Text become smaller than 'position' between the 'Math2.limit'
                // AND the 'modelToView') we just do nothing
                return;
            }
        }

        // Compute a rectangle around the cursor and make it visible
        final AttributeSet attributeSet = this.autoStyledDocument.getCharacterElement(position)
                                                                 .getAttributes();
        final JHelpFont font = new JHelpFont(StyleConstants.getFontFamily(attributeSet),
                                             StyleConstants.getFontSize(attributeSet),
                                             StyleConstants.isBold(attributeSet), StyleConstants.isItalic(attributeSet),
                                             StyleConstants.isUnderline(attributeSet));
        this.scrollRectToVisible(new Rectangle(location.x - 64, location.y - 32, 128, font.getHeight() + 64));
    }

    /**
     * Indicates if given style is defined
     *
     * @param name Style name
     * @return <code>true</code> if given style is defined
     */
    public boolean styleDefined(final String name)
    {
        if (name == null)
        {
            throw new NullPointerException("name mustn't be null");
        }

        return this.autoStyledDocument.getStyle(name) != null;
    }

    /**
     * Return symbolStyle
     *
     * @return symbolStyle
     */
    public String symbolStyle()
    {
        return this.symbolStyle;
    }

    /**
     * Modify symbolStyle
     *
     * @param symbolStyle New symbolStyle value
     */
    public void symbolStyle(final String symbolStyle)
    {
        if (symbolStyle == null)
        {
            this.symbolStyle = null;

            return;
        }

        final Style style = this.autoStyledDocument.getStyle(symbolStyle);
        if (style == null)
        {
            throw new IllegalArgumentException("The style " + symbolStyle + " doesn't exists !");
        }

        this.symbolStyle = symbolStyle;
    }
}
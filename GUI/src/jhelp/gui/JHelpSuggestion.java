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

import com.sun.glass.events.KeyEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import jhelp.gui.model.FilterSuggestionElement;
import jhelp.gui.model.ListFilterableModel;
import jhelp.gui.model.SuggestionElement;
import jhelp.gui.smooth.JHelpConstantsSmooth;
import jhelp.util.debug.Debug;
import jhelp.util.gui.JHelpFont;
import jhelp.util.text.UtilText;

/**
 * Represents a list of suggestions.<br>
 * Suggestions are linked to a {@link JTextComponent}, they are pop up while user type text to give them suggestion of what to
 * type.<br>
 * User can ignore them on continue typing, select the suggestion with mouse or keyboard.<br>
 * A suggestion have three part :
 * <ol>
 * <li>The key word (Mandatory): It is the word to insert if user choose the suggestion. The list of suggestion is build with
 * all key words that start with what user just start to type</li>
 * <li>The information (Optional) : It is a object that developer define for the suggestion.</li>
 * <li>The details (Optional) : Its some additional information for user. If suggestion have details, the show in additional
 * "window". This details can be formated in HTML, images need the absolute path, this issue can be solve with the additional
 * protocols "class:" and "external:" described in {@link UtilText#resolveImagesLinkInHTML(String)}. The aim of this is to give
 * the opportunity to give more information about the suggestion (By example, in a code editor, what do the instruction, its
 * usage condition, ...)</li>
 * </ol>
 *
 * @param <INFORMATION> Information type carry by suggestions
 * @author JHelp <br>
 */
public class JHelpSuggestion<INFORMATION>
{
    /**
     * Manage several events :
     * <ul>
     * <li>Caret position change in linked {@link JTextComponent}</li>
     * <li>Keyboard event in suggestion list</li>
     * <li>Mouse events in suggestion list</li>
     * <li>Selection in suggestion list</li>
     * </ul>
     *
     * @author JHelp <br>
     */
    class EventManager
            implements CaretListener, MenuKeyListener, MouseListener, ListSelectionListener
    {
        /**
         * Create a new instance of EventManager
         */
        EventManager()
        {
        }

        /**
         * Called each time the caret change position in linked {@link JTextComponent} <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param caretEvent Event description
         * @see CaretListener#caretUpdate(CaretEvent)
         */
        @Override
        public void caretUpdate(final CaretEvent caretEvent)
        {
            JHelpSuggestion.this.positionCaretChange();
        }

        /**
         * Called when a key typed in suggestion list <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param menuKeyEvent Event description
         * @see MenuKeyListener#menuKeyTyped(MenuKeyEvent)
         */
        @Override
        public void menuKeyTyped(final MenuKeyEvent menuKeyEvent)
        {
            JHelpSuggestion.this.keyTyped(menuKeyEvent.getKeyCode(), menuKeyEvent.getKeyChar());
        }

        /**
         * Called when key pressed in suggestion list <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param menuKeyEvent Event description
         * @see MenuKeyListener#menuKeyPressed(MenuKeyEvent)
         */
        @Override
        public void menuKeyPressed(final MenuKeyEvent menuKeyEvent)
        {
            JHelpSuggestion.this.keyCommand(menuKeyEvent.getKeyCode(), menuKeyEvent.getModifiers());
        }

        /**
         * Called when key released in suggestion list <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param menuKeyEvent Event description
         * @see MenuKeyListener#menuKeyReleased(MenuKeyEvent)
         */
        @Override
        public void menuKeyReleased(final MenuKeyEvent menuKeyEvent)
        {
            // Nothing to do
        }

        /**
         * Called when mouse clicked in suggestion list <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param mouseEvent Event description
         * @see MouseListener#mouseClicked(MouseEvent)
         */
        @Override
        public void mouseClicked(final MouseEvent mouseEvent)
        {
            JHelpSuggestion.this.hidePopup();
        }

        /**
         * Called when mouse pressed in suggestion list <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param mouseEvent Event description
         * @see MouseListener#mousePressed(MouseEvent)
         */
        @Override
        public void mousePressed(final MouseEvent mouseEvent)
        {
            JHelpSuggestion.this.hidePopup();
        }

        /**
         * Called when mouse released in suggestion list <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param mouseEvent Event description
         * @see MouseListener#mouseReleased(MouseEvent)
         */
        @Override
        public void mouseReleased(final MouseEvent mouseEvent)
        {
            JHelpSuggestion.this.hidePopup();
        }

        /**
         * Called when mouse entered in suggestion list <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param mouseEvent Event description
         * @see MouseListener#mouseEntered(MouseEvent)
         */
        @Override
        public void mouseEntered(final MouseEvent mouseEvent)
        {
            // Nothing to do
        }

        /**
         * Called when mouse exit from suggestion list <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param mouseEvent Event description
         * @see MouseListener#mouseExited(MouseEvent)
         */
        @Override
        public void mouseExited(final MouseEvent mouseEvent)
        {
            // Nothing to do
        }

        /**
         * Called when selection change in suggestion list <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param listSelectionEvent Event description
         * @see ListSelectionListener#valueChanged(ListSelectionEvent)
         */
        @Override
        public void valueChanged(final ListSelectionEvent listSelectionEvent)
        {
            JHelpSuggestion.this.select();
        }
    }

    /**
     * Attached/linked {@link JTextComponent}
     */
    private final JTextComponent                                      attachedComponent;
    /**
     * Label to shows the details
     */
    private final JLabel                                              detailsLabel;
    /**
     * Manager of several events
     */
    private final EventManager                                        eventManager;
    /**
     * Filter use for change suggestion list
     */
    private final FilterSuggestionElement<INFORMATION>                filterText;
    /**
     * Popup height
     */
    private       int                                                 popupHeight;
    /**
     * Popup menu that carry suggestion list and details
     */
    private final JPopupMenu                                          popupMenu;
    /**
     * Popup width
     */
    private       int                                                 popupWidth;
    /**
     * Suggestion list
     */
    private final JList<SuggestionElement<INFORMATION>>               suggestionList;
    /**
     * Model of suggestion list
     */
    private final ListFilterableModel<SuggestionElement<INFORMATION>> suggestionModel;

    /**
     * Create a new instance of JHelpSuggestion
     *
     * @param attachedComponent Linked {@link JTextComponent}
     */
    public JHelpSuggestion(final JTextComponent attachedComponent)
    {
        this(attachedComponent, 768, 512, true);
    }

    /**
     * Create a new instance of JHelpSuggestion
     *
     * @param attachedComponent Linked {@link JTextComponent}
     * @param popupWidth        Popup width in pixels
     * @param popupHeight       Popup height in pixels
     * @param showDetails       Indicates if details are show
     */
    public JHelpSuggestion(
            final JTextComponent attachedComponent, final int popupWidth, final int popupHeight,
            final boolean showDetails)
    {
        if (attachedComponent == null)
        {
            throw new NullPointerException("attachedComponent mustn't be null");
        }

        // Initialize
        this.attachedComponent = attachedComponent;
        this.popupWidth = popupWidth;
        this.popupHeight = popupHeight;
        this.filterText = new FilterSuggestionElement<INFORMATION>();
        this.suggestionModel = new ListFilterableModel<SuggestionElement<INFORMATION>>();
        this.suggestionModel.setFilter(this.filterText);
        this.eventManager = new EventManager();

        // Components
        this.popupMenu = new JPopupMenu();
        this.suggestionList = new JList<SuggestionElement<INFORMATION>>(this.suggestionModel);
        this.suggestionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        final JPanel panel = new JPanel(new BorderLayout());
        this.popupMenu.add(new JHelpFixedSizePanel(panel, this.popupWidth, this.popupHeight));
        panel.add(new JScrollPane(this.suggestionList), BorderLayout.WEST);
        this.detailsLabel = new JLabel("", SwingConstants.CENTER);
        this.detailsLabel.setFont(JHelpConstantsSmooth.FONT_CAPTION.getFont());

        if (showDetails)
        {
            panel.add(new JHelpLimitSizePanel(new JScrollPane(this.detailsLabel), (this.popupWidth << 1) / 3,
                                              this.popupHeight), BorderLayout.CENTER);
        }

        // Link listeners
        this.attachedComponent.addCaretListener(this.eventManager);
        this.attachedComponent.addMouseListener(this.eventManager);
        this.popupMenu.addMenuKeyListener(this.eventManager);
        this.suggestionList.addListSelectionListener(this.eventManager);

        this.setCellRenderer(null);
    }

    /**
     * Delete a character in linked {@link JTextComponent}
     *
     * @param current Indicates if have to delete current character or previous one
     */
    private void delete(final boolean current)
    {
        int deletePosition = this.attachedComponent.getCaretPosition();

        if (!current)
        {
            deletePosition--;
        }

        if ((deletePosition >= 0) && (deletePosition < this.attachedComponent.getDocument().getLength()))
        {
            try
            {
                this.attachedComponent.getDocument().remove(deletePosition, 1);
                this.attachedComponent.setCaretPosition(deletePosition);
            }
            catch (final BadLocationException exception)
            {
                // Should never happen since we have control the position before
                Debug.exception(exception);
            }
        }
    }

    /**
     * Select a suggestion without choose it, just put the focus on it
     *
     * @param index Suggestion index
     */
    private void selectChoiceIndex(final int index)
    {
        this.suggestionList.removeListSelectionListener(this.eventManager);
        this.suggestionList.setSelectedIndex(index);
        this.suggestionList.addListSelectionListener(this.eventManager);
        final String details = this.suggestionList.getSelectedValue().helpDetails();

        if (details == null)
        {
            this.detailsLabel.setText("");
        }
        else
        {
            this.detailsLabel.setText(UtilText.resolveImagesLinkInHTML(details));
        }

        this.suggestionList.ensureIndexIsVisible(index);
    }

    /**
     * Hide the suggestion list
     */
    void hidePopup()
    {
        if (this.popupMenu.isShowing())
        {
            this.popupMenu.setVisible(false);
        }
    }

    /**
     * Called when receive a command keyboard. <br>
     * Here we reflect some commands to linked {@link JTextComponent} that the user can continue to type even if the linked
     * {@link JTextComponent} haven't the focus
     *
     * @param keyCode   Key code
     * @param modifiers Key modifiers (control, shift, alt, altGr, meta)
     */
    void keyCommand(final int keyCode, final int modifiers)
    {
        int selection;
        int caretPosition;

        switch (keyCode)
        {
            case KeyEvent.VK_BACKSPACE:
                this.delete(false);
                break;
            case KeyEvent.VK_DELETE:
                this.delete(true);
                break;
            case KeyEvent.VK_ESCAPE:
                this.hidePopup();
                break;
            case KeyEvent.VK_LEFT:
                this.hidePopup();
                caretPosition = this.attachedComponent.getCaretPosition();

                if (caretPosition > 0)
                {
                    this.attachedComponent.setCaretPosition(caretPosition - 1);
                }

                break;
            case KeyEvent.VK_RIGHT:
                this.hidePopup();
                caretPosition = this.attachedComponent.getCaretPosition();

                if (caretPosition < this.attachedComponent.getText().length())
                {
                    this.attachedComponent.setCaretPosition(caretPosition + 1);
                }

                break;
            case KeyEvent.VK_UP:
                // Up not loop, it exit if first element
                selection = this.suggestionList.getSelectedIndex();

                if (selection > 0)
                {
                    this.selectChoiceIndex(selection - 1);
                }
                else
                {
                    this.hidePopup();
                }
                break;
            case KeyEvent.VK_DOWN:
                // Down loop
                selection = this.suggestionList.getSelectedIndex();

                if (selection < (this.suggestionModel.getSize() - 1))
                {
                    this.selectChoiceIndex(selection + 1);
                }
                else
                {
                    this.selectChoiceIndex(0);
                }
                break;
            case KeyEvent.VK_ENTER:
                this.select();
                break;
            case KeyEvent.VK_F4:
                if ((modifiers & KeyEvent.MODIFIER_ALT) != 0)
                {
                    this.hidePopup();
                }
            default:
                break;
        }
    }

    /**
     * Called when user type some text.<br>
     * Here we just send characters to linked {@link JTextComponent} like if user directly type on it, so he can continue to type
     *
     * @param keyCode Key code
     * @param keyChar Key character
     */
    void keyTyped(final int keyCode, final char keyChar)
    {
        if (keyChar >= 32)
        {
            try
            {
                this.attachedComponent.getDocument()
                                      .insertString(this.attachedComponent.getCaretPosition(), String.valueOf(keyChar),
                                                    null);
            }
            catch (final BadLocationException exception)
            {
                Debug.exception(exception);
            }
        }
    }

    /**
     * Called when caret in linked {@link JTextComponent} position change
     */
    void positionCaretChange()
    {
        final String text          = this.attachedComponent.getText();
        int          caretPosition = this.attachedComponent.getCaretPosition();

        if (caretPosition <= 0)
        {
            // No text before caret, so no suggestion
            this.hidePopup();
            return;
        }

        // Extract the word form first white space before caret to caret position
        caretPosition--;
        final int index = UtilText.lastIndexOf(text, caretPosition, ' ', '\n', '\t', '\r', '\f');

        if (index < caretPosition)
        {
            // Create the regular expression with word
            final String regex = UtilText.removeAccent(
                    text.substring(index + 1, caretPosition + 1).replaceAll("[^a-zA-Z0-9]", "").toLowerCase()).trim();

            if (regex.length() == 0)
            {
                // If the regular expression is empty, no suggestion
                this.hidePopup();
                return;
            }

            // Filter the suggestion list
            this.filterText.regex(regex + ".*", false, false);
            int size = this.suggestionModel.getSize();

            if (size > 0)
            {
                if (caretPosition > 0)
                {
                    int start =
                            UtilText.lastIndexOf(text, Math.min(caretPosition - 1, text.length() - 1), ' ', '\n', '\t',
                                                 '\r', '\f') + 1;

                    if (start < 0)
                    {
                        start = 0;
                    }

                    int end = UtilText.indexOf(text, start, ' ', '\n', '\t', '\r', '\f');

                    if (end < 0)
                    {
                        end = text.length();
                    }

                    if (start <= end)
                    {
                        final String word = text.substring(start, end);

                        for (int i = 0; i < size; i++)
                        {
                            //noinspection ConstantConditions
                            if (this.suggestionModel.getElementAt(i)
                                                    .keyWord()
                                                    .equals(word))
                            {
                                // If current word is one of suggestion, don't bother the user
                                size = -1;
                                break;
                            }
                        }
                    }
                }
            }

            if (size > 0)
            {
                // Show the suggestion list
                // First compute the position on screen
                final Document document = this.attachedComponent.getDocument();
                Point          position = this.attachedComponent.getCaret().getMagicCaretPosition();

                if (position == null)
                {
                    // Sometimes magic caret return a null position, we try to get it in an other way (more heavy in computing)
                    try
                    {
                        final Rectangle box = this.attachedComponent.modelToView(caretPosition);
                        position = new Point(box.x, box.y);
                    }
                    catch (final Exception exception)
                    {
                        // Should never reach their since position is sure to be correct
                        return;
                    }
                }

                // Now we have the position, compute the height of the text (We can't use the result of modelToView, because in some
                // look and feel the height is not accurate)
                // For compute the height, we get the font a current position and get its height
                JHelpFont font;

                if (document instanceof StyledDocument)
                {
                    // For styled document the information depends where the cursor is
                    final AttributeSet attributeSet = ((StyledDocument) document).getCharacterElement(caretPosition)
                                                                                 .getAttributes();
                    font = new JHelpFont(StyleConstants.getFontFamily(attributeSet),
                                         StyleConstants.getFontSize(attributeSet), StyleConstants.isBold(attributeSet),
                                         StyleConstants.isItalic(attributeSet),
                                         StyleConstants.isUnderline(attributeSet));
                }
                else
                {
                    // For simple document, the font is the same for all position in document
                    font = new JHelpFont(this.attachedComponent.getFont(), false);
                }

                final Dimension parentSize = this.attachedComponent.getSize();
                final int x = Math.max(1,
                                       Math.min(position.x, (parentSize.width - this.popupWidth) - 1));
                int y = position.y + font.getHeight();

                if ((y + this.popupHeight) >= (parentSize.height - 1))
                {
                    y = Math.max(1, position.y - this.popupHeight);
                }

                // Shows the suggestion list
                this.popupMenu.show(this.attachedComponent, x, y);

                // Select the first element of suggestion list
                this.selectChoiceIndex(0);

                return;
            }
        }

        // If no suggestion list, hide it if need
        this.hidePopup();
    }

    /**
     * Called when user choose a suggestion
     */
    void select()
    {
        // Get the selected word
        final String word = this.suggestionList.getSelectedValue().keyWord();

        if (word == null)
        {
            // In case of no selection (Should never happen)
            return;
        }

        try
        {
            // Compute where insert the word
            final String text          = this.attachedComponent.getText();
            final int    caretPosition = this.attachedComponent.getCaretPosition();

            if (caretPosition <= 0)
            {
                // If caret position at start of document no insertion (Should never happen)
                return;
            }

            // Search start of word under caret position
            int start =
                    UtilText.lastIndexOf(text, Math.min(caretPosition - 1, text.length() - 1), ' ', '\n', '\t', '\r',
                                         '\f') + 1;

            if (start < 0)
            {
                start = 0;
            }

            // Search end of word under caret position
            int end = UtilText.indexOf(text, start, ' ', '\n', '\t', '\r', '\f');

            if (end < 0)
            {
                end = text.length();
            }

            if (start > end)
            {
                // Thats means their no word to replace (Should never happen)
                return;
            }

            // Remove current word under caret and replace it by suggestion word
            final Document document = this.attachedComponent.getDocument();
            document.remove(start, end - start);
            document.insertString(start, word, null);
            this.hidePopup();
            this.attachedComponent.setCaretPosition(start + word.length());
        }
        catch (final BadLocationException exception)
        {
            // Should never reach their, because positions are checked
            Debug.exception(exception);
        }
    }

    /**
     * Add suggestions just based on key words (No information, no details)
     *
     * @param keyWords List of keywords to add
     * @throws NullPointerException If list is <code>null</code> or one of keywords is <code>null</code>
     */
    public void addSuggestion(final String... keyWords)
    {
        for (final String keyWord : keyWords)
        {
            this.addSuggestion(keyWord, null, null);
        }
    }

    /**
     * Add a suggestion without details
     *
     * @param keyWord     Keyword
     * @param information Information associated (Use <code>null</code> if none)
     * @throws NullPointerException If keyword is <code>null</code>
     */
    public void addSuggestion(final String keyWord, final INFORMATION information)
    {
        this.addSuggestion(keyWord, information, null);
    }

    /**
     * Add a suggestion
     *
     * @param keyWord     Keyword
     * @param information Information (Use <code>null</code> if none)
     * @param helpDetails Details (Use <code>null</code> if none)
     * @throws NullPointerException If keyword is <code>null</code>
     */
    public void addSuggestion(final String keyWord, final INFORMATION information, final String helpDetails)
    {
        this.suggestionModel.addElement(new SuggestionElement<INFORMATION>(keyWord, information, helpDetails));
    }

    /**
     * Add a list of suggestion
     *
     * @param suggestions Suggestions to add
     * @throws NullPointerException If list is <code>null</code> or one of suggestion is <code>null</code>
     */
    @SuppressWarnings("unchecked")
    public void addSuggestion(final SuggestionElement<INFORMATION>... suggestions)
    {
        this.suggestionModel.addElements(suggestions);
    }

    /**
     * Change the cell renderer of suggestion list
     *
     * @param renderer New renderer (Can use <code>null</code> to return to default one)
     */
    public void setCellRenderer(final ListCellRenderer<SuggestionElement<INFORMATION>> renderer)
    {
        if (renderer == null)
        {
            this.suggestionList.setCellRenderer(new JHelpSuggestionDefaultListCellRenderer<INFORMATION>());
        }
        else
        {
            this.suggestionList.setCellRenderer(renderer);
        }
    }
}
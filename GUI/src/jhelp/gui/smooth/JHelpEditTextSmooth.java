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
package jhelp.gui.smooth;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import jhelp.gui.smooth.event.SmoothEditTextListener;
import jhelp.gui.smooth.event.SmoothKeyInformation;
import jhelp.gui.undoRedo.UndoRedoAction;
import jhelp.gui.undoRedo.UndoRedoManager;
import jhelp.util.gui.ClipBoardManager;
import jhelp.util.gui.JHelpEditTextImage;
import jhelp.util.gui.JHelpFont;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpPaint;
import jhelp.util.text.UtilText;

/**
 * Edit text smooth.<br>
 * The <b>ENTER</b> key is used for validation, it is possible to registener listener for this event
 *
 * @author JHelp
 */
public class JHelpEditTextSmooth
        extends JHelpComponentSmooth
{
    /**
     * Action for undo/redo text edition
     *
     * @author JHelp
     */
    class ActionTextChanged
            implements UndoRedoAction
    {
        /** Text after action */
        private       String              after;
        /** Text before action */
        private final String              before;
        /** Cursor position after text changed */
        private       int                 cursorAfter;
        /** Cursor position before text changed */
        private final int                 cursorBefore;
        /** Edit text where action take place */
        private final JHelpEditTextSmooth editTextSmooth;

        /**
         * Create a new instance of ActionTextChanged
         *
         * @param editTextSmooth
         *           Edit text where action take place
         * @param before
         *           Text before action
         * @param cursorBefore
         *           Cursor position before text changed
         * @param after
         *           Text after action
         * @param cursorAfter
         *           Cursor position after text changed
         */
        ActionTextChanged(
                final JHelpEditTextSmooth editTextSmooth, final String before, final int cursorBefore,
                final String after, final int cursorAfter)
        {
            this.editTextSmooth = editTextSmooth;
            this.before = before;
            this.cursorBefore = cursorBefore;
            this.after = after;
            this.cursorAfter = cursorAfter;
        }

        /**
         * Try to compress this action with a following one <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param nextUndoRedo
         *           Follow action
         * @return Compressed action or {@code null} if compression not possible
         * @see jhelp.gui.undoRedo.UndoRedoAction#compress(jhelp.gui.undoRedo.UndoRedoAction)
         */
        @Override
        public UndoRedoAction compress(final UndoRedoAction nextUndoRedo)
        {
            // May be a previous compression make this action dummy (does nothing), so we can remove it now in that case
            if ((this.before.equals(this.after)) && (this.cursorBefore == this.cursorAfter))
            {
                return nextUndoRedo;
            }

            if (nextUndoRedo instanceof ActionTextChanged)
            {
                final ActionTextChanged actionTextChanged = (ActionTextChanged) nextUndoRedo;

                if (this.editTextSmooth == actionTextChanged.editTextSmooth)
                {
                    this.after = actionTextChanged.after;
                    this.cursorAfter = actionTextChanged.cursorAfter;
                    return this;
                }
            }

            return null;
        }

        /**
         * Do the action again <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @see jhelp.gui.undoRedo.UndoRedoAction#redo()
         */
        @Override
        public void redo()
        {
            this.editTextSmooth.cursorPosition = this.cursorAfter;
            this.editTextSmooth.stringBuilder.delete(0, this.editTextSmooth.stringBuilder.length());
            this.editTextSmooth.stringBuilder.append(this.after);
        }

        /**
         * Undo the action <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @see jhelp.gui.undoRedo.UndoRedoAction#undo()
         */
        @Override
        public void undo()
        {
            this.editTextSmooth.cursorPosition = this.cursorBefore;
            this.editTextSmooth.stringBuilder.delete(0, this.editTextSmooth.stringBuilder.length());
            this.editTextSmooth.stringBuilder.append(this.before);
        }
    }

    /** Image where text and cursor are draw */
    private final JHelpEditTextImage           editTextImage;
    /** Listeners of validation */
    private final List<SmoothEditTextListener> editTextListeners;
    /** Indicates if edit text is editable */
    private       boolean                      editable;
    /** Foreground color */
    private final int                          foreground;
    /** Lock for synchronization */
    private final Object lock = new Object();
    /** Current cursor position */
    int cursorPosition;
    /** Current text */
    final StringBuilder stringBuilder;

    /**
     * Create an edit text with a unique color for the text
     *
     * @param font
     *           Font to use
     * @param size
     *           Number of letter in edit text
     * @param color
     *           Text color
     * @param colorCursor
     *           Cursor color
     */
    public JHelpEditTextSmooth(final JHelpFont font, final int size, final int color, final int colorCursor)
    {
        this.editTextListeners = new ArrayList<SmoothEditTextListener>();
        this.foreground = color;
        this.editable = true;
        this.cursorPosition = 0;
        this.stringBuilder = new StringBuilder();
        this.editTextImage = JHelpEditTextImage.createEditTextImage(font, size, color, colorCursor);
        this.backgroundAndShadowColor(JHelpConstantsSmooth.COLOR_WHITE);
    }

    /**
     * Create an edit text with a texture for draw the text
     *
     * @param font
     *           Font to use
     * @param size
     *           Number of letter in edit text
     * @param texture
     *           Texture use for text
     * @param colorCursor
     *           Cursor cursor
     */
    public JHelpEditTextSmooth(final JHelpFont font, final int size, final JHelpImage texture, final int colorCursor)
    {
        this.editTextListeners = new ArrayList<SmoothEditTextListener>();
        this.foreground = colorCursor;
        this.editable = true;
        this.cursorPosition = 0;
        this.stringBuilder = new StringBuilder();
        this.editTextImage = JHelpEditTextImage.createEditTextImage(font, size, texture, colorCursor);
        this.backgroundAndShadowColor(JHelpConstantsSmooth.COLOR_WHITE);
    }

    /**
     * Create an edit text with paint for fill text
     *
     * @param font
     *           Font to use
     * @param size
     *           Number of letter in edit text
     * @param paint
     *           Paint to use for fill the text
     * @param colorCursor
     *           Cursor color
     */
    public JHelpEditTextSmooth(final JHelpFont font, final int size, final JHelpPaint paint, final int colorCursor)
    {
        this.editTextListeners = new ArrayList<SmoothEditTextListener>();
        this.foreground = colorCursor;
        this.editable = true;
        this.cursorPosition = 0;
        this.stringBuilder = new StringBuilder();
        this.editTextImage = JHelpEditTextImage.createEditTextImage(font, size, paint, colorCursor);
        this.backgroundAndShadowColor(JHelpConstantsSmooth.COLOR_WHITE);
    }

    /**
     * Signal a text changed to add it in Undo/Redo queue.<br>
     * Called just after a change
     *
     * @param before
     *           Text before change
     * @param cursorBefore
     *           Cursor position before change
     */
    private void textChanged(final String before, final int cursorBefore)
    {
        // /Control if its a real change
        if ((before.equals(this.stringBuilder.toString())) && (cursorBefore == this.cursorPosition))
        {
            return;
        }

        final ActionTextChanged actionTextChanged = new ActionTextChanged(this, before, cursorBefore,
                                                                          this.stringBuilder.toString(),
                                                                          this.cursorPosition);
        UndoRedoManager.UNDO_REDO_MANAGER.registerAction(actionTextChanged, false);
    }

    /**
     * Signal to listeners a validation happen
     */
    final void fireEditTextEnterTyped()
    {
        synchronized (this.lock)
        {
            for (final SmoothEditTextListener editTextListener : this.editTextListeners)
            {
                editTextListener.editTextEnterTyped(this);
            }
        }
    }

    /**
     * Draw the edit text <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param image
     *           Image where draw
     * @param x
     *           X
     * @param y
     *           Y
     * @param width
     *           Width
     * @param height
     *           Height
     * @param parentWidth
     *           Parent width
     * @param parentHeight
     *           Parent height
     * @see jhelp.gui.smooth.JHelpComponentSmooth#paint(JHelpImage, int, int, int, int, int, int)
     */
    @Override
    protected void paint(
            final JHelpImage image, final int x, final int y, final int width, final int height, final int parentWidth,
            final int parentHeight)
    {
        this.drawBackground(image, x, y, width, height);
        final Rectangle bounds = this.bounds();
        final int position = this.focus()
                             ? this.cursorPosition
                             : -1;

        this.editTextImage.setText(this.stringBuilder.toString(), position);
        final int xx = bounds.x + ((bounds.width - this.editTextImage.getWidth()) >> 1);
        final int yy = bounds.y + ((bounds.height - this.editTextImage.getHeight()) >> 1);
        image.drawImage(xx + 3, yy + 3, this.editTextImage);
        image.drawRectangle(xx, yy, this.editTextImage.getWidth() + 6, this.editTextImage.getHeight() + 6,
                            this.foreground);
    }

    /**
     * Compute edit text preferred size <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Preferred size
     * @see jhelp.gui.smooth.JHelpComponentSmooth#preferredSizeInternal()
     */
    @Override
    protected Dimension preferredSizeInternal()
    {
        return new Dimension(this.editTextImage.getWidth() + 6, this.editTextImage.getHeight() + 6);
    }

    /**
     * Process key events <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param keyInformation
     *           Key event description
     * @see jhelp.gui.smooth.JHelpComponentSmooth#processKeyEvent(SmoothKeyInformation)
     */
    @Override
    protected void processKeyEvent(final SmoothKeyInformation keyInformation)
    {
        if ((!this.focus()) || (!this.focusable()))
        {
            return;
        }

        final String before       = this.stringBuilder.toString();
        final int    cursorBefore = this.cursorPosition;
        final int    length       = this.stringBuilder.length();

        switch (keyInformation.keyCode)
        {
            case KeyEvent.VK_DELETE:
                if (this.cursorPosition < length)
                {
                    this.stringBuilder.delete(this.cursorPosition, this.cursorPosition + 1);
                    this.textChanged(before, cursorBefore);
                }

                return;
            case KeyEvent.VK_BACK_SPACE:
                if (this.cursorPosition > 0)
                {
                    this.stringBuilder.delete(this.cursorPosition - 1, this.cursorPosition);
                    this.cursorPosition--;
                    this.textChanged(before, cursorBefore);
                }

                return;
            case KeyEvent.VK_LEFT:
                if (this.cursorPosition > 0)
                {
                    this.cursorPosition--;
                    this.textChanged(before, cursorBefore);
                }

                return;
            case KeyEvent.VK_HOME:
                this.cursorPosition = 0;
                this.textChanged(before, cursorBefore);
                return;
            case KeyEvent.VK_RIGHT:
                if (this.cursorPosition < length)
                {
                    this.cursorPosition++;
                    this.textChanged(before, cursorBefore);
                }

                return;
            case KeyEvent.VK_END:
                this.cursorPosition = length;
                this.textChanged(before, cursorBefore);
                return;
            case KeyEvent.VK_V:
                if (keyInformation.controlDown)
                {
                    if (ClipBoardManager.CLIP_BOARD.isStringStore())
                    {
                        this.appendAtCursorPosition(ClipBoardManager.CLIP_BOARD.obtainString());
                    }

                    return;
                }

                break;
        }

        if ((keyInformation.keyCode == KeyEvent.VK_ENTER) || (keyInformation.keyChar == '\n'))
        {
            this.fireEditTextEnterTyped();
            return;
        }

        if (keyInformation.keyChar < ' ')
        {
            return;
        }

        if ((this.cursorPosition >= length) || (this.cursorPosition < 0))
        {
            this.stringBuilder.append(keyInformation.keyChar);
        }
        else
        {
            this.stringBuilder.insert(this.cursorPosition, keyInformation.keyChar);
        }

        this.cursorPosition++;
        this.textChanged(before, cursorBefore);
    }

    /**
     * Indicates if edit text is currently focusable <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return {@code true} if edit text is currently focusable
     * @see jhelp.gui.smooth.JHelpComponentSmooth#focusable()
     */
    @Override
    public boolean focusable()
    {
        return (this.editable) && (this.visible());
    }

    /**
     * Append text at current cursor position
     *
     * @param text
     *           Text to append
     */
    public void appendAtCursorPosition(String text)
    {
        final String before       = this.stringBuilder.toString();
        final int    cursorBefore = this.cursorPosition;
        text = UtilText.replaceWhiteCharactersBy(text, ' ');
        this.stringBuilder.insert(this.cursorPosition, text);
        this.cursorPosition += text.length();
        this.textChanged(before, cursorBefore);
    }

    /**
     * Append text at the end of current text
     *
     * @param text
     *           Text to append
     */
    public void appendTextAtEnd(final String text)
    {
        final String before       = this.stringBuilder.toString();
        final int    cursorBefore = this.cursorPosition;
        this.cursorPosition = this.stringBuilder.length();
        this.textChanged(before, cursorBefore);
        this.appendAtCursorPosition(text);
    }

    /**
     * Current cursor position
     *
     * @return Cursor position
     */
    public int getCursorPosition()
    {
        return this.cursorPosition;
    }

    /**
     * Change cursor position
     *
     * @param position
     *           New cursor position
     */
    public void setCursorPosition(final int position)
    {
        final String before       = this.stringBuilder.toString();
        final int    cursorBefore = this.cursorPosition;
        this.cursorPosition = Math.max(0, Math.min(this.stringBuilder.length(), position));
        this.textChanged(before, cursorBefore);
    }

    /**
     * Typed text
     *
     * @return Typed text
     */
    public String getText()
    {
        return this.stringBuilder.toString();
    }

    /**
     * Change the text
     *
     * @param text
     *           New text
     */
    public void setText(final String text)
    {
        final String before       = this.stringBuilder.toString();
        final int    cursorBefore = this.cursorPosition;
        this.stringBuilder.delete(0, this.stringBuilder.length());
        this.cursorPosition = 0;
        this.textChanged(before, cursorBefore);
        this.appendAtCursorPosition(text);
    }

    /**
     * Indicates if edit text is editable
     *
     * @return {@code true} if edit text is editable
     */
    public boolean isEditable()
    {
        return this.editable;
    }

    /**
     * Change editable state
     *
     * @param editable
     *           New editable state
     */
    public void setEditable(final boolean editable)
    {
        this.editable = editable;
    }

    /**
     * Register listener for text validation
     *
     * @param editTextListener
     *           Listener to register
     */
    public void registerEditTextListener(final SmoothEditTextListener editTextListener)
    {
        if (editTextListener == null)
        {
            throw new NullPointerException("editTextListener mustn't be null");
        }

        synchronized (this.lock)
        {
            if (!this.editTextListeners.contains(editTextListener))
            {
                this.editTextListeners.add(editTextListener);
            }
        }
    }

    /**
     * Put cursor at the end of the text
     */
    public void setCursorAtEnd()
    {
        final String before       = this.stringBuilder.toString();
        final int    cursorBefore = this.cursorPosition;
        this.cursorPosition = this.stringBuilder.length();
        this.textChanged(before, cursorBefore);
    }

    /**
     * Put cursor at start of the text
     */
    public void setCursorAtStart()
    {
        final String before       = this.stringBuilder.toString();
        final int    cursorBefore = this.cursorPosition;
        this.cursorPosition = 0;
        this.textChanged(before, cursorBefore);
    }

    /**
     * Change the background of the text area
     *
     * @param color
     *           New background of the text area
     */
    public void setEditTextBackground(final int color)
    {
        this.editTextImage.setBackgroundColor(color);
    }

    /**
     * Change the background of the text area
     *
     * @param image
     *           New background of the text area
     */
    public void setEditTextBackground(final JHelpImage image)
    {
        this.editTextImage.setBackgroundTexture(image);
    }

    /**
     * Change the background of the text area
     *
     * @param paint
     *           New background of the text area
     */
    public void setEditTextBackground(final JHelpPaint paint)
    {
        this.editTextImage.setBackgroundPaint(paint);
    }

    /**
     * Unregister text validation listener
     *
     * @param editTextListener
     *           Listener to unregister
     */
    public void unregisteredEditTextListener(final SmoothEditTextListener editTextListener)
    {
        synchronized (this.lock)
        {
            this.editTextListeners.remove(editTextListener);
        }
    }
}
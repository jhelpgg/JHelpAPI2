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
package jhelp.gui.undoRedo;

/**
 * Undo/redo manager <br>
 * <br>
 * Last modification : 4 fevr. 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public class UndoRedoManager
{
    /**
     * Limit of undo/redo
     */
    private static final int             UNDO_REDO_LIMIT   = 1024;
    /**
     * Manager of undo/redo singleton
     */
    public static final  UndoRedoManager UNDO_REDO_MANAGER = new UndoRedoManager();
    /**
     * List of undo/redo actions
     */
    private final UndoRedoAction[] undoRedoActionList;
    /**
     * Index of the actual end of the undo/redo list
     */
    private       int              undoRedoIndex;
    /**
     * Index of actual position read of undo/redo list
     */
    private       int              undoRedoReadIndex;

    /**
     * Constructs UndoRedoManager
     */
    private UndoRedoManager()
    {
        this.undoRedoActionList = new UndoRedoAction[UndoRedoManager.UNDO_REDO_LIMIT];
        this.undoRedoIndex = 0;
        this.undoRedoReadIndex = -1;
    }

    /**
     * Indicates if there are something to redo
     *
     * @return {@code true} if there are something to redo
     */
    public boolean canRedo()
    {
        return (this.undoRedoReadIndex + 1) < this.undoRedoIndex;
    }

    /**
     * Indicates if there are something to undo
     *
     * @return {@code true} if there are something to undo
     */
    public boolean canUndo()
    {
        return this.undoRedoReadIndex >= 0;
    }

    /**
     * Actual list of undo/redo
     *
     * @return Actual list of undo/redo
     */
    public UndoRedoAction[] readActualList()
    {
        final UndoRedoAction[] actualList = new UndoRedoAction[this.undoRedoIndex];
        System.arraycopy(this.undoRedoActionList, 0, actualList, 0, this.undoRedoIndex);
        return actualList;
    }

    /**
     * Redo the last undo action
     */
    public void redo()
    {
        if (!this.canRedo())
        {
            throw new IllegalStateException("Nothing to redo");
        }

        this.undoRedoActionList[this.undoRedoReadIndex + 1].redo();
        this.undoRedoReadIndex++;
    }

    /**
     * Register an undo/redo action
     *
     * @param undoRedoAction Action to register
     * @param doIt           Choose do action in same time
     */
    public void registerAction(final UndoRedoAction undoRedoAction, final boolean doIt)
    {
        if (undoRedoAction == null)
        {
            throw new NullPointerException("undoRedoAction mustn't be null");
        }

        if (doIt)
        {
            // Do the action
            undoRedoAction.redo();
        }

        // If the read is before the end, discard all undo/redo action after the
        // read
        for (int i = this.undoRedoReadIndex + 1; i < this.undoRedoIndex; i++)
        {
            this.undoRedoActionList[i] = null;
        }
        this.undoRedoIndex = this.undoRedoReadIndex + 1;

        // If there have enough space, just add the action
        if (this.undoRedoIndex < UndoRedoManager.UNDO_REDO_LIMIT)
        {
            this.undoRedoActionList[this.undoRedoIndex++] = undoRedoAction;
            this.undoRedoReadIndex++;
            return;
        }

        // Try compress the list if it is full
        boolean        compress     = false;
        UndoRedoAction actualAction = this.undoRedoActionList[0];
        UndoRedoAction compressUndoRedo;
        int            max          = this.undoRedoIndex;
        for (int i = 1; i < max; i++)
        {
            compressUndoRedo = actualAction.compress(this.undoRedoActionList[i]);
            if (compressUndoRedo == null)
            {
                actualAction = this.undoRedoActionList[i];
            }
            else
            {
                actualAction = compressUndoRedo;
                System.arraycopy(this.undoRedoActionList, i, this.undoRedoActionList, i - 1, max - i);
                this.undoRedoActionList[i - 1] = compressUndoRedo;
                max--;

                if (i < max)
                {
                    actualAction = this.undoRedoActionList[i];
                }

                compress = true;
            }
        }

        // If at least one compression append, just add at the end
        if (compress)
        {
            this.undoRedoReadIndex = this.undoRedoIndex = max;
            this.undoRedoActionList[this.undoRedoIndex++] = undoRedoAction;
            return;
        }

        // We have no other choice to discard the first undo/redo action
        System.arraycopy(this.undoRedoActionList, 1, this.undoRedoActionList, 0, UndoRedoManager.UNDO_REDO_LIMIT - 1);
        this.undoRedoActionList[UndoRedoManager.UNDO_REDO_LIMIT - 1] = undoRedoAction;
        this.undoRedoReadIndex = UndoRedoManager.UNDO_REDO_LIMIT - 1;
    }

    /**
     * Undo all action (since read index), remove the specified undo/redo action, redo all action (since read index).<br>
     * The undo all and redo all are not done if the index is after the read index.<br>
     * If the index is before the read index, read index is decremented after the call
     *
     * @param indexUndoRedo Index of remove undo/redo action
     */
    public void removeSpecificUndoRedo(final int indexUndoRedo)
    {
        if ((indexUndoRedo < 0) || (indexUndoRedo >= this.undoRedoIndex))
        {
            throw new IllegalArgumentException(
                    "Index specified dosen't exist. Must actual in [0, " + this.undoRedoIndex + "[ not " +
                    indexUndoRedo);
        }

        if (this.undoRedoReadIndex > indexUndoRedo)
        {
            final int temp = this.undoRedoReadIndex;
            while (this.canUndo())
            {
                this.undo();
            }
            this.undoRedoReadIndex = temp;
        }

        System.arraycopy(this.undoRedoActionList, indexUndoRedo + 1, this.undoRedoActionList, indexUndoRedo,
                         UndoRedoManager.UNDO_REDO_LIMIT - indexUndoRedo - 1);

        if (this.undoRedoReadIndex > indexUndoRedo)
        {
            final int temp = this.undoRedoReadIndex;
            this.undoRedoReadIndex = -1;
            for (int i = 0; i < temp; i++)
            {
                this.redo();
            }
        }

        this.undoRedoIndex--;
        if (this.undoRedoReadIndex > this.undoRedoIndex)
        {
            this.undoRedoReadIndex = this.undoRedoIndex;
        }
    }

    /**
     * Total of undo/redo action number
     *
     * @return Total of undo/redo action number
     */
    public int totalUndoRedo()
    {
        return this.undoRedoIndex;
    }

    /**
     * Undo the last action
     */
    public void undo()
    {
        if (!this.canUndo())
        {
            throw new IllegalStateException("Nothing to undo");
        }

        this.undoRedoActionList[this.undoRedoReadIndex].undo();
        this.undoRedoReadIndex--;
    }

    /**
     * Index in undo/redo list where we are
     *
     * @return Index in undo/redo list where we are
     */
    public int undoRedoReadIndex()
    {
        return this.undoRedoReadIndex;
    }
}
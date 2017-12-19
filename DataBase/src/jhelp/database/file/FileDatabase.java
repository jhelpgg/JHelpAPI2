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
package jhelp.database.file;

import java.io.File;
import java.io.IOException;
import java.util.Stack;
import jhelp.database.BinaryConditionAND;
import jhelp.database.ColumnDescription;
import jhelp.database.Condition;
import jhelp.database.ConditionColumnEquals;
import jhelp.database.ConditionIN;
import jhelp.database.UnaryConditionNOT;
import jhelp.database.Data;
import jhelp.database.DataType;
import jhelp.database.Database;
import jhelp.database.DatabaseException;
import jhelp.database.QueryResult;
import jhelp.database.SelectQuery;
import jhelp.database.UpdateQuery;
import jhelp.database.Value;
import jhelp.util.debug.Debug;
import jhelp.util.io.UtilIO;
import jhelp.util.list.ArrayInt;
import jhelp.util.text.UtilText;
import jhelp.util.thread.RunnableTask;
import jhelp.util.thread.ThreadManager;
import jhelp.util.util.Utilities;

/**
 * Data base store files with key words<br>
 * <br>
 * Last modification : 17 avr. 2009<br>
 * Version 0.0.0<br>
 *
 * @author JHelp
 */
public class FileDatabase
{
    /**
     * ASSOCIATIONS table. This table associate words with files
     */
    private static final String              ASSOCIATIONS       = "ASSOCIATIONS";
    /**
     * Column FILE_ID in ASSOCIATIONS table
     */
    private static final ColumnDescription   COLUMN_FILE_ID     = new ColumnDescription("FILE_ID", DataType.INT);
    /**
     * Column 'id' in FILES, WORDS and ASSOCIATIONS table
     */
    private static final ColumnDescription   COLUMN_ID          = new ColumnDescription("id", DataType.INT);
    /**
     * Column PATH in table FILES
     */
    private static final ColumnDescription   COLUMN_PATH        = new ColumnDescription("PATH", DataType.LONGVARCHAR);
    /**
     * Column WORD in table WORDS
     */
    private static final ColumnDescription   COLUMN_WORD        = new ColumnDescription("WORD", DataType.VARCHAR);
    /**
     * Column WORD_ID in table ASSOCIATIONS
     */
    private static final ColumnDescription   COLUMN_WORD_ID     = new ColumnDescription("WORD_ID", DataType.INT);
    /**
     * Table FILES
     */
    private static final String              FILES              = "FILES";
    /**
     * Columns in ASSOCIATIONS table
     */
    private static final ColumnDescription[] TABLE_ASSOCIATIONS =
            {
                    FileDatabase.COLUMN_FILE_ID, FileDatabase.COLUMN_WORD_ID
            };
    /**
     * Columns in FILES table
     */
    private static final ColumnDescription[] TABLE_FILES        =
            {
                    FileDatabase.COLUMN_PATH
            };
    /**
     * Columns in WORDS table
     */
    private static final ColumnDescription[] TABLE_WORDS        =
            {
                    FileDatabase.COLUMN_WORD
            };
    /**
     * Table WORDS
     */
    private static final String              WORDS              = "WORDS";

    /**
     * Thread to fill automatically with all files on the computer
     */
    private class ThreadFillFiles implements RunnableTask
    {
        /**
         * Constructs ThreadFillFiles
         */
        public ThreadFillFiles()
        {
        }

        /**
         * Fill with computer's files
         *
         * @see Thread#run()
         */
        @Override
        public void run()
        {
            final Stack<File> stack = new Stack<>();

            File[] children = File.listRoots();
            if (children != null)
            {
                for (final File child : children)
                {
                    if (child.exists())
                    {
                        stack.push(child);
                    }
                }
            }

            File file;
            while (!stack.isEmpty())
            {
                file = stack.pop();
                Debug.verbose(file.getAbsolutePath(), file.isDirectory()
                                                      ? " *"
                                                      : "");

                if (file.isDirectory())
                {
                    children = file.listFiles();
                    if (children != null)
                    {
                        for (final File child : children)
                        {
                            stack.push(child);
                        }
                    }
                }
                else
                {
                    try
                    {
                        final String path = file.getAbsolutePath();
                        if (path.indexOf('\'') >= 0)
                        {
                            final File f = new File(path.replace('\'', '_'));
                            try
                            {
                                UtilIO.rename(file, f);
                            }
                            catch (final IOException e)
                            {
                                Debug.exception(e);
                            }
                            file = f;
                            Debug.verbose("RENAME : ", file.getAbsolutePath());
                        }
                        FileDatabase.this.addFile(file);
                    }
                    catch (final DatabaseException e)
                    {
                        Debug.exception(e);
                    }
                }

                Utilities.sleep(1);
            }

            FileDatabase.this.threadFillFiles = null;
        }
    }

    /**
     * Data base link
     */
    private Database database;

    /**
     * Thread for automatic fill
     */
    ThreadFillFiles threadFillFiles;

    /**
     * Constructs FileDatabase
     *
     * @param path Relative data base path
     * @throws DatabaseException On data base connection problem
     */
    public FileDatabase(final String path)
            throws DatabaseException
    {
        this.database = new Database(path);

        this.database.createTable(FileDatabase.FILES, FileDatabase.TABLE_FILES);
        this.database.createTable(FileDatabase.WORDS, FileDatabase.TABLE_WORDS);
        this.database.createTable(FileDatabase.ASSOCIATIONS, FileDatabase.TABLE_ASSOCIATIONS);
    }

    /**
     * Add file to the data base
     *
     * @param file File to add
     * @return File ID
     * @throws DatabaseException On adding problem
     */
    public int addFile(final File file) throws DatabaseException
    {
        if (file == null)
        {
            throw new NullPointerException("file mustn't be null");
        }
        if (!file.exists())
        {
            throw new IllegalArgumentException(UtilText.concatenate("file ", file.getAbsolutePath(), " not exists !"));
        }

        String path = file.getAbsolutePath().replace('\'', (char) 128);

        Data data = Data.createLongStringData(path);

        // Get file id in FILES table
        final SelectQuery selectQuery = new SelectQuery(FileDatabase.FILES, FileDatabase.COLUMN_ID);
        selectQuery.setWhere(new ConditionColumnEquals(FileDatabase.COLUMN_PATH.getColumnName(), data));

        QueryResult queryResult = this.database.query(selectQuery);

        if (queryResult.numberOfRows() == 0)
        {
            // File not already exists

            queryResult.destroy();

            // Add the file in FILES table
            this.database.insert(FileDatabase.FILES, new Value(FileDatabase.COLUMN_PATH.getColumnName(), data));

            // Get the given id to the file in FILES table
            queryResult = this.database.query(selectQuery);
        }

        // Read the id
        final int id = queryResult.getData(0, 0).getInt();

        queryResult.destroy();
        path = null;
        data = null;
        queryResult = null;

        return id;
    }

    /**
     * Add word to data base
     *
     * @param word Word to add
     * @return Word ID
     * @throws DatabaseException On adding problem
     */
    public int addWord(String word) throws DatabaseException
    {
        if (word == null)
        {
            throw new NullPointerException("word mustn't be null");
        }
        word = word.trim();
        if (word.length() == 0)
        {
            throw new IllegalArgumentException("word mustn't be empty");
        }

        Data data = new Data(word);

        // Get id of word in WORDS table
        final SelectQuery selectQuery = new SelectQuery(FileDatabase.WORDS, FileDatabase.COLUMN_ID);
        selectQuery.setWhere(new ConditionColumnEquals(FileDatabase.COLUMN_WORD.getColumnName(), data));

        QueryResult queryResult = this.database.query(selectQuery);

        if (queryResult.numberOfRows() == 0)
        {
            // Word not already exists

            queryResult.destroy();

            // Add the word in WORDS table
            this.database.insert(FileDatabase.WORDS, new Value(FileDatabase.COLUMN_WORD.getColumnName(), data));

            // Get the given id to the word in WORDS table
            queryResult = this.database.query(selectQuery);
        }

        // Read the id
        final int id = queryResult.getData(0, 0).getInt();

        queryResult.destroy();
        data = null;
        queryResult = null;

        return id;
    }

    /**
     * Associate file to words
     *
     * @param file  File to associate
     * @param words Words associate to the file
     * @throws DatabaseException On adding problem
     */
    public void associate(final File file, final String... words) throws DatabaseException
    {
        if (words == null)
        {
            throw new NullPointerException("words mustn't be null");
        }

        // Add the file and memory its id
        Data dataFileID = new Data(this.addFile(file));

        // Prepare data for receive word id
        Data dataWordID = new Data(0);

        // Prepare query to select COLUMN_ID in ASSOCIATIONS table
        SelectQuery selectQuery = new SelectQuery(FileDatabase.ASSOCIATIONS, FileDatabase.COLUMN_ID);
        selectQuery.setWhere(
                new BinaryConditionAND(
                        new ConditionColumnEquals(FileDatabase.COLUMN_FILE_ID.getColumnName(), dataFileID),
                        new ConditionColumnEquals(
                                FileDatabase.COLUMN_WORD_ID.getColumnName(), dataWordID)));

        // For each word
        for (final String word : words)
        {
            // Add word and memorize its id
            dataWordID.set(this.addWord(word));

            // the query is automatically update because we use the same instance
            // of dataWordID, so no need to create it again or modify its "where"
            // part, it will be compute with new value

            // If association not already exists
            if (this.database.query(selectQuery).numberOfRows() == 0)
            {
                // Add the association
                this.database.insert(FileDatabase.ASSOCIATIONS,
                                     new Value(FileDatabase.COLUMN_FILE_ID.getColumnName(), dataFileID), new Value(
                                FileDatabase.COLUMN_WORD_ID.getColumnName(), dataWordID));
            }
        }

        selectQuery = null;
        dataFileID = dataWordID = null;
    }

    /**
     * Close data base<br>
     * Use to free memory and be sure all is commit before exit application
     *
     * @throws DatabaseException On closing problem
     */
    public void close() throws DatabaseException
    {
        this.database.closeDatabase();
        this.database = null;
    }

    /**
     * Delete a file
     *
     * @param file File to delete
     * @throws DatabaseException On deletion problem
     */
    public void deleteFile(final File file) throws DatabaseException
    {
        String path = file.getAbsolutePath().replace('\'', (char) 128);

        // Get the id of the file in FILES table
        SelectQuery selectQuery = new SelectQuery(FileDatabase.FILES, FileDatabase.COLUMN_ID);
        selectQuery.setWhere(
                new ConditionColumnEquals(FileDatabase.COLUMN_PATH.getColumnName(), Data.createLongStringData(path)));

        path = null;

        QueryResult queryResult = this.database.query(selectQuery);

        if (queryResult.numberOfRows() > 0)
        {
            // File found

            Data fileID = queryResult.getData(0, 0);

            // Remove all associations with the file
            this.database.delete(FileDatabase.ASSOCIATIONS,
                                 new ConditionColumnEquals(FileDatabase.COLUMN_FILE_ID.getColumnName(), fileID));

            // Select all WORD_ID in ASSOCIATIONS table
            selectQuery = new SelectQuery(FileDatabase.ASSOCIATIONS, FileDatabase.COLUMN_WORD_ID);

            // Delete all words that are not in ASSOCIATIONS table
            this.database.delete(FileDatabase.WORDS, new UnaryConditionNOT(
                    new ConditionIN(FileDatabase.COLUMN_ID.getColumnName(), selectQuery)));

            // Delete the file from FILES table
            this.database.delete(FileDatabase.FILES,
                                 new ConditionColumnEquals(FileDatabase.COLUMN_ID.getColumnName(), fileID));

            fileID = null;
        }

        queryResult.destroy();
        queryResult = null;
        selectQuery = null;

        UtilIO.delete(file);
    }

    /**
     * Delete a word from data base
     *
     * @param word Word to delete
     * @throws DatabaseException On database reading/writing problem or if the word is not in data base
     */
    public void deleteWord(final String word) throws DatabaseException
    {
        // Get the id of the word in WORDS table
        SelectQuery selectQuery = new SelectQuery(FileDatabase.WORDS, FileDatabase.COLUMN_ID);
        selectQuery.setWhere(new ConditionColumnEquals(FileDatabase.COLUMN_WORD.getColumnName(), new Data(word)));

        QueryResult queryResult = this.database.query(selectQuery);

        // If word not exists, throw an Exception
        if (queryResult.numberOfRows() == 0)
        {
            selectQuery = null;
            queryResult.destroy();
            queryResult = null;

            throw new DatabaseException(UtilText.concatenate("The word '", word, "' is not in data base"));
        }

        // Read the result
        Data wordID = queryResult.getData(0, 0);
        queryResult.destroy();

        // Remove all associations with the word
        this.database.delete(FileDatabase.ASSOCIATIONS,
                             new ConditionColumnEquals(FileDatabase.COLUMN_WORD_ID.getColumnName(), wordID));

        // Delete the word in WORDS table
        this.database.delete(FileDatabase.WORDS,
                             new ConditionColumnEquals(FileDatabase.COLUMN_ID.getColumnName(), wordID));

        selectQuery = null;
        queryResult = null;
        wordID = null;
    }

    /**
     * All files store in data base
     *
     * @return All files store in data base
     * @throws DatabaseException On getting problem
     */
    public File[] getAllFiles() throws DatabaseException
    {
        return this.getFilterFiles();
    }

    /**
     * All associate words for a file
     *
     * @param file File test
     * @return All associate words for a file
     * @throws DatabaseException On reading data base problem
     */
    public String[] getAssociateWords(final File file) throws DatabaseException
    {
        if (file == null)
        {
            throw new NullPointerException("file mustn't be null");
        }

        final SelectQuery selectQueryFileID = new SelectQuery(FileDatabase.FILES, FileDatabase.COLUMN_ID);
        selectQueryFileID.setWhere(new ConditionColumnEquals(FileDatabase.COLUMN_PATH.getColumnName(),
                                                             Data.createLongStringData(file.getAbsolutePath().replace(
                                                                     '\'', (char) 128))));

        final SelectQuery selectQueryWordsAssociate = new SelectQuery(FileDatabase.ASSOCIATIONS,
                                                                      FileDatabase.COLUMN_WORD_ID);
        selectQueryWordsAssociate.setWhere(
                new ConditionIN(FileDatabase.COLUMN_FILE_ID.getColumnName(), selectQueryFileID));

        final SelectQuery selectQueryWords = new SelectQuery(FileDatabase.WORDS, FileDatabase.COLUMN_WORD);
        selectQueryWords.setWhere(new ConditionIN(FileDatabase.COLUMN_ID.getColumnName(), selectQueryWordsAssociate));

        final QueryResult queryResult = this.database.query(selectQueryWords);
        final int         row         = queryResult.numberOfRows();
        final String[]    words       = new String[row];

        for (int i = 0; i < row; i++)
        {
            words[i] = queryResult.getData(0, i).getString();
        }

        return words;
    }

    /**
     * Use filters to select a set of files.<br>
     * Each filter is a word.<br>
     * The filters are "ANDed".<br>
     * If filter start with '!' character, that denote we want files are not associate with a specific word else filter denote a
     * word that file must associate with<br>
     * Examples, if you have in your database :<br>
     * <code>file1 <-> ("apple", "fruit", "pie")<br>file2 <-> ("apple","computer")</code> <br>
     * Where <code><-></code> denotes association<br>
     * If you request :
     * <ul>
     * <li>"apple" : result is file1 and file2</li>
     * <li>"!computer" : result is only file1 (file2 have computer has key word)</li>
     * </ul>
     *
     * @param filters Filters to use
     * @return Files match
     * @throws DatabaseException On reading data base problem
     */
    public File[] getFilterFiles(final String... filters) throws DatabaseException
    {
        // Prepare the query to select files in FILES table
        SelectQuery selectQueryFilePath = new SelectQuery(FileDatabase.FILES, FileDatabase.COLUMN_PATH);

        // If filters are specified
        if ((filters != null) && (filters.length > 0))
        {
            // Collected ids
            ArrayInt ids  = new ArrayInt();
            int      mult = 1;

            // Prepare query to select id of word in WORDS table
            SelectQuery selectQuery = new SelectQuery(FileDatabase.WORDS, FileDatabase.COLUMN_ID);
            Data        dataWord    = new Data("");
            selectQuery.setWhere(new ConditionColumnEquals(FileDatabase.COLUMN_WORD.getColumnName(), dataWord));

            // Prepare query to select FILE_ID in ASSOCIATIONS table where WORD_ID
            // is in select id of word in previous query
            SelectQuery selectQueryWordID = new SelectQuery(FileDatabase.ASSOCIATIONS, FileDatabase.COLUMN_FILE_ID);
            selectQueryWordID.setWhere(new ConditionIN(FileDatabase.COLUMN_WORD_ID.getColumnName(), selectQuery));

            Condition   where       = null;
            QueryResult queryResult = null;
            int         row;

            // For each filter
            for (String filter : filters)
            {
                // Determine if it is a negative or positive filter
                mult = 1;
                if (filter.charAt(0) == '!')
                {
                    mult = -1;
                    filter = filter.substring(1);
                }

                // Update and launch the query
                dataWord.set(filter);
                queryResult = this.database.query(selectQueryWordID);

                // If the word not found, then we are sure the result is empty
                row = queryResult.numberOfRows();
                if (row == 0)
                {
                    // If negative,not exists, so all files match
                    if (mult < 0)
                    {
                        // Clear for next filter
                        queryResult.destroy();
                        ids.clear();

                        continue;
                    }

                    return new File[0];
                }

                // Collect files id
                for (int y = 0; y < row; y++)
                {
                    ids.add(mult * queryResult.getData(0, y).getInt());
                }

                // Update where condition
                if (where == null)
                {
                    where = new ConditionFileAssociation(ids.toArray());
                }
                else
                {
                    where = new BinaryConditionAND(where, new ConditionFileAssociation(ids.toArray()));
                }

                // Clear for next filter
                queryResult.destroy();
                ids.clear();
            }

            // Set the where condition
            selectQueryFilePath.setWhere(where);

            queryResult = null;
            selectQueryWordID = null;
            selectQuery = null;
            dataWord = null;
            ids = null;
            where = null;
        }

        // Launch query to select files
        QueryResult queryResult = this.database.query(selectQueryFilePath);

        // Get selected files
        final int    count = queryResult.numberOfRows();
        final File[] files = new File[count];

        for (int i = 0; i < count; i++)
        {
            files[i] = new File(queryResult.getData(0, i).getLongString().replace((char) 128, '\''));
        }

        queryResult.destroy();
        queryResult = null;
        selectQueryFilePath = null;

        return files;
    }

    /**
     * All file witch no t yet association
     *
     * @return All file witch no t yet association
     * @throws DatabaseException On reading data base problem
     */
    public File[] getNotAssociateFiles() throws DatabaseException
    {
        SelectQuery selectQueryAssociatesFiles = new SelectQuery(FileDatabase.ASSOCIATIONS,
                                                                 FileDatabase.COLUMN_FILE_ID);

        SelectQuery selectQueryNotAssociates = new SelectQuery(FileDatabase.FILES, FileDatabase.COLUMN_PATH);
        selectQueryNotAssociates.setWhere(
                new UnaryConditionNOT(
                        new ConditionIN(FileDatabase.COLUMN_ID.getColumnName(), selectQueryAssociatesFiles)));

        QueryResult queryResult = this.database.query(selectQueryNotAssociates);

        final int    row   = queryResult.numberOfRows();
        final File[] files = new File[row];

        for (int y = 0; y < row; y++)
        {
            files[y] = new File(queryResult.getData(0, y).getLongString().replace((char) 128, '\''));
        }

        queryResult.destroy();
        queryResult = null;
        selectQueryNotAssociates = null;
        selectQueryAssociatesFiles = null;

        return files;
    }

    /**
     * Indicates if the thread for auto filling still running
     *
     * @return {@code true} if the thread for auto filling still running
     */
    public boolean isAutoFillFiles()
    {
        return this.threadFillFiles != null;
    }

    /**
     * Launch auto filling (can be long)
     */
    public void launchAutoFillFiles()
    {
        if (this.threadFillFiles == null)
        {
            this.threadFillFiles = new ThreadFillFiles();
            ThreadManager.parallel(this.threadFillFiles);
        }
    }

    /**
     * Rename/displace a file
     *
     * @param oldFile File to rename
     * @param newFile New location
     * @throws DatabaseException On data base reading/writing problem or if old file is not in data base or new file is in data base or the
     *                           physically rename fails
     */
    public void renameFile(final File oldFile, final File newFile) throws DatabaseException
    {
        if (oldFile == null)
        {
            throw new NullPointerException("oldFile mustn't be null");
        }
        if (newFile == null)
        {
            throw new NullPointerException("newFile mustn't be null");
        }

        // Name of new file
        Data newName = Data.createLongStringData(newFile.getAbsolutePath().replace('\'', (char) 128));

        // Get id of new file in FILES table to see if already exists
        SelectQuery selectQuery = new SelectQuery(FileDatabase.FILES, FileDatabase.COLUMN_ID);
        selectQuery.setWhere(new ConditionColumnEquals(FileDatabase.COLUMN_PATH.getColumnName(), newName));

        QueryResult queryResult = this.database.query(selectQuery);

        // If new file already exists throw an error
        if (queryResult.numberOfRows() > 0)
        {
            newName = null;
            selectQuery = null;
            queryResult.destroy();
            queryResult = null;

            throw new DatabaseException(
                    UtilText.concatenate("The new file '", newFile.getAbsolutePath(), "' already exists"));
        }
        queryResult.destroy();

        // Name of old file
        Data oldName = Data.createLongStringData(oldFile.getAbsolutePath().replace('\'', (char) 128));

        // Get id of the old file in FILES table
        selectQuery = new SelectQuery(FileDatabase.FILES, FileDatabase.COLUMN_ID);
        selectQuery.setWhere(new ConditionColumnEquals(FileDatabase.COLUMN_PATH.getColumnName(), oldName));

        queryResult = this.database.query(selectQuery);

        // If old file not in the data base, throw an exception
        if (queryResult.numberOfRows() == 0)
        {
            newName = null;
            oldName = null;
            selectQuery = null;
            queryResult.destroy();
            queryResult = null;

            throw new DatabaseException(
                    UtilText.concatenate("The old file '", oldFile.getAbsolutePath(), "' not exists"));
        }

        // Extract id from result
        Data oldID = queryResult.getData(0, 0);
        queryResult.destroy();

        // If old file exists in hard drive, rename it physically
        if (oldFile.exists())
        {
            try
            {
                UtilIO.rename(oldFile, newFile);
            }
            catch (final IOException e)
            {
                throw new DatabaseException("Can't rename file on hard drive", e);
            }
        }

        // Add new file
        final Data newID = new Data(this.addFile(newFile));

        // Modify associations in ASSOCIATIONS table
        UpdateQuery updateQuery = new UpdateQuery(FileDatabase.ASSOCIATIONS,
                                                  new Value(FileDatabase.COLUMN_FILE_ID.getColumnName(), newID));
        updateQuery.setWhere(new ConditionColumnEquals(FileDatabase.COLUMN_FILE_ID.getColumnName(), oldID));
        this.database.update(updateQuery);

        // Delete old file from FILES table
        this.database.delete(FileDatabase.FILES,
                             new ConditionColumnEquals(FileDatabase.COLUMN_ID.getColumnName(), oldID));

        newName = null;
        oldName = null;
        selectQuery = null;
        queryResult = null;
        oldID = null;
        updateQuery = null;
    }

    /**
     * Rename a word
     *
     * @param oldWord Old word
     * @param newWord New word
     * @throws DatabaseException On reading/writing data base problem or if old word not in data base
     */
    public void renameWord(final String oldWord, final String newWord) throws DatabaseException
    {
        if (oldWord == null)
        {
            throw new NullPointerException("oldWord mustn't be null");
        }
        if (newWord == null)
        {
            throw new NullPointerException("newWord mustn't be null");
        }

        // Old word data version
        Data oldData = new Data(oldWord);

        // Get the id of old word
        SelectQuery selectQuery = new SelectQuery(FileDatabase.WORDS, FileDatabase.COLUMN_ID);
        selectQuery.setWhere(new ConditionColumnEquals(FileDatabase.COLUMN_WORD.getColumnName(), oldData));

        QueryResult queryResult = this.database.query(selectQuery);

        // If old word not exists, throw an exception
        if (queryResult.numberOfRows() == 0)
        {
            queryResult.destroy();
            queryResult = null;
            selectQuery = null;
            oldData = null;

            throw new DatabaseException(UtilText.concatenate("The word '", oldWord, "' not exists in data base"));
        }

        // Read the id
        Data oldID = queryResult.getData(0, 0);
        queryResult.destroy();

        // Add new word
        Data newID = new Data(this.addWord(newWord));

        // Change associations
        UpdateQuery updateQuery = new UpdateQuery(FileDatabase.ASSOCIATIONS,
                                                  new Value(FileDatabase.COLUMN_WORD_ID.getColumnName(), newID));
        updateQuery.setWhere(new ConditionColumnEquals(FileDatabase.COLUMN_WORD_ID.getColumnName(), oldID));
        this.database.update(updateQuery);

        // Delete old word
        this.database.delete(FileDatabase.WORDS,
                             new ConditionColumnEquals(FileDatabase.COLUMN_ID.getColumnName(), oldID));

        queryResult = null;
        selectQuery = null;
        oldData = null;
        oldID = null;
        newID = null;
        updateQuery = null;
    }
}
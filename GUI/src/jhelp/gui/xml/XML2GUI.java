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
package jhelp.gui.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Hashtable;
import jhelp.util.debug.Debug;
import jhelp.util.io.UtilIO;
import jhelp.util.text.UtilText;
import jhelp.xml.ExceptionParseXML;
import jhelp.xml.ExceptionXML;
import jhelp.xml.ParseXMLlistener;
import jhelp.xml.ParserXML;

public class XML2GUI
        implements ParseXMLlistener, Constants
{
    private static final ParserXML PARSER_XML = new ParserXML();
    private static final XML2GUI   XML_GUI    = new XML2GUI();

    public static void parse(final InputStream inputStream, final File directory) throws ExceptionParseXML
    {
        XML2GUI.XML_GUI.directory = directory;

        XML2GUI.PARSER_XML.parse(XML2GUI.XML_GUI, inputStream);
    }

    private BufferedWriter bufferedWriter;
    private File           currentFile;
    private File           directory;

    private XML2GUI()
    {
    }

    private void writeLine(final String... strings) throws ExceptionXML
    {
        try
        {
            if (this.bufferedWriter == null)
            {
                this.bufferedWriter = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(this.currentFile)));
            }

            for (final String string : strings)
            {
                this.bufferedWriter.write(string);
            }

            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
        }
        catch (final Exception exception)
        {
            throw new ExceptionXML(
                    UtilText.concatenate("Issue while writing ", strings, " in ", this.currentFile.getAbsolutePath()),
                    exception);
        }
    }

    @Override
    public void commentFind(final String comment)
    {
    }

    @Override
    public void endMarkup(final String markupName) throws ExceptionXML
    {
        if (Constants.MARKUP_FRAME.equals(markupName))
        {
            this.writeLine("}");

            if (this.bufferedWriter != null)
            {
                try
                {
                    this.bufferedWriter.flush();
                }
                catch (final Exception ignored)
                {
                }

                try
                {
                    this.bufferedWriter.close();
                }
                catch (final Exception ignored)
                {
                }
            }

            this.currentFile = null;
            this.bufferedWriter = null;
            return;
        }

        Debug.todo("Implements markup " + markupName);
    }

    @Override
    public void endParse()
    {
    }

    @Override
    public void exceptionForceEndParse(final ExceptionParseXML exceptionParseXML)
    {
    }

    @Override
    public void startMarkup(final String markupName, final Hashtable<String, String> parameters) throws ExceptionXML
    {
        if (Constants.MARKUP_FRAME.equals(markupName))
        {
            if (this.currentFile != null)
            {
                throw new ExceptionXML("Only one " + Constants.MARKUP_FRAME + " per file !");
            }

            String name   = ParserXML.obtainParameter(markupName, parameters, Constants.PARAMETER_NAME, true);
            String title  = ParserXML.obtainParameter(markupName, parameters, Constants.PARAMETER_TITLE, false);
            String layout = ParserXML.obtainParameter(markupName, parameters, Constants.PARAMETER_LAYOUT, false);

            if (title == null)
            {
                title = name;
            }

            if (layout == null)
            {
                layout = "JHelpBorderLayout";
            }

            this.currentFile = new File(this.directory, name.replace('.', File.separatorChar) + ".java");

            if (!UtilIO.createFile(this.currentFile))
            {
                throw new ExceptionXML("Can't create the file : " + this.currentFile.getAbsolutePath());
            }

            final int index = name.lastIndexOf('.');

            this.writeLine("/**");
            this.writeLine(" * Auto generate from a XML description <br>");
            this.writeLine(" * To be modified for add/complete behaviors");
            this.writeLine(" */");

            if (index >= 0)
            {
                this.writeLine("package ", name.substring(0, index), ";");
                this.writeLine();

                name = name.substring(index + 1);
            }

            this.writeLine("import jhelp.gui.*;");
            this.writeLine();
            this.writeLine("/**");
            this.writeLine(" * Frame ", name, " with title ", title, "<br>");
            this.writeLine(" * Example of minimum launcher :<br>");
            this.writeLine(" * <table border=1>");
            this.writeLine(" * <tr><td><pre>");
            this.writeLine(" * public static void main(String[] args)");
            this.writeLine(" * {");
            this.writeLine(" *    ", name, " ", String.valueOf((char) ((name.charAt(0) - 'A') + 'a')),
                           name.substring(1), "= new ", name, "();");
            this.writeLine(" *    ", String.valueOf((char) ((name.charAt(0) - 'A') + 'a')), name.substring(1),
                           ".visible(true);");
            this.writeLine(" * }");
            this.writeLine(" * </pre></td></tr>");
            this.writeLine(" * </table>");
            this.writeLine(" */");
            this.writeLine("public class ", name, " extends JHelpFrame2D");
            this.writeLine("{");
            this.writeLine("   /**");
            this.writeLine("    * Create the frame");
            this.writeLine("    */");
            this.writeLine("   public ", name, "()");
            this.writeLine("   {");
            this.writeLine("      super(\"", title, "\", new ", layout, "());");
            this.writeLine("   }");

            return;
        }

        if (this.currentFile == null)
        {
            throw new ExceptionXML("Must start with " + Constants.MARKUP_FRAME + " markup");
        }

        Debug.todo("Implements markup " + markupName);
    }

    @Override
    public void startParse()
    {
        UtilIO.createDirectory(this.directory);
    }

    @Override
    public void textFind(final String text)
    {
    }
}
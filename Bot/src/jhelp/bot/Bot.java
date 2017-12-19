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

package jhelp.bot;

import java.util.Objects;
import jhelp.ontology.Graph;
import jhelp.util.io.UtilIO;
import jhelp.util.text.UtilText;

public class Bot
{
    private final BotFile botFile;
    private final Graph   graph;

    public Bot(String botName, BotFile botFile)
    {
        Objects.requireNonNull(botName, "botName MUST NOT be null!");
        Objects.requireNonNull(botFile, "botFile MUST NOT be null!");
        this.graph = Graph.obtainGraph(
                UtilIO.obtainExternalFile("JHelp/bot/" + UtilText.removeWhiteCharacters(botName) + "/graph.graph"));
        this.botFile = botFile;
    }
}

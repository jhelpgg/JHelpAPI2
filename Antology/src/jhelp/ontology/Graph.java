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

package jhelp.ontology;

import com.sun.istack.internal.NotNull;
import java.io.File;
import jhelp.util.debug.Debug;
import jhelp.util.io.ByteArray;
import jhelp.util.list.ArrayObject;
import jhelp.util.list.SortedArray;
import jhelp.util.reflection.Reflection;
import jhelp.util.thread.ConsumerTask;

/**
 * An ontology graph
 */
public final class Graph
{
    /**
     * Obtain graph from file.<br>
     * If file not exists, it tries to create the file.<br>
     * If file is valid, the graph will be automatically saved in it at each change
     *
     * @param file File where read/save the graph
     * @return Graph from the file or empty graph if read failed or file just created
     */
    public static Graph obtainGraph(File file)
    {
        return AutomaticSaveGraph.obtainGraph(file);
    }

    /**
     * Parse byte array with Graph data inside
     *
     * @param byteArray Byte array to parse
     * @return Parsed graph
     * @throws Exception If byte array data not a valid graph description
     */
    public static Graph parse(ByteArray byteArray) throws Exception
    {
        final Graph graph = new Graph();
        graph.version = byteArray.readInteger();
        int size = byteArray.readInteger();

        for (int i = 0; i < size; i++)
        {
            graph.nodes.add(Node.parse(byteArray));
        }

        size = byteArray.readInteger();

        for (int i = 0; i < size; i++)
        {
            graph.rules.add(Rule.parse(byteArray));
        }

        return graph;
    }

    /**
     * Listeners of graph events
     */
    private final ArrayObject<GraphListener> graphListeners;
    /**
     * Graph nodes
     */
    private final SortedArray<Node>          nodes;
    /**
     * Graph rules
     */
    private final ArrayObject<Rule>          rules;
    /**
     * Graph version
     */
    private       int                        version;

    /**
     * Creates an empty graph
     */
    public Graph()
    {
        this.version = 0;
        this.nodes = new SortedArray<>(Node.class, true);
        this.graphListeners = new ArrayObject<>();
        this.rules = new ArrayObject<>();
    }

    /**
     * Apply a rule
     *
     * @param rule Rule to apply
     */
    private void applyRule(@NotNull final Rule rule)
    {
        final SortedArray<Triplet> firsts = this.search(rule.firstSubject(), rule.firstPredicate(),
                                                        rule.firstInformation());

        if (firsts.empty())
        {
            return;
        }

        final SortedArray<Triplet> seconds = this.search(rule.secondSubject(), rule.secondPredicate(),
                                                         rule.secondInformation());

        if (seconds.empty())
        {
            return;
        }

        firsts.consume(first -> seconds.consume(second ->
                                                {
                                                    final Triplet triplet = rule.apply(first.subject(),
                                                                                       first.predicate(),
                                                                                       first.information(),
                                                                                       second.subject(),
                                                                                       second.predicate(),
                                                                                       second.information());

                                                    if (triplet != null)
                                                    {
                                                        this.add(triplet.subject(), triplet.predicate(),
                                                                 triplet.information());
                                                    }
                                                }));
    }

    /**
     * Apply all rules
     */
    private void applyRules()
    {
        this.rules.consume(this::applyRule);
    }

    /**
     * Alert listeners that graph changed
     */
    private void fireChanged()
    {
        synchronized (this.graphListeners)
        {
            this.graphListeners.forEach((ConsumerTask<GraphListener>) listener -> listener.graphChanged(this));
        }
    }

    /**
     * Remove a specific triplet
     *
     * @param triplet Triplet to remove
     */
    private void remove(Triplet triplet)
    {
        Node subject     = triplet.subject();
        Node predicate   = triplet.predicate();
        Node information = triplet.information();

        predicate.removeChild(information);

        if (predicate.numberChildren() == 0)
        {
            subject.removeChild(predicate);

            if (subject.numberChildren() == 0)
            {
                this.nodes.remove(subject);
            }
        }
    }

    /**
     * Add a triplet.<br>
     * Can't add {@link Node#WILDCARD}
     *
     * @param subject     Subject
     * @param predicate   Predicate
     * @param information Information
     */
    public void add(Node subject, Node predicate, Node information)
    {
        if (subject == Node.WILDCARD || predicate == Node.WILDCARD || information == Node.WILDCARD)
        {
            throw new IllegalArgumentException("Can't add the wildcard node");
        }

        if (subject.equals(information))
        {
            return;
        }

        subject = subject.duplicate();
        predicate = predicate.duplicate();
        information = information.duplicate();

        int index = this.nodes.indexOf(subject);

        if (index < 0)
        {
            predicate.addChild(information);
            subject.addChild(predicate);
            this.nodes.add(subject);
            this.applyRules();
            this.fireChanged();
            return;
        }

        subject = this.nodes.get(index);
        index = subject.indexOf(predicate);

        if (index < 0)
        {
            predicate.addChild(information);
            subject.addChild(predicate);
            this.applyRules();
            this.fireChanged();
            return;
        }

        predicate = subject.child(index);

        index = predicate.indexOf(information);

        if (index < 0)
        {
            predicate.addChild(information);
            this.applyRules();
            this.fireChanged();
        }
    }

    /**
     * Add a rule and apply it.<br>
     * The rule will become immutable after the call of this method,
     * so if have to add constraints, do it before call this method.
     *
     * @param rule Rule to add
     */
    public void addAndApplyRule(@NotNull Rule rule)
    {
        rule.makeImmutable();
        boolean add = false;

        synchronized (this.rules)
        {
            if (!this.rules.contains(rule))
            {
                this.rules.add(rule);
                add = true;
            }
        }

        if (add)
        {
            this.applyRule(rule);
            this.fireChanged();
        }
    }

    /**
     * Add a two ways triplet<br>
     * Does the same as:
     * <code lang="java">
     * graph.add(subject1, relation, subject2);
     * graph.add(subject2, relation, subject1);
     * </code>
     *
     * @param subject1 First subject
     * @param relation Relation between subjects
     * @param subject2 Second subject
     */
    public void addTwoWay(Node subject1, Node relation, Node subject2)
    {
        this.add(subject1, relation, subject2);
        this.add(subject2, relation, subject1);
    }

    /**
     * Clear the graph.<br>
     * All nodes and rules are removed
     */
    public void clear()
    {
        this.nodes.clear();
        this.rules.clear();
        this.fireChanged();
    }

    /**
     * Compute paths for go from a subject to an information
     *
     * @param subject     Source subject
     * @param information Destination information
     * @return List of paths found
     */
    public ArrayObject<Path> computePath(Node subject, Node information)
    {
        if (subject == Node.WILDCARD || information == Node.WILDCARD)
        {
            throw new IllegalArgumentException("Can't compute path with the wildcard node");
        }

        final ArrayObject<Path> paths = new ArrayObject<>();

        if (!subject.equals(information))
        {
            ArrayObject<Path> pool = new ArrayObject<>();
            Node              lastInformation;
            Path              path, copy;

            for (Triplet triplet : this.search(subject, Node.WILDCARD, Node.WILDCARD))
            {
                path = new Path();
                path.add(triplet);
                pool.add(path);
            }

            while (!pool.isEmpty())
            {
                for (int index = pool.size() - 1; index >= 0; index--)
                {
                    path = pool.get(index);
                    pool.remove(index);
                    lastInformation = path.lastInformation();

                    if (lastInformation.equals(information))
                    {
                        paths.add(path);
                    }
                    else
                    {
                        for (Triplet triplet : this.search(lastInformation, Node.WILDCARD, Node.WILDCARD))
                        {
                            copy = path.copy();
                            copy.add(triplet);
                            pool.add(copy);
                        }
                    }
                }
            }
        }

        return paths;
    }

    /**
     * Indicated if graph is empty
     *
     * @return {@code true} if graph is empty
     */
    public boolean empty()
    {
        return this.nodes.empty();
    }

    /**
     * Register listener to graph modifications
     *
     * @param graphListener Listener to register
     */
    public void register(GraphListener graphListener)
    {
        if (graphListener == null)
        {
            return;
        }

        synchronized (this.graphListeners)
        {
            if (!this.graphListeners.contains(graphListener))
            {
                this.graphListeners.add(graphListener);
            }
        }
    }

    /**
     * Remove all matching triplets.<br>
     * Can use {@link Node#WILDCARD} to means all node
     *
     * @param subject     Subject to remove or {@link Node#WILDCARD} to remove all subjects
     * @param predicate   Predicate to remove or {@link Node#WILDCARD} to remove all predicates
     * @param information Information to remove or {@link Node#WILDCARD} to remove all information
     */
    public void remove(Node subject, Node predicate, Node information)
    {
        this.search(subject, predicate, information).consume(this::remove);
        this.fireChanged();
    }

    /**
     * Remove all node subject, predicate or information that match to given node.<br>
     * Does the same as:
     * <code lang="java">
     * graph.remove(node, Node.WILDCARD, Node.WILDCARD);
     * graph.remove(Node.WILDCARD, node, Node.WILDCARD);
     * graph.remove(Node.WILDCARD, Node.WILDCARD, node);
     * </code>
     *
     * @param node
     */
    public void removeAll(Node node)
    {
        this.remove(node, Node.WILDCARD, Node.WILDCARD);
        this.remove(Node.WILDCARD, node, Node.WILDCARD);
        this.remove(Node.WILDCARD, Node.WILDCARD, node);
    }

    /**
     * Remove a rule
     *
     * @param rule Rule to remove
     */
    public void removeRule(Rule rule)
    {
        synchronized (this.rules)
        {
            if (this.rules.remove(rule))
            {
                this.fireChanged();
            }
        }
    }

    public <R> R query(Query query, Class<R> resultClass)
    {
        final R result = (R) Reflection.newInstance(resultClass);
        // TODO Do the query
        Debug.todo("Do the query");
        return result;
    }

    /**
     * Serialize the graph in byte array
     *
     * @param byteArray Byte array where serialize
     */
    public void serialize(ByteArray byteArray)
    {
        byteArray.writeInteger(this.version);
        byteArray.writeInteger(this.nodes.size());
        this.nodes.consume(node -> node.serialize(byteArray));
        byteArray.writeInteger(this.rules.size());
        this.rules.consume(rule -> rule.serialize(byteArray));
    }

    /**
     * Unregister listener of graph modifications.
     *
     * @param graphListener Listener to unregister
     */
    public void unregister(GraphListener graphListener)
    {
        synchronized (this.graphListeners)
        {
            this.graphListeners.remove(graphListener);
        }
    }

    /**
     * Graph version
     *
     * @return Graph version
     */
    public int version()
    {
        return this.version;
    }

    /**
     * Change graph version
     *
     * @param version New version. If lower or equals to current one, nothing happen
     */
    public void version(int version)
    {
        if (version <= this.version)
        {
            return;
        }

        this.version = version;
        this.fireChanged();
    }

    /**
     * Visit the graph to collect informations
     *
     * @param graphVisitor Graph visitor
     */
    public void visit(@NotNull GraphVisitor graphVisitor)
    {
        this.nodes.consume(node ->
                           {
                               graphVisitor.enterNode(node);
                               node.visit(graphVisitor);
                               graphVisitor.exitNode(node);
                           });
    }

    /**
     * Search all triplet that match given triplet.<br>
     * Can use {@link Node#WILDCARD} to mean all node of this position.
     *
     * @param subject     Subject to search or {@link Node#WILDCARD} to search all subjects
     * @param predicate   Predicate to search or {@link Node#WILDCARD} to search all predicates
     * @param information Information to search or {@link Node#WILDCARD} to search all information
     * @return List of triplet founds
     */
    public SortedArray<Triplet> search(Node subject, Node predicate, Node information)
    {
        final SortedArray<Triplet> triplets        = new SortedArray<>(Triplet.class, true);
        final NodeTest             subjectTest     = new NodeTest(subject);
        final NodeTest             predicateTest   = new NodeTest(predicate);
        final NodeTest             informationTest = new NodeTest(information);
        this.nodes.seekElements(subjectTest)
                  .consume(subject1 -> subject1.search(predicateTest)
                                               .consume(predicate1 ->
                                                                predicate1.search(informationTest)
                                                                          .consume(information1 ->
                                                                                           triplets.add(
                                                                                                   new Triplet(
                                                                                                           subject1,
                                                                                                           predicate1,
                                                                                                           information1)))));
        return triplets;
    }
}

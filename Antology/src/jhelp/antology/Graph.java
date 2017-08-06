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

package jhelp.antology;

import com.sun.istack.internal.NotNull;
import java.io.File;
import jhelp.util.io.ByteArray;
import jhelp.util.list.ArrayObject;
import jhelp.util.list.SortedArray;
import jhelp.util.thread.ConsumerTask;

/**
 * Created by jhelp on 22/07/17.
 */
public final class Graph
{
    public static Graph obtainGraph(File file)
    {
        return AutomaticSaveGraph.obtainGraph(file);
    }

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
    private final ArrayObject<GraphListener> graphListeners;
    private final SortedArray<Node>          nodes;
    private final ArrayObject<Rule>          rules;
    private       int                        version;

    public Graph()
    {
        this.version = 0;
        this.nodes = new SortedArray<>(Node.class, true);
        this.graphListeners = new ArrayObject<>();
        this.rules = new ArrayObject<>();
    }

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

    private void applyRules()
    {
        this.rules.consume(this::applyRule);
    }

    private void fireChanged()
    {
        synchronized (this.graphListeners)
        {
            this.graphListeners.forEach((ConsumerTask<GraphListener>) listener -> listener.graphChanged(this));
        }
    }

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

    public void addTwoWay(Node subject1, Node relation, Node subject2)
    {
        this.add(subject1, relation, subject2);
        this.add(subject2, relation, subject1);
    }

    public void clear()
    {
        this.nodes.clear();
        this.rules.clear();
        this.fireChanged();
    }

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

    public boolean empty()
    {
        return this.nodes.empty();
    }

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

    public void remove(Node subject, Node predicate, Node information)
    {
        this.search(subject, predicate, information).consume(this::remove);
        this.fireChanged();
    }

    public void removeAll(Node node)
    {
        this.remove(node, Node.WILDCARD, Node.WILDCARD);
        this.remove(Node.WILDCARD, node, Node.WILDCARD);
        this.remove(Node.WILDCARD, Node.WILDCARD, node);
    }

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

    public SortedArray<Triplet> search(Node subject, Node predicate, Node information)
    {
        final SortedArray<Triplet> triplets        = new SortedArray<>(Triplet.class, true);
        final NodeTest             subjectTest     = new NodeTest(subject);
        final NodeTest             predicateTest   = new NodeTest(predicate);
        final NodeTest             informationTest = new NodeTest(information);
        this.nodes.seekElements(subjectTest)
                  .consume(subject1 ->
                                   subject1.search(predicateTest)
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

    public void serialize(ByteArray byteArray)
    {
        byteArray.writeInteger(this.version);
        byteArray.writeInteger(this.nodes.size());
        this.nodes.consume(node -> node.serialize(byteArray));
        byteArray.writeInteger(this.rules.size());
        this.rules.consume(rule -> rule.serialize(byteArray));
    }

    public void unregister(GraphListener graphListener)
    {
        synchronized (this.graphListeners)
        {
            this.graphListeners.remove(graphListener);
        }
    }

    public int version()
    {
        return this.version;
    }

    public void version(int version)
    {
        if (version <= this.version)
        {
            return;
        }

        this.version = version;
        this.fireChanged();
    }

    public void visit(@NotNull GraphVisitor graphVisitor)
    {
        this.nodes.consume(node ->
                           {
                               graphVisitor.enterNode(node);
                               node.visit(graphVisitor);
                               graphVisitor.exitNode(node);
                           });
    }
}

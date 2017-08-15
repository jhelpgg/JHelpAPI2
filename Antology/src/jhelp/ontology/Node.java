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
import java.util.Objects;
import jhelp.util.io.Binarizable;
import jhelp.util.io.ByteArray;
import jhelp.util.list.SortedArray;
import jhelp.util.math.Math2;

/**
 * Node graph
 */
public final class Node implements Comparable<Node>
{
    /**
     * Special node that indicates all nodes.<br>
     * Can be used in {@link Graph#remove(Node, Node, Node)}, {@link Graph#removeAll(Node)},
     * {@link Graph#search(Node, Node, Node)}, {@link Rule#Rule(Node, Node, Node, Node, Node, Node, RuleResult, RuleResult, RuleResult)}
     */
    public static final Node WILDCARD = new Node();

    /**
     * Create a node that contains an int
     *
     * @param value Node value
     * @return Created node
     */
    public static Node createNode(int value)
    {
        return new Node(NodeType.INT, value);
    }

    /**
     * Create a node that contains a double
     *
     * @param value Node value
     * @return Created node
     */
    public static Node createNode(double value)
    {
        return new Node(NodeType.DOUBLE, value);
    }

    /**
     * Create a node that contains a String
     *
     * @param value Node value
     * @return Created node
     */
    public static Node createNode(String value)
    {
        Objects.requireNonNull(value, "value MUST NOT be null!");
        return new Node(NodeType.STRING, value);
    }

    /**
     * Create a node that contains a Binarizable
     *
     * @param value Node value
     * @return Created node
     */
    public static <B extends Binarizable> Node createNode(B value)
    {
        Objects.requireNonNull(value, "value MUST NOT be null!");
        return new Node(NodeType.BINARIZABLE, value);
    }

    /**
     * Create node that contains a triplet
     *
     * @param triplet Triplet content
     * @return Created node
     * @throws IllegalArgumentException if triplet have at least one node is {@link Node#WILDCARD}
     */
    public static Node createNode(Triplet triplet)
    {
        Objects.requireNonNull(triplet, "triplet MUST NOT be null!");

        if (triplet.subject() == Node.WILDCARD ||
            triplet.predicate() == Node.WILDCARD ||
            triplet.information() == Node.WILDCARD)
        {
            throw new IllegalArgumentException("Triplet contains at least one node is the wildcard");
        }

        return new Node(NodeType.TRIPLET, triplet);
    }

    /**
     * Parse byte array to create a node
     *
     * @param byteArray Byte array to parse
     * @return Parsed Node
     * @throws Exception If byte array not contains valid Node data
     */
    public static Node parse(ByteArray byteArray) throws Exception
    {
        NodeType nodeType = byteArray.readEnum(NodeType.class);
        Object   value;

        switch (nodeType)
        {
            case BINARIZABLE:
                value = byteArray.readBinarizableNamed();
                break;
            case DOUBLE:
                value = byteArray.readDouble();
                break;
            case INT:
                value = byteArray.readInteger();
                break;
            case STRING:
                value = byteArray.readString();
                break;
            case TRIPLET:
                value = Triplet.parse(byteArray);
                break;
            case WILDCARD:
                return Node.WILDCARD;
            default:
                throw new IllegalArgumentException("Can't parse : " + nodeType);
        }

        final Node node = new Node(nodeType, value);
        final int  size = byteArray.readInteger();

        for (int i = 0; i < size; i++)
        {
            node.addChild(Node.parse(byteArray));
        }

        return node;
    }

    /**
     * Node children
     */
    private final SortedArray<Node> children;
    /**
     * Node content type
     */
    private final NodeType          type;
    /**
     * Node content
     */
    private final Object            value;

    /**
     * Create a empty node
     */
    private Node()
    {
        this.children = null;
        this.type = NodeType.WILDCARD;
        this.value = null;
    }

    /**
     * Create a node
     *
     * @param type  Node content type
     * @param value Node content
     */
    private Node(final NodeType type, final Object value)
    {
        this.children = new SortedArray<>(Node.class, true);
        this.type = type;
        this.value = value;
    }

    /**
     * Check if given type if node content type
     *
     * @param nodeType Type to check
     * @throws IllegalStateException if given type not the node content type
     */
    private void check(NodeType nodeType)
    {
        if (this.type != nodeType)
        {
            throw new IllegalStateException("The current node type is " + this.type + " not " + nodeType);
        }
    }

    /**
     * Add a child
     *
     * @param child Child to add
     */
    void addChild(@NotNull Node child)
    {
        Objects.requireNonNull(child, "child MUST NOT be null!");
        this.children.add(child);
    }

    /**
     * Get child at given index
     *
     * @param index Node index
     * @return Node at given index
     */
    Node child(int index)
    {
        return this.children.get(index);
    }

    /**
     * Duplicate current node
     *
     * @return Duplicate
     */
    Node duplicate()
    {
        return new Node(this.type, this.value);
    }

    /**
     * Index of a child
     *
     * @param child Child search
     * @return child index
     */
    int indexOf(Node child)
    {
        return this.children.indexOf(child);
    }

    /**
     * Number of children
     *
     * @return Number of children
     */
    int numberChildren()
    {
        return this.children.size();
    }

    /**
     * Remove a child
     *
     * @param child Child to remove
     */
    void removeChild(Node child)
    {
        this.children.remove(child);
    }

    /**
     * Collect all children that match to given criteria
     *
     * @param nodeTest Criteria for search
     * @return List of searched children
     */
    SortedArray<Node> search(NodeTest nodeTest)
    {
        return this.children.seekElements(nodeTest);
    }

    /**
     * Visit the node
     *
     * @param graphVisitor Visitor that collects information
     */
    void visit(@NotNull GraphVisitor graphVisitor)
    {
        this.children.consume(node ->
                              {
                                  graphVisitor.enterNode(node);
                                  node.visit(graphVisitor);
                                  graphVisitor.exitNode(node);
                              });
    }

    /**
     * Obtain binarizable value
     *
     * @param <B> Binarizable type
     * @return Binarizable value
     * @throws IllegalStateException If node content not a binarizable
     */
    public <B extends Binarizable> B binarizableValue()
    {
        this.check(NodeType.BINARIZABLE);
        return (B) this.value;
    }

    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p>
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p>
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p>
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p>
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     * <p>
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param node the object to be compared.
     * @return a negative integer, zero, or a positive integer as this object
     * is less than, equal to, or greater than the specified object.
     * @throws NullPointerException if the specified object is null
     * @throws ClassCastException   if the specified object's type prevents it
     *                              from being compared to this object.
     */
    @Override
    public int compareTo(final Node node)
    {
        if (this == node)
        {
            return 0;
        }

        if (this.type != node.type)
        {
            return this.type.ordinal() - node.type.ordinal();
        }

        switch (this.type)
        {
            case BINARIZABLE:
                Class<?> classValue = this.value.getClass();

                int comparison = classValue.getName().compareTo(node.value.getClass().getName());

                if (comparison != 0)
                {
                    return comparison;
                }

                if (Comparable.class.isAssignableFrom(classValue))
                {
                    Comparable comparable = (Comparable) this.value;
                    return comparable.compareTo(node.value);
                }

                return this.value.hashCode() - node.value.hashCode();
            case DOUBLE:
                return Math2.compare((Double) this.value, (Double) node.value);
            case INT:
                return (Integer) this.value - (Integer) node.value;
            case STRING:
                return ((String) this.value).compareTo((String) node.value);
            case TRIPLET:
                return ((Triplet) this.value).compareTo((Triplet) node.value);
        }

        return this.value.hashCode() - node.value.hashCode();
    }

    /**
     * Obtain double value
     *
     * @return double value
     * @throws IllegalStateException If node content not a double
     */
    public double doubleValue()
    {
        this.check(NodeType.DOUBLE);
        return (Double) this.value;
    }

    /**
     * Indicates if given object equals to this node
     *
     * @param object Object to compare with
     * @return {@code true} if given object equals to this node
     */
    @Override
    public boolean equals(final Object object)
    {
        if (this == object)
        {
            return true;
        }

        if (null == object || Node.WILDCARD == this || Node.WILDCARD == object)
        {
            return false;
        }

        if (!Node.class.equals(object.getClass()))
        {
            return false;
        }

        Node node = (Node) object;

        if (this.type != node.type)
        {
            return false;
        }

        switch (this.type)
        {
            case BINARIZABLE:
            case STRING:
            case TRIPLET:
                return this.value.equals(node.value);
            case DOUBLE:
                return Math2.equals((Double) this.value, (Double) node.value);
            case INT:
                return ((Integer) this.value).intValue() == ((Integer) node.value).intValue();
            default:
                return false;
        }
    }

    /**
     * String representation
     *
     * @return String representation
     */
    @Override
    public String toString()
    {
        if (this == Node.WILDCARD)
        {
            return "#*#";
        }

        return this.value.toString();
    }

    /**
     * Obtain int value
     *
     * @return int value
     * @throws IllegalStateException If node content not a int
     */
    public int intValue()
    {
        this.check(NodeType.INT);
        return (Integer) this.value;
    }

    /**
     * Serialize node in byte array
     *
     * @param byteArray Byte array where write node data
     */
    public void serialize(ByteArray byteArray)
    {
        byteArray.writeEnum(this.type);

        switch (this.type)
        {
            case BINARIZABLE:
                byteArray.writeBinarizableNamed((Binarizable) this.value);
                break;
            case DOUBLE:
                byteArray.writeDouble((Double) this.value);
                break;
            case INT:
                byteArray.writeInteger((Integer) this.value);
                break;
            case STRING:
                byteArray.writeString((String) this.value);
                break;
            case TRIPLET:
                ((Triplet) this.value).serialize(byteArray);
                break;
            case WILDCARD:
                return;
        }

        byteArray.writeInteger(this.children.size());
        this.children.consume(node -> node.serialize(byteArray));
    }

    /**
     * Obtain String value
     *
     * @return String value
     * @throws IllegalStateException If node content not a String
     */
    public String stringValue()
    {
        this.check(NodeType.STRING);
        return (String) this.value;
    }

    /**
     * Obtain triplet value
     *
     * @return Triplet value
     * @throws IllegalStateException If node content not a triplet
     */
    public Triplet tripletValue()
    {
        this.check(NodeType.TRIPLET);
        return (Triplet) this.value;
    }

    /**
     * Node content type
     *
     * @return Node content type
     */
    public NodeType type()
    {
        return this.type;
    }
}

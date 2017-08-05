package jhelp.antology;

import com.sun.istack.internal.NotNull;
import java.util.Objects;
import jhelp.util.io.Binarizable;
import jhelp.util.io.ByteArray;
import jhelp.util.list.SortedArray;
import jhelp.util.math.Math2;

/**
 * Created by jhelp on 22/07/17.
 */
public final class Node implements Comparable<Node>
{
    public static final Node WILDCARD = new Node();

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

    public static Node createNode(int value)
    {
        return new Node(NodeType.INT, value);
    }

    public static Node createNode(double value)
    {
        return new Node(NodeType.DOUBLE, value);
    }

    public static Node createNode(String string)
    {
        Objects.requireNonNull(string, "string MUST NOT be null!");
        return new Node(NodeType.STRING, string);
    }

    public static <B extends Binarizable> Node createNode(B binarizable)
    {
        Objects.requireNonNull(binarizable, "binarizable MUST NOT be null!");
        return new Node(NodeType.BINARIZABLE, binarizable);
    }

    private final NodeType          type;
    private final Object            value;
    private final SortedArray<Node> children;

    private Node()
    {
        this.children = null;
        this.type = NodeType.WILDCARD;
        this.value = null;
    }

    private Node(final NodeType type, final Object value)
    {
        this.children = new SortedArray<>(Node.class, true);
        this.type = type;
        this.value = value;
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
        }

        return this.value.hashCode() - node.value.hashCode();
    }

    public NodeType type()
    {
        return this.type;
    }

    private void check(NodeType nodeType)
    {
        if (this.type != nodeType)
        {
            throw new IllegalStateException("The current node type is " + this.type + " not " + nodeType);
        }
    }

    public int intValue()
    {
        this.check(NodeType.INT);
        return (Integer) this.value;
    }

    public double doubleValue()
    {
        this.check(NodeType.DOUBLE);
        return (Double) this.value;
    }

    public String stringValue()
    {
        this.check(NodeType.STRING);
        return (String) this.value;
    }

    public <B extends Binarizable> B binarizableValue()
    {
        this.check(NodeType.BINARIZABLE);
        return (B) this.value;
    }

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
            case WILDCARD:
                return;
        }

        byteArray.writeInteger(this.children.size());
        this.children.consume(node -> node.serialize(byteArray));
    }

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
                return this.value.equals(node.value);
            case DOUBLE:
                return Math2.equals((Double) this.value, (Double) node.value);
            case INT:
                return ((Integer) this.value).intValue() == ((Integer) node.value).intValue();
            default:
                return false;
        }
    }

    void addChild(Node child)
    {
        Objects.requireNonNull(child, "child MUST NOT be null!");
        this.children.add(child);
    }

    void removeChild(Node child)
    {
        this.children.remove(child);
    }

    int numberChildren()
    {
        return this.children.size();
    }

    Node child(int index)
    {
        return this.children.get(index);
    }

    int indexOf(Node child)
    {
        return this.children.indexOf(child);
    }

    SortedArray<Node> search(NodeTest nodeTest)
    {
        return this.children.seekElements(nodeTest);
    }

    void visit(@NotNull GraphVisitor graphVisitor)
    {
        this.children.consume(node ->
                              {
                                  graphVisitor.enterNode(node);
                                  node.visit(graphVisitor);
                                  graphVisitor.exitNode(node);
                              });
    }

    @Override
    public String toString()
    {
        if (this == Node.WILDCARD)
        {
            return "#*#";
        }

        return this.value.toString();
    }

    Node duplicate()
    {
        return new Node(this.type, this.value);
    }
}

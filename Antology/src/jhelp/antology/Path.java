package jhelp.antology;

import java.util.Iterator;
import jhelp.util.list.ArrayObject;
import jhelp.util.list.SizedIterable;

/**
 * Created by jhelp on 23/07/17.
 */
public final class Path implements SizedIterable<Triplet>
{
    private final ArrayObject<Triplet> path;

    Path()
    {
        this.path = new ArrayObject<>();
    }

    void add(Triplet triplet)
    {
        if (this.path.isEmpty() || this.path.last().information().equals(triplet.subject()))
        {
            this.path.add(triplet);
        }
        else
        {
            throw new IllegalArgumentException(
                    "The adding triplet subject must be the last triplet information. Last=" +
                    this.path.last() + " | triplet=" + triplet);
        }
    }

    public Path copy()
    {
        Path copy = new Path();
        this.path.consume(copy::add);
        return copy;
    }

    public Node firstSubject()
    {
        if(this.path.isEmpty()) return null;

        return this.path.first().subject();
    }

    public Node lastInformation()
    {
        if(this.path.isEmpty()) return null;

        return this.path.last().information();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Triplet> iterator()
    {
        return this.path.iterator();
    }

    /**
     * Iterable size
     *
     * @return Iterable size
     */
    @Override
    public int size()
    {
        return this.path.size();
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\nPath:");

        for (Triplet triplet : this.path)
        {
            stringBuilder.append("\n\t");
            stringBuilder.append(triplet);
        }

        stringBuilder.append("\n-+*+-\n");

        return stringBuilder.toString();
    }
}

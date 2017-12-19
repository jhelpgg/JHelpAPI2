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

package jhelp.util.data;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import jhelp.util.list.ArrayObject;
import jhelp.util.thread.Filter;
import jhelp.util.thread.Task;

public final class ObservableList<T>
{
    private final Observable<T>       lastAdded;
    private final Observable<T>       lastAddedImmutable;
    private final Observable<T>       lastRemoved;
    private final Observable<T>       lastRemovedImmutable;
    private final ArrayObject<T>      list;
    private final Observable<List<T>> listChanged;
    private final Observable<List<T>> listChangedImmutable;

    public ObservableList()
    {
        this.list = new ArrayObject<>();
        this.listChanged = new Observable<>(Collections.unmodifiableList(this.list));
        this.listChangedImmutable = this.listChanged.immutable();
        this.lastAdded = new Observable<>(null);
        this.lastAddedImmutable = this.lastAdded.immutable();
        this.lastRemoved = new Observable<>(null);
        this.lastRemovedImmutable = this.lastRemoved.immutable();
    }

    public void add(T element)
    {
        this.list.add(element);
        this.listChanged.value(Collections.unmodifiableList(this.list));
        this.lastAdded.value(element);
    }

    public boolean contains(T element)
    {
        return this.list.contains(element);
    }

    public ObservableList<T> filter(final Filter<T> filter)
    {
        return this.map(element -> element, filter);
    }

    public T get(int index)
    {
        return this.list.get(index);
    }

    public int indexOf(T element)
    {
        return this.list.indexOf(element);
    }

    public Observable<T> lastAdded()
    {
        return this.lastAddedImmutable;
    }

    public Observable<T> lastRemoved()
    {
        return this.lastRemovedImmutable;
    }

    public Observable<List<T>> list()
    {
        return this.listChangedImmutable;
    }

    public <T1> ObservableList<T1> map(final Task<T, T1> task)
    {
        return this.map(task, null);
    }

    public <T1> ObservableList<T1> map(final Task<T, T1> task, final Filter<T> filter)
    {
        Objects.requireNonNull(task, "task MUST NOT be null!");
        final ObservableList<T1> observableList = new ObservableList<>();
        this.list.consume(element -> observableList.add(task.playTask(element)),
                          filter);

        this.lastAdded.startObserve((observable, addedValue) ->
                                    {
                                        if (filter == null || filter.isFiltered(addedValue))
                                        {
                                            observableList.add(task.playTask(addedValue));
                                        }
                                    });

        this.lastRemoved.startObserve((observable, removedValue) ->
                                      {
                                          if (filter == null || filter.isFiltered(removedValue))
                                          {
                                              observableList.remove(task.playTask(removedValue));
                                          }
                                      });

        return observableList;
    }

    public T remove(int index)
    {
        T element = this.list.remove(index);
        this.listChanged.value(Collections.unmodifiableList(this.list));
        this.lastRemoved.value(element);
        return element;
    }

    public boolean remove(T element)
    {
        if (this.list.remove(element))
        {
            this.listChanged.value(Collections.unmodifiableList(this.list));
            this.lastRemoved.value(element);
            return true;
        }

        return false;
    }

    public int size()
    {
        return this.list.size();
    }
}

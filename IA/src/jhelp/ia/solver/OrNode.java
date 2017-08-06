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

package jhelp.ia.solver;

import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jhelp.util.thread.Future;
import jhelp.util.thread.TaskException;

/**
 * Created by jhelp on 24/06/17.
 */
public class OrNode<T> extends SolverNode<T>
{
    private final List<SolverNode<T>> children;

    public OrNode()
    {
        this.children = new ArrayList<>();
    }

    @Override
    protected SolverNode<T> solveInternal()
    {
        Future<Solution<T>>[] futures;

        synchronized (this.children)
        {
            int length = this.children.size();

            if (length == 0)
            {
                return Solution.noSolution();
            }

            if (length == 1)
            {
                this.giveSolver(this.children.get(0));
                return this.children.get(0);
            }

            TaskSolve<T> taskSolve = this.createTask();
            futures = (Future<Solution<T>>[]) new Future[length];

            for (int index = 0; index < length; index++)
            {
                this.giveSolver(this.children.get(index));
                futures[index] = Future.launch(taskSolve, this.children.get(index));
            }
        }

        Future<Solution<T>> solverNodeFuture = Future.or(solution -> !solution.isNoSolution(),
                                                         futures);

        try
        {
            Solution<T> solverNode = solverNodeFuture.get();

            if (solverNode != null)
            {
                return solverNode;
            }
        }
        catch (TaskException ignored)
        {
        }

        return Solution.noSolution();
    }

    public void add(@NotNull SolverNode<T> solverNode)
    {
        Objects.requireNonNull(solverNode, "solverNode MUST NOT be null!");

        synchronized (this.children)
        {
            this.children.add(solverNode);
        }
    }

    public boolean empty()
    {
        return this.children.isEmpty();
    }
}

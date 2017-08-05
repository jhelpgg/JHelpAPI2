package jhelp.ia.solver;

import com.sun.istack.internal.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jhelp.util.debug.Debug;
import jhelp.util.thread.Future;
import jhelp.util.thread.TaskException;

/**
 * Created by jhelp on 24/06/17.
 */
public abstract class AndNode<T> extends SolverNode<T>
{
    private final List<SolverNode<T>> children;

    public AndNode()
    {
        this.children = new ArrayList<>();
    }

    public void add(@NotNull SolverNode<T> solverNode)
    {
        Objects.requireNonNull(solverNode, "solverNode MUST NOT be null!");

        synchronized (this.children)
        {
            this.children.add(solverNode);
        }
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

        Future<List<Solution<T>>> and = Future.and(futures);

        try
        {
            List<Solution<T>> solverNodes = and.get();
            List<T>           result      = new ArrayList<>();

            for (Solution<T> solverNode : solverNodes)
            {
                if (solverNode.isNoSolution())
                {
                    return Solution.noSolution();
                }
                else
                {
                    result.add(solverNode.solution());
                }
            }

            return Solution.solution(this.combine(result));
        }
        catch (TaskException e)
        {
            Debug.exception(e);
        }

        return Solution.noSolution();
    }

    protected abstract T combine(List<T> intermediates);

    public boolean empty()
    {
        return this.children.isEmpty();
    }
}

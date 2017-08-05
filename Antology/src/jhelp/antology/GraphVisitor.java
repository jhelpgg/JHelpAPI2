package jhelp.antology;

import com.sun.istack.internal.NotNull;

/**
 * Created by jhelp on 23/07/17.
 */
public interface GraphVisitor
{
    void enterNode(@NotNull Node node);

    void exitNode(@NotNull Node node);
}

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

package jhelp.util.math.formal;

/**
 * Constants references.<br>
 * A reference associate a symbol to a real value as PI, e, ... <br>
 * The symbol will be replace by its value when simplification occurs <br>
 * <br>
 *
 * @author JHelp
 */
public interface ConstantsReferences
{
    /**
     * Function representation of the constant
     *
     * @param s Symbol search
     * @return Function representation or {@code null} if no symbol match
     */
    Function constantFunction(String s);

    /**
     * Define a constant
     *
     * @param s Symbol associate
     * @param d Constant value
     */
    void defineConstant(String s, double d);

    /**
     * Indicates is a symbol is a define constant
     *
     * @param s Symbol tested
     * @return {@code true} if the symbol defined
     */
    boolean isConstantDefine(String s);

    /**
     * Real value of constant
     *
     * @param s Symbol search
     * @return Constant value or {@link Double#NaN} if not define
     */
    double obtainConstantValue(String s);
}
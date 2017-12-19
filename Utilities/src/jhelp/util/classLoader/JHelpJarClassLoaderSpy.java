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

package jhelp.util.classLoader;

/**
 * Spy/listener of {@link JHelpJarClassLoader} activity
 *
 * @author JHelp <br>
 */
@SuppressWarnings("rawtypes")
public interface JHelpJarClassLoaderSpy
{
    /**
     * Called if loading class is already know
     *
     * @param clas Loading class
     */
    void aleradyKnown(Class clas);

    /**
     * Called if loading class is load by default class loader
     *
     * @param clas Loading class
     */
    void loadByDefaultClassLoader(Class clas);

    /**
     * Called if loading class is load by the {@link JHelpJarClassLoader}
     *
     * @param clas Loading class
     */
    void loadByJHelpJarClassLoader(Class clas);

    /**
     * Called if loading class is load by an other class loader
     *
     * @param clas Loading class
     */
    void loadByOtherClassLoader(Class clas);

    /**
     * Called if loading class can't be load
     *
     * @param className Loading class name
     */
    void notLoad(String className);
}
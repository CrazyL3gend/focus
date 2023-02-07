package ru.crazylegend.focus.dependency;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

/**
 * This abstraction assumes that every Dependency is
 * file and it may be loaded.
 *
 * @author VoidPointer aka NyanGuyMF
 */
public interface DependencyLoader {

    /**
     * Load given dependency .jar archive to current JVM process.
     *
     * @throws MalformedURLException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    boolean load(File dependencyFile) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, MalformedURLException, NoSuchFieldException;

}

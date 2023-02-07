package ru.crazylegend.focus.dependency;


import org.bukkit.plugin.Plugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author VoidPointer aka NyanGuyMF
 */
public final class PluginDependencyLoader implements DependencyLoader {

    private static final String ADD_URL_METHOD_NAME = "addURL";
    private static final String URL_CLASS_PATH_FIELD_NAME = "ucp";
    private final URLClassLoader pluginClassLoader;

    private PluginDependencyLoader(final Plugin plugin) {
        ClassLoader pluginClassLoader = plugin.getClass().getClassLoader();
        if (pluginClassLoader instanceof URLClassLoader)
            this.pluginClassLoader = (URLClassLoader) pluginClassLoader;
        else
            throw new IllegalArgumentException("Plugin class loader is not instance of URLClassLoader");
    }

    /**
     * Returns {@link DependencyLoader} instance for specified plugin or
     * <tt>null</tt> if it cannot be created.
     */
    public static DependencyLoader forPlugin(final Plugin plugin) {
        DependencyLoader dependencyLoader = null;
        try {
            dependencyLoader = new PluginDependencyLoader(plugin);
        } catch (IllegalArgumentException ignore) {
        }
        return dependencyLoader;
    }

    /**
     * Load given dependency .jar archive to current JVM process
     * using plugin {@link URLClassLoader}.
     *
     * @throws MalformedURLException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Override
    public boolean load(final File dependencyFile) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, MalformedURLException {
        Method addUrlMethod = getAddUrlMethod();
        if (addUrlMethod == null)
            return false;

        URL url = dependencyFile.toPath().toUri().toURL();
        addUrlMethod.invoke(pluginClassLoader, url);
        return true;
    }

    private Method getAddUrlMethod() {
        Method addUrlMethod = null;

        try {
            addUrlMethod = URLClassLoader.class.getDeclaredMethod(ADD_URL_METHOD_NAME, URL.class);
            addUrlMethod.setAccessible(true);
        } catch (Exception ignored) {
        }

        return addUrlMethod;
    }

}

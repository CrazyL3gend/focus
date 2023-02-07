package ru.crazylegend.focus.api.service;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import ru.crazylegend.focus.api.Cancelable;

import java.util.function.Function;

public interface FocusApi extends Cancelable {

    static FocusApi bootstrapWith(Plugin plugin) {
        FocusApi focusApi = Bukkit.getServicesManager().load(FocusApi.class);
        if (focusApi != null) {
            return focusApi;
        }
        focusApi = new HashFocusApi(plugin);
        Bukkit.getServicesManager().register(FocusApi.class, focusApi, focusApi.getPlugin(), ServicePriority.Highest);
        return focusApi;
    }

    boolean isCancelled();

    Plugin getPlugin();

    <T extends FocusServiceApi> T registerService(Class<T> serviceClass, Function<Plugin, T> serviceApi);

    <T extends FocusServiceApi> T unregisterService(Class<T> serviceClass);

    <T extends FocusServiceApi> T getService(Class<T> serviceClass);

}

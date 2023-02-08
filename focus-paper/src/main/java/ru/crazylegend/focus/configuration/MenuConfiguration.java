package ru.crazylegend.focus.configuration;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.crazylegend.focus.menu.reader.MenuReaders;
import ru.crazylegend.focus.menu.reader.ReadMenu;
import ru.crazylegend.focus.menu.reader.YamlMenuReader;

public class MenuConfiguration extends AbstractConfiguration {

    private static final YamlMenuReader serializer = MenuReaders.yaml().pattern();

    public MenuConfiguration(@NotNull JavaPlugin plugin, @NotNull String fileName) {
        super(plugin, fileName);
    }

    public ReadMenu getMenu(String path) {
        ConfigurationSection menuSection = getSection(path);
        return serializer.read(menuSection);
    }

}

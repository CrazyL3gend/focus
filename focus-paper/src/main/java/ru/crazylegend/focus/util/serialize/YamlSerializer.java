package ru.crazylegend.focus.util.serialize;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public interface YamlSerializer<T> {

    ConfigurationSection serialize(YamlConfiguration config, String path, T t);

    T deserialize(Configuration config, String path);

}

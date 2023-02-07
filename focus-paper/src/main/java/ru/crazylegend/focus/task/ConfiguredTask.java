package ru.crazylegend.focus.task;

import org.jetbrains.annotations.NotNull;
import ru.crazylegend.focus.configuration.Configuration;

public interface ConfiguredTask extends PluginTask {

    @NotNull Configuration getConfig();

    @NotNull String getConfigurationRootPath();

}

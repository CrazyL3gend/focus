package ru.crazylegend.focus.command;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;

/**
 * The merge of bukkit {@link CommandExecutor} and bukkit {@link TabCompleter}
 */
public interface MergedExecutor extends CommandExecutor, TabCompleter {

}

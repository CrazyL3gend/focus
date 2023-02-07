package ru.crazylegend.focus.command.argument.suggestion;

import org.bukkit.command.CommandSender;

/**
 * The internal pseudo supplier (actually BiFunction) for the suggestions provider
 *
 * @param <T> type of result
 * @since 1.12.0
 */
@FunctionalInterface
public interface ProviderSupplier<T> {

    T supply(CommandSender sender, String input);

}

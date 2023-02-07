package ru.crazylegend.focus.util.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import ru.crazylegend.focus.util.function.Optionality;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class PlayerFilters {

    private PlayerFilters() {
        throw new UnsupportedOperationException();
    }

    public static Collection<Player> online() {
        return Bukkit.getOnlinePlayers().stream()
                .map(player -> (Player) player)
                .collect(Collectors.toList());
    }

    public static Optionality<Player> byName(String name) {
        return Optionality.optionalOf(() -> {
            Player player = Bukkit.getPlayer(name);
            if (player == null || !player.isOnline()) {
                return null;
            }
            return player;
        });
    }

    public static Optionality<Player> byUUID(UUID uuid) {
        return Optionality.optionalOf(() -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) {
                return null;
            }
            return player;
        });
    }

    public static Optionality<Player> byPredicate(Predicate<Player> predicate) {
        return Optionality.optionalOf(predicate(online(), predicate).stream()
                .findFirst()
                .orElse(null));
    }

    public static List<Player> predicate(Collection<Player> list, Predicate<Player> predicate) {
        return list.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static List<Player> predicate(Predicate<Player> predicate) {
        return predicate(online(), predicate);
    }

    public static List<Player> other(Collection<Player> list) {
        return predicate(player -> !list.contains(player));
    }

    public static List<Player> other(Collection<Player> players, Collection<Player> other) {
        return predicate(players, player -> !other.contains(player));
    }

}

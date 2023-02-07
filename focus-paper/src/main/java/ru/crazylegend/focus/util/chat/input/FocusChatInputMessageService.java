package ru.crazylegend.focus.util.chat.input;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import ru.crazylegend.focus.listener.QuickEventListener;
import ru.crazylegend.focus.task.AbstractPluginTask;
import ru.crazylegend.focus.task.PluginTask;
import ru.crazylegend.focus.util.function.Optionality;
import ru.crazylegend.focus.util.player.PlayerData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class FocusChatInputMessageService implements ChatInputMessageService {

    private Plugin plugin;
    private Map<UUID, ChatInputMessage> messageMap = new HashMap<>();
    private PluginTask timedTask;

    public FocusChatInputMessageService(Plugin plugin) {
        this.plugin = plugin;
        QuickEventListener.newListener().event(AsyncPlayerChatEvent.class, event -> {
                    Player player = event.getPlayer();
                    String message = event.getMessage();
                    getMessage(player).ifPresent(chatMessage -> {
                        event.setCancelled(true);
                        if (chatMessage.handleMessage(player, message)) {
                            messageMap.remove(chatMessage.getUuid());
                        }
                    });
                }).event(PlayerQuitEvent.class, event -> removeOnQuit(event.getPlayer()))
                .event(PlayerKickEvent.class, event -> removeOnQuit(event.getPlayer()))
                .register(plugin);
    }

    private void removeOnQuit(Player player) {
        getMessage(player).ifPresent(message -> {
            if (message.isSaveAfterReconnect()) {
                return;
            }
            removeMessage(message);
        });
    }

    @Override
    public ChatInputMessage createMessage(ChatInputMessage message) {
        messageMap.put(message.getUuid(), message);

        if (message instanceof TimedChatInputMessage && (timedTask == null || !timedTask.isRunning())) {
            timedTask = new TimedChatInputTask(plugin);
        }

        return message;
    }

    @Override
    public Optionality<ChatInputMessage> getMessage(UUID uuid) {
        return Optionality.optionalOfNullable(messageMap.get(uuid));
    }

    @Override
    public Optionality<ChatInputMessage> removeMessage(UUID uuid) {
        return Optionality.optionalOfNullable(messageMap.get(uuid)).ifPresent(this::removeMessage);
    }

    @Override
    public ChatInputMessage removeMessage(ChatInputMessage inputMessage) {
        messageMap.remove(inputMessage.getUuid());
        inputMessage.getRemoveAction().accept(new PlayerData(inputMessage.getUuid()));

        return inputMessage;
    }

    @Override
    public void cancel() {
        messageMap.clear();
        if (timedTask != null) {
            timedTask.shutdown();
        }
    }

    private class TimedChatInputTask extends AbstractPluginTask {

        private TimedChatInputTask(Plugin plugin) {
            super(plugin);
            runTaskTimer(true);
        }

        @Override
        protected long getPeriod() {
            return 0;
        }

        @Override
        public void start() {
            new HashSet<>(messageMap.values()).forEach(message -> {
                if (!(message instanceof TimedChatInputMessage)) {
                    return;
                }
                TimedChatInputMessage timed = (TimedChatInputMessage) message;
                if (!timed.checkTime()) {
                    messageMap.remove(message.getUuid());
                    Bukkit.getScheduler().runTask(plugin, () -> timed.getTimeExpiresAction().accept(new PlayerData(timed.getUuid())));
                }
            });
        }
    }
}

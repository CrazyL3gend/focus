package ru.crazylegend.focus.util.chat.input;

import org.bukkit.entity.Player;
import ru.crazylegend.focus.api.service.FocusServiceApi;
import ru.crazylegend.focus.util.function.Optionality;


import java.util.UUID;

public interface ChatInputMessageService extends FocusServiceApi {

    ChatInputMessage createMessage(ChatInputMessage message);

    default ChatInputMessage createMessage(ChatInputMessage.Builder builder) {
        return createMessage(builder.create());
    }

    Optionality<ChatInputMessage> getMessage(UUID uuid);

    default Optionality<ChatInputMessage> getMessage(Player player) {
        return getMessage(player.getUniqueId());
    }

    Optionality<ChatInputMessage> removeMessage(UUID uuid);

    default Optionality<ChatInputMessage> removeMessage(Player player) {
        return getMessage(player.getUniqueId());
    }

    ChatInputMessage removeMessage(ChatInputMessage inputMessage);


}

package ru.crazylegend.focus.util.chat.input;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.entity.Player;
import ru.crazylegend.focus.api.Creatable;
import ru.crazylegend.focus.util.function.Optionality;
import ru.crazylegend.focus.util.player.PlayerData;
import ru.crazylegend.focus.util.player.PlayerFilters;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ChatInputMessage {

    private UUID uuid;
    private Predicate<InputMessage> predicate;
    private Consumer<InputMessage> actionIfTrue, actionIfFalse;
    private Consumer<PlayerData> removeAction;
    private boolean saveAfterReconnect, removeAfterFalse;

    public ChatInputMessage(UUID uuid, Predicate<InputMessage> predicate, Consumer<InputMessage> actionIfTrue, Consumer<InputMessage> actionIfFalse, Consumer<PlayerData> removeAction, boolean saveAfterReconnect, boolean removeAfterFalse) {
        this.uuid = uuid;
        this.predicate = predicate;
        this.actionIfTrue = actionIfTrue;
        this.actionIfFalse = actionIfFalse;
        this.removeAction = removeAction;
        this.saveAfterReconnect = saveAfterReconnect;
        this.removeAfterFalse = removeAfterFalse;
    }

    boolean handleMessage(Player player, String message) {
        InputMessage inputMessage = new InputMessage(player, message);
        if(predicate.test(inputMessage)) {
            actionIfTrue.accept(inputMessage);
            return true;
        }
        actionIfFalse.accept(inputMessage);
        return removeAfterFalse;
    }

    public Optionality<Player> getPlayer() {
        return PlayerFilters.byUUID(uuid);
    }

    public UUID getUuid() {
        return uuid;
    }

    public Predicate<InputMessage> getPredicate() {
        return predicate;
    }

    public Consumer<InputMessage> getActionIfTrue() {
        return actionIfTrue;
    }

    public Consumer<InputMessage> getActionIfFalse() {
        return actionIfFalse;
    }

    public Consumer<PlayerData> getRemoveAction() {
        return removeAction;
    }

    public boolean isRemoveAfterFalse() {
        return removeAfterFalse;
    }

    public boolean isSaveAfterReconnect() {
        return saveAfterReconnect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatInputMessage that = (ChatInputMessage) o;
        return new EqualsBuilder()
                .append(saveAfterReconnect, that.saveAfterReconnect)
                .append(removeAfterFalse, that.removeAfterFalse)
                .append(uuid, that.uuid)
                .append(predicate, that.predicate)
                .append(removeAction, that.removeAction)
                .append(actionIfTrue, that.actionIfTrue)
                .append(actionIfFalse, that.actionIfFalse)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(uuid)
                .append(predicate)
                .append(actionIfTrue)
                .append(actionIfFalse)
                .append(saveAfterReconnect)
                .append(removeAfterFalse)
                .append(removeAction)
                .toHashCode();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder implements Creatable<ChatInputMessage> {

        private UUID uuid = UUID.randomUUID();
        private Predicate<InputMessage> predicate = inputMessage -> true;
        private Consumer<InputMessage> actionIfTrue = (inputMessage -> {}), actionIfFalse = (inputMessage -> {});
        private Consumer<PlayerData> removeAction = (data) -> {};
        private boolean saveAfterReconnect = false, removeAfterFalse = false;

        Builder() {
        }

        public Builder setUuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder setPlayer(Player player) {
            return setUuid(player.getUniqueId());
        }

        public Builder setPredicate(Predicate<InputMessage> predicate) {
            this.predicate = predicate;
            return this;
        }

        public Builder setActionIfTrue(Consumer<InputMessage> actionIfTrue) {
            this.actionIfTrue = actionIfTrue;
            return this;
        }

        public Builder setActionIfFalse(Consumer<InputMessage> actionIfFalse) {
            this.actionIfFalse = actionIfFalse;
            return this;
        }

        public Builder setRemoveAction(Consumer<PlayerData> removeAction) {
            this.removeAction = removeAction;
            return this;
        }

        public Builder setSaveAfterReconnect(boolean saveAfterReconnect) {
            this.saveAfterReconnect = saveAfterReconnect;
            return this;
        }

        public Builder setRemoveAfterFalse(boolean removeAfterFalse) {
            this.removeAfterFalse = removeAfterFalse;
            return this;
        }

        @Override
        public ChatInputMessage create() {
            return new ChatInputMessage(uuid, predicate, actionIfTrue, actionIfFalse, removeAction, saveAfterReconnect, removeAfterFalse);
        }
    }
}

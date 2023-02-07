package ru.crazylegend.focus.util.chat.input;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.entity.Player;
import ru.crazylegend.focus.util.player.PlayerData;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TimedChatInputMessage extends ChatInputMessage {

    private long time;
    private Consumer<PlayerData> timeExpiresAction;

    public TimedChatInputMessage(long time, Consumer<PlayerData> timeExpiresAction, ChatInputMessage message) {
        super(message.getUuid(), message.getPredicate(), message.getActionIfTrue(), message.getActionIfFalse(),
                message.getRemoveAction(), message.isSaveAfterReconnect(), message.isRemoveAfterFalse());
        this.time = time;
        this.timeExpiresAction = timeExpiresAction;
    }

    public long getTime() {
        return time;
    }

    public Consumer<PlayerData> getTimeExpiresAction() {
        return timeExpiresAction;
    }

    boolean checkTime() {
        if(time > 0) {
            time--;
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimedChatInputMessage that = (TimedChatInputMessage) o;
        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(time, that.time)
                .append(timeExpiresAction, that.timeExpiresAction)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(time)
                .append(timeExpiresAction)
                .toHashCode();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder extends ChatInputMessage.Builder {

        private long time = 60;
        private Consumer<PlayerData> timeExpiresAction = (data) -> {};

        private Builder() {
        }

        public Builder setTime(long time) {
            this.time = time;
            return this;
        }

        public Builder setTimeExpiresAction(Consumer<PlayerData> timeExpiresAction) {
            this.timeExpiresAction = timeExpiresAction;
            return this;
        }

        @Override
        public Builder setPlayer(Player player) {
            super.setPlayer(player);
            return this;
        }

        @Override
        public Builder setActionIfFalse(Consumer<InputMessage> actionIfFalse) {
            super.setActionIfFalse(actionIfFalse);
            return this;
        }

        @Override
        public Builder setActionIfTrue(Consumer<InputMessage> actionIfTrue) {
            super.setActionIfTrue(actionIfTrue);
            return this;
        }

        @Override
        public Builder setPredicate(Predicate<InputMessage> predicate) {
            super.setPredicate(predicate);
            return this;
        }

        @Override
        public Builder setRemoveAction(Consumer<PlayerData> removeAction) {
            super.setRemoveAction(removeAction);
            return this;
        }

        @Override
        public Builder setRemoveAfterFalse(boolean removeAfterFalse) {
            super.setRemoveAfterFalse(removeAfterFalse);
            return this;
        }

        @Override
        public Builder setUuid(UUID uuid) {
            super.setUuid(uuid);
            return this;
        }

        @Override
        public Builder setSaveAfterReconnect(boolean saveAfterReconnect) {
            super.setSaveAfterReconnect(saveAfterReconnect);
            return this;
        }

        @Override
        public ChatInputMessage create() {
            return new TimedChatInputMessage(time, timeExpiresAction, super.create());
        }
    }
}

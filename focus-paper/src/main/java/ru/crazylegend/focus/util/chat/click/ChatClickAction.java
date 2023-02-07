package ru.crazylegend.focus.util.chat.click;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.entity.Player;
import ru.crazylegend.focus.api.Creatable;

import java.util.UUID;
import java.util.function.Consumer;

public class ChatClickAction {

    private UUID uuid;
    private Consumer<Player> action;
    private boolean removeOnClick;

    public ChatClickAction(UUID uuid, Consumer<Player> action, boolean removeOnClick) {
        this.uuid = uuid;
        this.action = action;
        this.removeOnClick = removeOnClick;
    }

    public ChatClickAction(UUID uuid, Consumer<Player> action) {
        this.uuid = uuid;
        this.action = action;
    }

    public String getCommand() {
        return FocusChatClickActionService.COMMAND_START + uuid.toString();
    }

    public UUID getUuid() {
        return uuid;
    }

    public Consumer<Player> getAction() {
        return action;
    }

    public boolean isRemoveOnClick() {
        return removeOnClick;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatClickAction that = (ChatClickAction) o;
        return new EqualsBuilder().append(removeOnClick, that.removeOnClick).append(uuid, that.uuid).append(action, that.action).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(uuid).append(action).append(removeOnClick).toHashCode();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder implements Creatable<ChatClickAction> {

        private UUID uuid;
        private Consumer<Player> action;
        private boolean removeOnClick;

        private Builder() {
        }

        public Builder setUuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder setAction(Consumer<Player> action) {
            this.action = action;
            return this;
        }

        public Builder setRemoveOnClick(boolean removeOnClick) {
            this.removeOnClick = removeOnClick;
            return this;
        }

        ChatClickAction create(UUID uuid) {
            return new ChatClickAction(uuid, action, removeOnClick);
        }

        @Override
        public ChatClickAction create() {
            return new ChatClickAction(uuid, action, removeOnClick);
        }
    }
}

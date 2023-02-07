package ru.crazylegend.focus.cooldown.preset;

import ru.crazylegend.focus.cooldown.RichCooldownStorage;

public class RichPlayersCooldownStorage extends RichCooldownStorage<String> implements PlayersCooldownStorage {

    /**
     * Extension for {@link RichCooldownStorage} with String key preset
     *
     * @param defaultCooldown The cooldown default value for all stored entries without custom cooldown
     */
    public RichPlayersCooldownStorage(long defaultCooldown) {
        super(defaultCooldown);
    }
}
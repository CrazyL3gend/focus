package ru.crazylegend.focus.cooldown.preset;

import ru.crazylegend.focus.cooldown.LiteCooldownStorage;

public class LitePlayersCooldownStorage extends LiteCooldownStorage<String> implements PlayersCooldownStorage {

    /**
     * Extension for {@link LiteCooldownStorage} with String key preset
     *
     * @param cooldown The cooldown constant for all stored entries
     */
    public LitePlayersCooldownStorage(long cooldown) {
        super(cooldown);
    }

}
package ru.crazylegend.focus.cooldown;


import java.util.HashMap;

/**
 * Lite cooldown storage interface implementation with constant cooldown value
 *
 * @param <K> Type of key for the Map interface, such as String (player name and etc.)
 */
public class LiteCooldownStorage<K> extends HashMap<K, Long> implements CooldownStorage<K> {

    private final long cooldown;

    /**
     * Lite cooldown storage interface implementation with constant cooldown value
     *
     * @param cooldown The cooldown constant for all stored entries (in seconds)
     */
    public LiteCooldownStorage(long cooldown) {
        this.cooldown = cooldown * 1000;
    }

    @Override
    public long getElapsedTime(K target) {
        Long stored = get(target);
        if (stored == null) return -1;

        long current = System.currentTimeMillis();
        long elapsed = current - stored;

        return elapsed;
    }

    @Override
    public long getRemainedTime(K target) {
        long elapsed = getElapsedTime(target);
        if (elapsed == -1) return -1;

        long remained = this.cooldown - elapsed;
        if (remained < 0) return 0;

        return remained;
    }

    @Override
    public long getResetDate(K target) {
        Long stored = get(target);
        return stored != null ? stored : -1;
    }

    @Override
    public boolean hasCooldown(K target) {
        return containsKey(target);
    }

    @Override
    public void removeCooldown(K target) {
        remove(target);
    }

    @Override
    public void refreshResetDate(K target) {
        long current = System.currentTimeMillis();
        put(target, current);
    }

    @Override
    public void setResetDate(K target, long resetDate) {
        put(target, resetDate);
    }

}

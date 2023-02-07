package ru.crazylegend.focus.cooldown;

import java.util.HashMap;

/**
 * Rich cooldown storage interface implementation with individual cooldowns values
 */
public class RichCooldownStorage<K> extends HashMap<K, CustomCooldownValue> implements CooldownStorage<K> {

    private final long defaultCooldown;

    /**
     * Rich cooldown storage interface implementation with default cooldown value
     *
     * @param defaultCooldown The cooldown default value for all stored entries without custom cooldown (in seconds)
     */
    public RichCooldownStorage(long defaultCooldown) {
        this.defaultCooldown = defaultCooldown * 1000;
    }

    @Override
    public long getElapsedTime(K target) {
        CustomCooldownValue value = get(target);
        if (value == null) return -1;

        long stored = value.getResetDate();

        long current = System.currentTimeMillis();
        long elapsed = current - stored;

        return elapsed;
    }

    @Override
    public long getRemainedTime(K target) {
        CustomCooldownValue value = get(target);
        if (value == null) return -1;

        long elapsed = getElapsedTime(target);
        if (elapsed == -1) return -1;

        long remained = value.getCooldown() - elapsed;
        if (remained < 0) return 0;

        return remained;
    }

    @Override
    public long getResetDate(K target) {
        CustomCooldownValue value = get(target);
        if (value == null) return -1;

        return value.getResetDate();
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
        CustomCooldownValue oldValue = get(target);
        CustomCooldownValue newValue = new CustomCooldownValue(
                oldValue != null
                        ? oldValue.getCooldown()
                        : this.defaultCooldown
        );

        put(target, newValue);
    }

    /**
     * Resets the custom cooldown time to default time specified in constructor of this object
     *
     * @param target Target ket value to reset (player name and etc.)
     */
    public void resetCustomCooldown(K target) {
        CustomCooldownValue value = get(target);

        if (value == null)
            value = new CustomCooldownValue(this.defaultCooldown);
        else
            value.setCooldown(this.defaultCooldown);

        put(target, value);
    }

    /**
     * Sets the custom cooldown time in seconds for target key value
     *
     * @param target           Target key value to set (player name and etc.)
     * @param cooldown         The new cooldown time (in SECONDS)
     * @param refreshResetDate Should to refresh cooldown reset date or not
     */
    public void setCustomCooldown(K target, long cooldown, boolean refreshResetDate) {
        CustomCooldownValue oldValue = get(target);
        long date = oldValue != null && !refreshResetDate ? oldValue.getResetDate() : System.currentTimeMillis();

        CustomCooldownValue newValue = new CustomCooldownValue(cooldown * 1000, date);
        put(target, newValue);
    }

    /**
     * Sets the custom cooldown time in seconds and refreshes cooldown reset date
     *
     * @param target   Target key value to set (player name and etc.)
     * @param cooldown The new cooldown time (in SECONDS)
     */
    public void setCustomCooldown(K target, long cooldown) {
        setCustomCooldown(target, cooldown, true);
    }

    @Override
    public void setResetDate(K target, long resetDate) {
        CustomCooldownValue oldValue = get(target);
        long cooldown = oldValue != null ? oldValue.getCooldown() : this.defaultCooldown;

        CustomCooldownValue newValue = new CustomCooldownValue(cooldown, resetDate);
        put(target, newValue);
    }

}
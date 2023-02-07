package ru.crazylegend.focus.cooldown;

/**
 * Cooldowns storage interface with basic methods
 *
 * @param <K> Type of key for the Map interface, such as String (player name and etc.)
 */
public interface CooldownStorage<K> {

    /**
     * Gets the elapsed time from cooldown reset date in milliseconds
     *
     * @param target Target key value to getOrDefault (player name and etc.)
     * @return The elapsed time in milliseconds or -1 if cooldown is not set
     */
    long getElapsedTime(K target);

    /**
     * Gets the remained time of cooldown in milliseconds
     *
     * @param target Target key value to getOrDefault (player name and etc.)
     * @return The remained time in milliseconds or -1 of cooldown is not set
     */
    long getRemainedTime(K target);

    /**
     * Gets the cooldown reset date if it's has been reset
     *
     * @param target Target key value to getOrDefault (player name and etc.)
     * @return The cooldown reset date in milliseconds or -1 if it is not set
     */
    long getResetDate(K target);

    /**
     * Checks if cooldown is set for specified key value or not
     *
     * @param target Target key value to check (player name and etc.)
     * @return 'true' if cooldown is set or 'false' if not
     */
    boolean hasCooldown(K target);

    /**
     * Removes the cooldown if it's added for specified key value
     *
     * @param target Target key value to remove (player name and etc.)
     */
    void removeCooldown(K target);

    /**
     * Resets the cooldown reset date for specified key value to current time (in milliseconds)
     *
     * @param target Target key value to reset (player name and etc.)
     */
    void refreshResetDate(K target);

    /**
     * Sets the individual cooldown 'reset date' for specified key value
     *
     * @param target    Target key value to reset (player name and etc.)
     * @param resetDate The new reset date (in milliseconds)
     */
    void setResetDate(K target, long resetDate);

}
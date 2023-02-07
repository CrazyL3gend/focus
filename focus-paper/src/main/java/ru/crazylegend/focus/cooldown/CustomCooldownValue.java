package ru.crazylegend.focus.cooldown;

import lombok.Data;

/**
 * Cooldown value object with custom cooldown time and cooldown reset date
 */
@Data
public class CustomCooldownValue {

    private long cooldown;
    private long resetDate;

    /**
     * Cooldown value object with custom cooldown time and current cooldown reset date
     *
     * @param cooldown Custom (individual) cooldown time (in milliseconds)
     */
    public CustomCooldownValue(long cooldown) {
        this.cooldown = cooldown;
        this.resetDate = System.currentTimeMillis();
    }

    /**
     * Cooldown value object with custom cooldown time and custom cooldown reset date
     *
     * @param cooldown  Custom (individual) cooldown time (in milliseconds)
     * @param resetDate Custom cooldown reset date (in milliseconds)
     */
    public CustomCooldownValue(long cooldown, long resetDate) {
        this.cooldown = cooldown;
        this.resetDate = resetDate;
    }

    /**
     * Refreshes old reset date to current time in milliseconds
     */
    public void refreshResetDate() {
        this.resetDate = System.currentTimeMillis();
    }

}

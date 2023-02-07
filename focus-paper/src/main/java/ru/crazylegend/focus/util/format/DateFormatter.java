package ru.crazylegend.focus.util.format;

import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Formats amount of seconds to multiple {@link TimeUnit} string
 */
@Data
public class DateFormatter {

    public static final DateFormatter DEFAULT_ENGLISH;
    public static final DateFormatter DEFAULT_RUSSIAN;

    static {
        DEFAULT_ENGLISH = new DateFormatter();
        DEFAULT_RUSSIAN = new DateFormatter();
        DEFAULT_RUSSIAN.loadRussianTimeUnitFormats();
    }

    private final Map<TimeUnit, String> customFormats;
    private String separator;

    /**
     * Date formatter with default separator (a one space) for multiple {@link TimeUnit time units}
     */
    public DateFormatter() {
        this.separator = " ";
        this.customFormats = new HashMap<>();
    }

    /**
     * Date formatter with custom separator for multiple {@link TimeUnit time units}
     *
     * @param separator The custom time units separator
     */
    public DateFormatter(String separator) {
        this.separator = separator;
        this.customFormats = new HashMap<>();
    }

    /**
     * Sets the custom format for specified time unit
     *
     * @param timeUnit Target time unit
     * @param format   The custom format
     */
    public void setTimeUnitFormat(TimeUnit timeUnit, String format) {
        this.customFormats.put(timeUnit, format);
    }

    /**
     * Loads the time unit formats preset for russian localization
     */
    public void loadRussianTimeUnitFormats() {
        setTimeUnitFormat(TimeUnit.SECOND, "%seconds% сек.");
        setTimeUnitFormat(TimeUnit.MINUTE, "%minutes% мин.");
        setTimeUnitFormat(TimeUnit.HOUR, "%hours% ч.");
        setTimeUnitFormat(TimeUnit.DAY, "%days% д.");
        setTimeUnitFormat(TimeUnit.WEEK, "%weeks% нед.");
        setTimeUnitFormat(TimeUnit.MONTH, "%months% мес.");
        setTimeUnitFormat(TimeUnit.YEAR, "%years% г.");
    }

    /**
     * Formats specified count of seconds to string with multiple separated time units
     *
     * @param seconds Count of seconds to format
     * @return Formatted string with multiple separated time units
     */
    public String format(long seconds) {
        TimeUnitsConverter converter = new TimeUnitsConverter(seconds);

        seconds = converter.getSeconds();
        long minutes = converter.getMinutes();
        long hours = converter.getHours();
        long days = converter.getDays();
        long weeks = converter.getWeeks();
        long months = converter.getMonths();
        long years = converter.getYears();

        // String formating:
        List<String> parts = new ArrayList<>();

        if (months > 0)
            parts.add(formatUnit(TimeUnit.MONTH, months));
        if (weeks > 0)
            parts.add(formatUnit(TimeUnit.WEEK, weeks));
        if (days > 0)
            parts.add(formatUnit(TimeUnit.DAY, days));
        if (hours > 0)
            parts.add(formatUnit(TimeUnit.HOUR, hours));
        if (minutes > 0)
            parts.add(formatUnit(TimeUnit.MINUTE, minutes));
        if (seconds > 0)
            parts.add(formatUnit(TimeUnit.SECOND, seconds));

        return parts.stream().collect(Collectors.joining(separator));
    }

    private String formatUnit(TimeUnit unit, long count) {
        String format = customFormats.containsKey(unit) ? customFormats.get(unit) : unit.getDefaultFormat();
        return format.replace(unit.getPlaceholder(), String.valueOf(count));
    }

    @Getter
    private static class TimeUnitsConverter {

        private long seconds;
        private long minutes = 0;
        private long hours = 0;
        private long days = 0;
        private long weeks = 0;
        private long months = 0;
        private long years = 0;

        public TimeUnitsConverter(long seconds) {
            this.seconds = seconds;

            // Seconds -> minutes:
            if (seconds >= 60) {
                minutes = seconds / 60;
                this.seconds = seconds % 60;

                // Minutes -> hours:
                if (minutes >= 60) {
                    hours = minutes / 60;
                    minutes %= 60;

                    // Hours -> days:
                    if (hours >= 24) {
                        days = hours / 24;
                        hours %= 24;

                        /*
                         * Days converting
                         */

                        // Days -> years:
                        if (days >= 365) {
                            years = days / 365;
                            days %= 365;
                        }

                        // Days -> months:
                        if (days >= 30) {
                            months = days / 30;
                            days %= 30;
                        }

                        // Days -> weeks:
                        if (days >= 7) {
                            weeks = days / 7;
                            days %= 7;
                        }
                    }
                }
            }
        }
    }
}
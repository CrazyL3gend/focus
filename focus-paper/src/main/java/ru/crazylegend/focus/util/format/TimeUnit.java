package ru.crazylegend.focus.util.format;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The time unit enum which contains basic time units
 */
@Getter
@AllArgsConstructor
public enum TimeUnit {

    SECOND("%seconds%", "%seconds% second(s)"),
    MINUTE("%minutes%", "%minutes% minute(s)"),
    HOUR("%hours%", "%hours% hour(s)"),
    DAY("%days%", "%days% day(s)"),
    WEEK("%weeks%", "%weeks% week(s)"),
    MONTH("%months%", "%months% month(s)"),
    YEAR("%years%", "%years% year(s)");

    private final String placeholder;
    private final String defaultFormat;

}
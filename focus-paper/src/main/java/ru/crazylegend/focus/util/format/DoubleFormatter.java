package ru.crazylegend.focus.util.format;

/**
 * The shortener for double numbers to specified count of numbers after floating point
 */
public class DoubleFormatter {

    private final char floatingPointSymbol;

    /**
     * The shortener for double numbers with default floating point symbol ','
     */
    public DoubleFormatter() {
        this.floatingPointSymbol = ',';
    }

    /**
     * The shortener for double numbers with custom floating point symbol in output strings
     *
     * @param floatingPointSymbol The custom floating point symbol
     */
    public DoubleFormatter(char floatingPointSymbol) {
        this.floatingPointSymbol = floatingPointSymbol;
    }

    /**
     * @param source                         Original double number for shorting
     * @param numbersAfterFloatingPointCount Count of numbers after a floating point (more than 1)
     * @return The shorted double number as new string
     */
    public String shortToString(double source, int numbersAfterFloatingPointCount) {
        if (numbersAfterFloatingPointCount < 1)
            numbersAfterFloatingPointCount = 1;

        String shorted = String.format("%." + numbersAfterFloatingPointCount + "f", source);
        if (floatingPointSymbol != ',')
            shorted = shorted.replace(",", Character.toString(this.floatingPointSymbol));

        return shorted;
    }
}
package ru.crazylegend.focus.util.format;

/**
 * The shortener for float numbers to specified count of numbers after floating point
 */
public class FloatFormatter {

    private final char floatingPointSymbol;

    /**
     * The shortener for float numbers with default floating point symbol ','
     */
    public FloatFormatter() {
        this.floatingPointSymbol = ',';
    }

    /**
     * The shortener for float numbers with custom floating point symbol in output strings
     *
     * @param floatingPointSymbol The custom floating point symbol
     */
    public FloatFormatter(char floatingPointSymbol) {
        this.floatingPointSymbol = floatingPointSymbol;
    }

    /**
     * @param source                         Original float number for shorting
     * @param numbersAfterFloatingPointCount Count of numbers after a floating point (more than 1)
     * @return The shorted float number as new string
     */
    public String shortToString(float source, int numbersAfterFloatingPointCount) {
        if (numbersAfterFloatingPointCount < 1)
            numbersAfterFloatingPointCount = 1;

        String shorted = String.format("%." + numbersAfterFloatingPointCount + "f", source);
        if (floatingPointSymbol != ',')
            shorted = shorted.replace(",", Character.toString(this.floatingPointSymbol));

        return shorted;
    }

}

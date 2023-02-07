package ru.crazylegend.focus.util;

public final class LanguageUtil {

    private LanguageUtil() {
        throw new UnsupportedOperationException();
    }

    public static String plural(int count, String form1, String form2, String form3) {
        count = Math.abs(count) % 100;
        int count1 = count % 10;
        return (count > 10 && count < 20) ? form3 : ((count1 > 1 && count1 < 5) ? form2 : ((count1 == 1) ? form1 : form3));
    }

}

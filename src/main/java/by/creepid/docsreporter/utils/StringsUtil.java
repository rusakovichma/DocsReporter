/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package by.creepid.docsreporter.utils;

/**
 *
 * @author mirash
 */
public final class StringsUtil {

    private StringsUtil() {
    }

    public static String setFirstCharLower(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }

        String firstChar = string.substring(0, 1).toLowerCase();
        return setFirstChar(string, firstChar);
    }

    public static String setFirstCharUpper(String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }

        String firstChar = string.substring(0, 1).toUpperCase();
        return setFirstChar(string, firstChar);
    }

    public static String setFirstChar(String string, String firstChar) {
        StringBuilder builder = new StringBuilder(string);

        builder = builder.deleteCharAt(0);
        builder.insert(0, firstChar);

        return builder.toString();
    }
}

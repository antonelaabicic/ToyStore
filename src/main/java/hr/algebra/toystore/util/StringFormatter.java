package hr.algebra.toystore.util;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringFormatter {
    private StringFormatter() {
        throw new UnsupportedOperationException("StringFormatter is a utility class.");
    }

    public static String formatToDisplay(String rawDbString) {
        return Arrays.stream(rawDbString.split("_"))
                .map(word -> word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }

    public static String formatToDb(String displayString) {
        return displayString.trim().toUpperCase().replace(" ", "_");
    }
}

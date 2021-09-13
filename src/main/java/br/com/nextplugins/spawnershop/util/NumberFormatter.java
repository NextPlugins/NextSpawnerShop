package br.com.nextplugins.spawnershop.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NumberFormatter {

    private static final DecimalFormat numberFormat = new DecimalFormat("#,###.#");
    private static final List<String> chars = Arrays.asList("", "K", "M", "B", "T", "Q", "QQ", "S", "SS", "O", "N", "D",
        "UN", "DD", "TR", "QT", "QN", "SD", "SPD", "OD", "ND", "VG", "UVG", "DVG", "TVG", "QTV");

    public static String letterFormat(double startNumber) {
        int index = 0;
        double number = startNumber;
        while (number / 1000.0D >= 1.0D) {
            number /= 1000.0D;
            index++;
        }

        String character = index < chars.size() ? chars.get(index) : "";
        return numberFormat.format(number) + character;
    }

    public static String decimalFormat(double number) {
        return numberFormat.format(number);
    }

}

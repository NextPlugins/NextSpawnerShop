package br.com.nextplugins.spawnershop.util.time;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateParser {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static Date parse(String rawDate) {
        try {
            return dateFormat.parse(rawDate);
        } catch (Throwable t) {
            return null;
        }
    }

}

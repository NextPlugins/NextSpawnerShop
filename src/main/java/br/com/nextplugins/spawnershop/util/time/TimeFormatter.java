package br.com.nextplugins.spawnershop.util.time;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeFormatter {

    public static String format(long value) {
        if (value <= 0) return "Agora mesmo";

        long days = TimeUnit.MILLISECONDS.toDays(value);
        long hours = TimeUnit.MILLISECONDS.toHours(value) - (days * 24);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(value) - (TimeUnit.MILLISECONDS.toHours(value) * 60);
        long second = TimeUnit.MILLISECONDS.toSeconds(value) - (TimeUnit.MILLISECONDS.toMinutes(value) * 60);

        long[] times = {days, hours, minutes, second};
        String[] names = {"dia", "hora", "minuto", "segundo"};

        List<String> values = new ArrayList<>();
        for (int index = 0; index < times.length; index++) {
            long time = times[index];
            if (time > 0) {
                String name = plural(times[index], names[index]);
                values.add(name);
            }
        }

        if (values.size() == 1) {
            return values.get(0);
        }

        return String.join(", ", values.subList(0, values.size() - 1)) + " e " + values.get(values.size() - 1);
    }

    private static String plural(long quantity, String message) {
        return quantity + " " + message + (quantity == 1 ? "" : "s");
    }

}

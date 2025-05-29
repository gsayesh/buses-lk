package lk.buses.common.core.util;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    private static final ZoneId SRI_LANKA_ZONE = ZoneId.of("Asia/Colombo");

    public static LocalDateTime now() {
        return LocalDateTime.now(SRI_LANKA_ZONE);
    }

    public static LocalDate today() {
        return LocalDate.now(SRI_LANKA_ZONE);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public static String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static LocalDateTime toLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, SRI_LANKA_ZONE);
    }
}
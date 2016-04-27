package org.nuvola.tvshowtime.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

import static java.time.LocalDateTime.ofInstant;

public class DateUtils {
    public static LocalDateTime getDateTimeFromTimestamp(long timestamp) {
        if (timestamp == 0) {
            return null;
        }

        return ofInstant(Instant.ofEpochSecond(timestamp), TimeZone.getDefault().toZoneId());
    }
}

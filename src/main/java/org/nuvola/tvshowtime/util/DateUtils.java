/*
 * tvshowtimeplex
 * Copyright (C) 2016  Nuvola - Mrabti Idriss
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.nuvola.tvshowtime.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

import static java.time.LocalDateTime.ofInstant;

public class DateUtils {
    public static LocalDateTime getDateTimeFromTimestamp(long timestamp) {
        if (timestamp == 0) {
            timestamp = 1L;
        }
        return ofInstant(Instant.ofEpochSecond(timestamp), TimeZone.getDefault().toZoneId());
    }

    private static LocalDateTime getDateTime() {
        return ofInstant(Instant.now(), TimeZone.getDefault().toZoneId());
    }

    public static Boolean isTodayOrYesterday(LocalDateTime date) {
        return date.toLocalDate().equals(getDateTime().toLocalDate()) || date.toLocalDate().equals(getDateTime().toLocalDate().minusDays(1));
    }
}

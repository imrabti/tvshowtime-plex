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

public class Constants {
    public static final String TVST_CLIENT_ID = "UOWED7wBGRQv17skSZJO";
    public static final String TVST_CLIENT_SECRET = "ZHYcO8n8h6WbYuMDWVgXr7T571ZF_s1r1Rzu1-3B";
    public static final String TVST_USER_AGENT = "tvshowtime-plex";
    public static final String TVST_RATE_REMAINING_HEADER = "X-RateLimit-Remaining";
    public static final String TVST_AUTHORIZE_URI = "https://api.tvtime.com/v1/oauth/device/code";
    public static final String TVST_ACCESS_TOKEN_URI = "https://api.tvtime.com/v1/oauth/access_token";
    public static final String TVST_CHECKIN_URI = "https://api.tvtime.com/v1/checkin";

    public static final String PMS_WATCH_HISTORY = "/status/sessions/history/all";

    public static final Long SECOND_IN_MILIS = 1000L;
    public static final Long MINUTE_IN_MILIS = SECOND_IN_MILIS * 60;
}

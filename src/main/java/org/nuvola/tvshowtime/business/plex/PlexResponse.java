/*
 * tvshowtimeplex
 * Copyright (C) 2019  AScuderoni
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

package org.nuvola.tvshowtime.business.plex;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlexResponse {

    @JsonProperty("MediaContainer")
    private MediaContainer m_mediaContainer;

    public MediaContainer getMediaContainer() {
        return m_mediaContainer;
    }

    public void setMediaContainer(MediaContainer mediaContainer) {
        m_mediaContainer = mediaContainer;
    }

}

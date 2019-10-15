/*
 * tvshowtimeplex
 * Copyright (C) 2017  Appulize - Maciej Swic
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

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Account")
public class Account {
    private Long id;
    private String key;
    private String name;
    private String defaultAudioLanguage;
    private Boolean autoSelectAudio;
    private String defaultSubtitleLanguage;
    private Integer subtitleMode;
    private String thumb;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDefaultAudioLanguage() {
        return defaultAudioLanguage;
    }

    public void setDefaultAudioLanguage(String defaultAudioLanguage) {
        this.defaultAudioLanguage = defaultAudioLanguage;
    }

    public Boolean getAutoSelectAudio() {
        return autoSelectAudio;
    }

    public void setAutoSelectAudio(Boolean autoSelectAudio) {
        this.autoSelectAudio = autoSelectAudio;
    }

    public String getDefaultSubtitleLanguage() {
        return defaultSubtitleLanguage;
    }

    public void setDefaultSubtitleLanguage(String defaultSubtitleLanguage) {
        this.defaultSubtitleLanguage = defaultSubtitleLanguage;
    }

    public Integer getSubtitleMode() {
        return subtitleMode;
    }

    public void setSubtitleMode(Integer subtitleMode) {
        this.subtitleMode = subtitleMode;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("title")
    public String getTitle() {
        return name;
    }

    public void setTitle(String title) {
        this.name = title;
    }
}

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

package org.nuvola.tvshowtime.business.plex;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Video")
public class Video {
    private String grandparentTitle;
    private Integer parentIndex;
    private Integer index;
    private Long viewedAt;
    private String type;
    private String title;


    @XmlAttribute(name = "grandparentTitle")
    public String getGrandparentTitle() {
        return grandparentTitle;
    }

    public void setGrandparentTitle(String grandparentTitle) {
        this.grandparentTitle = grandparentTitle;
    }

    @XmlAttribute(name = "parentIndex")
    public Integer getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(Integer parentIndex) {
        this.parentIndex = parentIndex;
    }

    @XmlAttribute(name = "index")
    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @XmlAttribute(name = "viewedAt")
    public Long getViewedAt() {
        return viewedAt;
    }

    public void setViewedAt(Long viewedAt) {
        this.viewedAt = viewedAt;
    }

    @XmlAttribute(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @XmlAttribute(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Video={" +
                "grandparentTitle=" + grandparentTitle +
                ", parentIndex=" + parentIndex +
                ", index=" + index +
                ", viewedAt=" + viewedAt +
                ", type=" + type +
                ", title=" + title + "}";
    }
}

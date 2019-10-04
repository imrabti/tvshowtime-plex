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
import java.util.Date;

@XmlRootElement(name = "Video")
public class MetaData {

    @XmlAttribute(name = "grandparentTitle")
    private String grandparentTitle;
    @XmlAttribute(name = "parentIndex")
    private Integer parentIndex;
    @XmlAttribute(name = "index")
    private Integer index;
    @XmlAttribute(name = "viewedAt")
    private Long viewedAt;
    @XmlAttribute(name = "type")
    private String type;
    @XmlAttribute(name = "title")
    private String title;
    @XmlAttribute(name = "accountID")
    private Long accountID;

    private String key;
    private String parentKey;
    private String grandparentKey;
    private String thumb;
    private String grandparentThumb;
    private String grandparentArt;
    private Date originallyAvailableAt;
    private Integer deviceID;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getParentKey() {
        return parentKey;
    }

    public void setParentKey(String parentKey) {
        this.parentKey = parentKey;
    }

    public String getGrandparentKey() {
        return grandparentKey;
    }

    public void setGrandparentKey(String grandparentKey) {
        this.grandparentKey = grandparentKey;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getParentThumb() {
        return parentThumb;
    }

    public void setParentThumb(String parentThumb) {
        this.parentThumb = parentThumb;
    }

    public String getGrandparentThumb() {
        return grandparentThumb;
    }

    public void setGrandparentThumb(String grandparentThumb) {
        this.grandparentThumb = grandparentThumb;
    }

    public String getGrandparentArt() {
        return grandparentArt;
    }

    public void setGrandparentArt(String grandparentArt) {
        this.grandparentArt = grandparentArt;
    }

    public Date getOriginallyAvailableAt() {
        return originallyAvailableAt;
    }

    public void setOriginallyAvailableAt(Date originallyAvailableAt) {
        this.originallyAvailableAt = originallyAvailableAt;
    }

    public Integer getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(Integer deviceID) {
        this.deviceID = deviceID;
    }

    private String parentThumb;


    public String getGrandparentTitle() {
        return grandparentTitle;
    }

    public void setGrandparentTitle(String grandparentTitle) {
        this.grandparentTitle = grandparentTitle;
    }

    public Integer getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(Integer parentIndex) {
        this.parentIndex = parentIndex;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Long getViewedAt() {
        return viewedAt;
    }

    public void setViewedAt(Long viewedAt) {
        this.viewedAt = viewedAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getAccountID() {
        return accountID;
    }

    public void setAccountID(Long accountID) {
        this.accountID = accountID;
    }

    @Override
    public String toString() {
        return "Video={" +
                "grandparentTitle=" + grandparentTitle +
                ", parentIndex=" + parentIndex +
                ", index=" + index +
                ", viewedAt=" + viewedAt +
                ", type=" + type +
                ", title=" + title +
                ", accountID=" + accountID +
                "}";
    }
}

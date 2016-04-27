package org.nuvola.tvshowtime.business;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Video")
public class Video {
    private String grandparentTitle;
    private Integer parentIndex;
    private Integer index;
    private Long viewedAt;

    @XmlAttribute(name="grandparentTitle")
    public String getGrandparentTitle() {
        return grandparentTitle;
    }

    public void setGrandparentTitle(String grandparentTitle) {
        this.grandparentTitle = grandparentTitle;
    }

    @XmlAttribute(name="parentIndex")
    public Integer getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(Integer parentIndex) {
        this.parentIndex = parentIndex;
    }

    @XmlAttribute(name="index")
    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @XmlAttribute(name="viewedAt")
    public Long getViewedAt() {
        return viewedAt;
    }

    public void setViewedAt(Long viewedAt) {
        this.viewedAt = viewedAt;
    }
}

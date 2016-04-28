package org.nuvola.tvshowtime.setting;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="nuvola.pms")
public class PlexMediaServerSettings {
    private String path;
    private String watchHistory;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getWatchHistory() {
        return watchHistory;
    }

    public void setWatchHistory(String watchHistory) {
        this.watchHistory = watchHistory;
    }

    public String getWatchHistoryUrl() {
        return path + watchHistory;
    }
}

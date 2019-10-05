package org.nuvola.tvshowtime;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nuvola.tvshowtime.business.plex.MetaData;
import org.nuvola.tvshowtime.business.plex.PlexResponse;
import org.nuvola.tvshowtime.config.PMSConfig;
import org.nuvola.tvshowtime.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static org.nuvola.tvshowtime.util.Constants.PMS_GET_ACCOUNTS;
import static org.nuvola.tvshowtime.util.Constants.PMS_WATCH_HISTORY;

@Service
class PlexService {

    private static final Logger LOG = LoggerFactory.getLogger(PlexService.class);

    @Autowired
    private PMSConfig pmsConfig;
    @Autowired
    private RestTemplate pmsTemplate;
    private Map<String, Long> m_users = new HashMap<>();
    private String m_url;
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    void getUsersInPlex() throws IOException {
        m_url = pmsConfig.getPath() + PMS_GET_ACCOUNTS;
        appendPlexToken();
        LOG.debug("Get users from plex with url: " + m_url);
        String response = pmsTemplate.getForObject(m_url, String.class);
        PlexResponse plexResponse = OBJECT_MAPPER.readValue(response, PlexResponse.class);
        plexResponse.getMediaContainer().getAccount().stream().filter(user -> !user.getName().equals("")).forEach(user -> m_users.put(user.getName(), user.getId()));
    }

    Map<String, Long> getUsers() {
        return m_users;
    }

    Set<String> getWatchedOnPlex() throws IOException {
        //refresh m_users
        getUsersInPlex();

        Set<String> watched = new HashSet<>();
        m_url = pmsConfig.getPath() + PMS_WATCH_HISTORY;

        appendPlexToken();
        String response = pmsTemplate.getForObject(m_url, String.class);

        PlexResponse plexResponse = OBJECT_MAPPER.readValue(response, PlexResponse.class);
        LOG.debug(plexResponse.getMediaContainer().toString());

        for (MetaData metaData : plexResponse.getMediaContainer().getMetaData()) {
            LocalDateTime date = DateUtils.getDateTimeFromTimestamp(metaData.getViewedAt());

            LOG.debug(metaData.toString());

            // If present, mark as watched only episodes for configured user
            if (pmsConfig.getUsername() != null) {
                if (!metaData.getAccountID().equals(m_users.get(pmsConfig.getUsername()))) {
                    continue;
                }
            }

            // Mark as watched only today and yesterday episodes
            if (DateUtils.isTodayOrYesterday(date) || pmsConfig.getMarkall()) {
                if (metaData.getType().equals("episode")) {
                    String episode = new StringBuilder(metaData.getGrandparentTitle())
                            .append(" - S")
                            .append(metaData.getParentIndex())
                            .append("E").append(metaData.getIndex())
                            .toString();

                    watched.add(episode);
                }
                /*
                 * Enable when TvTime should accept movie

                else if (metaData.getType().equals("movie")) {
                    watched.add(metaData.getTitle());
                }
                */
            }
        }
        return watched;
    }

    private void appendPlexToken() {
        if (pmsConfig.getToken() != null && !pmsConfig.getToken().isEmpty()) {
            m_url += "?X-Plex-Token=" + pmsConfig.getToken();
            LOG.debug("Calling Plex with a X-Plex-Token = " + pmsConfig.getToken());
        }
    }

}

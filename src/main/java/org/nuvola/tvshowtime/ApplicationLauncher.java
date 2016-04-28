package org.nuvola.tvshowtime;

import org.nuvola.tvshowtime.business.plex.MediaContainer;
import org.nuvola.tvshowtime.business.plex.Video;
import org.nuvola.tvshowtime.business.tvshowtime.AuthorizationCode;
import org.nuvola.tvshowtime.setting.PlexMediaServerSettings;
import org.nuvola.tvshowtime.setting.TVShowTimeSettings;
import org.nuvola.tvshowtime.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ApplicationLauncher {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationLauncher.class);

    @Autowired
    private PlexMediaServerSettings pmsSettings;
    @Autowired
    private TVShowTimeSettings tvstSettings;

    private RestTemplate tvShowTimeTemplate;
    private RestTemplate pmsTemplate;

	public static void main(String[] args) {
		SpringApplication.run(ApplicationLauncher.class, args);
	}

    @PostConstruct
	public void init() {
        if (setupTVShowTimeAPI()) {
            processWatchedEpisodes();
        }
    }

    private boolean setupTVShowTimeAPI() {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_id", tvstSettings.getClientId());

        tvShowTimeTemplate = new RestTemplate();
        AuthorizationCode authorizationCode = tvShowTimeTemplate.postForObject(tvstSettings.getAuthorizeUri(),
                parameters, AuthorizationCode.class);

        System.out.println(authorizationCode);

        return true;
    }

    private void processWatchedEpisodes() {
        pmsTemplate = new RestTemplate();
        ResponseEntity<MediaContainer> response =  pmsTemplate.getForEntity(pmsSettings.getWatchHistoryUrl(),
                MediaContainer.class);
        MediaContainer mediaContainer = response.getBody();

        for (Video video : mediaContainer.getVideo()) {
            LocalDateTime date = DateUtils.getDateTimeFromTimestamp(video.getViewedAt());

            // Mark as watched only today episodes
            if(date.toLocalDate().equals(LocalDate.now().minusDays(1)) && video.getType().equals("episode")) {
                // TODO : Call TVSHow Time API to mark as Watched ...
                String episode = new StringBuilder(video.getGrandparentTitle())
                        .append(" - S")
                        .append(video.getParentIndex())
                        .append("E").append(video.getIndex())
                        .toString();

                LOG.info("Mark " + episode + " as watched");
            }
        }
    }
}

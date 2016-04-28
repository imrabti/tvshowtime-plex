package org.nuvola.tvshowtime;

import org.nuvola.tvshowtime.business.plex.MediaContainer;
import org.nuvola.tvshowtime.business.plex.Video;
import org.nuvola.tvshowtime.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class ApplicationLauncher {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationLauncher.class);

    @Value("${nuvola.pms.path}")
    private String pmsPath;
    @Value("${nuvola.pms.watchHistory}")
    private String watchHistory;

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
        tvShowTimeTemplate = new RestTemplate();

        return true;
    }

    private void processWatchedEpisodes() {
        pmsTemplate = new RestTemplate();
        ResponseEntity<MediaContainer> response =  pmsTemplate.getForEntity(pmsPath + watchHistory, MediaContainer.class);
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

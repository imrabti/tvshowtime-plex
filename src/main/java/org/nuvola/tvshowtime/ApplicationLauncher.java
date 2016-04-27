package org.nuvola.tvshowtime;

import org.nuvola.tvshowtime.business.MediaContainer;
import org.nuvola.tvshowtime.business.Video;
import org.nuvola.tvshowtime.util.DateUtils;
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
    @Value("${nuvola.pms.path}")
    private String pmsPath;
    @Value("${nuvola.pms.watchHistory}")
    private String watchHistory;

	public static void main(String[] args) {
		SpringApplication.run(ApplicationLauncher.class, args);
	}

    @PostConstruct
	public void init() {
        RestTemplate template = new RestTemplate();
        ResponseEntity<MediaContainer> response =  template.getForEntity(pmsPath + watchHistory, MediaContainer.class);
        MediaContainer mediaContainer = response.getBody();

        for (Video video : mediaContainer.getVideo()) {
            LocalDateTime date = DateUtils.getDateTimeFromTimestamp(video.getViewedAt());

            // Mark as watched only today episodes
            if((date.toLocalDate()).equals(LocalDate.now())) {
                // TODO : Call TVSHow Time API to mark as Watched ...
            }
        }
    }
}

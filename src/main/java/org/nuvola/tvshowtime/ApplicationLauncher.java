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

package org.nuvola.tvshowtime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

import org.nuvola.tvshowtime.business.plex.MediaContainer;
import org.nuvola.tvshowtime.business.plex.Video;
import org.nuvola.tvshowtime.business.tvshowtime.AccessToken;
import org.nuvola.tvshowtime.business.tvshowtime.AuthorizationCode;
import org.nuvola.tvshowtime.business.tvshowtime.Message;
import org.nuvola.tvshowtime.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import static org.nuvola.tvshowtime.util.Constants.MINUTE_IN_MILIS;
import static org.nuvola.tvshowtime.util.Constants.PMS_WATCH_HISTORY;
import static org.nuvola.tvshowtime.util.Constants.TVST_ACCESS_TOKEN_URI;
import static org.nuvola.tvshowtime.util.Constants.TVST_AUTHORIZE_URI;
import static org.nuvola.tvshowtime.util.Constants.TVST_CHECKIN_URI;
import static org.nuvola.tvshowtime.util.Constants.TVST_CLIENT_ID;
import static org.nuvola.tvshowtime.util.Constants.TVST_CLIENT_SECRET;
import static org.nuvola.tvshowtime.util.Constants.TVST_RATE_REMAINING_HEADER;
import static org.nuvola.tvshowtime.util.Constants.TVST_USER_AGENT;
import static org.springframework.http.HttpMethod.POST;

@SpringBootApplication
@EnableScheduling
public class ApplicationLauncher {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationLauncher.class);

    @Value("${nuvola.tvshowtime.tokenFile}")
    private String tokenFile;
    @Value("${nuvola.pms.path}")
    private String pmsHost;
    @Value("${nuvola.pms.token}")
    private String pmsToken;

    private RestTemplate tvShowTimeTemplate;
    private RestTemplate pmsTemplate;
    private AccessToken accessToken;
    private Timer tokenTimer;

	public static void main(String[] args) {
		SpringApplication.run(ApplicationLauncher.class, args);
	}

    @Scheduled(fixedDelay = Long.MAX_VALUE)
	public void init() {
        tvShowTimeTemplate = new RestTemplate();

        File storeToken = new File(tokenFile);
        if (storeToken.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(storeToken);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                accessToken = (AccessToken) objectInputStream.readObject();
                objectInputStream.close();
                fileInputStream.close();

                LOG.info("AccessToken loaded from file with success : " + accessToken);
            } catch (Exception e) {
                LOG.error("Error parsing the AccessToken stored in 'session_token'.");
                LOG.error("Please remove the 'session_token' file, and try again.");
                LOG.error(e.getMessage());

                System.exit(1);
            }

            try {
                processWatchedEpisodes();
            } catch (Exception e) {
                LOG.error("Error during marking episodes as watched.");
                LOG.error(e.getMessage());

                System.exit(1);
            }
        } else {
            requestAccessToken();
        }
    }

    private void requestAccessToken() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<String> entity = new HttpEntity<>("client_id=" + TVST_CLIENT_ID, headers);

            ResponseEntity<AuthorizationCode> content = tvShowTimeTemplate.exchange(TVST_AUTHORIZE_URI, POST, entity,
                    AuthorizationCode.class);
            AuthorizationCode authorizationCode = content.getBody();

            if (authorizationCode.getResult().equals("OK")) {
                LOG.info("Linking with your TVShowTime account using the code " + authorizationCode.getDevice_code());
                LOG.info("Please open the URL " + authorizationCode.getVerification_url() + " in your browser");
                LOG.info("Connect with your TVShowTime account and type in the following code : ");
                LOG.info(authorizationCode.getUser_code());
                LOG.info("Waiting for you to type in the code in TVShowTime :-D ...");

                tokenTimer = new Timer();
                tokenTimer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        loadAccessToken(authorizationCode.getDevice_code());
                    }
                }, 1000 * authorizationCode.getInterval(), 1000 * authorizationCode.getInterval());
            } else {
                LOG.error("OAuth authentication TVShowTime failed.");

                System.exit(1);
            }
        } catch (Exception e) {
            LOG.error("OAuth authentication TVShowTime failed.");
            LOG.error(e.getMessage());

            System.exit(1);
        }
    }

    private void loadAccessToken(String deviceCode) {
        String query = new StringBuilder("client_id=")
                .append(TVST_CLIENT_ID)
                .append("&client_secret=")
                .append(TVST_CLIENT_SECRET)
                .append("&code=")
                .append(deviceCode)
                .toString();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> entity = new HttpEntity<>(query, headers);

        ResponseEntity<AccessToken> content = tvShowTimeTemplate.exchange(TVST_ACCESS_TOKEN_URI, POST, entity,
                AccessToken.class);
        accessToken = content.getBody();

        if (accessToken.getResult().equals("OK")) {
            LOG.info("AccessToken from TVShowTime with success : " + accessToken);
            tokenTimer.cancel();

            storeAccessToken();
            processWatchedEpisodes();
        } else {
            if (!accessToken.getMessage().equals("Authorization pending")
                    && !accessToken.getMessage().equals("Slow down")) {
                LOG.error("Unexpected error did arrive, please reload the service :-(");
                tokenTimer.cancel();

                System.exit(1);
            }
        }
    }

    private void storeAccessToken() {
        try {
            File storeToken = new File(tokenFile);
            FileOutputStream fileOutputStream = new FileOutputStream(storeToken);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(accessToken);
            objectOutputStream.close();
            fileOutputStream.close();

            LOG.info("AccessToken store successfully inside a file...");
        } catch (Exception e) {
            LOG.error("Unexpected error did arrive when trying to store the AccessToken in a file ");
            LOG.error(e.getMessage());

            System.exit(1);
        }
    }

    private void processWatchedEpisodes() {
        pmsTemplate = new RestTemplate();
        String watchHistoryUrl = pmsHost + PMS_WATCH_HISTORY;

        if (pmsToken != null && !pmsToken.isEmpty()) {
            watchHistoryUrl += "?X-Plex-Token=" + pmsToken;
            LOG.info("Calling Plex with a X-Plex-Token...");
        }

        ResponseEntity<MediaContainer> response =  pmsTemplate.getForEntity(watchHistoryUrl, MediaContainer.class);
        MediaContainer mediaContainer = response.getBody();

        for (Video video : mediaContainer.getVideo()) {
            LocalDateTime date = DateUtils.getDateTimeFromTimestamp(video.getViewedAt());

            // Mark as watched only today and yesterday episodes
            if (DateUtils.isTodayOrYesterday(date)) {
                if (video.getType().equals("episode")) {
                    String episode = new StringBuilder(video.getGrandparentTitle())
                            .append(" - S")
                            .append(video.getParentIndex())
                            .append("E").append(video.getIndex())
                            .toString();

                    markEpisodeAsWatched(episode);
                } else {
                    continue;
                }
            } else {
                LOG.info("All episodes are processed successfully ...");
                System.exit(0);
            }
        }
    }

    private void markEpisodeAsWatched(String episode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("User-Agent", TVST_USER_AGENT);
        HttpEntity<String> entity = new HttpEntity<>("filename=" + episode, headers);

        String checkinUrl = new StringBuilder(TVST_CHECKIN_URI)
                .append("?access_token=")
                .append(accessToken.getAccess_token())
                .toString();

        ResponseEntity<Message> content = tvShowTimeTemplate.exchange(checkinUrl, POST, entity, Message.class);
        Message message = content.getBody();

        if (message.getResult().equals("OK")) {
            LOG.info("Mark " + episode + " as watched in TVShowTime");
        } else {
            LOG.error("Error while marking [" + episode + "] as watched in TVShowTime ");
        }

        // Check if we are below the Rate-Limit of the API
        int remainingApiCalls = Integer.parseInt(content.getHeaders().get(TVST_RATE_REMAINING_HEADER).get(0));
        if (remainingApiCalls == 0) {
            try {
                LOG.info("Consumed all available TVShowTime API calls slots, waiting for new slots ...");
                Thread.sleep(MINUTE_IN_MILIS);
            } catch (Exception e) {
                LOG.error(e.getMessage());

                System.exit(1);
            }
        }
    }
}

package org.nuvola.tvshowtime;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.nuvola.tvshowtime.business.tvshowtime.AccessToken;
import org.nuvola.tvshowtime.config.TVShowTimeConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.times;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TvTimeServiceTest extends TestBase {

    @Spy
    private RestTemplate m_restTemplate = new RestTemplate();

    @Spy
    private AccessToken m_accessToken;

    @Spy
    private Timer m_timer;

    @Spy
    private TVShowTimeConfig m_tvShowTimeConfig;

    @Mock
    private PlexService m_plexService;

    @Spy
    @InjectMocks
    private TvTimeService m_tvTimeService;
    private MockRestServiceServer mockServer = MockRestServiceServer.createServer(m_restTemplate);

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRequestAccessToken() throws Exception {
        String tvTimeResponse = readFile("tvtime_request_device_code.json");

        mockServer.expect(requestTo("https://api.tvtime.com/v1/oauth/device/code"))
                .andRespond(withSuccess(tvTimeResponse, MediaType.APPLICATION_JSON));

        m_tvTimeService.requestAccessToken();
    }

    @Test
    public void markEpisodeEmptySet() {
        when(m_accessToken.getAccess_token()).thenReturn("TokenTvTime");

        m_tvTimeService.markEpisodesAsWatched(Collections.emptySet());
    }

    @Test
    public void markEpisode() {
        String tvTimeResponse = readFile("tvtime_episode_not_found.json");

        Set<String> episodes = new HashSet<>(Arrays.asList("episode1", "episode2"));

        when(m_accessToken.getAccess_token()).thenReturn("TokenTvTime");
        mockServer.expect(times(2),
                requestTo("https://api.tvtime.com/v1/checkin?access_token=TokenTvTime"))
                .andRespond(withSuccess(tvTimeResponse, MediaType.APPLICATION_JSON));

        m_tvTimeService.markEpisodesAsWatched(episodes);
    }

    @Test
    public void markEpisodeSuccess() {
        String tvTimeResponse = readFile("tvtime_ok_response.json");

        Set<String> episodes = new HashSet<>(Arrays.asList("episode1", "episode2"));

        when(m_accessToken.getAccess_token()).thenReturn("TokenTvTime");
        mockServer.expect(times(2),
                requestTo("https://api.tvtime.com/v1/checkin?access_token=TokenTvTime"))
                .andRespond(withSuccess(tvTimeResponse, MediaType.APPLICATION_JSON));

        m_tvTimeService.markEpisodesAsWatched(episodes);
    }

    @Test
    public void markEpisodeTestRateLimit() {
        String tv_rateLimit = readFile("tvtime_rate_limit_exceeded.json");
        String tvTimeResponse = readFile("tvtime_ok_response.json");

        Set<String> episodes = new HashSet<>(Arrays.asList("episode1", "episode2"));

        long reset = System.currentTimeMillis();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-RateLimit-Limit", "10");
        headers.set("X-RateLimit-Remaining", "0");
        headers.set("X-RateLimit-Reset", Long.toString((reset / 1000) + 15));
        headers.set(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.APPLICATION_JSON));
        mockServer.expect(times(2), requestTo("https://api.tvtime.com/v1/checkin?access_token=TokenTvTime"))
                .andRespond(withStatus(HttpStatus.OK).headers(headers).body(tvTimeResponse));

        when(m_accessToken.getAccess_token()).thenReturn("TokenTvTime");

        m_tvTimeService.markEpisodesAsWatched(episodes);
    }

    @Test
    public void loadAccessToken() throws Exception {
        String tvTimeResponse = readFile("tvtime_access_token.json");

        when(m_accessToken.getAccess_token()).thenReturn("TokenTvTime");
        when(m_tvShowTimeConfig.getTokenFile()).thenReturn("TokenFile");

        mockServer.expect(requestTo("https://api.tvtime.com/v1/oauth/access_token"))
                .andRespond(withSuccess(tvTimeResponse, MediaType.APPLICATION_JSON));


        m_tvTimeService.loadAccessToken("tok");

        verify(m_plexService).getWatchedOnPlex();
    }

}

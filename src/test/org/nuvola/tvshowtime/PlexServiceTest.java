package org.nuvola.tvshowtime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.nuvola.tvshowtime.config.PMSConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PlexServiceTest extends TestBase {

    @Spy
    private PMSConfig m_pmsConfig;

    @Spy
    private RestTemplate m_restTemplate = new RestTemplate();

    @Spy
    @InjectMocks
    private PlexService m_plexService;

    private MockRestServiceServer mockServer = MockRestServiceServer.createServer(m_restTemplate);

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getUsersFromPlex() throws IOException {

        String accounts = readFile("accounts.json");

        when(m_pmsConfig.getToken()).thenReturn("TokenPlex");
        when(m_pmsConfig.getPath()).thenReturn("http://serveripport");
        mockServer.expect(requestTo("http://serveripport/accounts?X-Plex-Token=TokenPlex"))
                .andRespond(withSuccess(accounts, MediaType.APPLICATION_JSON));

        m_plexService.getUsersInPlex();

        assertThat("Users is not empty", !m_plexService.getUsers().isEmpty());
        assertNotNull("User admin should exist", m_plexService.getUsers().get("admin"));
        assertThat("User admin Map has key value 1", m_plexService.getUsers().get("admin").equals(1L));
        assertNotNull("User FirstUser should exist", m_plexService.getUsers().get("FirstUser"));
        assertThat("User FirstUser Map has key value 2", m_plexService.getUsers().get("FirstUser").equals(2L));
        assertNotNull("User SecondUser should exist", m_plexService.getUsers().get("SecondUser"));
        assertThat("User SecondUser Map has key value 3", m_plexService.getUsers().get("SecondUser").equals(3L));
    }

    @Test(expected = HttpServerErrorException.class)
    public void getUsersFromPlexFailed() throws IOException {
        when(m_pmsConfig.getPath()).thenReturn("http://serveripport");
        when(m_pmsConfig.getToken()).thenReturn("TokenPlex");
        mockServer.expect(requestTo("http://serveripport/accounts?X-Plex-Token=TokenPlex"))
                .andRespond(withServerError());

        m_plexService.getUsersInPlex();
    }

    @Test
    public void getWatchedEpisodesForToday() throws IOException {
        Set<String> expectation = new HashSet<>(
                Arrays.asList("Marvel's Agent Carter - S1E6",
                        "Marvel's Agent Carter - S1E8",
                        "Marvel's Agent Carter - S1E7")
        );

        String accounts = readFile("accounts.json");
        String metadata = readFile("metadata_2.json");
        String currentTime = String.valueOf(System.currentTimeMillis());
        metadata = metadata.replaceAll("SYSDATE", currentTime.substring(0, currentTime.length() - 3));

        when(m_pmsConfig.getToken()).thenReturn("TokenPlex");
        when(m_pmsConfig.getPath()).thenReturn("http://serveripport");
        mockServer.expect(requestTo("http://serveripport/accounts?X-Plex-Token=TokenPlex"))
                .andRespond(withSuccess(accounts, MediaType.APPLICATION_JSON));

        when(m_pmsConfig.getPath()).thenReturn("http://serveripport");
        mockServer.expect(requestTo("http://serveripport/status/sessions/history/all?X-Plex-Token=TokenPlex"))
                .andRespond(withSuccess(metadata, MediaType.APPLICATION_JSON));

        Set<String> episodes = m_plexService.getWatchedOnPlex();

        assertEquals(expectation, episodes);
    }

    @Test
    public void getWatchedEpisodesWithMarkAll() throws IOException {
        Set<String> expectation = new HashSet<>(
                Arrays.asList(
                        "Marvel's Agent Carter - S1E1",
                        "Marvel's Agent Carter - S1E6",
                        "Marvel's Agent Carter - S1E8",
                        "Marvel's Agent Carter - S1E7"
                )
        );

        String accounts = readFile("accounts.json");
        String metadata = readFile("metadata_2.json");
        String currentTime = String.valueOf(System.currentTimeMillis());
        metadata = metadata.replaceAll("SYSDATE", currentTime.substring(0, currentTime.length() - 3));

        when(m_pmsConfig.getToken()).thenReturn("TokenPlex");
        when(m_pmsConfig.getPath()).thenReturn("http://serveripport");
        mockServer.expect(requestTo("http://serveripport/accounts?X-Plex-Token=TokenPlex"))
                .andRespond(withSuccess(accounts, MediaType.APPLICATION_JSON));

        when(m_pmsConfig.getPath()).thenReturn("http://serveripport");
        when(m_pmsConfig.getMarkall()).thenReturn(true);
        mockServer.expect(requestTo("http://serveripport/status/sessions/history/all?X-Plex-Token=TokenPlex"))
                .andRespond(withSuccess(metadata, MediaType.APPLICATION_JSON));

        Set<String> episodes = m_plexService.getWatchedOnPlex();

        assertEquals(expectation, episodes);
    }

    @Test
    public void getWatchedEpisodesForTodayForAdminUser() throws IOException {
        Set<String> expectation = new HashSet<>(
                Arrays.asList("Marvel's Agent Carter - S1E6",
                        "Marvel's Agent Carter - S1E8")
        );

        String metadata = readFile("metadata_2.json");
        String currentTime = String.valueOf(System.currentTimeMillis());
        metadata = metadata.replaceAll("SYSDATE", currentTime.substring(0, currentTime.length() - 3));
        String accounts = readFile("accounts.json");

        when(m_pmsConfig.getToken()).thenReturn("TokenPlex");
        when(m_pmsConfig.getPath()).thenReturn("http://serveripport");
        mockServer.expect(requestTo("http://serveripport/accounts?X-Plex-Token=TokenPlex"))
                .andRespond(withSuccess(accounts, MediaType.APPLICATION_JSON));

        when(m_pmsConfig.getPath()).thenReturn("http://serveripport");
        when(m_pmsConfig.getUsername()).thenReturn("admin");
        mockServer.expect(requestTo("http://serveripport/status/sessions/history/all?X-Plex-Token=TokenPlex"))
                .andRespond(withSuccess(metadata, MediaType.APPLICATION_JSON));

        Set<String> episodes = m_plexService.getWatchedOnPlex();

        assertEquals(expectation, episodes);
    }

    @Test
    public void getWatchedEpisodesForAdminUserWithMarkAll() throws IOException {
        Set<String> expectation = new HashSet<>(
                Arrays.asList(
                        "Marvel's Agent Carter - S1E1",
                        "Marvel's Agent Carter - S1E6",
                        "Marvel's Agent Carter - S1E8")
        );

        String accounts = readFile("accounts.json");
        String metadata = readFile("metadata_2.json");
        String currentTime = String.valueOf(System.currentTimeMillis());
        metadata = metadata.replaceAll("SYSDATE", currentTime.substring(0, currentTime.length() - 3));

        when(m_pmsConfig.getToken()).thenReturn("TokenPlex");
        when(m_pmsConfig.getPath()).thenReturn("http://serveripport");
        mockServer.expect(requestTo("http://serveripport/accounts?X-Plex-Token=TokenPlex"))
                .andRespond(withSuccess(accounts, MediaType.APPLICATION_JSON));

        when(m_pmsConfig.getPath()).thenReturn("http://serveripport");
        when(m_pmsConfig.getUsername()).thenReturn("admin");
        when(m_pmsConfig.getMarkall()).thenReturn(true);
        mockServer.expect(requestTo("http://serveripport/status/sessions/history/all?X-Plex-Token=TokenPlex"))
                .andRespond(withSuccess(metadata, MediaType.APPLICATION_JSON));

        Set<String> episodes = m_plexService.getWatchedOnPlex();

        assertEquals(expectation, episodes);
    }

    @Test
    public void getWatchedEpisodesForTodayForDifferentUser() throws IOException {
        Set<String> expectation = new HashSet<>(
                Collections.singletonList("Marvel's Agent Carter - S1E7")
        );

        String accounts = readFile("accounts.json");
        String metadata = readFile("metadata_2.json");
        String currentTime = String.valueOf(System.currentTimeMillis());
        metadata = metadata.replaceAll("SYSDATE", currentTime.substring(0, currentTime.length() - 3));

        when(m_pmsConfig.getToken()).thenReturn("TokenPlex");
        when(m_pmsConfig.getPath()).thenReturn("http://serveripport");
        mockServer.expect(requestTo("http://serveripport/accounts?X-Plex-Token=TokenPlex"))
                .andRespond(withSuccess(accounts, MediaType.APPLICATION_JSON));

        when(m_pmsConfig.getPath()).thenReturn("http://serveripport");
        when(m_pmsConfig.getUsername()).thenReturn("FirstUser");
        mockServer.expect(requestTo("http://serveripport/status/sessions/history/all?X-Plex-Token=TokenPlex"))
                .andRespond(withSuccess(metadata, MediaType.APPLICATION_JSON));

        Set<String> episodes = m_plexService.getWatchedOnPlex();

        assertEquals(expectation, episodes);
    }
}

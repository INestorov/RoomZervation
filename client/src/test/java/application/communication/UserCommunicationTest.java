package application.communication;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.tomakehurst.wiremock.WireMockServer;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserCommunicationTest {

    private WireMockServer wireMockServer;
    private HttpClient client;

    @BeforeEach
    void setup() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        wireMockServer = new WireMockServer(wireMockConfig().httpsPort(8443));
        wireMockServer.start();
        client = HttpClients
            .custom()
            .setSSLContext(new SSLContextBuilder()
                .loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
            .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
            .build();
        ServerCommunication.setClient(client);
    }

    @AfterEach
    void teardown() {
        wireMockServer.stop();
    }

    @Test
    void deleteUserTest() {
        wireMockServer.givenThat(delete(urlEqualTo("/users/delete/1"))
            .willReturn(aResponse().withBody("[]")));

        assertTrue(ServerCommunication.deleteUser(1));
    }

    @Test
    void recovPassTest() {

        wireMockServer.stubFor(get(urlEqualTo("/mail/send/one"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "text/plain")
                .withBody("ok")));

        assertEquals(ServerCommunication.recovPass("one"), "ok");
    }


    @Test
    void getUserTypeTest() {

        wireMockServer.stubFor(get(urlEqualTo("/users/type/dummy"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "text/plain")
                .withBody("admin")));

        assertEquals(ServerCommunication.getUserType("dummy"), "admin");
    }

    @Test
    void loginTest() throws IOException {
        wireMockServer.givenThat(post(urlEqualTo("/login"))
            .willReturn(aResponse().withBody("{name: \"EWI\", openingTime: \"16:00:00\", "
                + "closingTime: \"18:00:00\"}"))
        );
        assertNotNull(ServerCommunication.login("dummy", "password"));
    }

    @Test
    void registerTest() throws IOException {
        wireMockServer.givenThat(post(urlEqualTo("/register"))
            .willReturn(aResponse().withBody("{name: \"EWI\", openingTime: \"16:00:00\", "
                + "closingTime: \"18:00:00\"}"))
        );
        assertNotNull(ServerCommunication.register(1,"dummy", "password","33983","user"));
    }

    @Test
    void getUserIdTest() {

        wireMockServer.stubFor(get(urlEqualTo("/users/id/dummy"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "text/plain")
                .withBody("1")));

        assertEquals(ServerCommunication.getUserId("dummy"), 1);
    }

    @Test
    void checkKeyTest() {

        wireMockServer.stubFor(get(urlEqualTo("/mail/check/key"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "text/plain")
                .withBody("1")));

        assertEquals(ServerCommunication.checkKey("key"),"1");
    }

    @Test
    void getHolidaysTest() {

        wireMockServer.stubFor(get(urlEqualTo("/holidays/read"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "text/plain")
                .withBody("1")));

        assertEquals(ServerCommunication.getHolidays(),"1");
    }

    @Test
    void changePasswordTest() {
        wireMockServer.givenThat(put(urlEqualTo("/update/password"))
            .willReturn(aResponse().withStatus(200))
        );

        assertNotNull(ServerCommunication.changePassword("dummy", "hello"));
    }

    @Test
    void authorizeUserTest() throws Exception {
        wireMockServer.givenThat(put(urlEqualTo("/users/upgrade"))
            .willReturn(aResponse().withStatus(200))
        );

        assertNotNull(ServerCommunication.authorizeUser(1, "hello"));
    }
}
package application.communication;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import application.entities.Building;
import com.github.tomakehurst.wiremock.WireMockServer;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FacilityCommunicationTest {

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
    void addFacilityTest() {
        wireMockServer.givenThat(post(urlEqualTo("/facilities/post"))
            .willReturn(aResponse().withStatus(200))
        );
        List<Integer> ints = new ArrayList();
        ints.add(1);
        assertTrue(ServerCommunication.addFacility("whiteboard", ints));
    }

    @Test
    void deleteFacilityTest() {

        wireMockServer.givenThat(delete(urlEqualTo("/facilities/delete/1"))
            .willReturn(aResponse().withBody("[]")));

        assertTrue(ServerCommunication.deleteFacility(1));

    }

    @Test
    void getFacilityByRoomTest() {
        wireMockServer.stubFor(get(urlEqualTo("/rooms/read/facilities/1"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("[\n"
                    + "    {\n"
                    + "        \"id\": 1,\n"
                    + "        \"name\": \"Aerospace Engineering\",\n"
                    + "        \"openingTime\": \"10:25:50\",\n"
                    + "        \"closingTime\": \"19:19:40\"\n"
                    + "    }]")));

        assertEquals(ServerCommunication.getFacilityByRoom(1), "[\n"
            + "    {\n"
            + "        \"id\": 1,\n"
            + "        \"name\": \"Aerospace Engineering\",\n"
            + "        \"openingTime\": \"10:25:50\",\n"
            + "        \"closingTime\": \"19:19:40\"\n"
            + "    }]");
    }

    @Test
    void getFacilityTest() {
        wireMockServer.stubFor(get(urlEqualTo("/facilities/read"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("[\n"
                    + "    {\n"
                    + "        \"id\": 1,\n"
                    + "        \"name\": \"Aerospace Engineering\",\n"
                    + "        \"openingTime\": \"10:25:50\",\n"
                    + "        \"closingTime\": \"19:19:40\"\n"
                    + "    }]")));

        assertEquals(ServerCommunication.getFacility(), "[\n"
            + "    {\n"
            + "        \"id\": 1,\n"
            + "        \"name\": \"Aerospace Engineering\",\n"
            + "        \"openingTime\": \"10:25:50\",\n"
            + "        \"closingTime\": \"19:19:40\"\n"
            + "    }]");
    }


}

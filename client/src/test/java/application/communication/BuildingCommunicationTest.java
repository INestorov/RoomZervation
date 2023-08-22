package application.communication;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.tomakehurst.wiremock.WireMockServer;
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

public class BuildingCommunicationTest {

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
    void addBuildingTest() {
        wireMockServer.givenThat(post(urlEqualTo("/buildings/post"))
            .willReturn(aResponse().withBody("{name: \"EWI\", openingTime: \"16:00:00\", "
                + "closingTime: \"18:00:00\"}"))
        );
        assertTrue(ServerCommunication.addBuilding("EWI", "16:00", "18:00"));
    }

    @Test
    void getBuildingsTest() {
        wireMockServer.stubFor(get(urlEqualTo("/buildings/read"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("[\n"
                    + "    {\n"
                    + "        \"id\": 1,\n"
                    + "        \"name\": \"Aerospace Engineering\",\n"
                    + "        \"openingTime\": \"10:25:50\",\n"
                    + "        \"closingTime\": \"19:19:40\"\n"
                    + "    }]")));

        assertEquals(ServerCommunication.getBuildings(), "[\n"
            + "    {\n"
            + "        \"id\": 1,\n"
            + "        \"name\": \"Aerospace Engineering\",\n"
            + "        \"openingTime\": \"10:25:50\",\n"
            + "        \"closingTime\": \"19:19:40\"\n"
            + "    }]");
    }

    @Test
    void updateBuildingTest() {
        wireMockServer.givenThat(put(urlEqualTo("/buildings/update/1"))
            .willReturn(aResponse().withBody("{name: \"EWI\", openingTime: \"16:00:00\", "
                + "closingTime: \"18:00:00\"}"))
        );
        assertTrue(ServerCommunication.updateBuilding("1","EWI", "16:00", "18:00"));
    }

    @Test
    void deleteBuildingTest() {

        wireMockServer.givenThat(delete(urlEqualTo("/buildings/delete/1"))
                .willReturn(aResponse().withBody("[]")));

        assertTrue(ServerCommunication.deleteBuilding(1));

    }

    @Test
    void getBuildingByIdTest() {
        wireMockServer.stubFor(get(urlEqualTo("/buildings/read/1"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\n"
                    + "        \"id\": 1,\n"
                    + "        \"name\": \"Aerospace Engineering\",\n"
                    + "        \"openingTime\": \"10:25:50\",\n"
                    + "        \"closingTime\": \"19:19:40\"\n"
                    + "    }")));

        assertEquals(ServerCommunication.getBuildingById(1), "{\n"
            + "        \"id\": 1,\n"
                + "        \"name\": \"Aerospace Engineering\",\n"
                + "        \"openingTime\": \"10:25:50\",\n"
                + "        \"closingTime\": \"19:19:40\"\n"
                + "    }");

    }


}

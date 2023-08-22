package application.communication;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

public class BikeCommunicationTest {

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
    void addBikeTest() {
        wireMockServer.stubFor(get(urlEqualTo("/buildings/read/1"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\n"
                    + "        \"id\": 1,\n"
                    + "        \"name\": \"Aerospace Engineering\",\n"
                    + "        \"openingTime\": \"10:25:50\",\n"
                    + "        \"closingTime\": \"19:19:40\"\n"
                    + "    }")));

        wireMockServer.givenThat(post(urlEqualTo("/bikes/post"))
            .willReturn(aResponse().withStatus(200))
        );
        assertTrue(ServerCommunication.addBike(1));
    }

    @Test
    void deleteBikeTest() {

        wireMockServer.givenThat(delete(urlEqualTo("/bikes/delete/1"))
            .willReturn(aResponse().withBody("[]")));

        assertTrue(ServerCommunication.deleteBike(1));

    }

    @Test
    void getLateBikesTest() {
        wireMockServer.stubFor(get(urlEqualTo("/bikeRentals/read/late"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\n"
                    + "        \"id\": 1,\n"
                    + "        \"building\": \"Aerospace Engineering\",\n"
                    + "    }")));

        assertEquals(ServerCommunication.getLateBikes(), "{\n"
            + "        \"id\": 1,\n"
            + "        \"building\": \"Aerospace Engineering\",\n"
            + "    }");
    }

    @Test
    void getBikesWithBuildingTest() {
        wireMockServer.stubFor(get(urlEqualTo("/bikes/read/withBuilding"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\n"
                    + "        \"id\": 1,\n"
                    + "        \"building\": \"Aerospace Engineering\",\n"
                    + "    }")));

        assertEquals(ServerCommunication.getBikesWithBuilding(), "{\n"
            + "        \"id\": 1,\n"
            + "        \"building\": \"Aerospace Engineering\",\n"
            + "    }");
    }


    @Test
    void getAllBikesTest() {
        wireMockServer.stubFor(get(urlEqualTo("/bikes/read"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\n"
                    + "        \"id\": 1,\n"
                    + "        \"building\": \"Aerospace Engineering\",\n"
                    + "    }")));

        assertEquals(ServerCommunication.getAllBikes(), "{\n"
            + "        \"id\": 1,\n"
            + "        \"building\": \"Aerospace Engineering\",\n"
            + "    }");
    }

    @Test
    void getAvailableBikesNumberByBuildingIdTest() {
        wireMockServer.stubFor(get(urlEqualTo("/bikes/read/number/1"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\n"
                    + "        \"id\": 1,\n"
                    + "        \"building\": \"Aerospace Engineering\",\n"
                    + "    }")));

        assertEquals(ServerCommunication.getAvailableBikesNumberByBuildingId(1), "{\n"
            + "        \"id\": 1,\n"
            + "        \"building\": \"Aerospace Engineering\",\n"
            + "    }");
    }

    @Test
    void getBikesByBuildingIdTest() {
        wireMockServer.stubFor(get(urlEqualTo("/bikes/read/building/1"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\n"
                    + "        \"id\": 1,\n"
                    + "        \"building\": \"Aerospace Engineering\",\n"
                    + "    }")));

        assertEquals(ServerCommunication.getBikesByBuildingId(1), "{\n"
            + "        \"id\": 1,\n"
            + "        \"building\": \"Aerospace Engineering\",\n"
            + "    }");
    }

    @Test
    void rentBikeTest() throws IOException {
        wireMockServer.givenThat(post(urlEqualTo("/bikes/rent"))
            .willReturn(aResponse().withBody("{name: \"EWI\", openingTime: \"16:00:00\", "
                + "closingTime: \"18:00:00\"}"))
        );
        assertNotNull(ServerCommunication.rentBike(1,1, System.currentTimeMillis()));
    }

    @Test
    void isBikeInUseTest() {
        wireMockServer.stubFor(get(urlEqualTo("/bikes/read/isUser"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\n"
                    + "        \"id\": 1,\n"
                    + "        \"building\": \"Aerospace Engineering\",\n"
                    + "    }")));
        assertFalse(ServerCommunication.isBikeInUse());
    }

    @Test
    void getBikeByUserIdTest() {
        wireMockServer.stubFor(get(urlEqualTo("/bikes/read/user"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\n"
                    + "        \"id\": 1,\n"
                    + "        \"building\": \"Aerospace Engineering\",\n"
                    + "    }")));
        assertNotNull(ServerCommunication.getBikeByUserId());
    }

    @Test
    void returnBikeTest() throws IOException {
        wireMockServer.givenThat(post(urlEqualTo("/bikes/update/return"))
            .willReturn(aResponse().withBody("{name: \"EWI\", openingTime: \"16:00:00\", "
                + "closingTime: \"18:00:00\"}"))
        );
        assertTrue(ServerCommunication.returnBike(1,1));
    }

    @Test
    void updateBikeTest() {
        wireMockServer.stubFor(get(urlEqualTo("/buildings/read/1"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\n"
                    + "        \"id\": 1,\n"
                    + "        \"name\": \"Aerospace Engineering\",\n"
                    + "        \"openingTime\": \"10:25:50\",\n"
                    + "        \"closingTime\": \"19:19:40\"\n"
                    + "    }")));

        wireMockServer.givenThat(put(urlEqualTo("/bikes/update/1"))
            .willReturn(aResponse().withStatus(200))
        );
        assertTrue(ServerCommunication.updateBike(1,1));
    }

}

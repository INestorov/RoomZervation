package application.communication;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import application.entities.Room;
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

public class ReservationCommunicationTest {

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
    void getReservationsTest() {
        wireMockServer.stubFor(get(urlEqualTo("/reservations/read"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("[\n"
                    + "    {\n"
                    + "        \"id\": 229,\n"
                    + "        \"room\": {\n"
                    + "            \"id\": 1,\n"
                    + "            \"building\": {\n"
                    + "                \"id\": 2,\n"
                    + "                \"name\": \"Applied Physics\",\n"
                    + "                \"openingTime\": \"10:53:20\",\n"
                    + "                \"closingTime\": \"18:48:36\"\n"
                    + "            },\n"
                    + "            \"name\": \"Oceania\",\n"
                    + "            \"size\": 12,\n"
                    + "            \"facilities\": [\n"
                    + "                {\n"
                    + "                    \"id\": 1,\n"
                    + "                    \"name\": \"Projector\"\n"
                    + "                },\n"
                    + "                {\n"
                    + "                    \"id\": 2,\n"
                    + "                    \"name\": \"White Board\"\n"
                    + "                },\n"
                    + "                {\n"
                    + "                    \"id\": 3,\n"
                    + "                    \"name\": \"Smart Board\"\n"
                    + "                }\n"
                    + "            ]\n"
                    + "        },\n"
                    + "        \"user\": {\n"
                    + "            \"id\": 1,\n"
                    + "            \"username\": \"srollin\",\n"
                    + "            \"password\": \"96e569809d9725623ebb4fb44b5c5d49d1a5f3609b0"
                    + "335b4f1cd5424a6565baedb4ca9aea989493f\",\n"
                    + "            \"type\": \"Student\"\n"
                    + "        },\n"
                    + "        \"start\": \"2020-03-15T10:53:00.000+0000\",\n"
                    + "        \"end\": \"2020-03-15T12:00:00.000+0000\",\n"
                    + "        \"userId\": null,\n"
                    + "        \"roomId\": null\n"
                    + "    }\n"
                    + "]")));

        assertEquals(ServerCommunication.getReservations(), "[\n"
            + "    {\n"
            + "        \"id\": 229,\n"
            + "        \"room\": {\n"
            + "            \"id\": 1,\n"
            + "            \"building\": {\n"
            + "                \"id\": 2,\n"
            + "                \"name\": \"Applied Physics\",\n"
            + "                \"openingTime\": \"10:53:20\",\n"
            + "                \"closingTime\": \"18:48:36\"\n"
            + "            },\n"
            + "            \"name\": \"Oceania\",\n"
            + "            \"size\": 12,\n"
            + "            \"facilities\": [\n"
            + "                {\n"
            + "                    \"id\": 1,\n"
            + "                    \"name\": \"Projector\"\n"
            + "                },\n"
            + "                {\n"
            + "                    \"id\": 2,\n"
            + "                    \"name\": \"White Board\"\n"
            + "                },\n"
            + "                {\n"
            + "                    \"id\": 3,\n"
            + "                    \"name\": \"Smart Board\"\n"
            + "                }\n"
            + "            ]\n"
            + "        },\n"
            + "        \"user\": {\n"
            + "            \"id\": 1,\n"
            + "            \"username\": \"srollin\",\n"
            + "            \"password\": \"96e569809d9725623ebb4fb44b5c5d49d1a5f3609b0"
            + "335b4f1cd5424a6565baedb4ca9aea989493f\",\n"
            + "            \"type\": \"Student\"\n"
            + "        },\n"
            + "        \"start\": \"2020-03-15T10:53:00.000+0000\",\n"
            + "        \"end\": \"2020-03-15T12:00:00.000+0000\",\n"
            + "        \"userId\": null,\n"
            + "        \"roomId\": null\n"
            + "    }\n"
            + "]");
    }

    @Test
    void getReservationTest() {
        wireMockServer.stubFor(get(urlEqualTo("/reservations/read/id/1"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\n"
                    + "        \"id\": 229,\n"
                    + "        \"room\": {\n"
                    + "            \"id\": 1,\n"
                    + "            \"building\": {\n"
                    + "                \"id\": 2,\n"
                    + "                \"name\": \"Applied Physics\",\n"
                    + "                \"openingTime\": \"10:53:20\",\n"
                    + "                \"closingTime\": \"18:48:36\"\n"
                    + "            },\n"
                    + "            \"name\": \"Oceania\",\n"
                    + "            \"size\": 12,\n"
                    + "            \"facilities\": [\n"
                    + "                {\n"
                    + "                    \"id\": 1,\n"
                    + "                    \"name\": \"Projector\"\n"
                    + "                },\n"
                    + "                {\n"
                    + "                    \"id\": 2,\n"
                    + "                    \"name\": \"White Board\"\n"
                    + "                },\n"
                    + "                {\n"
                    + "                    \"id\": 3,\n"
                    + "                    \"name\": \"Smart Board\"\n"
                    + "                }\n"
                    + "            ]\n"
                    + "        },\n"
                    + "        \"user\": {\n"
                    + "            \"id\": 1,\n"
                    + "            \"username\": \"srollin\",\n"
                    + "            \"password\": \"96e569809d9725623ebb4fb44b5c5d49d1a5f3609b0"
                    + "335b4f1cd5424a6565baedb4ca9aea989493f\",\n"
                    + "            \"type\": \"Student\"\n"
                    + "        },\n"
                    + "        \"start\": \"2020-03-15T10:53:00.000+0000\",\n"
                    + "        \"end\": \"2020-03-15T12:00:00.000+0000\",\n"
                    + "        \"userId\": null,\n"
                    + "        \"roomId\": null\n"
                    + "    }")));

        assertEquals(ServerCommunication.getReservation(1), "{\n"
            + "        \"id\": 229,\n"
            + "        \"room\": {\n"
            + "            \"id\": 1,\n"
            + "            \"building\": {\n"
            + "                \"id\": 2,\n"
            + "                \"name\": \"Applied Physics\",\n"
            + "                \"openingTime\": \"10:53:20\",\n"
            + "                \"closingTime\": \"18:48:36\"\n"
            + "            },\n"
            + "            \"name\": \"Oceania\",\n"
            + "            \"size\": 12,\n"
            + "            \"facilities\": [\n"
            + "                {\n"
            + "                    \"id\": 1,\n"
            + "                    \"name\": \"Projector\"\n"
            + "                },\n"
            + "                {\n"
            + "                    \"id\": 2,\n"
            + "                    \"name\": \"White Board\"\n"
            + "                },\n"
            + "                {\n"
            + "                    \"id\": 3,\n"
            + "                    \"name\": \"Smart Board\"\n"
            + "                }\n"
            + "            ]\n"
            + "        },\n"
            + "        \"user\": {\n"
            + "            \"id\": 1,\n"
            + "            \"username\": \"srollin\",\n"
            + "            \"password\": \"96e569809d9725623ebb4fb44b5c5d49d1a5f3609b0"
            + "335b4f1cd5424a6565baedb4ca9aea989493f\",\n"
            + "            \"type\": \"Student\"\n"
            + "        },\n"
            + "        \"start\": \"2020-03-15T10:53:00.000+0000\",\n"
            + "        \"end\": \"2020-03-15T12:00:00.000+0000\",\n"
            + "        \"userId\": null,\n"
            + "        \"roomId\": null\n"
            + "    }");
    }

    @Test
    void deleteReservationTest() {

        wireMockServer.givenThat(delete(urlEqualTo("/reservations/delete/1"))
            .willReturn(aResponse().withBody("[]")));

        assertTrue(ServerCommunication.deleteReservation(1));

    }

    @Test
    void addReservationTest() {

        wireMockServer.stubFor(get(urlEqualTo("/users/id/dummy"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "text/plain")
                .withBody("1")));

        wireMockServer.givenThat(post(urlEqualTo("/reservations/post"))
            .willReturn(aResponse().withBody("true"))
        );

        Room room = new Room(1, "Oceania", "Applied Physics", 12);
        assertEquals(ServerCommunication.addReservation(room, "2020-03-15 10:53:00",
            "2020-03-15 12:00:00", "dummy"), 1);

    }

    @Test
    void getReservationByUsernameTest() {

        wireMockServer.stubFor(get(urlEqualTo("/reservations/read1/dummy"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "text/plain")
                .withBody("admin")));

        assertEquals(ServerCommunication.getReservationByUsername("dummy"), "admin");
    }

    @Test
    void getReservationSpecificTest() {

        wireMockServer.stubFor(get(urlEqualTo("/reservations/read/dummy"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "text/plain")
                .withBody("admin")));

        assertEquals(ServerCommunication.getReservationSpecific("dummy"), "admin");
    }
}

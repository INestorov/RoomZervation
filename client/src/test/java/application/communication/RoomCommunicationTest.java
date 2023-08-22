package application.communication;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import application.entities.Room;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

public class RoomCommunicationTest {

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
    void addRoomTest() {
        wireMockServer.givenThat(post(urlEqualTo("/rooms/post"))
            .willReturn(aResponse().withBody("{\n"
                + "\t\"building\": {\n"
                + "            \"id\": 2,\n"
                + "            \"name\": \"Applied Physics\",\n"
                + "            \"openingTime\": \"10:53:20\",\n"
                + "            \"closingTime\": \"18:48:36\"\n"
                + "        },\n"
                + "        \"name\": \"Oceania\",\n"
                + "        \"size\": 12\n"
                + "}"))
        );
        assertTrue(ServerCommunication.addRoom(2, "Oceania", 12,
            "nice"));

    }

    @Test
    void getRoomsTest() {
        wireMockServer.stubFor(get(urlEqualTo("/rooms/read/0/10000"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("[{\n"
                    + "\t\"building\": {\n"
                    + "            \"id\": 2,\n"
                    + "            \"name\": \"Applied Physics\",\n"
                    + "            \"openingTime\": \"10:53:20\",\n"
                    + "            \"closingTime\": \"18:48:36\"\n"
                    + "        },\n"
                    + "        \"name\": \"Oceania\",\n"
                    + "        \"size\": 12\n"
                    + "}]")));

        assertEquals(ServerCommunication.getRooms(), "[{\n"
            + "\t\"building\": {\n"
            + "            \"id\": 2,\n"
            + "            \"name\": \"Applied Physics\",\n"
            + "            \"openingTime\": \"10:53:20\",\n"
            + "            \"closingTime\": \"18:48:36\"\n"
            + "        },\n"
            + "        \"name\": \"Oceania\",\n"
            + "        \"size\": 12\n"
            + "}]");

    }

    @Test
    void getFilteredRoomsTest() {
        wireMockServer.stubFor(get(urlEqualTo("/rooms/read/0/10000/4/5"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("[{\n"
                    + "\t\"building\": {\n"
                    + "            \"id\": 2,\n"
                    + "            \"name\": \"Applied Physics\",\n"
                    + "            \"openingTime\": \"10:53:20\",\n"
                    + "            \"closingTime\": \"18:48:36\"\n"
                    + "        },\n"
                    + "        \"name\": \"Oceania\",\n"
                    + "        \"size\": 12\n"
                    + "}]")));

        assertEquals(ServerCommunication.getFilteredRooms(0,10000,4,5), "[{\n"
            + "\t\"building\": {\n"
            + "            \"id\": 2,\n"
            + "            \"name\": \"Applied Physics\",\n"
            + "            \"openingTime\": \"10:53:20\",\n"
            + "            \"closingTime\": \"18:48:36\"\n"
            + "        },\n"
            + "        \"name\": \"Oceania\",\n"
            + "        \"size\": 12\n"
            + "}]");
    }

    @Test
    void getRoomByIdTest() {

        wireMockServer.stubFor(get(urlEqualTo("/rooms/read1/1"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\n"
                    + "\t\"building\": {\n"
                    + "            \"id\": 2,\n"
                    + "            \"name\": \"Applied Physics\",\n"
                    + "            \"openingTime\": \"10:53:20\",\n"
                    + "            \"closingTime\": \"18:48:36\"\n"
                    + "        },\n"
                    + "        \"name\": \"Oceania\",\n"
                    + "        \"size\": 12\n"
                    + "}")));

        assertEquals(ServerCommunication.getRoomById(1), "{\n"
            + "\t\"building\": {\n"
            + "            \"id\": 2,\n"
            + "            \"name\": \"Applied Physics\",\n"
            + "            \"openingTime\": \"10:53:20\",\n"
            + "            \"closingTime\": \"18:48:36\"\n"
            + "        },\n"
            + "        \"name\": \"Oceania\",\n"
            + "        \"size\": 12\n"
            + "}");

    }

    @Test
    void updateRoomTest() {
        wireMockServer.givenThat(put(urlEqualTo("/rooms/update/1"))
            .willReturn(aResponse().withBody("{\n"
                + "\t\"building\": {\n"
                + "            \"id\": 2,\n"
                + "            \"name\": \"Applied Physics\",\n"
                + "            \"openingTime\": \"10:53:20\",\n"
                + "            \"closingTime\": \"18:48:36\"\n"
                + "        },\n"
                + "        \"name\": \"Oceania\",\n"
                + "        \"size\": 12\n"
                + "}"))
        );
        assertTrue(ServerCommunication.updateRoom(1,2, "Oceania", 12,
            "nice"));
    }

    @Test
    void deleteRoomTest() {

        wireMockServer.givenThat(delete(urlEqualTo("/rooms/delete/1"))
            .willReturn(aResponse().withBody("[]")));

        assertTrue(ServerCommunication.deleteRoom(1));

    }

    @Test
    void getPictureRoomTest() {
        wireMockServer.stubFor(get(urlEqualTo("/rooms/image/1"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "image/jpeg").withBase64Body(null)));

        assertNull(ServerCommunication.getPictureRoom(1));
    }

    @Test
    void getBuildingTest() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree("{\n"
            + "        \"id\": 1,\n"
            + "        \"name\": \"Aerospace Engineering\",\n"
            + "        \"openingTime\": \"10:25:50\",\n"
            + "        \"closingTime\": \"19:19:40\"\n"
            + "    }");
        wireMockServer.stubFor(get(urlEqualTo("/buildings/read/1"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withJsonBody(jsonNode)));

        Room test = new Room(1, "Star", 1, 1, null, "nice");
        assertNotNull(test.getBuilding(1));
    }


}

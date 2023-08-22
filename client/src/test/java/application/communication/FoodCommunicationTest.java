package application.communication;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
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

public class FoodCommunicationTest {

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
    void deleteFoodTest() {

        wireMockServer.givenThat(delete(urlEqualTo("/menus/delete/1"))
            .willReturn(aResponse().withBody("[]")));

        assertTrue(ServerCommunication.deleteFood(1));

    }

    @Test
    void getFoodByRestaurantTest() {
        wireMockServer.stubFor(get(urlEqualTo("/menus/read/all/1"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "text/plain")
                .withBody("food")));
        assertEquals(ServerCommunication.getFoodByRestaurant(1), "food");
    }

    @Test
    void getFoodByIdTest() {
        wireMockServer.stubFor(get(urlEqualTo("/menus/read/1"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "text/plain")
                .withBody("food")));
        assertEquals(ServerCommunication.getFoodById(1), "food");
    }

    @Test
    void addFoodToResTest() {
        wireMockServer.stubFor(get(urlEqualTo("/restaurants/read/1"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\n"
                    + "        \"id\": 1,\n"
                    + "        \"name\": \"Aerospace Engineering\",\n"
                    + "        \"openingTime\": \"10:25:50\",\n"
                    + "        \"closingTime\": \"19:19:40\"\n"
                    + "    }")));

        wireMockServer.givenThat(post(urlEqualTo("/menus/post"))
            .willReturn(aResponse().withStatus(200))
        );
        assertTrue(ServerCommunication.addFoodToRes(1,"food","food",10.0,"food"));
    }

    @Test
    void updateFoodTest() {
        wireMockServer.stubFor(get(urlEqualTo("/restaurants/read/1"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "application/json")
                .withBody("{\n"
                    + "        \"id\": 1,\n"
                    + "        \"name\": \"Aerospace Engineering\",\n"
                    + "        \"openingTime\": \"10:25:50\",\n"
                    + "        \"closingTime\": \"19:19:40\"\n"
                    + "    }")));

        wireMockServer.givenThat(put(urlEqualTo("/menus/update/1"))
            .willReturn(aResponse().withStatus(200))
        );
        assertTrue(ServerCommunication.updateFood(1,1,"food","food","food",10.0));
    }
}

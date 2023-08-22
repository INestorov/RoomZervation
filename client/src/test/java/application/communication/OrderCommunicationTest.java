package application.communication;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

public class OrderCommunicationTest {

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
    void placeOrderTest() {
        wireMockServer.givenThat(post(urlEqualTo("orders/post"))
            .willReturn(aResponse().withBody("{name: \"EWI\", openingTime: \"16:00:00\", "
                + "closingTime: \"18:00:00\"}"))
        );
        assertNotNull(ServerCommunication.placeOrder(null));
    }

    @Test
    void getOrdersTest() {
        wireMockServer.stubFor(get(urlEqualTo("/orders/user/dummy"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "text/plain")
                .withBody("food")));
        assertEquals(ServerCommunication.getOrders("dummy"), "food");
    }

    @Test
    void getOrderByIdTest() {
        wireMockServer.stubFor(get(urlEqualTo("/orders/read/1"))
            .willReturn(aResponse()
                .withHeader("Content-Type", "text/plain")
                .withBody("food")));
        assertEquals(ServerCommunication.getOrderById(1), "food");
    }
}

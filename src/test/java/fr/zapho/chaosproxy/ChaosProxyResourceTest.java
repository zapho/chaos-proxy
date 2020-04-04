package fr.zapho.chaosproxy;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.socket.PortFactory;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@QuarkusTest
public class ChaosProxyResourceTest {

    private final RequestSpecification proxySpec = new RequestSpecBuilder().setProxy(8888).build();

    private final static int MOCK_SERVER_PORT = PortFactory.findFreePort();

    private ClientAndServer mockServer;

    @BeforeEach
    public void startMockServer() {
        mockServer = startClientAndServer(MOCK_SERVER_PORT);
    }

    @AfterEach
    public void stopMockServer() {
        mockServer.stop();
    }

    @Test
    public void when_no_configuration_http_request_is_passed_through() {
        resetConfiguration();

        mockServerResponding("/", "GET", 200);

        given()
                .spec(proxySpec)
                //                .log().all()
                .when()
                .get("http://localhost:" + MOCK_SERVER_PORT)
                .then()
                //                .log().all()
                .statusCode(200);
    }

    @Test
    public void when_blocking_configuration_http_request_gets_500_response() {
        resetConfiguration();

        mockServerResponding("/", "GET", 200);

        given()
                .when()
                .header("Content-Type", "application/json")
                .body("{ \"host\": \"localhost\",\t\"blockOutgoingRequest\":true }")
                .put("resources/chaos/conf")
                .then()
                .statusCode(200);

        given()
                .spec(proxySpec)
                //                .log().all()
                .when().get("http://localhost:" + MOCK_SERVER_PORT)
                .then()
                //                .log().all()
                .statusCode(500);
    }

    @Test
    void can_log_configuration_changes() {
        // Given
        // When
        resetConfiguration(); // this will trigger log activity in the proxy

        // Then
        // @formatter:off
        Response response = given()
            .when()
                .header("Content-Type", "application/json")
                .get("resources/chaos/logs?lvl=info");
        String content = response.getBody().asString();
        response
            .then()
                .statusCode(200);
        // @formatter:on

        assertThat(content).contains("Proxy configuration removed (all entries)");
    }

    @Test
    void can_clean_all_logs() {

        // Given
        resetConfiguration();
        // @formatter:off
        given()
            .when()
                .header("Content-Type", "application/json")
                .get("resources/chaos/logs?lvl=info")
            .then()
                .statusCode(200)
                .body("size()", is(greaterThan(0)));
        // @formatter:on

        // When
        // @formatter:off
        given()
            .when()
                .header("Content-Type", "application/json")
                .delete("resources/chaos/logs")
            .then()
                .statusCode(200);
        // @formatter:on

        // Then
        // @formatter:off
        given()
            .when()
                .header("Content-Type", "application/json")
                .get("resources/chaos/logs?lvl=info")
            .then()
                .statusCode(200)
                .body("size()", is(0));
        // @formatter:on

    }

    private void mockServerResponding(String path, String method, int statusCode) {
        mockServer
                .when(
                        request()
                                .withMethod(method)
                                .withPath(path)
                )
                .respond(
                        response()
                                .withStatusCode(statusCode)
                );
    }

    private void resetConfiguration() {
        given()
                //.log().all()
                .when().delete("resources/chaos/conf")
                .then()
                //.log().all()
                .statusCode(200);
    }

}

package com.agfa.orbis.orme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.mockserver.junit.MockServerRule;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ChaosProxyResourceTest {

    private final RequestSpecification proxySpec = new RequestSpecBuilder().setProxy(8888).build();

    private final static int MOCK_SERVER_PORT = 8889;

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, MOCK_SERVER_PORT);

    @Test
    public void when_no_configuration_http_request_is_passed_through() {
        resetConfiguration();

        given()
                .spec(proxySpec)
                .log().all()
                .when()
                .header("Accept", "text/html")
                .get("http://localhost:" + MOCK_SERVER_PORT)
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test
    public void when_blocking_configuration_http_request_gets_500_response() {
        resetConfiguration();

        given()
                .when()
                .body("{ \"host\": \"schema.org\",\t\"blockOutgoingRequest\":true }")
                .header("Accept", "text/html")
                .put("resources/chaos/conf")
                .then()
                .statusCode(200);

        given()
                .spec(proxySpec)
                .log().all()
                .when().get("http://localhost:" + MOCK_SERVER_PORT)
                .then()
                .log().all()
                .statusCode(500);
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

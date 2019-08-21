package com.agfa.orbis.orme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class PingResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("resources/ping")
          .then()
             .statusCode(200)
             .body(is("pong"));
    }

}
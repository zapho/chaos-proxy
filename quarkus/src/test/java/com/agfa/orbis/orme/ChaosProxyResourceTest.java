package com.agfa.orbis.orme;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ChaosProxyResourceTest {

    private final RequestSpecification proxySpec = new RequestSpecBuilder().setProxy(8888).build();

    @Test
    public void when_no_configuration_http_request_is_passed_through() {
        given()
                .log().all()
                .when().delete("resources/chaos/conf")
                .then()
                .log().all()
                .statusCode(200);

        given()
                .spec(proxySpec)
                .log().all()
                .when().get("http://wwww.google.com")
                .then()
                .log().all()
                .statusCode(200);
    }

}

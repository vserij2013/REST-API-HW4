package org.example;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class RestAssuredWideTest {

    final static String baseUrl = "https://reqres.in/api/users/";
    final static String resourceUrl = "https://reqres.in/api/unknown/";
    final static String registerUrl = "https://reqres.in/api/register/";
    final static String loginUrl = "https://reqres.in/api/login/";
    static RequestSpecification requestSpecification;

    @BeforeAll
    static void init() {
        String token = " ";
        requestSpecification = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.ANY)
                .build();

        RestAssured.requestSpecification = requestSpecification;
    }

    @Test
    void getListUsers() {
        given()
                .when()
                .get(baseUrl + "?page=2")
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    void getSingleUser() {
        given()
                .when()
                .get(baseUrl + "{id}", 2)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    void getSingleUserNotFound() {
        given()
                .when()
                .get(baseUrl + "{id}", 23)
                .prettyPeek()
                .then()
                .statusCode(404);
    }

    @Test
    void getListResource() {
        given()
                .when()
                .get(resourceUrl)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    void getSingleResource() {
        given()
                .when()
                .get(resourceUrl + "{id}", 2)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    void getSingleResourceNotFound() {
        given()
                .when()
                .get(resourceUrl + "{id}", 23)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @Test
    void postCreate() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("morpheus");
        userDTO.setJob("leader");

        given().log().all().contentType("application/json; charset=utf-8")
                .when().body(userDTO)
                .request("POST", baseUrl)
                .prettyPeek()
                .then().statusCode(201).extract()
                .response()
                .jsonPath();
    }

    @Test
    void postRegisterSuccessful() {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setEmail("eve.holt@reqres.in");
        emailDTO.setPassword("pistol");

        String token = given().log().all().contentType("application/json; charset=utf-8")
                .when().body(emailDTO)
                .request("POST", registerUrl)
                .prettyPeek()
                .then().statusCode(200).extract()
                .response()
                .jsonPath()
                .getString("token");
    }

    @Test
    void postRegisterUnsuccessful() {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setEmail("sydney@fife");

        String token = given().log().all().contentType("application/json; charset=utf-8")
                .when().body(emailDTO)
                .request("POST", registerUrl)
                .prettyPeek()
                .then().statusCode(400).extract()
                .response()
                .jsonPath()
                .getString("token");
    }

    @Test
    void postLoginSuccessful() {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setEmail("eve.holt@reqres.in");
        emailDTO.setPassword("cityslicka");

        String token = given().log().all().contentType("application/json; charset=utf-8")
                .when().body(emailDTO)
                .request("POST", loginUrl)
                .prettyPeek()
                .then().statusCode(200).extract()
                .response()
                .jsonPath()
                .getString("token");
    }

    @Test
    void postLoginUnsuccessful() {
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setEmail("peter@klaven");

        String token = given().log().all().contentType("application/json; charset=utf-8")
                .when().body(emailDTO)
                .request("POST", loginUrl)
                .prettyPeek()
                .then().statusCode(400).extract()
                .response()
                .jsonPath()
                .getString("token");
    }

    @Test
    void getDelayedResponse() {
        given()
                .when()
                .get(baseUrl + "?delay=3")
                .prettyPeek()
                .then()
                .statusCode(200);
    }
}

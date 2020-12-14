package ru.geekbrains.mini.market.tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class RestAssuredTests {
    /*
        {
            "id": 1,
            "title": "Milk",
            "price": 95,
            "categoryTitle": "Food"
        }
     */
    @Test
    public void simpleTest() {
        when()
                .get("http://localhost:8189/market/api/v1/products/1")
                .then()
                .statusCode(200)
                .and()
                .body("id", equalTo(1));
        // .body("title", equalTo("Milk"));
    }

    @Test
    public void getAllProductsTest() {
        when()
                .get("http://localhost:8189/market/api/v1/products")
                .then()
                .statusCode(200)
                .and()
                .body("$", hasSize(5))
                .body("[0].title", equalTo("Milk"));
    }

    @Test
    public void simpleManualTest() {
        Product p = when()
                .get("http://localhost:8189/market/api/v1/products/1")
                .then()
                .extract().body().as(Product.class);
        Assertions.assertEquals(1, p.getId());
        Assertions.assertEquals("Milk", p.getTitle());
    }

    @Test
    public void test2() {
        // Product p = new Product(null, "Calculator", 1000, "Electronic");

        Map<String, String> productMap = new HashMap<>();
        productMap.put("title", "Calculator");
        productMap.put("price", "1000");
        productMap.put("categoryTitle", "Electronic");

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(productMap)
                .when()
                .post("http://localhost:8189/market/api/v1/products")
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .body("title", equalTo("Calculator"));
    }

    @Test
    public void testSpec() {
        Product p = new Product(null, "Calculator", 1000, "Electronic");

        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(8189)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        given()
                .spec(requestSpecification)
                .body(p)
                .when()
                .post("market/api/v1/products")
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .body("title", equalTo("Calculator"));
    }
}

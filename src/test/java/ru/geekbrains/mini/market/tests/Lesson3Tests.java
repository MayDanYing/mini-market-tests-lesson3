package ru.geekbrains.mini.market.tests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class Lesson3Tests {

    // Домашнее задание:
    // 1. Протестировать CRUD-операции для продуктов
    // 2. Проверить что при отправке некорректных запросов
    // backend должен выдать 400 (возможно придется где-то
    // backend подкрутить)
    // 3. Проверить корректность сообщения об ошибке
    // в случае POST/PUT запросов

    RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setPort(8189)
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

    @Test
    public void getExistingProduct() {
        given()
                .spec(requestSpecification)
                .when()
                .get("market/api/v1/products")
                .then()
                .statusCode(200)
                .log().ifValidationFails(LogDetail.BODY)
                .body("[5].title", equalTo("PlayStation5"));

    }

    @Test
    public void getNonExistingProduct() {
        given()
                .spec(requestSpecification)
                .when()
                .get("market/api/v1/products/758595944")
                .then()
                .statusCode(404)
                .body("message", equalTo("Unable to find product with id: 758595944"));
    }

    @Test
    public void createProduct() {
        Map<String, String> productMap = new HashMap<>();
        productMap.put("title", "PlayStation5");
        productMap.put("price", "50000");
        productMap.put("categoryTitle", "Electronic");

        given()
                .spec(requestSpecification)
                .body(productMap)
                .when()
                .post("market/api/v1/products")
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .body("title", equalTo("PlayStation5"));
    }

    @Test
    public void createProductWithIdNotNull() {
        Map<String, String> productMap = new HashMap<>();
        productMap.put("id", "123");
        productMap.put("title", "XBox3");
        productMap.put("price", "40000");
        productMap.put("categoryTitle", "Electronic");

        given()
                .spec(requestSpecification)
                .body(productMap)
                .when()
                .post("market/api/v1/products")
                .then()
                .statusCode(400)
                .log().ifValidationFails(LogDetail.BODY)
                .body("message", equalTo("Id must be null for new entity"));

    }

    @Test
    public void modifyExistingProduct() {
        Map<String, String> productMap = new HashMap<>();
        productMap.put("id", "3");
        productMap.put("title", "NewCheese"); //меняем Cheese на NewCheese
        productMap.put("price", "380");
        productMap.put("categoryTitle", "Food");

        given()
                .spec(requestSpecification)
                .body(productMap)
                .when()
                .put("market/api/v1/products")
                .then()
                .log().ifValidationFails(LogDetail.BODY)
                .body("title", equalTo("NewCheese"));

            }

    @Test
    public void modifyNonExistingProduct() {
        Map<String, String> productMap = new HashMap<>();
        productMap.put("id", "123");
        productMap.put("title", "XBox3");
        productMap.put("price", "40000");
        productMap.put("categoryTitle", "Electronic");

        given()
                .spec(requestSpecification)
                .body(productMap)
                .when()
                .put("market/api/v1/products")
                .then()
                .statusCode(400)
                .log().ifValidationFails(LogDetail.BODY)
                .body("message", equalTo("Product with id: 123 doesn't exist"));

    }


    @Test
    public void deleteExistingProduct() {
        given()
                .spec(requestSpecification)
                .when()
                .delete("market/api/v1/products/5")
                .then()
                .statusCode(200);
    }

    @Test
    public void deleteNonExistingProduct() {
        given()
                .spec(requestSpecification)
                .when()
                .get("market/api/v1/products/5")
                .then()
                .statusCode(404)
                .body("message", equalTo("Unable to find product with id: 5"));
    }


}



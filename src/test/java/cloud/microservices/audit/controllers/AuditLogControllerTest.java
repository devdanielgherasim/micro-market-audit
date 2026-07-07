package cloud.microservices.audit.controllers;

import cloud.microservices.audit.repositories.AuditLogRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class AuditLogControllerTest {

    @Inject
    AuditLogRepository auditLogRepository;

    @BeforeEach
    @Transactional
    void cleanDatabase() {
        auditLogRepository.deleteAll();
    }

    @Test
    void createsAndListsPublicAuditLog() {
        Integer id = given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "timestamp": "2026-07-03T12:00:00",
                          "action": "CREATE",
                          "entityType": "Product",
                          "entityId": "101",
                          "username": "alice",
                          "details": "Created product",
                          "ipAddress": "127.0.0.1",
                          "userAgent": "JUnit",
                          "statusCode": 201
                        }
                        """)
                .when()
                .post("/api/audit")
                .then()
                .statusCode(201)
                .body("id", notNullValue())
                .body("action", equalTo("CREATE"))
                .extract()
                .path("id");

        given()
                .when()
                .get("/api/audit")
                .then()
                .statusCode(200)
                .body("content.find { it.id == %s }.entityType".formatted(id), equalTo("Product"));
    }

    @Test
    void rejectsAnonymousReadById() {
        given()
                .when()
                .get("/api/audit/{id}", 1)
                .then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "auditor", roles = "admin")
    void readsUpdatesAndDeletesAuditLogWhenAuthenticated() {
        Integer id = given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "timestamp": "2026-07-03T12:00:00",
                          "action": "UPDATE",
                          "entityType": "Order",
                          "entityId": "202",
                          "username": "alice",
                          "details": "Updated order",
                          "statusCode": 200
                        }
                        """)
                .when()
                .post("/api/audit")
                .then()
                .statusCode(201)
                .extract()
                .path("id");

        given()
                .when()
                .get("/api/audit/{id}", id)
                .then()
                .statusCode(200)
                .body("entityType", equalTo("Order"));

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "details": "Updated order status",
                          "statusCode": 202
                        }
                        """)
                .when()
                .put("/api/audit/{id}", id)
                .then()
                .statusCode(200)
                .body("details", equalTo("Updated order status"))
                .body("statusCode", equalTo(202));

        given()
                .when()
                .delete("/api/audit/{id}", id)
                .then()
                .statusCode(204);

        given()
                .when()
                .get("/api/audit/{id}", id)
                .then()
                .statusCode(404);
    }

    @Test
    void rejectsInvalidAuditPayload() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                          "timestamp": null,
                          "action": "",
                          "entityType": "",
                          "username": ""
                        }
                        """)
                .when()
                .post("/api/audit")
                .then()
                .statusCode(400);
    }
}

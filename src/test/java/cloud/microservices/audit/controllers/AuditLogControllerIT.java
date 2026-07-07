package cloud.microservices.audit.controllers;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusIntegrationTest
class AuditLogControllerIT {

    @Test
    void packagedApplicationServesReadinessProbe() {
        given()
                .when()
                .get("/health/ready")
                .then()
                .statusCode(200);
    }
}

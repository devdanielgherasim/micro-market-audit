package cloud.microservices.audit.controllers;

import cloud.microservices.audit.PostgresTestResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusIntegrationTest
@QuarkusTestResource(PostgresTestResource.class)
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

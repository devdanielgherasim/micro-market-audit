package cloud.microservices.audit.repositories;

import cloud.microservices.audit.PostgresTestResource;
import cloud.microservices.audit.entities.AuditLog;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
@QuarkusTestResource(PostgresTestResource.class)
class AuditLogRepositoryTest {

    @Inject
    AuditLogRepository auditLogRepository;

    @Test
    @TestTransaction
    void findsAuditLogsByActionEntityUserAndStatusCode() {
        AuditLog log = new AuditLog(
                LocalDateTime.now(),
                "CREATE",
                "Product",
                "101",
                "alice",
                "Created product",
                "127.0.0.1",
                "JUnit",
                201);
        auditLogRepository.persist(log);

        assertEquals(1, auditLogRepository.countByAction("CREATE"));
        assertEquals(1, auditLogRepository.countByEntityType("Product"));
        assertEquals(1, auditLogRepository.countByEntityId("101"));
        assertEquals(1, auditLogRepository.countByUser("alice"));
        assertEquals(1, auditLogRepository.countByStatusCode(201));
    }
}

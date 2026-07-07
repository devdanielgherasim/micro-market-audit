package cloud.microservices.audit.repositories;

import cloud.microservices.audit.entities.AuditLog;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class AuditLogRepositoryTest {

    @Inject
    AuditLogRepository auditLogRepository;

    @BeforeEach
    @Transactional
    void cleanDatabase() {
        auditLogRepository.deleteAll();
    }

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

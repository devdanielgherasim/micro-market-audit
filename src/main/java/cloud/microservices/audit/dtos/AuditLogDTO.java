package cloud.microservices.audit.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for AuditLog entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDTO {
    private Long id;
    private LocalDateTime timestamp;
    private String action;
    private String entityType;
    private String entityId;
    private String user;
    private String details;
    private String ipAddress;
    private String userAgent;
    private Integer statusCode;
}
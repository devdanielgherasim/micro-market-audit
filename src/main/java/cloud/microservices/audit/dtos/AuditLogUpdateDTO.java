package cloud.microservices.audit.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for updating an existing AuditLog.
 */
@Data
@NoArgsConstructor
public class AuditLogUpdateDTO {
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
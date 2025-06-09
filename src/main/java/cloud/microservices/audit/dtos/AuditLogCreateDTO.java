package cloud.microservices.audit.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for creating a new AuditLog.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogCreateDTO {
    @NotNull(message = "Timestamp is required")
    private LocalDateTime timestamp;
    
    @NotBlank(message = "Action is required")
    private String action;
    
    @NotBlank(message = "Entity type is required")
    private String entityType;
    
    private String entityId;
    
    @NotBlank(message = "User is required")
    private String user;
    
    private String details;
    
    private String ipAddress;
    
    private String userAgent;
    
    private Integer statusCode;
}
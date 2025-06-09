package cloud.microservices.audit.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity class for audit logs.
 */
@Data
@Entity
@Table(name = "audit_logs")
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog extends PanacheEntity {
    
    @NotNull(message = "Timestamp is required")
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @NotBlank(message = "Action is required")
    @Column(nullable = false)
    private String action;
    
    @NotBlank(message = "Entity type is required")
    @Column(name = "entity_type", nullable = false)
    private String entityType;
    
    @Column(name = "entity_id")
    private String entityId;
    
    @Column(nullable = false)
    private String user;
    
    @Column(length = 4000)
    private String details;
    
    @Column(name = "ip_address")
    private String ipAddress;
    
    @Column(name = "user_agent")
    private String userAgent;
    
    @Column(name = "status_code")
    private Integer statusCode;
}
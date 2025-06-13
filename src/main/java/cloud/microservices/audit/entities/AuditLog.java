package cloud.microservices.audit.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity class for audit logs.
 */
@Entity
@Table(name = "audit_logs")
public class AuditLog extends PanacheEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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
    private String username;

    @Column(length = 4000)
    private String details;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @Column(name = "status_code")
    private Integer statusCode;

    public AuditLog() {
    }

    public AuditLog(LocalDateTime timestamp, String action, String entityType, String entityId, String username, String details, String ipAddress, String userAgent, Integer statusCode) {
        this.timestamp = timestamp;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.username = username;
        this.details = details;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.statusCode = statusCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String user) {
        this.username = user;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditLog auditLog = (AuditLog) o;
        return Objects.equals(id, auditLog.id) &&
                Objects.equals(timestamp, auditLog.timestamp) &&
                Objects.equals(action, auditLog.action) &&
                Objects.equals(entityType, auditLog.entityType) &&
                Objects.equals(entityId, auditLog.entityId) &&
                Objects.equals(username, auditLog.username) &&
                Objects.equals(details, auditLog.details) &&
                Objects.equals(ipAddress, auditLog.ipAddress) &&
                Objects.equals(userAgent, auditLog.userAgent) &&
                Objects.equals(statusCode, auditLog.statusCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp, action, entityType, entityId, username, details, ipAddress, userAgent, statusCode);
    }

    @Override
    public String toString() {
        return "AuditLog{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", action='" + action + '\'' +
                ", entityType='" + entityType + '\'' +
                ", entityId='" + entityId + '\'' +
                ", user='" + username + '\'' +
                ", details='" + details + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}

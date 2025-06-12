package cloud.microservices.audit.dtos;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data Transfer Object for AuditLog entity.
 */
public class AuditLogDTO {
    private Long id;
    private LocalDateTime timestamp;
    private String action;
    private String entityType;
    private String entityId;
    private String username;
    private String details;
    private String ipAddress;
    private String userAgent;
    private Integer statusCode;

    public AuditLogDTO() {
    }

    public AuditLogDTO(Long id, LocalDateTime timestamp, String action, String entityType, String entityId, String username, String details, String ipAddress, String userAgent, Integer statusCode) {
        this.id = id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setUsername(String username) {
        this.username = username;
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
        AuditLogDTO that = (AuditLogDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(action, that.action) &&
                Objects.equals(entityType, that.entityType) &&
                Objects.equals(entityId, that.entityId) &&
                Objects.equals(username, that.username) &&
                Objects.equals(details, that.details) &&
                Objects.equals(ipAddress, that.ipAddress) &&
                Objects.equals(userAgent, that.userAgent) &&
                Objects.equals(statusCode, that.statusCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timestamp, action, entityType, entityId, username, details, ipAddress, userAgent, statusCode);
    }

    @Override
    public String toString() {
        return "AuditLogDTO{" +
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

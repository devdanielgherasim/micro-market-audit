package cloud.microservices.audit.dtos;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Data Transfer Object for updating an existing AuditLog.
 */
public class AuditLogUpdateDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private LocalDateTime timestamp;

    private String action;

    private String entityType;

    private String entityId;

    private String username;

    private String details;

    private String ipAddress;

    private String userAgent;

    private Integer statusCode;

    public AuditLogUpdateDTO() {
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
        AuditLogUpdateDTO that = (AuditLogUpdateDTO) o;
        return Objects.equals(timestamp, that.timestamp) &&
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
        return Objects.hash(timestamp, action, entityType, entityId, username, details, ipAddress, userAgent, statusCode);
    }

    @Override
    public String toString() {
        return "AuditLogUpdateDTO{" +
                "timestamp=" + timestamp +
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

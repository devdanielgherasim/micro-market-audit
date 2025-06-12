package cloud.microservices.audit.mappers;

import cloud.microservices.audit.dtos.AuditLogCreateDTO;
import cloud.microservices.audit.dtos.AuditLogDTO;
import cloud.microservices.audit.dtos.AuditLogUpdateDTO;
import cloud.microservices.audit.entities.AuditLog;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Mapper class for converting between AuditLog entity and DTOs.
 */
@ApplicationScoped
public class AuditLogMapper {

    /**
     * Map AuditLog entity to AuditLogDTO.
     *
     * @param auditLog the audit log entity
     * @return the audit log DTO
     */
    public AuditLogDTO toDTO(AuditLog auditLog) {
        if (auditLog == null) {
            return null;
        }

        return new AuditLogDTO(
                auditLog.id,
                auditLog.getTimestamp(),
                auditLog.getAction(),
                auditLog.getEntityType(),
                auditLog.getEntityId(),
                auditLog.getUsername(),
                auditLog.getDetails(),
                auditLog.getIpAddress(),
                auditLog.getUserAgent(),
                auditLog.getStatusCode()
        );
    }

    /**
     * Map AuditLogCreateDTO to AuditLog entity.
     *
     * @param dto      the audit log create DTO
     * @param auditLog the audit log entity to update
     */
    public void fromCreateDTO(AuditLogCreateDTO dto, AuditLog auditLog) {
        if (dto == null || auditLog == null) {
            return;
        }

        auditLog.setTimestamp(dto.getTimestamp());
        auditLog.setAction(dto.getAction());
        auditLog.setEntityType(dto.getEntityType());
        auditLog.setEntityId(dto.getEntityId());
        auditLog.setUsername(dto.getUsername());
        auditLog.setDetails(dto.getDetails());
        auditLog.setIpAddress(dto.getIpAddress());
        auditLog.setUserAgent(dto.getUserAgent());
        auditLog.setStatusCode(dto.getStatusCode());
    }

    /**
     * Map AuditLogUpdateDTO to AuditLog entity.
     *
     * @param dto      the audit log update DTO
     * @param auditLog the audit log entity to update
     */
    public void fromUpdateDTO(AuditLogUpdateDTO dto, AuditLog auditLog) {
        if (dto == null || auditLog == null) {
            return;
        }

        if (dto.getTimestamp() != null) {
            auditLog.setTimestamp(dto.getTimestamp());
        }
        if (dto.getAction() != null) {
            auditLog.setAction(dto.getAction());
        }
        if (dto.getEntityType() != null) {
            auditLog.setEntityType(dto.getEntityType());
        }
        if (dto.getEntityId() != null) {
            auditLog.setEntityId(dto.getEntityId());
        }
        if (dto.getUsername() != null) {
            auditLog.setUsername(dto.getUsername());
        }
        if (dto.getDetails() != null) {
            auditLog.setDetails(dto.getDetails());
        }
        if (dto.getIpAddress() != null) {
            auditLog.setIpAddress(dto.getIpAddress());
        }
        if (dto.getUserAgent() != null) {
            auditLog.setUserAgent(dto.getUserAgent());
        }
        if (dto.getStatusCode() != null) {
            auditLog.setStatusCode(dto.getStatusCode());
        }
    }
}
package cloud.microservices.audit.services;

import cloud.microservices.audit.dtos.AuditLogCreateDTO;
import cloud.microservices.audit.dtos.AuditLogDTO;
import cloud.microservices.audit.dtos.AuditLogUpdateDTO;
import cloud.microservices.audit.entities.AuditLog;
import cloud.microservices.audit.mappers.AuditLogMapper;
import cloud.microservices.audit.repositories.AuditLogRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for AuditLog entity providing business logic.
 */
@ApplicationScoped
public class AuditLogService {

    @Inject
    AuditLogRepository auditLogRepository;

    @Inject
    AuditLogMapper auditLogMapper;

    /**
     * Get all audit logs.
     *
     * @return list of all audit logs
     */
    public List<AuditLogDTO> getAllAuditLogs() {
        return auditLogRepository.findAll().stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get all audit logs with pagination.
     *
     * @param page the page number (0-based)
     * @param size the page size
     * @return paginated list of audit logs
     */
    public List<AuditLogDTO> getAllAuditLogsPaginated(int page, int size) {
        return auditLogRepository.findAllPaginated(page, size).list().stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get the total count of audit logs.
     *
     * @return the total count of audit logs
     */
    public long countAllAuditLogs() {
        return auditLogRepository.countAll();
    }

    /**
     * Get audit log by ID.
     *
     * @param id the audit log ID
     * @return the audit log DTO
     * @throws NotFoundException if audit log not found
     */
    public AuditLogDTO getAuditLogById(Long id) {
        AuditLog auditLog = auditLogRepository.findById(id);
        if (auditLog == null) {
            throw new NotFoundException("Audit log with ID " + id + " not found");
        }
        return auditLogMapper.toDTO(auditLog);
    }

    /**
     * Create a new audit log.
     *
     * @param auditLogCreateDTO the audit log data
     * @return the created audit log DTO
     */
    @Transactional
    public AuditLogDTO createAuditLog(AuditLogCreateDTO auditLogCreateDTO) {
        AuditLog auditLog = new AuditLog();
        auditLogMapper.fromCreateDTO(auditLogCreateDTO, auditLog);
        auditLogRepository.persist(auditLog);
        return auditLogMapper.toDTO(auditLog);
    }

    /**
     * Update an existing audit log.
     *
     * @param id                the audit log ID
     * @param auditLogUpdateDTO the audit log data to update
     * @return the updated audit log DTO
     * @throws NotFoundException if audit log not found
     */
    @Transactional
    public AuditLogDTO updateAuditLog(Long id, AuditLogUpdateDTO auditLogUpdateDTO) {
        AuditLog auditLog = auditLogRepository.findById(id);
        if (auditLog == null) {
            throw new NotFoundException("Audit log with ID " + id + " not found");
        }
        auditLogMapper.fromUpdateDTO(auditLogUpdateDTO, auditLog);
        auditLogRepository.persist(auditLog);
        return auditLogMapper.toDTO(auditLog);
    }

    /**
     * Delete an audit log.
     *
     * @param id the audit log ID
     * @throws NotFoundException if audit log not found
     */
    @Transactional
    public void deleteAuditLog(Long id) {
        AuditLog auditLog = auditLogRepository.findById(id);
        if (auditLog == null) {
            throw new NotFoundException("Audit log with ID " + id + " not found");
        }
        auditLogRepository.delete(auditLog);
    }

    /**
     * Find audit logs by action.
     *
     * @param action the action
     * @return list of audit logs with the specified action
     */
    public List<AuditLogDTO> findByAction(String action) {
        return auditLogRepository.findByAction(action).stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find audit logs by action with pagination.
     *
     * @param action the action
     * @param page the page number (0-based)
     * @param size the page size
     * @return paginated list of audit logs with the specified action
     */
    public List<AuditLogDTO> findByActionPaginated(String action, int page, int size) {
        return auditLogRepository.findByActionPaginated(action, page, size).list().stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Count audit logs by action.
     *
     * @param action the action
     * @return count of audit logs with the specified action
     */
    public long countByAction(String action) {
        return auditLogRepository.countByAction(action);
    }

    /**
     * Find audit logs by entity type.
     *
     * @param entityType the entity type
     * @return list of audit logs for the specified entity type
     */
    public List<AuditLogDTO> findByEntityType(String entityType) {
        return auditLogRepository.findByEntityType(entityType).stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find audit logs by entity type with pagination.
     *
     * @param entityType the entity type
     * @param page the page number (0-based)
     * @param size the page size
     * @return paginated list of audit logs for the specified entity type
     */
    public List<AuditLogDTO> findByEntityTypePaginated(String entityType, int page, int size) {
        return auditLogRepository.findByEntityTypePaginated(entityType, page, size).list().stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Count audit logs by entity type.
     *
     * @param entityType the entity type
     * @return count of audit logs for the specified entity type
     */
    public long countByEntityType(String entityType) {
        return auditLogRepository.countByEntityType(entityType);
    }

    /**
     * Find audit logs by entity ID.
     *
     * @param entityId the entity ID
     * @return list of audit logs for the specified entity ID
     */
    public List<AuditLogDTO> findByEntityId(String entityId) {
        return auditLogRepository.findByEntityId(entityId).stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find audit logs by entity ID with pagination.
     *
     * @param entityId the entity ID
     * @param page the page number (0-based)
     * @param size the page size
     * @return paginated list of audit logs for the specified entity ID
     */
    public List<AuditLogDTO> findByEntityIdPaginated(String entityId, int page, int size) {
        return auditLogRepository.findByEntityIdPaginated(entityId, page, size).list().stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Count audit logs by entity ID.
     *
     * @param entityId the entity ID
     * @return count of audit logs for the specified entity ID
     */
    public long countByEntityId(String entityId) {
        return auditLogRepository.countByEntityId(entityId);
    }

    /**
     * Find audit logs by user.
     *
     * @param user the user
     * @return list of audit logs for the specified user
     */
    public List<AuditLogDTO> findByUser(String user) {
        return auditLogRepository.findByUser(user).stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find audit logs by user with pagination.
     *
     * @param user the user
     * @param page the page number (0-based)
     * @param size the page size
     * @return paginated list of audit logs for the specified user
     */
    public List<AuditLogDTO> findByUserPaginated(String user, int page, int size) {
        return auditLogRepository.findByUserPaginated(user, page, size).list().stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Count audit logs by user.
     *
     * @param user the user
     * @return count of audit logs for the specified user
     */
    public long countByUser(String user) {
        return auditLogRepository.countByUser(user);
    }

    /**
     * Find audit logs by time range.
     *
     * @param startTime the start time
     * @param endTime   the end time
     * @return list of audit logs within the time range
     */
    public List<AuditLogDTO> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return auditLogRepository.findByTimeRange(startTime, endTime).stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find audit logs by time range with pagination.
     *
     * @param startTime the start time
     * @param endTime   the end time
     * @param page the page number (0-based)
     * @param size the page size
     * @return paginated list of audit logs within the time range
     */
    public List<AuditLogDTO> findByTimeRangePaginated(LocalDateTime startTime, LocalDateTime endTime, int page, int size) {
        return auditLogRepository.findByTimeRangePaginated(startTime, endTime, page, size).list().stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Count audit logs by time range.
     *
     * @param startTime the start time
     * @param endTime   the end time
     * @return count of audit logs within the time range
     */
    public long countByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return auditLogRepository.countByTimeRange(startTime, endTime);
    }

    /**
     * Find audit logs by IP address.
     *
     * @param ipAddress the IP address
     * @return list of audit logs from the specified IP address
     */
    public List<AuditLogDTO> findByIpAddress(String ipAddress) {
        return auditLogRepository.findByIpAddress(ipAddress).stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find audit logs by IP address with pagination.
     *
     * @param ipAddress the IP address
     * @param page the page number (0-based)
     * @param size the page size
     * @return paginated list of audit logs from the specified IP address
     */
    public List<AuditLogDTO> findByIpAddressPaginated(String ipAddress, int page, int size) {
        return auditLogRepository.findByIpAddressPaginated(ipAddress, page, size).list().stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Count audit logs by IP address.
     *
     * @param ipAddress the IP address
     * @return count of audit logs from the specified IP address
     */
    public long countByIpAddress(String ipAddress) {
        return auditLogRepository.countByIpAddress(ipAddress);
    }

    /**
     * Find audit logs by status code.
     *
     * @param statusCode the status code
     * @return list of audit logs with the specified status code
     */
    public List<AuditLogDTO> findByStatusCode(Integer statusCode) {
        return auditLogRepository.findByStatusCode(statusCode).stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Find audit logs by status code with pagination.
     *
     * @param statusCode the status code
     * @param page the page number (0-based)
     * @param size the page size
     * @return paginated list of audit logs with the specified status code
     */
    public List<AuditLogDTO> findByStatusCodePaginated(Integer statusCode, int page, int size) {
        return auditLogRepository.findByStatusCodePaginated(statusCode, page, size).list().stream()
                .map(auditLogMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Count audit logs by status code.
     *
     * @param statusCode the status code
     * @return count of audit logs with the specified status code
     */
    public long countByStatusCode(Integer statusCode) {
        return auditLogRepository.countByStatusCode(statusCode);
    }
}

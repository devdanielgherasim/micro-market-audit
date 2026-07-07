package cloud.microservices.audit.repositories;

import cloud.microservices.audit.entities.AuditLog;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * Repository for AuditLog entity providing database operations.
 * Includes methods for both standard and paginated queries.
 */
@ApplicationScoped
public class AuditLogRepository implements PanacheRepository<AuditLog> {

    /**
     * Get a paginated query for all audit logs.
     *
     * @param page the page number (0-based)
     * @param size the page size
     * @return a paginated query for all audit logs
     */
    public PanacheQuery<AuditLog> findAllPaginated(int page, int size) {
        return findAll().page(Page.of(page, size));
    }

    /**
     * Count all audit logs.
     *
     * @return the total count of audit logs
     */
    public long countAll() {
        return count();
    }

    /**
     * Find audit logs by action.
     *
     * @param action the action to search for
     * @return list of audit logs with the specified action
     */
    public List<AuditLog> findByAction(String action) {
        return list("action", action);
    }

    /**
     * Find audit logs by action with pagination.
     *
     * @param action the action to search for
     * @param page the page number (0-based)
     * @param size the page size
     * @return paginated query of audit logs with the specified action
     */
    public PanacheQuery<AuditLog> findByActionPaginated(String action, int page, int size) {
        return find("action", action).page(Page.of(page, size));
    }

    /**
     * Count audit logs by action.
     *
     * @param action the action to search for
     * @return count of audit logs with the specified action
     */
    public long countByAction(String action) {
        return count("action", action);
    }

    /**
     * Find audit logs by entity type.
     *
     * @param entityType the entity type to search for
     * @return list of audit logs for the specified entity type
     */
    public List<AuditLog> findByEntityType(String entityType) {
        return list("entityType", entityType);
    }

    /**
     * Find audit logs by entity type with pagination.
     *
     * @param entityType the entity type to search for
     * @param page the page number (0-based)
     * @param size the page size
     * @return paginated query of audit logs for the specified entity type
     */
    public PanacheQuery<AuditLog> findByEntityTypePaginated(String entityType, int page, int size) {
        return find("entityType", entityType).page(Page.of(page, size));
    }

    /**
     * Count audit logs by entity type.
     *
     * @param entityType the entity type to search for
     * @return count of audit logs for the specified entity type
     */
    public long countByEntityType(String entityType) {
        return count("entityType", entityType);
    }

    /**
     * Find audit logs by entity ID.
     *
     * @param entityId the entity ID to search for
     * @return list of audit logs for the specified entity ID
     */
    public List<AuditLog> findByEntityId(String entityId) {
        return list("entityId", entityId);
    }

    /**
     * Find audit logs by entity ID with pagination.
     *
     * @param entityId the entity ID to search for
     * @param page the page number (0-based)
     * @param size the page size
     * @return paginated query of audit logs for the specified entity ID
     */
    public PanacheQuery<AuditLog> findByEntityIdPaginated(String entityId, int page, int size) {
        return find("entityId", entityId).page(Page.of(page, size));
    }

    /**
     * Count audit logs by entity ID.
     *
     * @param entityId the entity ID to search for
     * @return count of audit logs for the specified entity ID
     */
    public long countByEntityId(String entityId) {
        return count("entityId", entityId);
    }

    /**
     * Find audit logs by user.
     *
     * @param user the user to search for
     * @return list of audit logs for the specified user
     */
    public List<AuditLog> findByUser(String user) {
        return list("username", user);
    }

    /**
     * Find audit logs by user with pagination.
     *
     * @param user the user to search for
     * @param page the page number (0-based)
     * @param size the page size
     * @return paginated query of audit logs for the specified user
     */
    public PanacheQuery<AuditLog> findByUserPaginated(String user, int page, int size) {
        return find("username", user).page(Page.of(page, size));
    }

    /**
     * Count audit logs by user.
     *
     * @param user the user to search for
     * @return count of audit logs for the specified user
     */
    public long countByUser(String user) {
        return count("username", user);
    }

    /**
     * Find audit logs by time range.
     *
     * @param startTime the start time
     * @param endTime   the end time
     * @return list of audit logs within the time range
     */
    public List<AuditLog> findByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return list("timestamp >= ?1 AND timestamp <= ?2", startTime, endTime);
    }

    /**
     * Find audit logs by time range with pagination.
     *
     * @param startTime the start time
     * @param endTime   the end time
     * @param page the page number (0-based)
     * @param size the page size
     * @return paginated query of audit logs within the time range
     */
    public PanacheQuery<AuditLog> findByTimeRangePaginated(LocalDateTime startTime, LocalDateTime endTime, int page, int size) {
        return find("timestamp >= ?1 AND timestamp <= ?2", startTime, endTime).page(Page.of(page, size));
    }

    /**
     * Count audit logs by time range.
     *
     * @param startTime the start time
     * @param endTime   the end time
     * @return count of audit logs within the time range
     */
    public long countByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return count("timestamp >= ?1 AND timestamp <= ?2", startTime, endTime);
    }

    /**
     * Find audit logs by IP address.
     *
     * @param ipAddress the IP address to search for
     * @return list of audit logs from the specified IP address
     */
    public List<AuditLog> findByIpAddress(String ipAddress) {
        return list("ipAddress", ipAddress);
    }

    /**
     * Find audit logs by IP address with pagination.
     *
     * @param ipAddress the IP address to search for
     * @param page the page number (0-based)
     * @param size the page size
     * @return paginated query of audit logs from the specified IP address
     */
    public PanacheQuery<AuditLog> findByIpAddressPaginated(String ipAddress, int page, int size) {
        return find("ipAddress", ipAddress).page(Page.of(page, size));
    }

    /**
     * Count audit logs by IP address.
     *
     * @param ipAddress the IP address to search for
     * @return count of audit logs from the specified IP address
     */
    public long countByIpAddress(String ipAddress) {
        return count("ipAddress", ipAddress);
    }

    /**
     * Find audit logs by status code.
     *
     * @param statusCode the status code to search for
     * @return list of audit logs with the specified status code
     */
    public List<AuditLog> findByStatusCode(Integer statusCode) {
        return list("statusCode", statusCode);
    }

    /**
     * Find audit logs by status code with pagination.
     *
     * @param statusCode the status code to search for
     * @param page the page number (0-based)
     * @param size the page size
     * @return paginated query of audit logs with the specified status code
     */
    public PanacheQuery<AuditLog> findByStatusCodePaginated(Integer statusCode, int page, int size) {
        return find("statusCode", statusCode).page(Page.of(page, size));
    }

    /**
     * Count audit logs by status code.
     *
     * @param statusCode the status code to search for
     * @return count of audit logs with the specified status code
     */
    public long countByStatusCode(Integer statusCode) {
        return count("statusCode", statusCode);
    }

    /**
     * Find audit logs by multiple criteria with pagination.
     *
     * @param startDate  the start date for filtering by timestamp
     * @param endDate    the end date for filtering by timestamp
     * @param username   the username to filter by
     * @param action     the action to filter by
     * @param entityType the entity type to filter by
     * @param entityId   the entity ID to filter by
     * @param page       the page number (0-based)
     * @param size       the page size
     * @return paginated query of audit logs matching the criteria
     */
    public PanacheQuery<AuditLog> findByFiltersPaginated(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String username,
            String action,
            String entityType,
            String entityId,
            int page,
            int size) {

        StringBuilder queryBuilder = new StringBuilder();
        List<Object> parameters = new ArrayList<>();
        int paramIndex = 1;

        // Build the query based on provided filters
        if (startDate != null) {
            queryBuilder.append("timestamp >= ?").append(paramIndex++);
            parameters.add(startDate);
        }

        if (endDate != null) {
            if (queryBuilder.length() > 0) {
                queryBuilder.append(" AND ");
            }
            queryBuilder.append("timestamp <= ?").append(paramIndex++);
            parameters.add(endDate);
        }

        if (username != null && !username.isEmpty()) {
            if (queryBuilder.length() > 0) {
                queryBuilder.append(" AND ");
            }
            queryBuilder.append("username = ?").append(paramIndex++);
            parameters.add(username);
        }

        if (action != null && !action.isEmpty()) {
            if (queryBuilder.length() > 0) {
                queryBuilder.append(" AND ");
            }
            queryBuilder.append("action = ?").append(paramIndex++);
            parameters.add(action);
        }

        if (entityType != null && !entityType.isEmpty()) {
            if (queryBuilder.length() > 0) {
                queryBuilder.append(" AND ");
            }
            queryBuilder.append("entityType = ?").append(paramIndex++);
            parameters.add(entityType);
        }

        if (entityId != null && !entityId.isEmpty()) {
            if (queryBuilder.length() > 0) {
                queryBuilder.append(" AND ");
            }
            queryBuilder.append("entityId = ?").append(paramIndex++);
            parameters.add(entityId);
        }

        // If no filters provided, return all records
        String query = queryBuilder.length() > 0 ? queryBuilder.toString() : null;

        // Execute the query with pagination
        if (query != null) {
            return find(query, parameters.toArray()).page(Page.of(page, size));
        } else {
            return findAll().page(Page.of(page, size));
        }
    }

    /**
     * Count audit logs by multiple criteria.
     *
     * @param startDate  the start date for filtering by timestamp
     * @param endDate    the end date for filtering by timestamp
     * @param username   the username to filter by
     * @param action     the action to filter by
     * @param entityType the entity type to filter by
     * @param entityId   the entity ID to filter by
     * @return count of audit logs matching the criteria
     */
    public long countByFilters(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String username,
            String action,
            String entityType,
            String entityId) {

        StringBuilder queryBuilder = new StringBuilder();
        List<Object> parameters = new ArrayList<>();
        int paramIndex = 1;

        // Build the query based on provided filters
        if (startDate != null) {
            queryBuilder.append("timestamp >= ?").append(paramIndex++);
            parameters.add(startDate);
        }

        if (endDate != null) {
            if (queryBuilder.length() > 0) {
                queryBuilder.append(" AND ");
            }
            queryBuilder.append("timestamp <= ?").append(paramIndex++);
            parameters.add(endDate);
        }

        if (username != null && !username.isEmpty()) {
            if (queryBuilder.length() > 0) {
                queryBuilder.append(" AND ");
            }
            queryBuilder.append("username = ?").append(paramIndex++);
            parameters.add(username);
        }

        if (action != null && !action.isEmpty()) {
            if (queryBuilder.length() > 0) {
                queryBuilder.append(" AND ");
            }
            queryBuilder.append("action = ?").append(paramIndex++);
            parameters.add(action);
        }

        if (entityType != null && !entityType.isEmpty()) {
            if (queryBuilder.length() > 0) {
                queryBuilder.append(" AND ");
            }
            queryBuilder.append("entityType = ?").append(paramIndex++);
            parameters.add(entityType);
        }

        if (entityId != null && !entityId.isEmpty()) {
            if (queryBuilder.length() > 0) {
                queryBuilder.append(" AND ");
            }
            queryBuilder.append("entityId = ?").append(paramIndex++);
            parameters.add(entityId);
        }

        // If no filters provided, count all records
        String query = queryBuilder.length() > 0 ? queryBuilder.toString() : null;

        // Execute the count query
        if (query != null) {
            return count(query, parameters.toArray());
        } else {
            return count();
        }
    }
}

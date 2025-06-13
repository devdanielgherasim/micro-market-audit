package cloud.microservices.audit.controllers;

import cloud.microservices.audit.dtos.AuditLogCreateDTO;
import cloud.microservices.audit.dtos.AuditLogDTO;
import cloud.microservices.audit.dtos.AuditLogUpdateDTO;
import cloud.microservices.audit.services.AuditLogService;
import cloud.microservices.audit.utils.PaginationUtil;
import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for AuditLog entity.
 */
@Path("/api/audit")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Audit Log", description = "Audit log operations")
@RolesAllowed("admin")
public class AuditLogController {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogController.class);

    /**
     * The Audit log service.
     */
    @Inject
    AuditLogService auditLogService;

    /**
     * Security identity for the authenticated user.
     */
    @Inject
    SecurityIdentity securityIdentity;

    /**
     * Gets all audit logs with pagination support.
     *
     * @param page    the page number (0-based)
     * @param size    the page size
     * @param uriInfo the URI info for building pagination links
     * @return the paginated audit logs
     */
    @GET
    @RolesAllowed("admin")
    @Operation(summary = "Get all audit logs", description = "Returns all audit logs in the system with pagination support")
    @APIResponse(responseCode = "200", description = "Paginated list of audit logs",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = AuditLogDTO.class)))
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @APIResponse(responseCode = "403", description = "Forbidden")
    public Response getAllAuditLogs(
            @Parameter(description = "Page number (0-based)", required = false) @QueryParam("page") Integer page,
            @Parameter(description = "Page size", required = false) @QueryParam("size") Integer size,
            @Context UriInfo uriInfo) {

        logger.info("Getting all audit logs with pagination - page: {}, size: {}", page, size);

        try {
            int[] pageParams = PaginationUtil.validatePaginationParams(page, size);
            int validPage = pageParams[0];
            int validSize = pageParams[1];

            logger.debug("Validated pagination parameters - page: {}, size: {}", validPage, validSize);

            List<AuditLogDTO> auditLogs = auditLogService.getAllAuditLogsPaginated(validPage, validSize);
            long totalCount = auditLogService.countAllAuditLogs();

            logger.info("Retrieved {} audit logs out of {} total", auditLogs.size(), totalCount);

            return PaginationUtil.createPaginatedResponse(auditLogs, totalCount, validPage, validSize, uriInfo);
        } catch (Exception e) {
            logger.error("Error retrieving all audit logs", e);
            throw e;
        }
    }

    /**
     * Gets audit log by id.
     *
     * @param id the id
     * @return the audit log by id
     */
    @GET
    @Path("/{id}")
    @RolesAllowed("admin")
    @Operation(summary = "Get audit log by ID", description = "Returns an audit log by its ID")
    @APIResponse(responseCode = "200", description = "The audit log",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = AuditLogDTO.class)))
    @APIResponse(responseCode = "404", description = "Audit log not found")
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @APIResponse(responseCode = "403", description = "Forbidden")
    public Response getAuditLogById(@PathParam("id") Long id) {
        logger.info("Getting audit log by ID: {}", id);

        try {
            AuditLogDTO auditLog = auditLogService.getAuditLogById(id);
            logger.info("Retrieved audit log: {}, action: {}, entity: {}/{}", 
                    id, auditLog.getAction(), auditLog.getEntityType(), auditLog.getEntityId());

            return Response.ok()
                    .entity(auditLog)
                    .header("Content-Type", MediaType.APPLICATION_JSON)
                    .encoding("UTF-8")
                    .build();
        } catch (NotFoundException e) {
            logger.warn("Audit log not found with ID: {}", id);
            throw e;
        } catch (Exception e) {
            logger.error("Error retrieving audit log with ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Create audit log response.
     *
     * @param auditLogCreateDTO the audit log create dto
     * @return the response
     */
    @POST
    @RolesAllowed("admin")
    @Operation(summary = "Create a new audit log", description = "Creates a new audit log in the system")
    @APIResponse(responseCode = "201", description = "Audit log created",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = AuditLogDTO.class)))
    @APIResponse(responseCode = "400", description = "Invalid input")
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @APIResponse(responseCode = "403", description = "Forbidden")
    public Response createAuditLog(@Valid AuditLogCreateDTO auditLogCreateDTO) {
        logger.info("Creating new audit log: action={}, entity={}/{}", 
                auditLogCreateDTO.getAction(), 
                auditLogCreateDTO.getEntityType(), 
                auditLogCreateDTO.getEntityId());

        try {
            // Set the authenticated username if not provided
            if (auditLogCreateDTO.getUsername() == null || auditLogCreateDTO.getUsername().isEmpty()) {
                String username = securityIdentity.getPrincipal().getName();
                auditLogCreateDTO.setUsername(username);
                logger.debug("Setting authenticated username: {}", username);
            }

            AuditLogDTO createdAuditLog = auditLogService.createAuditLog(auditLogCreateDTO);
            logger.info("Audit log created successfully with ID: {}", createdAuditLog.getId());

            return Response.created(URI.create("/api/audit/" + createdAuditLog.getId()))
                    .entity(createdAuditLog)
                    .header("Content-Type", MediaType.APPLICATION_JSON)
                    .encoding("UTF-8")
                    .build();
        } catch (Exception e) {
            logger.error("Error creating audit log: action={}, entity={}/{}", 
                    auditLogCreateDTO.getAction(), 
                    auditLogCreateDTO.getEntityType(), 
                    auditLogCreateDTO.getEntityId(), 
                    e);
            throw e;
        }
    }

    /**
     * Update audit log response.
     *
     * @param id                the id
     * @param auditLogUpdateDTO the audit log update dto
     * @return the response
     */
    @PUT
    @Path("/{id}")
    @RolesAllowed("admin")
    @Operation(summary = "Update an audit log", description = "Updates an existing audit log")
    @APIResponse(responseCode = "200", description = "Audit log updated",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = AuditLogDTO.class)))
    @APIResponse(responseCode = "404", description = "Audit log not found")
    @APIResponse(responseCode = "400", description = "Invalid input")
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @APIResponse(responseCode = "403", description = "Forbidden")
    public Response updateAuditLog(@PathParam("id") Long id, @Valid AuditLogUpdateDTO auditLogUpdateDTO) {
        logger.info("Updating audit log with ID: {}", id);
        logger.debug("Update details: action={}, entity={}/{}", 
                auditLogUpdateDTO.getAction(), 
                auditLogUpdateDTO.getEntityType(), 
                auditLogUpdateDTO.getEntityId());

        try {
            if (auditLogUpdateDTO.getUsername() != null) {
                String username = securityIdentity.getPrincipal().getName();
                auditLogUpdateDTO.setUsername(username);
                logger.debug("Setting authenticated username: {}", username);
            }

            AuditLogDTO updatedAuditLog = auditLogService.updateAuditLog(id, auditLogUpdateDTO);
            logger.info("Audit log updated successfully: ID={}, action={}", 
                    id, updatedAuditLog.getAction());

            return Response.ok()
                    .entity(updatedAuditLog)
                    .header("Content-Type", MediaType.APPLICATION_JSON)
                    .encoding("UTF-8")
                    .build();
        } catch (NotFoundException e) {
            logger.warn("Audit log not found for update with ID: {}", id);
            throw e;
        } catch (Exception e) {
            logger.error("Error updating audit log with ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Delete audit log response.
     *
     * @param id the id
     * @return the response
     */
    @DELETE
    @Path("/{id}")
    @RolesAllowed("admin")
    @Operation(summary = "Delete an audit log", description = "Deletes an audit log from the system")
    @APIResponse(responseCode = "204", description = "Audit log deleted")
    @APIResponse(responseCode = "404", description = "Audit log not found")
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @APIResponse(responseCode = "403", description = "Forbidden")
    public Response deleteAuditLog(@PathParam("id") Long id) {
        logger.info("Deleting audit log with ID: {}", id);

        try {
            auditLogService.deleteAuditLog(id);
            logger.info("Audit log deleted successfully: ID={}", id);

            return Response.noContent().build();
        } catch (NotFoundException e) {
            logger.warn("Audit log not found for deletion with ID: {}", id);
            throw e;
        } catch (Exception e) {
            logger.error("Error deleting audit log with ID: {}", id, e);
            throw e;
        }
    }

    /**
     * Find audit logs by action with pagination support.
     *
     * @param action  the action
     * @param page    the page number (0-based)
     * @param size    the page size
     * @param uriInfo the URI info for building pagination links
     * @return the paginated response
     */
    @GET
    @Path("/action/{action}")
    @RolesAllowed("admin")
    @Operation(summary = "Find audit logs by action", description = "Returns audit logs with the specified action with pagination support")
    @APIResponse(responseCode = "200", description = "Paginated list of audit logs",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = AuditLogDTO.class)))
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @APIResponse(responseCode = "403", description = "Forbidden")
    public Response findByAction(
            @PathParam("action") String action,
            @Parameter(description = "Page number (0-based)", required = false) @QueryParam("page") Integer page,
            @Parameter(description = "Page size", required = false) @QueryParam("size") Integer size,
            @Context UriInfo uriInfo) {

        logger.info("Finding audit logs with action: {}, page: {}, size: {}", action, page, size);

        try {
            int[] pageParams = PaginationUtil.validatePaginationParams(page, size);
            int validPage = pageParams[0];
            int validSize = pageParams[1];

            logger.debug("Validated pagination parameters - page: {}, size: {}", validPage, validSize);

            List<AuditLogDTO> auditLogs = auditLogService.findByActionPaginated(action, validPage, validSize);
            long totalCount = auditLogService.countByAction(action);

            logger.info("Found {} audit logs with action: {} out of {} total", 
                    auditLogs.size(), action, totalCount);

            return PaginationUtil.createPaginatedResponse(auditLogs, totalCount, validPage, validSize, uriInfo);
        } catch (Exception e) {
            logger.error("Error finding audit logs with action: {}", action, e);
            throw e;
        }
    }

    /**
     * Find audit logs by entity type with pagination support.
     *
     * @param entityType the entity type
     * @param page       the page number (0-based)
     * @param size       the page size
     * @param uriInfo    the URI info for building pagination links
     * @return the paginated response
     */
    @GET
    @Path("/entity-type/{entityType}")
    @RolesAllowed("admin")
    @Operation(summary = "Find audit logs by entity type", description = "Returns audit logs for the specified entity type with pagination support")
    @APIResponse(responseCode = "200", description = "Paginated list of audit logs",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = AuditLogDTO.class)))
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @APIResponse(responseCode = "403", description = "Forbidden")
    public Response findByEntityType(
            @PathParam("entityType") String entityType,
            @Parameter(description = "Page number (0-based)", required = false) @QueryParam("page") Integer page,
            @Parameter(description = "Page size", required = false) @QueryParam("size") Integer size,
            @Context UriInfo uriInfo) {

        logger.info("Finding audit logs with entity type: {}, page: {}, size: {}", entityType, page, size);

        try {
            int[] pageParams = PaginationUtil.validatePaginationParams(page, size);
            int validPage = pageParams[0];
            int validSize = pageParams[1];

            logger.debug("Validated pagination parameters - page: {}, size: {}", validPage, validSize);

            List<AuditLogDTO> auditLogs = auditLogService.findByEntityTypePaginated(entityType, validPage, validSize);
            long totalCount = auditLogService.countByEntityType(entityType);

            logger.info("Found {} audit logs with entity type: {} out of {} total", 
                    auditLogs.size(), entityType, totalCount);

            return PaginationUtil.createPaginatedResponse(auditLogs, totalCount, validPage, validSize, uriInfo);
        } catch (Exception e) {
            logger.error("Error finding audit logs with entity type: {}", entityType, e);
            throw e;
        }
    }

    /**
     * Find audit logs by entity ID with pagination support.
     *
     * @param entityId the entity ID
     * @param page     the page number (0-based)
     * @param size     the page size
     * @param uriInfo  the URI info for building pagination links
     * @return the paginated response
     */
    @GET
    @Path("/entity-id/{entityId}")
    @RolesAllowed("admin")
    @Operation(summary = "Find audit logs by entity ID", description = "Returns audit logs for the specified entity ID with pagination support")
    @APIResponse(responseCode = "200", description = "Paginated list of audit logs",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = AuditLogDTO.class)))
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @APIResponse(responseCode = "403", description = "Forbidden")
    public Response findByEntityId(
            @PathParam("entityId") String entityId,
            @Parameter(description = "Page number (0-based)", required = false) @QueryParam("page") Integer page,
            @Parameter(description = "Page size", required = false) @QueryParam("size") Integer size,
            @Context UriInfo uriInfo) {

        logger.info("Finding audit logs with entity ID: {}, page: {}, size: {}", entityId, page, size);

        try {
            int[] pageParams = PaginationUtil.validatePaginationParams(page, size);
            int validPage = pageParams[0];
            int validSize = pageParams[1];

            logger.debug("Validated pagination parameters - page: {}, size: {}", validPage, validSize);

            List<AuditLogDTO> auditLogs = auditLogService.findByEntityIdPaginated(entityId, validPage, validSize);
            long totalCount = auditLogService.countByEntityId(entityId);

            logger.info("Found {} audit logs with entity ID: {} out of {} total", 
                    auditLogs.size(), entityId, totalCount);

            return PaginationUtil.createPaginatedResponse(auditLogs, totalCount, validPage, validSize, uriInfo);
        } catch (Exception e) {
            logger.error("Error finding audit logs with entity ID: {}", entityId, e);
            throw e;
        }
    }

    /**
     * Find audit logs by user with pagination support.
     *
     * @param user    the user
     * @param page    the page number (0-based)
     * @param size    the page size
     * @param uriInfo the URI info for building pagination links
     * @return the paginated response
     */
    @GET
    @Path("/user/{user}")
    @RolesAllowed("admin")
    @Operation(summary = "Find audit logs by user", description = "Returns audit logs for the specified user with pagination support")
    @APIResponse(responseCode = "200", description = "Paginated list of audit logs",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = AuditLogDTO.class)))
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @APIResponse(responseCode = "403", description = "Forbidden")
    public Response findByUser(
            @PathParam("user") String user,
            @Parameter(description = "Page number (0-based)", required = false) @QueryParam("page") Integer page,
            @Parameter(description = "Page size", required = false) @QueryParam("size") Integer size,
            @Context UriInfo uriInfo) {

        logger.info("Finding audit logs for user: {}, page: {}, size: {}", user, page, size);

        try {
            int[] pageParams = PaginationUtil.validatePaginationParams(page, size);
            int validPage = pageParams[0];
            int validSize = pageParams[1];

            logger.debug("Validated pagination parameters - page: {}, size: {}", validPage, validSize);

            List<AuditLogDTO> auditLogs = auditLogService.findByUserPaginated(user, validPage, validSize);
            long totalCount = auditLogService.countByUser(user);

            logger.info("Found {} audit logs for user: {} out of {} total", 
                    auditLogs.size(), user, totalCount);

            return PaginationUtil.createPaginatedResponse(auditLogs, totalCount, validPage, validSize, uriInfo);
        } catch (Exception e) {
            logger.error("Error finding audit logs for user: {}", user, e);
            throw e;
        }
    }

    /**
     * Find audit logs by time range with pagination support.
     *
     * @param startTime the start time
     * @param endTime   the end time
     * @param page      the page number (0-based)
     * @param size      the page size
     * @param uriInfo   the URI info for building pagination links
     * @return the paginated response
     */
    @GET
    @Path("/time-range")
    @RolesAllowed("admin")
    @Operation(summary = "Find audit logs by time range", description = "Returns audit logs within the time range with pagination support")
    @APIResponse(responseCode = "200", description = "Paginated list of audit logs",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = AuditLogDTO.class)))
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @APIResponse(responseCode = "403", description = "Forbidden")
    public Response findByTimeRange(
            @Parameter(description = "Start time (ISO format)", required = true) @QueryParam("startTime") LocalDateTime startTime,
            @Parameter(description = "End time (ISO format)", required = true) @QueryParam("endTime") LocalDateTime endTime,
            @Parameter(description = "Page number (0-based)", required = false) @QueryParam("page") Integer page,
            @Parameter(description = "Page size", required = false) @QueryParam("size") Integer size,
            @Context UriInfo uriInfo) {

        logger.info("Finding audit logs between times: {} and {}, page: {}, size: {}", startTime, endTime, page, size);

        if (startTime == null || endTime == null) {
            logger.warn("Missing required time parameters: startTime={}, endTime={}", startTime, endTime);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Both startTime and endTime parameters are required")
                    .build();
        }

        try {
            int[] pageParams = PaginationUtil.validatePaginationParams(page, size);
            int validPage = pageParams[0];
            int validSize = pageParams[1];

            logger.debug("Validated pagination parameters - page: {}, size: {}", validPage, validSize);

            List<AuditLogDTO> auditLogs = auditLogService.findByTimeRangePaginated(startTime, endTime, validPage, validSize);
            long totalCount = auditLogService.countByTimeRange(startTime, endTime);

            logger.info("Found {} audit logs between {} and {} out of {} total", 
                    auditLogs.size(), startTime, endTime, totalCount);

            return PaginationUtil.createPaginatedResponse(auditLogs, totalCount, validPage, validSize, uriInfo);
        } catch (Exception e) {
            logger.error("Error finding audit logs between times: {} and {}", startTime, endTime, e);
            throw e;
        }
    }

    /**
     * Find audit logs by IP address with pagination support.
     *
     * @param ipAddress the IP address
     * @param page      the page number (0-based)
     * @param size      the page size
     * @param uriInfo   the URI info for building pagination links
     * @return the paginated response
     */
    @GET
    @Path("/ip-address/{ipAddress}")
    @RolesAllowed("admin")
    @Operation(summary = "Find audit logs by IP address", description = "Returns audit logs from the specified IP address with pagination support")
    @APIResponse(responseCode = "200", description = "Paginated list of audit logs",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = AuditLogDTO.class)))
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @APIResponse(responseCode = "403", description = "Forbidden")
    public Response findByIpAddress(
            @PathParam("ipAddress") String ipAddress,
            @Parameter(description = "Page number (0-based)", required = false) @QueryParam("page") Integer page,
            @Parameter(description = "Page size", required = false) @QueryParam("size") Integer size,
            @Context UriInfo uriInfo) {

        logger.info("Finding audit logs with IP address: {}, page: {}, size: {}", ipAddress, page, size);

        try {
            int[] pageParams = PaginationUtil.validatePaginationParams(page, size);
            int validPage = pageParams[0];
            int validSize = pageParams[1];

            logger.debug("Validated pagination parameters - page: {}, size: {}", validPage, validSize);

            List<AuditLogDTO> auditLogs = auditLogService.findByIpAddressPaginated(ipAddress, validPage, validSize);
            long totalCount = auditLogService.countByIpAddress(ipAddress);

            logger.info("Found {} audit logs with IP address: {} out of {} total", 
                    auditLogs.size(), ipAddress, totalCount);

            return PaginationUtil.createPaginatedResponse(auditLogs, totalCount, validPage, validSize, uriInfo);
        } catch (Exception e) {
            logger.error("Error finding audit logs with IP address: {}", ipAddress, e);
            throw e;
        }
    }

    /**
     * Find audit logs by status code with pagination support.
     *
     * @param statusCode the status code
     * @param page       the page number (0-based)
     * @param size       the page size
     * @param uriInfo    the URI info for building pagination links
     * @return the paginated response
     */
    @GET
    @Path("/status-code/{statusCode}")
    @RolesAllowed("admin")
    @Operation(summary = "Find audit logs by status code", description = "Returns audit logs with the specified status code with pagination support")
    @APIResponse(responseCode = "200", description = "Paginated list of audit logs",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = AuditLogDTO.class)))
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @APIResponse(responseCode = "403", description = "Forbidden")
    public Response findByStatusCode(
            @PathParam("statusCode") Integer statusCode,
            @Parameter(description = "Page number (0-based)", required = false) @QueryParam("page") Integer page,
            @Parameter(description = "Page size", required = false) @QueryParam("size") Integer size,
            @Context UriInfo uriInfo) {

        logger.info("Finding audit logs with status code: {}, page: {}, size: {}", statusCode, page, size);

        try {
            int[] pageParams = PaginationUtil.validatePaginationParams(page, size);
            int validPage = pageParams[0];
            int validSize = pageParams[1];

            logger.debug("Validated pagination parameters - page: {}, size: {}", validPage, validSize);

            List<AuditLogDTO> auditLogs = auditLogService.findByStatusCodePaginated(statusCode, validPage, validSize);
            long totalCount = auditLogService.countByStatusCode(statusCode);

            logger.info("Found {} audit logs with status code: {} out of {} total", 
                    auditLogs.size(), statusCode, totalCount);

            return PaginationUtil.createPaginatedResponse(auditLogs, totalCount, validPage, validSize, uriInfo);
        } catch (Exception e) {
            logger.error("Error finding audit logs with status code: {}", statusCode, e);
            throw e;
        }
    }
}

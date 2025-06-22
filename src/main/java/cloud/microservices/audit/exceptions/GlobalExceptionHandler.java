package cloud.microservices.audit.exceptions;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Global exception handler for the audit microservice.
 * Provides consistent error responses for different types of exceptions.
 */
@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionHandler.class);

    @Context
    private UriInfo uriInfo;

    @Override
    public Response toResponse(Throwable exception) {
        LOGGER.error("Exception occurred", exception);

        return switch (exception) {
            case NotFoundException ignored ->
                    buildResponse(Response.Status.NOT_FOUND, exception.getMessage());
            case ConstraintViolationException constraintViolationException ->
                    handleConstraintViolation(constraintViolationException);
            case IllegalArgumentException ignored ->
                    buildResponse(Response.Status.BAD_REQUEST, exception.getMessage());
            case null, default -> buildResponse(Response.Status.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred. Please contact support.");
        };
    }

    private Response handleConstraintViolation(ConstraintViolationException exception) {
        Map<String, String> errors = exception.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        this::getPropertyPath,
                        ConstraintViolation::getMessage,
                        (error1, error2) -> error1 + ", " + error2
                ));

        ErrorResponse errorResponse = buildErrorResponse(
                Response.Status.BAD_REQUEST.getStatusCode(),
                "Validation error",
                Response.Status.BAD_REQUEST.getReasonPhrase());

        StringBuilder detailedMessage = new StringBuilder("Validation error: ");
        errors.forEach((field, message) -> 
            detailedMessage.append(field).append(" - ").append(message).append("; "));

        errorResponse.setMessage(detailedMessage.toString().trim());

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errorResponse)
                .build();
    }

    private String getPropertyPath(ConstraintViolation<?> violation) {
        String propertyPath = violation.getPropertyPath().toString();
        int lastDotIndex = propertyPath.lastIndexOf('.');
        if (lastDotIndex > 0) {
            propertyPath = propertyPath.substring(lastDotIndex + 1);
        }
        return propertyPath;
    }

    private Response buildResponse(Response.Status status, String message) {
        ErrorResponse errorResponse = buildErrorResponse(
                status.getStatusCode(),
                message,
                status.getReasonPhrase());

        return Response.status(status)
                .entity(errorResponse)
                .build();
    }

    private ErrorResponse buildErrorResponse(int status, String message, String error) {
        String path = uriInfo != null ? uriInfo.getPath() : "";
        return new ErrorResponse(status, error, message, path);
    }
}

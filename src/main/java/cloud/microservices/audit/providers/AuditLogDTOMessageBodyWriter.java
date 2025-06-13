package cloud.microservices.audit.providers;

import cloud.microservices.audit.dtos.AuditLogDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Custom MessageBodyWriter for AuditLogDTO objects.
 * Ensures proper JSON serialization of AuditLogDTO objects.
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
public class AuditLogDTOMessageBodyWriter implements MessageBodyWriter<AuditLogDTO> {

    @Inject
    ObjectMapper objectMapper;

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return AuditLogDTO.class.isAssignableFrom(type);
    }

    @Override
    public void writeTo(AuditLogDTO auditLogDTO, Class<?> type, Type genericType, Annotation[] annotations,
                        MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {
        
        // Ensure content type is set to application/json
        httpHeaders.putSingle("Content-Type", MediaType.APPLICATION_JSON);
        
        // Use the injected ObjectMapper to serialize the AuditLogDTO object
        objectMapper.writeValue(entityStream, auditLogDTO);
    }
}
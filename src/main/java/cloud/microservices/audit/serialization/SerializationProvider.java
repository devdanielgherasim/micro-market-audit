package cloud.microservices.audit.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * Generic serialization provider for handling object serialization and deserialization.
 * This class provides a centralized way to handle different content types and formats.
 */
@ApplicationScoped
public class SerializationProvider {
    private static final Logger logger = LoggerFactory.getLogger(SerializationProvider.class);

    @Inject
    private ObjectMapper objectMapper;

    /**
     * Deserialize an object from an input stream.
     *
     * @param <T>         the type parameter
     * @param inputStream the input stream
     * @param type        the class type
     * @param genericType the generic type
     * @param mediaType   the media type
     * @return the deserialized object
     * @throws IOException if an I/O error occurs
     */
    public <T> T deserialize(InputStream inputStream, Class<T> type, Type genericType, MediaType mediaType) throws IOException {
        logger.debug("Deserializing object of type {} with media type {}", type.getName(), mediaType);
        
        if (MediaType.APPLICATION_JSON_TYPE.isCompatible(mediaType)) {
            return deserializeJson(inputStream, type, genericType);
        }
        
        throw new UnsupportedOperationException("Unsupported media type: " + mediaType);
    }

    /**
     * Serialize an object to an output stream.
     *
     * @param object       the object to serialize
     * @param outputStream the output stream
     * @param mediaType    the media type
     * @throws IOException if an I/O error occurs
     */
    public void serialize(Object object, OutputStream outputStream, MediaType mediaType) throws IOException {
        logger.debug("Serializing object of type {} with media type {}", 
                object.getClass().getName(), mediaType);
        
        if (MediaType.APPLICATION_JSON_TYPE.isCompatible(mediaType)) {
            serializeJson(object, outputStream);
            return;
        }
        
        throw new UnsupportedOperationException("Unsupported media type: " + mediaType);
    }

    /**
     * Deserialize JSON from an input stream.
     *
     * @param <T>         the type parameter
     * @param inputStream the input stream
     * @param type        the class type
     * @param genericType the generic type
     * @return the deserialized object
     * @throws IOException if an I/O error occurs
     */
    private <T> T deserializeJson(InputStream inputStream, Class<T> type, Type genericType) throws IOException {
        try {
            return objectMapper.readValue(inputStream, 
                    objectMapper.constructType(genericType != null ? genericType : type));
        } catch (IOException e) {
            logger.error("Error deserializing JSON to type {}: {}", type.getName(), e.getMessage());
            throw e;
        }
    }

    /**
     * Serialize an object to JSON.
     *
     * @param object       the object to serialize
     * @param outputStream the output stream
     * @throws IOException if an I/O error occurs
     */
    private void serializeJson(Object object, OutputStream outputStream) throws IOException {
        try {
            objectMapper.writeValue(outputStream, object);
        } catch (IOException e) {
            logger.error("Error serializing object to JSON: {}", e.getMessage());
            throw e;
        }
    }
}
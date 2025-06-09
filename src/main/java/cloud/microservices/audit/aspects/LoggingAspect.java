//package cloud.microservices.audit.aspects;
//
//import jakarta.annotation.Priority;
//import jakarta.enterprise.context.ApplicationScoped;
//import jakarta.interceptor.AroundInvoke;
//import jakarta.interceptor.Interceptor;
//import jakarta.interceptor.InvocationContext;
//import org.jboss.logging.Logger;
//
//import java.util.Arrays;
//
///**
// * Aspect for logging method entry and exit, as well as execution time.
// * This helps with debugging and performance monitoring.
// */
//@Logged
//@Interceptor
//@Priority(Interceptor.Priority.APPLICATION)
//@ApplicationScoped
//public class LoggingAspect {
//
//    private static final Logger LOGGER = Logger.getLogger(LoggingAspect.class);
//
//    @AroundInvoke
//    public Object logMethodExecution(InvocationContext context) throws Exception {
//        String className = context.getTarget().getClass().getName();
//        String methodName = context.getMethod().getName();
//        String methodSignature = className + "." + methodName;
//
//        // Log method entry with parameters (but sanitize sensitive data)
//        LOGGER.infof("Entering method: %s with parameters: %s",
//                methodSignature, sanitizeParameters(context.getParameters()));
//
//        long startTime = System.currentTimeMillis();
//        try {
//            // Execute the method
//            Object result = context.proceed();
//
//            // Log method exit with execution time
//            long executionTime = System.currentTimeMillis() - startTime;
//            LOGGER.infof("Exiting method: %s, execution time: %d ms, result: %s",
//                    methodSignature, executionTime, sanitizeResult(result));
//
//            return result;
//        } catch (Exception e) {
//            // Log exception
//            long executionTime = System.currentTimeMillis() - startTime;
//            LOGGER.errorf(e, "Exception in method: %s, execution time: %d ms",
//                    methodSignature, executionTime);
//            throw e;
//        }
//    }
//
//    /**
//     * Sanitize method parameters to avoid logging sensitive information.
//     *
//     * @param parameters The method parameters
//     * @return Sanitized string representation of parameters
//     */
//    private String sanitizeParameters(Object[] parameters) {
//        if (parameters == null || parameters.length == 0) {
//            return "[]";
//        }
//
//        return Arrays.stream(parameters)
//                .map(this::sanitizeParameter)
//                .toList()
//                .toString();
//    }
//
//    /**
//     * Sanitize a single parameter.
//     *
//     * @param param The parameter to sanitize
//     * @return Sanitized string representation of the parameter
//     */
//    private String sanitizeParameter(Object param) {
//        if (param == null) {
//            return "null";
//        }
//
//        // Add specific sanitization logic for sensitive data
//        // For example, if the parameter is a DTO with password field
//
//        return param.toString();
//    }
//
//    /**
//     * Sanitize method result to avoid logging sensitive information.
//     *
//     * @param result The method result
//     * @return Sanitized string representation of the result
//     */
//    private String sanitizeResult(Object result) {
//        if (result == null) {
//            return "null";
//        }
//
//        // Add specific sanitization logic for sensitive data
//
//        // For collections, just log the size instead of the full content
//        if (result instanceof java.util.Collection<?> collection) {
//            return String.format("Collection with %d items", collection.size());
//        }
//
//        return result.toString();
//    }
//}

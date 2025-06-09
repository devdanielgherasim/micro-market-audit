//package cloud.microservices.audit.aspects;
//
//import jakarta.enterprise.util.Nonbinding;
//import jakarta.interceptor.InterceptorBinding;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
///**
// * Annotation to mark methods or classes for logging.
// * When applied to a method or class, the method execution will be logged.
// */
//@InterceptorBinding
//@Target({ElementType.TYPE, ElementType.METHOD})
//@Retention(RetentionPolicy.RUNTIME)
//public @interface Logged {
//
//    /**
//     * Specify a custom logger name.
//     * If not provided, the class name will be used.
//     *
//     * @return the logger name
//     */
//    @Nonbinding
//    String value() default "";
//}
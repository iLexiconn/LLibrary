package net.ilexiconn.llibrary.server.network;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Assign a new network wrapper instance to the field.
 *
 * @author iLexiconn
 * @since 1.2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NetworkWrapper {
    /**
     * @return the channel name. If null, the modid will be used
     */
    String name() default "";

    /**
     * @return an array of class names of abstract messages to be registered
     */
    String[] messages() default {};
}

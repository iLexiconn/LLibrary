package net.ilexiconn.llibrary.server.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigEntry {
    String name() default "";

    String comment() default "";

    String category() default Configuration.CATEGORY_GENERAL;

    String minValue() default "";

    String maxValue() default "";

    String[] validValues() default "";

    Side side() default Side.SERVER;
}

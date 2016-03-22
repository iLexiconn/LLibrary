package net.ilexiconn.llibrary.server.config;

import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.config.Configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigEntry {
    /**
     * @return a custom config name for this entry. If empty, the field name will be used
     */
    String name() default "";

    /**
     * @return a comment for this entry
     */
    String comment() default "";

    /**
     * @return the category for this entry. If empty, the general category will be used
     */
    String category() default Configuration.CATEGORY_GENERAL;

    /**
     * @return return the minimal config value of this entry. This field is used by integers, floats and doubles
     */
    String minValue() default "";

    /**
     * @return return the maximal config value of this entry. This field is used by integers, floats and doubles
     */
    String maxValue() default "";

    /**
     * @return return a list of valid values. this field is used by all arrays.
     */
    String[] validValues() default "";

    /**
     * @return the side f this config entry. If it returns SERVER, this entry will be universal.
     */
    Side side() default Side.SERVER;
}

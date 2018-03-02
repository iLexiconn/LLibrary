package net.ilexiconn.llibrary.server.asm;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation injected into RuntimePatchers by LLibraryTransformer to indicate that the class has been transformed.
 * This annotation is not to be implemented manually.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Transformed {}

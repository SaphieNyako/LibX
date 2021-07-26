package io.github.noeppi_noeppi.libx.annotation.config;

import java.lang.annotation.*;

/**
 * Automatically registers a class as a LibX config.
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface RegisterConfig {

    /**
     * The name to use for the config.
     */
    String value() default "config";

    /**
     * Whether the config is a client config.
     */
    boolean client() default false;
}

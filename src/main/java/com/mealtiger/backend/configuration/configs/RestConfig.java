package com.mealtiger.backend.configuration.configs;

import com.mealtiger.backend.configuration.annotations.Config;
import com.mealtiger.backend.configuration.annotations.ConfigNode;

/**
 * Rest Config File.
 * Properties are represented as non-static fields. Categories can be done by nesting the properties in static nested classes.
 *
 * @see com.mealtiger.backend.configuration.Configurator
 */
@Config(name = "REST", configPath = "rest.yml")
@SuppressWarnings("unused")
public class RestConfig {

    private final String corsOrigins;

    public RestConfig() {
        this.corsOrigins = "*";
    }

    @ConfigNode(name = "corsAllowedOrigins", envKey = "ALLOWED_ORIGINS")
    public String getAllowedOrigins() {
        return corsOrigins;
    }

}

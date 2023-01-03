package com.mealtiger.backend.configuration.configs;

import com.mealtiger.backend.configuration.annotations.Config;
import com.mealtiger.backend.configuration.annotations.ConfigNode;
import org.springframework.boot.logging.LogLevel;

/**
 * Main config file.
 * Properties are represented as non-static fields. Categories can be done by nesting the properties in static nested classes.
 */

@Config(name = "Main", configPath = "main.yml")
@SuppressWarnings("unused")
public class MainConfig {
    private final Logging logging;

    @SuppressWarnings("unused")
    public MainConfig() {
        logging = new Logging();
    }

    @ConfigNode(name = "Logging.logLevel", envKey = "LOGLEVEL", springProperties = "logging.level.root")
    @SuppressWarnings("unused")
    public String getLogLevel() {
        return logging.logLevel;
    }

    static class Logging {
        private final String logLevel;

        Logging() {
            logLevel = LogLevel.INFO.toString();
        }
    }

}

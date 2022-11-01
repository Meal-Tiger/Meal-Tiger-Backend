package com.mealtiger.backend.configuration;

import com.mealtiger.backend.configuration.annotations.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;

/**
 * This class is used to load configs.
 *
 * @see com.mealtiger.backend.configuration.Configurator
 * @author Lucca Greschner
 */
public class ConfigLoader {

    private static final Logger log = LoggerFactory.getLogger(ConfigLoader.class);

    private final Yaml yaml;

    ConfigLoader() {
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        yaml = new Yaml(options);
        yaml.setBeanAccess(BeanAccess.FIELD);
    }

    /**
     * Loads the config file.
     *
     * @return instance of the Config class representing the configuration specified in the config file.
     * @throws IOException if an I/O-Error occurs.
     */
    Object loadConfig(Class<?> configClass) throws IOException {
        if (!configClass.isAnnotationPresent(Config.class)){
            throw new RuntimeException("Not a config!");
        }

        Config annotation = configClass.getAnnotation(Config.class);
        File file = new File(annotation.configPath());

        if (file.createNewFile()) {

            log.info("Creating new config file {} in the working directory.", file.getName());

            Object newConfig;
            try {
                newConfig = configClass.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                     InvocationTargetException e) {
                log.error("Could not create new config. Aborting...");
                throw new RuntimeException(e);
            }

            try (PrintWriter out = new PrintWriter(file)) {
                yaml.dump(newConfig, out);
            }

            return newConfig;

        } else {

            log.debug("Loading config file from {}.", file.getAbsolutePath());

            return yaml.load(Files.readString(file.toPath()));
        }


    }
}
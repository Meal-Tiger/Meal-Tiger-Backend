package com.mealtiger.backend.configuration;

import com.mealtiger.backend.configuration.configs.Config;
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
import java.util.HashMap;
import java.util.Map;


public class ConfigLoader {

    private static final Logger log = LoggerFactory.getLogger(ConfigLoader.class);

    private static final Map<File, Config> loadedConfigs = new HashMap<>();

    private final Yaml yaml;

    public ConfigLoader() {
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
    public Config loadConfig(Class<? extends Config> clazz) throws IOException {
        File file = null;
        try {
            file = new File((String) clazz.getField("configPath").get(""));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            log.error("Config path not set in Config class {}!", clazz.getName());
        }

        assert file != null;

        if (loadedConfigs.containsKey(file)) {
            log.debug("Using loaded config from {}", file.getAbsolutePath());
            return loadedConfigs.get(file);
        } else {
            if (file.createNewFile()) {

                log.info("Creating new config file {} in the working directory.", file.getName());

                Config newConfig;
                try {
                    newConfig = clazz.getConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                         InvocationTargetException e) {
                    log.error("Could not create new config. Aborting...");
                    throw new RuntimeException(e);
                }

                try (PrintWriter out = new PrintWriter(file)) {
                    yaml.dump(newConfig, out);
                }

                loadedConfigs.put(file, newConfig);

                return newConfig;

            } else {

                log.debug("Loading config file from {}.", file.getAbsolutePath());

                Config config = yaml.load(Files.readString(file.toPath()));

                loadedConfigs.put(file, config);

                return config;
            }
        }
    }
}
package com.mealtiger.backend.configuration;

import com.mealtiger.backend.configuration.annotations.Config;
import com.mealtiger.backend.configuration.exceptions.ConfigLoadingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.List;

/**
 * This class is used to load configs.
 *
 * @author Lucca Greschner
 * @see com.mealtiger.backend.configuration.Configurator
 */
public class ConfigLoader {

    private static final Logger log = LoggerFactory.getLogger(ConfigLoader.class);

    private final Yaml yaml;

    ConfigLoader(List<TypeDescription> typeDescriptionList) {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setPrettyFlow(true);
        dumperOptions.setProcessComments(true);

        Representer representer = new Representer(dumperOptions);
        representer.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        LoaderOptions loaderOptions = new LoaderOptions();

        org.yaml.snakeyaml.constructor.Constructor constructor = new org.yaml.snakeyaml.constructor.Constructor(loaderOptions);

        for (TypeDescription typeDescription : typeDescriptionList) {
            constructor.addTypeDescription(typeDescription);
            representer.addTypeDescription(typeDescription);
        }

        yaml = new Yaml(constructor, representer);
        yaml.setBeanAccess(BeanAccess.FIELD);
    }

    /**
     * Loads the config file.
     *
     * @return instance of the Config class representing the configuration specified in the config file.
     * @throws IOException if an I/O-Error occurs.
     */
    Object loadConfig(Class<?> configClass) throws IOException {
        if (!configClass.isAnnotationPresent(Config.class)) {
            throw new ConfigLoadingException(configClass.getSimpleName() + " is not a config!");
        }

        Config annotation = configClass.getAnnotation(Config.class);
        File file = new File(annotation.configPath());

        if (file.createNewFile()) {

            log.info("Creating new config file {} in the working directory.", file.getName());

            if (!annotation.sampleConfig().isEmpty()) {
                String sampleConfigPath = annotation.sampleConfig();

                try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(sampleConfigPath);
                     FileWriter fileWriter = new FileWriter(file)) {
                    assert inputStream != null;
                    String sampleConfigString = new String(inputStream.readAllBytes());
                    fileWriter.write(sampleConfigString);

                    return yaml.load(sampleConfigString);
                }
            } else {
                Object newConfig;
                try {
                    newConfig = configClass.getConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                         InvocationTargetException e) {
                    log.error("Could not create new config. Aborting...");
                    throw new ConfigLoadingException(e);
                }

                try (PrintWriter out = new PrintWriter(file)) {
                    yaml.dump(newConfig, out);
                }

                return newConfig;
            }

        } else {

            log.debug("Loading config file from {}.", file.getAbsolutePath());

            return yaml.load(Files.readString(file.toPath()));
        }


    }
}
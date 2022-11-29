package com.mealtiger.backend.configuration;

import com.mealtiger.backend.configuration.annotations.Config;
import com.mealtiger.backend.configuration.annotations.ConfigNode;
import com.mealtiger.backend.configuration.exceptions.ConfigLoadingException;
import com.mealtiger.backend.configuration.exceptions.ConfigPropertyException;
import com.mealtiger.backend.configuration.exceptions.NoSuchConfigException;
import com.mealtiger.backend.configuration.exceptions.NoSuchPropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.AnnotatedTypeScanner;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * This class serves as a means to load configs and to get properties from them.
 * It also manages config instances similar to the Singleton design pattern.
 *
 * @author Lucca Greschner
 */
public class Configurator {

    private static final Logger log = LoggerFactory.getLogger(Configurator.class);

    private static final Map<String, Object> loadedConfigs = new HashMap<>();

    public Configurator() {
        if (loadedConfigs.isEmpty()) {
            log.trace("Starting to load configs due to empty loadedConfigs Map!");
            try {
                loadConfigs();
            } catch (IOException e) {
                log.error("IO Error when trying to load configs. Check the permissions on the config files!");
                throw new ConfigLoadingException(e);
            }
        }
    }

    // APPLICATION SETUP

    /**
     * Loads all config files in the com.mealtiger.backend.configuration.configs package annotated with the @Config annotation
     *
     * @throws IOException When an IO error occurs upon trying to open the corresponding config files.
     * @see Config
     */
    private void loadConfigs() throws IOException {
        log.info("Loading configs...");

        AnnotatedTypeScanner annotatedTypeScanner = new AnnotatedTypeScanner(false, Config.class);

        Set<Class<?>> configSet = annotatedTypeScanner.findTypes("com.mealtiger.backend.configuration.configs");

        for (Class<?> configClass : configSet) {
            Config annotation = configClass.getAnnotation(Config.class);

            String configName = annotation.name();

            ConfigLoader configLoader = new ConfigLoader();
            Object config = configLoader.loadConfig(configClass);

            loadedConfigs.put(configName, config);
        }

        log.debug("Finished loading configs!");
    }

    /**
     * @return The Spring properties to be used when starting the Spring application.
     */
    public Properties getSpringProperties() {
        Properties properties = new Properties();

        String logLevel = getString("Main.Logging.logLevel");
        properties.put("logging.level.root", logLevel);

        log.debug("Handing over spring properties: {}!", properties);

        return properties;
    }

    // CONFIG QUERYING

    /**
     * @param property Property string to get from configs. Example: Main.Database.mongoDBURL
     * @return Object that corresponds to the property string
     */
    private Object getProperty(String property) throws NoSuchConfigException {
        log.trace("Getting property {}.", property);

        String[] paths = property.split("\\.");
        Object config = loadedConfigs.get(paths[0]);

        if (config == null) {
            throw new NoSuchConfigException(paths[0]);
        }

        String propertyDescriptor = property.substring(paths[0].length() + 1);

        Object returnValue = null;

        Method[] configMethods = config.getClass().getMethods();

        for (Method method : configMethods) {
            if (method.isAnnotationPresent(ConfigNode.class) && method.getAnnotation(ConfigNode.class).name().equals(propertyDescriptor)) {
                String envKey = method.getAnnotation(ConfigNode.class).envKey();
                String envValue = envKey.length() != 0 ? System.getenv(envKey) : "";

                if (envKey.length() != 0) {
                    Class<?> returnType = method.getReturnType();

                    try {
                        if (returnType == String.class) {
                            returnValue = envValue;
                        } else if (returnType == Integer.class) {
                            returnValue = Integer.valueOf(envValue);
                        } else if (returnType == Boolean.class) {
                            switch (envValue.toLowerCase()) {
                                case "true" -> returnValue = Boolean.TRUE;
                                case "false" -> returnValue = Boolean.FALSE;
                                default -> throw new IllegalArgumentException();
                            }
                        } else if (returnType == Double.class) {
                            returnValue = Double.valueOf(envValue);
                        }
                    } catch (Exception e) {
                        log.error("Environment variable {} cannot be parsed. Proceeding with config value!", envKey);
                    }
                }

                if (returnValue == null) {
                    try {
                        returnValue = method.invoke(config);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new ConfigPropertyException(property);
                    }
                }
            }
        }

        return returnValue;
    }

    /**
     * Used to get a boolean from a config file.
     *
     * @param property Property string to get from configs. Example: Main.Database.mongoDBURL
     * @return Boolean saved at the given property descriptor.
     * @throws NoSuchPropertyException is thrown when there is no property as provided.
     */
    public boolean getBoolean(String property) throws NoSuchPropertyException, NoSuchConfigException {
        log.debug("Getting boolean property {}.", property);

        Object returnValue = getProperty(property);

        if (!(returnValue instanceof Boolean booleanReturnValue)) {
            throw new NoSuchPropertyException(property);
        } else return booleanReturnValue;
    }

    /**
     * Used to get a String from a config file.
     *
     * @param property Property string to get from configs. Example: Main.Database.mongoDBURL
     * @return Boolean saved at the given property descriptor.
     * @throws NoSuchPropertyException is thrown when there is no property as provided.
     */
    public String getString(String property) throws NoSuchPropertyException, NoSuchConfigException {
        log.debug("Getting String property {}.", property);

        Object returnValue = getProperty(property);

        if (!(returnValue instanceof String stringReturnValue)) {
            throw new NoSuchPropertyException(property);
        } else return stringReturnValue;
    }

    /**
     * Used to get an integer from a config file.
     *
     * @param property Property string to get from configs. Example: Main.Database.mongoDBURL
     * @return Boolean saved at the given property descriptor.
     * @throws NoSuchPropertyException is thrown when there is no property as provided.
     */
    public int getInteger(String property) throws NoSuchPropertyException, NoSuchConfigException {
        log.debug("Getting integer property {}.", property);

        Object returnValue = getProperty(property);

        if (!(returnValue instanceof Integer integerReturnValue)) {
            throw new NoSuchPropertyException(property);
        } else return integerReturnValue;
    }

    /**
     * Used to get a double from a config file.
     *
     * @param property Property string to get from configs. Example: Main.Database.mongoDBURL
     * @return Boolean saved at the given property descriptor.
     * @throws NoSuchPropertyException is thrown when there is no property as provided.
     */
    public double getDouble(String property) throws NoSuchPropertyException, NoSuchConfigException {
        log.debug("Getting double property {}.", property);

        Object returnValue = getProperty(property);

        if (!(returnValue instanceof Double doubleReturnValue)) {
            throw new NoSuchPropertyException(property);
        } else return doubleReturnValue;
    }
}

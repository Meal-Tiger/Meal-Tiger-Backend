package com.mealtiger.backend.configuration;

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
            try {
                loadConfigs();
            } catch (IOException e) {
                log.error("IO Error when trying to load configs. Check the permissions on the config files!");
                throw new RuntimeException(e);
            }
        }
    }

    // APPLICATION SETUP

    /**
     * Loads all config files in the com.mealtiger.backend.configuration.configs package annotated with the @Config annotation
     * @see Config
     * @throws IOException When an IO error occurs upon trying to open the corresponding config files.
     */
    private void loadConfigs() throws IOException {
        AnnotatedTypeScanner annotatedTypeScanner = new AnnotatedTypeScanner(false, Config.class);

        Set<Class<?>> configSet = annotatedTypeScanner.findTypes("com.mealtiger.backend.configuration.configs");

        for (Class<?> configClass : configSet) {
            Config annotation = configClass.getAnnotation(Config.class);

            String configName = annotation.name();

            ConfigLoader configLoader = new ConfigLoader();
            Object config = configLoader.loadConfig(configClass);

            loadedConfigs.put(configName, config);
        }
    }

    /**
     * @return The Spring properties to be used when starting the Spring application.
     */
    public Properties getSpringProperties() {
        Properties properties = new Properties();

        try {
            String logLevel = getString("Main.Logging.logLevel");

            properties.put("logging.level.root", logLevel);
        } catch (NoSuchPropertyException e) {
            throw new RuntimeException(e);
        }

        return properties;
    }

    // CONFIG QUERYING

    /**
     * @param property Property string to get from configs. Example: Main.Database.mongoDBURL
     * @return Object that corresponds to the property string
     */
    private Object getProperty(String property) {
        String[] paths = property.split("\\.");
        Object config = loadedConfigs.get(paths[0]);

        String propertyDescriptor = property.substring(paths[0].length() + 1);

        System.out.println(propertyDescriptor);

        Object returnValue = null;

        Method[] configMethods = config.getClass().getMethods();

        for (Method method : configMethods) {
            if(method.isAnnotationPresent(ConfigNode.class) && method.getAnnotation(ConfigNode.class).name().equals(propertyDescriptor)) {
                try {
                    returnValue = method.invoke(config);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("Error when trying to retrieve config property " + property + "!");
                }
            }
        }

        return returnValue;
    }

    /**
     * Used to get a boolean from a config file.
     * @param property Property string to get from configs. Example: Main.Database.mongoDBURL
     * @return Boolean saved at the given property descriptor.
     * @throws NoSuchPropertyException is thrown when there is no property as provided.
     */
    public boolean getBoolean(String property) throws NoSuchPropertyException {
        Object returnValue = getProperty(property);

        if(!(returnValue instanceof Boolean)) {
            throw new NoSuchPropertyException(property);
        } else return (Boolean) returnValue;
    }

    /**
     * Used to get a String from a config file.
     * @param property Property string to get from configs. Example: Main.Database.mongoDBURL
     * @return Boolean saved at the given property descriptor.
     * @throws NoSuchPropertyException is thrown when there is no property as provided.
     */
    public String getString(String property) throws NoSuchPropertyException {
        Object returnValue = getProperty(property);

        if(!(returnValue instanceof String)) {
            throw new NoSuchPropertyException(property);
        } else return (String) returnValue;
    }

    /**
     * Used to get an integer from a config file.
     * @param property Property string to get from configs. Example: Main.Database.mongoDBURL
     * @return Boolean saved at the given property descriptor.
     * @throws NoSuchPropertyException is thrown when there is no property as provided.
     */
    public int getInteger(String property) throws NoSuchPropertyException {
        Object returnValue = getProperty(property);

        if(!(returnValue instanceof Integer)) {
            throw new NoSuchPropertyException(property);
        } else return (Integer) returnValue;
    }

    /**
     * Used to get a double from a config file.
     * @param property Property string to get from configs. Example: Main.Database.mongoDBURL
     * @return Boolean saved at the given property descriptor.
     * @throws NoSuchPropertyException is thrown when there is no property as provided.
     */
    public double getDouble(String property) throws NoSuchPropertyException {
        Object returnValue = getProperty(property);

        if(!(returnValue instanceof Double)) {
            throw new NoSuchPropertyException(property);
        } else return (Double) returnValue;
    }
}

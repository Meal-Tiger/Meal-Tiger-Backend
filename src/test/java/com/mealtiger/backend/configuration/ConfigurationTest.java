package com.mealtiger.backend.configuration;

import com.mealtiger.backend.configuration.configs.TestConfig;
import com.mealtiger.backend.configuration.exceptions.NoSuchConfigException;
import com.mealtiger.backend.configuration.exceptions.NoSuchPropertyException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigurationTest {

    @Test
    void loadConfigsTest() throws NoSuchFieldException, IllegalAccessException {
        Configurator configurator = new Configurator();

        Field loadedConfigsField = configurator.getClass().getDeclaredField("loadedConfigs");
        loadedConfigsField.setAccessible(true);

        Map<String, Object> loadedConfigs = (Map<String, Object>) loadedConfigsField.get(configurator);

        assertFalse(loadedConfigs.isEmpty());
    }

    @Test
    void loadSingleConfigTest() throws IOException, NoSuchPropertyException, NoSuchConfigException {
        ConfigLoader configLoader = new ConfigLoader();
        TestConfig testConfig = (TestConfig) configLoader.loadConfig(TestConfig.class);

        assertEquals("I'm a sample!", testConfig.getSampleNode());

        Configurator configurator = new Configurator();
        String output = configurator.getString("Test.Sample.Node");

        assertEquals("I'm a sample!", output);

    }

    // NEGATIVE TESTS

    @Test
    void noSuchPropertyExceptionTest() {
        Configurator configurator = new Configurator();

        assertThrows(NoSuchPropertyException.class, () -> configurator.getString("Test.Other.Node"));
    }

    @Test
    void noSuchConfigExceptionTest() {
        Configurator configurator = new Configurator();

        assertThrows(NoSuchConfigException.class, () -> configurator.getString("Some.Non.Existing.Config"));
    }

}

package com.mealtiger.backend.configuration;

import com.mealtiger.backend.configuration.configs.TestConfig;
import com.mealtiger.backend.configuration.exceptions.NoSuchConfigException;
import com.mealtiger.backend.configuration.exceptions.NoSuchPropertyException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.TypeDescription;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class ConfigurationTest {

    @Test
    void loadConfigsTest() throws NoSuchFieldException, IllegalAccessException {
        Configurator configurator = new Configurator();

        Field loadedConfigsField = configurator.getClass().getDeclaredField("loadedConfigs");
        loadedConfigsField.setAccessible(true);

        Map<?, ?> loadedConfigs = (Map<?, ?>) loadedConfigsField.get(configurator);

        assertFalse(loadedConfigs.isEmpty());
    }

    @Test
    void loadSingleConfigTest() throws IOException, NoSuchPropertyException, NoSuchConfigException {
        ConfigLoader configLoader = new ConfigLoader(List.of(new TypeDescription(TestConfig.class, "Test")));
        TestConfig testConfig = (TestConfig) configLoader.loadConfig(TestConfig.class);

        assertEquals("I'm a sample!", testConfig.getSampleNode());

        Configurator configurator = new Configurator();
        String output = configurator.getString("Test.Sample.Node");

        assertEquals("I'm a sample!", output);

    }

    @Test
    void environmentVariableTest() {
        Map<String, String> environmentVariables = new HashMap<>();
        environmentVariables.put("TEST_VARIABLE", "testValue");
        Configurator configurator = new Configurator(environmentVariables);

        assertEquals("testValue", configurator.getString("Test.Sample.Env.Node"));
    }

    @Test
    void springPropertiesTest() {
        Configurator configurator = new Configurator();

        assertEquals("testValue", configurator.getSpringProperties().getProperty("some.sample.property"));

        Map<String, String> environmentVariables = new HashMap<>();
        environmentVariables.put("SPRING_PROPERTY", "testValue2");
        configurator = new Configurator(environmentVariables);

        assertEquals("testValue2", configurator.getSpringProperties().getProperty("some.sample.property"));
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

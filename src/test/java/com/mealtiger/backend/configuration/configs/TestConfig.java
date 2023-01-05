package com.mealtiger.backend.configuration.configs;

import com.mealtiger.backend.configuration.annotations.Config;
import com.mealtiger.backend.configuration.annotations.ConfigNode;

@Config(name = "Test", configPath = "test.yml")
@SuppressWarnings("unused")
public class TestConfig {
    @ConfigNode(name = "Sample.Node")
    public String getSampleNode() {
        return "I'm a sample!";
    }

    @ConfigNode(name = "Sample.Spring.Property.Node", springProperties = "some.sample.property")
    public String getSampleSpringPropertyNode() {
        return "testValue";
    }

    @ConfigNode(name = "Sample.Env.Node", envKey = "TEST_VARIABLE")
    public String getSampleEnvNode() {
        return "testValue that should not be retrieved!";
    }
}

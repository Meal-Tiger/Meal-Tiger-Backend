package com.mealtiger.backend.configuration.configs;

import com.mealtiger.backend.configuration.Config;
import com.mealtiger.backend.configuration.ConfigNode;

@Config(name = "Test", configPath = "test.yml")
public class TestConfig {
    @ConfigNode(name = "Sample.Node")
    public String getSampleNode() {
        return "I'm a sample!";
    }
}

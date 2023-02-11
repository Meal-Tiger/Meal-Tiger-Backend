package com.mealtiger.backend;

import com.mealtiger.backend.configuration.annotations.Config;
import org.springframework.data.util.AnnotatedTypeScanner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Set;

public class UnitTestConfigSetup {

    public static void setupConfigs() {
        AnnotatedTypeScanner annotatedTypeScanner = new AnnotatedTypeScanner(false, Config.class);

        Set<Class<?>> configSet = annotatedTypeScanner.findTypes("com.mealtiger.backend.configuration.configs");
        
        configSet.forEach(config -> {
            String configPathString = config.getAnnotation(Config.class).configPath();
            Path configPath = Path.of(configPathString);
            if (Files.exists(configPath, LinkOption.NOFOLLOW_LINKS)) {
                try {
                    Path backupLocation = Path.of("configBackup");
                    if (!Files.exists(backupLocation)) {
                        Files.createDirectory(backupLocation);
                    }
                    Files.move(configPath, Path.of("configBackup", configPathString));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static void teardownConfigs() {
        AnnotatedTypeScanner annotatedTypeScanner = new AnnotatedTypeScanner(false, Config.class);

        Set<Class<?>> configSet = annotatedTypeScanner.findTypes("com.mealtiger.backend.configuration.configs");

        configSet.forEach(config -> {
            String configPathString = config.getAnnotation(Config.class).configPath();
            Path configPath = Path.of(configPathString);
            try {
                Files.deleteIfExists(configPath);
                Path backupPath = Path.of("configBackup", configPathString);
                if (Files.exists(backupPath)) {
                    Files.move(backupPath, configPath);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        try {
            Files.deleteIfExists(Path.of("configBackup"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

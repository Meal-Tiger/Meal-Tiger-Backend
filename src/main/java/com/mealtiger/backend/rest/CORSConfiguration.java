package com.mealtiger.backend.rest;

import com.mealtiger.backend.configuration.Configurator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * This class is used to configure the CORS parameters.
 */
@Configuration
public class CORSConfiguration {

    private final Configurator configurator;

    public CORSConfiguration(Configurator configurator) {
        this.configurator = configurator;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                String allowedOrigins = configurator.getString("REST.corsAllowedOrigins");

                String[] allowedOriginsArray = allowedOrigins.split(",");

                registry.addMapping("/recipes").allowedMethods("GET", "POST").allowedOrigins(allowedOriginsArray);
                registry.addMapping("/recipes/**").allowedMethods("GET", "POST", "PUT", "DELETE").allowedOrigins(allowedOriginsArray);
                registry.addMapping("/image").allowedMethods("POST").allowedOrigins(allowedOriginsArray);
                registry.addMapping("/images").allowedMethods("POST").allowedOrigins(allowedOriginsArray);
                registry.addMapping("/image/**").allowedMethods("GET", "POST", "PUT", "DELETE").allowedOrigins(allowedOriginsArray);
                registry.addMapping("/user").allowedMethods("GET", "POST", "PUT").allowedOrigins(allowedOriginsArray);
                registry.addMapping("/user/**").allowedMethods("GET").allowedOrigins(allowedOriginsArray);
            }
        };
    }

}

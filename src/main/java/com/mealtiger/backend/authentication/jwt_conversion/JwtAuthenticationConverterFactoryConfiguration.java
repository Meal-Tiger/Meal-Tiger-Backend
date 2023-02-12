package com.mealtiger.backend.authentication.jwt_conversion;

import com.mealtiger.backend.authentication.jwt_conversion.converters.JwtAuthenticationConverter;
import com.mealtiger.backend.configuration.Configurator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtAuthenticationConverterFactoryConfiguration {

    private final Configurator configurator;

    public JwtAuthenticationConverterFactoryConfiguration(Configurator configurator) {
        this.configurator = configurator;
    }

    @Bean
    public JwtAuthenticationConverter getJwtAuthenticationConverter() {
        String authenticationProvider = configurator.getString("Authentication.OIDC.authenticationProvider");

        JwtAuthenticationConverterFactory factory = new JwtAuthenticationConverterFactory(
                JwtAuthenticationConverterFactory.AuthenticationProvider.valueOf(authenticationProvider));
        return factory.getObject();
    }

}

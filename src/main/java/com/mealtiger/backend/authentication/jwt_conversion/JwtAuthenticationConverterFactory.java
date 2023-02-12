package com.mealtiger.backend.authentication.jwt_conversion;

import com.mealtiger.backend.authentication.jwt_conversion.converters.JwtAuthenticationConverter;
import com.mealtiger.backend.authentication.jwt_conversion.converters.KeycloakJwtAuthenticationConverter;
import org.springframework.beans.factory.FactoryBean;

import java.util.Objects;

public class JwtAuthenticationConverterFactory implements FactoryBean<JwtAuthenticationConverter> {
    
    public enum AuthenticationProvider {
        KEYCLOAK
    }
    
    private final AuthenticationProvider authenticationProvider;
    
    public JwtAuthenticationConverterFactory(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }
    
    @Override
    public JwtAuthenticationConverter getObject() {
        switch (authenticationProvider) {
            case KEYCLOAK -> {
                return new KeycloakJwtAuthenticationConverter();
            }
            default -> throw new IllegalArgumentException("Unsupported authentication provider!");
        }
    }

    @Override
    public Class<?> getObjectType() {
        return Objects.requireNonNull(getObject()).getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}

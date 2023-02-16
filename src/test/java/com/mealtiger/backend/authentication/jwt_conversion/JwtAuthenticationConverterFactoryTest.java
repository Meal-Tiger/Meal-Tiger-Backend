package com.mealtiger.backend.authentication.jwt_conversion;

import com.mealtiger.backend.authentication.jwt_conversion.converters.KeycloakJwtAuthenticationConverter;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
class JwtAuthenticationConverterFactoryTest {

    @Test
    void instantiationTest() {
        JwtAuthenticationConverterFactory factory = new JwtAuthenticationConverterFactory(JwtAuthenticationConverterFactory.AuthenticationProvider.KEYCLOAK);

        assertEquals(Objects.requireNonNull(factory.getObject()).getClass(), KeycloakJwtAuthenticationConverter.class);
    }

    @Test
    void getObjectTypeTest() {
        JwtAuthenticationConverterFactory factory = new JwtAuthenticationConverterFactory(JwtAuthenticationConverterFactory.AuthenticationProvider.KEYCLOAK);

        assertEquals(factory.getObjectType(), KeycloakJwtAuthenticationConverter.class);
    }

}

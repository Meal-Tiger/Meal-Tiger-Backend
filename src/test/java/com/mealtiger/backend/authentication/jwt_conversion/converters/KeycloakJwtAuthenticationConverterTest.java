package com.mealtiger.backend.authentication.jwt_conversion.converters;

import com.mealtiger.backend.configuration.Configurator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("unit")
class KeycloakJwtAuthenticationConverterTest {

    /**
     * Tests the role extraction in CustomJwtAuthenticationConverterTest
     */
    @Test
    void roleExtractionTest() {
        Configurator configurator = new Configurator();
        String clientIdString = configurator.getString("Authentication.OIDC.clientID");

        KeycloakJwtAuthenticationConverter converter = new KeycloakJwtAuthenticationConverter();

        Map<String, Object> roles = new HashMap<>();
        roles.put("roles", List.of("test#1",
                "test#2",
                "test#3"));

        Map<String, Object> clientId = new HashMap<>();
        clientId.put(clientIdString, roles);

        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaim("resource_access")).thenReturn(clientId);

        AbstractAuthenticationToken token = converter.convert(jwt);
        assert token != null;
        Collection<GrantedAuthority> grantedAuthorities = token.getAuthorities();

        assertTrue(grantedAuthorities.stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_test#1")));
        assertTrue(grantedAuthorities.stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_test#2")));
        assertTrue(grantedAuthorities.stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_test#3")));
    }

}

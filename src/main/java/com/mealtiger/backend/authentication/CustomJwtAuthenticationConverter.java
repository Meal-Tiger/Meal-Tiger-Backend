package com.mealtiger.backend.authentication;

import com.mealtiger.backend.configuration.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This custom JWT authentication converter guarantees that Spring recognizes roles set in keycloak.
 */
public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final Logger log = LoggerFactory.getLogger(CustomJwtAuthenticationConverter.class);


    /**
     * Extracts the resource roles from the JWT
     *
     * @param jwt JWT to extract roles from.
     * @return Collection of granted authorities
     */
    private static Collection<? extends GrantedAuthority> extractResourceRoles(final Jwt jwt) {
        Configurator configurator = new Configurator();
        String clientID = configurator.getString("Authentication.OIDC.authenticationProviderURL");

        log.trace("Got client id {} from configuration!", clientID);

        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        if (resourceAccess == null) return Collections.emptySet();

        @SuppressWarnings("unchecked")
        Map<String, Object> resource = (Map<String, Object>) resourceAccess.get(clientID);
        if (resource == null) return Collections.emptySet();

        @SuppressWarnings("unchecked")
        Collection<String> resourceRoles = (Collection<String>) resource.get("roles");
        if (resourceRoles == null) return Collections.emptySet();

        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }

    /**
     * Converts the Jwt into an JwtAuthenticationToken with the extracted authorities added.
     *
     * @param source the source object to convert, which must be an instance of {@code S} (never {@code null})
     * @return JwtAuthenticationToken with the extracted authorities added.
     */
    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        Collection<? extends GrantedAuthority> authorities = extractResourceRoles(source);

        log.trace("Got the following resource roles from JWT: {}", authorities);

        return new JwtAuthenticationToken(source, authorities);
    }
}
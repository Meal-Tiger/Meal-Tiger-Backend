package com.mealtiger.backend.authentication;

import com.mealtiger.backend.configuration.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration for Spring Security
 */
@Configuration
public class AuthenticationConfiguration {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationConfiguration.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        Configurator configurator = new Configurator();

        if(configurator.getBoolean("Authentication.OIDC.oidcAuthenticationStatus")) {
            log.trace("Going through OIDC security filter chain!");

            final String jwtIssuerURL = configurator.getString("Authentication.OIDC.authenticationProviderURL");

            log.trace("Got jwt issuer URI {} from configuration!", jwtIssuerURL);

            // Routes

            final String recipes = "/recipes/**";

            http.authorizeRequests(authorizeRequests -> authorizeRequests
                            .antMatchers(HttpMethod.POST, recipes).authenticated()
                            .antMatchers(HttpMethod.PUT, recipes).authenticated()
                            .antMatchers(HttpMethod.DELETE, recipes).authenticated())
                    .oauth2ResourceServer(oauth2ResourceServer ->
                            oauth2ResourceServer
                                    .jwt(jwt -> jwt.decoder(JwtDecoders.fromIssuerLocation(jwtIssuerURL)))
                                    .jwt().jwtAuthenticationConverter(new CustomJwtAuthenticationConverter())
                    );
        }

        //TODO: Figure out what CSRF is and configure postman to cope with it.
        http.csrf().disable();
        http.cors();

        return http.build();
    }
}

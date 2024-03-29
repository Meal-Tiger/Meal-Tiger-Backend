package com.mealtiger.backend.authentication;

import com.mealtiger.backend.authentication.jwt_conversion.converters.JwtAuthenticationConverter;
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
 *
 * @author Lucca Greschner
 */
@Configuration
public class AuthenticationConfiguration {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationConfiguration.class);

    private final Configurator configurator;

    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public AuthenticationConfiguration(Configurator configurator, JwtAuthenticationConverter jwtAuthenticationConverter) {
        this.configurator = configurator;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    /**
     * This security filter chain provides authentication through JWTs which is coordinated through a Keycloak Authentication Provider
     * @param http Automatically injected by spring
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        if(configurator.getBoolean("Authentication.OIDC.oidcAuthenticationStatus")) {
            log.trace("Going through OIDC security filter chain!");

            final String jwtIssuerURL = configurator.getString("Authentication.OIDC.authenticationProviderURL");

            log.trace("Got jwt issuer URI {} from configuration!", jwtIssuerURL);

            // Routes

            final String recipes = "/recipes/**";
            final String userWithoutId = "/user";
            final String userRecipes = "/user/recipes";
            final String userImages = "/user/images";
            final String user = "/user/**";
            final String images = "/images";
            final String image = "/image/**";

            http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                            .requestMatchers(HttpMethod.POST, recipes).authenticated()
                            .requestMatchers(HttpMethod.PUT, recipes).authenticated()
                            .requestMatchers(HttpMethod.DELETE, recipes).authenticated()
                            .requestMatchers(HttpMethod.GET, userWithoutId).authenticated()
                            .requestMatchers(HttpMethod.GET, userRecipes).authenticated()
                            .requestMatchers(HttpMethod.GET, userImages).authenticated()
                            .requestMatchers(HttpMethod.PUT, user).authenticated()
                            .requestMatchers(HttpMethod.POST, user).authenticated()
                            .requestMatchers(HttpMethod.POST, images).authenticated()
                            .requestMatchers(HttpMethod.DELETE, image).authenticated()
                            .requestMatchers(HttpMethod.POST, image).authenticated()
                            .anyRequest().permitAll()
                    )
                    .oauth2ResourceServer(oauth2ResourceServer ->
                            oauth2ResourceServer
                                    .jwt(jwt -> jwt.decoder(JwtDecoders.fromIssuerLocation(jwtIssuerURL))
                                            .jwtAuthenticationConverter(jwtAuthenticationConverter))
                    );
        }

        //CSRF protection is not needed as we are using HTTP Bearer authentication
        http.csrf().disable();
        http.cors();

        return http.build();
    }
}

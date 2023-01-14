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
 *
 * @author Lucca Greschner
 */
@Configuration
public class AuthenticationConfiguration {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationConfiguration.class);

    private final Configurator configurator;

    public AuthenticationConfiguration(Configurator configurator) {
        this.configurator = configurator;
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

            http.authorizeRequests(authorizeRequests -> authorizeRequests
                            .antMatchers(HttpMethod.POST, recipes).authenticated()
                            .antMatchers(HttpMethod.PUT, recipes).authenticated()
                            .antMatchers(HttpMethod.DELETE, recipes).authenticated()
                            .antMatchers(HttpMethod.GET, userWithoutId).authenticated()
                            .antMatchers(HttpMethod.GET, userRecipes).authenticated()
                            .antMatchers(HttpMethod.GET, userImages).authenticated()
                            .antMatchers(HttpMethod.PUT, user).authenticated()
                            .antMatchers(HttpMethod.POST, user).authenticated()
                            .antMatchers(HttpMethod.POST, images).authenticated()
                            .antMatchers(HttpMethod.DELETE, image).authenticated()
                            .antMatchers(HttpMethod.POST, image).authenticated()
                    )
                    .oauth2ResourceServer(oauth2ResourceServer ->
                            oauth2ResourceServer
                                    .jwt(jwt -> jwt.decoder(JwtDecoders.fromIssuerLocation(jwtIssuerURL))
                                            .jwtAuthenticationConverter(new CustomJwtAuthenticationConverter()))
                    );
        }

        //CSRF protection is not needed as we are using HTTP Bearer authentication
        http.csrf().disable();
        http.cors();

        return http.build();
    }
}

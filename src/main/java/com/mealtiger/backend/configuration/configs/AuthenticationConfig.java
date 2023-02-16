package com.mealtiger.backend.configuration.configs;

import com.mealtiger.backend.configuration.annotations.Config;
import com.mealtiger.backend.configuration.annotations.ConfigNode;

/**
 * Authentication Config File
 * Properties are represented as non-static fields. Categories can be done by nesting the properties in static nested classes.
 *
 * @see com.mealtiger.backend.configuration.Configurator
 */

@Config(name = "Authentication", configPath = "auth.yml", sampleConfig = "configuration-samples/auth.sample.yml")
@SuppressWarnings("unused")
public class AuthenticationConfig {

    private final OIDC oidc;

    @SuppressWarnings("unused")
    public AuthenticationConfig() {
        this.oidc = new OIDC();
    }

    @ConfigNode(name = "OIDC.oidcAuthenticationStatus", envKey = "OIDC_AUTHENTICATION_ENABLED")
    public boolean isOIDCAuthenticationEnabled() {
        return oidc.enableOIDCAuthentication;
    }

    @ConfigNode(name = "OIDC.authenticationProviderURL", envKey = "OIDC_AUTHENTICATION_PROVIDER_URL")
    public String getAuthenticationProviderURL() {
        return oidc.authenticationProviderURL;
    }

    @ConfigNode(name = "OIDC.authenticationProvider", envKey = "OIDC_AUTHENTICATION_PROVIDER")
    public String getAuthenticationProvider() {
        return oidc.authenticationProvider;
    }

    @ConfigNode(name = "OIDC.clientID", envKey = "OIDC_CLIENT_ID")
    public String getOIDCClientID() {
        return oidc.clientID;
    }

    @ConfigNode(name = "OIDC.adminRole", envKey = "OIDC_ADMIN_ROLE")
    public String getAdminRole() {
        return oidc.adminRole;
    }

    static class OIDC {
        /**
         * Defines whether OIDC authentication shall be used. This setting should be set to true for production
         * use. However, it may be useful for debugging purposes
         */
        private final boolean enableOIDCAuthentication;

        /**
         * URL of the OIDC authentication provider. In the case of keycloak:
         * "https://{HOSTNAME}/realms/master"
         */
        private final String authenticationProviderURL;

        /**
         * Name of the authentication provider.
         */
        private final String authenticationProvider;

        /**
         * ID of the client used to validate JWTs
         */
        private final String clientID;

        /**
         * Admin Role
         */
        private final String adminRole;

        OIDC() {
            this.enableOIDCAuthentication = true;

            this.authenticationProviderURL = "";

            this.authenticationProvider = "KEYCLOAK";

            this.clientID = "mealtiger";

            this.adminRole = "ADMIN";
        }
    }

}

package com.mealtiger.backend.configuration.configs;

import com.mealtiger.backend.configuration.annotations.Config;
import com.mealtiger.backend.configuration.annotations.ConfigNode;

/**
 * Authentication Config File
 * Properties are represented as non-static fields. Categories can be done by nesting the properties in static nested classes.
 *
 * @see com.mealtiger.backend.configuration.Configurator
 */

@Config(name = "Authentication", configPath = "auth.yml")
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

    @ConfigNode(name = "OIDC.clientID", envKey = "OIDC_CLIENT_ID")
    public String getOIDCClientID() {
        return oidc.clientID;
    }

    static class OIDC {
        private final boolean enableOIDCAuthentication;
        private final String authenticationProviderURL;
        private final String clientID;

        OIDC() {
            //TODO: Set the default to true once implemented in frontend!
            this.enableOIDCAuthentication = false;

            this.authenticationProviderURL = "";

            this.clientID = "mealtiger";
        }
    }

}

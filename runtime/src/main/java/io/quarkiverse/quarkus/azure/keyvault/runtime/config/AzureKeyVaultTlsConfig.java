package io.quarkiverse.quarkus.azure.keyvault.runtime.config;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigGroup
public class AzureKeyVaultTlsConfig {

    /**
     * Allows to bypass certificate validation on TLS communications.
     * <p>
     * If true this will allow TLS communications with Vault, without checking the validity of the
     * certificate presented by Azure Key Vault. This is discouraged in production because it allows man in the middle
     * type of attacks.
     */
    @ConfigItem
    public Optional<Boolean> skipVerify = Optional.empty();

    /**
     * Certificate bundle used to validate TLS communications with Vault.
     * <p>
     * The path to a pem bundle file, if TLS is required, and trusted certificates are not set through
     * javax.net.ssl.trustStore system property.
     */
    @ConfigItem
    public Optional<String> caCert;

}

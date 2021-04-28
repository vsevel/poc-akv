package io.quarkiverse.quarkus.azure.keyvault.runtime.config;

import java.time.Duration;
import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigDocSection;
import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = AzureKeyVaultBootstrapConfig.NAME, phase = ConfigPhase.BOOTSTRAP)
public class AzureKeyVaultBootstrapConfig {

    public static final String NAME = "azure-key-vault";
    public static final String DEFAULT_CONFIG_ORDINAL = "270";
    public static final String DEFAULT_TLS_USE_KUBERNETES_CACERT = "true";
    public static final String DEFAULT_CONNECT_TIMEOUT = "5S";
    public static final String DEFAULT_READ_TIMEOUT = "5S";
    public static final String DEFAULT_SECRET_CONFIG_CACHE_PERIOD = "10M";

    /**
     * Microprofile Config ordinal.
     * <p>
     * This is provided as an alternative to the `config_ordinal` property defined by the specification, to
     * make it easier and more natural for applications to override the default ordinal.
     * <p>
     * The default value is higher than the file system or jar ordinals, but lower than env vars.
     */
    @ConfigItem(defaultValue = DEFAULT_CONFIG_ORDINAL)
    public int configOrdinal;

    /**
     * If set to true, the application will attempt to look up the configuration from Azure Key Vault
     */
    @ConfigItem(defaultValue = "false")
    public boolean enabled;

    /**
     * Azure Key Vault server url. Required if enabled=true.
     * <p>
     * Example: https://myvault.vault.azure.net
     *
     * @asciidoclet
     */
    @ConfigItem
    public Optional<String> url;

    /**
     * Azure Key Vault server property prefix.
     * <p>
     * If prefix=mysecrets, and vault contains password=s3cr3t, then MP config
     * property mysecrets.password with be available with value s3cr3t
     *
     * @asciidoclet
     */
    @ConfigItem
    public Optional<String> prefix;

    /**
     * Azure Key Vault Client Secret. Used for Authentication. Required if enabled=true.
     */
    @ConfigItem
    public Optional<String> clientSecret;

    /**
     * Azure Key Vault Tenant ID. Used for Authentication. Required if enabled=true.
     */
    @ConfigItem
    public Optional<String> tenantId;

    /**
     * Azure Key Vault Client ID. Used for Authentication. Required if enabled=true
     */
    @ConfigItem
    public Optional<String> clientId;

    /**
     * Azure Key Vault config source cache period.
     * <p>
     * Properties fetched from vault as MP config will be kept in a cache, and will not be fetched from vault
     * again until the expiration of that period.
     *
     * @asciidoclet
     */
    @ConfigItem(defaultValue = DEFAULT_SECRET_CONFIG_CACHE_PERIOD)
    public Duration secretConfigCachePeriod;

    /**
     * TLS
     */
    @ConfigItem
    @ConfigDocSection
    public AzureKeyVaultTlsConfig tls;

    /**
     * Timeout to establish a connection with Vault.
     */
    @ConfigItem(defaultValue = DEFAULT_CONNECT_TIMEOUT)
    public Duration connectTimeout;

    /**
     * Request timeout on Vault.
     */
    @ConfigItem(defaultValue = DEFAULT_READ_TIMEOUT)
    public Duration readTimeout;

}

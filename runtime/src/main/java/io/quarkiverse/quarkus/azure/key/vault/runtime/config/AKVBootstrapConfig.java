package io.quarkiverse.quarkus.azure.key.vault.runtime.config;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

@ConfigRoot(name = AKVBootstrapConfig.NAME, phase = ConfigPhase.BOOTSTRAP)
public class AKVBootstrapConfig {

    public static final String NAME = "akv";
    public static final String DEFAULT_CONFIG_ORDINAL = "270";

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
     * Azure Key Vault server url.
     * <p>
     * Example: https://myvault.vault.azure.net
     *
     * @asciidoclet
     */
    @ConfigItem
    public Optional<String> url;

    /**
     * TODO
     */
    @ConfigItem
    public Optional<String> clientSecret;

    /**
     * TODO
     */
    @ConfigItem
    public Optional<String> tenantId;

    /**
     * TODO
     */
    @ConfigItem
    public Optional<String> clientId;

}

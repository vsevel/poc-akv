package io.quarkiverse.quarkus.azure.keyvault.runtime.config;

import javax.inject.Singleton;

@Singleton
public class AzureKeyVaultConfigHolder {

    private AzureKeyVaultBootstrapConfig config;

    public AzureKeyVaultBootstrapConfig getConfig() {
        return config;
    }

    public AzureKeyVaultConfigHolder setAKVBootstrapConfig(AzureKeyVaultBootstrapConfig config) {
        this.config = config;
        return this;
    }
}

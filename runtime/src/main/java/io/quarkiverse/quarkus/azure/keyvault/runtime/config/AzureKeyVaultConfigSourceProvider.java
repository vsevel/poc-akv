package io.quarkiverse.quarkus.azure.keyvault.runtime.config;

import java.util.Arrays;

import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.ConfigSourceProvider;
import org.jboss.logging.Logger;

public class AzureKeyVaultConfigSourceProvider implements ConfigSourceProvider {

    private static final Logger log = Logger.getLogger(AzureKeyVaultConfigSourceProvider.class);

    private AzureKeyVaultBootstrapConfig config;

    public AzureKeyVaultConfigSourceProvider(AzureKeyVaultBootstrapConfig config) {
        this.config = config;
    }

    @Override
    public Iterable<ConfigSource> getConfigSources(ClassLoader forClassLoader) {
        return Arrays.asList(new AzureKeyVaultConfigSource(config));
    }
}

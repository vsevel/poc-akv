package io.quarkiverse.quarkus.azure.key.vault.runtime.config;

import java.util.Arrays;

import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.ConfigSourceProvider;
import org.jboss.logging.Logger;

public class AKVConfigSourceProvider implements ConfigSourceProvider {

    private static final Logger log = Logger.getLogger(AKVConfigSourceProvider.class);

    private AKVBootstrapConfig akvBootstrapConfig;

    public AKVConfigSourceProvider(AKVBootstrapConfig akvBootstrapConfig) {
        this.akvBootstrapConfig = akvBootstrapConfig;
    }

    @Override
    public Iterable<ConfigSource> getConfigSources(ClassLoader forClassLoader) {
        return Arrays.asList(new AKVConfigSource(akvBootstrapConfig));
    }
}

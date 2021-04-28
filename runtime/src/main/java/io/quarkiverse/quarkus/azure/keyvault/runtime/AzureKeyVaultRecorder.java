package io.quarkiverse.quarkus.azure.keyvault.runtime;

import static java.util.Collections.emptyList;

import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.ConfigSourceProvider;
import org.jboss.logging.Logger;

import io.quarkiverse.quarkus.azure.keyvault.runtime.client.VertxAKVClient;
import io.quarkiverse.quarkus.azure.keyvault.runtime.config.AzureKeyVaultBootstrapConfig;
import io.quarkiverse.quarkus.azure.keyvault.runtime.config.AzureKeyVaultConfigHolder;
import io.quarkiverse.quarkus.azure.keyvault.runtime.config.AzureKeyVaultConfigSourceProvider;
import io.quarkus.arc.Arc;
import io.quarkus.arc.ArcContainer;
import io.quarkus.runtime.RuntimeValue;
import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class AzureKeyVaultRecorder {

    private static final Logger log = Logger.getLogger(AzureKeyVaultRecorder.class);

    private static final EmptyConfigSourceProvider EMPTY = new EmptyConfigSourceProvider();

    private static class EmptyConfigSourceProvider implements ConfigSourceProvider {
        @Override
        public Iterable<ConfigSource> getConfigSources(ClassLoader classLoader) {
            return emptyList();
        }
    }

    public RuntimeValue<ConfigSourceProvider> configure(AzureKeyVaultBootstrapConfig config) {
        ConfigSourceProvider configSourceProvider = EMPTY;
        if (config.enabled) {
            if (!config.url.isPresent()) {
                throw new RuntimeException("missing azure key vault url");
            }
            ArcContainer container = Arc.container();
            container.instance(AzureKeyVaultConfigHolder.class).get().setAKVBootstrapConfig(config);
            container.instance(VertxAKVClient.class).get().init();
            configSourceProvider = new AzureKeyVaultConfigSourceProvider(config);
        }
        return new RuntimeValue<>(configSourceProvider);
    }
}
